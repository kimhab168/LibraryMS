package libraryms;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class LibraryMS extends JFrame {

    public static String username;
    public static boolean is_available = false;
    FileDropForm FDF = new FileDropForm();
    Font khmerFont = new Font("Khmer OS Siemreap", Font.PLAIN, 16); // Use a font that supports Khmer

    public JPanel centerPanel;

    public LibraryMS(String user) {
        username = user;

        // Set up the frame
        setTitle("RUPP's Library");
        setSize(1500, 1000); // Adjust size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Center the frame

        // Title label with modern design
        JLabel titleLabel = new JLabel("Welcome " + user + " To RUPP's Library", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 36)); // Larger font for modern look
        titleLabel.setOpaque(true); // Make background color visible
        titleLabel.setBackground(new Color(0, 0, 255)); // Dark blue background
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.YELLOW, 2), // White border with 2px thickness
                BorderFactory.createEmptyBorder(10, 20, 10, 20) // Padding around the text
        ));
        add(titleLabel, BorderLayout.NORTH);

        // Center panel with light background color
        centerPanel = new JPanel();
        centerPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around the panel
        centerPanel.setLayout(new BorderLayout());

        // Create a placeholder label for the center panel
        JLabel placeholderLabel = new JLabel("Click the button to Action", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 20));
        centerPanel.add(placeholderLabel, BorderLayout.CENTER);

        // Button panel at the top right
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 5, 5)); // 4 rows, 1 column
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create and style buttons
        JButton addBookButton = createStyledButton("Add Book");
        JButton viewBookButton = createStyledButton("View Book");
        JButton borrowedBookButton = createStyledButton("Borrowed Book");
        JButton developerButton = createStyledButton("Teamates");

        // Add buttons to the panel
        buttonPanel.add(addBookButton);
        buttonPanel.add(viewBookButton);
        buttonPanel.add(borrowedBookButton);
        buttonPanel.add(developerButton);

        // Use a panel with BorderLayout to place the button panel at the top-right
        JPanel topRightPanel = new JPanel(new BorderLayout());
        topRightPanel.add(buttonPanel, BorderLayout.EAST);

        // Add top-right panel and center panel to the frame
        add(centerPanel, BorderLayout.CENTER);
        add(topRightPanel, BorderLayout.EAST);

        // Set up button actions
        addBookButton.addActionListener(e -> {
            centerPanel.removeAll(); // Remove previous components
            centerPanel.add(new BookForm(), BorderLayout.CENTER); // Add the BookForm instance
            centerPanel.revalidate(); // Refresh the panel to show the new content
            centerPanel.repaint(); // Repaint the panel
        });

        viewBookButton.addActionListener(e -> {
            centerPanel.removeAll(); // Clear the center panel
            ViewBookPanel viewBookPanel = new ViewBookPanel(centerPanel); // Create a new instance of ViewBookPanel and pass centerPanel
            centerPanel.add(viewBookPanel, BorderLayout.CENTER); // Add the ViewBookPanel to centerPanel
            centerPanel.revalidate(); // Revalidate the panel to update its content
            centerPanel.repaint(); // Repaint the panel
        });

        borrowedBookButton.addActionListener(e -> {
            centerPanel.removeAll(); // Clear the center panel
            centerPanel.add(new borrowBookUI(), BorderLayout.CENTER); // Add the ViewBookPanel to centerPanel
            centerPanel.revalidate(); // Revalidate the panel to update its content
            centerPanel.repaint(); // Repaint the panel
        });

        developerButton.addActionListener(e -> {
            centerPanel.removeAll(); // Remove previous components
            centerPanel.add(new DeveloperMenu(), BorderLayout.CENTER); // Add the BookForm instance
            centerPanel.revalidate(); // Refresh the panel to show the new content
            centerPanel.repaint(); // Repaint the panel
        });

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 16)); // Font style and size
        button.setPreferredSize(new Dimension(200, 40)); // Width and height
        button.setBackground(new Color(70, 130, 180)); // Modern blue color
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Padding within the button
        button.setFocusPainted(false); // Remove the default focus border

        // Rounded corners and custom border
        button.setBorder(new RoundedBorder(5)); // Use custom RoundedBorder class

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200)); // Lighter blue on hover
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Reset to original color
            }
        });

        return button;
    }

    // Custom border class for rounded corners
    class RoundedBorder implements Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 5, this.radius + 5, this.radius + 5, this.radius + 5);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    // BookForm integrated directly into the LibraryMS class
    class BookForm extends JPanel {

        private JTextField titleFieldInput;
        private JTextField authorFieldInput;
        private JTextField writtenYearFieldInput; // Added writtenYearFieldInput
        private JTextField genreFieldInput; // Added genreFieldInput
        private JTextArea descriptionAreaInput;

        public BookForm() {
            // Set up the panel
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(245, 245, 245)); // Light gray background

            // Create a panel for form fields
            JPanel formPanelInput = new JPanel();
            formPanelInput.setLayout(new GridBagLayout());
            formPanelInput.setBackground(new Color(245, 245, 245)); // Light gray background

            // Define GridBagConstraints for centering components
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); // Padding around components
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            // Create and style components
            JLabel titleLabelInput = new JLabel("Title:");
            styleLabel(titleLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanelInput.add(titleLabelInput, gbc);

            titleFieldInput = new JTextField();
            styleTextField(titleFieldInput);
            gbc.gridx = 1;
            formPanelInput.add(titleFieldInput, gbc);

            JLabel authorLabelInput = new JLabel("Author:");
            styleLabel(authorLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanelInput.add(authorLabelInput, gbc);

            authorFieldInput = new JTextField();
            styleTextField(authorFieldInput);
            gbc.gridx = 1;
            formPanelInput.add(authorFieldInput, gbc);

            JLabel genreLabelInput = new JLabel("Genre:");
            styleLabel(genreLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 2;
            formPanelInput.add(genreLabelInput, gbc);

            genreFieldInput = new JTextField(); // Added genreFieldInput
            styleTextField(genreFieldInput);
            gbc.gridx = 1;
            formPanelInput.add(genreFieldInput, gbc);

            JLabel writtenYearLabelInput = new JLabel("Written Year:"); // Added writtenYearLabelInput
            styleLabel(writtenYearLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 3;
            formPanelInput.add(writtenYearLabelInput, gbc);

            writtenYearFieldInput = new JTextField(); // Added writtenYearFieldInput
            styleTextField(writtenYearFieldInput);
            gbc.gridx = 1;
            formPanelInput.add(writtenYearFieldInput, gbc);

            JLabel descriptionLabelInput = new JLabel("Description:");
            styleLabel(descriptionLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            formPanelInput.add(descriptionLabelInput, gbc);

            descriptionAreaInput = new JTextArea(6, 30); // Increase height
            descriptionAreaInput.setLineWrap(true);
            descriptionAreaInput.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(descriptionAreaInput);
            styleTextArea(scrollPane);
            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.weighty = 1.0; // Allow vertical expansion
            gbc.fill = GridBagConstraints.BOTH;
            formPanelInput.add(scrollPane, gbc);

            JLabel imageLabelInput = new JLabel("Image:");
            styleLabel(imageLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 5;
            formPanelInput.add(imageLabelInput, gbc);

            // Add drag-and-drop functionality for image selection
            styleLabel(genreLabelInput); // This line is likely misplaced and unnecessary, so it is not included in the new code
            gbc.gridx = 1;
            formPanelInput.add(FDF, gbc); // Assume FDF is a file chooser or similar component

            // Create a panel for buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            JButton addButton = new JButton("Add");
            addButton.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 14));
            addButton.setPreferredSize(new Dimension(100, 30));
            addButton.setBackground(new Color(70, 130, 180));
            addButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            addButton.setFocusPainted(false);
            addButton.setBorder(new RoundedBorder(15));
            buttonPanel.add(addButton);

            JButton clearButton = new JButton("Clear");
            clearButton.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 14));
            clearButton.setPreferredSize(new Dimension(100, 30));
            clearButton.setBackground(new Color(180, 70, 70));
            clearButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            clearButton.setFocusPainted(false);
            clearButton.setBorder(new RoundedBorder(15));
            buttonPanel.add(clearButton);

            // Add formPanel and buttonPanel to the main panel
            add(formPanelInput, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            // Set up button actions
            addButton.addActionListener(e -> addBook());

            clearButton.addActionListener(e -> clearForm());

            // Handle Enter key for Add button
            KeyAdapter enterKeyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        addBook();
                    }
                }
            };

            titleFieldInput.addKeyListener(enterKeyListener);
            authorFieldInput.addKeyListener(enterKeyListener);
            genreFieldInput.addKeyListener(enterKeyListener);
            writtenYearFieldInput.addKeyListener(enterKeyListener);
            descriptionAreaInput.addKeyListener(enterKeyListener);
        }

        private void styleLabel(JLabel label) {
            label.setFont(khmerFont);
            label.setForeground(new Color(50, 50, 150));
            label.setHorizontalAlignment(SwingConstants.RIGHT); // Align text to the right
        }

        private void styleTextField(JTextField textField) {
            textField.setFont(khmerFont);
            textField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
            textField.setPreferredSize(new Dimension(200, 30));
        }

        private void styleTextArea(JScrollPane scrollPane) {
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        }

        private void addBook() {
            // Retrieve values from the form
            String bookTitle = titleFieldInput.getText().trim();
            String bookAuthor = authorFieldInput.getText().trim();
            String bookGenre = genreFieldInput.getText().trim(); // Get genre from genreFieldInput
            String writtenYear = writtenYearFieldInput.getText().trim(); // Get written year from writtenYearFieldInput
            String bookDescription = descriptionAreaInput.getText().trim();
            String fileName = FDF.selectedFile.getName();

            // Validate input
            if (FDF.selectedFile == null || bookTitle.isEmpty() || bookAuthor.isEmpty() || bookGenre.isEmpty() || writtenYear.isEmpty() || bookDescription.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required. Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Handle image saving logic
            if (FDF.selectedFile != null) {
                File saveDir = new File("src/images");
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                File saveFile = new File(saveDir, fileName);
                try {
                    Files.copy(FDF.selectedFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    JOptionPane.showMessageDialog(null, "File saved successfully!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "No file selected. Please choose a file first.");
            }

            // Database URL and SQL statement
            String url = "jdbc:mysql://localhost:4000/library";
            String sql = "INSERT INTO Books (title, author, genre, written_year, description, image) VALUES (?, ?, ?, ?, ?, ?)";
            String user = "adminhab";
            String pwd = "mrkimhab20@";

            // Register MySQL driver
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL driver
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Server cannot be connected.", "Driver Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }

            // Use try-with-resources to ensure proper resource management
            try (Connection conn = DriverManager.getConnection(url, user, pwd); PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Set parameters
                pstmt.setString(1, bookTitle);
                pstmt.setString(2, bookAuthor);
                pstmt.setString(3, bookGenre);
                pstmt.setString(4, writtenYear);
                pstmt.setString(5, bookDescription);
                pstmt.setString(6, fileName);

                // Execute the update
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm(); // Clear the form after successful addition
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add the book. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while adding the book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void clearForm() {
            titleFieldInput.setText("");
            authorFieldInput.setText("");
            genreFieldInput.setText(""); // Reset genreFieldInput
            writtenYearFieldInput.setText(""); // Reset writtenYearFieldInput
            descriptionAreaInput.setText("");
            FDF.selectedFile = null;
        }
    }

    class EditBookForm extends JPanel {

        private JTextField titleFieldInput;
        private JTextField authorFieldInput;
        private JTextField writtenYearFieldInput;
        private JTextArea descriptionAreaInput;
        private JTextField genreFieldInput;
        private JButton updateButton;
        private JButton cancelButton;
        private String originalTitle; // Store the original title to identify the book to update
        private JPanel centerPanel; // Reference to centerPanel passed from LibraryMS

        public EditBookForm(String title, String author, String genre, String writtenYear, String description, JPanel centerPanel) {
            this.originalTitle = title;
            this.centerPanel = centerPanel; // Store reference to the main centerPanel

            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(245, 245, 245)); // Light gray background

            // Create a panel for form fields
            JPanel formPanelInput = new JPanel();
            formPanelInput.setLayout(new GridBagLayout());
            formPanelInput.setBackground(new Color(245, 245, 245)); // Light gray background

            // Define GridBagConstraints for centering components
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); // Padding around components
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            // Create and style components
            JLabel titleLabelInput = new JLabel("Title:");
            styleLabel(titleLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanelInput.add(titleLabelInput, gbc);

            titleFieldInput = new JTextField(title);
            styleTextField(titleFieldInput);
            gbc.gridx = 1;
            formPanelInput.add(titleFieldInput, gbc);

            JLabel authorLabelInput = new JLabel("Author:");
            styleLabel(authorLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanelInput.add(authorLabelInput, gbc);

            authorFieldInput = new JTextField(author);
            styleTextField(authorFieldInput);
            gbc.gridx = 1;
            formPanelInput.add(authorFieldInput, gbc);

            JLabel genreLabelInput = new JLabel("Genre:");
            styleLabel(genreLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 2;
            formPanelInput.add(genreLabelInput, gbc);

            genreFieldInput = new JTextField(genre);
            styleTextField(genreFieldInput);
            gbc.gridx = 1;
            formPanelInput.add(genreFieldInput, gbc);

            JLabel writtenYearLabelInput = new JLabel("Written Year:");
            styleLabel(writtenYearLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 3;
            formPanelInput.add(writtenYearLabelInput, gbc);

            writtenYearFieldInput = new JTextField(writtenYear);
            styleTextField(writtenYearFieldInput);
            gbc.gridx = 1;
            gbc.gridy = 3;
            formPanelInput.add(writtenYearFieldInput, gbc);

            JLabel descriptionLabelInput = new JLabel("Description:");
            styleLabel(descriptionLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            formPanelInput.add(descriptionLabelInput, gbc);

            descriptionAreaInput = new JTextArea(6, 30);
            descriptionAreaInput.setLineWrap(true);
            descriptionAreaInput.setWrapStyleWord(true);
            descriptionAreaInput.setText(description);
            JScrollPane scrollPane = new JScrollPane(descriptionAreaInput);
            styleTextArea(scrollPane);
            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;
            formPanelInput.add(scrollPane, gbc);

            // Create a panel for buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            updateButton = new JButton("Update");
            updateButton.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 14));
            updateButton.setPreferredSize(new Dimension(100, 30));
            updateButton.setBackground(new Color(70, 130, 180));
            updateButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            updateButton.setFocusPainted(false);
            updateButton.setBorder(new RoundedBorder(15));
            buttonPanel.add(updateButton);

            cancelButton = new JButton("Cancel");
            cancelButton.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 14));
            cancelButton.setPreferredSize(new Dimension(100, 30));
            cancelButton.setBackground(new Color(180, 70, 70));
            cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            cancelButton.setFocusPainted(false);
            cancelButton.setBorder(new RoundedBorder(15));
            buttonPanel.add(cancelButton);

            // Add formPanel and buttonPanel to the main panel
            add(formPanelInput, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            // Set up button actions
            updateButton.addActionListener(e -> updateBook());

            cancelButton.addActionListener(e -> {
                // Return to the ViewBookPanel
                centerPanel.removeAll();
                centerPanel.add(new ViewBookPanel(centerPanel), BorderLayout.CENTER);
                centerPanel.revalidate();
                centerPanel.repaint();
            });
        }

        private void styleLabel(JLabel label) {
            label.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 16));
            label.setForeground(new Color(50, 50, 150));
        }

        private void styleTextField(JTextField textField) {
            textField.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 14));
            textField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
            textField.setPreferredSize(new Dimension(200, 30));
        }

        private void styleTextArea(JScrollPane scrollPane) {
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        }

        private void updateBook() {
            // Retrieve values from the form
            String bookTitle = titleFieldInput.getText().trim();
            String bookAuthor = authorFieldInput.getText().trim();
            String bookGenre = genreFieldInput.getText().trim();
            String bookDescription = descriptionAreaInput.getText().trim();
            String writtenYear = writtenYearFieldInput.getText().trim();

            // Validate input
            if (bookTitle.isEmpty() || bookAuthor.isEmpty() || bookGenre.isEmpty() || bookDescription.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required. Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Database URL and SQL statement
            String url = "jdbc:mysql://localhost:4000/library";
            String sql = "UPDATE Books SET title = ?, author = ?, written_year = ?, description = ?, genre = ? WHERE title = ?";
            String user = "adminhab";
            String pwd = "mrkimhab20@";

            // Register MySQL driver
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "MySQL driver not found.", "Driver Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }

            // Use try-with-resources to ensure proper resource management
            try (Connection conn = DriverManager.getConnection(url, user, pwd); PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Set parameters
                pstmt.setString(1, bookTitle);
                pstmt.setString(2, bookAuthor);
                pstmt.setString(3, writtenYear);
                pstmt.setString(4, bookDescription);
                pstmt.setString(5, bookGenre);
                pstmt.setString(6, originalTitle);

                // Execute the update
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Return to the ViewBookPanel after successful update
                    centerPanel.removeAll();
                    centerPanel.add(new ViewBookPanel(centerPanel), BorderLayout.CENTER);
                    centerPanel.revalidate();
                    centerPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update the book. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while updating the book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

// Panel to view books
    class ViewBookPanel extends JPanel {

        private JTable bookTable;
        private DefaultTableModel tableModel;
        private JPopupMenu popupMenu;
        private JButton viewToggleButton;
        private JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        private JPanel centerPanel; // Reference to centerPanel passed from LibraryMS
        private ModernLibraryUI menuUI; // Declare here but initialize later

        public ViewBookPanel(JPanel centerPanel) {
            this.centerPanel = centerPanel; // Store reference to the main centerPanel

            setLayout(new BorderLayout());

            // Initialize the menuUI after the centerPanel has been initialized
            this.menuUI = new ModernLibraryUI(centerPanel, this);

            // Create a panel to hold the View button and the checkboxes
            // Create a "View as Menu" button at the top left
            viewToggleButton = new JButton("View as Menu");
            viewToggleButton.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 16));
            viewToggleButton.setPreferredSize(new Dimension(200, 40));
            viewToggleButton.setBackground(new Color(70, 130, 180));
            viewToggleButton.setFocusPainted(false);

            // Add hover effect
            viewToggleButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) {
                    viewToggleButton.setBackground(new Color(100, 150, 200)); // Lighter blue on hover
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    viewToggleButton.setBackground(new Color(70, 130, 180)); // Reset to original color
                }
            });

            // Add action listener to toggle the button text
            viewToggleButton.addActionListener(e -> toggleView());

            // Create the checkbox for "Available Book"
            JCheckBox availableCheckBox = createStyledCheckbox("Available Book", "There are books available!");

            // Add the View button and checkboxes to the top panel
            topPanel.add(viewToggleButton);
            topPanel.add(Box.createHorizontalStrut(20)); // Add margin between the button and the checkboxes
            topPanel.add(availableCheckBox);

            // Add the top panel to the main panel
            add(topPanel, BorderLayout.NORTH);

            // Create a table model with column names
            tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Genre", "Written Year", "Description"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Disable editing
                }
            };

            bookTable = new JTable(tableModel);
            bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Set larger, bold font for table header and cells
            Font headerFont = new Font("Khmer OS Siemreap", Font.BOLD, 20);
            Font cellFont = new Font("Khmer OS Siemreap", Font.BOLD, 17);

            // Set header font
            bookTable.getTableHeader().setFont(headerFont);

            // Set cell font
            bookTable.setFont(cellFont);

            // Adjust table row height to be larger
            bookTable.setRowHeight(cellFont.getSize() + 20);

            // Add table to scroll pane
            JScrollPane scrollPane = new JScrollPane(bookTable);
            add(scrollPane, BorderLayout.CENTER);

            // Load book data
            loadBookData();

            // Create popup menu
            popupMenu = new JPopupMenu();

            // Load and scale icons for the menu items
            ImageIcon viewIcon = scaleIcon(new ImageIcon(getClass().getResource("/icons/view.png")), 30, 30);
            ImageIcon editIcon = scaleIcon(new ImageIcon(getClass().getResource("/icons/edit.png")), 30, 30);
            ImageIcon deleteIcon = scaleIcon(new ImageIcon(getClass().getResource("/icons/delete.png")), 30, 30);

            Font menuItemFont = new Font("Khmer OS Siemreap", Font.PLAIN, 18);

            JMenuItem viewMenuItem = new JMenuItem("View");
            viewMenuItem.setIcon(viewIcon);
            viewMenuItem.setFont(menuItemFont);

            JMenuItem editMenuItem = new JMenuItem("Edit");
            editMenuItem.setIcon(editIcon);
            editMenuItem.setFont(menuItemFont);

            JMenuItem deleteMenuItem = new JMenuItem("Delete");
            deleteMenuItem.setIcon(deleteIcon);
            deleteMenuItem.setFont(menuItemFont);

            popupMenu.setPopupSize(125, 150);
            popupMenu.add(viewMenuItem);
            popupMenu.add(editMenuItem);
            popupMenu.add(deleteMenuItem);

            // Add listeners to menu items
            viewMenuItem.addActionListener(e -> viewBook());
            editMenuItem.addActionListener(e -> editBook());
            deleteMenuItem.addActionListener(e -> deleteBook());

            // Add mouse listener to show popup menu
            bookTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        int row = bookTable.rowAtPoint(e.getPoint());
                        if (row >= 0 && row < bookTable.getRowCount()) {
                            bookTable.setRowSelectionInterval(row, row);
                        }
                        popupMenu.show(bookTable, e.getX(), e.getY());
                    }
                }
            });
        }

        private void toggleView() {
            // Clear the centerPanel that was passed to ViewBookPanel
            centerPanel.removeAll();

            if (viewToggleButton.getText().equals("View as Menu")) {
                centerPanel.add(menuUI, BorderLayout.CENTER); // Add MenuUI to the centerPanel
            } else {
                viewToggleButton.setText("View as Menu");
                centerPanel.add(this, BorderLayout.CENTER); // Add this ViewBookPanel back to the centerPanel
            }

            centerPanel.revalidate(); // Revalidate the panel to update its content
            centerPanel.repaint(); // Repaint the panel
        }

        private JCheckBox createStyledCheckbox(String label, String message) {
            JCheckBox checkBox = new JCheckBox(label);
            checkBox.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 16));
            checkBox.setFocusPainted(false);
            checkBox.setOpaque(false); // Make background transparent

            // Style the checkbox
            checkBox.setUI(new javax.swing.plaf.basic.BasicCheckBoxUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    super.paint(g, c);
                    if (checkBox.isSelected()) {
                        checkBox.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 16));
                    } else {
                        checkBox.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 16));
                    }
                }
            });

            // Change the style when the checkbox is selected
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    // JOptionPane.showMessageDialog(this, message);
                    is_available = true;
                    loadBookData();
                } else {
                    is_available = false;
                    loadBookData();
                }
                checkBox.setFont(checkBox.isSelected()
                        ? new Font("Khmer OS Siemreap", Font.BOLD, 16)
                        : new Font("Khmer OS Siemreap", Font.PLAIN, 16));
            });

            return checkBox;
        }

        public void loadBookData() {
            // Database URL and SQL statement
            String url = "jdbc:mysql://localhost:4000/library";
            String sql = (is_available == true) ? "SELECT id, title, author, genre, written_year, description FROM Books WHERE available=1" : "SELECT id, title, author, genre, written_year, description FROM Books";
            String user = "adminhab";
            String pwd = "mrkimhab20@";

            // Register MySQL driver
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "MySQL driver not found.", "Driver Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }

            // Use try-with-resources to ensure proper resource management
            try (Connection conn = DriverManager.getConnection(url, user, pwd); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

                // Clear existing rows
                tableModel.setRowCount(0);

                // Add rows to table model
                while (rs.next()) {
                    String id = rs.getString("id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String genre = rs.getString("genre");
                    String writtenYear = rs.getString("written_year");
                    String description = rs.getString("description");
                    tableModel.addRow(new Object[]{id, title, author, genre, writtenYear, description});
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while loading book data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void viewBook() {
            JOptionPane.showMessageDialog(this, "View Book functionality is not implemented yet.");
        }

        private void editBook() {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                String title = (String) tableModel.getValueAt(selectedRow, 1);
                String author = (String) tableModel.getValueAt(selectedRow, 2);
                String genre = (String) tableModel.getValueAt(selectedRow, 3);
                String writtenYear = (String) tableModel.getValueAt(selectedRow, 4);
                String description = (String) tableModel.getValueAt(selectedRow, 5);

                // Remove existing components in the center panel and add the EditBookForm
                centerPanel.removeAll();
                centerPanel.add(new EditBookForm(title, author, genre, writtenYear, description, centerPanel), BorderLayout.CENTER);
                centerPanel.revalidate();
                centerPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        }

        private void deleteBook() {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                String title = (String) tableModel.getValueAt(selectedRow, 1);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete the book titled \"" + title + "\"?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Database URL and SQL statement
                    String sql = "DELETE FROM Books WHERE title = ?";
                    String url = "jdbc:mysql://localhost:4000/library";
                    String user = "adminhab";
                    String pwd = "mrkimhab20@";

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                    } catch (ClassNotFoundException e) {
                        JOptionPane.showMessageDialog(this, "MySQL driver not found.", "Driver Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                        return;
                    }

                    // Use try-with-resources to ensure proper resource management
                    try (Connection conn = DriverManager.getConnection(url, user, pwd); PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        // Set parameters
                        pstmt.setString(1, title);

                        // Execute the update
                        int rowsAffected = pstmt.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            loadBookData(); // Reload the data
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete the book. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "An error occurred while deleting the book.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            }
        }

        // Utility method to scale icons
        private ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
            Image img = icon.getImage();
            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LibraryMS(username));
    }
}
