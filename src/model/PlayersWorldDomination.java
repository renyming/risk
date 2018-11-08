package model;

import java.util.ArrayList;
import java.util.Observable;

public class PlayersWorldDomination extends Observable {

    private static PlayersWorldDomination instance;

    private ArrayList<Player> players;
    private int totalNumCountries;

    private PlayersWorldDomination() {}

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
    public void setTotalNumCountries(int totalNumCountries) { this.totalNumCountries = totalNumCountries; }

    public ArrayList<String> getCountryPercentage() {
        ArrayList<String> countryPercentage = new ArrayList<>();
        if (0 != totalNumCountries) {
            for (Player player : players) {
                countryPercentage.add(player.getName() + ":  " + player.getCountriesOwned().size() * 1.0 / totalNumCountries + "%");
            }
        }
        return countryPercentage;
    }

    public ArrayList<String> getArmyDistribution() {
        ArrayList<String> armyDistribution = new ArrayList<>();
        for (Player player : players) {
            armyDistribution.add(player.getName() + ": " + player.getTotalStrength());
        }
        return armyDistribution;
    }

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
