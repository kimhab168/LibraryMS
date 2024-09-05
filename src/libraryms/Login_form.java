package libraryms;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class Login_form extends JFrame {

    private Form_Register registerForm; // Reference to the register form

    public Login_form() {
        // Set the title of the frame
        setTitle("LOGIN FORM");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Load the background image
        ImageIcon icon = new ImageIcon(getClass().getResource("login-bggg.png"));
        JLabel backgroundLabel = new JLabel(icon);
        backgroundLabel.setBounds(0, 0, 400, 600); // Left side of the frame
        add(backgroundLabel);

        // Create the login panel on the right
        JPanel loginPanel = new JPanel();
        loginPanel.setBounds(450, 150, 300, 200);
        loginPanel.setLayout(new GridLayout(5, 2, 10, 10));

        // Username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        // Password label and text field
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);

        // Sign Up button
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBackground(Color.GREEN);
        signupButton.setForeground(Color.WHITE);

        // Add components to the login panel
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel()); // Empty label for spacing
        loginPanel.add(showPasswordCheckBox);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);
        loginPanel.add(new JLabel());
        loginPanel.add(signupButton);

        // Add the login panel to the frame
        add(loginPanel);

        // Add ActionListener to Show Password checkbox
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    // Show the password
                    passwordField.setEchoChar((char) 0);
                } else {
                    // Hide the password
                    passwordField.setEchoChar('*');
                }
            }
        });

        // Add ActionListener to Sign Up button
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open register form and hide login form
                if (registerForm == null) {
                    registerForm = new Form_Register(Login_form.this); // Pass current login form to the register form
                }
                registerForm.setVisible(true);
                setVisible(false); // Hide the login form
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(Login_form.this, "Username and Password are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Database connection details
                    String url = "jdbc:mysql://localhost:4000/library";
                    String sql = "SELECT username, password FROM user_account WHERE username = ?";
                    String dbUser = "adminhab";
                    String dbPwd = "mrkimhab20@";
                    boolean isLogin = false;

                    // Register MySQL driver
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                    } catch (ClassNotFoundException error) {
                        JOptionPane.showMessageDialog(null, "MySQL driver not found.", "Driver Error", JOptionPane.ERROR_MESSAGE);
                        error.printStackTrace();
                        return;
                    }

                    // Use try-with-resources to ensure proper resource management
                    try (Connection conn = DriverManager.getConnection(url, dbUser, dbPwd); PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        // Set the username parameter
                        pstmt.setString(1, username);
                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next()) {
                            String usernameFromDB = rs.getString("username");
                            String passwordFromDB = rs.getString("password");

                            if (password.equals(passwordFromDB)) {
                                isLogin = true;
                                JOptionPane.showMessageDialog(Login_form.this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                setVisible(false);
                                // Launch the library system
                                try {
                                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                                } catch (Exception err) {
                                    err.printStackTrace();
                                }
                                SwingUtilities.invokeLater(() -> new LibraryMS(username));
                            } else {
                                JOptionPane.showMessageDialog(null, "Incorrect Password");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "User: " + username + " not found");
                        }

                    } catch (SQLException err) {
                        err.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred while logging in.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });

        // Set the frame visibility
        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the login form
        new Login_form();
    }

    // Method to show login form again after registration
    public void showLoginForm() {
        setVisible(true);
    }
}
