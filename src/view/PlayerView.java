package view;

import javafx.scene.control.Label;
import model.Player;

import java.util.Observable;
import java.util.Observer;

public class PlayerView implements Observer {

    private View view;
    private Player currentPlayer;
    private String name;
    private String color;
    private int armiesInHands;

    private Label currentPlayerLabel;
    private Label armiesInHandLabel;

    public PlayerView(View view, MapController mapController) {
        this.view = view;
        currentPlayerLabel = mapController.getCurrentPlayerLabel();
        armiesInHandLabel = mapController.getArmiesInHandLabel();
    }

    @Override
    public void update(Observable obs, Object arg) {
//        System.out.print("PlayerView.update: ");
        Player nextPlayer = (Player) obs;
//        if (nextPlayer != currentPlayer) System.out.print("newPlayer = " + nextPlayer.getName());
        currentPlayer = nextPlayer;
        name = currentPlayer.getName();
        currentPlayerLabel.setText(name);
        armiesInHands = currentPlayer.getArmies();
//        System.out.print(", armies = " + armiesInHands);
        armiesInHandLabel.setText(Integer.toString(armiesInHands));
        color = currentPlayer.getColor();
        currentPlayerLabel.setStyle("-fx-background-color: " + color);
        armiesInHandLabel.setStyle("-fx-background-color: " + color);
        if (0 == armiesInHands) view.prepareNextPhase();
        //        System.out.println("");
    }

    public String getName() { return name; }

    public int getArmiesInHands() { return armiesInHands; }
}
