package stepDef.Upgrade_steps;

import base.GenericFunctions;
import cucumber.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObject.Upgrade_page.Upgrade_Account_page;

public class Upgrade_Account_steps {
    TestContext testContext;
    Upgrade_Account_page Upgrade_Account;

    public Upgrade_Account_steps(TestContext context) {
        testContext = context;
        Upgrade_Account = context.getPageObjectManager().getUpgrade_Account_page();
    }
    @Given("the user navigates to the homepage")
    public void the_user_navigates_to_the_homepage() {
        GenericFunctions.hardWait(1000);
        Upgrade_Account.loginToLoginWebsite();
    }
    @When("the user creates a new profile")
    public void the_user_creates_a_new_profile() {
        Upgrade_Account.clickOnCreateAccount();
        Upgrade_Account.createAccount();
    }
    @When("the user enter valid username and password")
    public void the_user_enter_valid_username_and_password() {

    }




}