/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the NCI OCR Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the NCI OCR Software; (ii) distribute and
 * have distributed to and by third parties the NCI OCR Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM and the National Cancer Institute. If You do not include
 * such end-user documentation, You shall include this acknowledgment in the
 * Software itself, wherever such third-party acknowledgments normally appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", or "5AM"
 * to endorse or promote products derived from this Software. This License does
 * not authorize You to use any trademarks, service marks, trade names, logos or
 * product names of either NCI or 5AM, except as required to comply with the
 * terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC. OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.firebird.selenium2.pages.base;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import gov.nih.nci.firebird.commons.selenium2.support.AbstractLoadableComponent;
import gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils;
import gov.nih.nci.firebird.selenium2.pages.coordinator.investigators.BrowseInvestigatorsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.BrowseCoordinatorsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.BrowseAnnualRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ProfessionalContactInformationTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.BrowseCtepInvestigatorsPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.AnnualRegistrationSubmissionsPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.EditAnnualRegistrationConfigurationPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.CreateProtocolPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolsListPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.delegates.BrowseSponsorDelegatesPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.export.PersonsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.protocol.ExportProtocolsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.protocol.ImportProtocolPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.protocol.SelectImportFilePage;
import gov.nih.nci.firebird.selenium2.pages.user.MyAccountPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public abstract class AbstractMenuPage<T extends AbstractAuthorizedPage<T>> extends AbstractAuthorizedPage<T> {

    private static final String MY_ACCOUNT_LINK_ID = "myAccountLink";
    private static final String HOME_MENU_LINK_ID = "homeBtn";

    @FindBy(id = HOME_MENU_LINK_ID)
    private WebElement homeMenuItem;

    @FindBy(id = MY_ACCOUNT_LINK_ID)
    private WebElement myAccountLink;

    protected AbstractMenuPage(WebDriver driver) {
        super(driver);
    }

    protected AbstractMenuPage(WebDriver driver, int timeoutSeconds) {
        super(driver, timeoutSeconds);
    }

    public String getUserDisplayName() {
        return myAccountLink.getText();
    }

    public MyAccountPage clickMyAccount() {
        myAccountLink.click();
        return new MyAccountPage(getDriver()).waitUntilReady();
    }

    public HomePage clickHome() {
        homeMenuItem.click();
        return HomePage.FACTORY.create(getDriver());
    }

    public InvestigatorMenu getInvestigatorMenu() {
        return new InvestigatorMenu(getDriver()).waitUntilReady();
    }

    public InvestigatorsMenu getInvestigatorsMenu() {
        return new InvestigatorsMenu(getDriver()).waitUntilReady();
    }

    public ProtocolsMenu getProtocolsMenu() {
        return new ProtocolsMenu(getDriver()).waitUntilReady();
    }

    public SponsorMenu getSponsorMenu() {
        return new SponsorMenu(getDriver()).waitUntilReady();
    }

    public boolean isAnnualRegistrationsMenuPresent() {
        return WebElementUtils.isPresent(getDriver(),
                By.id(AnnualRegistrationsMenu.ANNUAL_REGISTRATIONS_MENU_HEADING_ID));
    }

    public boolean isProtocolsMenuPresent() {
        return WebElementUtils.isPresent(getDriver(),
                By.id(ProtocolsMenu.PROTOCOLS_MENU_HEADING_ID));
    }

    public AnnualRegistrationsMenu getAnnualRegistrationsMenu() {
        return new AnnualRegistrationsMenu(getDriver()).waitUntilReady();
    }

    private void moveToMenuHeading(WebElement menuHeading) {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(menuHeading);
        actions.perform();
    }

    @Override
    protected void assertLoaded() {
        super.assertLoaded();
        assertPresent(By.id(HOME_MENU_LINK_ID));
        assertPresent(By.id(MY_ACCOUNT_LINK_ID));
    }

    abstract class AbstractMenu<MENU_TYPE extends AbstractLoadableComponent<MENU_TYPE>> extends
            AbstractLoadableComponent<MENU_TYPE> {

        private final WebElement menuHeading;
        private final String menuHeadingId;

        private AbstractMenu(WebDriver driver, String menuHeadingId) {
            super(driver);
            this.menuHeadingId = menuHeadingId;
            this.menuHeading = findElement(By.id(menuHeadingId));
        }

        WebElement getMenuHeading() {
            return menuHeading;
        }

        boolean isMenuItemPresent(String menuItemKey) {
            moveToMenuHeading(menuHeading);
            return WebElementUtils.isPresent(getDriver(), By.linkText(getPropertyText(menuItemKey)));
        }

        void clickMenuItem(String menuItemKey) {
            pause(200);
            moveToMenuHeading(menuHeading);
            By menuItemSelector = By.linkText(getPropertyText(menuItemKey));
            waitFor(elementToBeClickable(menuItemSelector), 1);
            WebElement menuItem = findElement(menuItemSelector);
            menuItem.click();
        }

        @Override
        protected void assertLoaded() {
            By menuHeadingLocator = By.id(menuHeadingId);
            assertConditionTrue(elementToBeClickable(menuHeadingLocator));
        }
    }

    public class InvestigatorMenu extends AbstractMenu<InvestigatorMenu> {

        private static final String INVESTIGATOR_MENU_HEADING_ID = "investigatorBtn";

        private InvestigatorMenu(WebDriver driver) {
            super(driver, INVESTIGATOR_MENU_HEADING_ID);
        }

        public ProfessionalContactInformationTab click() {
            getMenuHeading().click();
            return new ProfessionalContactInformationTab(getDriver()).waitUntilReady();
        }

        public ProfessionalContactInformationTab clickProfile() {
            clickMenuItem("investigator.menu.item.profile");
            return new ProfessionalContactInformationTab(getDriver()).waitUntilReady();
        }

        public BrowseRegistrationsPage clickProtocolRegistrations() {
            clickMenuItem("investigator.menu.item.registrations");
            return new BrowseRegistrationsPage(getDriver()).waitUntilReady();
        }

        public BrowseCoordinatorsPage clickRegistrationCoordinators() {
            clickMenuItem("investigator.menu.item.coordinators");
            return new BrowseCoordinatorsPage(getDriver()).waitUntilReady();
        }

        public BrowseAnnualRegistrationsPage clickAnnualRegistrations() {
            clickMenuItem("investigator.menu.item.annual.registrations");
            return new BrowseAnnualRegistrationsPage(getDriver()).waitUntilReady();
        }
    }

    public class InvestigatorsMenu extends AbstractMenu<InvestigatorsMenu> {

        private static final String INVESTIGATORS_MENU_HEADING_ID = "investigatorsBtn";

        private InvestigatorsMenu(WebDriver driver) {
            super(driver, INVESTIGATORS_MENU_HEADING_ID);
        }

        public BrowseInvestigatorsPage click() {
            getMenuHeading().click();
            return new BrowseInvestigatorsPage(getDriver()).waitUntilReady();
        }
    }

    public class ProtocolsMenu extends AbstractMenu<ProtocolsMenu> {

        private static final String PROTOCOLS_MENU_HEADING_ID = "protocolBtn";

        private ProtocolsMenu(WebDriver driver) {
            super(driver, PROTOCOLS_MENU_HEADING_ID);
        }

        public ProtocolsListPage click() {
            getMenuHeading().click();
            return new ProtocolsListPage(getDriver()).waitUntilReady();
        }

        public ProtocolsListPage clickBrowse() {
            clickMenuItem("menu.item.protocols.browse");
            return new ProtocolsListPage(getDriver()).waitUntilReady();
        }

        public boolean isCreateNewPresent() {
            return isMenuItemPresent("menu.item.protocols.create");
        }

        public CreateProtocolPage clickCreateNew() {
            clickMenuItem("menu.item.protocols.create");
            return new CreateProtocolPage(getDriver()).waitUntilReady();
        }

        public SelectImportFilePage clickImport() {
            clickMenuItem("menu.item.protocols.import");
            return new SelectImportFilePage(getDriver()).waitUntilReady();
        }

        public ImportProtocolPage clickImportInProgress() {
            clickMenuItem("menu.item.protocols.import.in.progress");
            return new ImportProtocolPage(getDriver()).waitUntilReady();
        }

        public ExportProtocolsTab clickExport() {
            clickMenuItem("menu.item.protocols.export");
            return new ExportProtocolsTab(getDriver()).waitUntilReady();
        }
    }

    public class SponsorMenu extends AbstractMenu<SponsorMenu> {

        private static final String SPONSOR_MENU_HEADING_ID = "sponsorBtn";

        private SponsorMenu(WebDriver driver) {
            super(driver, SPONSOR_MENU_HEADING_ID);
        }

        public PersonsTab clickExportDataToCurate() {
            clickMenuItem("menu.heading.export.data.to.curate");
            return new PersonsTab(getDriver()).waitUntilReady();
        }

        public BrowseSponsorDelegatesPage clickDelegates() {
            clickMenuItem("menu.heading.sponsor.delegates");
            return new BrowseSponsorDelegatesPage(getDriver()).waitUntilReady();
        }
    }

    public class AnnualRegistrationsMenu extends AbstractMenu<AnnualRegistrationsMenu> {

        private static final String ANNUAL_REGISTRATIONS_MENU_HEADING_ID = "annualRegistrationsBtn";

        private AnnualRegistrationsMenu(WebDriver driver) {
            super(driver, ANNUAL_REGISTRATIONS_MENU_HEADING_ID);
        }

        public boolean isRequiredFormsMenuItemPresent() {
            return isMenuItemPresent("label.required.forms");
        }

        public EditAnnualRegistrationConfigurationPage clickRequiredForms() {
            clickMenuItem("label.required.forms");
            return new EditAnnualRegistrationConfigurationPage(getDriver()).waitUntilReady();
        }

        public AnnualRegistrationSubmissionsPage clickSubmissions() {
            clickMenuItem("label.submissions");
            return new AnnualRegistrationSubmissionsPage(getDriver()).waitUntilReady();
        }

        public BrowseCtepInvestigatorsPage clickBrowseInvestigators() {
            clickMenuItem("menu.item.ctep.investigator.search");
            return new BrowseCtepInvestigatorsPage(getDriver()).waitUntilReady();
        }
    }
}
