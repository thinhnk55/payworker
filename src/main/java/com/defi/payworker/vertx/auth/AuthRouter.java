package com.defi.payworker.vertx.auth;

import com.defi.common.SimpleResponse;
import com.defi.payworker.vertx.PayWorkerVertx;
import com.defi.util.log.DebugLogger;
import com.google.gson.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class AuthRouter {
    public static void internal(RoutingContext rc) {
        try{
            String token = rc.request().getHeader("token");
            boolean result = PayWorkerVertx.instance().verify(token);
            if(result){
                rc.next();
            }else{
                rc.response().end(SimpleResponse.createResponse(2).toString());
            }
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            rc.response().end(SimpleResponse.createResponse(1).toString());
        }
    }
}
