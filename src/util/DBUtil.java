package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static String url="jdbc:mysql://127.0.0.1:3306/cart?characterEncoding=UTF-8";
    private static String username = "root";
    private static String password = "123456";
    private static String Driver = "com.mysql.jdbc.Driver";
    static{
        try {
            Class.forName(Driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }
}
