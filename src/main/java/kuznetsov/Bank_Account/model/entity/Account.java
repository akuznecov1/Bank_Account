package kuznetsov.Bank_Account.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    private String accountNumber;
    private String name;
    @Column(length = 4)
    private String pinCode;
    private BigDecimal balance;
    public Account(String pinCode, String name) {
        this.accountNumber = UUID.randomUUID().toString();
        this.name = name;
        this.pinCode = pinCode;
        this.balance = BigDecimal.ZERO;
    }
}
