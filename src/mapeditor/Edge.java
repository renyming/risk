package mapeditor;

import javafx.scene.shape.Line;

public class Edge extends Line {
    private Country p1, p2;
    public Edge(Country p1, Country p2){
        this.setStartX(p1.getLayoutX()+40);
        this.setStartY(p1.getLayoutY()+40);
        this.setEndX(p2.getLayoutX()+40);
        this.setEndY(p2.getLayoutY()+40);
        this.p1=p1;
        this.p2=p2;
    }
    public Country getP1(){
        return p1;
    }
    public Country getP2(){
        return p2;
    }

}
