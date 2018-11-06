package model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Define class of a country
 * The following is only base code, further features need to be added
 *
 * Note:
 *      1. No arg in notifyObservers() call because view will refresh all the elements on "Country" node;
 */
public class Country extends Observable implements Comparable<Country> {

    //Unique ID for each country, starts from 1
    private int ID;
    //Counter to assign unique ID
    private static int cID = 0;
    private String name;
    private Continent continent;
    private Player player;
    private int armies;
    private ArrayList<Country> adjCountries;
    // verify if the country is visited in BFS
    private boolean isProcessed;
    private double x;
    private double y;


    /**
     * Constructor of Country
     * @param name The name of new country
     * @param continent The continent it belongs to
     */
    public Country(String name, Continent continent){
        this.name = name;
        this.ID = ++cID;
        this.continent = continent;
        this.player = null;
        this.armies  = 0;
        this.adjCountries = new ArrayList<>();
        this.isProcessed  =  false;

    }

    /**
     * Constructor of Country
     * @param name The name of new country
     */
    public Country(String name){
        this.name = name;
        this.ID = ++cID;
        this.player = null;
        this.armies  = 0;
        this.adjCountries = new ArrayList<>();
        this.isProcessed  =  false;
    }

    /**
     * Add an adjacent country to the adjacent list
     * @param country Country that is adjacent to this country
     */
    public void addEdge(Country country){
        adjCountries.add(country);
    }

    /**
     * Getter for country ID
     * @return Country ID
     */
    public int getId(){ return ID; }

    /**
     * Getter for country owner
     * @return The player of this country
     */
    public Player getOwner() {return player;}

    /**
    * Getter for the continent it belongs to
    * @return  continent
    */
    public Continent getContinent() {
        return continent;
    }

    /**
     * Setter for belonging continent
     * @param continent The continent country belongs to
     */
    public void setContinent(Continent continent){
        this.continent = continent;
    }

    /**
     * Getter for number of armies of a country
     * @return The number of armies of a country
     */
    public int getArmies() {
        return armies;
    }


    /**
     * Overall handler for the change of armies, will call observers
     * @param armies Number of armies to set to
     */
    public void setArmies(int armies){
        this.armies = armies;
        callObservers();
    }

    /**
     * Getter for country name
     * @return The name of country
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for country name(for which situation?)
     * @param name The name to set to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Mark for processed node in map validation
     * @param isProcessed The flag to validate map connection
     */
    public void setProcessed(boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    /**
     * Getter for processed flag
     * @return the isProcessed
     */
    public boolean isProcessed() {
        return isProcessed;
    }

    /**
     * Set the ownership of a country to a player
     * @param player The player who owns the country
     */
    public void setPlayer(Player player){
        this.player = player;
        callObservers();
    }

    /**
     * Getter for y position
     * @return y position of the country
     */
    public double getY(){
        return y;
    }

    /**
     * Setter for y position
     * @param y The y position of the country
     */
    public void setY(String y){
        this.y = Double.parseDouble(y);
    }

    /**
     * Getter for x position
     * @return x position of the country
     */
    public double getX(){
        return x;
    }

    /**
     * Setter for x position
     * @param x The x position of the country
     */
    public void setX(String x){
        this.x = Double.parseDouble(x);
    }

    /** 
    * Verify if they are the same country according the ID
    * @param  c Country need to be compared
    * @return  true same country, false different country
    */ 
    public boolean equals(Country c) {
        return ID == c.ID? true : false;
    }

    /**
     * Add armies to a country
     * @param armies Number of armies to be added
     * @return Whether add operation is successful
     */
    public boolean addArmies(int armies){
        //Player doesn't have enough number of armies as specified
        if (player.getArmies() < armies)
            return false;
        setArmies(getArmies()+armies);
        return true;
    }

    /**
     * Attack another country
     * @param defenderCountry Country being defender
     * @return Whether the attack is valid, in another word, whether those two countries are adjacent
     */
    public boolean attack(Country defenderCountry){
        if (!adjCountries.contains(defenderCountry))
            return false;
        //TODO: implement attack phase
        defenderCountry.beingdefender(this);
        return true;
    }

    /**
     * Country being defender
     * @param attackingCountry Country performs attacking operation
     */
    private void beingdefender(Country attackingCountry){
        return;
    }

    /**
     * Helper method to set change state and notify observers
     */
    public void callObservers() {
        setChanged();
        notifyObservers();
    }

    /**
     * Test if two countries are adjacent
     * @param country The country to be tested
     * @return true-The country passed in argument is adjacent with current country, otherwise false
     */
    public boolean isAdjacent(Country country) {return adjCountries.contains(country);};

    /**
     * Getter for adjacent list of countries
     * @return The adjacentCountries list
     */
    public ArrayList<Country> getAdjCountries() {
        return adjCountries;
    }

    /**
     * Compare two counties based on their ID
     * @param c Country need to be compare
     * @return  returns a negative integer, zero, or a positive integer as this employee id,is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Country c) {
        return (this.ID - c.ID);
    }

    /**
     * Test if dice num is valid for a defender
     * @return If the dice is less than armies owned in the country, return true else return false
     */
    public boolean isValidDefender(int defenderDiceNum){

        if (this.armies > 0) {
            return defenderDiceNum <= this.armies && defenderDiceNum > 0 && defenderDiceNum <= 2;
        } else if (this.armies == 0){
            return defenderDiceNum == 0? true : false;
        } else {
            return false;
        }



    }

    /**
     * Test if dice num is valid for a attacker
     * @return If the dice is less than armies owned in the country, and more than 0, less than 3, return true else return false
     */
    public boolean isValidAttacker(int attackerDiceNum) {

        return this.armies >= 2 && attackerDiceNum <= this.armies && attackerDiceNum > 0 && attackerDiceNum <= 3;

    }


}
