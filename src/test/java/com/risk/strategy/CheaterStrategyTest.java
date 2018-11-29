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
import static org.junit.Assert.*;

/**
 * Test Player class
 */
public class CheaterStrategyTest {

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
     *
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

        newPlayer = new Player("Lee", 5, "cheater computer");

        player = new Player("Ann", 5, "cheater computer");

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


        defender = new Player("Mike", 5, "cheater computer");
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
        defender.reinforcement();
        assertEquals(8, china.getArmies());
        assertEquals(2, thailand.getArmies());
        assertEquals(10, defender.getTotalStrength());
        Assert.assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());

        // original totalStrength = 9
        //singapore = 0, canada = 1, usa = 8
        player.reinforcement();
        assertEquals(2, singapore.getArmies());
        assertEquals(2, canada.getArmies());
        assertEquals(16, usa.getArmies());
        assertEquals(20, player.getTotalStrength());
        assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());
    }

    @Test
    public void attack(){

        // first attack, conquer china
        player.attack(null, "0", null, "0", false);
        assertEquals(player, china.getOwner());
        assertEquals(Action.Move_After_Conquer, Phase.getInstance().getActionResult());
        assertEquals(1, player.getTotalCards());

        // second attack, conquer thailand, win the game
        player.attack(null, "0", null, "0", false);
        assertEquals(player, thailand.getOwner());
        assertEquals(Action.Win, Phase.getInstance().getActionResult());
        assertEquals(2, player.getTotalCards());

    }


    /**
     * test fortification() method
     */
    @Test
    public void fortification() {

        // original totalStrength = 10
        //singapore = 1, canada = 1, usa = 8
        player.fortification(null, null,0);
        assertEquals(2, singapore.getArmies());
        assertEquals(1, canada.getArmies());
        assertEquals(8, usa.getArmies());
        assertEquals(11, player.getTotalStrength());
        assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());
    }
}
