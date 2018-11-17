package com.qatestlab.cources;

public class Main {
    private static final SeleniumScripts script = new SeleniumScripts();

    public static void main(String[] args) {
        // Home task 2
//        script.authorizeAndQuit();
//        script.checkDashboard();

        //Home task 3
        script.goToCategoriesAndCreateNewOne();
    }
}
