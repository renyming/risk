package com.risk.controller;

import com.risk.common.Action;
import com.risk.common.SleepTime;
import com.risk.model.*;
import com.risk.view.*;
import com.risk.view.Menu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


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
    @FXML public Label gamesPerMapLabel;
    @FXML public Label turnsPerGameLabel;

    @FXML public TextField numPlayerTextField;

    @FXML public Button startButton;
    @FXML public Button startButton1;
    @FXML public Button selectMapButton;
    @FXML public Button deleteMapButton;

    @FXML public ChoiceBox<String> playerOneTypeChoiceBox;
    @FXML public ChoiceBox<String> playerTwoTypeChoiceBox;
    @FXML public ChoiceBox<String> playerThreeTypeChoiceBox;
    @FXML public ChoiceBox<String> playerFourTypeChoiceBox;
    @FXML public ChoiceBox<String> playerFiveTypeChoiceBox;
    @FXML public ChoiceBox<String> playerSixTypeChoiceBox;


    @FXML public AnchorPane startGamePane;
    @FXML public AnchorPane mainMenuPane;
    @FXML public AnchorPane newGamePane;
    @FXML public AnchorPane quitPane;

    @FXML public Spinner<Integer> gamesPerMapSpinner;
    @FXML public Spinner<Integer> turnsPerGameSpinner;

    @FXML private ListView<String> selectedMapsListView;

    private Model model;
    private View view;
    private Menu menu;
    private MapController mapController;

    private FileInfoMenuView fileInfoMenuView;
    private NumPlayerMenuView numPlayerMenuView;
    private HashMap<Integer, ChoiceBox<String>> playerTypeChoiceBoxes;
    private boolean tournamentMode;
    private ObservableList<String> selectedMaps = FXCollections.observableArrayList();
    private ObservableList<String> playerTypes;
    private ArrayList<String> filesPath = new ArrayList<>();
    private ArrayList<String> selectPlayerTypes = new ArrayList<>();

    /**
     * Default ctor
     */
    public MenuController() {}


    /**
     * * Store reference of Model, View and MapController
     * @param model is the reference of Model
     * @param view is the reference of View
     * @param menu game menu
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
        selectedMapsListView.setItems(selectedMaps);
        addEventListener();
        SpinnerValueFactory<Integer> gamesPerMapValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1);
        gamesPerMapSpinner.setValueFactory(gamesPerMapValueFactory);
        SpinnerValueFactory<Integer> turnsPerGameValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 50, 10);
        turnsPerGameSpinner.setValueFactory(turnsPerGameValueFactory);
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
     * Show/Hide tournament mode relative UI
     * @param tournamentMode determines whether it's tournament or not
     */
    private void tournamentMode(boolean tournamentMode) {
        this.tournamentMode = tournamentMode;
        selectedMapsListView.setVisible(tournamentMode);
        deleteMapButton.setVisible(tournamentMode);
        gamesPerMapLabel.setVisible(tournamentMode);
        gamesPerMapSpinner.setVisible(tournamentMode);
        turnsPerGameLabel.setVisible(tournamentMode);
        turnsPerGameSpinner.setVisible(tournamentMode);
        if (tournamentMode) selectedMaps.clear();
    }


    /**
     * Called when users click StartNewGame Button
     */
    public void startNewGame() {
        System.out.println("Start New Game");
        tournamentMode(false);
        Model.isTournamentMode = false;
        startButton.setVisible(true);
        startButton1.setVisible(false);
        switchToSelectMapMenu();
    }


    /**
     * Called when users click Load Saved Game
     * @throws IOException exception
     * @throws ClassNotFoundException exception
     */
    public void loadGame() throws IOException,ClassNotFoundException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Saved Game");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Load Game (*.sg)", "*.sg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File savedGameFile = fileChooser.showOpenDialog(Menu.getInstance().getMenuStage());

        if (null != savedGameFile) {
            mapController.initPhaseView();

            String fileName = savedGameFile.getName();
            fileName = fileName.substring(0, fileName.length() - 3);
            fileName += "_";

            FileInputStream fileStream = new FileInputStream(fileName + "model.ser");
            ObjectInputStream os = new ObjectInputStream(fileStream);
            model = (Model) os.readObject();
            model.nonStaticToStatic();

            view.setModel(model);

            os.close();

            fileStream = new FileInputStream(fileName + "phase.ser");
            os = new ObjectInputStream(fileStream);

            Phase phase = (Phase)os.readObject();
            phase.setCurrentPhase("Start Up Phase");

            phase.addObserver(PhaseView.getInstance());
            phase.update();
            phase.setActionResult(Action.Show_Next_Phase_Button);
            phase.update();


            PlayersWorldDomination.getInstance().setTotalNumCountries(model.getCountries().size());
            PlayersWorldDomination.getInstance().setPlayers(model.getPlayers());
            PlayersWorldDomination.getInstance().addObserver(PlayersWorldDominationView.getInstance());
            PlayersWorldDomination.getInstance().update();

            os.close();

            mapController.setNumOfCountries(model.getCountries().size());
            model.startUp(mapController.createCountryViews());
            menu.hide();
            mapController.showMapStage();
        }
    }

    /**
     * Called when users click TournamentMode
     */
    public void tournamentMode() {
        System.out.println("Start Tournament Mode");
        tournamentMode(true);
        Model.isTournamentMode = true;
        startButton.setVisible(false);
        startButton1.setVisible(true);
        switchToSelectMapMenu();
    }


    /**
     * Switch to select map menu, hide irrelevant panes
     * Called when user click the new game button
     */
    private void switchToSelectMapMenu() {
        if (null == fileInfoMenuView && null == numPlayerMenuView) {
            fileInfoMenuView = new FileInfoMenuView();
            fileInfoMenuView.init(selectedFilenameLabel, mapValidationInfoLabel, selectedMaps, deleteMapButton);
            numPlayerMenuView = new NumPlayerMenuView();

            HashMap<Integer, Label> playerNumLabels;
            playerNumLabels = new HashMap<>();
            playerNumLabels.put(0, playerOneLabel);
            playerNumLabels.put(1, playerTwoLabel);
            playerNumLabels.put(2, playerThreeLabel);
            playerNumLabels.put(3, playerFourLabel);
            playerNumLabels.put(4, playerFiveLabel);
            playerNumLabels.put(5, playerSixLabel);

            playerTypeChoiceBoxes = new HashMap<>();
            playerTypeChoiceBoxes.put(0, playerOneTypeChoiceBox);
            playerTypeChoiceBoxes.put(1, playerTwoTypeChoiceBox);
            playerTypeChoiceBoxes.put(2, playerThreeTypeChoiceBox);
            playerTypeChoiceBoxes.put(3, playerFourTypeChoiceBox);
            playerTypeChoiceBoxes.put(4, playerFiveTypeChoiceBox);
            playerTypeChoiceBoxes.put(5, playerSixTypeChoiceBox);

            playerTypes = FXCollections.observableArrayList();
            numPlayerMenuView.init(numPlayerInstructionLabel, validationOfUserEnteredLabel, numPlayerTextField,
                    startButton, startButton1, mapController, playerNumLabels, playerTypeChoiceBoxes);
            model.setMenuViews(fileInfoMenuView, numPlayerMenuView);
        }
        if (null != fileInfoMenuView) {
            if (tournamentMode) {
                fileInfoMenuView.setTournament(true);
                numPlayerMenuView.setTournament(true);
            } else {
                fileInfoMenuView.setTournament(false);
                numPlayerMenuView.setTournament(false);
            }
        } else {
            System.out.println("MenuController.switchToSelectMapMenu: fileInfoMenuView is null");
            return;
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
        System.out.println("Selecting Map......");
        if (tournamentMode && 5 == selectedMaps.size()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Max map number is 5, remove first");
            alert.show();
            return;
        }
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Risk Map File");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Map files (*.map)", "*.map");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File riskMapFile = fileChooser.showOpenDialog(menu.getMenuStage());
        if (null != riskMapFile && riskMapFile.exists()) {
            fileInfoMenuView.setSelectedFilename(riskMapFile.getName());
            filesPath.add(riskMapFile.getPath());
            try {
                model.readFile(riskMapFile.getPath());
            } catch (IOException exception) {
                System.out.println("MenuController.readFile(): " + exception.getMessage());
            }
            System.out.println("Current Map Is : "+riskMapFile.getName());
        }
    }


    /**
     * Delete a selected map in tournament mode
     */
    public void deleteMap() {
        selectedMaps.remove(selectedMapsListView.getSelectionModel().getSelectedItem());
        if (0 == selectedMapsListView.getItems().size()) deleteMapButton.setDisable(true);
    }


    /**
     * Pass user entered string into NumPlayerMenuView
     * Update menu view to display the map number
     * Ask Model to initiate Player relative info
     * @param enteredPlayerNum is what user entered in the text field
     */
    private void validateEnteredNumPlayer(String enteredPlayerNum) {
        if (!tournamentMode) {
            playerTypes.clear();
            playerTypes.addAll("Human Player", "Aggressive Computer", "Benevolent Computer", "Random Computer", "Cheater Computer");
            for (int i = 0; i < 6; ++i) {
                playerTypeChoiceBoxes.get(i).setItems(playerTypes);
                playerTypeChoiceBoxes.get(i).getSelectionModel().selectFirst();
            }
        } else {
            playerTypes.clear();
            playerTypes.addAll("Aggressive Computer", "Benevolent Computer", "Random Computer", "Cheater Computer");
            for (int i = 0; i < 6; ++i) {
                playerTypeChoiceBoxes.get(i).setItems(playerTypes);
                playerTypeChoiceBoxes.get(i).getSelectionModel().selectFirst();
            }
        }
        numPlayerMenuView.setTotalNumPlayer(enteredPlayerNum);
        System.out.println("Total Player Number: "+enteredPlayerNum);
        model.checkPlayersNum(enteredPlayerNum);
    }


    /**
     * Called when user click the start button
     * Create PhaseView, CountryViews
     * Pass info to the Menu, Model, and MapController
     */
    public void startGame() {
        selectPlayerTypes.clear();
        for (int i = 0; i < numPlayerMenuView.getTotalNumPlayer(); ++i) {
            selectPlayerTypes.add(playerTypeChoiceBoxes.get(i).getValue());
        }
        System.out.println("Start Game");
        System.out.println("Player Type: " + selectPlayerTypes);
        System.out.println("Tournament Mode: " + tournamentMode);
        if (tournamentMode) {
            System.out.println("Selected Map: " + selectedMaps);
            System.out.println("Games per map: " + gamesPerMapSpinner.getValue());
            System.out.println("Turns per Game: " + turnsPerGameSpinner.getValue());
        }

        mapController.initPhaseView();
        model.initiatePlayers(selectPlayerTypes);
        model.startUp(mapController.createCountryViews());
        if (!tournamentMode) {
            menu.hide();
            mapController.showMapStage();
        }
        model.isComputerPlayer();
    }


    /**
     * Called when user confirm the quitGame process by clicking yes button
     */
    public void quitGame() {
        Menu.getInstance().close();
        mapController.quitGame();
    }


    /**
     * start tournament game
     */
    public void startTournamentGame(){
        SleepTime.setSleepTime(1);
        TournamentModel.startTournament(selectedMaps,filesPath,model,gamesPerMapSpinner,
                turnsPerGameSpinner,this,selectPlayerTypes);
        selectedMaps.clear();
        filesPath.clear();
        model.reset();
        model.resetValue();
        SleepTime.setSleepTime(500);
    }
}
