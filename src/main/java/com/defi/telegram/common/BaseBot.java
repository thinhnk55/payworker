package com.defi.telegram.common;

import com.defi.util.log.DebugLogger;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;

public class BaseBot implements UpdatesListener {
    public String name;
    public String token;
    public TelegramBot bot;
    ITelegramProcessor processor;

    public BaseBot(ITelegramProcessor processor,
                   String name, String token) {
        this.name = name;
        this.token = token;
        this.processor = processor;
    }
    public void run(){
        this.bot = new TelegramBot(token);
        this.bot.setUpdatesListener(this);
    }
    @Override
    public int process(List<Update> updates) {
        for(final Update update: updates){
            try {
                processor.process(bot, update);
            }catch (Exception e){
                DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
