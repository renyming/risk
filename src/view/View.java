package view;

import controller.MapController;
import controller.MenuController;
import model.Model;
import common.Message;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Observable;
import java.util.Observer;


/**
 * View, support user interaction, do partial user input validation
 * Corresponding Observable subject is Model
 */
public class View implements Observer {

    public enum PHASE {ENTER_NUM_PLAYER, START_UP, REINFORCEMENT, ATTACK, FORTIFICATION}

//    private final double COUNTRY_VIEW_HEIGHT = 60;
//    private final double COUNTRY_VIEW_WIDTH = 60;
//    private final double MENU_WIDTH = 500;
//    private final double MENU_HEIGHT = 300;
//    private final double GAME_BOARD_WIDTH = 1000;
//    private final double GAME_BOARD_HEIGHT = 700;

    private Stage mapEditorStage;
    private Model model;
    private Menu menu;
    private Map map;

    // TODO: remove later?
    private MenuController menuController;
    private MapController mapController;
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
        this.model = model;
        menuController = menu.getMenuController();
        mapController = map.getMapController();

        menuController.init(model, this, menu, mapController);
        mapController.init(model, this, map, menuController);
    }


    /**
     * Observer update method, allow View to know what to do next
     * Notified by corresponding Model Observable subject when game state changes
     * @param obs Model Observable subject
     * @param x Message object, encapsulated STATE info
     */
    @Override
    public void update(Observable obs, Object x) {
//        Message message = (Message) x;
////        System.out.println("View.update(): new state is " + message.state + ", ");
//        switch (message.state) {
//            case LOAD_FILE:
//                menuController.displaySelectedFileName(false, (String) message.obj);
//                break;
//            case CREATE_OBSERVERS:
//                menuController.displaySelectedFileName(true, "Useful info here");
//                mapController.initCountryViews((int) message.obj);
//                break;
//            case PLAYER_NUMBER:
//                currentPhase = PHASE.ENTER_NUM_PLAYER;
//                menuController.showNumPlayerTextField(mapController.getCountryViewsSize());
//                break;
//            case INIT_ARMIES:
//                currentPhase = PHASE.START_UP;
//                mapController.setPhaseLabel("Start Up Phase");
//                menuController.showStartGameButton();
//                break;
//            case ROUND_ROBIN:
//                mapController.showNextPhaseButton("Enter Reinforcement Phase");
//                mapController.showPlayerViewPane(false);
//                pause = true;
//                model.reinforcement();
//                break;
//            case NEXT_PLAYER:
//                mapController.showNextPhaseButton("Enter Reinforcement Phase");
//                mapController.displayFromToCountriesInfoPane(false);
//                mapController.showPlayerViewPane(false);
//                mapController.showInvalidMoveLabelInfo(false, "");
//                model.nextPlayer();
//                break;
//        }
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