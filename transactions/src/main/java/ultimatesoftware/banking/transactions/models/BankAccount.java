package ultimatesoftware.banking.transactions.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Created by justinp on 7/21/17.
 */
public class BankAccount {
    @Id
    private String id;
    private double balance;

    public BankAccount(String id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
