package managers;

import org.openqa.selenium.WebDriver;
import pageObject.*;
import pageObject.Upgrade_page.*;

public class PageObjectManager {

    private WebDriver driver;
    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }
    public WebDriver getDriver() {
        return driver;
    }
    private pageObject.Upgrade_page.Upgrade_Account_page Upgrade_Account_page;
    public Upgrade_Account_page getUpgrade_Account_page() {
        return (Upgrade_Account_page == null) ? Upgrade_Account_page= new Upgrade_Account_page(driver) : Upgrade_Account_page;
    }
}