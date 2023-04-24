/**
 * TestListener class implements TestNG's ITestListener interface, which provides a way to listen to events occurring during the test execution.
 * This class contains methods that are called when specific events occur in the test, such as before and after the test methods, before and after the test suite, and on test failure.
 *
 * Overall, the TestListener class provides a powerful way to customize and extend the behavior of TestNG, making it a valuable tool for any automated testing project.
 */

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import javax.mail.MessagingException;
import java.time.Duration;


public class TestListener implements ITestListener {
    static ExtentTest test;
    static ExtentReports extent;
    static WebDriver driver;
    static webElementsPOF elements = new webElementsPOF();


    @BeforeSuite
    public void setUp() {
        // initialize extent report
        extent = ExtentManager.GetExtent();

        System.setProperty("webdriver.chrome.driver","C:\\webdriver\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        elements = PageFactory.initElements(driver, webElementsPOF.class);
    }

    @AfterSuite
    public void afterSuite() {
        // flush extent report
        extent.flush();
    }

    @BeforeTest
    public void beforeTest() {
        // Code to be executed before each test
        elements = PageFactory.initElements(driver, webElementsPOF.class);
    }

    @AfterTest
    public void afterTest() throws InterruptedException {
        // Code to be executed after each test
        Thread.sleep(2000);
    }

    @Override
    public void onTestStart(ITestResult result) {
        test = ExtentManager.extent.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, "Test failed with exception: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        test.log(Status.SKIP, "Test skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        test.log(Status.FAIL, "Test failed but within success percentage: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {

        System.out.println("Test execution started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //driver.quit();

        System.out.println("Test execution finished: " + context.getName());
        System.out.println("Total number of tests passed: " + context.getPassedTests().size());
        System.out.println("Total number of tests failed: " + context.getFailedTests().size());
        System.out.println("Total number of tests skipped: " + context.getSkippedTests().size());

        /*String[] recipients = { "recipient1@gmail.com", "recipient2@gmail.com" };
        String recipientList = String.join(",", recipients);
        String subject = "Extent Report";
        String messageBody = "Please find the attached report";
        String filePath = "D:\\"+ ExtentManager.reportDate+"\\exReport.html";
        String sender = "sender@gmail.com";
        String senderPassword = "sender_password";

        GmailService service = new GmailService(sender, senderPassword);
        try {
            GmailService.sendEmailWithAttachment(recipientList, subject, messageBody, filePath);
            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            System.err.println("Email send failed");
            throw new RuntimeException(e);

        }

         */

    }
}