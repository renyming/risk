package mapeditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MapEditor extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        View view=new View();
        Scene scene=new Scene(view,1080,720);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Map Editor");
        primaryStage.show();
    }
}