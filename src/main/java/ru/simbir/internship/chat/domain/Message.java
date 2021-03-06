package ru.simbir.internship.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Модель сообщения чата
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"room", "user"})
@NoArgsConstructor
@AllArgsConstructor
public class Message extends ParentEntity {
    @NotBlank
    @Length(max = 2048, message = "The message is too long")
    private String text;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
