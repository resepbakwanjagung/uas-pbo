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
                // Alternatif pembacaan jika tidak ditemukan di classpath (misalnya jika dijalankan langsung di beberapa IDE)
                try (InputStream altIn = new java.io.FileInputStream("src/config/database.properties")) {
                    p.load(altIn);
                    return p;
                } catch (Exception ex) {
                    throw new RuntimeException("File config/database.properties tidak ditemukan di classpath maupun path lokal");
                }
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
