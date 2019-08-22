package edu.rpi.cs.csci4963.u19.tumats.hw02.gol_gui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import java.util.HashMap;

/**
 * Handles all operations relating to the Grid that is visible to the user. Additionally, event handlers are added
 * to the grid to make it responsive.
 *
 * @author Srikanth Tumati
 * @version 1.0
 * @since 1.0
 */
public class GridView extends GridPane {

    /** gridPane is the GridPane where all cells are added */
    private GridPane gridPane = new GridPane();
    /** gol is the GameOfLife object where all data related operations occur */
    private GameOfLife gol;
    /** golui is an instance of the GameOfLifeUI and allows the UserMenu to call various 'central methods' */
    private GameOfLifeUI golui;
    /** userStatistics represents the instance of UserStatistics that is used to display data about the current tick to the user */
    private UserStatistics userStatistics;
    /** gridData is a HashMap that stores all Rectangles that are present in the grid */
    private HashMap<Pair<Integer, Integer>, Rectangle> gridData = new HashMap<>();

    /** Default color of a dead cell rgb(255, 255, 255) */
    private static Color defaultDeadColor = Color.rgb(255, 255, 255);
    /** Default color of an alive cell rgb(211, 211, 211) */
    private static Color defaultAliveColor = Color.rgb(211, 211, 211);

    /**
     * Constructor for the GridView class
     * @param gol The GameOfLife object that is currently being used in the program
     * @param golui The GameOfLifeUI object that is currently being used in the program
     * @param userStatistics the UserStatistics object that is currently being used in the program
     */
    public GridView(GameOfLife gol, GameOfLifeUI golui, UserStatistics userStatistics){
        this.gol = gol;
        this.golui = golui;
        this.userStatistics = userStatistics;
    }

    /**
     * Retrieves the GridPane (does not have to be populated)
     * @return The GridPane to be displayed in the GUI
     */
    public GridPane getGridView(){
        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    /**
     * Actions that run by the Rectangles on the grid when clicked on by the user
     * @param rectangle The rectangle that is currently being clicked on
     * @return the EventHandler processing the interaction
     */
    public EventHandler updateRectangle(Rectangle rectangle){
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (gol.getCurrentTick() != 0 && gol.getTotalTickData().keySet().size() > 1)
                    return;
                if (gol.getTotalTickData().keySet().size() > 1 && gol.getCurrentTick() == 0)
                    gol.resetFutureTickData();
                Node source = (Node)mouseEvent.getSource();
                int row = GridPane.getRowIndex(source);
                int col = GridPane.getColumnIndex(source);
                if (rectangle.getFill().equals(defaultDeadColor)){
                    rectangle.setFill(defaultAliveColor);
                    userStatistics.incrementAliveCell();
                    userStatistics.decrementDeadCell();
                    gol.setState(row, col, true);
                }
                else{
                    rectangle.setFill(defaultDeadColor);
                    userStatistics.decrementAliveCell();
                    userStatistics.incrementDeadCell();
                    gol.setState(row, col, false);
                }
            }
        };
        return eventHandler;
    }

    /**
     * Generates the grid when given row and column values
     * @param defaultRows The number of rows that should be placed in the grid
     * @param defaultCols The number of columns that should be placed in the grid
     */
    public void generateGrid(int defaultRows, int defaultCols){
        Pair<Integer, Integer> dimensions = new Pair<>(defaultRows, defaultCols);
        gol.initializeBoard(dimensions);
        for (int row = 0; row < defaultRows; row++ ){
            for (int col = 0; col < defaultCols; col++ ){
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(30);
                rectangle.setHeight(30);
                rectangle.setFill(defaultDeadColor);
                rectangle.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, updateRectangle(rectangle));
                gridPane.add(rectangle, col, row);
                gridData.put(new Pair<>(row, col), rectangle);
            }
        }
        gridPane.setGridLinesVisible(true);
    }

    /**
     * Creates the grid based on the loaded data file. Also adds EventHandlers to all Rectangles being added.
     */
    public void loadGrid(){
        int numRows = gol.getNumRows();
        int numCols = gol.getNumCols();
        for (int row = 0; row < numRows; row++ ){
            for (int col = 0; col < numCols; col++ ){
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(30);
                rectangle.setHeight(30);
                if (gol.getCellVal(row, col) == 0)
                    rectangle.setFill(defaultDeadColor);
                else
                    rectangle.setFill(defaultAliveColor);
                rectangle.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, updateRectangle(rectangle));
                gridPane.add(rectangle, col, row);
                gridData.put(new Pair<>(row, col), rectangle);
            }
        }
        gridPane.setGridLinesVisible(true);
    }

    /**
     * Updates the cells in the grid being displayed to the user by either changing the state of the cell (alive or dead)
     * or by changing the shade of the cell based on the lifespan of the cell
     */
    public void updateGrid(){
        for (int row = 0; row < gol.getNumRows(); row++ ){
            for (int col = 0; col < gol.getNumCols(); col++ ){
                if (gol.getCellVal(row, col ) == 1){
                    Rectangle temp = gridData.get(new Pair(row, col));
                    Color shade = calculateShade(row, col);
                    temp.setFill(shade);
                }
                else{
                    Rectangle temp = gridData.get(new Pair(row, col));
                    temp.setFill(defaultDeadColor);
                }

            }
        }
        if (gol.getCurrentTick() > 0){
            for(Pair pair: gridData.keySet()){
                Tooltip tooltip = new Tooltip("State locked after initial tick");
                Tooltip.install(gridData.get(pair), tooltip);
            }
        }
        else{
            for(Pair pair: gridData.keySet()){
                Tooltip tooltip = new Tooltip("State locked after initial tick");
                Tooltip.uninstall(gridData.get(pair), tooltip);
            }
        }
    }

    /**
     * Determines the shade of the cell based on its lifespan
     * @param row The row of the cell that is being considered
     * @param col The column of the cell that is being considered
     * @return the Color of the cell after calculating its lifespan
     */
    private Color calculateShade(int row, int col){
        int defaultRed =  (int) Math.round(defaultAliveColor.getRed() * 255);
        int defaultGreen = (int) Math.round(defaultAliveColor.getGreen() * 255);
        int defaultBlue = (int) Math.round(defaultAliveColor.getBlue() * 255);
        int shadeDarken = 30;
        int sum = 0;
        HashMap<Integer, Integer[][]> tickData = gol.getTotalTickData();
        for (int i = 0; i < gol.getCurrentTick(); i++){
            Integer[][] tickBoard = tickData.get(i);
            if (tickBoard[row][col] == 1)
                sum += 1;
            else
                sum = 0;
        }

        if (sum == 0)
            return defaultAliveColor;
        else{
            int fullDarken = sum * shadeDarken;
            return Color.rgb(Math.max(0, defaultRed - fullDarken), Math.max(0, defaultGreen - fullDarken), Math.max(0, defaultBlue - fullDarken));
        }
    }

    /**
     * Determines whether the grid is populated or not
     * @return True if the grid is currently populated and False otherwise
     */
    public boolean isPopulated(){
        return gridData.size() > 0;
    }

}
