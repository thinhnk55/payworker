package com.defi.telegram.admin;

import com.defi.telegram.channel.TelegramChannelManager;
import com.defi.telegram.common.CommonMessage;
import com.defi.telegram.common.MessageTemplate;
import com.defi.util.time.TimeUtil;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;

public class AdminMessage {
    public static void publish_donation(JsonObject data){
        long chat_id = TelegramChannelManager.instance().channel_id;
        TelegramBot bot = TelegramChannelManager.instance().getTelegramBot();
        String content = AdminLanguage.getEmojiMessage("vi", AdminLanguage.donation_update);
        String sender = data.get("sender").getAsString();
        String receiver = data.get("receiver").getAsString();
        String message = data.get("message").getAsString();
        String amount = data.get("amount").getAsString();
        long create_time = data.get("create_time").getAsLong();
        content = content.replace("@SENDER", sender);
        content = content.replace("@AMOUNT", amount);
        content = content.replace("@RECEIVER", receiver);
        content = content.replace("@MESSAGE", MessageTemplate.toMarkdownV2(message));
        content = content.replace("@TIME", TimeUtil.parseTime(create_time,
                "hh:mm:ss dd/mm/yyy","Asia/Ho_Chi_Minh"));
        CommonMessage.sendMessage(bot, chat_id, content, ParseMode.MarkdownV2);
    }

    public static void send_notification(String message) {
        String content = AdminLanguage.getEmojiMessage("vi", AdminLanguage.notification);
        content = new StringBuilder()
                .append(content)
                .append(message)
                .toString();
        TelegramBot bot = TelegramChannelManager.instance().getTelegramBot();
        long[] list_chat_id = AdminModule.instance().admin_list;
        for(int i = 0; i < list_chat_id.length; i++){
            long chat_id = list_chat_id[i];
            CommonMessage.sendMessage(bot, chat_id, content);
        }
    }
}
