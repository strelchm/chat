package ru.simbir.internship.chat.service.bot;

import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;

import java.util.List;

public interface RoomBot {

    List<MessageDto> help();

    List<MessageDto> create(String command, UserDto userDto);

    List<MessageDto> remove(String command, UserDto userDto);

    List<MessageDto> rename(String command, UserDto userDto);

    List<MessageDto> connect(String command, UserDto userDto);

    List<MessageDto> disconnect(String command, UserDto userDto);
}
