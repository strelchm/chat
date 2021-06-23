package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;
import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.WebSocketService;

import java.util.Comparator;
import java.util.UUID;

import static ru.simbir.internship.chat.service.bot.YBotImpl.BOT_ROOM_ID;

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
        simpMessagingTemplate.convertAndSendToUser(
                sessionId, "/chat/room/00000000-0000-0000-0000-000000000000",
                webSocketService.process(dto, BOT_ROOM_ID, userDto),
                createMessageHeaders(sessionId));
        webSocketService.processBot(dto, userDto).stream()
                .sorted(Comparator.comparing(MessageDto::getCreated))
                .forEach(m -> simpMessagingTemplate.convertAndSendToUser(
                        sessionId, "/chat/room/00000000-0000-0000-0000-000000000000", m,
                        createMessageHeaders(sessionId)));
    }

    @MessageExceptionHandler
    @SendToUser("/chat/error")
    public String handleException(RuntimeException e) {
        return e.getMessage();
    }

    private MessageHeaders createMessageHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);
        headerAccessor.setSessionId(sessionId);
        return headerAccessor.getMessageHeaders();
    }
}

