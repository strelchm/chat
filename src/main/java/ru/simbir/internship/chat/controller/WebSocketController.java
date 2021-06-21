package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;
import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.WebSocketService;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

import static ru.simbir.internship.chat.service.bot.YouTubeBotImpl.BOT_ROOM_ID;

@Controller
public class WebSocketController {

    private final JwtTokenService jwtTokenService;
    private final WebSocketService webSocketService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Autowired
    public WebSocketController(WebSocketService webSocketService,
                               JwtTokenService jwtTokenService, SimpMessagingTemplate simpMessagingTemplate) {
        this.webSocketService = webSocketService;
        this.jwtTokenService = jwtTokenService;
        this.simpMessagingTemplate = simpMessagingTemplate;
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
        return webSocketService.process(dto, roomId, userDto);
    }

    @MessageMapping("/room/00000000-0000-0000-0000-000000000000")
    public void processBotMessage(@Header(name = "token") String token,
                                  MessageDto dto,
                                  SimpMessageHeaderAccessor headerAccessor) {
        UserDto userDto = jwtTokenService.parseToken(token);
        if (userDto.getUserAppRole() == null) {
            throw new AccessDeniedException();
        }
        String sessionId = headerAccessor.getSessionId();
        Stream.concat(
                Stream.of(webSocketService.process(dto, BOT_ROOM_ID, userDto)),
                webSocketService.processBot(dto).stream())
                .sorted(Comparator.comparing(MessageDto::getCreated))
                .forEach(m -> simpMessagingTemplate.convertAndSendToUser(
                        sessionId, "/chat/room/00000000-0000-0000-0000-000000000000", m,
                        createMessageHeaders(sessionId)));
    }

    private MessageHeaders createMessageHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);
        headerAccessor.setSessionId(sessionId);
        return headerAccessor.getMessageHeaders();
    }
}

