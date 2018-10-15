package view;

import javafx.scene.layout.AnchorPane;
import model.Country;
import model.Player;

import java.util.Observable;
import java.util.Observer;

class CountryView extends AnchorPane implements Observer {

    private static int idCounter = 1;
    private int countryViewId;
    private Country country; // TODO: the relative Observable subject, may not need
    private String countryViewName;
    private Player owner;

    public CountryView() {
        countryViewId = idCounter++;
        this.countryViewName = "Country_" + countryViewId;
        setStyle("-fx-background-color: red");
    }

    public void update(Observable obj, Object x) {
        // TODO: use it to update info, position, # of armies, owner,
    }

    public void addCountry(Country country) {
        this.country = country;
        // TODO: extract additional Country object info, add them into CountryView object
    }

    public int getCountryViewId() { return countryViewId; }
}