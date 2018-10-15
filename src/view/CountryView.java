package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import model.Country;
import model.Player;

import java.util.Observable;
import java.util.Observer;

class CountryView implements Observer {

    private static int IdCounter = 1;

    private int Id;
    private int arimes;
    private String name;
    private Player owner;
    private Country country;
    private String ownerColor;
    private String continentColor;

    private View view;
    private AnchorPane countryPane;
    private CountryController countryController;


    /**
     * Get necessary info for create a default countryPane, load the countryPane from Country.xml
     * @param layoutX The x location of the countryPane relative to the mapRootPane
     * @param layoutY The y location of the countryPane relative to the mapRootPane
     */
    public CountryView(View view, double layoutX, double layoutY, String continentColor) {
        Id = IdCounter++;
        this.view = view;
        this.continentColor = continentColor; // TODO: may not need
        try {
            FXMLLoader countryFxmlLoader = new FXMLLoader(getClass().getResource("Country.fxml"));
            countryPane = countryFxmlLoader.load();
            countryController = countryFxmlLoader.getController();
            countryController.setCountryView(this);
            countryPane.setLayoutX(layoutX);
            countryPane.setLayoutY(layoutY);
            // TODO: get user/continent color
            // TODO: set proper test color
        } catch (Exception e) {
            System.out.println("CountryView ctor: " + e);
        }
    }

    /**
     * Observer update method
     * Called by Country object
     * @param obj The relative Country Observable object
     * @param x Additional info for update
     */
    public void update(Observable obj, Object x) {
        System.out.println("CountryView Observer updates");
        updateCountryInfo();
    }

    /**
     * Set the Country Observable object
     * Called by View
     * @param country
     */
    public void setCountry(Country country) {
        this.country = country;
        updateCountryInfo();
    }

    /**
     * Update additional Country object info
     * Called by View
     */
    public void updateCountryInfo() {
        Id = country.getID(); // TODO: should be .getId()
        arimes = country.getArmies();
        name = country.getName();
        owner = country.getOwner();
        ownerColor = "-fx-background-color: " + owner.getColor();
        countryPane.setStyle(ownerColor);
        // TODO: set owner, new owner color
    }

    /**
     * View use this function to add the countryPane to the mapRootPane
     * Called by View
     * @return the countryPane object
     */
    public AnchorPane getCountryPane() { return countryPane; }

    public void removeCountryView() {
        countryPane.getChildren().clear();
        view.removeCountryView(this);
    }

    /**
     * Get the country id as key
     * Called by View when store CountryView into CountryViews HashMap
     * @return countryView Id
     */
    public int getId() { return Id; }
}