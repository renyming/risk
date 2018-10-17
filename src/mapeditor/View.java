package mapeditor;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class View extends AnchorPane{

    private ViewController viewController;

    public View() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("View.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
        viewController = fxmlLoader.getController();
        viewController.initialize();
    }


}
