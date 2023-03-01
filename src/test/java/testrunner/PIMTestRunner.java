package testrunner;

import com.github.javafaker.Faker;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.PIMPage;
import setup.Setup;
import utils.Utils;
import java.io.IOException;


public class PIMTestRunner extends Setup {
    DashboardPage dashboardPage;
    LoginPage loginPage;
    PIMPage pimPage;

    @Test(priority = 1, description = "User cannot login with invalid creds")
    public void doLoginWithInvalidCreds() throws InterruptedException {
        loginPage = new LoginPage(driver);
        String message_actual = loginPage.doLoginWithInvalidCreds("admin", "wrong password");
        String message_expected = "Invalid credentials";
        org.testng.Assert.assertTrue(message_actual.contains(message_expected));
        Thread.sleep(1500);

    }
    @Test(priority = 2, description = "User can do login with valid Creds")
    public void doLogin() throws IOException, ParseException, InterruptedException {

        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        String username, password;
        JSONObject userObject = Utils.loadJSONFile("./src/test/resources/Admin.json");
        if (System.getProperty("username") != null && System.getProperty("password") != null) {
            username = System.getProperty("username");
            password = System.getProperty("password");
        } else {
            username = (String) userObject.get("username");
            password = (String) userObject.get("password");
        }
        loginPage.doLogin(username, password);

        // Assertion
        WebElement headerText = driver.findElement(By.tagName("h6"));
        String header_actual = headerText.getText();
        String header_expected = "Dashboard";
        Assert.assertEquals(header_actual, header_expected);
        Thread.sleep(1500);
        dashboardPage.menus.get(1).click();
        Thread.sleep(1500);
    }

    @Test(priority = 3, description = "Doesn't create employee without Username")
    public void createEmployeeWithoutUsername() throws InterruptedException, IOException, ParseException {
        pimPage = new PIMPage(driver);
        Thread.sleep(1500);
        pimPage.button.get(1).click();
        Faker faker = new Faker();
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        int empId = Utils.generateRandomNumber(10000, 99999);
        String employeeId = String.valueOf(empId);
        String password = "Siddik@234";
        Thread.sleep(1500);
        pimPage.createEmployeeWithoutUsername(firstname, lastname, employeeId, password);

        // Assertion
        String header_actual = driver.findElements(By.className("oxd-text")).get(15).getText();
        String header_expected = "Required";
        Assert.assertTrue(header_actual.contains(header_expected));
        driver.navigate().refresh();

    }

    @Test(priority = 4, description = "Create first employee")
    public void createEmployee1() throws InterruptedException, IOException, ParseException {
        pimPage = new PIMPage(driver);
        Faker faker = new Faker();
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        int empId = Utils.generateRandomNumber(10000, 99999);
        String employeeId = String.valueOf(empId);
        String username = "Siddik" + Utils.generateRandomNumber(1000, 9999);
        String password = "Siddik@234";
        Thread.sleep(1500);
        pimPage.createEmployee(firstname, lastname, employeeId, username, password);

        // Assertion
        String header_actual = driver.findElements(By.className("orangehrm-main-title")).get(0).getText();
        String header_expected = "Personal Details";
        if ((header_actual.contains(header_expected))) {
            Utils.addJsonArray(firstname, lastname, employeeId, username, password);
        }
        driver.findElements(By.className("oxd-topbar-body-nav-tab-item")).get(2).click();
        Thread.sleep(1500);

    }

    @Test(priority = 5, description = "Create second employee")
    public void createEmployee2() throws InterruptedException, IOException, ParseException {
        pimPage = new PIMPage(driver);
        Faker faker = new Faker();
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        int empId = Utils.generateRandomNumber(10000, 99999);
        String employeeId = String.valueOf(empId);
        String username = "Siddik" + Utils.generateRandomNumber(1000, 9999);
        String password = "Siddik@234";
        Thread.sleep(1500);
        pimPage.createEmployee(firstname, lastname, employeeId, username, password);

        // Assertion
        String header_actual = driver.findElements(By.className("orangehrm-main-title")).get(0).getText();
        String header_expected = "Personal Details";
        if ((header_actual.contains(header_expected))) {

            Utils.addJsonArray(firstname, lastname, employeeId, username, password);
        }
        Thread.sleep(1000);
    }

    @Test(priority = 6, description = "Searching With Invalid Employee's Name")
    public void searchEmployeeByInvalidName() throws InterruptedException {
        pimPage = new PIMPage(driver);
        dashboardPage = new DashboardPage(driver);
        dashboardPage.menus.get(1).click();
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String employeeName = name;
        pimPage.SearchEmployeeByInvalidName(employeeName);
        Thread.sleep(1500);

        //Assertion
        String message_actual = driver.findElements(By.className("oxd-text--span")).get(11).getText();
        String message_expected = "No Records Found";
        Assert.assertEquals(message_expected, message_actual);
        Thread.sleep(1000);

    }

    @Test(priority = 7, description = "Searching with Valid Employee's name")
    public void searchEmployeeByName() throws IOException, ParseException, InterruptedException {
        loginPage = new LoginPage(driver);
        JSONObject userObject = Utils.loadJSONFileContainingArray("./src/test/resources/Employee.json", 0);
        String employeeFirstName = userObject.get("firstname").toString();
        String employeeLastName=userObject.get("lastname").toString();
        String employeeName=employeeFirstName + " " + employeeLastName;

        pimPage.txtSearchEmpName.get(1).sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        pimPage.SearchEmployeeByValidName(employeeName);
        Thread.sleep(1500);

        //Assertion

        String message_actual = driver.findElements(By.className("oxd-text--span")).get(11).getText();
        String message_expected = "Record Found";
        Assert.assertTrue(message_actual.contains(message_expected));
        Thread.sleep(1000);
    }
    @Test(priority = 8, description = "Update user Id by Random Id")
    public void updateEmployeeById() throws InterruptedException, IOException, ParseException {
        pimPage = new PIMPage(driver);
        int empId = Utils.generateRandomNumber(10000, 99999);
        String randomEmployeeId = String.valueOf(empId);
        Utils.updateJSONObject("./src/test/resources/Employee.json", "employeeId", randomEmployeeId,0 );
        Utils.doScroll(driver,300);
        pimPage.updateEmployeeById(randomEmployeeId);
        Thread.sleep(1500);

        // Assertion
        String header_actual = driver.findElements(By.className("orangehrm-main-title")).get(0).getText();
        String header_expected = "Personal Details";
        Assert.assertTrue(header_actual.contains(header_expected));
    }
    @Test(priority = 9, description = "Search by updated Employee Id")
    public void searchEmployeeById() throws IOException, ParseException, InterruptedException {
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        dashboardPage.menus.get(1).click();
        JSONObject userObject = Utils.loadJSONFileContainingArray("./src/test/resources/Employee.json", 0);
        String employeeId = userObject.get("employeeId").toString();
        pimPage.SearchEmployeeByValidId(employeeId);
        Thread.sleep(1500);
        Utils.doScroll(driver,500);

        //Assertion
        String message_actual = driver.findElements(By.className("oxd-text--span")).get(11).getText();
        String message_expected = "Record Found";
        Assert.assertTrue(message_actual.contains(message_expected));
        Thread.sleep(1000);

    }
    @Test(priority = 10,description = "Admin Logout Successfully")
    public void logOut() {
        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.doLogout();
        driver.close();
    }
}