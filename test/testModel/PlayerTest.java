package testModel;

import model.Continent;
import model.Country;
import model.Player;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

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

    @BeforeClass
    /**
     * Preparation before all this method
     */
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

        northAmerica = new Continent("NorthAmerica", 6);;

        canada = new Country("canada", northAmerica);
        usa = new Country("usa", northAmerica);

        canada.addEdge(usa);
        usa.addEdge(canada);

        northAmerica.addCountry(canada);
        northAmerica.addCountry(usa);

    }

    @Before
    /**
     * Each time invoke method, do this preparation
     */
    public void setUp() throws Exception {

        newPlayer = new Player("Lee");

        player = new Player("Ann");

        ArrayList<Country> countries= new ArrayList<Country>();
        countries.add(singapore);
        countries.add(canada);
        countries.add(usa);

        player.setArmies(10);
        player.setCountriesOwned(countries);
    }

    @Test
    /**
     * Test addInitArmies() method
     */
    public void addInitArmies() {

        int num = 15;
        newPlayer.addInitArmies();
        assertEquals(num, newPlayer.getArmies());
    }

    @Test
    /**
     * Test addRoundArmies() method
     */
    public void addRoundArmies() {
        int num = 17;
        player.addRoundArmies();
        assertEquals(num, player.getArmies());
    }

    @Test
    /**
     * Test subArmies() method
     */
    public void subArmies() {
        int num = 9;
        int subnum = 1;
        player.subArmies(1);
        assertEquals(num, player.getArmies());
    }

    @Test
    /**
     * Test isEmptyArmy() method
     */
    public void isEmptyArmy() {
        assertTrue(newPlayer.isEmptyArmy());
        assertFalse(player.isEmptyArmy());
    }

    @Test
    /**
     * Test addCountry() method
     */
    public void addCountry() {
        Country[] correct = {singapore, canada, usa, china};

        player.addCountry(china);
        int size = player.getCountriesOwned().size();
        Country[] re = player.getCountriesOwned().toArray(new Country[size]);

        assertArrayEquals(correct, re);
    }

    @Test
    /**
     * Test delCountry() method
     */
    public void delCountry() {

        Country[] correct = {singapore, canada};

        player.delCountry(usa);
        int size = player.getCountriesOwned().size();
        Country[] re = player.getCountriesOwned().toArray(new Country[size]);

        assertArrayEquals(correct, re);
    }

    @Test
    /**
     * Test isContain() method
     */
    public void isContain() {

        assertTrue(player.isContain(usa));
        assertFalse(player.isContain(china));
    }

    @Test
    /**
     * Test isConnected() method
     */
    public void isConnected() {

        assertTrue(player.isConnected(canada, usa));
        assertFalse(player.isConnected(canada, singapore));
        assertFalse(player.isConnected(china, singapore));
    }

}