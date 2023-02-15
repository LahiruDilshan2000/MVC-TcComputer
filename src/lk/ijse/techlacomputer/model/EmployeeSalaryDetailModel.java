package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.to.Employee;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeSalaryDetailModel {
    public static Boolean updateSalary(Employee employee) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE EmpSalaryDetail SET salary=? WHERE empID=?",
                employee.getEmpSalary(),
                employee.getEmployeeId());
    }

    public static boolean deleteSalary(Employee employee) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE EmpSalaryDetail WHERE empID=?",
                employee.getEmployeeId());
    }

    public static boolean addSalary(Employee employee) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO EmpSalaryDetail VALUES(?,?)",
                employee.getEmployeeId(),
                employee.getEmpSalary());
    }

}
