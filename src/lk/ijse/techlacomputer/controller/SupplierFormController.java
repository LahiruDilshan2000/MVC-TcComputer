package lk.ijse.techlacomputer.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import lk.ijse.techlacomputer.model.EmployeeModel;
import lk.ijse.techlacomputer.model.ItemModel;
import lk.ijse.techlacomputer.model.SupplierModel;
import lk.ijse.techlacomputer.to.Employee;
import lk.ijse.techlacomputer.to.Item;
import lk.ijse.techlacomputer.to.Supplier;
import lk.ijse.techlacomputer.to.SupplierTm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

public class SupplierFormController {
    public TableView tblSupplier;
    public JFXTextField txtSearch;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public JFXButton btnAddUpdate;
    public TableColumn clmSupplierId;
    public TableColumn clmSupplierName;
    public TableColumn clmSupplierAddress;
    public TableColumn clmSupplierContact;
    private static String supplierId;
    public TableColumn clmOption;
    private Pattern namePattern;
    private Pattern addressPattern;
    private Pattern contactPattern;

    public void initialize(){
        initializeTable();
        setTableData();
        initializePattern();
    }

    private void initializePattern() {
        namePattern = Pattern.compile("([\\w ]{1,})");
        addressPattern = Pattern.compile("^[0-9a-zA-Z]{2,}");
        contactPattern = Pattern.compile("^(075|077|071|074|078|076|070|072)([0-9]{7})$");
    }

    private void initializeTable() {
        clmSupplierId.setCellValueFactory(new PropertyValueFactory<>("SupId"));
        clmSupplierName.setCellValueFactory(new PropertyValueFactory<>("SupName"));
        clmSupplierAddress.setCellValueFactory(new PropertyValueFactory<>("SupAddress"));
        clmSupplierContact.setCellValueFactory(new PropertyValueFactory<>("SupContact"));
        clmOption.setCellValueFactory(new PropertyValueFactory<>("option"));

        tblSupplier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setText((SupplierTm) newValue);
                btnAddUpdate.setText("Update");
                btnAddUpdate.setStyle("-fx-background-color: #706fd3; -fx-background-radius: 20");
            }
        });
    }


    private void setText(SupplierTm supplierTm) {
        supplierId = supplierTm.getSupId();
        txtName.setText(supplierTm.getSupName());
        txtAddress.setText(supplierTm.getSupAddress());
        txtContact.setText(supplierTm.getSupContact());
    }
    private void clear() {
        supplierId = "";
        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        txtSearch.clear();
    }

    private void setTable(ArrayList<Supplier> supplierArrayList) {
        ObservableList<SupplierTm> tmList = FXCollections.observableArrayList();
        for (Supplier supplier : supplierArrayList) {
            JFXButton btn = new JFXButton("Delete");
            btn.setStyle("-fx-background-color: #e55039; -fx-background-radius: 20; -fx-text-fill: #ffffff;");
            tmList.add(new SupplierTm(supplier.getSupId(),
                    supplier.getSupName(),
                    supplier.getSupAddress(),
                    supplier.getSupContact(),
                    btn));
            btn.setOnAction(event -> {
                deleteSupplier(supplier.getSupId());
                if (!txtSearch.getText().equalsIgnoreCase("")) {
                    searchSupplier();
                } else {
                    setTableData();
                }
            });
        }
        tblSupplier.setItems(tmList);
    }

    private void setTableData() {
        try {
            ArrayList<Supplier> supplierArrayList = SupplierModel.getSupplierList();
            if (supplierArrayList != null) {
                setTable(supplierArrayList);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void txtSearchKeyReleaseEvent(KeyEvent keyEvent) {
        searchSupplier();
    }

    private void searchSupplier() {
        if (!txtSearch.getText().equalsIgnoreCase("")) {
            try {
                ArrayList<Supplier> supplierArrayList = SupplierModel.searchSupplier(txtSearch.getText());
                if (supplierArrayList != null) {
                    setTable(supplierArrayList);
                } else {
                    tblSupplier.getItems().clear();
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            setTableData();
        }
    }

    private Supplier getObject(){
        return new Supplier(supplierId,
                txtName.getText(),
                txtAddress.getText(),
                txtContact.getText());
    }

    public void tbnAddUpdateOnAction(ActionEvent actionEvent) {
        try {
            if(namePattern.matcher(txtName.getText()).matches()) {
                if(addressPattern.matcher(txtAddress.getText()).matches()) {
                    if(contactPattern.matcher(txtContact.getText()).matches()) {
                        if (btnAddUpdate.getText().equalsIgnoreCase("Add")) {
                            generateNextSupplierId();
                            boolean isAdded = SupplierModel.addSupplier(getObject());
                            if (isAdded) {
                                new Alert(Alert.AlertType.INFORMATION, "Supplier added successful", ButtonType.OK).showAndWait();
                                setData();
                            }
                        } else {
                            boolean isUpdated = SupplierModel.updateSupplier(getObject());
                            if (isUpdated) {
                                new Alert(Alert.AlertType.INFORMATION, "Supplier update successful", ButtonType.OK).showAndWait();
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
        }catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void generateNextSupplierId() {
        try {
            String oldId = SupplierModel.getNextSupplierId();
            if(oldId!=null) {
                String[] split = oldId.split("[A]");
                int lastDigits = Integer.parseInt(split[1]);
                lastDigits++;
                String newRoomId = String.format("A%04d", lastDigits);
                supplierId=newRoomId;
            }else{
                supplierId="A0001";
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteSupplier(String supId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure do you want to delete this Supplier?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            try {
                boolean isDeleted = SupplierModel.deleteSupplier(supId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Delete successfully.", ButtonType.OK).showAndWait();
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
