package com.risk.strategy;

import com.risk.model.Country;
import com.risk.model.Phase;
import com.risk.model.Player;

public interface PlayerBehaviorStrategy {

    void reinforcement( ) throws InterruptedException;

//    void allocateArmy(Country country, boolean disable);

    void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) throws InterruptedException;

    void moveArmy(String num);

    void fortification(Country source, Country target, int armyNumber) throws InterruptedException;

    String getName();

    void execute() throws InterruptedException;
}
