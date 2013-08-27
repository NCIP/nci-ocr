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
package gov.nih.nci.firebird.selenium2.tests.profile;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.Address;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.OrderingDesignee;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.base.ConfirmDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.DesigneeAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ProfessionalContactInformationTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SelectOrderingDesigneeDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SelectShippingDesigneeDialog;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.LoginAccount.InvestigatorLogin;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;
import gov.nih.nci.firebird.test.util.FirebirdPropertyUtils;

import org.junit.Test;

public class DesigneeAssociationsTabTest extends AbstractFirebirdWebDriverTest {

    private DataSet dataSet;

    @Test
    public void testCtepRequired() throws Exception {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        InvestigatorProfile dcpProfile = builder.createProfile().complete().get();
        InvestigatorProfile ctepProfile = builder.createProfile().complete().get();
        FirebirdUser dcpUser = builder.createInvestigator(dcpProfile).get();
        FirebirdUser ctepUser = builder.createInvestigator(ctepProfile).asCtepUser()
                .withLogin(InvestigatorLogin.fbciinv2).get();
        dataSet = builder.build();

        ProfessionalContactInformationTab contactInfoTab = openHomePage(
                InvestigatorLogin.valueOf(dcpUser.getBaseUsername())).getInvestigatorMenu().clickProfile();
        assertFalse(contactInfoTab.getPage().isDesigneesTabPresent());

        contactInfoTab = openHomePage(InvestigatorLogin.valueOf(ctepUser.getBaseUsername()), getCtepProvider())
                .getInvestigatorMenu().clickProfile();
        assertTrue(contactInfoTab.getPage().isDesigneesTabPresent());
    }

    @Test
    public void testTabDisplaysInformation() throws Exception {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        InvestigatorProfile ctepProfile = builder.createProfile().complete().get();
        builder.createInvestigator(ctepProfile).asCtepUser().get();
        dataSet = builder.build();

        DesigneeAssociationsTab designeesTab = openHomePage(dataSet.getInvestigatorLogin(), getCtepProvider())
                .getInvestigatorMenu().clickProfile().getPage().clickDesigneeAssociationsTab();

        designeesTab.getHelper().assertShippingDesigneeListed(ctepProfile.getShippingDesignee());
        for (OrderingDesignee orderingDesignee : ctepProfile.getOrderingDesignees()) {
            designeesTab.getHelper().assertOrderingDesigneeListed(orderingDesignee);
        }
    }

    @Test
    public void testOrderingDesigneeManagement() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createInvestigator().asCtepUser().get();
        dataSet = builder.build();

        DesigneeAssociationsTab designeesTab = openHomePage(dataSet.getInvestigatorLogin(), getCtepProvider())
                .getInvestigatorMenu().clickProfile().getPage().clickDesigneeAssociationsTab();
        Person newPerson = createNewOrderingDesignee(designeesTab);
        Person existingPerson = selectExistingOrderingDesignee(designeesTab);
        assertEquals(2, designeesTab.getOrderingDesigneeListings().size());
        removeOrderingDesignee(designeesTab, newPerson);
        assertEquals(1, designeesTab.getOrderingDesigneeListings().size());
        removeOrderingDesignee(designeesTab, existingPerson);
        assertTrue(designeesTab.getOrderingDesigneeListings().isEmpty());
    }

    private Person createNewOrderingDesignee(DesigneeAssociationsTab designeesTab) {
        checkRequiredFields(designeesTab);
        SelectOrderingDesigneeDialog orderingDesigneeDialog = designeesTab.clickAddOrderingDesignee();
        Person newPerson = PersonFactory.getInstance().create();
        orderingDesigneeDialog.getHelper().enterNewPersonData(newPerson);
        orderingDesigneeDialog.clickSave();
        designeesTab.getHelper().assertOrderingDesigneeListed(newPerson);
        return newPerson;
    }

    private void checkRequiredFields(DesigneeAssociationsTab designeesTab) {
        final SelectOrderingDesigneeDialog orderingDesigneeDialog = designeesTab.clickAddOrderingDesignee();
        orderingDesigneeDialog.getHelper().enterNewPersonData(new Person());
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure();
        expectedValidationFailure.addExpectedRequiredFields("person.firstName", "person.lastName", "person.email",
                "person.postalAddress.streetAddress", "person.postalAddress.city",
                "person.postalAddress.stateOrProvince", "person.postalAddress.postalCode");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                orderingDesigneeDialog.clickSave();
            }
        });
        orderingDesigneeDialog.clickCancel();
    }

    private Person selectExistingOrderingDesignee(DesigneeAssociationsTab designeesTab) {
        SelectOrderingDesigneeDialog orderingDesigneeDialog = designeesTab.clickAddOrderingDesignee();
        Person existingPerson = getExistingNesPerson();
        orderingDesigneeDialog.getHelper().searchAndSelectPerson(existingPerson);
        designeesTab.getHelper().assertOrderingDesigneeListed(existingPerson);
        checkDuplicateSelection(designeesTab, existingPerson);
        return existingPerson;
    }

    private void checkDuplicateSelection(DesigneeAssociationsTab designeesTab, final Person duplicatePerson) {
        final SelectOrderingDesigneeDialog orderingDesigneeDialog = designeesTab.clickAddOrderingDesignee();
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "duplicate.ordering.designee.error");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                orderingDesigneeDialog.getHelper().searchAndSelectPerson(duplicatePerson);
            }
        });
        orderingDesigneeDialog.clickCancel();
    }

    private void removeOrderingDesignee(DesigneeAssociationsTab designeesTab, Person orderingDesignee) {
        ConfirmDialog confirmDialog = designeesTab.getHelper().getOrderingDesigneeListing(orderingDesignee).clickRemove();
        confirmDialog.getHelper().checkTitleAndMessage(orderingDesignee.getDisplayName());
        confirmDialog.clickConfirmButton();
    }

    @Test
    public void testShippingDesigneeManagement() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createInvestigator().asCtepUser().get();
        dataSet = builder.build();

        DesigneeAssociationsTab designeesTab = openHomePage(dataSet.getInvestigatorLogin(), getCtepProvider())
                .getInvestigatorMenu().clickProfile().getPage().clickDesigneeAssociationsTab();
        Person newPerson = createNewShippingDesignee(designeesTab);
        removeShippingDesignee(designeesTab, newPerson);
        Person existingPerson = selectExistingShippingDesignee(designeesTab);
        removeShippingDesignee(designeesTab, existingPerson);
    }

    private Person createNewShippingDesignee(DesigneeAssociationsTab designeesTab) {
        checkRequiredCreateNewShippingDesigneeFields(designeesTab);
        checkRequiredSelectExistingShippingDesigneeFields(designeesTab);
        SelectShippingDesigneeDialog shippingDesigneeDialog = designeesTab.clickSelectShippingDesignee();
        Person newPerson = PersonFactory.getInstance().create();
        shippingDesigneeDialog.getHelper().enterNewPersonData(newPerson);
        Organization newOrganization = OrganizationFactory.getInstance().create();
        shippingDesigneeDialog.getHelper().enterNewOrganizationData(newOrganization);
        Address shippingAddress = ValueGenerator.getUniqueAddress();
        shippingDesigneeDialog.getHelper().setShippingAddress(shippingAddress);
        shippingDesigneeDialog.clickSave();
        designeesTab.getHelper().assertShippingDesigneeListed(newPerson, newOrganization, shippingAddress);
        return newPerson;
    }

    private void checkRequiredCreateNewShippingDesigneeFields(DesigneeAssociationsTab designeesTab) {
        final SelectShippingDesigneeDialog shippingDesigneeDialog = designeesTab.clickSelectShippingDesignee();
        shippingDesigneeDialog.getHelper().enterNewPersonData(new Person());
        shippingDesigneeDialog.getHelper().enterNewOrganizationData(new Organization());
        checkSectionHeaderErrorMessagesAppear(shippingDesigneeDialog);
        shippingDesigneeDialog.getHelper().checkForPersonRequiredFields("person.firstName", "person.lastName",
                "person.email", "person.postalAddress.streetAddress", "person.postalAddress.city",
                "person.postalAddress.stateOrProvince", "person.postalAddress.postalCode");
        shippingDesigneeDialog.getHelper().checkForOrganizationRequiredFields("profile.organization.name",
                "profile.organization.email", "profile.organization.postalAddress.streetAddress",
                "profile.organization.postalAddress.city", "profile.organization.postalAddress.stateOrProvince",
                "profile.organization.postalAddress.postalCode");
        shippingDesigneeDialog.getHelper().checkForShippingAddressRequiredFields("person.postalAddress.streetAddress",
                "person.postalAddress.city", "person.postalAddress.stateOrProvince", "person.postalAddress.postalCode");
        shippingDesigneeDialog.clickCancel();
    }

    private void checkSectionHeaderErrorMessagesAppear(final SelectShippingDesigneeDialog shippingDesigneeDialog) {
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure();
        String errorInSectionPrefix = FirebirdPropertyUtils
                .getPropertyText("accordion.form.errors.in.section") + " ";
        expectedValidationFailure.addExpectedText(errorInSectionPrefix
                + getPropertyText("person.association.shipping.designee.person.section.header"));
        expectedValidationFailure.addExpectedText(errorInSectionPrefix
                + getPropertyText("person.association.shipping.designee.organization.section.header"));
        expectedValidationFailure.addExpectedText(errorInSectionPrefix
                + getPropertyText("person.association.shipping.designee.shipping.address.section.header"));
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                shippingDesigneeDialog.clickSave();
            }
        });
    }

    private void checkRequiredSelectExistingShippingDesigneeFields(DesigneeAssociationsTab designeesTab) {
        final SelectShippingDesigneeDialog shippingDesigneeDialog = designeesTab.clickSelectShippingDesignee();
        checkSectionHeaderErrorMessagesAppear(shippingDesigneeDialog);
        shippingDesigneeDialog.getHelper().checkForNoPersonSelectionMessage();
        shippingDesigneeDialog.getHelper().checkForNoOrganizationSelectionMessage();
        shippingDesigneeDialog.getHelper().checkForShippingAddressRequiredFields("person.postalAddress.streetAddress",
                "person.postalAddress.city", "person.postalAddress.stateOrProvince", "person.postalAddress.postalCode");
        shippingDesigneeDialog.clickCancel();
    }

    private Person selectExistingShippingDesignee(DesigneeAssociationsTab designeesTab) {
        SelectShippingDesigneeDialog shippingDesigneeDialog = designeesTab.clickSelectShippingDesignee();
        Person existingPerson = getExistingNesPerson();
        shippingDesigneeDialog.getHelper().searchAndSelectPerson(existingPerson);
        Organization existingOrganization = getExistingNesOrganization();
        shippingDesigneeDialog.getHelper().searchAndSelectOrganization(existingOrganization);
        Address shippingAddress = ValueGenerator.getUniqueAddress();
        shippingDesigneeDialog.getHelper().setShippingAddress(shippingAddress);
        shippingDesigneeDialog.clickSave();
        designeesTab.getHelper().assertShippingDesigneeListed(existingPerson, existingOrganization, shippingAddress);
        return existingPerson;
    }

    private void removeShippingDesignee(DesigneeAssociationsTab designeesTab, Person shippingDesignee) {
        assertFalse(designeesTab.isAddShippingDesigneeButtonPresent());
        ConfirmDialog confirmDialog = designeesTab.getHelper().getShippingDesigneeListing(shippingDesignee).clickRemove();
        confirmDialog.getHelper().checkTitleAndMessage(shippingDesignee.getDisplayName());
        confirmDialog.clickConfirmButton();
        assertTrue(designeesTab.isAddShippingDesigneeButtonPresent());
    }

}
