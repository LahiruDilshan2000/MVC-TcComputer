package lk.ijse.techlacomputer.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.techlacomputer.model.LogModel;
import lk.ijse.techlacomputer.to.User;
import lk.ijse.techlacomputer.util.Navigation;
import lk.ijse.techlacomputer.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class CreateAccountFormController {
    public JFXButton btnBack;
    public JFXButton btnCancel;
    public JFXButton btnSigUp;
    public JFXTextField txtUserId;
    public JFXTextField txtPassword1;
    public JFXComboBox cmbRole;
    public JFXTextField txtPassword2;
    public AnchorPane logPane;
    private Pattern userNamePattern;
    private Pattern passwordPattern;

    public void initialize() {
        initializePattern();
        cmbRole.getItems().addAll("Reception", "StockManager");
    }

    private void initializePattern() {
        userNamePattern = Pattern.compile("^[a-zA-Z0-9@]{4,}$");
        passwordPattern = Pattern.compile("^[a-zA-Z0-9_]{8,}$");
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.Login, logPane);
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnSigUpOnAction(ActionEvent actionEvent) {
        if (userNamePattern.matcher(txtUserId.getText()).matches()) {
            if (passwordPattern.matcher(txtPassword1.getText()).matches()) {
                if (txtPassword1.getText().equalsIgnoreCase(txtPassword2.getText())) {
                    if (!cmbRole.getSelectionModel().isSelected(-1)) {
                        CreateAccount();
                    }else {
                        setFocusRole();
                    }
                }else {
                    setFocusPassword2();
                }
            }else {
                setFocusPassword1();
            }
        }else {
            setFocusUserName();
        }
    }

    private void CreateAccount() {
        try {
            boolean isAdded = LogModel.addAccount(new User(txtUserId.getText(),
                    txtPassword1.getText(),
                    cmbRole.getValue().toString()));
            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "Account Create Successful", ButtonType.OK).showAndWait();
                Navigation.navigate(Routes.Login, logPane);
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setFocusUserName() {
        txtUserId.setFocusColor(Paint.valueOf("Red"));
        txtUserId.requestFocus();
    }

    private void setFocusPassword1() {
        txtPassword1.setFocusColor(Paint.valueOf("Red"));
        txtPassword1.requestFocus();
    }

    private void setFocusPassword2() {
        txtPassword2.setFocusColor(Paint.valueOf("Red"));
        txtPassword2.requestFocus();
    }

    private void setFocusRole() {
        cmbRole.setFocusColor(Paint.valueOf("Red"));
        cmbRole.requestFocus();
    }
}
