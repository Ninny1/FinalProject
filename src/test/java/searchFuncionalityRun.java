import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Listeners(TestListener.class)

public class searchFuncionalityRun extends TestListener{

    @Test(priority = 1)
    public void navigateToURL() {
        driver.get("http://www.amazon.com");
    }

    @Test(priority = 2)
    public void languageVerifier() {
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
        //Define a list of search strings
        List<String> searchStrings = Arrays.asList("Apple Watch", "סמסונג", "12345", "????"); //send the different types of string
        //Iterate through the list and search for each string
        for (String searchString : searchStrings) {
            elements.searchBar.clear();
            elements.searchBar.sendKeys(searchString + Keys.ENTER); //send the string and press to enter
            Assert.assertFalse(elements.searchResultItems.isEmpty(), "No results found for search string: " + searchString);

            // Loop through each search result item (list) and check if the product name matches and contains the expected value
            for (WebElement searchResultItem : elements.searchResultItems) {
                String productName = searchResultItem.getText().toLowerCase();
                if (productName.contains(searchString)) {
                    // Assertion to check if the expected product name is displayed on the search results page
                    try {
                        //Assert.assertTrue(true, "Expected product name is displayed in the search results");

                        Assert.assertTrue(productName.contains(searchString.toLowerCase()));
                    } catch (AssertionError e) {
                        test.fail("Search string \"" + searchString + "\" not found in result: " + productName);
                        throw e;
                    }
                    test.pass("Search result is " + searchString);
                    break;
                }
            }
        }
    }


}
