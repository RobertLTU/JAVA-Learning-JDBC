package lt.bit.java2.services;

import lt.bit.java2.model.Employee;
import lt.bit.java2.model.Salary;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SalaryMap {

    public static Salary fromResultSet(ResultSet resultSet) {
        try {
            Salary salary = new Salary();
            salary.setEmpNo(resultSet.getInt("emp_no"));
            salary.setFromDate(resultSet.getDate("from_date").toLocalDate());
            salary.setToDate(resultSet.getDate("to_date").toLocalDate());
            salary.setSalary(resultSet.getInt("salary"));
            return salary;

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
