package edu.rpi.cs.csci4963.u19.tumats.hw02.gol_gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class is used to process String data and also create and populate output files (with appropriate names) for each tick.
 * Also creates the directory (name chosen by the user) where the tick files are placed
 *
 * @author Srikanth Tumati
 * @version 1.1
 * @since 1.0
 */
public class WriteData {

    /**
     This method is the main method which is used to place the output from the GameOfLife into new text files.
     * @param outputVal the String that should be placed within the file (which is the board in String format)
     * @param filePattern The file pattern for the data files
     * @param folderPattern The folder pattern (or folder name) for the data files
     * @param currentTick The current tick of the program
     */
    public static void writeFile(String outputVal, String filePattern, String folderPattern, int currentTick){
        try {
            if (!Files.isDirectory(Paths.get(folderPattern))){
                File dir = new File(folderPattern);
                dir.mkdir();
            }
            File file = new File(folderPattern + '/' + filePattern + "_" + currentTick + ".txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(outputVal);
            bw.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }


}
