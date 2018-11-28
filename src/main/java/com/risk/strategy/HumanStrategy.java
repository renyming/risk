package com.risk.strategy;

import com.risk.common.Action;
import com.risk.model.*;

import java.io.Serializable;

import static java.lang.Thread.sleep;

/**
 * Human strategy class
 */
public class HumanStrategy implements PlayerBehaviorStrategy, Serializable {

    private String name;
    private Player player;
    private Phase phase;

    //TODO:doc
    public HumanStrategy(Player player){
        name = "human";
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
     * Not need
     */
    @Override
    public void execute() {
    }

    //-----------------------------------------reinforcement----------------------------------------------
    /**
     *  Implementation of reinforcement
     */
    @Override
    public void reinforcement() throws InterruptedException {
        Phase.getInstance().setCurrentPhase("Reinforcement Phase");
        player.addRoundArmies();
        Phase.getInstance().update();

    }

//    /**
//     * allocate one army in a specific country
//     * @param country Country reference
//     */
//    @Override
//    public void allocateArmy(Country country, boolean disable){
//        System.out.println("here");
//
//        if(disable) {
//            Phase.getInstance().setInvalidInfo("Start Up Phase ended!");
//            Phase.getInstance().update();
//            return;
//        }
//        if(!Model.getCurrentPlayer().getCountriesOwned().contains(country)){
//            Phase.getInstance().setInvalidInfo("Invalid country!");
//            Phase.getInstance().update();
//            return;
//        }
//
//        country.addArmies(1);
//        country.getOwner().subArmies(1);
//
//
//        Phase.getInstance().setActionResult(Action.Allocate_Army);
//        Phase.getInstance().update();
//
//        //rPhase
//        if(country.getOwner().getArmies() == 0){
//            disable = true;
//            Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
//            Phase.getInstance().update();
//            Model.phaseNumber = 2;
//        }
//    }

//--------------------------------------------------attack--------------------------------------------------------
    /**
     * Method for attack operation
     * @param attacker The country who start an attack
     * @param attackerNum how many dice the attacker choose
     * @param defender The country who defend
     * @param defenderNum how many dice the defender choose
     * @param isAllOut true, if the attacker want to all-out; else false
     */
    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) throws InterruptedException {

        if (!player.isValidAttack(attacker, attackerNum, defender, defenderNum)) {
            return;
        }

        // if defender country doesn't has army
        if (player.isDefenderLoose()) {
            phase.update();
            return;
        }

        if (isAllOut) {
            // dice number depend by computer
            player.allOut();
        } else {
            // players choose how many dice need to put
            player.attackOnce();
        }

        //update phase info
        if (phase.getActionResult() == null) {
            phase.setActionResult(Action.Show_Next_Phase_Button);
        }
        if (phase.getActionResult() != Action.Win && phase.getActionResult() != Action.Move_After_Conquer){
            phase.setActionResult(Action.Show_Next_Phase_Button);
            if (!player.isAttackPossible()) {
                phase.setActionResult(Action.Attack_Impossible);
                phase.setInvalidInfo("Attack Impossible. You Can Enter Next Phase Now.");
            }
        }

        phase.update();

        return;
    }


    /**
     * Move number of armies to the new conquered country
     * @param num the number of armies need to be move
     */
    @Override
    public void moveArmy(String num){

        Country attacker = player.getAttacker();
        Country defender = player.getDefender();
        int attackerDiceNum = player.getAttackerDiceNum();

        int numArmies = 0;
        try{
            numArmies = Integer.valueOf(num);
        } catch (Exception e){
            phase.setActionResult(Action.Invalid_Move);
            phase.setInvalidInfo("Please input a number");
            phase.update();
            return;
        }

        if (player.isContain(attacker) && player.isContain(defender)
                && attacker.getArmies() >= numArmies && numArmies >= attackerDiceNum) {
            attacker.setArmies(attacker.getArmies() - numArmies);
            defender.setArmies(defender.getArmies() + numArmies);

            phase.setActionResult(Action.Show_Next_Phase_Button);
            phase.setInvalidInfo("Army Movement Finish. You Can Start Another Attack Or Enter Next Phase Now");
            if (!player.isAttackPossible()) {
                phase.setActionResult(Action.Attack_Impossible);
                phase.setInvalidInfo("Attack Impossible. You Can Enter Next Phase Now.");
            }
            phase.update();
            return;
        }
        phase.setActionResult(Action.Invalid_Move);
        phase.setInvalidInfo("You Must Place At Least " + attackerDiceNum + ", And Maximum "
                +attacker.getArmies()+" Armies.");
        phase.update();
        return;

    }


//---------------------------------------------fortify----------------------------------------------------
    /**
     * Method for fortification operation
     * @param source The country moves out army
     * @param target The country receives out army
     * @param armyNumber Number of armies to move
     */
    @Override
    public void fortification(Country source, Country target, int armyNumber){
        //return no response to view if source country's army number is less than the number of armies on moving,
        //or the source and target countries aren't connected through the same player's countries
        if (!source.getOwner().equals(player) || !target.getOwner().equals(player)) {
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Invalid move, This is not your country.");
            Phase.getInstance().update();
            return;
        }

        if(source.getArmies()<armyNumber || !source.getOwner().isConnected(source,target)) {
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("invalid move");
            Phase.getInstance().update();
            return;
        }

        source.setArmies(source.getArmies()-armyNumber);
        target.setArmies(target.getArmies()+armyNumber);

        Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        Phase.getInstance().update();
    }

}
