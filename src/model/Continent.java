package model;

import java.util.ArrayList;

/**
 * Define class of a continent
 * The following is only base code, further features need to be added
 */
public class Continent {

    //Unique ID for each continent, starts from 1
    private int ID;
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
     */
    public Continent(String name, int controlVal){
        this.name=name;
        this.ID=++cID;
        this.controlVal = controlVal;
        contries=new ArrayList<>();
    }

    /**
     * Add a country to continent
     * @param country Country to be added
     */
    public void addCountry(Country country){
        contries.add(country);
    }

    /**
    * Get the controlVal
    * @Param:  None
    * @return:  controlVal
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
     * Get the number of countries of a continent
     * @return The number of countries of a continent
     */
    public int getSize(){
        return contries.size();
    }

    /**
     * Assert if the continent has no country associated with it
     * @return true-no country belongs to this continent, otherwise false
     */
    public boolean isEmpty(){
        return contries.isEmpty();
    }

    /**
     * get continent name
     * @return the name
     */
    public String getName() {
        return name;
    }


}
