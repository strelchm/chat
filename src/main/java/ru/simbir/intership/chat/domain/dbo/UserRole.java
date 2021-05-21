package ru.simbir.intership.chat.domain.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends ParentEntity {
    @Enumerated(EnumType.STRING)
    private Role name;

    @OneToOne(mappedBy = "role")
    private ChatUser user;

    public static enum Role {
        USER,
        BLOCKED_USER,
        MODERATOR,
        ADMIN
    }
}
