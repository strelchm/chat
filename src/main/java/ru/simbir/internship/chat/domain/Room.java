package ru.simbir.internship.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Модель чат-комнаты
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"userRooms", "messages"})
@NoArgsConstructor
@AllArgsConstructor
public class Room extends ParentEntity {
    @NotBlank
    @Size(min = 1, max = 50)
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoom> userRooms;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoomType type;
}
