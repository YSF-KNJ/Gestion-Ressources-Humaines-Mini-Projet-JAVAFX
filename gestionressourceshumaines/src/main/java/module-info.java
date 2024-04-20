module com.mycompany {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires poi;
    requires poi.ooxml;
    requires java.base;

    opens com.mycompany to javafx.fxml;
    exports com.mycompany;
    exports com.mycompany.controller to javafx.fxml;
    exports com.mycompany.util to javafx.fxml;
    opens com.mycompany.controller to javafx.fxml;
}