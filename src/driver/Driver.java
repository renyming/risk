package driver;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.View;


enum Phase {
    START, CREATE_MAP, LOAD_MAP, INIT_USER, INIT_ARMY, ROUND_ROBIN, REINFORCEMENT, ATTACK, FORTIFICATION, WIN, CLOSE
}

/**
* The entrence of the application, main() method
*/
public class Driver extends Application {

    private Model model;
    private View view;
    private Phase nextPhase;

    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        model = new Model();
        view = new View();
        model.addObserver(view);

        run();
    }

    public void run() {

        while (true) {
            switch (nextPhase){
                case START:
                    // first interface to choose create or load a map， view.showMenuStage
                    nextStage = model.initInterface();
                    break;
                case CREATE_MAP:
                    // if create map, jump into create map module, return close stage
                    nextStage = model.createMap();
                case LOAD_MAP:
                    // include read file, createGraph, verify the validation, and add observer
                    // if load success, return initUser stage, or else return loadMap stage (ask re-choose the map path and reload)
                    nextStage = model.loadMap();
                case INIT_USER:
                    // include input user num, randomly allocated countries, and notify view to show map
                    // if success, return initArmy stage, or else return initUser stage (ask re-input the map path and reload)
                    nextStage = model.initUser();
                case INIT_ARMY:
                    // include allocated initial armies, and place their given armies on their own countries until armies is empty
                    // return Reinforcement stage
                    nextStage = model.initArmy();
                case ROUND_ROBIN:
                    // for all the user, do reinforcement, attack, fortificaion phase, until one of user win.
                    // return win stage;
                    // the detail task like following description:
//                    "Reinforcement":
//                        // include calculation of correct number of reinforcement armies, and place them in the country owned by the user
//                        // return attack stage
//                        nextStage = model.renforcement();
//                    "attack":
//                        // next phase implementation
//                        // return Fortification stage
//                        nextStage = model.attack();
//                     "Fortification":
//                        // armies movement once time in owned connected countries
                          // if game not finished, return Reinforcement, else win stage
                    nextStage = model.roundRobin();
                case WIN:
                    // show winner's view;
                    // return start stage
                    nextStage = model.win();
                case CLOSE:
                    // when finish create a map, or any time player click the close button
                    close();
            }

        }
    }

    /**
     * Called when the game quits
     */
    public void close() {
        this.close();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
