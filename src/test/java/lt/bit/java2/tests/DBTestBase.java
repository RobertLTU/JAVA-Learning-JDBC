package lt.bit.java2.tests;

import lt.bit.java2.services.DBService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTestBase {

    @BeforeEach
    void start() throws SQLException {
//         Valdo uzduoti -> sukurti 14 employee irasus is kuriu 11'as
//         turi tureti 2 salary irasus, o 12'as turi tureti 3

        Connection connection = DBService.getConnectionFromCP();
        Statement stmt = connection.createStatement();
        stmt.execute("drop table if exists employees");
        stmt.execute(
                "CREATE TABLE employees (" +
                        " emp_no int NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        " first_name varchar(14)," +
                        " last_name varchar(16)," +
                        " gender char(1)," +
                        " birth_date date," +
                        " hire_date date" +
                        ")");
        stmt.execute(
                "INSERT INTO employees VALUES" +
                        " (1, 'A1', 'B1', 'F', '2000-01-01', '2018-03-01')," +
                        " (2, 'A2', 'B2', 'M', '2000-01-02', '2018-03-02')," +
                        " (3, 'A3', 'B3', 'F', '2000-01-03', '2018-03-03')," +
                        " (4, 'A4', 'B4', 'M', '2000-01-04', '2018-03-04')," +
                        " (5, 'A5', 'B5', 'F', '2000-01-05', '2018-03-05')," +
                        " (6, 'A6', 'B6', 'M', '2000-01-06', '2018-03-06')," +
                        " (7, 'A7', 'B7', 'F', '2000-01-07', '2018-03-07')," +
                        " (8, 'A8', 'B8', 'M', '2000-01-08', '2018-03-08')," +
                        " (9, 'A9', 'B9', 'F', '2000-01-09', '2018-03-09')," +
                        " (10, 'A10', 'B10', 'M', '2000-01-10', '2018-03-10')," +
                        " (11, 'A11', 'B11', 'F', '2000-01-11', '2018-03-11')," +
                        " (12, 'A12', 'B12', 'M', '2000-01-12', '2018-03-12')," +
                        " (13, 'A13', 'B13', 'F', '2000-01-13', '2018-03-13')," +
                        " (14, 'A14', 'B14', 'M', '2000-01-14', '2018-03-14')"

        );
        stmt.execute("drop table if exists salaries");
        stmt.execute(
                "CREATE TABLE salaries (" +
                        " emp_no int," +
                        " from_date date," +
                        " to_date date," +
                        " salary int" +
                        ")");
        stmt.execute(
                "INSERT INTO salaries VALUES" +
                        " (1, '2018-03-01', '9999-01-01', 1500)," +
                        " (3, '2018-03-03', '2018-04-01', 1000)," +
                        " (3, '2018-04-01', '9999-01-01', 2000)," +
                        " (4, '2018-03-04', '2018-05-01', 1100)," +
                        " (4, '2018-05-01', '2020-02-15', 1200)," +
                        " (4, '2020-02-15', '9999-01-01', 1300)," +
                        " (5, '2018-03-05', '9999-01-01', 1111)," +
                        " (6, '2018-03-06', '9999-01-01', 1200)," +
                        " (7, '2018-03-07', '9999-01-01', 1700)," +
                        " (8, '2018-03-08', '9999-01-01', 1800)," +
                        " (9, '2018-03-09', '9999-01-01', 1900)," +
                        " (10, '2018-03-10', '9999-01-01', 2000)," +
                        " (11, '2018-03-11', '2018-06-01', 1200)," +
                        " (11, '2018-06-01', '9999-01-01', 11000)," +
                        " (12, '2018-03-12', '2018-12-01', 1300)," +
                        " (12, '2018-12-01', '2019-09-01', 5500)," +
                        " (12, '2019-09-01', '9999-01-01', 12000)," +
                        " (13, '2019-03-13', '2019-06-01', 800)," +
                        " (13, '2019-06-01', '9999-01-01', 2600)," +
                        " (14, '2019-03-14', '9999-01-01', 7777)"
        );

        // ============== DEBUG LINES ==================
//        ResultSet resultSet1 = stmt.executeQuery("SELECT COUNT(*) FROM employees");
//        System.out.println("Total count of rows in Employees: " + resultSet1.getInt(1));
//        ResultSet resultSet2 = stmt.executeQuery("SELECT COUNT(*) FROM salaries");
//        System.out.println("Total count of rows in Salaries: " + resultSet2.getInt(1));
    }

//    @AfterEach
//    void stop() throws SQLException {
//        Connection connection = DBService.getConnectionFromCP();
//        Statement stmt = connection.createStatement();
//        stmt.execute("drop table if exists employees");
//        stmt.execute("drop table if exists salaries");
//        connection.commit();
//    }

}
