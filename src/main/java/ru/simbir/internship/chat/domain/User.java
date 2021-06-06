package ru.simbir.internship.chat.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Модель пользователя системы
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends ParentEntity {
    @NotNull
    @Size(min = 1, max = 50)
    @Column(unique = true)
    private String login;

    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private UserAppRole userAppRole;

    @OneToMany(mappedBy = "user")
    private Set<UserRoom> userRooms;

    @OneToMany(mappedBy = "user")
    private Set<Message> messages;
}
