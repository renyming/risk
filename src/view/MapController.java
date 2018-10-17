package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    @FXML private Button nextPhaseButton;
    @FXML private AnchorPane currentPlayerPane;
    @FXML private Label currentPlayerLabel;
    @FXML private Label armiesInHandLabel;
    @FXML private Label reinforceFromLabel;
    @FXML private Label reinforceFromCountryLabel;
    @FXML private Label reinforceToLabel;
    @FXML private Label reinforceToCountryLabel;
    @FXML private Label phaseLabel;

    public void initialize(View view, double newCountryViewWidth, double newCountryViewHeight) {
        this.view = view;
        this.countryViewWidth = newCountryViewWidth;
        this.countryViewHeight = newCountryViewHeight;
        playerColor = DEFAULT_PLAYER_COLOR;
        continentColor = DEFAULT_CONTINENT_COLOR;
        reinforceFromLabel.setVisible(false);
        reinforceFromCountryLabel.setVisible(false);
        reinforceToLabel.setVisible(false);
        reinforceToCountryLabel.setVisible(false);
        nextPhaseButton.setVisible(false);
        mapPane.setOnMouseClicked((e) -> {
            if (view.checkEdit() && e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                //TODO: get the player color somehow
                //TODO: get the continent color by the map continent framework
                // cursor position is translated to the countryView lef-top corner position
                view.createDefaultCountryView(e.getX() - countryViewWidth/2, e.getY() - countryViewHeight/2, playerColor, continentColor);
            }
        });
    }

    public void backToMenu() { view.showMenuStage(); }

    public AnchorPane getCurrentPlayerPane() { return currentPlayerPane; }

    public void setPlayerColor(String color) { playerColor = color; }

    public void setContinentColor(String color) { continentColor = color; }

    public Label getCurrentPlayerLabel() { return currentPlayerLabel; }

    public Label getArmiesInHandLabel() { return armiesInHandLabel; }

    public Label getPhaseLabel() { return phaseLabel; }

    public void switchToNextPhase() {
        view.switchToNextPhase();
    }

    public void showNextPhaseButton(String nextPhase) {
        nextPhaseButton.setText(nextPhase);
        nextPhaseButton.setVisible(true);
    }
}
