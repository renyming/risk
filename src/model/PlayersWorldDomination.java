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

    public String getCountryPercentage() {
        StringBuilder countryPercentage = new StringBuilder("Player    Percentage");
        if (0 != totalNumCountries) {
            for (Player player : players) {
                countryPercentage.append(player.getName()).append(": ").append(player.getCountriesOwned().size() * 1.0 / totalNumCountries).append("\n");
            }
        }
        return countryPercentage.toString();
    }

    public String getContinentNames() {
        StringBuilder continentNames = new StringBuilder("Player    Continents");
        for (Player player : players) {
            // TODO:
//            controlledContinents.append(player.getName()).append(": ").append(player.).append("\n");
        }
        return continentNames.toString();
    }

    public String getArmyDistribution() {
        StringBuilder armyDistribution = new StringBuilder("Player    Total Army");
        for (Player player : players) {
            // TODO:
            armyDistribution.append(player.getName()).append(": ").append(player.getTotalStrength()).append("\n");
        }
        return armyDistribution.toString();
    }
}
