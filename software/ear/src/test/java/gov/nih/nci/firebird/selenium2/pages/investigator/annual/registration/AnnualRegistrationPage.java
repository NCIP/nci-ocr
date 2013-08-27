package gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration;

import gov.nih.nci.firebird.selenium2.pages.base.AbstractMenuPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

/**
 * /investigator/annual/registration/view_registration.jsp
 */
public class AnnualRegistrationPage extends AbstractMenuPage<AnnualRegistrationPage> {
    
    private static final String BACK_BUTTON_ID = "backButton";

    @FindBy(tagName = "h1")
    @CacheLookup
    private WebElement registrationHeader;

    @FindBy(id = BACK_BUTTON_ID)
    private WebElement backButton;

    private final AnnualRegistrationPageHelper helper;

    public AnnualRegistrationPage(WebDriver driver) {
        super(driver);
        this.helper = new AnnualRegistrationPageHelper(this);
    }

    public AnnualRegistrationPageHelper getHelper() {
        return helper;
    }

    public String getRegistrationHeader() {
        return registrationHeader.getText();
    }

    public SupplementalInvestigatorDataFormTab clickSupplementalInvestigatorDataFormTab() {
        clickTab(SupplementalInvestigatorDataFormTab.TAB_ID);
        return new SupplementalInvestigatorDataFormTab(getDriver(), this).waitUntilReady();
    }

    public FinancialDisclosureTab clickFinancialDisclosureTab() {
        clickTab(FinancialDisclosureTab.TAB_ID);
        return new FinancialDisclosureTab(getDriver(), this).waitUntilReady();
    }

    public OverviewTab clickOverviewTab() {
        clickTab(OverviewTab.TAB_ID);
        return new OverviewTab(getDriver(), this).waitUntilReady();
    }

    private void clickTab(String id) {
        pause(300);
        findElement(By.id(id)).findElement(By.tagName("a")).click();
    }

    public BrowseAnnualRegistrationsPage clickBack() {
        backButton.click();
        return new BrowseAnnualRegistrationsPage(getDriver()).waitUntilReady();
    }

    @Override
    protected void assertLoaded() {
        super.assertLoaded();
        assertPresent(By.id(OverviewTab.TAB_ID));
    }

    public AnnualForm1572Tab clickForm1572Tab() {
        clickTab(AnnualForm1572Tab.TAB_ID);
        return new AnnualForm1572Tab(getDriver(), this).waitUntilReady();
    }

    public AnnualAdditionalAttachmentsTab clickAdditionalAttachmentsTab() {
        clickTab(AnnualAdditionalAttachmentsTab.TAB_ID);
        return new AnnualAdditionalAttachmentsTab(getDriver(), this).waitUntilReady();
    }

}
