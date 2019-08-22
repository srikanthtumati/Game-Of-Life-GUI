package edu.rpi.cs.csci4963.u19.tumats.hw02.gol_gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Creates the statistics that are displayed to the user for the entire lifetime of the program. Additionally, provides
 * methods for other classes to access to update the values in the statistics
 *
 * @author Srikanth Tumati
 * @version 1.0
 * @since 1.0
 */
public class UserStatistics extends GridPane {

    /** The GridPane that is used to house all the values and present them to the user in an organized manner */
    private GridPane gridPane = new GridPane();
    /** Contains the value for the tick that the program is currently on */
    private Label currentTickVal = new Label("0");
    /** Contains the value for the number of cells that are alive on the current tick */
    private Label currentAliveVal = new Label("0");
    /** Contains the value for the number of cells that are dead on the current tick */
    private Label currentDeadVal = new Label("0");
    /** Contains the value for the number of cells that were alive on the previous tick (reverts to 0 if the program is on tick 0 */
    private Label previousAliveVal = new Label("0");
    /** Contains the value for the number of cells that were dead on the previous tick (reverts to 0 if the program is on tick 0 */
    private Label previousDeadVal = new Label("0");
    /** The label presented to the user that is paired with currentTickVal */
    private Label currentTick = new Label("Current Tick: ");
    /** The label presented to the user that is paired with currentAliveVal */
    private Label currentAlive = new Label("Number of Alive Cells: ");
    /** The label presented to the user that is paired with currentDeadVal */
    private Label currentDead = new Label("Number of Dead Cells");
    /** The label presented to the user that is paired with previousAliveVal */
    private Label previousAlive = new Label("Alive Cells compared to previous tick: ");
    /** The label presented to the user that is paired with previousDeadVal */
    private Label previousDead = new Label("Dead Cells compared to previous tick: ");

    /**
     * Constructor for the UserStatistics class
     */
    public UserStatistics(){
        gridPane.add(currentTick, 0,0);
        gridPane.add(currentTickVal, 1, 0);
        gridPane.add(currentAlive, 0, 1);
        gridPane.add(currentAliveVal, 1, 1);
        gridPane.add(currentDead, 0, 2);
        gridPane.add(currentDeadVal, 1,2);
        gridPane.add(previousAlive, 0, 3);
        gridPane.add(previousAliveVal, 1, 3);
        gridPane.add(previousDead, 0, 4);
        gridPane.add(previousDeadVal, 1, 4);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
    }

    /**
     * Allows access to set the tick that is currently displayed to the user
     * @param val the number that should be displayed as the tick
     */
    public void setCurrentTickVal(int val){
        this.currentTickVal.setText(Integer.toString(val));
    }

    /**
     * Allows access to set the value for the number of alive cells that is currently displayed to the user
     * @param val the number that should be displayed as the number of alive cells
     */
    public void setCurrentAliveVal(int val){
        this.currentAliveVal.setText(Integer.toString(val));
    }

    /**
     * Allows access to set the value for the number of dead cells that is currently displayed to the user
     * @param val the number that should be displayed as the number of dead cells
     */
    public void setCurrentDeadVal(int val){
        this.currentDeadVal.setText(Integer.toString(val));
    }

    /**
     * Allows access to set the value for the number of alive cells in the previous tick that is currently displayed to the user
     * @param val the number that should be displayed as the number of alive cells in the previous tick
     */
    public void setPreviousAliveVal(int val){
        this.previousAliveVal.setText(Integer.toString(val));
    }

    /**
     * Allows access to set the value for the number of dead cells in the previous tick that is currently displayed to the user
     * @param val the number that should be displayed as the number of dead cells in the previous tick
     */
    public void setPreviousDeadVal(int val){
        this.previousDeadVal.setText(Integer.toString(val));
    }

    /**
     * Allows access to increment the tick counter by one
     */
    public void incrementTick(){
        currentTickVal.setText(Integer.toString(Integer.parseInt(currentTickVal.getText()) + 1));
    }

    /**
     * Allows access to increment the value for the number of alive cells by one
     */
    public void incrementAliveCell(){
        currentAliveVal.setText(Integer.toString(Integer.parseInt(currentAliveVal.getText()) + 1));
    }

    /**
     * Allows access to increment the value for the number of dead cells by one
     */
    public void incrementDeadCell(){
        currentDeadVal.setText(Integer.toString(Integer.parseInt(currentDeadVal.getText()) + 1));
    }

    /**
     * Allows access to decrement the value for the number of alive cells by one
     */
    public void decrementAliveCell(){
        currentAliveVal.setText(Integer.toString(Integer.parseInt(currentAliveVal.getText()) - 1));
    }

    /**
     * Allows access to decrement the value for the number of alive cells by one
     */
    public void decrementDeadCell(){
        currentDeadVal.setText(Integer.toString(Integer.parseInt(currentDeadVal.getText()) - 1));
    }

    /**
     * Returns the gridPane object that contains all of the statistics that will be displayed to the user
     * @return The populated GridPane
     */
    public GridPane getGridPane(){
        return gridPane;
    }

    /**
     * Resets all the statistics by setting all the values to 0.
     */
    public void resetStatistics(){
        this.currentTickVal.setText("0");
        this.currentAliveVal.setText("0");
        this.currentDeadVal.setText("0");
        this.previousAliveVal.setText("0");
        this.previousDeadVal.setText("0");
    }

}
