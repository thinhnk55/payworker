package com.defi.payworker.vertx.timo;

import com.defi.common.SimpleResponse;
import com.defi.payworker.bank.timo.logic.TimoWorker;
import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;
import com.google.gson.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class TimoRouter {
    public static void login(RoutingContext rc) {
        try{
            String data = rc.body().asString();
            JsonObject json = GsonUtil.toJsonObject(data);
            String username = json.get("username").getAsString();
            String password = json.get("password").getAsString();
            JsonObject response = TimoWorker.instance().manager.login(username, password);
            rc.response().end(response.toString());
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            rc.response().end(SimpleResponse.createResponse(1).toString());
        }
    }

    public static void commitLogin(RoutingContext rc) {
        try{
            String data = rc.body().asString();
            JsonObject json = GsonUtil.toJsonObject(data);
            String username = json.get("username").getAsString();
            String otp = json.get("otp").getAsString();
            JsonObject response = TimoWorker.instance().manager.commitLogin(username, otp);
            rc.response().end(response.toString());
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            rc.response().end(SimpleResponse.createResponse(1).toString());
        }
    }
}
