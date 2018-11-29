package com.risk.strategy;

import com.risk.common.Action;
import com.risk.model.Continent;
import com.risk.model.Country;
import com.risk.model.Phase;
import com.risk.model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is the test of AggressiveStrategy test
 */
public class AggressiveStrategyTest {

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
    @SuppressWarnings("Duplicates")
    @BeforeClass
    public static void before() throws Exception {


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
     *
     * @throws Exception exceptions
     */
    @Before
    public void setUp() throws Exception {

        newPlayer = new Player("Lee", 5, "aggressive computer");

        player = new Player("Ann", 5, "aggressive computer");

        ArrayList<Country> countries = new ArrayList<Country>();
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


        defender = new Player("Mike", 5, "aggressive computer");
        defender.setTotalStrength(5);

        china.setPlayer(defender);
        china.setArmies(4);
        defender.addCountry(china);
        thailand.setPlayer(defender);
        thailand.setArmies(1);
        defender.addCountry(thailand);
    }

    /**
     * Test reinforcement() method
     */
    @Test
    public void reinforcement() {

        // original totalStrength = 5
        // china = 4, thailand = 1
        // addedArmies = 3
        defender.reinforcement();
        assertEquals(7, china.getArmies());
        assertEquals(1, thailand.getArmies());
        assertEquals(0, defender.getArmies());
        assertEquals(8, defender.getTotalStrength());
        Assert.assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());

        // original totalStrength = 10
        //singapore = 1, canada = 1, usa = 8
        // addedArmies = 7
        player.reinforcement();
        assertEquals(1, singapore.getArmies());
        assertEquals(1, canada.getArmies());
        assertEquals(15, usa.getArmies());
        assertEquals(17, player.getTotalStrength());
        assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());

    }

    /**
     * Test1 of attack() method
     */
    @Test
    public void attack1() {

        defender.attack(null, "0", null, "0", true);
        assertEquals(defender, singapore.getOwner());
        assertEquals(1, player.getTotalCards());
        assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());

    }

    /**
     * Test2 of attack() method
     */
    @Test
    public void attack2() {

        //set up
        singapore.addEdge(thailand);
        thailand.addEdge(singapore);

        // original totalStrength = 10
        //singapore = 1, canada = 1, usa = 8
        singapore.setArmies(100);
        canada.setArmies(1);
        usa.setArmies(8);

        player.setTotalStrength(109);

        player.attack(null, "0", null, "0", true);
        assertEquals(player, china.getOwner());
        assertEquals(player, thailand.getOwner());
        assertTrue(player.getContinentsOwned().contains(asien));
        assertEquals(Action.Win, Phase.getInstance().getActionResult());

    }


    /**
     * test fortification() method
     */
    @Test
    public void fortification() {

        // original totalStrength = 10
        //singapore = 1, canada = 1, usa = 8
        player.fortification(null, null,0);
        assertEquals(1, singapore.getArmies());
        assertEquals(0, canada.getArmies());
        assertEquals(9, usa.getArmies());
        assertEquals(10, player.getTotalStrength());
        assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());


        // original totalStrength = 5
        // china = 4, thailand = 1
        // addedArmies = 3
        defender.fortification(null, null,0);
        assertEquals(5, china.getArmies());
        assertEquals(0, thailand.getArmies());
        assertEquals(0, defender.getArmies());
        assertEquals(5, defender.getTotalStrength());
        assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());
    }



}
