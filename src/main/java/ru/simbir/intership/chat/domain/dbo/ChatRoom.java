package ru.simbir.intership.chat.domain.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * Модель обьекта чат-комнаты
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends ParentEntity {
    @OneToMany(mappedBy = "chatRoom")
    private Set<ChatMessage> messages;

    @OneToMany(mappedBy = "chatRoom")
    private Set<ChatRoomUser> chatRoomUsers;

    @Enumerated(EnumType.STRING)
    private ChartRoomType type;
}
