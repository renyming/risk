package controller;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import view.Menu;


/**
 * Handle event when user interact with the menu, pass it to View
 */
public class MenuController {

    @FXML public Label playerNumInstructionLabel;
    @FXML public Label userEnteredPlayNumLabel;
    @FXML public TextField playerNumTextField;
    @FXML public AnchorPane startGamePane;
    @FXML public AnchorPane mainMenuPane;
    @FXML public AnchorPane newGamePane;
    @FXML public Label selectedMapLabel;
    @FXML public Button startGameButton;
    @FXML public AnchorPane quitPane;
    @FXML public Label mapInfoPane;

    private Menu menu;


    /**
     * Get View reference, add event listener
     * @param menu the Menu reverence
     */
    public void init(Menu menu) {
        this.menu = menu;
        startGamePane.setVisible(true);
        newGamePane.setVisible(true);
        quitPane.setVisible(true);
        addEventListener();
    }


    /**
     * Add event listener to the playerNumTextField
     */
    private void addEventListener() {
        playerNumTextField.setOnAction((event) -> menu.validateEnteredPlayerNum(playerNumTextField.getText()));
    }


    /**
     * Switch to Map Editor
     * Called when user click the map editor button
     */
    public void switchToMapEditor() { menu.openMapEditor(); }


    /**
     * Switch to start game menu
     * Called when user quitGame to the menu page
     */
    public void switchToStartGameMenu() {
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(startGamePane);
    }


    /**
     * Switch to new game menu, hide irrelevant panes
     * Called when user click the new game button
     */
    public void switchToNewGameMenu() {
        resetNewGameMenu();
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(newGamePane);
    }


    /**
     * Switch to quitGame menu
     * Called when user click quitGame button on menu
     */
    public void switchToQuitMenu() {
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(quitPane);
    }


    /**
     * Reset start up menu, reset previous loading file info
     * Called by switchToStartGameMenu()
     */
    private void resetNewGameMenu() {
        selectedMapLabel.setText("Selected map: NONE");
        selectedMapLabel.setStyle("-fx-border-color: red; -fx-border-width: 3");
        mapInfoPane.setVisible(false);
        playerNumInstructionLabel.setVisible(false);
        userEnteredPlayNumLabel.setStyle("-fx-border-color: #ff7f00; -fx-border-width: 3");
        userEnteredPlayNumLabel.setVisible(false);
        playerNumTextField.setVisible(false);
        playerNumTextField.clear();
        startGameButton.setVisible(false);
    }


    /**
     * Display selected file name and invalid message if necessary
     * Called by View.update()
     * @param validFile decides whether the selected file is valid
     * @param filename is the selected file name
     * @param mapInfo gives additional info if the selected is invalid
     */
    public void displaySelectedFileName(boolean validFile, String filename, String mapInfo) {
        mapInfoPane.setVisible(true);
        startGameButton.setVisible(false);
        if (validFile) {
            playerNumInstructionLabel.setVisible(true);
            playerNumTextField.setVisible(true);
            userEnteredPlayNumLabel.setVisible(true);
            selectedMapLabel.setText("Valid map: " + filename);
            selectedMapLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
            mapInfoPane.setText(mapInfo);
        } else {
            selectedMapLabel.setText("Invalid map: " + filename);
            playerNumInstructionLabel.setVisible(false);
            playerNumTextField.setVisible(false);
            playerNumTextField.clear();
            userEnteredPlayNumLabel.setVisible(false);
            selectedMapLabel.setStyle("-fx-border-color: red; -fx-border-width: 3");
            mapInfoPane.setText(mapInfo);
        }
    }


    /**
     * Set then show the number of player text field
     * Called by View.update()
     * @param maxPlayerNum is the max number of player allowed for the selected file
     */
    public void showNumPlayerTextField(int maxPlayerNum) {
        playerNumInstructionLabel.setVisible(true);
        playerNumTextField.setVisible(true);
        playerNumInstructionLabel.setText("Max number of players: " + maxPlayerNum);
        userEnteredPlayNumLabel.setStyle("-fx-border-color: #ff7f00; -fx-border-width: 3");
        userEnteredPlayNumLabel.setText("Total Player: ");
        playerNumTextField.clear();
    }


    public void displayValidationResult(boolean valid, String invalidInfo) {
        userEnteredPlayNumLabel.setText(invalidInfo);
        if (valid) {
            userEnteredPlayNumLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        } else {
            userEnteredPlayNumLabel.setStyle("-fx-border-color: #ff7f00; -fx-border-width: 3");
            startGameButton.setVisible(false);
        }
    }


    /**
     * Called when user confirm the quitGame process by clicking yes button
     * Pass the event to View
     */
    public void quitGame() { menu.quitGame(); }


    /**
     * Select map for starting the new game
     * Called when user clicked the select map button
     * Pass event to View
     */
    public void selectMap() { menu.selectMap(); }


    /**
     * Show start game button, game is fully loaded and ready to start
     * Called by View.update()
     */
    public void showStartGameButton() { startGameButton.setVisible(true); }


    /**
     * Called when user click the start game button
     * Pass event to the View
     */
    public void startGame() { menu.showMapStage(); }
}
