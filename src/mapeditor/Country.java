package mapeditor;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

/**
 * this is Country class for MapEditor
 */
public class Country extends AnchorPane {

    private static int cID=0;

    //****************************************
    // Data members for export map to file
    private int ID;
    private String name;
    private String continent;
    private double x;
    private double y;
    private static double widthCountry=120;
    private static double heightCountry=80;
    private ArrayList<Country> adjList;
    //****************************************

    private ArrayList<Edge> edgeList;
    private CountryController countryController;

    //getId() is conflict with super class, has to write as "getID"

    /**
     * getter for ID data member
     * @return ID The ID of the country object
     */
    public int getID() {
        return ID;
    }

    /**
     * setter for ID data member
     * @param ID The ID of the country object
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * getter for name data member
     * @return name The name of the country
     */
    public String getName() {
        return name;
    }

    /**
     * setter for name data member
     * @param name The name of the country object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for the continent name
     * @return continent The name of the belonging continent
     */
    public String getContinent() {
        return continent;
    }

    /**
     * setter for name of the continent of the country
     * @param continent The name of the continent
     */
    public void setContinent(String continent) {
        this.continent = continent;
    }

    /**
     * getter for the x position of the country
     * @return x The x position of the country
     */
    public double getX() {
        return x;
    }

    /**
     * setter for the x position of the country
     * @param x The x positon of the country
     */
    public void setX(int x) {
        this.x = x;
        this.relocate(x,y);
    }

    /**
     * getter for the y position of the country
     * @return y The y position of the country
     */
    public double getY() {
        return y;
    }

    /**
     * setter for the y position of the country
     * @param y The y positioj of the country
     */
    public void setY(int y) {
        this.y = y;
        this.relocate(x,y);
    }

    /**
     * getter for the counties adjacent list which contains graph information
     * @return adjList the countries adjacent list
     */
    public ArrayList<Country> getAdjList() {
        return adjList;
    }

    /**
     * add a countru to the adjacent list of the current country
     * @param country The new neighbour country
     */
    public void addAdjCountry(Country country){
        adjList.add(country);
    }

    /**
     * remove a countru from the adjacent list of the current country
     * @param country The neighbour country to be removed
     */
    public void removeAdjCountry(Country country){
        adjList.remove(country);
    }

    /**
     * add an edge to the edge list
     * @param e the edge to be added
     */
    public void addEdge(Edge e){
        edgeList.add(e);
    }

    /**
     * remove an dege from the edge list
     * @param e the edge to be removed
     */
    public void removeEdge(Edge e){
        edgeList.remove(e);
    }

    /**
     * getter for the edge list
     * @return current edge list
     */
    public ArrayList<Edge> getEdgeList() {
        return edgeList;
    }

    /**
     * to decide whether a given country is a neighbour of current country
     * @param country The given country
     * @return true if the given country is a neighbour of current country; otherwise return false
     */
    public boolean isAdjacent(Country country){
        return adjList.contains(country);
    }

    /**
     * the ctor for Country class
     * @param x The x position of the country
     * @param y The y position of the country
     */
    public Country(double x, double y){
        this("Country "+(cID+1),x,y,View.continents.get(0));
    }

    /**
     * the ctor for Country class
     * @param name The name of the country
     * @param x The x position of the country
     * @param y The y position of the country
     * @param continent The name of the continent it belongs to
     */
    public Country(String name, double x, double y, String continent){
        this.ID=++cID;
        this.name=name;
        this.continent=continent;
        this.x=x;
        this.y=y;
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
        setVisible(true);
    }

    /**
     * getter for ID of the country
     * @return ID of the country
     */
    public int getCountryId(){
        return ID;
    }

    public void relocateToPoint(){
//        System.out.println("Scene x: "+point.getX()+", y: "+point.getY());
//        System.out.println("Local x: "+getParent().sceneToLocal(point).getX()+", y: "+getParent().sceneToLocal(point).getY());
//        System.out.println("Relocate x: "+(getParent().sceneToLocal(point).getX() - (getWidth() / 2))+", y: "+(getParent().sceneToLocal(point).getY() - (getHeight() / 2)));
//        System.out.println(getBoundsInLocal().getWidth());
//        System.out.println(widthProperty());

        relocate(
                (getParent().sceneToLocal(x,y).getX() - (widthCountry / 2)),
                (getParent().sceneToLocal(x,y).getY() - (heightCountry / 2))
        );
    }
}
