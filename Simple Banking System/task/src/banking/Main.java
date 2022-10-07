package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static boolean isRunning = true;
    private static boolean isRunningSecondMenu = true;
    private static final List<BankCard> storageBankCards = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            showMainMenuDescription();
            String currentAction = scanner.next();
            switch (currentAction) {
                case "1" -> {
                    BankCard bankCard = new BankCard();
                    storageBankCards.add(bankCard);
                    System.out.println("Your card has been created");
                    System.out.println("Your card number:");
                    System.out.println(bankCard.getBankCardNumber());
                    System.out.println("Your card PIN:");
                    System.out.println(bankCard.getPinCode());
                }
                case "2" -> {
                    System.out.println("Enter your card number:");
                    String currentBankCardNumber = scanner.next();
                    System.out.println("Enter your PIN:");
                    String currentBankCardPinCode = scanner.next();
                    if (!BankCard.isValidCardNumber(currentBankCardNumber)) {
                        System.out.println("Wrong card number or PIN!");
                        break;
                    }
                    BankCard currentBankCard = searchBankCard(currentBankCardNumber, currentBankCardPinCode);
                    boolean isCorrectBankCard = currentBankCard != null;
                    if (isCorrectBankCard) {
                        System.out.println("You have successfully logged in!");

                        while (isRunningSecondMenu) {
                            showAccountMenuDescription();
                            runAccountMenu(currentBankCard);
                        }
                    }
                }
                case "0" -> {
                    System.out.println("Bye!");
                    isRunning = false;
                }
            }
        }
    }

    private static void showMainMenuDescription() {
        System.out.println("""
                                
                1. Create an account
                2. Log into account
                0. Exit
                """);
    }

    private static void runAccountMenu(BankCard currentBankCard) {
        Scanner scanner = new Scanner(System.in);
        String currentAction = scanner.next();
        switch (currentAction) {
            case "1" -> System.out.printf("Balance: %d%n", currentBankCard.getBalance());
            case "2" -> {
                System.out.println("You have successfully logged out!");
                isRunningSecondMenu = false;
            }
            case "0" -> {
                isRunningSecondMenu = false;
                isRunning = false;
                System.out.println("Bye!");
            }
        }
    }

    private static void showAccountMenuDescription() {
        System.out.println("""
                                
                1. Balance
                2. Log out
                0. Exit
                """);
    }

    private static BankCard searchBankCard(String currentBankCardNumber, String pinCode) {
        BankCard correctBankCard = null;
        for (BankCard currentBankCard : storageBankCards
        ) {
            if (currentBankCard.getBankCardNumber().contains(currentBankCardNumber)) {
                if (currentBankCard.getPinCode().contains(pinCode)) {
                    correctBankCard = currentBankCard;
                } else {
                    System.out.println("Wrong card number or PIN!");
                }
            } else {
                System.out.println("Wrong card number or PIN!");
            }
        }
        return correctBankCard;
    }

}