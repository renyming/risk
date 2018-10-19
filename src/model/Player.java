package model;

import java.awt.*;
import java.util.*;

/**
 * Define class of a player
 * The following is only base code, further features need to be added
 */
public class Player extends Observable {

    //Unique Id for each player, starts from 1
    private int Id;
    //Counter to assign unique Id
    private static int cId=0;
    private String name;
    private int armies;
    private ArrayList<Country> countriesOwned;
    private String color = "#4B0082";


    /**
     * Constructor of player
     */
    public Player(String name){
        this.Id=++cId;
        this.name= name;
        armies = 0;
        countriesOwned = new ArrayList<>();
    }

    /**
     * Getter for color of player
     * @return Color of this player
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for color of player
     */
    public void setColor() {
        Random rand = new Random(Id*Id*Id);

        // Will produce only bright / light colours:
        float r = (float) (rand.nextFloat() / 2f + 0.4);
        float g = (float) (rand.nextFloat() / 2f + 0.4);
        float b = (float) (rand.nextFloat() / 2f + 0.4);

        Color randomColor = new Color(r, g, b);
        String hex = "#"+Integer.toHexString(randomColor.getRGB()).substring(2);

        this.color=hex;
    }

    /**
    * Getter to get Id
    * @return Id of the player
    */ 
    public int getId() {
        return Id;
    }

    /**
     * Getter to get name
     * @return  player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter to get the number of armies
     * @return The number of armies that the player has currently
     */
    public int getArmies() {
        return armies;
    }

    /**
     * Getter to get CountriesOwned
     * @return  The list of countries object that the player has currently
     */
    public ArrayList<Country> getCountriesOwned() {
        return countriesOwned;
    }

    /**
    * set the number of armies
    * @param  armies the number of armies
    */
    public void setArmies(int armies) {
        this.armies = armies;

        setChanged();
        notifyObservers(this);
    }

    /**
    * set the countries owned
    * @param  countriesOwned the countries owned
    */
    public void setCountriesOwned(ArrayList<Country> countriesOwned) {
        this.countriesOwned = countriesOwned;

        setChanged();
        notifyObservers(this);
    }

    /**
    * Add armies in the very first of the reinforcement phase
    * The number of armies added is computed based on the number of countries and cards it has
    */
    public void addRoundArmies(){

        int newArmies = armies + getArmiesAdded();
        setArmies(newArmies);
    }

    /**
    * Compute the armiesAdded based on the number of countries continent and cards it has
    * @return armies need to be added
    */
    private int getArmiesAdded() {

        int armiesAdded = 0;

        // based on countries num
        if (countriesOwned.size() > 0) {
            armiesAdded = countriesOwned.size() / 3;
        }

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
    * @return the number of armies need to be added based on the continents it has
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
    */
    public void subArmies(int num){

        int newArmies = armies - num;
        setArmies(newArmies);
    }

    /**
    * Verify if the armies is empty
    * @return  True if armies == 0, else False
    */
    public boolean isEmptyArmy() { return armies == 0 ? true : false; }

    /**
    * Add a country in the countriesOwned list
    * @param  c country need to be added
    */
    public void addCountry(Country c){

        //verify if the country is exist in the countriesOwned??
        countriesOwned.add(c);

        setChanged();
        notifyObservers(this);
    }

    /**
    * Remove a country from the countriesOwned list
    * @param  c country need to be deleted
    * @return true delete success, false delete failed
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

    /**
     * Helper method to set change state and notify observers
     */
    public void callObservers() {
        setChanged();
        notifyObservers();
    }

    public boolean isContain(Country c) {

        for (Country each : countriesOwned) {
            if (c.equals(each)) {
                return true;
            }
        }
        return false;
    }


    public boolean isConnected(Country start, Country end) {

        // if doesn't contain both of countries
        if (!isContain(start) ||  !isContain(end)) {
            return false;
        }

        //both contain, BFS find a path
        for (Country c : countriesOwned) {
            if (!c.equals(start)) {
                c.setProcessed(false);
            }
        }

        Queue<Country> queue = new PriorityQueue<Country>();
        queue.add(start);
        start.setProcessed(true);

        while (queue.isEmpty() == false) {
            Country c = queue.poll();

            ArrayList<Country> adjCountries = c.getAdjCountries();

            if ( adjCountries != null && adjCountries.size() != 0) {

                for(Country each : adjCountries) {

                    if (each.equals(end))  return true;

                    if (countriesOwned.contains(each) && !each.isProcessed()) {
                        each.setProcessed(true);
                        queue.add(each);
                    }
                }
            }
        }
        return false;
    }

    /**
     * verify if two users is equal
     * @param p Player need to be compare
     */
    public boolean equals(Player p) {
        if (this.getId() == p.getId()) {
            return true;
        }
        return false;
    }
}
