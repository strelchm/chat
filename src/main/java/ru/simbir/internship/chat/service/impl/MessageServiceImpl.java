package ru.simbir.internship.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.Message;
import ru.simbir.internship.chat.domain.UserAppRole;
import ru.simbir.internship.chat.domain.UserRoom;
import ru.simbir.internship.chat.domain.UserRoomRole;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.exception.NotFoundException;
import ru.simbir.internship.chat.repository.MessageRepository;
import ru.simbir.internship.chat.repository.UserRoomRepository;
import ru.simbir.internship.chat.service.CheckRoomAccessService;
import ru.simbir.internship.chat.service.MessageService;
import ru.simbir.internship.chat.service.RoomService;
import ru.simbir.internship.chat.service.UserService;
import ru.simbir.internship.chat.util.MappingUtil;

import java.util.UUID;

@Service
public class MessageServiceImpl extends CheckRoomAccessService implements MessageService {
    private final MessageRepository messageRepository;
    private final RoomService roomService;
    private final UserService userService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserRoomRepository userRoomRepository,
                              RoomService roomService, UserService userService) {
        super(userRoomRepository);
        this.messageRepository = messageRepository;
        this.roomService = roomService;
        this.userService = userService;
    }

    @Override
    public Page<MessageDto> getAll(Pageable pageable, UUID chatRoomId, UserDto userDto) { // todo Pageable / Criteria??
        checkRoomAccess(userDto, chatRoomId);
        return messageRepository.findAll(pageable).map(MappingUtil::mapToMessageDto);
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
        Message message = getMessageById(messageId);
        checkRoomAccess(userDto, chatRoomId, UserRoomRole.OWNER, UserRoomRole.BLOCKED_USER, UserRoomRole.USER);
        messageRepository.delete(message);
    }

    private Message getMessageById(UUID messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new NotFoundException("Message with id " + messageId + " not found"));
    }
}
