package banking;

public class Main {
    public static void main(String[] args) {
        String dataBasePath = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-fileName")) {
                dataBasePath = args[i + 1];
            }
        }
        Database database = new Database(dataBasePath);
        BankCard.setDatabase(database);
                Menu.runMainMenu();

    }


}