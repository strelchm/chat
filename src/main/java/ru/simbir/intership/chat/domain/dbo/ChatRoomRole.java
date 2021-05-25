package ru.simbir.intership.chat.domain.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Модель роли участника чата
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRole extends ParentEntity {
    @Enumerated(EnumType.STRING)
    private Role name;

    @OneToMany(mappedBy = "role")
    private Set<ChatRoomMembership> users;

    public enum Role {
        OWNER,
        BLOCKED_USER,
        MODERATOR,
        USER
    }
}
