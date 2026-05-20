package org.app;


import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.app.controller.GraphDataController;
import org.app.model.Algorithm;
import org.app.model.FileType;
import org.app.view.FileButton;
import org.app.view.GraphPanel;


public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Graph Coordinates");
            
            GraphDataController graphDataController = new GraphDataController();
            GraphPanel graphPanel = new GraphPanel();

            graphDataController.registerOnVerticesChanged((vertices) -> {
                // Update graph visualization
                graphPanel.setGraphData(vertices, graphDataController.gPoints());
            });

            FileButton inButton = new FileButton("Input file", frame, (file) -> {
                String filePath = file.getAbsolutePath();
                graphDataController.setInputFile(filePath);
            });

            graphDataController.registerOnPointsChanged((points) -> {
                // Update graph visualization
                graphPanel.setGraphData(graphDataController.gVerticesModels(), points);
            });

            FileButton outButton = new FileButton("Output file", frame, (file) -> {
                String filePath = file.getAbsolutePath();
                String fileName = file.getName();
                String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                FileType fileType = null;

                switch (fileExt) {
                    case "txt":
                        fileType = FileType.TXT;
                        break;
                    case "bin":
                        fileType = FileType.BIN;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Unsupported file format! Only txt and bin are supported", "Error", JOptionPane.ERROR_MESSAGE);
                        return;

                }
                graphDataController.setOutputFile(filePath, fileType);
            });

            JToggleButton fruhmanBtn = new JToggleButton("Fruhman");
            JToggleButton tuttermanBtn = new JToggleButton("Tutterman");

            fruhmanBtn.addActionListener(e -> {
                graphDataController.setAlgorithm(Algorithm.FR);
            });

            tuttermanBtn.addActionListener(e -> {
                graphDataController.setAlgorithm(Algorithm.TUTTE);
            });

            ButtonGroup group = new ButtonGroup();

            group.add(fruhmanBtn);
            group.add(tuttermanBtn);

            fruhmanBtn.setSelected(true);

            JPanel controlPanel = new JPanel();
            controlPanel.add(inButton);
            controlPanel.add(outButton);

            controlPanel.add(fruhmanBtn);
            controlPanel.add(tuttermanBtn);

            frame.add(controlPanel, "North");
            frame.add(graphPanel, "Center");
            
            frame.setLocationRelativeTo(null); // Center window on screen
            frame.setSize(900, 750);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            
            // Load default files from resources
            try {
                String inPath = App.class.getResource("/in.txt").getPath();
                String outPath = App.class.getResource("/out.txt").getPath();
                graphDataController.setInputFile(inPath);
                graphDataController.setOutputFile(outPath, FileType.TXT);
                graphDataController.setAlgorithm(Algorithm.FR);
            } catch (Exception e) {
                System.out.println("Could not load default files: " + e.getMessage());
            }
        });
    }
}
