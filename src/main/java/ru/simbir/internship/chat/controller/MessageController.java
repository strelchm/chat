package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.internship.chat.dto.*;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.service.MessageService;
import ru.simbir.internship.chat.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Validated
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
public class MessageController extends ParentController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        super(userService);
        this.messageService = messageService;
    }


    @GetMapping("/rooms/{roomId}/messages")
    public Page<MessageDto> getAllMessages(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                                           @ModelAttribute(USER_CONTEXT) UserContext userContext,
                                           @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return messageService.getAll(pageable, roomId, userContext.getUser().get());
    }

    @GetMapping("/rooms/{roomId}/messages/{id}")
    public MessageDto getMessageById(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                     @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                                     @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        return messageService.getById(id, roomId, userContext.getUser().get());
    }

    @PostMapping("/rooms/{roomId}/messages")
    @ResponseStatus(value = HttpStatus.CREATED)
    public IdDto createMessage(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody MessageDto dto,
                               @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                               @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        return new IdDto(messageService.add(dto, roomId, userContext.getUser().get()));
    }

    @PutMapping("/rooms/{roomId}/messages/{id}")
    public MessageDto updateMessage(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody MessageDto dto,
                                    @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                    @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                                    @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        if (id != dto.getId()) throw new BadRequestException();
        return messageService.edit(dto, roomId, userContext.getUser().get());
    }

    @Deprecated // todo not needed here
    @PatchMapping("/rooms/{roomId}/messages/{id}")
    public MessageDto patchMessage(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                   @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody MessageDto dto,
                                   @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                                   @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        if (id != dto.getId()) throw new BadRequestException();
        MessageDto messageDto = messageService.getById(id, roomId, userContext.getUser().get());
        if (!messageDto.getText().equals(dto.getText())) {
            messageDto.setText(dto.getText());
        }
        return messageService.edit(messageDto, roomId, userContext.getUser().get());
    }

    @DeleteMapping("/rooms/{roomId}/messages/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMessage(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                              @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                              @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        messageService.delete(id, roomId, userContext.getUser().get());
    }
}
