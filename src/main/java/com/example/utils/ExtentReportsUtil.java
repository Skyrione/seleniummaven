package com.example.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.example.common.BasePage;

public class ExtentReportsUtil implements ITestListener {

    public static ExtentSparkReporter htmlReporter;
    public static ExtentReports extent;
    public static ExtentTest test;

    public static void startExtentReport(String path) {
        htmlReporter = new ExtentSparkReporter(Configuration.ROOTPATH + path + ".html");
        Logger.log("Creating " + path + ".html");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        htmlReporter.config().setDocumentTitle("Automation Report");
        htmlReporter.config().setReportName("Automation Report");
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setTimeStampFormat("mm/dd/yyyy hh:mm:ss a");
    }

    public static void getExtentResult(ITestResult result) throws Exception {
        String screenshot = getScreenShot(BasePage.getDriver(), result.getName());

        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail("Entry Failed = " + result.getName(), MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
            Logger.log("Method Failed = " + result.getName());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Entry Skipped = " + result.getName());
            Logger.log("Method Skipped = " + result.getName());
        } else {
            test.pass("Entry Passed = " + result.getName(), MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
            Logger.log("Method Passed = " + result.getName());
        }
        htmlReporter.getReport();
    }
    
    public static void logsetp(String details) {
        test = extent.createTest(details);
        System.out.println("\n=============================================\n" + details + "===================================================");
    }

    public static void pass(String details) {
        test.log(Status.PASS, MarkupHelper.createLabel(details, ExtentColor.YELLOW));
    }

    public static void fail(String details) {
        test.log(Status.FAIL, MarkupHelper.createLabel(details, ExtentColor.RED));
    }

    public static void info(String details) {
        test.log(Status.INFO, MarkupHelper.createLabel(details, ExtentColor.AMBER));
    }

    public static void skip(String details) {
        test.log(Status.SKIP, MarkupHelper.createLabel(details, ExtentColor.BLUE));
    }

    public static void endExtentReport() {
        extent.flush();
        Logger.log("ExtentReports closed");
    }

    public static String getScreenShot(WebDriver driver, String screenshotname) throws Exception {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String destination = Configuration.ROOTPATH + "/screenshots/" + screenshotname + ".png";
            File finalDestination = new File(destination);
            FileUtils.copyFile(source, finalDestination);
            return destination;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}