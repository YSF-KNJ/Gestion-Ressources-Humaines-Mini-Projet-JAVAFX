package com.mycompany.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLConnector {
    private static final String protocole = "jdbc:mysql:";
    private static final String ip = "localhost";  // dépend du contexte
    private static final String port = "3306";  // port MySQL par défaut
    private static final String nomBase = "HR";  // dépend du contexte
    private static final String conString = protocole + "//" + ip + ":" + port + "/" + nomBase;
    private static final String nomConnexion = "root";  // dépend du contexte
    private static final String motDePasse = "root";  // dépend du contexte

    public static Connection getConnection() {
        Connection cnct = null;
        try {
            cnct = DriverManager.getConnection(conString, nomConnexion, motDePasse);
        } catch (SQLException ex) {
            Logger.getLogger(MySQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cnct;

    }
}