package ultimatesoftware.banking.transactions.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Created by justinp on 7/21/17.
 */
public class BankTransaction {
    @Id
    private String id;
    private TransactionType type;
    private String account;
    private double amount;
    private String destinationAccount;

    public BankTransaction(String id, TransactionType type, String account, double amount, String destinationAccount) {
        this.id = id;
        this.type = type;
        this.account = account;
        this.amount = amount;
        this.destinationAccount = destinationAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
