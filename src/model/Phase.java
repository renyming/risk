package model;

import java.util.Observable;
import common.Action;


public class Phase extends Observable {

    private static Phase instance;

    private String currentPhase;
    private Player currentPlayer;
    private Action actionResult;

    private String invalidInfo;

    private Phase() { actionResult = Action.None; }

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

    public String getCurrentPhase() { return currentPhase; }


    /**
     * Called when the Model change the current player to the next one
     * @param currentPlayer is the next Player reference
     */
    void setCurrentPlayer(Player currentPlayer) {this.currentPlayer = currentPlayer; }

    public Player getCurrentPlayer() { return currentPlayer; }


    /**
     * Model call this to help phase to update UI, show buttons, hide panes, etc
     * i.e. actionResult = Allocate_Army, etc
     * @param actionResult is the action that are taking place during a phase
     */
    void setActionResult(Action actionResult) { this.actionResult = actionResult; }

    public Action getActionResult() { return actionResult; }


    /**
     * Model call this to help user to identify the invalid action which is performed during a phase
     * i.e. invalidInfo = "Select one of your own countries", "There is no path between ...", etc
     * @param invalidInfo is the user invalid action info
     */
    void setInvalidInfo(String invalidInfo) { this.invalidInfo = invalidInfo; }

    public String getInvalidInfo() { return invalidInfo; }


}
