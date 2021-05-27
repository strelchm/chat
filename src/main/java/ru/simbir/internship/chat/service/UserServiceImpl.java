package ru.simbir.internship.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.repository.UserRepository;
import ru.simbir.internship.chat.util.MappingUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository){
        this.repository = repository;
    }

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream().map(MappingUtil::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(UUID id){
        return MappingUtil.mapToUserDto(repository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public UUID add(UserDto dto){
        return repository.save(MappingUtil.mapToUserEntity(dto)).getId();
    }

    @Override
    public UserDto edit(UserDto dto){
        repository.findById(dto.getId()).orElseThrow(RuntimeException::new);
        repository.save(MappingUtil.mapToUserEntity(dto));
        return dto;
    }

    @Override
    public UserDto delete(UserDto dto){
        repository.findById(dto.getId()).orElseThrow(RuntimeException::new);
        repository.delete(MappingUtil.mapToUserEntity(dto));
        return dto;
    }
}
