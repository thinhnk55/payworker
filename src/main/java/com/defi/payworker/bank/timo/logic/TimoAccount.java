package com.defi.payworker.bank.timo.logic;

import com.defi.util.json.GsonUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TimoAccount {
    public String username;
    public String password;
    public String account_number;
    public String account_owner;
    public JsonObject other;
    public int state;
    public int failure;

    public TimoAccount(JsonObject json) {
        this.username = json.get("username").getAsString();
        this.password = json.get("password").getAsString();
        this.account_number = json.get("account_number").isJsonNull()? null : json.get("account_number").getAsString();
        this.account_owner = json.get("account_owner").isJsonNull()? null : json.get("account_owner").getAsString();
        this.other = json.getAsJsonObject("other");
        this.state = json.get("state").getAsInt();
        this.failure = 0;
    }
    public String getToken() {
        String token = other.getAsJsonObject("login").get("token").getAsString();
        return token;
    }

    public long getLastNotification(){
        JsonElement json = other.get("notification");
        if(json == null){
            return 0;
        }
        return json.getAsJsonObject().get("last").getAsLong();
    }

    public void setLastNotification(long last) {
        JsonElement json = other.get("notification");
        if(json == null){
            JsonObject notification = new JsonObject();
            notification.addProperty("last", last);
            other.add("notification", notification);
        }else{
            JsonObject notification = json.getAsJsonObject();
            notification.addProperty("last", last);
        }
    }

    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", "***");
        json.addProperty("account_number", account_number);
        json.addProperty("account_owner", account_owner);
        json.add("other", other);
        json.addProperty("state", state);
        json.addProperty("failure", failure);
        return json;
    }
}
