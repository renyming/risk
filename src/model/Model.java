package model;

import java.util.Observable;

public class Model extends Observable {

    /**
     * for test purpose
     */
    public void changeSomething() {
        setChanged();
        notifyObservers();
    }
}
