package org.app.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.app.model.Algorithm;
import org.app.model.FileType;

public class GraphCoordinatesCli {
    private static final String CLI_PATH = "./tools/graphcoordinates";
    
    private static GraphCoordinatesCli instance;

    private GraphCoordinatesCli() {}
    
    public static GraphCoordinatesCli getInstance() {
        if (instance == null) {
            instance = new GraphCoordinatesCli();
        }

        return instance;
    }

    public void execute(String inputFilePath, String outputFilePath, Algorithm alg, FileType fileType, IOnGraphCoordinatesCliExecution onSuccess, Consumer<String> onFail) {
        ProcessBuilder pb = 
            new ProcessBuilder(CLI_PATH, inputFilePath, outputFilePath, alg.toString(), fileType.toString());
        try {
            Process process = pb.start();

            process.onExit().whenComplete((completedProcess, exception) -> {

                if (exception != null) {
                    onFail.accept("JVM Error while waiting for process: " + exception.getMessage());
                    return;
                }

                if (completedProcess.exitValue() != 0) {
                    // Read the error message the C tool printed to stderr
                    String errorMessage = readErrorStream(completedProcess);
                    onFail.accept("Tool failed (Code " + completedProcess.exitValue() + "): " + errorMessage);
                    return;
                }

                onSuccess.onCliExecution(outputFilePath);
            });

        } catch (IOException e) {
            onFail.accept("Failed to launch the CLI tool: " + e.getMessage());
        }
    }

    private String readErrorStream(Process process) {
        try {
            byte[] errorBytes = process.getErrorStream().readAllBytes();
            return new String(errorBytes, StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            return "Could not read error output.";
        }
    }
} 
