package com.defi.telegram.channel;

import com.defi.telegram.common.BaseModule;
import com.defi.telegram.common.CommandParams;
import com.defi.telegram.common.ITelegramProcessor;
import com.defi.telegram.admin.AdminModule;
import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.LinkedList;
import java.util.List;

public class ChannelProcessor implements ITelegramProcessor {
    JsonObject config;
    List<BaseModule> modules;


    public ChannelProcessor(JsonObject config){
        this.config = config;
        modules = new LinkedList<>();
        JsonObject adminConfig = config.getAsJsonObject("modules")
                .getAsJsonObject("admin");
        AdminModule.instance().init(adminConfig);
        modules.add(AdminModule.instance());
    }
    @Override
    public void process(TelegramBot bot, Update update) {
        try {
            CommandParams params = parseCommand(update);
            DebugLogger.logger.info("{}", GsonUtil.gson.toJson(update));
            for(BaseModule module: modules){
                if(module.accept(params)){
                    module.process(bot, update, params, config);
                    break;
                }
            }
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private CommandParams parseCommand(Update update) {
        String data = null;
        boolean is_callback = false;
        if(update.message() != null && update.message().text() != null){
            data = update.message().text();
        }else
        if(update.callbackQuery() != null && update.callbackQuery().data() != null){
            data = update.callbackQuery().data();
            is_callback = true;
        }else if(update.message().photo() != null || update.message().document() != null){
            data = "";
        }
        return new CommandParams(data, is_callback);
    }
}
