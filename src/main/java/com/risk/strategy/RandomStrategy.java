package com.risk.strategy;

import com.risk.common.Action;
import com.risk.model.Country;
import com.risk.model.Model;
import com.risk.model.Phase;
import com.risk.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomStrategy implements PlayerBehaviorStrategy {

    private Player player;
    private Phase phase;
    Country attackingCountry;
    Country defendingCountry;
    int attackerDiceNum;
    int defenderDiceNum;
    private Random random=new Random();

    public RandomStrategy(Player player) {
        this.player = player;
        phase = Phase.getInstance();
    }

    /**
     * Reinforcement operation for random player
     */
    @Override
    public void reinforcement() {
        //update current phase on view
        phase.setCurrentPhase("Reinforcement Phase");
        phase.update();

        player.addRoundArmies();
        Country country= getRandomCountry(player.getCountriesOwned());
        country.addArmies(player.getArmies());
        player.setArmies(0);

        //update result on view
        phase.setActionResult(Action.Show_Next_Phase_Button);
        phase.update();
        Model.phaseNumber=2;

    }

    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) {

        //player has no country is a valid attacker
        if (!player.isAttackPossible()) return;

        int randomNumAttacks=random.nextInt();
        ArrayList<Country> attackingCandidatesList=new ArrayList<>(player.getCountriesOwned());

        for (int i=1;i<randomNumAttacks;i++){
            attackingCountry= getRandomCountry(attackingCandidatesList);
            if (isValidAttacker(attackingCountry)) {
                //get all adjacent countries belongs to other players
                ArrayList<Country> defendingCandiatesList=attackingCountry.getAdjCountries().stream()
                        .filter(c -> c.getOwner()!=player)
                        .collect(Collectors.toCollection(ArrayList::new));

                //randomly pick an adjacent country to attack
                defendingCountry= getRandomCountry(defendingCandiatesList);

                //randomly generates number of dices
                if (attackingCountry.getArmies()==2) {
                    attackerDiceNum=random.nextInt(2)+1; //1~2
                } else {
                    attackerDiceNum=random.nextInt(3)+1; //1~3
                }

                if (defendingCountry.getArmies()==1) {
                    defenderDiceNum=1;
                } else {
                    defenderDiceNum=random.nextInt(2)+1; //1~2
                }

                player.attackOnce(attackingCountry, attackerDiceNum, defendingCountry, defenderDiceNum);

                if (phase.getActionResult()==Action.Move_After_Conquer)
                    moveArmy(""); //dummy param

                // update phase
                phase.setActionResult(Action.Show_Next_Phase_Button);
                phase.update();

            } else {
                attackingCandidatesList.remove(attackingCountry);
                --i;
            }
        }

    }

    @Override
    public void moveArmy(String num) {
        int n=random.nextInt(attackingCountry.getArmies()-attackerDiceNum)+attackerDiceNum;
        attackingCountry.setArmies(attackingCountry.getArmies()-n);
        defendingCountry.addArmies(n);
    }

    @Override
    public void fortification(Country source, Country target, int armyNumber) {

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
