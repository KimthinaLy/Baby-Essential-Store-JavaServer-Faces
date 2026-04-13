/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Admin
 */
public class DBUtil {

    private static final String URL = System.getenv("DB_URL");
    private static final String USERNAME = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Just for testing!
            System.out.println("--- DB CONFIG CHECK ---");
            System.out.println("User: " + System.getenv("DB_USERNAME"));
            System.out.println("Pass: " + System.getenv("DB_PASSWORD"));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing DBUtil: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
