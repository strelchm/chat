package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.internship.chat.dto.IdDto;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.service.MessageService;
import ru.simbir.internship.chat.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@Validated
public class MessageController extends ParentController {
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        super(userService);
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping
    public List<MessageDto> getAllMessages() {
        return messageService.getAll();
    }

    @GetMapping("/{id}")
    public MessageDto getMessageById(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id) {
        return messageService.getById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public IdDto createMessage(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody MessageDto dto) {
        return new IdDto(messageService.add(dto));
    }

    @PutMapping("/{id}")
    public MessageDto updateMessage(@NotNull(message = NULL_UPDATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody MessageDto dto) {
        return messageService.edit(dto);
    }

    @Deprecated // todo not needed here
    @PatchMapping("/{id}")
    public MessageDto patchMessage(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                   @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody MessageDto dto) {
        MessageDto messageDto = messageService.getById(id);

        if (!messageDto.getText().equals(dto.getText())) {
            messageDto.setText(dto.getText());
        }

        return messageService.edit(messageDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public MessageDto deleteMessage(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id) { // todo - void return type
        MessageDto messageDto = messageService.getById(id); // todo
        return messageService.delete(messageDto);
    }
}
