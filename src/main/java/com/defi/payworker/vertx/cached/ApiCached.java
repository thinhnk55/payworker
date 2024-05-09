package com.defi.payworker.vertx.cached;

import java.util.HashMap;
import java.util.Map;

public class ApiCached {
    private static ApiCached ins;
    private ApiCached() {
        expired =  new HashMap<>();
        cached =  new HashMap<>();
    }

    public static ApiCached instance() {
        if (ins == null) {
            ins = new ApiCached();
        }
        return ins;
    }
    Map<String, Long> expired;
    Map<String, String> cached;

    public String getCached(String uri){
        if(!expired.containsKey(uri)){
            return null;
        }
        long expiredTime = expired.get(uri);
        long now = System.currentTimeMillis();
        if(expiredTime < now){
            return null;
        }
        return cached.get(uri);
    }
    public void setCached(String uri, String response, long timeout){
        long now = System.currentTimeMillis();
        long expiredTime = now + timeout;
        expired.put(uri, expiredTime);
        cached.put(uri, response);
    }
}
