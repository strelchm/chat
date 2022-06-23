package ru.simbir.internship.chat.service.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoomAndUserBotCommand {
    ROOM_CREATE("^//room create(?: -c)? \\S+$",
            "//room create {Название комнаты} - создает комнату;\n" +
                    "//room create -c {Название комнаты} - создает приватную комнату.\n"),
    ROOM_REMOVE("^//room remove \\S+$",
            "//room remove {Название комнаты} - удаляет комнату (владелец и админ).\n"),
    ROOM_RENAME("^//room rename \\S+ \\S+$",
            "//room rename {Старое название комнаты} {Новое название комнаты} - переименование комнаты (владелец и админ).\n"),
    ROOM_CONNECT("^//room connect(?: -l \\S+)? \\S+$",
            "//room connect {Название комнаты} - войти в комнату;\n" +
                    "//room connect -l {login пользователя} {Название комнаты} - добавить пользователя в комнату.\n"),
    ROOM_DISCONNECT("^//room disconnect(?: -l \\S+)?(?: -m \\d+)?(?: \\S+)?$",
            "//room disconnect {Название комнаты} - выйти из заданной комнаты;\n" +
                    "//room disconnect -l {login пользователя} {Название комнаты} - выгоняет пользователя из комнаты (для владельца, модератора и админа);\n" +
                    "//room disconnect -l {login пользователя} -m {Количество минут} {Название комнаты} - время на которое пользователь не сможет войти (для владельца, модератора и админа).\n"),
    USER_RENAME("^//user rename \\S+(?: \\S+)?$",
            "//user rename {новый логин пользователя} - переименовать пользователя (владелец).\n" +
                    "//user rename {login пользователя} {новый логин пользователя} - переименовать другого пользователя (админ).\n"),
    USER_BAN("^//user ban(?: -l \\S+)?(?: -m \\d+)?$",
            "//user ban -l {login пользователя} -m {Количество минут} - выгоняет пользователя из всех комнат на m минут.\n" +
                    "//user ban -l {login пользователя} - выгоняет пользователя из всех комнат навсегда."),
    USER_MODERATOR("^//user moderator -[nd] \\S+ \\S+$",
            "//user moderator -n {название комнаты} {login} - назначить пользователя модератором комнаты.\n" +
                    "//user moderator -d {название комнаты} {login} - разжаловать модератора.\n"),
    HELP("^//help$",
            "//help - выводит список доступных команд.");

    private final String regex;
    private final String title;
}