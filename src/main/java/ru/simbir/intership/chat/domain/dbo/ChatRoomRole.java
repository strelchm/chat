package ru.simbir.intership.chat.domain.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRole extends ParentEntity {
    @Enumerated(EnumType.STRING)
    private Role name;

    @OneToMany(mappedBy = "role")
    private Set<ChatRoomUser> users;

    public enum Role {
        OWNER,
        BLOCKED_USER,
        MODERATOR,
        USER
    }
}
