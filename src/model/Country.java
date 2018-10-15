package model;

/**
 * Define class of a country
 * The following is only base code, further features need to be added
 */
public class Country {

    //Unique ID for each country, starts from 1
    private int ID;
    //Counter to assign unique ID
    private static int cID=0;
    private String name;
    private Continent continent;
    private Player player;
    private int cArmy;

    /**
     * Constructor of Country
     * @param name The name of new country
     * @param continent The continent it belongs to
     */
    public Country(String name, Continent continent){
        this.name=name;
        this.ID=++cID;
        this.continent=continent;
        this.player=null;
        this.cArmy=0;
    }

    /**
    * Getter to get the continent it belongs to
    * @Param:  None
    * @return:  continent
    */
    public Continent getContinent() {
        return continent;
    }

    /**
     * Set the ownership of a country to a player
     * @param player The player who owns the country
     */
    public void setPlayer(Player player){
        this.player=player;
    }

    /** 
    * Verify if they are the same contry according the ID
    * @Param:  c contry need to be compared
    * @return:  true same country, false different country
    */ 
    public boolean equals(Country c) {
        return ID == c.ID? true : false;
    }


}
