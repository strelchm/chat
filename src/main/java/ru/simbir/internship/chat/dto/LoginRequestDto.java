package ru.simbir.internship.chat.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String login;
    private String password;
}
