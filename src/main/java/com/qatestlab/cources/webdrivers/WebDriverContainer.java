package com.qatestlab.cources.webdrivers;

import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverContainer {

    public static FirefoxDriver getFirefoxDriver() {
        System.setProperty("webdriver.gecko.driver", "E:\\geckodriver.exe");
        return new FirefoxDriver();
    }
}
