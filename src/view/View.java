package view;

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

    public final double COUNTRY_WIDTH = 70;
    public final double COUNTRY_HEIGHT = 70;
    public final double GAME_BOARD_WIDTH = 1200;
    public final double GAME_BOARD_HEIGHT = 700;

    private Model model;
    private MenuController menuController;
    private MapController mapController;
    private PlayerView playerView;

    private Stage menuStage;
    private Stage mapStage;
    private String selectedFileName;
    private AnchorPane mainMenuPane;
    private AnchorPane mapRootPane;
    private AnchorPane currentPlayerPane;
    private HashMap<Integer, CountryView> countryViews;
    private HashMap<Integer, LineView> lineViews;



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
                menuController.displaySelectedFileName(selectedFileName, true, "Useful info here");
                if (null == countryViews) countryViews = new HashMap<>();
                for (int i = 1; i <= (int) message.obj; ++i) {
                    countryViews.put(i, createDefaultCountryView(0, 0, "#ff0000", "#0000ff"));
                }
                model.linkCountryObservers(countryViews);
                break;
            case PLAYER_NUMBER:
                System.out.println("allow user to enter the number of players");
                menuController.showNumPlayerTextField(countryViews.size());
                break;
            case INIT_ARMIES:
                System.out.println("start up phase begins");
                mapController.getPhaseLabel().setText("Start Up Phase");
                menuController.showStartGameButton();
                break;
        }
    }

    public void showMenuStage() {
        mapStage.hide();
        menuStage.show();
        menuController.resetStartUpMenu();
        // TODO: clear drawing area from previous creation
        if (null != countryViews) countryViews.clear();
        if (null != lineViews) lineViews.clear();
    }

    public void showMapStage() {
        menuStage.hide();
        mapStage.show();
        if (null == countryViews) countryViews = new HashMap<>();
        lineViews = new HashMap<>();
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
     * Editing map phase / Loading existing RISK map file phase
     * Called by mapRootPane // TODO: may change later
     * Called by update() for CREATE_OBSERVERS state
     * Create a Default CountryView Observer object
     * @param layoutX countryPane left-top corner position X
     * @param layoutY countryPane left-top corner position Y
     * @return CountryView object, only useful for 'loading existing Risk map file phase'
     */
    public CountryView createDefaultCountryView(double layoutX, double layoutY, String playerColor, String continentColor) {
        CountryView countryView = new CountryView(this, layoutX, layoutY, playerColor, continentColor);
        countryViews.put(countryView.getId(), countryView);
        mapRootPane.getChildren().add(countryView.getCountryPane());
        return countryView;
    }

    public void removeCountryView(CountryView countryView) {
        countryViews.remove(countryView.getId());
        mapRootPane.getChildren().remove(countryView.getCountryPane());
    }

    public void initializePlayer(int playerNum) {
        playerView = new PlayerView(this, mapController);
        model.initiatePlayers(playerNum, playerView);
    }
}
