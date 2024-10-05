
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MySQLDatabaseApp extends JFrame {

    // JDBC URL, username, and password of MySQL server
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    static final String USER = "root"; // Change this to your MySQL username
    static final String PASS = "Wanjala4122."; // Change this to your MySQL password

    public MySQLDatabaseApp() {
        // Frame settings
        setTitle("MySQL Database Operations");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between buttons
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make buttons fill horizontally

        // Button 1: Create Database
        JButton btnCreateDatabase = new JButton("Create JHubDB Database");
        btnCreateDatabase.setBackground(Color.BLUE);
        btnCreateDatabase.setForeground(Color.WHITE);
        btnCreateDatabase.addActionListener(e -> createDatabase());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(btnCreateDatabase, gbc);

        // Button 2: Create Table
        JButton btnCreateTable = new JButton("Create JHub Table");
        btnCreateTable.setBackground(Color.BLUE);
        btnCreateTable.setForeground(Color.WHITE);
        btnCreateTable.addActionListener(e -> createTable());
        gbc.gridy = 1;
        add(btnCreateTable, gbc);

        // Button 3: Insert Data
        JButton btnInsertData = new JButton("Insert Data into JHub Table");
        btnInsertData.setBackground(Color.BLUE);
        btnInsertData.setForeground(Color.WHITE);
        btnInsertData.addActionListener(e -> insertData());
        gbc.gridy = 2;
        add(btnInsertData, gbc);

        // Button 4: Retrieve Data
        JButton btnRetrieveData = new JButton("Retrieve Data from JHub Table");
        btnRetrieveData.setBackground(Color.BLUE);
        btnRetrieveData.setForeground(Color.WHITE);
        btnRetrieveData.addActionListener(e -> retrieveData());
        gbc.gridy = 3;
        add(btnRetrieveData, gbc);

        // Button 5: Delete Data
        JButton btnDeleteData = new JButton("Delete Data from JHub Table");
        btnDeleteData.setBackground(Color.RED);
        btnDeleteData.setForeground(Color.WHITE);
        btnDeleteData.addActionListener(e -> deleteData());
        gbc.gridy = 4;
        add(btnDeleteData, gbc);
    }

    // Method to create the database
    private void createDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS JHubDB";
            stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Database JHubDB created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating database: " + e.getMessage());
        }
    }

    // Method to create the table
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL + "JHubDB", USER, PASS);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS JHub (" +
                    "Regno VARCHAR(50) NOT NULL," +
                    "Studentname VARCHAR(100)," +
                    "Phoneno VARCHAR(15)," +
                    "PRIMARY KEY (Regno)" +
                    ")";
            stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Table JHub created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating table: " + e.getMessage());
        }
    }

    // Method to insert data
    private void insertData() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL + "JHubDB", USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO JHub (Regno, Studentname, Phoneno) VALUES (?, ?, ?)")) {

            String regno = JOptionPane.showInputDialog("Enter Regno:");
            String studentName = JOptionPane.showInputDialog("Enter student name:");
            String phoneNo = JOptionPane.showInputDialog("Enter phone number:");

            pstmt.setString(1, regno);
            pstmt.setString(2, studentName);
            pstmt.setString(3, phoneNo);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Data inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inserting data: " + e.getMessage());
        }
    }

    // Method to retrieve data
    private void retrieveData() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL + "JHubDB", USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM JHub")) {

            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                String regno = rs.getString("Regno");
                String studentName = rs.getString("Studentname");
                String phoneNo = rs.getString("Phoneno");

                result.append("Regno: ").append(regno)
                        .append(", Studentname: ").append(studentName)
                        .append(", Phoneno: ").append(phoneNo).append("\n");
            }

            if (result.length() > 0) {
                JOptionPane.showMessageDialog(this, result.toString());
            } else {
                JOptionPane.showMessageDialog(this, "No data found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving data: " + e.getMessage());
        }
    }

    // Method to delete data
    private void deleteData() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL + "JHubDB", USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM JHub WHERE Regno = ?")) {

            String regno = JOptionPane.showInputDialog("Enter Regno of the record to delete:");
            pstmt.setString(1, regno);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Data deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found with Regno: " + regno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MySQLDatabaseApp app = new MySQLDatabaseApp();
            app.setVisible(true);
        });
    }
}
