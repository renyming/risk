package controller;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import model.Model;
import view.FileInfoMenuView;
import view.Menu;
import view.NumPlayerMenuView;
import view.View;

import java.io.File;
import java.io.IOException;


/**
 * Handle event when user interact with the menu, pass it to View
 */
public class MenuController {

    @FXML public Label selectedFilenameLabel;
    @FXML public Label mapValidationInfoLabel;

    @FXML public Label numPlayerInstructionLabel;
    @FXML public Label validationOfUserEnteredLabel;
    @FXML public TextField numPlayerTextField;
    @FXML public Button startButton;

    @FXML public AnchorPane startGamePane;
    @FXML public AnchorPane mainMenuPane;
    @FXML public AnchorPane newGamePane;
    @FXML public AnchorPane quitPane;

    private Model model;
    private View view;
    private Menu menu;
    private MapController mapController;

    private int maxPlayerNum;
    private String selectedFileName;
    private FileInfoMenuView fileInfoMenuView;
    private NumPlayerMenuView numPlayerMenuView;



    /**
     * Store reference of Model, View and MapController
     * @param model is the reference of Model
     * @param view is the reference of View
     * @param mapController is the reference of the MapController
     */
    public void init(Model model, View view, Menu menu, MapController mapController) {
        this.model = model;
        this.view = view;
        this.menu = menu;
        this.mapController = mapController;
        startGamePane.setVisible(true);
        newGamePane.setVisible(true);
        quitPane.setVisible(true);
        addEventListener();
    }

    /**
     * Add event listener to the numPlayerTextField
     */
    private void addEventListener() {
        numPlayerTextField.setOnAction((event) -> validateEnteredPlayerNum(numPlayerTextField.getText()));
    }


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
     * Switch to Map Editor
     * Called when user click the map editor button
     */
    public void switchToMapEditor() { view.openMapEditor(); }


    /**
     * Switch to quitGame menu
     * Called when user click quitGame button on menu
     */
    public void switchToQuitMenu() {
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(quitPane);
    }


    /**
     * Switch to select map menu, hide irrelevant panes
     * Called when user click the new game button
     */
    public void switchToNewGameMenu() {
        resetNewGameMenu();
        // TODO: pass two views to model
        if (null == fileInfoMenuView && null == numPlayerMenuView) {
            fileInfoMenuView = new FileInfoMenuView();
            fileInfoMenuView.init(selectedFilenameLabel, mapValidationInfoLabel);
            numPlayerMenuView = new NumPlayerMenuView();
            numPlayerMenuView.init(numPlayerInstructionLabel, validationOfUserEnteredLabel, numPlayerTextField, startButton, mapController);
            model.setMenuViews(fileInfoMenuView, numPlayerMenuView);
        } else {
            numPlayerMenuView.reset();
            fileInfoMenuView.reset();
        }
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(newGamePane);
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
            fileInfoMenuView.setSelectedFilename(selectedFileName);
            try {
                model.readFile(riskMapFile.getPath());
            } catch (IOException exception) {
                System.out.println("MenuController.readFile(): " + exception.getMessage());
            }
        }
    }


    private void validateEnteredPlayerNum(String enteredPlayerNum) {

        numPlayerMenuView.setTotalNumPlayer(enteredPlayerNum);

        // TODO:
//        mapController.initCountryViews(maxPlayerNum);
//        model.setPhaseView(mapController.createPhaseView());
//        model.initiatePlayers(enteredPlayerNum);


        // TODO: code below should be checked by model itself
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
                model.initiatePlayers(playerNum, mapController.createPlayerView());
            }
        } catch (Exception e) {
            validationInfo = "Enter an integer";
            System.out.println("Menu.validateEnteredPlayerNum(): " + e.getMessage());
        }

        displayValidationResult(valid, validationInfo);
    }


    /**
     * Called when user click the start game button
     * Pass event to the View
     */
    public void startGame() {
        menu.hide();
        mapController.showMapStage();
    }


    /**
     * Called when user confirm the quitGame process by clicking yes button
     * Pass the event to View
     */
    public void quitGame() {
        menu.close();
        mapController.quitGame();
    }












    /**
     * Reset start up menu, reset previous loading file info
     * Called by switchToStartGameMenu()
     */
    private void resetNewGameMenu() {
        selectedFilenameLabel.setText("Selected map: NONE");
        selectedFilenameLabel.setStyle("-fx-border-color: red; -fx-border-width: 3");
        mapValidationInfoLabel.setVisible(false);
        numPlayerInstructionLabel.setVisible(false);
        validationOfUserEnteredLabel.setStyle("-fx-border-color: #ff7f00; -fx-border-width: 3");
        validationOfUserEnteredLabel.setVisible(false);
        numPlayerTextField.setVisible(false);
        numPlayerTextField.clear();
        startButton.setVisible(false);
    }


    /**
     * Display selected file name and invalid message if necessary
     * Called by View.update()
     * @param validFile decides whether the selected file is valid
     * @param mapInfo gives additional info if the selected is invalid
     */
    public void displaySelectedFileName(boolean validFile, String mapInfo) {
        mapValidationInfoLabel.setVisible(true);
        startButton.setVisible(false);
        if (validFile) {
            numPlayerInstructionLabel.setVisible(true);
            numPlayerTextField.setVisible(true);
            validationOfUserEnteredLabel.setVisible(true);
            selectedFilenameLabel.setText("Valid map: " + selectedFileName);
            selectedFilenameLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
            mapValidationInfoLabel.setText(mapInfo);
        } else {
            selectedFilenameLabel.setText("Invalid map: " + selectedFileName);
            numPlayerInstructionLabel.setVisible(false);
            numPlayerTextField.setVisible(false);
            numPlayerTextField.clear();
            validationOfUserEnteredLabel.setVisible(false);
            selectedFilenameLabel.setStyle("-fx-border-color: red; -fx-border-width: 3");
            mapValidationInfoLabel.setText(mapInfo);
        }
    }


    /**
     * Set then show the number of player text field
     * Called by View.update()
     * @param maxPlayerNum is the max number of player allowed for the selected file
     */
    public void showNumPlayerTextField(int maxPlayerNum) {
        this.maxPlayerNum = maxPlayerNum;
        numPlayerInstructionLabel.setVisible(true);
        numPlayerTextField.setVisible(true);
        numPlayerInstructionLabel.setText("Max number of players: " + maxPlayerNum);
        validationOfUserEnteredLabel.setStyle("-fx-border-color: #ff7f00; -fx-border-width: 3");
        validationOfUserEnteredLabel.setText("Total Player: ");
        numPlayerTextField.clear();
    }


    private void displayValidationResult(boolean valid, String invalidInfo) {
        validationOfUserEnteredLabel.setText(invalidInfo);
        if (valid) {
            validationOfUserEnteredLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        } else {
            validationOfUserEnteredLabel.setStyle("-fx-border-color: #ff7f00; -fx-border-width: 3");
            startButton.setVisible(false);
        }
    }


    /**
     * Show start game button, game is fully loaded and ready to start
     * Called by View.update()
     */
    public void showStartGameButton() { startButton.setVisible(true); }
}
