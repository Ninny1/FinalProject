/**
 * This class represents a Page Object Factory (POF) for the website. It provides methods for creating and returning instances of various Page Objects
 * for different pages on the website. The POF uses the Page Object Model (POM) design pattern to provide a clean separation between the test code
 * and the web page elements and actions.

 * The POF class uses the Selenium WebDriver API and TestNG framework to interact with the website and perform automated tests. It is designed to be
 * flexible and easily maintainable, with a clear hierarchy of Page Objects and reusable helper methods.

 * The class includes methods for initializing the WebDriver instance, loading the website, and creating instances of Page Objects for various pages
 * on the website, such as the homepage, search results page, product details page, cart page, and checkout page.

 * @author Nino Blitsman
 * @version 1.0
 * @since 08.04.2023
 */


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;


public class webElementsPOF {

    @FindBy(xpath = "//span[@class='nav-line-2']//div")
    public WebElement language;

    @FindBy(className = "icp-nav-link-inner")
    public WebElement languageDropdown;

    @FindBy(className = "icp-nav-link-inner")
    public List<WebElement> languageDropdownOptions;

    @FindBy(id = "twotabsearchtextbox")
    public WebElement searchBar;

    @FindBy(tagName = "h2")
    public List<WebElement> searchResultItems;


}
