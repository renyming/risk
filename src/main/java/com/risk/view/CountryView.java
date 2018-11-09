package com.risk.view;

import com.risk.controller.CountryController;
import com.risk.controller.MapController;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import java.util.Observable;
import java.util.Observer;
import com.risk.model.Country;


/**
 * Responsible for visualizing the Country object, show useful info to user
 * Corresponding Observable subject is Country
 */
public class CountryView implements Observer {

    private CountryController countryController;
    private AnchorPane countryPane;
    private Country country;


    /**
     * Create a default CountryView, add View reference, load the countryPane from Country.xml
     * @param mapController is the MapController
     */
    public CountryView(MapController mapController) {
        FXMLLoader countryFxmlLoader = new FXMLLoader(getClass().getResource("Country.fxml"));
        try {
            countryPane = countryFxmlLoader.load();
        } catch (Exception e) {
            System.out.println("CountryView.ctor(): " + e);
        }
        countryController = countryFxmlLoader.getController();
        countryController.init(this, mapController);
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
        countryController.updateCountryPaneInfo(country);
    }


    /**
     * View use this function to add/remove the countryPane to the mapRootPane
     * or clear the countryPane's component
     * Called by View.*()
     * @return the countryPane reference
     */
    public AnchorPane getCountryPane() { return countryPane; }


    /**
     * Used during fortification phase, View then get the relative country info and do the verification
     * Called by View.drawMap()
     * @return Country reference
     */
    public Country getCountry() { return country; }
}
