package ru.simbir.intership.chat.domain.dbo;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Модель чат-комнаты
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends ParentEntity {
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @OneToMany(mappedBy = "chatRoom")
    private Set<ChatMessage> messages;

    @OneToMany(mappedBy = "chatRoom")
    private Set<ChatRoomMembership> chatRoomMemberships;

    @Enumerated(EnumType.STRING)
    private ChartRoomType type;
}
