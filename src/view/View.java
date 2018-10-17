package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {

    public enum PHASE {
        ENTER_NUM_PLAYER,
        START_UP,
        REINFORCEMENT,
        ATTACK,
        FORTIFICATION,
    }

    public final double COUNTRY_VIEW_WIDTH = 70;
    public final double COUNTRY_VIEW_HEIGHT = 70;
    public final double GAME_BOARD_WIDTH = 1200;
    public final double GAME_BOARD_HEIGHT = 700;

    private Model model;
    private MenuController menuController;
    private MapController mapController;
    private PlayerView playerView;

    private boolean editEnable;
    private boolean pause;
    private Stage menuStage;
    private Stage mapStage;
    private String selectedFileName;
    private PHASE currentPhase;
    private AnchorPane mainMenuPane;
    private AnchorPane mapRootPane;
    private HashMap<Integer, CountryView> countryViews;
    private Country fromToCountries[];
    private int fromToCountriesCounter;
//    private HashMap<Integer, Line> lineViews;



    /**
     * Initiate menu/map stages
     * @throws Exception Deal with the loading .fxml file issues
     */
    public View() throws Exception {
        FXMLLoader menuFxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        mainMenuPane = menuFxmlLoader.load();
        menuController = menuFxmlLoader.getController();
        menuController.initialize(this);
        menuStage = new Stage();
        menuStage.setTitle("Risk Game");
        menuStage.setScene(new Scene(mainMenuPane));

        FXMLLoader mapFxmlLoader = new FXMLLoader(getClass().getResource("Map.fxml"));
        mapRootPane = mapFxmlLoader.load();
        mapController = mapFxmlLoader.getController();
        mapController.initialize(this, COUNTRY_VIEW_WIDTH, COUNTRY_VIEW_HEIGHT);
        mapStage = new Stage();
        mapStage.setTitle("Risk Game");
        mapStage.setScene(new Scene(mapRootPane));

        pause = false;
        fromToCountries = new Country[2];
        fromToCountriesCounter = 0;
    }



    /**
     * Allow View to pass the selected file path back to the Model
     * Called by Model Observable subject
     * @param model Model Observable subject
     */
    public void setModel(Model model) { this.model = model; }



    /**
     * Observer update method, allow View to know what to do next
     * Called by corresponding Model Observable subject
     * @param obs Model Observable subject
     * @param x Message object, encapsulated STATE info
     */
    @Override
    public void update(Observable obs, Object x) {
        Message message = (Message) x;
        System.out.print("View.update(): new state is " + message.state + ", ");
        switch (message.state) {
            case LOAD_FILE: // TODO: Model should pass some invalid info back
                System.out.println("tried to load an invalid file, select it again");
                menuController.displaySelectedFileName(selectedFileName, false, (String) message.obj);
                break;
            case CREATE_OBSERVERS:
                System.out.println("create Country Observers");
                if (null == countryViews) {
                    countryViews = new HashMap<>();
                } else if (0 != countryViews.size()) {
                    countryViews.clear();
                }
                menuController.displaySelectedFileName(selectedFileName, true, "Useful info here");
                // TODO: click the 'Select Map more than once' triggers the bug
//                System.out.println((int) message.obj + " " + countryViews.size());
                int numOfCountries = (int) message.obj;
                for (int i = 1; i <= numOfCountries; ++i) {
                    countryViews.put(i, createDefaultCountryView(0, 0, "#ff0000", "#0000ff"));
//                    System.out.println((i + " " + countryViews.size()));
                }
//                System.out.println((int) message.obj + " " + countryViews.size());

                model.linkCountryObservers(countryViews);
                break;
            case PLAYER_NUMBER:
                System.out.println("allow user to enter the number of players");
                currentPhase = PHASE.ENTER_NUM_PLAYER;
                menuController.showNumPlayerTextField(countryViews.size());
                break;
            case INIT_ARMIES:
                System.out.println("start up Phase begins");
                currentPhase = PHASE.START_UP;
                mapController.setPhaseLabel("Start Up Phase");
                menuController.showStartGameButton();
                drawMap();
                break;
            case ROUND_ROBIN:
                System.out.println("round robin begins");
                showNextPhaseButton("Enter Reinforcement Phase");
//                currentPhase = PHASE.REINFORCEMENT;
                model.reinforcement();
                pause = true;
                break;
            case NEXT_PLAYER:
                showNextPhaseButton("Enter Reinforcement Phase");
                mapController.showFromToCountriesInfoPane(false);
                System.out.println("call model next player");
                model.nextPlayer();
                break;
        }
    }


    /**
     * Display Menu page to the user, user then can interact with i.e. click buttons
     */
    public void showMenuStage() {
        // TODO: reset all Label, Button etc.
        // TODO: clear drawing area from previous creation
        editEnable = false;
        mapStage.hide();
        menuStage.show();
        menuController.resetStartUpMenu();
        if (null != countryViews) countryViews.clear();
//        if (null != lineViews) lineViews.clear();
    }


    /**
     * Display
     */
    public void showMapStage() {
        menuStage.hide();
        mapStage.show();
//        lineViews = new HashMap<>();
    }

    public void closeMenuStage() {
        mapStage.close();
        menuStage.close();
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
                System.out.println("readFIle : " + riskMapFile.getPath());
            } catch (IOException e) {
                System.out.println("View.selectMap(): " + e.getMessage());
            }
        }
    }

    /**
     * Editing map currentPhase / Loading existing RISK map file currentPhase
     * Called by mapRootPane // TODO: may change later
     * Called by update() for CREATE_OBSERVERS state
     * Create a Default CountryView Observer object
     * @param layoutX countryPane left-top corner position X
     * @param layoutY countryPane left-top corner position Y
     * @return CountryView object, only useful for 'loading existing Risk map file currentPhase'
     */
    public CountryView createDefaultCountryView(double layoutX, double layoutY, String playerColor, String continentColor) {
        CountryView countryView = new CountryView(this, layoutX, layoutY, playerColor, continentColor);
        countryViews.put(countryView.getId(), countryView);
        return countryView;
    }

    public void removeCountryView(CountryView countryView) {
        if (editEnable) {
            countryView.getCountryPane().getChildren().clear();
            mapRootPane.getChildren().remove(countryView.getCountryPane());
            countryViews.remove(countryView.getId());
        }
    }

    public void initializePlayer(int playerNum) {
        playerView = new PlayerView(this, mapController);
        model.initiatePlayers(playerNum, playerView);
    }


    public void allocateArmy(Country country) {
        if (!pause && 0 != playerView.getArmiesInHands() && playerView.getName().equals(country.getOwner().getName()))  {
            model.allocateArmy(country);
        }
    }

    public void fortification(int numArmiesMove) {
        if (null != fromToCountries[0] && null != fromToCountries[1]) {
            model.fortification(fromToCountries[0], fromToCountries[1], numArmiesMove);
        } else {
            System.out.println("View.fortification(): at least one country is not set");
        }
    }

    public void startNextPhase() { // or start current phase
        pause = false;
        mapController.hideNextPhaseButton();
        mapController.setPhaseLabel(mapController.getNextPhaseButtonTest().substring(6));
        switch (currentPhase) {
            case START_UP:
                currentPhase = PHASE.REINFORCEMENT;
                mapController.showFromToCountriesInfoPane(false);
                break;
            case REINFORCEMENT: // current state is reinforcement,
                currentPhase = PHASE.FORTIFICATION;
                mapController.showFromToCountriesInfoPane(true);
                break;
            case FORTIFICATION:
                currentPhase = PHASE.REINFORCEMENT;
                mapController.showFromToCountriesInfoPane(false);
        }
    }

    public void showNextPhaseButton(String nextPhase) {
        mapController.showNextPhaseButton(nextPhase);
    }

    public boolean checkEdit() { return editEnable; }

    public void prepareNextPhase() {
        System.out.println("Current phase is " + currentPhase + ", prepare step");
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
                break;
            default:
                break;
        }
    }

    public void drawMap() {
        for (int key : countryViews.keySet()) {
            Country countryA = countryViews.get(key).getCountry();
            for (Country countryB : countryA.getAdjCountries()) {
                Line line = new Line();
                line.setStartX(countryA.getX() + COUNTRY_VIEW_WIDTH /2);
                line.setStartY(countryA.getY() + COUNTRY_VIEW_HEIGHT /2);
                line.setEndX(countryB.getX() + COUNTRY_VIEW_WIDTH /2);
                line.setEndY(countryB.getY() + COUNTRY_VIEW_HEIGHT /2);
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(2);
                mapRootPane.getChildren().add(line);
            }
        }

        for (int key : countryViews.keySet()) mapRootPane.getChildren().add(countryViews.get(key).getCountryPane());

    }

    public boolean getEditEnable() { return editEnable; }

    public void clickedCountry(Country country) {
        System.out.println("Clicked country " + country.getName() +"  "+ country.getOwner().getName() + ", phase is " + currentPhase);
        if (PHASE.START_UP == currentPhase || PHASE.REINFORCEMENT == currentPhase) {
            allocateArmy(country);
        } else if (PHASE.FORTIFICATION == currentPhase) {
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
                    fromToCountriesCounter = 0;
                    fromToCountries[0] = null;
                    fromToCountries[1] = null;
                    mapController.resetFromToCountriesInfo();
                    break;
            }
        }
    }

    public double getCountryViewWidth() { return COUNTRY_VIEW_WIDTH; }

    public double getCountryViewHeight() { return COUNTRY_VIEW_HEIGHT; }
}
