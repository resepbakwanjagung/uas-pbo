package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties props = loadProperties();

    private static Properties loadProperties() {
        Properties p = new Properties();
        try (InputStream in = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("config/database.properties")) {
            if (in == null) {
                throw new RuntimeException("File config/database.properties tidak ditemukan di classpath");
            }
            p.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Gagal membaca database.properties: " + e.getMessage(), e);
        }
        return p;
    }

    public static Connection getConnection() throws SQLException {
        String url      = props.getProperty("db.url");
        String user     = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }
}