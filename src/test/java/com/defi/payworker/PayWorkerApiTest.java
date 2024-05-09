package com.defi.payworker;

import org.apache.log4j.xml.DOMConfigurator;

public class PayWorkerApiTest {
    public static void main(String[] args) {
        try {
            initConfig();
            TimoTest.startTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void initConfig() throws Exception {
        DOMConfigurator.configure("config/payworker/log/log4j.xml");
        VertXClient.init("http://127.0.0.1:8282/pw", "pw-SLQOP4556acoqwke");
    }
}
