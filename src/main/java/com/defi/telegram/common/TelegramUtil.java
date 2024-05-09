package com.defi.telegram.common;

import com.pengrad.telegrambot.model.Update;

public class TelegramUtil {
    public static long getTelegramId(Update update) {
        if(update.message() != null){
            return update.message().from().id();
        }
        if(update.callbackQuery() != null){
            return update.callbackQuery().from().id();
        }
        return 0;
    }
    public static long getChatId(Update update) {
        if(update.message() != null && update.message().chat() != null){
            return update.message().chat().id();
        }
        if(update.callbackQuery() != null
                && update.callbackQuery().message() != null
                && update.callbackQuery().message().chat() != null){
            return update.callbackQuery().message().chat().id();
        }
        return 0;
    }
    public static String getTelegramUserName(Update update) {
        if(update.message() != null){
            return update.message().from().username();
        }
        if(update.callbackQuery() != null){
            return update.callbackQuery().from().username();
        }
        return null;
    }

    public static String getLanguage(Update update) {
        if(update.message() != null){
            return update.message().from().languageCode();
        }
        if(update.callbackQuery() != null){
            return update.callbackQuery().from().languageCode();
        }
        return null;
    }
}
