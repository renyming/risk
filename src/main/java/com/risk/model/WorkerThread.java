package com.risk.model;

import com.risk.common.Action;
import javafx.application.Platform;

/**
 * new thread to automatically run the strategy execute method
 */
public class WorkerThread extends Thread {

    Player player;
    Model model;

    /**
     * constructor of WorkerThread
     * @param player player obj need to bound
     * @param model model need to bound
     */
    public WorkerThread(Player player, Model model) {
        this.player=player;
        this.model=model;
    }

    /**
     * automatically run the strategy execute method
     */
    @Override
    public void run() {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                player.execute();
                Model.currentTurn++;
                if (Phase.getInstance().getActionResult() == Action.Win) {
                    return;
                }else if(Model.currentTurn >= Model.maxTurn){
                    return;
                }

                if (!model.isNextPlayerHuman()) {
                    model.nextPlayer();
                }
            }
        });
    }
}
