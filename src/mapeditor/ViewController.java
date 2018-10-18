package mapeditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ViewController {

    @FXML
    AnchorPane view_pane;
    @FXML
    Button btnAdd;
    @FXML
    AnchorPane draw_pane;
    @FXML
    Button btnReduce;
    @FXML
    Label lblNContinents;

    @FXML
    public void initialize() {

        btnReduce.setDisable(true);

        draw_pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton()== MouseButton.PRIMARY){
                    Country country=new Country(event.getSceneX(),event.getSceneY());
                    country.relocate(event.getSceneX(),event.getSceneY());
                    setCountryListener(country);
                    draw_pane.getChildren().add(country);
                    event.consume();
                }
            }
        });

    }

    public void addContinent(){
        btnReduce.setDisable(false);
        View.continents.add("Continent "+(View.continents.size()+1));
        lblNContinents.setText(Integer.toString(View.continents.size()));
    }

    public void reduceContinent(){
        if (View.continents.size()>0)
            View.continents.remove(View.continents.size()-1);
        else
            btnReduce.setDisable(true);
        lblNContinents.setText(Integer.toString(View.continents.size()));
    }

    private void setCountryListener(Country country){
        addDragDetection(country);
        addDragOver(country);
        addDragDropped(country);
    }

    private void addDragDetection(Country country) {
        country.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ClipboardContent content = new ClipboardContent();
                content.putString("");
                country.startDragAndDrop(TransferMode.ANY).setContent(content);
                event.consume();
            }
        });
    }

    private void addDragDropped(Country country) {
        country.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                Country source=(Country) event.getGestureSource();

                if (source!=country){
                    System.out.println("Country "+source.getCountryId()+" and Country "+country.getCountryId()+" are connected");
                    drawLine(source,country);
                }

                event.consume();

            }
        });
    }

    private void addDragOver(Country country) {
        country.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
    }

    private void drawLine(Country p1, Country p2){
        Edge line=new Edge(p1,p2);
        setLineListener(line);

        draw_pane.getChildren().add(line);
    }

    private void setLineListener(Edge line){
        addMouseOver(line);
        addMouseExit(line);
        addDelLine(line);
    }

    private void addMouseOver(Edge line){
        line.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                line.setStroke(Edge.deleteColor);
                line.setStrokeWidth(Edge.deleteWidth);
                event.consume();
            }
        });
    }

    private void addDelLine(Edge line){
        line.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton()== MouseButton.SECONDARY){
                    draw_pane.getChildren().remove(line);
                    System.out.println("Country "+line.getP1().getCountryId()+" and Country "+line.getP2().getCountryId()+" are disconnected");
                }

                event.consume();
            }
        });
    }

    private void addMouseExit(Edge line){
        line.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                line.setStroke(Edge.normalColor);
                line.setStrokeWidth(Edge.normalWidth);
                event.consume();
            }
        });
    }

    public void save(){

    }

}
