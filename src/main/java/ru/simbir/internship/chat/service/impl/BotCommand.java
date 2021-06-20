package ru.simbir.internship.chat.service.impl;

public enum BotCommand {
    YBOT_CHANNEL_INFO("","^//yBot channelInfo \\s*"),
    YBOT_HELP ("//yBot help - список доступных команд для взаимодействия","^//yBot help$");

    private String title;
    private String regex;

    BotCommand(String title, String regex) {
        this.title = title;
        this.regex = regex;
    }

    public String getTitle() {
        return title;
    }
}
