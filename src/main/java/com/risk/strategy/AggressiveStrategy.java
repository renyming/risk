package com.risk.strategy;


import com.risk.model.Country;
import com.risk.model.Phase;
import com.risk.model.Player;

public class AggressiveStrategy implements PlayerBehaviorStrategy {

    private Player player;
    private Phase phase;

    public AggressiveStrategy(Player player) {
        this.player = player;
        phase = Phase.getInstance();
    }

    @Override
    public void reinforcement() {

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
}
