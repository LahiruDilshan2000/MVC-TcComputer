package lk.ijse.techlacomputer.to;

import javafx.scene.control.Button;

public class EmployeeTm {
    private String employeeId;
    private String employeeName;
    private String employeeMail;
    private String employeeContact;
    private double empSalary;
    private Button option;

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

    public Button getOption() {
        return option;
    }

    public void setOption(Button option) {
        this.option = option;
    }

    public EmployeeTm(String employeeId, String employeeName, String employeeMail, String employeeContact, double empSalary, Button option) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeMail = employeeMail;
        this.employeeContact = employeeContact;
        this.empSalary = empSalary;
        this.option = option;
    }

    public EmployeeTm() {
    }
}
