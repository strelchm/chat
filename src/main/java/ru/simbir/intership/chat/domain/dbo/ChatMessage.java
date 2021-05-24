package ru.simbir.intership.chat.domain.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Модель обьекта сообщения чата
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends ParentEntity {
    @NotNull
    @Size(min = 1, max = 2048)
    private String text;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}
