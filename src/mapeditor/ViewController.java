package mapeditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ViewController {

    View view;
    private static enum VALIDITY {OK,CONTINENT_NOT_SET,ISOLATED_COUNTRY};

    @FXML
    AnchorPane view_pane;
    @FXML
    AnchorPane draw_pane;
    @FXML
    ListView lstContinent;
    @FXML
    TextField txtContinent;
    @FXML
    Button btnDelContinent;

    @FXML
    public void initialize(View view) {
        this.view=view;
        btnDelContinent.setDisable(true);


        lstContinent.setItems(View.continents);
        lstContinent.getSelectionModel().selectFirst();

        draw_pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton()== MouseButton.PRIMARY){
                    Country country=new Country(event.getSceneX(),event.getSceneY());
                    drawCountry(country);
                    event.consume();
                }
            }
        });

        //List view listener
        lstContinent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                if (!lstContinent.getSelectionModel().isEmpty() && lstContinent.getItems().size()>1)
                    btnDelContinent.setDisable(false);
            }
        });


    }

    public void delContinent() {
        if (lstContinent.getItems().size()<=1){
            Alert alert=new Alert(Alert.AlertType.WARNING,"Must have at least one continent");
            alert.show();
            return;
        }
        View.continents.remove(lstContinent.getSelectionModel().getSelectedItem());
        if (lstContinent.getItems().size()<=1){
            btnDelContinent.setDisable(true);
        }
    }

    public void addNewContinent(){
        if (txtContinent.getText().equals("")){
            Alert alert=new Alert(Alert.AlertType.WARNING,"Name of new continent cannot be empty");
            alert.show();
            return;
        }

        if (lstContinent.getItems().contains(txtContinent.getText())){
            Alert alert=new Alert(Alert.AlertType.WARNING,"Name of continent \""+txtContinent.getText()+" \" already exists.");
            alert.show();
            return;
        }

        View.continents.add(txtContinent.getText());
    }


    public void drawCountry(Country country){
//        country.relocate(event.getSceneX(),event.getSceneY());
        setCountryListener(country);
        draw_pane.getChildren().add(country);
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

    public void drawLine(Country p1, Country p2){
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

    public void openFile(){
        view.openMap();
    }

    public void save(){

        ArrayList<Country> countryList=buildCountryList();
        if (countryList.isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save to File");
            alert.setHeaderText("Save to file failed");
            alert.setContentText("Oops, you haven't created any country yet. Please create some countries and connect them by drag-drop a line");
            alert.show();
            return;
        }

        VALIDITY validity=validateCountry(countryList);
        if (validity==VALIDITY.CONTINENT_NOT_SET){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save to File");
            alert.setHeaderText("Save to file failed");
            alert.setContentText("Some country(s) doesn't belong to any continent. Please select continent for every countries before save.");
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
                    return;
                }else{
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Save to File");
                    alert.setHeaderText("Save to file failed");
                    alert.setContentText("Oops, your map configuration is invalid. Please adjust your map configuration.");
                    alert.show();
                    return;
                }
            } catch (IOException ex) {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Save to File");
                alert.setHeaderText("Save to file failed");
                alert.setContentText("IO Error: \n"+ex.getMessage());
                alert.show();
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
