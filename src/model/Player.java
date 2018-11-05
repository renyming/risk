package model;

import java.awt.*;
import java.util.*;

/**
 * Define class of a player
 * The following is only base code, further features need to be added
 */
public class Player extends Observable {

    //Unique Id for each player, starts from 1
    private int Id;
    //Counter to assign unique Id
    private static int cId=0;
    private String name;
    private int armies;
    private ArrayList<Country> countriesOwned;
    private String color = "#4B0082";
    private int totalStrength;
    private int dices;


    /**
     * Constructor of player
     * @param name player name
     */
    public Player(String name){
        this.Id=++cId;
        this.name= name;
        armies = 0;
        countriesOwned = new ArrayList<>();
        totalStrength = 0;
        dices = 0;
    }

    /**
     * Getter for color of player
     * @return Color of this player
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for color of player
     */
    public void setColor() {
        Random rand = new Random(Id*Id*Id);

        // Will produce only bright / light colours:
        float r = (float) (rand.nextFloat() / 2f + 0.4);
        float g = (float) (rand.nextFloat() / 2f + 0.4);
        float b = (float) (rand.nextFloat() / 2f + 0.4);

        Color randomColor = new Color(r, g, b);
        String hex = "#"+Integer.toHexString(randomColor.getRGB()).substring(2);

        this.color=hex;
    }

    /**
    * Getter to get Id
    * @return Id of the player
    */ 
    public int getId() {
        return Id;
    }

    /**
     * Getter to get name
     * @return  player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter to get the number of armies
     * @return The number of armies that the player has currently
     */
    public int getArmies() {
        return armies;
    }

    /**
     * Getter to get CountriesOwned
     * @return  The list of countries object that the player has currently
     */
    public ArrayList<Country> getCountriesOwned() {
        return countriesOwned;
    }

    /**
     * set the countries owned
     * @param  countriesOwned the countries owned
     */
    public void setCountriesOwned(ArrayList<Country> countriesOwned) {
        this.countriesOwned = countriesOwned;

        setChanged();
        notifyObservers(this);
    }

    /**
    * set the number of armies
    * @param  armies the number of armies
    */
    public void setArmies(int armies) {
        this.armies = armies;
        callObservers();
    }

    /**
     * Getter to get totalStrength
     * @return  The num of totalStrength that the player has currently
     */
    public int getTotalStrength() {
        return totalStrength;
    }

    /**
     * set the number of totalStrength
     * @param totalStrength the number of totalStrength
     */
    public void setTotalStrength(int totalStrength) {
        this.totalStrength = totalStrength;
        callObservers();
    }

    /**
    * Add armies in the very first of the reinforcement phase
    * The number of armies added is computed based on the number of countries and cards it has
    */
    public void addRoundArmies(){

        int newArmies = getArmiesAdded();
        setArmies(newArmies);
        setTotalStrength(totalStrength + newArmies);
    }

    /**
    * Compute the armiesAdded based on the number of countries continent and cards it has
    * @return armies need to be added
    */
    private int getArmiesAdded() {

        int armiesAdded = 0;

        // based on countries num
        if (countriesOwned.size() > 0) {
            armiesAdded = countriesOwned.size() / 3;
        }

        //based on continent
        armiesAdded += getArmiesAddedFromContinent();

        //based on card
        //need to implement next phase

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

        // record the number of countries in a continent
        HashMap<Continent, Integer> continentStatics = new HashMap<Continent, Integer>();

        for (Country country : countriesOwned) {

            Continent continent = country.getContinent();

            if (continentStatics.containsKey(continent)){

                int newValue = continentStatics.get(continent) + 1;
                continentStatics.put(continent, newValue);

            } else {
                continentStatics.put(continent, 1);
            }
        }

        //
        int armiesAdded = 0;
        for (Continent c : continentStatics.keySet()) {

            if (c.getSize() == continentStatics.get(c)) {
                armiesAdded += c.getControlVal();
            }
        }

        return armiesAdded;
    }


    /**
     * Substract one for armies when allocated army in the initArmy() or the reinforcements phase
     * @param num remove army from player
     */
    public void subArmies(int num){

        int newArmies = armies - num;
        setArmies(newArmies);
    }

    /**
     * Substract one for armies when allocated army in the initArmy() or the reinforcements phase
     * @param num remove army from player
     */
    public void subTotalStrength(int num){

        int newStrength = totalStrength - num;
        setTotalStrength(newStrength);
    }

    /**
    * Verify if the armies is empty
    * @return  True if armies == 0, else False
    */
    public boolean isEmptyArmy() { return armies == 0 ? true : false; }

    /**
    * Add a country in the countriesOwned list
    * @param  c country need to be added
    */
    public void addCountry(Country c){

        //verify if the country is exist in the countriesOwned??
        countriesOwned.add(c);

        setChanged();
        notifyObservers(this);
    }

    /**
    * Remove a country from the countriesOwned list
    * @param  c country need to be deleted
    * @return true delete success, false delete failed
    */
    public boolean delCountry(Country c){

        Iterator<Country> it = countriesOwned.iterator();
        while(it.hasNext())
        {
            if (c.equals(it.next())){
                it.remove();

                setChanged();
                notifyObservers(this);
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to set change state and notify observers
     */
    public void callObservers() {
        setChanged();
        notifyObservers();
    }

    public boolean isContain(Country c) {

        for (Country each : countriesOwned) {
            if (c.equals(each)) {
                return true;
            }
        }
        return false;
    }


    public boolean isConnected(Country start, Country end) {

        // if doesn't contain both of countries
        if (!isContain(start) ||  !isContain(end)) {
            return false;
        }

        //both contain, BFS find a path
        for (Country c : countriesOwned) {
            if (!c.equals(start)) {
                c.setProcessed(false);
            }
        }

        Queue<Country> queue = new PriorityQueue<Country>();
        queue.add(start);
        start.setProcessed(true);

        while (queue.isEmpty() == false) {
            Country c = queue.poll();

            ArrayList<Country> adjCountries = c.getAdjCountries();

            if ( adjCountries != null && adjCountries.size() != 0) {

                for(Country each : adjCountries) {

                    if (each.equals(end))  return true;

                    if (countriesOwned.contains(each) && !each.isProcessed()) {
                        each.setProcessed(true);
                        queue.add(each);
                    }
                }
            }
        }
        return false;
    }

    /**
     * verify if two users is equal
     * @param p Player need to be compare
     * @return true when comparing the same player false otherwise
     */
    public boolean equals(Player p) { return this.getId() == p.getId(); }

    /**
     * Test if attack is valid
     * @param attacker The country who start the attack
     * @param attackerDiceNum how many dise the attacker will use in this attack
     * @param defender The country who defend the attack
     * @param defenderDiceNum how many dise the defender will use in this attack
     * @return if two country is adjacent, and their dice is less the armies they owned, return true, else false
     */
    private boolean isValidAttack(Country attacker, int attackerDiceNum, Country defender, int defenderDiceNum){

        return attacker.isValidAttacker(attackerDiceNum) && defender.isValidDefender(defenderDiceNum) && attacker.isAdjacent(defender);

    }

    /**
     * Get a sorted list of random dices
     * @param num how many dices needed
     */
    public ArrayList<Integer> getRandomDice(int num){

        ArrayList<Integer> dices = new ArrayList<Integer>();
        Random random = new Random();

        for (int i=0; i<num; i++){
            dices.add(random.nextInt(6)+1);
        }
        Collections.sort(dices, Collections.reverseOrder());
        return dices;
    }

    private void occupy(Country defender){
        // get country added or deleted form the players
        this.addCountry(defender);
        defender.getOwner().delCountry(defender);
    }


    /**
     *
     * @param attacker The country who start the attack
     * @param attackerNum how many dise the attacker will use in this attack
     * @param defender The country who defend the attack
     * @param defenderNum how many dise the defender will use in this attack
     * @return 1, if there attacker successfully occupied a country; 0, attack but not occupied; -1 Invalid attack.
     */
    public int attack(Country attacker, int attackerNum, Country defender, int defenderNum){

        // if int valid
        int attackerDiceNum = 0;
        int defenderDiceNum = 0;
        try{
            attackerDiceNum = Integer.valueOf(attackerNum);
            defenderDiceNum = Integer.valueOf(defenderNum);
        } catch (Exception e){
            return -1;
        }

        //if valid attack
        if (attacker.getOwner().equals(defender.getOwner())) return -1;
        if (!isValidAttack(attacker, attackerDiceNum, defender, defenderDiceNum)) return -1;

        // reset the number of dices
        dices = attackerDiceNum;

        // if defender country doesn't has army
        if (defender.getArmies() == 0) {
            attacker.getOwner().occupy(defender);
            return 1;
        }

        // roll the dices to battle
        ArrayList<Integer> dicesAttacker = getRandomDice(attackerDiceNum);
        System.out.println(dicesAttacker);
        ArrayList<Integer> diceDefender = getRandomDice(defenderDiceNum);
        System.out.println(diceDefender);

//        System.out.println("before roll:");
//        System.out.println("attacker:");
//        System.out.println("country " + attacker.getArmies());
//        System.out.println("player " + attacker.getOwner().getTotalStrength());
//        System.out.println("defender:");
//        System.out.println("country " + defender.getArmies());
//        System.out.println("player " + defender.getOwner().getTotalStrength());

        for (int i=0; i<defenderDiceNum; i++){

            if (diceDefender.get(i) >= dicesAttacker.get(i)) {
                attacker.setArmies(attacker.getArmies()-1);
                attacker.getOwner().subTotalStrength(1);

            } else {
                defender.setArmies(defender.getArmies()-1);
                defender.getOwner().subTotalStrength(1);

                //if defender's armies == 0, attacker victory
                if (defender.getArmies() == 0) {
                    attacker.getOwner().occupy(defender);
                    //TODO: add card
                    return 1;
                }
            }

//            System.out.println("after roll:");
//            System.out.println("attacker:");
//            System.out.println("country " + attacker.getArmies());
//            System.out.println("player " + attacker.getOwner().getTotalStrength());
//            System.out.println("defender:");
//            System.out.println("country " + defender.getArmies());
//            System.out.println("player " + defender.getOwner().getTotalStrength());
        }
        return 0;
    }

    /**
     * Move the armies to the new conquered country
     * @param c1 attcker country, which the armies move out
     * @param c2 conquered country, whiche the armies move in
     * @param num the number of armies need to be move
     * @return 1 success, -1 somehow wrong
     */
    public int moveArmy(Country c1, Country c2, String num){

        int numArmies = 0;
        try{
            numArmies = Integer.valueOf(num);
        } catch (Exception e){
            return -1;
        }

        if (this.isContain(c1) && this.isContain(c2) && c1.getArmies() >= numArmies && numArmies >= dices) {
            c1.setArmies(c1.getArmies() - numArmies);
            c2.setArmies(c2.getArmies() + numArmies);
            return 1;
        }
        return -1;
    }

}
