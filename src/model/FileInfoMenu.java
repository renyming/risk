package model;

import java.util.Observable;


/**
 * Instruction:
 *      Within Model's loadFile() method
 *          if file is valid
 *              call setValidationResult(true, validInfo)
 *          else
 *              call setValidationResult(false, invalidInfo)
 *          then call update();
 */
public class FileInfoMenu extends Observable {

    private boolean valid;
    private String validationInfo;

    FileInfoMenu() {}

    public void update() {
        setChanged();
        notifyObservers();
    }

    void setValidationResult(boolean valid, String validationInfo) {
        this.valid = valid;
        this.validationInfo = validationInfo;
    }

    public boolean getValid() { return valid; }

    public String getValidationInfo() { return validationInfo; }
}
