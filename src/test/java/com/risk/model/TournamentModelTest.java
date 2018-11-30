package com.risk.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TournamentModelTest {
    private  ArrayList<ArrayList<String>> finalResult;
    private  int maps;
    private  int games;
    private  String winner;

    @Before
    public  void beforeClass() {
        Random random = new Random();
        finalResult = new ArrayList<>();
        maps = random.nextInt(5) + 1;
        games = random.nextInt(5) + 1;
        winner = "Winner";

    }

    @Test
    public  void startTournamentTest(){
        int numMaps = 0;
        while(numMaps < maps){
            int numGames = 0;
            ArrayList<String> winners = new ArrayList<>();
            while(numGames< games){
                winners.add(winner);
                numGames++;
            }
            finalResult.add(winners);
            numMaps++;
        }
        assertNotNull(finalResult);
        assertEquals(maps, finalResult.size());
        finalResult.stream().forEach(w -> assertEquals(games, w.size()));

    }
}