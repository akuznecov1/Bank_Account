package kuznetsov.Bank_Account.model.entity;

import jakarta.persistence.*;
import kuznetsov.Bank_Account.model.enums.Operation;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name="transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String time;
    @Column(columnDefinition = "numeric")
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private Operation operation;

    private String name;

    private String accountFrom;

    private String accountTo;

}
