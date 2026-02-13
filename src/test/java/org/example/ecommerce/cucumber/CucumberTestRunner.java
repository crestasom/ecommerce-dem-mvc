package org.example.ecommerce.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

/**
 * JUnit 5 Platform Suite Runner for Cucumber Tests
 * This will look for feature files in src/test/resources/features
 * and step definitions in org.example.ecommerce.cucumber package.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "org.example.ecommerce.cucumber")
public class CucumberTestRunner {
}
