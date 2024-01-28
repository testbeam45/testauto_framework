package com.dd.automation;

import com.dd.automation.utilities.TestReporter;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.dd.automation.steps",
        tags = "@Sanity",
        plugin = "json:target/cucumber-parallel/RunCukesReport.json"
)
public class RunCukesTest {

    @AfterClass
    public static void teardown() {
        TestReporter reports = new TestReporter();
        reports.generateCucumberHtmlReports();
    }
}

