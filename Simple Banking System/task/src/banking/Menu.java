package banking;

import java.util.Scanner;

public class Menu {
    private static boolean isRunning = true;
    private static boolean isRunningSecondMenu = true;

    private static void showMainMenuDescription() {
        System.out.println("""
                                
                1. Create an account
                2. Log into account
                0. Exit
                """);
    }

    protected static void runMainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            showMainMenuDescription();
            String currentAction = scanner.next();
            switch (currentAction) {
                case "1" -> {
                    BankCard bankCard = new BankCard();
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
                    if (BankCard.isCreditCardNumberExist(currentBankCardNumber, currentBankCardPinCode)) {
                        System.out.println("You have successfully logged in!");
                        BankCard currentBankCard = new BankCard(currentBankCardNumber, currentBankCardPinCode);
                        while (isRunningSecondMenu) {
                            showAccountMenuDescription();
                            runAccountMenu(currentBankCard);
                        }
                    } else {
                        System.out.println("Wrong card number or PIN!");
                    }
                }
                case "0" -> {
                    System.out.println("Bye!");
                    isRunning = false;
                }
            }
        }
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


}
