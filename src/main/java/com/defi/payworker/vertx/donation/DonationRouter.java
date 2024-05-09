package com.defi.payworker.vertx.donation;

import com.defi.common.SimpleResponse;
import com.defi.donation.logic.DonationManager;
import com.defi.payworker.vertx.cached.ApiCached;
import com.defi.util.log.DebugLogger;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class DonationRouter {
    public static void listDonation(RoutingContext rc) {
        try{
            try{
                String uri = rc.request().uri();
                String cached = ApiCached.instance().getCached(uri);
                if(cached != null){
                    rc.response().end(cached);
                    return;
                }
                long page = Long.parseLong(rc.request().getParam("page", "0"));
                long limit = Long.parseLong(rc.request().getParam("limit", "10"));
                if(limit > 100){
                    limit = 10;
                }
                long offset = page*limit;
                String response = DonationManager.instance().service.listDonation(limit, offset).toString();
                rc.response().end(response);
                ApiCached.instance().setCached(uri, response, 5000);
            }catch (Exception e){
                DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
                rc.response().end(SimpleResponse.createResponse(1).toString());
            }
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            rc.response().end(SimpleResponse.createResponse(1).toString());
        }
    }

    public static void topDonation(RoutingContext rc) {
        try{
            String uri = rc.request().uri();
            String cached = ApiCached.instance().getCached(uri);
            if(cached != null){
                rc.response().end(cached);
                return;
            }
            long from = rc.request().params().contains("from") ? Long.parseLong(rc.request().getParam("from"))
                    : System.currentTimeMillis() - 604800000;
            long limit = Long.parseLong(rc.request().getParam("limit", "10"));
            if(limit > 100){
                limit = 10;
            }
            String response = DonationManager.instance().service.topDonation(from, limit).toString();
            rc.response().end(response);
            ApiCached.instance().setCached(uri, response, 5000);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            rc.response().end(SimpleResponse.createResponse(1).toString());
        }
    }
}
