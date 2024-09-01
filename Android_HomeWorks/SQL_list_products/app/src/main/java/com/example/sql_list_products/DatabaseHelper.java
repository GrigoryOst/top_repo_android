package com.example.sql_list_products;

import android.content.Context;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    private static Connection connection;

    // Метод для получения соединения с базой данных
    public static synchronized Connection getConnection(Context context) throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Загрузка драйвера SQL Server
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Установка соединения с базой данных
                String url = "jdbc:sqlserver://your_server;databaseName=your_database";
                String user = "your_username";
                String password = "your_password";

                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException("SQL Server Driver not found.", e);
            }
        }
        return connection;
    }

    // Метод для закрытия соединения
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
