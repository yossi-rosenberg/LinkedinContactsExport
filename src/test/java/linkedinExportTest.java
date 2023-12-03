import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dataTypes.UserDetails;


import java.util.List;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class linkedinExportTest {

    final private String linkedinHomeURL = "https://www.linkedin.com/home";
    final private int numOfThreads = 8;
    private WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver,ofMillis(5000));
    UserDetails userDetails;
    private String linkedinTitle = "LinkedIn: Log In or Sign Up";
    private String userNameLcator = "session_key";
    private String userName = "<Your_UserName>";
    private String passwordLocator = "session_password";
    private String password = "User_password";
    private String submitLocator = "//button[contains(text(),'Sign in')]";
    private String meLocator = "//html/body/div[5]/header/div/nav/ul/li[6]/div/button";
    private String viewProfileLocator ="//*[text()='View Profile']";
    private String userProfileNameLocator = "//*[starts-with(@class, 'text-heading-xlarge')]";
    private String userProfilePOWLocator = "//*[starts-with(@class, 'text-body-medium')]";
    private String userProfileLocationLocator = "//*[starts-with(@class, 'text-body-small')]";
    private String ConnectionsLocator = "span[class='link-without-visited-state']";
    private String contactsLocator = "//*[starts-with(@class, 'mn-connection-card__name')]";
    private String powLocator = "//*[starts-with(@class, 'mn-connection-card__occupation')]";

    private String showMoreResultsLocator = "//*[text()='Show more results']";
    @Test
    public void exportContactsTest() throws InterruptedException {
        driver.manage().window().maximize();
        driver.get(linkedinHomeURL);

        String title = driver.getTitle();
        assertEquals(linkedinTitle, title);

        WebElement userNameElement = driver.findElement(By.name(userNameLcator));
        userNameElement.sendKeys(userName);
        WebElement textBox = driver.findElement(By.name(passwordLocator));
        textBox.sendKeys(password);
        WebElement submitButton = driver.findElement(By.xpath(submitLocator));
        submitButton.click();
        wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath(meLocator)));
        WebElement meButton = driver.findElement(By.xpath(meLocator));
        meButton.click();
        wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath(viewProfileLocator)));
        WebElement viewProfileButton = driver.findElement(By.xpath(viewProfileLocator));
        viewProfileButton.click();
        wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(userProfileNameLocator)));
        userDetails = new UserDetails(driver.findElement(By.xpath(userProfileNameLocator)).getText().toString(),
                driver.findElement(By.xpath(userProfilePOWLocator)).getText().toString(),
                driver.findElements(By.xpath(userProfileLocationLocator)).get(0).getText().toString());
        WebElement myNetworkButton = driver.findElement(By.cssSelector(ConnectionsLocator));
        myNetworkButton.click();
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath(contactsLocator)));
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        int currContactsNameSize = 0;
        List<WebElement> contactsName = driver.findElements(By.xpath(contactsLocator));
        while(!(contactsName.size() == currContactsNameSize && driver.findElements(By.xpath(showMoreResultsLocator)).size() == 0)) {
        //while (currLastContact.getText() != contactsName.get(contactsName.size() -1).getText()) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(2000);
            currContactsNameSize = contactsName.size();
            contactsName = driver.findElements(By.xpath(contactsLocator));
            System.out.println("listSize " + contactsName.size());
            try {
                if(driver.findElements(By.xpath(showMoreResultsLocator)).size() > 0) {
                    driver.findElement(By.xpath(showMoreResultsLocator)).click();
                    System.out.println("clicked!");
                    Thread.sleep(1000); //Wait for DOM to retrieve show more results locator again
                }
            }
            catch (StaleElementReferenceException ex) {

            }
        }
        List<WebElement> currentLastPlaceOfWorkList = driver.findElements(By.xpath(powLocator));
        JSONObject exportedJson = null;
        try {
            exportedJson = JsonUtils.parseContactsJson(contactsName, currentLastPlaceOfWorkList, userDetails);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        JsonUtils.writeJsonObjectToFile(exportedJson);
    }
}