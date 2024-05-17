package com.defi.payworker.bank.timo.logic;

import com.defi.common.SimpleResponse;
import com.defi.payworker.bank.timo.service.ITimoAccountervice;
import com.defi.payworker.bank.timo.service.TimoConstant;
import com.defi.telegram.admin.AdminMessage;
import com.defi.util.log.DebugLogger;
import com.defi.util.string.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class TimoAccountManager {
    public ITimoAccountervice service;
    public Map<String, TimoAccount> accounts;

    public TimoAccountManager(ITimoAccountervice service) {
        accounts = new HashMap<>();
        this.service = service;
        JsonObject response = this.service.listByState(TimoConstant.STATE_COMMIT);
        if(SimpleResponse.isSuccess(response)){
            JsonArray array = response.getAsJsonArray("d");
            for(int i = 0; i < array.size(); i++){
                JsonObject json = array.get(i).getAsJsonObject();
                TimoAccount account = new TimoAccount(json);
                accounts.put(account.username, account);
            }
        }
    }


    public JsonObject login(String username, String password){
        JsonObject response = service.getByUserName(username);
        if(!SimpleResponse.isSuccess(response)){
            response = service.create(username, password);
            if(!SimpleResponse.isSuccess(response)){
                return SimpleResponse.createResponse(1);
            }
        }
        JsonObject data = response.getAsJsonObject("d");
        JsonObject other = data.getAsJsonObject("other");
        JsonObject header = other.getAsJsonObject("header");
        String hashedPassword = StringUtil.sha512(password);
        response = TimoConnector.login(username, hashedPassword, header);
        int error = response.get("e").getAsInt();
        if(error == 0){
            JsonObject timoResponse = response.getAsJsonObject("d");
            other.add("login", timoResponse);
            data.addProperty("state", TimoConstant.STATE_COMMIT);
            service.updateLogin(username, other, TimoConstant.STATE_COMMIT);
            TimoAccount account = new TimoAccount(data);
            onLogin(account);
            return SimpleResponse.createResponse(0);
        }
        if(error == 10){
            JsonObject timoResponse = response.getAsJsonObject("d");
            other.add("login", timoResponse);
            data.addProperty("state", TimoConstant.STATE_NOT_COMMIT);
            service.updateLogin(username,  other, TimoConstant.STATE_NOT_COMMIT);
            return SimpleResponse.createResponse(10);
        }
        return SimpleResponse.createResponse(11);
    }

    private void onLogin(TimoAccount account) {
        if(account.account_number == null){
            updateAccountNumber(account);
        }
        accounts.put(account.username, account);
        DebugLogger.logger.info("onLogin {}", account.username);
        String message = new StringBuilder()
                .append("Account Login: ")
                .append(account.username)
                .toString();
        AdminMessage.send_notification(message);
    }

    private void updateAccountNumber(TimoAccount account) {
        String token = account.getToken();
        JsonObject response = TimoConnector.getBankInfo(token);
        if(SimpleResponse.isSuccess(response)) {
            JsonObject data = response.getAsJsonObject("d");
            account.account_owner = data.get("fullName").getAsString();
            account.account_number = data.get("accountNumber").getAsString();
            service.updateBankInfo(account.username, account.account_owner, account.account_number);
        }
    }

    public JsonObject commitLogin(String username, String otp) {
        JsonObject response = service.getByUserName(username);
        if(!SimpleResponse.isSuccess(response)){
            return SimpleResponse.createResponse(1);
        }
        JsonObject data = response.getAsJsonObject("d");
        JsonObject other = data.getAsJsonObject("other");
        String refNo = other.getAsJsonObject("login").get("refNo").getAsString();
        String token = other.getAsJsonObject("login").get("token").getAsString();
        response = TimoConnector.commitLogin(refNo, otp, token);
        if(SimpleResponse.isSuccess(response)){
            JsonObject timoResponse = response.getAsJsonObject("d");
            other.add("login", timoResponse);
            data.addProperty("state", TimoConstant.STATE_COMMIT);
            service.updateLogin(username, other, TimoConstant.STATE_COMMIT);
            TimoAccount account = new TimoAccount(data);
            onLogin(account);
            return SimpleResponse.createResponse(0);
        }
        return SimpleResponse.createResponse(10);
    }

    public void onFailure(TimoAccount account) {
        account.failure++;
        if(account.failure > 3) {
            accounts.remove(account.username);
            service.updateState(account.username, TimoConstant.STATE_NOT_COMMIT);
            String message = new StringBuilder()
                    .append("Account Failure: ")
                            .append(account.username)
                                    .toString();
            AdminMessage.send_notification(message);
        }
    }
}
