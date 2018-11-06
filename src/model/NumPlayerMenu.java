package model;

import java.util.Observable;


/**
 * Instruction:
 *      Within Model's loadFile() method
 *          if file is valid
 *              call setVisible(true);
 *              call setValidationResult(false, "Total Player: NONE")
 *              call setMaxNumPlayer(int)
 *          else
 *              call setVisible(false)
 *          then call update();
 *      Within Model's initiatePlayer() method
 *          if player number is valid
 *              call setValidationResult(true, "")
 *          else
 *              call setValidationResult(false, "invalid Info")
 *          then call update()
 */
public class NumPlayerMenu extends Observable {

    private boolean visible;
    private boolean valid;
    private String validationInfo;
    private int maxNumPlayer;

    NumPlayerMenu() {}

    public void update() {
        setChanged();
        notifyObservers();
    }

    void setVisible(boolean visible) { this.visible = visible; }

    void setMaxNumPlayer(int maxNumPlayer) {this.maxNumPlayer = maxNumPlayer; }

    void setValidationResult(boolean valid, String validationInfo) {
        this.valid = valid;
        this.validationInfo = valid ? "Total Player: " + maxNumPlayer : validationInfo;
    }

    public boolean getVisible() { return visible; }

    public boolean getValid() { return valid; }

    public int getMaxNumPlayer() { return maxNumPlayer; }

    public String getValidationInfo() { return validationInfo; }
}
