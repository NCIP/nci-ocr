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
package gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration;

import static gov.nih.nci.firebird.test.util.FirebirdPropertyUtils.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.support.AbstractLoadableComponent;
import gov.nih.nci.firebird.commons.selenium2.support.IdentifiableComponentFactory;
import gov.nih.nci.firebird.commons.selenium2.util.JQueryUtils;
import gov.nih.nci.firebird.commons.selenium2.util.TableUtils;
import gov.nih.nci.firebird.commons.selenium2.util.WaitUtils;
import gov.nih.nci.firebird.selenium2.pages.base.TableListing;
import gov.nih.nci.firebird.selenium2.pages.base.ValidationMessageDialog;
import gov.nih.nci.firebird.selenium2.pages.components.tags.CommentsTag;
import gov.nih.nci.firebird.selenium2.pages.components.tags.RegistrationCommentsTag;
import gov.nih.nci.firebird.selenium2.pages.components.tags.RegistrationCommentsTag.CommentType;
import gov.nih.nci.firebird.selenium2.pages.registration.common.FormRejectionCommentsDialog;
import gov.nih.nci.firebird.selenium2.pages.util.FirebirdTableUtils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * /investigator/registration/ajax/view_overview.jsp
 */
public class RegistrationOverviewTab extends AbstractRegistrationTab<RegistrationOverviewTab> {

    public static final IdentifiableComponentFactory<RegistrationOverviewTab> FACTORY = new Factory();

    static final String TAB_LINK_CSS_SELECTOR = "#overviewTab > a";
    private static final String TAB_ID = "overviewTab";
    private static final String SUBMIT_REGISTRATION_BUTTON_ID = "processRegistrationBtn";
    private static final String REVISE_BUTTON_ID = "initiateRegistrationRevisionButton";
    private static final String CANCEL_REVISION_BUTTON_ID = "cancelRegistrationRevisionButton";
    private static final String STATUS_HEADING_CSS_SELECTOR = "h3.clear";

    @FindBy(id = SUBMIT_REGISTRATION_BUTTON_ID)
    private WebElement submitRegistrationButton;

    @FindBy(id = REVISE_BUTTON_ID)
    private WebElement reviseRegistrationButton;

    @FindBy(id = CANCEL_REVISION_BUTTON_ID)
    private WebElement cancelRevisionButton;

    @FindBy(css = STATUS_HEADING_CSS_SELECTOR)
    private WebElement statusHeading;

    private RegistrationOverviewTabHelper helper = new RegistrationOverviewTabHelper(this);
    private final RegistrationCommentsTag commentsTag;
    private final RegistrationFormsTable formsTable;

    RegistrationOverviewTab(WebDriver driver, InvestigatorRegistrationPage page) {
        super(driver, page);
        this.commentsTag = new RegistrationCommentsTag(driver);
        this.formsTable = new RegistrationFormsTable();
    }

    public RegistrationOverviewTabHelper getHelper() {
        return helper;
    }

    public String getStatus() {
        String headingPrefix = getPropertyText("registration.status.label") + getPropertyText("label.separator") + " ";
        int statusStartIndex = headingPrefix.length();
        return statusHeading.getText().substring(statusStartIndex).trim();
    }

    public boolean areCommentsPresent() {
        return CommentsTag.isCommentsTagPresent(getDriver());
    }

    public String getAdditionalComments() {
        return getCommentsTag().getComments(CommentType.ADDITIONAL);
    }

    public boolean isSubmitRegistrationButtonPresent() {
        return isPresent(By.id(SUBMIT_REGISTRATION_BUTTON_ID));
    }

    public AbstractLoadableComponent<?> clickSubmitRegistration() {
        submitRegistrationButton.click();
        WaitUtils.pause(200);
        return identifyDisplayedComponent(
                SignAndSubmitRegistrationDialog.getFactory(RegistrationOverviewTab.this),
                ConfirmSubmissionToInvestigatorDialog.getFactory(RegistrationOverviewTab.this),
                ValidationMessageDialog.getFactory(this),
                ResubmissionCommentsDialog.getFactory(this));
    }

    public boolean isReviseRegistrationButtonPresent() {
        return isPresent(By.id(REVISE_BUTTON_ID));
    }

    public void clickReviseRegistration() {
        reviseRegistrationButton.click();
        waitUntilReady();
    }

    public BrowseRegistrationsPage clickCancelRevision() {
        cancelRevisionButton.click();
        return new BrowseRegistrationsPage(getDriver()).waitUntilReady();
    }

    public boolean isCancelRevisionButtonPresent() {
        return isPresent(By.id(CANCEL_REVISION_BUTTON_ID));
    }

    public List<FormListing> getFormListings() {
        return getFormsTable().getFormListings();
    }

    private RegistrationFormsTable getFormsTable() {
        return formsTable.waitUntilReady();
    }

    @Override
    protected void assertLoaded() {
        super.assertLoaded();
        assertFalse(JQueryUtils.isDialogDisplayed(getDriver()));
        assertPresent(By.cssSelector(STATUS_HEADING_CSS_SELECTOR));
        assertTrue(findElement(By.cssSelector(STATUS_HEADING_CSS_SELECTOR)).isDisplayed());
    }

    protected String getTabId() {
        return TAB_ID;
    }

    RegistrationCommentsTag getCommentsTag() {
        return commentsTag.waitUntilReady();
    }

    private static class Factory extends IdentifiableComponentFactory<RegistrationOverviewTab> {

        @Override
        protected RegistrationOverviewTab getInstance(WebDriver driver) {
            return new RegistrationOverviewTab(driver, new InvestigatorRegistrationPage(driver));
        }

    }

    private class RegistrationFormsTable extends AbstractLoadableComponent<RegistrationFormsTable> {

        private static final String FORM_TABLE_ID = "registrationFormsTable";

        @FindBy(id = FORM_TABLE_ID)
        private WebElement table;

        protected RegistrationFormsTable() {
            super(RegistrationOverviewTab.this.getDriver());
        }

        @Override
        protected void assertLoaded() {
            assertFindBysPresent();
        }

        private List<FormListing> getFormListings() {
            return FirebirdTableUtils.transformDataTableRows(RegistrationOverviewTab.this, table, FormListing.class);
        }

    }

    public class FormListing implements TableListing {

        private static final int FORM_COLUMN_INDEX = 0;
        private static final int OPTIONALITY_COLUMN_INDEX = 1;
        private static final int STATUS_DATE_COLUMN_INDEX = 2;
        private static final int STATUS_COLUMN_INDEX = 3;
        private static final int COMMENTS_COLUMN_INDEX = 4;

        private final Long id;
        private final String form;
        private final WebElement formLink;
        private final String optionality;
        private final String statusDate;
        private final String status;
        private final WebElement commentLink;

        public FormListing(WebElement row) {
            this.id = Long.valueOf(RegistrationOverviewTab.getId(row));
            List<WebElement> cells = TableUtils.getCells(row);
            this.form = cells.get(FORM_COLUMN_INDEX).getText();
            this.formLink = cells.get(FORM_COLUMN_INDEX).findElement(By.tagName("a"));
            this.optionality = cells.get(OPTIONALITY_COLUMN_INDEX).getText();
            this.statusDate = cells.get(STATUS_DATE_COLUMN_INDEX).getText();
            this.status = cells.get(STATUS_COLUMN_INDEX).getText();
            commentLink = getElementIfPresent(cells.get(COMMENTS_COLUMN_INDEX), By.tagName("img"));
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getForm() {
            return form;
        }

        public String getOptionality() {
            return optionality;
        }

        public String getStatusDate() {
            return statusDate;
        }

        public String getStatus() {
            return status;
        }

        public AbstractFormTab<?> clickFormLink() {
            pause(200);
            formLink.click();
            pause(200);
            return (AbstractFormTab<?>) identifyDisplayedComponent(30, ProtocolForm1572Tab.getFactory(getPage()),
                    FinancialDisclosureTab.getFactory(getPage()), CurriculumVitaeTab.getFactory(getPage()),
                    HumanResearchCertificateTab.getFactory(getPage()), ProtocolAdditionalAttachmentsTab.getFactory(getPage()));
        }

        public FormRejectionCommentsDialog clickComments() {
            commentLink.click();
            return new FormRejectionCommentsDialog(getDriver(), RegistrationOverviewTab.this).waitUntilReady();
        }

    }

}
