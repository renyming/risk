package model;

import java.util.List;
import java.util.Observable;

public class Model extends Observable {
    private List<Continent> continents;

    /**
     * @return the continents list
     */
    public List<Continent> getContinents() {
        return continents;
    }

    /**
     * for test purpose
     */
    public void changeSomething() {
        setChanged();
        notifyObservers();
    }
}
