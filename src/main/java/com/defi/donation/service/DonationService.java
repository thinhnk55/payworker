package com.defi.donation.service;

import com.defi.common.SimpleResponse;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.SQLJavaBridge;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class DonationService implements IDonationService{
    String table;
    SQLJavaBridge bridge;
    public DonationService(String table, SQLJavaBridge bridge){
        this.table = table;
        this.bridge = bridge;
        boolean exist = bridge.checkTableExisting(table);
        if(!exist){
            createTable();
        }
    }

    private void createTable() {
        String createTableSQL = ("CREATE TABLE IF NOT EXISTS table_name ("
                + "id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                + "receiver VARCHAR(256),"
                + "sender VARCHAR(256),"
                + "hidden_sender VARCHAR(256),"
                + "amount BIGINT,"
                + "message VARCHAR(256),"
                + "create_time VARCHAR(256)"
                + ")").replace("table_name", table);
        String index1 = "CREATE INDEX table_name_create_time_index ON table_name (create_time ASC)"
                .replace("table_name", table);
        String index2 = "CREATE INDEX table_name_receiver_index ON table_name (receiver ASC)"
                .replace("table_name", table);
        String index3 = "CREATE INDEX table_name_amount_index ON table_name (amount ASC)"
                .replace("table_name", table);
        bridge.createTable(table, createTableSQL, index1, index2, index3);
    }

    @Override
    public JsonObject createDonation(String receiver, String sender, String hidden_sender, long amount, String message) {
        try{
            long create_time = System.currentTimeMillis();
            JsonObject json = new JsonObject();
            json.addProperty("receiver", receiver);
            json.addProperty("sender", sender);
            json.addProperty("hidden_sender", hidden_sender);
            json.addProperty("amount", amount);
            json.addProperty("message", message);
            json.addProperty("create_time", create_time);
            bridge.insertObjectToDB(table, json);
            return SimpleResponse.createResponse(0, json);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject listDonation(long limit, long offset) {
        try{
            String query = new StringBuilder("SELECT id, receiver, hidden_sender, amount, message, create_time FROM ")
                    .append(table)
                    .append(" ORDER BY id DESC LIMIT ? OFFSET ?")
                    .toString();
            JsonArray array = bridge.query(query, limit, offset);
            if(array.size() == 0){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0, array);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject topDonation(long from_timestamp, long limit) {
        try{
            String query = new StringBuilder("SELECT hidden_sender, SUM(amount) as donation FROM ")
                    .append(table)
                    .append(" WHERE create_time > ? GROUP BY sender ORDER BY donation DESC LIMIT ?")
                    .toString();
            JsonArray array = bridge.query(query, from_timestamp, limit);
            if(array.size() == 0){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0, array);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject listDonation(String receiver, long limit, long offset) {
        try{
            String query = new StringBuilder("SELECT id, receiver, hidden_sender, amount, message, create_time FROM ")
                    .append(table)
                    .append(" WHERE receiver = ? ORDER BY id DESC LIMIT ? OFFSET ?")
                    .toString();
            JsonArray array = bridge.query(query, receiver, limit, offset);
            if(array.size() == 0){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0, array);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject summary(String receiver) {
        try{
            String query = new StringBuilder("SELECT receiver, COUNT(id) as count, SUM(amount) as total, MAX(amount) as max FROM ")
                    .append(table)
                    .append(" WHERE receiver = ?")
                    .toString();
            JsonObject json = bridge.queryOne(query, receiver);
            if(json == null){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0, json);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
}
