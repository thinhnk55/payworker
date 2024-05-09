package com.defi.telegram.common;

import com.defi.util.log.DebugLogger;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class CommonMessage {
    public static SendResponse sendMessage(TelegramBot bot, SendMessage message) {
        SendResponse response = bot.execute(message);
        if(!response.isOk()){
            DebugLogger.logger.info("Send Error {}", response.description());
        }
        return response;
    }
    public static SendResponse sendMessage(TelegramBot bot, long chat_id, String content) {
        SendMessage message = new SendMessage(chat_id, content);
        return sendMessage(bot, message);
    }
    public static SendResponse sendMessage(TelegramBot bot, long chat_id, String content, ParseMode mode) {
        SendMessage message = new SendMessage(chat_id, content)
                .parseMode(mode);
        return sendMessage(bot, message);
    }

    public static void sendMessage(TelegramBot bot, Update update, String data) {
        long chat_id = TelegramUtil.getChatId(update);
        sendMessage(bot, chat_id, data);
    }

    public static void errorCommand(TelegramBot bot, Update update, String data) {
        String content = new StringBuilder("Error: ").append(data)
                .toString();
        sendMessage(bot, update, content);
    }

    public static void completeCommand(TelegramBot bot, Update update, String data, String response) {
        String content = new StringBuilder("Complete: ").append(data)
                .append("\n").append(response)
                .toString();
        sendMessage(bot, update, content);
    }
}

