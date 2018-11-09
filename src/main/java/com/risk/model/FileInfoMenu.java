package com.risk.model;

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


    /**
     * default ctor
     */
    public FileInfoMenu() {}


    /**
     * combined notify observer method
     */
    public void update() {
        setChanged();
        notifyObservers();
    }


    /**
     * Display the feedback about the selected file
     * @param valid determines if the select file is valid
     * @param validationInfo shows additional result about the validation result
     */
    void setValidationResult(boolean valid, String validationInfo) {
        this.valid = valid;
        this.validationInfo = validationInfo;
    }


    /**
     * Observer, which is FileInfoMenuView, uses this method to get the valid info
     * @return true if selected is valid, false otherwise
     */
    public boolean getValid() { return valid; }


    /**
     * Observer, which is FileInfoMenuView, uses this method to get the additional validation result
     * @return the additional validation result
     */
    public String getValidationInfo() { return validationInfo; }
}
