package mapeditor;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ViewController {

    private static enum VALIDITY {OK,CONTINENT_NOT_SET,ISOLATED_COUNTRY};

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
        if (View.continents.size()>1) {
            View.continents.remove(View.continents.size() - 1);
            if (View.continents.size()==1) btnReduce.setDisable(true);
        }

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
        p1.addAdjCountry(p2);
        p1.addEdge(line);
        p2.addAdjCountry(p1);
        p2.addEdge(line);
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
                    Country p1=line.getP1();
                    Country p2=line.getP2();

                    //disconnect in data structure
                    p1.removeAdjCountry(p2);
                    p2.removeAdjCountry(p1);
                    p1.removeEdge(line);
                    p2.removeEdge(line);

                    draw_pane.getChildren().remove(line);
                    System.out.println("Country "+p1.getCountryId()+" and Country "+p2.getCountryId()+" are disconnected");
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

        ArrayList<Country> countryList=buildCountryList();
        if (countryList.isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save to file failed");
            alert.setHeaderText("Oops, you haven't created any country yet");
            alert.setContentText("Please create some countries and connect them by drag-drop a line");
            alert.show();
            return;
        }

        VALIDITY validity=validateCountry(countryList);
        if (validity==VALIDITY.CONTINENT_NOT_SET){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save to File");
            alert.setHeaderText("Save to File failed");
            alert.setContentText("Some country(s) doesn't belong to any continentPlease select continent for every countries before save.");
            alert.show();
            return;
        } else if (validity==VALIDITY.ISOLATED_COUNTRY){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save to File");
            alert.setHeaderText("Save to file failed");
            alert.setContentText("Some country(s) is isolated. Please create at lease one connection for those isolated country(s).");
            alert.show();
            return;
        }

        //basic country check passed

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map to File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Risk Map Files", "*.map"));
        File file = fileChooser.showSaveDialog(view_pane.getScene().getWindow());

        if (file != null) {
            try {
                Writer writer=new Writer(countryList,file.getPath());
                if (writer.write()){
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Save to File");
                    alert.setHeaderText("Save to file successfully");
                    alert.show();
                }else{
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Save to File");
                    alert.setHeaderText("Save to file failed");
                    alert.setContentText("Oops, your map configuration is invalid. Please adjust your map configuration.");
                    alert.show();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    private ArrayList<Country> buildCountryList(){
        ArrayList<Country> countryList=new ArrayList<>();
        for (Node node:draw_pane.getChildren()){
            if (node instanceof Country){
                Country country=(Country) node;
                countryList.add(country);
            }
        }
        return countryList;
    }

    private VALIDITY validateCountry(ArrayList<Country> countryList){
        for (Country country:countryList){
            if (country.getEdgeList().isEmpty())
                return VALIDITY.ISOLATED_COUNTRY;
            ChoiceBox cb=(ChoiceBox) country.lookup("#listContinent");
            if (cb.getSelectionModel().isEmpty())
                return VALIDITY.CONTINENT_NOT_SET;
        }
        return VALIDITY.OK;
    }

}
