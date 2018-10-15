package testModel;

import model.Continent;
import model.Model;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class ModelTest {

    public static Model newModel1;

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
    }

    @Test
    public void testReadFile() throws IOException {

        Model testedModel = new Model();
        testedModel.readFile("Aden.map");
        assertEquals(newModel1.getContinents().size(), testedModel.getContinents().size());
        assertTrue(testedModel.getContinents().get(1).getName().equals("Centre Metro"));
        assertTrue(testedModel.getContinents().get(7).getControlVal() == 4);
    }
}
