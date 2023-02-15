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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.techlacomputer.model.ItemModel;
import lk.ijse.techlacomputer.to.Item;
import lk.ijse.techlacomputer.to.ItemTm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

public class StockFormController {
    public AnchorPane loadPane;
    public TableView tblStock;
    public TableColumn clmItemCode;
    public TableColumn clmBrand;
    public TableColumn clmItemName;
    public TableColumn clmQtyOnHand;
    public JFXTextField txtSearch;
    public JFXTextField txtBrand;
    public JFXTextField txtName;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public TableColumn clmUnitPrice;
    private static String itemCode="";
    public TableColumn clmOption;
    public JFXButton btnAddUpdate;
    private Pattern namePattern;
    private Pattern qtyPattern;
    private Pattern amountPattern;

    public void initialize() {
        initializeTableData();
        setTableData();
        initializePattern();
    }

    private void initializePattern() {
        namePattern = Pattern.compile("([\\w ]{1,})");
        qtyPattern = Pattern.compile("[0-9]{1,}");
        amountPattern = Pattern.compile("^[0-9|.]{1,}$");
    }

    private void setTableData() {
        try {
            ArrayList<Item> itemArrayList = ItemModel.getItemList();
            if (itemArrayList != null) {
                setTable(itemArrayList);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setTable(ArrayList<Item> itemArrayList) {
        ObservableList<ItemTm> tmList = FXCollections.observableArrayList();
        for (Item item : itemArrayList) {
            JFXButton btn = new JFXButton("Delete");
            btn.setStyle("-fx-background-color: #e55039; -fx-background-radius: 20; -fx-text-fill: #ffffff;");
            tmList.add(new ItemTm(item.getItemCode(),
                    item.getItemBrand(),
                    item.getItemName(),
                    item.getQtyOnHand(),
                    item.getUnitPrice(),
                    btn));
            btn.setOnAction(event -> {
                deleteItem(item.getItemCode());
                if(!txtSearch.getText().equalsIgnoreCase("")){
                    searchItem();
                }else {
                    setTableData();
                }
            });
        }
        tblStock.setItems(tmList);
    }

    private void initializeTableData() {
        clmItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        clmBrand.setCellValueFactory(new PropertyValueFactory<>("itemBrand"));
        clmItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        clmQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        clmUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        clmOption.setCellValueFactory(new PropertyValueFactory<>("option"));

        tblStock.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setText((ItemTm) newValue);
                btnAddUpdate.setText("Update");
                btnAddUpdate.setStyle("-fx-background-color: #706fd3; -fx-background-radius: 20");
            }
        });
    }

    private void setText(ItemTm itemtm) {
        itemCode = itemtm.getItemCode();
        txtName.setText(itemtm.getItemName());
        txtBrand.setText(itemtm.getItemBrand());
        txtQtyOnHand.setText(String.valueOf(itemtm.getQtyOnHand()));
        txtUnitPrice.setText(String.valueOf(itemtm.getUnitPrice()));
    }

    private void clear() {
        itemCode = "";
        txtName.clear();
        txtBrand.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtSearch.clear();
    }

    public void txtSearchKeyReleaseEvent(KeyEvent keyEvent) {
        searchItem();
    }

    private void searchItem() {
        if (!txtSearch.getText().equalsIgnoreCase("")) {
            try {
                ArrayList<Item> itemArrayList = ItemModel.searchItem(txtSearch.getText());
                if (itemArrayList != null) {
                    setTable(itemArrayList);
                } else {
                    tblStock.getItems().clear();
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            setTableData();
        }
    }

    private void generateNextItemCode() {
        try {
            String oldId = ItemModel.getNextItemCode();
            if(oldId!=null) {
                String[] split = oldId.split("[S]");
                int lastDigits = Integer.parseInt(split[1]);
                lastDigits++;
                String newRoomId = String.format("S%04d", lastDigits);
                itemCode=newRoomId;
            }else{
                itemCode="S0001";
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteItem(String itemCode) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure do you want to delete this Item?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            try {
                boolean isDelete = ItemModel.deleteItem(itemCode);
                if (isDelete) {
                    new Alert(Alert.AlertType.INFORMATION, "Item Delete successfully.", ButtonType.OK).showAndWait();
                    setData();
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void setData(){
        btnAddUpdate.setText("Add to Cart");
        btnAddUpdate.setStyle("-fx-background-color:  #2f3542; -fx-background-radius: 20");
        setTableData();
        clear();
    }

    public void btnAddUpdateOnAction(ActionEvent actionEvent) {
        try {
            if(namePattern.matcher(txtBrand.getText()).matches()) {
                if(namePattern.matcher(txtName.getText()).matches()) {
                    if(qtyPattern.matcher(txtQtyOnHand.getText()).matches()) {
                        if(amountPattern.matcher(txtUnitPrice.getText()).matches()) {
                            if (btnAddUpdate.getText().equalsIgnoreCase("Add")) {
                                generateNextItemCode();
                                boolean isAdded = ItemModel.addItem(new Item(itemCode,
                                        txtBrand.getText(),
                                        txtName.getText(),
                                        Integer.parseInt(txtQtyOnHand.getText()),
                                        Double.parseDouble(txtUnitPrice.getText())));
                                if (isAdded) {
                                    new Alert(Alert.AlertType.INFORMATION, "Item add Successful", ButtonType.OK).showAndWait();
                                    setData();
                                }
                            } else {
                                boolean isUpdate = ItemModel.updateItem(new Item(itemCode,
                                        txtBrand.getText(),
                                        txtName.getText(),
                                        Integer.parseInt(txtQtyOnHand.getText()),
                                        Double.parseDouble(txtUnitPrice.getText())));
                                if (isUpdate) {
                                    new Alert(Alert.AlertType.INFORMATION, "Item Update Successful", ButtonType.OK).showAndWait();
                                    setData();
                                }
                            }
                        }else {
                            txtUnitPrice.setFocusColor(Paint.valueOf("Red"));
                            txtUnitPrice.requestFocus();
                        }
                    }else {
                        txtQtyOnHand.setFocusColor(Paint.valueOf("Red"));
                        txtQtyOnHand.requestFocus();
                    }
                }else {
                    txtName.setFocusColor(Paint.valueOf("Red"));
                    txtName.requestFocus();
                }
            }else {
                txtBrand.setFocusColor(Paint.valueOf("Red"));
                txtBrand.requestFocus();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
