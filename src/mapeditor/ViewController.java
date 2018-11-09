package mapeditor;

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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    private View view;

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

        draw_pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (event.getClickCount() == 2) {
                        Country country = new Country(event.getSceneX(), event.getSceneY());
                        drawCountry(country);
                        event.consume();
                    }
                }
            }
        });

        //List view listener
        lstContinent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                if (!lstContinent.getSelectionModel().isEmpty() && lstContinent.getItems().size() > 1)
                    btnDelContinent.setDisable(false);
            }
        });
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
     */
    public void drawCountry(Country country) {
        setCountryListener(country);
        draw_pane.getChildren().add(country);
        country.relocateToPoint();
    }

    /**
     * Set on event listeners for Country object
     * @param country Country object
     */
    private void setCountryListener(Country country) {
        addDragDetection(country);
        addDragOver(country);
        addDragDropped(country);
    }

    /**
     * Set on event listener when drag action starts
     * @param country Country object
     */
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

    /**
     * Set on event listener when drag action drops
     * @param country Country object
     */
    private void addDragDropped(Country country) {
        country.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                Country source = (Country) event.getGestureSource();

                if (source != country) {
                    System.out.println(source.getName() + " and " + country.getName() + " are connected");
                    drawLine(source, country);
                }

                event.consume();

            }
        });
    }

    /**
     * Set on event listener when drag over
     * Set transfer mode of receiver to accept drag
     * @param country Country object
     */
    private void addDragOver(Country country) {
        country.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
    }

    /**
     * Draw line between two counties and set linkage in each adjacent list, add edge to edge list
     * @param p1 Country 1 to be connected
     * @param p2 Country 2 to be connected
     */
    public void drawLine(Country p1, Country p2) {
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
        }


        //basic country check passed

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map to File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Risk Map Files", "*.map"));
        File file = fileChooser.showSaveDialog(view_pane.getScene().getWindow());

        if (file != null) {
            try {
                Writer writer = new Writer(countryList, file.getPath());
                if (writer.write()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Save to File");
                    alert.setHeaderText("Save to file successfully");
                    alert.show();
                    return;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Save to File");
                    alert.setHeaderText("Save to file failed");
                    alert.setContentText(writer.invalidReason);
                    alert.show();
                    return;
                }
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Save to File");
                alert.setHeaderText("Save to file failed");
                alert.setContentText("IO Error: \n" + ex.getMessage());
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
            if (country.getEdgeList().isEmpty())
                return VALIDITY.ISOLATED_COUNTRY;
            ChoiceBox cb = (ChoiceBox) country.lookup("#listContinent");
            if (cb.getSelectionModel().isEmpty())
                return VALIDITY.CONTINENT_NOT_SET;
        }
        return VALIDITY.OK;
    }

    private static enum VALIDITY {OK, CONTINENT_NOT_SET, ISOLATED_COUNTRY}

}
