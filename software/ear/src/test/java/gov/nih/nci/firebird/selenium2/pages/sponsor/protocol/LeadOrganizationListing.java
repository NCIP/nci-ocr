package gov.nih.nci.firebird.selenium2.pages.sponsor.protocol;

import gov.nih.nci.firebird.commons.selenium2.util.TableUtils;
import gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils;
import gov.nih.nci.firebird.selenium2.pages.base.TableListing;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
*   Listing Class used when Modifying Protocol details.
*/
public class LeadOrganizationListing implements TableListing {
    private static final String REMOVE_LEAD_ORGANIZATION_BUTTON_PREFIX = "remove.";
    private static final int ORGANIZATION_NAME_COLUMN = 0;
    private static final int PRINCIPAL_INVESTIGATOR_NAME_COLUMN = 1;

    private String organizationSearchKey;
    private String organizationName;
    private String principalInvestigatorName;
    private WebElement removeLink;

    public LeadOrganizationListing(WebElement row) {
        this.organizationSearchKey = WebElementUtils.getId(row);
        List<WebElement> cells = TableUtils.getCells(row);
        this.organizationName = cells.get(ORGANIZATION_NAME_COLUMN).getText();
        this.principalInvestigatorName = cells.get(PRINCIPAL_INVESTIGATOR_NAME_COLUMN).getText();
        this.removeLink = row.findElement(By.id(REMOVE_LEAD_ORGANIZATION_BUTTON_PREFIX + organizationSearchKey));
    }

    @Override
    public Long getId() {
        return null; //The row id isn't actually a long.
    }

    public String getOrganizationSearchKey() {
        return organizationSearchKey;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getPrincipalInvestigatorName() {
        return principalInvestigatorName;
    }

    public void clickDeleteLeadOrganization() {
        removeLink.click();
    }
}
