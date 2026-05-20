package org.app.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.app.model.FileType;
import org.app.model.Point;
import org.app.model.Vertex;

public final class FileHandler {
    private static FileHandler instance;
    
    private FileHandler() {}

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
        List<Vertex> vertices = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filename)){
            String[] contents;
            // read line contents
            while ((contents = fileReadLineContents(fis)).length != 0) {
                if (contents.length != 4)
                    continue;
                // parse to vertices
                vertices.add(new Vertex(contents[0], Integer.parseInt(contents[1]), Integer.parseInt(contents[2]), Double.parseDouble(contents[3])));
            }
        }
        return vertices.toArray(new Vertex[0]);
    }

    // save modified vertex data to file
    public void save_vertices_data(String filename, Vertex[] vertices) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(filename))) {
            for (int i = 0; i < vertices.length; i++) {
                printWriter.println(vertices[i].toString());
            }
        } 
    }


    private Point[] readPointsFromTxt(String filename) throws IOException {
        List<Point> points = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filename)){
            String[] contents;
            // read line contents
            while ((contents = fileReadLineContents(fis)).length != 0) {
                if (contents.length != 3)
                    continue;
                // parse points 
                points.add(new Point(contents[0], Double.parseDouble(contents[1]), Double.parseDouble(contents[2])));
            }
        }
        return points.toArray(new Point[0]);
    }


    private Point[] readPointsFromBin(String filename) throws IOException {
        List<Point> points = new ArrayList<>();
        // 3 fields of 8 bytes so 24 bytes 
        final int RECORD_SIZE = 24;

        try (FileInputStream fis = new FileInputStream(filename)){
            byte[] buff = new byte[RECORD_SIZE];
            while (fis.read(buff) == RECORD_SIZE) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(buff);
                // we need to switch to order of c binaryfile output
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                
                double id = byteBuffer.getDouble();
                double x  = byteBuffer.getDouble();
                double y  = byteBuffer.getDouble();

                points.add(new Point(Double.toString(id), x, y));
            }
        }

        return points.toArray(new Point[0]);
    }


    // load points data from file
    public Point[] load_points_data(String filename, FileType fileType) throws IOException {
        switch (fileType) {
            case TXT:
                return readPointsFromTxt(filename);
            case BIN:
                return readPointsFromBin(filename);
            default:
                throw new UnsupportedOperationException("This file type is not supported");
        }
    }
    
    // // run algorithm (returns false if error)
    // public boolean run_c_alg(Algorithm algorithm, FileType fileType);
}
