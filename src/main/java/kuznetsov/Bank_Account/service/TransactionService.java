package kuznetsov.Bank_Account.service;

import kuznetsov.Bank_Account.model.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransaction();

    List<Transaction> getAllTransactionForAccount(String accountFrom);

}
