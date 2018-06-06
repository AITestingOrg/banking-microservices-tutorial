package ultimatesoftware.banking.transactions.models;

public class BankAccount {
    private  String id;
    private double balance;
    private String customerId;

    public BankAccount() {

    }

    public BankAccount(String id, double balance, String customerId) {
        this.id = id;
        this.balance = balance;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
