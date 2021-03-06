package com.risk.model;

import java.io.Serializable;
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
public class NumPlayerMenu extends Observable implements Serializable {

    private boolean visible;
    private boolean valid;
    private String validationInfo;
    private int maxNumPlayer;


    /**
     * default ctor
     */
    public NumPlayerMenu() {}


    /**
     * combined notify observer method
     */
    public void update() {
        setChanged();
        notifyObservers();
    }


    /**
     * Display the NumPlayerMenu or not
     * @param visible determines whether this menu is to be displayed or not
     */
    void setVisible(boolean visible) { this.visible = visible; }


    /**
     * Set the max number of player for the selected map
     * @param maxNumPlayer is the max number of players
     */
    void setMaxNumPlayer(int maxNumPlayer) {this.maxNumPlayer = maxNumPlayer; }


    /**
     * Display the feedback about the entered player number
     * @param valid determines if the entered thing is valid
     * @param validationInfo shows additional feedback about the validation result
     */
    void setValidationResult(boolean valid, String validationInfo) {
        this.valid = valid;
        this.validationInfo = valid ? "Total Player: " + maxNumPlayer : validationInfo;
    }


    /**
     * Observer, which is NumPlayerMenuView, uses this method to update menu UI
     * @return true if menu should be displayed, false otherwise
     */
    public boolean getVisible() { return visible; }


    /**
     * Observer uses this method to show valid result
     * @return true if user entered thing is valid, false otherwise
     */
    public boolean getValid() { return valid; }


    /**
     * Observer uses this method display max number of player info
     * @return the max player number
     */
    public int getMaxNumPlayer() { return maxNumPlayer; }


    /**
     * Observer uses this method to display additional validation feedback
     * @return the additional validation result
     */
    public String getValidationInfo() { return validationInfo; }
}
