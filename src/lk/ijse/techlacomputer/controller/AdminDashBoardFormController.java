package lk.ijse.techlacomputer.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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

public class AdminDashBoardFormController {
    public AnchorPane loadPane;
    public JFXButton btnDashboard;
    public JFXButton btnEmployee;
    public JFXButton btnSupplier;
    public JFXButton btnSupplies;
    public JFXButton btnItem;
    public JFXButton btnIncome;
    public Label lblTime;
    public Label lblDate;
    public JFXButton btnLogOut;
    public JFXButton btnExit;
    public LineChart lineChart;
    public Label lblIncome;
    public Label lblEmployee;
    public Label lblOrders;
    public Label lblRepairs;
    public Label lblItems;
    public Label lblTitle;
    public Label lblUser;
    public JFXTextField txtUserId;
    public JFXTextField txtPass;
    public JFXPasswordField txtPasswordField;
    public ImageView picHide;
    public ImageView picShow;
    public Pane editPane;
    public JFXButton btnSave;
    public Pane ReportPane1;
    public Pane ReportPane2;
    public JFXButton btnReport;
    private Pattern userNamePattern;
    private Pattern passwordPattern;
    private Button selectedButton;

    public void initialize() {
        initializePane();
        setDateAndTime();
        initializeLineChart();
        initializeLabel();
        setButtonStyle(btnDashboard,true);
        initializePattern();
        setPane();
    }

    private void setPane() {
        ReportPane1.setVisible(false);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(20), ReportPane2);
        translateTransition.setCycleCount(-600);
        translateTransition.play();

    }

    public void btnReportOnAction(ActionEvent actionEvent) {
        reSetButton();
        setButtonStyle(btnReport,false);
        ReportPane1.setVisible(true);
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(20), ReportPane2);
        translateTransition1.setCycleCount(+600);
        translateTransition1.play();
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

    private void initializeLineChart() {
        lblTitle.setText(String.valueOf(LocalDate.now().getYear()) + "  " + (LocalDate.now().getMonth().toString()));
        try {
            ArrayList<Income> incomes = IncomeModel.getIncome(LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue());
            XYChart.Series series = new XYChart.Series();
            if (incomes != null) {
                for (Income income : incomes) {
                    String x = String.valueOf(income.getDate());
                    int y = (int) income.getTotal();
                    series.getData().add(new XYChart.Data(x, y));
                }
                lineChart.getData().addAll(series);
                lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
                series.getNode().setStyle("-fx-stroke: #0fb9b1");

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeLabel() {
        try {
            ArrayList<Income> incomeArrayList = IncomeModel.getIncome(LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue());
            if (incomeArrayList != null) {
                double total = 0;
                for (Income income : incomeArrayList) {
                    total += income.getTotal();
                }
                lblIncome.setText(String.valueOf(total));
            }
            lblEmployee.setText(String.valueOf(EmployeeModel.getEmployeeCount()));
            lblOrders.setText(String.valueOf(OrderModel.getTodayOrder(LocalDate.now().toString())));
            lblRepairs.setText(String.valueOf(RepairModel.getTodayRepair(LocalDate.now().toString())));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        loadDashBoard();
    }

    private void loadDashBoard() {
        reSetButton();
        try {
            Navigation.navigate(Routes.AdminDashBoardFrom, loadPane);
            setButtonStyle(btnDashboard,true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void btnEmployeeOnAction(ActionEvent actionEvent) {
        reSetButton();
        try {
            Navigation.navigate(Routes.EmployeesForm, loadPane);
            setButtonStyle(btnEmployee,true);
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

    public void btnItemOnAction(ActionEvent actionEvent) {
        reSetButton();
        try {
            Navigation.navigate(Routes.StockManageForm, loadPane);
            setButtonStyle(btnItem,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnIncomeOnAction(ActionEvent actionEvent) {
        loadIncome();
    }

    private void loadIncome() {
        reSetButton();
        try {
            Navigation.navigate(Routes.IncomeForm, loadPane);
            setButtonStyle(btnIncome,true);
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

    public void btnLogOutOnAction(ActionEvent actionEvent) {
        try {
            Navigation.navigate(Routes.LoginBack, loadPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reSetButton() {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(btnDashboard);
        buttons.add(btnEmployee);
        buttons.add(btnSupplier);
        buttons.add(btnItem);
        buttons.add(btnIncome);
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

    public void btnExitOnAction(ActionEvent actionEvent) {
        System.exit(0);
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

    public void editPaneOnAction(MouseEvent mouseEvent) {
        editPane.setVisible(false);
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

    public void btnIncomesOnAction(ActionEvent actionEvent) {
        loadIncome();
    }

    public void ReportPane1MouseClickEvent(MouseEvent mouseEvent) {
        ReportPane1.setVisible(false);
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), ReportPane2);
        translateTransition1.setCycleCount(-600);
        translateTransition1.play();
        reSetButton();
        setButton();
    }

    public void btnEmployeeReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/EmployeeReport.jrxml"));
    }

    public void btnCustomerReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/CustomerReport.jrxml"));
    }

    public void btnSupplierReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/SupplierReport.jrxml"));
    }

    public void btnItemReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/ItemReport.jrxml"));
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

    public void btnIncomeReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/IncomeReport.jrxml"));
    }

    public void btnExpensesReportOnAction(ActionEvent actionEvent) {
        Navigation.navigateReport(this.getClass().getResourceAsStream("/lk/ijse/techlacomputer/report/ExpensesReport.jrxml"));
    }
}