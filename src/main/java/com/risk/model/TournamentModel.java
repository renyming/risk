package com.risk.model;

import com.risk.controller.MenuController;
import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Tournament model to start tournament mode game
 */
public class TournamentModel {

    private static ArrayList<ArrayList<String>> finalResult = new ArrayList<>();

    /**
     * start tournament game when called by menu controller
     * @param selectedMaps maps
     * @param filesPath map files path
     * @param model game model
     * @param gamesPerMapSpinner games per map
     * @param turnsPerGameSpinner max turn per game
     * @param menuController menu controller
     * @param selectPlayerTypes player types (strategies)
     */
    public static void startTournament(ObservableList<String> selectedMaps, ArrayList<String> filesPath,
                                Model model, Spinner<Integer> gamesPerMapSpinner, Spinner<Integer> turnsPerGameSpinner,
                                MenuController menuController, ArrayList<String> selectPlayerTypes){

        int numMaps = 0;
        while(numMaps < selectedMaps.size()){
            int numGames = 0;
            String mapPath = filesPath.get(numMaps);
            ArrayList<String> winners = new ArrayList<>();

            try {
                model.resetValue();
                model.readFile(mapPath);
            } catch (IOException exception) {
                System.out.println("MenuController.readFile(): " + exception.getMessage());
            }
            System.out.println("Next map is "+mapPath);
            while(numGames< gamesPerMapSpinner.getValue()){
                model.resetValue();
                System.out.println("Next game is "+ (numGames+1));
                Model.maxTurn = turnsPerGameSpinner.getValue();
                System.out.println("Max Turn: "+Model.maxTurn);
                menuController.startGame();
                System.out.println("Finish one game on map :"+mapPath);
                winners.add(Model.winner);
                numGames++;
            }
            System.out.println("Finish all "+gamesPerMapSpinner.getValue()+" games on map "+mapPath);
            finalResult.add(winners);
            numMaps++;
        }
        System.out.println("");
        System.out.println("=============Tournament Result============= ");
        System.out.println("Maps : "+selectedMaps);
        System.out.println("Players : "+ selectPlayerTypes);
        System.out.println("Games Per Map : "+gamesPerMapSpinner.getValue());
        System.out.println("Max Turns : "+turnsPerGameSpinner.getValue());
        for(int i=0; i<selectedMaps.size(); i++){
            for(int j=0; j<gamesPerMapSpinner.getValue(); j++){
                System.out.print("Map : "+selectedMaps.get(i)+"  Game : "+(j+1)+"  Winner : "+finalResult.get(i).get(j));
                System.out.println("");

            }
        }
        System.out.println("=============Tournament Finish=============");
        finalResult.clear();
    }
}
