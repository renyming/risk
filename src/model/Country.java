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
public class Country extends Observable {

    //Unique ID for each country, starts from 1
    private int ID;
    //Counter to assign unique ID
    private static int cID=0;
    private String name;
    private Continent continent;
    private Player player;
    private int armies;
    private ArrayList<Country> adjCountries;
    private boolean isProcessed;


    /**
     * Constructor of Country
     * @param name The name of new country
     * @param continent The continent it belongs to
     * @param adjList The list of adjacent countries to current country
     */
    public Country(String name, Continent continent, ArrayList<Country> adjList){
        this.name=name;
        this.ID=++cID;
        this.continent=continent;
        this.player=null;
        this.armies =0;
        this.adjCountries=new ArrayList<>(adjList);
    }

    /**
    * Getter to get the continent it belongs to
    * @return  continent
    */
    public Continent getContinent() {
        return continent;
    }

    /**
     * Getter for number of armies of a country
     * @return The number of armies of a country
     */
    public int getArmies() {
        return armies;
    }

    /**
     * set army
     * @param armies
     */
    private void setArmies(int armies){
        this.armies=armies;
        callObservers();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name  the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param isProcessed the flat to validate map connection
     */
    public void setProcessed(boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    /**
     * Set the ownership of a country to a player
     * @param player The player who owns the country
     */
    public void setPlayer(Player player){
        this.player=player;
        callObservers();
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
        if (player.getArmies()<armies)
            return false;

        player.subArmies(armies);
        setArmies(getArmies()+armies);
        return true;
    }

    /**
     * Attack another country
     * @param attackedCountry Country being attacked
     * @return Whether the attack is valid, in another word, whether those two countries are adjacent
     */
    public boolean attack(Country attackedCountry){
        if (!adjCountries.contains(attackedCountry))
            return false;
        //TODO: implement attack phase
        attackedCountry.beingAttacked(this);
        return true;
    }

    /**
     * Country being attacked
     * @param attackingCountry Country performs attacking operation
     */
    private void beingAttacked(Country attackingCountry){
        return;
    }

    /**
     * Helper method to set change state and notify observers
     */
    private void callObservers() {
        setChanged();
        notifyObservers();
    }

    /**
     * Move some number of armies from one country to another (fortification phase)
     * @param targetCountry The country receives the armies
     * @param armies Number of armies on moving
     * @return false
     */
    public boolean moveArmiesTo(Country targetCountry, int armies){
        if (!adjCountries.contains(targetCountry) || getArmies()<armies)
            return false;

        this.setArmies(getArmies()-armies);
        targetCountry.setArmies(targetCountry.getArmies()+armies);
        return true;
    }

    /**
     * @return the adjacentCountries list
     */
    public ArrayList<Country> getAdjCountries() {
        return adjCountries;
    }


}
