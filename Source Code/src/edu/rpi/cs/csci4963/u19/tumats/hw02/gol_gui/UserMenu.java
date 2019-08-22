package edu.rpi.cs.csci4963.u19.tumats.hw02.gol_gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Creates the menu for the GUI and also handles all dialog boxes that can be produced from the menu. Additionally, it
 * performs various checks to ensure that the user input is valid and also attempts to reject incorrect input
 * (such as characters or negative numbers for the row/column size)
 *
 * @author Srikanth Tumati
 * @version 1.0
 * @since 1.0
 */
public class UserMenu extends MenuBar{

    /** configHandler represents the instance of ConfigHandler that handles all operations related to the Configuration File */
    private ConfigHandler configHandler;
    /** gol is the GameOfLife object where all data related operations occur */
    private GameOfLife gol;
    /** golui is an instance of the GameOfLifeUI and allows the UserMenu to call various 'central methods' */
    private GameOfLifeUI golui;
    /** menuBar is an instance of UserMenu */
    private MenuBar menuBar;

    /**
     * Constructor for the UserMenu class
     * @param gol The GameOfLife object that is currently being used in the program
     * @param golui The GameOfLifeUI object that is currently being used in the program
     * @param configHandler The ConfigHandler object that is currently being used in the program
     */
    public UserMenu(GameOfLife gol, GameOfLifeUI golui, ConfigHandler configHandler) {
        this.gol = gol;
        this.golui = golui;
        this.configHandler = configHandler;
        menuBar = new MenuBar();
        Menu menuTools = generateMenuTools();
        Menu menuFile = generateMenuFiles();
        menuBar.getMenus().add(menuFile);
        menuBar.getMenus().add(menuTools);
    }

    /**
     * Returns the UserMenu after being generated and properly formatted
     * @return The created UserMenu to be used in the GUI
     */
    public MenuBar getMenu(){
        return this.menuBar;
    }


    /**
     * Generates the 'Tools' portion of the menu and all connected submenus with proper event handlers.
     * @return The 'Tools' part of the menu
     */
    private Menu generateMenuTools() {
        Menu menuTools = new Menu("Tools");
        MenuItem menuReset = new MenuItem("Reset");
        MenuItem menuConfig = new MenuItem("Configuration Panel");
        menuReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Are you sure you want to reset ALL tick data?");
                alert.setTitle("Reset Tick Data");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    golui.resetData();
                }
            }
        });
        menuConfig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                configHandler.generateConfigDialog();
            }
        });
        menuTools.getItems().addAll(menuReset, menuConfig);
        return menuTools;
    }

    /**
     * Generates the 'Files' portion of the menu and all connected submenus with proper event handlers.
     * @return The 'Files' part of the menu
     */
    private Menu generateMenuFiles() {
        Menu menuFile = new Menu("File");
        MenuItem menuLoadFile = new MenuItem("Load File");
        Menu menuSaveFile = new Menu("Save File");
        MenuItem menuSaveAll = new MenuItem("Save All Ticks");
        MenuItem menuSaveRange = new MenuItem("Save a Range of Ticks");
        menuLoadFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open data file");
                File userFile = fileChooser.showOpenDialog(null);
                if (userFile != null){
                    if (gol.getCurrentTick() != 0){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Data detected for current run!");
                        alert.setHeaderText("Are you sure you want to reset ALL current tick data?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() != ButtonType.OK){
                            return;
                        }
                    }
                    golui.loadFile(userFile.getName());
                }
            }
        });

        menuSaveAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save Tick Data");
                alert.setHeaderText("Are you sure you want to save ALL tick data?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    gol.writeData(0, gol.getTotalTickData().size() - 1, ConfigHandler.filePattern, ConfigHandler.folderPattern);
                }
            }
        });
        menuSaveRange.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Pair<Integer, Integer> userInputRange = generateUserTickRangeDialog();
                if (userInputRange != null){
                    for (int count = userInputRange.getKey(); count < userInputRange.getValue() + 1; count++){
                        WriteData.writeFile(gol.boardToString(gol.getSpecificTickData(count)), ConfigHandler.filePattern, ConfigHandler.folderPattern  , count);
                    }
                }
            }
        });
        menuSaveFile.getItems().addAll(menuSaveAll, menuSaveRange);
        menuFile.getItems().addAll(menuLoadFile, menuSaveFile);
        return menuFile;
    }


    /**
     * Generates the dialog box for the user to input the desired range of ticks for output and collects the output
     * @return The userinput in a Pair object with the starting tick (inclusive) in the key and the ending tick (inclusive) in the value
     */
    public Pair generateUserTickRangeDialog(){
        GridPane gridPane = new GridPane();
        ArrayList<Integer> tickChoices = new ArrayList<>();
        for (int count = 0; count <= gol.getCurrentTick(); count++ ){
            tickChoices.add(count);
        }
        ChoiceBox<Integer> startingTickRange = new ChoiceBox<>(FXCollections.observableArrayList(tickChoices));
        ChoiceBox<Integer> endingTickRange = new ChoiceBox<>(FXCollections.observableArrayList(tickChoices));
        startingTickRange.getSelectionModel().selectFirst();
        endingTickRange.getSelectionModel().selectFirst();
        Label startingTick = new Label("Starting tick: ");
        Label endingTick = new Label("Ending tick: ");
        gridPane.add(startingTick, 0, 0);
        gridPane.add(startingTickRange, 1, 0);
        gridPane.add(endingTick, 0, 1);
        gridPane.add(endingTickRange, 1 ,1);
        Dialog<Pair> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setTitle("Tick Range");
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(userButton -> {
            if (userButton == ButtonType.OK){
                Pair<Integer, Integer> temp = new Pair (startingTickRange.getValue(), endingTickRange.getValue());
                if (temp.getValue() < temp.getKey()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Range!");
                    alert.setContentText("Starting Tick must be less than or equal to Ending Tick!");
                    alert.showAndWait();
                }
                else
                    return temp;
            }
            return null;
        });
        Optional<Pair> userInput = dialog.showAndWait();
        if (userInput.isPresent())
            return userInput.get();
        return null;
    }

}

