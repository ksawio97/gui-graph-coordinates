package org.app.view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

public class FileButton extends JButton {

    public FileButton(String label, Frame parent, Consumer<File> onFileSelected, boolean fileTxt) {
        super(label);
        addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Input Text File");
            
            if (fileTxt) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
                fileChooser.setFileFilter(filter);
            }

            int result = fileChooser.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                onFileSelected.accept(fileChooser.getSelectedFile());
            } else {
                System.out.println("File selection cancelled.");
            }
        });
    }
}
