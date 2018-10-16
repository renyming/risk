package model;

import common.Message;
import common.STATE;
import view.PlayerView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * Define Observable class
 * ...
 */


public class Model extends Observable {

    private static Player currentPlayer;
    private int numOfCountries;
    private int numOfContinents;
    private ArrayList<Player> players;

    private HashMap<String,Country> countries;

    private ArrayList<Continent> continents;

    private int playerCounter;


    /**
     * ctor for Model
     */
    public Model(){
        players = new ArrayList<>();
        countries = new HashMap<>();
        continents = new ArrayList<>();
    }

    /**
     * get current player
     * @return current player
     */
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * for test purpose
     * @param
     */
    public void notify(Message message) {
        setChanged();
        notifyObservers(message);
    }

    // TODO
    public void fortification(String country1, String country2, int armyNumber){
        //if success
        Message message = new Message(STATE.NEXT_PLAYER,null);
        notify(message);

    }

    /**
     * attack phase method
     */
    public void attack(){
        //TODO: NEED TO IMPLEMENT NEXT PHASE
    }

    /**
     *  Set new current player
     *  Add armies to the plaer
     */
    public void reinforcement(){
        nextPlayer();
        currentPlayer.addRoundArmies();
    }

    // TODO
    public void nextPlayer(){
        //change current player

        //if playercounter == 0
        Message message = new Message(STATE.REINFORCEMENT,null);
        notify(message);
    }


    /**
     * allocate one army in a specific counry
     * @param countryName country name
     */
    public void allocateArmy(String countryName){

        //country army + 1
        Country c = countries.get(countryName);
        c.addArmies(1);

        //player army - 1
        c.getOwner().subArmies(1);

    }

    /**
     * create Player object for every Player, and add the observer
     * Players are allocated a number of initial armies
     * notify CountryView (country info
     * notify PlayerView  (current player)
     * notify View (state and additional info)
     * @param numOfPlayers number of players
     * @param playerView  the observer
     */
    public void initiatePlayers(int numOfPlayers, PlayerView playerView){

        playerCounter = numOfPlayers;

        for (int i = 0; i < numOfPlayers; i ++){

            Player newPlayer = new Player("Player" + String.valueOf(i));
            newPlayer.addInitArmies();

            //add observer(playerView)
            newPlayer.addObserver(playerView);

            players.add(newPlayer);
        }

        //current player notify
        currentPlayer = players.get(0);
        currentPlayer.callObservers();

        //all country notify

        //give state
        Message message = new Message(STATE.INIT_ARMIES,null);
        notify(message);
    }

    /**
     * load the map from a player chose map file
     * initiate continents,countries and players
     * notify View
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
        for(int i = 2; i < bodies.length; i ++){
            initiateCountries(bodies[i]);
        }
        //if fail
        Message message = new Message(STATE.LOAD_FILE,false);
        //if ture
        message = new Message(STATE.CREATE_OBSERVERS,countries.size());
        notify(message);
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
     * initiate countries and all the neighbours
     * @param countriesList The list of countries
     */
    private void initiateCountries(String countriesList){
        if(countriesList.startsWith("[")){
            int index = countriesList.indexOf(']');
            countriesList = countriesList.substring(index + 2);
        }
        String[] list = countriesList.split("\n");
        for (String s : list) {
            String contents[] = s.split(",");
            String continentName = contents[3];
            int indexOfContinent = -1;
            for(int i = 0; i < continents.size(); i ++){
                if(continents.get(i).getName().equals(continentName)){
                    indexOfContinent = i;
                }
            }
            //current country does not exist in the map
            if(!countries.containsKey(contents[0])){
                Country newCountry = new Country(contents[0]);
                countries.put(contents[0],newCountry);
            }
            countries.get(contents[0]).setX(contents[1]);
            countries.get(contents[0]).setY(contents[2]);
            countries.get(contents[0]).setContinent(continents.get(indexOfContinent));

            //no adjacent neighbour
            if(contents.length <= 4)
                break;

            for(int i = 4; i < contents.length; i ++){
                //neighbour country does not exist in the map
                if(!countries.containsKey(contents[i])){
                    Country newCountry = new Country(contents[i]);
                    countries.put(contents[i],newCountry);
                }
                //add neighbours to current country
                countries.get(contents[0]).addEdge(countries.get(contents[i]));
            }
        }
    }

    /**
     *
     */
    public void linkCountryObservers(HashMap<Integer,CountryView>){

        //every country notify

//        Message message = new Message(STATE.PLAYER_NUMBER,null);
//
//        notify(message);
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
    public HashMap<String, Country> getCountries(){
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
