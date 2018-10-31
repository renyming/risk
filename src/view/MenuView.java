package view;

import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;

class MenuView {

    private static MenuView instance = new MenuView();

    private MenuController menuController;
    private AnchorPane mainMenuPane;
    private Stage menuStage;

    private MenuView() { }

    static MenuView getInstance(){
        if (null == instance) instance = new MenuView();
        return instance;
    }

    void init(View view) {
        FXMLLoader menuFxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        try {
            mainMenuPane = menuFxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("MenuView.ctor(): " + exception.getMessage());
        }
        menuController = menuFxmlLoader.getController();
        menuController.initialize(view);
        menuStage = new Stage();
        menuStage.setTitle("Risk Game");
        menuStage.setScene(new Scene(mainMenuPane,500,300));
        menuStage.setResizable(false);
        menuStage.sizeToScene();
    }

    void switchToStartGameMenu() {
        show();
        menuController.switchToStartGameMenu();
    }

    void displaySelectedFileName(String filename, boolean validFile, String mapInfo) {
        menuController.displaySelectedFileName(filename, validFile, mapInfo);
    }

    void showNumPlayerTextField(int maxPlayerNum) { menuController.showNumPlayerTextField(maxPlayerNum); }

    void showStartGameButton() { menuController.showStartGameButton(); }

    void show() { menuStage.show(); }

    void hide() { menuStage.hide(); }

    void close() { menuStage.close(); }

    Stage getMenuStage() { return menuStage; }
}
