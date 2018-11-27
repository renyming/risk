package com.risk.strategy;

import com.risk.common.Action;
import com.risk.model.Country;
import com.risk.model.Model;
import com.risk.model.Phase;
import com.risk.model.Player;

import java.util.Random;

public class RandomStrategy implements PlayerBehaviorStrategy {

    private Player player;
    private Phase phase;
    private Random random=new Random();

    /**
     * Reinforcement operation for random player
     */
    @Override
    public void reinforcement() {
        //update current phase on view
        phase.setCurrentPhase("Reinforcement Phase");
        phase.update();

        player.addRoundArmies();
        Country country=getRandomOwnedCountry();
        country.addArmies(player.getArmies());
        player.setArmies(0);

        //update result on view
        phase.setActionResult(Action.Show_Next_Phase_Button);
        phase.update();
        Model.phaseNumber=2;

    }

    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) {

    }

    @Override
    public void moveArmy(String num) {

    }

    @Override
    public void fortification(Country source, Country target, int armyNumber) {

    }

    /**
     * Get a random country from the country list owned by current player
     * @return A random country
     */
    private Country getRandomOwnedCountry() {
        int randomIdx=random.nextInt(player.getCountriesSize());
        return player.getCountriesOwned().get(randomIdx);
    }

}
