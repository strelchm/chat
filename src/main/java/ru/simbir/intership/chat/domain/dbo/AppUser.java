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
public class AppUser extends ParentEntity {
    @NotNull
    @Size(min = 1, max = 50)
    @Column(unique = true)
    private String login;

    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private AppUserRole role;

    @OneToMany(mappedBy = "appUser")
    private Set<ChatRoomUser> chatRoomUsers;
}
