package kuznetsov.Bank_Account.controller;

import jakarta.validation.constraints.NotBlank;
import kuznetsov.Bank_Account.exception.InvalidPinCodeException;
import kuznetsov.Bank_Account.exception.NotFoundException;
import kuznetsov.Bank_Account.exception.OperationBankException;
import kuznetsov.Bank_Account.model.dto.AccountDto;
import kuznetsov.Bank_Account.model.dto.OperationDto;
import kuznetsov.Bank_Account.model.entity.Account;
import kuznetsov.Bank_Account.serviceImpl.AccountServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bank")
public class AccountController {

    private final AccountServiceImpl accountService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<AccountDto> createAccountBank(@RequestBody AccountDto accountDto) {

        Optional<Account> optionalAccount = accountService.createAccount(accountDto);

        return optionalAccount.map(account -> modelMapper.map(account, AccountDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new InvalidPinCodeException("Некорректно введены данные"));
    }

    @GetMapping("/account")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<Account> accountList = accountService.findAllAccounts();

        List<AccountDto> collect = accountList.stream().map(account -> modelMapper.map(account, AccountDto.class))
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            throw new NotFoundException("Запрос не найден");
        }

        return new ResponseEntity<>(collect, HttpStatus.OK);

    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDto> findAccountById(@PathVariable @NonNull String accountNumber) {
        Account account = accountService.findById(accountNumber).orElseThrow(() -> new NotFoundException("Номер счёта не существует"));
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        return ResponseEntity.ok(accountDto);
    }

    @PatchMapping("/{accountNumber}/deposit")
    public ResponseEntity<String> addMoney(@PathVariable @NonNull String accountNumber,
                                           @RequestBody OperationDto operationDto) {

        try {
            accountService.deposit(accountNumber, operationDto);
            return ResponseEntity.ok("Счёт пополнен");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("Номер счёта не найден");
        } catch (InvalidPinCodeException e) {
            return ResponseEntity.badRequest().body("Неверный пинкод");
        }

    }

    @PatchMapping("/{accountNumber}/withdraw")
    public ResponseEntity<String> getCash(@PathVariable @NonNull String accountNumber,
                                          @RequestBody OperationDto operationDto){

        try{
            accountService.withdraw(accountNumber, operationDto);
            return ResponseEntity.ok("Операция завершена успешно");
        } catch (InvalidPinCodeException e){
            return ResponseEntity.badRequest().body("Неверный пинкод");
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body("Проверьте номёр счёта");
        } catch (OperationBankException e){
            return ResponseEntity.badRequest().body("Недостаточно средств");
        }

    }

    @PatchMapping(value = "/{fromAccountNumber}/transfer/{toAccountNumber}")
    public ResponseEntity<String> transferMoney(@PathVariable @NotBlank String fromAccountNumber,
                                           @PathVariable @NotBlank String toAccountNumber,
                                           @RequestBody OperationDto operationDto) {

        try {
            accountService.transfer(fromAccountNumber, toAccountNumber, operationDto);
            return new ResponseEntity<>("Перевод выполнен успешно", HttpStatus.OK);

        } catch (NotFoundException e) {
            return new ResponseEntity<>("Ошибка перевода", HttpStatus.BAD_REQUEST);
        } catch (InvalidPinCodeException e) {
            return new ResponseEntity<>("Неверный пин", HttpStatus.BAD_REQUEST);
        } catch (OperationBankException e) {
            return new ResponseEntity<>("Недостаточно средств", HttpStatus.BAD_REQUEST);
        }


    }
}
