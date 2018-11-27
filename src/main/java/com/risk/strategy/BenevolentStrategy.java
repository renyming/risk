package com.risk.strategy;

import com.risk.common.Action;
import com.risk.model.Country;
import com.risk.model.Phase;
import com.risk.model.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        phase.setActionResult(Action.Show_Next_Phase_Button);
        phase.update();

    }

    @Override
    public void moveArmy(String num) {

    }

    @Override
    public void fortification(Country source, Country target, int armyNumber) {

        List<Country> increaseSorted = player.getCountriesOwned().stream()
                .sorted(Comparator.comparingInt(Country::getArmies))
                .collect(Collectors.toList());

        List<Country> decreaseSorted = player.getCountriesOwned().stream()
                .sorted((c1, c2) -> c2.getArmies() - c1.getArmies())
                .collect(Collectors.toList());


        for (Country weaker : increaseSorted) {
            for (Country stronger : decreaseSorted) {
                if (player.isConnected(weaker, stronger)) {
                    // re-allocated armies
                    int total = stronger.getArmies() + weaker.getArmies();
                    stronger.setArmies(total/2);
                    weaker.setArmies(total - total/2);

                    Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
                    Phase.getInstance().update();
                    return;
                }
            }
        }
    }
}

