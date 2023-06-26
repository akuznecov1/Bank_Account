package kuznetsov.Bank_Account.serviceImpl;

import kuznetsov.Bank_Account.model.entity.Transaction;
import kuznetsov.Bank_Account.repository.TransactionRepository;
import kuznetsov.Bank_Account.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;


    @Override
    public List<Transaction> getAllTransaction() {

        return transactionRepository.findAll();

    }

    @Override
    public List<Transaction> getAllTransactionForAccount(String accountFrom) {

        return transactionRepository.findAllTransactionByAccountFrom(accountFrom);

    }
}
