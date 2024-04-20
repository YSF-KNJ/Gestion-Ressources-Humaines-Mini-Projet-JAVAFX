package com.mycompany.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Createtables {
    public static void createtables() throws SQLException {
        Connection conct = MySQLConnector.getConnection();
        Statement stmt = conct.createStatement();
        String createAdminTable = "CREATE TABLE IF NOT EXISTS admin ("
                + "id_admin INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                + "first_name VARCHAR(50),"
                + "last_name VARCHAR(50),"
                + "email VARCHAR(100),"
                + "password VARCHAR(255) NOT NULL"
                + ")";
        stmt.executeUpdate(createAdminTable);

        String createPosteTable = "CREATE TABLE IF NOT EXISTS poste ("
                + "id_poste INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                + "titre_poste VARCHAR(255)"
                + ")";
        stmt.executeUpdate(createPosteTable);

        String createLocalisationTable = "CREATE TABLE IF NOT EXISTS localisation ("
                + "id_localisation INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                + "adresse VARCHAR(255),"
                + "ville VARCHAR(255)"
                + ")";
        stmt.executeUpdate(createLocalisationTable);


        String createDepartementTable = "CREATE TABLE IF NOT EXISTS departement ("
                + "id_departement INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                + "nom_departement VARCHAR(255),"
                + "id_localisation INT,"
                + "FOREIGN KEY (id_localisation) REFERENCES localisation(id_localisation)"
                + ")";
        stmt.executeUpdate(createDepartementTable);

        String createEmployesTable = "CREATE TABLE IF NOT EXISTS employes ("
                + "id_employe INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                + "prenom VARCHAR(255),"
                + "nom VARCHAR(255),"
                + "email VARCHAR(255),"
                + "telephone VARCHAR(20),"
                + "salaire DECIMAL(10, 2),"
                + "id_poste INT,"
                + "id_departement INT,"
                + "id_manager INT,"
                + "FOREIGN KEY (id_poste) REFERENCES poste(id_poste),"
                + "FOREIGN KEY (id_departement) REFERENCES departement(id_departement),"
                + "FOREIGN KEY (id_manager) REFERENCES employes(id_employe)"
                + ")";
        stmt.executeUpdate(createEmployesTable);
        conct.close();
    }
}
