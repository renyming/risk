package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CountryController {

    private CountryView countryView;

    @FXML private Label countryNameLabel;
    @FXML private Label numArmiesLabel;
    @FXML private AnchorPane countryPane;

    public void initiate(CountryView countryView) {
        this.countryView = countryView;
        addEventListener();
    }

    public void addEventListener() {
        countryPane.setOnMouseClicked((e) -> {
            if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                countryView.clicked();
            }
            e.consume();
        });
    }

    public void updateCountryPaneInfo(String countryName, String playerColor, String continentColor, int armiesNumber) {
        countryNameLabel.setText(countryName);
        countryNameLabel.setStyle("-fx-background-color: " + continentColor + "; -fx-background-radius: 5");
        numArmiesLabel.setText(Integer.toString(armiesNumber));
        numArmiesLabel.setStyle("-fx-background-color: " + playerColor + "; -fx-background-radius: 5");
    }
}
