package com.risk.model;

import java.util.Observable;
import com.risk.common.Action;


/**
 * Observable Phase class, store
 * 1) the name of the game phase currently being played
 * 2) the current player's name
 * 3) information about actions that are taking place during this phase
 */
public class Phase extends Observable {

    private static Phase instance;

    private String currentPhase;
    private Player currentPlayer;
    private Action actionResult;
    private String invalidInfo;


    /**
     * ctor
     */
    private Phase() { actionResult = Action.None; }


    /**
     * Static get instance method, get the instance,
     * if the instance has not been initialized, then new one
     * @return the instance
     */
    public static Phase getInstance() {
        if (null == instance) instance = new Phase();
        return instance;
    }


    /**
     * Model call this when finish calling any setXXX() method within this class
     * i.e. call setCurrentPhase("Start Up Phase"), then must call update()
     */
    public void update() {
        setChanged();
        notifyObservers();
    }


    /**
     * Model only update the current phase when the phase changed
     * i.e. currentPhase = "Start Up Phase", "Reinforcement Phase", "Attack Phase", "Fortification Phase"
     * @param currentPhase represent the current phase label name
     */
    public void setCurrentPhase(String currentPhase) { this.currentPhase = currentPhase; }


    /**
     * Observer uses it to set the current Phase label
     * @return the current phase
     */
    public String getCurrentPhase() { return currentPhase; }


    /**
     * Called when the Model change the current player to the next one
     * @param currentPlayer is the next Player reference
     */
    void setCurrentPlayer(Player currentPlayer) { this.currentPlayer = currentPlayer; }


    /**
     * Observer uses it to get the current Player reference, then update the relative part of UI
     * @return the current Player reference
     */
    public Player getCurrentPlayer() { return currentPlayer; }


    /**
     * Model call this to help phase to update UI, show buttons, hide panes, etc
     * i.e. actionResult = Allocate_Army, etc
     * @param actionResult is the action that are taking place during a phase
     */
    public void setActionResult(Action actionResult) { this.actionResult = actionResult; }


    /**
     * Observer uses it to get the current action within a phase
     * @return the current action
     */
    public Action getActionResult() {
        return actionResult;
    }


    /**
     * Clear current action
     */
    public void clearActionResult() {
        actionResult = Action.None;
    }


    /**
     * Model call this to help user to identify the invalid action which is performed during a phase
     * i.e. invalidInfo = "Select one of your own countries", "There is no path between ...", etc
     * @param invalidInfo is the user invalid action info
     */
    public void setInvalidInfo(String invalidInfo) { this.invalidInfo = invalidInfo; }


    /**
     * Model call this to set invalid info during each phase
     * @return the invalid info
     */
    public String getInvalidInfo() { return invalidInfo; }
}
