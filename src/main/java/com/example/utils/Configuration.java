package com.example.utils;

public class Configuration {
    public static final int DEFAULT_WAIT = 15;

    public static final String CHROME = "webdriver.chrome.driver";
    public static final String IE = "webdriver.ie.driver";
    public static final String FIREFOX = "webdriver.gecko.driver";
    public static final String EDGE = "webdriver.edge.driver";

    public static final String ROOTPATH = System.getProperty("user.dir").replace("/", "\\");
    public static final String DATASHEET_PATH = ROOTPATH + "\\datasheet\\";
    public static final String DOWNLOAD_CSV_FILE = ROOTPATH + "\\src\\test\\resources\\";
}
