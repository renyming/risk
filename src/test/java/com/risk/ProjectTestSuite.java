package com.risk;
 /**
 * the test suite of all project
 */

import com.risk.model.ContinentTest;
import com.risk.model.CountryTest;
import com.risk.model.ModelTest;
import com.risk.model.PlayerTest;
import com.risk.validate.MapValidatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ContinentTest.class,
        ModelTest.class,
        CountryTest.class,
        PlayerTest.class,
        MapValidatorTest.class})

public class ProjectTestSuite {

}
