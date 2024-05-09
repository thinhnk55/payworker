package com.defi.payworker.vertx;

import com.defi.payworker.vertx.donation.DonationApi;
import com.defi.payworker.vertx.timo.TimoApi;
import io.vertx.ext.web.Router;

public class PayWorkerHttpAPI {
    public static void configAPI(Router router) {
        DonationApi.configAPI(router);
        TimoApi.configAPI(router);
    }
}
