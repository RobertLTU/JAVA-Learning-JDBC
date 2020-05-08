package lt.bit.java2.services;

import lt.bit.java2.model.Employee;
import lt.bit.java2.model.Salary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    public static List<Employee> loadEmployees(int pageNo, int pageSize) {

        List<Employee> employeeList = new ArrayList<>();
        String sqlStatement = "SELECT * FROM employees LEFT JOIN salaries USING (emp_no) ORDER BY emp_no LIMIT ?, ? ";



        try (
//                Connection connection = DBService.getConnectionSQLiteEmbedded();
                Connection connection = DBService.getConnectionFromCP();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        ){
            ResultSet resultSet;
            Employee currentEmployee = null;
            Salary currSalary;
            int prevEmpNo = 0;
            int currEmpNo = 0;

            preparedStatement.setInt(1, pageSize * pageNo);
            preparedStatement.setInt(2, pageSize);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                currEmpNo = resultSet.getInt("emp_no");

                if(currEmpNo != prevEmpNo){
                    currentEmployee = EmployeeMap.fromResultSet(resultSet);
                    currSalary = SalaryMap.fromResultSet(resultSet);
                    currSalary.setEmployee(currentEmployee);
                    currentEmployee.getSalaries().add(currSalary);
                    employeeList.add(currentEmployee);
                    prevEmpNo = currentEmployee.getEmpNo();

                } else {
                    currSalary = SalaryMap.fromResultSet(resultSet);
                    currentEmployee.getSalaries().add(currSalary);
                    currSalary.setEmployee(currentEmployee);
                }
            }
//            =================================================

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
//            z.printStackTrace();
//            System.out.println(z.getMessage());
        }

        return employeeList;
    }

    // SELECT * FROM employees  LIMIT 5 OFFSET 10
    // SELECT * FROM employees  LIMIT 10,5

    // SELECT * FROM employees  LIMIT ? OFFSET ?
    // 1? <= 10
    // 2? <= 5


}
