package com.risk;
 /**
 * the test suite of all project
 */

import com.risk.model.*;
import com.risk.strategy.AggressiveStrategyTest;
import com.risk.strategy.BenevolentStrategyTest;
import com.risk.strategy.CheaterStrategyTest;
import com.risk.strategy.RandomStrategyTest;
import com.risk.validate.MapValidatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.util.zip.CheckedInputStream;


@RunWith(Suite.class)
@SuiteClasses({ContinentTest.class,
        ModelTest.class,
        CountryTest.class,
        PlayerTest.class,
        MapValidatorTest.class,
        AggressiveStrategyTest.class,
        BenevolentStrategyTest.class,
        CheaterStrategyTest.class,
        RandomStrategyTest.class,
        TournamentModelTest.class
})

/**
 * project test suite
 */
public class ProjectTestSuite {

}
