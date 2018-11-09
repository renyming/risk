package com.risk.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import com.risk.model.PlayersWorldDomination;

import java.util.Observable;
import java.util.Observer;


/**
 * Observer PlayerWorldDominationView class, display
 * 1) the percentage of the map controlled by every player
 * 2) the continents controlled by every player
 * 3) the total number of armies owned by every player
 */
public class PlayersWorldDominationView implements Observer {

    private static PlayersWorldDominationView instance;

    private ObservableList<String> allPlayerCountryPercentage = FXCollections.observableArrayList();
    private ObservableList<String> allPlayerArmyDistribution = FXCollections.observableArrayList();
    private ObservableList<String> allPlayerContinentName = FXCollections.observableArrayList();


    /**
     * Ctor
     */
    private PlayersWorldDominationView() {}


    /**
     * Singleton standard getter method, get the instance
     * @return the instance
     */
    public static PlayersWorldDominationView getInstance() {
        if (null == instance) instance = new PlayersWorldDominationView();
        return instance;
    }


    /**
     * Initialize the map component
     * @param countryPercentageListView is the map component for displaying the country percentage
     * @param armyDistributionListView is the map component for displaying the army distribution
     * @param continentNameListView is the map component for displaying the continent name
     */
    public void init(ListView<String> countryPercentageListView, ListView<String> armyDistributionListView, ListView<String> continentNameListView) {
        countryPercentageListView.setItems(allPlayerCountryPercentage);
        armyDistributionListView.setItems(allPlayerArmyDistribution);
        continentNameListView.setItems(allPlayerContinentName);
    }


    /**
     * Standard Observer update method
     * @param obs is the Observable subject, which is the PlayersWorldDomination
     * @param obj is the additional update info
     */
    @Override
    public void update(Observable obs, Object obj) {

        PlayersWorldDomination playersWorldDomination = (PlayersWorldDomination)obs;

        // update country percentage
        allPlayerCountryPercentage.clear();
        allPlayerCountryPercentage.addAll(playersWorldDomination.getCountryPercentage());

        // update army distribution
        allPlayerArmyDistribution.clear();
        allPlayerArmyDistribution.addAll(playersWorldDomination.getArmyDistribution());

        // update continent name
        allPlayerContinentName.clear();
        allPlayerContinentName.addAll(playersWorldDomination.getContinentNames());
    }
}
