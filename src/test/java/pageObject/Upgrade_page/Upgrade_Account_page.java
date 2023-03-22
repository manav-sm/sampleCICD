package pageObject.Upgrade_page;

import base.GenericFunctions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import java.time.Duration;
public class Upgrade_Account_page extends GenericFunctions {

    WebDriver driver;
    Actions actions;
    WebDriverWait wait;

    public Upgrade_Account_page (WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        actions = new Actions(this.driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    /** Page Locators **/
    @FindBy(how = How.XPATH, using = "//a[normalize-space()='Login automation']")
    public WebElement navigateToLoginBtn;
    @FindBy(how = How.XPATH, using = "//a[normalize-space()='Create a new account']")
    public WebElement createAccount;
    @FindBy(how = How.XPATH, using = "(//input[@id='user[first_name]'])[1]")
    public WebElement firstName;
    @FindBy(how = How.XPATH, using = "(//input[@id='user[last_name]'])[1]")
    public WebElement lastName;
    @FindBy(how = How.XPATH, using = "(//input[@id='user[email]'])[1]")
    public WebElement email;
    @FindBy(how = How.XPATH, using = "(//input[@id='user[password]'])[1]")
    public WebElement password;
    @FindBy(how = How.XPATH, using = "(//input[@id='user[terms]'])[1]")
    public WebElement clickOnCheckBox;
    @FindBy(how = How.XPATH, using = "//button[normalize-space()='Sign up']")
    public WebElement signUpBtn;

    /** Page Functions **/
    public void loginToLoginWebsite(){
        navigateToLoginBtn.click();
    }

    public void clickOnCreateAccount(){
        waitForPageToLoadCompletely();
        hardWait(4000);
        scrollDown(createAccount);
        waitForElementToBeClickable(createAccount);
        hardWait(2000);
        createAccount.click();
        hardWait(2000);
    }

    public void createAccount(){
        firstName.sendKeys("QSWED");
        lastName.sendKeys("aswdas");
        email.sendKeys("qwesa@gmail.com");
        password.sendKeys("123@edqwa");
        clickOnCheckBox.click();
        signUpBtn.click();
    }




}
