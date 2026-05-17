package org.app.view;

import java.awt.Frame;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class FileButton extends JButton {

    public FileButton(String label, Frame parent, Consumer<File> onFileSelected) {
        super(label);
        addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Input Text File");

            int result = fileChooser.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                onFileSelected.accept(fileChooser.getSelectedFile());
            } else {
                System.out.println("File selection cancelled.");
            }
        });
    }
}
