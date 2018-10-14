package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class View extends Application implements Observer {

    public final double COUNTRY_WIDTH = 100;
    public final double COUNTRY_HEIGHT = 100;

    private MenuController menu_controller;
    private MapController map_controller;
    private Stage stage_menu;
    private Stage stage_map;
    private boolean validFile;
    private AnchorPane pane_main_map;
    // TODO: list of countries
    // TODO: list of lines

    @Override
    public void start(Stage stage_primary) throws Exception {
        FXMLLoader menu_fxml_loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        AnchorPane pane_main_menu = menu_fxml_loader.load();
        menu_controller = menu_fxml_loader.getController();
        menu_controller.initialize(this);
        stage_menu = new Stage();
        stage_menu.setTitle("Risk Game");
        stage_menu.setScene(new Scene(pane_main_menu));

        FXMLLoader map_fxml_loader = new FXMLLoader(getClass().getResource("Map.fxml"));
        pane_main_map = map_fxml_loader.load();
        map_controller = map_fxml_loader.getController();
        map_controller.initialize(this);
        stage_map = new Stage();
        stage_map.setTitle("Risk Game");
        stage_map.setScene(new Scene(pane_main_map));

        showMenuStage();
    }

    public void showMenuStage() {
        stage_map.hide();
        stage_menu.show();
    }

    public void showMapStage() {
        stage_menu.hide();
        stage_map.show();
    }

    public void closeMenuStage() {
        stage_map.close();
        stage_menu.close();
    }

    public void selectMap() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Risk Map File");
        File riskMapFile = fileChooser.showOpenDialog(stage_menu);
        if (null != riskMapFile && riskMapFile.exists()) {
            // TODO: ask controller, let model to valid the file format，get info
//            validFile = menuController.checkMapFileValid(riskMapFile.getName())*/;
            validFile = true;
            String additional_info = "This is additional info";
            menu_controller.setMapName(riskMapFile.getName(), validFile);
            menu_controller.setAdditionalMapInfo(additional_info);
        }
    }

    public void update(Observable obs, Object x) {
        // TODO: get obs updated info, pass it to controller
        System.out.println("receive notify: " + x);
    }

    public void createCountry(double cursorX, double cursorY) {
        Country country = new Country(cursorX, cursorY);
        pane_main_map.getChildren().add(country);
    }

    class Country extends AnchorPane {
        public Country(double cursorX, double cursorY) {
            setPrefSize(COUNTRY_WIDTH, COUNTRY_HEIGHT);
            setLayoutX(cursorX - COUNTRY_WIDTH/2);
            setLayoutY(cursorY - COUNTRY_HEIGHT/2);
            setStyle("-fx-background-color: red");
        }
    }
}
