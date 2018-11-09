package com.risk.model;

import java.util.ArrayList;
import java.util.Observable;


/**
 * Observable PlayerWorldDomination class, display
 * 1) the percentage of the map controlled by every player
 * 2) the continents controlled by every player
 * 3) the total number of armies owned by every player
 */
public class PlayersWorldDomination extends Observable {

    private static PlayersWorldDomination instance;

    private ArrayList<Player> players;
    private int totalNumCountries;


    /**
     * ctor
     */
    private PlayersWorldDomination() {}


    /**
     * Static get instance method, get the instance,
     * if the instance has not been initialized, then new one
     * @return the instance
     */
    public static PlayersWorldDomination getInstance() {
        if (null == instance) instance = new PlayersWorldDomination();
        return instance;
    }


    /**
     * Model call this method after calling all of the following setter functions
     */
    public void update() {
        setChanged();
        notifyObservers();
    }

    /**
     * Model sets the total Player reference, use them to get each player info
     * @param players is the total Player reference
     */
    public void setPlayers(ArrayList<Player> players) { this.players = players; }

    /**
     * Model sets the total number of countries, use the value to calculate country percentage later
     * @param totalNumCountries is the total number of countries for the map
     */
    void setTotalNumCountries(int totalNumCountries) { this.totalNumCountries = totalNumCountries; }


    /**
     * Observer uses it to get the percentage of the map controlled by every player info
     * @return the percentage of the map controlled by every player info
     */
    public ArrayList<String> getCountryPercentage() {
        ArrayList<String> countryPercentage = new ArrayList<>();
        if (0 != totalNumCountries) {
            for (Player player : players) {
                countryPercentage.add(player.getName() + ":  " + player.getCountriesOwned().size() * 100.0 / totalNumCountries + "%");
            }
        }
        return countryPercentage;
    }


    /**
     * Observer uses it to get the total number of armies owned by every player info
     * @return the total number of armies owned by every player info
     */
    public ArrayList<String> getArmyDistribution() {
        ArrayList<String> armyDistribution = new ArrayList<>();
        for (Player player : players) {
            armyDistribution.add(player.getName() + ": " + player.getTotalStrength());
        }
        return armyDistribution;
    }


    /**
     * Observer uses it to get the continents controlled by every player
     * @return the continents controlled by every player
     */
    public ArrayList<String> getContinentNames() {
        ArrayList<String> continentNames = new ArrayList<>();
        for (Player player : players) {
            StringBuilder allContinentNamesPerPlayer = new StringBuilder();
            for (Continent continent : player.getContinentsOwned()) {
                allContinentNamesPerPlayer.append(continent.getName()).append(", ");
            }
            continentNames.add(player.getName() + ": " + allContinentNamesPerPlayer.toString());
        }
        return continentNames;
    }
}
