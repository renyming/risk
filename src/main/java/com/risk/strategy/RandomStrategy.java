package com.risk.strategy;

import com.risk.model.Country;
import com.risk.model.Phase;
import com.risk.model.Player;

public class RandomStrategy implements PlayerBehaviorStrategy {

    private Player player;
    private Phase phase;

    private Country attacker;
    private int attackerDiceNum;
    private Country defender;
    private int defenderDiceNum;

    public RandomStrategy(Player player) {
        this.player = player;
        phase = Phase.getInstance();
    }

    @Override
    public void reinforcement() {

    }

    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) {

        player.attackOnce();

    }

    @Override
    public void moveArmy(String num) {

    }

    @Override
    public void fortification(Country source, Country target, int armyNumber) {

    }

}
