package com.defi.payworker.vertx.timo;

import com.defi.payworker.vertx.PayWorkerVertx;
import com.defi.payworker.vertx.auth.AuthRouter;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class TimoApi {
    public static void configAPI(Router router) {
        internalApi(router);
    }
    private static void internalApi(Router router) {
        router.post(PayWorkerVertx.instance().getPath("/timo/login"))
                .handler(BodyHandler.create(false))
                .handler(AuthRouter::internal)
                .handler(TimoRouter::login);
        router.post(PayWorkerVertx.instance().getPath("/timo/login/commit"))
                .handler(BodyHandler.create(false))
                .handler(AuthRouter::internal)
                .handler(TimoRouter::commitLogin);
    }
}
