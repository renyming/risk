package com.risk.model;

import static org.junit.Assert.*;

import com.risk.strategy.PlayerBehaviorStrategy;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Test Model class
 */
public class ModelTest {

    public static Model newModel1;

    public static Continent asien;
    public static Country china;
    public static Country thailand;
    public static Country singapore;

    public static Player p;

    /**
     * Preparation before all this method
     */
    @BeforeClass
    public static void beforeClass(){

        newModel1 = new Model();
        newModel1.getContinents().add(new Continent("Berga",4));
        newModel1.getContinents().add(new Continent("Centre Metro",7));
        newModel1.getContinents().add(new Continent("Craterland",5));
        newModel1.getContinents().add(new Continent("Great Horn Island",2));
        newModel1.getContinents().add(new Continent("Greenwood Island",3));
        newModel1.getContinents().add(new Continent("Polar Island",1));
        newModel1.getContinents().add(new Continent("Queensbasin",5));
        newModel1.getContinents().add(new Continent("Republic of Cranberra",4));

        asien = new Continent("Asian", 5);
        p = new Player("Lee", 5, "human");
        p.setArmies(7);
        newModel1.setCurrentPlayer(p);

        china = new Country("china", asien);
        thailand = new Country("thailand", asien);
        singapore = new Country("singapore", asien);

        china.addEdge(thailand);
        china.addEdge(singapore);
        thailand.addEdge(china);
        singapore.addEdge(china);

        newModel1.getCountries().put(china.getName(), china);

        china.setPlayer(p);
        p.addCountry(china);
        china.setArmies(5);
    }

    /**
     * Test save() method
     * @throws Exception exception
     */
    @Test
    public void testSaveAndLoad() throws Exception{
        newModel1.save("game");
        Model newModel2;
        newModel2 = newModel1.load("g_model.ser");

        assertTrue(newModel2.getContinents().size() == 8);
        assertTrue(newModel2.getCountries().size() == 1);
        assertTrue(newModel2.getCurrentPlayer().getName().equals("Lee"));
        assertTrue(newModel2.getCurrentPlayer().getCountriesOwned().size() == 1);
    }

    /**
     * Test allocateArmy() method
     */
    @Test
    public void testAllocateArmy() {

        newModel1.setCurrentPlayer(p);
        newModel1.allocateArmy(china);

        assertEquals(6, china.getArmies());
        assertEquals(6, p.getArmies());
    }

    /**
     * Test map file validation
     * @throws IOException io exceptions
     */
    @Test
    public void isValidFile() throws IOException{

        Model testedModel1 = new Model();
        testedModel1.setFileInfoMenu(new FileInfoMenu());
        testedModel1.setNumPlayerMenu(new NumPlayerMenu());

        Model testedModel2 = new Model();
        testedModel2.setFileInfoMenu(new FileInfoMenu());
        testedModel2.setNumPlayerMenu(new NumPlayerMenu());

        Model testedModel3 = new Model();
        testedModel3.setFileInfoMenu(new FileInfoMenu());
        testedModel3.setNumPlayerMenu(new NumPlayerMenu());

        testedModel1.readFile("./src/main/resources/maps/Invalid1.map");
        assertEquals(false, testedModel1.getFileInfoMenu().getValid());
        assertEquals(false, testedModel1.isValidFile());

        testedModel2.readFile("./src/main/resources/maps/Invalid3.map");
        assertEquals(false, testedModel2.getFileInfoMenu().getValid());
        assertEquals(false, testedModel2.isValidFile());

        testedModel3.readFile("./src/main/resources/maps/Invalid4.map");
        assertEquals(false, testedModel3.getFileInfoMenu().getValid());
        assertEquals(false, testedModel3.isValidFile());
    }

    /**
     * Test readFile() method
     * @throws IOException io exceptions
     */
    @Test
    public void testReadFile() throws IOException {

        Model testedModel = new Model();
        testedModel.setFileInfoMenu(new FileInfoMenu());
        testedModel.setNumPlayerMenu(new NumPlayerMenu());

        testedModel.readFile("./src/main/resources/maps/Aden.map");
        assertEquals(newModel1.getContinents().size(), testedModel.getContinents().size());
        assertTrue(testedModel.getContinents().get(1).getName().equals("Centre Metro"));
        assertTrue(testedModel.getContinents().get(7).getControlVal() == 4);


        for(int i = 0; i < newModel1.getContinents().size(); i ++){
            assertTrue(newModel1.getContinents().get(i).equals(testedModel.getContinents().get(i)));
        }

        assertEquals(42, testedModel.getCountries().size());
        assertEquals("Queensbasin", testedModel.getCountries().get("Quandry").getContinent().getName());
        assertEquals(7, testedModel.getCountries().get("Rand").getAdjCountries().size());
        assertEquals(376, (int)testedModel.getCountries().get("Quintess").getY());


        for(int i = 0; i < testedModel.getContinents().size(); i ++){
            assertNotNull(testedModel.getContinents().get(i).getColor());
        }
    }


    /**
     * Test startUp phase method
     * @throws IOException io exceptions
     */
    @Test
    public void testStartUpPhase() throws IOException {

        Model testedModel = new Model();
        testedModel.setFileInfoMenu(new FileInfoMenu());
        testedModel.setNumPlayerMenu(new NumPlayerMenu());


        testedModel.readFile("./src/main/resources/maps/Aden.map");
        ArrayList<String> players1 = new ArrayList<>();
        IntStream.range(0, 10)
                .forEach(c -> players1.add("human player"));
//        PlayersWorldDomination.getInstance().setTotalNumCountries(42);
        testedModel.checkPlayersNum("10");
        assertEquals(0, testedModel.getPlayers().size());

        testedModel.readFile("./src/main/resources/maps/Aden.map");
        ArrayList<String> players2 = new ArrayList<>();
        IntStream.range(0, 4)
                .forEach(c -> players2.add("human player"));
//        PlayersWorldDomination.getInstance().setTotalNumCountries(42);
        testedModel.initiatePlayers(players2);
        assertEquals(4, testedModel.getPlayers().size());
        assertEquals(42, testedModel.getCountries().size());
        assertEquals(8, testedModel.getContinents().size());
    }


}
