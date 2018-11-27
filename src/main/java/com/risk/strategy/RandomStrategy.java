package com.risk.strategy;

import com.risk.common.Action;
import com.risk.model.Country;
import com.risk.model.Model;
import com.risk.model.Phase;
import com.risk.model.Player;

import java.util.ArrayList;
import java.util.Random;

public class RandomStrategy implements PlayerBehaviorStrategy {

    private Player player;
    private Phase phase;
    private Random random=new Random();

    public RandomStrategy(Player player) {
        this.player = player;
        phase = Phase.getInstance();
    }

    /**
     * Reinforcement operation for random player
     */
    @Override
    public void reinforcement() {
        //update current phase on view
        phase.setCurrentPhase("Reinforcement Phase");
        phase.update();

        player.addRoundArmies();
        Country country=getRandomOwnedCountry(player.getCountriesOwned());
        country.addArmies(player.getArmies());
        player.setArmies(0);

        //update result on view
        phase.setActionResult(Action.Show_Next_Phase_Button);
        phase.update();
        Model.phaseNumber=2;

    }

    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) {
        int randomNumAttacks=random.nextInt();


    }

    @Override
    public void moveArmy(String num) {

    }

    @Override
    public void fortification(Country source, Country target, int armyNumber) {

    }

    /**
     * Get a random country from the given country list
     * @param countryList Country list to randomly choose a country from
     * @return A random country
     */
    private Country getRandomOwnedCountry(ArrayList<Country> countryList) {
        int randomIdx=random.nextInt(countryList.size());
        return countryList.get(randomIdx);
    }



}
