package view;

import controller.MapController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Map {

    private static Map instance;

    private AnchorPane mapRootPane;
    private MapController mapController;
    private Stage mapStage;

    private Map() {
        FXMLLoader mapFxmlLoader = new FXMLLoader(getClass().getResource("Map.fxml"));
        try {
            mapRootPane = mapFxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("Map.ctor(): " + exception.getMessage());
        }
        mapController = mapFxmlLoader.getController();
        mapStage = new Stage();
        mapStage.setTitle("Risk Game");
        mapStage.setScene(new Scene(mapRootPane,1000,700));
        mapStage.setResizable(false);
        mapStage.sizeToScene();
    }

    static Map getInstance(){
        if (null == instance) instance = new Map();
        return instance;
    }

    MapController getMapController() { return mapController; }

    // for drawMap and resetMap usage
    public AnchorPane getMapRootPane() { return mapRootPane; }

    public void show() { mapStage.show(); }

    public void hide() { mapStage.hide(); }

    public void close() { mapStage.close(); }


}
