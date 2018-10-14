package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class MenuController {

    public View view;

    @FXML private AnchorPane pane_main_menu;
    @FXML private AnchorPane pane_startUp_menu;
    @FXML private AnchorPane pane_newGame_menu;
    @FXML private AnchorPane pane_quit_menu;
    @FXML private Label label_selected_map;
    @FXML private Label label_map_info;
    @FXML private Button button_start_game;

    public void initialize(View view) {
        this.view = view;
        pane_startUp_menu.setVisible(true);
        pane_newGame_menu.setVisible(true);
        pane_quit_menu.setVisible(true);
        button_start_game.setVisible(false);
        switchToStartUpMenu();
    }

    public void switchToStartUpMenu() {
        pane_main_menu.getChildren().clear();
        pane_main_menu.getChildren().add(pane_startUp_menu);
    }

    public void switchToNewGameMenu() {
        // TODO: call view to reset selected file?
        pane_main_menu.getChildren().clear();
        pane_main_menu.getChildren().add(pane_newGame_menu);
    }

    public void switchToQuitMenu() {
        pane_main_menu.getChildren().clear();
        pane_main_menu.getChildren().add(pane_quit_menu);
    }

    public void quit() { view.closeMenuStage(); }

    public void selectMap() { view.selectMap(); }

    public void setMapName(String filename, boolean valid) {
        if (valid) {
            button_start_game.setVisible(true);
            label_selected_map.setText("Valid map: " + filename);
            label_selected_map.setStyle("-fx-border-color: green; -fx-border-width: 2");
        } else {
            button_start_game.setVisible(false);
            label_selected_map.setText("Invalid map: " + filename);
            label_selected_map.setStyle("-fx-border-color: red; -fx-border-width: 2");
        }
    }

    public void setAdditionalMapInfo(String info) {
        label_map_info.setText(info);
    }

    public void createMap() {
        view.showMapStage();
    }

    public void startGame() {
        loadMap();
        view.showMapStage();
    }

    public void loadMap() {

    }
}
