package gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration;

import static org.junit.Assert.*;

import gov.nih.nci.firebird.commons.selenium2.support.IdentifiableComponentFactory;
import gov.nih.nci.firebird.commons.selenium2.util.FileDownloadUtils;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.selenium2.pages.components.CredentialsCvIdfDisplayComponent;
import gov.nih.nci.firebird.selenium2.pages.components.PersonCvIdfDisplayComponent;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

/**
 * investigator/annual/registration/ajax/idf/supplemental_data_form_tab.jsp
 */
public class SupplementalInvestigatorDataFormTab extends
        AbstractAnnualRegistrationTab<SupplementalInvestigatorDataFormTab> {

    static final String TAB_ID = "form_supplemental_investigator_data_form_tab";

    private static final String SECTION_BODY_CLASS = ".section_body";

    private static final String PRIMARY_ORG_NAME_CONTENT_CSS = "#primaryOrgNameSection " + SECTION_BODY_CLASS;
    private static final String PRIMARY_ORG_ADDRESS_CONTENT_CSS = "#primaryOrgAddressSection " + SECTION_BODY_CLASS;
    private static final String PRIMARY_ORG_CTEP_CONTENT_CSS = "#primaryOrgCtepIdSection " + SECTION_BODY_CLASS;
    private static final String PRIMARY_ORG_EMAIL_CONTENT_CSS = "#primaryOrgEmailSection " + SECTION_BODY_CLASS;
    private static final String PRIMARY_ORG_PHONE_CONTENT_CSS = "#primaryOrgPhoneNumberSection " + SECTION_BODY_CLASS;

    private static final String LOCKED_PDF_BUTTON_DIV_ID = "lockedViewIdfPdfButtonTopDiv";
    private static final String UNLOCKED_PDF_BUTTON_DIV_ID = "unlockedViewIdfPdfButtonTopDiv";
    private static final String VIEW_PDF_BUTTON_ID = "viewIdfPdfButtonTop";

    @FindBy(css = PRIMARY_ORG_NAME_CONTENT_CSS)
    @CacheLookup
    private WebElement primaryOrgNameContent;

    @FindBy(css = PRIMARY_ORG_ADDRESS_CONTENT_CSS)
    @CacheLookup
    private WebElement primaryOrgAddressContent;

    @FindBy(css = PRIMARY_ORG_CTEP_CONTENT_CSS)
    @CacheLookup
    private WebElement primaryOrgCtepIdContent;

    @FindBy(css = PRIMARY_ORG_EMAIL_CONTENT_CSS)
    @CacheLookup
    private WebElement primaryOrgEmailContent;

    @FindBy(css = PRIMARY_ORG_PHONE_CONTENT_CSS)
    @CacheLookup
    private WebElement primaryOrgPhoneContent;

    @FindBy(id = VIEW_PDF_BUTTON_ID)
    private WebElement viewPdfButton;


    private final SupplementalInvestigatorDataFormTabHelper helper;
    private final PersonCvIdfDisplayComponent personDisplayComponent;
    private final CredentialsCvIdfDisplayComponent credentialsDisplayComponent;

    public SupplementalInvestigatorDataFormTab(WebDriver driver, AnnualRegistrationPage page) {
        super(driver, page);

        helper = new SupplementalInvestigatorDataFormTabHelper(this);
        personDisplayComponent = new PersonCvIdfDisplayComponent(getDriver());
        credentialsDisplayComponent = new CredentialsCvIdfDisplayComponent(driver);
    }

    public String getPrimaryOrgName() {
        return primaryOrgNameContent.getText();
    }

    public String getPrimaryOrgAddress() {
        return primaryOrgAddressContent.getText();
    }

    public String getPrimaryOrgCtepId() {
        return primaryOrgCtepIdContent.getText();
    }

    public String getPrimaryOrgEmail() {
        return primaryOrgEmailContent.getText();
    }

    public String getPrimaryOrgPhone() {
        return primaryOrgPhoneContent.getText();
    }

    public File clickViewPdfButton() throws IOException {
        return FileDownloadUtils.clickDownloadLink(getDriver(), viewPdfButton).getFile();
    }

    PersonCvIdfDisplayComponent getPersonDisplayComponent() {
        return personDisplayComponent.waitUntilReady();
    }

    CredentialsCvIdfDisplayComponent getCredentialsDisplayComponent() {
        return credentialsDisplayComponent.waitUntilReady();
    }

    public SupplementalInvestigatorDataFormTabHelper getHelper() {
        return helper;
    }

    @Override
    protected String getTabId() {
        return TAB_ID;
    }

    public static IdentifiableComponentFactory<?> getFactory(AnnualRegistrationPage page) {
        return new Factory(page);
    }

    @Override
    public boolean isReadOnly() {
        return isPresent(By.id(LOCKED_PDF_BUTTON_DIV_ID));
    }

    @Override
    public FormTypeEnum getFormType() {
        return FormTypeEnum.SUPPLEMENTAL_INVESTIGATOR_DATA_FORM;
    }

    @Override
    protected void assertLoaded() {
        super.assertLoaded();
        assertTrue(isPresent(By.id(LOCKED_PDF_BUTTON_DIV_ID)) || isPresent(By.id(UNLOCKED_PDF_BUTTON_DIV_ID)));
        if (isPresent(By.id(UNLOCKED_PDF_BUTTON_DIV_ID))) {
            assertFindBysPresent();
            personDisplayComponent.assertLoaded();
        }
        assertElementWithIdPresent(VIEW_PDF_BUTTON_ID);
    }

    private static class Factory extends IdentifiableComponentFactory<SupplementalInvestigatorDataFormTab> {

        private final AnnualRegistrationPage page;

        public Factory(AnnualRegistrationPage page) {
            this.page = page;
        }

        @Override
        protected SupplementalInvestigatorDataFormTab getInstance(WebDriver driver) {
            return new SupplementalInvestigatorDataFormTab(driver, page);
        }

    }
}