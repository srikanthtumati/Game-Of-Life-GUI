package edu.rpi.cs.csci4963.u19.tumats.hw02.gol_gui;

import javafx.util.Pair;
import java.util.HashMap;

/**
 * Handles all data related operations for the program. Also stores all tick data for the entirety of the program.
 *
 * @author Srikanth Tumati
 * @version 1.1
 * @since 1.0
 */
public class GameOfLife {

    /** the instance of the GameOfLife that will be used in the program */
    private static GameOfLife instance = new GameOfLife();
    /** Stores tick data for every tick that is run by the program */
    private HashMap<Integer, Integer[][]> tickData = new HashMap<>();
    /** Stores state data for every tick that is run by the program */
    private HashMap<Integer, Pair> stateData = new HashMap<>();
    /** The number of rows that the board must have */
    private int boardRows;
    /** The number of columns that the board must have */
    private int boardCols;
    /** The current tick of the program */
    private int currentTick = 0;

    /**
     * Returns the board for the current game tick
     * @return A two dimensional array containing cell data for the current tick
     */
    public Integer[][] getCurrentBoard(){
        return tickData.get(currentTick);
    }

    /**
     * Returns the board for a determined game tick
     * @param i The game tick that we are gathering data for
     * @return A two dimensional array containing cell data for the determined tick
     */
    public Integer[][] getSpecificTickData(int i){
        return tickData.get(i);
    }

    /**
     * Populates the board when the values are initialized as null
     */
    public void populateBoard(){
        Integer[][] board = getCurrentBoard();
        if (board == null){
            board = new Integer[boardRows][boardCols];
            tickData.put(0, board);
        }
        for (int row = 0; row < board.length; row++ ){
            for (int col = 0; col < board[0].length; col++ ){
                if (board[row][col] == null)
                    board[row][col] = 0;
            }
        }
    }

    /**
     * Changes the state of an individual cell within the current board
     * @param row The row for the cell which is being changed
     * @param col The column for the cell which is being changed
     * @param state True if the cell is being changed to 1 and False of the cell is being changed to 0
     */
    public void setState(int row, int col, boolean state){
        Integer[][] board = getCurrentBoard();
        if (instance != null && row < board.length && col < board[0].length){
            if (state)
                board[row][col] = 1;
            else
                board[row][col] = 0;
        }
    }

    /**
     * Returns the String version of a specified board
     * @param printBoard the Board that will be converted into a String
     * @return The String equivalent of the board
     */
    public String boardToString(Integer[][] printBoard){
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < printBoard.length; row++){
            for(int column = 0; column < printBoard[0].length; column++){
                sb.append(printBoard[row][column]).append(" ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Set the current game tick as long as the given number is greater than 0
     * @param currentTick the number that the tick should be set to
     */
    public void setCurrentTick(int currentTick){
        if (currentTick >= 0)
            this.currentTick = currentTick;
    }

    /**
     * Returns the current number of rows in the board
     * @return The number of rows
     */
    public int getNumRows(){
        return boardRows;
    }

    /**
     * Returns the current number of columns in the board
     * @return The number of columns
     */
    public int getNumCols(){
        return boardCols;
    }

    /**
     * Returns the current game tick
     * @return The tick
     */
    public int getCurrentTick(){
        return currentTick;
    }

    /**
     * Resets all data structures and sets the tick to 0. This allows the GameOfLife to essentially 'restart'.
     */
    public void resetData(){
        tickData.clear();
        stateData.clear();
        currentTick = 0;
    }

    /**
     * Resets all data structures for all ticks (except tick 0).
     */
    public void resetFutureTickData(){
        int size = tickData.size();
        for (int i = 0; i < size; i++){
            if (i == 0)
                continue;
            tickData.remove(i);
        }
    }

    /**
     * Returns a HashMap containing all tick data with the tick being the key and the board data being the value
     * @return All tick data
     */
    public HashMap getTotalTickData(){
        return this.tickData;
    }

    /**
     * Adds up the number of cells that are currently alive and currently dead.
     * @param tick The specific game tick for which we are totaling the number of dead/alive cells
     * @return A Pair with the key as the number of cells that are alive and the value as the number of cells that are dead
     */
    public Pair<Integer, Integer> getTickSumStates(int tick){
        Integer[][] board = tickData.get(tick);
        int totalAliveCells = 0;
        int totalDeadCells = 0;
            for (int row = 0; row < board.length; row++){
                for(int column = 0; column < board[0].length; column++){
                    if (board[row][column] == 1)
                        totalAliveCells += 1;
                    else
                        totalDeadCells += 1;
                }
            }
        return new Pair<>(totalAliveCells, totalDeadCells);
    }

    /**
     * Provides the value of a certain cell in the current board
     * @param row The desired row of the cell
     * @param col The desired column of the cell
     * @return 1 if the cell is alive and 0 otherwise
     */
    public int getCellVal(int row, int col){
        Integer[][] board = getCurrentBoard();
        return board[row][col];
    }

    /**
     * Initializes the board by setting it to the proper dimensions
     * @param dimensions The number rows in the key and the number of columns in the value
     */
    public void initializeBoard(Pair<Integer, Integer> dimensions){
        boardRows = dimensions.getKey();
        boardCols = dimensions.getValue();
        tickData.put(0, new Integer[boardRows][boardCols]);
    }

    /**
     * Ensures that the cell in question is either within bounds of the board or not in bounds
     * @param row The row of the cell in question
     * @param column The column of the cell in question
     * @return true if the cell is within bounds and false otherwise
     */
    public boolean checkBounds(int row, int column){
        Integer[][] board = getCurrentBoard();
        return (board.length - 1 >= row && row >= 0 && column >= 0 && board[0].length - 1 >= column);
    }

    /**
     * Sets an entire row of the board with values that are given by rowData[].
     * @param rowData An Integer array containing values (of 1 or 0) for the cells of a certain row
     * @param row The desired row for which the data should be placed in
     */
    public void setRow(Integer[] rowData, int row){
        Integer[][] board = getCurrentBoard();
        for (int count = 0; count < rowData.length; count++){
            board[row][count] = rowData[count];
        }
    }

    /**
     * This method performs the appropriate action by checking the current status of the cell and the number of live
     * neighbors surrounding it. It uses the following rules to determine the state of the cell. The cell is kept alive
     * if it has 2 or 3 cells (1). Any cell with fewer than two neighbors or more than three neighbors dies (0). Any cell
     * that is currently dead and has 3 neighboring cells that are alive also becomes alive (1).
     * @param row The specific row of the desired cell in which we are updating
     * @param column The specific column of the desired cell in which we are updating
     * @param liveNeighbors The number of live neighbors that the cell currently has
     */
    public void updateCell(int row, int column, int liveNeighbors){
        Integer[][] board = this.getSpecificTickData(currentTick - 1);
        if (liveNeighbors == 3 || (liveNeighbors == 2 && board[row][column] == 1))
            getCurrentBoard()[row][column] = 1;
        else
            getCurrentBoard()[row][column] = 0;
    }

    /**
     * Performs all necessary operations needed to update the game tick. Stores updated board with cell data and state data.
     */
    public void updateTick(){
        if (tickData.containsKey(currentTick + 1)){
            currentTick += 1;
        }
        else{
            if (currentTick == 0){
                stateData.put(currentTick, getTickSumStates(currentTick));
            }
            int liveNeighbors;
            Integer[][] board = getCurrentBoard();
            currentTick += 1;
            tickData.put(currentTick, new Integer[boardRows][boardCols]);
            for (int row = 0; row < boardRows; row++){
                for(int column = 0; column < boardCols; column++){
                    liveNeighbors = checkNeighbors(row, column, board);
                    updateCell(row, column, liveNeighbors);
                }
            }
            stateData.put(currentTick, getTickSumStates(currentTick));
        }
    }

    /**
     * This method confirms that each cell in the board checks 8 neighboring cells and wraps around the board
     * when necessary.
     * @param row The specific row which is being checked
     * @param column The specific column which is being checked
     * @param board the two dimensional array that is being currently being checked
     * @return the number of neighbors to the source cell that are currently alive
     */
    public int checkNeighbors(int row, int column, Integer[][] board){
        int sum = 0;
        if (checkBounds(row, column + 1))
            sum += board[row][column + 1];
        else{
            sum += board[row][0];
        }

        if (checkBounds(row - 1, column))
            sum += board[row - 1][column];
        else{
            sum += board[board.length - 1][column];
        }

        if (checkBounds(row + 1, column))
            sum += board[row + 1][column];
        else{
            sum += board[0][column];
        }

        if (checkBounds(row, column - 1))
            sum += board[row][column - 1];
        else{
            sum += board[row][board[0].length - 1];
        }

        /** Top 4 are cardinal directions N S W E and should be all good */

        if (checkBounds(row + 1, column + 1))
            sum += board[row + 1][column + 1];
        else{
            /** First case is when point is on bottom row */
            if (board.length - 1 == row){
                /** Check if point is not the bottom right corner*/
                if (board[0].length - 1 > column)
                    sum += board[0][column + 1];
                /** Switch to 0,0 if bottom right corner point*/
                else
                    sum += board[0][0];
            }
            /** Second case is when point is on last column */
            else if (board[0].length - 1 == column){
                sum += board[row + 1][0];
            }
        }


        if (checkBounds(row - 1, column + 1))
            sum += board[row - 1][column + 1];
        else{
            /** First case is when point is on top row*/
            if (row == 0){
                /** Make sure we aren't dealing with top right point */
                if (board[0].length - 1 > column)
                    sum += board[board.length - 1][column + 1];
                else
                    sum += board[board.length - 1][0];
            }

            /** Second case is when we are dealing with last column */

            else if (board[0].length - 1 == column){
                sum += board[row - 1][0];
            }
        }

        /** Top left */
        if (checkBounds(row - 1, column - 1))
            sum += board[row - 1][column - 1];
        else{
            /** First case is when point is on top row*/
            if (row == 0){

                /** make sure its not the top left point */
                if (column != 0){
                    sum += board[board.length - 1][column - 1];
                }
                /** correct point for the top left point*/
                else
                    sum += board[board.length - 1][board[0].length - 1];

            }
            /** Second case is for first column */
            else if (column == 0)
                sum += board[row - 1][board[0].length - 1];

        }
        /** Bottom left */
        if (checkBounds(row + 1, column - 1))
            sum += board[row + 1][column - 1];
        else{
            /** First case is first column*/
            if (column == 0){
                /** confirm not bottom left point */
                if (row != board.length - 1){
                    sum += board[row + 1][board[0].length - 1];
                }
                else
                    sum += board[0][board[0].length - 1];
            }
            /** Second case is last row*/
            else if (board.length - 1 == row)
                sum += board[0][column - 1];

        }
        return sum;
    }

    /**
     * Writes the board data for the desired number of ticks
     * @param start The starting tick to print data for (inclusive)
     * @param end The ending tick to print data for (inclusive)
     * @param filePattern The file pattern for the output files
     * @param folderPattern The folder pattern for the folder where the output files are placed
     */
    public void writeData(int start, int end, String filePattern, String folderPattern){
        for (int count = start; count <= end; count++){
            WriteData.writeFile(this.boardToString(this.getSpecificTickData(count)), filePattern, folderPattern  , count);
        }
    }

}
