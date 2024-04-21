package com.mycompany.controller;

import com.mycompany.model.Poste;
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

public class PosteController {

    @FXML
    private TextField titredeposte;


    @FXML
    private Button posadd;

    @FXML
    private Button posupdate;

    @FXML
    private Button posdelete;

    @FXML
    private TableView<Poste> table;

    @FXML
    private TableColumn<Poste, Integer> colpid;

    @FXML
    private TableColumn<Poste, String> coltitredeposte;

    @FXML
    private Poste selectedPoste;

    @FXML
    public void initialize() {

        colpid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coltitredeposte.setCellValueFactory(new PropertyValueFactory<>("titre_poste"));


        try {
            ObservableList<Poste> data = Poste.getAllPostes();
            table.setItems(data);
        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de la récupération des données");
        }

        posadd.setOnAction(event -> handleAddButtonAction());
        posupdate.setOnAction(event -> handleUpdateButtonAction());
        posdelete.setOnAction(event -> handleDeleteButtonAction());

    }

    @FXML
    public void handleAddButtonAction() {
        if (titredeposte.getText().isEmpty() ) {
            Utils.displayError("Veuillez remplir tous les champs");
        } else {
            try {

                    Poste.addPost(titredeposte.getText());
                    Utils.displayInfo("Poste ajouté avec succès");
                    updateFields();

            } catch (SQLException e) {
                Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ajout de poste");
            }
        }
    }

    @FXML
    public void handleUpdateButtonAction() {
        if (selectedPoste == null) {
            selectedPoste = table.getSelectionModel().getSelectedItem();
            if (selectedPoste != null) {
                titredeposte.setText(selectedPoste.getTitre_poste());
            } else {
                Utils.displayError("Veuillez sélectionner un poste à modifier");
            }
        } else {
            if (titredeposte.getText().isEmpty()) {
                Utils.displayError("Veuillez remplir tous les champs");
            } else {
                try {
                    Poste.updatePost(selectedPoste.getId(), titredeposte.getText());
                    Utils.displayInfo("Poste modifié avec succès");
                    updateFields();
                    selectedPoste = null;
                } catch (SQLException e) {
                    Utils.displayErrorAndExit("Une erreur s'est produite lors de la modification de poste");
                }
            }
        }
    }



    @FXML
    public void handleDeleteButtonAction() {
        Poste selectPoste = table.getSelectionModel().getSelectedItem();

        if (selectPoste != null) {
            try {
                Poste.deletePoste(selectPoste.getId());
                table.getItems().remove(selectPoste);
                Utils.displayInfo("Poste supprimé avec succès");
                updateFields();

            } catch (SQLException e) {
                Utils.displayError("Une erreur s'est produite lors de la suppression de la poste");
            }
        } else {
            Utils.displayError("Veuillez sélectionner une Poste à supprimer");
        }
    }


    public void openPosteWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/poste.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("PosteWindow");
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
        this.titredeposte.clear();


        try {
            ObservableList<Poste> data = Poste.getAllPostes();
            table.setItems(data);
        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de la récupération des données");
        }
    }

}