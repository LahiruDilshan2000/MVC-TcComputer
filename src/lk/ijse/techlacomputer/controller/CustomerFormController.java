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
import lk.ijse.techlacomputer.model.CustomerModel;
import lk.ijse.techlacomputer.to.Customer;
import lk.ijse.techlacomputer.to.CustomerTm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

public class CustomerFormController {
    public TableView tblCustomer;
    public TableColumn clmCusId;
    public TableColumn clmName;
    public TableColumn clmAddress;
    public TableColumn clmContact;
    public JFXTextField txtSearch;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public JFXButton btnAddUpdate;
    public Label lblCusId;
    public TableColumn clmOption;
    public Pane cusIdPane;
    private Pattern namePattern;
    private Pattern addressPattern;
    private Pattern contactPattern;

    public void initialize(){
        initializeTableData();
        setTableData();
        initializePattern();
        cusIdPane.setVisible(false);
    }

    private void initializePattern() {
        namePattern = Pattern.compile("([\\w ]{1,})");
        addressPattern = Pattern.compile("^[0-9a-zA-Z]{2,}");
        contactPattern = Pattern.compile("^(075|077|071|074|078|076|070|072)([0-9]{7})$");
    }

    private void setTableData() {
        try {
            ArrayList<Customer> customerArrayList = CustomerModel.getCustomerList();
            if (customerArrayList != null) {
                setTable(customerArrayList);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setTable(ArrayList<Customer> customerArrayList) {
        ObservableList<CustomerTm> tmList = FXCollections.observableArrayList();
        for (Customer customer : customerArrayList) {
            JFXButton btn = new JFXButton("Delete");
            btn.setStyle("-fx-background-color: #e55039; -fx-background-radius: 20; -fx-text-fill: #ffffff;");
            tmList.add(new CustomerTm(customer.getCusId(),
                    customer.getCusName(),
                    customer.getCusAddress(),
                    customer.getCusContact(),
                    btn));
            btn.setOnAction(event -> {
                deleteCustomer(customer.getCusId());
                if(!txtSearch.getText().equalsIgnoreCase("")){
                    searchCustomer();
                }else {
                    setTableData();
                }
            });
        }
        tblCustomer.setItems(tmList);
    }

    private void initializeTableData() {
        clmCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        clmAddress.setCellValueFactory(new PropertyValueFactory<>("cusAddress"));
        clmContact.setCellValueFactory(new PropertyValueFactory<>("cusContact"));
        clmOption.setCellValueFactory(new PropertyValueFactory<>("option"));

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setText((CustomerTm) newValue);
                btnAddUpdate.setText("Update");
                btnAddUpdate.setStyle("-fx-background-color: #706fd3; -fx-background-radius: 20");
                cusIdPane.setVisible(false);
            }
        });
    }

    private void setText(CustomerTm customerTm) {
        lblCusId.setText(customerTm.getCusId());
        txtName.setText(customerTm.getCusName());
        txtAddress.setText(customerTm.getCusAddress());
        txtContact.setText(customerTm.getCusContact());
    }

    private void clear() {
        lblCusId.setText("");
        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        txtSearch.clear();
    }

    public void txtSearchKeyReleaseEvent(KeyEvent keyEvent) {
        searchCustomer();
    }

    private void searchCustomer() {
        if (!txtSearch.getText().equalsIgnoreCase("")) {
            try {
                ArrayList<Customer> customerArrayList = CustomerModel.searchCustomer(txtSearch.getText());
                if (customerArrayList != null) {
                    setTable(customerArrayList);
                } else {
                    tblCustomer.getItems().clear();
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            setTableData();
        }
    }

    public void tbnAddUpdateOnAction(ActionEvent actionEvent) {
        try {
            if(namePattern.matcher(txtName.getText()).matches()) {
                if(addressPattern.matcher(txtAddress.getText()).matches()) {
                    if(contactPattern.matcher(txtContact.getText()).matches()) {
                        if (btnAddUpdate.getText().equalsIgnoreCase("Add")) {
                            generateNextItemCode();
                            boolean isAdded = CustomerModel.addCustomer(getObject());
                            if (isAdded) {
                                new Alert(Alert.AlertType.INFORMATION, "Customer add Successful", ButtonType.OK).showAndWait();
                                setData();
                            }
                        } else {
                            boolean isUpdate = CustomerModel.updateCustomer(getObject());
                            if (isUpdate) {
                                new Alert(Alert.AlertType.INFORMATION, "Customer Update Successful", ButtonType.OK).showAndWait();
                                setData();
                            }
                        }
                    }else {
                        txtContact.setFocusColor(Paint.valueOf("Red"));
                        txtContact.requestFocus();
                    }
                }else {
                    txtAddress.setFocusColor(Paint.valueOf("Red"));
                    txtAddress.requestFocus();
                }
            }else {
                txtName.setFocusColor(Paint.valueOf("Red"));
                txtName.requestFocus();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Customer getObject(){
        return new Customer(lblCusId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtContact.getText());
    }

    private void generateNextItemCode() {
        try {
            String oldId = CustomerModel.getNextCustomerId();
            if(oldId!=null) {
                String[] split = oldId.split("[C]");
                int lastDigits = Integer.parseInt(split[1]);
                lastDigits++;
                String newRoomId = String.format("C%04d", lastDigits);
                lblCusId.setText(newRoomId);
            }else{
                lblCusId.setText("C0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomer(String customerId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure do you want to delete this Customer?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            try {
                boolean isDelete = CustomerModel.deleteCustomer(customerId);
                if (isDelete) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Delete successfully.", ButtonType.OK).showAndWait();
                    setData();
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void setData(){
        btnAddUpdate.setText("Add");
        btnAddUpdate.setStyle("-fx-background-color:   #2f3542 ;-fx-background-radius: 20");
        setTableData();
        clear();
    }
}
