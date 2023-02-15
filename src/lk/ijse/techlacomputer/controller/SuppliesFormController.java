package lk.ijse.techlacomputer.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import lk.ijse.techlacomputer.db.ItemDescriptionArray;
import lk.ijse.techlacomputer.model.ItemModel;
import lk.ijse.techlacomputer.model.SupplierModel;
import lk.ijse.techlacomputer.model.SuppliesModel;
import lk.ijse.techlacomputer.to.*;
import lk.ijse.techlacomputer.util.Navigation;
import lk.ijse.techlacomputer.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

public class SuppliesFormController {
    public AnchorPane loadPane;
    public JFXButton btnAdd;
    public TableColumn clmItemCode;
    public TableColumn clmItemBrand;
    public TableColumn clmItemName;
    public TableColumn clmUnitPrice;
    public TableColumn clmQty;
    public TableColumn clmTotal;
    public TableColumn clmOption;
    public Label lblSuppliesCode;
    public Label lblDate;
    public TableView tblSupplies;
    public Label lblTime;
    public JFXComboBox cmbSupplierId;
    public Label lblSupplierName;
    public Label lblSupplierAddress;
    public Label lblSupplierContact;
    public JFXComboBox cmbItemCode;
    public Label lblBrandName;
    public Label lblItemName;
    public JFXTextField txtQty;
    public Label lblTotal;
    public JFXButton btnPlaceOrder;
    public ImageView picEdit;
    public JFXButton btnEdit;
    public JFXTextField txtUnitPrice;
    private String itemCode;
    private Pattern amountPattern;
    private Pattern qtyPattern;

    public void initialize() {
        ItemDescriptionArray.itemDescriptionArrayList.clear();
        setDateAndTime();
        getSuppliesCode();
        initializeTableData();
        setTableData();
        setSupplierIdCmb();
        initializePattern();
        clearButton(false);
    }

    private void initializePattern() {
        amountPattern = Pattern.compile("^[0-9|.]{1,}$");
        qtyPattern = Pattern.compile("[0-9|.]{1,}");
    }

    private void setSupplierIdCmb() {
        try {
            ArrayList<Supplier> supplierArrayList = SupplierModel.getSupplierList();
            for (Supplier supplier : supplierArrayList) {
                cmbSupplierId.getItems().addAll(supplier.getSupId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeTableData() {
        clmItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        clmItemBrand.setCellValueFactory(new PropertyValueFactory<>("itemBrand"));
        clmItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        clmUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        clmQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        clmTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        clmOption.setCellValueFactory(new PropertyValueFactory<>("option"));

        tblSupplies.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setText((ItemDescriptionTm) newValue);
                btnAdd.setText("Update Cart");
                btnAdd.setStyle("-fx-background-color: #706fd3; -fx-background-radius: 20");
            }
        });
    }

    private void setDateAndTime() {
        lblDate.setText(String.valueOf(LocalDate.now()));
        setTime();
    }

    private void setTime() {
        Timeline time = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm : a");
                    lblTime.setText(LocalDateTime.now().format(formatter));

                }), new KeyFrame(Duration.seconds(1)));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    private void getSuppliesCode() {
        try {
            String oldId = SuppliesModel.getNextSuppliesCode();
            if (oldId != null) {
                String[] split = oldId.split("[-]");
                int lastDigits = Integer.parseInt(split[1]);
                lastDigits++;
                String newRoomId = String.format("SE-%04d", lastDigits);
                lblSuppliesCode.setText(newRoomId);
            } else {
                lblSuppliesCode.setText("SE-0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void setText(ItemDescriptionTm itemDescriptionTm) {
        cmbItemCode.setValue(itemDescriptionTm.getItemCode());
        txtQty.setText(String.valueOf(itemDescriptionTm.getQty()));
        txtQty.requestFocus();
        lblBrandName.setText(itemDescriptionTm.getItemBrand());
        lblItemName.setText(itemDescriptionTm.getItemName());
        itemCode = itemDescriptionTm.getItemCode();
        txtUnitPrice.setText(String.valueOf(itemDescriptionTm.getUnitPrice()));
    }

    private void setTableData() {
        ObservableList<ItemDescriptionTm> tmList = FXCollections.observableArrayList();
        if (!ItemDescriptionArray.itemDescriptionArrayList.isEmpty()) {
            for (ItemDescription itemDescription : ItemDescriptionArray.itemDescriptionArrayList) {
                JFXButton btn = new JFXButton("Delete");
                btn.setStyle("-fx-background-color: #e55039; -fx-background-radius: 20;-fx-text-fill: #ffffff");
                tmList.add(new ItemDescriptionTm(itemDescription.getItemCode(), itemDescription.getItemBrand(), itemDescription.getItemName(),
                        itemDescription.getUnitPrice(), itemDescription.getQty(), itemDescription.getTotal(), btn));
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure do you want to remove this item",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {
                        ItemDescriptionArray.itemDescriptionArrayList.remove(itemDescription);
                        setButton();
                    }
                    setDetails();
                });
                tblSupplies.setItems(tmList);
            }
        } else {
            tblSupplies.getItems().clear();
        }
    }

    private void setDetails() {
        setTableData();
        setTotal();
        clear();
    }

    private void setTotal() {
        double total = 0;
        for (ItemDescription itemDescription : ItemDescriptionArray.itemDescriptionArrayList) {
            total += itemDescription.getTotal();
        }
        lblTotal.setText(String.valueOf(total));
    }

    private void clear() {
        cmbItemCode.getSelectionModel().clearSelection();
        lblBrandName.setText("Item Brand...");
        lblItemName.setText("Item Name...");
        txtUnitPrice.clear();
        txtQty.clear();
    }

    public void tbnAddOnAction(ActionEvent actionEvent) {
        if (!cmbSupplierId.getSelectionModel().isSelected(-1)) {
            if (!cmbItemCode.getSelectionModel().isSelected(-1)) {
                if (amountPattern.matcher(txtUnitPrice.getText()).matches()) {
                    if (qtyPattern.matcher(txtQty.getText()).matches()) {
                        if (btnAdd.getText().equalsIgnoreCase("Add to cart")) {
                            if (checkDuplicate()) {
                                ItemDescriptionArray.itemDescriptionArrayList.add(new ItemDescription(cmbItemCode.getValue().toString(),
                                        lblBrandName.getText(),
                                        lblItemName.getText(),
                                        Double.parseDouble(txtUnitPrice.getText()),
                                        Integer.parseInt(txtQty.getText()),
                                        (Double.parseDouble(txtUnitPrice.getText()) * Double.parseDouble(txtQty.getText()))));
                                setDetails();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You all ready add this item in the list.Do you want to update this item ?",
                                        ButtonType.YES, ButtonType.NO);
                                Optional<ButtonType> buttonType = alert.showAndWait();
                                if (buttonType.get() == ButtonType.YES) {
                                    getDetails();
                                    btnAdd.setText("Update Cart");
                                    btnAdd.setStyle("-fx-background-color: #706fd3; -fx-background-radius: 20");
                                }else{
                                    clear();
                                }
                            }
                        } else {
                            updateItem();
                            setButton();
                            itemCode = "";
                            setDetails();
                        }
                    } else {
                        txtQty.setFocusColor(Paint.valueOf("Red"));
                        txtQty.requestFocus();
                    }
                } else {
                    txtUnitPrice.setFocusColor(Paint.valueOf("Red"));
                    txtUnitPrice.requestFocus();
                }
            } else {
                cmbItemCode.setFocusColor(Paint.valueOf("Red"));
                cmbItemCode.requestFocus();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Please Select the Customer details", ButtonType.OK).showAndWait();
        }
    }

    private void setButton(){
        btnAdd.setText("Add to Cart");
        btnAdd.setStyle("-fx-background-color:  #2f3542; -fx-background-radius: 20");
    }
    private void updateItem(){
        for (int i = 0; i < ItemDescriptionArray.itemDescriptionArrayList.size(); i++) {
            if (ItemDescriptionArray.itemDescriptionArrayList.get(i).getItemCode().equalsIgnoreCase(itemCode)) {
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setItemCode(cmbItemCode.getValue().toString());
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setItemBrand(lblBrandName.getText());
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setItemName(lblItemName.getText());
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setQty(Integer.parseInt(txtQty.getText()));
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setTotal(Double.parseDouble(txtUnitPrice.getText()) * Double.parseDouble(txtQty.getText()));
                break;
            }
        }
    }

    private void getDetails() {
        for(ItemDescription itemDescription: ItemDescriptionArray.itemDescriptionArrayList){
            if(itemDescription.getItemCode().equalsIgnoreCase(cmbItemCode.getValue().toString())){
                itemCode=itemDescription.getItemCode();
                txtUnitPrice.setText(String.valueOf(itemDescription.getUnitPrice()));
                txtQty.setText(String.valueOf(itemDescription.getQty()));
                break;
            }
        }
    }

    private boolean checkDuplicate() {
        for (ItemDescription itemDescription : ItemDescriptionArray.itemDescriptionArrayList) {
            if (itemDescription.getItemCode().equalsIgnoreCase(cmbItemCode.getValue().toString())) {
                return false;
            }
        }
        return true;
    }

    public void cmbSupplierIdOnAction(ActionEvent actionEvent) {
        setItemCmb();
        try {
            ArrayList<Supplier> supplierArrayList = SupplierModel.getSupplierList();
            for (Supplier supplier : supplierArrayList) {
                if (supplier.getSupId().equalsIgnoreCase(cmbSupplierId.getValue().toString())) {
                    lblSupplierName.setText(supplier.getSupName());
                    lblSupplierAddress.setText(supplier.getSupAddress());
                    lblSupplierContact.setText(supplier.getSupContact());
                    break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setItemCmb() {
        try {
            ArrayList<Item> itemArrayList = ItemModel.getItemList();
            for (Item item : itemArrayList) {
                cmbItemCode.getItems().addAll(item.getItemCode());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cmbItemCodeOnAction(ActionEvent actionEvent) {
        try {
            if (!cmbItemCode.getSelectionModel().isSelected(-1)) {
                ArrayList<Item> itemArrayList = ItemModel.getItemList();
                for (Item item : itemArrayList) {
                    if (item.getItemCode().equalsIgnoreCase(cmbItemCode.getValue().toString())) {
                        lblBrandName.setText(item.getItemBrand());
                        txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                        lblItemName.setText(item.getItemName());
                        cmbSupplierId.setDisable(true);
                        clearButton(true);
                        txtUnitPrice.requestFocus();
                        break;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        try {
            if (!tblSupplies.getItems().isEmpty()) {
                ArrayList<SuppliesDetail> suppliesDetailArrayList = new ArrayList<>();
                for (int i = 0; i < tblSupplies.getItems().size(); i++) {
                    ItemDescriptionTm itemDescriptionTm = (ItemDescriptionTm) tblSupplies.getItems().get(i);
                    suppliesDetailArrayList.add(new SuppliesDetail(lblSuppliesCode.getText(),
                            itemDescriptionTm.getItemCode(),
                            itemDescriptionTm.getQty(),
                            itemDescriptionTm.getUnitPrice()));
                }
                boolean isAdded = SuppliesModel.addSupplies(new Supplies(lblSuppliesCode.getText(),
                        lblDate.getText(),
                        cmbSupplierId.getValue().toString(),
                        suppliesDetailArrayList),Double.parseDouble(lblTotal.getText()));
                if (isAdded) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplies add successful", ButtonType.OK).showAndWait();
                    Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/SuppliesInvoice.jrxml"));
                    Navigation.navigate(Routes.SuppliesForm, loadPane);
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void btnEditOnAction(ActionEvent actionEvent) {
        if (tblSupplies.getItems().isEmpty()) {
            cmbSupplierId.setDisable(false);
            clearButton(false);
            clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure do you want to clear supplies history",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                ItemDescriptionArray.itemDescriptionArrayList.clear();
                cmbSupplierId.setDisable(false);
                clearButton(false);
                setDetails();
            }
        }
    }
    private void clearButton(boolean b){
        picEdit.setVisible(b);
        btnEdit.setVisible(b);
    }
}
