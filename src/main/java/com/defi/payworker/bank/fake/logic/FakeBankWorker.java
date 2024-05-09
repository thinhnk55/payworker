package com.defi.payworker.bank.fake.logic;

import com.defi.common.SimpleResponse;
import com.defi.payworker.bank.common.logic.BankManager;
import com.defi.payworker.bank.common.service.BankCode;
import com.defi.payworker.bank.common.service.BankTransactionState;
import com.defi.util.string.StringUtil;
import com.google.gson.JsonObject;

public class FakeBankWorker {
    private static FakeBankWorker ins;
    private FakeBankWorker() {

    }

    public static FakeBankWorker instance() {
        if (ins == null) {
            ins = new FakeBankWorker();
        }
        return ins;
    }

    public JsonObject onFakeTransaction(String receiver_account, String receiver_name, long amount, String note){
        JsonObject transaction = new JsonObject();
        transaction.addProperty("bank_code", BankCode.FAKE_BANK);
        transaction.addProperty("receiver_account", receiver_account);
        transaction.addProperty("receiver_name", receiver_name);
        transaction.addProperty("amount", amount);
        transaction.addProperty("note", note);
        String transaction_id = StringUtil.generateUUID();
        transaction.addProperty("transaction_id", transaction_id);
        transaction.addProperty("create_time", System.currentTimeMillis());
        transaction.addProperty("state", BankTransactionState.STATE_WAITING);
        BankManager.instance().onBankTransaction(transaction);
        return SimpleResponse.createResponse(0, transaction);
    }
}
