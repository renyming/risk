package com.risk.strategy;

/**
 * The test suite of strategy
 **/

import com.risk.model.ContinentTest;
import com.risk.model.CountryTest;
import com.risk.model.ModelTest;
import com.risk.model.PlayerTest;
import com.risk.strategy.BenevolentStrategyTest;
import com.risk.strategy.CheaterStrategyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({BenevolentStrategyTest.class,
        CheaterStrategyTest.class,
        AggressiveStrategyTest.class,
        RandomStrategyTest.class,
})
public class StrategyTestSuite {

}
