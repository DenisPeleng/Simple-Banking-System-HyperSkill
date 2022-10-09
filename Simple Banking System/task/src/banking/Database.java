package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public String getDataBaseurl() {
        return dataBaseurl;
    }

    String dataBaseurl;

    Database(String dataBasePath) {
        this.dataBaseurl = "jdbc:sqlite:" + dataBasePath;
        initTable();
    }

    private void initTable() {
        String sqlQueryTable = """
                CREATE TABLE IF NOT EXISTS card
                (id INTEGER PRIMARY KEY AUTOINCREMENT, number TEXT, pin TEXT, balance INTEGER DEFAULT 0);
                """;
        executeSql(sqlQueryTable);
    }

    public void executeSql(String sqlQuery) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(dataBaseurl);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statementSql = con.createStatement()) {
                statementSql.execute(sqlQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
