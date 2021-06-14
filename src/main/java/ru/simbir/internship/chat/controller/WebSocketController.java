package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;
import ru.simbir.internship.chat.service.MessageService;
import ru.simbir.internship.chat.service.UserService;

import java.security.Principal;
import java.util.Comparator;
import java.util.UUID;

@Controller
public class WebSocketController extends ParentController{

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    @Autowired
    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate,
                               MessageService messageService,
                               UserService userService) {
        super(userService);
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @SubscribeMapping("/user/{userId}/room/{roomId}")
    public void subscribe(@DestinationVariable UUID roomId,
                          @DestinationVariable UUID userId,
                          Authentication auth){
        UserDto userDto = getUserDto(auth);
        if (userDto == null || userDto.getId() != userId) {
            throw new AccessDeniedException();
        }
        StringBuilder topic = new StringBuilder("/app/user/");
        topic.append(userId).append("/room/").append(roomId);
        messageService.findAll(roomId, userId)
                .stream()
                .sorted(Comparator.comparing(MessageDto::getCreated))
                .forEach(dto -> simpMessagingTemplate.convertAndSend(topic.toString(), dto));
    }

    @MessageMapping("/room/{roomId}")
    public void processMessage(MessageDto dto,
                               @DestinationVariable UUID roomId,
                               Authentication auth){
        UserDto userDto = getUserDto(auth);
        if (userDto == null) {
            throw new AccessDeniedException();
        }
        StringBuilder topic = new StringBuilder("/chat/room/");
        topic.append(dto.getRoomId());
        simpMessagingTemplate.convertAndSend(topic.toString(), messageService.process(dto, roomId, userDto));
    }

}
