package com.risk.strategy;

import com.risk.common.Action;
import com.risk.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HumanStrategy implements PlayerBehaviorStrategy {
    private Player player;

    private Phase phase = Phase.getInstance();
    private Country attacker;
    private int attackerDiceNum;
    private Country defender;
    private int defenderDiceNum;

    //TODO:doc
    public HumanStrategy(Player player){
        this.player = player;
    }


//-----------------------------------------reinforcement----------------------------------------------
    /**
     *  Implementation of reinforcement
     */
    @Override
    public void reinforcement(){
        Phase.getInstance().setCurrentPhase("Reinforcement Phase");
        player.addRoundArmies();
        Phase.getInstance().update();
    }

    /**
     * allocate one army in a specific country
     * @param country Country reference
     */
    @Override
    public void allocateArmy(Country country, boolean disable){
        System.out.println("here");

        if(disable) {
            Phase.getInstance().setInvalidInfo("Start Up Phase ended!");
            Phase.getInstance().update();
            return;
        }
        if(!Model.getCurrentPlayer().getCountriesOwned().contains(country)){
            Phase.getInstance().setInvalidInfo("Invalid country!");
            Phase.getInstance().update();
            return;
        }

        country.addArmies(1);
        country.getOwner().subArmies(1);


        Phase.getInstance().setActionResult(Action.Allocate_Army);
        Phase.getInstance().update();

        //rPhase
        if(country.getOwner().getArmies() == 0){
            disable = true;
            Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
            Phase.getInstance().update();
            Model.phaseNumber = 2;
        }
    }

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
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut){

        if (!isValidAttack(attacker, attackerNum, defender, defenderNum)) {
            return;
        }

        // if defender country doesn't has army
        if (isDefenderLoose()) {
            phase.update();
            return;
        }

        if (isAllOut) {
            // dice number depend by computer
            allOut();
        } else {
            // players choose how many dice need to put
            attackOnce();
        }

        //update phase info
        if (phase.getActionResult() == null) {
            phase.setActionResult(Action.Show_Next_Phase_Button);
        }
        if (phase.getActionResult() != Action.Win && phase.getActionResult() != Action.Move_After_Conquer){
            phase.setActionResult(Action.Show_Next_Phase_Button);
            if (!isAttackPossible()) {
                phase.setActionResult(Action.Attack_Impossible);
                phase.setInvalidInfo("Attack Impossible. You Can Enter Next Phase Now.");
            }
        }

        phase.update();

        return;
    }

    /**
     * Test if attack is valid
     * @param attacker The country who start the attack
     * @param attackerNum how many dise the attacker will use in this attack
     * @param defender The country who defend the attack
     * @param defenderNum how many dise the defender will use in this attack
     * @return if two country is adjacent, and their dice is less the armies they owned, return true, else false
     */
    private boolean isValidAttack(Country attacker, String attackerNum, Country defender, String defenderNum){

        // if any of countries is none
        if (attacker == null || defender == null) {
            phase.setActionResult(Action.Invalid_Move);
            phase.setInvalidInfo("Countries can not be none");
            phase.update();
            return false;
        }

        // if int valid
        int attackerDiceNum = 0;
        int defenderDiceNum = 0;
        try{
            attackerDiceNum = Integer.valueOf(attackerNum);
            defenderDiceNum = Integer.valueOf(defenderNum);
        } catch (Exception e){
            phase.setActionResult(Action.Invalid_Move);
            phase.setInvalidInfo("Input error, invalid dice number.");
            phase.update();
            return false;
        }

        if (!attacker.getOwner().equals(player)) {
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Invalid attack, This is not your country.");
            Phase.getInstance().update();
            return false;
        }

        //if valid attack
        if (attacker.getOwner().equals(defender.getOwner())) {
            phase.setActionResult(Action.Invalid_Move);
            phase.setInvalidInfo("Invalid attack, cannot attack a country owned by player himself.");
            phase.update();
            return false;
        }

        // if attacker's dice valid
        if (!attacker.isValidAttacker(attackerDiceNum)) {
            phase.setActionResult(Action.Invalid_Move);
            phase.setInvalidInfo("Invalid attacker dice number, " +
                    "armies in attacker must more than two, and the dice must less than armies");
            phase.update();
            return false;
        }

        // if defender's dice valid
        if (!defender.isValidDefender(defenderDiceNum)) {
            phase.setActionResult(Action.Invalid_Move);
            phase.setInvalidInfo("Invalid defender's dice number, the dice must less than armies");
            phase.update();
            return false;
        }

        // if two countries adjacent
        if (!attacker.isAdjacent(defender)){
            phase.setActionResult(Action.Invalid_Move);
            phase.setInvalidInfo("Two countries is not adjacent");
            phase.update();
            return false;
        }

        // update the attack info
        this.attacker = attacker;
        this.attackerDiceNum = attackerDiceNum;
        this.defender = defender;
        this.defenderDiceNum = defenderDiceNum;
        return true;
    }

    /**
     * Verify if defender loose the country
     * @return
     */
    private boolean isDefenderLoose() {

        if (defender.getArmies() == 0) {

            if (defender.getOwner().getCountriesOwned().size() == 1) {

                // TODO: add all cards form defender's owner
                phase.setInvalidInfo(defender.getOwner().getName() + " lost all the countries!");
                phase.update();
                getDefenderCards(attacker.getOwner(),defender.getOwner());
            }

            // change the ownership of the defender country
            defender.getOwner().delCountry(defender);
            defender.setPlayer(attacker.getOwner());
            attacker.getOwner().addCountry(defender);

            // add numOfOccupy
            player.increaseNumberOccupy();
            phase.setActionResult(Action.Move_After_Conquer);
            phase.setInvalidInfo("Successfully Conquered Country : "+ defender.getName()
                    +". Now You Must Place At Least " + attackerDiceNum + " Armies.");

            // if attacker win the game
            if (attacker.getOwner().getCountriesOwned().size() == player.getCountriesSize()) {
                phase.setActionResult(Action.Win);
                // give the name of winner
                phase.setInvalidInfo("Congratulations, You Win!");
            }

            return true;
        }
        return false;
    }

    /**
     * attacker get all the defender's cards
     * @param attacker the player who attack
     * @param defender the player who defend
     */
    public void getDefenderCards(Player attacker, Player defender){
        for(String key : defender.getCards().keySet()){
            attacker.getCards().put(key, attacker.getCards().get(key) + defender.getCards().get(key));
            defender.getCards().put(key,0);
        }
    }

    /**
     * Battle until the defender be occupied or the attacker consume its armies
     */
    private void allOut() {

        while (true) {
            attackerDiceNum = attacker.getArmies() > 3? 3 : attacker.getArmies();
            defenderDiceNum = defender.getArmies() > 2? 2 : defender.getArmies();

            attackOnce();
            // if defender is occupied by attacker
            if(attacker.getOwner().equals(defender.getOwner())) break;
            // if attacker exhaust all its armies
            // if attack possible
            if(attacker.getArmies() == 0) break;
        }
    }

    /**
     * Battle only run once time
     */
    private void attackOnce() {

        // roll the dices to battle
        ArrayList<Integer> dicesAttacker = getRandomDice(attackerDiceNum);
//        System.out.println(dicesAttacker);
        ArrayList<Integer> diceDefender = getRandomDice(defenderDiceNum);
//        System.out.println(diceDefender);

        // compare the rolling result
        int range = attackerDiceNum < defenderDiceNum? attackerDiceNum : defenderDiceNum;
        for (int i=0; i<range; i++){

            if (diceDefender.get(i) >= dicesAttacker.get(i)) {
                attacker.setArmies(attacker.getArmies()-1);
                attacker.getOwner().subTotalStrength(1);

            } else {
                defender.setArmies(defender.getArmies()-1);
                defender.getOwner().subTotalStrength(1);

                //if defender's armies == 0, attacker victory
                if (isDefenderLoose()) return;
            }
        }
        phase.setInvalidInfo("Attack Finish. You Can Start Another Attack Or Enter Next Phase Now.");

    }

    /**
     * Get a sorted list of random dices
     * @param num how many dices needed
     * @return list of random dice
     */
    public ArrayList<Integer> getRandomDice(int num){

        ArrayList<Integer> dices = new ArrayList<Integer>();
        Random random = new Random();

        for (int i=0; i<num; i++){
            int temp = random.nextInt(6)+1;
//            System.out.println("Dice " + i + " : " + temp);
            dices.add(temp);
        }
        Collections.sort(dices, Collections.reverseOrder());
        return dices;
    }

    /**
     * Verify if attack is possible
     * @return true, if has at least one country to attack; else false
     */
    @Override
    public boolean isAttackPossible() {

        for (Country c : player.getCountriesOwned()) {
            if (c.getArmies() < 2) continue;
            for (Country adj : c.getAdjCountries()) {
                if (!adj.getOwner().equals(c.getOwner())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Move number of armies to the new conquered country
     * @param num the number of armies need to be move
     */
    @Override
    public void moveArmy(String num){

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
            if (!isAttackPossible()) {
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
