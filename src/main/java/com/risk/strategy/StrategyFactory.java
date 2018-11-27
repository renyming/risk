package com.risk.strategy;

import com.risk.model.Player;

/**
 * Return a correspond strategy according the param
 **/
public class StrategyFactory {

    /**
     * private constructor
     */
    private StrategyFactory() {}

    /**
     * static method to create strategy method
     *
     */
    public static PlayerBehaviorStrategy getStrategy(String s, Player p) {

        if (s.equalsIgnoreCase("aggressive")) {
            return new AggressiveStrategy(p);
        } else if (s.equalsIgnoreCase("benevolentStrategy")) {
            return new BenevolentStrategy(p);
        } else if (s.equalsIgnoreCase("human")) {
            return new HumanStrategy(p);
        } else if (s.equalsIgnoreCase("random")) {
            return new RandomStrategy(p);
        } else if (s.equalsIgnoreCase("cheater")) {
            return new CheaterStrategy(p);
        }

        return null;
    }
}
