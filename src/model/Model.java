package model;

import common.Message;
import common.STATE;
import view.PlayerView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Define Observable class
 * ...
 */


public class Model extends Observable {

    public enum State {READ_FILE,START_UP,REINFORCEMENT,FORTIFICATION}

    private State currentState;
    private static Player currentPlayer;
    private int numOfCountries;
    private int numOfContinents;
    private ArrayList<Player> players;
    private ArrayList<Country> countries;
    private ArrayList<Continent> continents;



    private int playerCounter;


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
     * @param
     */
    public void notify(Message message) {
        setChanged();
        notifyObservers(message);
    }

    public void fortification(String country1, String country2, int armyNumber){
        //if success
        Message message = new Message(STATE.NEXT_PLAYER,null);


    }

    public void attack(){

    }

    public void getAddedArmies(){

    }

    public void reinforcement(){
        nextPlayer();
        getAddedArmies();
    }

    public void nextPlayer(){
        //change current player

        //if playercounter == 0
        Message message = new Message(STATE.REINFORCEMENT,null);
        notify(message);
    }

    public void allocateArmy(int countryId){
        //country army + 1
        //player army - 1

    }

    public void initiatePlayers(int numOfPlayers, PlayerView playerView){

        playerCounter = numOfPlayers;

        for (int i = 0; i < numOfPlayers; i ++){
            Player newPlayer = new Player("Player" + String.valueOf(i));
            players.add(newPlayer);
        }

        //give state
        //TODO：add observer(playerView)

        //current player notify
        //all country notify

        Message message = new Message(STATE.INIT_ARMIES,null);
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
        for(int i = 2; i < bodies.length; i ++){
            initiateCountries(bodies[i]);
        }

        //if fail
        Message message = new Message(STATE.LOAD_FILE,false);
        //if ture
        message = new Message(STATE.CREATE_OBSERVERS,true);

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
     * initiate countries
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
            Country newCountry = new Country(contents[0], continents.get(indexOfContinent));
            newCountry.setX(contents[1]);
            newCountry.setY(contents[2]);

            if(contents.length <= 4)
                break;

            for(int i = 4; i < contents.length; i ++){

            }
            countries.add(newCountry);
        }
    }

    public void linkCountryObservers(){

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
