package com.defi.payworker.bank.timo.service;

import com.defi.common.SimpleResponse;
import com.defi.payworker.bank.timo.logic.TimoUtil;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.SQLJavaBridge;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class TimoAccountervice implements ITimoAccountervice {
    String table;
    SQLJavaBridge bridge;
    public TimoAccountervice(String table, SQLJavaBridge bridge){
        this.table = table;
        this.bridge = bridge;
        boolean exist = bridge.checkTableExisting(table);
        if(!exist){
            createTable();
        }
    }

    private void createTable() {
        String createTableSQL = ("CREATE TABLE IF NOT EXISTS table_name ("
                + "username VARCHAR(64) PRIMARY KEY,"
                + "password VARCHAR(2048),"
                + "account_number VARCHAR(32),"
                + "account_owner VARCHAR(128),"
                + "other VARCHAR(8192),"
                + "state INT DEFAULT 0"
                + ")").replace("table_name", table);
        bridge.createTable(table, createTableSQL);
    }
    public JsonObject create(String username, String password) {
        try {
            String query = new StringBuilder()
                    .append("INSERT INTO ")
                    .append(table)
                    .append("(username, password,other) VALUES (?,?,?)")
                    .toString();
            JsonObject header = TimoUtil.generateHeader();
            JsonObject other = new JsonObject();
            other.add("header", header);
            int x = bridge.update(query, username, password, other);
            if(x == 0){
                return SimpleResponse.createResponse(10);
            }
            return getByUserName(username);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject getByUserName(String username) {
        try {
            String query = new StringBuilder()
                    .append("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE username = ?")
                    .toString();
            JsonObject json = bridge.queryOne(query, username);
            if(json == null){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0, json);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject listByState(int state) {
        try {
            String query = new StringBuilder()
                    .append("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE state = ?")
                    .toString();
            JsonArray array = bridge.query(query, state);
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
    public JsonObject updateLogin(String username, JsonObject other, int state) {
        try {
            String query = new StringBuilder()
                    .append("UPDATE ")
                    .append(table)
                    .append(" SET other = ?, state = ? WHERE username = ?")
                    .toString();
            int x = bridge.update(query, other, state, username);
            if(x == 0){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
    @Override
    public JsonObject updateOther(String username, JsonObject other) {
        try {
            String query = new StringBuilder()
                    .append("UPDATE ")
                    .append(table)
                    .append(" SET other = ? WHERE username = ?")
                    .toString();
            int x = bridge.update(query, other, username);
            if(x == 0){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject updateBankInfo(String username, String account_owner, String account_number) {
        try {
            String query = new StringBuilder()
                    .append("UPDATE ")
                    .append(table)
                    .append(" SET account_owner = ?, account_number = ? WHERE username = ?")
                    .toString();
            int x = bridge.update(query, account_owner, account_number, username);
            if(x == 0){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject updateState(String username, int state) {
        try {
            String query = new StringBuilder()
                    .append("UPDATE ")
                    .append(table)
                    .append(" SET state = ? WHERE username = ?")
                    .toString();
            int x = bridge.update(query, state, username);
            if(x == 0){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
}
