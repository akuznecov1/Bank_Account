package kuznetsov.Bank_Account.controller;


import kuznetsov.Bank_Account.exception.NotFoundException;
import kuznetsov.Bank_Account.model.dto.TransactionDto;
import kuznetsov.Bank_Account.model.entity.Transaction;
import kuznetsov.Bank_Account.serviceImpl.TransactionServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bank/transaction")
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    private final ModelMapper modelMapper;

   @GetMapping("/all")
    public ResponseEntity<List<TransactionDto>> getAll(){

        List<Transaction> allTransactions = transactionService.getAllTransaction();

        List<TransactionDto> collect = allTransactions.stream().map(transaction ->
                modelMapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());

        if(collect.isEmpty()){
           throw new NotFoundException("Транзакций не найдено");
        }
        return new ResponseEntity<>(collect, HttpStatus.OK);

    }

    @GetMapping("/{accountFrom}")
    public ResponseEntity<List<TransactionDto>> getTransactionByAccountNumber(@PathVariable @NonNull String accountFrom){

       List<Transaction> allTransactions = transactionService.getAllTransactionForAccount(accountFrom);

       List<TransactionDto> collect = allTransactions.stream().map(transaction ->
               modelMapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());

       if(collect.isEmpty()){
           throw new NotFoundException("Запрос не верный");
       }
       return new ResponseEntity<>(collect, HttpStatus.OK);
    }
}
