package lt.bit.java2.services;

import lt.bit.java2.model.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMap {

    public static Employee fromResultSet(ResultSet resultSet) {
        try {
            Employee employee = new Employee();
            employee.setEmpNo(resultSet.getInt("emp_no"));
            employee.setFirstName(resultSet.getString("first_name"));
            employee.setLastName(resultSet.getString("last_name"));
            employee.setGender(resultSet.getString("gender"));
            employee.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
            employee.setHireDate(resultSet.getDate("hire_date").toLocalDate());
            return employee;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
}
