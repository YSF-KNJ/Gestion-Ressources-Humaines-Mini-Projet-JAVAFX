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

public class Poste {
    private final int id;
    private final String titre_poste;

    public int getId() {
        return id;
    }

    public String getTitre_poste() {
        return titre_poste;
    }

    public Poste(int id, String titre_poste) {
        this.id = id;
        this.titre_poste = titre_poste;
    }


    public static ObservableList<Poste> getAllPostes() {
        ObservableList<Poste> data;
        try {
            String query = "SELECT * FROM poste";
            Connection conct = MySQLConnector.getConnection();
            PreparedStatement stmt = conct.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            data = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int idposte = resultSet.getInt("id_poste");
                String titre_poste = resultSet.getString("titre_poste");

                data.add(new Poste(idposte, titre_poste));
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
        String Query = "SELECT COUNT(*) AS count FROM poste WHERE id_poste = ?";
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

    public static void deletePoste(int id) throws SQLException {
        Connection conct = null;
        try {
            String Query = "DELETE FROM poste WHERE id_poste = ?;";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setInt(1, id);
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

    public static int getLastRowId() {
        try {
            Connection conct = MySQLConnector.getConnection();
            String query = "SELECT MAX(id_poste) AS max_id FROM poste";
            PreparedStatement stmt = conct.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("max_id");
            } else {
                Utils.displayErrorAndExit("Unable to retrieve the last row id from the poste table");
                return 0;
            }
        } catch (SQLException e) {
            Utils.displayErrorAndExit("Une erreur s'est produite");
            return 0;
        }
    }

    public static void updatePost(int id, String title) throws SQLException {
        Connection conct = null;
        if (checkID(id)) {
            try {
                String Query = "UPDATE poste SET titre_poste = ? WHERE id_poste = ?";
                conct = MySQLConnector.getConnection();
                conct.setAutoCommit(false);
                PreparedStatement stmt = conct.prepareStatement(Query);
                stmt.setString(1, title.trim().toUpperCase());
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
    }

    public static void addPost(String title) throws SQLException {
        Connection conct = null;
        try {
            String Query = "INSERT INTO poste (titre_poste) VALUES (?);";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setString(1, title.trim().toUpperCase());
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

    public static int countPost() {
        try {
            int count = 0;
            Connection conct = MySQLConnector.getConnection();
            String query = "SELECT COUNT(*) AS total FROM poste";
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

    public static String[][] getPostesData() throws SQLException {
        String[][] data = null;

        String query = "SELECT * FROM poste";
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


    public static void replacePosts(int oldId, int newId) throws SQLException {
        Connection conct = null;
        try {
            String Query = "UPDATE employes SET id_poste = ? WHERE id_poste = ?";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setInt(1, newId);
            stmt.setInt(2, oldId);
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

    public static boolean isPosteOccupied(int id) throws SQLException {
        String Query = "SELECT COUNT(*) AS count FROM employes WHERE id_poste = ?";
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

    public static void addFromFile(FileInputStream file) throws SQLException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            addPost(line.trim());
        }
    }

    public static void exportFileTxt(String fileName) throws IOException, SQLException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        String Query = "SELECT * FROM poste";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            String line = resultSet.getString("titre_poste");
            writer.write(line);
            writer.newLine();

        }
        writer.close();
        conct.close();
    }

    public static void exportFileXls(String fileName) throws SQLException, IOException {
        String Query = "SELECT * FROM poste";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        ResultSet resultSet = stmt.executeQuery();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Postes");
        int rowNum = 0;
        while (resultSet.next()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(resultSet.getString("titre_poste"));

        }

        FileOutputStream fileOut = new FileOutputStream(fileName.trim());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        conct.close();
    }


}
