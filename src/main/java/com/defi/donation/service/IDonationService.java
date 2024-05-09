package com.defi.donation.service;

import com.google.gson.JsonObject;

public interface IDonationService {
    JsonObject createDonation(String receiver, String sender, String hidden_sender, long amount, String message);
    JsonObject listDonation(long limit, long offset);
    JsonObject topDonation(long from_timestamp, long limit);
}
