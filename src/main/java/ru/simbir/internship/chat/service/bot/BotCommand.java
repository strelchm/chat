package ru.simbir.internship.chat.service.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotCommand {
    ROOM_CREATE("^//room create(?: -c)? .+",
                    "//room create {Название комнаты} - создает комнаты;\n" +
                    "-c закрытая комната. Только (владелец, модератор и админ) может добавлять/удалять пользователей из комнаты.\n"),
    ROOM_REMOVE("^//room remove .+",
                    "//room remove {Название комнаты} - удаляет комнату (владелец и админ).\n"),
    ROOM_RENAME("^//room rename .+",
                    "//room rename {Название комнаты} - переименование комнаты (владелец и админ).\n"),
    ROOM_CONNECT("^//room connect .+",
                    "//room connect {Название комнаты} - войти в комнату;\n" +
                    "-l {login пользователя} - добавить пользователя в комнату.\n"),
    ROOM_DISCONNECT("^//room disconnect(?: -l)?(?: -m)?(?: .+)?",
                    "//room disconnect - выйти из текущей комнаты;\n" +
                    "//room disconnect {Название комнаты} - выйти из заданной комнаты;\n" +
                    "-l {login пользователя} - выгоняет пользователя из комнаты (для владельца, модератора и админа);\n" +
                    "-m {Количество минут} - время на которое пользователь не сможет войти (для владельца, модератора и админа).\n"),
    USER_RENAME("^//user rename .+",
                    "//user rename {login пользователя} (владелец и админ).\n"),
    USER_BAN("^//user ban -l .+(?: -m \\d+)?",
                    "//user ban \n" +
                    "-l {login пользователя} - выгоняет пользователя из всех комнат;\n" +
                    "-m {Количество минут} - время на которое пользователь не сможет войти.\n"),
    USER_MODERATOR("^//user moderator \\S+.+-[nd]",
                    "//user moderator {login пользователя} - действия над модераторами.\n" +
                    "-n - назначить пользователя модератором.\n" +
                    "-d - “разжаловать” пользователя."),
    HELP("^//help$",
            "//help - выводит список доступных команд.");

    private final String regex;
    private final String title;
}
