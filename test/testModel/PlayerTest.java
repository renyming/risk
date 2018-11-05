package testModel;

import model.Continent;
import model.Country;
import model.Player;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
    private Player defender;

    /**
     * Preparation before all this method
     * @throws Exception exceptions
     */
    @BeforeClass
    public static void before() throws Exception{


        asien = new Continent("Asian", 5);

        china = new Country("china", asien);
        china.setArmies(1);
        thailand = new Country("thailand", asien);
        singapore = new Country("singapore", asien);
        singapore.setArmies(3);

        china.addEdge(thailand);
        china.addEdge(singapore);
        thailand.addEdge(china);
        singapore.addEdge(china);


        asien.addCountry(china);
        asien.addCountry(thailand);
        asien.addCountry(singapore);

        northAmerica = new Continent("NorthAmerica", 6);

        canada = new Country("canada", northAmerica);
        usa = new Country("usa", northAmerica);

        canada.addEdge(usa);
        usa.addEdge(canada);

        northAmerica.addCountry(canada);
        northAmerica.addCountry(usa);

    }

    /**
     * Each time invoke method, do this preparation
     * @throws Exception exceptions
     */
    @Before
    public void setUp() throws Exception {

        newPlayer = new Player("Lee");

        player = new Player("Ann");

        ArrayList<Country> countries= new ArrayList<Country>();
        countries.add(singapore);
        singapore.setPlayer(player);
        countries.add(canada);
        canada.setPlayer(player);
        countries.add(usa);
        usa.setPlayer(player);

        player.setArmies(10);
        player.setTotalStrength(10);
        player.setCountriesOwned(countries);

        defender = new Player("Mike");
        defender.setTotalStrength(5);
        defender.addCountry(china);
        china.setPlayer(defender);
    }

    /**
     * Test addRoundArmies() method
     */
    @Test
    public void addRoundArmies() {
        int num = 17;
        player.addRoundArmies();
        assertEquals(num, player.getArmies());

        num = 20;
        player.delCountry(usa);
        player.addRoundArmies();
        assertEquals(num, player.getArmies());

        num = 3;
        newPlayer.addRoundArmies();
        assertEquals(num, newPlayer.getArmies());

    }

    /**
     * Test subArmies() method
     */
    @Test
    public void subArmies() {
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
    @Test
    public void getRandomDice() {

        System.out.println(player.getRandomDice(3));
        System.out.println(player.getRandomDice(2));
        System.out.println(player.getRandomDice(10));

    }

    /**
     * Test attack() method
     */
    @Test
    public void attack() {

        int result = singapore.getOwner().attack(singapore, 5, china, 2);
        assertEquals(-1, result);

        result = singapore.getOwner().attack(singapore, 3, china, 3);
        assertEquals(-1, result);

        result = singapore.getOwner().attack(singapore, 3, china, -1);
        assertEquals(-1, result);

        result = singapore.getOwner().attack(singapore, 3, canada, 1);
        assertEquals(-1, result);

        result = singapore.getOwner().attack(singapore, 3, china, 0);
        assertEquals(-1, result);

        china.setArmies(0);
        result = singapore.getOwner().attack(singapore, 3, china, 0);
        assertTrue(player.isContain(china));
        assertFalse(defender.isContain(china));
        System.out.println(result);

    }




}