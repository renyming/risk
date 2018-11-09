package com.risk.view;

import com.risk.controller.MapController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Load the map UI from fxml file, able to initialize, show, hide and close it
 */
public class Map {

    private static Map instance;

    private AnchorPane mapRootPane;
    private MapController mapController;
    private Stage mapStage;


    /**
     * Ctor, load UI, set default values
     */
    private Map() {
        final double GAME_BOARD_WIDTH = 1024;
        final double GAME_BOARD_HEIGHT = 768;

        FXMLLoader mapFxmlLoader = new FXMLLoader(getClass().getResource("/Map.fxml"));
        try {
            mapRootPane = mapFxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("Map.ctor(): " + exception.getMessage());
        }
        mapController = mapFxmlLoader.getController();
        mapStage = new Stage();
        mapStage.setTitle("Risk Game");
        mapStage.setScene(new Scene(mapRootPane, GAME_BOARD_WIDTH, GAME_BOARD_HEIGHT));
        mapStage.setResizable(false);
        mapStage.sizeToScene();
    }


    /**
     * Singleton standard getter method, get the instance
     * @return the instance
     */
    static Map getInstance(){
        if (null == instance) instance = new Map();
        return instance;
    }


    /**
     * Get the MapController reference
     * @return the MapController reference
     */
    MapController getMapController() { return mapController; }


    /**
     * Get the map root pane
     * For draw map and reset map usage
     * @return the mao root pane
     */
    public AnchorPane getMapRootPane() { return mapRootPane; }


    /**
     * Show map
     */
    public void show() { mapStage.show(); }


    /**
     * Hide map
     */
    public void hide() { mapStage.hide(); }


    /**
     * Close map
     */
    public void close() { mapStage.close(); }
}
