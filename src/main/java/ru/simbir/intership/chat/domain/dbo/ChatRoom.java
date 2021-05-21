package ru.simbir.intership.chat.domain.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * Модель обьекта чат-комнаты
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends ParentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(mappedBy = "chatRoom")
    private Set<ChatMessage> messages;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "chat_room_users",
            joinColumns = {@JoinColumn(name = "chat_user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_room_id", referencedColumnName = "id")})
    private Set<ChatUser> users;

    @Enumerated(EnumType.STRING)
    private ChartRoomType type;
}