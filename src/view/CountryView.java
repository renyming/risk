package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import model.Country;
import model.Player;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/**
 * Responsible for visualizing the Country object, show useful info to user
 * Corresponding Observable subject is Country
 */
public class CountryView implements Observer {

    private static int IdCounter = 0;

    private int Id;
    private Country country;
    private View view;
    private AnchorPane countryPane;
    private CountryController countryController;


    /**
     * Create a default CountryView, add View reference, load the countryPane from Country.xml
     * @param view Default country view
     */
    public CountryView(View view) {
        Id = ++IdCounter;
        this.view = view;
        try {
            FXMLLoader countryFxmlLoader = new FXMLLoader(getClass().getResource("Country.fxml"));
            countryPane = countryFxmlLoader.load();
            countryController = countryFxmlLoader.getController();
            countryController.initiate(this);
        } catch (Exception e) {
            System.out.println("CountryView ctor: " + e);
        }
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
        Id = country.getId(); // TODO: may not need, not sure
        countryPane.setLayoutX(country.getX());
        countryPane.setLayoutY(country.getY());
        countryController.updateCountryPaneInfo(country.getName(), country.getOwner().getColor(), country.getContinent().getColor(), country.getArmies());
    }


    /**
     * View use this function to add/remove the countryPane to the mapRootPane
     * or clear the countryPane's component
     * Called by View.*()
     * @return the countryPane reference
     */
    public AnchorPane getCountryPane() { return countryPane; }


    /**
     * Called by countryController when countryPane is clicked by user
     * Pass this event to view
     */
    public void clicked() { view.clickedCountry(country); }

    // TODO: for draw arrow purpose
//    public void pressed() { view.pressedCountry(country); }
//    public void entered() { view.enteredCountry(country); }
//    public void released() { view.releasedCountry(country); }


    /**
     * Get the country id as key
     * Called by View when store CountryView into CountryViews HashMap
     * @return countryView Id
     */
    public int getId() { return Id; }


    /**
     * Used during fortification phase, View then get the relative country info and do the verification
     * Called by View.drawMap()
     * @return Country reference
     */
    public Country getCountry() { return country; }
}