package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Country;
import model.Model;

import java.io.File;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class View /*extends Application*/ implements Observer {

    public final double COUNTRY_WIDTH = 100;
    public final double COUNTRY_HEIGHT = 100;

    private Model model; // TODO: set it, where?

    private MenuController menuController;
    private MapController mapController;
    private Stage menuStage;
    private Stage mapStage;
    private boolean validFile;
    private AnchorPane mainMenuPane;
    private AnchorPane mapRootPane;
    private HashMap<Integer, CountryView> countryViews;
    private HashMap<Integer, LineView> lineViews;

    public void update(Observable obs, Object x) {
        // TODO: get obs new state info, i.e., new CountryView state, then ask Model to get newCountry info ???
        System.out.println("notify: new state is " + x);
    }

//    @Override
    public void start(/*Stage primaryStage*/) throws Exception {
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

        showMenuStage();
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

    public void closeMenuStage() throws Exception {
        mapStage.close();
        menuStage.close();
//        this.stop();
    }

    public void selectMap() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Risk Map File");
        File riskMapFile = fileChooser.showOpenDialog(menuStage);
        if (null != riskMapFile && riskMapFile.exists()) {
            // TODO: ask model to valid the file format，get info
//            validFile = model.checkMapFileValid(riskMapFile.getName())*/;
            // TODO: what should I do here?
            validFile = true;
            String additional_info = "This is additional info";
            menuController.setMapName(riskMapFile.getName(), validFile);
            menuController.setAdditionalMapInfo(additional_info);
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
    public CountryView createCountryView(double layoutX, double layoutY, Country country) {
        CountryView countryView = new CountryView();
        countryView.setPrefSize(COUNTRY_WIDTH, COUNTRY_HEIGHT);
        countryView.setLayoutX(layoutX);
        countryView.setLayoutY(layoutY);
        if (null != country) { countryView.addCountry(country); }
        countryViews.put(countryView.getCountryViewId(), countryView);
        mapRootPane.getChildren().add(countryView);
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
        return createCountryView(layoutX, layoutY, country);
    }


}
