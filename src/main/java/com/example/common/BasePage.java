package com.example.common;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.utils.Configuration;

public class BasePage {
    
    protected final int DEFAULT_TIMEOUT = Configuration.DEFAULT_WAIT;
    protected static WebDriver driver;
    protected static WebDriverWait wait;

    public BasePage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(WebDriver driver) {
        BasePage.driver = driver;
    }
}
