package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.db.DBConnection;
import lk.ijse.techlacomputer.to.Employee;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {
    public static ArrayList<Employee> getEmployeeDetails() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT Employee.empID,Employee.name,Employee.mail,Employee.contact,EmpSalaryDetail.salary FROM Employee INNER JOIN EmpSalaryDetail ON Employee.empID = EmpSalaryDetail.empID");
        ArrayList<Employee>employeeArrayList=new ArrayList<>();
        while (rst.next()){
            employeeArrayList.add(new Employee(rst.getString(1),
            rst.getString(2),
            rst.getString(3),
            rst.getString(4),
            rst.getDouble(5)));
        }
        return employeeArrayList;
    }

    public static Boolean updateEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isUpdateEmployee = CrudUtil.execute("UPDATE Employee SET name=?,mail=?,contact=? WHERE empID=?",
                    employee.getEmployeeName(),
                    employee.getEmployeeMail(),
                    employee.getEmployeeContact(),
                    employee.getEmployeeId());
            if (isUpdateEmployee) {
                boolean isEmpSalaryUpdate = EmployeeSalaryDetailModel.updateSalary(employee);
                if (isEmpSalaryUpdate) {
                    DBConnection.getInstance().getConnection().commit();
                    return true;
                }
            }
            DBConnection.getInstance().getConnection().rollback();
            return false;
        } finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public static Boolean deleteEmployee(String text) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Employee WHERE empID=?",
                text);
    }

    public static boolean addEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isEmployeeAdded = CrudUtil.execute("INSERT INTO Employee VALUES(?,?,?,?)",
                    employee.getEmployeeId(),
                    employee.getEmployeeName(),
                    employee.getEmployeeMail(),
                    employee.getEmployeeContact());
            if (isEmployeeAdded) {
                boolean isSalaryAdded = EmployeeSalaryDetailModel.addSalary(employee);
                if (isSalaryAdded) {
                    DBConnection.getInstance().getConnection().commit();
                    return true;
                }
            }
            DBConnection.getInstance().getConnection().rollback();
            return false;
        } finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public static ArrayList<Employee> SearchEmployee(String text) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT Employee.empID,Employee.name,Employee.mail,Employee.contact,EmpSalaryDetail.salary \n" +
                "FROM Employee INNER JOIN EmpSalaryDetail ON Employee.empID = EmpSalaryDetail.empID WHERE name LIKE '"+text+"%'");
        ArrayList<Employee> employeeArrayList = new ArrayList<>();
        while (rst.next()) {
            employeeArrayList.add(new Employee(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5)));
        }
        return employeeArrayList;
    }

    public static String getNextEmpId() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select empID from Employee order by empID DESC LIMIT 1");
        if(rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public static int getEmployeeCount() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select COUNT(empID) from Employee");
        if(rst.next()){
            return rst.getInt(1);
        }
        return -1;
    }
}
