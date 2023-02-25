package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class DashboardPage {
    @FindBy(className = "oxd-userdropdown-tab")
    public WebElement btnProfileTab;
    @FindBy(partialLinkText = "Logout")
    public WebElement logOutLink;
    @FindBy(className = "oxd-main-menu-item--name")
    public List<WebElement> menus;

    public DashboardPage(WebDriver driver) {

        PageFactory.initElements(driver, this);
    }

    public void doLogout() {
        btnProfileTab.click();
        logOutLink.click();
    }
}
