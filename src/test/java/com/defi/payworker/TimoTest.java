package com.defi.payworker;

import com.google.gson.JsonObject;

public class TimoTest {

    public static void startTest() {
        loginTimo("0836993400", "SNTB@13nkt");
    }

    private static void loginTimo(String username, String password) {
        String path = new StringBuilder()
                .append("/timo/login")
                .toString();
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        VertXClient.post(path, json);
    }
}
