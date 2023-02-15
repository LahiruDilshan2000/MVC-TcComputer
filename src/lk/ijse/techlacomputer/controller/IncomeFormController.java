package lk.ijse.techlacomputer.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import lk.ijse.techlacomputer.model.IncomeModel;
import lk.ijse.techlacomputer.model.OutcomeModel;
import lk.ijse.techlacomputer.to.Income;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class IncomeFormController {
    public JFXComboBox cmbYear;
    public JFXComboBox cmbMonth;
    public JFXComboBox cmbOutcomeYear;
    public JFXComboBox cmbOutcomeMonth;
    public Label lblIncomeTotal;
    public LineChart lineChart;
    public LineChart outcomeLineChart;
    public Label lblEarning;
    public Label lblExpenses;
    public PieChart pieChart;
    public Label lblOutComeTotal;

    public void initialize() {
        initializeComboBox();
        initializeLineChart(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        setIncomeTotal(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        setOutComeTotal(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        initializePieChart();
        initializeOutcomeLineChart(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
    }

    private void setOutComeTotal(int year, int month) {
        try {
            lblOutComeTotal.setText(String.valueOf(OutcomeModel.getTotalOutCome(year, month)));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializePieChart() {
        try {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Income", IncomeModel.getTotalIncome(Integer.parseInt(cmbYear.getValue().toString()),
                            getMonthValue(cmbMonth.getValue().toString()))),
                    new PieChart.Data("Expenses", OutcomeModel.getTotalOutCome(Integer.parseInt(cmbOutcomeYear.getValue().toString()),
                            getMonthValue(cmbOutcomeMonth.getValue().toString())))
            );
            pieChart.setData(pieChartData);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setIncomeTotal(int year, int month) {
        try {
            lblIncomeTotal.setText(String.valueOf(IncomeModel.getTotalIncome(year, month)));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeOutcomeLineChart(int year, int month) {
        lblExpenses.setText(String.valueOf(year) + "  " + cmbOutcomeMonth.getValue().toString());
        try {
            ArrayList<Income> outcomes = OutcomeModel.getSelectedOutcome(year, month);
            XYChart.Series series = new XYChart.Series();
            if (!outcomes.isEmpty()) {
                for (Income outcome : outcomes) {
                    String x = String.valueOf(outcome.getDate());
                    int y = (int) outcome.getTotal();
                    series.getData().add(new XYChart.Data(x, y));
                }
                outcomeLineChart.getData().clear();
                outcomeLineChart.getData().addAll(series);
                outcomeLineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
                series.getNode().setStyle("-fx-stroke: #3ae374");

            } else {
                outcomeLineChart.getData().clear();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeComboBox() {
        cmbMonth.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        cmbMonth.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        cmbYear.setValue(LocalDate.now().getYear());
        cmbOutcomeMonth.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        cmbOutcomeMonth.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        cmbOutcomeYear.setValue(LocalDate.now().getYear());
        try {
            ArrayList<Integer> incomeYearList = IncomeModel.getYear();
            if (incomeYearList != null) {
                for (int i : incomeYearList) {
                    cmbYear.getItems().add(i);
                }
            }
            ArrayList<Integer> outcomeYearList = OutcomeModel.getYear();
            if (outcomeYearList != null) {
                for (int i : outcomeYearList) {
                    cmbOutcomeYear.getItems().add(i);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeLineChart(int year, int month) {
        lblEarning.setText(String.valueOf(year) + "  " + cmbMonth.getValue().toString());
        try {
            ArrayList<Income> incomes = IncomeModel.getIncome(year, month);
            XYChart.Series series = new XYChart.Series();
            if (!incomes.isEmpty()) {
                for (Income income : incomes) {
                    String x = String.valueOf(income.getDate());
                    int y = (int) income.getTotal();
                    series.getData().add(new XYChart.Data(x, y));
                }
                lineChart.getData().clear();
                lineChart.getData().addAll(series);
                lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
                series.getNode().setStyle("-fx-stroke: #EA2027");

            } else {
                lineChart.getData().clear();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getMonthValue(String month) {
        switch (month) {
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
        }
        return -1;
    }

    public void cmbMonthOnAction(ActionEvent actionEvent) {
        if(!cmbYear.getSelectionModel().isSelected(-1)) {
            initializeLineChart(Integer.parseInt(cmbYear.getValue().toString()),
                    getMonthValue(cmbMonth.getValue().toString()));
            setIncomeTotal(Integer.parseInt(cmbYear.getValue().toString()),
                    getMonthValue(cmbMonth.getValue().toString()));
            initializePieChart();
        }else{
            cmbYear.setFocusColor(Paint.valueOf("Red"));
            cmbYear.requestFocus();
        }
    }

    public void cmbOutcomeMonthOnAction(ActionEvent actionEvent) {
        if(!cmbOutcomeYear.getSelectionModel().isSelected(-1)) {
            initializeOutcomeLineChart(Integer.parseInt(cmbOutcomeYear.getValue().toString()),
                    getMonthValue(cmbOutcomeMonth.getValue().toString()));
            setOutComeTotal(Integer.parseInt(cmbOutcomeYear.getValue().toString()),
                    getMonthValue(cmbOutcomeMonth.getValue().toString()));
            initializePieChart();
        }else {
            cmbOutcomeYear.setFocusColor(Paint.valueOf("Red"));
            cmbOutcomeYear.requestFocus();
        }
    }
}
