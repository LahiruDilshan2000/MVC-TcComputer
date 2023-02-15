package lk.ijse.techlacomputer.to;

public class Employee {
    private String employeeId;
    private String employeeName;
    private String employeeMail;
    private String employeeContact;
    private double empSalary;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeMail() {
        return employeeMail;
    }

    public void setEmployeeMail(String employeeMail) {
        this.employeeMail = employeeMail;
    }

    public String getEmployeeContact() {
        return employeeContact;
    }

    public void setEmployeeContact(String employeeContact) {
        this.employeeContact = employeeContact;
    }

    public double getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(double empSalary) {
        this.empSalary = empSalary;
    }

    public Employee(String employeeId, String employeeName, String employeeMail, String employeeContact, double empSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeMail = employeeMail;
        this.employeeContact = employeeContact;
        this.empSalary = empSalary;
    }

    public Employee() {
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeMail='" + employeeMail + '\'' +
                ", employeeContact='" + employeeContact + '\'' +
                ", empSalary=" + empSalary +
                '}';
    }
}
