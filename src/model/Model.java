package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    public void readFile(String filePath)throws IOException {

        String content = "";
        String line = "";
        String bodies[];
        FileReader fileReader = new FileReader(filePath);
        BufferedReader in = new BufferedReader(fileReader);
        while (line != null){
            content = content + line + "\n";
            line = in.readLine();
        }
        bodies = content.split("\n\n");
        initiateContinents(bodies[1]);
    }

    /**
     * initiate continents
     * @param continentsList The list of continents
     */
    private void initiateContinents(String continentsList){
        int index = continentsList.indexOf(']');
        continentsList = continentsList.substring(index + 2);
        String[] list = continentsList.split("\n");
        for (String s : list) {
            int indexOfCol = s.indexOf('=');
            String continentName = s.substring(0,indexOfCol);
            int controlVal = Integer.parseInt(s.substring(indexOfCol + 1));
            Continent newContinent = new Continent(continentName,controlVal);
            this.continents.add(newContinent);
        }
    }


    /**
     * get continenet list
     * @return continents list
     */
    public ArrayList<Continent> getContinents(){
        return continents;
    }

    /**
     * get country list
     * @return contries list
     */
    public ArrayList<Country> getCountries(){
        return countries;
    }

    /**
     * get player list
     * @return players list
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

}
