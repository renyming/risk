package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import model.Country;
import model.Model;
import common.Message;

import javafx.fxml.FXMLLoader;
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

    private final double COUNTRY_VIEW_WIDTH = 60;
    private final double COUNTRY_VIEW_HEIGHT = 60;
    private final double GAME_BOARD_WIDTH = 1200;
    private final double GAME_BOARD_HEIGHT = 700;

    private MenuController menuController;
    private MapController mapController;
    private int fromToCountriesCounter;
    private AnchorPane mainMenuPane;
    private String selectedFileName;
    private AnchorPane mapRootPane;
    private PlayerView playerView;
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
     * Initiate menu/map stages, and some default variables' values
     * @throws Exception Deal with the loading .fxml file issues
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

            FXMLLoader mapFxmlLoader = new FXMLLoader(getClass().getResource("Map.fxml"));
            mapRootPane = mapFxmlLoader.load();
            mapController = mapFxmlLoader.getController();
            mapController.initialize(this);
            mapStage = new Stage();
            mapStage.setTitle("Risk Game");
            mapStage.setScene(new Scene(mapRootPane,1000,700));

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
     * Called by corresponding Model Observable subject
     * @param obs Model Observable subject
     * @param x Message object, encapsulated STATE info
     */
    @Override
    public void update(Observable obs, Object x) {
        Message message = (Message) x;
        System.out.println("View.update(): new state is " + message.state + ", ");
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
                    countryViews.put(i, createDefaultCountryView(0, 0, "#ff0000", "#0000ff"));
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
     */
    public void showMenuStage() {
        mapStage.hide();
        menuStage.show();
        menuController.resetStartUpMenu();
        menuController.switchToStartGameMenu();
        clearMapComponents();
    }



    /**
     * Draw the loaded map components, then display it
     */
    public void showMapStage() {
        menuStage.hide();
        drawMap();
        mapStage.show();
    }



    /**
     * Quit the game
     */
    public void closeMenuStage() {
        mapStage.close();
        menuStage.close();
    }



    /**
     * Display map editor, user than can create a RISK map
     */
    public void openMapEditor() {
        // TODO: start map editor
    }



    /**
     * Select a RISK map file, then ask Model to validate
     * Called by 'Select Map' Button when user clicks it
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
     * Loading existing RISK map file phase
     * Called by update() for CREATE_OBSERVERS state
     * Create a Default CountryView Observer object
     * @param layoutX countryPane left-top corner position X
     * @param layoutY countryPane left-top corner position Y
     * @return CountryView object, only useful for 'loading existing Risk map file currentPhase'
     */
    public CountryView createDefaultCountryView(double layoutX, double layoutY, String playerColor, String continentColor) {
        CountryView countryView = new CountryView(this, layoutX, layoutY, playerColor, continentColor);
        return countryView;
    }



    /**
     * Clear all CountryViews that generated before, and remove all countryPane from mapRootPane
     */
    public void clearMapComponents() {
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
     * Receive number of player which user entered correctly, create playerView, pass them to model
     * @param playerNum
     */
    public void initializePlayer(int playerNum) {
        playerView = new PlayerView(this, mapController);
        model.initiatePlayers(playerNum, playerView);
    }



    /**
     * During start up and reinforcement phase, tell model user clicked a country to allocate army
     * @param country the Country which current armies should be added by one
     */
    public void allocateArmy(Country country) {
        if (!pause && 0 != playerView.getArmiesInHands() && playerView.getName().equals(country.getOwner().getName()))  {
            model.allocateArmy(country);
        }
    }



    /**
     * During fortification phase, tell model user just moved valid number of armies between
     * two of his/her owned country with valid armies number
     * @param enteredNumArmiesMoved number of armies that user tried to move
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

    public void skipFortificationPhase() {
        showNextPhaseButton("Enter Reinforcement Phase");
        mapController.showFromToCountriesInfoPane(false);
        mapController.showInvalidMoveLabelInfo(false, "");
        model.nextPlayer();
    }

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
            case REINFORCEMENT: // current state is reinforcement,
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

    public void showNextPhaseButton(String nextPhase) { mapController.showNextPhaseButton(nextPhase); }

    public void prepareNextPhase() {
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

    public void drawMap() {
        for (int key : countryViews.keySet()) mapRootPane.getChildren().add(countryViews.get(key).getCountryPane());

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
    }

    public void clickedCountry(Country country) {
        if (PHASE.START_UP == currentPhase || PHASE.REINFORCEMENT == currentPhase) {
            allocateArmy(country);
        } else if (PHASE.FORTIFICATION == currentPhase) {
            if (2 != fromToCountriesCounter && country.getOwner().getName() != playerView.getName()) {
                mapController.showInvalidMoveLabelInfo(true, "Select your own country");
                return;
            }
            switch (fromToCountriesCounter++) {
                case 0:
                    fromToCountries[0] = country;
                    mapController.setFromCountryInfo(country);
                    break;
                case 1:
//                    if (fromToCountries[0].getName().equals(country.getName())) {
//                        mapController.showInvalidMoveLabelInfo(true, "Select two different countries");
//                    }
                    fromToCountries[1] = country;
                    mapController.setToCountryInfo(country);
                    break;
                case 2:
                    resetFromToCountries();
//                    arrow = null;
                    break;
            }
//            ++fromToCountriesCounter;
        }
    }

    public void clickedMap() { if (PHASE.FORTIFICATION == currentPhase) resetFromToCountries(); }

    public void resetFromToCountries() {
        fromToCountries[0] = null;
        fromToCountries[1] = null;
        fromToCountriesCounter = 0;
         mapController.resetFromToCountriesInfo();
        mapController.showInvalidMoveLabelInfo(false, "");
    }

    public double getCountryViewWidth() { return COUNTRY_VIEW_WIDTH; }

    public double getCountryViewHeight() { return COUNTRY_VIEW_HEIGHT; }




    // TODO: for self test purpose need to be removed later

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
