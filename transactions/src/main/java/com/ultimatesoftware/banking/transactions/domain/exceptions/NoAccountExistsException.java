package ultimatesoftware.banking.transactions.domain.exceptions;

public class NoAccountExistsException extends Exception {
    public NoAccountExistsException(String msg) {
        super(msg);
    }
}
