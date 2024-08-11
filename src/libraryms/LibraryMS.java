package libraryms;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.util.ArrayList;
import java.util.List;

// Chantha class
class PageLibrary extends Frame {

    public PageLibrary() {
        setTitle("Library RUPP");
        setLayout(new BorderLayout());

        // ==========Switch to List=============
        Panel panelSwitch = new Panel();
        panelSwitch.setLayout(new FlowLayout());
        Button btnSwitch = createButton("Switch to List");
        panelSwitch.add(btnSwitch);
        add(panelSwitch, BorderLayout.NORTH);

        // ====================Card layout for input image book=============
        Panel panelImg = new Panel();
        panelImg.setLayout(new GridLayout(0, 3, 10, 10));

        // ===================Card image========================
        addBookCard(panelImg, "./pf1.png", "Kulab Bailin 1");
        addBookCard(panelImg, "./pf1.png", "Kulab Bailin 2");
        addBookCard(panelImg, "./pf1.png", "Kulab Bailin 3");
        addBookCard(panelImg, "./pf1.png", "Kulab Bailin 4");
        addBookCard(panelImg, "./pf1.png", "Kulab Bailin 5");
        addBookCard(panelImg, "./pf1.png", "Kulab Bailin 6");
        addBookCard(panelImg, "./pf1.png", "Kulab Bailin 7");
        addBookCard(panelImg, "./pf1.png", "Kulab Bailin 8");
        addBookCard(panelImg, "./pf1.png", "Kulab Bailin 9");
        addBookCard(panelImg, "./pf1.png", "Kulab Bailin 10");

        // Add the center panel to a ScrollPane
        ScrollPane scrollPaneCenter = new ScrollPane();
        scrollPaneCenter.add(panelImg);

        // Create the right panel with the buttons
        Panel rightPanel = new Panel();
        rightPanel.setLayout(new GridLayout(6, 1, 10, 10)); // 6 rows, 1 column with gaps

        // Add buttons to the right panel
        Button btnViewBook = createButton("View Book");
        Button btnBorrow = createButton("Borrow");
        Button btnViewUser = createButton("Users");
        Button btnManageBook = createButton("Manage Book");
        Button btnDeveloper = createButton("Developer");
        Button btnLogout = createButton("Logout");

        rightPanel.add(btnViewBook);
        rightPanel.add(btnBorrow);
        rightPanel.add(btnViewUser);
        rightPanel.add(btnManageBook);
        rightPanel.add(btnDeveloper);
        rightPanel.add(btnLogout);

        // Main content panel that includes scrollPaneCenter and rightPanel
        Panel mainContentPanel = new Panel();
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.add(scrollPaneCenter, BorderLayout.CENTER);
        mainContentPanel.add(rightPanel, BorderLayout.EAST);

        // Add the main content panel to a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(mainContentPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with the exit button
        Panel bottomPanel = new Panel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Button btnExit = createButton("Exit");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        btnViewBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add action here
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add action here
            }
        });

        btnDeveloper.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add action here
            }
        });

        btnManageBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add action here
            }
        });

        bottomPanel.add(btnExit);
        add(bottomPanel, BorderLayout.SOUTH);
        setSize(800, 600);
        setVisible(true);
    }

    private void addBookCard(Panel panelImg, String imgPath, String bookTitle) {
        Panel bookPanel = new Panel();
        bookPanel.setLayout(new BorderLayout());
        Image img = Toolkit.getDefaultToolkit().getImage(imgPath);

        // Canvas to draw the image
        Canvas canvas = new Canvas() {
            public void paint(Graphics g) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Set preferred size for the canvas
        canvas.setPreferredSize(new Dimension(100, 150));

        // Add the image and label to the panel
        bookPanel.add(canvas, BorderLayout.CENTER);
        Label bookLabel = new Label(bookTitle, Label.CENTER);
        bookPanel.add(bookLabel, BorderLayout.SOUTH);
        bookPanel.setBackground(Color.orange);
        panelImg.add(bookPanel);
    }

    private Button createButton(String label) {
        Button button = new Button(label);
        button.setBackground(new Color(0, 122, 204));
        button.setForeground(Color.WHITE); // Text color
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 144, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 122, 204));
            }
        });

        return button;
    }
}

// Start
// Class library to store all books
// Done
// LibraryMS class
public class LibraryMS extends JFrame {

    // Books inner class
    public class Books {

        private String title;
        private String author;
        private String genre;
        private boolean available;

        public Books(String title, String author, String genre, boolean available) {
            this.title = title;
            this.author = author;
            this.genre = genre;
            this.available = available;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public void setAvailable(boolean avaiable) {
            this.available = available;
        }
    }

    // Add book method
    public static void addBook() {
        String title = JOptionPane.showInputDialog("Enter book title:");
        String author = JOptionPane.showInputDialog("Enter author:");
        String genre = JOptionPane.showInputDialog("Enter genre:");
        int availableOption = JOptionPane.showConfirmDialog(null, "Is the book available?", "Availability", JOptionPane.YES_NO_OPTION);
        boolean available = (availableOption == JOptionPane.YES_OPTION);

        // Table Books
        String url = "jdbc:ucanaccess://D:\\MSAccess\\habdb.accdb";
        String sql = "INSERT INTO Books (title, author, genre, available) VALUES (?, ?, ?, ?)";
        try (Connection cnn = DriverManager.getConnection(url); PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, genre);
            pstmt.setBoolean(4, available);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all books
    public static List<Books> getAllBooks() {
        String url = "jdbc:ucanaccess://D:\\MSAccess\\habdb.accdb";
        String sql = "SELECT * FROM Books";
        Books allBooks[] = new Books[6];
        List<Books> books = new ArrayList<>();

        try (Connection cnn = DriverManager.getConnection(url); PreparedStatement pstmt = cnn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            int i = 0;
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                boolean available = rs.getBoolean("available");

                // allBooks[i] = new Books();
                // new Books(title, author, genre, available);
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return books;
    }

    // Get available books
    public static List<Books> getAvailableBooks() {
        String url = "jdbc:ucanaccess://D:\\MSAccess\\habdb.accdb";
        String sql = "SELECT * FROM Books WHERE available = True";
        List<Books> books = new ArrayList<>();
        ResultSet rss = null;

        try (Connection cnn = DriverManager.getConnection(url); PreparedStatement pstmt = cnn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                boolean available = rs.getBoolean("available");

                // books.add(new Books(title, author, genre, available));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving available books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return books;
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Login");
            loginFrame.setLayout(new GridLayout(4, 2));
            loginFrame.setSize(330, 240);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create components
            JLabel l1 = createLabel("Username");
            JLabel l2 = createLabel("Password");
            JLabel l3 = createLabel("No Account?");
            JTextField usernameTF = new JTextField();
            JPasswordField passwordTF = new JPasswordField();
            JCheckBox showpwdchk = new JCheckBox("Show password");
            JButton loginBtn = createButton("Login");
            JButton signUpBtn = createButton("Sign Up");

            // Show password functionality
            showpwdchk.addActionListener(e -> {
                if (showpwdchk.isSelected()) {
                    passwordTF.setEchoChar((char) 0);
                } else {
                    passwordTF.setEchoChar('*');
                }
            });

            // Login button action
            loginBtn.addActionListener(e -> {
                String username = usernameTF.getText();
                String password = new String(passwordTF.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username or Password cannot be empty");
                } else if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
                    new PageLibrary();
                    loginFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username/Password!");
                }
            });

            // Sign up button action
            signUpBtn.addActionListener(e -> {
                // Handle sign up functionality here
            });

            // Add components to login frame
            loginFrame.add(l1);
            loginFrame.add(usernameTF);
            loginFrame.add(l2);
            loginFrame.add(passwordTF);
            loginFrame.add(showpwdchk);
            loginFrame.add(l3);
            loginFrame.add(loginBtn);
            loginFrame.add(signUpBtn);
            loginFrame.setVisible(true);
        });
    }

    private static JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(200, 30));
        label.setBackground(new Color(20, 122, 255));
        label.setForeground(Color.white);
        return label;
    }

    private static JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setBackground(new Color(20, 122, 255));
        button.setForeground(Color.white);
        button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }
}
