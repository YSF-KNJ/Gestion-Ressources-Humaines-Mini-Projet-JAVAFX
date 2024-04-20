package com.mycompany.model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Localisation {
    public static boolean checkID(int id) throws SQLException {
        boolean exists = false;
        String Query = "SELECT COUNT(*) AS count FROM localisation WHERE id_localisation = ?";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            exists = count > 0;
        }
        conct.close();
        return exists;
    }

    public static void deleteLocalisation(int id) throws SQLException {
        Connection conct = null;
        try {
            String Query = "DELETE FROM localisation WHERE id_localisation = ?;";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            conct.commit();
            conct.close();
            System.out.println("Localisation avec l'ID " + id + " est supprimé.");
        } catch (SQLException e) {
            if (conct != null) {
                conct.rollback();
                throw e;
            }
        }

    }


    public static int countLocalisation() throws SQLException {
        int count = 0;
        Connection conct = MySQLConnector.getConnection();
        String query = "SELECT COUNT(*) AS total FROM localisation";
        PreparedStatement stmt = conct.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            count = rs.getInt("total");
        }
        return count;
    }


    public static String[][] getLocalisationsData() throws SQLException {
        String[][] data = null;
        String query = "SELECT * FROM localisation";
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


    public static void updateLocalisation(int id, String adresse, String ville) throws SQLException {
        Connection conct = null;
        try {
            String Query = "UPDATE localisation SET adresse = ?,ville = ? WHERE id_localisation = ?";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setString(1, adresse.trim().toUpperCase());
            stmt.setString(2, ville.trim().toUpperCase());
            stmt.setInt(3, id);
            stmt.executeUpdate();
            conct.close();
        } catch (SQLException e) {
            if (conct != null) {
                conct.rollback();
                throw e;
            }
        }
    }

    public static void addLocalisation(String adresse, String ville) throws SQLException {
        Connection conct = null;
        try {
            String Query = "INSERT INTO localisation (adresse, ville) VALUES (?, ?);";
            conct = MySQLConnector.getConnection();
            conct.setAutoCommit(false);
            PreparedStatement stmt = conct.prepareStatement(Query);
            stmt.setString(1, adresse.trim().toUpperCase());
            stmt.setString(2, ville.trim().toUpperCase());
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

    public static void replaceLocalisations(int oldId, int newId) throws SQLException {
        Connection conct = null;
        try {
            String Query = "UPDATE departement SET id_localisation = ? WHERE id_localisation = ?";
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

    public static boolean isLocalisationOccupied(int id) throws SQLException {
        boolean bool = false;
        String Query = "SELECT COUNT(*) AS count FROM departement WHERE id_localisation = ?";
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
            String adresse = parts[0];
            String ville = parts[1];
            addLocalisation(adresse, ville);
        }
    }

    public static void exportFileTxt(String fileName) throws IOException, SQLException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        String Query = "SELECT * FROM localisation";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            String line = resultSet.getString("adresse") + "," + resultSet.getString("ville");
            writer.write(line);
            writer.newLine();

        }
        writer.close();
        conct.close();
    }

    public static void exportFileXls(String fileName) throws SQLException, IOException {
        String Query = "SELECT * FROM localisation";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        ResultSet resultSet = stmt.executeQuery();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Localisations");
        int rowNum = 0;
        while (resultSet.next()) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(resultSet.getString("adresse"));
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(resultSet.getString("ville"));
        }

        FileOutputStream fileOut = new FileOutputStream(fileName.trim());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        conct.close();
    }

}
