package lk.ijse.techlacomputer.util;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.techlacomputer.db.DBConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;


public class Navigation {
    private static AnchorPane anchorPane;
    private StackPane patten;

    public static void navigate(Routes route, AnchorPane anchorPane) throws IOException {
        Navigation.anchorPane=anchorPane;
        Navigation.anchorPane.getChildren().clear();
        Stage window = (Stage) Navigation.anchorPane.getScene().getWindow();

        switch (route){
            case CreateAccount  :window.setTitle("Create Account Form");
                                initUI("CreateAccountForm",true);
                                break;
            case Login          :window.setTitle("Login Form");
                                initUI("LoginForm",true);
                                 break;
            case LoginBack      :window.setTitle("Login Form");
                                initUI("LoginForm",false);
                                break;
            case AdminDashBoardFrom :window.setTitle("Admin Dashboard");
                                initUI("AdminDashBoardForm",false);
                                break;
            case EmployeesForm  :window.setTitle("Employee Form");
                                initUI("EmployeesForm",true);
                                break;
            case SupplierForm  :window.setTitle("Supplier Form");
                                initUI("SupplierForm",true);
                                break;
            case PlaceOrderForm :window.setTitle("Place Order Form");
                                initUI("PlaceOrderForm",true);
                                break;
            case PlaceRepairForm :window.setTitle("Place Repair Form");
                                initUI("PlaceRepairForm",true);
                                break;
            case SuppliesForm   :window.setTitle("Place Supplies Form");
                                initUI("SuppliesForm",true);
                                break;
            case ReceptionForm  :window.setTitle("Reception Form");
                                initUI("ReceptionDashBoardForm",false);
                                break;
            case StockManageForm:window.setTitle("Stock Mange Form");
                                initUI("StockForm",true);
                                break;
            case CustomerForm   :window.setTitle("Customer Mange Form");
                                initUI("CustomerForm",true);
                                break;
            case IncomeForm     :window.setTitle("Income Form");
                                initUI("IncomeForm",true);
                                break;
            case StockManageDashBoardForm:window.setTitle("Stock Manage Form");
                                initUI("StockManageDashBordForm",false);
                                break;
        }
    }
    public static void initUI(String location,boolean flag) throws IOException {
        if(flag) {
            Parent root=FXMLLoader.load(Navigation.class.getResource("/lk/ijse/techlacomputer/view/" +location+".fxml"));
            anchorPane.getChildren().add(root);
            root.translateXProperty().set(anchorPane.getWidth());

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.3), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(t -> {
                anchorPane.getChildren().remove(anchorPane);
            });
            timeline.play();
            //Navigation.anchorPane.getChildren().add(FXMLLoader.load(Navigation.class.getResource("/lk/ijse/techlacomputer/view/" +location+".fxml")));
        }else{
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(Navigation.class.getResource("/lk/ijse/techlacomputer/view/"+location+".fxml"))));
            stage.centerOnScreen();
        }
    }

    public static void navigateReport(InputStream inputStream) {
        try {
            JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager
                    .fillReport(compileReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

