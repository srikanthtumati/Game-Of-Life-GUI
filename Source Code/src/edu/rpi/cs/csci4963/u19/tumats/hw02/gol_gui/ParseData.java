package edu.rpi.cs.csci4963.u19.tumats.hw02.gol_gui;

import javafx.util.Pair;
import java.io.*;

/**
 * This class reads input data from a file that is given by the user. Additionally, the data is checked to ensure that
 * the input file is appropriately formatted.
 *
 * @author Srikanth Tumati
 * @version 1.1
 * @since 1.0
 */
public class ParseData {

    /**
     * This method is the main method which reads the file and transfers the appropriate row data the GameOfLife object
     * @param filename the file which is being used as a source for data
     * @param gol The GameOfLife object that is currently being used in the program
     */
    public static void readFile(String filename, GameOfLife gol){
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            int counter = 0;
            String line = br.readLine();
            Pair<Integer, Integer> dimensions = parseDimensions(line);
            gol.initializeBoard(dimensions);
            while ((line = br.readLine()) != null){
                gol.setRow(parseRowData(line, dimensions.getValue()), counter);
                counter += 1;
            }
            if (counter != dimensions.getKey()){
                System.out.println("Given Dimensions do not match input data!");
                System.exit(1);
            }
        }
        catch (IOException ex){
            System.out.println("Input file unable to be read/found!");
            System.exit(1);
        }
    }

    /**
     * This method analyzes an individual row of data from the input file. Ultimately, the method converts this data
     * into Integer values and places them in order, into an Integer array.
     * @param line The specific line of data from the input file which is being checked/utilized.
     * @param cols The required number of columns that must currently be in the row of data in order for the board to be
     * fully populated
     * @return An Integer array containing the data that was gathered from the specific row from the input file
     */
    public static Integer[] parseRowData(String line, Integer cols){
        Integer[] rowData = new Integer[cols];
        String[] values = line.split(",");
        if (values.length != cols) {
            System.out.println("Incorrect number of row data found in input file!");
            System.exit(1);
        }
        for (int count = 0; count < cols; count++){
            try {
                rowData[count] = Integer.parseInt(values[count].trim());
            }
            catch(NumberFormatException ex){
                System.out.println("Invalid Integer data found in input file!");
                System.exit(1);
            }
        }
        return rowData;
    }

    /**
     * This method parses the first line of the input file (which should contain the dimensions for the board. Additionally,
     * if the input file is incorrectly formatted, then the appropriate error is thrown.
     * @param line String data from the input file that should contain the dimensions for the board
     * @return The dimensions which should be used when creating the internal boards in the GameOfLife
     */
    public static Pair<Integer, Integer> parseDimensions(String line){
        Integer[] dimensions = new Integer[2];
        String[] values = line.split(",");
        if (values.length != 2) {
            System.out.println("Invalid number of dimensions arguments given!");
            System.exit(1);
        }
        for (int count = 0; count < 2; count++){
            try {
                dimensions[count] = Integer.parseInt(values[count].trim());
            }
            catch (NumberFormatException ex){
                System.out.println("Invalid Integer data found! Please verify the first line of the input file contains ONLY the row and column in the form 'row, column'");
                System.exit(1);
            }
        }
        return new Pair<>(dimensions[0], dimensions[1]);
    }

}
