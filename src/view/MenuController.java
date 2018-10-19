package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


/**
 * Handle event when user interact with the menu, pass it to View
 */
public class MenuController {

    @FXML private AnchorPane mainMenuPane;
    @FXML private AnchorPane startGamePane;
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


    /**
     * Get View reference, add event listener
     * @param view the View reverence
     */
    public void initialize(View view) {
        this.view = view;
        startGamePane.setVisible(true);
        newGamePane.setVisible(true);
        quitPane.setVisible(true);
        addEventListener();
        switchToStartGameMenu();
    }


    /**
     * Add event listener to the playerNumTextField
     */
    private void addEventListener() {
        playerNumTextField.setOnAction((event) -> {
            playerNumInstructionLabel.setStyle("-fx-border-color: red; -fx-border-width: 2");
            try { // TODO: refactor, only catch enteredNum
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
                    playerNumInstructionLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
                    view.initializePlayer(userEnteredPlayerNum);
                }
            } catch (Exception e) {
                userEnteredPlayNumLabel.setText("Enter an integer");
                startGameButton.setVisible(false);
                System.out.println("MenuController.initialize(): " + e.getMessage());
            }
        });
    }


    /**
     * Switch to Map Editor
     * Called when user click the map editor button
     */
    public void switchToMapEditor() { view.openMapEditor(); }


    /**
     * Switch to start game menu
     * Called when user quit to the menu page
     */
    public void switchToStartGameMenu() {
        resetStartUpMenu();
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(startGamePane);
    }


    /**
     * Switch to new game menu, hide irrelevant panes
     * Called when user click the new game button
     */
    public void switchToNewGameMenu() {
        mapInfoPane.setVisible(false);
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(newGamePane);
    }


    /**
     * Switch to quit menu
     * Called when user click quit button on menu
     */
    public void switchToQuitMenu() {
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(quitPane);
    }


    /**
     * Reset start up menu, reset previous loading file info
     * Called by switchToStartGameMenu()
     */
    public void resetStartUpMenu() {
        selectedMapLabel.setText("Selected map: NONE");
        selectedMapLabel.setStyle("-fx-border-color: red; -fx-border-width: 3");
        playerNumInstructionLabel.setVisible(false);
        playerNumInstructionLabel.setStyle("-fx-border-color: #ff7f00; -fx-border-width: 3");
        playerNumTextField.setVisible(false);
        playerNumTextField.clear();
        userEnteredPlayNumLabel.setVisible(false);
        startGameButton.setVisible(false);
    }


    /**
     * Called when user confirm the quit process by clicking yes button
     * Pass the event to View
     */
    public void quit() { view.closeMenuStage(); }


    /**
     * Select map for starting the new game
     * Called when user clicked the select map button
     * Pass event to View
     */
    public void selectMap() {view.selectMap(); }


    /**
     * Display selected file name and invalid message if necessary
     * Called by View.update()
     * @param filename is the selected file name
     * @param validFile decides whether the selected file is valid
     * @param mapInfo gives additional info if the selected is invalid
     */
    public void displaySelectedFileName(String filename, boolean validFile, String mapInfo) {
        mapInfoPane.setVisible(true);
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
        this.maxPlayerNum = maxPlayerNum;
        playerNumInstructionLabel.setVisible(true);
        playerNumTextField.setVisible(true);
        playerNumInstructionLabel.setStyle("-fx-border-color: #ff7f00; -fx-border-width: 3");
        playerNumInstructionLabel.setText("Max number of players: " + this.maxPlayerNum);
        userEnteredPlayNumLabel.setText("Total Player: ");
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
    public void startGame() { view.showMapStage(); }
}
