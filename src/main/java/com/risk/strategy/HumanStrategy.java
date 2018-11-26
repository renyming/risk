package com.risk.strategy;

import com.risk.model.Continent;
import com.risk.model.Phase;
import com.risk.model.Player;

public class HumanStrategy implements PlayerBehaviorStrategy {
    private Player player;
    /**
     *  Implementation of reinforcement
     */
    @Override
    public void reinforcement(Player player){
        this.player = player;
        Phase.getInstance().setCurrentPhase("Reinforcement Phase");
        addRoundArmies();
        Phase.getInstance().update();
    }

    /**
     * Add armies in the very first of the reinforcement phase
     * The number of armies added is computed based on the number of countries and cards it has
     */
    public void addRoundArmies(){

        int newArmies = getArmiesAdded();
        player.setArmies(newArmies);
        player.setTotalStrength(player.getTotalStrength() + newArmies);
    }

    /**
     * Compute the armiesAdded based on the number of countries continent and cards it has
     * @return armies need to be added
     */
    private int getArmiesAdded() {

        int armiesAdded = 0;

        // based on countries num
        if (player.getCountriesOwned().size() > 0) {
            armiesAdded = player.getCountriesOwned().size() / 3;
        }

        //based on continent
        armiesAdded += getArmiesAddedFromContinent();

        System.out.println("REINFORCEMENT ARMY NUMBER: " + armiesAdded);
        System.out.println("CARD ARMY NUMBER: " + player.getCardsArmy());
        //need to implement next phase
        armiesAdded += player.getCardsArmy();

        // the minimal number of reinforcement armies is 3
        if (armiesAdded < 3) {
            armiesAdded = 3;
        }

        return armiesAdded;

    }

    /**
     * Compute the armiesAdded based on the continents it has
     * @return the number of armies need to be added based on the continents it has
     */
    private int getArmiesAddedFromContinent() {

        int armiesAdded = 0;

        for (Continent continent : player.getContinentsOwned()) {
            armiesAdded += continent.getControlVal();
        }

        return armiesAdded;
    }

//------------------------------attack-----------------------------


}
