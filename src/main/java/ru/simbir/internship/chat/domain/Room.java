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
 * Модель чат-комнаты
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"userRooms", "messages"})
@NoArgsConstructor
@AllArgsConstructor
public class Room extends ParentEntity {
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @OneToMany(mappedBy = "room")
    private Set<Message> messages;

    @OneToMany(mappedBy = "room")
    private Set<UserRoom> userRooms;

    @Enumerated(EnumType.STRING)
    private RoomType type;
}
