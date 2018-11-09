package com.risk.driver;

import javafx.application.Application;
import com.risk.model.Model;
import com.risk.view.View;



/**
* The entrance of the application, like the main() method
*/
public class Driver extends Application {

    private Model model;
    private View view;

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        model = new Model();
        view = new View();

        // This is necessary because when the selecting map action happens,
        // View need to pass file path to the Model, but View don't have the
        // Model reference
        view.setModel(model);
        view.display();
    }



    /**
     * If possible, call this function when the game quits
     */
    public void close() { this.close(); }
}
