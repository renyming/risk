package com.risk.strategy;

import com.risk.common.Action;
import com.risk.common.SleepTime;
import com.risk.common.Tool;
import com.risk.model.Country;
import com.risk.model.Model;
import com.risk.model.Phase;
import com.risk.model.Player;

import java.io.Serializable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

/**
 * Cheater Strategy class
 */
public class CheaterStrategy implements PlayerBehaviorStrategy, Serializable {

    private String name;
    private Player player;
//    private Phase Phase.getInstance();

    /**
     * Constructor
     * @param player the correspinding player
     */
    public CheaterStrategy(Player player){
        name = "cheater";
        this.player = player;
//        Phase.getInstance() = Phase.getInstance();
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
        Phase.getInstance().update();
        attack(null, "0", null, "0", true);
        if (Phase.getInstance().getActionResult() == Action.Win) {
            return;
        }

        //fortification
        Phase.getInstance().setCurrentPhase("Fortification Phase");
        Phase.getInstance().update();
        fortification(null, null, 0);
    }


    /**
     *  reinforcement method
     *  doubles the number of armies on all its countries
     */
    @Override
    public void reinforcement() throws InterruptedException {

        System.out.println(player.getName() + " enter the reinforcement phase");

        // correct display current phase
        Phase.getInstance().setCurrentPhase("Reinforcement Phase");
        Phase.getInstance().update();

        // double armies in owned country
        doubleArmies(c -> true);

        // update phase
        Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        Phase.getInstance().update();
        Model.phaseNumber = 2;

        Tool.printBasicInfo(player, "After reinforcement: ");
        sleep(SleepTime.getSleepTime());
    }

    /**
     * Automatically conquers all the neighbors of a country
     * @param country a country who attack others
     */
    private void conquerAdj(Country country) {

        country.getAdjCountries().stream()
                .filter(c -> !c.getOwner().equals(player))
                .forEach(defender -> {

                    // change the ownership of the defender country
                    defender.getOwner().delCountry(defender);
                    defender.getOwner().subTotalStrength(Math.toIntExact(defender.getArmies()));
                    defender.setPlayer(country.getOwner());
                    country.getOwner().addCountry(defender);
                    country.getOwner().setTotalStrength(country.getOwner().getTotalStrength()+ defender.getArmies());


                    // set phase info
                    player.setNumberOccupy(player.getNumberOccupy() + 1);
                    Phase.getInstance().setActionResult(Action.Move_After_Conquer);
                    Phase.getInstance().setInvalidInfo("Successfully Conquered Country : "+ defender.getName());

                    // if attacker win the game
                    if (player.isWin()) {
                        Phase.getInstance().setActionResult(Action.Win);
                        // give the name of winner
                        Phase.getInstance().setInvalidInfo("Congratulations, You Win!");
                        System.out.println(player.getName() + ", Congratulations, You Win! ");
                        Model.winner = player.getName();
                        Phase.getInstance().update();
                        return;
                    }
                    Phase.getInstance().update();

                });

        player.addRandomCard();

    }


    /**
     * Attack method
     * automatically conquers all the neighbors of all its countries
     * @param attacker  null
     * @param attackerNum 0
     * @param defender null
     * @param defenderNum 0
     * @param isAllOut false
     */
    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) throws InterruptedException {

        System.out.println(player.getName() + " enter the attack phase");

        player.getCountriesOwned().stream()
                .collect(Collectors.toSet())
                .forEach(c -> conquerAdj(c));

        Tool.printBasicInfo(player,"After attack: ");
        sleep(SleepTime.getSleepTime());

    }


    /**
     * moveArmy method
     * doesn't do anything in cheater strategy
     * @param num 0
     */
    @Override
    public void moveArmy(String num) {

        Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        Phase.getInstance().setInvalidInfo("Army Movement Finish. You Can Start Another Attack Or Enter Next Phase Now");
        Phase.getInstance().update();

    }

    /**
     * fortification method
     * doubles the number of armies on its countries that have neighbors that belong to other players.
     * @param source null
     * @param target null
     * @param armyNumber 0
     */
    @Override
    public void fortification(Country source, Country target, int armyNumber) throws InterruptedException {

        System.out.println(player.getName() + " enter the fortification phase");

        // double armies in owned country
        doubleArmies(c -> c.hasAdjEnemy());

        // update phase
        Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        Phase.getInstance().update();

        Tool.printBasicInfo(player,"After fortification: ");
        sleep(SleepTime.getSleepTime());

    }

    /**
     * Double armies in all the countries owned
     * @param p the conditon of filter
     */
    public void doubleArmies(Predicate<Country> p) {

        player.getCountriesOwned().stream()
                .filter(p)
                .forEach(country -> {
                    player.setTotalStrength(player.getTotalStrength() + country.getArmies());
                    country.setArmies(country.getArmies() * 2);

                });
    }



}
