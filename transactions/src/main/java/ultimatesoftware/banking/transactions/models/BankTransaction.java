package ultimatesoftware.banking.transactions.models;

import org.springframework.data.annotation.Id;

public class BankTransaction {
    @Id
    private String id;
    private TransactionType type;
    private String account;
    private String customerId;
    private double amount;
    private String destinationAccount;

    public BankTransaction() {}

    public BankTransaction(String id, TransactionType type, String customerId, String account, double amount, String destinationAccount) {
        this.id = id;
        this.type = type;
        this.account = account;
        this.customerId = customerId;
        this.amount = amount;
        this.destinationAccount = destinationAccount;
    }

    public BankTransaction(String id, TransactionType type, String account, double amount) {
        this.id = id;
        this.type = type;
        this.account = account;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getAccount() {
        return account;
    }

    public double getAmount() {
        return amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public static class BankTransactionBuilder {
        private String id;
        private TransactionType type;
        private String account;
        private double amount;
        private String destinationAccount;
        private String customerId;

        public BankTransactionBuilder setType(TransactionType type) {
            this.type = type;
            return this;
        }

        public BankTransactionBuilder setCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public BankTransactionBuilder setAccount(String account) {
            this.account = account;
            return this;
        }

        public BankTransactionBuilder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public BankTransactionBuilder setDestinationAccount(String destinationAccount) {
            this.destinationAccount = destinationAccount;
            return this;
        }

        public BankTransaction build() {
            return new BankTransaction(id, type, customerId, account, amount, destinationAccount);
        }
    }
}
