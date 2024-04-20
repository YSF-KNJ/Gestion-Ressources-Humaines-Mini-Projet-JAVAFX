package com.mycompany.controller;

import com.mycompany.model.Departement;
import com.mycompany.model.Employe;
import com.mycompany.model.Poste;
import com.mycompany.util.CustomSpinner;
import com.mycompany.util.EmailValidator;
import com.mycompany.util.Utils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.mycompany.model.Employe.getAllEmployes;

public class EmployeeController implements Initializable {

    @FXML
    private TextField empnom;
    @FXML
    private TextField emprenom;
    @FXML
    private TextField empemail;
    @FXML
    private TextField emptelephone;
    @FXML
    private CustomSpinner empsalaire;

    @FXML
    private CustomSpinner empidposte;

    @FXML
    private CustomSpinner empiddepartement;
    @FXML
    private CustomSpinner empidmanager;

    @FXML
    private TableView<Employe> table;

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
    private Button empadd;
    @FXML
    private Button empupdate;
    @FXML
    private Button empdelete;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colnom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colprenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        colemail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        coltelephone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        colsalaire.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSalaire()).asObject());
        colidposte.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPoste()).asObject());
        coliddepartement.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdDepartement()).asObject());
        colidmanger.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdManager()).asObject());


        ObservableList<Employe> data = getAllEmployes();
        table.setItems(data);


        empidposte.setRange(1, Poste.countPost(), 1);
        empiddepartement.setRange(1, Departement.countDepartement(), 1);
        empidmanager.setRange(1, Employe.countEmployes(), 1);
        empsalaire.setRange(0, Integer.MAX_VALUE, Employe.getSalaryAvg());
        empadd.setOnAction(event -> handleAddButton());
        empupdate.setOnAction(event -> handleUpdateButton());
        empdelete.setOnAction(event -> handleDeleteButton());

    }

    private void handleAddButton() {
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

            stage.setResizable(false);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            double centerX = bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) / 2;
            double centerY = bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) / 2;
            stage.setX(centerX);
            stage.setY(centerY);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ouverture de la page de employee");
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

