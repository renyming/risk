package mapeditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class View extends AnchorPane{

    private ViewController viewController;
    public static ObservableList<String> continents= FXCollections.observableArrayList();

    public View() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("View.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
        viewController = fxmlLoader.getController();
        viewController.initialize();
        continents.add("Continent 1");
        System.out.println(continents);
    }

}
