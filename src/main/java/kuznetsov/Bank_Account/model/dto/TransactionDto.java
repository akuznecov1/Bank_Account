package kuznetsov.Bank_Account.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionDto {

    String name;

    BigDecimal balance;

    LocalDate time;


}
