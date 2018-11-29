package com.risk.model;

import com.risk.common.Action;
import javafx.application.Platform;

public class WorkerThread extends Thread {

    Player player;
    Model model;

    public WorkerThread(Player player, Model model) {
        this.player=player;
        this.model=model;
    }

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                player.execute();
                if (Phase.getInstance().getActionResult() == Action.Win) {
                    return;
                }
                model.nextPlayer();
            }
        });
    }
}
