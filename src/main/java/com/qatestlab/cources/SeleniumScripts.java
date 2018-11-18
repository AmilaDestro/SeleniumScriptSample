package com.qatestlab.cources;

import com.qatestlab.cources.webdrivers.WebDriverContainer;
import net.bndy.config.ConfigurationManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class SeleniumScripts {
    private static final Logger LOG = LoggerFactory.getLogger(SeleniumScripts.class);
    private static final String ADMIN_PANEL_URL = ConfigurationManager
            .getConfiguration("application.properties", "link", String.class);
    private static final String USER = ConfigurationManager
            .getConfiguration("application.properties", "username", String.class);
    private static final String PASSWORD = ConfigurationManager
            .getConfiguration("application.properties", "password", String.class);

    private final WebDriver driver;
    private WebDriverWait wait;

    public SeleniumScripts() {
        driver = WebDriverContainer.getFirefoxDriver();
        wait = new WebDriverWait(driver, 5);
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
    public void goToCategoriesAndCreateNewOne() {
        authorize();

        final WebElement catalog = driver.findElement(By.cssSelector("#subtab-AdminCatalog"));
        LOG.info("Moving to element {}", catalog.getText());
        final Actions actions = new Actions(driver);
        actions.moveToElement(catalog).build().perform();

        final String categories = "категории";
        LOG.info("Waiting for presence of {} element then click on it", categories);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                String.format("//a[contains(text(),'%s')]", categories))))
                .click();

        LOG.info("Waiting for presence of button Добавить категорию then click on it");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".process-icon-new"))).click();

        final String newCategoryName = "test_northel";
        LOG.info("Waiting for presence of category creation menu and submitting name of new category: {}",
                newCategoryName);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains(text(),'Добавить')]")))
                .findElement(By.xpath("//input[@id='name_1']"))
                .sendKeys(newCategoryName);

        LOG.info("Saving new category");
        driver.findElement(By.cssSelector("#category_form_submit_btn")).click();

        LOG.info("Waiting for notification of successful category creation then filter");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class~='alert-success']")))
            .findElement(By.xpath("//input[@name='categoryFilter_name']"))
                .sendKeys(newCategoryName);
        driver.findElement(By.cssSelector("#submitFilterButtoncategory")).click();
        wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//td[contains(text(),'%s')]",
                                                                                    newCategoryName))));

        LOG.info("Clearing test data");
        driver.findElement(By.xpath("//input[@name='categoryBox[]']")).click();
        driver.findElements(By.cssSelector("button[class~='dropdown-toggle']"))
                .stream()
                .filter(element -> element.getText()
                        .equals("Групповые действия"))
                .findFirst()
                .get()
                .click();
        driver.findElement(By.linkText("Удалить выбранное")).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#deleteMode_delete"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("i[class~='icon-trash']"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class~='alert-success']"))).click();

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
