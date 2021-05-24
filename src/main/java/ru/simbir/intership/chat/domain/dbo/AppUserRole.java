package ru.simbir.intership.chat.domain.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRole extends ParentEntity {
    @Enumerated(EnumType.STRING)
    private Role name;

    @OneToMany(mappedBy = "role")
    private Set<AppUser> user;

    public enum Role {
        CLIENT,
        ADMIN
    }
}
