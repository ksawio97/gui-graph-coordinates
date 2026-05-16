package org.app;


import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.app.controller.GraphDataController;
import org.app.model.Vertex;


public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Graph Coordinates");
            GraphDataController graphDataController = new GraphDataController();
            
            graphDataController.registerOnVerticesChanged((verticies) -> {
                // print all verticies for testing
                for (Vertex v : verticies) {
                    System.out.print(v.toString() + "\n");
                }
            });

            JButton openButton = new JButton("Load Vertex File...");

            openButton.addActionListener(event -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select Input Text File");

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
                fileChooser.setFileFilter(filter);

                // Open the desktop file picker window
                int result = fileChooser.showOpenDialog(frame);

                // Check if the user actually clicked "Open"
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();


                    graphDataController.setInputFile(filePath);
                } else {
                    System.out.println("File selection cancelled.");
                }
            });

            JPanel panel = new JPanel();
            panel.add(openButton);
            frame.add(panel);
            frame.setLocationRelativeTo(null); // Center window on screen
            frame.setVisible(true);


            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setVisible(true);
        });
    }
}
