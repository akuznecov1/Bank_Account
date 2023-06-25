package kuznetsov.Bank_Account;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class BankAccountApplication {



	public static void main(String[] args) {
		SpringApplication.run(BankAccountApplication.class, args);
	}

}
