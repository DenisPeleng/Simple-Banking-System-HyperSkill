package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class BankCard {
    private String pinCode;
    private int balance;
    private String bankCardNumber;

    public static void setDatabase(Database database) {
        BankCard.database = database;
    }

    private static Database database;


    public BankCard() {
        String bankCard;
        String accountIdentifier = createAccountIdentifier();
        String binNumber = "400000";
        String checkSum = generateCheckSum(binNumber + accountIdentifier);
        bankCard = binNumber + accountIdentifier + checkSum;
        this.bankCardNumber = bankCard;
        this.pinCode = generatePin();
        this.balance = 0;
        addNewCardToDB(bankCardNumber, pinCode);
    }

    public BankCard(String bankCardNumber, String pinCode) {
        String sqlQuery = String.format("""
                SELECT balance FROM card
                WHERE number='%s' AND pin='%s';
                """, bankCardNumber, pinCode);
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(database.getDataBaseurl());
        try (Connection con = dataSource.getConnection()) {
            Statement statementSql = con.createStatement();
            try (ResultSet resultSet = statementSql.executeQuery(sqlQuery)) {
                while (resultSet.next()) {
                    this.balance = resultSet.getInt("balance");
                    this.bankCardNumber = bankCardNumber;
                    this.pinCode = pinCode;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addNewCardToDB(String bankCardNumber, String pinCode) {
        String sqlQuery = String.format("""
                INSERT INTO card (number, pin)
                VALUES ('%s', '%s')
                ;""", bankCardNumber, pinCode);
        database.executeSql(sqlQuery);
    }


    public int getBalance() {
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

    public static boolean isCreditCardNumberExist(String bankCardNumber, String pinCode) {
        String sqlQuery = String.format("""
                SELECT * FROM card
                WHERE number='%s' AND pin='%s';
                """, bankCardNumber, pinCode);
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(database.getDataBaseurl());
        String answerFromBD = "";
        try (Connection con = dataSource.getConnection()) {
            Statement statementSql = con.createStatement();
            try (ResultSet resultSet = statementSql.executeQuery(sqlQuery)) {
                while (resultSet.next()) {
                    answerFromBD = resultSet.getString("number");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return answerFromBD.contains(bankCardNumber);
    }


}


