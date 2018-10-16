package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CountryController {

    private CountryView countryView;

    @FXML private Label countryNameLabel;
    @FXML private Label numArmiesLabel;
    @FXML private HBox displayArmiesHBox;

    public void setDefaultInfo(CountryView countryView, String name, int armies, String playerColor, String continentColor) {
        this.countryView = countryView;
        Text t = new Text ("Stroke and Fill");
        t.setStyle("-fx-background-color: yellow");
        countryNameLabel.setText(name);
        numArmiesLabel.setText(Integer.toString(armies));
        countryNameLabel.setStyle("-fx-background-color: " + continentColor);
        displayArmiesHBox.setStyle("-fx-background-color: " + playerColor);
    }

    public void removeCountryView() { countryView.removeCountryView(); }

    public void updateCountryPaneInfo(String countryName, String playerColor, String continentColor, int armiesNumber) {
        countryNameLabel.setText(countryName);
        countryNameLabel.setStyle("-fx-background-color: " + continentColor);
        numArmiesLabel.setText(Integer.toString(armiesNumber));
        displayArmiesHBox.setStyle("-fx-background-color: " + playerColor);
    }
}
