package view;

import controller.MapController;
import javafx.scene.control.Label;
import model.Player;

import java.util.Observable;
import java.util.Observer;


/**
 * Responsible for visualizing the current player info
 * Corresponding Observable subject is Player
 */
public class PlayerView implements Observer {

    private View view;
    private String name;
    private int armiesInHands;

    private Label currentPlayerLabel;
    private Label armiesInHandLabel;


    /**
     * Create a default PlayerView, add View reference and MapController reference
     * @param view is the View
     * @param mapController is the map controller which handle event when user interact with the map, pass it to View
     */
    public PlayerView(View view, MapController mapController) {
        this.view = view;
        currentPlayerLabel = mapController.getCurrentPlayerLabel();
        armiesInHandLabel = mapController.getArmiesInHandLabel();
    }


    /**
     * Observer update method, update current Player info, store and set it to countryPane
     * Called by corresponding Country Observable subject
     * @param obs is the corresponding Player Observable subject
     * @param arg is the additional info for update
     */
    @Override
    public void update(Observable obs, Object arg) {
//        System.out.print("PlayerView.update: ");
        Player currentPlayer = (Player) obs;
        String color = currentPlayer.getColor();

        name = currentPlayer.getName();
        currentPlayerLabel.setText(name);
        armiesInHands = currentPlayer.getArmies();
//        System.out.print(", armies = " + armiesInHands);
        armiesInHandLabel.setText(Integer.toString(armiesInHands));
        currentPlayerLabel.setStyle("-fx-background-color: " + color);
        armiesInHandLabel.setStyle("-fx-background-color: " + color);
        if (0 == armiesInHands) view.prepareNextPhase();
        //        System.out.println("");
    }


    /**
     * Get the current player name
     * Called by View, to check clicked country's ownership
     * @return the current player name
     */
    public String getName() { return name; }


    /**
     * Get the current player's armies in hand
     * Called by View.allocateArmy()
     * @return number of armies that the current player has in hands
     */
    int getArmiesInHands() { return armiesInHands; }
}
