package ru.simbir.internship.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Size(min = 3, max = 50)
    @Column(unique = true)
    private String login;

    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserAppRole userAppRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoom> userRooms;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages;
}
