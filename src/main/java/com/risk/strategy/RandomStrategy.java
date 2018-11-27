package com.risk.strategy;

import com.risk.model.Country;
import com.risk.model.Player;

import java.util.Random;

public class RandomStrategy implements PlayerBehaviorStrategy {

    private Player player;
    private Random random=new Random();

    @Override
    public void reinforcement() {
        //TODO: Update view
        player.addRoundArmies();
        Country country=getRandomOwnedCountry();
        country.addArmies(player.getArmies());
        player.setArmies(0);

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

    private Country getRandomOwnedCountry() {
        int randomIdx=random.nextInt(player.getCountriesSize());
        return player.getCountriesOwned().get(randomIdx);
    }

}
