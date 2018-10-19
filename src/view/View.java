package view;

import model.Country;
import model.Model;
import common.Message;

import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {

    public enum PHASE {ENTER_NUM_PLAYER, START_UP, REINFORCEMENT, ATTACK, FORTIFICATION}

    private final double COUNTRY_VIEW_HEIGHT = 60;
    private final double COUNTRY_VIEW_WIDTH = 60;
    private final double GAME_BOARD_HEIGHT = 700;
    private final double GAME_BOARD_WIDTH = 1200;

    private MenuController menuController;
    private MapController mapController;
    private int fromToCountriesCounter;
    private AnchorPane mainMenuPane;
    private String selectedFileName;
    private AnchorPane mapRootPane;
    private PlayerView playerView;
    private Stage mapEditorStage;
    private PHASE currentPhase;
    private Stage menuStage;
    private Stage mapStage;
    private boolean pause;
    private Model model;
//    private Arrow arrow;

    private HashMap<Integer, CountryView> countryViews;
    private Country fromToCountries[];
    private HashSet<Line> lines;



    /**
     * Initiate menu/map stages with corresponding .fxml file, set some default variables
     */
    public View() {
        try {
            FXMLLoader menuFxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            mainMenuPane = menuFxmlLoader.load();
            menuController = menuFxmlLoader.getController();
            menuController.initialize(this);
            menuStage = new Stage();
            menuStage.setTitle("Risk Game");
            menuStage.setScene(new Scene(mainMenuPane,500,300));
            menuStage.setResizable(false);


            FXMLLoader mapFxmlLoader = new FXMLLoader(getClass().getResource("Map.fxml"));
            mapRootPane = mapFxmlLoader.load();
            mapController = mapFxmlLoader.getController();
            mapController.initialize(this);
            mapStage = new Stage();
            mapStage.setTitle("Risk Game");
            mapStage.setScene(new Scene(mapRootPane,1000,700));
            mapStage.setResizable(false);

            pause = false;
            fromToCountriesCounter = 0;
            fromToCountries = new Country[2];

//            arrow = new Arrow();
//            drawArrow(); // TODO: draw it in attack and fortification phase
        } catch (Exception e) {
            System.out.println("View.ctor(): " + e.getMessage());
        }
    }



    /**
     * Allow View to pass the selected file path back to the Model
     * Called by Model Observable subject
     * @param model Model Observable subject
     */
    public void initialize(Model model) { this.model = model; }



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
                menuController.displaySelectedFileName(selectedFileName, false, (String) message.obj);
                break;
            case CREATE_OBSERVERS:
                if (null == countryViews) {
                    countryViews = new HashMap<>();
                } else {
                    countryViews.clear();
                }
                menuController.displaySelectedFileName(selectedFileName, true, "Useful info here");
                int numOfCountries = (int) message.obj;
                for (int i = 1; i <= numOfCountries; ++i) {
                    countryViews.put(i, createDefaultCountryView());
                }
                model.linkCountryObservers(countryViews);
                break;
            case PLAYER_NUMBER:
                currentPhase = PHASE.ENTER_NUM_PLAYER;
                menuController.showNumPlayerTextField(countryViews.size());
                break;
            case INIT_ARMIES:
                currentPhase = PHASE.START_UP;
                mapController.setPhaseLabel("Start Up Phase");
                menuController.showStartGameButton();
                break;
            case ROUND_ROBIN:
                showNextPhaseButton("Enter Reinforcement Phase");
                mapController.showPlayerViewPane(false);
                pause = true;
                model.reinforcement();
                break;
            case NEXT_PLAYER:
                showNextPhaseButton("Enter Reinforcement Phase");
                mapController.showFromToCountriesInfoPane(false);
                mapController.showPlayerViewPane(false);
                mapController.showInvalidMoveLabelInfo(false, "");
                model.nextPlayer();
                break;
        }
    }



    /**
     * Display the beginning of page of the menu, user then can interact with i.e. click buttons
     * Called when 'quit' button is clicked from the on-going game, then clear all previous work
     */
    public void showMenuStage() {
        mapStage.hide();
        menuStage.show();
        menuController.resetStartUpMenu();
        menuController.switchToStartGameMenu();
        clearMapComponents();
    }



    /**
     * Hide the menu, draw every single map component, then display the map
     */
    public void showMapStage() {
        menuStage.hide();
        drawMap();
        mapStage.show();
    }


    /**
     * Close the map editor, show beginning of the menu page
     * Called when 'quit' is click through the map editor menu
     */
    public void closeMapStage() {
        mapEditorStage.hide();
        menuStage.show();
    }



    /**
     * Quit the game
     * Called when 'Quit' button is clicked on the menu, and then user click 'Yes' button to confirm
     */
    public void closeMenuStage() {
        mapStage.close();
        menuStage.close();
    }



    /**
     * Display map editor, user than can create a RISK map
     * Called when user click the 'Map Editor' on the main page
     */
    public void openMapEditor() {
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
        menuStage.hide();
        mapEditorStage.show();
    }



    /**
     * Select a RISK map file, then ask Model to validate
     * Called when user click 'Select Map' button on menu
     */
    public void selectMap() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Risk Map File");
        File riskMapFile = fileChooser.showOpenDialog(menuStage);
        if (null != riskMapFile && riskMapFile.exists()) {
            try {
                selectedFileName = riskMapFile.getName();
                model.readFile(riskMapFile.getPath());
            } catch (IOException e) {
                System.out.println("View.selectMap(): " + e.getMessage());
            }
        }
    }



    /**
     * After loading a valid file, create a default CountryView Observer object, and then return it
     * Called by View.update() at state CREATE_OBSERVERS
     * Allow Model to bind it with Country Observable object later
     * @return CountryView Observer object
     */
    private CountryView createDefaultCountryView() {
        return new CountryView(this);
    }



    /**
     * After loading a valid file, receive number of player which user entered correctly
     * create playerView, pass it to model
     * Called when user entered a valid total number of players on the menu text field, then press enter
     * @param playerNum valid total number of players for the game with specific loaded RISK map file
     */
    public void initializePlayer(int playerNum) {
        playerView = new PlayerView(this, mapController);
        model.initiatePlayers(playerNum, playerView);
    }



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
     * During fortification phase, skip this phase if current user is not able to move armies
     * i.e. only has one country left or the user does not want to
     * Called by 'Skip' button on player view
     */
    public void skipFortificationPhase() {
        showNextPhaseButton("Enter Reinforcement Phase");
        mapController.showFromToCountriesInfoPane(false);
        mapController.showInvalidMoveLabelInfo(false, "");
        model.nextPlayer();
    }



    /**
     * Called by PlayerView when current player has 0 army in hand, call
     * Prefer next phase info, i.e. reset button, hide/show panes
     */
    public void prepareNextPhase() {
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
                mapController.resetFromToCountriesInfo();
                mapController.showPlayerViewPane(false);
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
        mapController.showPhaseLabel();
        mapController.hideNextPhaseButton();
        mapController.showPlayerViewPane(true);
        mapController.setPhaseLabel(mapController.getNextPhaseButtonTest().substring(6));
        switch (currentPhase) {
            case START_UP:
                currentPhase = PHASE.REINFORCEMENT;
                mapController.showFromToCountriesInfoPane(false);
                break;
            case REINFORCEMENT:
                currentPhase = PHASE.FORTIFICATION;
                mapController.showFromToCountriesInfoPane(true);
                mapController.showReinforcementPhaseButton(true);
                break;
            case FORTIFICATION:
                currentPhase = PHASE.REINFORCEMENT;
                mapController.showFromToCountriesInfoPane(false);
                break;
        }
    }



    /**
     * Reset the 'Enter xxx Phase' button name and display it
     * Called by View.*()
     * @param nextPhase represents the display text of the 'Enter xxx Phase' button
     */
    private void showNextPhaseButton(String nextPhase) { mapController.showNextPhaseButton(nextPhase); }



    /**
     * Draw each CountryView and Line into mapRootPane
     * Called by View.showMapStage()
     */
    private void drawMap() {
        // TODO: use efficient way to draw lines, avoid duplicate
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
     * Clear all CountryViews and Lines that generated before
     * Called by View.showMenuStage()
     */
    private void clearMapComponents() {
        if (null != countryViews) {
            for (int key : countryViews.keySet()) {
                AnchorPane countryPane = countryViews.get(key).getCountryPane();
                countryPane.getChildren().clear();
                mapRootPane.getChildren().remove(countryPane);
            }
            countryViews.clear();
        }
        if (null != lines) {
            for (Line line : lines) {
                mapRootPane.getChildren().remove(line);
            }
            lines.clear();
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
        mapController.resetFromToCountriesInfo();
        mapController.showInvalidMoveLabelInfo(false, "");
    }



    /**
     * Called when user clicked a country
     * For reinforcement phase: add clicked counter
     * For fortification phase: set selected countries
     * @param country the country which user clicked
     */
    public void clickedCountry(Country country) {
        if (PHASE.START_UP == currentPhase || PHASE.REINFORCEMENT == currentPhase) {
            allocateArmy(country);
        } else if (PHASE.FORTIFICATION == currentPhase) {
            if (2 != fromToCountriesCounter && !country.getOwner().getName().equals(playerView.getName())) {
                mapController.showInvalidMoveLabelInfo(true, "Select your own country");
                return;
            }
            switch (fromToCountriesCounter++) {
                case 0:
                    fromToCountries[0] = country;
                    mapController.setFromCountryInfo(country);
                    break;
                case 1:
                    fromToCountries[1] = country;
                    mapController.setToCountryInfo(country);
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

    // TODO: for self test purpose, dynamically drag and draw an arrow, need to be removed later

    //    public void pressedCountry(Country country) {
//        removeArrow();
//        System.out.println("pressed country " + country.getName());
//        arrow.setStart(country.getX() + COUNTRY_VIEW_WIDTH/2, country.getY() + COUNTRY_VIEW_HEIGHT/2);
//        drawArrow();
//    }

//    public void enteredCountry(Country country) {
////        System.out.println("entered country " + country.getName());
////        arrow.setEnd(country.getX() + COUNTRY_VIEW_WIDTH/2, country.getY() + COUNTRY_VIEW_HEIGHT/2);
////        arrow.update();
//    }

//    public void releasedCountry(Country country) {
//        arrow.setEnd(country.getX() + COUNTRY_VIEW_WIDTH/2, country.getY() + COUNTRY_VIEW_HEIGHT/2);
//    }

//    public void pressedMap(double cursorX, double cursorY) {
////        System.out.println("pressed " + cursorX + " " + cursorY);
//        arrow.setStart(cursorX, cursorY);
//        arrow.setEnd(cursorX, cursorY);
//    }
//
//    public void draggedMap(double cursorX, double cursorY) {
////        System.out.println("dragging " + cursorX + " " + cursorY);
//        arrow.setEnd(cursorX, cursorY);
//        arrow.update();
//    }
//
//    public void releasedMap(double cursorX, double cursorY) {
////        System.out.println("released " + cursorX + " " + cursorY);
//        arrow.setEnd(cursorX, cursorY);
//        arrow.update();
//    }
//
//    public void drawArrow() { mapRootPane.getChildren().add(arrow); }
//
//    public void removeArrow() { mapRootPane.getChildren().remove(arrow); }
}
