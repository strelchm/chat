package ru.simbir.internship.chat.service.bot;

import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.dto.MessageDto;

import java.util.List;

@Service
public class UserBotImpl implements UserBot{
    @Override
    public List<MessageDto> rename(String command) {
        return null;
    }

    @Override
    public List<MessageDto> ban(String command) {
        return null;
    }

    @Override
    public List<MessageDto> moderator(String command) {
        return null;
    }
}
