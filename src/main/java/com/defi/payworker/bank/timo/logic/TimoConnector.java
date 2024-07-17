package com.defi.payworker.bank.timo.logic;

import com.defi.common.SimpleResponse;
import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;
import com.defi.util.network.OkHttpUtil;
import com.google.gson.JsonObject;
import okhttp3.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

import static com.defi.payworker.bank.timo.logic.TimoConfig.*;

public class TimoConnector {
    public static JsonObject login(String username, String password, JsonObject header) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("username", username);
            body.addProperty("password", password);
            body.addProperty("lang", "en");
            Map<String, String> headers = new HashMap<>();
            String xTimoDevicereg = header.get("x-timo-devicereg").getAsString();
            headers.put("x-timo-devicereg", xTimoDevicereg);
            String gofsContextId = header.get("x-gofs-context-id").getAsString();
            headers.put("x-gofs-context-id", gofsContextId);
            try (Response response = OkHttpUtil.postFullResponse(URL_LOGIN, body.toString(), headers)) {
                if (response != null && response.code() == 200) {
                    String responseBody = response.body().string();
                    DebugLogger.logger.info("TimoConnector login: {} {}", body, responseBody);
                    JsonObject res = GsonUtil.toJsonObject(responseBody);
                    if (res.get("code").getAsInt() == ERROR_TIMO_ACCOUNT_NOT_COMMIT) {
                        JsonObject data = res.get("data").getAsJsonObject();
                        return SimpleResponse.createResponse(10, data);
                    }else
                    if (res.get("code").getAsInt() == CODE_SUCCESS) {
                        JsonObject data = res.get("data").getAsJsonObject();
                        return SimpleResponse.createResponse(0, data);
                    }else {
                        return SimpleResponse.createResponse(11);
                    }
                }
                return SimpleResponse.createResponse(12);
            }
        } catch (Exception e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
    public static JsonObject commitLogin(String refNo, String otp, String token) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("refNo", refNo);
            body.addProperty("otp", otp);
            body.addProperty("securityChallenge", otp);
            body.addProperty("securityCode", otp);
            Map<String, String> headers = new HashMap<>();
            headers.put("token", token);
            try (Response response = OkHttpUtil.postFullResponse(TimoConfig.URL_LOGIN_COMMIT, body.toString(), headers)) {
                if (response != null && response.code() == 200) {
                    String responseBody = response.body().string();
                    DebugLogger.logger.info("TimoConnector login: {} {}", body, responseBody);
                    JsonObject res = GsonUtil.toJsonObject(responseBody);
                    if (res.get("code").getAsInt() == ERROR_OTP_INVALID) {
                        return SimpleResponse.createResponse(10);
                    }
                    if (res.get("code").getAsInt() == ERROR_OTP_EXPIRED) {
                        return SimpleResponse.createResponse(11);
                    }
                    if (res.get("code").getAsInt() == CODE_SUCCESS) {
                        JsonObject data = res.get("data").getAsJsonObject();
                        return SimpleResponse.createResponse(0, data);
                    }
                }
                return SimpleResponse.createResponse(12);
            }
        } catch (Exception e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
    public static JsonObject getBankInfo(String token) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("token", token);
            try(Response response = OkHttpUtil.getFullResponse(TimoConfig.URL_BANK_INFO, headers)){
                if(response.code() == 200){
                    String responseBody = response.body().string();
                    JsonObject res = GsonUtil.toJsonObject(responseBody);
                    return SimpleResponse.createResponse(0, res.get("data").getAsJsonObject());
                }
                return SimpleResponse.createResponse(10);
            }
        } catch (Exception e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    public static JsonObject getNumberOfNotification(String token){
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Token", token);
            try (Response response = OkHttpUtil.getFullResponse(TimoConfig.URL_NOTIFICATION_CHECK, headers)){
                if (response != null && response.code() == 200) {
                    String data = response.body().string();
                    response.body().close();
                    JsonObject jsonResponse = GsonUtil.toJsonObject(data);
                    //Lay so luong thong bao moi
                    JsonObject json = jsonResponse.getAsJsonObject("data");
                    return SimpleResponse.createResponse(0, json);
                } else {
                    return SimpleResponse.createResponse(10);
                }
            }
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
    public static JsonObject getCurrentNotification(String token) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Token", token);
            try(Response response = OkHttpUtil.getFullResponse(TimoConfig.URL_NOTIFICATION_EN, headers)) {
                if (response != null && response.code() == 200) {
                    String data = response.body().string();
                    response.body().close();
                    JsonObject jsonResponse = GsonUtil.toJsonObject(data);
                    return SimpleResponse.createResponse(0, jsonResponse);
                } else {
                    return SimpleResponse.createResponse(10);
                }
            }
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
    public static JsonObject getNotificationByNotificationId(String token, long notificationId) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Token", token);
            String url = new StringBuilder(TimoConfig.URL_NOTIFICATION_EN)
                    .append("?idIndex=")
                    .append(notificationId).toString();
            try(Response response = OkHttpUtil.getFullResponse(url, headers)) {
                if (response.code() == 200) {
                    String data = response.body().string();
                    response.body().close();
                    JsonObject jsonResponse = GsonUtil.toJsonObject(data);
                    return SimpleResponse.createResponse(0, jsonResponse);
                } else {
                    return SimpleResponse.createResponse(10);
                }
            }
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
    public static int checkNotification(String token) {
        try{
            Map<String, String> headers = new HashMap<>();
            headers.put("Token", token);
            try (Response response = OkHttpUtil.getFullResponse(TimoConfig.URL_NOTIFICATION_CHECK, headers)) {
                if (response != null && response.code() == 200) {
                    String data = response.body().string();
                    JsonObject jsonResponse = GsonUtil.toJsonObject(data);
                    //Lay so luong thong bao moi
                    int numberOfNotifications = jsonResponse.get("data").getAsJsonObject().get("numberOfNotification").getAsInt();
                    return numberOfNotifications;
                }
            }
            return -1;
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return -1;
        }
    }
}
