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
//    private Country country; // TODO: may not need
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
    public CountryView(View view, double layoutX, double layoutY) {
        Id = IdCounter++;
        this.view = view;
        this.ownerColor = "#ff0000";
        this.continentColor = "#0000ff";
        try {
            FXMLLoader countryFxmlLoader = new FXMLLoader(getClass().getResource("Country.fxml"));
            countryPane = countryFxmlLoader.load();
            countryController = countryFxmlLoader.getController();
            countryController.setCountryView(this);
            countryPane.setLayoutX(layoutX);
            countryPane.setLayoutY(layoutY);
        } catch (Exception e) {
            System.out.println("CountryView ctor: " + e);
        }
    }

    /**
     * Observer update method, get all Country info, store and set it to panel
     * Called by Country object
     * @param obj The relative Country Observable object
     * @param x Additional info for update
     */
    public void update(Observable obj, Object x) {
        System.out.println("CountryView.update(): ");

        updateCountryInfo((Country) obj);
    }

    /**
     * Update additional Country object info
     * Called by View
     */
    public void updateCountryInfo(Country country) {
        Id = country.getId();
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