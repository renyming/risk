package view;

import controller.MenuController;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;

public class Menu {

    private static Menu instance;

    private MenuController menuController;
    private AnchorPane mainMenuPane;
    private Stage menuStage;

    private Menu() { }

    static Menu getInstance(){
        if (null == instance) instance = new Menu();
        return instance;
    }

    void init(Model model, View view) {
        FXMLLoader menuFxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        try {
            mainMenuPane = menuFxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("Menu.ctor(): " + exception.getMessage());
        }
        menuController = menuFxmlLoader.getController();
        menuController.init(model, view, this);
        menuStage = new Stage();
        menuStage.setTitle("Risk Game");
        menuStage.setScene(new Scene(mainMenuPane,500,300));
        menuStage.setResizable(false);
        menuStage.sizeToScene();
    }


    public void show() { menuStage.show(); }

    public void hide() { menuStage.hide(); }

    public void close() { menuStage.close(); }

    // for select file usage
    public Stage getMenuStage() { return menuStage; }

    MenuController getMenuController() { return menuController; } // TODO: remove later
}
