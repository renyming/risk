package view;

import javafx.scene.control.Label;
import model.FileInfoMenu;

import java.util.Observable;
import java.util.Observer;

public class FileInfoMenuView implements Observer {

    private Label selectedFilenameLabel;
    private Label mapValidationInfoLabel;

    public FileInfoMenuView() {}

    public void init(Label selectedFilenameLabel, Label mapValidationInfoLabel) {
        this.selectedFilenameLabel = selectedFilenameLabel;
        this.mapValidationInfoLabel = mapValidationInfoLabel;
    }

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

    // called by MenuController
    public void setSelectedFilename(String selectedFilename) {
        selectedFilenameLabel.setText("Selected Map: " + selectedFilename);
    }

    public void reset() {
        selectedFilenameLabel.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3");
        selectedFilenameLabel.setText("Selected Map: NONE");
        mapValidationInfoLabel.setVisible(false);
    }
}
