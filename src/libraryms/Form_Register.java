package libraryms;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class Form_Register extends JFrame {

    private Login_form loginForm; // Reference to the login form

    public Form_Register(Login_form loginForm) {
        this.loginForm = loginForm; // Store reference to login form
        // Create frame
        setTitle("Register");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Add image
        ImageIcon icon = new ImageIcon(Form_Register.class.getResource("register.png"));
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(10, 50, icon.getIconWidth(), icon.getIconHeight());
        add(imageLabel);

        // Create panel for form
        JPanel formPanel = new JPanel();
        formPanel.setBounds(400, 50, 350, 450); // Adjusted height
        formPanel.setLayout(new GridLayout(12, 2, 10, 10)); // Adjusted to 12 rows for additional fields

        // Register form title
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Set font and size
        titleLabel.setBounds(450, 10, 200, 50); // Position the title
        add(titleLabel); // Add the title to the frame

        // Full Name
        JLabel nameLabel = new JLabel("Username : ");
        JTextField nameField = new JTextField();

        // Email
        JLabel emailLabel = new JLabel("Email : ");
        JTextField emailField = new JTextField();

        // Phone Number
        JLabel phoneLabel = new JLabel("Phone Number : ");
        JTextField phoneField = new JTextField();

        // Password
        JLabel passwordLabel = new JLabel("Password : ");
        JPasswordField passwordField = new JPasswordField();

        // Confirm Password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password : ");
        JPasswordField confirmPasswordField = new JPasswordField();

        // Gender
        JLabel genderLabel = new JLabel("Gender : ");
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton maleButton = new JRadioButton("Male");
        JRadioButton femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);

        // Show Password checkbox
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.BLUE);
        registerButton.setForeground(Color.WHITE);

        // Back to login button
        JButton backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setBackground(Color.GRAY);
        backToLoginButton.setForeground(Color.WHITE);

        // Add components to form panel
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(confirmPasswordLabel);
        formPanel.add(confirmPasswordField);
        formPanel.add(genderLabel);
        formPanel.add(genderPanel);
        formPanel.add(new JPanel());
        formPanel.add(showPasswordCheckBox);
        formPanel.add(registerButton);
        formPanel.add(backToLoginButton);

        // Add panel to frame
        add(formPanel);

        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    // Show the password
                    passwordField.setEchoChar((char) 0);
                    confirmPasswordField.setEchoChar((char) 0);
                } else {
                    // Hide the password
                    passwordField.setEchoChar('*');
                    confirmPasswordField.setEchoChar('*');
                }
            }
        });

        // Add ActionListener to Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform registration logic here...
                String username = nameField.getText();
                String password = new String(confirmPasswordField.getPassword());
                String confirm_password = new String(passwordField.getPassword());
                String email = emailField.getText();
                String phone = phoneField.getText();

                // Correct gender handling
                String gender = null;
                if (maleButton.isSelected()) {
                    gender = "Male";
                } else if (femaleButton.isSelected()) {
                    gender = "Female";
                }

                if (username.isEmpty() || password.isEmpty() || confirm_password.isEmpty() || email.isEmpty() || phone.isEmpty() || gender == null) {
                    JOptionPane.showMessageDialog(Form_Register.this, "All Fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (password.compareToIgnoreCase(confirm_password) == 0) {

                        // Database connection details
                        String url = "jdbc:mysql://localhost:4000/library";
                        String sql = "INSERT INTO user_account (username, email, phone, password, gender) VALUES (?, ?, ?, ?, ?)";
                        String dbUser = "adminhab";
                        String dbPwd = "mrkimhab20@";
                        boolean isSuccess = false;

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

                            // Set the parameters
                            pstmt.setString(1, username);
                            pstmt.setString(2, email);
                            pstmt.setString(3, phone);
                            pstmt.setString(4, password);
                            pstmt.setString(5, gender);

                            // Execute update
                            pstmt.executeUpdate();
                            isSuccess = true;

                        } catch (SQLException err) {
                            err.printStackTrace();
                            JOptionPane.showMessageDialog(null, "An error occurred while registering.", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                        if (isSuccess) {
                            JOptionPane.showMessageDialog(Form_Register.this, "Registration successful!");
                        }

                    } else {
                        JOptionPane.showMessageDialog(Form_Register.this, "The Confirm Password does not match!");
                    }
                }

                // Return to login form after successful registration
                setVisible(false);
                loginForm.showLoginForm();
            }
        });

        // Add ActionListener to Back to Login button
        backToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hide the register form and show the login form
                setVisible(false);
                loginForm.showLoginForm();
            }
        });
    }

    public static void main(String[] args) {
        // For testing purposes only
        new Form_Register(new Login_form()).setVisible(true);
    }
}
