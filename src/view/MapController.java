package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Country;


/**
 * Handle event when user interact with the map, pass it to View
 */
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


    /**
     * Get View reference, add event listener
     * @param view the View reverence
     */
    public void initialize(View view) {
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
        this.view = view;
    }


    /**
     * Add event listener to the countryPane
     */
    private void addEventListener() {
        mapPane.setOnMouseClicked((e) -> { if (e.getEventType() == MouseEvent.MOUSE_CLICKED) { view.clickedMap(); } });
        // TODO: for draw arrow purpose
//        mapPane.setOnMousePressed((e) -> { if (e.getEventType() == MouseEvent.MOUSE_PRESSED) { view.pressedMap(e.getX(), e.getY()); } });
//        mapPane.setOnMouseDragged((e) -> { if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) { view.draggedMap(e.getX(), e.getY()); } });
//        mapPane.setOnMouseReleased((e) -> { if (e.getEventType() == MouseEvent.MOUSE_RELEASED) { view.releasedMap(e.getX(), e.getY()); } });
    }


    /**
     * Called when user entered number of armies moved value and press enter button, pass event to View
     */
    public void enteredNumArmiesMoved() { view.fortification(numArmiesMoveTextField.getText()); }


    /**
     * Display/Hide the invalid move label, update the invalid info
     * Called by View.*()
     * @param show decides whether the invalid move label need to be displayed
     * @param invalidInfo is the invalid move info
     */
    public void showInvalidMoveLabelInfo(boolean show, String invalidInfo) {
        numArmiesMoveTextField.clear(); // TODO: could be removed?
        invalidMoveLabel.setVisible(show);
        invalidMoveLabel.setText(invalidInfo);
    }


    /**
     * Display/Hide the from-to countries info pane between different phase
     * Called by View.*()
     * @param show decides whether the from-to countries info need to be displayed
     */
    public void showFromToCountriesInfoPane(boolean show) {
        countryALabel.setVisible(show);
        countryAName.setVisible(show);
        countryBLabel.setVisible(show);
        countryBName.setVisible(show);
        numArmiesMoveLabel.setVisible(show);
        numArmiesMoveTextField.setVisible(show);
    }


    /**
     * Reset from-to countries instruction labels and color
     * Called by View.*()
     */
    public void resetFromToCountriesInfo() {
        countryAName.setText("NONE");
        countryAName.setStyle("-fx-border-color: red; -fx-border-width: 3");
        countryBName.setText("NONE");
        countryBName.setStyle("-fx-border-color: red; -fx-border-width: 3");
    }


    /**
     * Display 'Entering xxx Phase' with new text based on the received parameter, hide phase label
     * Called by View.showNextPhaseButton()
     * @param nextPhase is the next phase name
     */
    public void showNextPhaseButton(String nextPhase) {
        nextPhaseButton.setText(nextPhase);
        nextPhaseButton.setVisible(true);
        hidePhaseLabel();
    }


    /**
     * Update from-country info pane
     * Called by View.clickedCountry()
     * @param country is the from-country
     */
    public void setFromCountryInfo(Country country) {
        countryAName.setText(country.getName());
        countryAName.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
    }


    /**
     * Update to-country info pane
     * Called by View.clickedCountry()
     * @param country is the to-country
     */
    public void setToCountryInfo(Country country) {
        countryBName.setText(country.getName());
        countryBName.setStyle("-fx-border-color: #00ff00;  -fx-border-width: 3");
    }


    /**
     * Called when user click the 'Skip' button during reinforcement phase
     * Pass the event to View
     */
    public void skipReinforcementPhase() {
        showPlayerViewPane(false);
        view.skipFortificationPhase();
    }


    /**
     * Called when user click the 'Quit' button during the game play
     */
    public void backToMenu() { view.showMenuStage(); }


    /**
     * Set the phaseLabel name based on the parameter
     * Called by View.*()
     * @param phase is the phase name
     */
    public void setPhaseLabel(String phase) { phaseLabel.setText(phase); }


    /**
     * Hide phase label
     * Called by View.startNextPhase()
     */
    public void hidePhaseLabel() { phaseLabel.setVisible(false); }


    /**
     * Show phase label
     * Called by View.startNextPhase()
     */
    public void showPhaseLabel() { phaseLabel.setVisible(true); }


    /**
     * Hide next phase button
     * Called by View.startNextPhase()
     */
    public void hideNextPhaseButton() { nextPhaseButton.setVisible(false); }


    /**
     * Show/Hide the player view pane based on the parameter
     * Clear num armies moved text field before hide it
     * Called by map controller
     * @param show decides whether the player view pane need to be shown
     */
    private void showPlayerViewPane(boolean show) {
        if (show) {
            currentPlayerPane.setVisible(true);
        } else {
            skipReinforcementPhaseButton.setVisible(false);
            numArmiesMoveTextField.clear();
            numArmiesMoveTextField.setVisible(false);
        }
    }


    /**
     * Start next phase
     * Called when user clicked the next phase button, pass the event to View
     */
    public void startNextPhase() { view.startNextPhase(); }


    /**
     * Show/Hide reinforcement phase button based on parameter
     * Called by View.startNextPhase()
     * @param show decides whether the reinforcement phase button need to be shown
     */
    public void showReinforcementPhaseButton(boolean show) { skipReinforcementPhaseButton.setVisible(show); }


    /**
     * Allow player view to update the current player label text
     * Called by PlayerView
     * @return the current player label reference
     */
    public Label getCurrentPlayerLabel() { return currentPlayerLabel; }


    /**
     * Allow player view to update the armies in hand label text
     * Called by PlayerView
     * @return the armies in hand label reference
     */
    public Label getArmiesInHandLabel() { return armiesInHandLabel; }


    /**
     * Allow player view to update the next phase button text
     * Called by PlayerView
     * @return the next phase button text
     */
    public String getNextPhaseButtonTest() { return nextPhaseButton.getText(); }
}
