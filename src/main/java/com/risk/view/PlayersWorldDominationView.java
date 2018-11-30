package com.risk.view;

import com.risk.model.Model;
import com.risk.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import com.risk.model.PlayersWorldDomination;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    private AnchorPane countryPercentagePane;
    private ObservableList<String> allPlayerCountryPercentage = FXCollections.observableArrayList();
    private ObservableList<String> allPlayerArmyDistribution = FXCollections.observableArrayList();
    private ObservableList<String> allPlayerContinentName = FXCollections.observableArrayList();
    private Model model;
    private PieChart countryChart;
    private ArrayList<String> pieChartColor;


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
     * @param countryChart pie chart
     * @param model model
     * @param countryPercentagePane pane
     * @param countryPercentageListView is the map component for displaying the country percentage
     * @param armyDistributionListView is the map component for displaying the army distribution
     * @param continentNameListView is the map component for displaying the continent name
     */
    public void init(AnchorPane countryPercentagePane, ListView<String> countryPercentageListView, ListView<String> armyDistributionListView, ListView<String> continentNameListView, Model model, PieChart countryChart) {
        this.countryPercentagePane = countryPercentagePane;
        countryPercentageListView.setItems(allPlayerCountryPercentage);
        armyDistributionListView.setItems(allPlayerArmyDistribution);
        continentNameListView.setItems(allPlayerContinentName);
        this.model = model;
        this.countryChart = countryChart;
        this.pieChartColor = new ArrayList<>();
    }

    /**
     * populate country domination percentage of players to pie chart
     */
    private void populateCountryDominationData() {
        HashMap<Player, Double> playerTerPercent = PlayersWorldDomination.getInstance().setCountryDominationData(model);
        ArrayList<PieChart.Data> chartData = new ArrayList<>();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<Player, Double> entry : playerTerPercent.entrySet()) {
            double percentage = Math.round(entry.getValue() * 100.0) / 100.0;
            chartData.add(new PieChart.Data(entry.getKey().getName() + ": " + percentage + "%", entry.getValue()));
            pieChartColor.add(entry.getKey().getColor());
        }
        pieChartData.addAll(chartData);
        countryChart.setData(pieChartData);
        countryChart.setLegendSide(Side.LEFT);
        countryChart.setTitle("Country Domination");
        countryChart.setLegendVisible(false);
        for (PieChart.Data data : pieChartData) {
            data.getNode().setStyle("-fx-pie-color: " + pieChartColor.get(0) + ";");
            pieChartColor.remove(0);
        }

    }


    /**
     * Standard Observer update method
     * @param obs is the Observable subject, which is the PlayersWorldDomination
     * @param obj is the additional update info
     */
    @Override
    public void update(Observable obs, Object obj) {

        // update country percentage
        allPlayerCountryPercentage.clear();
        allPlayerCountryPercentage.addAll(PlayersWorldDomination.getInstance().getCountryPercentage());
        if(!Model.isTournamentMode){
            countryPercentagePane.setVisible(false);
            allPlayerCountryPercentage.clear();
            populateCountryDominationData();
        }

        // update army distribution
        allPlayerArmyDistribution.clear();
        allPlayerArmyDistribution.addAll(PlayersWorldDomination.getInstance().getArmyDistribution());

        // update continent name
        allPlayerContinentName.clear();
        allPlayerContinentName.addAll(PlayersWorldDomination.getInstance().getContinentNames());
    }
}
