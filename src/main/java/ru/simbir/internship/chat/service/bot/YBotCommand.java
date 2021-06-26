package ru.simbir.internship.chat.service.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YBotCommand {
    YBOT_CHANNEL_INFO("^//yBot channelInfo .+",
                    "//yBot channelInfo {имя канала}\n" +
                    " - Первым сообщением от бота выводится имя канала.\n" +
                    " - Вторым - ссылки на последние 5 роликов."),
    YBOT_HELP("^//yBot help$",
                    "//yBot help\n" +
                    " - Cписок доступных команд для взаимодействия."),
    YBOT_VIDEO_COMMENT_RANDOM("^//yBot videoCommentRandom .+\\|\\|.+",
                    "//yBot videoCommentRandom {имя канала}||{Название ролика}\n" +
                    " - Среди комментариев к ролику рандомно выбирается 1.\n" +
                    " - Первым сообщением бот выводит login человека, который оставил этот комментарий.\n" +
                    " - Вторым сообщением бот выводит сам комментарий."),
    YBOT_FIND("^//yBot find -[kvl] .+\\|\\|.+",
                "//yBot find -k {название канала}||{название видео} - в ответ бот присылает ссылку на ролик;\n" +
                    "-v - выводит количество текущих просмотров;\n" +
                    "-l - выводит количество лайков под видео.");

    private final String regex;
    private final String title;
}
