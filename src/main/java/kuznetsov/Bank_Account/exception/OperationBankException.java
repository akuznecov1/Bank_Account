package kuznetsov.Bank_Account.exception;

public class OperationBankException extends RuntimeException{
    public OperationBankException(String message) {
        super(message);
    }
}
