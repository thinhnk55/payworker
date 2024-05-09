package com.defi.payworker.bank.timo.logic;

import com.defi.payworker.bank.timo.service.ITimoAccountervice;
import com.defi.util.log.DebugLogger;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimoWorker {
    private static TimoWorker ins;
    private TimoWorker() {

    }

    public static TimoWorker instance() {
        if (ins == null) {
            ins = new TimoWorker();
        }
        return ins;
    }
    public void init(ITimoAccountervice service){
        this.service = service;
        manager = new TimoAccountManager(service);
    }
    public ITimoAccountervice service;
    public TimoAccountManager manager;

    public void loop(){
        try {
            for (TimoAccount account : manager.accounts.values()) {
                TimoMachine.process(account);
            }
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public void startLoop() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::loop, 0, 5, TimeUnit.SECONDS);
    }
}
