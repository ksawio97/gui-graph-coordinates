package org.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.app.model.FileType;
import org.app.model.Point;
import org.app.model.Vertex;

public class GraphDataController {
    // for now we store variables here bcs in future we might modify this files
    private String iFileName;
    private String oFileName;
    private FileType outFileType;

    private FileHandler fileHandler;
    

    private Vertex[] verticies;
    private Map<String, Integer> nameToVertexIndex;
    private final List<IOnChangedListener<Vertex[]>> onVerticiesChanged = new ArrayList<>();
    private Point[] points;

    private final List<IOnChangedListener<Point[]>> onPointsChanged = new ArrayList<>();


    public GraphDataController() {
        fileHandler = FileHandler.getInstance();
    }

    public void setInputFile(String iFileName) {
        this.iFileName = iFileName;

        // refresh our Vertex data
        try {
            verticies = fileHandler.load_vertices_data(iFileName);
        } catch(IOException e) {
            System.out.print("IOException: couldn't read vertices from " + iFileName);
        }

        onVerticiesChanged.forEach(f -> f.onChanged(verticies));
    }


    public void setOutputFile(String oFileName, FileType fileType) {
        this.oFileName = oFileName;
        this.outFileType = fileType;

        // refresh our Points data
        try {
            points = fileHandler.load_points_data(oFileName, fileType);
        } catch(IOException e) {
            System.out.print("IOException: couldn't read points from " + oFileName);
        }

        onPointsChanged.forEach(f -> f.onChanged(points));
    }

    public void registerOnVerticesChanged(IOnChangedListener<Vertex[]> onChanged) {
        onVerticiesChanged.add(onChanged);
    }

    public void registerOnPointsChanged(IOnChangedListener<Point[]> onChanged) {
        onPointsChanged.add(onChanged);
    }

    public Vertex[] gVerticesModels() {
        return this.verticies;
    }

    public Point[] gPoints() {
        return this.points;
    }

    // saves modified input file to choosen location
    public void saveVerticiesChangesToFile(String filename) throws IOException {
        fileHandler.save_vertices_data(filename, this.verticies);
    }

    // modifies current input file 
    public void saveVerticiesChangesToFile() throws IOException {
        fileHandler.save_vertices_data(this.iFileName, this.verticies);
    }
}
