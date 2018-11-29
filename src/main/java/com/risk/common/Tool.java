package com.risk.common;

import com.risk.model.Model;
import com.risk.model.Phase;
import com.risk.model.Player;
import javafx.application.Platform;

/**
 * tools
 **/
public class Tool {

    public Tool() {

    }

    /**
     * Print out the useful info
     * @param player player need to print
     * @param title title
     */
    public static void printBasicInfo(Player player, String title) {

        System.out.println();
        System.out.println(title);
//        System.out.println("-----------------------------------------------");
//        System.out.println("Current Player: " + player.getName());
//        System.out.println("Current total strength: " + player.getTotalStrength());
//        System.out.println("Current armies need to allocated: " + player.getArmies());
//        System.out.println("Current size of countries owned: " + player.getCountriesOwned().size());
//        System.out.println(player.getCountriesOwned());
//        System.out.println("Current continent owned: ");
//        player.getContinentsOwned().stream().forEach(continent -> System.out.print(continent.getName() + ", "));
//        System.out.println("Current Card owned: ");
//        System.out.println(player.getCards());
//        System.out.println("Current turn " + Model.currentTurn);
        System.out.println();
    }

    /**
     * update UI when not in the FX thread
     */
    public static void updateThread() {

//        new Thread(new Runnable() {
//            @Override public void run() {
//                Platform.runLater(new Runnable() {
//                    @Override public void run() {
//                        Phase.getInstance().update();
//                    }
//                });
//            }
//        }).start();
    }
}
