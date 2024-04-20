package com.mycompany.util;

import javafx.scene.control.Alert;

public class ErrorUtils {

    public static void displayErrorAndExit(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        System.exit(0);
    }
}