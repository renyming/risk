package view;

public class CountryController {

    private CountryView countryView;

    public void setCountryView(CountryView countryView) { this.countryView = countryView; }

    public void removeCountryView() { countryView.removeCountryView(); }
}
