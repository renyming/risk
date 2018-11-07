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

    // general map components
    @FXML private Label currentPlayerLabel;
    @FXML private Label armiesInHandLabel;
    @FXML private Button nextPhaseButton;
    @FXML private Label invalidMovedLabel;
    @FXML private AnchorPane mapPane;
    @FXML private Label phaseLabel;

    // From-To country relative components
    @FXML private Label countryALabel;
    @FXML private Label countryANameLabel;
    @FXML private Label countryBLabel;
    @FXML private Label countryBNameLabel;

    // attack phase relative components
    @FXML private Label attackerDiceLabel;
    @FXML private Button attackerDiceOneButton;
    @FXML private Button attackerDiceTwoButton;
    @FXML private Button attackerDiceThreeButton;
    @FXML private Label defenderDiceLabel;
    @FXML private Button defenderDiceOneButton;
    @FXML private Button defenderDiceTwoButton;
    @FXML private Label allOutLabel;
    @FXML private Button allOutEnableButton;
    @FXML private Button allOutDisableButton;

    // fortification relative components
    @FXML private Label numArmiesMovedLabel;
    @FXML private TextField numArmiesMovedTextField;
    @FXML private Button skipFortificationPhaseButton;



    private Model model;
    private Map map;
    private MenuController menuController;

    private Country fromToCountries[];
    private int fromToCountriesCounter;
    private HashMap<Integer, CountryView> countryViews;
    private HashSet<Line> lines;
    private int numOfCountries;
    private String currentPhase;
    private boolean enableFortification;

    public void init(Model model, Map map, MenuController menuController) {
        this.model = model;
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
        enableFortification = false;
    }


    /**
     * Add event listener to the countryPane
     */
    private void addEventListener() {
        mapPane.setOnMouseClicked((e) -> { if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            clickedMap();
        } });
    }


    public void setNumOfCountries(int numOfCountries) { this.numOfCountries = numOfCountries; }


    public void setCurrentPhase(String currentPhase) {
        this.currentPhase = currentPhase;
        switch (currentPhase) {
            case "Fortification Phase":
                enableFortification = true;
                break;
        }
    }

    void createPhaseView() {
        PhaseView phaseView = PhaseView.getInstance();
        phaseView.init(phaseLabel, nextPhaseButton, currentPlayerLabel, armiesInHandLabel,
                countryALabel, countryANameLabel, countryBLabel, countryBNameLabel,
                numArmiesMovedLabel, numArmiesMovedTextField, invalidMovedLabel,
                skipFortificationPhaseButton,
                this);
        phaseView.initAttackComponents(attackerDiceLabel, attackerDiceOneButton, attackerDiceTwoButton, attackerDiceThreeButton,
                defenderDiceLabel, defenderDiceOneButton, defenderDiceTwoButton,
                allOutLabel, allOutEnableButton, allOutDisableButton);
    }


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


    /**
     * After loading a valid file, create a default CountryView Observer object, and then return it
     * Called by View.update() at state CREATE_OBSERVERS
     * Allow Model to bind it with Country Observable object later
     * @return CountryView Observer object
     */
    private CountryView createDefaultCountryView() { return new CountryView(this); }


    /**
     * Called by MenuController when user click the start button
     */
    void showMapStage() {
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

        map.show();
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
                fromToCountriesCounter = 0;
                break;
            case "Attack Phase":
                Phase.getInstance().setCurrentPhase("Fortification Phase");
                Phase.getInstance().update();
                fromToCountriesCounter = 0;
                break;
        }
    }


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
                if (enableFortification) {
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
                }
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
     * During fortification phase, reset selected countries
     */
    private void clickedMap() {
        if (enableFortification) {
            resetFromToCountries();
            fromToCountriesCounter = 0;
        }
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
    }


    /**
     * Called when user entered number of armies moved value and press enter button, pass event to View
     */
    public void enteredNumArmiesMoved() {
        invalidMovedLabel.setVisible(false);
        if (enableFortification) model.fortification(fromToCountries[0], fromToCountries[1], numArmiesMovedTextField.getText());
    }


    public void disableFortification() {
        enableFortification = false;
        skipFortificationPhaseButton.setVisible(false);
    }


    /**
     * During fortification phase, skip this phase if current user is not able to move armies
     * i.e. only has one country left or the user does not want to
     * Called by 'Skip' button on player view
     */
    public void skipReinforcementPhase() {
        model.nextPlayer();
        model.reinforcement();
    }





    /**
     * Called when user click the 'Quit' button during the game play
     */
    public void backToMenu() {
        resetMapComponents();
        map.hide();
        menuController.switchToStartGameMenu();
    }

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
     * Called by MenuController when user quit the game from menu
     */
    void quitGame() { map.close(); }

}
