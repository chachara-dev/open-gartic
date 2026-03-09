package com.opengartic;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final Dotenv dotenv = Dotenv.configure()
        .directory("../")
        .load();

    private static final String URL = "jdbc:postgresql://" 
        + dotenv.get("DB_HOST") + ":"
        + dotenv.get("DB_PORT") + "/"
        + dotenv.get("DB_NAME");

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(
                URL,
                dotenv.get("DB_USER"),
                dotenv.get("DB_PASSWORD")
            );
            System.out.println("✅ Conectado a PostgreSQL AWS!");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return null;
        }
    }
}