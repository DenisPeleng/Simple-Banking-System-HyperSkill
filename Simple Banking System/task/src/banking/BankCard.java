package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankCard {
    private static final List<String> storageOfUsedCardNumbers = new ArrayList<>();
    private long balance;
    private final String pinCode;
    private final String bankCardNumber;

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public BankCard() {
        this.pinCode = generatePin();
        String bankCard;
        do {
            String accountIdentifier = createAccountIdentifier();
            String binNumber = "400000";
            String checkSum = generateCheckSum(binNumber + accountIdentifier);
            bankCard = binNumber + accountIdentifier + checkSum;
        } while (storageOfUsedCardNumbers.contains(bankCard));
        storageOfUsedCardNumbers.add(bankCard);
        setBalance(0);
        this.bankCardNumber = bankCard;
    }

    public long getBalance() {
        return balance;
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

    private static String generateCheckSum(String currentCardNumber) {
        char[] cardNumberInChar = currentCardNumber.toCharArray();
        int sumDigits = 0;
        for (int i = 0; i < cardNumberInChar.length; i++) {
            int currentNumber = Character.getNumericValue(cardNumberInChar[i]);
            int newNumber = (i + 1) % 2 != 0 ? currentNumber * 2 : currentNumber;
            sumDigits += newNumber > 9 ? newNumber - 9 : newNumber;
        }
        return String.valueOf(sumDigits % 10 == 0 ? 0 : 10 - sumDigits % 10);
    }

    public static boolean isValidCardNumber(String bankCardNumber) {
        String currentCheckSum = bankCardNumber.substring(bankCardNumber.length() - 1);
        String correctCheckSum = generateCheckSum(bankCardNumber.substring(0, bankCardNumber.length() - 1));
        return currentCheckSum.equals(correctCheckSum);
    }
}


