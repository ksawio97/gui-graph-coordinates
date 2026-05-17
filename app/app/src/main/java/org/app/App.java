package org.app;


import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.app.controller.GraphDataController;
import org.app.model.FileType;
import org.app.model.Point;
import org.app.model.Vertex;
import org.app.view.FileButton;


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

            FileButton inButton = new FileButton("Input file", frame, (file) -> {
                String filePath = file.getAbsolutePath();
                graphDataController.setInputFile(filePath);
            });

            graphDataController.registerOnPointsChanged((points) -> {
                // print all points for testing
                for (Point point : points) {
                    System.out.print(point.toString() + "\n");
                }
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

            JPanel panel = new JPanel();

            panel.add(inButton);
            panel.add(outButton);


            frame.add(panel);
            frame.setLocationRelativeTo(null); // Center window on screen
            frame.setVisible(true);


            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setVisible(true);
        });
    }
}
