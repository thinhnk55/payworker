package com.defi.payworker.vertx;

import com.defi.util.json.GsonUtil;
import com.google.gson.JsonObject;
import io.vertx.core.Vertx;

import java.util.Set;

public class PayWorkerVertx {
    public Vertx vertx;
    private static PayWorkerVertx ins = null;
    public static PayWorkerVertx instance() {
        if (ins == null) {
            ins = new PayWorkerVertx();
        }
        return ins;
    }
    private PayWorkerVertx(){

    }

    public void init(String configFile) {
        config = GsonUtil.getJsonObject(configFile);
        this.name = config.get("name").getAsString();
        this.url_prefix = config.get("url_prefix").getAsString();
        this.http_port= config.get("http_port").getAsInt();
        this.websocket_port = config.get("websocket_port").getAsInt();
        this.internal_token = config.get("internal_token").getAsString();

    }
    JsonObject config;

    public String name;
    public int http_port;
    public int websocket_port;
    public String url_prefix;

    public String internal_token;

    public String getPath(String path) {
        String fullPath = new StringBuilder(url_prefix).append(path).toString();
        return fullPath;
    }

    public boolean verify(String token) {
        return internal_token.equals(token);
    }
}
