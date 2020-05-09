package lt.bit.java2;

import com.google.gson.Gson;
import lt.bit.java2.model.Employee;
import lt.bit.java2.model.Salary;
import lt.bit.java2.services.DBService;
import lt.bit.java2.services.EmployeeMap;
import lt.bit.java2.services.EmployeeService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemoEmployees {
    /*
        SELECT * FROM employees LIMIT 10

        1. Gauti 'Connection' - prisjungimo sesija
        2. Gauti 'Statement' - is connection mes turime gauti statement
            kurio pagelba vykdome konkrecius SQL kalbos sakinius
        3. Ir kaip vykdymo rezultata gauname 'ResultSet'
     */
    public static void main(String[] args) {
        List<Employee> employeeList = EmployeeService.loadEmployees(0, 100);

        }
//        List<Employee> testList = EmployeeService.loadEmployees(2,100);

//        Properties properties = new Properties();
//        try {
//            // failas 'application.properties' ieskomas projekto katalogo virsuje,
//            // t.y. kataloge is kurio paleidziama programa
//            // InputStream is = new FileInputStream("application.properties");
//
//            InputStream is = DemoEmployees.class.getClassLoader().getResourceAsStream("application.properties");
//            properties.load(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//
//        try (
//                Connection conn = DriverManager.getConnection(
//                    properties.getProperty("db.url"),
//                        properties.getProperty("db.user"),
//                        properties.getProperty("db.password"));
//                Statement statement = conn.createStatement();
//                ResultSet resultSet = statement.executeQuery(
//                        "SELECT * FROM employees LIMIT 10");
//        ) {
//
//            while (resultSet.next()) {
//                int empNo = resultSet.getInt("emp_no");
//                System.out.println(
//                        empNo + " " +
//                        resultSet.getString("first_name") + " " +
//                        resultSet.getString("last_name") + " " +
//                        resultSet.getDate("hire_date")
//                        );
//                printSalaryByEmp(empNo, 100);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        updateEmployee(10001, "Petras", "Petraitis");
//        updateEmployee(10000, "A", "A");


//        Employee employee = new Employee();
//        employee.setFirstName("Antanas sdsdsdsssdsdsdsdsdsdsddsdsdsdsdsdsdsdsd");
//        employee.setLastName("Birutis");
//        employee.setGender("M");
////        employee.setBirthDate(Date.valueOf(LocalDate.of(1990, 1, 1)));
////        employee.setHireDate(Date.valueOf(LocalDate.of(2020, 1, 15)));
//        employee.setBirthDate(LocalDate.of(1990, 1, 1));
//        employee.setHireDate(LocalDate.of(2020, 1, 15));
//        insertEmployee(employee);


//        getSalariesByEmp(10001, 100);

//        long start = System.currentTimeMillis();
//        long count = 0;
//        for (int i = 0; i < 1000; i++) {
//            if (i % 10 == 0) System.out.println(i);
//            List<Employee> employees = getEmployees();
//            count += employees.size();
//        }
//        long time = System.currentTimeMillis() - start;
//        System.out.println("Viso: " + count + " per " + time + "ms");




    static List<Employee> getEmployees() {
        try (Connection conn = DBService.getConnectionFromCP();  // 1 zingsnis
             PreparedStatement statement = conn.prepareStatement(
                        "SELECT * FROM employees LIMIT 100"))
        {
            try (ResultSet resultSet = statement.executeQuery()) { // 2 zingsnis
                List<Employee> employees = new ArrayList<>();
                while (resultSet.next()) {
                    employees.add(EmployeeMap.fromResultSet(resultSet));   // 3 zingsnis
                }
                return employees;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void getSalariesByEmp(int empNo, int limit) {
        try (
                Connection conn = DBService.getConnection();  // 1 zingsnis
                PreparedStatement statement = conn.prepareStatement(
                        "SELECT * FROM salaries WHERE emp_no = ? LIMIT ?")
                )
        {

            statement.setInt(1, empNo);
            statement.setInt(2, limit);

            try (ResultSet resultSet = statement.executeQuery()) { // 2 zingsnis

                while (resultSet.next()) {
                    System.out.println(" -> " +
                            resultSet.getInt("emp_no") + " " +
                            resultSet.getInt("salary") + " " +
                            resultSet.getDate("to_date") + " " +
                            resultSet.getDate("from_date")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void updateEmployee(int empNo, String firstName, String lastName) {
        try (
                Connection conn = DBService.getConnection();
                PreparedStatement statement = conn.prepareStatement(
                        "UPDATE employees SET first_name=?, last_name=? WHERE emp_no=?")
        ) {

            statement.setInt(3, empNo);
            statement.setString(1, firstName);
            statement.setString(2, lastName);

            int updated = statement.executeUpdate();
            if (updated != 1) {
                System.err.println("Kazkas negerai!!!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void insertEmployee(Employee employee) {
        try (
                Connection conn = DBService.getConnection();
                PreparedStatement statement = conn.prepareStatement(
                        "INSERT employees(first_name, last_name, gender, birth_date, hire_date) VALUES(?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)
        ) {

            try {
                // paleidziame tranzakcija, t.y. START TRANSACTION
                conn.setAutoCommit(false);

                statement.setString(1, employee.getFirstName());
                statement.setString(2, employee.getLastName());
                statement.setString(3, employee.getGender());
                statement.setDate(4, Date.valueOf(employee.getBirthDate()));
                statement.setDate(5, Date.valueOf(employee.getHireDate()));

                int updated = statement.executeUpdate();
                if (updated != 1) {
//                    System.err.println("Kazkas negerai!!!");
//                    return;
                    throw new SQLException("Kazkas negerai!!!");
                }

                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    // System.out.println("Sugeneruotas raktas yra: " + resultSet.getInt(1));
                    employee.setEmpNo(resultSet.getInt(1));

                    Gson gson = new Gson();
                    // TODO ...
                    System.out.println(gson.toJson(employee));

                } else {
//                    System.err.println("Negavau sugeneruoto rakto!!!");
//                    return;
                    throw new SQLException("Negavau sugeneruoto rakto!!!");
                }

                // uzbaigiam tranzakcija
                conn.commit();

            } catch (Exception e) {
                // jei kazkokia klaida - tai tranzakcija vyniojam atgal
                conn.rollback();
                throw new ManoException("Kazkokia klaida", e);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ManoException extends RuntimeException {
    public ManoException() {
    }

    public ManoException(String message) {
        super(message);
    }

    public ManoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManoException(Throwable cause) {
        super(cause);
    }

    public ManoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}