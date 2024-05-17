package com.defi.payworker.vertx.donation;

import com.defi.payworker.vertx.PayWorkerVertx;
import com.defi.payworker.vertx.auth.AuthRouter;
import io.vertx.ext.web.Router;

public class DonationApi {
    public static void configAPI(Router router) {
        internalApi(router);
    }
    private static void internalApi(Router router) {
        router.get(PayWorkerVertx.instance().getPath("/donation/list"))
                .handler(AuthRouter::internal)
                .handler(DonationRouter::listDonation);
        router.get(PayWorkerVertx.instance().getPath("/donation/summary"))
                .handler(AuthRouter::internal)
                .handler(DonationRouter::summaryDonation);
        router.get(PayWorkerVertx.instance().getPath("/donation/top"))
                .handler(AuthRouter::internal)
                .handler(DonationRouter::topDonation);
    }
}
