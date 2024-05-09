package com.defi.payworker.vertx.donation;

import com.defi.payworker.vertx.PayWorkerVertx;
import com.defi.payworker.vertx.auth.AuthRouter;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class DonationApi {
    public static void configAPI(Router router) {
        internalApi(router);
    }
    private static void internalApi(Router router) {
        router.post(PayWorkerVertx.instance().getPath("/donation/list"))
                .handler(BodyHandler.create(false))
                .handler(AuthRouter::internal)
                .handler(DonationRouter::listDonation);
        router.post(PayWorkerVertx.instance().getPath("/donation/top"))
                .handler(BodyHandler.create(false))
                .handler(AuthRouter::internal)
                .handler(DonationRouter::topDonation);
    }
}
