package com.risk.model;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Define class of a continent
 * The following is only base code, further features need to be added
 */
public class Continent implements Serializable {

    //Unique ID for each continent, starts from 1
    private int ID = 0;
    //Counter to assign unique ID
    private static int cID=0;
    private String name;
    // additional number of armies given to players when he hold the whole continent
    private int controlVal;
    //May change ArrayList to other data structure later
    private ArrayList<Country> contries;
    private String color;

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
     * getter for continent color
     * @return  color of the continent
     */
    public String getColor(){
        return color;
    }

    /**
     * setter for continent color
     */
    public void setColor() {

        Random randomGenerator = new Random(ID);
        int red = randomGenerator.nextInt(200);
        int green = randomGenerator.nextInt(200);
        int blue = randomGenerator.nextInt(200);

        Color randomColour = new Color(red,green,blue);
        randomColour.darker();

        String hex = "#"+Integer.toHexString(randomColour.getRGB()).substring(2);
        this.color=hex;
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
     * Set the controlVal
     * @param value The value to set
     */
    public void setControlVal(int value) {
        this.controlVal = value;
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

    /**
     * Get the ID of the continent
     * @return ID of the continent
     */
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
    * @param obj continent need to be compare
    * @return  true same continent, false  different continent
    */
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Continent)) {
            return false;
        }
        Continent continent = (Continent) obj;
        return continent.getName().equalsIgnoreCase(name);
    }

}
