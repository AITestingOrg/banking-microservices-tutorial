package ultimatesoftware.banking.accounts.models;

import org.springframework.data.annotation.Id;

public class Account {

    @Id
    public String id;
    public String customerId;
    public float balance;

    public Account(){}

    public Account(String customerId, float balance){
        this.customerId = customerId;
        this.balance = balance;
    }
}
