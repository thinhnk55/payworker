package com.defi.payworker;

import com.defi.payworker.bank.timo.logic.TimoUtil;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;

public class TimoTest {

    public static void startTest() {
//        loginTimo("0836993400", "SNTB@13nkt");
        connectTimoTest();
    }
    public static void connectTimoTest(){
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String jsonBody = "{\"username\":\"0836993400\",\"password\":\"9639447d615a4d6989ef9c59215451e198fa399b1520d41db7ddf31f17aed429a37b69541ab70879a51905bbf652d7b0370f450828141a3b2dd0290ca9af4e9a\",\"lang\":\"en\"}";

        RequestBody body = RequestBody.create(jsonBody, JSON);
        JsonObject jsonHeader = TimoUtil.generateHeader();
        String xTimoDevicereg = jsonHeader.get("x-timo-devicereg").getAsString();
        String gofsContextId = jsonHeader.get("x-gofs-context-id").getAsString();
        Request request = new Request.Builder()
                .url("https://app2.timo.vn/login")
                .post(body)
                .addHeader("accept", "application/json, text/plain")
                .addHeader("accept-language", "en,vi;q=0.9,en-US;q=0.8")
                .addHeader("cache-control", "no-cache")
                .addHeader("content-type", "application/json; charset=UTF-8")
                .addHeader("pragma", "no-cache")
                .addHeader("priority", "u=1, i")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("x-gofs-context-id", gofsContextId)
                .addHeader("x-timo-devicereg", xTimoDevicereg)
                .addHeader("Referer", "https://my.timo.vn/")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Print response
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
