package model;

import java.util.List;
import java.util.Observable;

/**
 * Define Observable class
 * ...
 */

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

    /**
     * load the map
     * @param filePath The path of the map file
     */
    public void readFile(String filePath) {

    }



}
