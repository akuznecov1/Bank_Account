package kuznetsov.Bank_Account.repository;

import kuznetsov.Bank_Account.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllTransactionByAccountFrom(String accountFrom);

}
