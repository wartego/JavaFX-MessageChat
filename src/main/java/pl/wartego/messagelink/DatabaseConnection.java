package pl.wartego.messagelink;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {
    public static Connection databaseLink;
    public static Connection getConnection() throws IOException {
        if (databaseLink == null) {
            Properties properties = new Properties();
            ClassLoader classLoader = DatabaseConnection.class.getClassLoader(); // tu bylo this.getClass()....
            try (InputStream stream = classLoader.getResourceAsStream("database.properties")) {
                properties.load(stream);
            }
            String databaseDriver = properties.getProperty("jdbc.driver.class.name");
            String url = properties.getProperty("jdbc.url");
            String databaseUserName = properties.getProperty("db.username");
            String databasePassword = properties.getProperty("db.password");

            try {
                Class.forName(databaseDriver);
                databaseLink = DriverManager.getConnection(url, databaseUserName, databasePassword);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return databaseLink;
    }
}
