package online_exam;
import java.sql.*;

public class DBConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/online_exam",
                    "root",
                    "pallu@15"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
