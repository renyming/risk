package mapeditor;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class Country extends AnchorPane {

    private static int cID=0;

    //****************************************
    // Data members for export map to file
    private int ID;
    private String name;
    private String continent;
    private int x;
    private int y;
    private ArrayList<Country> adjList;
    //****************************************

    private ArrayList<Edge> edgeList;
    private CountryController countryController;

    //getId() is conflict with super class, has to write as "getID"
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        this.relocate(x,y);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        this.relocate(x,y);
    }

    public ArrayList<Country> getAdjList() {
        return adjList;
    }

    public void addAdjCountry(Country country){
        adjList.add(country);
    }

    public void removeAdjCountry(Country country){
        adjList.remove(country);
    }

    public void addEdge(Edge e){
        edgeList.add(e);
    }

    public void removeEdge(Edge e){
        edgeList.remove(e);
    }

    public ArrayList<Edge> getEdgeList() {
        return edgeList;
    }

    public boolean isAdjacent(Country country){
        return adjList.contains(country);
    }

    public Country(double x, double y){
        this("Country "+(cID+1),x,y,View.continents.get(0));
    }

    public Country(String name, double x, double y, String continent){
        this.ID=++cID;
        this.name=name;
        this.continent=continent;
        this.x=(int) x;
        this.y=(int) y;
        adjList=new ArrayList<>();
        edgeList=new ArrayList<>();

        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("Country.fxml"));
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countryController = fxmlLoader.getController();
        countryController.initialize(this);
        this.relocate(x,y);
        setVisible(true);
    }

    public int getCountryId(){
        return ID;
    }

    public Point2D getLocation(){
        return new Point2D(this.getLayoutX(),this.getLayoutY());
    }

}
