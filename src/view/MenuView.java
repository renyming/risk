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
    private int maxPlayerNum;
    private Stage menuStage;
    private View view;

    private MenuView() { }

    static MenuView getInstance(){
        if (null == instance) instance = new MenuView();
        return instance;
    }

    void init(View view) {
        this.view = view;
        FXMLLoader menuFxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        try {
            mainMenuPane = menuFxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("MenuView.ctor(): " + exception.getMessage());
        }
        menuController = menuFxmlLoader.getController();
        menuController.init(this);
        menuStage = new Stage();
        menuStage.setTitle("Risk Game");
        menuStage.setScene(new Scene(mainMenuPane,500,300));
        menuStage.setResizable(false);
        menuStage.sizeToScene();
    }

    void validateEnteredPlayerNum(String enteredPlayerNum) {

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
                view.initializePlayer(playerNum);
            }
        } catch (Exception e) {
            validationInfo = "Enter an integer";
            System.out.println("MenuView.validateEnteredPlayerNum(): " + e.getMessage());
        }

        menuController.displayValidationResult(valid, validationInfo);
    }

    void displaySelectedFileName(boolean validFile, String filename, String mapInfo) {
        menuController.displaySelectedFileName(validFile, filename, mapInfo);
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

    void closeMenuStage() { view.closeMenuStage(); }

    void openMapEditor() { view.openMapEditor(); }

    void showMapStage() { view.showMapStage(); }

    Stage getMenuStage() { return menuStage; }

    void selectMap() { view.selectMap(); }

    void close() { menuStage.close(); }

    void show() { menuStage.show(); }

    void hide() { menuStage.hide(); }
}
