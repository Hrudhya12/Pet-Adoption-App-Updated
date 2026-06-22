package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/pet_adoption";
    private static final String USER = "root";
    private static final String PASSWORD = "Maria@2004";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("DB Connection failed: " + e.getMessage());
            return null;
        }
    }
}
