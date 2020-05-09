package lt.bit.java2.services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lt.bit.java2.DemoEmployees;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBService {

    static private Properties properties;
    static {
        properties = new Properties();
        try {
//            OutputStream os = new FileOutputStream("blabla.txt");
//            os.write("Kuku :)".getBytes());
//            os.close();


            // failas 'application.properties' ieskomas:

            // 1) nurodomas tik failo vardas - tai failas ieskomas einamajame kataloge
            // InputStream is = new FileInputStream("application.properties");

            // 2) nurodomas santykinis failo kelias - tai failas ieskomas einamojo katalogo atzvilgiu
            // InputStream is = new FileInputStream("abc/def/application.properties");

            // 3) nurodomas absoliutus failo kelias - tai failas ieskomas tiksliai tame katalogeu
            // InputStream is = new FileInputStream("/user/Petras/abc/def/application.properties");


            // DemoEmployees.class.getClassLoader() zino kad klases failas yra kataloge:
            // 'lt/bit/java2/DemoEmployees.class'
            // t.y. klases laoderio einamasis katalogas yra katalogas kuriame yra
            // 'lt' subkatalogas


            InputStream is;
            try {
                is = new BufferedInputStream(new FileInputStream("application.properties"));
            } catch (FileNotFoundException e) {
                is = DemoEmployees.class.getClassLoader().getResourceAsStream("application.properties");
            }

//            InputStream is = DemoEmployees.class.getClassLoader().getResourceAsStream("application.properties");

            properties.load(is);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static DataSource dataSource;
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.user"));
        config.setPassword(properties.getProperty("db.password"));
        String driver = properties.getProperty("db.driver");
        if (driver != null) {
            config.setDriverClassName(driver);
        }

        dataSource = new HikariDataSource(config);
    }


    public static Connection getConnectionFromCP() throws SQLException {
        return dataSource.getConnection();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password"));
    }

    public static Connection getConnectionH2Embedded() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url.H2Embedded"),
                properties.getProperty("db.user.H2Embedded"),
                properties.getProperty("db.password.H2Embedded"));
    }

    public static Connection getConnectionSQLiteEmbedded() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite::memory:");
    }
}
