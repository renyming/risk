package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import model.Country;
import model.Player;

import java.util.Observable;
import java.util.Observer;

public class CountryView implements Observer {

    private static int IdCounter = 0;

    private int Id;
    private int arimes;        // used for additional display
    private String name;       // used for additional display
    private Player owner;
    private String ownerColor = "red";
    private String continentColor = "blue";

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
            countryController.setDefaultInfo(this, "Country_"+Id, 0, ownerColor, continentColor);
            countryPane.setLayoutX(layoutX);
            countryPane.setLayoutY(layoutY);
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
        System.out.println("CountryView.update(): ");
        if (null == country) country = (Country) obj;
        Id = country.getId();
        name = country.getName();
        arimes = country.getArmies();
        System.out.println(arimes);
        owner = country.getOwner();
//        ownerColor = owner.getColor(); // TODO:
        countryPane.setLayoutX(country.getX());
        countryPane.setLayoutY(country.getY());
        countryController.updateCountryPaneInfo(name, ownerColor, continentColor, arimes);
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