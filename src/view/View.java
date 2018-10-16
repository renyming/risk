package view;

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

    public final double COUNTRY_WIDTH = 70;
    public final double COUNTRY_HEIGHT = 70;

    private Model model;

    private MenuController menuController;
    private MapController mapController;
    private Stage menuStage;
    private Stage mapStage;
    private String selectedFileName;
    private AnchorPane mainMenuPane;
    private AnchorPane mapRootPane;
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
     * Called when corresponding Model Observable subject calls its notifyObservers() method
     * @param obs Model Observable subject
     * @param x Message object, encapsulated STATE info
     */
    @Override
    public void update(Observable obs, Object x) {
        Message message = (Message) x;
//        Model model = (Model) obs;
        System.out.print("View.update(): new state is " + message.state + ", ");
        switch (message.state) {
            case LOAD_FILE:
                System.out.println("tried to load an invalid file, select it again");
                menuController.displaySelectedFileName(selectedFileName, false, "The invalid reasons");
                break;
            case CREATE_OBSERVERS:
                System.out.println("create Country Observers");
                menuController.displaySelectedFileName(selectedFileName, true, "Useful info here");

                for (int i = 0; i < (int) message.obj; ++i) {
                    // create default CountryView
                    createCountryView(0,0);
                }
                model.linkCountryObservers(); // TODO: should add countryViews as parameter
                break;
            case PLAYER_NUMBER:
                System.out.println("allow user to enter the number of players");
                menuController.setNumPlayers();
                break;
        }
    }

    public void showMenuStage() {
        mapStage.hide();
        menuStage.show();
        // TODO: clear drawing area from previous creation
        if (null != countryViews) countryViews.clear();
        if (null != lineViews) lineViews.clear();
    }

    public void showMapStage() {
        menuStage.hide();
        mapStage.show();
        countryViews = new HashMap<>();
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
                System.out.println("View.selectMap(): file path is " + riskMapFile.getPath());
            } catch (IOException e) {
                System.out.println("View.selectMap(): " + e.getMessage());
            }
        }
    }

    /**
     * Editing map phase / Loading existing RISK map file phase
     * Called by AnchorPane: mapRootPane if param country is null
     * Called by loadCountry method if param country is not null
     * Create a CountryView Observer object
     * @param layoutX cursor position X relative to the mapRootPane
     * @param layoutY cursor position Y relative to the mapRootPane
     * @return CountryView object, only useful for 'loading existing Risk map file phase'
     */
    public CountryView createCountryView(double layoutX, double layoutY) {
        CountryView countryView = new CountryView(this, layoutX, layoutY);
        countryViews.put(countryView.getId(), countryView);
        mapRootPane.getChildren().add(countryView.getCountryPane());
        return countryView;
    }

    /**
     * Loading existing RISK map file phase
     * Called by Country object
     * Create the corresponding CountryView Observer object and return it
     * @param country The Country object which needs a CountryView Observer
     */
    public CountryView loadCountry(Country country) {
        // TODO: following four lines code could be removed when there are
        // TODO: exist proper getter functions in Country object
        int layoutX = 0;
        int layoutY = 0;
//        layoutX = country.getLayoutX();
//        layoutY =  country.getLayoutY();
        return createCountryView(layoutX, layoutY);
    }

    public void removeCountryView(CountryView countryView) {
        mapRootPane.getChildren().remove(countryView.getCountryPane());
        countryViews.remove(countryView.getId());
    }
}
