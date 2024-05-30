package com.defi.payworker.bank.timo.logic;

public class TimoConfig {
    public static String AAP_VERSION = "218";
    public static final int CODE_SUCCESS = 200;
    public static final int ERROR_TIMO_ACCOUNT_NOT_COMMIT = 6001;
    public static final int ERROR_UNAUTHORIZED = 401;
    public static final int ERROR_OTP_INVALID = 8102;
    public static final int ERROR_OTP_EXPIRED = 8106;

    public static final String URL_LOGIN = "https://app2.timo.vn/login";
    public static final String URL_NOTIFICATION_EN = "https://app2.timo.vn/notification/en";
    public static final String URL_NOTIFICATION_CHECK = "https://app2.timo.vn/user/notification/check";
    public static final String URL_TRANSACTION_DETAIL = "https://app2.timo.vn/user/account/transaction/receipt";
    public static final String URL_TRANSACTION_LIST = "https://app2.timo.vn/user/account/transaction/list";
    public static final String URL_BANK_INFO = "https://app2.timo.vn/user/bankinfo";
    public static final String URL_NOTIFICATION_UPDATE = "https://app2.timo.vn/notification/update"; //update notification had been read
    public static final String URL_LOGIN_COMMIT = "https://app2.timo.vn/login/commit";
    public static final String URL_FAST_TRANSFER_GET_INFO = "https://app2.timo.vn/user/fastTransfer/getInfo";
    public static final String URL_CARD_GAME = "https://app2.timo.vn/user/smartLink/cardName";
    public static final String URL_TRANSFER_ACCOUNT = "https://app2.timo.vn/user/txn/transfer/account";
    public static final String commit = "https://app2.timo.vn/user/txn/commit";
    public static final String CHECK_TYPE = "timo";
    public static final String ACCOUNT_TYPE = "1025";
    public static final String FORMAT = "list";
    public static final int INDEX = 0;
    public static final int OFFSET = -1;
}
