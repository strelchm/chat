package ru.simbir.intership.chat.domain.dbo;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Пользователь
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser extends ParentEntity {
    @NotNull
    @Size(min = 1, max = 50)
    @Column(unique = true)
    private String login;

    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private UserRole role;

    @ManyToMany
    @JoinTable(name = "chat_room_users",
            joinColumns = {@JoinColumn(name = "chat_room_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_user_id", referencedColumnName = "id")})
    private Set<ChatRoom> chatRooms;
}
