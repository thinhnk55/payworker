package com.defi.payworker.bank.common.service;

import com.google.gson.JsonObject;

public interface IBankTransactionService {
    JsonObject create(JsonObject transaction);
    JsonObject get(int bank_code, String transaction_id);
    JsonObject complete(JsonObject transaction);
}
