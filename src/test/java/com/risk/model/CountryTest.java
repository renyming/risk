package com.risk.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test Country class
 */
public class CountryTest {

    Continent continent;
    Country country1;
    Country country2;
    Player player1;
    Player player2;

    /**
     * Each time invoke a method, set this info
     * @throws Exception exceptions
     */
    @Before
    public void setUp() throws Exception {
        continent = new Continent("OuterSpace",10);
        country1 = new Country("Country1",continent);
        country2 = new Country("Country2",continent);
        country1.addEdge(country2);
        country2.addEdge(country1);

        player1 = new Player("player1", 2);
        player2 = new Player("player2", 2);

        country1.setPlayer(player1);
        country2.setPlayer(player2);
    }

    /**
     * Test addEdge() method
     */
    @Test
    public void addEdge() {
        ArrayList<Country> result = new ArrayList<>();
        result.add(country2);
        assertTrue(country1.getAdjCountries().equals(result));

        result.clear();
        result.add(country1);
        assertTrue(country2.getAdjCountries().equals(result));

        assertFalse(country1.getAdjCountries().equals(result));
    }

    /**
     * Test equals() method
     */
    @Test
    public void equals() {
        assertFalse(country1.equals(country2));
        assertTrue(country1.equals(country1));
    }

    /**
     * Test addArmies() method
     */
    @Test
    public void addArmies() {

        player1.setArmies(2);
        assertEquals(country1.getArmies(),0);
        assertTrue(country1.addArmies(2));
        assertEquals(country1.getArmies(),2);

        player1.setArmies(5);
        assertFalse(country1.addArmies(10));
        assertEquals(country1.getArmies(),2);

        //boundary condition
        assertTrue(country1.addArmies(5));
        assertEquals(country1.getArmies(),7);
    }

    /**
     * Test attack() method
     */
    @Test
    public void attack() {
        Country country3 = new Country("country3",continent);
        assertFalse(country1.attack(country3));
        assertTrue(country1.attack(country2));
    }
}