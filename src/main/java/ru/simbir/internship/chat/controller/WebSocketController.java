package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;
import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.MessageService;

import java.util.UUID;

@Controller
public class WebSocketController {

    private final JwtTokenService jwtTokenService;
    private final MessageService messageService;

    @Autowired
    public WebSocketController(MessageService messageService,
                               JwtTokenService jwtTokenService) {
        this.messageService = messageService;
        this.jwtTokenService = jwtTokenService;
    }

    @MessageMapping("/room/{roomId}")
    @SendTo("/chat/room/{roomId}")
    public MessageDto processMessage(@Header(name = "token") String token,
                                     @DestinationVariable UUID roomId,
                                     MessageDto dto) {
        UserDto userDto = jwtTokenService.parseToken(token);
        if (userDto.getUserAppRole() == null) {
            throw new AccessDeniedException();
        }
        return messageService.process(dto, roomId, userDto);
    }
}

