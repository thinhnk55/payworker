package com.defi.donation.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DonationUtil {
    public static String parsePhoneNumber(String text){
        Pattern pattern = Pattern.compile("\\b0\\d{9}\\b");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String phoneNumber = matcher.group();
            return phoneNumber;
        }
        return "NA";
    }
}
