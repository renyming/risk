package testModel;

import model.Continent;
import model.Country;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ContinentTest {

    private Continent asian;
    public Country china;
    public Country thailand;
    public Country singapore;

    @Before
    public void setUp() throws Exception {

        asian = new Continent("Asian", 5);
        china = new Country("china", asian);
        thailand = new Country("thailand", asian);
        singapore = new Country("singapore", asian);
    }

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

    @Test
    public void getSize() {
        int correct = 3;
        asian.addCountry(china);
        asian.addCountry(thailand);
        asian.addCountry(singapore);

        assertEquals(correct, asian.getSize());
    }

    @Test
    public void isEmpty() {

        assertTrue(asian.isEmpty());
    }
}