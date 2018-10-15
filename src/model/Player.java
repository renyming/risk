package model;

import java.util.ArrayList;

/**
 * Define class of a player
 * The following is only base code, further features need to be added
 */
public class Player {

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

}
