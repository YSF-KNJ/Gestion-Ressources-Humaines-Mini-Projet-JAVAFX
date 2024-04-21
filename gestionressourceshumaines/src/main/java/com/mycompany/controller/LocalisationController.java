package com.mycompany.controller;

import com.mycompany.model.Localisation;
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

public class LocalisationController {

    @FXML
    private TextField adresse;


    @FXML
    private TextField ville;


    @FXML
    private Button locadd;

    @FXML
    private Button locupdate;

    @FXML
    private Button locdelete;

    @FXML
    private TableView<Localisation> table;

    @FXML
    private TableColumn<Localisation, Integer> collid;

    @FXML
    private Localisation selectedLocalisation;

    @FXML
    private TableColumn<Localisation, String> coladresse;

    @FXML
    private TableColumn<Localisation, String> colville;

    @FXML
    public void initialize() {

        collid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coladresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colville.setCellValueFactory(new PropertyValueFactory<>("ville"));


        try {
            ObservableList<Localisation> data = Localisation.getAllLocalisations();
            table.setItems(data);
        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de la récupération des données");
        }

        locadd.setOnAction(event -> handleAddButtonAction());
        locupdate.setOnAction(event -> handleUpdateButtonAction());
        locdelete.setOnAction(event -> handleDeleteButtonAction());

    }

    @FXML
    public void handleAddButtonAction() {
        if (adresse.getText().isEmpty() || ville.getText().isEmpty()) {
            Utils.displayError("Veuillez remplir tous les champs");
        } else {
            try {

                Localisation.addLocalisation(adresse.getText(), ville.getText());
                Utils.displayInfo("Localisation ajouté avec succès");
                updateFields();

            } catch (SQLException e) {
                Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ajout de poste");
            }
        }
    }

    @FXML
    public void handleUpdateButtonAction() {
        if (selectedLocalisation == null) {
            selectedLocalisation = table.getSelectionModel().getSelectedItem();
            if (selectedLocalisation != null) {
                adresse.setText(selectedLocalisation.getAdresse());
                ville.setText(selectedLocalisation.getVille());
            } else {
                Utils.displayError("Veuillez sélectionner une localisation à modifier");
            }
        } else {
            if (adresse.getText().isEmpty() || ville.getText().isEmpty()) {
                Utils.displayError("Veuillez remplir tous les champs");
            } else {
                try {
                    Localisation.updateLocalisation(selectedLocalisation.getId(), adresse.getText(), ville.getText());
                    Utils.displayInfo("Localisation modifiée avec succès");
                    updateFields();
                    selectedLocalisation = null;
                } catch (Exception e) {
                    Utils.displayErrorAndExit("Une erreur s'est produite lors de la modification de la localisation");
                }
            }
        }
    }

    @FXML
    public void handleDeleteButtonAction() {
        Localisation selectLocalisation = table.getSelectionModel().getSelectedItem();

        if (selectLocalisation != null) {
            try {
                Localisation.deleteLocalisation(selectLocalisation.getId());
                table.getItems().remove(selectLocalisation);
                Utils.displayInfo("Localisation supprimé avec succès");
                updateFields();

            } catch (SQLException e) {
                Utils.displayError("Une erreur s'est produite lors de la suppression de localisation");
            }
        } else {
            Utils.displayError("Veuillez sélectionner une Poste à supprimer");
        }
    }


    public void openLocalisationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/localisation.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("LocalisationWindow");
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
        this.adresse.clear();
        this.ville.clear();


        try {
            ObservableList<Localisation> data = Localisation.getAllLocalisations();
            table.setItems(data);
        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de la récupération des données");
        }
    }

}