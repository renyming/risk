package model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test Model class
 */
public class ContinentTest {

    private Continent asian;
    public Country china;
    public Country thailand;
    public Country singapore;

    /**
     * Each time invoke a method, set this info
     * @throws Exception exceptions
     */
    @Before
    public void setUp() throws Exception {

        asian = new Continent("Asian", 5);
        china = new Country("china", asian);
        thailand = new Country("thailand", asian);
        singapore = new Country("singapore", asian);
    }

    /**
     * Test addCountry() method
     */
    @Test
    public void addCountry() {
        ArrayList<Country> correct = new ArrayList<Country>();
        correct.add(china);
        correct.add(thailand);
        correct.add(singapore);

        asian.addCountry(china);
        asian.addCountry(thailand);
        asian.addCountry(singapore);
        ArrayList<Country> result = asian.getCountry();

        assertArrayEquals(correct.toArray(), result.toArray());

    }

    /**
     * Test getSize() method
     */
    @Test
    public void getSize() {
        int correct = 3;
        asian.addCountry(china);
        asian.addCountry(thailand);
        asian.addCountry(singapore);

        assertEquals(correct, asian.getSize());
    }

    /**
     * Test isEmpty() method
     */
    @Test
    public void isEmpty() {

        assertTrue(asian.isEmpty());
    }
}