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
    public static void before() throws Exception{


        asien = new Continent("Asien", 5);
        china = new Country("china", asien);
        thailand = new Country("thailand", asien);
        singapore = new Country("singapore", asien);


        asien.addCountry(china);
        asien.addCountry(thailand);
        asien.addCountry(singapore);

        northAmerica = new Continent("NorthAmerica", 6);;
        canada = new Country("canada", northAmerica);
        usa = new Country("usa", northAmerica);

        northAmerica.addCountry(canada);
        northAmerica.addCountry(usa);

    }

    @Before
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
    public void addInitArmies() {

        int num = 15;
        newPlayer.addInitArmies();
        assertEquals(num, newPlayer.getArmies());
    }

    @Test
    public void addRoundArmies() {
        int num = 17;
        player.addRoundArmies();
        assertEquals(num, player.getArmies());
    }

    @Test
    public void subArmies() {
        int num = 9;
        int subnum = 1;
        player.subArmies(1);
        assertEquals(num, player.getArmies());
    }

    @Test
    public void isEmptyArmy() {
        assertTrue(newPlayer.isEmptyArmy());
        assertFalse(player.isEmptyArmy());
    }

    @Test
    public void addCountry() {
        Country[] correct = {singapore, canada, usa, china};

        player.addCountry(china);
        int size = player.getCountriesOwned().size();
        Country[] re = player.getCountriesOwned().toArray(new Country[size]);

        assertArrayEquals(correct, re);
    }

    @Test
    public void delCountry() {

        Country[] correct = {singapore, canada};

        player.delCountry(usa);
        int size = player.getCountriesOwned().size();
        Country[] re = player.getCountriesOwned().toArray(new Country[size]);

        assertArrayEquals(correct, re);
    }

}