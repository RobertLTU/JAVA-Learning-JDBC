package lt.bit.java2.services;

import lt.bit.java2.model.Employee;
import lt.bit.java2.model.Salary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    public static List<Employee> loadEmployees(int pageNo, int pageSize) {

        List<Employee> employeeList = new ArrayList<>();
        String sqlStatement = "SELECT * FROM (SELECT * FROM employees LIMIT ?, ?) employees LEFT JOIN salaries USING (emp_no)";

        try (
                // Connection connection = DBService.getConnectionSQLiteEmbedded();
//                 Connection connection = DBService.getConnectionH2Embedded();
                Connection connection = DBService.getConnectionFromCP();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)
        ) {
            ResultSet resultSet;
            Employee currentEmployee = null;
            Salary currSalary;
            int prevEmpNo = 0;
            int currEmpNo;

            preparedStatement.setInt(1, pageSize * pageNo);
            preparedStatement.setInt(2, pageSize);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                currEmpNo = resultSet.getInt("emp_no");

                if (currEmpNo != prevEmpNo) {
                    currentEmployee = EmployeeMap.fromResultSet(resultSet);

                    currSalary = SalaryMap.fromResultSet(resultSet);
                    if (currSalary != null) {
                        currSalary.setEmployee(currentEmployee);
                        currentEmployee.getSalaries().add(currSalary);
                    }
                    employeeList.add(currentEmployee);
                    prevEmpNo = currentEmployee.getEmpNo();

                } else {
                    currSalary = SalaryMap.fromResultSet(resultSet);
                    currentEmployee.getSalaries().add(currSalary);
                    currSalary.setEmployee(currentEmployee);
                }
            }
//            =================================================

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace(); //Meta NullPointerEx kai yra darbuotojas be atlyginimo irasu.
        }
        printLoadedEmployeesAndSalaries(employeeList);
        return employeeList;
    }

    private static void printLoadedEmployeesAndSalaries(List<Employee> employeeList) {
        for (Employee employee : employeeList) {
            System.out.print("" + employee.getEmpNo() + " || ");
            System.out.print("" + employee.getFirstName() + " || ");
            System.out.print("" + employee.getLastName() + " || ");
            System.out.println("Salary records count : " + employee.getSalaries().size() + " || ");
            for (Salary salary : employee.getSalaries()) {
                System.out.println("\t Salary :" + salary.getSalary());
//                System.out.println(" || hash code: " + employee.hashCode()); // DEBUG line
            }
        }
    }


    // SELECT * FROM employees  LIMIT 5 OFFSET 10
    // SELECT * FROM employees  LIMIT 10,5

    // SELECT * FROM employees  LIMIT ? OFFSET ?
    // 1? <= 10
    // 2? <= 5


}
