package com.mycompany.model;

import com.mycompany.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Departement {
    private final int id;
    private final String nom_Departement;
    private final int id_localisation;


    public int getId() {
        return id;
    }

    public int getId_localisation() {
        return id_localisation;
    }

    public String getNom_Departement() {
        return nom_Departement;
    }

    public Departement(int id_departement, String nom_Departement, int id_localisation) {
        this.id = id_departement;
        this.nom_Departement = nom_Departement;
        this.id_localisation = id_localisation;
    }


    public static ObservableList<Departement> getAllDepartements() {
        ObservableList<Departement> data;
        try {
            String query = "SELECT * FROM departement";
            Connection conct = MySQLConnector.getConnection();
            PreparedStatement stmt = conct.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            data = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int id = resultSet.getInt("id_departement");
                String nom_Departement = resultSet.getString("nom_departement");
                int id_localisation = resultSet.getInt("id_localisation");
                data.add(new Departement(id, nom_Departement, id_localisation));
            }
            conct.close();
            return data;

        } catch (SQLException e) {
            Utils.displayError("Une erreur s'est produite here");
            return null;
        }
    }


    public static boolean checkID(int id) throws SQLException {
        boolean bool = false;
        String Query = "SELECT COUNT(*) AS count FROM departement WHERE id_departement = ?";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            bool = count > 0;
        }
        conct.close();
        return bool;
    }

    public static void deleteDepartement(int id) throws SQLException {
        if (checkID(id)) {
            Connection conct = null;
            try {
                String Query = "DELETE FROM departement WHERE id_departement = ?;";
                conct = MySQLConnector.getConnection();
                conct.setAutoCommit(false);
                PreparedStatement stmt = conct.prepareStatement(Query);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                conct.commit();
                conct.close();
            } catch (SQLException e) {
                if (conct != null) {
                    conct.rollback();
                    throw e;
                }
            }

        }

    }

    public static int countDepartement() {
        try {
            Connection conct = null;
            int count = 0;
            conct = MySQLConnector.getConnection();
            String query = "SELECT COUNT(*) AS total FROM departement";
            PreparedStatement stmt = conct.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
            return count;
        } catch (SQLException e) {
            Utils.displayErrorAndExit("Une erreur s'est produite");
            return 0;
        }
    }


    public static void updateDepartement(int id, String nom_Departement, int id_localisation) throws SQLException {
        if (checkID(id)) {
            Connection conct = null;
            try {
                String Query = "UPDATE  departement SET nom_departement = ? , id_localisation = ? WHERE id_departement = ?";
                conct = MySQLConnector.getConnection();
                conct.setAutoCommit(false);
                PreparedStatement stmt = conct.prepareStatement(Query);
                stmt.setString(1, nom_Departement);
                stmt.setInt(2, id_localisation);
                stmt.setInt(3, id);
                stmt.executeUpdate();
                conct.commit();
                conct.close();
            } catch (SQLException e) {
                if (conct != null) {
                    conct.rollback();
                    throw e;
                }
            }
        }
    }

    public static void addDepartement(String nom_Departement, int id_localisation) throws SQLException {
        Connection conct = null;
        try {
            String Query = "INSERT INTO departement (nom_Departement,id_localisation) VALUES (? , ? );";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setString(1, nom_Departement.trim().toUpperCase());
            stmt.setInt(2, id_localisation);
            stmt.executeUpdate();
            conct.commit();
            conct.close();
        } catch (SQLException e) {
            if (conct != null) {
                conct.rollback();
                throw e;
            }
        }
    }

    public static String[][] getDepartementData() throws SQLException {
        String[][] data = null;
        String query = "SELECT * FROM departement";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = stmt.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        int rowCount = 0;
        if (resultSet.last()) {
            rowCount = resultSet.getRow();
            resultSet.beforeFirst();
        }

        data = new String[rowCount][columnCount];

        int row = 0;
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                data[row][i - 1] = resultSet.getString(i);
            }
            row++;
        }

        conct.close();
        return data;
    }


    public static void replaceDepartements(int oldId, int newId) throws SQLException {
        Connection conct = null;
        try {
            String Query = "UPDATE employes SET id_departement = ? WHERE id_departement = ?";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setInt(1, newId);
            stmt.setInt(2, oldId);
            stmt.executeUpdate();
            conct.commit();
            conct.close();
            //
        } catch (SQLException e) {
            if (conct != null) {
                conct.rollback();
                throw e;
            }
        }
    }

    public static boolean isDepartmentOccupied(int id) throws SQLException {
        boolean bool = false;
        String Query = "SELECT COUNT(*) AS count FROM employes WHERE id_departement = ?";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            bool = count > 0;
        }
        conct.close();
        return bool;
    }

    public static void addFromFile(FileInputStream file) throws SQLException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            String nom_departement = parts[0];
            int id_localisation = Integer.parseInt(parts[1]);
            addDepartement(nom_departement, id_localisation);
        }

    }

    public static void exportFileTxt(String fileName) throws IOException, SQLException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        String Query = "SELECT * FROM departement";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            String line = resultSet.getString("nom_departement") + "," + resultSet.getString("id_localisation");
            writer.write(line);
            writer.newLine();

        }
        writer.close();
        conct.close();
    }

    public static void exportFileXls(String fileName) throws SQLException, IOException {
        String Query = "SELECT * FROM departement";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        ResultSet resultSet = stmt.executeQuery();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Departements");
        int rowNum = 0;
        while (resultSet.next()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(resultSet.getString("nom_departement"));
            row.createCell(1).setCellValue(resultSet.getString("id_localisation"));
        }

        FileOutputStream fileOut = new FileOutputStream(fileName.trim());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        conct.close();
    }

}
