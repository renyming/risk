package testMapEditor;

import mapeditor.Country;
import mapeditor.Writer;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;


public class WriteFileTest {

    private static ArrayList<Country> countries;
    private static String filePath;

    @BeforeClass
    public static void beforeClass(){

        countries = new ArrayList<Country>();

        Country country = new Country(100,200);
        country.setContinent("Asia");
        countries.add(country);

        Country newCountry1 = new Country(101,202);
        newCountry1.setContinent("Europe");
        countries.add(newCountry1);

        Country newCountry2 = new Country(201,402);
        newCountry2.setContinent("NorthAmerica");
        countries.add(newCountry2);

        Country newCountry3 = new Country(12,14);
        newCountry3.setContinent("Asia");
        countries.add(newCountry3);

        Country aloneCountry = new Country(0,0);
        aloneCountry.setContinent("Naniya");
        countries.add(aloneCountry);

        country.getAdjList().add(newCountry1);
        newCountry1.getAdjList().add(country);

        country.getAdjList().add(newCountry2);
        newCountry2.getAdjList().add(country);

        newCountry2.getAdjList().add(newCountry3);
        newCountry3.getAdjList().add(newCountry2);

        filePath = "writeTest1.map";
    }

    @Test
    public void testWriteFile() throws IOException {
        Writer writer= new Writer(countries,filePath);
        assertFalse(writer.write());
    }
}
