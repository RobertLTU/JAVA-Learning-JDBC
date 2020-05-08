package lt.bit.java2.services;

import lt.bit.java2.model.Employee;
import lt.bit.java2.model.Salary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {


    /**
     * Grazinti employee puslapi
     *
     * @param pageNo   puslapio numeris (numeruojame nuo 0)
     * @param pageSize puslapio dydis
     * @return List of Employee Objects based on parameters
     */
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

            preparedStatement.setInt(1, pageSize * (pageNo-1));
            preparedStatement.setInt(2, pageSize);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();     // DEBUG line
            int emp_no = resultSet.getInt("emp_no");      // DEBUG line
            String debugString = resultSet.getString("first_name");   // DEBUG line
            int debugSalary = resultSet.getInt("salary");   // DEBUG line
            String degubString = resultSet.getString("gender"); // DEBUG line

// 101 Query eilute   '10009', '1952-04-19', 'Sumant', 'Peac', 'F', '1985-02-18', '85875', '1997-02-15', '1998-02-15'
// 201 Query eilute   '10021', '1960-02-20', 'Ramzi', 'Erde', 'M', '1988-02-10', '61117', '1992-02-09', '1993-02-08'
//          =========================================================


            /*
            Kodel programa nuluzta sitoj vietoj kvieciant resultSet.next() metoda ?
            Ar todel kad ResultSet jau uzdarytas??? Tokiu atveju nesuprantu kodel jis sitoj vietoj uzsidaro ?
            Ar neturetu jis uzsidaryti TRYwithResources bloko, kuriame sukurtas Statement, pabaigoje ?
            Is ORACLE docs:
            " The try-with-resources statement ensures that each resource is closed at the end of the statement."
            */

            while (resultSet.next()) {
                currEmpNo = resultSet.getInt("emp_no");

                if(currEmpNo != prevEmpNo){
                    currentEmployee = EmployeeMap.fromResultSet(resultSet);
                    currSalary = SalaryMap.fromResultSet(resultSet);
                    currSalary.setEmployee(currentEmployee);
                    currentEmployee.getSalaries().add(currSalary);
                    employeeList.add(currentEmployee);

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
        }

        return employeeList;
    }

//    private static List<Employee> ResultSetToEmployeeList (ResultSet resultSet, List<Employee> employeeList){
//        try {
//            System.out.println(" <<<<<<< MESSAGE from SECOND TRY block >>>>>>>>");
//            Employee currentEmployee = null;
//            int currEmpNo = 0;
//            Salary salary = null;
//            while (resultSet.next()) {
//                System.out.println(" <<<<<<< MESSAGE from WHILE LOOP inside SECOND TRY block >>>>>>>>");
//                if(currEmpNo != resultSet.getInt("emp_no")){
//                    currentEmployee = EmployeeMap.fromResultSet(resultSet);
//                    salary = SalaryMap.fromResultSet(resultSet);
//                    currentEmployee.getSalaries().add(salary);
//                    currEmpNo = resultSet.getInt("emp_no");
//                    employeeList.add(currentEmployee);
//                    salary.setEmployee(currentEmployee);
//                } else {
//                    salary = SalaryMap.fromResultSet(resultSet);
//                    currentEmployee.getSalaries().add(salary);
//                    salary.setEmployee(currentEmployee);
//                }
//            }
//
//        }catch(SQLException e){
//            System.out.println(e.getMessage());
//        }
//        return employeeList;
//    }

    // SELECT * FROM employees  LIMIT 5 OFFSET 10
    // SELECT * FROM employees  LIMIT 10,5

    // SELECT * FROM employees  LIMIT ? OFFSET ?
    // 1? <= 10
    // 2? <= 5


}
