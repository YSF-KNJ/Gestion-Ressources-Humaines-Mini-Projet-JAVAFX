package com.mycompany.controller;

import com.mycompany.util.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController {

    @FXML
    private Label headerLabel;

    @FXML
    private Button buttonEmployees;

    @FXML
    private Button buttonDepartments;

    @FXML
    private Button buttonPosts;

    @FXML
    private Button buttonLocalisations;

    @FXML
    private ImageView employeeImageView;

    @FXML
    private ImageView departmentsImageView;

    @FXML
    private ImageView postsImageView;

    @FXML
    private ImageView localisationsImageView;

    @FXML
    public void initialize() {

        buttonEmployees.setOnAction(event -> handleEmployeeButtonClick());
        buttonDepartments.setOnAction(event -> handleDepartmentButtonClick());
        buttonPosts.setOnAction(event -> handlePostsButtonClick());
        buttonLocalisations.setOnAction(event -> handleLocalisationsButtonClick());
    }

    @FXML
    private void handleEmployeeButtonClick() {
        EmployeeController employeeController = new EmployeeController();
        employeeController.openEmployeeWindow();

    }

    @FXML
    private void handleDepartmentButtonClick() {
        DepartmentController departmentController = new DepartmentController();
        departmentController.openDepartmentWindow();
    }
    @FXML
    private void handlePostsButtonClick() {
        System.out.println("Posts");
    }

    @FXML
    private void handleLocalisationsButtonClick() {
        System.out.println("Localisations");
    }

    public void openHomeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des ressources humaines");
            Image hrImage = new Image(getClass().getResourceAsStream("/icons/management.png"));
            stage.getIcons().add(hrImage);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ouverture de la page d'accueil");

        }
    }

}
