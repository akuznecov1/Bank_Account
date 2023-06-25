package kuznetsov.Bank_Account.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OperationDto {
    @Size(min = 4, max = 4)
    String pinCode;

    BigDecimal balance;
}
