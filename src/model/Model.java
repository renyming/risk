package model;

import common.Message;
import common.STATE;
import view.PlayerView;
import view.CountryView;

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

    /**
     * Method for fortification operation
     * @param source The country moves out army
     * @param target The country receives out army
     * @param armyNumber Number of armies to move
     */
    public void fortification(Country source, Country target, int armyNumber){
        //return no response to view if source country's army number is less than the number of armies on moving,
        //or the source and target countries aren't connected through the same player's countries
        if(source.getArmies()<armyNumber || source.getOwner().isConnected(source,target))
            return;

        source.setArmies(source.getArmies()-armyNumber);
        target.setArmies(target.getArmies()+armyNumber);

        Message message = new Message(STATE.NEXT_PLAYER,null);
        notify(message);

    }

    /**
     * attack phase method
     */
    public void attack(){
        //TODO: NEED TO IMPLEMENT IN NEXT BUILD
    }

    /**
     * Reinforcement phase
     * Set new current player
     * Add armies to the player
     */
    public void reinforcement(){
        //get armies for each round
        currentPlayer.addRoundArmies();
        //As the first step of each round, notify the change of current player to view
        currentPlayer.notifyObservers();

        //View already in ROUND_ROBIN state, then it finds out there's allocatable armies of this player, it will
        //show the "Allocate Armies" button
    }

    /**
     * Set current player to the next one according in round robin fashion
     * If a new round starts from the next player, send ROUND_ROBIN STATE to view
     * Considerations:
     * 1. When allocate armies at start up phase, when the last player finishes army allocation, this method tells view
     * round robin starts;
     * 2. In round robin, when the last player finishes fortification phase, this method tells view round robin starts
     * again, meanwhile change current player to the first player
     */
    public void nextPlayer(){
        int currentId = currentPlayer.getId();
        int numPlayer = getNumOfPlayer();
        //wraps around the bounds of ID
        int nextId = (currentId%numPlayer+numPlayer)%numPlayer;
        //The next player is the first player, current round ended, send STATE message
        if (nextId == 1) {
            Message message = new Message(STATE.ROUND_ROBIN, null);
            notify(message);
        }
    }

    /**
     * Getter for player number
     * @return Number of players
     */
    public int getNumOfPlayer() {
        return players.size();
    }

    /**
     * allocate one army in a specific counry
     * @param country Country reference
     */
    public void allocateArmy(Country country){

        //country army + 1
        country.addArmies(1);

        //player army - 1
        country.getOwner().subArmies(1);

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
            newPlayer.callObservers();
            players.add(newPlayer);
        }

        //current player notify
        currentPlayer = players.get(0);
        currentPlayer.callObservers();

        //notify all countriesView
        for (String key:countries.keySet()) {
            countries.get(key).callObservers();
        }

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
        Message message = new Message(STATE.LOAD_FILE,"Why invalid");
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
            if(contents.length <=  4)
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
     * link every country in the countries with corresponding CountryView
     * @param countryViewHashMap The CountryView map
     */
    public void linkCountryObservers(HashMap<Integer,CountryView> countryViewHashMap){

        //link every countryView
        int id = 1;
        for (String key:countries.keySet()) {
            countries.get(key).addObserver(countryViewHashMap.get(id));
            id ++;
        }
        //send next state message
        Message message = new Message(STATE.PLAYER_NUMBER,null);
        notify(message);
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
