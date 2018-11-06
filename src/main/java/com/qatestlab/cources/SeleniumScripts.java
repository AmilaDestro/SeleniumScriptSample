package com.qatestlab.cources;

import com.qatestlab.cources.webdrivers.WebDriverContainer;
import net.bndy.config.ConfigurationManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertEquals;

public class SeleniumScripts {
    private static final String ADMIN_PANEL_URL = ConfigurationManager
            .getConfiguration("application.properties", "link", String.class);
    private static final String USER = ConfigurationManager
            .getConfiguration("application.properties", "username", String.class);
    private static final String PASSWORD = ConfigurationManager
            .getConfiguration("application.properties", "password", String.class);

    private final WebDriver driver;

    public SeleniumScripts() {
        driver = WebDriverContainer.getFirefoxDriver();
    }

    public void authorizeAndQuit() {
        authorize();
        logout();

        wait(1500);
    }

    public void checkDashboard() {
        authorize();

        navigateToSubMenu("tab-AdminDashboard");
        navigateToSubMenu("subtab-AdminParentOrders");
        navigateToSubMenu("subtab-AdminCatalog");

        driver.quit();
    }

    private void navigateToSubMenu(final String elementId) {
        driver.findElement(By.id(elementId)).click();
        wait(2000);
        final String originalPageTitle = driver.getTitle();
        System.out.printf("Title of current page: %s\n", originalPageTitle);

        driver.navigate().refresh();
        wait(2000);
        assertEquals(driver.getTitle(), originalPageTitle, "Web page titles ae not equal after refresh");
    }

    private void authorize() {
        driver.navigate().to(ADMIN_PANEL_URL);
        driver.findElement(By.id("email")).sendKeys(USER);
        driver.findElement(By.id("passwd")).sendKeys(PASSWORD);
        driver.findElement(By.name("submitLogin")).click();
        wait(3000);
    }

    private void logout() {
        driver.findElement(By.id("header_employee_box")).click();
        wait(200);
        driver.findElement(By.id("header_logout")).click();
    }

    private void wait(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.err.printf("Exception was thrown: %s", e.getMessage());
        }
    }
}
