package com.risk.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import com.risk.model.Country;
import com.risk.model.Model;
import com.risk.model.Phase;
import com.risk.view.*;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Handle event when user interacts UI during the map stage
 */
public class MapController {

    // general map components
    @FXML private Label currentPlayerLabel;
    @FXML private Label armiesInHandLabel;
    @FXML private Button nextPhaseButton;
    @FXML private Label invalidMovedLabel;
    @FXML private AnchorPane mapPane;
    @FXML private Label phaseLabel;
    @FXML private AnchorPane currentPlayerPane;

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
    @FXML private Button attackButton;

    // fortification relative components
    @FXML private Label numArmiesMovedLabel;         // also used for attack when conquer occurs
    @FXML private TextField numArmiesMovedTextField; // also used for attack when conquer occurs
    @FXML private Button skipFortificationPhaseButton;

    // players world domination view
    @FXML private ListView<String> countryPercentageListView;
    @FXML private ListView<String> armyDistributionListView;
    @FXML private ListView<String> continentNameListView;
    @FXML private PieChart countryChart;

    //card button
    @FXML private Button cardButton;

    private Model model;
    private Map map;
    private MenuController menuController;
    private CardView card;
    private CardController cardController;

    private Country fromToCountries[];
    private int fromToCountriesCounter;
    private HashMap<Integer, CountryView> countryViews;
//    private HashSet<Line> lines;
    private HashSet<Arrow> arrows;
    private int numOfCountries;
    private String currentPhase;
    private boolean enableFortification;

    // attack phase relative info
    private int attackerDefenderDices[];
    private boolean allOut;
    private boolean countryClickable;
    private boolean win;


    /**
     * Initialize Map Controller, set references, set default button visibility
     * @param model is the Model reference
     * @param map is the Map reference
     * @param menuController is the MenuController reference
     */
    public void init(Model model, Map map, MenuController menuController, CardView card, CardController cardController) {
        this.model = model;
        this.map = map;
        this.menuController = menuController;
        this.card = card;
        this.cardController= cardController;

        skipFortificationPhaseButton.setVisible(false);
        numArmiesMovedTextField.setVisible(false);
        numArmiesMovedLabel.setVisible(false);
        invalidMovedLabel.setVisible(false);
        nextPhaseButton.setVisible(false);
        countryALabel.setVisible(false);
        countryBLabel.setVisible(false);
        countryANameLabel.setVisible(false);
        countryBNameLabel.setVisible(false);
        cardButton.setDisable(true);
        addEventListener();


        fromToCountriesCounter = 0;
        fromToCountries = new Country[2];
        attackerDefenderDices = new int[2];
        enableFortification = false;
        countryClickable = true;
        win = false;

        PlayersWorldDominationView.getInstance().init(countryPercentageListView, armyDistributionListView, continentNameListView, model, countryChart);
    }


    /**
     * Add event listener to the countryPane
     */
    private void addEventListener() {
        mapPane.setOnMouseClicked((e) -> clickedMap());
        currentPlayerPane.setOnMouseClicked(Event::consume);
        phaseLabel.setOnMouseClicked(Event::consume);
        attackerDiceOneButton.setOnMouseClicked((e) -> {
            attackerDefenderDices[0] = 1;
            clearAttackerDiceButtons();
            attackerDiceOneButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        });
        attackerDiceTwoButton.setOnMouseClicked((e) -> {
            attackerDefenderDices[0] = 2;
            clearAttackerDiceButtons();
            attackerDiceTwoButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        });
        attackerDiceThreeButton.setOnMouseClicked((e) -> {
            attackerDefenderDices[0] = 3;
            clearAttackerDiceButtons();
            attackerDiceThreeButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        });
        defenderDiceOneButton.setOnMouseClicked((e) -> {
            attackerDefenderDices[1] = 1;
            clearDefenderDiceButtons();
            defenderDiceOneButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        });
        defenderDiceTwoButton.setOnMouseClicked((e) -> {
            attackerDefenderDices[1] = 2;
            clearDefenderDiceButtons();
            defenderDiceTwoButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        });
        allOutEnableButton.setOnMouseClicked((e) -> {
            allOut = true;
            attackerDefenderDices[0] = 1;
            attackerDefenderDices[1] = 1;
            clearAttackerDiceButtons();
            clearDefenderDiceButtons();
            displayDices(false);
            allOutEnableButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
            allOutDisableButton.setStyle("");
        });
        allOutDisableButton.setOnMouseClicked((e) -> resetAllDices());
    }


    /**
     * Set total number of countries, help to create CountryViews later
     * @param numOfCountries is the total number of countries for a given map
     */
    public void setNumOfCountries(int numOfCountries) { this.numOfCountries = numOfCountries; }


    /**
     * Set the current phase, help to determine the next phase
     * @param currentPhase is the current phase
     */
    public void setCurrentPhase(String currentPhase) {
        this.currentPhase = currentPhase;
        switch (currentPhase) {
            case "Fortification Phase":
                enableFortification = true;
                break;
        }
    }


    /**
     * Create the PhaseView, pass relative button/pane/.. reference to it
     */
    void createPhaseView() {
        PhaseView phaseView = PhaseView.getInstance();
        phaseView.init(phaseLabel, nextPhaseButton, currentPlayerLabel, armiesInHandLabel,
                countryALabel, countryANameLabel, countryBLabel, countryBNameLabel,
                numArmiesMovedLabel, numArmiesMovedTextField, invalidMovedLabel,
                skipFortificationPhaseButton,
                this);
        phaseView.initAttackComponents(attackerDiceLabel, attackerDiceOneButton, attackerDiceTwoButton, attackerDiceThreeButton,
                defenderDiceLabel, defenderDiceOneButton, defenderDiceTwoButton,
                allOutLabel, allOutEnableButton, allOutDisableButton, attackButton);
    }


    /**
     * Create CountryViews for each Country
     * @return the CountryViews
     */
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
     * Create a single default CountryView Observer object, and then return it
     * @return CountryView Observer object
     */
    private CountryView createDefaultCountryView() { return new CountryView(this); }


    /**
     * Called by MenuController when user click the start button in select-map menu
     */
    void showMapStage() {
        AnchorPane mapRootPane = map.getMapRootPane();

        // draw lines
        final double COUNTRY_VIEW_WIDTH = 60;
        final double COUNTRY_VIEW_HEIGHT = 60;
//        lines = new HashSet<>();
        arrows = new HashSet<>();
        for (int key : countryViews.keySet()) {
            Country countryA = countryViews.get(key).getCountry();
            for (Country countryB : countryA.getAdjCountries()) {
//                Line line = new Line();
//                line.setStartX(countryA.getX() + COUNTRY_VIEW_WIDTH/2);
//                line.setStartY(countryA.getY() + COUNTRY_VIEW_HEIGHT/2);
//                line.setEndX(countryB.getX() + COUNTRY_VIEW_WIDTH/2);
//                line.setEndY(countryB.getY() + COUNTRY_VIEW_HEIGHT/2);
//                line.setStroke(Color.BLACK);
//                line.setStrokeWidth(2);
                Arrow arrow = new Arrow("DEFAULT");
//                System.out.println(countryA.getName() + " " + countryB.getName());
                arrow.setStart(countryA.getX() + COUNTRY_VIEW_WIDTH/2, countryA.getY() + COUNTRY_VIEW_HEIGHT/2);
                arrow.setEnd(countryB.getX() + COUNTRY_VIEW_WIDTH/2, countryB.getY() + COUNTRY_VIEW_HEIGHT/2);
//                lines.add(line);
                arrows.add(arrow);
                mapRootPane.getChildren().add(arrow);
            }
        }

        // draw countries
        for (int key : countryViews.keySet()) mapRootPane.getChildren().add(countryViews.get(key).getCountryPane());

        map.show();
    }


    /**
     * Prepare all necessary map component for the next phase, then enter the next phase
     * Called when user clicked the next phase button
     */
    public void enterNextPhase() {
        nextPhaseButton.setVisible(false);
        switch (currentPhase) {
            case "Start Up Phase": case "Fortification Phase":
                model.nextPlayer();
                model.reinforcement();
                if(cardButton.isDisable()){
                    cardButton.setDisable(false);
                }
                break;
            case "Reinforcement Phase":
                Phase.getInstance().setCurrentPhase("Attack Phase");
                Phase.getInstance().update();
                fromToCountriesCounter = 0;
                clearAttackerDiceButtons();
                attackerDefenderDices[0] = 1;
                attackerDiceOneButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
                clearDefenderDiceButtons();
                attackerDefenderDices[1] = 1;
                defenderDiceOneButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
                resetAllDices();
                setCountryClick(true);
                model.isAttackPossible();
                break;
            case "Attack Phase":
                Phase.getInstance().setCurrentPhase("Fortification Phase");
                Phase.getInstance().update();
                skipFortificationPhaseButton.setDisable(false);
                fromToCountriesCounter = 0;
                model.addRandomCard();
                break;
        }
    }


    /**
     * Called when user clicked a country
     * @param country the country which user clicked
     */
    void clickedCountry(Country country) {
        switch (currentPhase) {
            case "Start Up Phase":
                model.allocateArmy(country);
                break;
            case "Reinforcement Phase":
//                Model.getCurrentPlayer().getStrategy().allocateArmy(country, Model.disable);
                model.allocateArmy(country);
                break;
            case "Attack Phase":
                if (!countryClickable || win) break;
                invalidMovedLabel.setVisible(false);
                switch (fromToCountriesCounter) {
                    case 0:
                        resetFromToCountries();
                        resetAllDices();
                        setFromCountryInfo(country);
                        fromToCountriesCounter++;
                        break;
                    case 1:
                        setToCountryInfo(country);
                        if (0 == country.getArmies()) {
                            attackerDefenderDices[1] = 0;
                            clearAttackerDiceButtons();
                            clearDefenderDiceButtons();
                            displayDices(false);
                            allOutEnableButton.setDisable(true);
                        }
                        fromToCountriesCounter--;
                        break;
                }
                break;
            case "Fortification Phase":
                if (enableFortification) {
                    invalidMovedLabel.setVisible(false);
                    switch (fromToCountriesCounter) {
                        case 0:
                            resetFromToCountries();
                            setFromCountryInfo(country);
                            fromToCountriesCounter++;
                            break;
                        case 1:
                            setToCountryInfo(country);
                            fromToCountriesCounter--;
                            break;
                    }
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
        if(Phase.getInstance().getCurrentPlayer().equals(country.getOwner())){
        countryANameLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        }
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
     * Called when user click the map, reset the from-to country relative info
     */
    private void clickedMap() {
        if (win) return;
        if (currentPhase.equals("Attack Phase") && countryClickable) {
            resetFromToCountries();
            resetAllDices();
        } else if (currentPhase.equals("Fortification Phase") && enableFortification) {
            resetFromToCountries();
        }
    }


    /**
     * Reset selected countries
     * Called by MapController if user trying to reset the selected countries by clicking the map or another country
     */
    private void resetFromToCountries() {
        fromToCountries[0] = null;
        fromToCountries[1] = null;
        fromToCountriesCounter = 0;
        countryANameLabel.setText("NONE");
        countryANameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        countryBNameLabel.setText("NONE");
        countryBNameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
//        invalidMovedLabel.setVisible(false);
    }


    /**
     * Clear attacker's dices selection
     */
    private void clearAttackerDiceButtons() {
        attackerDiceOneButton.setStyle("");
        attackerDiceTwoButton.setStyle("");
        attackerDiceThreeButton.setStyle("");
    }


    /**
     * Clear defender's dices selection
     */
    private void clearDefenderDiceButtons() {
        defenderDiceOneButton.setStyle("");
        defenderDiceTwoButton.setStyle("");
    }


    /**
     * Reset all dice relative info
     */
    private void resetAllDices() {
        if (null != fromToCountries[1] && 0 == fromToCountries[1].getArmies()) return;
        allOut = false;
        attackerDefenderDices[0] = 1;
        clearAttackerDiceButtons();
        attackerDiceOneButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        attackerDefenderDices[1] = 1;
        clearDefenderDiceButtons();
        defenderDiceOneButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        allOutEnableButton.setStyle("");
        allOutDisableButton.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        displayDices(true);
        allOutEnableButton.setDisable(false);
    }


    /**
     * After gather countries and dices info, pass them to model to execute the attack
     */
    public void attack() {
        numArmiesMovedTextField.requestFocus();
        invalidMovedLabel.setVisible(false);
        model.attack(fromToCountries[0], Integer.toString(attackerDefenderDices[0]), fromToCountries[1], Integer.toString(attackerDefenderDices[1]), allOut);
    }


    /**
     * Show or hide all dices button
     * @param display determine either show or hide
     */
    private void displayDices(boolean display) {
        attackerDiceOneButton.setDisable(!display);
        attackerDiceTwoButton.setDisable(!display);
        attackerDiceThreeButton.setDisable(!display);
        defenderDiceOneButton.setDisable(!display);
        defenderDiceTwoButton.setDisable(!display);
    }


    /**
     * Called when user entered number of armies moved value and press enter button, pass event to View
     */
    public void enteredNumArmiesMoved() {
        invalidMovedLabel.setVisible(false);
        if (currentPhase.equals("Fortification Phase") && enableFortification) {
            model.fortification(fromToCountries[0], fromToCountries[1], numArmiesMovedTextField.getText());
        } else if (currentPhase.equals("Attack Phase")) {
            model.moveAfterConquer(numArmiesMovedTextField.getText());
            resetAllDices();
        }
    }


    /**
     * Set country clickable after conquer a country
     * @param countryClickable determine whether is clickable
     */
    public void setCountryClick(boolean countryClickable) { this.countryClickable = countryClickable; }


    public void setWin() { win = true; }


    /**
     * Since fortification can only be done once, so call it after one successful fortification
     */
    public void disableFortification() {
        enableFortification = false;
        skipFortificationPhaseButton.setVisible(false);
    }


    /**
     * During fortification phase, skip this phase if current user is not able to move armies
     * i.e. only has one country left or the user does not want to
     * Called by 'Skip' button on PhaseView
     */
    public void skipFortificationPhase() {
        enableFortification = false;
//        phaseLabel.setVisible(false);
        nextPhaseButton.setVisible(true);
        countryANameLabel.setVisible(false);
        countryALabel.setVisible(false);
        countryBNameLabel.setVisible(false);
        countryBLabel.setVisible(false);
        numArmiesMovedTextField.setVisible(false);
        numArmiesMovedLabel.setVisible(false);
        invalidMovedLabel.setVisible(false);
        skipFortificationPhaseButton.setDisable(true);


    }

    /**
     * card button to open card exchange window
     */
    public void openCardWindow(){
        cardController.openReadOnlyCardWindow();
        card.show();
    }

//    /**
//     * Called when user click the 'Quit' during the game play
//     */
//    public void backToMenu() {
//        resetMapComponents();
//        map.hide();
//        menuController.switchToStartGameMenu();
//    }
//
//
//    /**
//     * Clear all lines and countries on the map
//     */
//    private void resetMapComponents() {
//        if (null != countryViews) {
//            for (int key : countryViews.keySet()) {
//                AnchorPane countryPane = countryViews.get(key).getCountryPane();
//                countryPane.getChildren().clear();
//                map.getMapRootPane().getChildren().remove(countryPane);
//            }
//            countryViews.clear();
//        }
//        if (null != lines) {
//            for (Line line : lines) {
//                map.getMapRootPane().getChildren().remove(line);
//            }
//            lines.clear();
//        }
//    }


    /**
     * Called by MenuController when user quit the game from menu
     */
    public void quitGame() { map.close(); }
}
