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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class DepartmentController {

    @FXML
    private TextField titrededepartement;

    @FXML
    private Label messagelabel;

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
    private Departement selectedDepartement;

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


        if (Localisation.countLocalisation() == 0) {
            depidlocalisation.setDisable(true);
            depidlocalisation.setNull();
            messagelabel.setText("Veuillez ajouter une localisation avant d'ajouter un département");
        } else {
            this.depidlocalisation.setRange(1, Localisation.countLocalisation(), 1);
        }
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
        if (selectedDepartement == null) {
            selectedDepartement = table.getSelectionModel().getSelectedItem();
            if (selectedDepartement != null) {
                titrededepartement.setText(selectedDepartement.getNom_Departement());
                depidlocalisation.setRange(1, Localisation.countLocalisation(), selectedDepartement.getId_localisation());
            } else {
                Utils.displayError("Veuillez sélectionner un département à modifier");
            }
        } else {
            if (titrededepartement.getText().isEmpty() || depidlocalisation.getValue() == null) {
                Utils.displayError("Veuillez remplir tous les champs");
            } else {
                try {
                    if (Localisation.checkID(depidlocalisation.getIntValue())) {
                        Departement.updateDepartement(selectedDepartement.getId(), titrededepartement.getText(), depidlocalisation.getIntValue());
                        Utils.displayInfo("Département modifié avec succès");
                        updateFields();
                        selectedDepartement = null;
                    } else {
                        Utils.displayError("vérifier l'ID de la localisation");
                    }
                } catch (SQLException e) {
                    Utils.displayErrorAndExit("Une erreur s'est produite lors de la modification de département");
                }
            }
        }
    }

    @FXML
    public void handleDeleteButtonAction() {
        Departement selectedDepartement = table.getSelectionModel().getSelectedItem();
        if (selectedDepartement != null) {
            try {
                Employe.deleteEmploye(selectedDepartement.getId());
                table.getItems().remove(selectedDepartement);
                Utils.displayInfo("Departement supprimé avec succès");
                updateFields();

            } catch (SQLException e) {
                Utils.displayError("Une erreur s'est produite lors de la suppression de département");
            }
        } else {
            Utils.displayError("Veuillez sélectionner un département à supprimer");
        }
    }


    public void openDepartmentWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/departement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des départements");
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