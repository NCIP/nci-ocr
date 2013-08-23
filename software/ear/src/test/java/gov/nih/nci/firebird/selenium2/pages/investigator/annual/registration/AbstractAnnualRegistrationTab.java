package gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration;

import java.util.List;

import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.selenium2.pages.components.AbstractTab;
import gov.nih.nci.firebird.selenium2.pages.components.tags.MessagesTag;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class AbstractAnnualRegistrationTab<T extends AbstractAnnualRegistrationTab<T>> extends AbstractTab<T> {
    private static final String NEXT_BUTTON_CLASS_PREFIX = "nextFormButton_";

    private AnnualRegistrationPage page;

    public AbstractAnnualRegistrationTab(WebDriver driver, AnnualRegistrationPage page) {
        super(driver);
        this.page = page;
    }

    @Override
    public AnnualRegistrationPage getPage() {
        return page.waitUntilReady();
    }

    public AbstractAnnualRegistrationTab<?> clickNextButton() {
        getNextButton().click();
        pause(500);
        return (AbstractAnnualRegistrationTab<?>) identifyDisplayedComponent(10,
                SupplementalInvestigatorDataFormTab.getFactory(new AnnualRegistrationPage(getDriver())),
                FinancialDisclosureTab.getFactory(new AnnualRegistrationPage(getDriver())),
                AnnualAdditionalAttachmentsTab.getFactory(new AnnualRegistrationPage(getDriver())),
                AnnualForm1572Tab.getFactory(new AnnualRegistrationPage(getDriver())));
    }

    private WebElement getNextButton() {
        return getDriver().findElement(getNextButtonLocator());
    }

    By getNextButtonLocator() {
        return By.id(NEXT_BUTTON_CLASS_PREFIX + getFormType().name());
    }

    public String getNextButtonText() {
        return getNextButton().getText();
    }

    public boolean isNextButtonPresent() {
        return getElementIfPresent(getDriver(), getNextButtonLocator()) != null;
    }

    public List<String> getValidationErrors() {
        return MessagesTag.getFactory().create(getDriver()).getActionErrors();
    }

    public abstract boolean isReadOnly();

    public abstract FormTypeEnum getFormType();

}
