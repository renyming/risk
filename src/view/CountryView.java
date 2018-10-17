package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Country;
import model.Player;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CountryView implements Observer {

    private static int IdCounter = 0;

    private int Id;
    private int armies;        // used for additional display
    private String name;       // used for additional display
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
     * @param layoutX The x location of the countryPane center relative to the mapRootPane
     * @param layoutY The y location of the countryPane center relative to the mapRootPane
     */
    public CountryView(View view, double layoutX, double layoutY, String ownerColor, String continentColor) {
        Id = ++IdCounter;
        this.view = view;
        this.continentColor = continentColor;
        try {
            FXMLLoader countryFxmlLoader = new FXMLLoader(getClass().getResource("Country.fxml"));
            countryPane = countryFxmlLoader.load();
            countryController = countryFxmlLoader.getController();
        } catch (Exception e) {
            System.out.println("CountryView ctor: " + e);
        }
        countryController.initiate(this);
        countryController.setDefaultInfo("Country_"+Id, 0, ownerColor, continentColor);
        countryPane.setLayoutX(layoutX + view.getCountryViewWidth()/2);
        countryPane.setLayoutY(layoutY + view.getCountryViewHeight()/2);
        // TODO: edd drag event
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
        System.out.println("CountryView.update(): " + country.getContinent().getColor());
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

    public void removeCountryView() { view.removeCountryView(this); }

    public void clicked() { view.clickedCountry(country); }

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