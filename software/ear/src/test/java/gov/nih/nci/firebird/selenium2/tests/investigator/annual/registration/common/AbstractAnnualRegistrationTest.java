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
package gov.nih.nci.firebird.selenium2.tests.investigator.annual.registration.common;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.common.FirebirdDateUtils;
import gov.nih.nci.firebird.commons.selenium2.util.WaitUtils;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.AnnualRegistrationConfiguration;
import gov.nih.nci.firebird.data.AnnualRegistrationType;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AbstractAnnualRegistrationTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AnnualAdditionalAttachmentsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AnnualForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.BrowseAnnualRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.BrowseAnnualRegistrationsPage.RegistrationListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.OverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.SupplementalInvestigatorDataFormTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.InvestigatorRegistrationFormTablesTag;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.InvestigatorRegistrationFormTablesTag.FormListing;
import gov.nih.nci.firebird.test.LoginAccount.SponsorDelegateLogin;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;
import gov.nih.nci.firebird.test.util.FirebirdPropertyUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import com.google.common.collect.Iterables;

public abstract class AbstractAnnualRegistrationTest extends AbstractFirebirdWebDriverTest {

    @Test
    public void testBrowseRegistrations_NoExistingRegistration() {
        DataSet dataSet = createAnnualRegistrationConfigurationDataSet();
        assertTrue(dataSet.getInvestigator().getInvestigatorRole().getProfile().getAnnualRegistrations().isEmpty());
        BrowseAnnualRegistrationsPage annualRegistrationsPage = createInitialRegistration(dataSet);
        AnnualRegistration registration = Iterables.getOnlyElement(dataSet.getInvestigator().getInvestigatorRole()
                .getProfile().getAnnualRegistrations());
        checkCreatedRegistrationAgainstConfiguration(registration, dataSet.getAnnualRegistrationConfiguration());
        checkRegistrationDisplayed(annualRegistrationsPage, registration);
    }

    private BrowseAnnualRegistrationsPage createInitialRegistration(DataSet dataSet) {
        BrowseAnnualRegistrationsPage annualRegistrationsPage = openRegistrationsPage(dataSet);
        assertTrue(annualRegistrationsPage.getRegistrationListings().isEmpty());
        annualRegistrationsPage.clickCreateRegistration();
        assertEquals(1, annualRegistrationsPage.getRegistrationListings().size());
        dataSet.reload();
        return annualRegistrationsPage;
    }

    abstract protected BrowseAnnualRegistrationsPage openRegistrationsPage(DataSet dataSet);

    protected DataSet createAnnualRegistrationConfigurationDataSet() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        FirebirdUser investigator = builder.createInvestigator().asCtepUser().get();
        builder.createSponsor().asCtepUser();
        builder.createSponsor().asCtepUser().asDelegate().withLogin(SponsorDelegateLogin.fbcidel1);
        configureAnnualRegistrationConfigurationDataSet(builder, investigator);
        DataSet dataSet = builder.build();
        return dataSet;
    }

    protected void configureAnnualRegistrationConfigurationDataSet(DataSetBuilder builder, FirebirdUser investigator) {
        builder.createAnnualRegistrationConfiguration().get();
    }

    private void checkCreatedRegistrationAgainstConfiguration(AnnualRegistration registration,
            AnnualRegistrationConfiguration configuration) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            assertEquals(configuration.getFormSetConfiguration().getOptionality(form.getFormType()),
                    form.getFormOptionality());
        }
    }

    private void checkRegistrationDisplayed(BrowseAnnualRegistrationsPage annualRegistrationsPage,
            AnnualRegistration registration) {
        RegistrationListing listing = annualRegistrationsPage.getHelper().getRegistrationListing(registration);
        annualRegistrationsPage.getHelper().assertEquivalent(registration, listing);
        for (AbstractRegistrationForm form : registration.getForms()) {
            BrowseAnnualRegistrationsPage.FormListing formListing = annualRegistrationsPage.getHelper().getFormListing(
                    form);
            annualRegistrationsPage.getHelper().assertEquivalent(form, formListing);
            checkFormLink(formListing, form);
        }
    }

    private void checkFormLink(BrowseAnnualRegistrationsPage.FormListing formListing, AbstractRegistrationForm form) {
        AbstractAnnualRegistrationTab<?> tab = formListing.clickFormLink();
        assertEquals(form.getFormType().getFormTypeEnum(), tab.getFormType());
        tab.getPage().clickBack();
    }

    @Test
    public void testBrowsePage() throws ParseException {
        DataSet dataSet = createAnnualRegistrationConfigurationDataSet();
        createInitialRegistration(dataSet);

        checkRegistrationListing(dataSet);

        AnnualRegistration annualRegistration = (AnnualRegistration) dataSet.getInvestigator().getInvestigatorRole()
                .getProfile().getAnnualRegistrations().toArray()[0];
        annualRegistration.setStatus(RegistrationStatus.SUBMITTED);
        dataSet.update(annualRegistration);
        checkRegistrationListing(dataSet);
    }

    private void checkRegistrationListing(DataSet dataSet) throws ParseException {
        BrowseAnnualRegistrationsPage annualRegistrationsPage = openRegistrationsPage(dataSet);

        Set<AnnualRegistration> annualRegistrationSet = dataSet.getInvestigator().getInvestigatorRole().getProfile()
                .getAnnualRegistrations();

        for (AnnualRegistration registration : annualRegistrationSet) {
            RegistrationListing registrationListing = annualRegistrationsPage.getHelper().getRegistrationListing(
                    registration);

            assertEquals(registration.getId(), registrationListing.getId());
            assertEquals(registration.getAnnualRegistrationType(), registrationListing.getType());
            assertEquals(registration.getStatus(), registrationListing.getStatus());

            assertDateEqualsString(registration.getDueDate(), registrationListing.getDueDate());

            assertDateEqualsString(registration.getLastSubmissionDate(), registrationListing.getSubmissionDate());

            if (registration.isSubmittable()) {
                assertEquals("Registration is not complete and Edit button is Not visible", "Edit",
                        registrationListing.getEditButtonLabel());
            } else {
                assertEquals("Registration is complete and View button is Not visible", "View",
                        registrationListing.getEditButtonLabel());
            }
        }
    }

    public void assertDateEqualsString(Date expected, String actual) throws ParseException {
        if (expected != null) {
            assertEquals("Dates do not match", FirebirdDateUtils.getAsMonthDateAndYearOrEmptyString(expected), actual);
        }
    }

    @Test
    public void testOverviewTabWithForms() {
        DataSet dataSet = createRegistrationDataSet();
        BrowseAnnualRegistrationsPage annualRegistrationsPage = openRegistrationsPage(dataSet);
        AnnualRegistration registration = reloadRegistration(dataSet);
        RegistrationListing registrationListing = annualRegistrationsPage.getHelper().getRegistrationListing(
                registration);

        OverviewTab overviewTab = registrationListing.clickEditButton();
        registration = reloadRegistration(dataSet);
        overviewTab.getHelper().checkProgressBar(1, 3);

        List<InvestigatorRegistrationFormTablesTag.FormListing> forms = overviewTab.getFormsListing();
        assertEquals("Not all forms are listed on the Overview tab", registration.getForms().size(), forms.size());

        for (AbstractRegistrationForm form : registration.getForms()) {
            FormListing formListing = overviewTab.getHelper().getListing(form);
            assertEquals(form.getFormStatus().getDisplay(), formListing.getFormStatus());
            checkFormLink(formListing, form);
        }
        checkNextButtons(overviewTab, registration);
    }

    private AnnualRegistration reloadRegistration(DataSet dataSet) {
        dataSet.reload();
        return dataSet.getAnnualRegistrations().get(1);
    }

    private DataSet createRegistrationDataSet() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        InvestigatorProfile profile = builder.createProfile().complete().get();
        FirebirdUser investigator = builder.createInvestigator(profile).asCtepUser().get();
        builder.createSponsor().asCtepUser();
        builder.createSponsor().asCtepUser().asDelegate().withLogin(SponsorDelegateLogin.fbcidel1);
        configureRegistrationDataSet(builder, investigator);
        return builder.build();
    }

    protected void configureRegistrationDataSet(DataSetBuilder builder, FirebirdUser investigator) {
        Date now = new Date();
        Date initialRegistrationDate = DateUtils.addMonths(now, -11);
        Date renewalDueDate = DateUtils.addYears(initialRegistrationDate, 1);
        AnnualRegistration initialRegistration = builder.createAnnualRegistration(investigator)
                .withType(AnnualRegistrationType.INITIAL).withStatus(RegistrationStatus.APPROVED)
                .withStatusDate(initialRegistrationDate).complete().get();
        builder.createAnnualRegistration(investigator).renewalOf(initialRegistration).withDueDate(renewalDueDate);
    }

    private void checkFormLink(FormListing formListing, AbstractRegistrationForm form) {
        AbstractAnnualRegistrationTab<?> tab = (AbstractAnnualRegistrationTab<?>) formListing.click();
        assertEquals(form.getFormType().getFormTypeEnum(), tab.getFormType());
        WaitUtils.pause(100);
        tab.getPage().clickOverviewTab();
        WaitUtils.pause(100);
    }

    private void checkNextButtons(OverviewTab overviewTab, AnnualRegistration registration) {
        AnnualForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        assertEquals(getExpectedNextButtonText(registration.getFinancialDisclosure()), form1572Tab.getNextButtonText());

        WaitUtils.pause(400);
        FinancialDisclosureTab financialDisclosureTab = (FinancialDisclosureTab) form1572Tab.clickNextButton();
        assertEquals(getExpectedNextButtonText(registration.getSupplementalInvestigatorDataForm()),
                financialDisclosureTab.getNextButtonText());

        SupplementalInvestigatorDataFormTab supplementalInvestigatorDataForm = (SupplementalInvestigatorDataFormTab) financialDisclosureTab
                .clickNextButton();
        assertEquals(getExpectedNextButtonText(registration.getAdditionalAttachmentsForm()),
                supplementalInvestigatorDataForm.getNextButtonText());

        AnnualAdditionalAttachmentsTab additionalAttachmentsTab = (AnnualAdditionalAttachmentsTab) supplementalInvestigatorDataForm
                .clickNextButton();
        assertFalse(additionalAttachmentsTab.isNextButtonPresent());
    }

    private String getExpectedNextButtonText(AbstractRegistrationForm form) {
        return FirebirdPropertyUtils.getPropertyText("button.next") + " (" + form.getFormType().getName() + ")";
    }

    @Test
    public void testOverviewTabWithNoForms() {
        DataSet dataSet = createRegistrationDataSetWithNoForms();
        BrowseAnnualRegistrationsPage annualRegistrationsPage = openRegistrationsPage(dataSet);

        AnnualRegistration registration = dataSet.getAnnualRegistration();
        RegistrationListing registrationListing = annualRegistrationsPage.getHelper().getRegistrationListing(
                registration);

        OverviewTab overviewTab = registrationListing.clickEditButton();
        overviewTab.getHelper().checkProgressBar(0, 0);
        assertTrue("Unexpected data in Overview Tab", overviewTab.getFormsListing().isEmpty());
    }

    private DataSet createRegistrationDataSetWithNoForms() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        FirebirdUser investigator = builder.createInvestigator().asCtepUser().get();
        builder.createSponsor().asCtepUser();
        builder.createSponsor().asCtepUser().asDelegate().withLogin(SponsorDelegateLogin.fbcidel1);
        configureRegistrationDataSetWithNoForms(builder, investigator);
        return builder.build();
    }

    protected void configureRegistrationDataSetWithNoForms(DataSetBuilder builder, FirebirdUser investigator) {
        AnnualRegistrationConfiguration configuration = builder.createAnnualRegistrationConfiguration().withNoForms()
                .get();
        AnnualRegistration initial = builder.createAnnualRegistration(investigator, configuration)
                .withType(AnnualRegistrationType.INITIAL).get();
        builder.createAnnualRegistration(investigator, configuration).renewalOf(initial);
    }

    @Test
    public void testBrowseRegistrations_ExistingRegistrations() {
        DataSet dataSet = createRegistrationDataSet();
        BrowseAnnualRegistrationsPage annualRegistrationsPage = openRegistrationsPage(dataSet);
        assertEquals(AnnualRegistrationType.INITIAL, annualRegistrationsPage.getRegistrationListings().get(0).getType());
        checkRegistrationDisplayed(annualRegistrationsPage, dataSet.getAnnualRegistrations().get(0));
        checkRegistrationDisplayed(annualRegistrationsPage, dataSet.getAnnualRegistrations().get(1));
    }

}
