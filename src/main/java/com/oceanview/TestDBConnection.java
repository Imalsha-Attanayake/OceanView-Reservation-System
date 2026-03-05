package com.oceanview;

import com.oceanview.util.DBConnection;
import java.sql.Connection;

public class TestDBConnection {

    public static void main(String[] args) {

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("MySQL connection successful!");
        } else {
            System.out.println("Database connection failed!");
        }
    }
}