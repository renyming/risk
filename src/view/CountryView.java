package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import model.Country;
import model.Player;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CountryView implements Observer {

    private static int IdCounter = 0;

    private int Id;
    private int armies;
    private String name;
    private Player owner;
    private String ownerColor = "red";
    private String continentColor = "blue";
    private double locationX;
    private double locationY;

    private Country country;
    private View view;
    private AnchorPane countryPane;
    private CountryController countryController;



    /**
     * Get necessary info for create a default countryPane, load the countryPane from Country.xml
     * @param view The view object
     */
    public CountryView(View view) {
        Id = ++IdCounter;
        this.view = view;
        try {
            FXMLLoader countryFxmlLoader = new FXMLLoader(getClass().getResource("Country.fxml"));
            countryPane = countryFxmlLoader.load();
            countryController = countryFxmlLoader.getController();
        } catch (Exception e) {
            System.out.println("CountryView ctor: " + e);
        }
        countryController.initiate(this);
    }



    /**
     * Observer update method, update all Country info, store and set it to countryPane
     * Called by corresponding Country Observable subject
     * @param obj The relative Country Observable object
     * @param x Additional info for update
     */
    @Override
    public void update(Observable obj, Object x) {
//        System.out.println("CountryView.update(): ");
        country = (Country) obj;
        Id = country.getId();
        name = country.getName();
        armies = country.getArmies();
        owner = country.getOwner();
        ownerColor = owner.getColor();
        continentColor = country.getContinent().getColor();
        locationX = country.getX();
        locationY = country.getY();
        countryPane.setLayoutX(locationX);
        countryPane.setLayoutY(locationY);
        countryController.updateCountryPaneInfo(name, ownerColor, continentColor, armies);
    }



    /**
     * View use this function to add/remove the countryPane to the mapRootPane
     * or clear the countryPane's component
     * Called by View
     * @return the countryPane object
     */
    public AnchorPane getCountryPane() { return countryPane; }

    public void clicked() { view.clickedCountry(country); }

//    public void pressed() { view.pressedCountry(country); }
//
//    public void entered() { view.enteredCountry(country); }
//
//    public void released() { view.releasedCountry(country); }

    /**
     * Get the country id as key
     * Called by View when store CountryView into CountryViews HashMap
     * @return countryView Id
     */
    public int getId() { return Id; }

    public double getLocationX() { return locationX; }

    public double getLocationY() { return locationY; }

    public Country getCountry() { return country; }

    public ArrayList<Country> getAdjCountries() { return country.getAdjCountries(); }
}