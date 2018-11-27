package com.risk.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.risk.model.FileInfoMenu;

import java.util.Observable;
import java.util.Observer;


/**
 * Observer class, display the selected file relative info
 */
public class FileInfoMenuView implements Observer {

    private Label selectedFilenameLabel;
    private Label mapValidationInfoLabel;
    private ObservableList<String> selectedMaps;
    private boolean tournamentMode;
    private String selectedFilename;
    private Button deleteMapButton;

    /**
     * default ctor
     */
    public FileInfoMenuView() {}


    /**
     * Initialize the relative menu components
     * @param selectedFilenameLabel displays the selected file name
     * @param mapValidationInfoLabel displays the validation result
     */
    public void init(Label selectedFilenameLabel, Label mapValidationInfoLabel, ObservableList<String> selectedMaps, Button deleteMapButton) {
        this.selectedFilenameLabel = selectedFilenameLabel;
        this.mapValidationInfoLabel = mapValidationInfoLabel;
        this.selectedMaps = selectedMaps;
        this.deleteMapButton = deleteMapButton;
    }


    /**
     * Standard Observer update method
     * @param obs is the Observable subject, which is FileInfoMenuView
     * @param obj is the additional passed info
     */
    @Override
    public void update(Observable obs, Object obj) {
        FileInfoMenu fileInfoMenu = (FileInfoMenu) obs;
        boolean valid = fileInfoMenu.getValid();
        if (!valid) {
            selectedFilenameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        } else {
            if (tournamentMode) {
                if (!selectedMaps.contains(selectedFilename)) {
                    selectedMaps.add(selectedFilename);
                    deleteMapButton.setDisable(false);
                }
            }
            selectedFilenameLabel.setStyle("-fx-border-color: #00ff00; -fx-border-width: 3");
        }
        mapValidationInfoLabel.setText(fileInfoMenu.getValidationInfo());
        mapValidationInfoLabel.setVisible(true);
    }


    /**
     * Set the selected file name
     * Called by MenuController
     * @param selectedFilename is the selected file name
     */
    public void setSelectedFilename(String selectedFilename) {
        this.selectedFilename = selectedFilename;
        selectedFilenameLabel.setText("Selected Map: " + selectedFilename);
    }


    /**
     * Reset the file selection relative menu components
     */
    public void reset() {
        selectedFilenameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        selectedFilenameLabel.setText("Selected Map: NONE");
        mapValidationInfoLabel.setVisible(false);
    }


    /**
     * Set tournament mode
     * @param tournamentMode determine weather it's tournament of not
     */
    public void setTournament(boolean tournamentMode) { this.tournamentMode = tournamentMode; }
}
