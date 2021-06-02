package ru.simbir.internship.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.Message;
import ru.simbir.internship.chat.domain.UserRoomRole;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.NotFoundException;
import ru.simbir.internship.chat.repository.MessageRepository;
import ru.simbir.internship.chat.repository.UserRoomRepository;
import ru.simbir.internship.chat.util.MappingUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends CheckRoomAccessService implements MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserRoomRepository userRoomRepository) {
        super(userRoomRepository);
        this.messageRepository = messageRepository;
    }

    @Override
    public List<MessageDto> getAll(UUID chatRoomId, UserDto userDto) { // todo Pageable / Criteria??
        checkAccess(userDto, chatRoomId);
        return messageRepository.findAll().stream().map(MappingUtil::mapToMessageDto).collect(Collectors.toList());
    }

    @Override
    public MessageDto getById(UUID messageId, UUID chatRoomId, UserDto userDto) {
        checkAccess(userDto, chatRoomId);
        return MappingUtil.mapToMessageDto(getMessageById(messageId));
    }

    @Override
    public UUID add(MessageDto dto, UUID chatRoomId, UserDto userDto) { // todo id в дто или отдельным параметром?
        checkAccess(userDto, chatRoomId, UserRoomRole.BLOCKED_USER);
        return messageRepository.save(MappingUtil.mapToMessageEntity(dto)).getId();
    }

    @Override
    public MessageDto edit(MessageDto dto, UUID chatRoomId, UserDto userDto) { // todo id в дто или отдельным параметром?
        getMessageById(dto.getId());
        checkAccess(userDto, chatRoomId, UserRoomRole.BLOCKED_USER);
        messageRepository.save(MappingUtil.mapToMessageEntity(dto));
        return dto;
    }

    @Override
    public void delete(UUID messageId, UUID chatRoomId, UserDto userDto) {
        getMessageById(messageId);
        checkAccess(userDto, chatRoomId, UserRoomRole.BLOCKED_USER, UserRoomRole.USER);
        messageRepository.delete(getMessageById(messageId));
    }

    private Message getMessageById(UUID messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new NotFoundException("Message with id " + messageId + " not found"));
    }
}
