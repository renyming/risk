package com.risk.model;

/**
 * The test suite of model
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
