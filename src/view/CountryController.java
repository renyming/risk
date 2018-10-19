package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


/**
 * Handle event when user interact with the country, pass it to CountryView
 */
public class CountryController {

    private CountryView countryView;

    @FXML private Label countryNameLabel;
    @FXML private Label numArmiesLabel;
    @FXML private AnchorPane countryPane;


    /**
     * Get corresponding CountryView reference, add event listener
     * @param countryView the corresponding CountryView reference
     */
    public void initiate(CountryView countryView) {
        this.countryView = countryView;
        addEventListener();
    }


    /**
     * Add event listener to the countryPane
     */
    private void addEventListener() {
        countryPane.setOnMouseClicked((e) -> { if (e.getEventType() == MouseEvent.MOUSE_CLICKED) { countryView.clicked(); e.consume(); } });
        // TODO: for draw arrow purpose, need to be update later
//        countryPane.setOnMousePressed((e) -> { if (e.getEventType() == MouseEvent.MOUSE_PRESSED) { countryView.pressed(); } });
//        countryPane.setOnMouseEntered((e) -> { if (e.getEventType() == MouseEvent.MOUSE_ENTERED) { countryView.entered(); } });
//        countryPane.setOnMouseReleased((e) -> { if (e.getEventType() == MouseEvent.MOUSE_RELEASED) { countryView.released(); } });
    }


    /**
     * Receive new labels' info, update them
     * Called by CountryView for updating labels' info
     * TODO: need to be refactor
     * @param countryName new country name
     * @param playerColor current owner color
     * @param continentColor continent color
     * @param armiesNumber current armies left on this country
     */
    public void updateCountryPaneInfo(String countryName, String playerColor, String continentColor, int armiesNumber) {
        countryNameLabel.setText(countryName);
        countryNameLabel.setStyle("-fx-background-color: " + continentColor + "; -fx-background-radius: 5");
        numArmiesLabel.setText(Integer.toString(armiesNumber));
        numArmiesLabel.setStyle("-fx-background-color: " + playerColor + "; -fx-background-radius: 5");
    }
}
