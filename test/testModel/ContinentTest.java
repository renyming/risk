package testModel;

import model.Continent;
import model.Country;
import org.junit.Before;
import org.junit.BeforeClass;
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

    @Before
    /**
     * Each time invoke a method, set this info
     */
    public void setUp() throws Exception {

        asian = new Continent("Asian", 5);
        china = new Country("china", asian);
        thailand = new Country("thailand", asian);
        singapore = new Country("singapore", asian);
    }

    @Test
    /**
     * Test addCountry() method
     */
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

    @Test
    /**
     * Test getSize() method
     */
    public void getSize() {
        int correct = 3;
        asian.addCountry(china);
        asian.addCountry(thailand);
        asian.addCountry(singapore);

        assertEquals(correct, asian.getSize());
    }

    @Test
    /**
     * Test isEmpty() method
     */
    public void isEmpty() {

        assertTrue(asian.isEmpty());
    }
}