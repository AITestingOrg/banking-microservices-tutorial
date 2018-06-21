package com.ultimatesoftware.banking.account.common.events;

public class TransactionFailedEvent {
    private String id;
    private String msg;

    public TransactionFailedEvent(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }
}
