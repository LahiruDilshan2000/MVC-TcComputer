package lk.ijse.techlacomputer.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import lk.ijse.techlacomputer.model.EmployeeModel;
import lk.ijse.techlacomputer.to.Employee;
import lk.ijse.techlacomputer.to.EmployeeTm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

public class EmployeesFormController {
    public JFXTextField txtUserName;
    public JFXTextField txtMail;
    public JFXTextField txtContact;
    public JFXTextField txtSalary;
    public JFXTextField txtSearch;
    public TableColumn clmEmpId;
    public TableColumn clmName;
    public TableColumn clmMail;
    public TableColumn clmContact;
    public TableColumn clmSalary;
    public TableView tblEmployee;
    public TableColumn clmOption;
    public Label lblEmpId;
    public JFXButton btnAddUpdate;
    public Pane empIdPane;
    private Pattern namePattern;
    private Pattern mailPattern;
    private Pattern contactPattern;
    private Pattern salaryPattern;

    public void initialize() {
        initializeTable();
        setDataToTable();
        initializePattern();
        empIdPane.setVisible(false);
    }

    private void initializePattern() {
        namePattern = Pattern.compile("([\\w ]{1,})");
        mailPattern = Pattern.compile("^([a-zA-Z|0-9]{3,})[@]([a-z]{2,})\\.(com|lk)$");
        contactPattern = Pattern.compile("^(075|077|071|074|078|076|070|072)([0-9]{7})$");
        salaryPattern = Pattern.compile("^[0-9|.]{2,}$");
    }

    private void initializeTable() {
        clmEmpId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        clmMail.setCellValueFactory(new PropertyValueFactory<>("employeeMail"));
        clmContact.setCellValueFactory(new PropertyValueFactory<>("employeeContact"));
        clmSalary.setCellValueFactory(new PropertyValueFactory<>("empSalary"));
        clmOption.setCellValueFactory(new PropertyValueFactory<>("option"));

        tblEmployee.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setTexts((EmployeeTm) newValue);
                empIdPane.setVisible(true);
                btnAddUpdate.setText("Update");
                btnAddUpdate.setStyle("-fx-background-color: #706fd3; -fx-background-radius: 20");
            }
        });
    }

    private void setTexts(EmployeeTm employeeTm) {
        lblEmpId.setText(employeeTm.getEmployeeId());
        txtUserName.setText(employeeTm.getEmployeeName());
        txtMail.setText(employeeTm.getEmployeeMail());
        txtContact.setText(employeeTm.getEmployeeContact());
        txtSalary.setText(String.valueOf(employeeTm.getEmpSalary()));
    }

    private void setTable(ArrayList<Employee> employeeArrayList) {
        ObservableList<EmployeeTm> tmList = FXCollections.observableArrayList();
        for (Employee employee : employeeArrayList) {
            JFXButton btn = new JFXButton("Delete");
            btn.setStyle("-fx-background-color: #e55039; -fx-background-radius: 20; -fx-text-fill: #ffffff;");
            tmList.add(new EmployeeTm(employee.getEmployeeId(),
                    employee.getEmployeeName(),
                    employee.getEmployeeMail(),
                    employee.getEmployeeContact(),
                    employee.getEmpSalary(),
                    btn));
            btn.setOnAction(event -> {
                deleteEmployee(employee.getEmployeeId());
                if (!txtSearch.getText().equalsIgnoreCase("")) {
                    searchEmployee();
                    setDefault();
                } else {
                    setDataToTable();
                    setDefault();
                }
            });
        }
        tblEmployee.setItems(tmList);
    }

    private void setDataToTable() {
        try {
            ArrayList<Employee> employeeArrayList = EmployeeModel.getEmployeeDetails();
            if (employeeArrayList != null) {
                setTable(employeeArrayList);
            } else {
                clearTableData();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void clear() {
        txtUserName.clear();
        txtMail.clear();
        txtContact.clear();
        txtSalary.clear();
        txtSearch.clear();
    }

    private void setUpdate() {
        try {
            boolean isUpdate = EmployeeModel.updateEmployee(new Employee(lblEmpId.getText(),
                    txtUserName.getText(),
                    txtMail.getText(),
                    txtContact.getText(),
                    Double.parseDouble(txtSalary.getText())));
            if (isUpdate) {
                new Alert(Alert.AlertType.INFORMATION, "Employee Detail update successfully.", ButtonType.OK).showAndWait();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteEmployee(String employeeId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure do you want to delete this Employee?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            try {
                boolean isDelete = EmployeeModel.deleteEmployee(employeeId);
                if (isDelete) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee Delete successfully.", ButtonType.OK).showAndWait();
                    setDataToTable();
                    clear();
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void addEmployee() {
        try {
            boolean isEmployeeAdd = EmployeeModel.addEmployee(new Employee(lblEmpId.getText(),
                    txtUserName.getText(),
                    txtMail.getText(),
                    txtContact.getText(),
                    Double.parseDouble(txtSalary.getText())));
            if (isEmployeeAdd) {
                new Alert(Alert.AlertType.INFORMATION, "Employee Added successful.", ButtonType.OK).showAndWait();
                setDataToTable();
                clear();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void txtSearchKeyReleaseEvent(KeyEvent keyEvent) {
        searchEmployee();
    }

    private void searchEmployee() {
        if (!txtSearch.getText().equalsIgnoreCase("")) {
            try {
                ArrayList<Employee> employeeArrayList = EmployeeModel.SearchEmployee(txtSearch.getText());
                if (employeeArrayList != null) {
                    setTable(employeeArrayList);
                } else {
                    clearTableData();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else {
            setDataToTable();
        }
    }

    private void clearTableData() {
        tblEmployee.getItems().clear();
    }

    private void getNextEmployeeId() {
        try {
            String oldId = EmployeeModel.getNextEmpId();
            if (oldId != null) {
                String[] split = oldId.split("[E]");
                int lastDigits = Integer.parseInt(split[1]);
                lastDigits++;
                String newRoomId = String.format("E%04d", lastDigits);
                lblEmpId.setText(newRoomId);
            } else {
                lblEmpId.setText("E0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void tbnAddUpdateOnAction(ActionEvent actionEvent) {
        if(namePattern.matcher(txtUserName.getText()).matches()) {
            if(mailPattern.matcher(txtMail.getText()).matches()) {
                if(contactPattern.matcher(txtContact.getText()).matches()) {
                    if(salaryPattern.matcher(txtSalary.getText()).matches()) {
                        if (btnAddUpdate.getText().equalsIgnoreCase("Add")) {
                            getNextEmployeeId();
                            addEmployee();
                        } else {
                            setUpdate();
                            setDefault();
                            setDataToTable();
                            clear();
                        }
                    }else {
                        txtSalary.setFocusColor(Paint.valueOf("Red"));
                        txtSalary.requestFocus();
                    }
                }else {
                    txtContact.setFocusColor(Paint.valueOf("Red"));
                    txtContact.requestFocus();
                }
            }else {
                txtMail.setFocusColor(Paint.valueOf("Red"));
                txtMail.requestFocus();
            }
        }else {
            txtUserName.setFocusColor(Paint.valueOf("Red"));
            txtUserName.requestFocus();
        }
    }

    private void setDefault() {
        empIdPane.setVisible(false);
        btnAddUpdate.setText("Add");
        btnAddUpdate.setStyle("-fx-background-color:   #2f3542 ;-fx-background-radius: 20");
    }
}
