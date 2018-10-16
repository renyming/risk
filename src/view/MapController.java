package view;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MapController {

    public View view;
    public AnchorPane mapPane;
    public Button saveEditedMapButton;
    public Button backToMenuButton;
    public double countryViewWidth;
    public double countryViewHeight;

    public void initialize(View view, double newCountryViewWidth, double newCountryViewHeight) {
        this.view = view;
        this.countryViewWidth = newCountryViewWidth;
        this.countryViewHeight = newCountryViewHeight;
        mapPane.setOnMouseClicked((e) -> {
            if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                //TODO: get the continent color by the map continent framework
                createCountry(e.getX() - countryViewWidth/2, e.getY() - countryViewHeight/2);
            }
        });
    }



    /**
     *
     * Called by the mapPane when user clicks it // TODO my changed later if map canvas is added
     * @param cursorX Cursor X position
     * @param cursorY Cursor Y position
     */
    public void createCountry(double cursorX, double cursorY) { view.createCountryView(cursorX, cursorY); }

    public void backToMenu() { view.showMenuStage(); }
}
