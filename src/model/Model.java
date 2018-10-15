package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Define Observable class
 * ...
 */


public class Model extends Observable {

    public enum State {READ_FILE,START_UP,REINFORCEMENT,FORTIFICATION}

    private State currentState;
    private Player currentPlayer;
    private int numOfCountries;
    private int numOfContinents;
    private ArrayList<Player> players;
    private ArrayList<Country> countries;
    private ArrayList<Continent> continents;

    /**
     * ctor for Model
     */
    public Model(){
        players = new ArrayList<>();
        countries = new ArrayList<>();
        continents = new ArrayList<>();
    }

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
