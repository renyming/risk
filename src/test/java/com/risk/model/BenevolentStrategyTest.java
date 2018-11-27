package com.risk.model;

import com.risk.common.Action;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 This is the test class for the BenevolentStrategy.
 **/
public class BenevolentStrategyTest {

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

            newPlayer = new Player("Lee", 5, "benevolent");

            player = new Player("Ann", 5, "benevolent");

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


            defender = new Player("Mike", 5, "benevolent");
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
        assertEquals(4, china.getArmies());
        assertEquals(4, thailand.getArmies());
        assertEquals(0, defender.getArmies());
        assertEquals(8, defender.getTotalStrength());
        assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());

        // original totalStrength = 10
        //singapore = 1, canada = 1, usa = 8
        // addedArmies = 7
        player.reinforcement();
        assertEquals(8, singapore.getArmies());
        assertEquals(1, canada.getArmies());
        assertEquals(8, usa.getArmies());
        assertEquals(17, player.getTotalStrength());
        assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());
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
        assertEquals(5, canada.getArmies());
        assertEquals(4, usa.getArmies());
        assertEquals(10, player.getTotalStrength());
        assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());


        // original totalStrength = 5
        // china = 4, thailand = 1
        // addedArmies = 3
        defender.fortification(null, null,0);
        assertEquals(2, china.getArmies());
        assertEquals(3, thailand.getArmies());
        assertEquals(0, defender.getArmies());
        assertEquals(5, defender.getTotalStrength());
        assertEquals(Action.Show_Next_Phase_Button, Phase.getInstance().getActionResult());
    }



    }
