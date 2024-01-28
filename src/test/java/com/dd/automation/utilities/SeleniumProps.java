package com.dd.automation.utilities;

import java.io.File;
import java.util.List;

public class SeleniumProps {
    private static final String FILE = "src/test/resources/properties/selenium.properties";
    private static final PropUtils PROP_FILE = new PropUtils(new File(FILE));
    public static String getValue(String key) {
        return PROP_FILE.getValue(key);
    }
    public static List<String> getValues(String key) {
        return PROP_FILE.getValues(key);
    }
    public static void setValue(String key, String value) {
        PROP_FILE.setValue(key, value);
    }
    /*
     *************************************************************************
     * Runtime Property Keys
     *************************************************************************
    */
    public static final String RT_TEST_BROWSER = "runtime.test.browser";
    public static final String RT_APP_URL = "runtime.app.url";
    public static final String RT_APP_ENV = "runtime.app.env";
    public static final String RT_REMOTE_URL = "runtime.remote.url";
    public static final String RT_REMOTE_HEADLESS = "runtime.remote.headless";
    public static final String RT_LOCAL_HEADLESS = "runtime.local.headless";

    /*
     *************************************************************************
     * WebDriver Global Property Keys
     *************************************************************************
    */

    public static final String WD_FLUENT_TIMEOUT = "webdriver.fluent.timeout";
    public static final String WD_BINARY_DIR = "webdriver.binary.dir";
    public static final String WD_DL_DIR = "webdriver.download.dir";
    public static final String WD_FL_DIR = "webdriver.files.dir";
    public static final String WD_CHROME_BIN = "webdriver.chrome";
    public static final String WD_ENABLE_PROXY = "webdriver.enable.proxy";
    public static final String WD_PROXY = "webdriver.proxy";
    public static final String WD_SS_FORMAT = "webdriver.screenshot.format";
    public static final String WD_CLEAR_COOKIES = "webdriver.clear.cookies.after";





/*
     *************************************************************************
     * autoIT Property Keys
     *************************************************************************
     */
    public static final String autoItpath ="src/test/resources/autoIt/compiled";
/*

    private static final String FILE = "src/test/resources/properties/selenium.properties";
    private static final PropUtils PROP_FILE = new PropUtils(new File(FILE));

    public static String getValue(String key) {
        return PROP_FILE.getValue(key);
    }

    public static List<String> getValues(String key) {
        return PROP_FILE.getValues(key);
    }

    public static void setValue(String key, String value) {
        PROP_FILE.setValue(key, value);
    }*/
}
