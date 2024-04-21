package com.mycompany.controller;

import com.mycompany.model.Admin;
import com.mycompany.util.EmailValidator;
import com.mycompany.util.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private TextField txtnom;

    @FXML
    private TextField txtprénom;

    @FXML
    private TextField txtemail;

    @FXML
    private PasswordField txtmotdepasse;

    @FXML
    private Button registerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerButton.setOnAction(event -> handleRegisterButtonClick());
    }

    @FXML
    private void handleRegisterButtonClick() {
        String nom = txtnom.getText().strip();
        String prénom = txtprénom.getText().strip();
        String email = txtemail.getText().strip();
        email = email.replace("'", "\"");
        String motdepasse = txtmotdepasse.getText().strip();
        if (nom.isEmpty() || prénom.isEmpty() || email.isEmpty() || motdepasse.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
        } else {
            if (EmailValidator.validateEmail(email)) {
                try {
                    Admin.addAdmin(nom, prénom, email, motdepasse);
                    System.out.println("Inscription réussie");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Inscription réussie");
                    alert.showAndWait();
                    //registerButton.getScene().getWindow().hide();
                    Stage stage = (Stage) registerButton.getScene().getWindow();
                    stage.close();
                    new SigninController().openSignInWindow();

                } catch (Exception e) {
                    Utils.displayErrorAndExit("Une erreur s'est produite");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Message");
                alert.setHeaderText(null);
                alert.setContentText("Email invalide");
                alert.showAndWait();
            }
        }
    }

    public void openSignUpWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Inscription Fenêtre");
            Image hrImage = new Image(getClass().getResourceAsStream("/icons/management.png"));
            stage.getIcons().add(hrImage);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ouverture de la page d'inscription");

        }
    }

}
