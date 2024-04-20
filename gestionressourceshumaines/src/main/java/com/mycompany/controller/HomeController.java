package com.mycompany.controller;

import com.mycompany.util.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

    public void initialize() {

        buttonEmployees.setOnAction(event -> handleEmployeeButtonClick());
        buttonDepartments.setOnAction(event -> handleDepartmentButtonClick());
        buttonPosts.setOnAction(event -> handlePostsButtonClick());
        buttonLocalisations.setOnAction(event -> handleLocalisationsButtonClick());
    }

    private void handleEmployeeButtonClick() {
        new EmployeeController().openEmployeeWindow();
        Stage stage = (Stage) buttonEmployees.getScene().getWindow();
        stage.close();

    }

    private void handleDepartmentButtonClick() {
        System.out.println("Departments");
    }

    private void handlePostsButtonClick() {
        System.out.println("Posts");
    }

    private void handleLocalisationsButtonClick() {
        System.out.println("Localisations");
    }

    public void openHomeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Home");
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
            Utils.displayErrorAndExit("Une erreur s'est produite lors de l'ouverture de la page d'accueil");
        }
    }

}
