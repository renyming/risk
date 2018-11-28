package com.risk.common;

import com.risk.model.Player;

/**
 * tools
 **/
public class Tool {

    public Tool() {

    }

    public static void printBasicInfo(Player player, String title) {

        System.out.println();
        System.out.println(title);
        System.out.println("-----------------------------------------------");
        System.out.println("Current Player: " + player.getName());
        System.out.println("Current total strength: " + player.getTotalStrength());
        System.out.println("Current armies need to allocated: " + player.getArmies());
        System.out.println("Current size of countries owned: " + player.getCountriesOwned().size());
        System.out.print("{");
        player.getCountriesOwned().stream().forEach(c -> System.out.print(c.getName()+","));
        System.out.println("}");
        System.out.println("Current continent owned: ");
        System.out.print("{");
        player.getContinentsOwned().stream().forEach(continent -> System.out.print(continent.getName() + ", "));
        System.out.println("}");
        System.out.println("Current Card owned: ");
        System.out.println(player.getCards());
        System.out.println();
    }
}
