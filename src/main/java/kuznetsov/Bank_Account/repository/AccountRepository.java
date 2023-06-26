package kuznetsov.Bank_Account.repository;

import kuznetsov.Bank_Account.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Override
    Optional<Account> findById(String s);


}
