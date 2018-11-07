package controller;

import common.Action;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.Continent;
import model.Country;
import model.Model;
import model.Phase;
import view.*;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Handle event when user interact with the map, pass it to View
 */
public class MapController {

    /**
     * Determine the current or next phase
     */

    @FXML private Button skipFortificationPhaseButton;
    @FXML private TextField numArmiesMovedTextField;
    @FXML private AnchorPane currentPlayerPane;
    @FXML private Label currentPlayerLabel;
    @FXML private Label numArmiesMovedLabel;
    @FXML private Label armiesInHandLabel;
    @FXML private Button nextPhaseButton;
    @FXML private Label invalidMovedLabel;
    @FXML private Label countryALabel;
    @FXML private Label countryBLabel;
    @FXML private Label countryANameLabel;
    @FXML private Label countryBNameLabel;
    @FXML private AnchorPane mapPane;
    @FXML private Label phaseLabel;

    private Model model;
    private View view;
    private Map map;
    private MenuController menuController;

    private Country fromToCountries[];
    private int fromToCountriesCounter;
    private HashMap<Integer, CountryView> countryViews;
    private HashSet<Line> lines;
    private PlayerView playerView;
    private PhaseView phaseView;
    private int numOfCountries;
    private String currentPhase;

    public void init(Model model, View view, Map map, MenuController menuController) {
        this.model = model;
        this.view = view;
        this.map = map;
        this.menuController = menuController;

        skipFortificationPhaseButton.setVisible(false);
        numArmiesMovedTextField.setVisible(false);
        numArmiesMovedLabel.setVisible(false);
        invalidMovedLabel.setVisible(false);
        nextPhaseButton.setVisible(false);
        countryALabel.setVisible(false);
        countryBLabel.setVisible(false);
        countryANameLabel.setVisible(false);
        countryBNameLabel.setVisible(false);
        addEventListener();


        fromToCountriesCounter = 0;
        fromToCountries = new Country[2];
    }


    /**
     * Add event listener to the countryPane
     */
    private void addEventListener() {
        mapPane.setOnMouseClicked((e) -> { if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            clickedMap();
        } });
    }


    /**
     * Display/Hide the invalid move label, update the invalid info
     * Called by View.*()
     * @param show decides whether the invalid move label need to be displayed
     * @param invalidInfo is the invalid move info
     */
    private void showInvalidMoveLabelInfo(boolean show, String invalidInfo) {
        numArmiesMovedTextField.clear(); // TODO: could be removed?
        invalidMovedLabel.setVisible(show);
        invalidMovedLabel.setText(invalidInfo);
    }


    /**
     * Display/Hide the from-to countries info pane between different phase
     * Called by View.*()
     * @param show decides whether the from-to countries info need to be displayed
     */
    private void displayFromToCountriesInfoPane(boolean show) {
        if (show) {
            resetFromToCountriesInfo();
        }
        countryALabel.setVisible(show);
        countryANameLabel.setVisible(show);
        countryBLabel.setVisible(show);
        countryBNameLabel.setVisible(show);
        numArmiesMovedLabel.setVisible(show);
        numArmiesMovedTextField.setVisible(show);
    }


    /**
     * Called when user click the 'Skip' button during reinforcement phase
     * Pass the event to View
     */
    public void skipReinforcementPhase() {
        showPlayerViewPane(false);
        displayFromToCountriesInfoPane(false);
        showInvalidMoveLabelInfo(false, "");
        skipFortificationPhase();
    }


    /**
     * Called when user click the 'Quit' button during the game play
     */
    public void backToMenu() {
        resetMapComponents();
        displayFromToCountriesInfoPane(false);
        map.hide();
        menuController.switchToStartGameMenu();
    }


    /**
     * Show/Hide the player view pane based on the parameter
     * Clear num armies moved text field before hide it
     * Called by map controller
     * @param show decides whether the player view pane need to be shown
     */
    private void showPlayerViewPane(boolean show) {
        if (show) {
            currentPlayerPane.setVisible(true);
        } else {
            skipFortificationPhaseButton.setVisible(false);
            numArmiesMovedTextField.clear();
            numArmiesMovedTextField.setVisible(false);
        }
    }


    /**
     * Start next phase
     * Called when user clicked the next phase button, pass the event to View
     */
    public void startNextPhase() {
        nextPhaseButton.setVisible(false);
        switch (currentPhase) {
            case "Start Up Phase": case "Fortification Phase":
                model.nextPlayer();
                model.reinforcement();
                break;
            case "Reinforcement Phase":
                Phase.getInstance().setCurrentPhase("Attack Phase");
                Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button); // TODO: set fake attack, remove it later
                Phase.getInstance().update();
                break;
            case "Attack Phase":
                Phase.getInstance().setCurrentPhase("Fortification Phase");
                Phase.getInstance().update();
                break;
        }
    }


    /**
     * During fortification phase, reset selected countries
     */
    private void clickedMap() { if (View.PHASE.FORTIFICATION == view.getCurrentPhase()) resetFromToCountries(); }


    /**
     * During fortification phase, skip this phase if current user is not able to move armies
     * i.e. only has one country left or the user does not want to
     * Called by 'Skip' button on player view
     */
    private void skipFortificationPhase() { model.nextPlayer(); }


    /**
     * Clear all CountryViews and Lines that generated before
     * Called by View.showMenuStage()
     */
    private void resetMapComponents() {
        if (null != countryViews) {
            for (int key : countryViews.keySet()) {
                AnchorPane countryPane = countryViews.get(key).getCountryPane();
                countryPane.getChildren().clear();
                map.getMapRootPane().getChildren().remove(countryPane);
            }
            countryViews.clear();
        }
        if (null != lines) {
            for (Line line : lines) {
                map.getMapRootPane().getChildren().remove(line);
            }
            lines.clear();
        }
    }


    /**
     * Draw each CountryView and Line into mapRootPane
     * Called by View.showMapStage()
     */
    public void drawMap() {
        // TODO: use efficient way to draw lines, avoid duplicate
        double COUNTRY_VIEW_WIDTH = 60; // TODO: refactor
        double COUNTRY_VIEW_HEIGHT = 60; // TODO: refactor
        AnchorPane mapRootPane = map.getMapRootPane();
        lines = new HashSet<>();
        for (int key : countryViews.keySet()) {
            Country countryA = countryViews.get(key).getCountry();
            for (Country countryB : countryA.getAdjCountries()) {
                Line line = new Line();
                line.setStartX(countryA.getX() + COUNTRY_VIEW_WIDTH/2);
                line.setStartY(countryA.getY() + COUNTRY_VIEW_HEIGHT/2);
                line.setEndX(countryB.getX() + COUNTRY_VIEW_WIDTH/2);
                line.setEndY(countryB.getY() + COUNTRY_VIEW_HEIGHT/2);
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(2);
                lines.add(line);
                mapRootPane.getChildren().add(line);
            }
        }
        for (int key : countryViews.keySet()) mapRootPane.getChildren().add(countryViews.get(key).getCountryPane());
    }


    /**
     * After loading a valid file, create a default CountryView Observer object, and then return it
     * Called by View.update() at state CREATE_OBSERVERS
     * Allow Model to bind it with Country Observable object later
     * @return CountryView Observer object
     */
    private CountryView createDefaultCountryView() { return new CountryView(this); }


    /**
     * Called when user clicked a country
     * For reinforcement phase: add clicked counter
     * For fortification phase: set selected countries
     * @param country the country which user clicked
     */
    void clickedCountry(Country country) {
        switch (currentPhase) {
            case "Start Up Phase": case "Reinforcement Phase":
                model.allocateArmy(country);
                break;
            case "Attack Phase":
                // TODO: collect From-TO countries info and dices info
                break;
            case "Fortification Phase":
                switch (fromToCountriesCounter) {
                    case 0:
                        setFromCountryInfo(country);
                        break;
                    case 1:
                        setToCountryInfo(country);
                        break;
                    case 2:
                        resetFromToCountries();
                        break;
                }
                fromToCountriesCounter++;
                break;
        }
    }


    /**
     * Update from-country info pane
     * Called by clickedCountry()
     * @param country is the from-country
     */
    private void setFromCountryInfo(Country country) {
        fromToCountries[0] = country;
        countryANameLabel.setText(country.getName());
        countryANameLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
    }


    /**
     * Update to-country info pane
     * Called by clickedCountry()
     * @param country is the to-country
     */
    private void setToCountryInfo(Country country) {
        fromToCountries[1] = country;
        countryBNameLabel.setText(country.getName());
        countryBNameLabel.setStyle("-fx-border-color: #00ff00;  -fx-border-width: 3");
    }


    /**
     * For the fortification usage, reset selected countries
     * Called by MapController if user trying to reset the selected countries by clicking the map or another country
     */
    private void resetFromToCountries() {
        fromToCountries[0] = null;
        fromToCountries[1] = null;
        fromToCountriesCounter = -1;
        resetFromToCountriesInfo();
    }


    /**
     * Reset from-to countries instruction labels and color
     * Called by View.*()
     */
    private void resetFromToCountriesInfo() {
        countryANameLabel.setText("NONE");
        countryANameLabel.setStyle("-fx-border-color: red; -fx-border-width: 3");
        countryBNameLabel.setText("NONE");
        countryBNameLabel.setStyle("-fx-border-color: red; -fx-border-width: 3");
        showInvalidMoveLabelInfo(false, "");
    }


    /**
     * Called when user entered number of armies moved value and press enter button, pass event to View
     */
    public void enteredNumArmiesMoved() { model.fortification(fromToCountries[0], fromToCountries[1], numArmiesMovedTextField.getText()); }






    HashMap<Integer, CountryView> createCountryViews() {
        if (null == countryViews) {
            countryViews = new HashMap<>();
        } else {
            countryViews.clear();
        }
        for (int i = 1; i <= numOfCountries; ++i) {
            countryViews.put(i, createDefaultCountryView());
        }
        return countryViews;
    }

    public void setNumOfCountries(int numOfCountries) {
        this.numOfCountries = numOfCountries;
    }

    void quitGame() { map.close(); }

    void showMapStage() { map.show(); }

    void createPhaseView() {
        phaseView = PhaseView.getInstance();
        phaseView.init(phaseLabel, nextPhaseButton, currentPlayerLabel, armiesInHandLabel,
                countryALabel, countryANameLabel, countryBLabel, countryBNameLabel,
                numArmiesMovedLabel, numArmiesMovedTextField, invalidMovedLabel,
                skipFortificationPhaseButton,
                this);
    }

    public void setCurrentPhase(String currentPhase) { this.currentPhase = currentPhase; }
}
