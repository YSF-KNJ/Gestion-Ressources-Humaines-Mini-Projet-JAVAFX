package com.mycompany.controller;

import com.mycompany.model.Departement;
import com.mycompany.model.Employe;
import com.mycompany.model.Poste;
import com.mycompany.util.CustomSpinner;
import com.mycompany.util.EmailValidator;
import com.mycompany.util.Utils;
import javafx.event.ActionEvent;
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
import java.sql.SQLException;

public class EmployeeController {

    @FXML
    private TextField empnom;

    @FXML
    private TextField emprenom;

    @FXML
    private CustomSpinner empidposte;

    @FXML
    private TextField empemail;

    @FXML
    private CustomSpinner empsalaire;

    @FXML
    private TextField emptelephone;

    @FXML
    private CustomSpinner empiddepartement;

    @FXML
    private CustomSpinner empidmanager;

    @FXML
    private Button empadd;

    @FXML
    private Button empupdate;

    @FXML
    private Button empdelete;

    @FXML
    private TableView table;

    @FXML
    private TableColumn colnom;

    @FXML
    private TableColumn colprenom;

    @FXML
    private TableColumn colemail;

    @FXML
    private TableColumn coltelephone;

    @FXML
    private TableColumn colsalaire;

    @FXML
    private TableColumn colidposte;

    @FXML
    private TableColumn coliddepartement;

    @FXML
    private TableColumn colidmanger;

    @FXML
    public void initialize() {

        empadd.setOnAction(event -> handleAddButtonAction());
        empupdate.setOnAction(event -> handleUpdateButtonAction());
        empdelete.setOnAction(event -> handleDeleteButtonAction());

        empidposte.setRange(1, Poste.countPost(), 1);
        empiddepartement.setRange(1, Departement.countDepartement(), 1);
        empidmanager.setRange(1, Employe.countEmployes(), 1);
        empsalaire.setRange(0, Integer.MAX_VALUE, Employe.getSalaryAvg());

    }

    @FXML
    private void handleAddButtonAction() {
        if (empnom.getText().isEmpty() || empsalaire.getValue() == null || empidposte.getValue() == null || empiddepartement.getValue() == null || empidmanager.getValue() == null || empemail.getText().isEmpty() || emptelephone.getText().isEmpty()) {
            Utils.displayError("Veuillez remplir tous les champs");
        } else {
            if (!EmailValidator.validateEmail(empemail.getText())) {
                Utils.displayError("Veuillez entrer un email valide");
            } else {

                try {
                    if (Poste.checkID(empidposte.getIntValue()) && Employe.checkID(empidmanager.getIntValue()) && Departement.checkID(empiddepartement.getIntValue())) {
                        Employe.addEmploye(emprenom.getText(),
                                empnom.getText(),
                                empemail.getText(),
                                emptelephone.getText(),
                                empsalaire.getIntValue(),
                                empidposte.getIntValue(),
                                empiddepartement.getIntValue(),
                                empidmanager.getIntValue());
                        Utils.displayInfo("Employé ajouté avec succès");
                        clearFields();

                    } else {
                        Utils.displayError("vérifier les identifiants de poste, de manager et de département");
                    }
                } catch (SQLException e) {
                    Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ajout de l'employé");
                }

            }

        }
    }

    @FXML
    private void handleUpdateButtonAction() {
        System.out.println("Update button clicked");
    }

    @FXML
    private void handleDeleteButtonAction() {
        System.out.println("Delete button clicked");
    }

    @FXML
    public void openEmployeeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/employee.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des employés");
            Image hrImage = new Image(getClass().getResourceAsStream("/icons/management.png"));
            stage.getIcons().add(hrImage);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ouverture de la page d'inscription");
        }
    }

    public void clearFields() {
        this.empnom.clear();
        this.emprenom.clear();
        this.empemail.clear();
        this.emptelephone.clear();
        this.empidposte.setRange(1, Poste.countPost(), 1);
        this.empiddepartement.setRange(1, Departement.countDepartement(), 1);
        this.empidmanager.setRange(1, Employe.countEmployes(), 1);
        this.empsalaire.setRange(0, Integer.MAX_VALUE, Employe.getSalaryAvg());
    }
}

