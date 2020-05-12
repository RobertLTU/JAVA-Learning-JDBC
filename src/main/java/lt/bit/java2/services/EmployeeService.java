package lt.bit.java2.services;

import lt.bit.java2.model.Employee;
import lt.bit.java2.model.Salary;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    public static List<Employee> loadEmployees(int pageNo, int pageSize) {

        List<Employee> employeeList = new ArrayList<>();
        String sqlStatement = "SELECT * FROM (SELECT * FROM employees LIMIT ?, ?) AS employees LEFT JOIN salaries USING (emp_no)";

        try (
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

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("loadEmployees ==>> SQL or NullPointer Exception");
        }
        printLoadedEmployeesAndSalaries(employeeList);
        return employeeList;
    }

    public static void printLoadedEmployeesAndSalaries(List<Employee> employeeList) {
        for (Employee employee : employeeList) {
            System.out.print("" + employee.getEmpNo() + " || ");
            System.out.print("" + employee.getFirstName() + " || ");
            System.out.print("" + employee.getLastName() + " || ");
            System.out.println("Salary records count : " + employee.getSalaries().size() + " || ");
            for (Salary salary : employee.getSalaries()) {
                System.out.println("\t Salary: " + salary.getSalary());
//                System.out.println(" || hash code: " + employee.hashCode()); // DEBUG line
            }
        }
    }

    public static void printEmployee(Employee employee) {
            System.out.print("empNo: " + employee.getEmpNo() + " || ");
            System.out.print("" + employee.getFirstName() + " || ");
            System.out.println("" + employee.getLastName() + " || ");
        System.out.print("gender: " + employee.getGender() + " || ");
        System.out.print("birth-date: " + employee.getBirthDate() + " || ");
        System.out.print("hire-date:  " + employee.getHireDate() + " || ");
            System.out.println("Salary records count: " + employee.getSalaries().size() + " || ");
            for (Salary salary : employee.getSalaries()) {
                System.out.println("\t Salary :" + salary.getSalary());
            }
    }


    public static Employee loadEmployee(int empNo) {
        Employee employee = null;
        String sqlQuery = "SELECT * FROM (SELECT * FROM employees WHERE emp_no = ?) AS employees LEFT JOIN salaries USING (emp_no)";
        try (Connection connection = DBService.getConnectionFromCP();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, empNo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (employee == null) {
                        employee = EmployeeMap.fromResultSet(resultSet);
                    }
                    Salary salary = SalaryMap.fromResultSet(resultSet);
                    if(salary != null) {
                        employee.getSalaries().add(salary);
                        salary.setEmployee(employee);
                    }
                }
            }else{
                System.out.println("********** INVALID employee number  ************");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    public static void updateEmployee(Employee employee) {
        Integer empNo = employee.getEmpNo();
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        String gender = employee.getGender();
        LocalDate birthDate = employee.getBirthDate();
        LocalDate hireDate = employee.getHireDate();
        String sqlQuery = "UPDATE employees SET " +
//                ", emp_no = " + empNo.toString() +
                " first_name = \'" + firstName + "\'" +
                ", last_name = \'" + lastName + "\'" +
                ", gender = \'" + gender + "\'" +
                ", birth_date = \'" + birthDate.toString() + "\'" +
                ", hire_date = \'" + hireDate.toString() + "\'" +
                " WHERE emp_no = " + empNo;
        try(Connection connection = DBService.getConnectionFromCP();
        PreparedStatement statement = connection.prepareStatement(sqlQuery)){
            System.out.println("RETURN value from statement.execute() ===> " + statement.execute());;
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static Employee insertNewEmployee(Employee employee) {
        int newEmpNo = 0;
        String sqlQuery = "INSERT INTO employees (birth_date, first_name, last_name, gender, hire_date) VALUES ("+
                "\'" + employee.getBirthDate() + "\'," +
                "\'" + employee.getFirstName() + "\'," +
                "\'" + employee.getLastName() + "\'," +
                "\'" + employee.getGender() + "\'," +
                "\'" + employee.getHireDate() + "\')";
        try(Connection connection = DBService.getConnectionFromCP();
        PreparedStatement statement = connection.prepareStatement(sqlQuery)){
            statement.execute();
            ResultSet resultSet = connection.prepareStatement("SELECT LAST_INSERT_ID()").executeQuery();
            resultSet.next();
            newEmpNo = resultSet.getInt(1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return EmployeeService.loadEmployee(newEmpNo);
    }

    public static void deleteEmployee(Employee employee) {
        int empNo = employee.getEmpNo();
        String sqlDelete = "DELETE FROM employees WHERE emp_no = ?";
        try(Connection connection = DBService.getConnectionFromCP();
        PreparedStatement statement = connection.prepareStatement(sqlDelete)){
        statement.setInt(1, empNo);
        statement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }



}
