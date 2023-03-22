package stepDef;

import base.GenericFunctions;
import base.GlobalTestData;
import base.Setup;
import cucumber.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.util.Strings;


public class Hook {
    public static String url;
    public static String baseURL = System.getProperty("env");
    public static String browserType = System.getProperty("browser");
    Logger logger = LoggerFactory.getLogger(Hook.class);
    WebDriver driver;
    Setup setup;
    TestContext testContext;

    // Embed Request and Response in Cucumber JVM Report
    public static Scenario scenario;

    @Before
    public void before(Scenario scenario) {
        Hook.scenario = scenario;
    }

    public Hook() {}
    public Hook(TestContext context) {
        testContext = context;
    }

    public WebDriver getDriver() {
        initialize();
        initializeBrowserType();
        databaseConnection();
        return driver;
    }

    public void initialize() {
        setup = new Setup();
        if (Strings.isNullOrEmpty(browserType)) {
            browserType = "ch";
        }
        if (Strings.isNullOrEmpty(baseURL)) {
            baseURL = "stage_up";
        }
        driver = setup.initDriver(browserType);
        switch (baseURL) {
            case "stage_up":
            case "stageCI_up":
                url = "https://ultimateqa.com/automation";
                break;
        }
        logger.info("Test Started");
        driver.get(url);
    }

    public void initialize_data_API() {
        if (Strings.isNullOrEmpty(baseURL)) {
            baseURL = "prod_carSaver";
        }
        switch (baseURL) {
            case "stage_carSaver":
                GlobalTestData.carSaver_API_baseURL = "https://api.staging.carsaver.com";
                break;
            case "prod_carSaver":
                GlobalTestData.carSaver_API_baseURL = "https://api.carsaver.com";
                break;
        }
        logger.info("API Started");
    }
    public void initializeBrowserType() {
        switch (browserType) {
            case "android":
                GlobalTestData.ECOM_LOGIN_LINK_TEXT = "Log In";
                break;
            default:
                GlobalTestData.ECOM_LOGIN_LINK_TEXT = "Sign in here";
                break;
        }
        logger.info("Browser details loaded");
    }
    public void databaseConnection() {
//        String[] DbEnv = baseURL.split("_");
//        switch (DbEnv[0]) {
//            case "stage":
//                GlobalTestData.DB_DRIVER = "org.postgresql.Driver";
//                GlobalTestData.DB_USER = "admin";
//                GlobalTestData.DB_PWD = "admin";
//                GlobalTestData.DB_URL = "jdbc:postgresql://carsaver-beta-cluster.cluster-ctqmxtymvucy.us-east-1.rds.amazonaws.com:5432/carsaver_staging";
//                GlobalTestData.DB_URL_TUNNEL = "carsaver-beta-cluster.cluster-ctqmxtymvucy.us-east-1.rds.amazonaws.com";
//                GlobalTestData.DB_PORT = 5432;
//                break;
//            case "stageCI":
//                GlobalTestData.DB_DRIVER = "org.postgresql.Driver";
//                //GlobalTestData.DB_USER = "qa_automation";
//                //GlobalTestData.DB_PWD = "5bSD_j5Gki";
//                GlobalTestData.DB_USER = "admin";
//                GlobalTestData.DB_PWD = "admin";
//                GlobalTestData.DB_URL = "jdbc:postgresql://localhost:56715/carsaver_staging";
//                GlobalTestData.DB_URL_TUNNEL = "";
//                GlobalTestData.DB_PORT = 56715;
//                break;
//            case "qa":
//                GlobalTestData.DB_DRIVER = "";
//                GlobalTestData.DB_USER = "";
//                GlobalTestData.DB_PWD = "";
//                GlobalTestData.DB_URL = "";
//                GlobalTestData.DB_URL_TUNNEL = "";
//                GlobalTestData.DB_PORT = 0;
//                break;

    }
    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                final byte[] screenshot = ((TakesScreenshot) testContext.getPageObjectManager().getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName());
            }
        } finally {
            testContext.getPageObjectManager().getDriver().quit();
            GenericFunctions.hardWait(5000);
        }
    }
}