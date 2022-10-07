package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankCard {
    private static List<String> storageOfUsedCardNumbers = new ArrayList<>();
    private String iban = "400000";
    private String checkSum = "9";
    private String accountIdentifier;

    public long getBalance() {
        return balance;
    }

    private long balance;
    private final String pinCode;
    private final String bankCardNumber;

    public BankCard() {
        this.pinCode = generatePin();
        String bankCard;
        do {
            this.accountIdentifier = createAccountIdentifier();
            bankCard = iban + accountIdentifier + checkSum;
        } while (storageOfUsedCardNumbers.contains(bankCard));
        storageOfUsedCardNumbers.add(bankCard);
        this.balance = 0;
        this.bankCardNumber = bankCard;
    }

    private String createAccountIdentifier() {
        Random random = new Random();
        StringBuilder generatedAccountNumber = new StringBuilder();
        while (generatedAccountNumber.length() < 9) {
            generatedAccountNumber.append(random.nextInt(10));
        }
        return generatedAccountNumber.toString();

    }

    private String generatePin() {
        Random random = new Random();
        StringBuilder generatedPinCode = new StringBuilder();
        while (generatedPinCode.length() < 4) {
            generatedPinCode.append(random.nextInt(10));
        }
        return generatedPinCode.toString();
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public String getPinCode() {
        return pinCode;
    }
}


