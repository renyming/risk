package model;


import java.util.ArrayList;

/**
 * @program: risk
 * @description: The player of the game
 * @author: Zhijing Ling
 * @create: 2018-10-14 19:04
 **/
public class User {

    private String name;
    private int armies;
    private ArrayList<Country> countriesOwned;

    /**
    * This is the constructor
    * @Param: name The name of the user
    * @Author: Zhijing Ling
    * @Date: 2018-10-14
    */
    public User(String name) {
        this.name = name;
        this.armies = 0;
        this.countriesOwned = new ArrayList<Country>();

    }

    /**
    * Getter to get name
    * @Param:  None
    * @return:  user's name
    * @Author: Zhijing Ling
    * @Date: 2018-10-14
    */
    public String getName() {
        return name;
    }

    /**
    * Getter to get the number of armies
    * @Param:  None
    * @return: The number of armies that the user has currently
    * @Author: Zhijing Ling
    * @Date: 2018-10-14
    */
    public int getArmies() {
        return armies;
    }

    /**
    * Getter to get CountriesOwned
    * @Param:  None
    * @return:  The list of countries object that the user has currently
    * @Author: Zhijing Ling
    * @Date: 2018-10-14
    */
    public ArrayList<Country> getCountriesOwned() {
        return countriesOwned;
    }


}
