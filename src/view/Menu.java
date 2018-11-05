package view;

import controller.MenuController;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;

public class Menu {

    private static Menu instance;

    private MenuController menuController;
    private String selectedFileName;
    private AnchorPane mainMenuPane;
    private int maxPlayerNum;
    private Stage menuStage;
    private Model model;
    private View view;

    private Menu() { }

    static Menu getInstance(){
        if (null == instance) instance = new Menu();
        return instance;
    }

    void init( Model model, View view) {
        this.model = model;
        this.view = view;
        FXMLLoader menuFxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        try {
            mainMenuPane = menuFxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("Menu.ctor(): " + exception.getMessage());
        }
        menuController = menuFxmlLoader.getController();
        menuController.init(this);
        menuStage = new Stage();
        menuStage.setTitle("Risk Game");
        menuStage.setScene(new Scene(mainMenuPane,500,300));
        menuStage.setResizable(false);
        menuStage.sizeToScene();
    }

    public void validateEnteredPlayerNum(String enteredPlayerNum) {

        boolean valid = false;
        String validationInfo;
        int playerNum;

        try {
            playerNum = Integer.parseInt(enteredPlayerNum);
            if (playerNum > maxPlayerNum) {
                validationInfo = "Greater than " + maxPlayerNum;
            } else if (playerNum <= 1) {
                validationInfo = "Must greater than 1";
            } else {
                valid = true;
                validationInfo = "Total Player: " + playerNum;
                // TODO: combined following lines
                PlayerView playerView = new PlayerView(view, view.getMapController());
                view.setPlayerView(playerView); // remove this line
                model.initiatePlayers(playerNum, playerView);
            }
        } catch (Exception e) {
            validationInfo = "Enter an integer";
            System.out.println("Menu.validateEnteredPlayerNum(): " + e.getMessage());
        }

        menuController.displayValidationResult(valid, validationInfo);
    }

    void displaySelectedFileName(boolean validFile, String mapInfo) {
        menuController.displaySelectedFileName(validFile, selectedFileName, mapInfo);
    }

    void showNumPlayerTextField(int maxPlayerNum) {
        this.maxPlayerNum = maxPlayerNum;
        menuController.showNumPlayerTextField(maxPlayerNum);
    }

    void switchToStartGameMenu() {
        show();
        menuController.switchToStartGameMenu();
    }

    void showStartGameButton() { menuController.showStartGameButton(); }


    /**
     * Close menuStage, ask View to close mapStage
     * Called by MenuController
     */
    public void quitGame() {
        menuStage.close();
        view.quitGame();
    }

    public void openMapEditor() { view.openMapEditor(); }

    public void showMapStage() { view.showMapStage(); }


    /**
     * Select a RISK map file, pass it to View
     * Called when user click 'Select Map' button on menu
     */
    public void selectMap() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Risk Map File");
        File riskMapFile = fileChooser.showOpenDialog(menuStage);
        if (null != riskMapFile && riskMapFile.exists()) {
            selectedFileName = riskMapFile.getName();
            try {
                model.readFile(riskMapFile.getPath());
            } catch (IOException exception) {
                System.out.println("Menu.readFile(): " + exception.getMessage());
            }
        }
    }

    void show() { menuStage.show(); }

    void hide() { menuStage.hide(); }
}
