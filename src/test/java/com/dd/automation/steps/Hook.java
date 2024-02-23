package com.dd.automation.steps;

import com.dd.automation.base.BaseUtil;
import com.dd.automation.utilities.ReusableLibrary;
import com.dd.automation.utilities.SeleniumProps;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;


import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Hook extends BaseUtil {

    private BaseUtil base;

    public Hook(BaseUtil base1) {
        this.base = base1;
    }


    /**
     * @description: To be executed before every scenario block
     */
    @Before
    public void setup() throws IOException {
        System.out.println("===============Start Browser==============");
        String appUrl = SeleniumProps.getValue(SeleniumProps.RT_APP_URL);
        String env = SeleniumProps.getValue(SeleniumProps.RT_APP_ENV);
        System.out.println("Application Environment: "+env);
        System.out.println("Application URL: "+appUrl);
        String remoteUrl = SeleniumProps.getValue(SeleniumProps.RT_REMOTE_URL);
        String browser;

        if (env.equalsIgnoreCase("LOCAL")) {
            browser = SeleniumProps.getValue(SeleniumProps.RT_TEST_BROWSER);
            setUpLocal(browser, appUrl);
        } else if (env.equalsIgnoreCase("JENKINS")) {
            browser = System.getProperty("Browser");
            setUpJenkins(browser, appUrl);
        } else {
            browser = System.getProperty("Browser");
            setUpRemote(browser, remoteUrl, appUrl);
        }
    }

    private static final long DEFAULT_TIMEOUT = 10;
    public static WebDriver driver;
    //static BrowserMobProxyServer server;
    static Actions actions;
    static WebDriverWait wait;
   // public static org.slf4j.Logger log = GenericUtil.getLogger(Login.class);


    public void setUpLocal(String browser, String url) {
        if ("IE".equalsIgnoreCase(browser)) {
            WebDriverManager.iedriver().setup();
            /* System.setProperty("webdriver.ie.driver","D:\\jseleniumqa\\src\\test\\resources\\drivers\\New folder\\IEDriverServer.exe"); */
            InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
            internetExplorerOptions.ignoreZoomSettings();
            internetExplorerOptions.introduceFlakinessByIgnoringSecurityDomains();
            internetExplorerOptions.requireWindowFocus();
            internetExplorerOptions.enablePersistentHovering();
/*
            internetExplorerOptions.setCapability("ignoreProtectedModeSettings",1);
            internetExplorerOptions.setCapability("IntroduceInstabilityByIgnoringProtectedModeSettings",true);
*/
            internetExplorerOptions.setCapability("browserFocus",true);
            base.Driver = new InternetExplorerDriver(internetExplorerOptions);
/*
            InternetExplorerDriverManager.getInstance(IEXPLORER).setup();
            InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
            internetExplorerOptions.ignoreZoomSettings();
            internetExplorerOptions.introduceFlakinessByIgnoringSecurityDomains();
            internetExplorerOptions.requireWindowFocus();
            base.Driver = new InternetExplorerDriver(internetExplorerOptions);
*/

        } else if ("FIREFOX".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
//            FirefoxDriverManager.getInstance(FIREFOX).setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            if ("true".equalsIgnoreCase(SeleniumProps.getValue(SeleniumProps.RT_LOCAL_HEADLESS))) {
                firefoxOptions.addArguments("--headless");
            }
            base.Driver = new FirefoxDriver(firefoxOptions);
        } else if ("CHROME".equalsIgnoreCase(browser)) {
//            WebDriverManager.chromedriver().clearDriverCache();
//            WebDriverManager.chromedriver().clearResolutionCache();
            WebDriverManager.chromedriver().setup();
//            ChromeDriverManager.getInstance(CHROME).version("2.46").setup();
            ChromeOptions options = new ChromeOptions();
            if ("true".equalsIgnoreCase(SeleniumProps.getValue(SeleniumProps.RT_LOCAL_HEADLESS))) {
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--start-fullscreen");
            }
            base.Driver = new ChromeDriver(options);
        }
        initialiseDriver(url);
    }

    public void setUpJenkins(String browser, String url) {
        if ("FIREFOX".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
//            FirefoxDriverManager.getInstance(FIREFOX).setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            if ("true".equalsIgnoreCase(SeleniumProps.getValue(SeleniumProps.RT_REMOTE_HEADLESS))) {
                firefoxOptions.addArguments("--headless");
            }
            base.Driver = new FirefoxDriver(firefoxOptions);
        } else if ("CHROME".equalsIgnoreCase(browser)) {
            WebDriverManager.chromedriver().setup();
//            ChromeDriverManager.getInstance(CHROME).setup();
            ChromeOptions options = new ChromeOptions();
            if ("true".equalsIgnoreCase(SeleniumProps.getValue(SeleniumProps.RT_REMOTE_HEADLESS))) {
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--start-fullscreen");
            }
            base.Driver = new ChromeDriver(options);
        }
        initialiseDriver(url);
    }

    public void setUpRemote(String browser, String remoteUrl, String appUrl) {

        switch (browser) {
            case "IE":
                WebDriverManager.iedriver().setup();
//                InternetExplorerDriverManager.getInstance(IEXPLORER).setup();
                InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
                internetExplorerOptions.ignoreZoomSettings();
                internetExplorerOptions.introduceFlakinessByIgnoringSecurityDomains();
                internetExplorerOptions.requireWindowFocus();
                try {
                    base.Driver = new RemoteWebDriver(new URL(remoteUrl), internetExplorerOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "FIREFOX":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
               if ("true".equalsIgnoreCase(SeleniumProps.getValue(SeleniumProps.RT_REMOTE_HEADLESS))) {
                firefoxOptions.addArguments("--headless");
                firefoxOptions.addArguments("--disable-gpu");
                }
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
                try {
                    base.Driver = new RemoteWebDriver(new URL(remoteUrl), desiredCapabilities);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "CHROME":
            default:
                ChromeOptions options = new ChromeOptions();
                if ("true".equalsIgnoreCase(SeleniumProps.getValue(SeleniumProps.RT_REMOTE_HEADLESS))) {
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--start-maximized");
                }
                desiredCapabilities = DesiredCapabilities.chrome();
                desiredCapabilities.setVersion(System.getenv("BROWSER_VERSION"));
                try {
                    base.Driver = new RemoteWebDriver(new URL(remoteUrl), desiredCapabilities);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        initialiseDriver(appUrl);
    }

    public void initialiseDriver(String url) {
        base.Driver.get(url);
        base.Driver.manage().window().maximize();
        base.Driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        ReusableLibrary rs = new ReusableLibrary(base.Driver);
        rs.pageSync(base.Driver);
  //      rs.validateHttpPageError(url);
    }

    /**
     * @description: To be executed after every scenario block
     */


    @After(order = 0)
    public void tearDown() {
        base.Driver.quit();
    }

    @After(order = 1)
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            TakesScreenshot scrShot = ((TakesScreenshot) base.Driver);
            final byte[] screenshot = scrShot.getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
        }
    }
}
