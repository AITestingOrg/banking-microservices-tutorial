package ultimatesoftware.banking.transactions.services;

import ultimatesoftware.banking.transactions.models.BankAccount;

import java.util.HashMap;

/**
 * Created by justinp on 7/21/17.
 */
public class TransactionService extends Service {
    private final static String BANK_ACCOUNT_SERVICE = "bank-accounts";
    private final static String BANK_ACCOUNT_GET_PATH = "/api/account/";
    @Override
    protected BankAccount getAccount(String ownerId, String accountId) {
        HashMap<String, String> params = new HashMap();
        params.put("owner", ownerId);
        return request(BANK_ACCOUNT_SERVICE, BANK_ACCOUNT_GET_PATH + accountId, params, BankAccount.class);
    }
}
