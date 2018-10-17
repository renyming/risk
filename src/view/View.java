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

    public final double COUNTRY_WIDTH = 70;
    public final double COUNTRY_HEIGHT = 70;
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
        mapController.initialize(this, COUNTRY_WIDTH, COUNTRY_HEIGHT);
        mapStage = new Stage();
        mapStage.setTitle("Risk Game");
        mapStage.setScene(new Scene(mapRootPane));

        pause = false;
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
                menuController.displaySelectedFileName(selectedFileName, false, "The invalid reasons");
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
                drawLines();
                break;
            case ROUND_ROBIN:
                System.out.println("round robin begins");
                model.reinforcement();
                showNextPhaseButton("Enter Reinforcement Phase");
                pause = true;
            case NEXT_PLAYER:
                showNextPhaseButton("Enter Reinforcement Phase");
        }
    }

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
        mapRootPane.getChildren().add(countryView.getCountryPane());
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

    public void fortification(Country fromCountry, Country toCountry, int numArmiesMove) {
        model.fortification(fromCountry, toCountry, numArmiesMove);
    }

    public void startNextPhase() {
        pause = false;
        mapController.hideNextPhaseButton();
        mapController.setPhaseLabel(mapController.getNextPhaseButtonTest().substring(6));
        switch (currentPhase) {
            case START_UP:
                currentPhase = PHASE.REINFORCEMENT;
                break;
            case REINFORCEMENT:
                currentPhase = PHASE.FORTIFICATION;
                break;
        }
    }

    public void showNextPhaseButton(String nextPhase) {
        mapController.showNextPhaseButton(nextPhase);
    }

    public boolean checkEdit() { return editEnable; }

    public void prepareNextPhase() {
        System.out.println(currentPhase);
        switch (currentPhase) {
            case START_UP:
                model.nextPlayer();
                break;
            case REINFORCEMENT:
                showNextPhaseButton("Enter Fortification Phase");
                mapController.showFortificationInfoPane();
                break;
            case ATTACK:
                break;
            case FORTIFICATION:
                showNextPhaseButton("Enter Reinforcement Phase");
                model.nextPlayer();
                break;
            default:
                break;
        }
    }

    public void drawLines() {
        for (int key : countryViews.keySet()) {
            Country countryA = countryViews.get(key).getCountry();
            for (Country countryB : countryA.getAdjCountries()) {
                Line line = new Line();
                line.setStartX(countryA.getX() + COUNTRY_WIDTH/2);
                line.setStartY(countryA.getY() + COUNTRY_HEIGHT/2);
                line.setEndX(countryB.getX() + COUNTRY_WIDTH/2);
                line.setEndY(countryB.getY() + COUNTRY_HEIGHT/2);
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(2);
                mapRootPane.getChildren().add(line);
            }
        }
    }

    public boolean getEditEnable() { return editEnable; }


    // TODO: self test purpose
    Country countries[] = new Country[2];
    int counter = 0;

    public void clickedCountry(Country country) {
        System.out.println("Clicked, phase is " + currentPhase);
        if (PHASE.START_UP == currentPhase || PHASE.REINFORCEMENT == currentPhase) {
            allocateArmy(country);
        } else if (PHASE.FORTIFICATION == currentPhase) {
            countries[counter] = country;
            counter++;
            System.out.println("add a country, counter is " + counter);
            if (2 == counter) {
                System.out.println("send fortification to model");
                model.fortification(countries[0], countries[1], 1);
            }
        }
    }
}
