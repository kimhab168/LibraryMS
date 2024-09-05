package libraryms;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ModernLibraryUI extends JPanel {

    private JButton viewToggleButton;
    private JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel mainPanel;
    public static boolean is_available = false;

    public ModernLibraryUI(JPanel centerPanel, JPanel ListUI) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // 20px padding around the panel
        setBackground(Color.WHITE);

        // Create a "View as Menu" button at the top left
        viewToggleButton = new JButton("View as List");
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
        viewToggleButton.addActionListener(e -> toggleView(centerPanel, ListUI));

        // Main panel to hold the books
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 20px padding around the panel
        mainPanel.setLayout(new GridLayout(0, 5, 10, 10)); // 5 rows, 5 columns, 10px padding
        mainPanel.setBackground(Color.WHITE);

        // Create the checkbox for "Available Book"
        JCheckBox availableCheckBox = createStyledCheckbox("Available Book");

        // Add the View button and checkboxes to the top panel
        topPanel.add(viewToggleButton);
        topPanel.add(Box.createHorizontalStrut(20)); // Add margin between the button and the checkboxes
        topPanel.add(availableCheckBox);

        // Add the top panel to the main panel
        add(topPanel, BorderLayout.NORTH);

        // Fetch initial book data from the database
        loadBookData();

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);

        // Disable horizontal scrolling
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Customize vertical scroll bar
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Increase this value for faster scrolling

        add(scrollPane, BorderLayout.CENTER);
    }

    private JCheckBox createStyledCheckbox(String label) {
        JCheckBox checkBox = new JCheckBox(label);
        checkBox.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 16));
        checkBox.setFocusPainted(false);
        checkBox.setOpaque(false); // Make background transparent

        // Change the style when the checkbox is selected
        checkBox.addActionListener(e -> {
            is_available = checkBox.isSelected(); // Toggle availability filter
            loadBookData(); // Reload book data based on the availability filter
            checkBox.setFont(checkBox.isSelected()
                    ? new Font("Khmer OS Siemreap", Font.BOLD, 16)
                    : new Font("Khmer OS Siemreap", Font.PLAIN, 16));
        });

        return checkBox;
    }

    private void loadBookData() {
        // Clear the current content from mainPanel
        mainPanel.removeAll();

        // Database URL and SQL statement
        String url = "jdbc:mysql://localhost:4000/library";
        String sql = is_available
                ? "SELECT id, title, author, written_year, description, genre, image FROM Books WHERE available=1"
                : "SELECT id, title, author, written_year, description, genre, image FROM Books";
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
        try (Connection conn = DriverManager.getConnection(url, user, pwd);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Add rows to mainPanel
            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int writtenYear = rs.getInt("written_year");
                String description = rs.getString("description");
                String genre = rs.getString("genre");
                String image = rs.getString("image");
                JPanel itemPanel = createItemPanel(Integer.parseInt(id), title, author, genre, description, image, writtenYear);
                mainPanel.add(itemPanel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while loading book data.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Revalidate and repaint to update the mainPanel
        mainPanel.revalidate();
        mainPanel.repaint();
    }

private JPanel createItemPanel(int index, String title, String author, String genre, String description, String image, int written_year) {
    JPanel itemPanel = new RoundedPanel(20, Color.LIGHT_GRAY); // Rounded corners with a radius of 20 and a gray border
    itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
    itemPanel.setBackground(Color.WHITE);

    // Set preferred size based on the largest child component and padding
    int panelWidth = 218; // 218px for width + padding on each side
    int panelHeight = 350; // 350px for height + padding on each side
    itemPanel.setPreferredSize(new Dimension(panelWidth, panelHeight)); // Set preferred size for uniformity

    itemPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Increased padding inside the panel

    // Placeholder for image
    JLabel imageLabel = new JLabel(new ImageIcon("src/images/" + image));
    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    imageLabel.setPreferredSize(new Dimension(156, 180));
    imageLabel.setMaximumSize(new Dimension(156, 180));

    // Book title
    JLabel titleLabel = new JLabel(title);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    titleLabel.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 14));
    titleLabel.setForeground(Color.DARK_GRAY);

    // Book genre and written year
    JLabel yearLabel = new JLabel("Genre: " + genre + ", Year: " + written_year);  // Combine genre and year
    yearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    yearLabel.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 12));
    yearLabel.setForeground(Color.GRAY);

    // Buttons panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Increased horizontal gap
    buttonPanel.setBackground(Color.WHITE);

    // Custom buttons with rounded corners and hover effect
    JButton borrowButton = new RoundedButton("Borrow", Color.YELLOW, Color.BLACK);
    buttonPanel.add(borrowButton);
    
    borrowButton.addActionListener(e -> borrowFunction(title));

    JButton viewButton = new RoundedButton("View", Color.BLUE, Color.WHITE);
    buttonPanel.add(viewButton);

    viewButton.addActionListener(e -> showBookDetails(title, author, genre, description, String.valueOf(written_year), imageLabel.getIcon()));

    // Add components to itemPanel
    itemPanel.add(imageLabel);
    itemPanel.add(Box.createVerticalStrut(10)); // Add space between components
    itemPanel.add(titleLabel);
    itemPanel.add(Box.createVerticalStrut(5));
    itemPanel.add(yearLabel);
    itemPanel.add(Box.createVerticalStrut(10));
    itemPanel.add(buttonPanel);

    // Add double-click event to trigger detailed view
    itemPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                showBookDetails(title, author, genre, description, String.valueOf(written_year), imageLabel.getIcon());
            }
        }
    });

    return itemPanel;
}

    private void showBookDetails(String title, String author, String genre, String description, String year, Icon image) {
        JFrame detailsFrame = new JFrame(title);
        detailsFrame.setSize(600, 800);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailsFrame.setLocationRelativeTo(null);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the panel

        JLabel imageLabel = new JLabel(image);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setMaximumSize(new Dimension(150, 150));

        JLabel titleLabel = new JLabel("Title: " + title);
        titleLabel.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel authorLabel = new JLabel("Author: " + author);
        authorLabel.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 14));
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        authorLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel genreLabel = new JLabel("Genre: " + genre);
        genreLabel.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 14));
        genreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        genreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel yearLabel = new JLabel("Year: " + year);
        yearLabel.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 14));
        yearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        yearLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Buttons panel for Back and Borrow buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Center the buttons
        buttonPanel.setBackground(Color.WHITE);

        // Back button
        JButton backButton = new RoundedButton("Back", Color.RED, Color.WHITE);
        backButton.addActionListener(e -> detailsFrame.dispose()); // Close the details frame
        buttonPanel.add(backButton);

        // Borrow button
        JButton borrowButton = new RoundedButton("Borrow", Color.GREEN, Color.WHITE);
        buttonPanel.add(borrowButton);
        
         borrowButton.addActionListener(e -> borrowFunction(title));

        detailsPanel.add(imageLabel);
        detailsPanel.add(titleLabel);
        detailsPanel.add(authorLabel);
        detailsPanel.add(genreLabel);
        detailsPanel.add(yearLabel);
        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(new JScrollPane(descriptionArea));
        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(buttonPanel); // Add the buttons panel to the details panel

        detailsFrame.add(detailsPanel);
        detailsFrame.setVisible(true);
    }
            private void borrowFunction(String title) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to borrow the book titled \"" + title + "\"?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION){
            // Database URL and SQL statement
            String url = "jdbc:mysql://localhost:4000/library";
            String sql = "UPDATE Books SET available = ? WHERE title = ?";
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
                pstmt.setBoolean(1, false);
                pstmt.setString(2, title);

                // Execute the update
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Book Borrowed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to Borrow the book. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while Borrowing the book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
}
        }
  

    private void toggleView(JPanel centerPanel, JPanel ListUI) {
        // Clear the centerPanel that was passed to ViewBookPanel
        centerPanel.removeAll();

        if (viewToggleButton.getText().equals("View as List")) {
            centerPanel.add(ListUI, BorderLayout.CENTER); // Add MenuUI to the centerPanel
        }

        centerPanel.revalidate(); // Revalidate the panel to update its content
        centerPanel.repaint(); // Repaint the panel
    }
}

// Custom button class with rounded corners and hover effects
class RoundedButton extends JButton {

    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;

    public RoundedButton(String text, Color backgroundColor, Color txtColor) {
        super(text);
        setContentAreaFilled(false);
        setBackground(backgroundColor);
        setForeground(txtColor);
        setFocusPainted(false); // Remove focus border
        setFont(new Font("Khmer OS Siemreap", Font.BOLD, 12));

        // Add padding inside the button to account for the border
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        hoverBackgroundColor = backgroundColor.darker();
        pressedBackgroundColor = backgroundColor.darker().darker();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackgroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(backgroundColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBackgroundColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverBackgroundColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Ensure consistent border size by using the same stroke width regardless of background color
        g2.setStroke(new BasicStroke(2));
        g2.setColor(getBackground().darker());  // Use a consistent border color
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10); // Adjust the border to fit within the button

        g2.dispose();
    }
}

// Custom panel class with rounded corners
class RoundedPanel extends JPanel {

    private int radius;
    private Color borderColor;

    public RoundedPanel(int radius, Color borderColor) {
        super();
        this.radius = radius;
        this.borderColor = borderColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);

        // Draw the border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2)); // Set border thickness
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius); // Adjusted to fit within the panel
    }
}

// Corrected CustomScrollBarUI class
class CustomScrollBarUI extends BasicScrollBarUI {

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(169, 169, 169); // Light black (dark gray)
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}
