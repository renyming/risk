package com.risk.view;

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


    /**
     * default ctor
     */
    public FileInfoMenuView() {}


    /**
     * Initialize the relative menu components
     * @param selectedFilenameLabel displays the selected file name
     * @param mapValidationInfoLabel displays the validation result
     */
    public void init(Label selectedFilenameLabel, Label mapValidationInfoLabel) {
        this.selectedFilenameLabel = selectedFilenameLabel;
        this.mapValidationInfoLabel = mapValidationInfoLabel;
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
    public void setSelectedFilename(String selectedFilename) { selectedFilenameLabel.setText("Selected Map: " + selectedFilename); }


    /**
     * Reset the file selection relative menu components
     */
    public void reset() {
        selectedFilenameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        selectedFilenameLabel.setText("Selected Map: NONE");
        mapValidationInfoLabel.setVisible(false);
    }
}
