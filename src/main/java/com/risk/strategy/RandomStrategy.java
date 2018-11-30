package com.risk.strategy;

import com.risk.common.Action;
import com.risk.common.SleepTime;
import com.risk.common.Tool;
import com.risk.model.Country;
import com.risk.model.Model;
import com.risk.model.Phase;
import com.risk.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

/**
 * Random strategy class
 */
public class RandomStrategy implements PlayerBehaviorStrategy, Serializable {

    private String name;
    private Player player;
//    private Phase Phase.getInstance();
    Country attackingCountry;
    Country defendingCountry;
    int attackerDiceNum;
    int defenderDiceNum;
    private Random random=new Random();

    /**
     * Constructor
     * @param player the correspinding player
     */
    public RandomStrategy(Player player) {
        name = "random";
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
            Model.winner = player.getName();
            return;
        }

        //fortification
        Phase.getInstance().setCurrentPhase("Fortification Phase");
        Phase.getInstance().update();
        fortification(null, null, 0);
    }

    /**
     * Reinforcement operation for random player
     */
    @Override
    public void reinforcement() throws InterruptedException {

        System.out.println(player.getName() + " enter the reinforcement phase");
        //update current phase on view
//        phase.setCurrentPhase("Reinforcement Phase");
        Phase.getInstance().update();

        player.addRoundArmies();
        Country country= getRandomCountry(player.getCountriesOwned());
        country.addArmies(player.getArmies());
        player.setArmies(0);

        //update result on view
        Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        Phase.getInstance().update();
        Model.phaseNumber=2;

        Tool.printBasicInfo(player, "After reinforcement: ");
        sleep(SleepTime.getSleepTime());

    }

    /**
     * Attacks at a random number of times, each attack chooses a country as attacking country and one of its adjacent enemy countries as defending country
     * @param attacker dummy param
     * @param attackerNum dummy param
     * @param defender dummy param
     * @param defenderNum dummy param
     * @param isAllOut dummy param
     * @throws InterruptedException exception
     */
    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) throws InterruptedException {

        System.out.println(player.getName() + " enter the attack phase");

        //player has no country is a valid attacker
        if (!player.isAttackPossible()) return;

        ArrayList<Country> attackingCandidatesList=new ArrayList<>(player.getCountriesOwned());

        while (player.isAttackPossible()){
            attackingCountry= getRandomCountry(attackingCandidatesList);
//            System.out.println("Attacking country: "+ attackingCountry.getName());
            if (isValidAttacker(attackingCountry)) {
                //get all adjacent countries belongs to other players
                ArrayList<Country> defendingCandiatesList=attackingCountry.getAdjCountries().stream()
                        .filter(c -> c.getOwner()!=player)
                        .collect(Collectors.toCollection(ArrayList::new));

                //randomly pick an adjacent country to attack
                defendingCountry= getRandomCountry(defendingCandiatesList);
//                System.out.println("Defending country: "+defendingCountry.getName());

                //randomly generates number of dices
                if (attackingCountry.getArmies()==2) {
                    attackerDiceNum=random.nextInt(2)+1; //1~2
                } else {
                    attackerDiceNum=random.nextInt(3)+1; //1~3
                }
//                System.out.println("Attacker dice number: "+attackerDiceNum);

                if (defendingCountry.getArmies()==0) {
                    player.isDefenderLoose(attackingCountry,defendingCountry);
                    continue;
                } else if (defendingCountry.getArmies()==1) {
                    defenderDiceNum=1;
                } else {
                    defenderDiceNum=random.nextInt(2)+1; //1~2
                }
//                System.out.println("Defender dice number: "+defenderDiceNum);

                player.attackOnce(attackingCountry, attackerDiceNum, defendingCountry, defenderDiceNum);

                if (Phase.getInstance().getActionResult()==Action.Move_After_Conquer)
                    moveArmy(""); //dummy param

                // update phase
                Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
                Phase.getInstance().update();

            } else {
                attackingCandidatesList.remove(attackingCountry);
//                System.out.println("Country invalid");
                continue;
            }

            int moreAttack=random.nextInt(2); //0~1; 0-exit attacking; 1-continue attacking

            //stop attacking if moreAttack=0
            if (moreAttack==0) {
                break;
            }

            //reinitialize attacking candidates list in case player owns a new country
            attackingCandidatesList=new ArrayList<>(player.getCountriesOwned());

        }

        Tool.printBasicInfo(player,"After attack: ");
        sleep(SleepTime.getSleepTime());

    }

    /**
     * Move a random number of armies from attacking country to recently conquered country
     * @param num dummy param
     */
    @Override
    public void moveArmy(String num) {
        int n=random.nextInt(Math.toIntExact(attackingCountry.getArmies())-attackerDiceNum+1)+attackerDiceNum;
        attackingCountry.setArmies(attackingCountry.getArmies()-n);
        defendingCountry.setArmies(defendingCountry.getArmies()+n);
    }

    /**
     * Randomly fortificates a country by a random number of armies
     * @param source dummy param
     * @param target dummy param
     * @param armyNumber dummy param
     * @throws InterruptedException exception
     */
    @Override
    public void fortification(Country source, Country target, int armyNumber) throws InterruptedException {

        System.out.println(player.getName() + " enter the fortification phase");

        //terminates if player has less than two countries
        if (player.getCountriesSize()<2) {
            Tool.printBasicInfo(player,"After fortification: ");
            return;
        }

        //get source country candidates list
        ArrayList<Country> sourceCandidates=new ArrayList<>(player.getCountriesOwned());

        Country fromCountry;
        while(true) {
            fromCountry=getRandomCountry(sourceCandidates);
            ArrayList<Country> adjOwnedCountries=fromCountry.getAdjCountries().stream()
                    .filter(c -> c.getOwner()==player)
                    .collect(Collectors.toCollection(ArrayList::new));
            if (adjOwnedCountries.isEmpty()) {
                sourceCandidates.remove(fromCountry);
                if (sourceCandidates.isEmpty()) return;
            } else {
                break;
            }
        }

        //get dest country candidates list
        ArrayList<Country> destCandidates=new ArrayList<>(player.getCountriesOwned());
        destCandidates.remove(fromCountry);
        Country toCountry=getRandomCountry(destCandidates);

        //pick a dest country until it's not the origin country
        while(!player.isConnected(fromCountry,toCountry)){
            destCandidates.remove(toCountry);
            toCountry=getRandomCountry(destCandidates);
        }

        //number of armies to move
        int numArmies=random.nextInt(Math.toIntExact(fromCountry.getArmies())+1);
        fromCountry.setArmies(fromCountry.getArmies()-numArmies);
        toCountry.setArmies(toCountry.getArmies()+numArmies);

        Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        Phase.getInstance().update();

        Tool.printBasicInfo(player,"After fortification: ");
        sleep(SleepTime.getSleepTime());

    }


    /**
     * Test if a country can act as valid attacker
     * @param country Country to be tested
     * @return True-if the country has more than 2 armies, and has at least one adjacent country belongs to enemy
     */
    private boolean isValidAttacker(Country country){
        return country.getArmies()>=2 && country.hasAdjEnemy();
    }

    /**
     * Get a random country from the given country list
     * @param countryList Country list to randomly choose a country from
     * @return A random country
     */
    private Country getRandomCountry(ArrayList<Country> countryList) {
        int randomIdx=random.nextInt(countryList.size());
        return countryList.get(randomIdx);
    }



}
