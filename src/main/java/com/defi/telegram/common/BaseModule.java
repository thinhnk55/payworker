package com.defi.telegram.common;

import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

public interface BaseModule {
    boolean accept(CommandParams params);
    void process(TelegramBot bot, Update update, CommandParams params, JsonObject config);
}
