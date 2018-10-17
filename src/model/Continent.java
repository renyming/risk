package model;

import java.util.ArrayList;

/**
 * Define class of a continent
 * The following is only base code, further features need to be added
 */
public class Continent {

    //Unique ID for each continent, starts from 1
    private int ID = 0;
    //Counter to assign unique ID
    private static int cID=0;
    private String name;
    // additional number of armies given to players when he hold the whole continent
    private int controlVal;
    //May change ArrayList to other data structure later
    private ArrayList<Country> contries;

    /**
     * Constructor of Continent
     * @param name The name of new continent
     * @param controlVal Additional number of armies given to players when he hold the whole continent
     */
    public Continent(String name, int controlVal){
        this.name=name;
        this.ID=++cID;
        this.controlVal = controlVal;
        contries = new ArrayList<Country>();
    }

    /**
     * get continent name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * set continent name
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Get the controlVal
    * @return  controlVal
    */
    public int getControlVal() {
        return controlVal;
    }

    /**
     * Get all countries of a continent
     * @return List of all countries belong to the continent
     */
    public ArrayList<Country> getCountry(){
        return contries;
    }

    /**
     * Add a country to continent
     * @param country Country to be added
     */
    public void addCountry(Country country){
        contries.add(country);
    }

    /**
     * Get the number of countries of a continent
     * @return The number of countries of a continent
     */
    public int getSize(){
        return contries.size();
    }

    public int getID(){
        return ID;
    }

    /**
     * Assert if the continent has no country associated with it
     * @return true-no country belongs to this continent, otherwise false
     */
    public boolean isEmpty(){
        return contries.isEmpty();
    }

    /**
    * verify if two continent is same continent
    * @Param  c continent need to be compare
    * @return  true same continent, false  different continent
    */
    public boolean equals(Continent c) {

        if (this.name.equals(c.name)) {
            return true;
        }
        return false;
    }
    

}
