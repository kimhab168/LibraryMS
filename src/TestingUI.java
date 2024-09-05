package libraryms;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class TestingUI extends JPanel {
    public File selectedFile;
    JnaFileChooser jj = new JnaFileChooser();
    
    public TestingUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(45, 45, 48));

        // Label for drag-and-drop or file selection
        JLabel dropLabel = new JLabel("Drag and drop Image", SwingConstants.CENTER);
        dropLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dropLabel.setForeground(Color.WHITE);
        dropLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Blue color for the buttons
        Color buttonColor = new Color(0, 0, 255); // Blue

        // "Choose File" button with blue background color
        JButton chooseFileButton = new JButton("Choose Image");
        chooseFileButton.setFont(new Font("Arial", Font.BOLD, 14));
        chooseFileButton.setBackground(buttonColor);
        chooseFileButton.setForeground(Color.WHITE);
        chooseFileButton.setFocusPainted(false);
        chooseFileButton.setBorderPainted(false); // Remove button border
        chooseFileButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // "Choose File" button action
        ActionListener chooseFileAction = e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a File");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setAcceptAllFileFilterUsed(true);

            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
//                dropLabel.setText("File: " + selectedFile.getName());
                displayImage(dropLabel, selectedFile);
            }
        };

        chooseFileButton.addActionListener(chooseFileAction);

        // Panel for drop label and button
        JPanel centerDrop = new JPanel(new GridBagLayout());
        centerDrop.setBackground(new Color(45, 45, 48));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        centerDrop.add(dropLabel, gbc);

        gbc.gridx = 1;
        centerDrop.add(chooseFileButton, gbc);

        // Allow clicking on the drop area to open the file chooser
        centerDrop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chooseFileAction.actionPerformed(null);
            }
        });

        // Drag and Drop functionality
        new DropTarget(centerDrop, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                centerDrop.setBackground(new Color(30, 144, 255)); // Lighter blue on drag
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {}

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {}

            @Override
            public void dragExit(DropTargetEvent dte) {
                centerDrop.setBackground(new Color(45, 45, 48)); // Return to original background
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                centerDrop.setBackground(new Color(45, 45, 48));
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = dtde.getTransferable();
                try {
                    DataFlavor[] flavors = transferable.getTransferDataFlavors();
                    for (DataFlavor flavor : flavors) {
                        if (flavor.isFlavorJavaFileListType()) {
                            java.util.List<File> files = (java.util.List<File>) transferable.getTransferData(flavor);
                            if (!files.isEmpty()) {
                                selectedFile = files.get(0);
                                displayImage(dropLabel, selectedFile);
//                              dropLabel.setText("File: " + selectedFile.getName());
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Add the centerDrop panel to the main panel
        add(centerDrop, new GridBagConstraints());
    }  
    private void displayImage(JLabel label, File file) {
    ImageIcon icon = new ImageIcon(file.getAbsolutePath());
    Image img = icon.getImage();

    // Set the desired fixed height
    int fixedHeight = 300;

    // Calculate the width to maintain the aspect ratio
    int imgWidth = img.getWidth(null);
    int imgHeight = img.getHeight(null);
    int newWidth = (int) ((double) imgWidth / imgHeight * fixedHeight);

    // Scale the image with the new width and the fixed height
    Image scaledImg = img.getScaledInstance(newWidth, fixedHeight, Image.SCALE_SMOOTH);
    label.setIcon(new ImageIcon(scaledImg));
    label.setText(null); // Remove the text
}
    


    public static void main(String[] args) {
        // Create a JFrame to hold the panel
        JFrame frame = new JFrame("File Drop Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 350);

        // Apply the modern Windows look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add the TestingUI panel to the frame
        frame.add(new TestingUI());
        frame.setVisible(true);
    }
}
