package com.defi.payworker;

import com.defi.util.log.DebugLogger;
import com.defi.util.network.OkHttpUtil;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class VertXClient {
    public static String base_url;
    public static String access_token;
    public static void init(String url, String token){
        base_url = url;
        access_token = token;
    }
    public static JsonObject get(String path){
        String url = createUrl(path);
        Map<String, String> headers = new HashMap<>();
        headers.put("token", access_token);
        JsonObject response = OkHttpUtil.get(url, headers);
        DebugLogger.logger.info("\nGET url = {}\nHeader: token = {}\nresponse = {}", url, access_token, response);
        return response;
    }
    public static JsonObject post(String path, JsonObject json){
        String url = createUrl(path);
        Map<String, String> headers = new HashMap<>();
        headers.put("token", access_token);
        String data = json.toString();
        JsonObject response = OkHttpUtil.postJson(url, data, headers);
        DebugLogger.logger.info("\nPOST url = {}\nHeader: token = {}\n data = {} response = {}", url, access_token, data, response);
        return response;
    }

    public static String createUrl(String path) {
        return new StringBuilder(base_url)
                .append(path).toString();
    }
}
