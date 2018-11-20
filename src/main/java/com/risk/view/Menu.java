package com.risk.view;

import com.risk.controller.MenuController;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


/**
 * Load the map UI from fxml file, able to initialize, show, hide and close it
 */
public class Menu {

    private static Menu instance;

    private MenuController menuController;
    private AnchorPane mainMenuPane;
    private Stage menuStage;


    /**
     * Ctor, load UI, set default values
     */
    private Menu() {
        FXMLLoader menuFxmlLoader = new FXMLLoader(getClass().getResource("/Menu.fxml"));
        try {
            mainMenuPane = menuFxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("Menu.ctor(): " + exception.getMessage());
        }
        menuController = menuFxmlLoader.getController();
        menuStage = new Stage();
        menuStage.setTitle("Risk Game");
        menuStage.setScene(new Scene(mainMenuPane));
        menuStage.setResizable(false);
        menuStage.sizeToScene();

        Media sound = new Media(Objects.requireNonNull(getClass().getClassLoader().getResource("GOT.mp3")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }


    /**
     * Singleton standard getter method, get the instance
     * @return the instance
     */
    static Menu getInstance(){
        if (null == instance) instance = new Menu();
        return instance;
    }


    /**
     * Show menu
     */
    public void show() { menuStage.show(); }


    /**
     * Hide menu
     */
    public void hide() { menuStage.hide(); }


    /**
     * Close menu
     */
    public void close() { menuStage.close(); }


    /**
     * Get the menu Stage
     * For select file usage
     * @return the menu Stage
     */
    public Stage getMenuStage() { return menuStage; }


    /**
     * Get the MenuController
     * @return the MenuController reference
     */
    MenuController getMenuController() { return menuController; } // TODO: remove later
}
