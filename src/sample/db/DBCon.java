package sample.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCon {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://85.82.241.232:3306/";
    private static final String DB_NAME = "bindia";
    private static final String USER = "bindia";
    private static final String PASS = "1234";
    private static Connection conn;

    public static Connection getConn()
    {
        if (conn != null)
        {
            return conn;
        }

        try
        {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL + DB_NAME, USER, PASS);
            return conn;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void close()
    {
        try
        {
            conn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
