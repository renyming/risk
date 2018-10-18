package mapeditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

public class CountryController {

    @FXML
    AnchorPane country_pane;
    @FXML
    Label lblCountry;
    @FXML
    ChoiceBox listContinent;
    @FXML
    AnchorPane left_pane;
    @FXML
    AnchorPane right_pane;

    @FXML
    public void initialize(Country country){
        lblCountry.setText(country.getName());

        listContinent.setItems(View.continents);
        listContinent.setValue(View.continents.get(0));

        //avoid create new countries over existing country
        country.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                event.consume();
            }
        });


//        country.setEventDispatcher(new EventDispatcher() {
//            @Override
//            public Event dispatchEvent(Event event, EventDispatchChain tail) {
////                boolean valid = myValidationLogicForEvents(event);
////                return valid ? tail.dispatchEvent(event) : null;
//                return null;
//            }
//        });

    }

}
