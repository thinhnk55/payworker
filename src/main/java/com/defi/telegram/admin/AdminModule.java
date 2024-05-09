package com.defi.telegram.admin;

import com.defi.payworker.bank.fake.logic.FakeBankWorker;
import com.defi.payworker.bank.timo.logic.TimoWorker;
import com.defi.telegram.common.BaseModule;
import com.defi.telegram.common.CommandParams;
import com.defi.telegram.common.CommonMessage;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

public class AdminModule implements BaseModule {
    public AdminModule(JsonObject config) {
        String languageFile = config.get("language").getAsString();
        AdminLanguage.init(languageFile);
    }

    @Override
    public boolean accept(CommandParams params) {
        int offset = params.command - params.command % 100;
        return offset == AdminCommandId.offset;
    }

    @Override
    public void process(TelegramBot bot, Update update, CommandParams params, JsonObject config) {
        try{
            if(params.command == AdminCommandId.fake_bank_transfer){
                fake_bank_transfer(bot, update, params, config);
            }
            if(params.command == AdminCommandId.timo_login){
                timo_login(bot, update, params, config);
            }
            if(params.command == AdminCommandId.timo_login_otp){
                timo_login_otp(bot, update, params, config);
            }
        }catch (Exception e){
            CommonMessage.errorCommand(bot, update, params.data);
        }
    }

    private void timo_login(TelegramBot bot, Update update, CommandParams params, JsonObject config) {
        try {
            String username = params.params[2];
            String password = params.params[3];
            JsonObject response = TimoWorker.instance().manager.login(username, password);
            CommonMessage.completeCommand(bot, update, params.data, response.toString());
        }catch (Exception e){
            CommonMessage.errorCommand(bot, update, params.data);
        }
    }

    private void timo_login_otp(TelegramBot bot, Update update, CommandParams params, JsonObject config) {
        try {
            String username = params.params[2];
            String otp = params.params[3];
            JsonObject response = TimoWorker.instance().manager.commitLogin(username, otp);
            CommonMessage.completeCommand(bot, update, params.data, response.toString());
        }catch (Exception e){
            CommonMessage.errorCommand(bot, update, params.data);
        }
    }

    private void fake_bank_transfer(TelegramBot bot, Update update, CommandParams params, JsonObject config) {
        try {
            long amount = Long.parseLong(params.params[2]);
            String note = params.params[3];
            String receiver_account = "FAKE ACCOUNT";
            String receiver_name = "FAKE NAME";
            JsonObject response = FakeBankWorker.instance().onFakeTransaction(receiver_account, receiver_name, amount, note);
            CommonMessage.completeCommand(bot, update, params.data, response.toString());
        }catch (Exception e){
            CommonMessage.errorCommand(bot, update, params.data);
        }
    }
}
