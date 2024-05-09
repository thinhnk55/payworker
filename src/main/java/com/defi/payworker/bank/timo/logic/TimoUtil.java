package com.defi.payworker.bank.timo.logic;

import com.defi.util.string.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimoUtil {
    public static JsonObject generateHeader(){
        JsonObject json = new JsonObject();
        String uuid = StringUtil.generateUUID();
        String appVersion = "214";
        String client = new StringBuilder()
                .append(":WEB:WEB:")
                .append(appVersion)
                .append(":WEB:desktop:chrome")
                .toString();
        String xTimoDevicereg = new StringBuilder(StringUtil.generateUUID()).append(client).toString();
        json.addProperty("x-timo-devicereg", xTimoDevicereg);
        String gofsContext = StringUtil.sha256(
                new StringBuilder()
                        .append("WEB.")
                        .append(uuid).append(".")
                        .append(appVersion)
                        .toString()
        );
        String gofsContextId =  new StringBuilder()
                .append(gofsContext).append(".")
                .append(StringUtil.generateUUID())
                .toString();
        json.addProperty("x-gofs-context-id", gofsContextId);
        return json;
    }
    public static long getNotificationIdFromResponse(JsonObject data) {
        long id = data.getAsJsonObject("data")
                .get("idIndex").getAsLong();
        return id;
    }
    public static JsonArray getNotificationListFromResponse(JsonObject data) {
        JsonArray array = data.getAsJsonObject("data")
                .get("notifyList").getAsJsonArray();
        return array;
    }

    public static JsonObject extractBalanceTransactionFromNotification(JsonObject data) {
        JsonObject transactionInfo = new JsonObject();
        String fullContent = data.get("content").getAsString();
        String[] lines = fullContent.split("\n");
        String description = lines[2];
        int index = description.indexOf(":");
        String note = description.substring(index+2);
        transactionInfo.addProperty("note", note);

        String deeplink = data.get("deeplink").getAsString();
        String bank_transaction_id = deeplink.substring(deeplink.length()-17, deeplink.length()-1);
        transactionInfo.addProperty("transaction_id", bank_transaction_id);
        String line = lines[0];
        String regex = "\\d+([.]\\d+)*";
        Pattern accountBalancePattern = Pattern.compile(regex);
        Matcher matcher = accountBalancePattern.matcher(line);
        long amount = 0;
        if (matcher.find()) {
            String money = matcher.group(0).replaceAll("\\.","");
            amount = Long.parseLong(money);
        }
        if(line.contains("gi")){
            transactionInfo.addProperty("amount", -amount);
        }else{
            transactionInfo.addProperty("amount", amount);
        }
        return transactionInfo;
    }
}
