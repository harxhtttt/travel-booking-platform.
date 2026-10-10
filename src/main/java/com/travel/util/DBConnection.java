package com.travel.util;

import com.travel.exception.DataAccessException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/** Handles JDBC connections reading from db.properties. */
public class DBConnection {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new DataAccessException("db.properties not found in classpath.");
            }
            props.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new DataAccessException("Failed to initialize database connection configuration", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(props.getProperty("db.url"), props.getProperty("db.username"), props.getProperty("db.password"));
        } catch (Exception e) {
            throw new DataAccessException("Could not establish database connection", e);
        }
    }
}
