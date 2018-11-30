package com.risk.mapeditor;

import com.risk.exception.InvalidMapException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller class for View object
 */
public class ViewController {

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
    Button btnAddContinent;
    @FXML
    Slider sldOpacity;

    private View view;
    private ArrayList<Country> countryList;
    private boolean dragActive=false;
    private static Line currentLine;
    private DoubleProperty mouseX=new SimpleDoubleProperty();
    private DoubleProperty mouseY=new SimpleDoubleProperty();

    /**
     * Click to draw new country
     */
    private EventHandler drawPaneClicked=new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.isStillSincePress()){
                Country country = new Country(event.getSceneX(), event.getSceneY());
                drawCountry(country, true);
            }
        }
    };

    /**
     * Initialize the base pane
     * Fills continent list, set mouse listener to add new country, set listener for continent list view
     * @param view View object to be initialized
     */
    @FXML
    public void initialize(View view) {
        Image icon_down=new Image(getClass().getClassLoader().getResourceAsStream("down-icon.png"));
        btnAddContinent.setGraphic(new ImageView(icon_down));

        Image icon_delete=new Image(getClass().getClassLoader().getResourceAsStream("delete-icon.png"));
        btnDelContinent.setGraphic(new ImageView(icon_delete));

        this.view = view;
        btnDelContinent.setDisable(true);

        lstContinent.setItems(View.continents);
        lstContinent.getSelectionModel().selectFirst();

        draw_pane.setOnMouseClicked(drawPaneClicked);
        countryList=new ArrayList<>();
        attachDrawPaneListener(draw_pane);
        currentLine=new Line();
        currentLine.getStrokeDashArray().add(2d);
        currentLine.setVisible(false);
        draw_pane.getChildren().add(currentLine);

        //List view listener
        lstContinent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                if (!lstContinent.getSelectionModel().isEmpty() && lstContinent.getItems().size() > 1)
                    btnDelContinent.setDisable(false);
            }
        });

        //attach opacity slider
        sldOpacity.valueProperty().addListener((observable, oldValue, newValue) -> {
            countryList.stream().forEach(c->c.setOpacity((double) newValue));
        });

    }

    /**
     * Find the country object with the scene coordinates
     * @param x Scene X
     * @param y Scene Y
     * @return Country that contains the scene coordinates
     */
    private Optional<Country> findCountry(double x, double y){
        return countryList.stream().filter(c->c.boundsInLocalProperty().get().contains(c.sceneToLocal(x,y))).findAny();
    }

    /**
     * Drag a line starts
     * @param country Country initiates the drag action
     */
    private void startDrag(Country country){
//        System.out.println("StartDrage: "+country.getName());
        if (dragActive)
            return;

        dragActive=true;

        currentLine.setUserData(country);
        Point2D p=currentLine.getParent().sceneToLocal(country.getX(),country.getY());
        currentLine.setStartX(p.getX()+country.getWidth()/2);
        currentLine.setStartY(p.getY()+country.getHeight()/2);
        currentLine.setVisible(true);
        currentLine.endXProperty().bind(mouseX);
        currentLine.endYProperty().bind(mouseY);

    }

    /**
     * Drag a line stops
     * @param country Country where the drag action finishes
     */
    private void stopDrag(Country country) {
        dragActive=false;

        if (currentLine.getUserData()!=country){
//            System.out.println("different country");
            //different countries
            currentLine.endYProperty().unbind();
            currentLine.endXProperty().unbind();
            currentLine.setEndX(country.getCenterX());
            currentLine.setEndY(country.getCenterY());
            currentLine.setVisible(false);
            drawLine((Country) currentLine.getUserData(),country);
        } else {
            //same country
//            System.out.println("same country");
            stopDrag();
        }
    }

    /**
     * Drag a line stops outside of a country
     */
    private void stopDrag() {
        dragActive=false;

        currentLine.endXProperty().unbind();
        currentLine.endYProperty().unbind();
        currentLine.setVisible(false);

    }

    /**
     * Handler to delete a continent in continent list view
     * Bounded to: btnDelContinent
     */
    public void delContinent() {
        if (lstContinent.getItems().size() <= 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Must have at least one continent");
            alert.show();
            return;
        }
        View.continents.remove(lstContinent.getSelectionModel().getSelectedItem());
        if (lstContinent.getItems().size() <= 1) {
            btnDelContinent.setDisable(true);
        }
    }

    /**
     * Remove a country from the list keeping country positions when deleted
     * @param country Country to be deleted
     */
    public void removeFromCountryList(Country country) {
        countryList.remove(country);
    }

    /**
     * Handler to add a continent from text box to list view
     * Bounded to: btnAddContinent
     */
    public void addContinent() {
        if (txtContinent.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Name of new continent cannot be empty");
            alert.show();
            return;
        }

        if (lstContinent.getItems().contains(txtContinent.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Name of continent \"" + txtContinent.getText() + " \" already exists.");
            alert.show();
            return;
        }

        View.continents.add(txtContinent.getText());
    }

    /**
     * Draw a country on canvas
     * A wrapper method
     * @param country Country object
     * @param isUserClick If this operation is to deal with a country adding by user click
     */
    public void drawCountry(Country country, boolean isUserClick) {
        setCountryListener(country);
        draw_pane.getChildren().add(country);
        country.relocateToPoint(isUserClick);
        countryList.add(country);
        country.setOpacity(sldOpacity.getValue());
    }

    /**
     * Set on event listeners for Country object
     * @param country Country object
     */
    private void setCountryListener(Country country) {
        addMousePressed(country);
    }

    /**
     * Set on event listeners for draw_pane object
     * @param draw_pane Draw_pane object
     */
    private void attachDrawPaneListener(AnchorPane draw_pane){
        addMouseDragged(draw_pane);
        addMouseMoved(draw_pane);
        addMouseReleased(draw_pane);
    }

    /**
     * Set on event listener when drag action starts
     * @param country Country object
     */
    private void addMousePressed(Country country) {
        country.setOnMousePressed(event -> {
//            System.out.println("Country pressed");
//            findCountry(event.getSceneX(),event.getSceneY()).ifPresent(this::startDrag);
            startDrag(country);
            event.consume();
        });
    }

    /**
     * Set on event listener when mouse moves over the draw_pane
     * @param draw_pane Draw pane object
     */
    private void addMouseMoved(AnchorPane draw_pane) {
        draw_pane.setOnMouseMoved(event -> {
//            System.out.println("Draw pane moved");
            Point2D p=draw_pane.sceneToLocal(event.getSceneX(),event.getSceneY());
            mouseX.set(p.getX());
            mouseY.set(p.getY());
            event.consume();
        });
    }

    /**
     * Set on event listener when mouse drags over the draw_pane
     * @param draw_pane Draw pane object
     */
    private void addMouseDragged(AnchorPane draw_pane) {
        draw_pane.setOnMouseDragged(event -> {
//            System.out.println("Draw pane dragged");
            Point2D p=draw_pane.sceneToLocal(event.getSceneX(),event.getSceneY());
            mouseX.set(p.getX());
            mouseY.set(p.getY());
            event.consume();
        });
    }

    /**
     * Set on event listener when mouse released on the draw_pane
     * @param draw_pane Draw pane object
     */
    private void addMouseReleased(AnchorPane draw_pane) {
        draw_pane.setOnMouseReleased(event -> {
//            System.out.println("Country released");
            if (!dragActive) return;
            Optional<Country> country_tmp=findCountry(event.getSceneX(),event.getSceneY());
            if (country_tmp.isPresent()){
                stopDrag(country_tmp.get());
            }else{
                stopDrag();
            }
            event.consume();
        });
    }

    /**
     * Draw line between two counties and set linkage in each adjacent list, add edge to edge list
     * @param p1 Country 1 to be connected
     * @param p2 Country 2 to be connected
     */
    public void drawLine(Country p1, Country p2) {
        if(p1==null || p2==null) {
            System.out.println("null");
        }

        if (p1.isAdjacent(p2)) return;

        Edge line = new Edge(p1, p2);
        p1.addAdjCountry(p2);
        p1.addEdge(line);
        p2.addAdjCountry(p1);
        p2.addEdge(line);
        setLineListener(line);
        draw_pane.getChildren().add(line);
        line.toBack();
    }

    /**
     * Set on event listeners for lines
     * @param line Line object
     */
    private void setLineListener(Edge line) {
        addMouseOver(line);
        addMouseExit(line);
        addDelLine(line);
    }

    /**
     * Event listener when mouse moves over a line
     * Highlight current line
     * @param line Line object
     */
    private void addMouseOver(Edge line) {
        line.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                line.setStroke(Edge.deleteColor);
                line.setStrokeWidth(Edge.deleteWidth);
                event.consume();
            }
        });
    }

    /**
     * Event listener when mouse right clicks
     * Delete current line
     * @param line Line object
     */
    private void addDelLine(Edge line) {
        line.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    Country p1 = line.getP1();
                    Country p2 = line.getP2();

                    //disconnect in data structure
                    p1.removeAdjCountry(p2);
                    p2.removeAdjCountry(p1);
                    p1.removeEdge(line);
                    p2.removeEdge(line);

                    draw_pane.getChildren().remove(line);
                    System.out.println(p1.getName() + " and " + p2.getName() + " are disconnected");
                }

                event.consume();
            }
        });
    }

    /**
     * Set line back to normal style when mouse moves away
     * @param line Line object
     */
    private void addMouseExit(Edge line) {
        line.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                line.setStroke(Edge.normalColor);
                line.setStrokeWidth(Edge.normalWidth);
                event.consume();
            }
        });
    }

    /**
     * Clear all nodes on draw_pane
     */
    public void clearCanvas() {
        draw_pane.getChildren().clear();
        initialize(view);
    }

    /**
     * Handler for btnOpen
     */
    public void openFile() {
        view.openMap();
    }

    /**
     * Handler for btnExit
     */
    public void exit() {
        view.exit();
    }

    /**
     * Save current map to file
     *
     * Tests:
     * 1. Isolated country;
     * 2. Continent not set for any country;
     * 3. Empty map
     *
     * Then:
     * 4. Open file chooser, passes file to Writer class to further validate map configuration, outputs to file if passed
     */
    public void save() {

        ArrayList<Country> countryList = buildCountryList();
        if (countryList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save to File");
            alert.setHeaderText("Save to file failed");
            alert.setContentText("Oops, you haven't created any country yet. Please create some countries and connect them by drag-drop a line");
            alert.show();
            return;
        }

        VALIDITY validity = validateCountry(countryList);
        if (validity == VALIDITY.CONTINENT_NOT_SET) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save to File");
            alert.setHeaderText("Save to file failed");
            alert.setContentText("Some country(s) doesn't belong to any continent. Please select continent for every countries before save.");
            alert.show();
            return;
        } else if (validity == VALIDITY.ISOLATED_COUNTRY) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save to File");
            alert.setHeaderText("Save to file failed");
            alert.setContentText("Some country(s) is isolated. Please create at lease one connection for those isolated country(s).");
            alert.show();
            return;
        } else if (validity == VALIDITY.COUNTRY_NO_NAME) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save to File");
            alert.setHeaderText("Save to file failed");
            alert.setContentText("Some country(s) has no name. Please name every country before saving.");
            alert.show();
            return;
        }


        //basic country check passed

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map to File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Risk Map Files", "*.map"));
        File file = fileChooser.showSaveDialog(view_pane.getScene().getWindow());

        if (file != null) {

                Writer writer = new Writer(countryList, file.toPath());
                try {
                    writer.write();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Save to File");
                    alert.setHeaderText("Save to file successfully");
                    alert.show();
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Save to File");
                    alert.setHeaderText("Save to file failed");
                    alert.setContentText("IO Error: \n" + ex.getMessage());
                    alert.show();
                } catch (InvalidMapException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Save to File");
                    alert.setHeaderText("Save to file failed");
                    alert.setContentText("Invalid map layout: \n" + ex.getMessage());
                    alert.show();
                }

        }

    }

    /**
     * Iterates and build a list for all the countries currently on canvas
     * @return Country objects in ArrayList
     */
    private ArrayList<Country> buildCountryList() {
        ArrayList<Country> countryList = new ArrayList<>();
        for (Node node : draw_pane.getChildren()) {
            if (node instanceof Country) {
                Country country = (Country) node;
                countryList.add(country);
            }
        }
        return countryList;
    }

    /**
     * Validates if a map has isolates country or any country that has no continent selected
     * @param countryList List of Country objects
     * @return ViewController VALIDITY enum: OK, CONTINENT_NOT_SET, ISOLATED_COUNTRY
     */
    private VALIDITY validateCountry(ArrayList<Country> countryList) {
        for (Country country : countryList) {
            if (country.getName().trim().isEmpty())
                return VALIDITY.COUNTRY_NO_NAME;
            if (country.getEdgeList().isEmpty())
                return VALIDITY.ISOLATED_COUNTRY;
            ChoiceBox cb = (ChoiceBox) country.lookup("#listContinent");
            if (cb.getSelectionModel().isEmpty())
                return VALIDITY.CONTINENT_NOT_SET;
        }
        return VALIDITY.OK;
    }

    private static enum VALIDITY {OK, CONTINENT_NOT_SET, ISOLATED_COUNTRY, COUNTRY_NO_NAME}

}
