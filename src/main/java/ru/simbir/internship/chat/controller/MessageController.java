package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.internship.chat.dto.IdDto;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserContext;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.service.MessageService;
import ru.simbir.internship.chat.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@Validated
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
public class MessageController extends ParentController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        super(userService);
        this.messageService = messageService;
    }


    @GetMapping("/{roomId}/messages")
    public Page<MessageDto> getAllMessages(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                                           @ModelAttribute(USER_CONTEXT) UserContext userContext,
                                           @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        return messageService.getAll(pageable, roomId, userContext.getUser().get());
    }

    @GetMapping("/{roomId}/messages/{id}")
    public MessageDto getMessageById(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                     @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                                     @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        return messageService.getById(id, roomId, userContext.getUser().get());
    }

    @PostMapping("/{roomId}/messages")
    @ResponseStatus(value = HttpStatus.CREATED)
    public IdDto createMessage(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody MessageDto dto,
                               @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                               @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        return new IdDto(messageService.add(dto, roomId, userContext.getUser().get()));
    }

    @PutMapping("/{roomId}/messages/{id}")
    public MessageDto updateMessage(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody MessageDto dto,
                                    @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                    @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                                    @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        if (dto.getId() == null) {
            dto.setId(id);
        } else if (!id.equals(dto.getId())) {
            throw new BadRequestException();
        }
        return messageService.edit(dto, roomId, userContext.getUser().get());
    }

    @DeleteMapping("/{roomId}/messages/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMessage(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                              @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID roomId,
                              @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        messageService.delete(id, roomId, userContext.getUser().get());
    }
}
