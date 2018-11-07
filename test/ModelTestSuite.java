/**
 * @program: risk
 * @description: Model test suite class, combination all the test case in the model
 * @author: Zhijing Ling
 * @create: 2018-11-07 18:04
 **/

import model.Continent;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import testModel.ContinentTest;
import testModel.CountryTest;
import testModel.PlayerTest;

@RunWith(Suite.class)
@SuiteClasses({ContinentTest.class,
        CountryTest.class,
        PlayerTest.class})

public class ModelTestSuite {

}
