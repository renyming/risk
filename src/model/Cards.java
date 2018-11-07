package model;

import java.util.Observable;

public class Cards extends Observable {

    private static Cards instance;
    private Player currentPlayer;

    public static Cards getInstance() {
        if (null == instance) instance = new Cards();
        return instance;
    }

    public void update() {
        setChanged();
        notifyObservers();
    }

    public void display(){

    }

    public void hide(){

    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() { return currentPlayer; }
}
