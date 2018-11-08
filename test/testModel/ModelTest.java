package testModel;

import model.*;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import view.View;

import java.io.IOException;

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
        p = new Player("Lee", 5);
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

        testedModel1.readFile("./resource/Invalid1.map");
        assertEquals(false, testedModel1.getFileInfoMenu().getValid());
        assertEquals(false, testedModel1.isValidFile());

        testedModel2.readFile("./resource/Invalid3.map");
        assertEquals(false, testedModel2.getFileInfoMenu().getValid());
        assertEquals(false, testedModel2.isValidFile());

        testedModel3.readFile("./resource/Invalid4.map");
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

        testedModel.readFile("./resource/Aden.map");
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
     * Test allocateArmy() method
     */
    @Test
    public void testAllocateArmy() {

        int armiesC = 6;
        int armiesP = 6;

        newModel1.allocateArmy(china);

        assertEquals(armiesC, china.getArmies());
        assertEquals(armiesP, p.getArmies());
    }

//    @Test
//    /**
//     * Test initiatePlayers() method
//     */
//    public void testInitiatePlayers() {
//
//        PlayerView playerView = new PlayerView();
//
//        int num = 4;
//
//        newModel1.initiatePlayers(4, playerView);
//
//        assertEquals(num, newModel1.getPlayers().size());
//        assertEquals(newModel1.getPlayers().get(0), newModel1.getCurrentPlayer());
//    }


}
