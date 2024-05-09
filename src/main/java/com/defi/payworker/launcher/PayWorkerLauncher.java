package com.defi.payworker.launcher;

import com.defi.donation.logic.DonationManager;
import com.defi.donation.service.DonationService;
import com.defi.donation.service.IDonationService;
import com.defi.payworker.bank.common.logic.BankManager;
import com.defi.payworker.bank.common.service.BankTransactionService;
import com.defi.payworker.bank.common.service.IBankTransactionService;
import com.defi.payworker.bank.timo.logic.TimoWorker;
import com.defi.payworker.bank.timo.service.ITimoAccountervice;
import com.defi.payworker.bank.timo.service.TimoAccountervice;
import com.defi.payworker.vertx.PayWorkerVerticle;
import com.defi.payworker.vertx.PayWorkerVertx;
import com.defi.telegram.channel.TelegramChannelManager;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.HikariClients;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.apache.log4j.xml.DOMConfigurator;

public class PayWorkerLauncher {
    public static void main(String[] args) {
        try {
            initConfig();
            initLogic();
            startLoop();
            startHttpServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void startHttpServer() {
        int procs = Runtime.getRuntime().availableProcessors();
        VertxOptions vxOptions = new VertxOptions()
                .setBlockedThreadCheckInterval(30000);
        PayWorkerVertx.instance().vertx = Vertx.vertx(vxOptions);
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setThreadingModel(ThreadingModel.WORKER)
                .setWorkerPoolSize(procs * 2);
        PayWorkerVertx.instance().vertx.deployVerticle(PayWorkerVerticle.class.getName(),
                deploymentOptions.setInstances(procs * 2), event -> {
                    if (event.succeeded()) {
                        DebugLogger.logger.error("Your Vert.x application is started!");
                    } else {
                        DebugLogger.logger.error("Unable to start your application", event.cause());
                    }
                });
    }

    private static void startLoop() {
        TimoWorker.instance().startLoop();
    }


    private static void initLogic() {
        TelegramChannelManager.instance().init("data/telegram/channel_bot.json");
        IDonationService donationService = new DonationService("donation",
                HikariClients.instance().defaulSQLJavaBridge());
        DonationManager.instance().init(donationService);

        IBankTransactionService bankTransactionService = new BankTransactionService("bank_transaction",
                HikariClients.instance().defaulSQLJavaBridge());
        BankManager.instance().init(bankTransactionService);

        ITimoAccountervice timoAccountervice = new TimoAccountervice("timo_account",
                HikariClients.instance().defaulSQLJavaBridge());
        TimoWorker.instance().init(timoAccountervice);
    }

    public static void initConfig() throws Exception {
        DOMConfigurator.configure("config/payworker/log/log4j.xml");
        PayWorkerVertx.instance().init("config/payworker/vertx/http.json");
        HikariClients.instance().init("config/payworker/sql/databases.json",
                "config/payworker/sql/hikari.properties");
    }
}
