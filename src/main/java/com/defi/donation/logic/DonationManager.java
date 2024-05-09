package com.defi.donation.logic;

import com.defi.common.SimpleResponse;
import com.defi.donation.service.IDonationService;
import com.defi.telegram.admin.AdminMessage;
import com.defi.telegram.channel.TelegramChannelManager;
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
        String sender = DonationUtil.parsePhoneNumber(note);
        String hidden_sender = hideSender(sender);
        note = note.replace(sender, hidden_sender);
        JsonObject response = service.createDonation(receiver, sender, hidden_sender, amount, note);
        if(SimpleResponse.isSuccess(response)){
            JsonObject data = response.getAsJsonObject("d");
            AdminMessage.publish_donation(data);
        }
        return response;
    }
    private String hideSender(String sender) {
        if(sender.equals("NA")){
            return sender;
        }else{
            String sub = sender.substring(0, sender.length()-3);
            return new StringBuilder(sub).append("***").toString();
        }
    }
}
