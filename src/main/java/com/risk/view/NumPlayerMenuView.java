package com.risk.view;

import com.risk.controller.MapController;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.risk.model.NumPlayerMenu;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;


/**
 * Display the player number relative menu components
 */
public class NumPlayerMenuView implements Observer {

    private Label numPlayerInstructionLabel;
    private Label validationOfUserEnteredLabel;
    private TextField numPlayerTextField;
    private Button startButton;
    private Button startButton1;
    private HashMap<Integer, Label> playerNumLabels;
    private HashMap<Integer, ChoiceBox<String>> playerTypeChoiceBoxes;
    private String totalPlayer;
    private MapController mapController;
    private boolean tournamentMode;


    /**
     * Default ctor
     */
    public NumPlayerMenuView() {}


    /**
     * Initialize the number of player relative menu components
     * @param playerNumInstructionLabel displays the max allow number of player
     * @param userEnteredPlayNumLabel displays the valid entered number, or invalid result
     * @param playerNumTextField allows user to enter the number of player
     * @param startButton allows user to enter the game
     * @param startButton1 start tournament game
     * @param mapController allow map to be shown
     * @param playerNumLabels displays each Player's name
     * @param playerTypeChoiceBoxes allows user to select each Player's type
     */
    public void init(Label playerNumInstructionLabel, Label userEnteredPlayNumLabel, TextField playerNumTextField,
                     Button startButton, Button startButton1, MapController mapController,
                     HashMap<Integer, Label> playerNumLabels, HashMap<Integer, ChoiceBox<String>> playerTypeChoiceBoxes) {
        this.numPlayerInstructionLabel = playerNumInstructionLabel;
        this.validationOfUserEnteredLabel = userEnteredPlayNumLabel;
        this.numPlayerTextField = playerNumTextField;
        this.startButton = startButton;
        this.startButton1 = startButton1;
        this.mapController = mapController;
        this.playerNumLabels = playerNumLabels;
        this.playerTypeChoiceBoxes = playerTypeChoiceBoxes;
    }


    /**
     * Standard Observer update
     * @param obs is the Observable subject, which is NumPlayerMenu
     * @param obj is the additional update info
     */
    @Override
    public void update(Observable obs, Object obj) {
        NumPlayerMenu numPlayerMenu = (NumPlayerMenu) obs;
        if (!numPlayerMenu.getVisible()) {
            reset();
        } else {
            int totalCountries = numPlayerMenu.getMaxNumPlayer();
            int max = tournamentMode ? 4 : 6;
            int maxNumPlayer = totalCountries > max ? max : totalCountries;
            mapController.setNumOfCountries(totalCountries);
            numPlayerInstructionLabel.setText("Enter number of players, max " + maxNumPlayer);
            numPlayerInstructionLabel.setVisible(true);
            validationOfUserEnteredLabel.setVisible(true);
            numPlayerTextField.clear();
            numPlayerTextField.setDisable(false);
        }
        if (numPlayerMenu.getValid()) {
            validationOfUserEnteredLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
            startButton.setStyle("-fx-border-color: #4CAF50;");
            startButton1.setStyle("-fx-border-color: #4CAF50;");
            showPlayerTypeInfo(Integer.parseInt(totalPlayer), true);
            startButton.setDisable(false);
            startButton1.setDisable(false);
        } else {
            validationOfUserEnteredLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
            validationOfUserEnteredLabel.setText(numPlayerMenu.getValidationInfo());
            showPlayerTypeInfo(6, false);
            startButton.setDisable(true);
            startButton1.setDisable(true);
        }
    }


    /**
     * Reset all relative number of player menu components
     */
    public void reset() {
        //numPlayerInstructionLabel.setVisible(false);
        //validationOfUserEnteredLabel.setVisible(false);
        validationOfUserEnteredLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        validationOfUserEnteredLabel.setText("Total Player: NONE");
        numPlayerTextField.setDisable(true);
        numPlayerTextField.clear();
        startButton.setDisable(true);
        startButton1.setDisable(true);
        startButton.setStyle("");
        startButton1.setStyle("");
        showPlayerTypeInfo(6, false);
    }


    /**
     * Set the user entered total player number
     * @param totalNumPlayer is the total number of player
     */
    public void setTotalNumPlayer(String totalNumPlayer) {
        this.totalPlayer = totalNumPlayer;
        validationOfUserEnteredLabel.setText("Total Player: " + totalNumPlayer);
        validationOfUserEnteredLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
    }


    /**
     * Show/hide Player type info
     * @param x is the number of Players
     * @param show is true if the info need to be displayed, false otherwise
     */
    private void showPlayerTypeInfo(int x, boolean show) {
        for (int i = 0; i < 6; ++i) {
            if (i < x) {
                playerNumLabels.get(i).setVisible(show);
                playerTypeChoiceBoxes.get(i).setVisible(show);
            } else {
                playerNumLabels.get(i).setVisible(!show);
                playerTypeChoiceBoxes.get(i).setVisible(!show);
            }
        }
    }


    /**
     * Set tournament mode
     * @param tournamentMode determine weather it's tournament of not
     */
    public void setTournament(boolean tournamentMode) {
        this.tournamentMode = tournamentMode;
        numPlayerInstructionLabel.setText("Enter number of players, max " + 4);
    }


    /**
     * Get total number of Players
     * @return the number of total Players
     */
    public int getTotalNumPlayer() { return Integer.parseInt(totalPlayer); }
}
