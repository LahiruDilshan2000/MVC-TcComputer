package lk.ijse.techlacomputer.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.techlacomputer.db.ItemDescriptionArray;
import lk.ijse.techlacomputer.model.LogModel;
import lk.ijse.techlacomputer.to.User;
import lk.ijse.techlacomputer.util.Navigation;
import lk.ijse.techlacomputer.util.Routes;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public JFXTextField txtUserId;
    public JFXTextField txtPassword;
    public ImageView picHidePass;
    public JFXPasswordField txtPasswordField;
    public ImageView picShowPass;
    public JFXButton btnCancel;
    public JFXButton btnHideShow;
    public AnchorPane pane;
    public JFXButton btnSignup;
    public JFXButton btnSignIn;

    public void initialize() {
        txtPassword.setVisible(false);
        picHidePass.setVisible(false);
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnHideShowOnAction(ActionEvent actionEvent) {
        if (txtPasswordField.isVisible()) {
            txtPassword.setText(txtPasswordField.getText());
            txtPasswordField.setVisible(false);
            txtPassword.setVisible(true);
            picShowPass.setVisible(false);
            picHidePass.setVisible(true);
            return;
        }
        txtPasswordField.setText(txtPassword.getText());
        txtPassword.setVisible(false);
        txtPasswordField.setVisible(true);
        picHidePass.setVisible(false);
        picShowPass.setVisible(true);
    }

    public void btnSignupOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.CreateAccount, pane);
    }

    public void btnSignInOnAction(ActionEvent actionEvent) {
        try {
            if (!txtUserId.getText().equalsIgnoreCase("")) {
                if (txtPassword.isVisible() ? !txtPassword.getText().equalsIgnoreCase("") : !txtPasswordField.getText().equalsIgnoreCase("")) {
                    ResultSet rst = LogModel.getDetails(txtUserId.getText());
                    if (rst.next()) {
                        ItemDescriptionArray.user =new User(rst.getString(1), rst.getString(2), rst.getString(3) );
                        if (txtPassword.isVisible()) {
                            if (rst.getString(2).equalsIgnoreCase(txtPassword.getText())) {
                                checkRole(rst.getString(3));
                            }
                            setFocusPassword();
                        } else {
                            if (rst.getString(2).equalsIgnoreCase(txtPasswordField.getText())) {
                                checkRole(rst.getString(3));
                            }
                            setFocusPasswordField();
                        }
                    } else {
                        setFocusUserId();
                    }
                } else {
                    if (txtPassword.isVisible()) {
                        setFocusPassword();
                    } else {
                        setFocusPasswordField();
                    }
                }
            } else {
                setFocusUserId();
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkRole(String role) {
        try {
            switch (role) {
                case "Admin":
                    System.out.println(role);
                    Navigation.navigate(Routes.AdminDashBoardFrom, pane);
                    break;
                case "Reception":
                    System.out.println(role);
                    Navigation.navigate(Routes.ReceptionForm, pane);
                    break;
                case "StockManager":
                    System.out.println(role);
                    Navigation.navigate(Routes.StockManageDashBoardForm, pane);
                    break;
                case "null":
                    System.out.println(role);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setFocusUserId() {
        txtUserId.setFocusColor(Paint.valueOf("Red"));
        txtUserId.requestFocus();
    }

    private void setFocusPassword() {
        txtPassword.setFocusColor(Paint.valueOf("Red"));
        txtPassword.requestFocus();
    }

    private void setFocusPasswordField() {
        txtPasswordField.setFocusColor(Paint.valueOf("Red"));
        txtPasswordField.requestFocus();
    }
}
