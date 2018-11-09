package com.risk.view;

import com.risk.controller.MapController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.risk.model.NumPlayerMenu;

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

    private MapController mapController;


    /**
     * Default ctor
     */
    public NumPlayerMenuView() {}


    /**
     * Initialize the number of player relative menu components
     * @param playerNumInstructionLabel displays the max allow number of player
     * @param userEnteredPlayNumLabel displays the valid entered number, or invalid result
     * @param playerNumTextField allows user to enter the number of playey
     * @param startButton allows user to enter the game
     * @param mapController allow map to be shown
     */
    public void init(Label playerNumInstructionLabel, Label userEnteredPlayNumLabel,
                     TextField playerNumTextField, Button startButton, MapController mapController) {
        this.numPlayerInstructionLabel = playerNumInstructionLabel;
        this.validationOfUserEnteredLabel = userEnteredPlayNumLabel;
        this.numPlayerTextField = playerNumTextField;
        this.startButton = startButton;
        this.mapController = mapController;
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
            int maxNumPlayer = totalCountries > 6 ? 6 : totalCountries;
            mapController.setNumOfCountries(totalCountries);
            numPlayerInstructionLabel.setText("Enter number of players, max " + maxNumPlayer);
            numPlayerInstructionLabel.setVisible(true);
            validationOfUserEnteredLabel.setVisible(true);
            numPlayerTextField.clear();
            numPlayerTextField.setVisible(true);
        }
        if (!numPlayerMenu.getValid()) {
            validationOfUserEnteredLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
            validationOfUserEnteredLabel.setText(numPlayerMenu.getValidationInfo());
            startButton.setVisible(false);
        } else {
            validationOfUserEnteredLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
            startButton.setVisible(true);
        }
    }


    /**
     * Reset all relative number of player menu components
     */
    public void reset() {
        numPlayerInstructionLabel.setVisible(false);
        validationOfUserEnteredLabel.setVisible(false);
        validationOfUserEnteredLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        validationOfUserEnteredLabel.setText("Total Player: NONE");
        numPlayerTextField.setVisible(false);
        numPlayerTextField.clear();
        startButton.setVisible(false);
    }


    /**
     * Set the user entered total player number
     * @param totalNumPlayer is the total number of player
     */
    public void setTotalNumPlayer(String totalNumPlayer) {
        validationOfUserEnteredLabel.setText("Total Player: " + totalNumPlayer);
        validationOfUserEnteredLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
    }
}
