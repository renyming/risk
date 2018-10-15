package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

/**
 * Define class of a player
 * The following is only base code, further features need to be added
 */
public class Player extends Observable {

    //Unique ID for each player, starts from 1
    private int ID;
    //Counter to assign unique ID
    private static int cID=0;
    private String name;
    private int armies;
    private ArrayList<Country> countriesOwned;

    /**
     * Constructor of player
     * @param name The name of new player
     */
    public Player(String name){
        this.name=name;
        this.ID=++cID;
        armies = 0;
        countriesOwned = new ArrayList<Country>();
    }

    /** 
    * Getter to get ID
    * @Param:  
    * @return: id of the player
    */ 
    public int getID() {
        return ID;
    }

    /**
     * Getter to get name
     * @Param:  None
     * @return:  player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter to get the number of armies
     * @Param:  None
     * @return: The number of armies that the player has currently
     */
    public int getArmies() {
        return armies;
    }

    /**
     * Getter to get CountriesOwned
     * @Param:  None
     * @return:  The list of countries object that the player has currently
     */
    public ArrayList<Country> getCountriesOwned() {
        return countriesOwned;
    }

    /**
    * set the number of armies
    * @Param:  armies the number of armies
    * @return:  void
    */
    public void setArmies(int armies) {
        this.armies = armies;

        setChanged();
        notifyObservers(this);
    }

    /**
    * set the countries owned
    * @Param:  countriesOwned the countries owned
    * @return:  void
    */
    public void setCountriesOwned(ArrayList<Country> countriesOwned) {
        this.countriesOwned = countriesOwned;

        setChanged();
        notifyObservers(this);
    }

    /**
    * add armies in the startup phase, before the first round of allocated armies
    * the initial number of armies is 15
    * @Param:
    * @return:
    */
    public void addInitArmies(){
        setArmies(15);
    }

    /**
    * Add armies in the very first of the reinforcement phase
    * The number of armies added is computed based on the number of countries and cards it has
    * @Param:  None
    * @return:  None
    */
    public void addRoundArmies(){

        int newArmies = armies + getArmiesAdded();
        setArmies(newArmies);
    }

    /**
    * Compute the armiesAdded based on the number of countries continent and cards it has
    * @Param: None
    * @return: armies need to be added
    */
    private int getArmiesAdded() {

        // based on countries num
        int armiesAdded = countriesOwned.size() / 3;

        //based on continent
        armiesAdded += getArmiesAddedFromContinent();

        //based on card
        //need to implement next phase

        // the minimal number of reinforcement armies is 3
        if (armiesAdded < 3) {
            armiesAdded = 3;
        }

        return armiesAdded;

    }

    /**
    * Compute the armiesAdded based on the continents it has
    * @Param:
    * @return:
    */
    private int getArmiesAddedFromContinent() {

        // record the number of countries in a continent
        HashMap<Continent, Integer> continentStatics = new HashMap<Continent, Integer>();

        for (Country country : countriesOwned) {

            Continent continent = country.getContinent();

            if (continentStatics.containsKey(continent)){

                int newValue = continentStatics.get(continent) + 1;
                continentStatics.put(continent, newValue);

            } else {
                continentStatics.put(continent, 1);
            }
        }

        //
        int armiesAdded = 0;
        for (Continent c : continentStatics.keySet()) {

            if (c.getSize() == continentStatics.get(c)) {
                armiesAdded += c.getControlVal();
            }
        }

        return armiesAdded;
    }


    /**
    * Substract one for armies when allocated army in the initArmy() or the reinforcements phase
    * @Param: None
    * @return: void
    */
    public void subArmies(int num){

        int newArmies = armies - num;
        setArmies(newArmies);
    }

    /**
    * Verify if the armies is empty
    * @Param:  None
    * @return:  True if armies == 0, else False
    */
    public boolean isEmptyArmy() { return armies == 0 ? true : false; }

    /**
    * Add a country in the countriesOwned list
    * @Param:  c country need to be added
    * @return:  void
    */
    public void addCountry(Country c){

        //verify if the country is exist in the countriesOwned??
        countriesOwned.add(c);

        setChanged();
        notifyObservers(this);
    }

    /**
    * Remove a country from the countriesOwned list
    * @Param:  c country need to be deleted
    * @return: true delete success, false delete failed
    */
    public boolean delCountry(Country c){

        Iterator<Country> it = countriesOwned.iterator();
        while(it.hasNext())
        {
            if (c.equals(it.next())){
                it.remove();

                setChanged();
                notifyObservers(this);
                return true;
            }
        }
        return false;
    }


}
