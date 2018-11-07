package view;

import controller.MapController;
import controller.MenuController;
import model.Model;
import common.Message;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Observable;


/**
 * View, support user interaction, do partial user input validation
 * Corresponding Observable subject is Model
 */
public class View {

    public enum PHASE {ENTER_NUM_PLAYER, START_UP, REINFORCEMENT, ATTACK, FORTIFICATION}

//    private final double COUNTRY_VIEW_HEIGHT = 60;
//    private final double COUNTRY_VIEW_WIDTH = 60;
//    private final double MENU_WIDTH = 500;
//    private final double MENU_HEIGHT = 300;

    private Stage mapEditorStage;
    private Menu menu;
    private Map map;

    // TODO: remove later?
    private MenuController menuController;
    private PHASE currentPhase;
    private boolean pause;


    /**
     * Initiate menu/map stages with corresponding .fxml file, set some default variables
     */
    public View() {
        menu = Menu.getInstance();
        map = Map.getInstance();
        pause = false;
    }


    /**
     * Allow View to pass the selected file path back to the Model
     * Called by Model Observable subject
     * @param model Model Observable subject
     */
    public void setModel(Model model) {
        menuController = menu.getMenuController();
        MapController mapController = map.getMapController();

        menuController.init(model, this, menu, mapController);
        mapController.init(model, map, menuController);
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
            mapeditor.View mapView = new mapeditor.View();
            mapView.setMenuView(this);
            mapEditorStage.setScene(new Scene(mapView,1080,746));
            mapEditorStage.setResizable(false);
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

    public PHASE getCurrentPhase() { return currentPhase; }

    public void setCurrentPhase(PHASE currentPhase) { this.currentPhase = currentPhase; }

    public void setPause(boolean pause) { this.pause = pause; }

    public boolean getPause() { return pause; }

}