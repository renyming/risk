package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Country;

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
    @FXML private Label countryALabel;
    @FXML private Label countryAName;
    @FXML private Label countryBLabel;
    @FXML private Label countryBName;
    @FXML private Label phaseLabel;
    @FXML private Label numArmiesMoveLabel;
    @FXML private TextField numArmiesMoveTextField;

    public void initialize(View view, double newCountryViewWidth, double newCountryViewHeight) {
        this.view = view;
        this.countryViewWidth = newCountryViewWidth;
        this.countryViewHeight = newCountryViewHeight;
        playerColor = DEFAULT_PLAYER_COLOR;
        continentColor = DEFAULT_CONTINENT_COLOR;
        countryALabel.setVisible(false);
        countryAName.setVisible(false);
        countryBLabel.setVisible(false);
        countryBName.setVisible(false);
        nextPhaseButton.setVisible(false);
        numArmiesMoveLabel.setVisible(false);
        numArmiesMoveTextField.setVisible(false);
        addListener(); // TODO: further refactor
        mapPane.setOnMouseClicked((e) -> {
            if (view.checkEdit() && e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                //TODO: get the player color somehow
                //TODO: get the continent color by the map continent framework
                // cursor position is translated to the countryView lef-top corner position
                view.createDefaultCountryView(e.getX() - countryViewWidth/2, e.getY() - countryViewHeight/2, playerColor, continentColor);
            }
        });
        numArmiesMoveTextField.setOnAction((event) -> {
            int numArmiesMoved = 0;
            try {
                numArmiesMoved = Integer.parseInt(numArmiesMoveTextField.getText());
            } catch (Exception e) {
                System.out.println("MapController.initialize(): input value not integer " + numArmiesMoveTextField.getText());
            }
            if (numArmiesMoved > 0) {
                view.fortification(numArmiesMoved);
            } else {
                System.out.println("MapController.initialize(): input integer not positive, " + numArmiesMoveTextField.getText());
            }
        });
    }

    public void addListener() {

    }

    public void backToMenu() { view.showMenuStage(); }

    public AnchorPane getCurrentPlayerPane() { return currentPlayerPane; }

    public void setPlayerColor(String color) { playerColor = color; }

    public void setContinentColor(String color) { continentColor = color; }

    public Label getCurrentPlayerLabel() { return currentPlayerLabel; }

    public Label getArmiesInHandLabel() { return armiesInHandLabel; }

    public void setPhaseLabel(String phase) { phaseLabel.setText(phase); }

    public void hidePhaseLabel() { phaseLabel.setVisible(false); }

    public void showPhaseLabel() { phaseLabel.setVisible(true); }

    public void showNextPhaseButton(String nextPhase) {
        nextPhaseButton.setText(nextPhase);
        nextPhaseButton.setVisible(true);
    }

    public void hideNextPhaseButton() { nextPhaseButton.setVisible(false); }

    public void startNextPhase() { view.startNextPhase(); }

    public void showFromToCountriesInfoPane(boolean show) {
        countryALabel.setVisible(show);
        countryAName.setVisible(show);
        countryBLabel.setVisible(show);
        countryBName.setVisible(show);
        numArmiesMoveLabel.setVisible(show);
        numArmiesMoveTextField.setVisible(show);
    }

    public String getNextPhaseButtonTest() { return nextPhaseButton.getText(); }

    public void setFromCountryInfo(Country country) {
        countryAName.setText(country.getName());
        countryAName.setStyle("-fx-background-color: " + country.getOwner().getColor());
    }

    public void setToCountryInfo(Country country) {
        countryBName.setText(country.getName());
        countryBName.setStyle("-fx-background-color: " + country.getOwner().getColor());
    }

    public void resetFromToCountriesInfo() {
        countryAName.setText("NONE");
        countryAName.setStyle("-fx-background-color: white");
        countryBName.setText("NONE");
        countryBName.setStyle("-fx-background-color: white");
    }
}
