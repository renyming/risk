package com.risk.strategy;

import com.risk.model.Country;

public interface PlayerBehaviorStrategy {
    void reinforcement( );
    void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut);
    void moveArmy(String num);
    boolean isAttackPossible();
    void addRandomCard(String newCard);
//    void fortification();
}
