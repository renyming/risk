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

    private Label phaseLabel;
    private Label currentPlayerLabel;
    private Label armiesInHandLabel;
    private Label countryALabel;
    private Label countryANameLabel;
    private Label countryBLabel;
    private Label countryBNameLabel;
    private Label numArmiesMovedLabel;
    private TextField numArmiesMovedTextField;
    private Label invalidMovedLabel;
    private Button skipFortificationPhaseButton;

    private String currentPhase;
    private Player currentPlayer;
    private MapController mapController;


    private PhaseView() { currentPhase = "Init"; }

    public static PhaseView getInstance() {
        if (null == instance) instance = new PhaseView();
        return instance;
    }

    public void init(Label phaseLabel, Label currentPlayerLabel, Label armiesInHandLabel,
                     Label countryALabel, Label countryANameLabel, Label countryBLabel, Label countryBNameLabel,
                     Label numArmiesMovedLabel, TextField numArmiesMovedTextField, Label invalidMoveLabel,
                     Button skipFortificationPhaseButton,
                     MapController mapController) {
        this.phaseLabel = phaseLabel;
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

    @Override
    public void update(Observable obs, Object obj) {
        Phase phase = (Phase) obs;
        if (!currentPhase.equals(phase.getCurrentPhase())) {
            currentPhase = phase.getCurrentPhase();
            phaseLabel.setText(phase.getCurrentPhase());
            switch (phase.getCurrentPhase()) {
                case "Start Up Phase":
                    // set Start Up Phase UI
                    // useful when start another game within one application launch
                    hide();
                    reset();
                    // update current Player UI
                    currentPlayer = phase.getCurrentPlayer();
                    currentPlayerLabel.setText(currentPlayer.getName());
                    currentPlayerLabel.setStyle("-fx-background-color: " + currentPlayer.getColor());
                    armiesInHandLabel.setText(Integer.toString(currentPlayer.getArmies()));
                    break;
                case "Reinforcement Phase":
                    // set Reinforcement Phase UI
                    hide();
                    // update current Player UI
                    currentPlayer = phase.getCurrentPlayer();
                    currentPlayerLabel.setText(currentPlayer.getName());
                    currentPlayerLabel.setStyle("-fx-background-color: " + currentPlayer.getColor());
                    armiesInHandLabel.setText(Integer.toString(currentPlayer.getArmies()));
                    break;
                case "Attack Phase":
                    // set Attack Phase UI
                    reset();
                    // TODO: show From-To relative components
                    break;
                case "Fortification Phase":
                     reset();
                    // TODO: hide dice relative components
                    numArmiesMovedLabel.setVisible(true);
                    numArmiesMovedTextField.setVisible(true);
                    skipFortificationPhaseButton.setVisible(true);
                    break;
            }
        }

        switch (phase.getActionResult()) {
            case Finish_Current_Phase:
                mapController.setNextPhase(currentPhase);
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
            default:
                break;
        }
    }

    private void hide() {
        countryALabel.setVisible(false);
        countryANameLabel.setVisible(false);
        countryBLabel.setVisible(false);
        countryBNameLabel.setVisible(false);
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
