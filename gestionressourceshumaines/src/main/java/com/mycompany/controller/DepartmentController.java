package com.mycompany.controller;

import com.mycompany.model.Departement;
import com.mycompany.model.Employe;
import com.mycompany.model.Localisation;
import com.mycompany.util.CustomSpinner;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class DepartmentController {

    @FXML
    private TextField titrededepartement;

    @FXML
    private CustomSpinner depidlocalisation;

    @FXML
    private Button depadd;

    @FXML
    private Button depupdate;

    @FXML
    private Button depdelete;

    @FXML
    private TableView<Departement> table;

    @FXML
    private TableColumn<Departement, Integer> coltid;

    @FXML
    private TableColumn<Departement, String> coltitrededepartement;

    @FXML
    private TableColumn<Departement, Integer> coldepidlocalisation;

    @FXML
    public void initialize() {

        coltid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coltitrededepartement.setCellValueFactory(new PropertyValueFactory<>("nom_Departement"));
        coldepidlocalisation.setCellValueFactory(new PropertyValueFactory<>("id_localisation"));

        try {
            ObservableList<Departement> data = Departement.getAllDepartements();
            table.setItems(data);
        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de la récupération des données");
        }

        depadd.setOnAction(event -> handleAddButtonAction());
        depupdate.setOnAction(event -> handleUpdateButtonAction());
        depdelete.setOnAction(event -> handleDeleteButtonAction());
        this.depidlocalisation.setRange(1, Localisation.countLocalisation(), 1);
    }

    @FXML
    public void handleAddButtonAction() {
        if (titrededepartement.getText().isEmpty() || depidlocalisation.getValue() == null) {
            Utils.displayError("Veuillez remplir tous les champs");
        } else {
            try {
                if (Localisation.checkID(depidlocalisation.getIntValue())) {
                    Departement.addDepartement(titrededepartement.getText(), depidlocalisation.getIntValue());
                    Utils.displayInfo("Département ajouté avec succès");
                    updateFields();
                } else {
                    Utils.displayError("vérifier l'ID de la localisation");
                }
            } catch (SQLException e) {
                Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ajout de manager");
            }
        }
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
        this.depidlocalisation.setRange(1, Localisation.countLocalisation(), 1);

        try {
            ObservableList<Departement> data = Departement.getAllDepartements();
            table.setItems(data);
        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de la récupération des données");
        }
    }

}