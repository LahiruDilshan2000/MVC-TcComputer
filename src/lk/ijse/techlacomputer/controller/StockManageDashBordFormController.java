package lk.ijse.techlacomputer.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import lk.ijse.techlacomputer.db.ItemDescriptionArray;
import lk.ijse.techlacomputer.model.ItemModel;
import lk.ijse.techlacomputer.model.LogModel;
import lk.ijse.techlacomputer.to.Item;
import lk.ijse.techlacomputer.to.User;
import lk.ijse.techlacomputer.util.Navigation;
import lk.ijse.techlacomputer.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class StockManageDashBordFormController {
    public JFXButton btnExit;
    public AnchorPane loadPane;
    public JFXButton btnDashboard;
    public JFXButton btnItem;
    public JFXButton btnSupplier;
    public JFXButton btnSupplies;
    public Label lblTime;
    public Label lblDate;
    public JFXButton btnLogOut;
    public LineChart lineChart;
    public Label lblQty;
    public TableView tblItem;
    public TableColumn clmCode;
    public TableColumn clmName;
    public TableColumn clmQty;
    public Label lblStockCount;
    public Label lblStockPercentage;
    public JFXButton btnUpdateStock;
    public JFXButton btnAddNewStock;
    public JFXComboBox cmbItemName;
    public JFXButton btnUpdate;
    public Label lblUser;
    public JFXTextField txtUserId;
    public JFXTextField txtPass;
    public JFXPasswordField txtPasswordField;
    public ImageView picHide;
    public ImageView picShow;
    public JFXButton btnSave;
    public Pane editPane;
    public JFXButton btnReport;
    public Pane ReportPane1;
    public Pane ReportPane2;
    private Pattern userNamePattern;
    private Pattern passwordPattern;
    private Button selectedButton;

    public void initialize(){
        setDateAndTime();
        initializePane();
        initializeBarChart();
        setButtonStyle(btnDashboard,true);
        initializeTable();
        setItemDetails();
        initializePattern();
        setPane();
    }

    private void setPane() {
        ReportPane1.setVisible(false);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(20), ReportPane2);
        translateTransition.setCycleCount(-600);
        translateTransition.play();

        ReportPane1.setOnMouseClicked(event -> {
            reSetButton();
            ReportPane1.setVisible(false);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), ReportPane2);
            translateTransition1.setCycleCount(-600);
            translateTransition1.play();
            setButton();
        });

    }

    private void setDateAndTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd / MM / yyyy");
        lblDate.setText(String.valueOf(LocalDate.now().format(formatter)));
        setTime();
    }

    private void setTime() {
        Timeline time = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm   a");
                    lblTime.setText(LocalDateTime.now().format(formatter));

                }), new KeyFrame(Duration.seconds(1)));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    private void initializePattern() {
        userNamePattern = Pattern.compile("^[a-zA-Z0-9@]{4,}$");
        passwordPattern = Pattern.compile("^[a-zA-Z0-9_]{8,}$");
    }

    private void initializePane() {
        picHide.setVisible(false);
        editPane.setVisible(false);
        txtPass.setVisible(false);
        btnSave.setVisible(false);
        setText();
        setEdit(false);
    }

    private void setEdit(boolean b) {
        txtUserId.setEditable(b);
        txtPass.setEditable(b);
        txtPasswordField.setEditable(b);
    }

    private void setItemDetails() {
        try {
            ArrayList<Item>itemArrayList=ItemModel.getItemList();
            int count=0;
            if(itemArrayList!=null){
                for (Item item: itemArrayList){
                    cmbItemName.getItems().add(item.getItemName());
                    count+=item.getQtyOnHand();
                }
            }
            lblStockCount.setText(String.valueOf(count));
            lblStockPercentage.setText(String.valueOf((count/1000)*100));
            addTableData();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addTableData() {
        try {
            ArrayList<Item>itemArrayList=ItemModel.getRemaningItems();
            ObservableList<Item> tmList = FXCollections.observableArrayList();
            for (Item item : itemArrayList) {
                tmList.add(item);
            }
            tblItem.setItems(tmList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeTable() {
        clmCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        clmQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
    }

    private void initializeBarChart() {
        try {
            ArrayList<Item> items= ItemModel.getItemList();
            XYChart.Series series=new XYChart.Series();
            if(!items.isEmpty()){
                for(Item item: items) {
                    String x=item.getItemName();
                    int y= item.getQtyOnHand();
                    series.getData().add(new XYChart.Data(x,y));
                }
                lineChart.getData().addAll(series);
                lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
                series.getNode().setStyle("-fx-stroke: #00b894");

            }else {
                lineChart.getData().clear();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        reSetButton();
        try {
            Navigation.navigate(Routes.StockManageDashBoardForm,loadPane);
            setButtonStyle(btnDashboard,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnItemOnAction(ActionEvent actionEvent) {
        addStock();
    }

    private void addStock() {
        reSetButton();
        try {
            Navigation.navigate(Routes.StockManageForm,loadPane);
            setButtonStyle(btnItem,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnSupplierOnAction(ActionEvent actionEvent) {
        reSetButton();
        try {
            Navigation.navigate(Routes.SupplierForm,loadPane);
            setButtonStyle(btnSupplier,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnSuppliesOnAction(ActionEvent actionEvent) {
        updateStock();
    }

    private void updateStock() {
        reSetButton();
        try {
            Navigation.navigate(Routes.SuppliesForm,loadPane);
            setButtonStyle(btnSupplies,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {
        reSetButton();
        try {
            Navigation.navigate(Routes.LoginBack,loadPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reSetButton(){
        ArrayList<Button>buttons=new ArrayList<>();
        buttons.add(btnDashboard);
        buttons.add(btnSupplier);
        buttons.add(btnItem);
        buttons.add(btnSupplies);
        buttons.add(btnReport);
        for(int i=0; i<buttons.size();i++){
            buttons.get(i).setStyle("-fx-background-color: transparent;\n" +
                    "    -fx-text-fill: #dcdde1;");
        }
    }
    private void setButtonStyle(Button btn,boolean flag) {
        if(flag) {
            selectedButton=btn;
            setButton();
        }else {
            btn.setStyle("-fx-background-color: #5352ed;\n" +
                    "    -fx-border-color: WHITE;\n" +
                    "    -fx-border-width: 0px 0px 0px 5px;\n" +
                    "    -fx-text-fill: WHITE;");
        }
    }

    private void setButton() {
        selectedButton.setStyle("-fx-background-color: #5352ed;\n" +
                "    -fx-border-color: WHITE;\n" +
                "    -fx-border-width: 0px 0px 0px 5px;\n" +
                "    -fx-text-fill: WHITE;");
    }

    public void btnUpdateStockOnAction(ActionEvent actionEvent) {
        updateStock();
    }

    public void btnAddNewStockOnAction(ActionEvent actionEvent) {
        addStock();
    }

    public void cmbItemNameOnAction(ActionEvent actionEvent) {
        try {
            ArrayList<Item>itemArrayList=ItemModel.getItemList();
            if (itemArrayList!=null){
                for (Item item:itemArrayList){
                    if (item.getItemName().equalsIgnoreCase(cmbItemName.getValue().toString())){
                        lblQty.setText(String.valueOf(item.getQtyOnHand()));
                        break;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        updateStock();
    }

    public void btnEditUserOnAction(ActionEvent actionEvent) {
        setText();
        editPane.setVisible(true);
    }

    public void editPaneOnAction(MouseEvent mouseEvent) {
        editPane.setVisible(false);
    }

    private void setText() {
        lblUser.setText(ItemDescriptionArray.user.getUserName());
        txtUserId.setText(ItemDescriptionArray.user.getUserName());
        txtPass.setText(ItemDescriptionArray.user.getPassword());
        txtPasswordField.setText(ItemDescriptionArray.user.getPassword());
    }

    public void btnEditFieldONAction(ActionEvent actionEvent) {
        setEdit(true);
        btnSave.setVisible(true);
    }

    public void btnShowHideOnAction(ActionEvent actionEvent) {
        if (picShow.isVisible()) {
            txtPass.setText(txtPasswordField.getText());
            txtPasswordField.setVisible(false);
            txtPass.setVisible(true);
            picShow.setVisible(false);
            picHide.setVisible(true);
        } else {
            txtPasswordField.setText(txtPass.getText());
            txtPass.setVisible(false);
            txtPasswordField.setVisible(true);
            picHide.setVisible(false);
            picShow.setVisible(true);
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (userNamePattern.matcher(txtUserId.getText()).matches()) {
            if (txtPasswordField.isVisible()) {
                if (passwordPattern.matcher(txtPasswordField.getText()).matches()) {
                    updateUser();
                } else {
                    txtPasswordField.setFocusColor(Paint.valueOf("Red"));
                    txtPasswordField.requestFocus();
                }
            } else {
                if (passwordPattern.matcher(txtPass.getText()).matches()) {
                    updateUser();

                } else {
                    txtPass.setFocusColor(Paint.valueOf("Red"));
                    txtPass.requestFocus();
                }
            }
        } else {
            txtUserId.setFocusColor(Paint.valueOf("Red"));
            txtUserId.requestFocus();
        }
    }
    private void updateUser() {
        try {
            User user = new User(txtUserId.getText(),
                    txtPasswordField.isVisible() ? txtPasswordField.getText() : txtPass.getText(),
                    ItemDescriptionArray.user.getRole());
            boolean isUpdate = LogModel.updateDetails(user,
                    ItemDescriptionArray.user.getUserName());
            if (isUpdate) {
                new Alert(Alert.AlertType.INFORMATION, "User details update successful", ButtonType.OK).showAndWait();
                ItemDescriptionArray.user = user;
                setText();
                setEdit(false);
                btnSave.setVisible(false);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnReportOnAction(ActionEvent actionEvent) {
        reSetButton();
        setButtonStyle(btnReport,false);
        ReportPane1.setVisible(true);
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(20), ReportPane2);
        translateTransition1.setCycleCount(+600);
        translateTransition1.play();
    }

    public void btnSupplierReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/SupplierReport.jrxml"));
    }

    public void btnItemReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/ItemReport.jrxml"));
    }

    public void btnSuppliesReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/SuppliesReport.jrxml"));
    }
}
