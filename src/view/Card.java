package view;

import controller.CardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

class Card {

    private static Card instance;

    private AnchorPane mainCardPane;
    private CardController cardController;
    private Stage cardStage;

    private Card() {
        FXMLLoader menuFxmlLoader = new FXMLLoader(getClass().getResource("Card.fxml"));
        try {
            mainCardPane = menuFxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("Card.ctor(): " + exception.getMessage());
        }
        cardController = menuFxmlLoader.getController();
        cardStage = new Stage();
        cardStage.setTitle("Card Exchange");
        cardStage.setScene(new Scene(mainCardPane,500,300));
        cardStage.setResizable(false);
        cardStage.sizeToScene();
    }

    static Card getInstance() {
        if (null == instance) instance = new Card();
        return instance;
    }

    // TODO: get some controllers
    void init () {}

    public void show() { cardStage.show(); }

    public void hide() { cardStage.hide(); }

    public void close() { cardStage.close(); }

}
