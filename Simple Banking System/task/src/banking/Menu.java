package banking;

import java.util.Scanner;

public class Menu {
    private static boolean isRunning;
    private static boolean isRunningSecondMenu;

    private static void showMainMenuDescription() {
        System.out.println("""
                                
                1. Create an account
                2. Log into account
                0. Exit
                """);
    }

    protected static void runMainMenu() {
        Scanner scanner = new Scanner(System.in);
        isRunning = true;
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
                    if (BankCard.isNotValidCardNumber(currentBankCardNumber)) {
                        System.out.println("Wrong card number or PIN!");
                        break;
                    }
                    if (BankCard.isCreditCardAndPinValid(currentBankCardNumber, currentBankCardPinCode)) {
                        System.out.println("You have successfully logged in!");
                        isRunningSecondMenu = true;
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
                System.out.println("Enter income:");
                int income = scanner.nextInt();
                currentBankCard.addIncome(income);
                System.out.println("Income was added!");
            }
            case "3" -> {
                System.out.println("Transfer");
                System.out.println("Enter card number:");
                String cardNumberOfReceiver = scanner.next();
                if (BankCard.isNotValidCardNumber(cardNumberOfReceiver)) {
                    System.out.println("Probably you made a mistake in the card number. Please try again!");
                    break;
                }
                if (!BankCard.isCreditCardExist(cardNumberOfReceiver)) {
                    System.out.println("Such a card does not exist.");
                    break;
                }
                if (currentBankCard.getBankCardNumber().contains(cardNumberOfReceiver)) {
                    System.out.println("You can't transfer money to the same account!");
                    break;
                }
                System.out.println("Enter how much money you want to transfer:");
                int amountOfTransfer = scanner.nextInt();
                if (amountOfTransfer > currentBankCard.getBalance()) {
                    System.out.println("Not enough money!");
                    break;
                }
                currentBankCard.doTransfer(cardNumberOfReceiver, amountOfTransfer);
                System.out.println("Success!");
            }
            case "4" -> {
                BankCard.closeAccount(currentBankCard);
                System.out.println("The account has been closed!");
                isRunningSecondMenu = false;
            }
            case "5" -> {
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
                2. Add income
                3. Do transfer
                4. Close account
                5. Log out
                0. Exit
                """);
    }


}
