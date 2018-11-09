package model; /**
 * @program: risk
 * @description: Model test suite class, combination all the test case in the model
 * @author: Zhijing Ling
 * @create: 2018-11-07 18:04
 **/
/**
 * @program: risk
 * @description: The test suite of model
 * @author: Zhijing Ling
 * @create: 2018-11-08 20:39
 **/
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ContinentTest.class,
        ModelTest.class,
        CountryTest.class,
        PlayerTest.class})

public class ModelTestSuite {

}
