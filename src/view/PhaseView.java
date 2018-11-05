package view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Phase;
import model.Player;

import java.util.Observable;
import java.util.Observer;

public class PhaseView implements Observer {

    private Label phaseLabel;
    private Label currentPlayerLabel;
    private Label armiesInHandLabel;
    private Label countryALabel;
    private Label countryANameLabel;
    private Label countryBLabel;
    private Label countryBNameLabel;
    private Label numArmiesMovedLabel;
    private TextField numArmiesMovedTextField;
    private Label invalidMoveLabel;
    private Button skipFortificationPhaseButton;

    private String currentPhase;
    private Player currentPlayer;


    public PhaseView() {}

    public void init(Label phaseLabel, Label currentPlayerLabel, Label armiesInHandLabel,
                     Label countryALabel, Label countryANameLabel, Label countryBLabel, Label countryBNameLabel,
                     Label numArmiesMovedLabel, TextField numArmiesMovedTextField, Label invalidMoveLabel,
                     Button skipFortificationPhaseButton) {
        this.phaseLabel = phaseLabel;
        this.currentPlayerLabel = currentPlayerLabel;
        this.armiesInHandLabel = armiesInHandLabel;
        this.countryALabel = countryALabel;
        this.countryANameLabel = countryANameLabel;
        this.countryBLabel = countryBLabel;
        this.countryBNameLabel = countryBNameLabel;
        this.numArmiesMovedLabel = numArmiesMovedLabel;
        this.numArmiesMovedTextField = numArmiesMovedTextField;
        this.invalidMoveLabel = invalidMoveLabel;
        this.skipFortificationPhaseButton = skipFortificationPhaseButton;
    }

    @Override
    public void update(Observable obs, Object obj) {
        Phase phase = (Phase) obs;
        if (!currentPhase.equals(phase.getCurrentPhase())) {
            phaseLabel.setText(phase.getCurrentPhase());
            reset();
        }

        Player newPlayer = phase.getCurrentPlayer();
        if (null == currentPlayer || !currentPlayer.getName().equals(newPlayer.getName())) {
            currentPlayer = newPlayer;
            currentPlayerLabel.setText(currentPlayer.getName());
            currentPlayerLabel.setStyle("-fx-background-color: " + currentPlayer.getColor());
            armiesInHandLabel.setText(Integer.toString(currentPlayer.getArmies()));
        }

        switch (phase.getCurrentAction()) {
            case Allocate_Army:
                armiesInHandLabel.setText(Integer.toString(currentPlayer.getArmies()));
                break;
            case Exchange_Card:
                // TODO: update UI, allow user to exchange card
                break;
            case Select_Attack_Countries:
                // TODO: update UI, allow attacker and defender to choose two dices
                break;
            case Invalid_Attack:
                // TODO: display invalid info
                break;
            case Conquer_A_Country:
                // TODO: update UI, allow user place some armies to the conquered country
                // TODO: pass two Countries and user entered String
                break;
            case Select_Fortification_Countries:
                countryALabel.setVisible(true);
                countryANameLabel.setVisible(true);
                countryBLabel.setVisible(true);
                countryBNameLabel.setVisible(true);
                skipFortificationPhaseButton.setVisible(true);
                break;
            case Invalid_Fortification:
                invalidMoveLabel.setText(phase.getInvalidInfo());
                invalidMoveLabel.setVisible(true);
        }
    }

    private void reset() {
        countryALabel.setVisible(false);
        countryANameLabel.setVisible(false);
        countryANameLabel.setText("NONE");
        countryANameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        countryBLabel.setVisible(false);
        countryBNameLabel.setVisible(false);
        countryBNameLabel.setText("NONE");
        countryBNameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        numArmiesMovedLabel.setVisible(false);
        numArmiesMovedTextField.setVisible(false);
        numArmiesMovedTextField.clear();
        invalidMoveLabel.setVisible(false);
        skipFortificationPhaseButton.setVisible(false);
    }
}
