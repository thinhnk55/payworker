package com.defi.payworker.bank.timo.logic;

import com.defi.common.SimpleResponse;
import com.defi.payworker.bank.common.logic.BankManager;
import com.defi.payworker.bank.common.service.BankCode;
import com.defi.payworker.bank.common.service.BankTransactionState;
import com.defi.util.log.DebugLogger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TimoMachine {
    public static void process(TimoAccount account) {
        long last = account.getLastNotification();
        if(last == 0){
            updateNotificationNewest(account);
        }else{
            updateNotificationFrom(account, last);
        }
    }

    private static void updateNotificationFrom(TimoAccount account, long last) {
        String token = account.getToken();
        JsonObject response = TimoConnector.getCurrentNotification(token);
        if(!SimpleResponse.isSuccess(response)){
            TimoWorker.instance().manager.onFailure(account);
            return;
        }
        JsonArray list = new JsonArray();
        JsonObject data = response.getAsJsonObject("d");
        JsonArray array = TimoUtil.getNotificationListFromResponse(data);
        for(int i = 0; i < array.size(); i++){
            JsonObject json = array.get(i).getAsJsonObject();
            long notificationId = json.get("iD").getAsLong();
            if(notificationId > last) {
                list.add(json);
            }
        }
        long currentNotificationId = TimoUtil.getNotificationIdFromResponse(data);
        while (currentNotificationId > last){
            response = TimoConnector.getNotificationByNotificationId(token, currentNotificationId);
            if(!SimpleResponse.isSuccess(response)){
                TimoWorker.instance().manager.onFailure(account);
                return;
            }
            data = response.getAsJsonObject("d");
            array = TimoUtil.getNotificationListFromResponse(data);
            for(int i = 0; i < array.size(); i++){
                JsonObject json = array.get(i).getAsJsonObject();
                long notificationId = json.get("iD").getAsLong();
                if(notificationId > last) {
                    list.add(json);
                }
            }
            currentNotificationId = TimoUtil.getNotificationIdFromResponse(data);
        }
        processNotification(account, list);
    }

    private static void updateNotificationNewest(TimoAccount account) {
        String token = account.getToken();
        JsonObject response = TimoConnector.getCurrentNotification(token);
        if(!SimpleResponse.isSuccess(response)){
            TimoWorker.instance().manager.onFailure(account);
            return;
        }
        JsonObject data = response.getAsJsonObject("d");
        JsonArray array = TimoUtil.getNotificationListFromResponse(data);
        processNotification(account, array);
    }

    private static void processNotification(TimoAccount account, JsonArray array) {
        long last = account.getLastNotification();
        for(int i = array.size()-1; i >= 0; i--){
            JsonObject json = array.get(i).getAsJsonObject();
            long notificationId = json.get("iD").getAsLong();
            if(notificationId > last) {
                last = notificationId;
                String group = json.get("group").getAsString();
                if (group.equals("Transfer")) {
                    String deeplink = json.get("deeplink").getAsString();
                    if(deeplink.length() > 0) {
                        DebugLogger.logger.info("{}", json);
                        JsonObject transaction = TimoUtil.extractBalanceTransactionFromNotification(json);
                        transaction.addProperty("bank_code", BankCode.TIMO);
                        transaction.addProperty("receiver_account", account.account_number);
                        transaction.addProperty("receiver_name", account.account_owner);
                        transaction.addProperty("create_time", System.currentTimeMillis());
                        transaction.addProperty("state", BankTransactionState.STATE_WAITING);
                        BankManager.instance().onBankTransaction(transaction);
                    }
                }
            }
        }
        account.setLastNotification(last);
        TimoWorker.instance().manager.service.updateOther(account.username, account.other);
    }
}
