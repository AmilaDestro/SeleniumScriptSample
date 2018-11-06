package com.qatestlab.cources;

public class HomeTaskTwo {
    private static final SeleniumScripts script = new SeleniumScripts();

    public static void main(String[] args) {
        script.authorizeAndQuit();
        script.checkDashboard();
    }
}
