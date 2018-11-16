package com.qatestlab.cources;

import com.qatestlab.cources.webdrivers.WebDriverContainer;
import net.bndy.config.ConfigurationManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

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

    /**
     * Home task 2, part 1
     */
    public void authorizeAndQuit() {
        authorize();
        logout();

        wait(1500);
    }

    /**
     * Home task 2, part 2
     */
    public void checkDashboard() {
        authorize();

        final List<String> sections = Arrays.asList("Dashboard",
                "Заказы",
                "Каталог",
                "Клиенты",
                "Служба поддержки",
                "Статистика",
                "Modules",
                "Design",
                "Доставка",
                "Способ оплаты",
                "International",
                "Shop Parameters",
                "Конфигурация");

        sections.forEach(this::navigateToSubMenu);

        driver.quit();
    }

    /**
     * Home task 3
     */
    public void goToCategories() {
        authorize();

        final WebElement catalog = driver.findElement(By.cssSelector("#subtab-AdminCatalog"));

        final Actions actions = new Actions(driver);
        actions.moveToElement(catalog).build().perform();

        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'категории')]"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".process-icon-new"))).click();

        final String myCategoryName = "test_northel";
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains(text(),'Добавить')]")))
                .findElement(By.xpath("//input[@id='name_1']"))
                .sendKeys(myCategoryName);
        driver.findElement(By.cssSelector("#category_form_submit_btn")).click();

//        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".alert alert-success"))))
//            .findElement(By.xpath("//input[@name='categoryFilter_name']"))
//                .sendKeys(myCategoryName);
//        driver.findElement(By.cssSelector("#submitFilterButtoncategory")).click();
//        wait.until(
//                ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//td[contains(text(),'%s')]",
//                                                                                    myCategoryName))));
        driver.quit();
    }

    private void navigateToSubMenu(final String elementText) {
        driver.findElement(By.linkText(elementText)).click();
        wait(3000);
        final String originalPageTitle = driver.getTitle();
        System.out.printf("Title of current page: %s\n", originalPageTitle);

        driver.navigate().refresh();
        wait(3000);
        assertEquals(driver.getTitle(), originalPageTitle, "Web page titles are not equal after refresh");
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
