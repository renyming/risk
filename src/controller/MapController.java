package controller;

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
import view.CountryView;
import view.Map;
import view.PlayerView;
import view.View;

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
    @FXML private TextField numArmiesMoveTextField;
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
    private int numOfCountries;

    public void init(Model model, View view, Map map, MenuController menuController) {
        this.model = model;
        this.view = view;
        this.map = map;
        this.menuController = menuController;

        skipFortificationPhaseButton.setVisible(false);
        numArmiesMoveTextField.setVisible(false);
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
     * Called when user entered number of armies moved value and press enter button, pass event to View
     */
    public void enteredNumArmiesMoved() { fortification(numArmiesMoveTextField.getText()); } // TODO: refactor


    /**
     * Display/Hide the invalid move label, update the invalid info
     * Called by View.*()
     * @param show decides whether the invalid move label need to be displayed
     * @param invalidInfo is the invalid move info
     */
    public void showInvalidMoveLabelInfo(boolean show, String invalidInfo) {
        numArmiesMoveTextField.clear(); // TODO: could be removed?
        invalidMovedLabel.setVisible(show);
        invalidMovedLabel.setText(invalidInfo);
    }


    /**
     * Display/Hide the from-to countries info pane between different phase
     * Called by View.*()
     * @param show decides whether the from-to countries info need to be displayed
     */
    public void displayFromToCountriesInfoPane(boolean show) {
        if (show) {
            resetFromToCountriesInfo();
        }
        countryALabel.setVisible(show);
        countryANameLabel.setVisible(show);
        countryBLabel.setVisible(show);
        countryBNameLabel.setVisible(show);
        numArmiesMovedLabel.setVisible(show);
        numArmiesMoveTextField.setVisible(show);
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
     * Display 'Entering xxx Phase' with new text based on the received parameter, hide phase label
     * Called by View.showNextPhaseButton()
     * @param nextPhase is the next phase name
     */
    public void showNextPhaseButton(String nextPhase) {
        nextPhaseButton.setText(nextPhase);
        nextPhaseButton.setVisible(true);
        hidePhaseLabel();
    }


    private void showPhaseLabel() {
        phaseLabel.setVisible(true);
        nextPhaseButton.setVisible(false);
    }


    /**
     * Update from-country info pane
     * Called by View.clickedCountry()
     * @param country is the from-country
     */
    private void setFromCountryInfo(Country country) {
        countryANameLabel.setText(country.getName());
        countryANameLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
    }


    /**
     * Update to-country info pane
     * Called by View.clickedCountry()
     * @param country is the to-country
     */
    private void setToCountryInfo(Country country) {
        countryBNameLabel.setText(country.getName());
        countryBNameLabel.setStyle("-fx-border-color: #00ff00;  -fx-border-width: 3");
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
     * Set the phaseLabel name based on the parameter
     * Called by View.*()
     * @param phase is the phase name
     */
    public void setPhaseLabel(String phase) { phaseLabel.setText(phase); }


    /**
     * Hide phase label
     * Called by View.startNextPhase()
     */
    private void hidePhaseLabel() { phaseLabel.setVisible(false); }


    /**
     * Show/Hide the player view pane based on the parameter
     * Clear num armies moved text field before hide it
     * Called by map controller
     * @param show decides whether the player view pane need to be shown
     */
    public void showPlayerViewPane(boolean show) {
        if (show) {
            currentPlayerPane.setVisible(true);
        } else {
            skipFortificationPhaseButton.setVisible(false);
            numArmiesMoveTextField.clear();
            numArmiesMoveTextField.setVisible(false);
        }
    }


    /**
     * Start next phase
     * Called when user clicked the next phase button, pass the event to View
     */
    public void startNextPhase() {
        showPhaseLabel();
        showPlayerViewPane(true);
        setPhaseLabel(nextPhaseButton.getText().substring(6));
        view.setPause(false);
        View.PHASE currentPhase = view.getCurrentPhase();
        switch (currentPhase) {
            case START_UP:
                view.setCurrentPhase(View.PHASE.REINFORCEMENT);
                displayFromToCountriesInfoPane(false);
                break;
            case REINFORCEMENT:
                view.setCurrentPhase(View.PHASE.FORTIFICATION);
                resetFromToCountries();
                displayFromToCountriesInfoPane(true);
                showSkipFortificationPhaseButton(true); //TODO: refactor
                break;
            case FORTIFICATION:
                view.setCurrentPhase(View.PHASE.REINFORCEMENT);
                displayFromToCountriesInfoPane(false);
                break;
        }
    }


    /**
     * Show/Hide reinforcement phase button based on parameter
     * Called by View.startNextPhase()
     * @param show decides whether the reinforcement phase button need to be shown
     */
    private void showSkipFortificationPhaseButton(boolean show) { skipFortificationPhaseButton.setVisible(show); }


    /**
     * Allow player view to update the current player label text
     * Called by PlayerView
     * @return the current player label reference
     */
    public Label getCurrentPlayerLabel() { return currentPlayerLabel; }


    /**
     * Allow player view to update the armies in hand label text
     * Called by PlayerView
     * @return the armies in hand label reference
     */
    public Label getArmiesInHandLabel() { return armiesInHandLabel; }


    /**
     * During fortification phase, reset selected countries
     */
    private void clickedMap() { if (View.PHASE.FORTIFICATION == view.getCurrentPhase()) resetFromToCountries(); }


    /**
     * During fortification phase, receive a user entered value, check whether the selected countries info is valid,
     * then verify user entered value. However, condition "there is a path between these two countries that
     * is composed of countries that the user owns" has to be checked by Model
     * Called after user enter the number of armies moved value and press enter
     * @param enteredNumArmiesMoved user entered number of armies value
     */
    public void fortification(String enteredNumArmiesMoved) {
        int numArmiesMoved = 0;
        boolean enteredAnInteger = true;
        try {
            numArmiesMoved = Integer.parseInt(enteredNumArmiesMoved);
        } catch (Exception e) {
            enteredAnInteger = false;
        }
        if (null == fromToCountries[0] || null == fromToCountries[1]) {
            showInvalidMoveLabelInfo(true, "At least one country is not selected properly");
        } else if (fromToCountries[0].getName().equals(fromToCountries[1].getName())) {
            showInvalidMoveLabelInfo(true, "Select two different countries");
        } else if(!enteredAnInteger) {
            showInvalidMoveLabelInfo(true, "Enter an positive integer");
        } else {
            if (numArmiesMoved < 1) {
                showInvalidMoveLabelInfo(true, "Enter an positive integer");
            } else if (fromToCountries[0].getArmies() < numArmiesMoved) {
                showInvalidMoveLabelInfo(true, "Not enough armies to move");
            }  else {
                // TODO: need to be fixed
                // Since View does not check if there is a path between these two countries
                // that is composed of countries that he owns, so assume it's a invalid move
                showInvalidMoveLabelInfo(true, "There is no path between these two countries that is composed of countries that you owns");
                model.fortification(fromToCountries[0], fromToCountries[1], numArmiesMoved);
            }
        }
    }


    /**
     * During fortification phase, skip this phase if current user is not able to move armies
     * i.e. only has one country left or the user does not want to
     * Called by 'Skip' button on player view
     */
    private void skipFortificationPhase() {
        showNextPhaseButton("Enter Reinforcement Phase");
        model.nextPlayer();
    }


    /**
     * Clear all CountryViews and Lines that generated before
     * Called by View.showMenuStage()
     */
    private void resetMapComponents() {
        setPhaseLabel("Start Up Phase"); // TODO: refactor
        showPhaseLabel();
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
        View.PHASE currentPhase = view.getCurrentPhase();
        if (View.PHASE.START_UP == currentPhase || View.PHASE.REINFORCEMENT == currentPhase) {
            if (!view.getPause() && 0 != playerView.getArmiesInHands() && playerView.getName().equals(country.getOwner().getName()))  {
                model.allocateArmy(country);
            }
        } else if (View.PHASE.FORTIFICATION == currentPhase) {
            if (2 != fromToCountriesCounter && !country.getOwner().getName().equals(playerView.getName())) {
                showInvalidMoveLabelInfo(true, "Select your own country");
                return;
            }
            switch (fromToCountriesCounter++) {
                case 0:
                    fromToCountries[0] = country;
                    setFromCountryInfo(country);
                    break;
                case 1:
                    fromToCountries[1] = country;
                    setToCountryInfo(country);
                    break;
                case 2:
                    resetFromToCountries();
//                    arrow = null;
                    break;
            }
        }
    }


    /**
     * Called by PlayerView when current player has 0 army in hand, call
     * Prefer next phase info, i.e. reset button, hide/show panes
     */
    public void prepareNextPhase() {
        // TODO: should be trigger by button, because when armies in hand is 0, user could re-done,
//        System.out.println("Current phase is " + currentPhase + ", preparing next phase");
        View.PHASE currentPhase = view.getCurrentPhase();
        switch (currentPhase) {
            case START_UP:
                model.nextPlayer();
                break;
            case REINFORCEMENT:
                showNextPhaseButton("Enter Fortification Phase");
                break;
            case ATTACK:
                break;
            case FORTIFICATION:
                showNextPhaseButton("Enter Reinforcement Phase");
                model.reinforcement();
                showPlayerViewPane(false);
                break;
            default:
                break;
        }
    }


    /**
     * For the fortification usage, reset selected countries
     * Called by View.*() if user trying to reset the selected countries by clicking the map or another country
     */
    private void resetFromToCountries() {
        fromToCountries[0] = null;
        fromToCountries[1] = null;
        fromToCountriesCounter = 0;
        resetFromToCountriesInfo();
    }

    public void initCountryViews(int numOfCountries) {
        if (null == countryViews) {
            countryViews = new HashMap<>();
        } else {
            countryViews.clear();
        }
        for (int i = 1; i <= numOfCountries; ++i) {
            countryViews.put(i, createDefaultCountryView());
        }
        model.linkCountryObservers(countryViews);
    }

    public int getCountryViewsSize() { return countryViews.size(); }

    PlayerView createPlayerView() {
        playerView = new PlayerView(this);
        return playerView;
    }

    public void setNumOfCountries(int numOfCountries) {
        this.numOfCountries = numOfCountries;
    }

    void quitGame() { map.close(); }

    void showMapStage() { map.show(); }
}
