package ru.simbir.internship.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.simbir.internship.chat.domain.*;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.exception.NotFoundException;
import ru.simbir.internship.chat.repository.MessageRepository;
import ru.simbir.internship.chat.repository.UserRoomRepository;
import ru.simbir.internship.chat.service.CheckRoomAccessService;
import ru.simbir.internship.chat.service.MessageService;
import ru.simbir.internship.chat.service.RoomService;
import ru.simbir.internship.chat.service.UserService;
import ru.simbir.internship.chat.service.bot.YouTubeBot;
import ru.simbir.internship.chat.util.MappingUtil;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends CheckRoomAccessService implements MessageService {
    private final MessageRepository messageRepository;
    private final RoomService roomService;
    private final UserService userService;
    private final YouTubeBot youTubeBot;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserRoomRepository userRoomRepository,
                              RoomService roomService, UserService userService, YouTubeBot youTubeBot) {
        super(userRoomRepository);
        this.messageRepository = messageRepository;
        this.roomService = roomService;
        this.userService = userService;
        this.youTubeBot = youTubeBot;
    }

    @Override
    public Page<MessageDto> getAll(Pageable pageable, UUID chatRoomId, UserDto userDto) {
        checkRoomAccess(userDto, chatRoomId);
        return messageRepository.findAllByRoom_IdOrderByCreatedDesc(pageable, chatRoomId).map(MappingUtil::mapToMessageDto);
    }

    @Override
    public MessageDto getById(UUID messageId, UUID chatRoomId, UserDto userDto) {
        checkRoomAccess(userDto, chatRoomId);
        return MappingUtil.mapToMessageDto(getMessageById(messageId));
    }

    @Override
    public UUID add(MessageDto dto, UUID chatRoomId, UserDto userDto) {
        checkRoomAccess(userDto, chatRoomId, UserRoomRole.BLOCKED_USER);
        Message message = MappingUtil.mapToMessageEntity(dto);
        message.setRoom(roomService.getRoomById(chatRoomId));
        message.setUser(userService.getUserById(userDto.getId()));
        return messageRepository.save(message).getId();
    }

    @Override
    public MessageDto edit(MessageDto dto, UUID chatRoomId, UserDto userDto) {
        UserRoom userRoom = checkRoomAccess(userDto, chatRoomId, UserRoomRole.BLOCKED_USER);

        if (dto.getText() == null) {
            throw new BadRequestException();
        }

        Message message = getMessageById(dto.getId());

        if (userDto.getUserAppRole() != UserAppRole.ADMIN && userRoom.getUserRoomRole() != UserRoomRole.MODERATOR &&
                message.getUser().getId() != userDto.getId()) {
            throw new AccessDeniedException("The author and editor are different users");
        }

        message.setText(dto.getText());
        return MappingUtil.mapToMessageDto(messageRepository.save(message));
    }

    @Override
    public void delete(UUID messageId, UUID chatRoomId, UserDto userDto) {
        UserRoom userRoom = checkRoomAccess(userDto, chatRoomId, UserRoomRole.BLOCKED_USER);
        Message message = getMessageById(messageId);

        if (userDto.getUserAppRole() != UserAppRole.ADMIN && userRoom.getUserRoomRole() != UserRoomRole.MODERATOR &&
                message.getUser().getId() != userDto.getId()) {
            throw new AccessDeniedException("Only admin or moderator can delete other user message");
        }

        messageRepository.delete(message);
    }

    private Message getMessageById(UUID messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new NotFoundException("Message with id " + messageId + " not found"));
    }

    @Override
    public MessageDto save(MessageDto dto, UUID roomID, UserDto userDto) {
        checkRoomAccess(userDto, roomID, UserRoomRole.BLOCKED_USER);
        Message message = MappingUtil.mapToMessageEntity(dto);
        message.setUser(userService.getUserById(dto.getUserId()));
        message.setRoom(roomService.getRoomById(dto.getRoomId()));
        return MappingUtil.mapToMessageDto(messageRepository.save(message));
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public Set<MessageDto> findAll(UUID roomId, UUID userId) {
        checkRoomAccess(userService.getById(userId), roomId);
        return roomService.getRoomById(roomId).getMessages().stream().map(MappingUtil::mapToMessageDto).collect(Collectors.toSet());
    }

    @Override
    public MessageDto process(MessageDto messageDto, UUID roomID, UserDto userDto) {
        if (messageDto == null || roomID == null || userDto == null) {
            throw new BadRequestException();
        }
        if (messageDto.getRoomId() == null) {
            messageDto.setRoomId(roomID);
        }
        if (!messageDto.getRoomId().equals(roomID)) {
            throw new BadRequestException();
        }
        if (messageDto.getId() == null) {
            if (messageDto.getUserId() == null) {
                messageDto.setUserId(userDto.getId());
            }
            if (!messageDto.getUserId().equals(userDto.getId())) {
                throw new BadRequestException();
            }
            return save(messageDto, roomID, userDto);
        } else {
            return edit(messageDto, roomID, userDto);
        }
    }

    @Override
    public List<MessageDto> processBot(MessageDto messageDto, UserDto userDto) {
        if (messageDto == null || userDto == null) {
            throw new BadRequestException();
        }
        if (!roomService.getRoomById(messageDto.getRoomId()).getType().equals(RoomType.BOT)) {
            throw new BadRequestException();
        }
        String command = messageDto.getText();
        if (command.matches(BotCommand.YBOT_CHANNEL_INFO.getTitle())) {
            return youTubeBot.channelInfo(command);
        }
        if (command.matches(BotCommand.YBOT_HELP.getTitle())) {
            return youTubeBot.help();
        }
        return null;
    }
}
