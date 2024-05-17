package com.defi.payworker.bank.timo.service;

import com.google.gson.JsonObject;

public interface ITimoAccountervice {
    JsonObject create(String username, String password);
    JsonObject getByUserName(String username);
    JsonObject listByState(int state);

    JsonObject updateLogin(String username, JsonObject other, int state);
    JsonObject updateOther(String username, JsonObject other);

    JsonObject updateBankInfo(String username, String accountOwner, String accountNumber);

    JsonObject updateState(String username, int stateNotCommit);
}
