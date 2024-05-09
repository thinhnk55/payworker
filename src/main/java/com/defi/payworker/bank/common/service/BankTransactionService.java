package com.defi.payworker.bank.common.service;

import com.defi.common.SimpleResponse;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.SQLJavaBridge;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class BankTransactionService implements IBankTransactionService{
    String table;
    SQLJavaBridge bridge;
    public BankTransactionService(String table, SQLJavaBridge bridge){
        this.table = table;
        this.bridge = bridge;
        boolean exist = bridge.checkTableExisting(table);
        if(!exist){
            createTable();
        }
    }

    private void createTable() {
        String createTableSQL = ("CREATE TABLE IF NOT EXISTS table_name ("
                + "bank_code INT,"
                + "transaction_id VARCHAR(64),"
                + "receiver_account VARCHAR(32),"
                + "receiver_name VARCHAR(128),"
                + "amount INT,"
                + "note VARCHAR(256),"
                + "create_time VARCHAR(256),"
                + "state INT DEFAULT 0,"
                + "PRIMARY KEY (bank_code, transaction_id)"
                + ")").replace("table_name", table);
        String index1 = "CREATE INDEX table_name_create_time_index ON table_name (create_time ASC)"
                .replace("table_name", table);
        String index2 = "CREATE INDEX table_name_note_index ON table_name (note ASC)"
                .replace("table_name", table);
        bridge.createTable(table, createTableSQL, index1, index2);
    }
    @Override
    public JsonObject create(JsonObject transaction) {
        int bank_code = transaction.get("bank_code").getAsInt();
        String transaction_id = transaction.get("transaction_id").getAsString();
        JsonObject response = get(bank_code, transaction_id);
        if(SimpleResponse.isSuccess(response)){
            return SimpleResponse.createResponse(10);
        }
        try {
            String query = new StringBuilder()
                    .append("INSERT INTO ")
                    .append(table)
                    .append("(bank_code, transaction_id, receiver_account, receiver_name, amount, note, create_time, state) VALUES (?,?,?,?,?,?,?,?)")
                    .toString();
            String receiver_account = transaction.get("receiver_account").getAsString();
            String receiver_name = transaction.get("receiver_name").getAsString();
            long amount = transaction.get("amount").getAsLong();
            String note = transaction.get("note").getAsString();
            long create_time = transaction.get("create_time").getAsLong();
            int state = transaction.get("state").getAsInt();
            int x = bridge.update(query, bank_code, transaction_id, receiver_account, receiver_name
            , amount, note, create_time, state);
            if(x == 0){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0, transaction);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject get(int bank_code, String transaction_id) {
        try {
            String query = new StringBuilder()
                    .append("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE bank_code = ? AND transaction_id = ?")
                    .toString();
            JsonObject json = bridge.queryOne(query, bank_code, transaction_id);
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
    public JsonObject complete(JsonObject transaction) {
        try {
            int bank_code = transaction.get("bank_code").getAsInt();
            String transaction_id = transaction.get("transaction_id").getAsString();
            String query = new StringBuilder()
                    .append("UPDATE ")
                    .append(table)
                    .append(" SET state = ? WHERE bank_code = ? AND transaction_id = ?")
                    .toString();
            JsonObject json = bridge.queryOne(query, BankTransactionState.STATE_DONE,
                    bank_code, transaction_id);
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
