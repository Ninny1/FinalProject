import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.annotations.*;

@Listeners(TestListener.class)

public class Main extends TestListener {

    @Test(priority = 1)
    public void navigateToURL() {
        driver.get("http://www.amazon.com");
    }

    @Test(priority = 2)
    public void languageVerifier(){
        try {
            Assert.assertEquals(elements.language.getText(), "EN");
        } catch (AssertionError e) {
            test.fail("English is not chosen");
            throw e;
        }
        test.pass("English is chosen");

        Actions action = new Actions(driver);
        //hoover over the language element
        action.moveToElement(elements.languageDropdown).build().perform();

        WebElement languageOption = elements.languageDropdownOptions.get(0);
        languageOption.click();
    }

    @Test(priority = 3)
    public void searchBarFunctionality() throws InterruptedException {
        String searchString = "Apple Watch";

        elements.searchBar.sendKeys(searchString + Keys.ENTER); //send the string and press to enter
        Thread.sleep(1000);
        // Loop through each search result item (list) and check if the product name matches and contains the expected value
        for (WebElement searchResultItem : elements.searchResultItems) {
            String productName = searchResultItem.findElement(By.cssSelector("h2 a span")).getText();
            if (productName.contains(searchString)) {
                // Assertion to check if the expected product name is displayed on the search results page
                try {
                    Assert.assertTrue(true, "Expected product name is displayed in the search results");
                } catch (AssertionError e) {
                    test.fail("Search result is not " + searchString);
                    throw e;
                }
                test.pass("Search result is " + searchString);
                break;
            }
        }
    }
}





