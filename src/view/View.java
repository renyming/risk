package view;

import controller.MapController;
import model.Country;
import model.Model;
import common.Message;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;


/**
 * View, support user interaction, do partial user input validation
 * Corresponding Observable subject is Model
 */
public class View implements Observer {


    /**
     * Determine the current or next phase
     */
    public enum PHASE {ENTER_NUM_PLAYER, START_UP, REINFORCEMENT, ATTACK, FORTIFICATION}

//    private final double COUNTRY_VIEW_HEIGHT = 60;
//    private final double COUNTRY_VIEW_WIDTH = 60;
//    private final double MENU_WIDTH = 500;
//    private final double MENU_HEIGHT = 300;
//    private final double GAME_BOARD_WIDTH = 1000;
//    private final double GAME_BOARD_HEIGHT = 700;

    private int fromToCountriesCounter;
    private PlayerView playerView;
    private Stage mapEditorStage;
    private PHASE currentPhase;
    private boolean pause;
    private Model model;
    private Menu menu;
    private Map map;

    private HashMap<Integer, CountryView> countryViews;
    private Country fromToCountries[];
    private HashSet<Line> lines;


    /**
     * Initiate menu/map stages with corresponding .fxml file, set some default variables
     */
    public View() {

        menu = Menu.getInstance();
        map = Map.getInstance();

        pause = false;
        fromToCountriesCounter = 0;
        fromToCountries = new Country[2];
    }


    /**
     * Allow View to pass the selected file path back to the Model
     * Called by Model Observable subject
     * @param model Model Observable subject
     */
    public void init(Model model) {
        this.model = model;
        menu.init(model, this);
        map.init(this, model);
    }


    /**
     * Observer update method, allow View to know what to do next
     * Notified by corresponding Model Observable subject when game state changes
     * @param obs Model Observable subject
     * @param x Message object, encapsulated STATE info
     */
    @Override
    public void update(Observable obs, Object x) {
        Message message = (Message) x;
//        System.out.println("View.update(): new state is " + message.state + ", ");
        switch (message.state) {
            case LOAD_FILE:
                menu.displaySelectedFileName(false, (String) message.obj);
                break;
            case CREATE_OBSERVERS:
                if (null == countryViews) {
                    countryViews = new HashMap<>();
                } else {
                    countryViews.clear();
                }
                menu.displaySelectedFileName(true, "Useful info here");
                int numOfCountries = (int) message.obj;
                for (int i = 1; i <= numOfCountries; ++i) {
                    countryViews.put(i, createDefaultCountryView());
                }
                model.linkCountryObservers(countryViews);
                break;
            case PLAYER_NUMBER:
                currentPhase = PHASE.ENTER_NUM_PLAYER;
                menu.showNumPlayerTextField(countryViews.size());
                break;
            case INIT_ARMIES:
                currentPhase = PHASE.START_UP;
                map.setPhaseLabel("Start Up Phase");
                menu.showStartGameButton();
                break;
            case ROUND_ROBIN:
                showNextPhaseButton("Enter Reinforcement Phase");
                map.displayPlayerViewPane(false);
                pause = true;
                model.reinforcement();
                break;
            case NEXT_PLAYER:
                showNextPhaseButton("Enter Reinforcement Phase");
                map.displayFromToCountriesInfoPane(false);
                map.displayPlayerViewPane(false);
                map.showInvalidMoveLabelInfo(false, "");
                model.nextPlayer();
                break;
        }
    }


    /**
     * Display the beginning of page of the menu, user then can interact with i.e. click buttons
     * Called when 'quitGame' button is clicked from the on-going game, then clear all previous work
     */
    public void showMenuStage() {
        resetMapComponents();
        map.displayFromToCountriesInfoPane(false);
        map.hide();
        menu.switchToStartGameMenu();
    }


    /**
     * Clear all CountryViews and Lines that generated before
     * Called by View.showMenuStage()
     */
    private void resetMapComponents() {
        map.setPhaseLabel("Start Up Phase");
        map.showPhaseLabel();
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
     * Display map editor, user than can create a RISK map
     * Called by Menu
     */
    void openMapEditor() {
        if (null != mapEditorStage) mapEditorStage.close();
        mapEditorStage = new Stage();
        try {
            mapeditor.View mapView = new mapeditor.View();
            mapView.setMenuView(this);
            mapEditorStage.setScene(new Scene(mapView,1080,746));
            mapEditorStage.setResizable(false);
        } catch (Exception e) {
            System.out.println("View.openMapEditor(): " + e.getMessage());
        }
        mapEditorStage.setTitle("Map Editor");
        menu.hide();
        mapEditorStage.show();
    }


    /**
     * Quit the game
     * Called when 'Quit' button is clicked on the menu, and then user click 'Yes' button to confirm
     */
    void quitGame() { map.close(); }


    /**
     * After loading a valid file, create a default CountryView Observer object, and then return it
     * Called by View.update() at state CREATE_OBSERVERS
     * Allow Model to bind it with Country Observable object later
     * @return CountryView Observer object
     */
    private CountryView createDefaultCountryView() { return new CountryView(this); }


    /**
     * Hide the menu, draw every single map component, then display the map
     * Called by Menu
     */
    void showMapStage() {
        menu.hide();
        drawMap();
        map.show();
    }


    /**
     * Draw each CountryView and Line into mapRootPane
     * Called by View.showMapStage()
     */
    private void drawMap() {
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
     * Called when user clicked a country
     * For reinforcement phase: add clicked counter
     * For fortification phase: set selected countries
     * @param country the country which user clicked
     */
    void clickedCountry(Country country) {
        if (PHASE.START_UP == currentPhase || PHASE.REINFORCEMENT == currentPhase) {
            allocateArmy(country);
        } else if (PHASE.FORTIFICATION == currentPhase) {
            if (2 != fromToCountriesCounter && !country.getOwner().getName().equals(playerView.getName())) {
                map.showInvalidMoveLabelInfo(true, "Select your own country");
                return;
            }
            switch (fromToCountriesCounter++) {
                case 0:
                    fromToCountries[0] = country;
                    map.setFromCountryInfo(country);
                    break;
                case 1:
                    fromToCountries[1] = country;
                    map.setToCountryInfo(country);
                    break;
                case 2:
                    resetFromToCountries();
//                    arrow = null;
                    break;
            }
        }
    }


    /**
     * During fortification phase, reset selected countries
     */
    public void clickedMap() { if (PHASE.FORTIFICATION == currentPhase) resetFromToCountries(); }


    /**
     * Called by PlayerView when current player has 0 army in hand, call
     * Prefer next phase info, i.e. reset button, hide/show panes
     */
    void prepareNextPhase() {
        // TODO: should be trigger by button, because when armies in hand is 0, user could re-done,
//        System.out.println("Current phase is " + currentPhase + ", preparing next phase");
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
                map.showPlayerViewPane(false);
                break;
            default:
                break;
        }
    }


    /**
     * Resume the paused phase, active corresponding listeners
     * Called when user click the 'Enter xxx phase' button
     * Display or hide the relative info panes/buttons
     */
    public void startNextPhase() {
        pause = false;
        switch (currentPhase) {
            case START_UP:
                currentPhase = PHASE.REINFORCEMENT;
                map.displayFromToCountriesInfoPane(false);
                break;
            case REINFORCEMENT:
                currentPhase = PHASE.FORTIFICATION;
                resetFromToCountries();
                map.displayFromToCountriesInfoPane(true);
                map.showSkipFortificationPhaseButton(true); //TODO: refactor
                break;
            case FORTIFICATION:
                currentPhase = PHASE.REINFORCEMENT;
                map.displayFromToCountriesInfoPane(false);
                break;
        }
    }


    /**
     * Reset the 'Enter xxx Phase' button name and display it
     * Called by View.*()
     * @param nextPhase represents the display text of the 'Enter xxx Phase' button
     */
    private void showNextPhaseButton(String nextPhase) { map.getMapController().showNextPhaseButton(nextPhase); }


    /**
     * During start up and reinforcement phase, tell model that user clicked a valid country to allocate army
     * Called by View.clickedCountry() after the click event is verified and distributed
     * @param country the Country which current armies should be added by one
     */
    public void allocateArmy(Country country) {
        if (!pause && 0 != playerView.getArmiesInHands() && playerView.getName().equals(country.getOwner().getName()))  {
            model.allocateArmy(country);
        }
    }


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
        MapController mapController = map.getMapController();
        if (null == fromToCountries[0] || null == fromToCountries[1]) {
            mapController.showInvalidMoveLabelInfo(true, "At least one country is not selected properly");
        } else if (fromToCountries[0].getName().equals(fromToCountries[1].getName())) {
            mapController.showInvalidMoveLabelInfo(true, "Select two different countries");
        } else if(!enteredAnInteger) {
            mapController.showInvalidMoveLabelInfo(true, "Enter an positive integer");
        } else {
            if (numArmiesMoved < 1) {
                mapController.showInvalidMoveLabelInfo(true, "Enter an positive integer");
            } else if (fromToCountries[0].getArmies() < numArmiesMoved) {
                mapController.showInvalidMoveLabelInfo(true, "Not enough armies to move");
            }  else {
                // TODO: need to be fixed
                // Since View does not check if there is a path between these two countries
                // that is composed of countries that he owns, so assume it's a invalid move
                mapController.showInvalidMoveLabelInfo(true, "There is no path between these two countries that is composed of countries that you owns");
                model.fortification(fromToCountries[0], fromToCountries[1], numArmiesMoved);
            }
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
        map.getMapController().resetFromToCountriesInfo();
    }


    /**
     * During fortification phase, skip this phase if current user is not able to move armies
     * i.e. only has one country left or the user does not want to
     * Called by 'Skip' button on player view
     */
    public void skipFortificationPhase() {
        showNextPhaseButton("Enter Reinforcement Phase");
        model.nextPlayer();
    }


    /**
     * Close the map editor, show beginning of the menu page
     * Called when 'quitGame' is click through the map editor menu
     */
    public void quitToMenu() { // TODO: rename, quitToMenu
        mapEditorStage.hide();
        menu.show();
    }

    MapController getMapController() { return map.getMapController(); }


    // TODO: removed after refactor

    void setPlayerView(PlayerView playerView) {
        this.playerView = playerView;
    }
}