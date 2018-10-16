package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MapController {

    private String DEFAULT_PLAYER_COLOR = "#ff0000";    // red
    private String DEFAULT_CONTINENT_COLOR = "#0000ff"; // blue

    private View view;
    private double countryViewWidth;
    private double countryViewHeight;
    private String playerColor;
    private String continentColor;

    @FXML private AnchorPane mapPane;
    @FXML private Button saveEditedMapButton;
    @FXML private Button backToMenuButton;

    public void initialize(View view, double newCountryViewWidth, double newCountryViewHeight) {
        this.view = view;
        this.countryViewWidth = newCountryViewWidth;
        this.countryViewHeight = newCountryViewHeight;
        playerColor = DEFAULT_PLAYER_COLOR;
        continentColor = DEFAULT_CONTINENT_COLOR;
        mapPane.setOnMouseClicked((e) -> {
            if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                //TODO: get the player color somehow
                //TODO: get the continent color by the map continent framework
                // cursor position is translated to the countryView lef-top corner position
                view.createDefaultCountryView(e.getX() - countryViewWidth/2, e.getY() - countryViewHeight/2, playerColor, continentColor);
            }
        });
    }

    public void backToMenu() { view.showMenuStage(); }

    public void setPlayerColor(String color) { playerColor = color; }

    public void setContinentColor(String color) { continentColor = color; }
}
