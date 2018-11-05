package view;

import controller.MapController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Country;
import model.Model;

import java.io.IOException;

public class Map {

    private static Map instance;

    private View view;
    private Model model;
    private AnchorPane mapRootPane;
    private MapController mapController;
    private Stage mapStage;



    private Map() { }

    static Map getInstance(){
        if (null == instance) instance = new Map();
        return instance;
    }

    void init(View view, Model model) {
        this.view = view;
        this.model = model;
        FXMLLoader mapFxmlLoader = new FXMLLoader(getClass().getResource("Map.fxml"));
        try {
            mapRootPane = mapFxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("Map.ctor(): " + exception.getMessage());
        }
        mapController = mapFxmlLoader.getController();
        mapController.initialize(view, model); // TODO: pass this
        mapStage = new Stage();
        mapStage.setTitle("Risk Game");
        mapStage.setScene(new Scene(mapRootPane,1000,700));
        mapStage.setResizable(false);
        mapStage.sizeToScene();
    }

    void setPhaseLabel(String phase) { mapController.setPhaseLabel(phase); }

    void displayPlayerViewPane(boolean show) { mapController.showPlayerViewPane(show); }

    void displayFromToCountriesInfoPane(boolean show) { mapController.displayFromToCountriesInfoPane(show); }

    void showInvalidMoveLabelInfo(boolean show, String info) { mapController.showInvalidMoveLabelInfo(show, info); }

    void showPhaseLabel() { mapController.showPhaseLabel(); }

    MapController getMapController() { return mapController; }

    void setFromCountryInfo(Country country) { mapController.setFromCountryInfo(country); }

    void setToCountryInfo(Country country) { mapController.setToCountryInfo(country); }

    void showPlayerViewPane(boolean show) { mapController.showPlayerViewPane(show); }

    void showSkipFortificationPhaseButton(boolean show) { mapController.showSkipFortificationPhaseButton(show); }

    AnchorPane getMapRootPane() { return mapRootPane; }

    void hide() { mapStage.hide(); }

    void close() { mapStage.close(); }

    void show() { mapStage.show(); }
}
