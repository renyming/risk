package com.risk.validate;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import com.risk.model.Continent;
import com.risk.model.Model;
import com.risk.model.Country;
import org.junit.rules.ExpectedException;
import com.risk.exception.InvalidMapException;

/**
 * Map Validator Test class.
 * @version 1.0.0
 */
public class MapValidatorTest {

    static MapValidator mapValidator;
    static Continent continent;
    static Continent continent2;
    static Country country;
    static Country country2;
    static Country country3;
    static Model model;

    static String continentName = "Asia";
    static String continentName2 = "Europe";
    static String countryName = "China";
    static String countryName2 = "Japan";
    static String countryName3 = "Korea";
    static int controlValue =7;

    private List<Continent> listOfContinents;

    /**
     * This method is invoked at the start of all the test methods.
     */
    @Before
    public void beforeTest() {
        continent = new Continent(continentName,controlValue);
        continent2 = new Continent(continentName2,controlValue);
        country = new Country(countryName,continent);
        country2 = new Country(countryName2,continent);
        country3 = new Country(countryName3,continent);
        model = new Model();
        mapValidator = new MapValidator();

		listOfContinents = new ArrayList<>();
		listOfContinents.add(continent);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * This method is used to test if a map is null or not.
     * @throws InvalidMapException Invalid map exception.
     */
    @Test
    public void validateMapForNullMap() throws InvalidMapException{
        exception.expect(InvalidMapException.class);
        exception.expectMessage("Empty file: no map exist.");
        MapValidator.validateMap(null);
    }

    /**
     * This method is used to test if a map has at least one continent or not.
     * @throws InvalidMapException Invalid map exception.
     */
    @Test
    public void validateMapForContinent() throws InvalidMapException {
        exception.expect(InvalidMapException.class);
        exception.expectMessage("Map should contain at least one continent.");
        MapValidator.validateMap(new Model());

    }

    /**
     * This method is used to test if a continent has at least one country or not.
     * @throws InvalidMapException Invalid map exception.
     */
    @Test
    public void validateContinentForCountry() throws InvalidMapException {
        exception.expect(InvalidMapException.class);
        exception.expectMessage("Continent: " + continent.getName() + " should contain at least one country");
        model.setContinents(listOfContinents);
        MapValidator.validateContinent(continent,model);

    }

    /**
     * This method is used to test if a continent is a sub-graph or not.
     */
    @Test
    public void validateContinentForSubGraph() {
        Assert.assertFalse(MapValidator.continentIsASubGraph(continent, model));
    }

    /**
     * This method is used to test if a country has adjacent country or not.
     * @throws InvalidMapException Invalid map exception.
     */
    @Test
    public void validateAdjCountry() throws InvalidMapException {
        exception.expect(InvalidMapException.class);
        exception.expectMessage("Country: " + country.getName()
                + " should be mapped with at least one adjacent country.");
        MapValidator.validateCountry(country, model);
    }

    /**
     * This method is used to test if a country is forming a connected sub graph.
     * @throws InvalidMapException Invalid map exception.
     */
    @Test
    public void validateConnectCountry() throws InvalidMapException {
        exception.expect(InvalidMapException.class);
        exception.expectMessage("Country: " + country.getName()
                + " is not forming a connected sub graph.");
        continent.addCountry(country);
        continent.addCountry(country2);
        continent.addCountry(country3);
        country.addEdge(country2);
        country2.addEdge(country);
        model.setContinents(listOfContinents);
        MapValidator.validateCountry(country, model);
    }

    /**
     * This method is used to test if a country is linked by all its adjacent country or not.
     * @throws InvalidMapException Invalid map exception.
     */
    @Test
    public void validateLinkCountry() throws InvalidMapException {
        exception.expect(InvalidMapException.class);
        exception.expectMessage("Country: " + country.getName()
                + " is not linked by all its adjacent Country: " + country2.getName());
        continent.addCountry(country);
        continent.addCountry(country2);
        continent.addCountry(country3);
        country.addEdge(country2);
        country2.addEdge(country3);
        country3.addEdge(country2);
        model.setContinents(listOfContinents);
        MapValidator.validateCountry(country, model);
    }

    /**
     * This method is used to test if a country is uniquely associated.
     * @throws InvalidMapException Invalid map exception.
     */
    @Test (expected = InvalidMapException.class)
    public void isCountryUniquelyAssociated() throws InvalidMapException {
        listOfContinents.add(continent2);
        continent.addCountry(country);
        continent2.addCountry(country);
        model.setContinents(listOfContinents);
        MapValidator.validateCountry(country, model);
    }
}
