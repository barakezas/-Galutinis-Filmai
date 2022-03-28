package com.example.filmai.controller;

import com.example.filmai.MainApplication;
import com.example.filmai.model.User;
import com.example.filmai.model.UserDao;
import com.example.filmai.utils.BCryptPassword;
import com.example.filmai.utils.Validation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private Label reLoginStatus;
    @FXML
    private TextField reUsername;
    @FXML
    private PasswordField rePassword;
    @FXML
    private PasswordField reConfirmPassword;
    @FXML
    private TextField reEmail;
    @FXML
    private Button reRegister;

    @FXML
    private CheckBox isAdminCheckBox;

    @FXML
    public void reOnRegisterButtonClick(ActionEvent event) throws IOException {
        String reUsername2 = reUsername.getText();
        String rePassword2 = rePassword.getText();
        String reConfirmPassword2 = reConfirmPassword.getText();
        String reEmail2 = reEmail.getText();

        if (Validation.isValidUsername(reUsername2) && Validation.isValidPassword(rePassword2) && Validation.isValidPassword(reConfirmPassword2) && Validation.isValidEmail(reEmail2)) {
            reLoginStatus.setText("Duomenys įvesti teisingai");

            String passwordBCrypt = BCryptPassword.hashPassword(rePassword2);

            boolean isAdmin = isAdminCheckBox.isSelected();
            User user = new User(reUsername2, passwordBCrypt, reEmail2, isAdmin);
            UserDao.create(user);

            goToLogin(event);
        }
        if (!Validation.isValidEmail(reEmail2)) reLoginStatus.setText("Blogai įvestas el. pašto adresas");
        if (!rePassword2.equals(reConfirmPassword2)) reLoginStatus.setText("Slaptažodžiai nesutampa");
        if (!Validation.isValidPassword(reConfirmPassword2))
            reLoginStatus.setText("Blogai įvestas patvirtinimo slaptažodis");
        if (!Validation.isValidPassword(rePassword2)) reLoginStatus.setText("Blogai įvestas slaptažodis");
        if (!Validation.isValidUsername(reUsername2)) reLoginStatus.setText("Blogai įvestas vartotojo vardas");
    }

    @FXML
    public void goToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(MainApplication.class.getResource("login-view.fxml"));
        Stage LoginStage = new Stage();
        LoginStage.setTitle("Prisijungimo langas");
        LoginStage.setScene(new Scene(root, 600, 400));
        LoginStage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}
