package model;

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

    /**
     * Constructor of player
     * @param name The name of new player
     */
    public Player(String name){
        this.name=name;
        this.ID=++cID;
    }

}
