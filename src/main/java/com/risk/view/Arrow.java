package com.risk.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;

public class Arrow extends Group {

    // common attributes
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private String arrowType;
//    private Text armies;
//    private final int armiesTextSize;

    // attributes for default arrow
    private Line line;
    private Polygon triangle;

    // attributes for attack arrow

    // attributes for fortification arrow
//    private ArrayList<Polygon> upperRectangle;
//    private ArrayList<Polygon> lowerRectangle;
//    private int arrowComponentCounter;


    public Arrow(String arrowType) {
        this.arrowType = arrowType;
//        armies = new Text("99");
//        armiesTextSize = 20;
//        armies.setFont(new Font(armiesTextSize));

        switch(arrowType) {
            case "DEFAULT":
                Color attackColor = Color.color(0, 0, 0);

                line = new Line();
                line.setStroke(attackColor);
                line.setStrokeWidth(2);

                triangle = new Polygon();
//                triangle.setFill(Color.TRANSPARENT);
                triangle.setStroke(attackColor);
                triangle.setStrokeWidth(2);

                getChildren().addAll(line, triangle);
                break;
            case "ATTACK":

                break;
            case "FORTIFICATION":
//                upperRectangle = new ArrayList<>();
//                lowerRectangle = new ArrayList<>();
                break;
        }
    }

    private void update() {
        // calculate arrow parameters
        int countryViewHalfSize = 30;
//        double height = width < 40 ? width : 40;
        double degree = Math.atan((endY - startY) / (endX - startX)) / (2 * Math.PI) * 360;
        double endXIndent = 0; // used for fixed arrow
        double positiveDegree = degree < 0 ? degree * -1 : degree;
        System.out.println(positiveDegree);
        if (0 <= positiveDegree && positiveDegree < 45) {
            System.out.println("case_1");
            endXIndent = countryViewHalfSize / cos(Math.toRadians(positiveDegree));
        } else if (45 <= positiveDegree && positiveDegree <= 90) {
            System.out.println("case_2");
            endXIndent = countryViewHalfSize / sin(Math.toRadians(positiveDegree));
        }

        double width = Math.sqrt(Math.pow((endY - startY), 2) + Math.pow((endX - startX), 2));
        if (startX > endX) degree += 180;
        Rotate rotate = new Rotate(degree, startX, startY);

        switch (arrowType) {
            case "DEFAULT":
                // clear previous drawing components' parameters
                triangle.getPoints().clear();
                line.getTransforms().clear();
                triangle.getTransforms().clear();

                // calculate new parameters
//                double endXIndent = 0;
//                if (0 <= degree && degree < 45) {
//                    endXIndent = countryViewHalfSize / cos(Math.toRadians(degree));
//                } else if (45 <= degree && degree <= 90) {
//                    endXIndent = countryViewHalfSize / sin(Math.toRadians(degree));
//                }
                double horizontalEndX = startX + width - endXIndent;
                double arrowHeadWidth = 10;
                double arrowHeadHeight = 6;

                // set new parameters for all components
                line.setStartX(startX);
                line.setStartY(startY);
//                line.setEndX(startX + width/4*3);
                line.setEndX(startX + width);
                line.setEndY(startY);
                line.getTransforms().add(rotate);

                Double [] trianglePoints = new Double[] {
                        horizontalEndX - arrowHeadWidth, startY + arrowHeadHeight / 2,
                        horizontalEndX - arrowHeadWidth, startY - arrowHeadHeight / 2,
                        horizontalEndX, startY
                };
                triangle.getPoints().addAll(trianglePoints);
                triangle.getTransforms().add(rotate);
                break;
            case "ATTACK":

                break;
            case "FORTIFICATION":

                break;
        }
//        armies.getTransforms().clear();
        // offset is - 0.6*armiesTextSize+1
//        armies.setLayoutX(startX + (endX - startX)/4*3/2 - 0.6*armiesTextSize+1);
        // offset is + 0.4*armiesTextSize-1
//        armies.setLayoutY(startY + (endY - startY)/4*3/2 + 0.4*armiesTextSize-1);
    }

    public void setStart(double startX, double startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void setEnd(double endX, double endY) {
        this.endX = endX;
        this.endY = endY;
        update();
    }
}