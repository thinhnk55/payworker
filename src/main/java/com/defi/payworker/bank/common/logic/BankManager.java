package com.defi.payworker.bank.common.logic;

import com.defi.common.SimpleResponse;
import com.defi.donation.logic.DonationManager;
import com.defi.payworker.bank.common.service.IBankTransactionService;
import com.google.gson.JsonObject;

public class BankManager {
    private static BankManager ins;
    private BankManager() {

    }

    public static BankManager instance() {
        if (ins == null) {
            ins = new BankManager();
        }
        return ins;
    }
    public void init(IBankTransactionService service){
        this.service = service;
    }
    public IBankTransactionService service;

    public void onBankTransaction(JsonObject transaction){
        long amount = transaction.get("amount").getAsLong();
        if(amount > 0) {
            JsonObject response = service.create(transaction);
            if (SimpleResponse.isSuccess(response)) {
                response = DonationManager.instance().onBankDonation(transaction);
                if (SimpleResponse.isSuccess(response)) {
                    service.complete(transaction);
                }
            }
        }
    }
}
