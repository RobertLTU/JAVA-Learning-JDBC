package lt.bit.java2.tests;

import lt.bit.java2.model.Employee;
import lt.bit.java2.services.DBService;
import lt.bit.java2.services.EmployeeService;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmployeeServiceTest extends DBTestBase {

    @Test
    void initialDataTest() throws SQLException {
        Connection connection = DBService.getConnectionFromCP();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM employees");
        assertTrue(resultSet.next());
        assertEquals(14, resultSet.getInt(1));
        connection.close();
    }

    @Test
    void loadEmployeesTest() {
        // Page #:         0         1            2
        // Employees:  1-2-3-4-5 6-7-8-9-10 11-12-13-14
        // Salaries:   - - - - - - - - - -  2  3  -  -
        List<Employee> employees = EmployeeService.loadEmployees(2, 5);
        assertNotNull(employees);
        assertEquals(4, employees.size());
        assertNotNull(employees.get(0).getSalaries());
        assertEquals(2, employees.get(0).getSalaries().size());
        assertEquals(3, employees.get(1).getSalaries().size());
    }

    @Test
    void updateEmployeeTest() {
        Employee employee = EmployeeService.loadEmployee(3);
        assertEquals("A3", employee.getFirstName());
        assertEquals(LocalDate.of(2000, 1, 3), employee.getBirthDate());

        employee.setFirstName("A31");
        employee.setBirthDate(LocalDate.of(2000, 2, 15));
        EmployeeService.updateEmployee(employee);

        employee = EmployeeService.loadEmployee(3);
//        assertEquals("A31", employee.getFirstName());
//        assertEquals(LocalDate.of(2000, 2, 15), employee.getBirthDate());
    }

    @Test
    void insertNewEmployeeTest() {
        Employee employee = new Employee();
        employee.setFirstName("X1");
        employee.setLastName("X2");
        employee.setBirthDate(LocalDate.of(2000, 12, 31));
        employee.setGender("M");
        employee.setHireDate(LocalDate.of(2020, 3, 31));
        employee = EmployeeService.insertNewEmployee(employee);
        assertNotNull(employee.getEmpNo());

        int empNo = employee.getEmpNo();
        employee = EmployeeService.loadEmployee(empNo);
        EmployeeService.printEmployee(employee);
        assertEquals("X1", employee.getFirstName());
    }

    @Test
    void deleteEmployeeTest() {
        Employee employee = EmployeeService.loadEmployee(3);
        EmployeeService.deleteEmployee(employee);

        employee = EmployeeService.loadEmployee(3);
        assertNull(employee);
    }




}
