package com.mycompany.controller;
import com.mycompany.util.ErrorUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private TextField txtnom;
    @FXML
    private TextField txtemail;
    @FXML
    private TextField txtdate;
    @FXML
    private TextField txtgenre;
    @FXML
    private TextField txtgrade;
    @FXML
    private TableView<?> table;
    @FXML
    private TableColumn<?, ?> idcol;
    @FXML
    private TableColumn<?, ?> nomcol;
    @FXML
    private TableColumn<?, ?> emailcol;
    @FXML
    private TableColumn<?, ?> datecol;
    @FXML
    private TableColumn<?, ?> gradecol;
    @FXML
    private TableColumn<?, ?> depprofcol;
    @FXML
    private TableColumn<?, ?> genrecol;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setOnAction(event -> handleAddButton());
        updateButton.setOnAction(event -> handleUpdateButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
    }

    private void handleAddButton() {
    }

    private void handleUpdateButton() {
    }

    private void handleDeleteButton() {
    }

    public void openEmployeeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/employee.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Employee");
            Image hrImage = new Image(getClass().getResourceAsStream("/icons/management.png"));
            stage.getIcons().add(hrImage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorUtils.displayErrorAndExit("Une erreur s'est produite lors de l'ouverture de la page de employee");
        }
    }
}

