package com.risk.view;

import com.risk.controller.CardController;
import com.risk.controller.MapController;
import com.risk.controller.MenuController;
import com.risk.model.Model;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * View, support user interaction, do partial user input validation
 * Corresponding Observable subject is Model
 */
public class View {

//    private final double COUNTRY_VIEW_HEIGHT = 60;
//    private final double COUNTRY_VIEW_WIDTH = 60;
//    private final double MENU_WIDTH = 500;
//    private final double MENU_HEIGHT = 300;

    private Stage mapEditorStage;
    private Menu menu;
    private Map map;
    private CardView card;

    // TODO: remove later?
    private MenuController menuController;


    /**
     * Initiate menu/map stages with corresponding .fxml file, set some default variables
     */
    public View() {
        menu = Menu.getInstance();
        map = Map.getInstance();
        card = CardView.getInstance();
    }


    /**
     * Allow View to pass the selected file path back to the Model
     * Called by Model Observable subject
     * @param model Model Observable subject
     */
    public void setModel(Model model) {
        menuController = menu.getMenuController();
        MapController mapController = map.getMapController();
        CardController cardController = card.getCardController();

        menuController.init(model, this, menu, mapController);
        mapController.init(model, map, menuController, card, cardController);
        cardController.init(model, card, mapController);

    }


    public void display() { menuController.switchToStartGameMenu(); }


    /**
     * Display map editor, user than can create a RISK map
     * Called by Menu
     */
    public void openMapEditor() {
        if (null != mapEditorStage) mapEditorStage.close();
        mapEditorStage = new Stage();
        try {
            com.risk.mapeditor.View mapView = new com.risk.mapeditor.View();
            mapView.setMenuView(this);
            mapEditorStage.setScene(new Scene(mapView,1080,746));
//            mapEditorStage.setResizable(false);
        } catch (Exception e) {
            System.out.println("View.openMapEditor(): " + e.getMessage());
        }
        mapEditorStage.setTitle("Map Editor");
        menu.hide();
        mapEditorStage.show();
    }


    /**
     * Close the map editor, show beginning of the menu page
     * Called when 'quitGame' is click through the map editor menu
     */
    public void quitToMenu() { // TODO: rename, quitToMenu
        mapEditorStage.hide();
        menu.show();
    }
}