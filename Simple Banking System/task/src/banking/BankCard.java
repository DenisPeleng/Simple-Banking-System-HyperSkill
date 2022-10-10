package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Random;

public class BankCard {
    private final String pinCode;
    private int balance;
    private final String bankCardNumber;

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
        this.bankCardNumber = bankCardNumber;
        this.pinCode = pinCode;
        updateBalanceFromDB();
    }

    private void updateBalanceFromDB() {
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
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isCreditCardExist(String bankCardNumber) {
        String sqlQuery = String.format("""
                SELECT * FROM card
                WHERE number='%s';
                """, bankCardNumber);
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

    public static boolean isNotValidCardNumber(String bankCardNumber) {
        if (bankCardNumber.length() != 16) {
            return false;
        }
        String currentCheckSum = bankCardNumber.substring(bankCardNumber.length() - 1);
        String correctCheckSum = generateCheckSum(bankCardNumber.substring(0, bankCardNumber.length() - 1));
        return !currentCheckSum.equals(correctCheckSum);
    }

    public static boolean isCreditCardAndPinValid(String bankCardNumber, String pinCode) {
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

    public void addIncome(int income) {

        String sqlQuery = String.format("""
                UPDATE card
                SET balance=balance+'%d'
                WHERE number='%s' AND pin='%s';
                """, income, bankCardNumber, pinCode);
        database.executeSql(sqlQuery);
        updateBalanceFromDB();
    }

    public static void closeAccount(BankCard currentBankCard) {
        String sqlQuery = String.format("""
                DELETE FROM card
                WHERE number='%s' AND pin='%s';
                """, currentBankCard.getBankCardNumber(), currentBankCard.getPinCode());
        database.executeSql(sqlQuery);
    }

    public void doTransfer(String cardNumberOfReceiver, int amountOfIncome) {
        String sqlQueryWithdrawMoney = """
                UPDATE card
                SET balance = balance-?
                WHERE number = ? AND pin = ?;
                """;
        String sqlQueryAddMoney = """
                UPDATE card
                SET balance = balance+?
                WHERE number = ?;
                """;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(database.getDataBaseurl());

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement insertInvoice = con.prepareStatement(sqlQueryWithdrawMoney);
                 PreparedStatement insertOrder = con.prepareStatement(sqlQueryAddMoney)) {

                con.setSavepoint();
                insertInvoice.setInt(1, amountOfIncome);
                insertInvoice.setString(2, bankCardNumber);
                insertInvoice.setString(3, pinCode);
                insertInvoice.executeUpdate();

                insertOrder.setInt(1, amountOfIncome);
                insertOrder.setString(2, cardNumberOfReceiver);
                insertOrder.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                try {
                    System.err.print("Transaction is being rolled back");
                    con.rollback();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
        updateBalanceFromDB();
    }


}


