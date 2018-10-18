package mapeditor;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import javax.swing.*;
import java.util.ArrayList;

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
    Button btnRemove;

    @FXML
    public void initialize(Country country){
        lblCountry.setText(country.getName());

        listContinent.setItems(View.continents);
        listContinent.getSelectionModel().selectFirst();

        //avoid create new countries over existing country
        country.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });

        // remove button
        btnRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Edge> edgeList=new ArrayList<>(country.getEdgeList());
                for (Edge line:edgeList){
                    Country p1=line.getP1();
                    Country p2=line.getP2();
                    p1.removeAdjCountry(p2);
                    p2.removeAdjCountry(p1);
                    p1.removeEdge(line);
                    p2.removeEdge(line);
                    AnchorPane draw_pane=(AnchorPane) line.getParent();
                    draw_pane.getChildren().remove(line);
                }
                AnchorPane draw_pane=(AnchorPane) country.getParent();
                draw_pane.getChildren().remove(country);
                event.consume();
            }
        });


    }

}
