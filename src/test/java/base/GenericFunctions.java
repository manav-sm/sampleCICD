package base;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static stepDef.Hook.scenario;

public class GenericFunctions extends Setup {

    protected static WebDriver driver;
    Actions actions;
    static WebDriverWait wait;

    public GenericFunctions(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**Commonly used web elements**/
    public static WebElement h1Tag(){ return driver.findElement(By.xpath("//h1")); }
    public static WebElement h1TagByText(String h1Text) { return driver.findElement(By.xpath("//h1[normalize-space()='"+h1Text+"']")); }
    public static WebElement h2TagByText(String h2Text) { return driver.findElement(By.xpath("//h2[normalize-space()='"+h2Text+"']")); }
    public static WebElement h5Tag(){ return driver.findElement(By.xpath("//h5")); }
    public static WebElement h5TagByText(String h5Text) { return driver.findElement(By.xpath("//h5[normalize-space()='"+h5Text+"']")); }
    public static WebElement submitButton() { return driver.findElement(By.xpath("//button[@type='submit']")); }
    public static WebElement buttonByText(String buttonText) { return driver.findElement(By.xpath("//button[normalize-space()='"+buttonText+"']")); }
    public static WebElement linkByText(String linkText) { return driver.findElement(By.xpath("//a[normalize-space()='"+linkText+"']")); }

    /** Return web element containing QA attribute **/
    public static WebElement qaXpathToWebElement(String qa) { return driver.findElement(By.xpath("//*[@qa-data='+"+qa+"']")); }

    /* Waits Functions */
    public static void waitForElementToBeVisible(WebElement element) {
        waitForElementToBeVisible(element,20);
    }
    public static void waitForElementToBeVisible(WebElement element,int time) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForElementToBeInVisible(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }
    public static void waitForElementToBeInVisible(WebElement element,int time) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public static void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean isElementPresent(WebElement webE) {
        try {
            webE.isDisplayed();
            return true;
        } catch (NoSuchElementException e){
            return false;
        }
    }

    public static void waitForPageToLoadCompletely() {
        JavascriptExecutor j = (JavascriptExecutor) driver;
        if (j.executeScript("return document.readyState").toString().equals("complete")) {
            System.out.println("Page has loaded");
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*")));
    }
    //End of wait functions

    public static void refreshFailedPage (WebElement element) {
        int i = 0;
        String ex;
        do{
            ex = null;
            try{
                waitForElementToBeVisible(element,120);
            }catch (Exception e){
                ex = e.toString();
                i++;
                scenario.log("Web element "+element.toString()+" failed "+i+" time(s).");
                refreshPage();
            }
        }while(ex != null && i<5);
        if(i>4 && ex != null){
            scenario.log("Web element "+ element +" failed to open after "+i+" tries.");
            Assert.fail();
        }
    }

    public void click(WebElement element) {
        element.click();
    }
    public static String pageTitle(){
         return driver.getTitle();
    }

    public static void openUrl(String url) {
        driver.get(url);
    }

    public static String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public static boolean verifyDateFormat(String format, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void hardWait(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return Integer.toString(cal.get(Calendar.YEAR));
    }

    public static String fullDate(String date) {
            //date needs to be MM/DD/YYYY or MM/DD/YY
            String[] partialDate = date.split("/");
            String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            String monthName = monthNames[Integer.parseInt(partialDate[0])-1];

            String dayName;
        switch (partialDate[1]) {
            case "1":
            case "21":
            case "31":
                dayName = partialDate[1] + "st";
                break;
            case "2":
            case "22":
                dayName = partialDate[1] + "nd";
                break;
            case "3":
            case "23":
                dayName = partialDate[1] + "rd";
                break;
            default:
                dayName = partialDate[1] + "th";
                break;
        }

            String year = partialDate[2];
            if (partialDate[2].length() == 2){
                year = "20"+partialDate[2];
            }

            return monthName+" "+dayName+" "+year;
    }

    public static String getDate(int future) {
        //Use 0 to get today's date
        DateFormat formatter = new SimpleDateFormat("MM");
        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
        DateFormat dateFormatter = new SimpleDateFormat("DD");
        SimpleDateFormat dateParse = new SimpleDateFormat("DD");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,future);
        String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String date = Integer.toString(cal.get(Calendar.DATE));
        try {
            month = formatter.format(monthParse.parse(month));
            date = dateFormatter.format(dateParse.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String year = Integer.toString(cal.get(Calendar.YEAR));
        return month + "/" + date + "/" + year;
    }

    public static String getYearMonthDayDate(int future) {
        //Use 0 to get today's date
        DateFormat formatter = new SimpleDateFormat("MM");
        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
        DateFormat dateFormatter = new SimpleDateFormat("DD");
        SimpleDateFormat dateParse = new SimpleDateFormat("DD");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,future);
        String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String date = Integer.toString(cal.get(Calendar.DATE));
        try {
            month = formatter.format(monthParse.parse(month));
            date = dateFormatter.format(dateParse.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String year = Integer.toString(cal.get(Calendar.YEAR));
        return year + "-" + month + "-" + date;
    }

    public static String getDashedDateDayMonthYear(int future) {
        //Use 0 to get today's date
        DateFormat formatter = new SimpleDateFormat("MMM");
        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
        DateFormat dateFormatter = new SimpleDateFormat("DD");
        SimpleDateFormat dateParse = new SimpleDateFormat("DD");
        DateFormat yearFormatter = new SimpleDateFormat("yy");
        SimpleDateFormat yearParse = new SimpleDateFormat("yy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,future);
        String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String date = Integer.toString(cal.get(Calendar.DATE));
        String year = Integer.toString(cal.get(Calendar.YEAR));
        try {
            month = formatter.format(monthParse.parse(month));
            date = dateFormatter.format(dateParse.parse(date));
            year = yearFormatter.format(yearParse.parse(year));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date + "-" + month + "-" + year;
    }

    public static String getDayOfWeek(int future) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,future);
        DateFormat formatter = new SimpleDateFormat("EEEE");

        return formatter.format(cal.getTime());
    }

    public static String phoneFormat(String phone) {

        String[] phoneSplit = new String[3];
        phoneSplit[0] = phone.substring(0,3);
        phoneSplit[1] = phone.substring(3,6);
        phoneSplit[2] = phone.substring(6);

        return "("+phoneSplit[0]+") "+phoneSplit[1]+"-"+phoneSplit[2];
    }

    public void sendKeysUsingAction(WebElement el, String text) {
        Actions actions = new Actions(driver);
        actions.moveToElement(el).click().sendKeys(text).build().perform();
    }


    public void scrollDown(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public String getTextUsingJS(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        try {
            return executor.executeScript("return arguments[0].innerText;", element).toString();
        } catch (StaleElementReferenceException sre) {
            System.out.println("Stale Exception handled.");
            hardWait(1000);
            return null;
        }
    }

    public static void jsClick(WebElement ele) {
        hardWait(3000);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", ele);
    }

    public void clickUsingActions(WebElement element) {
        try {
            actions.moveToElement(element).click().build().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mouseHoverUsingActions(WebElement element) {
        try {
            actions.moveToElement(element).build().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAnyKey(Keys key) {
        Actions actions = new Actions(driver);
        actions.sendKeys(key).build().perform();
    }

//    public static JSONObject extractValueFromJSONArray(JSONArray jsonArray){
//        JSONObject tempObj = null;
//        for(int index = 0;index<jsonArray.length();index++) {
//            tempObj = jsonArray.getJSONObject(index);
//        }
//        return tempObj;
//    }

    public static void clickButtonByText(String buttonText){
        WebElement neConfirmAddressModalSelect = driver.findElement(By.xpath("//button[normalize-space()='"+buttonText+"']"));
        waitForElementToBeClickable(neConfirmAddressModalSelect);
        neConfirmAddressModalSelect.click();
    }
    public static void clickButtonsWithSameText(String buttonText,String buttonNumber){
        WebElement neConfirmAddressModalSelect = driver.findElement(By.xpath("(//button[normalize-space()='"+buttonText+"'])["+buttonNumber+"]"));
        waitForElementToBeClickable(neConfirmAddressModalSelect);
        neConfirmAddressModalSelect.click();
    }

    public static void clickLinkByText(String linkText){
        WebElement webLink = driver.findElement(By.xpath("//*[normalize-space()='"+linkText+"']"));
        waitForElementToBeClickable(webLink);
        webLink.click();
    }

    /**Database Calls**/
    public static void dbCount(String table, String where) {

        if(!where.trim().equals("") && !where.toLowerCase().trim().startsWith("where")){
            where = "where "+where.trim();
        }

        try {
            GlobalTestData.VEHICLE_INFO = DbManager.getQuery(1,1,"select count(*) from "+table+" "+where);
        } catch (SQLException ex) {
            scenario.log("Cannot make a SQL call: "+ex);
        }
        GlobalTestData.SQL_ROW = Integer.parseInt(GlobalTestData.SQL_QUERY[0][0]);
    }
    public static void dbSql(int row, int col, String sql) {
        try {
            GlobalTestData.SQL_QUERY = DbManager.getQuery(row,col,sql);
        } catch (SQLException ex) {
            scenario.log("Problem with SQL call: "+ex);
        }
    }
    public static void refreshPage(){
        driver.navigate().refresh();
    }
    public static void backPage(){
        driver.navigate().back();
    }

    /** make zip file of reports **/
    public static void zip (String filepath) {
        Date d = new Date();
        try {
            File inFolder = new File(filepath);
            File outFolder = new File("cucumber-reports-for_" + d.toString().replace(":", "_").replace(" ", "-") + ".zip");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream("src/test/java/report/cuke-archive/"+outFolder)));
            BufferedInputStream in;
            byte[] data = new byte[5000];
            String[] files = inFolder.list();
            for (int i = 0; i < files.length; i++) {
                if(!files[i].equals("archive")){
                    in = new BufferedInputStream(new FileInputStream(inFolder.getPath() + "/" + files[i]), 5000);
                    out.putNextEntry(new ZipEntry(files[i]));
                    int count;
                    while ((count = in.read(data, 0, 5000)) != -1) {
                        out.write(data, 0, count);
                    }
                    out.closeEntry();
                }
            }
            out.flush();
            out.close();
            System.out.println("Finished zipping extent report files to archive folder.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to zip files: "+e);
            scenario.log("Failed to zip files:"+e);
        }
    }

}