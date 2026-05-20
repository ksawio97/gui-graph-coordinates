package org.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.app.model.Algorithm;
import org.app.model.FileType;
import org.app.model.Point;
import org.app.model.Vertex;

public class GraphDataController {
    private String iFileName;
    private String oFileName;
    private FileType oFileType;
    private Algorithm algorithm;

	private FileHandler fileHandler;
    private GraphCoordinatesCli graphCoordinatesCli;
    

    private Vertex[] verticies;
    private final List<IOnChangedListener<Vertex[]>> onVerticiesChanged = new ArrayList<>();
    private Point[] points;

    private final List<IOnChangedListener<Point[]>> onPointsChanged = new ArrayList<>();


    public GraphDataController() {
        fileHandler = FileHandler.getInstance();
        graphCoordinatesCli = GraphCoordinatesCli.getInstance();
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

        runGraphCoordinatesCli();
    }


    public void setOutputFile(String oFileName, FileType fileType) {
        this.oFileName = oFileName;
        this.oFileType = fileType;
        
        // it triggers onPointsChanged
        runGraphCoordinatesCli();
    }


    public void setAlgorithm(Algorithm algorithm) {
        // prevent expensive action if nothing changed
        if (algorithm == this.algorithm)
            return;
		this.algorithm = algorithm;
        runGraphCoordinatesCli();
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

    private void onOutputFileChanged(String oFilename, FileType fileType) {
        // refresh our Points data
        try {
            this.points = fileHandler.load_points_data(oFileName, fileType);
        } catch(IOException e) {
            System.out.print("IOException: couldn't read points from " + oFileName);
        }

        onPointsChanged.forEach(f -> f.onChanged(points));
    }
    
    // everytime it's successful it triggers onOutputFileChanged
    private void runGraphCoordinatesCli() {
        if (this.iFileName != null && this.oFileName != null && this.algorithm != null && this.oFileType != null)
            graphCoordinatesCli.execute(
                    this.iFileName,
                    this.oFileName,
                    this.algorithm,
                    this.oFileType,
                    // output file contents changed
                    (outputFileName) -> onOutputFileChanged(outputFileName, this.oFileType),
                    (errMessage) -> System.out.print("Error saving cli data: " + errMessage)
                    );
    }
}
