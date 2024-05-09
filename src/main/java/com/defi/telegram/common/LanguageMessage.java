package com.defi.telegram.common;

import com.defi.util.json.GsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vdurmont.emoji.EmojiParser;

import java.util.Set;

public class LanguageMessage {
    JsonObject lang;
    String defaultLanguageCode;
    public Set<String> support_language_set;
    public JsonArray support_language;

    public LanguageMessage(String configFile){
        lang = GsonUtil.getJsonObject(configFile);
        defaultLanguageCode = lang.get("default").getAsString();
        this.support_language = lang.getAsJsonArray("support_language");
        this.support_language_set = GsonUtil.toSet(this.support_language);
    }
    public String getMessage(String languageCode, String key){
        if(lang.has(languageCode)){
            JsonObject data = lang.getAsJsonObject(languageCode);
            if(data.has(key)){
                return data.get(key).getAsString();
            }
        }
        JsonObject data = lang.getAsJsonObject(defaultLanguageCode);
        if(data.has(key)){
            return data.get(key).getAsString();
        }
        return new StringBuilder("[")
                .append(languageCode).append("|").append(key)
                .append("]").toString();
    }
    public String getEmojiMessage(String languageCode, String key){
        String data = getMessage(languageCode, key);
        return EmojiParser.parseToUnicode(data);
    }
    public boolean isSupportLanguage(String language) {
        return this.support_language_set.contains(language);
    }
}
