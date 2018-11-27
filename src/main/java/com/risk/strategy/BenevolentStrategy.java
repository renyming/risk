package com.risk.strategy;

import com.risk.common.Action;
import com.risk.model.Country;
import com.risk.model.Phase;
import com.risk.model.Player;

import java.util.Comparator;

public class BenevolentStrategy implements PlayerBehaviorStrategy {

    private Player player;
    private Phase phase;

    public BenevolentStrategy(Player player) {
        this.player = player;
        phase = Phase.getInstance();
    }

    @Override
    public void reinforcement() {

        // computer the armies that need to added roundly
        Phase.getInstance().setCurrentPhase("Reinforcement Phase");
        player.addRoundArmies();
        Phase.getInstance().update();

        // find the weakest country
        Country weakest = player.getCountriesOwned().stream()
                .min(Comparator.comparingInt(Country::getArmies))
                .get();

        // add all the armies to weakest
        weakest.addArmies(player.getArmies());
        player.setArmies(0);

        // update phase
        phase.setActionResult(Action.Show_Next_Phase_Button);
        phase.update();

    }

    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) {

    }

    @Override
    public void moveArmy(String num) {

    }

    @Override
    public void fortification(Country source, Country target, int armyNumber) {

        // target is the weakest country in the countries owned
        // find the weakest country
        Country weakest = player.getCountriesOwned().stream()
                .min(Comparator.comparingInt(Country::getArmies))
                .get();

        // source is the strongest contry that connected to the target(weakest)
        Country stronger = player.getCountriesOwned().stream()
                .filter(c -> player.isConnected(weakest, c) && c.getArmies() > weakest.getArmies())
                .max(Comparator.comparingInt(Country::getArmies))
                .get();

        // re-allocated armies
        int total = stronger.getArmies() + weakest.getArmies();
        stronger.setArmies(total/2);
        weakest.setArmies(total - total/2);

        Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        Phase.getInstance().update();



    }
}

