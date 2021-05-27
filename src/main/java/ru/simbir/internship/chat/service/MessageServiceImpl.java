package ru.simbir.internship.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.repository.MessageRepository;
import ru.simbir.internship.chat.util.MappingUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService{

    private MessageRepository repository;

    @Autowired
    public void setRepository(MessageRepository repository){
        this.repository = repository;
    }

    @Override
    public List<MessageDto> getAll() {
        return repository.findAll().stream().map(MappingUtil::mapToMessageDto).collect(Collectors.toList());
    }

    @Override
    public MessageDto getById(UUID id){
        return MappingUtil.mapToMessageDto(repository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public UUID add(MessageDto dto){
        return repository.save(MappingUtil.mapToMessageEntity(dto)).getId();
    }

    @Override
    public MessageDto edit(MessageDto dto){
        repository.findById(dto.getId()).orElseThrow(RuntimeException::new);
        repository.save(MappingUtil.mapToMessageEntity(dto));
        return dto;
    }

    @Override
    public MessageDto delete(MessageDto dto){
        repository.findById(dto.getId()).orElseThrow(RuntimeException::new);
        repository.delete(MappingUtil.mapToMessageEntity(dto));
        return dto;
    }

}