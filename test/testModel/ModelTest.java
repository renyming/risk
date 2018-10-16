package testModel;

import model.Continent;
import model.Model;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class ModelTest {

    public static Model newModel3;
    public static Model newModel1;

    @BeforeClass
    public static void beforeClass(){
        newModel3 = new Model();
        newModel3.getContinents().add(new Continent("Berga",4));
        newModel3.getContinents().add(new Continent("Centre Metro",7));
        newModel3.getContinents().add(new Continent("Craterland",5));
        newModel3.getContinents().add(new Continent("Great Horn Island",2));
        newModel3.getContinents().add(new Continent("Greenwood Island",3));
        newModel3.getContinents().add(new Continent("Polar Island",1));
        newModel3.getContinents().add(new Continent("Queensbasin",5));
        newModel3.getContinents().add(new Continent("Republic of Cranberra",4));

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
        assertEquals(newModel3.getContinents().size(), testedModel.getContinents().size());
        assertTrue(testedModel.getContinents().get(1).getName().equals("Centre Metro"));
        assertTrue(testedModel.getContinents().get(7).getControlVal() == 4);


        for(int i = 0; i < newModel1.getContinents().size(); i ++){
            assertTrue(newModel1.getContinents().get(i).equals(testedModel.getContinents().get(i)));
        }
        //assertEquals(16, testedModel.getCountries().size());
    }
}
