package testModel;

import model.Continent;
import model.Country;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ContinentTest {

    private Continent asien;
    public Country china;
    public Country thailand;
    public Country singapore;

    @Before
    public void setUp() throws Exception {

        asien = new Continent("Asien", 5);
        china = new Country("china", asien);
        thailand = new Country("thailand", asien);
        singapore = new Country("singapore", asien);
    }

    @Test
    public void addCountry() {
        ArrayList<Country> correct = new ArrayList<Country>();
        correct.add(china);
        correct.add(thailand);
        correct.add(singapore);

        asien.addCountry(china);
        asien.addCountry(thailand);
        asien.addCountry(singapore);
        ArrayList<Country> result = asien.getCountry();

        assertArrayEquals(correct.toArray(), result.toArray());

    }

    @Test
    public void getSize() {
        int correct = 3;
        asien.addCountry(china);
        asien.addCountry(thailand);
        asien.addCountry(singapore);

        assertEquals(correct, asien.getSize());
    }

    @Test
    public void isEmpty() {

        assertTrue(asien.isEmpty());
    }
}