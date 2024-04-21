package com.mycompany;

import com.mycompany.controller.EmployeeController;
import com.mycompany.controller.HomeController;
import com.mycompany.controller.SigninController;
import com.mycompany.controller.SignupController;
import com.mycompany.model.Admin;
import com.mycompany.model.Createdb;
import com.mycompany.model.Createtables;
import com.mycompany.model.InsertValues;
import com.mycompany.util.Utils;
import javafx.application.Application;
import javafx.stage.Stage;


public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try {
            Createdb.createdb();
            Createtables.createtables();
            InsertValues.insert();
            if (Admin.CheckEmpty()) {
                new SignupController().openSignUpWindow();
            } else {
                new SigninController().openSignInWindow();
            }

        } catch (Exception e) {
            Utils.displayErrorAndExit("Une erreur s'est produite");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
