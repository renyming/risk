package validate;

import model.Continent;
import model.Model;
import model.Country;
import exception.InvalidMapException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Map validator.
 */
public class MapValidator {

    /**
     * Validate the map data.
     * @param model model object
     * @throws InvalidMapException invalid exception
     */
    public static void validateMap(Model model) throws InvalidMapException {
        if (model != null) {
            if (model.getContinents().size() > 0) {
                for (Continent continent : model.getContinents()) {
                    if (continent != null) {
                        validateContinent(continent, model);
                    }
                }
            } else {
                throw new InvalidMapException("Map should contain at least one continent.");
            }
            isCountryUniquelyAssociated(model);
        } else {
            throw new InvalidMapException("Empty file: no map exist.");
        }
    }

    /**
     * Validate continet. It should have at least one country.
     * @param continent continent object
     * @param model model object
     * @throws InvalidMapException invalid map exception
     */
    public static void validateContinent(Continent continent, Model model) throws InvalidMapException {
        if (continent.getCountry().size() < 1) {
            throw new InvalidMapException(
                    "Continent: " + continent.getName() + " should contain at least one country");
        }
        if (!continentIsASubGraph(continent, model)) {
            throw new InvalidMapException("Continent: " + continent.getName()
                    + " is not a subgraph. The continent should be connected to another continent via country.");
        }
        for (Country country : continent.getCountry()) {
            if (country != null) {
                validateCountry(country, model);
            }
        }
    }

    /**
     * Check if graph is a connected graph.
     * @param continent continent object
     * @param model model object
     * @return boolean true or false
     */
    public static boolean continentIsASubGraph(Continent continent, Model model) {
        boolean isASubGraph = false;
        HashSet<Continent> set = new HashSet<>();
        for (Country country : continent.getCountry()) {
            for (Continent otherContinent : model.getContinents()) {
                if (!otherContinent.equals(continent)) {
                    for (Country otherCountry : otherContinent.getCountry()) {
                        if (otherCountry.getAdjCountries().contains(country)) {
                            set.add(otherContinent);
                        }
                    }
                }
            }
        }
        if (set.size() > 0 || model.getContinents().size()==1) {
            isASubGraph = true;
        }
        return isASubGraph;
    }

    /**
     * Check if the country is a valid country. It should has at least one adjacent neighbor country
     * @param country country object
     * @param model model object
     * @throws InvalidMapException invalid map exception
     */
    public static void validateCountry(Country country, Model model) throws InvalidMapException {

        List<Country> adjCountryList = country.getAdjCountries();

        if (adjCountryList != null && adjCountryList.size() < 1) {
            throw new InvalidMapException(
                    "Country: " + country.getName() + " should be mapped with at least one adjacent country.");
        } else if (!isCountryAConnectedGraph(country)) {
            throw new InvalidMapException(
                    "Country: " + country.getName() + " is not forming a connected sub graph.");
        } else {
            for (Country adjCountry : adjCountryList) {
                if (!adjCountry.getAdjCountries().contains(country)) {
                    throw new InvalidMapException("Country: " + country.getName()
                            + " is not linked by all its adjacent Country: " + adjCountry.getName());
                }
            }
        }
    }

    /**
     * Check whether a country forms a connected graph or not.
     * @param country country object
     * @return boolean isCountry a connected graph
     */
    public static boolean isCountryAConnectedGraph(Country country) {
        HashSet<Country> coountrySet = new HashSet<>();
        Continent continent = country.getContinent();
        List<Country> countryList = continent.getCountry();
        coountrySet.add(country);
        country.setProcessed(true);

        checkGraph(country, coountrySet);
        for (Country cou : continent.getCountry()) {
            cou.setProcessed(false);
        }
        return coountrySet.containsAll(countryList);
    }

    /**
     * Check if a graph is connected or not.
     * @param country country object
     * @param tSet tSet object
     */
    public static void checkGraph(Country country, HashSet<Country> tSet) {
        boolean isUnProcessedCountry = false;
        for (Country t : country.getAdjCountries()) {
            if (t.getContinent().equals(country.getContinent()) && !t.isProcessed()) {
                t.setProcessed(true);
                isUnProcessedCountry = true;
                tSet.add(t);
                checkGraph(t, tSet);
            }
        }
        if (!isUnProcessedCountry) {
            return;
        }
    }

    /**
     * Check if the countries are uniquely associated with the continent.
     * @param model model object
     * @throws InvalidMapException invalid map exception
     */
    public static void isCountryUniquelyAssociated(Model model) throws InvalidMapException {
        HashMap<Country, Integer> countryAssociation = new HashMap<>();

        for (Continent continent : model.getContinents()) {
            for (Country country : continent.getCountry()) {
                if (countryAssociation.containsKey(country)) {
                    countryAssociation.put(country, countryAssociation.get(country) + 1);
                } else {
                    countryAssociation.put(country, 1);
                }
            }
        }

        for (Map.Entry<Country, Integer> set : countryAssociation.entrySet()) {
            if (set.getValue() > 1) {
                throw new InvalidMapException(
                        "Country: " + set.getKey().getName() + " belongs to multiple continent.");
            }
        }
    }



}
