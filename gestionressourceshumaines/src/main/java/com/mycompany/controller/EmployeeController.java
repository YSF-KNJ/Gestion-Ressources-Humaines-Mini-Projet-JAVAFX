package com.mycompany.controller;

import com.mycompany.model.Departement;
import com.mycompany.model.Employe;
import com.mycompany.model.Poste;
import com.mycompany.util.CustomSpinner;
import com.mycompany.util.EmailValidator;
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
    private CheckBox managercheckbox;

    @FXML
    private Button empadd;

    @FXML
    private Button empupdate;

    @FXML
    private Button empdelete;

    @FXML
    private TableView<Employe> table;

    @FXML
    private Employe selectedEmploye;

    @FXML
    private TableColumn<Employe, Integer> colid;

    @FXML
    private TableColumn<Employe, String> colnom;

    @FXML
    private TableColumn<Employe, String> colprenom;

    @FXML
    private TableColumn<Employe, String> colemail;

    @FXML
    private TableColumn<Employe, String> coltelephone;

    @FXML
    private TableColumn<Employe, Integer> colsalaire;

    @FXML
    private TableColumn<Employe, Integer> colidposte;

    @FXML
    private TableColumn<Employe, Integer> coliddepartement;

    @FXML
    private TableColumn<Employe, Integer> colidmanger;

    @FXML
    public void initialize() {
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colnom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colprenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        coltelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colsalaire.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        colidposte.setCellValueFactory(new PropertyValueFactory<>("idPoste"));
        coliddepartement.setCellValueFactory(new PropertyValueFactory<>("idDepartement"));
        colidmanger.setCellValueFactory(new PropertyValueFactory<>("idManager"));

        try {
            ObservableList<Employe> data = Employe.getAllEmployes();
            table.setItems(data);
        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de la récupération des données");
        }

        empadd.setOnAction(event -> handleAddButtonAction());
        empupdate.setOnAction(event -> handleUpdateButtonAction());
        empdelete.setOnAction(event -> handleDeleteButtonAction());
        empsalaire.setRange(0, Integer.MAX_VALUE, Employe.getSalaryAvg());

        if (Poste.countPost() == 0) {
            empidposte.setDisable(true);
            empidposte.setNull();
        } else {
            empidposte.setRange(1, Poste.countPost(), 1);
        }

        if (Departement.countDepartement() == 0) {
            empiddepartement.setDisable(true);
            empiddepartement.setNull();
        } else {
            empiddepartement.setRange(1, Departement.countDepartement(), 1);
        }

        if(Employe.countEmployes() == 0){
            managercheckbox.setSelected(true);
            empidmanager.setVisible(false);
            empidmanager.setDisable(true);
            empidmanager.setNull();
        } else {
            empidmanager.setRange(1, Employe.countEmployes(), 1);
        }

        managercheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            empidmanager.setVisible(!newValue);
        });

    }

    @FXML
    private void handleAddButtonAction() {
        if (!managercheckbox.isSelected()) {
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
                            updateFields();
                        } else {
                            Utils.displayError("vérifier les identifiants de poste, de manager et de département");
                        }
                    } catch (SQLException e) {
                        Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ajout de l'employé");
                    }

                }

            }
        } else {
            if (empnom.getText().isEmpty() || empsalaire.getValue() == null || empidposte.getValue() == null || empiddepartement.getValue() == null || empemail.getText().isEmpty() || emptelephone.getText().isEmpty()) {
                Utils.displayError("Veuillez remplir tous les champs");
            } else {
                if (!EmailValidator.validateEmail(empemail.getText())) {
                    Utils.displayError("Veuillez entrer un email valide");
                } else {

                    try {
                        if (Poste.checkID(empidposte.getIntValue()) && Departement.checkID(empiddepartement.getIntValue())) {
                            Employe.addManger(emprenom.getText(),
                                    empnom.getText(),
                                    empemail.getText(),
                                    emptelephone.getText(),
                                    empsalaire.getIntValue(),
                                    empidposte.getIntValue(),
                                    empiddepartement.getIntValue());
                            Utils.displayInfo("Manager ajouté avec succès");
                            updateFields();
                        } else {
                            Utils.displayError("vérifier les identifiants de poste et de département");
                        }
                    } catch (SQLException e) {
                        Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ajout de manager");
                    }

                }

            }
        }
    }

    @FXML
    private void handleUpdateButtonAction() {
        if (selectedEmploye == null) {
            selectedEmploye = table.getSelectionModel().getSelectedItem();
            if (selectedEmploye != null) {
                empnom.setText(selectedEmploye.getNom());
                emprenom.setText(selectedEmploye.getPrenom());
                empemail.setText(selectedEmploye.getEmail());
                emptelephone.setText(selectedEmploye.getTelephone());
                empidposte.setRange(1, Poste.countPost(), selectedEmploye.getIdPoste());
                empiddepartement.setRange(1, Departement.countDepartement(), selectedEmploye.getIdDepartement());
                empidmanager.setRange(1, Employe.countEmployes(), selectedEmploye.getIdManager());
                empsalaire.setRange(0, Integer.MAX_VALUE, selectedEmploye.getSalaire());
            } else {
                Utils.displayError("Veuillez sélectionner un employé à modifier");
            }
        } else {
            try {
                if (!managercheckbox.isSelected()) {
                    if (empnom.getText().isEmpty() || empsalaire.getValue() == null || empidposte.getValue() == null || empiddepartement.getValue() == null || empidmanager.getValue() == null || empemail.getText().isEmpty() || emptelephone.getText().isEmpty()) {
                        Utils.displayError("Veuillez remplir tous les champs");
                    } else {
                        if (!EmailValidator.validateEmail(empemail.getText())) {
                            Utils.displayError("Veuillez entrer un email valide");
                        } else {

                            try {
                                if (Poste.checkID(empidposte.getIntValue()) && Employe.checkID(empidmanager.getIntValue()) && Departement.checkID(empiddepartement.getIntValue())) {
                                    Employe.updateEmploye(selectedEmploye.getId(),
                                            emprenom.getText(),
                                            empnom.getText(),
                                            empemail.getText(),
                                            emptelephone.getText(),
                                            empsalaire.getIntValue(),
                                            empidposte.getIntValue(),
                                            empiddepartement.getIntValue(),
                                            empidmanager.getIntValue());

                                    Utils.displayInfo("Employé mis à jour");
                                    updateFields();
                                    selectedEmploye = null;
                                } else {
                                    Utils.displayError("vérifier les identifiants de poste, de manager et de département");
                                }
                            } catch (SQLException e) {
                                Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ajout de l'employé");
                            }

                        }

                    }
                } else {
                    if (empnom.getText().isEmpty() || empsalaire.getValue() == null || empidposte.getValue() == null || empiddepartement.getValue() == null || empemail.getText().isEmpty() || emptelephone.getText().isEmpty()) {
                        Utils.displayError("Veuillez remplir tous les champs");
                    } else {
                        if (!EmailValidator.validateEmail(empemail.getText())) {
                            Utils.displayError("Veuillez entrer un email valide");
                        } else {

                            try {
                                if (Poste.checkID(empidposte.getIntValue()) && Departement.checkID(empiddepartement.getIntValue())) {
                                    Employe.updateManager(selectedEmploye.getId(),
                                            emprenom.getText(),
                                            empnom.getText(),
                                            empemail.getText(),
                                            emptelephone.getText(),
                                            empsalaire.getIntValue(),
                                            empidposte.getIntValue(),
                                            empiddepartement.getIntValue());
                                    Utils.displayInfo("manager mis à jour avec succès");
                                    updateFields();
                                    selectedEmploye = null;
                                } else {
                                    Utils.displayError("vérifier les identifiants de poste et de département");
                                }
                            } catch (SQLException e) {
                                Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ajout de manager");
                            }

                        }

                    }
                }
            } catch (Exception e) {
                Utils.displayError("Une erreur s'est produite lors de la mise à jour de l'employé");
            }
        }
    }

    @FXML
    private void handleDeleteButtonAction() {
        Employe selectedEmploye = table.getSelectionModel().getSelectedItem();
        if (selectedEmploye != null) {
            try {
                Employe.deleteEmploye(selectedEmploye.getId());
                table.getItems().remove(selectedEmploye);
                Utils.displayInfo("Employé supprimé avec succès");
                updateFields();

            } catch (SQLException e) {
                Utils.displayError("Une erreur s'est produite lors de la suppression de l'employé");
            }
        } else {
            Utils.displayError("Veuillez sélectionner un employé à supprimer");
        }
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
            e.printStackTrace();
            Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ouverture de la page");
        }
    }

    public void updateFields() {
        this.empnom.clear();
        this.emprenom.clear();
        this.empemail.clear();
        this.emptelephone.clear();
        this.empidposte.setRange(1, Poste.countPost(), 1);
        this.empiddepartement.setRange(1, Departement.countDepartement(), 1);
        this.empidmanager.setRange(1, Employe.countEmployes(), 1);
        this.empsalaire.setRange(0, Integer.MAX_VALUE, Employe.getSalaryAvg());

        try {
            ObservableList<Employe> data = Employe.getAllEmployes();
            table.setItems(data);
        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite lors de la récupération des données");
        }

    }
}

