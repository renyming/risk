package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import model.PlayersWorldDomination;

import java.util.Observable;
import java.util.Observer;

public class PlayersWorldDominationView implements Observer {

    private static PlayersWorldDominationView instance;

    private ObservableList<String> allPlayerCountryPercentage = FXCollections.observableArrayList();
    private ObservableList<String> allPlayerArmyDistribution = FXCollections.observableArrayList();
    private ObservableList<String> allPlayerContinentName = FXCollections.observableArrayList();



    private PlayersWorldDominationView() {}

    public static PlayersWorldDominationView getInstance() {
        if (null == instance) instance = new PlayersWorldDominationView();
        return instance;
    }

    public void init(ListView<String> countryPercentageListView, ListView<String> armyDistributionListView, ListView<String> continentNameListView) {
        allPlayerCountryPercentage.addAll("Player_1: 5%", "Player_2: 10%", "Player_3: 15%", "Player_4: 20%", "Player_5: 25%", "Player_6: 30%"); // TODO remove later
        countryPercentageListView.setItems(allPlayerCountryPercentage);

        allPlayerArmyDistribution.addAll("Player_1: 5", "Player_2: 10", "Player_3: 15", "Player_4: 20", "Player_5: 25", "Player_6: 30"); // TODO remove later
        armyDistributionListView.setItems(allPlayerArmyDistribution);

        allPlayerContinentName.add("Player_1: Continent_1, Continent_2, Continent_3, Continent_4, Continent_5, Continent_6, Continent_7, Continent_8"); // TODO remove later
        continentNameListView.setItems(allPlayerContinentName);
    }

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
