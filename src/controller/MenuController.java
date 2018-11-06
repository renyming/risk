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
    public void switchToSelectMapMenu() {
        if (null == fileInfoMenuView && null == numPlayerMenuView) {
            fileInfoMenuView = new FileInfoMenuView();
            fileInfoMenuView.init(selectedFilenameLabel, mapValidationInfoLabel);
            numPlayerMenuView = new NumPlayerMenuView();
            numPlayerMenuView.init(numPlayerInstructionLabel, validationOfUserEnteredLabel, numPlayerTextField, startButton, mapController);
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
     * Pass event to View
     */
    public void selectMap() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Risk Map File");
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
     * Ask MapController create CountryViews and pass it to Model
     * Ask MapController create PhaseView and pass it to Model
     * Ask Model to initiate Player relative info
     * @param enteredPlayerNum is what user entered in the text field
     */
    private void validateEnteredPlayerNum(String enteredPlayerNum) {
        numPlayerMenuView.setTotalNumPlayer(enteredPlayerNum);
        mapController.initCountryViews();
        mapController.createPhaseView();
        model.initiatePlayers(enteredPlayerNum);
    }


    /**
     * Called when user click the start game button
     * Pass event to the View
     */
    public void startGame() {
//        model.startUp(); // TODO:
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
