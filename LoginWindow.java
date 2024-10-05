import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginWindow extends JFrame {
    // JDBC URL, username, and password of MySQL server
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/JHubDB"; // Ensure the database name is included
    static final String USER = "root"; // Change this to your MySQL username
    static final String PASS = "Wanjala4122."; // Change this to your MySQL password

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkRememberMe;
    private JCheckBox chkShowPassword;

    public LoginWindow() {
        // Frame settings
        setTitle("Login Window");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load icons from resources
        ImageIcon originalUserIcon = new ImageIcon(getClass().getResource("/resources/user_icon.png"));
        ImageIcon originalKeyIcon = new ImageIcon(getClass().getResource("/resources/key_icon.png"));
        ImageIcon originalEyeIcon = new ImageIcon(getClass().getResource("/resources/eye_icon.png"));
        
        // Resize icons to the same size (20x20 pixels)
        Image userImage = originalUserIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Image keyImage = originalKeyIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Image eyeImage = originalEyeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        
        // Create new ImageIcons with the resized images
        Icon userIcon = new ImageIcon(userImage);
        Icon keyIcon = new ImageIcon(keyImage);
        Icon eyeIcon = new ImageIcon(eyeImage);
        
        // Create labels and text fields
        JLabel lblUsername = new JLabel("Username:", userIcon, JLabel.LEFT);
        txtUsername = new JTextField(15);
        JLabel lblPassword = new JLabel("Password:", keyIcon, JLabel.LEFT);
        txtPassword = new JPasswordField(15);
        
        // Create a button to show/hide password
        JButton btnTogglePasswordVisibility = new JButton(eyeIcon);
        btnTogglePasswordVisibility.setBackground(Color.WHITE);
        btnTogglePasswordVisibility.setForeground(Color.WHITE);
        btnTogglePasswordVisibility.setPreferredSize(new Dimension(30, 30));
        btnTogglePasswordVisibility.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle password visibility
                if (txtPassword.getEchoChar() == '*') {
                    txtPassword.setEchoChar((char) 0); // Show password
                } else {
                    txtPassword.setEchoChar('*'); // Hide password
                }
            }
        });

        // Create a panel for the username and password inputs, and position it at the top
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components

        // Add username label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblUsername, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(txtUsername, gbc);

        // Add password label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(txtPassword, gbc);

        // Add the eye icon button next to the password field
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(btnTogglePasswordVisibility, gbc);
        
        // Checkbox to remember username and password
        chkRememberMe = new JCheckBox("Remember me");
        
        // Add remember me checkbox
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Make it span both columns
        gbc.anchor = GridBagConstraints.CENTER; // Center align the checkbox
        inputPanel.add(chkRememberMe, gbc);

        // Create a panel for the buttons, and position it in the center
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Login button
        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(Color.BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(); // Call login method
            }
        });

        // Register button
        JButton btnRegister = new JButton("Register");
        btnRegister.setBackground(Color.BLUE);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser(); // Call register method
            }
        });

        // Exit System button
        JButton btnExit = new JButton("Exit System");
        btnExit.setBackground(Color.RED);
        btnExit.setForeground(Color.WHITE);
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(LoginWindow.this, "Are you sure you want to exit the system?", "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0); // Exit the system
                }
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnExit);

        // Add panels to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Set visibility
        setVisible(true);
    }

    // Method to login
    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM JHubUsers WHERE Username = ? AND Password = ?")) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose(); // Close the login window
                new MySQLDatabaseApp().setVisible(true); // Open MySQLDatabaseApp
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error logging in: " + e.getMessage());
        }
    }

    // Method to register a new user
    private void registerUser() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO JHubUsers (Username, Password, email, PhoneNo, Usertype) VALUES (?, ?, ?, ?, ?)")) {

            String username = JOptionPane.showInputDialog("Enter Username:");
            String password = JOptionPane.showInputDialog("Enter Password:");
            String email = JOptionPane.showInputDialog("Enter Email:");
            String phoneNo = JOptionPane.showInputDialog("Enter Phone Number:");

            String userType;
            while (true) {
                userType = JOptionPane.showInputDialog("Enter Usertype (Administrator/Basic):");
                if (userType.equalsIgnoreCase("Administrator") || userType.equalsIgnoreCase("Basic")) {
                    break; // Valid input, exit the loop
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid usertype! Please enter either 'Administrator' or 'Basic'.");
                }
            }

            // Set the input values into the prepared statement
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, phoneNo);
            pstmt.setString(5, userType);

            // Execute the update (insert the data)
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "User registered successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering user: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow());
    }
}
