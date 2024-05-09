package com.defi.telegram.admin;

import com.defi.telegram.common.LanguageMessage;

public class AdminLanguage {
    public static final String donation_update = "donation_update";
    public static LanguageMessage message;

    public static void init(String configFile){
        message = new LanguageMessage(configFile);
    }
    public static String getEmojiMessage(String lang, String key){
        return message.getEmojiMessage(lang, key);
    }
}
