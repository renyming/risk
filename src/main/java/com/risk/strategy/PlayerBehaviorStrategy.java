package com.risk.strategy;

import com.risk.model.Country;
import com.risk.model.Phase;
import com.risk.model.Player;

/**
 * the interface of player strategy
 */
public interface PlayerBehaviorStrategy {

    /**
     * reinforcement method
     * @throws InterruptedException
     */
    void reinforcement( ) throws InterruptedException;

    /**
     * attack method
     * @param attacker the country who attack
     * @param attackerNum the number of dice the attacker use
     * @param defender the country who defend
     * @param defenderNum the number of dice the defender use
     * @param isAllOut true, allout; false, not allout
     * @throws InterruptedException
     */
    void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) throws InterruptedException;

    /**
     * move army method after conquer
     * @param num number of armies want to move
     */
    void moveArmy(String num);

    /**
     * fortification method
     * @param source source country
     * @param target target country
     * @param armyNumber number of armies want to move
     * @throws InterruptedException
     */
    void fortification(Country source, Country target, int armyNumber) throws InterruptedException;

    /**
     * get the strategy name
     * @return name of strategy
     */
    String getName();

    /**
     * for the computer player, orderly execute the reinforcement, attack and fortification methods
     * @throws InterruptedException
     */
    void execute() throws InterruptedException;
}
