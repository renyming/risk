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
    //May change ArrayList to other data structure later
    private ArrayList<Country> continent;

    /**
     * Constructor of Continent
     * @param name The name of new continent
     */
    public Continent(String name){
        this.name=name;
        this.ID=++cID;
        continent=new ArrayList<>();
    }

    /**
     * Add a country to continent
     * @param country Country to be added
     */
    public void addCountry(Country country){
        continent.add(country);
    }

    /**
     * Get all countries of a continent
     * @return List of all countries belong to the continent
     */
    public ArrayList<Country> getCountry(){
        return continent;
    }

    /**
     * Get the number of countries of a continent
     * @return The number of countries of a continent
     */
    public int getSize(){
        return continent.size();
    }

    /**
     * Assert if the continent has no country associated with it
     * @return true-no country belongs to this continent, otherwise false
     */
    public boolean isEmpty(){
        return continent.isEmpty();
    }
}
