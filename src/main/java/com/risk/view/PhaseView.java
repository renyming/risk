package com.risk.view;

import com.risk.controller.MapController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.risk.model.Phase;
import com.risk.model.Player;

import java.util.Observable;
import java.util.Observer;


/**
 * Observer Phase class, store
 * 1) the name of the game phase currently being played
 * 2) the current player's name
 * 3) information about actions that are taking place during this phase
 */
public class PhaseView implements Observer {

    private static PhaseView instance;

    // general map components
    private Label phaseLabel;
    private Button nextPhaseButton;
    private Label currentPlayerNameLabel;
    private Label currentPlayerTypeLabel;
    private Label armiesInHandLabel;
    private Label invalidMovedLabel;
    private Button saveGameButton;

    // From-To country relative components
    private Label countryALabel;
    private Label countryANameLabel;
    private Label countryBLabel;
    private Label countryBNameLabel;

    // attack phase relative components
    private Label attackerDiceLabel;
    private Button attackerDiceOneButton;
    private Button attackerDiceTwoButton;
    private Button attackerDiceThreeButton;
    private Label defenderDiceLabel;
    private Button defenderDiceOneButton;
    private Button defenderDiceTwoButton;
    private Label allOutLabel;
    private Button allOutEnableButton;
    private Button allOutDisableButton;
    private Button attackButton;

    // fortification relative components
    private Label numArmiesMovedLabel;
    private TextField numArmiesMovedTextField;
    private Button skipFortificationPhaseButton;

    private String currentPhase;
    private Player currentPlayer;
    private MapController mapController;


    /**
     * Ctor
     */
    private PhaseView() { currentPhase = "Init"; }


    /**
     * Singleton standard getter method, get the instance
     * @return the instance
     */
    public static PhaseView getInstance() {
        if (null == instance) instance = new PhaseView();
        return instance;
    }


    /**
     * Initialize the relative map component
     * @param phaseLabel displays the current phase
     * @param nextPhaseButton allows user the enter the next phase
     * @param currentPlayerNameLabel displays the current player name
     * @param currentPlayerTypeLabel displays the current player type
     * @param armiesInHandLabel displays the number of armies in current player's hand
     * @param countryALabel indicates the from-country
     * @param countryANameLabel displays the from-country name
     * @param countryBLabel indicates the to-country
     * @param countryBNameLabel displays the to-country name
     * @param numArmiesMovedLabel indicated the number of armies text field
     * @param numArmiesMovedTextField displays the num of armies text field
     * @param invalidMoveLabel displays the invalid move info
     * @param skipFortificationPhaseButton allows user to skip the fortification phase
     * @param mapController is the MapController reference
     */
    public void init(Label phaseLabel, Button nextPhaseButton, Label currentPlayerNameLabel, Label currentPlayerTypeLabel, Label armiesInHandLabel,
                     Label countryALabel, Label countryANameLabel, Label countryBLabel, Label countryBNameLabel,
                     Label numArmiesMovedLabel, TextField numArmiesMovedTextField, Label invalidMoveLabel,
                     Button skipFortificationPhaseButton, Button saveGameButton,
                     MapController mapController) { // TODO: refactor
        this.phaseLabel = phaseLabel;
        this.nextPhaseButton = nextPhaseButton;
        this.currentPlayerNameLabel = currentPlayerNameLabel;
        this.currentPlayerTypeLabel = currentPlayerTypeLabel;
        this.armiesInHandLabel = armiesInHandLabel;
        this.countryALabel = countryALabel;
        this.countryANameLabel = countryANameLabel;
        this.countryBLabel = countryBLabel;
        this.countryBNameLabel = countryBNameLabel;
        this.numArmiesMovedLabel = numArmiesMovedLabel;
        this.numArmiesMovedTextField = numArmiesMovedTextField;
        this.invalidMovedLabel = invalidMoveLabel;
        this.skipFortificationPhaseButton = skipFortificationPhaseButton;
        this.saveGameButton = saveGameButton;
        this.mapController = mapController;
    }


    /**
     * Initialize the relative attack phase map component
     * @param attackerDiceLabel indicates the attacker's dices
     * @param attackerDiceOneButton displays the attacker's 1 dice
     * @param attackerDiceTwoButton displays the attacker's 2 dices
     * @param attackerDiceThreeButton displays the attacker's 3 dices
     * @param defenderDiceLabel indicated the defender's dices
     * @param defenderDiceOneButton displays the defender's 1 dice
     * @param defenderDiceTwoButton displays the defender's 2 dice
     * @param allOutLabel indicates the all out mode
     * @param allOutEnableButton displays the all out mode is enable
     * @param allOutDisableButton display the all out mode is disable
     * @param attackButton allows user to perform attack action
     */
    public void initAttackComponents(Label attackerDiceLabel, Button attackerDiceOneButton, Button attackerDiceTwoButton, Button attackerDiceThreeButton,
                                     Label defenderDiceLabel, Button defenderDiceOneButton, Button defenderDiceTwoButton,
                                     Label allOutLabel, Button allOutEnableButton, Button allOutDisableButton, Button attackButton) {
        this.attackerDiceLabel = attackerDiceLabel;
        this.attackerDiceOneButton = attackerDiceOneButton;
        this.attackerDiceTwoButton = attackerDiceTwoButton;
        this.attackerDiceThreeButton = attackerDiceThreeButton;
        this.defenderDiceLabel = defenderDiceLabel;
        this.defenderDiceOneButton = defenderDiceOneButton;
        this.defenderDiceTwoButton = defenderDiceTwoButton;
        this.allOutLabel = allOutLabel;
        this.allOutEnableButton = allOutEnableButton;
        this.allOutDisableButton = allOutDisableButton;
        this.attackButton = attackButton;
    }


    /**
     * Observer standard update method
     * @param obs is the Observable subject, which ic the Phase
     * @param obj is the additional update info
     */
    @Override
    public void update(Observable obs, Object obj) {
        Phase phase = (Phase) obs;
        Player nextPlayer = phase.getCurrentPlayer();

        // check current Player update
        if (null == currentPlayer || currentPlayer.getId() != nextPlayer.getId()) {
            currentPlayer = phase.getCurrentPlayer();
            currentPlayerNameLabel.setText(currentPlayer.getName());
            currentPlayerNameLabel.setStyle("-fx-background-color: " + currentPlayer.getColor());
            currentPlayerTypeLabel.setText(currentPlayer.getStrategy().getName());
            armiesInHandLabel.setText(Long.toString(phase.getCurrentPlayer().getArmies()));
        }

        // check current Phase update, or current action update
        if (!currentPhase.equals(phase.getCurrentPhase())) {
            currentPhase = phase.getCurrentPhase();
            mapController.setCurrentPhase(currentPhase);
            phaseLabel.setText(phase.getCurrentPhase());
            phaseLabel.setVisible(true);
            switch (currentPhase) {
                case "Start Up Phase":
                    // set Start Up Phase UI
                    // useful when start another game within one application launch
                    hide();
                    reset();
                    // update current Player UI
                    armiesInHandLabel.setText(Long.toString(currentPlayer.getArmies()));
                    nextPhaseButton.setText("Click To Enter Reinforcement Phase");
                    break;
                case "Reinforcement Phase":
                    // set Reinforcement Phase UI
                    hide();
                    // update current Player UI
                    armiesInHandLabel.setText(Long.toString(currentPlayer.getArmies()));
                    nextPhaseButton.setText("Click To Enter Attack Phase");
                    break;
                case "Attack Phase":
                    // set Attack Phase UI
                    hide();
                    reset();
                    countryALabel.setVisible(true);
                    countryANameLabel.setVisible(true);
                    countryBLabel.setVisible(true);
                    countryBNameLabel.setVisible(true);
                    displayAttackPhaseMapComponent(true);
                    nextPhaseButton.setText("Click To Enter Fortification Phase");
                    break;
                case "Fortification Phase":
                    // set Fortification Phase UI
                    hide();
                    reset(); // TODO: may not needed
                    countryALabel.setVisible(true);
                    countryANameLabel.setVisible(true);
                    countryBLabel.setVisible(true);
                    countryBNameLabel.setVisible(true);
                    numArmiesMovedLabel.setVisible(true);
                    numArmiesMovedTextField.setVisible(true);
                    skipFortificationPhaseButton.setVisible(true);
                    nextPhaseButton.setText("Click To Enter Reinforcement Phase");
                    break;
            }
        } else {
            switch (phase.getActionResult()) {
                case Invalid_Card_Exchange:
                    // TODO: display the invalid result in card exchange View, Model may tell cardView directly
                    phase.clearActionResult();
                case Allocate_Army:
                    armiesInHandLabel.setText(Long.toString(currentPlayer.getArmies()));
                    phase.clearActionResult();
                    break;
                case Invalid_Move:
                    invalidMovedLabel.setText(phase.getInvalidInfo());
                    invalidMovedLabel.setStyle("-fx-border-color: red; -fx-border-width: 3");
                    invalidMovedLabel.setVisible(true);
                    phase.clearActionResult();
                    break;
                case Move_After_Conquer:
                    numArmiesMovedLabel.setVisible(true);
                    numArmiesMovedTextField.setVisible(true);
                    nextPhaseButton.setVisible(false);
                    displayAttackPhaseMapComponent(false);
                    phase.clearActionResult();
                    mapController.setCountryClick(false);
                    reset();
                    invalidMovedLabel.setText(phase.getInvalidInfo());
                    invalidMovedLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
                    invalidMovedLabel.setVisible(true);
                    break;
                case Show_Next_Phase_Button:
                    invalidMovedLabel.setVisible(false);
                    switch (currentPhase) {
                        case "Start Up Phase":
                            saveGameButton.setVisible(true);
                            break;
                        case "Attack Phase":
                            displayAttackPhaseMapComponent(true);
                            numArmiesMovedLabel.setVisible(false);
                            numArmiesMovedTextField.clear();
                            numArmiesMovedTextField.setVisible(false);
                            mapController.setCountryClick(true);
                            invalidMovedLabel.setText(phase.getInvalidInfo());
                            invalidMovedLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
                            invalidMovedLabel.setVisible(true);
                            break;
                        case "Fortification Phase":
                            mapController.disableFortification();
                            saveGameButton.setVisible(true);
                            break;
                    }
//                    phaseLabel.setVisible(false);
                    nextPhaseButton.setVisible(true);
                    phase.clearActionResult();
                    break;
                case Win:
                    phaseLabel.setVisible(false);
                    nextPhaseButton.setVisible(false);
                    hide();
                    invalidMovedLabel.setText(phase.getInvalidInfo());
                    invalidMovedLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
                    invalidMovedLabel.setVisible(true);
                    mapController.setWin();
                    break;
                case Attack_Impossible:
                    displayAttackPhaseMapComponent(false);
                    numArmiesMovedLabel.setVisible(false);
                    numArmiesMovedTextField.clear();
                    numArmiesMovedTextField.setVisible(false);
                    mapController.setCountryClick(false);
                    countryALabel.setVisible(false);
                    countryANameLabel.setVisible(false);
                    countryBLabel.setVisible(false);
                    countryBNameLabel.setVisible(false);
                    invalidMovedLabel.setText(phase.getInvalidInfo());
                    invalidMovedLabel.setStyle("-fx-border-color: red; -fx-border-width: 3");
                    invalidMovedLabel.setVisible(true);
                    nextPhaseButton.setVisible(true);
                    phase.clearActionResult();
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * Hide all map component for attack/fortification phase
     */
    public void hide() {
        countryALabel.setVisible(false);
        countryANameLabel.setVisible(false);
        countryBLabel.setVisible(false);
        countryBNameLabel.setVisible(false);
        attackerDiceLabel.setVisible(false);
        attackerDiceOneButton.setVisible(false);
        attackerDiceTwoButton.setVisible(false);
        attackerDiceThreeButton.setVisible(false);
        defenderDiceLabel.setVisible(false);
        defenderDiceOneButton.setVisible(false);
        defenderDiceTwoButton.setVisible(false);
        allOutLabel.setVisible(false);
        allOutEnableButton.setVisible(false);
        allOutDisableButton.setVisible(false);
        attackButton.setVisible(false);
        numArmiesMovedLabel.setVisible(false);
        numArmiesMovedTextField.setVisible(false);
        numArmiesMovedTextField.clear();
        invalidMovedLabel.setVisible(false);
        skipFortificationPhaseButton.setVisible(false);
    }


    /**
     * Display attack phase relative map component
     * @param display indicates whether the components should be show or hide
     */
    private void displayAttackPhaseMapComponent(boolean display) {
        // TODO: add a pane, just disable the pane
        attackerDiceLabel.setVisible(display);
        attackerDiceOneButton.setVisible(display);
        attackerDiceTwoButton.setVisible(display);
        attackerDiceThreeButton.setVisible(display);
        defenderDiceLabel.setVisible(display);
        defenderDiceOneButton.setVisible(display);
        defenderDiceTwoButton.setVisible(display);
        allOutLabel.setVisible(display);
        allOutEnableButton.setVisible(display);
        allOutDisableButton.setVisible(display);
        attackButton.setVisible(display);
    }


    /**
     * Reset from-to country Labels
     * Called when phase changes
     */
    private void reset() {
        countryANameLabel.setText("NONE");
        countryANameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        countryBNameLabel.setText("NONE");
        countryBNameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
    }
}
