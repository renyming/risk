package driver;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.View;

public class Driver extends Application {
    private Model model;
    private View view;

    @Override
    public void start(Stage primaryStage) throws Exception {
        model = new Model();
        view = new View();
        model.addObserver(view);

        // initialize something here;

        view.showMenuStage(); // display the game menu
    }

    /**
     * Called when the game quits
     */
    public void close() {
        this.close();
    }
}
