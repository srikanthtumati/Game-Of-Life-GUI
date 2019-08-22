package edu.rpi.cs.csci4963.u19.tumats.hw02.gol_gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Main Class for the GUI. It contains all parts of the GUI and also houses 'central' methods that require a number of
 * individual classes.
 *
 * @author Srikanth Tumati
 * @version 1.0
 * @since 1.0
 */
public class GameOfLifeUI extends Application {

    /** The maximum number of ticks that the program can run */
    private int maxTicks;
    /** configHandler represents the instance of ConfigHandler that handles all operations related to the Configuration File */
    private ConfigHandler configHandler;
    /** gol is the GameOfLife object where all data related operations occur */
    private GameOfLife gol = new GameOfLife();
    /** borderPane is the main GUI component that connects the various parts of the GUI */
    private BorderPane borderPane;
    /** gridView represents the instance of the GridView which generates the grid viewable by the user for the lifetime of the program */
    private GridView gridView;
    /** userStatistics represents the instance of UserStatistics that is used to display data about the current tick to the user */
    private UserStatistics userStatistics;
    /** primaryStage is the Stage where the GUI is being displayed */
    private Stage primaryStage;
    /** menuBar is the Menu that is displayed to the user at the top of the GUI */
    private UserMenu menuBar;
    /** toolbar is the Toolbar that is displayed to the user at the bottom of the GUI */
    private UserToolbar toolbar;

    /**
     * assignDefaultVals is run to initialize the program and get the maximum number of ticks from the configuration file
     * (if it exists) and sets it to 50 otherwise.
     */
    public void assignDefaultVals(){
        String[] configData = ConfigHandler.readConfigFile();
        maxTicks = Integer.parseInt(configData[2]);
    }

    /**
     * Performs the necessary operations to 'reset' the program and prepare to load values from a data file
     * @param filename the name of the data file (determined by user)
     */
    public void loadFile(String filename){
        gol.resetData();
        userStatistics.resetStatistics();
        userStatistics.setCurrentAliveVal(0);
        ParseData.readFile(filename, gol);
        userStatistics.setCurrentDeadVal(gol.getNumCols() * gol.getNumRows());
        gridView = new GridView(gol, this, userStatistics);
        gridView.loadGrid();
        borderPane.setCenter(gridView.getGridView());
        resizeWindow();
    }

    /**
     * Updates the GUI to account for changes in the configuration file
     * @param dimensionChange true if the user changed the default row/column values and false otherwise
     */
    public void updateDefaultVals(boolean dimensionChange){
        this.maxTicks = ConfigHandler.maxTicks;
        if (dimensionChange) {
            resetData();
            userStatistics.setCurrentDeadVal(gol.getNumCols() * gol.getNumRows());
            resizeWindow();
        }


    }

    /**
     * Ensures that the window is properly sized and also sets a minimum so the user is unable to hide a part of the GUI
     */
    public void resizeWindow(){
        primaryStage.sizeToScene();
        primaryStage.setWidth(primaryStage.getWidth());
        primaryStage.setHeight(primaryStage.getHeight());
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }

    /**
     * Allows the program to reset and also account for any changes in the dimensions of the grid
     */
    public void resetData(){
        gol.resetData();
        gridView = new GridView(gol, this, userStatistics);
        gridView.generateGrid(ConfigHandler.defaultRows, ConfigHandler.defaultCols);
        borderPane.setCenter(gridView.getGridView());
        gol.populateBoard();
        userStatistics.resetStatistics();
        userStatistics.setCurrentAliveVal(0);
        userStatistics.setCurrentDeadVal(gol.getNumCols() * gol.getNumRows());
    }

    /**
     * Performs all necessary operations in order to update the current tick
     */
    public void updateGOLSTick(){
        if (gridView.isPopulated() && gol.getCurrentTick() < maxTicks){
//            if (gol.getCurrentTick() == 0)
//            gol.populateBoard();
            gol.updateTick();
            userStatistics.incrementTick();
            Pair<Integer, Integer> totalStates = gol.getTickSumStates(gol.getCurrentTick());
            userStatistics.setCurrentAliveVal(totalStates.getKey());
            userStatistics.setCurrentDeadVal(totalStates.getValue());
            if (gol.getCurrentTick() > 0){
                Pair<Integer, Integer> previousStates = gol.getTickSumStates(gol.getCurrentTick() - 1);
                userStatistics.setPreviousAliveVal(previousStates.getKey());
                userStatistics.setPreviousDeadVal(previousStates.getValue());
            }
            gridView.updateGrid();
        }
    }

    /**
     * Initializes all GUI components and prepares the GUI for the user
     */
    public void init(){
        userStatistics = new UserStatistics();
        configHandler = new ConfigHandler(this);
        assignDefaultVals();
        userStatistics.setCurrentDeadVal(ConfigHandler.defaultCols * ConfigHandler.defaultRows);
        menuBar = new UserMenu(gol, this, configHandler);
        gridView = new GridView(gol, this, userStatistics);
        gridView.generateGrid(ConfigHandler.defaultRows, ConfigHandler.defaultCols);
        gol.populateBoard();
        toolbar = new UserToolbar(gol, this, configHandler, userStatistics, gridView);
    }

    @Override
    /**
     * Connects the components of the GUI to the BorderPane and displays the GUI to the user
     */
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        borderPane = new BorderPane();
        primaryStage.setTitle("Conway's Game of Life Simulator");
        borderPane.setRight(userStatistics.getGridPane());
        borderPane.setTop(menuBar.getMenu());
        borderPane.setCenter(gridView.getGridView());
        borderPane.setBottom(toolbar.getToolBar());
        Scene scene = new Scene(borderPane);
        this.primaryStage.setScene(scene);
        this.primaryStage.sizeToScene();
        this.primaryStage.show();
        this.primaryStage.setMinWidth(this.primaryStage.getWidth());
        this.primaryStage.setMinHeight(this.primaryStage.getHeight());
    }

    /**
     * The main method to the program
     */
    public static void main(String[] args) {
        launch(args);
    }
}


