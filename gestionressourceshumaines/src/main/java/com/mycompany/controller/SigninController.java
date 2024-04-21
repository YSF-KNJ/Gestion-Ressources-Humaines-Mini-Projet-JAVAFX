package com.mycompany.controller;

import com.mycompany.model.Admin;
import com.mycompany.util.EmailValidator;
import com.mycompany.util.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SigninController {

    @FXML
    private TextField emailtxt;

    @FXML
    private PasswordField txtmotdepasse1;

    @FXML
    private Button signinButton;

    @FXML
    private ImageView personIcon;

    @FXML
    private ImageView passwordIcon;

    @FXML
    public void initialize() {
        signinButton.setOnAction(event -> handleSignInButtonClick());
    }

    @FXML
    private void handleSignInButtonClick() {
        String email = emailtxt.getText().strip();
        email = email.replace("'", "\"");
        String motDePasse = txtmotdepasse1.getText().strip();
        if (email.isEmpty() || motDePasse.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
        } else {
            try {
                if (EmailValidator.validateEmail(email)) {
                    if (Admin.checkLogin(email, motDePasse)) {
                        signinButton.getScene().getWindow().hide();
                        new HomeController().openHomeWindow();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Email ou mot de passe incorrect");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Email invalide");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Utils.displayErrorAndExit("Une erreur s'est produite");
            }
        }

    }

    public void openSignInWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            Image hrImage = new Image(getClass().getResourceAsStream("/icons/management.png"));
            stage.getIcons().add(hrImage);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ouverture de la page de connexion");

        }
    }
}

