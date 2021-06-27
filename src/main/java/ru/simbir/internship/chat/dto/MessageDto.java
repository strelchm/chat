package ru.simbir.internship.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UUID roomId;
    private UUID userId;
    private String userLogin;
    @NotNull(message = "text can't be null")
    @NotEmpty(message = "text can't be empty")
    private String text;
    private Date created;
    private Date updated;

    public MessageDto(String text, Date created){
        this.text = text;
        this.created = created;
    }
}
