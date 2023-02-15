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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import lk.ijse.techlacomputer.db.ItemDescriptionArray;
import lk.ijse.techlacomputer.model.CustomerModel;
import lk.ijse.techlacomputer.model.ItemModel;
import lk.ijse.techlacomputer.model.RepairModel;
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

public class PlaceRepairFormController {
    public AnchorPane loadPane;
    public JFXButton btnAdd;
    public TableView tblRepair;
    public TableColumn clmItemCode;
    public TableColumn clmItemBrand;
    public TableColumn clmItemName;
    public TableColumn clmUnitPrice;
    public TableColumn clmQty;
    public TableColumn clmTotal;
    public TableColumn clmOption;
    public Label lblRepairId;
    public Label lblDate;
    public Label lblTime;
    public JFXComboBox cmbCusId;
    public Label lblCusName;
    public Label lblCusAddress;
    public Label lblCusContact;
    public JFXTextField txtItemName;
    public JFXTextField txtAmount;
    public Label lblTotal;
    public JFXButton btnPlaceRepair;
    public ImageView picEdit;
    public JFXButton btnEdit;
    public JFXComboBox cmbItemCode;
    public Label lblBrandName;
    public Label lblItemName;
    public Label lblQtyOnHand;
    public Label lblUnitPrice;
    public JFXTextField txtQty;
    public JFXComboBox cmbItemType;
    private String itemCode;
    private Pattern namePattern;
    private Pattern amountPattern;
    private Pattern qtyPattern;

    public void initialize() {
        ItemDescriptionArray.itemDescriptionArrayList.clear();
        setDateAndTime();
        getRepairId();
        initializeTableData();
        setTableData();
        setCmbDetails();
        initializePattern();
        picEdit.setVisible(false);
        btnEdit.setVisible(false);
    }

    private void initializePattern() {
        namePattern = Pattern.compile("([\\w ]{1,})");
        amountPattern = Pattern.compile("^[0-9|.]{1,}$");
        qtyPattern = Pattern.compile("[0-9|.]{1,}");
    }

    private void initializeTableData() {
        clmItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        clmItemBrand.setCellValueFactory(new PropertyValueFactory<>("itemBrand"));
        clmItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        clmUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        clmQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        clmTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        clmOption.setCellValueFactory(new PropertyValueFactory<>("option"));

        tblRepair.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setText((ItemDescriptionTm) newValue);
                btnAdd.setText("Update Cart");
                btnAdd.setStyle("-fx-background-color: #706fd3; -fx-background-radius: 20");
            }
        });
    }

    private void setText(ItemDescriptionTm itemDescriptionTm) {
        cmbItemCode.setValue(itemDescriptionTm.getItemCode());
        txtQty.setText(String.valueOf(itemDescriptionTm.getQty()));
        txtQty.requestFocus();
        itemCode = itemDescriptionTm.getItemCode();
    }

    private void setDetails(Item item) {
        lblBrandName.setText(item.getItemBrand());
        lblItemName.setText(item.getItemName());
        lblQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
        lblUnitPrice.setText(String.valueOf(item.getUnitPrice()));
    }

    private void setTableData() {
        ObservableList<ItemDescriptionTm> tmList = FXCollections.observableArrayList();
        if (!ItemDescriptionArray.itemDescriptionArrayList.isEmpty()) {
            for (ItemDescription itemDescription : ItemDescriptionArray.itemDescriptionArrayList) {
                JFXButton btn = new JFXButton("Delete");
                btn.setStyle("-fx-background-color: #e55039; -fx-background-radius: 20; -fx-text-fill: #ffffff");
                tmList.add(new ItemDescriptionTm(itemDescription.getItemCode(), itemDescription.getItemBrand(), itemDescription.getItemName(),
                        itemDescription.getUnitPrice(), itemDescription.getQty(), itemDescription.getTotal(), btn));
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure do you want to delete this item",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {
                        ItemDescriptionArray.itemDescriptionArrayList.remove(itemDescription);
                        btnAdd.setText("Add to Cart");
                        btnAdd.setStyle("-fx-background-color:  #2f3542 ; -fx-background-radius: 20");
                    }
                    setDetails();
                });
                tblRepair.setItems(tmList);
            }
        } else {
            tblRepair.getItems().clear();
        }
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

    private void getRepairId() {
        try {
            String oldId = RepairModel.getNextRepairId();
            if (oldId != null) {
                String[] split = oldId.split("[-]");
                int lastDigits = Integer.parseInt(split[1]);
                lastDigits++;
                String newRoomId = String.format("RR-%04d", lastDigits);
                lblRepairId.setText(newRoomId);
            } else {
                lblRepairId.setText("RR-0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void setCmbDetails() {
        cmbItemType.getItems().addAll("Laptop", "Computer", "Another accessories");
        try {
            ArrayList<Customer> customerArrayList = CustomerModel.getCustomerList();
            for (Customer customer : customerArrayList) {
                cmbCusId.getItems().addAll(customer.getCusId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setTotal() {
        double total = !txtAmount.getText().equalsIgnoreCase("") ? Double.parseDouble(txtAmount.getText()) : 0;
        for (ItemDescription itemDescription : ItemDescriptionArray.itemDescriptionArrayList) {
            total += itemDescription.getTotal();
        }
        lblTotal.setText(String.valueOf(total));
    }

    private void clear() {
        cmbItemCode.getSelectionModel().clearSelection();
        lblBrandName.setText("Item Brand...");
        lblItemName.setText("Item Name...");
        lblQtyOnHand.setText("Qty On Hand...");
        lblUnitPrice.setText("Unit Price...");
        txtQty.clear();
    }

    private void setDetails() {
        setTableData();
        setTotal();
        clear();
    }

    public void tbnAddOnAction(ActionEvent actionEvent) {
        if (!cmbCusId.getSelectionModel().isSelected(-1)) {
            if (!cmbItemType.getSelectionModel().isSelected(-1)) {
                if (namePattern.matcher(txtItemName.getText()).matches()) {
                    if (amountPattern.matcher(txtAmount.getText()).matches()) {
                        if (!cmbItemCode.getSelectionModel().isSelected(-1)) {
                            if (qtyPattern.matcher(txtQty.getText()).matches()) {
                                if (Integer.parseInt(txtQty.getText()) <= Integer.parseInt(lblQtyOnHand.getText())) {
                                    if (btnAdd.getText().equalsIgnoreCase("Add to cart")) {
                                        if (checkDuplicate()) {
                                            addItem();
                                            setDetails();
                                        } else {
                                            if (checkQtyInOrders()) {
                                                addOrdersForOldOne();
                                                setDetails();
                                            } else {
                                                txtQty.setFocusColor(Paint.valueOf("Red"));
                                                txtQty.requestFocus();
                                            }
                                        }
                                    } else {
                                        updateItem();
                                        btnAdd.setText("Add to Cart");
                                        btnAdd.setStyle("-fx-background-color:  #2f3542 ; -fx-background-radius: 20");
                                        itemCode = "";
                                        setDetails();
                                    }
                                } else {
                                    new Alert(Alert.AlertType.INFORMATION, "Out of stock", ButtonType.OK).showAndWait();
                                    txtQty.setFocusColor(Paint.valueOf("Red"));
                                    txtQty.requestFocus();
                                }
                            } else {
                                txtQty.setFocusColor(Paint.valueOf("Red"));
                                txtQty.requestFocus();
                            }
                        } else {
                            cmbItemCode.setFocusColor(Paint.valueOf("Red"));
                            cmbItemCode.requestFocus();
                        }
                    } else {
                        txtAmount.setFocusColor(Paint.valueOf("Red"));
                        txtAmount.requestFocus();
                    }
                } else {
                    txtItemName.setFocusColor(Paint.valueOf("Red"));
                    txtItemName.requestFocus();
                }
            } else {
                cmbItemType.setFocusColor(Paint.valueOf("Red"));
                cmbItemType.requestFocus();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Please Select the Customer details", ButtonType.OK).showAndWait();
        }
    }

    private void updateItem() {
        for (int i = 0; i < ItemDescriptionArray.itemDescriptionArrayList.size(); i++) {
            if (ItemDescriptionArray.itemDescriptionArrayList.get(i).getItemCode().equalsIgnoreCase(itemCode)) {
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setItemCode(cmbItemCode.getValue().toString());
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setItemBrand(lblBrandName.getText());
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setItemName(lblItemName.getText());
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setUnitPrice(Double.parseDouble(lblUnitPrice.getText()));
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setQty(Integer.parseInt(txtQty.getText()));
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setTotal(Double.parseDouble(lblUnitPrice.getText()) * Double.parseDouble(txtQty.getText()));
                break;
            }
        }
    }

    private void addItem() {
        ItemDescriptionArray.itemDescriptionArrayList.add(new ItemDescription(cmbItemCode.getValue().toString(),
                lblBrandName.getText(),
                lblItemName.getText(),
                Double.parseDouble(lblUnitPrice.getText()),
                Integer.parseInt(txtQty.getText()),
                (Double.parseDouble(lblUnitPrice.getText()) * Double.parseDouble(txtQty.getText()))));
    }

    private boolean checkQtyInOrders() {
        for (ItemDescription itemDescription : ItemDescriptionArray.itemDescriptionArrayList) {
            if (itemDescription.getItemCode().equalsIgnoreCase(cmbItemCode.getValue().toString())) {
                if ((itemDescription.getQty() + Integer.parseInt(txtQty.getText())) <= Integer.parseInt(lblQtyOnHand.getText())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addOrdersForOldOne() {
        for (int i = 0; i < ItemDescriptionArray.itemDescriptionArrayList.size(); i++) {
            if (ItemDescriptionArray.itemDescriptionArrayList.get(i).getItemCode().equalsIgnoreCase(cmbItemCode.getValue().toString())) {
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setQty(
                        ItemDescriptionArray.itemDescriptionArrayList.get(i).getQty() + Integer.parseInt(txtQty.getText())
                );
                ItemDescriptionArray.itemDescriptionArrayList.get(i).setTotal(
                        ItemDescriptionArray.itemDescriptionArrayList.get(i).getTotal() + (Double.parseDouble(lblUnitPrice.getText()) * Double.parseDouble(txtQty.getText()))
                );
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

    public void cmbCusIdOnAction(ActionEvent actionEvent) {
        setItemCmb();
        try {
            ArrayList<Customer> customerArrayList = CustomerModel.getCustomerList();
            for (Customer customer : customerArrayList) {
                if (customer.getCusId().equalsIgnoreCase(cmbCusId.getValue().toString())) {
                    lblCusName.setText(customer.getCusName());
                    lblCusAddress.setText(customer.getCusAddress());
                    lblCusContact.setText(customer.getCusContact());
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

    public void btnPlaceRepairOnAction(ActionEvent actionEvent) {
        try {
            if (!cmbCusId.getSelectionModel().isSelected(-1)) {
                if (!cmbItemType.getSelectionModel().isSelected(-1)) {
                    if (namePattern.matcher(txtItemName.getText()).matches()) {
                        if (amountPattern.matcher(txtAmount.getText()).matches()) {
                            if (!tblRepair.getItems().isEmpty()) {
                                ArrayList<RepairReducesItemDetail> repairReducesItemDetails = new ArrayList<>();
                                for (int i = 0; i < tblRepair.getItems().size(); i++) {
                                    ItemDescriptionTm itemDescriptionTm = (ItemDescriptionTm) tblRepair.getItems().get(i);
                                    repairReducesItemDetails.add(new RepairReducesItemDetail(lblRepairId.getText(),
                                            itemDescriptionTm.getItemCode(),
                                            itemDescriptionTm.getQty(),
                                            itemDescriptionTm.getUnitPrice()));
                                }
                                boolean isAdded = RepairModel.addRepairWithReducesItem(new Repair(lblRepairId.getText(),
                                                cmbItemType.getValue().toString(),
                                                txtItemName.getText(),
                                                lblDate.getText(),
                                                Double.parseDouble(txtAmount.getText()),
                                                cmbCusId.getValue().toString(),
                                                repairReducesItemDetails),
                                        Double.parseDouble(lblTotal.getText()));
                                if (isAdded) {
                                    getNotified();
                                }
                            } else {
                                boolean isAdded = RepairModel.addRepairWithoutReducesItem(new Repair(lblRepairId.getText(),
                                                cmbItemType.getValue().toString(),
                                                txtItemName.getText(),
                                                lblDate.getText(),
                                                Double.parseDouble(txtAmount.getText()),
                                                cmbCusId.getValue().toString(),
                                                null),
                                        Double.parseDouble(lblTotal.getText()));
                                if (isAdded) {
                                    getNotified();
                                }
                            }
                        } else {
                            txtAmount.setFocusColor(Paint.valueOf("Red"));
                            txtAmount.requestFocus();
                        }
                    } else {
                        txtItemName.setFocusColor(Paint.valueOf("Red"));
                        txtItemName.requestFocus();
                    }
                } else {
                    cmbItemType.setFocusColor(Paint.valueOf("Red"));
                    cmbItemType.requestFocus();
                }
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Please Add the Repair details", ButtonType.OK).showAndWait();
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void getNotified() throws IOException {
        new Alert(Alert.AlertType.INFORMATION, "Repair place successful", ButtonType.OK).showAndWait();
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/RepairInvoice.jrxml"));
        Navigation.navigate(Routes.PlaceRepairForm, loadPane);
    }

    public void btnEditOnAction(ActionEvent actionEvent) {
        if (tblRepair.getItems().isEmpty()) {
            cmbCusId.setDisable(false);
            clear();
            cmbItemType.setValue("Item Type...");
            txtItemName.clear();
            txtAmount.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure do you want to clear order history",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                ItemDescriptionArray.itemDescriptionArrayList.clear();
                cmbCusId.setDisable(false);
                setDetails();
            }
        }
    }

    public void cmbItemCodeOnAction(ActionEvent actionEvent) {
        try {
            if (!cmbItemCode.getSelectionModel().isSelected(-1)) {
                ArrayList<Item> itemArrayList = ItemModel.getItemList();
                for (Item item : itemArrayList) {
                    if (item.getItemCode().equalsIgnoreCase(cmbItemCode.getValue().toString())) {
                        setDetails(item);
                        cmbCusId.setDisable(true);
                        picEdit.setVisible(true);
                        btnEdit.setVisible(true);
                        break;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void txtAmountKeyReleaseEvent(KeyEvent keyEvent) {
        setTotal();
    }
}
