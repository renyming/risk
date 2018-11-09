package mapeditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import model.Continent;
import model.Model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Model class for View (root_pane) in map editor
 * In order to unify model.Model notify mechanism, implements Observer to receive file validation info
 */
public class View extends AnchorPane {

    private ViewController viewController;
    private view.View menuView;
    public static ObservableList<String> continents= FXCollections.observableArrayList();

    /**
     * Constructor
     * Loads View.fxml file for layout, initialize View object, and global continents list
     * @throws IOException Exception for IO error
     */
    public View() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("View.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
        viewController = fxmlLoader.getController();
        viewController.initialize(this);
        continents.clear();
        continents.add("Default Continent");
    }

    /**
     * Setter to obtain view.View reference in order to exit back to main menu
     * @param view View reference from main menu
     */
    public void setMenuView(view.View view){
        menuView=view;
    }

    /**
     * Exit map editor, show main menu
     */
    public void exit(){
        menuView.quitToMenu();
    }

    /**
     * Open an existing map
     * Add current View object as observer to model.Model, to receive file validation info
     */
    public void openMap() {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Open a new file will discard all the unsaved changes, are you sure to continue?", ButtonType.NO,ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult()==ButtonType.NO)
            return;

        Model model=new Model();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a Map File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Risk Map Files", "*.map"));
        File file = fileChooser.showOpenDialog(getScene().getWindow());

        if (file==null) return;

        try{
            model.editorReadFile(file.toString());
        }catch(IOException e) {
            Alert err=new Alert(Alert.AlertType.ERROR,"File IO error: \n"+e.getMessage());
            err.show();
            return;
        }

        if (model.isValidFile())
            drawMap(model);
        else{
            Alert err=new Alert(Alert.AlertType.ERROR,"Invalid map format");
            err.show();
            return;
        }
    }


    /**
     * Draw the map from model after map file has been loaded
     * @param model Model reference
     */
    public void drawMap(Model model){

        //Clear draw_pane
        viewController.clearCanvas();

        HashMap<String, model.Country> countryList=model.getCountries();
        ArrayList<Continent> continentList=model.getContinents();

        //add continents
        continents.clear();
        for (Continent continent:continentList){

            continents.add(continent.getName());
        }

        //to determine if country has been already added
        HashMap<String, mapeditor.Country> countryViewList=new HashMap<>();
        //to determine if line has already been added
        HashMap<String, mapeditor.Edge> edgeViewList=new HashMap<>();

        //reconstruct graph
        for (HashMap.Entry<String, model.Country> entry:countryList.entrySet()){
            model.Country country=entry.getValue();
            mapeditor.Country countryView=countryViewList.get(country.getName());
            if (countryView==null) {

                String continentName=country.getContinent().getName();

                countryView = new mapeditor.Country(country.getName(), country.getX(), country.getY(), continentName);

                countryView.setContinent(continentName);
                countryViewList.put(countryView.getName(), countryView);
                viewController.drawCountry(countryView);
            }

            //connect line
            ArrayList<model.Country> adjList= country.getAdjCountries();
            for (model.Country adjCountry:adjList){
                mapeditor.Country adjCountryView=countryViewList.get(adjCountry.getName());
                if (adjCountryView==null){
                    adjCountryView=new mapeditor.Country(adjCountry.getName(),adjCountry.getX(),adjCountry.getY(),adjCountry.getContinent().getName());
                    countryViewList.put(adjCountryView.getName(),adjCountryView);
                    viewController.drawCountry(adjCountryView);
                }
                //if already drew a line
                if (!countryView.isAdjacent(adjCountryView))
                    viewController.drawLine(countryView,adjCountryView);
            }

        }

    }



}
