package kuznetsov.Bank_Account.serviceImplTest;

import kuznetsov.Bank_Account.exception.InvalidPinCodeException;
import kuznetsov.Bank_Account.exception.NotFoundException;
import kuznetsov.Bank_Account.model.dto.AccountDto;
import kuznetsov.Bank_Account.model.dto.OperationDto;
import kuznetsov.Bank_Account.model.entity.Account;
import kuznetsov.Bank_Account.model.entity.Transaction;
import kuznetsov.Bank_Account.repository.AccountRepository;
import kuznetsov.Bank_Account.repository.TransactionRepository;
import kuznetsov.Bank_Account.serviceImpl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount_withNullName_shouldThrowInvalidPinCodeException() {
        var accountDto = new AccountDto();
        accountDto.setPinCode("1234");

        assertThrows(InvalidPinCodeException.class, () -> accountService.createAccount(accountDto));
    }

    @Test
    void createAccount_withInvalidPinCode_shouldThrowInvalidPinCodeException() {

        var accountDto = new AccountDto();
        accountDto.setName("John Doe");
        accountDto.setPinCode("123");

        assertThrows(InvalidPinCodeException.class, () -> accountService.createAccount(accountDto));
    }

    @Test
    void createAccount_withValidAccountDto_shouldReturnNewAccount() {

        var accountDto = new AccountDto();
        accountDto.setName("Сашка Кузнецов");
        accountDto.setPinCode("1234");

        var result = accountService.createAccount(accountDto);

        assertTrue(result.isPresent());
        assertEquals(accountDto.getName(), result.get().getName());
        assertEquals(accountDto.getPinCode(), result.get().getPinCode());
    }

    @Test
    void findAllAccounts_shouldReturnListOfAllAccounts() {

        List<Account> accounts = new ArrayList<>();
        var account1 = new Account("1234", "Саша Кузнецов");
        var account2 = new Account("5678", "Валя Цыгановская");
        accounts.add(account1);
        accounts.add(account2);
        when(accountRepository.findAll()).thenReturn(accounts);

        var result = accountService.findAllAccounts();

        assertEquals(accounts.size(), result.size());
        assertTrue(result.contains(account1));
        assertTrue(result.contains(account2));
    }

    @Test
    void findById_withExistingAccountNumber_shouldReturnOptionalWithAccount() {
        var account = new Account("1234", "John Doe");
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));

        var result = accountService.findById("123");

        assertTrue(result.isPresent());
        assertEquals(account, result.get());
    }

    @Test
    void findById_withNonExistingAccountNumber_shouldReturnEmptyOptional() {

        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());

        var result = accountService.findById("123");

        assertTrue(result.isEmpty());
    }

    @Test
    void deposit_withExistingAccountNumberAndCorrectPinCodeAndPositiveBalance_shouldUpdateAccountAndCreateTransaction() {
        // Arrange
        var operationDto = new OperationDto();
        operationDto.setPinCode("1234");
        operationDto.setBalance(BigDecimal.valueOf(100));
        var account = new Account("1234", "John Doe");
        account.setBalance(BigDecimal.valueOf(100));
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        when(transactionRepository.save(any())).thenReturn(new Transaction());

        accountService.deposit("123", operationDto);

        assertEquals(BigDecimal.valueOf(200), account.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void deposit_withNonExistingAccountNumber_shouldThrowNotFoundException() {

        var operationDto = new OperationDto();
        operationDto.setPinCode("1234");
        operationDto.setBalance(BigDecimal.valueOf(100));
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.deposit("123", operationDto));
    }

    @Test
    void deposit_withIncorrectPinCode_shouldThrowInvalidPinCodeException() {

        var operationDto = new OperationDto();
        operationDto.setPinCode("1111");
        operationDto.setBalance(BigDecimal.valueOf(100));
        var account = new Account("1234", "John Doe");
        when(accountRepository.findAll());
    }

}