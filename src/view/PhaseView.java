package view;

import controller.MapController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Phase;
import model.Player;

import java.util.Observable;
import java.util.Observer;

public class PhaseView implements Observer {

    private static PhaseView instance;

    // general map components
    private Label phaseLabel;
    private Button nextPhaseButton;
    private Label currentPlayerLabel;
    private Label armiesInHandLabel;
    private Label invalidMovedLabel;

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


    private PhaseView() { currentPhase = "Init"; }

    public static PhaseView getInstance() {
        if (null == instance) instance = new PhaseView();
        return instance;
    }

    public void init(Label phaseLabel, Button nextPhaseButton, Label currentPlayerLabel, Label armiesInHandLabel,
                     Label countryALabel, Label countryANameLabel, Label countryBLabel, Label countryBNameLabel,
                     Label numArmiesMovedLabel, TextField numArmiesMovedTextField, Label invalidMoveLabel,
                     Button skipFortificationPhaseButton,
                     MapController mapController) { // TODO: refactor
        this.phaseLabel = phaseLabel;
        this.nextPhaseButton = nextPhaseButton;
        this.currentPlayerLabel = currentPlayerLabel;
        this.armiesInHandLabel = armiesInHandLabel;
        this.countryALabel = countryALabel;
        this.countryANameLabel = countryANameLabel;
        this.countryBLabel = countryBLabel;
        this.countryBNameLabel = countryBNameLabel;
        this.numArmiesMovedLabel = numArmiesMovedLabel;
        this.numArmiesMovedTextField = numArmiesMovedTextField;
        this.invalidMovedLabel = invalidMoveLabel;
        this.skipFortificationPhaseButton = skipFortificationPhaseButton;
        this.mapController = mapController;
    }

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

    @Override
    public void update(Observable obs, Object obj) {
        Phase phase = (Phase) obs;
        Player nextPlayer = phase.getCurrentPlayer();
        if (null == currentPlayer || currentPlayer.getId() != nextPlayer.getId()) {
            currentPlayer = phase.getCurrentPlayer();
            currentPlayerLabel.setText(currentPlayer.getName());
            currentPlayerLabel.setStyle("-fx-background-color: " + currentPlayer.getColor());
        }

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
                    armiesInHandLabel.setText(Integer.toString(currentPlayer.getArmies()));
                    nextPhaseButton.setText("Enter Reinforcement Phase");
                    break;
                case "Reinforcement Phase":
                    // set Reinforcement Phase UI
                    hide();
                    // update current Player UI
                    armiesInHandLabel.setText(Integer.toString(currentPlayer.getArmies()));
                    nextPhaseButton.setText("Enter Attack Phase");
                    break;
                case "Attack Phase":
                    // set Attack Phase UI
                    hide();
                    reset();
                    countryALabel.setVisible(true);
                    countryANameLabel.setVisible(true);
                    countryBLabel.setVisible(true);
                    countryBNameLabel.setVisible(true);
                    attackerDiceLabel.setVisible(true);
                    attackerDiceOneButton.setVisible(true);
                    attackerDiceTwoButton.setVisible(true);
                    attackerDiceThreeButton.setVisible(true);
                    defenderDiceLabel.setVisible(true);
                    defenderDiceOneButton.setVisible(true);
                    defenderDiceTwoButton.setVisible(true);
                    allOutLabel.setVisible(true);
                    allOutEnableButton.setVisible(true);
                    allOutDisableButton.setVisible(true);
                    attackButton.setVisible(true);
                    nextPhaseButton.setText("Enter Fortification Phase");
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
                    nextPhaseButton.setText("Enter Reinforcement Phase");
                    break;
            }
        }
        switch (phase.getActionResult()) {
            case Finish_Current_Phase: // TODO: useful?
                break;
            case Invalid_Card_Exchange:
                // TODO: display the invalid result in card exchange View, Model may tell cardView directly
            case Allocate_Army:
                armiesInHandLabel.setText(Integer.toString(currentPlayer.getArmies()));
                break;
            case Invalid_Move:
                invalidMovedLabel.setText(phase.getInvalidInfo());
                invalidMovedLabel.setVisible(true);
                break;
            case Show_Next_Phase_Button:
                phaseLabel.setVisible(false);
                nextPhaseButton.setVisible(true);
                if (currentPhase.equals("Fortification Phase")) {
                    mapController.disableFortification();
                }
            default:
                break;
        }
    }

    private void hide() {
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

    private void reset() {
        countryANameLabel.setText("NONE");
        countryANameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        countryBNameLabel.setText("NONE");
        countryBNameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
    }
}
