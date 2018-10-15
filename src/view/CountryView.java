package view;

import javafx.scene.layout.AnchorPane;

import java.util.Observable;
import java.util.Observer;

class CountryView extends AnchorPane implements Observer {

    private static int idCounter = 1;
    private int countryViewId;

//    private Country country; // TODO: the relative Observable subject
    private String name;

    public CountryView() {
        countryViewId = idCounter++;
        this.name = "Country_" + countryViewId;
        setStyle("-fx-background-color: red");
    }

    public void update(Observable obj, Object x) {
        // TODO: use it to update info, position, # of armies
    }



    public int getCountryViewId() { return countryViewId; }
}