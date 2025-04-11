package com.example.utils;

public class Logger extends ExtentReportsUtil {
    
    public static void log(String logs) {
        System.out.println(logs);
    }

    public static void logInfo(String logs) {
        log("------------------------------------------------------------\n" + logs + "\n------------------------------------------------------------");
        info(logs);
    }
}
