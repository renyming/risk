package mapeditor;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * this is Edge class for MapEditor
 */
public class Edge extends Line {
    private Country p1, p2;
    public final static Color normalColor=Color.web("#000000");
    public final static Color deleteColor=Color.web("#f2e94e");
    public final static int normalWidth=2;
    public final static int deleteWidth=3;

    /**
     * the ctor for Edge class
     * @param p1 The starting country of the edge
     * @param p2 The ending country of the dege
     */
    public Edge(Country p1, Country p2){
        this.setStartX(p1.getLayoutX()+60);
        this.setStartY(p1.getLayoutY()+40);
        this.setEndX(p2.getLayoutX()+60);
        this.setEndY(p2.getLayoutY()+40);
        this.p1=p1;
        this.p2=p2;
        this.setStroke(normalColor);
        this.setStrokeWidth(normalWidth);
    }

    /**
     * getter for the starting country of the edge
     * @return the starting country of the edge
     */
    public Country getP1(){
        return p1;
    }

    /**
     * getter for the ending country of the edge
     * @return the ending country of the edge
     */
    public Country getP2(){
        return p2;
    }

}
