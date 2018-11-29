package com.risk.mapeditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Controller class for Country class in map editor
 * Bounded with CountryEditor.fxml
 */
public class CountryController {

    private static final String HIGHLIGHTCOLOR="#f2e94e";
    private static final String NORMALCOLOR="#171a1e";
    private Rectangle rectangle=new Rectangle(120,80);

    @FXML
    AnchorPane country_pane;
    @FXML
    TextField lblCountry;
    @FXML
    ChoiceBox listContinent;
    @FXML
    AnchorPane left_pane;
    @FXML
    AnchorPane right_pane;
    @FXML
    Label btnRemove;

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
        btnRemove.setOnMouseClicked(event ->  {
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
        });

        btnRemove.setOnMouseEntered(event -> {
            btnRemove.setTextFill(Color.web(HIGHLIGHTCOLOR));
        });

        btnRemove.setOnMouseExited(event -> {

            btnRemove.setTextFill(Color.web(NORMALCOLOR));
        });

        //Choice box listener
        listContinent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                country.setContinent(newValue);
                System.out.println(newValue);
            }
        });

        //Country title listener
        lblCountry.setOnMouseEntered(event -> {
            lblCountry.setStyle("-fx-background-color: transparent; -fx-text-fill: "+HIGHLIGHTCOLOR+"; -fx-highlight-fill: #72bce4");
        });

        lblCountry.setOnMouseExited(event -> {
            lblCountry.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff");
            country.setName(lblCountry.getText());
        });

        rectangle.setArcHeight(15);
        rectangle.setArcWidth(12);

        country_pane.setClip(rectangle);

    }

}
