package mapeditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Driver class to start up map editor only
 */
public class MapEditor extends Application {

    /**
     * Main function
     * @param args Arguments to run with
     */
    public static void main(String[] args){
        launch(args);
    }

    /**
     * Overrides start method in Application class
     * @param primaryStage Stage object
     * @throws IOException Throws exception of IO error
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        View view=new View();
        Scene scene=new Scene(view,1080,746);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Map Editor");
        primaryStage.show();
    }
}