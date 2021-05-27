package ru.simbir.internship.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.repository.RoomRepository;
import ru.simbir.internship.chat.util.MappingUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomRepository repository;

    @Autowired
    public void setRepository(RoomRepository repository){
        this.repository = repository;
    }

    @Override
    public List<RoomDto> getAll() {
        return repository.findAll().stream().map(MappingUtil::mapToRoomDto).collect(Collectors.toList());
    }

    @Override
    public RoomDto getById(UUID id){
        return MappingUtil.mapToRoomDto(repository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public UUID add(RoomDto dto){
        return repository.save(MappingUtil.mapToRoomEntity(dto)).getId();
    }

    @Override
    public RoomDto edit(RoomDto dto){
        repository.findById(dto.getId()).orElseThrow(RuntimeException::new);
        repository.save(MappingUtil.mapToRoomEntity(dto));
        return dto;
    }

    @Override
    public RoomDto delete(RoomDto dto){
        repository.findById(dto.getId()).orElseThrow(RuntimeException::new);
        repository.delete(MappingUtil.mapToRoomEntity(dto));
        return dto;
    }
}
