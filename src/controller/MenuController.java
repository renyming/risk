package controller;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import model.Model;
import view.Menu;
import view.View;

import java.io.File;
import java.io.IOException;


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

    private Model model;
    private Menu menu;
    private View view;
    private int maxPlayerNum;
    private String selectedFileName;


    /**
     * Get View reference, add event listener
     * @param menu the Menu reverence
     */
    public void init(Model model, View view, Menu menu) {
        this.model = model;
        this.view = view;
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
        playerNumTextField.setOnAction((event) -> validateEnteredPlayerNum(playerNumTextField.getText()));
    }


    /**
     * Switch to Map Editor
     * Called when user click the map editor button
     */
    public void switchToMapEditor() { view.openMapEditor(); }


    /**
     * Switch to start game menu
     * Called when user quitGame to the menu page
     */
    public void switchToStartGameMenu() {
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(startGamePane);
        menu.show();
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
     * @param mapInfo gives additional info if the selected is invalid
     */
    public void displaySelectedFileName(boolean validFile, String mapInfo) {
        mapInfoPane.setVisible(true);
        startGameButton.setVisible(false);
        if (validFile) {
            playerNumInstructionLabel.setVisible(true);
            playerNumTextField.setVisible(true);
            userEnteredPlayNumLabel.setVisible(true);
            selectedMapLabel.setText("Valid map: " + selectedFileName);
            selectedMapLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
            mapInfoPane.setText(mapInfo);
        } else {
            selectedMapLabel.setText("Invalid map: " + selectedFileName);
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
        this.maxPlayerNum = maxPlayerNum;
        playerNumInstructionLabel.setVisible(true);
        playerNumTextField.setVisible(true);
        playerNumInstructionLabel.setText("Max number of players: " + maxPlayerNum);
        userEnteredPlayNumLabel.setStyle("-fx-border-color: #ff7f00; -fx-border-width: 3");
        userEnteredPlayNumLabel.setText("Total Player: ");
        playerNumTextField.clear();
    }


    private void displayValidationResult(boolean valid, String invalidInfo) {
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
    public void quitGame() {
        menu.close();
        view.quitGame();
    }


    /**
     * Select map for starting the new game
     * Called when user clicked the select map button
     * Pass event to View
     */
    public void selectMap() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Risk Map File");
        File riskMapFile = fileChooser.showOpenDialog(menu.getMenuStage());
        if (null != riskMapFile && riskMapFile.exists()) {
            selectedFileName = riskMapFile.getName();
            try {
                model.readFile(riskMapFile.getPath());
            } catch (IOException exception) {
                System.out.println("MenuController.readFile(): " + exception.getMessage());
            }
        }
    }


    /**
     * Show start game button, game is fully loaded and ready to start
     * Called by View.update()
     */
    public void showStartGameButton() { startGameButton.setVisible(true); }


    /**
     * Called when user click the start game button
     * Pass event to the View
     */
    public void startGame() { showMapStage(); }


    private void validateEnteredPlayerNum(String enteredPlayerNum) { // TODO: should be checked by model itself
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
                model.initiatePlayers(playerNum, view.createPlayerView());
            }
        } catch (Exception e) {
            validationInfo = "Enter an integer";
            System.out.println("Menu.validateEnteredPlayerNum(): " + e.getMessage());
        }

        displayValidationResult(valid, validationInfo);
    }

    private void showMapStage() {
        menu.hide();
        view.showMapStage();
    }
}
