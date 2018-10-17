package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import static java.lang.Math.min;

public class MenuController {

    @FXML private AnchorPane mainMenuPane;
    @FXML private AnchorPane startUpPane;
    @FXML private AnchorPane newGamePane;
    @FXML private AnchorPane quitPane;
    @FXML private Label selectedMapLabel;
    @FXML private Label mapInfoPane;
    @FXML private Button startGameButton;
    @FXML private Label playerNumInstructionLabel;
    @FXML private TextField playerNumTextField;
    @FXML private Label userEnteredPlayNumLabel;

    private View view;
    private int maxPlayerNum;


    public void initialize(View view) {
        this.view = view;
        startUpPane.setVisible(true);
        newGamePane.setVisible(true);
        quitPane.setVisible(true);
        switchToStartUpMenu();

        // TODO: refactor
        playerNumTextField.setOnAction((event) -> {
            playerNumInstructionLabel.setStyle("-fx-border-color: red; -fx-border-width: 2");
            try {
                int userEnteredPlayerNum = Integer.parseInt(playerNumTextField.getText());
                userEnteredPlayNumLabel.setVisible(true);
                userEnteredPlayNumLabel.setText("Total Player: " + userEnteredPlayerNum);
                if (userEnteredPlayerNum > maxPlayerNum) {
                    userEnteredPlayNumLabel.setText("Greater than " + maxPlayerNum);
                    startGameButton.setVisible(false);
                } else if (userEnteredPlayerNum <= 1) {
                    userEnteredPlayNumLabel.setText("Must greater than 1");
                    startGameButton.setVisible(false);
                } else {
                    playerNumInstructionLabel.setStyle("-fx-border-color: green; -fx-border-width: 2");
                    view.initializePlayer(userEnteredPlayerNum);
                }
            } catch (Exception e) {
                userEnteredPlayNumLabel.setText("Enter an integer");
                startGameButton.setVisible(false);
                System.out.println("MenuController.initialize(): " + e.getMessage());
            }

        });
    }

    public void switchToStartUpMenu() {
        resetStartUpMenu();
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(startUpPane);
    }

    public void switchToNewGameMenu() {
        // TODO: call view to reset selected file?
        mapInfoPane.setVisible(false);
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(newGamePane);
    }

    public void switchToQuitMenu() {
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(quitPane);
    }

    public void resetStartUpMenu() {
        selectedMapLabel.setText("Selected map: NONE");
        selectedMapLabel.setStyle("-fx-border-color: red; -fx-border-width: 2");
        playerNumInstructionLabel.setVisible(false);
        playerNumInstructionLabel.setStyle("-fx-border-color: yellow; -fx-border-width: 2");
        playerNumTextField.setVisible(false);
        playerNumTextField.clear();
        userEnteredPlayNumLabel.setVisible(false);
        startGameButton.setVisible(false);
    }

    public void quit() { view.closeMenuStage(); }

    public void selectMap() {view.selectMap(); }

    public void displaySelectedFileName(String filename, boolean validFile, String mapInfo) {
        mapInfoPane.setVisible(true);
        if (validFile) {
            playerNumInstructionLabel.setVisible(true);
            playerNumTextField.setVisible(true);
            userEnteredPlayNumLabel.setVisible(true);
            selectedMapLabel.setText("Valid map: " + filename);
            selectedMapLabel.setStyle("-fx-border-color: green; -fx-border-width: 2");
            mapInfoPane.setText(mapInfo);
        } else {
            selectedMapLabel.setText("Invalid map: " + filename);
            playerNumInstructionLabel.setVisible(false);
            playerNumTextField.setVisible(false);
            userEnteredPlayNumLabel.setVisible(false);
            selectedMapLabel.setStyle("-fx-border-color: red; -fx-border-width: 2");
            mapInfoPane.setText(mapInfo);
        }
    }

    public void showNumPlayerTextField(int maxPlayerNum) {
        this.maxPlayerNum = min(maxPlayerNum, 10);
        playerNumInstructionLabel.setVisible(true);
        playerNumTextField.setVisible(true);
        playerNumInstructionLabel.setStyle("-fx-border-color: yellow; -fx-border-width: 2");
        playerNumInstructionLabel.setText("Max number of players: " + this.maxPlayerNum);
        userEnteredPlayNumLabel.setText("Total Player: ");
    }

    public void showStartGameButton() { startGameButton.setVisible(true); }

    public void createMap() { view.showMapStage(); }

    public void startGame() { view.showMapStage(); }
}
