package com.example.common;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;

import com.example.utils.Configuration;
import com.example.utils.ExtentReportsUtil;
import com.example.utils.Logger;
import com.example.utils.Login_Configuration;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
    
    // CONSTANTS: -------------------------------------------------------

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public ITestResult result;
    public static String browser;
    public static String url;

    // CONSTRUCTOR: ------------------------------------------------------

    public BaseTest() {
        super();
    }

    public static void clearChromeDriver() {
        try {
            new ProcessBuilder("taskkill", "/F", "/IM", "chromedriver.exe", "/T").start();
            Logger.log("[LOG] ChromeDriver process has been killed");
        } catch (Exception e) {
            Logger.log("[LOG] Error in killing ChromeDriver process: " + e.getMessage());
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    public void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    private static void setupChromedriver() {
        try {
            WebDriverManager.chromedriver().setup();
        } catch (Exception e) {
            System.setProperty(Configuration.CHROME, Configuration.ROOTPATH + "\\driver\\chromedriver.exe");
            Logger.log(Configuration.ROOTPATH + "\\driver\\chromedriver.exe");
        }
    }

    public static void setupLaunch(boolean isHeadless) {
        startTime();
        setBrowser();
        setURL();
        switch (browser) {
            case "chrome":
                Logger.log("[LOG] Chromedriver is being launched");
                setupChromedriver();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("---remote-allow-origin=*");
                if (isHeadless) options.addArguments("--headless=new");
                options.addArguments("--window-size=1920, 1080");
                options.addArguments("--start-maximized");
                options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

                ChromeDriver chromeDriver = new ChromeDriver(options);
                setDriver(chromeDriver);
                BasePage.setDriver(chromeDriver);
                Capabilities cap = chromeDriver.getCapabilities();

                if (driver == null) throw new IllegalArgumentException("[LOG] Driver is null");

                Logger.log("[LOG] Browser: " + cap.getBrowserName() + " " + cap.getCapability("browserVersion"));
                Logger.log("[LOG] Accessing: " + url);
                driver.get().get(getURL());
                break;
            default:
                Logger.log("[LOG] Invalid browser");
        }
    }

    public static void resetupLaunch(boolean isHeadless) {
        setBrowser();
        setURL();
        switch (browser) {
            case "chrome":
                Logger.log("[LOG] Chromedriver is being launched");
                setupChromedriver();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("---remote-allow-origin=*");
                if (isHeadless) options.addArguments("--headless=new");
                options.addArguments("--window-size=1920, 1080");
                options.addArguments("--start-maximized");
                options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

                ChromeDriver chromeDriver = new ChromeDriver(options);
                setDriver(chromeDriver);
                BasePage.setDriver(chromeDriver);
                Capabilities cap = chromeDriver.getCapabilities();

                if (driver == null) throw new IllegalArgumentException("[LOG] Driver is null");

                Logger.log("[LOG] Browser: " + cap.getBrowserName() + " " + cap.getCapability("browserVersion"));
                Logger.log("[LOG] Accessing: " + url);
                driver.get().get(getURL());
                break;
            default:
                Logger.log("[LOG] Invalid browser");
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {
        ExtentReportsUtil.getExtentResult(result);
        Logger.log("Result Retrieved\n");
    }

    @AfterClass(alwaysRun = true)
    public Boolean endReports() {
        ExtentReportsUtil.endExtentReport();
        finishTime();
        Logger.log("End Report");
        return true;
    }

    @AfterTest
    public void endTest(ITestContext context) {
        Logger.logInfo("Test Results retrieved");
        StringBuilder summary = new StringBuilder();

        for (ITestResult result : context.getPassedTests().getAllResults()) {
            summary.append("PASSED: ").append(result.getName()).append("\n");
        }
        for (ITestResult result : context.getFailedTests().getAllResults()) {
            summary.append("FAILED: ").append(result.getName()).append("\n");
        }
        for (ITestResult result : context.getSkippedTests().getAllResults()) {
            summary.append("SKIPPED: ").append(result.getName()).append("\n");
        }
        Logger.log(summary.toString());
        Logger.log("End Test");
    }

    @AfterSuite
    public void sendEmail() {
        Logger.log("---End of Test---");
        ExtentReportsUtil.logsetp("---End of Test---");
    }

    // Login Methods
    public static void websiteLogin() {
        switch (Login_Configuration.ENVI) {
            case "STAGE":
                stageLogin();
                break;
            case "PROD":
                prodLogin();
                break;
            default:
                Logger.log("[LOG] Invalid environment");
        }
    }

    public static void stageLogin() {

    }

    public static void prodLogin() {

    }

    //
    private static ZonedDateTime START;
    private static ZonedDateTime FINISH;
    private static Duration timeElapsed;
    private static String timeDuration;
    private static ZoneId zone = ZoneId.of("Asia/Manila");

    private static void startTime() {
        START = ZonedDateTime.now(zone);
        Logger.log("Start Time: " + formatDate(START));
    }

    private static void finishTime() {
        FINISH = ZonedDateTime.now(zone);
        timeElapsed = Duration.between(START, FINISH);

        long hours = timeElapsed.toHours();
        long minutes = timeElapsed.toMinutesPart();
        long seconds = timeElapsed.toSecondsPart();

        timeDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        Logger.log("Finish Time: " + formatDate(FINISH));
        Logger.log("Total Time Execution Recorded: " + timeDuration);
        Logger.info("Total Time Execution Recorded: " + timeDuration);
    }

    private static String formatDate(ZonedDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a z");
        return dateTime.format(formatter);
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(new Date());
    }

    public static void setBrowser() {
        setBrowser(Login_Configuration.browser);
    }

    public static void setBrowser(String browser) {
        BaseTest.browser = browser;
        Logger.log("[LOG] Browser set to " + BaseTest.browser);
    }

    public static String getBrowser() {
        return BaseTest.browser;
    }

    public static void setURL() {
        url = Login_Configuration.URL();
        Logger.log("[LOG] URL set to " + BaseTest.url);
    }

    public static void setURL(String url) {
        BaseTest.url = url;
        Logger.log("[LOG] URL set to " + BaseTest.url);
    }

    public static String getURL() {
        return BaseTest.url;
    }
}