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
package gov.nih.nci.firebird.selenium2.pages.investigator.profile;

import static gov.nih.nci.firebird.commons.selenium2.util.TableUtils.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.support.IdentifiableComponentFactory;
import gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils;
import gov.nih.nci.firebird.selenium2.pages.base.TableListing;
import gov.nih.nci.firebird.selenium2.pages.components.AbstractTab;
import gov.nih.nci.firebird.selenium2.pages.components.DataTable;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubInvestigatorAssociationFormDialog;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * investigator/profile/associations/subinvestigators/ajax/subinvestigator_associations_tab.jsp
 */
public class SubInvestigatorAssociationsTab extends AbstractTab<SubInvestigatorAssociationsTab> {

    public static final String TAB_ID = "subInvestigatorAssociationTab";
    private static final String SUBINVESTIGATOR_TABLE_ID = "subInvestigatorsTable";
    private static final String ADD_NEW_BUTTON_ID = "addPerson";

    @FindBy(id = ADD_NEW_BUTTON_ID)
    private WebElement addNewButton;
    private final DataTable<SubInvestigatorAssociationListing> table;
    private final InvestigatorProfilePage page;
    private final SubInvestigatorAssociationsTabHelper helper = new SubInvestigatorAssociationsTabHelper(this);

    protected SubInvestigatorAssociationsTab(WebDriver driver, InvestigatorProfilePage page) {
        super(driver);
        this.page = page;
        table = new DataTable<SubInvestigatorAssociationListing>(driver, SUBINVESTIGATOR_TABLE_ID,
                SubInvestigatorAssociationListing.class, this);
    }

    public SubInvestigatorAssociationsTabHelper getHelper() {
        return helper;
    }

    public SubInvestigatorAssociationFormDialog clickAddNew() {
        addNewButton.click();
        return new SubInvestigatorAssociationFormDialog(getDriver(), this).waitUntilReady();
    }

    public List<SubInvestigatorAssociationListing> getListings() {
        return table.getListings();
    }

    @Override
    public InvestigatorProfilePage getPage() {
        return page;
    }

    @Override
    protected String getTabId() {
        return TAB_ID;
    }

    @Override
    protected void assertLoaded() {
        super.assertLoaded();
        assertFindBysPresent();
        assertTrue(addNewButton.isDisplayed());
    }

    public static IdentifiableComponentFactory<?> getFactory(InvestigatorProfilePage page) {
        return new Factory(page);
    }

    private static class Factory extends IdentifiableComponentFactory<SubInvestigatorAssociationsTab> {

        private final InvestigatorProfilePage page;

        public Factory(InvestigatorProfilePage page) {
            this.page = page;
        }

        @Override
        protected SubInvestigatorAssociationsTab getInstance(WebDriver driver) {
            return new SubInvestigatorAssociationsTab(driver, page);
        }

    }

    public class SubInvestigatorAssociationListing implements TableListing {

        private static final int NAME_COLUMN_INDEX = 0;
        private static final int EMAIL_ADDRESS_COLUMN_INDEX = 1;
        private static final int ADDRESS_COLUMN_INDEX = 2;
        private static final int PHONE_NUMBER_COLUMN_INDEX = 3;
        private static final int REMOVE_BUTTON_COLUMN_INDEX = 4;

        private final Long id;
        private final String name;
        private final String emailAddress;
        private final String address;
        private final String phoneNumber;
        private final WebElement removeButton;

        public SubInvestigatorAssociationListing(WebElement row) {
            id = Long.valueOf(WebElementUtils.getId(row));
            List<WebElement> cells = getCells(row);
            name = cells.get(NAME_COLUMN_INDEX).getText();
            emailAddress = cells.get(EMAIL_ADDRESS_COLUMN_INDEX).getText();
            address = cells.get(ADDRESS_COLUMN_INDEX).getText();
            phoneNumber = cells.get(PHONE_NUMBER_COLUMN_INDEX).getText();
            removeButton = cells.get(REMOVE_BUTTON_COLUMN_INDEX).findElement(By.tagName("img"));
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public String getAddress() {
            return address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public ConfirmSubInvestigatorRemovalDialog clickRemove() {
            removeButton.click();
            return new ConfirmSubInvestigatorRemovalDialog(getDriver(), page).waitUntilReady();
        }
    }
}
