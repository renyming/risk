package com.risk.controller;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import com.risk.model.Model;
import com.risk.view.FileInfoMenuView;
import com.risk.view.Menu;
import com.risk.view.NumPlayerMenuView;
import com.risk.view.View;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Handle event when user interact with the menu, pass it to Model
 */
public class MenuController {

    @FXML public Label selectedFilenameLabel;
    @FXML public Label mapValidationInfoLabel;
    @FXML public Label numPlayerInstructionLabel;
    @FXML public Label validationOfUserEnteredLabel;
    @FXML public Label playerOneLabel;
    @FXML public Label playerTwoLabel;
    @FXML public Label playerThreeLabel;
    @FXML public Label playerFourLabel;
    @FXML public Label playerFiveLabel;
    @FXML public Label playerSixLabel;

    @FXML public TextField numPlayerTextField;

    @FXML public Button startButton;
    @FXML public Button selectMapOneButton;
    @FXML public Button selectMapTwoButton;
    @FXML public Button selectMapThreeButton;
    @FXML public Button selectMapFourButton;
    @FXML public Button selectMapFiveButton;

    @FXML public ChoiceBox playerOneTypeChoiceBox;
    @FXML public ChoiceBox playerTwoTypeChoiceBox;
    @FXML public ChoiceBox playerThreeTypeChoiceBox;
    @FXML public ChoiceBox playerFourTypeChoiceBox;
    @FXML public ChoiceBox playerFiveTypeChoiceBox;
    @FXML public ChoiceBox playerSixTypeChoiceBox;


    @FXML public AnchorPane startGamePane;
    @FXML public AnchorPane mainMenuPane;
    @FXML public AnchorPane newGamePane;
    @FXML public AnchorPane quitPane;

    private Model model;
    private View view;
    private Menu menu;
    private MapController mapController;

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
        numPlayerTextField.setOnAction((event) -> validateEnteredNumPlayer(numPlayerTextField.getText()));
    }


    /**
     * Switch to start game menu
     */
    public void switchToStartGameMenu() {
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(startGamePane);
        menu.show();
    }


    /**
     * Switch to Map Editor
     * Called when user clicks the map editor button
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
     * Called when users click StartNewGame Button
     */
    public void startNewGame() {
        selectMapTwoButton.setVisible(false);
        selectMapThreeButton.setVisible(false);
        selectMapFourButton.setVisible(false);
        selectMapFiveButton.setVisible(false);
        switchToSelectMapMenu();
    }


    /**
     * Called when users click LoadSavedGame
     */
    public void loadSavedGame() {

    }


    /**
     * Called when users click TournamentMode
     */
    public void tournamentMode() {
        selectMapTwoButton.setVisible(true);
        selectMapThreeButton.setVisible(true);
        selectMapFourButton.setVisible(true);
        selectMapFiveButton.setVisible(true);
        switchToSelectMapMenu();
    }


    /**
     * Switch to select map menu, hide irrelevant panes
     * Called when user click the new game button
     */
    private void switchToSelectMapMenu() {
        if (null == fileInfoMenuView && null == numPlayerMenuView) {
            fileInfoMenuView = new FileInfoMenuView();
            fileInfoMenuView.init(selectedFilenameLabel, mapValidationInfoLabel);
            numPlayerMenuView = new NumPlayerMenuView();
            HashMap<Integer, Label> playerNumLabels = new HashMap<>();
            playerNumLabels.put(1, playerOneLabel);
            playerNumLabels.put(2, playerTwoLabel);
            playerNumLabels.put(3, playerThreeLabel);
            playerNumLabels.put(4, playerFourLabel);
            playerNumLabels.put(5, playerFiveLabel);
            playerNumLabels.put(6, playerSixLabel);
            HashMap<Integer, ChoiceBox> playerTypeChoiceBoxes = new HashMap<>();
            playerTypeChoiceBoxes.put(1, playerOneTypeChoiceBox);
            playerTypeChoiceBoxes.put(2, playerTwoTypeChoiceBox);
            playerTypeChoiceBoxes.put(3, playerThreeTypeChoiceBox);
            playerTypeChoiceBoxes.put(4, playerFourTypeChoiceBox);
            playerTypeChoiceBoxes.put(5, playerFiveTypeChoiceBox);
            playerTypeChoiceBoxes.put(6, playerSixTypeChoiceBox);

            numPlayerMenuView.init(numPlayerInstructionLabel, validationOfUserEnteredLabel, numPlayerTextField,
                    startButton, mapController, playerNumLabels, playerTypeChoiceBoxes);
            model.setMenuViews(fileInfoMenuView, numPlayerMenuView);
        }
        numPlayerMenuView.reset();
        fileInfoMenuView.reset();
        mainMenuPane.getChildren().clear();
        mainMenuPane.getChildren().add(newGamePane);
    }


    /**
     * Select map for starting the new game
     * Called when user clicked the select map button
     */
    public void selectMap() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Risk Map File");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Map files (*.map)", "*.map");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File riskMapFile = fileChooser.showOpenDialog(menu.getMenuStage());
        if (null != riskMapFile && riskMapFile.exists()) {
            fileInfoMenuView.setSelectedFilename(riskMapFile.getName());
            try {
                model.readFile(riskMapFile.getPath());
            } catch (IOException exception) {
                System.out.println("MenuController.readFile(): " + exception.getMessage());
            }
        }
    }


    /**
     * Pass user entered string into NumPlayerMenuView
     * Update menu view to display the map number
     * Ask Model to initiate Player relative info
     * @param enteredPlayerNum is what user entered in the text field
     */
    private void validateEnteredNumPlayer(String enteredPlayerNum) {
        numPlayerMenuView.setTotalNumPlayer(enteredPlayerNum);
        model.initiatePlayers(enteredPlayerNum);
    }


    /**
     * Called when user click the start button
     * Create PhaseView, CountryViews
     * Pass info to the Menu, Model, and MapController
     */
    public void startGame() {
        mapController.createPhaseView();
        model.startUp(mapController.createCountryViews());
        menu.hide();
        mapController.showMapStage();
    }


    /**
     * Called when user confirm the quitGame process by clicking yes button
     */
    public void quitGame() {
        menu.close();
        mapController.quitGame();
    }
}
