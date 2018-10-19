package testMapEditor;

import mapeditor.Country;
import mapeditor.Writer;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Test Writer class
 */
public class WriteFileTest {

    private static ArrayList<Country> countries;
    private static String filePath;

    /**
     * initiation before testing
     */
    @BeforeClass
    public static void beforeClass(){

        countries = new ArrayList<Country>();

        Country country = new Country("China",100,200, "Aisa");
        countries.add(country);

        Country newCountry1 = new Country("UK",101,202, "Europe");
        countries.add(newCountry1);

        Country newCountry2 = new Country("Canada",201,402 ,"NorthAmerica");
        countries.add(newCountry2);

        Country newCountry3 = new Country("Japan",12,14, "Asia");
        newCountry3.setContinent("Asia");
        countries.add(newCountry3);

        Country aloneCountry = new Country("????",0,0,"?????");
        countries.add(aloneCountry);

        country.getAdjList().add(newCountry1);
        newCountry1.getAdjList().add(country);

        country.getAdjList().add(newCountry2);
        newCountry2.getAdjList().add(country);

        newCountry2.getAdjList().add(newCountry3);
        newCountry3.getAdjList().add(newCountry2);

        filePath = "writeTest1.map";
    }

    /**
     * test for validation of player designed map
     * @throws IOException IOException
     */
    @Ignore
    @Test
    public void testWriteFile() throws IOException {
        Writer writer= new Writer(countries,filePath);
        assertFalse(writer.write());
    }
}
