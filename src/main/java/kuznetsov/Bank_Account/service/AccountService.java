package kuznetsov.Bank_Account.service;

import kuznetsov.Bank_Account.model.dto.AccountDto;
import kuznetsov.Bank_Account.model.dto.OperationDto;
import kuznetsov.Bank_Account.model.entity.Account;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<Account> createAccount(AccountDto accountDto);

    List<Account> findAllAccounts();

    Optional<Account> findById(String accountNumber);

    void deposit(String accountNumber, OperationDto operationDto);

    public void withdraw(String accountNumber, OperationDto operationDto);

    public void transfer(String fromAccountNumber, String toAccountNumber, OperationDto operationDto);







}
