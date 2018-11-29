package com.risk.strategy;


import com.risk.common.Action;
import com.risk.common.Tool;
import com.risk.model.Country;
import com.risk.model.Phase;
import com.risk.model.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;


public class AggressiveStrategy implements PlayerBehaviorStrategy {

    private String name;
    private Player player;
    private Phase phase;

    /**
     * constructor
     * @param player
     */
    public AggressiveStrategy(Player player) {

        name = "aggressive";
        this.player = player;
        phase = Phase.getInstance();
    }


    /**
     * Get name
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Orderly execute reinforcement(), attack() and fortification method
     */
    @Override
    public void execute() throws InterruptedException {

        Tool.printBasicInfo(player,"Before Round-Robin");

        //reinforcement
        reinforcement();

        //attack
        Phase.getInstance().setCurrentPhase("Attack Phase");
        attack(null, "0", null, "0", true);
        if (phase.getActionResult() == Action.Win) {
            return;
        }

        //fortification
        Phase.getInstance().setCurrentPhase("Fortification Phase");
        fortification(null, null, 0);
    }


    /**
     * Reinforcement method
     * reinforces its strongest country
     */
    @Override
    public void reinforcement() throws InterruptedException {

        System.out.println(player.getName() + " enter the reinforcement phase");

        // change card first
        // cards = {"infantry","cavalry","artillery"};
        while (player.getTotalCards() >= 5) {
            player.autoTradeCard();
        }

        // computer the armies that need to added roundly
//        Phase.getInstance().setCurrentPhase("Reinforcement Phase");
        player.addRoundArmies();
        phase.update();
        Tool.updateThread();

        // find the strongest country
        Country strongest = player.getCountriesOwned().stream()
                .max(Comparator.comparingLong(Country::getArmies))
                .get();

        // add all the armies to weakest
        strongest.addArmies(player.getArmies());
        player.setArmies(0);

        // update phase
        phase.setActionResult(Action.Show_Next_Phase_Button);
        phase.update();
//        Tool.updateThread();

        Tool.printBasicInfo(player, "After reinforcement: ");

//        sleep(500);
    }



    /**
     * Attack method
     * always attack with the strongest country until it cannot attack anymore
     * @param attacker null
     * @param attackerNum 0
     * @param defender null
     * @param defenderNum 0
     * @param isAllOut true
     */
    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) throws InterruptedException {

        System.out.println(player.getName() + " enter the attack phase");

        // attacker is the strongest country
        Country strongest = player.getCountriesOwned().stream()
                .max(Comparator.comparingLong(Country::getArmies))
                .get();

        List<Country> enemies = strongest.getAdjCountries().stream()
                .filter(c -> !c.getOwner().equals(player))
                .collect(Collectors.toList());

        for (Country enemy : enemies) {
            if (strongest.getArmies() >= 2) {
                player.allOut(strongest, enemy);

                if (phase.getActionResult() == Action.Win) {
//                    Tool.updateThread();
                    player.addRandomCard();
                    Phase.getInstance().update();
                    return;
                }

                if (phase.getActionResult() == Action.Move_After_Conquer) {
                    moveArmy(String.valueOf(player.getAttackerDiceNum()));
                }
            }
        }

        player.addRandomCard();
        phase.update();
//        Tool.updateThread();
        Tool.printBasicInfo(player,"After attack: ");

//        sleep(500);
    }



    /**
     * Move army method
     * move the mininum armies that could
     * @param num
     */
    @Override
    public void moveArmy(String num) {

        int numArmies = Integer.valueOf(num);
        Country attacker = player.getAttacker();
        Country defender = player.getDefender();

        attacker.setArmies(attacker.getArmies() - numArmies);
        defender.setArmies(defender.getArmies() + numArmies);

        phase.setActionResult(Action.Show_Next_Phase_Button);
        phase.setInvalidInfo("Army Movement Finish. You Can Start Another Attack Or Enter Next Phase Now");
    }


    /**
     * Fortification method
     * maximize aggregation of forces in one country
     * @param source
     * @param target
     * @param armyNumber
     */
    @Override
    public void fortification(Country source, Country target, int armyNumber) throws InterruptedException {

        System.out.println(player.getName() + " enter the fortification phase");

        List<Country> decreaseSorted = player.getCountriesOwned().stream()
                .sorted((c1, c2) -> {
                    if (c2.getArmies() - c1.getArmies() > 0 ) return 1;
                    else if (c2.getArmies() - c1.getArmies() == 0) return 0;
                    else return -1; })
                .collect(Collectors.toList());

        for (int i = 0; i < decreaseSorted.size(); i++) {
            for (int j = 1; j < decreaseSorted.size(); j++) {

                Country c1 = decreaseSorted.get(i);
                Country c2 = decreaseSorted.get(j);

                if (player.isConnected(c1, c2)){

                    System.out.println(c1.getName());
                    System.out.println(c2.getName());

                    // re-allocated armies
                    c1.setArmies(c1.getArmies() + c2.getArmies());
                    c2.setArmies(0);

                    phase.setActionResult(Action.Show_Next_Phase_Button);
                    phase.update();
//                    Tool.updateThread();

                    Tool.printBasicInfo(player,"After fortification: ");
                    return;
                }
            }
        }

        Tool.printBasicInfo(player,"After fortification: ");
//        sleep(500);
    }
}
