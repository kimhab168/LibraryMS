package libraryms;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeveloperMenu extends JPanel {

    public DeveloperMenu() {
        setLayout(new BorderLayout());

        // Add padding to the mainPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 20px padding around the panel
        mainPanel.setLayout(new GridLayout(2, 4, 10, 10)); // 2 rows, 4 columns, 10px padding
        String DevFirstName[] = {"KHUN", "TOUCH", "CHAT", "SOU", "IM", "OEURN", "KONG", "KOUNG"};
        String DevLastName[] = {"KIMHAB", "CHANTHA", "BOREY", "CHAMROEUNRAKSA", "CHANTHYDONA", "NUPHEA", "MONI", "MENGHOUR"};
        String DevFirstKHName[] = {"ឃុន", "ទូច", "ចាត", "ស៊ូ", "អុីម", "អឿន", "គង់", "គួង"};
        String DevLastKHName[] = {"គឹមហាប់", "ចាន់ថា", "បូរី", "ចំរើនរក្សា", "ចាន់ធីដូណា", "នុភា", "មុនី", "ម៉េងហួរ"};
        String image_path[] = {"kimhab.png", "chantha.png", "borey.png", "reaksa.png", "dona.png", "nuphea.png", "moni.png", "menghour.png"};
        mainPanel.setBackground(Color.WHITE);

        for (int i = 0; i < 8; i++) {
            String firstName = DevFirstName[i];
            String lastName = DevLastName[i];
            String image = image_path[i];
            JPanel itemPanel = createItemPanel(firstName, lastName, image);
            mainPanel.add(itemPanel);
        }

        // Add mainPanel to the JScrollPane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Increase for faster scrolling

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createItemPanel(String firstName, String lastName, String image) {
        JPanel itemPanel = new RoundedPanel(20, Color.LIGHT_GRAY); // Rounded corners with a radius of 20 and a gray border
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBackground(Color.WHITE);

        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Reduced padding to 10px on all sides

        // Placeholder for image with fixed width and auto height
        ImageIcon icon = new ImageIcon("src/teamates/" + image);
        int newWidth = 160;
        int newHeight = (int) ((double) newWidth / icon.getIconWidth() * icon.getIconHeight()); // Auto height
        Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(newWidth, newHeight));
        imageLabel.setMaximumSize(new Dimension(newWidth, newHeight));

        // Full name label
        String labelName = firstName + " " + lastName;
        JLabel titleLabel = new JLabel(labelName);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 14));
        titleLabel.setForeground(Color.DARK_GRAY);

        // Class label
        String classE1 = "E1-108";
        JLabel classLabel = new JLabel("Class: " + classE1);
        classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        classLabel.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 12));
        classLabel.setForeground(Color.GRAY);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Decreased padding at the bottom
        buttonPanel.setBackground(Color.WHITE);

        JButton viewButton = new RoundedButton("View", Color.BLUE, Color.WHITE);
        buttonPanel.add(viewButton);

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMemberDetails(labelName, imageLabel.getIcon());
            }
        });

        // Add components to itemPanel
        itemPanel.add(imageLabel);
        itemPanel.add(Box.createVerticalStrut(10)); // Add space between components
        itemPanel.add(titleLabel);
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(classLabel);
        itemPanel.add(Box.createVerticalStrut(10));
        itemPanel.add(buttonPanel);

        return itemPanel;
    }

    private void showMemberDetails(String fullname, Icon imageIcon) {
        JFrame detailsFrame = new JFrame(fullname);
        detailsFrame.setSize(400, 600);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailsFrame.setLocationRelativeTo(null);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the panel

        ImageIcon icon = (ImageIcon) imageIcon;
        int newWidth = 130;
        int newHeight = (int) ((double) newWidth / icon.getIconWidth() * icon.getIconHeight()); // Auto height
        Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(newWidth, newHeight));
        imageLabel.setMaximumSize(new Dimension(newWidth, newHeight));

        JLabel fullnameENG = new JLabel(fullname);
        fullnameENG.setFont(new Font("Khmer OS Siemreap", Font.BOLD, 16));
        fullnameENG.setAlignmentX(Component.CENTER_ALIGNMENT);
        fullnameENG.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel authorLabel = new JLabel("E1-108 Year II ");
        authorLabel.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 14));
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        authorLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel phraseLabel = new JLabel("Phrase: SL KE MNEAK ENG");
        phraseLabel.setFont(new Font("Khmer OS Siemreap", Font.PLAIN, 20));
        phraseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        phraseLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Center the buttons
        buttonPanel.setBackground(Color.WHITE);

        JButton backButton = new RoundedButton("Back", Color.RED, Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailsFrame.dispose(); // Close the details frame
            }
        });
        buttonPanel.add(backButton);

        detailsPanel.add(imageLabel);
        detailsPanel.add(fullnameENG);
        detailsPanel.add(authorLabel);
        detailsPanel.add(phraseLabel);
        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(buttonPanel);

        detailsFrame.add(detailsPanel);
        detailsFrame.setVisible(true);
    }
}

// CustomScrollBarUI class
class CustomScrollBarUI extends BasicScrollBarUI {

    @Override
    protected void configureScrollBarColors() {
        thumbColor = Color.BLUE; // Set custom thumb color
        trackColor = Color.LIGHT_GRAY; // Set custom track color
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
