package com.defi.telegram.channel;

import com.defi.telegram.common.BaseBot;
import com.defi.util.json.GsonUtil;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;

public class TelegramChannelManager {
    private static TelegramChannelManager ins;
    public static TelegramChannelManager instance() {
        if(ins == null){
            ins = new TelegramChannelManager();
        }
        return ins;
    }
    public TelegramChannelManager(){

    }
    public void init(String configFile){
        JsonObject config = GsonUtil.getJsonObject(configFile);
        String name = config.get("name").getAsString();
        String token = config.get("token").getAsString();
        channel_id = config.get("channel_id").getAsLong();
        ChannelProcessor channelProcessor = new ChannelProcessor(config);
        channelBot = new BaseBot(channelProcessor, name, token);
        channelBot.run();
    }
    BaseBot channelBot;
    public long channel_id;

    public TelegramBot getTelegramBot(){
        return channelBot.bot;
    }
}
