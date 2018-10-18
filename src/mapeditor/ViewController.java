package mapeditor;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ViewController {

    @FXML
    AnchorPane view_pane;

    @FXML
    public void initialize() {
        view_pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getButton()== MouseButton.PRIMARY){
                    Country country=new Country(event.getSceneX(),event.getSceneY());
                    country.relocate(event.getSceneX(),event.getSceneY());
                    view_pane.getChildren().add(country);
                    addDragDetection(country);
                    addDragOver(country);
                    addDragDropped(country);
//                addDragDone(country);
                }

            }
        });

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

//    private void addDragDone(Country country) {
//        country.setOnDragDone(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent event) {
//                if (event.getDragboard().hasString()) {
//                    String stringID = event.getDragboard().getString();
//                    int ID=Integer.parseInt(stringID);
//                    System.out.println("Connected country"+ID+" and country"+country.getCountryId());
//                }
//                event.consume();
//            }
//        });
//    }

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
        addMouseOver(line);
        addMouseExit(line);
        addDelLine(line);

        view_pane.getChildren().add(line);
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
                    view_pane.getChildren().remove(line);
                    System.out.println("Country "+line.getP1().getCountryId()+" and Country "+line.getP2().getCountryId()+" are disconnected");
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

}
