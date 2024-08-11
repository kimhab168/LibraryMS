package libraryms;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CombineV2 extends JFrame {

    private JPanel centerPanel;

    public CombineV2() {
        // Set up the frame
        setTitle("RUPP's Library");
        setSize(1500, 1000); // Adjust size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Center the frame

        // Title label with modern design
        JLabel titleLabel = new JLabel("RUPP's Library", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Larger font for modern look
        titleLabel.setForeground(Color.WHITE); // White text
        titleLabel.setOpaque(true); // Make background color visible
        titleLabel.setBackground(new Color(50, 50, 150)); // Dark blue background
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2), // White border with 2px thickness
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
        placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        centerPanel.add(placeholderLabel, BorderLayout.CENTER);

        // Button panel at the top right
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 5, 5)); // 4 rows, 1 column
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create and style buttons
        JButton addBookButton = createStyledButton("Add Book");
        JButton viewBookButton = createStyledButton("View Book");
        JButton borrowedBookButton = createStyledButton("Borrowed Book");
        JButton developerButton = createStyledButton("Developer");

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
            centerPanel.removeAll();
            centerPanel.add(new ViewBookPanel(), BorderLayout.CENTER);
            centerPanel.revalidate();
            centerPanel.repaint();
        });

        borrowedBookButton.addActionListener(e -> JOptionPane.showMessageDialog(CombineV2.this, "Borrowed Book clicked"));

        developerButton.addActionListener(e -> JOptionPane.showMessageDialog(CombineV2.this, "Developer clicked"));

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Font style and size
        button.setPreferredSize(new Dimension(200, 40)); // Width and height
        button.setBackground(new Color(70, 130, 180)); // Modern blue color
        button.setForeground(Color.WHITE); // White text
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Padding within the button
        button.setFocusPainted(false); // Remove the default focus border

        // Rounded corners and custom border
        button.setBorder(new RoundedBorder(15)); // Use custom RoundedBorder class

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

    // BookForm integrated directly into the Combine class
    class BookForm extends JPanel {

        private JTextField titleFieldInput;
        private JTextField authorFieldInput;
        private JTextField genreFieldInput;
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

            genreFieldInput = new JTextField();
            styleTextField(genreFieldInput);
            gbc.gridx = 1;
            formPanelInput.add(genreFieldInput, gbc);

            JLabel descriptionLabelInput = new JLabel("Description:");
            styleLabel(descriptionLabelInput);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            formPanelInput.add(descriptionLabelInput, gbc);

            descriptionAreaInput = new JTextArea(6, 30); // Increase height
            descriptionAreaInput.setLineWrap(true);
            descriptionAreaInput.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(descriptionAreaInput);
            styleTextArea(scrollPane);
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.weighty = 1.0; // Allow vertical expansion
            gbc.fill = GridBagConstraints.BOTH;
            formPanelInput.add(scrollPane, gbc);

            // Create a panel for buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            JButton addButton = new JButton("Add");
            addButton.setFont(new Font("Arial", Font.BOLD, 14));
            addButton.setPreferredSize(new Dimension(100, 30));
            addButton.setBackground(new Color(70, 130, 180));
            addButton.setForeground(Color.WHITE);
            addButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            addButton.setFocusPainted(false);
            addButton.setBorder(new RoundedBorder(15));
            buttonPanel.add(addButton);

            JButton clearButton = new JButton("Clear");
            clearButton.setFont(new Font("Arial", Font.BOLD, 14));
            clearButton.setPreferredSize(new Dimension(100, 30));
            clearButton.setBackground(new Color(180, 70, 70));
            clearButton.setForeground(Color.WHITE);
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
            descriptionAreaInput.addKeyListener(enterKeyListener);
        }

        private void styleLabel(JLabel label) {
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setForeground(new Color(50, 50, 150));
        }

        private void styleTextField(JTextField textField) {
            textField.setFont(new Font("Arial", Font.PLAIN, 14));
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
            String bookGenre = genreFieldInput.getText().trim();
            String bookDescription = descriptionAreaInput.getText().trim();

            // Validate input
            if (bookTitle.isEmpty() || bookAuthor.isEmpty() || bookGenre.isEmpty() || bookDescription.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required. Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Database URL and SQL statement
            String url = "jdbc:ucanaccess://D:/MSAccess/habdb.accdb";
            String sql = "INSERT INTO Books (title, author, genre, description) VALUES (?, ?, ?, ?)";

            // Register UCanAccess driver (not necessary if using UCanAccess version 5.0.1 or later)
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); // Load the UCanAccess driver
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "UCanAccess driver not found.", "Driver Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }

            // Use try-with-resources to ensure proper resource management
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Set parameters
                pstmt.setString(1, bookTitle);
                pstmt.setString(2, bookAuthor);
                pstmt.setString(3, bookGenre);
                pstmt.setString(4, bookDescription);

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
            genreFieldInput.setText("");
            descriptionAreaInput.setText("");
        }
    }
    // EditBookForm class to edit an existing book
class EditBookForm extends JPanel {
    private JTextField titleFieldInput;
    private JTextField authorFieldInput;
    private JTextField genreFieldInput;
    private JTextArea descriptionAreaInput;
    private JButton updateButton;
    private JButton cancelButton;
    private String originalTitle; // Store the original title to identify the book to update

    public EditBookForm(String title, String author, String genre, String description) {
        originalTitle = title;
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

        JLabel descriptionLabelInput = new JLabel("Description:");
        styleLabel(descriptionLabelInput);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanelInput.add(descriptionLabelInput, gbc);

        descriptionAreaInput = new JTextArea(6, 30);
        descriptionAreaInput.setLineWrap(true);
        descriptionAreaInput.setWrapStyleWord(true);
        descriptionAreaInput.setText(description);
        JScrollPane scrollPane = new JScrollPane(descriptionAreaInput);
        styleTextArea(scrollPane);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        formPanelInput.add(scrollPane, gbc);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        updateButton = new JButton("Update");
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setPreferredSize(new Dimension(100, 30));
        updateButton.setBackground(new Color(70, 130, 180));
        updateButton.setForeground(Color.WHITE);
        updateButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        updateButton.setFocusPainted(false);
        updateButton.setBorder(new RoundedBorder(15));
        buttonPanel.add(updateButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setBackground(new Color(180, 70, 70));
        cancelButton.setForeground(Color.WHITE);
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
            // Code to go back to the previous panel, e.g., remove the EditBookForm from the centerPanel
            Container parent = getParent();
            if (parent != null) {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }
        });
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(50, 50, 150));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
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

        // Validate input
        if (bookTitle.isEmpty() || bookAuthor.isEmpty() || bookGenre.isEmpty() || bookDescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required. Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database URL and SQL statement
        String url = "jdbc:ucanaccess://D:/MSAccess/habdb.accdb";
        String sql = "UPDATE Books SET title = ?, author = ?, genre = ?, description = ? WHERE title = ?";

        // Register UCanAccess driver
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "UCanAccess driver not found.", "Driver Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Use try-with-resources to ensure proper resource management
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters
            pstmt.setString(1, bookTitle);
            pstmt.setString(2, bookAuthor);
            pstmt.setString(3, bookGenre);
            pstmt.setString(4, bookDescription);
            pstmt.setString(5, originalTitle);

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                Container parent = getParent();
                if (parent != null) {
                    parent.remove(this);
                    parent.revalidate();
                    parent.repaint();
                }
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

        public ViewBookPanel() {
            setLayout(new BorderLayout());

            // Create a table model with column names
            tableModel = new DefaultTableModel(new String[]{"Title", "Author", "Genre", "Description"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Disable editing
                }
            };

            bookTable = new JTable(tableModel);
            bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Set larger, bold font for table header and cells
            Font headerFont = new Font("Arial", Font.BOLD, 18); // Larger and bold font for header
            Font cellFont = new Font("Arial", Font.BOLD, 16);   // Larger and bold font for cells

            // Set header font
            bookTable.getTableHeader().setFont(headerFont);

            // Set cell font
            bookTable.setFont(cellFont);

            // Adjust table row height to accommodate larger font
            bookTable.setRowHeight(cellFont.getSize() + 10);

            // Add table to scroll pane
            JScrollPane scrollPane = new JScrollPane(bookTable);
            add(scrollPane, BorderLayout.CENTER);

            // Load book data
            loadBookData();

            // Create popup menu
            popupMenu = new JPopupMenu();
            JMenuItem editMenuItem = new JMenuItem("Edit");
            JMenuItem deleteMenuItem = new JMenuItem("Delete");
            popupMenu.add(editMenuItem);
            popupMenu.add(deleteMenuItem);

            // Add listeners to menu items
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

        private void loadBookData() {
            // Database URL and SQL statement
            String url = "jdbc:ucanaccess://D:/MSAccess/habdb.accdb";
            String sql = "SELECT title, author, genre, description FROM Books";

            // Register UCanAccess driver (not necessary if using UCanAccess version 5.0.1 or later)
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); // Load the UCanAccess driver
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "UCanAccess driver not found.", "Driver Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }

            // Use try-with-resources to ensure proper resource management
            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                // Clear existing rows
                tableModel.setRowCount(0);

                // Add rows to table model
                while (rs.next()) {
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String genre = rs.getString("genre");
                    String description = rs.getString("description");
                    tableModel.addRow(new Object[]{title, author, genre, description});
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while loading book data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void editBook() {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                String title = (String) tableModel.getValueAt(selectedRow, 0);
                String author = (String) tableModel.getValueAt(selectedRow, 1);
                String genre = (String) tableModel.getValueAt(selectedRow, 2);
                String description = (String) tableModel.getValueAt(selectedRow, 3);

                // Remove existing components in the center panel and add the EditBookForm
                centerPanel.removeAll();
                centerPanel.add(new EditBookForm(title, author, genre, description), BorderLayout.CENTER);
                centerPanel.revalidate();
                centerPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        }


        private void deleteBook() {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                String title = (String) tableModel.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete the book titled \"" + title + "\"?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Database URL and SQL statement
                    String url = "jdbc:ucanaccess://D:/MSAccess/habdb.accdb";
                    String sql = "DELETE FROM Books WHERE title = ?";

                    try {
                        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); // Load the UCanAccess driver
                    } catch (ClassNotFoundException e) {
                        JOptionPane.showMessageDialog(this, "UCanAccess driver not found.", "Driver Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                        return;
                    }

                    // Use try-with-resources to ensure proper resource management
                    try (Connection conn = DriverManager.getConnection(url);
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CombineV2::new);
    }
}
