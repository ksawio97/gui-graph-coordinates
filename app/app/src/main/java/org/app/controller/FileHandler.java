package org.app.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.app.model.FileType;
import org.app.model.Point;
import org.app.model.Vertex;

public final class FileHandler {
    private static FileHandler instance;
    
    public static FileHandler getInstance() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }
    
    private final String[] fileReadLineContents(FileInputStream fis) throws IOException {
        StringBuffer content = new StringBuffer();
        List<String> contents = new ArrayList<String>(); 
        int c;
        while ((c = fis.read()) != -1 && c != '\n') {
            if (c == ' ') {
                if (!content.isEmpty()) {
                    contents.add(content.toString());
                    content = new StringBuffer();
                }
                continue;
            }
            content.append((char) c);
        }
        if (!content.isEmpty())
            contents.add(content.toString());
        return contents.toArray(new String[0]);
    }

    // load vertex data from file
    public Vertex[] load_vertices_data(String filename) throws IOException {
        List<Vertex> vericies = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filename)){
            String[] contents;
            // read line contents
            while ((contents = fileReadLineContents(fis)).length != 0) {
                if (contents.length != 4)
                    continue;
                // parse to vericies
                vericies.add(new Vertex(contents[0], Integer.parseInt(contents[1]), Integer.parseInt(contents[2]), Double.parseDouble(contents[3])));
            }
        }
        return vericies.toArray(new Vertex[0]);
    }

    // save modified vertex data to file
    public void save_vertices_data(String filename, FileType fileType) {


    }



    // load points data from file
    public Point[] load_points_data(String filename, FileType fileType) {

        switch (fileType) {
            case FileType.TXT:
                break;
            case FileType.BIN:
                throw new UnsupportedOperationException("Not implemented yet");
            default:
                throw new UnsupportedOperationException("Not implemented yet");
        }
        return null;
    }

    // save points data to file
    public void save_points_data(String filename, FileType fileType) {
        
    }
    
    // // run algorithm (returns false if error)
    // public boolean run_c_alg(Algorithm algorithm, FileType fileType);
}
