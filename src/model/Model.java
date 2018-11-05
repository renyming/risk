package model;

import common.Message;
import common.STATE;
import validate.MapValidator;
import view.FileInfoMenuView;
import view.NumPlayerMenuView;
import view.PlayerView;
import view.CountryView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
    private boolean validFile = true;

    private FileInfoMenu fileInfoMenu;
    private NumPlayerMenu numPlayerMenu;

//    private String[] userColors = {"#FFD700","#FFFF00","#F4A460","#7CFC00","#00FFFF","#FF4500","#E9967A","#BA55D3","#FFB6C1","#FF00FF"};
//    private String[] continentColors = {"#000080","#800080","#800000","#006400","#778899","#000000","#FFD700"};

    /**
     * ctor for Model
     */
    public Model(){
        players = new ArrayList<>();
        countries = new HashMap<>();
        continents = new ArrayList<>();
        playerCounter = 0;
    }

    /**
     * rest model object before reload mapfile
     */
    private void rest(){
        players = new ArrayList<>();
        countries = new HashMap<>();
        continents = new ArrayList<>();
        validFile = true;
    }

    /**
     * get current player
     * @return current player
     */
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     *  notify the view that model state has changed
     * @param message The message to send to the view, may include some important information
     */
    private void notify(Message message) {
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
        if(source.getArmies()<armyNumber || !source.getOwner().isConnected(source,target))
            return;

        source.setArmies(source.getArmies()-armyNumber);
        target.setArmies(target.getArmies()+armyNumber);

        Message message = new Message(STATE.NEXT_PLAYER,null);
        notify(message);
    }

    /**
     * attack phase method
     */
    public void attack(Country attacker, int attackerDiceNum, Country attacked, int attackedDiceNum){
       attacker.getOwner().attack(attacker, attackerDiceNum, attacked, attackedDiceNum);
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
     * again, meanwhile change current player to the first player.
     */
    public void nextPlayer(){
        int currentId = currentPlayer.getId();
        //can be achieved by players rather than getNumOfPlayer()
        int numPlayer = players.size();
        //wraps around the bounds of ID
        int nextId = (currentId%numPlayer+numPlayer)%numPlayer+1;
        currentPlayer=players.get(nextId-1);

        //The next player is the first player, current round ended, send STATE message
        if (nextId == 1) {
            Message message = new Message(STATE.ROUND_ROBIN, null);
            notify(message);
        } else {
            //CurrentPlayer notifies view to update
            currentPlayer.callObservers();
        }
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

        players.clear();

        playerCounter = numOfPlayers;
        int initialArmies = 50/numOfPlayers;

        for (int i = 0; i < numOfPlayers; i++){

            Player newPlayer = new Player("Player" + String.valueOf(i));
            newPlayer.setArmies(initialArmies);
            //assign each player a different color
            newPlayer.setColor();
            //add observer(playerView)
            newPlayer.addObserver(playerView);
            //newPlayer.callObservers();
            players.add(newPlayer);
        }

        ArrayList<String> shuffle = new ArrayList<>();

        //assign countries to all the players justly
        for (String key:countries.keySet()) {
            shuffle.add(key);
        }
        for(int i = 0; i < shuffle.size(); i ++){
            countries.get(shuffle.get(i)).setPlayer(players.get(i % players.size()));
            players.get(i % players.size()).addCountry(countries.get(shuffle.get(i)));
        }

        //notify view to unpdate information
        for (String key:countries.keySet()) {
            countries.get(key).callObservers();
        }
        //current player notify
        currentPlayer = players.get(0);
        currentPlayer.callObservers();
        //give state to view
        Message message = new Message(STATE.INIT_ARMIES,null);
        notify(message);
        
    }

    /**
     * load the map from a player chose map file
     * validate map file
     * initiate continents,countries and players
     * notify View
     * @param filePath The path of the map file
     * @throws IOException io exceptions
     */
    public void readFile(String filePath) throws IOException {
        rest();
        String content = "";
        String line = "";
        String bodies[];
        FileReader fileReader = new FileReader(filePath);
        BufferedReader in = new BufferedReader(fileReader);
        while (line != null){
            content = content + line + "\n";
            line = in.readLine();
        }
        //validate map file
        try {
            bodies = content.split("\n\n");
            initiateContinents(bodies[1]);
            for(int i = 2; i < bodies.length; i ++){
                initiateCountries(bodies[i]);
            }
        } catch (Exception ex){
            validFile = false;
//            System.out.println("adasafa");
        }
        Message message;
        if(validFile){
            message = new Message(STATE.CREATE_OBSERVERS,countries.size());
        } else {
            message = new Message(STATE.LOAD_FILE,"invalid file format!");
            notify(message);
            return;
        }

        try {
            MapValidator.validateMap(this);
        }
        catch (Exception ex){
            message = new Message(STATE.LOAD_FILE,ex.getMessage());
            //ex.getMessage();
            System.out.println(ex.toString());
            notify(message);
            return;
        }
        notify(message);
    }

    /**
     * initiate continents
     * @param continentsList The list of continents
     */
    private void initiateContinents(String continentsList){
        int index = continentsList.indexOf("[Continents]");
        continentsList = continentsList.substring(index + 13);
        String[] list = continentsList.split("\n");
        int i = 0;
        for (String s : list) {
            int indexOfCol = s.indexOf('=');
            String continentName = s.substring(0,indexOfCol);
            int controlVal = Integer.parseInt(s.substring(indexOfCol + 1));
            Continent newContinent = new Continent(continentName,controlVal);
            newContinent.setColor();
            this.continents.add(newContinent);
            i ++;
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

            //add country to corresponding continents
            continents.get(indexOfContinent).addCountry(countries.get(contents[0]));

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

    /**
     * set continents list of model
     * @param continents  The continents to set
     */
    public void setContinents(List<Continent> continents) { this.continents = (ArrayList<Continent>) continents; }

    /**
     * report if the loaded map file is valid or not
     * @return true if the map file is valid; otherwise return false
     */
    public boolean isValidFile() {
        return validFile;
    }

    /**
     * Receive two menu observer references, bind them with corresponding observable subjects
     * @param fileInfoMenuView displays the general selected file info
     * @param numPlayerMenuView displays the num of players info
     */
    public void setMenuViews(FileInfoMenuView fileInfoMenuView, NumPlayerMenuView numPlayerMenuView) {
        fileInfoMenu = new FileInfoMenu();
        fileInfoMenu.addObserver(fileInfoMenuView);
        numPlayerMenu = new NumPlayerMenu();
        numPlayerMenu.addObserver(numPlayerMenuView);
    }
}
