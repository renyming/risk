package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Country;

public class MapController {

    @FXML private Button skipReinforcementPhaseButton;
    @FXML private TextField numArmiesMoveTextField;
    @FXML private AnchorPane currentPlayerPane;
    @FXML private Label currentPlayerLabel;
    @FXML private Label numArmiesMoveLabel;
    @FXML private Label armiesInHandLabel;
    @FXML private Button nextPhaseButton;
    @FXML private Label invalidMoveLabel;
    @FXML private Label countryALabel;
    @FXML private Label countryBLabel;
    @FXML private Label countryAName;
    @FXML private Label countryBName;
    @FXML private AnchorPane mapPane;
    @FXML private Label phaseLabel;

    private View view;

    public void initialize(View view) {
        this.view = view;
        skipReinforcementPhaseButton.setVisible(false);
        numArmiesMoveTextField.setVisible(false);
        numArmiesMoveLabel.setVisible(false);
        invalidMoveLabel.setVisible(false);
        nextPhaseButton.setVisible(false);
        countryALabel.setVisible(false);
        countryBLabel.setVisible(false);
        countryAName.setVisible(false);
        countryBName.setVisible(false);
        addEventListener();
    }

    public void addEventListener() {
        mapPane.setOnMouseClicked((e) -> {
            if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                view.clickedMap();
            }
        });
    }

    public void enteredNumArmiesMoved() {
        int numArmiesMoved = 0;
        try {
            numArmiesMoved = Integer.parseInt(numArmiesMoveTextField.getText());
        } catch (Exception e) {
            showInvalidMoveLabelInfo(true, "Enter an positive integer");
            System.out.println("MapController.initialize(): input value not integer " + numArmiesMoveTextField.getText());
        }
        if (numArmiesMoved > 0) {
            showInvalidMoveLabelInfo(false, "");
            view.fortification(numArmiesMoved);
        } else {
            showInvalidMoveLabelInfo(true, "Enter an positive integer");
            System.out.println("MapController.initialize(): input integer not positive, " + numArmiesMoveTextField.getText());
        }
    }

    public void showInvalidMoveLabelInfo(boolean show, String invalidInfo) {
        numArmiesMoveTextField.clear(); // TODO: could be removed?
        invalidMoveLabel.setVisible(show);
        invalidMoveLabel.setText(invalidInfo);
    }

    public void showFromToCountriesInfoPane(boolean show) {
        countryALabel.setVisible(show);
        countryAName.setVisible(show);
        countryBLabel.setVisible(show);
        countryBName.setVisible(show);
        numArmiesMoveLabel.setVisible(show);
        numArmiesMoveTextField.setVisible(show);
    }

    public void resetFromToCountriesInfo() {
        countryAName.setText("NONE");
        countryAName.setStyle("-fx-border-color: red; -fx-border-width: 3");
        countryBName.setText("NONE");
        countryBName.setStyle("-fx-border-color: red; -fx-border-width: 3");
    }

    public void showNextPhaseButton(String nextPhase) {
        nextPhaseButton.setText(nextPhase);
        nextPhaseButton.setVisible(true);
        hidePhaseLabel();
    }

    public void setFromCountryInfo(Country country) {
        countryAName.setText(country.getName());
        countryAName.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
    }

    public void setToCountryInfo(Country country) {
        countryBName.setText(country.getName());
        countryBName.setStyle("-fx-border-color: #00ff00;  -fx-border-width: 3");
    }

    public void skipReinforcementPhase() {
        showPlayerViewPane(false);
        view.skipFortificationPhase();
    }

    public void backToMenu() { view.showMenuStage(); }

    public void setPhaseLabel(String phase) { phaseLabel.setText(phase); }

    public void hidePhaseLabel() { phaseLabel.setVisible(false); }

    public void showPhaseLabel() { phaseLabel.setVisible(true); }

    public void hideNextPhaseButton() { nextPhaseButton.setVisible(false); }

    public void showPlayerViewPane(boolean show) {
        if (show) {
            currentPlayerPane.setVisible(true);
        } else {
            skipReinforcementPhaseButton.setVisible(false);
            numArmiesMoveTextField.clear();
            numArmiesMoveTextField.setVisible(false);
        }
    }

    public void startNextPhase() { view.startNextPhase(); }

    public void showReinforcementPhaseButton(boolean show) { skipReinforcementPhaseButton.setVisible(show); }

    // give the reference to PlayerView for updating info
    public Label getCurrentPlayerLabel() { return currentPlayerLabel; }

    public Label getArmiesInHandLabel() { return armiesInHandLabel; }

    public String getNextPhaseButtonTest() { return nextPhaseButton.getText(); }






}
