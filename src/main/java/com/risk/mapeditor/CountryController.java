package com.risk.mapeditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

/**
 * Controller class for Country class in map editor
 * Bounded with CountryEditor.fxml
 */
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

    /**
     * Initialization
     * Fill continents list, add remove button listener, add choice box listenr
     * @param country Country object to be initialized
     */
    @FXML
    public void initialize(Country country){
        lblCountry.setText(country.getName());

        listContinent.setItems(View.continents);
        listContinent.getSelectionModel().select(country.getContinent());

        //avoid create new countries over existing country
        country.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });

        // remove button listener
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

        //Choice box listener
        listContinent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                country.setContinent(newValue);
                System.out.println(newValue);
            }
        });


    }

}
