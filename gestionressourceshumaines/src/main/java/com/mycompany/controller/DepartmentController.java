package com.mycompany.controller;

import com.mycompany.model.Departement;
import com.mycompany.util.Utils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class DepartmentController {

    @FXML
    private TextField titrededepartement;

    @FXML
    private TextField depidlocalisation;

    @FXML
    private Button depadd;

    @FXML
    private Button depupdate;

    @FXML
    private Button depdelete;

    @FXML
    private TableView<Departement> table;

    @FXML
    private TableColumn<Departement, String> coltitrededepartement;

    @FXML
    private TableColumn<Departement, Integer> coldepidlocalisation;

    @FXML
    public void initialize() {
        depadd.setOnAction(event -> handleAddButtonAction());
        depupdate.setOnAction(event -> handleUpdateButtonAction());
        depdelete.setOnAction(event -> handleDeleteButtonAction());
    }

    @FXML
    public void handleAddButtonAction() {
    }

    @FXML
    public void handleUpdateButtonAction() {
    }

    @FXML
    public void handleDeleteButtonAction() {
    }


    public void openDepartmentWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/departement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Department Window");
            Image hrImage = new Image(getClass().getResourceAsStream("/icons/management.png"));
            stage.getIcons().add(hrImage);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.displayErrorAndExit("An error occurred while opening the page");
        }
    }

    public void updateFields() {
        this.titrededepartement.clear();
        this.depidlocalisation.clear();

        try {
            ObservableList<Departement> data = Departement.getAllDepartements();
            table.setItems(data);
        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de la récupération des données");
        }
    }

}