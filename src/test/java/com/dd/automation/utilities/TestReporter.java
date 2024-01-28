package com.dd.automation.utilities;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.reducers.ReducingMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestReporter {
    public static void main(String... args) {
        TestReporter reports = new TestReporter();
        reports.generateCucumberHtmlReports();
    }

    public void generateCucumberHtmlReports() {
        Configuration config = new Configuration(new File("target/"), "Test-Project");
        config.addReducingMethod(ReducingMethod.MERGE_FEATURES_BY_ID);
        net.masterthought.cucumber.ReportBuilder reportBuilder;

        reportBuilder = new net.masterthought.cucumber.ReportBuilder(getJsonPaths(), config);
        reportBuilder.generateReports();
    }

    public List<String> getJsonPaths() {
        Collection<File> jsonFiles = org.apache.commons.io.FileUtils.listFiles(new File("target/cucumber-parallel"), new String[]{"json"}, true);
        List<String> jsonPaths = new ArrayList(jsonFiles.size());
        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
        return jsonPaths;
    }
}
