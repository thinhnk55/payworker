package com.defi.donation.logic;

import com.defi.common.SimpleResponse;
import com.defi.donation.service.IDonationService;
import com.defi.telegram.admin.AdminMessage;
import com.google.gson.JsonObject;

public class DonationManager {
    private static DonationManager ins;
    private DonationManager() {

    }

    public static DonationManager instance() {
        if (ins == null) {
            ins = new DonationManager();
        }
        return ins;
    }
    public void init(IDonationService service){
        this.service = service;
    }
    public IDonationService service;

    public JsonObject onBankDonation(JsonObject transaction){
        String receiver = transaction.get("receiver_name").getAsString();
        long amount = transaction.get("amount").getAsLong();
        String note = transaction.get("note").getAsString();
        String sender = DonationUtil.findSender(note);
        String hidden_sender = hideSender(sender);
        JsonObject response = service.createDonation(receiver, sender, hidden_sender, amount, note);
        if(SimpleResponse.isSuccess(response)){
            JsonObject data = response.getAsJsonObject("d");
            AdminMessage.publish_donation(data);
        }
        return response;
    }
    private String hideSender(String sender) {
        return sender;
    }
}
