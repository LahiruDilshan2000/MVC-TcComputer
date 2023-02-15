package lk.ijse.techlacomputer.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import lk.ijse.techlacomputer.db.ItemDescriptionArray;
import lk.ijse.techlacomputer.model.*;
import lk.ijse.techlacomputer.to.Income;
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

public class ReceptionDashBoardFormController {
    public JFXButton btnExit;
    public AnchorPane loadPane;
    public JFXButton btnDashboard;
    public JFXButton btnCustomer;
    public JFXButton btnOrders;
    public JFXButton btnRepair;
    public JFXButton btnSupplier;
    public JFXButton btnSupplies;
    public Label lblTime;
    public Label lblDate;
    public JFXButton btnLogOut;
    public Label lblUser;
    public LineChart lineChart;
    public PieChart pieChart;
    public Label lblOrder;
    public Label lblRepair;
    public Label lblCustomer;
    public Label lblSupplier;
    public Label lblOrderTotal;
    public Label lblRepairTotal;
    public Label lblOrderMonthlyTotal;
    public Label lblRepairMonthlyTotal;
    public Pane ReportPane1;
    public Pane ReportPane2;
    public JFXButton btnReport;
    public Pane editPane;
    public JFXTextField txtUserId;
    public JFXTextField txtPass;
    public JFXPasswordField txtPasswordField;
    public ImageView picHide;
    public ImageView picShow;
    public JFXButton btnSave;
    private Pattern userNamePattern;
    private Pattern passwordPattern;
    public Button selectedButton;


    public void initialize() {
        initializePane();
        setDateAndTime();
        initializeAllCharts();
        initializeLabel();
        setButtonStyle(btnDashboard,true);
        setPane();
        initializePattern();
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

    private void initializeLabel() {
        try {
            lblOrder.setText(String.valueOf(OrderModel.getTodayOrder(LocalDate.now().toString())));
            lblRepair.setText(String.valueOf(RepairModel.getTodayRepair(LocalDate.now().toString())));
            lblCustomer.setText(String.valueOf(CustomerModel.getCustomerTotal()));
            lblSupplier.setText(String.valueOf(SupplierModel.getSupplierTotal()));
            ArrayList<Income> incomes = IncomeModel.getIncome( LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue());
            double  i=0;
            for (Income income:incomes){
                if(income.getIncomeId().startsWith("OD") && income.getDate()==LocalDate.now().getDayOfMonth()){
                    i+=income.getTotal();
                }
            }
            lblOrderTotal.setText(String.valueOf(i));
            i=0;
            for (Income income:incomes){
                if(income.getIncomeId().startsWith("RR") && income.getDate()==LocalDate.now().getDayOfMonth()){
                    i+=income.getTotal();
                }
            }
            lblRepairTotal.setText(String.valueOf(i));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeAllCharts() {
        double order = 0, repair = 0;
        lineChart.setTitle(String.valueOf(LocalDate.now().getYear()) + "  " + (LocalDate.now().getMonth().toString()));
        try {
            ArrayList<Income> incomes = IncomeModel.getIncome( LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue());
            if (incomes != null) {
                XYChart.Series series = new XYChart.Series();
                series.getData().add(new XYChart.Data("0", 0));
                XYChart.Series series1 = new XYChart.Series();
                series1.getData().add(new XYChart.Data("0", 0));
                for (Income income : incomes) {
                    if(income.getIncomeId().startsWith("OD")) {
                        order += income.getTotal();
                        String x = String.valueOf(income.getDate());
                        int y = (int) income.getTotal();
                        series.getData().add(new XYChart.Data(x, y));
                    }else {
                        repair += income.getTotal();
                        String x = String.valueOf(income.getDate());
                        int y = (int) income.getTotal();
                        series1.getData().add(new XYChart.Data(x, y));
                    }
                }
                lineChart.getData().addAll(series);
                lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
                series.getNode().setStyle("-fx-stroke: #8e44ad");
                lineChart.getData().addAll(series1);
                lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
                series1.getNode().setStyle("-fx-stroke: #0fb9b1");
            }
            lblOrderMonthlyTotal.setText(String.valueOf(order));
            lblRepairMonthlyTotal.setText(String.valueOf(repair));
            initializePieChart(order, repair);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializePieChart(double order, double repair) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Orders", order),
                new PieChart.Data("Repairs", repair)
        );
        pieChart.setData(pieChartData);
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

    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        try {
            Navigation.navigate(Routes.ReceptionForm, loadPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnCustomerOnAction(ActionEvent actionEvent) {
        reSetButton();
        try {
            Navigation.navigate(Routes.CustomerForm, loadPane);
            setButtonStyle(btnCustomer,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnOrdersOnAction(ActionEvent actionEvent) {
        reSetButton();
        try {
            Navigation.navigate(Routes.PlaceOrderForm, loadPane);
            setButtonStyle(btnOrders,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnRepairOnAction(ActionEvent actionEvent) {
        reSetButton();
        try {
            Navigation.navigate(Routes.PlaceRepairForm, loadPane);
            setButtonStyle(btnRepair,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnSupplierOnAction(ActionEvent actionEvent) {
        reSetButton();
        try {
            Navigation.navigate(Routes.SupplierForm, loadPane);
            setButtonStyle(btnSupplier,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnSuppliesOnAction(ActionEvent actionEvent) {
        reSetButton();
        try {
            Navigation.navigate(Routes.SuppliesForm, loadPane);
            setButtonStyle(btnSupplies,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reSetButton() {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(btnDashboard);
        buttons.add(btnCustomer);
        buttons.add(btnSupplier);
        buttons.add(btnOrders);
        buttons.add(btnRepair);
        buttons.add(btnSupplies);
        buttons.add(btnReport);
        for (int i = 0; i < buttons.size(); i++) {
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

    public void btnLogOutOnAction(ActionEvent actionEvent) {
        try {
            Navigation.navigate(Routes.LoginBack, loadPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnEditUserOnAction(ActionEvent actionEvent) {
        setText();
        editPane.setVisible(true);
    }

    private void setText() {
        lblUser.setText(ItemDescriptionArray.user.getUserName());
        txtUserId.setText(ItemDescriptionArray.user.getUserName());
        txtPass.setText(ItemDescriptionArray.user.getPassword());
        txtPasswordField.setText(ItemDescriptionArray.user.getPassword());
    }

    public void btnReportOnAction(ActionEvent actionEvent) {
        reSetButton();
        setButtonStyle(btnReport,false);
        ReportPane1.setVisible(true);
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(20), ReportPane2);
        translateTransition1.setCycleCount(+600);
        translateTransition1.play();
    }

    public void btnCustomerReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/CustomerReport.jrxml"));
    }

    public void btnSupplierReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/SupplierReport.jrxml"));
    }

    public void btnOrderReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/OrderReport.jrxml"));
    }

    public void btnRepairReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/RepairReport.jrxml"));
    }

    public void btnSuppliesReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/SuppliesReport.jrxml"));
    }

    public void editPaneOnAction(MouseEvent mouseEvent) {
        editPane.setVisible(false);
    }

    public void btnEditFieldOnAction(ActionEvent actionEvent) {
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
}
