package kuznetsov.Bank_Account.serviceImpl;

import kuznetsov.Bank_Account.exception.InvalidPinCodeException;
import kuznetsov.Bank_Account.exception.NotFoundException;
import kuznetsov.Bank_Account.exception.OperationBankException;
import kuznetsov.Bank_Account.model.dto.AccountDto;
import kuznetsov.Bank_Account.model.dto.OperationDto;
import kuznetsov.Bank_Account.model.entity.Account;
import kuznetsov.Bank_Account.model.entity.Transaction;
import kuznetsov.Bank_Account.model.enums.Operation;
import kuznetsov.Bank_Account.repository.AccountRepository;
import kuznetsov.Bank_Account.repository.TransactionRepository;
import kuznetsov.Bank_Account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    @Override
    public Optional<Account> createAccount(AccountDto accountDto) {

        if (accountDto.getName() == null || accountDto.getPinCode().length() != 4) {
            throw new InvalidPinCodeException("Некорректно введены данные");
        }

        var account = new Account(accountDto.getPinCode(), accountDto.getName());
        accountRepository.save(account);

        return Optional.of(account);
    }

    public List<Account> findAllAccounts() {

        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(String accountNumber) {

        return accountRepository.findById(accountNumber);

    }


    @Override
    public void deposit(String accountNumber, OperationDto operationDto) {

        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
        if (!optionalAccount.isPresent()) {
            throw new NotFoundException("Счёт не найден");
        }

        if (!Objects.equals(optionalAccount.get().getPinCode(), operationDto.getPinCode())) {
            throw new InvalidPinCodeException("Неверный пинкод ");
        }

        Account account = optionalAccount.orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        BigDecimal newBalance = account.getBalance().add(operationDto.getBalance());
        account.setBalance(newBalance);

        Transaction transaction = Transaction.builder()
                .accountFrom(null)
                .name(optionalAccount.get().getName())
                .accountTo(accountNumber)
                .operation(Operation.DEPOSIT)
                .balance(newBalance)
                .time(LocalDateTime.now().toString())
                .build();

        transactionRepository.save(transaction);

        accountRepository.save(account);

    }

    @Override
    public void withdraw(String accountNumber, OperationDto operationDto){

        Optional<Account> account = accountRepository.findById(accountNumber);
        if(!account.isPresent()){
            throw new NotFoundException("Счёт не найден");
        }
        if(account.get().getBalance().compareTo(operationDto.getBalance()) < 0){
            throw new OperationBankException("Проверьте баланс");
        }

        if (!Objects.equals(account.get().getPinCode(), operationDto.getPinCode())) {
            throw new InvalidPinCodeException("Неверный пинкод ");
        }

        BigDecimal newBalance = account.get().getBalance().subtract(operationDto.getBalance());

        account.get().setBalance(newBalance);

        Transaction transaction = Transaction.builder()
                .accountFrom(accountNumber)
                .name(account.get().getName())
                .balance(newBalance)
                .time(LocalDateTime.now().toString())
                .operation(Operation.WITHDRAW)
                .build();

        accountRepository.save(account.get());

        transactionRepository.save(transaction);

    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transfer(String fromAccountNumber, String toAccountNumber, OperationDto operationDto) {

         Optional<Account> accountFrom = accountRepository.findById(fromAccountNumber);

        Optional<Account> accountTo = accountRepository.findById(toAccountNumber);

        if(!(accountFrom.isPresent() || accountTo.isPresent())){
            throw new NotFoundException("Проверьте данные");
        }
        if(!Objects.equals(accountFrom.get().getPinCode(), operationDto.getPinCode())){

            throw new InvalidPinCodeException("Неверный пинкод");
        }

        if(accountFrom.get().getBalance().compareTo(operationDto.getBalance()) < 0){
            throw new OperationBankException("Проверьте баланс");
        }

        accountFrom.get().setBalance(accountFrom.get().getBalance().subtract(operationDto.getBalance()));

        accountTo.get().setBalance(accountTo.get().getBalance().add(operationDto.getBalance()));

        Transaction transaction = Transaction.builder()
                .accountFrom(fromAccountNumber)
                .accountTo(toAccountNumber)
                .name(accountFrom.get().getName())
                .operation(Operation.TRANSFER)
                .time(LocalDateTime.now().toString())
                .balance(operationDto.getBalance())
                .build();

        transactionRepository.save(transaction);

        accountRepository.save(accountFrom.get());

        accountRepository.save(accountTo.get());


    }

}

