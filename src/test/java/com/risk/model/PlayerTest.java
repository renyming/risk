package com.risk.model;

import com.risk.common.Action;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test Player class
 */
public class PlayerTest {

    public static Continent asien;
    public static Country china;
    public static Country thailand;
    public static Country singapore;

    public static Continent northAmerica;
    public static Country canada;
    public static Country usa;


    private Player newPlayer;
    private Player player;
    private Player defender;

    /**
     * Preparation before all this method
     * @throws Exception exceptions
     */
    @BeforeClass
    public static void before() throws Exception{


        asien = new Continent("Asian", 5);

        china = new Country("china", asien);
        thailand = new Country("thailand", asien);
        singapore = new Country("singapore", asien);


        china.addEdge(thailand);
        china.addEdge(singapore);
        thailand.addEdge(china);
        singapore.addEdge(china);


        asien.addCountry(china);
        asien.addCountry(thailand);
        asien.addCountry(singapore);

        china.setContinent(asien);
        thailand.setContinent(asien);
        singapore.setContinent(asien);

        northAmerica = new Continent("NorthAmerica", 6);

        canada = new Country("canada", northAmerica);
        usa = new Country("usa", northAmerica);

        canada.addEdge(usa);
        usa.addEdge(canada);

        northAmerica.addCountry(canada);
        northAmerica.addCountry(usa);

        canada.setContinent(northAmerica);
        usa.setContinent(northAmerica);

    }

    /**
     * Each time invoke method, do this preparation
     * @throws Exception exceptions
     */
    @Before
    public void setUp() throws Exception {

        newPlayer = new Player("Lee",5, "human player");

        player = new Player("Ann",5, "human player");

        ArrayList<Country> countries= new ArrayList<Country>();
        singapore.setPlayer(player);
        singapore.setArmies(1);
        player.addCountry(singapore);
        canada.setPlayer(player);
        canada.setArmies(1);
        player.addCountry(canada);
        usa.setPlayer(player);
        usa.setArmies(8);
        player.addCountry(usa);
        player.setTotalStrength(10);

//        countries.add(singapore);
//        countries.add(canada);
//        countries.add(usa);

//
//        player.setTotalStrength(10);


        defender = new Player("Mike", 5, "human player");
        defender.setTotalStrength(5);

        china.setPlayer(defender);
        china.setArmies(4);
        defender.addCountry(china);
        thailand.setPlayer(defender);
        thailand.setArmies(1);
        defender.addCountry(thailand);



    }

    /**
     * Test addRoundArmies() method
     */
    @Test
    public void addRoundArmies() {
        int num = 7;
        player.addRoundArmies();
        assertEquals(num, player.getArmies());

        num = 3;
        player.delCountry(usa);
        player.addRoundArmies();
        assertEquals(num, player.getArmies());

        num = 3;
        newPlayer.addRoundArmies();
        assertEquals(num, newPlayer.getArmies());

    }

//    /**
//     * Test doubleArmies() method
//     */
//    @Test
//    public void doubleArmies(){
//
//        // original totalStrength = 5
//        // china = 4, thailand = 1
//
//        defender.doubleArmies(c -> true);
//        assertEquals(8, china.getArmies());
//        assertEquals(2, thailand.getArmies());
//        assertEquals(10, defender.getTotalStrength());
//
//        // original totalStrength = 9
//        //singapore = 0, canada = 1, usa = 8
//
//        player.doubleArmies(c -> true);
//        assertEquals(2, singapore.getArmies());
//        assertEquals(2, canada.getArmies());
//        assertEquals(16, usa.getArmies());
//        assertEquals(20, player.getTotalStrength());
//
//        // has condition when country has enemy around
//        player.doubleArmies(c -> c.hasAdjEnemy());
//        assertEquals(4, singapore.getArmies());
//        assertEquals(2, canada.getArmies());
//        assertEquals(16, usa.getArmies());
//        assertEquals(22, player.getTotalStrength());
//
//    }

    /**
     * Test subArmies() method
     */
    @Test
    public void subArmies() {
        player.setArmies(10);
        int num = 9;
        int subnum = 1;
        player.subArmies(1);
        assertEquals(num, player.getArmies());
    }

    /**
     * Test isEmptyArmy() method
     */
    @Test
    public void isEmptyArmy() {
        player.setArmies(10);
        assertTrue(newPlayer.isEmptyArmy());
        assertFalse(player.isEmptyArmy());
    }

    /**
     * Test addCountry() method
     */
    @Test
    public void addCountry() {
        Country[] correct = {singapore, canada, usa, china};

        player.addCountry(china);
        int size = player.getCountriesOwned().size();
        Country[] re = player.getCountriesOwned().toArray(new Country[size]);

        assertArrayEquals(correct, re);
    }

    /**
     * Test delCountry() method
     */
    @Test
    public void delCountry() {

        Country[] correct = {singapore, canada};

        player.delCountry(usa);
        int size = player.getCountriesOwned().size();
        Country[] re = player.getCountriesOwned().toArray(new Country[size]);

        assertArrayEquals(correct, re);
    }

    /**
     * Test isContain() method
     */
    @Test
    public void isContain() {

        assertTrue(player.isContain(usa));
        assertFalse(player.isContain(china));
    }

    /**
     * Test isConnected() method
     */
    @Test
    public void isConnected() {

        assertTrue(player.isConnected(canada, usa));
        assertFalse(player.isConnected(canada, singapore));
        assertFalse(player.isConnected(china, singapore));
    }


    /**
     * Test isConnected() method
     */
//    @Test
//    public void getRandomDice() {
//
//        System.out.println(player.getRandomDice(3));
//        System.out.println(player.getRandomDice(2));
//        System.out.println(player.getRandomDice(10));
//
//    }

    /**
     * Test attack() method
     */
    @Test
    public void attack() {

        Phase.getInstance().setCurrentPhase("Attack Phase");

        // test invalidation
        singapore.getOwner().attack(singapore, "5", china, "2", false);
        assertEquals(Action.Invalid_Move, Phase.getInstance().getActionResult());

        singapore.getOwner().attack(singapore, "3", china, "3", false);
        assertEquals(Action.Invalid_Move, Phase.getInstance().getActionResult());

        singapore.getOwner().attack(singapore, "3", china, "-1", false);
        assertEquals(Action.Invalid_Move, Phase.getInstance().getActionResult());

        singapore.getOwner().attack(singapore, "3", canada, "1", false);
        assertEquals(Action.Invalid_Move, Phase.getInstance().getActionResult());

        singapore.getOwner().attack(singapore, "3", china, "0", false);
        assertEquals(Action.Invalid_Move, Phase.getInstance().getActionResult());

        // test attacket occupied a country
        singapore.setArmies(100);
        china.setArmies(2);
        singapore.getOwner().attack(singapore, "3", china, "2", true);
        assertEquals(player, china.getOwner());
        assertEquals(Action.Move_After_Conquer, Phase.getInstance().getActionResult());

        // test no attack possible after attack
        Phase.getInstance().clearActionResult();
        player.delCountry(china);
        singapore.setArmies(2);
        china.setPlayer(defender);
        defender.addCountry(china);
        china.setArmies(100);
        singapore.getOwner().attack(singapore, "2", china, "2", true);
        assertEquals("Attack Impossible. You Can Enter Next Phase Now.", Phase.getInstance().getInvalidInfo());
        assertEquals(Action.Attack_Impossible, Phase.getInstance().getActionResult());

        // test attacker occupied all the countries, and win the game
        player.delCountry(china);
        singapore.setArmies(20);
        china.setPlayer(defender);
        defender.addCountry(china);
        china.setArmies(2);
        player.addCountry(thailand);
        singapore.getOwner().attack(singapore, "3", china, "2", true);
        assertEquals(Action.Win, Phase.getInstance().getActionResult());

    }

    /**
     * Test of fortification method
     */
    @Test
    public void fortification() {

        // valid move
        canada.setArmies(5);
        usa.setArmies(0);
        player.fortification(canada, usa, 5);
        assertEquals(0, canada.getArmies());
        assertEquals(5, usa.getArmies());

        // move between countries not own by one player
        player.fortification(usa, china, 5);
        assertEquals(Action.Invalid_Move, Phase.getInstance().getActionResult());

        // armies want to move exceeded the country has
        player.fortification(usa, canada, 10);
        assertEquals(Action.Invalid_Move, Phase.getInstance().getActionResult());

    }

}