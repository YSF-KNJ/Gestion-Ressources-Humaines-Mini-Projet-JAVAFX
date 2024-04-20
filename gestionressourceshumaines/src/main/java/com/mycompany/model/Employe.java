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

public class Employe {

    private String prenom;
    private String nom;
    private String email;
    private String telephone;
    private double salaire;
    private int id_poste;
    private int id_departement;
    private Integer id_manager;

    public Employe(String prenom, String nom, String email, String telephone, double salaire, int id_poste, int id_departement, Integer id_manager) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.salaire = salaire;
        this.id_poste = id_poste;
        this.id_departement = id_departement;
        this.id_manager = id_manager;
    }

    public static boolean checkID(int id) throws SQLException {
        String Query = "SELECT COUNT(*) AS count FROM employes WHERE id_employe = ?";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            return count > 0;
        }
        conct.close();
        return false;
    }

    public static ObservableList<Employe> getAllEmployes() {
        ObservableList<Employe> data = FXCollections.observableArrayList();
        try {
            Connection conct = MySQLConnector.getConnection();
            String query = "SELECT * FROM employes";
            PreparedStatement stmt = conct.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String prenom = rs.getString("prenom");
                String nom = rs.getString("nom");
                String email = rs.getString("email");
                String telephone = rs.getString("telephone");
                int salaire = rs.getInt("salaire");
                int id_poste = rs.getInt("id_poste");
                int id_departement = rs.getInt("id_departement");
                int id_manager = rs.getInt("id_manager");
                Employe employe = new Employe(prenom, nom, email, telephone, salaire, id_poste, id_departement, id_manager);
                data.add(employe);
            }
        } catch (SQLException e) {
            Utils.displayErrorAndExit("Une erreur s'est produite");
        }
        return data;
    }


    public static String[][] getEmployeesData() throws SQLException {
        String[][] data = null;
        String query = "SELECT * FROM employes";
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


    public static void addManger(String prenom, String nom, String email, String telephone, double salaire, int id_poste, int id_departement) throws SQLException {
        Connection conct = null;
        try {
            String Query = "INSERT INTO employes (prenom, nom, email, telephone, salaire, id_poste, id_departement) VALUES (?, ?, ?, ?, ?, ?, ?)";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setString(1, prenom);
            stmt.setString(2, nom);
            stmt.setString(3, email);
            stmt.setString(4, telephone);
            stmt.setDouble(5, salaire);
            stmt.setInt(6, id_poste);
            stmt.setInt(7, id_departement);
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

    public static void addEmploye(String prenom, String nom, String email, String telephone, double salaire, int id_poste, int id_departement, int id_manager) throws SQLException {
        Connection conct = null;
        try {
            String Query = "INSERT INTO employes (prenom, nom, email, telephone, salaire, id_poste, id_departement, id_manager) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setString(1, prenom);
            stmt.setString(2, nom);
            stmt.setString(3, email);
            stmt.setString(4, telephone);
            stmt.setDouble(5, salaire);
            stmt.setInt(6, id_poste);
            stmt.setInt(7, id_departement);
            stmt.setInt(8, id_manager);
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

    public static void updateEmploye(int id, String prenom, String nom, String email, String telephone, double salaire, int id_poste, int id_departement, int id_manager) throws SQLException {
        Connection conct = null;
        if (checkID(id)) {
            try {
                String Query = "UPDATE employes SET prenom = ?, nom = ?, email = ?, telephone = ?, salaire = ?, id_poste = ?, id_departement = ?, id_manager = ? WHERE id_employe = ?";
                conct = MySQLConnector.getConnection();
                conct.setAutoCommit(false);
                PreparedStatement stmt = conct.prepareStatement(Query);
                stmt.setString(1, prenom);
                stmt.setString(2, nom);
                stmt.setString(3, email);
                stmt.setString(4, telephone);
                stmt.setDouble(5, salaire);
                stmt.setInt(6, id_poste);
                stmt.setInt(7, id_departement);
                stmt.setInt(8, id_manager);
                stmt.setInt(9, id);
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

    public static void updateManager(int id, String prenom, String nom, String email, String telephone, double salaire, int id_poste, int id_departement) throws SQLException {
        Connection conct = null;
        if (checkID(id)) {
            try {
                String Query = "UPDATE employes SET prenom = ?, nom = ?, email = ?, telephone = ?, salaire = ?, id_poste = ?, id_departement = ?, id_manager = null WHERE id_employe = ?";
                conct = MySQLConnector.getConnection();
                conct.setAutoCommit(false);
                PreparedStatement stmt = conct.prepareStatement(Query);
                stmt.setString(1, prenom);
                stmt.setString(2, nom);
                stmt.setString(3, email);
                stmt.setString(4, telephone);
                stmt.setDouble(5, salaire);
                stmt.setInt(6, id_poste);
                stmt.setInt(7, id_departement);
                stmt.setInt(8, id);
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

    public static boolean isManager(int id) throws SQLException {
        boolean bool = false;
        String query = "SELECT COUNT(*) AS count FROM employes WHERE id_manager = ?";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            bool = count > 0;
        }
        return bool;
    }

    public static void replaceManager(int newManagerId, int oldManagerId) throws SQLException {
        Connection conct = null;
        try {
            String query = "UPDATE employes SET id_manager = ? WHERE id_manager = ?";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(query);
            stmt.setInt(1, newManagerId);
            stmt.setInt(2, oldManagerId);
            stmt.executeUpdate();
            conct.commit();
            conct.close();
            deleteEmploye(oldManagerId);
        } catch (SQLException e) {
            if (conct != null) {
                conct.rollback();
                throw e;
            }
        }
    }

    public static int getSalaryAvg() {
        try {
            Connection conct = null;
            double avgSalary = 0;
            conct = MySQLConnector.getConnection();
            String query = "SELECT AVG(salaire) AS avg_salary FROM employes";
            PreparedStatement stmt = conct.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                avgSalary = rs.getDouble("avg_salary");
            }
            return (int) avgSalary;
        } catch (SQLException e) {
            Utils.displayErrorAndExit("Une erreur s'est produite");
            return 0;
        }
    }

    public static int countEmployes() {
        try {
            Connection conct = null;
            int count = 0;
            conct = MySQLConnector.getConnection();
            String query = "SELECT COUNT(*) AS total FROM employes";
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


    public static void deleteEmploye(int id) throws SQLException {
        Connection conct = null;
        try {
            String Query = "DELETE FROM employes WHERE id_employe = ?;";
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

    public static void increaseSalary(int id, double amount) throws SQLException {
        Connection conct = null;
        try {
            String Query = "UPDATE employes SET salaire = salaire + ? WHERE id_employe = ?";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setDouble(1, amount);
            stmt.setInt(2, id);
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

    public static void addFromFile(FileInputStream file) throws SQLException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            String prenom = parts[0];
            String nom = parts[1];
            String email = parts[2];
            String telephone = parts[3];
            double salaire = Double.parseDouble(parts[4]);
            int id_poste = Integer.parseInt(parts[5]);
            int id_departement = Integer.parseInt(parts[6]);
            int id_manager = Integer.parseInt(parts[6]);
            addEmploye(prenom, nom, email, telephone, salaire, id_poste, id_departement, id_manager);

        }
    }

    public static void exportFileTxt(String fileName) throws SQLException, IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        String Query = "SELECT * FROM employes";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            String line = resultSet.getString("prenom") + "," + resultSet.getString("nom") + "," + resultSet.getString("email") + "," + resultSet.getString("telephone") + "," + resultSet.getString("salaire") + "," + resultSet.getString("id_poste") + "," + resultSet.getString("id_departement") + "," + resultSet.getString("id_manager");
            writer.write(line);
            writer.newLine();
        }
        writer.close();
        conct.close();
    }

    public static void exportFileXls(String fileName) throws SQLException, IOException {
        String Query = "SELECT * FROM employes";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        ResultSet resultSet = stmt.executeQuery();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employes");
        int rowNum = 0;
        while (resultSet.next()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(resultSet.getString("prenom"));

            row.createCell(1).setCellValue(resultSet.getString("nom"));

            row.createCell(2).setCellValue(resultSet.getString("email"));

            row.createCell(3).setCellValue(resultSet.getString("telephone"));

            row.createCell(4).setCellValue(resultSet.getString("salaire"));

            row.createCell(5).setCellValue(resultSet.getString("id_poste"));

            row.createCell(6).setCellValue(resultSet.getString("id_departement"));

            row.createCell(7).setCellValue(resultSet.getString("id_manager"));

        }

        FileOutputStream fileOut = new FileOutputStream(fileName.trim());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        conct.close();
    }

    public String getNom() {
        return this.nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public String getEmail() {
        return email;
    }

    public int getSalaire() {
        return (int) this.salaire;
    }

    public int getIdPoste() {
        return this.id_poste;
    }

    public int getIdDepartement() {
        return this.id_departement;
    }

    public Integer getIdManager() {
        return (Integer) this.id_manager;
    }

}