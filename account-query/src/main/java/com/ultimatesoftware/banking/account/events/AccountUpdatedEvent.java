package com.ultimatesoftware.banking.account.events;

public class AccountUpdatedEvent extends AccountEvent {
    private String customerId;

    public AccountUpdatedEvent(String id, String customerId) {
        super(id);
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
