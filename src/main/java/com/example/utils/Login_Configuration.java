package com.example.utils;

import com.example.common.BaseTest;

public class Login_Configuration extends BaseTest {
    
    public static String ENVI = "STAGE";
    public static final String BROWSER = "chrome";
    public static String TESTID;

    public static String STAGE_PASSWORD = "";
    public static String PROD_PASSWORD = "";

    public static final void LOGIN_WEBSITE(String testid, String envi, boolean isHeadless) {
        TESTID = testid;
        setupLaunch(isHeadless);
        websiteLogin();
    }

    public static void RELOGIN_WEBSITE(String testid, String envi, boolean isHeadless) throws Exception {
        Thread.sleep(1000);
        ENVI = envi;
        TESTID = testid;
        resetupLaunch(isHeadless);
        websiteLogin();
    }

    public static final String URL() {
        String url = null;

        switch (ENVI) {
            case "STAGE":
                // url here
                break;
            case "PROD":
                // url here
                break;
            default:
                return null;
        }
        return url;
    }

    public static final String STAGE_TEST_IDS() {
        String testid = null;
        switch (TESTID) {
            case "Admin":
                testid = "admin";
                break;
            default:
                return null;
        }
        return testid;
    }

    public static final String PROD_TEST_IDS() {
        String testid = null;
        switch (TESTID) {
            case "Admin":
                testid = "admin";
                break;
            default:
                return null;
        }
        return testid;
    }
}
