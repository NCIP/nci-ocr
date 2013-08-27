/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a organization or an entity, and all other entities that control,
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
package gov.nih.nci.firebird.selenium2.tests.protocol;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.EditFormsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.EditFormsDialog.FormListing;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolsListPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.RegistrationFormsTab;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * test protocol forms are editable.
 */
public class EditRegistrationFormsTest extends AbstractFirebirdWebDriverTest {

    @Inject
    private DataSetBuilder builder;

    private FormType form1572Type;
    private FormType financialDisclosureType;
    private FormType curriculumVitaeType;
    private FormType additionalAttachmentsType;
    private FormType hrcType;
    private DataSet dataSet;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        setUpDataSet();
        Set<FormType> registrationFormTypes = getRegistrationFormTypes();
        setDefaultOptionalities(registrationFormTypes);
    }

    private void setUpDataSet() {
        Protocol protocol = builder.createProtocol().get();
        builder.createSponsor().forSponsors(protocol.getSponsor()).get();
        builder.createSponsor().forSponsors(protocol.getSponsor()).asDelegate();
        InvestigatorProfile investigatorProfile = builder.createProfile().complete().get();
        FirebirdUser investigator = builder.createInvestigator(investigatorProfile).get();
        InvestigatorProfile subinvestigatorProfile = builder.createProfile().complete().get();
        builder.createInvestigator(subinvestigatorProfile).get();
        InvestigatorRegistration investigatorRegistration =
                builder.createRegistration(protocol, investigator).complete().get();
        builder.createSubinvestigatorRegistration(subinvestigatorProfile, investigatorRegistration).complete();
        dataSet = builder.build();
    }

    private HashSet<FormType> getRegistrationFormTypes() {
        return Sets.newHashSet(getProtocol().getRegistrationConfiguration()
                .getInvestigatorConfiguration().getFormOptionalities().keySet());
    }

    private void setDefaultOptionalities(Set<FormType> registrationFormTypes) {
        EnumMap<FormTypeEnum, FormType> formTypes = Maps.newEnumMap(FormTypeEnum.class);
        for (FormType type : registrationFormTypes) {
            if (type.getInvestigatorDefault() != FormOptionality.SUPPLEMENTARY) {
                getProtocol().getRegistrationConfiguration().setInvestigatorOptionality(type, FormOptionality.NONE);
                getProtocol().getRegistrationConfiguration().setSubinvestigatorOptionality(type, FormOptionality.NONE);
            }
            formTypes.put(type.getFormTypeEnum(), type);
        }
        form1572Type = formTypes.get(FormTypeEnum.FORM_1572);
        financialDisclosureType = formTypes.get(FormTypeEnum.FINANCIAL_DISCLOSURE_FORM);
        curriculumVitaeType = formTypes.get(FormTypeEnum.CV);
        additionalAttachmentsType = formTypes.get(FormTypeEnum.ADDITIONAL_ATTACHMENTS);
        hrcType = formTypes.get(FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE);
        getProtocol().getRegistrationConfiguration().setInvestigatorOptionality(form1572Type, FormOptionality.REQUIRED);
        getProtocol().getRegistrationConfiguration().setSubinvestigatorOptionality(financialDisclosureType,
                FormOptionality.OPTIONAL);
        dataSet.update(getProtocol());
    }

    private LoginAccount getSponsorLogin() {
        return dataSet.getSponsorLogins().get(0);
    }

    private LoginAccount getSponsorDelegateLogin() {
        return dataSet.getSponsorLogins().get(1);
    }

    private FirebirdUser getSponsorDelegateUser() {
        return dataSet.getSponsor();
    }

    private Protocol getProtocol() {
        return dataSet.getProtocol();
    }

    private LoginAccount getPrimaryInvestigatorLogin() {
        return dataSet.getInvestigatorLogins().get(0);
    }

    private InvestigatorRegistration getPrimaryInvestigatorRegistration() {
        return dataSet.getInvestigatorRegistration();
    }

    private LoginAccount getSubinvestigatorLogin() {
        return dataSet.getInvestigatorLogins().get(1);
    }

    private SubInvestigatorRegistration getSubinvestigatorRegistration() {
        return dataSet.getSubInvestigatorRegistration();
    }

    @Test
    public void testSponsorDelegateCantEditForms() {
        getProtocol().setSponsor(getSponsorDelegateUser().getSponsorOrganizations().get(0));
        dataSet.update(getProtocol());
        RegistrationFormsTab formsTab = openFormsTab(getSponsorDelegateLogin(), getProtocol());
        assertFalse("Sponsor Delegate should not be able to edit registration forms", formsTab.isEditButtonPresent());
    }

    private RegistrationFormsTab openFormsTab(LoginAccount login, Protocol protocol) {
        ProtocolsListPage protocolsPage = openHomePage(login).getProtocolsMenu().click();
        RegistrationFormsTab formsTab = protocolsPage.getHelper().clickLink(protocol).getPage().clickFormsTab();
        return formsTab;
    }

    @Test
    public void testEditForm_Sponsor() {
        RegistrationFormsTab formsTab = openFormsTab(getSponsorLogin(), getProtocol());
        checkForRequiredFormOptionalities(formsTab);
        FormOptionality newSubInvestigator1572Optionality = FormOptionality.OPTIONAL;
        form1572Type.setSubinvestigatorDefault(newSubInvestigator1572Optionality);
        addCVRemoveFinancialDisclosureFromProtocol(formsTab, getProtocol(), newSubInvestigator1572Optionality);
        checkFormsTableValues(formsTab, newSubInvestigator1572Optionality);
        checkInvestigatorForms();
        checkSubInvestigatorForms();
    }

    private void checkForRequiredFormOptionalities(RegistrationFormsTab formsTab) {
        final EditFormsDialog editDialog = formsTab.clickEdit();
        check1572AllowableOptionalities(editDialog);
        editDialog.getHelper().getListing(form1572Type).selectInvestigatorOptionality(FormOptionality.OPTIONAL);
        editDialog.getHelper().getListing(form1572Type).selectSubinvestigatorOptionality(FormOptionality.NONE);
        editDialog.getHelper().getListing(financialDisclosureType).selectInvestigatorOptionality(FormOptionality.OPTIONAL);
        editDialog.getHelper().getListing(financialDisclosureType).selectSubinvestigatorOptionality(FormOptionality.NONE);
        ExpectedValidationFailure failure = new ExpectedValidationFailure();
        failure.addExpectedMessage("protocol.change.comment.required");
        failure.addExpectedMessage("protocol.investigator.optionalities.require.one.required.form");
        failure.addExpectedMessage("protocol.subinvestigator.optionalities.require.one.required.or.optional.form");
        failure.assertFailureOccurs(new FailingAction() {
            public void perform() {
                editDialog.clickSave();
            }
        });
        editDialog.clickCancel();
    }

    private void check1572AllowableOptionalities(EditFormsDialog editDialog) {
        FormListing listing = editDialog.getHelper().getListing(form1572Type);
        List<String> selectableInvestigatorOptionalities = listing.getSelectableInvestigatorOptionalities();
        ArrayList<String> expectedAllowableOptionalities = Lists.newArrayList(FormOptionality.REQUIRED.getDisplay(),
                FormOptionality.NONE.getDisplay(), FormOptionality.OPTIONAL.getDisplay());
        assertEquals(expectedAllowableOptionalities.size(), selectableInvestigatorOptionalities.size());
        assertTrue(selectableInvestigatorOptionalities.containsAll(expectedAllowableOptionalities));

        List<String> selectableSubinvestigatorOptionalities = listing.getSelectableSubinvestigatorOptionalities();
        expectedAllowableOptionalities.remove(FormOptionality.REQUIRED.getDisplay());
        assertEquals(expectedAllowableOptionalities.size(), selectableSubinvestigatorOptionalities.size());
        assertTrue(selectableSubinvestigatorOptionalities.containsAll(expectedAllowableOptionalities));
    }

    private void addCVRemoveFinancialDisclosureFromProtocol(RegistrationFormsTab formsTab, Protocol protocol,
            FormOptionality newSubinvestigatorOptionality) {
        assertNull(formsTab.getHelper().getListing(additionalAttachmentsType));
        EditFormsDialog editDialog = formsTab.clickEdit();
        assertNull(editDialog.getHelper().getListing(additionalAttachmentsType));
        editDialog.selectFormType(curriculumVitaeType.getName());
        editDialog.clickAdd();
        editDialog.getHelper().getListing(financialDisclosureType).clickRemove();
        editDialog.getHelper().getListing(form1572Type).selectSubinvestigatorOptionality(newSubinvestigatorOptionality);
        checkCantSaveWithoutComment(editDialog);
        editDialog.typeComments("some comment");
        editDialog.clickSave();
    }

    private void checkCantSaveWithoutComment(final EditFormsDialog editDialog) {
        new ExpectedValidationFailure("protocol.change.comment.required").assertFailureOccurs(
            new FailingAction() {
                @Override
                public void perform() {
                    editDialog.clickSave();
                }
            }
        );
    }

    private void checkFormsTableValues(RegistrationFormsTab formsTab, FormOptionality newSubInvestigator1572Optionality) {
        List<gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.RegistrationFormsTab.FormListing> formListings = formsTab.getListings();
        assertEquals(formListings.toString(), 2, formListings.size());
        gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.RegistrationFormsTab.FormListing cvListing =
                formsTab.getHelper().getListing(curriculumVitaeType);
        assertEquals(curriculumVitaeType.getDescription(), cvListing.getName());
        assertEquals(curriculumVitaeType.getInvestigatorDefault().getDisplay(), cvListing.getInvestigatorOptionality());
        assertEquals(curriculumVitaeType.getSubinvestigatorDefault().getDisplay(), cvListing.getSubinvestigatorOptionality());
        gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.RegistrationFormsTab.FormListing form1572Listing =
                formsTab.getHelper().getListing(form1572Type);
        assertEquals(form1572Type.getDescription(), form1572Listing.getName());
        assertEquals(form1572Type.getInvestigatorDefault().getDisplay(), form1572Listing.getInvestigatorOptionality());
        assertEquals(newSubInvestigator1572Optionality.getDisplay(), form1572Listing.getSubinvestigatorOptionality());
    }

    private void checkInvestigatorForms() {
        BrowseRegistrationsPage registrationsPage = openHomePage(getPrimaryInvestigatorLogin()).getInvestigatorMenu().clickProtocolRegistrations();
        RegistrationOverviewTab overviewTab = registrationsPage.getHelper().clickRegistrationLink(getPrimaryInvestigatorRegistration());
        List<gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab.FormListing> listedForms = overviewTab.getFormListings();
        assertEquals(3, listedForms.size());
        assertEquals(form1572Type.getInvestigatorDefault().getDisplay(),
                overviewTab.getHelper().getFormListing(form1572Type).getOptionality());
        assertEquals(curriculumVitaeType.getInvestigatorDefault().getDisplay(),
                overviewTab.getHelper().getFormListing(curriculumVitaeType).getOptionality());
        assertEquals(FormOptionality.SUPPLEMENTARY.getDisplay(),
                overviewTab.getHelper().getFormListing(additionalAttachmentsType).getOptionality());
        assertNull(overviewTab.getHelper().getFormListing(financialDisclosureType));
        assertNull(overviewTab.getHelper().getFormListing(hrcType));

        checkFormsRemovedInDatabase(getPrimaryInvestigatorRegistration());
    }

    private void checkFormsRemovedInDatabase(AbstractProtocolRegistration registration) {
        registration = dataSet.reloadObject(registration);
        assertNull(registration.getFinancialDisclosure());
        assertNull(registration.getHumanResearchCertificateForm());
    }

    private void checkSubInvestigatorForms() {
        BrowseRegistrationsPage registrationsPage = openHomePage(getSubinvestigatorLogin()).getInvestigatorMenu().clickProtocolRegistrations();
        RegistrationOverviewTab overviewTab = registrationsPage.getHelper().clickRegistrationLink(getSubinvestigatorRegistration());

        List<gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab.FormListing> listedForms = overviewTab.getFormListings();
        assertEquals(3, listedForms.size());
        assertEquals(form1572Type.getSubinvestigatorDefault().getDisplay(),
                overviewTab.getHelper().getFormListing(form1572Type).getOptionality());
        assertEquals(curriculumVitaeType.getSubinvestigatorDefault().getDisplay(),
                overviewTab.getHelper().getFormListing(curriculumVitaeType).getOptionality());
        assertEquals(FormOptionality.SUPPLEMENTARY.getDisplay(),
                overviewTab.getHelper().getFormListing(additionalAttachmentsType).getOptionality());
        assertNull(overviewTab.getHelper().getFormListing(financialDisclosureType));
        assertNull(overviewTab.getHelper().getFormListing(hrcType));

        checkFormsRemovedInDatabase(getSubinvestigatorRegistration());
    }
}
