package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Country;
import view.CountryView;


/**
 * Handle event when user interact with the country, pass it to CountryView
 */
public class CountryController {

    @FXML private AnchorPane countryPane;
    @FXML private Label countryNameLabel;
    @FXML private Label numArmiesLabel;

    private CountryView countryView;


    /**
     * Get corresponding CountryView reference, add event listener
     * @param countryView the corresponding CountryView reference
     */
    public void init(CountryView countryView) {
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
     * @param country is the Country object which info need to be updated
     */
    public void updateCountryPaneInfo(Country country) {
        countryPane.setLayoutX(country.getX());
        countryPane.setLayoutY(country.getY());
        countryNameLabel.setText(country.getName());
        countryNameLabel.setStyle("-fx-background-color: " + country.getContinent().getColor() + "; -fx-background-radius: 5");
        numArmiesLabel.setText(Integer.toString(country.getArmies()));
        numArmiesLabel.setStyle("-fx-background-color: " + country.getOwner().getColor() + "; -fx-background-radius: 5");
    }
}
