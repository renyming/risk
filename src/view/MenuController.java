package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MenuController {

    public View view;

    @FXML private AnchorPane mainMenuPane;
    @FXML private AnchorPane startUpPane;
    @FXML private AnchorPane newGamePane;
    @FXML private AnchorPane quitPane;
    @FXML private Label selectedMapLabel;
    @FXML private Label mapInfoPane;
    @FXML private Button startGameButton;
    @FXML private Label numPlayerLabel;
    @FXML private TextField numPlayerTextField;

    public void initialize(View view) {
        this.view = view;
        startUpPane.setVisible(true);
        newGamePane.setVisible(true);
        quitPane.setVisible(true);
        startGameButton.setVisible(false);
        numPlayerLabel.setVisible(false);
        numPlayerTextField.setVisible(false);
        switchToStartUpMenu();

//        numPlayerLabel.setOn
    }

    public void switchToStartUpMenu() {
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(startUpPane);
    }

    public void switchToNewGameMenu() {
        // TODO: call view to reset selected file?
        selectedMapLabel.setText("Selected map: NONE");
        selectedMapLabel.setStyle("-fx-border-color: red; -fx-border-width: 2");
        mapInfoPane.setVisible(false);
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(newGamePane);
    }

    public void switchToQuitMenu() {
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(quitPane);
    }

    public void quit() { view.closeMenuStage(); }

    public void selectMap() { view.selectMap(); }

    public void displaySelectedFileName(String filename, boolean validFile, String mapInfo) {
        mapInfoPane.setVisible(true);
        if (validFile) {
            selectedMapLabel.setText("Valid map: " + filename);
            selectedMapLabel.setStyle("-fx-border-color: green; -fx-border-width: 2");
            mapInfoPane.setText(mapInfo);
            showNumPlayerTextField(5); //TODO: for self test purposes, should be removed later

        } else {
            selectedMapLabel.setText("Invalid map: " + filename);
            selectedMapLabel.setStyle("-fx-border-color: red; -fx-border-width: 2");
            mapInfoPane.setText(mapInfo);
        }
    }

    public void showNumPlayerTextField(int maxPlayer) {
        numPlayerLabel.setVisible(false);
        numPlayerTextField.setVisible(true);
        numPlayerLabel.setText("Enter number of players, max " + maxPlayer);
    }

    public void enterSomething() {
        System.out.println("123");
    }



//    startGameButton.setVisible(false);

    public void createMap() { view.showMapStage(); }

    public void startGame() { view.showMapStage(); }
}
