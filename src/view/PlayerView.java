package view;

import javafx.scene.control.Label;
import model.Player;

import java.util.Observable;
import java.util.Observer;

public class PlayerView implements Observer {

    private View view;
    private MapController mapController;
    private Player currentPlayer;
    private String name;
    private String color;
    private int armiesInHands;

    private Label currentPlayerLabel;
    private Label armiesInHandLabel;

    public PlayerView(View view, MapController mapController) {
        this.view = view;
        this.mapController = mapController;
        currentPlayerLabel = mapController.getCurrentPlayerLabel();
        armiesInHandLabel = mapController.getArmiesInHandLabel();
    }

    @Override
    public void update(Observable obs, Object arg) {
//        System.out.println("PlayerView.update: ");
        if (null == currentPlayer) currentPlayer = (Player) obs;
        name = currentPlayer.getName();
        currentPlayerLabel.setText(name);
        armiesInHands = currentPlayer.getArmies();
        armiesInHandLabel.setText(Integer.toString(armiesInHands));
        color = currentPlayer.getColor();
        currentPlayerLabel.setStyle("-fx-background-color: " + color);
        if (0 == armiesInHands) view.nextPlayer();
    }

    public String getName() { return name; }

    public int getArmiesInHands() { return armiesInHands; }
}
