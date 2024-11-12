package io.github.soat7.myburgercontrol.testbdd

import io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME
import io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME
import io.cucumber.junit.platform.engine.Constants.JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME
import io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.ConfigurationParameters
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.SelectClasspathResources
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Suite
@IncludeEngines("cucumber")
@SelectPackages("io.github.soat7.myburgercontrol.testbdd")
@SelectClasspathResources(
    value = [
        SelectClasspathResource("/features"),
    ],
)
@ConfigurationParameters(
    value = [
        ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty"),
        ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "io.github.soat7.myburgercontrol.testbdd"),
        ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long"),
        ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @Ignorar"),
    ],
)
class CucumberRunnerTest
