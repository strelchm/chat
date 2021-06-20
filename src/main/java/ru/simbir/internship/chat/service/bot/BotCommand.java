package ru.simbir.internship.chat.service.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotCommand {
    YBOT_CHANNEL_INFO("//yBot channelInfo {имя канала}\n- Первым сообщением от бота выводится имя канала.\n" +
            " - Вторым - ссылки на последние 5 роликов.", "^//yBot channelInfo .*"),
    YBOT_HELP ("//yBot help\n- Cписок доступных команд для взаимодействия.", "^//yBot help$");

    private final String title;
    private final String regex;
}
