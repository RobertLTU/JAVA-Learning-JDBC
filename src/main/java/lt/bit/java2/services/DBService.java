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
    //Deklaruojam static kintamaji objektui kuris implementuoja DataSource interface
    private static DataSource dataSource;

    //Paleidziam static initialization block kuris sukuria ir priskiria DataSource objekta.
    static {
        //Sukuriamas objektas kuriame yra hikari konfiguraciniai laukai
        HikariConfig config = new HikariConfig();

        //Uzduodam reiksmes butiniems Hikari konfiguracijos laukams
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.user"));
        config.setPassword(properties.getProperty("db.password"));

        //DBService klaseje sukuriam kintamaji kuriame saugomas db.draiver pavadinimas
        String driver = properties.getProperty("db.driver");

        // Jeigu is properties pavyksta gauti db.driver reiksme (t.y. ji nelygi null)
        // Tada perduodam driver reiksme i Hikari Config objekta.
        if (driver != null) {
            config.setDriverClassName(driver);
        }
        //Priskiriam datasource kintamajam naujai sukurta objekta HikariDataSource
        //Kuris sukuriamas pagal Hikari konfiguracini objekta, kuri priima kaip parametra
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
}
