package view;

import controller.MapController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.NumPlayerMenu;

import java.util.Observable;
import java.util.Observer;

public class NumPlayerMenuView implements Observer {

    private Label numPlayerInstructionLabel;
    private Label validationOfUserEnteredLabel;
    private TextField numPlayerTextField;
    private Button startButton;

    private MapController mapController;

    public NumPlayerMenuView() {}

    public void init(Label playerNumInstructionLabel, Label userEnteredPlayNumLabel,
                     TextField playerNumTextField, Button startButton, MapController mapController) {
        this.numPlayerInstructionLabel = playerNumInstructionLabel;
        this.validationOfUserEnteredLabel = userEnteredPlayNumLabel;
        this.numPlayerTextField = playerNumTextField;
        this.startButton = startButton;
        this.mapController = mapController;
    }

    public void update(Observable obs, Object obj) {
        NumPlayerMenu numPlayerMenu = (NumPlayerMenu) obs;
        if (!numPlayerMenu.getVisible()) {
            reset();
        } else {
            numPlayerInstructionLabel.setText("Enter number of players, max " + numPlayerMenu.getMaxNumPlayer());
            mapController.setNumOfCountries(numPlayerMenu.getMaxNumPlayer());
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

    public void reset() {
        numPlayerInstructionLabel.setVisible(false);
        validationOfUserEnteredLabel.setVisible(false);
        validationOfUserEnteredLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        validationOfUserEnteredLabel.setText("Total Player: NONE");
        numPlayerTextField.setVisible(false);
        numPlayerTextField.clear();
        startButton.setVisible(false);
    }

    public void setTotalNumPlayer(String totalNumPlayer) {
        validationOfUserEnteredLabel.setText("Total Player: " + totalNumPlayer);
        validationOfUserEnteredLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
    }
}
