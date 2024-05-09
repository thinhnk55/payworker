package com.defi.telegram.common;

import com.pengrad.telegrambot.model.Update;

public class CommandParams {
    public String data;
    public boolean isCallback;
    public int command;

    public String params[];

    public CommandParams(Update update){

    }

    public CommandParams(String data, boolean isCallback) {
        this.data = data;
        this.isCallback = isCallback;
        if(data.startsWith("c")){
            params = data.split("\\|");
            command = Integer.parseInt(params[1]);
        }
    }
}
