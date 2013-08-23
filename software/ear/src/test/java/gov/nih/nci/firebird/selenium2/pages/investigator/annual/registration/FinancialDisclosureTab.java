package gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration;

import static org.apache.commons.lang3.StringUtils.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.support.IdentifiableComponentFactory;
import gov.nih.nci.firebird.commons.selenium2.util.FileDownloadUtils;
import gov.nih.nci.firebird.commons.selenium2.util.JQueryUtils;
import gov.nih.nci.firebird.commons.selenium2.util.TableUtils;
import gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.selenium2.pages.base.ConfirmDialog;
import gov.nih.nci.firebird.selenium2.pages.components.tags.SupportingDocumentsTag;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Function;

/**
 * investigator/annual/registration/ajax/financialdisclosure/financial_disclosure_tab.jsp
 */
public class FinancialDisclosureTab extends AbstractAnnualRegistrationTab<FinancialDisclosureTab> {

    static final String TAB_ID = "form_fdf_tab";
    private static final String TAB_UNIQUE_LOCATOR_CLASS = "ctepFinancialDisclosure";
    private static final String LOCKED_PDF_BUTTON_DIV_ID = "lockedViewFinancialDisclosurePdfButtonDiv";
    private static final String UNLOCKED_PDF_BUTTON_DIV_ID = "unlockedViewFinancialDisclosurePdfButtonDiv";
    private static final String REQUIRED_PHARMACEUTICAL_COMPANY_ASTERISK_ID = "requiredPharmaceuticalCompanyAsterisk";
    private static final String VIEW_PDF_BUTTON_ID = "viewFinancialDisclosurePdfButtonTop";
    private static final String ADD_PHARMACEUTICAL_COMPANY_BUTTON_ID = "addPharmaceuticalCompany";
    private static final String PHARMACEUTICAL_COMPANIES_TABLE_ID = "pharmaceuticalCompaniesTable";

    public enum Question {
        Q1_MONETARY_GAIN("registration_financialDisclosure_monetaryGain", "monetaryGainAsterisk"),
        Q2_OTHER_SPONSOR_PAYMENTS("registration_financialDisclosure_otherSponsorPayments", "otherSponsorPaymentsAsterisk"),
        Q3_FINANCIAL_INTEREST("registration_financialDisclosure_financialInterest", "financialInterestAsterisk"),
        Q4_EQUITY_IN_SPONSOR("registration_financialDisclosure_equityInSponsor", "equityInSponsorAsterisk");

        String questionId;
        String asteriskId;

        Question(String questionId, String asteriskId) {
            this.questionId = questionId;
            this.asteriskId = asteriskId;
        }
    }

    @FindBy(id = ADD_PHARMACEUTICAL_COMPANY_BUTTON_ID)
    private WebElement addPharmaceuticalCompanyButton;
    @FindBy(id = PHARMACEUTICAL_COMPANIES_TABLE_ID)
    private WebElement pharmaceuticalCompaniesTable;
    @FindBy(id = VIEW_PDF_BUTTON_ID)
    private WebElement viewPdfButton;
    private final SupportingDocumentsTag supportingDocumentsTag;
    private FinancialDisclosureTabHelper helper;

    public FinancialDisclosureTab(WebDriver driver, AnnualRegistrationPage page) {
        super(driver, page);
        this.helper = new FinancialDisclosureTabHelper(this);
        this.supportingDocumentsTag = new SupportingDocumentsTag(getDriver(), this);
    }

    public void answerQuestion(Question question, boolean answer) {
        getDriver().findElement(By.id(question.questionId + answer)).click();
        this.waitUntilReady();
    }

    public Boolean getAnswer(Question question) {
        boolean yesRadio = getDriver().findElement(By.id(question.questionId + true)).isSelected();
        boolean noRadio = getDriver().findElement(By.id(question.questionId + false)).isSelected();
        if (!yesRadio && !noRadio) {
            return null;
        } else {
            return yesRadio;
        }
    }

    public boolean isAsteriskDisplayed(Question question) {
        return getElementIfPresent(getDriver(), By.id(question.asteriskId)) != null;
    }

    public boolean isRequiredPharmaceuticalCompanyAsteriskDisplayed() {
        return getElementIfPresent(getDriver(), By.id(REQUIRED_PHARMACEUTICAL_COMPANY_ASTERISK_ID)) != null;
    }

    public List<PharmaceuticalCompanyListing> getListings() {
        Function<WebElement, PharmaceuticalCompanyListing> transformation = new Function<WebElement, PharmaceuticalCompanyListing>() {
            @Override
            public PharmaceuticalCompanyListing apply(WebElement row) {
                return new PharmaceuticalCompanyListing(row);
            }
        };
        return JQueryUtils.transformDataTableRows(pharmaceuticalCompaniesTable, transformation);
    }

    public AddPharmaceuticalCompanyDialog clickAddPharmaceuticalCompanyButton() {
        addPharmaceuticalCompanyButton.click();
        return new AddPharmaceuticalCompanyDialog(getDriver()).waitUntilReady();
    }

    public File clickViewPdfButton() throws IOException {
        return FileDownloadUtils.clickDownloadLink(getDriver(), viewPdfButton).getFile();
    }

    public SupportingDocumentsTag getSupportingDocumentsTag() {
        return supportingDocumentsTag.waitUntilReady();
    }

    @Override
    public boolean isReadOnly() {
        return !isPresent(By.id(ADD_PHARMACEUTICAL_COMPANY_BUTTON_ID))
                && !isPresent(By.id(PHARMACEUTICAL_COMPANIES_TABLE_ID))
                && isPresent(By.id(SupportingDocumentsTag.SUPPORTING_DOCUMENT_TABLE_ID));
    }

    @Override
    protected String getTabId() {
        return TAB_ID;
    }

    @Override
    public FormTypeEnum getFormType() {
        return FormTypeEnum.CTEP_FINANCIAL_DISCLOSURE_FORM;
    }

    @Override
    protected void assertLoaded() {
        super.assertLoaded();
        assertPresent(By.className(TAB_UNIQUE_LOCATOR_CLASS));
        assertTrue(isPresent(By.id(LOCKED_PDF_BUTTON_DIV_ID)) || isPresent(By.id(UNLOCKED_PDF_BUTTON_DIV_ID)));
        if (isPresent(By.id(UNLOCKED_PDF_BUTTON_DIV_ID))) {
            assertFindBysPresent();
            assertQuestionsPresent();
        }
        assertElementWithIdPresent(VIEW_PDF_BUTTON_ID);
    }

    private void assertQuestionsPresent() {
        for (Question question : Question.values()) {
            assertPresent(By.id(question.questionId + true));
            assertPresent(By.id(question.questionId + false));
        }
    }

    public FinancialDisclosureTabHelper getHelper() {
        return helper;
    }

    public static IdentifiableComponentFactory<FinancialDisclosureTab> getFactory(AnnualRegistrationPage page) {
        return new Factory(page);
    }

    private static class Factory extends IdentifiableComponentFactory<FinancialDisclosureTab> {

        private final AnnualRegistrationPage page;

        public Factory(AnnualRegistrationPage page) {
            this.page = page;
        }

        @Override
        protected FinancialDisclosureTab getInstance(WebDriver driver) {
            return new FinancialDisclosureTab(driver, page);
        }

    }

    public class PharmaceuticalCompanyListing {

        private static final int NAME_INDEX = 0;
        private static final int CTEP_ID_INDEX = 1;
        private static final int EMAIL_INDEX = 2;
        private static final int PHONE_NUMBER_INDEX = 3;
        private static final int ADDRESS_INDEX = 4;
        private static final int DELETE_INDEX = 5;

        private final String id;
        private final String name;
        private final String ctepId;
        private final String email;
        private final String phoneNumber;
        private final String address;
        private final WebElement deleteIcon;

        public PharmaceuticalCompanyListing(WebElement row) {
            id = WebElementUtils.getId(row);
            List<WebElement> cells = TableUtils.getCells(row);
            name = cells.get(NAME_INDEX).getText();
            ctepId = trimToNull(cells.get(CTEP_ID_INDEX).getText());
            email = trimToNull(cells.get(EMAIL_INDEX).getText());
            phoneNumber = trimToNull(cells.get(PHONE_NUMBER_INDEX).getText());
            address = cells.get(ADDRESS_INDEX).getText();
            deleteIcon = cells.get(DELETE_INDEX).findElement(By.tagName("a"));
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCtepId() {
            return ctepId;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public ConfirmDialog clickDeleteIcon() {
            deleteIcon.click();
            String titleKey = "remove.pharmaceutical.company.confirm.title";
            String messageKey = "remove.pharmaceutical.company.confirm.text";
            return new ConfirmDialog(getDriver(), FinancialDisclosureTab.this, titleKey, messageKey).waitUntilReady();
        }
    }
}