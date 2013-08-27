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
package gov.nih.nci.firebird.selenium2.tests.sponsor.annual.registration;

import static gov.nih.nci.firebird.data.FormOptionality.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.cagrid.GridInvocationException;
import gov.nih.nci.firebird.data.AnnualRegistrationConfiguration;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.base.ValidationException;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.EditAnnual1572FormDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.EditAnnualRegistrationConfigurationPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.EditAnnualRegistrationConfigurationPage.RegistrationFormListing;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.EditAnnualRegistrationConfigurationPageHelper;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.AnnualRegistrationConfigurationTestDataSet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

public class EditAnnualRegistrationConfigurationTest extends AbstractFirebirdWebDriverTest {

    private LoginAccount sponsorRepresentativeLogin = LoginAccount.SponsorLogin.fbcisponsor1;
    private LoginAccount sponsorDelegateLogin = LoginAccount.SponsorDelegateLogin.fbcidel1;
    private AnnualRegistrationConfigurationTestDataSet dataSet;
    private List<FormType> standardConfigureableForms;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dataSet = AnnualRegistrationConfigurationTestDataSet.createWithSponsorDelegate(getDataLoader(),
                getGridResources(), sponsorRepresentativeLogin, sponsorDelegateLogin);
        standardConfigureableForms = dataSet.getStandardConfigureableForms();
    }

    @Test
    public void testDelegateCantAccess() throws GridInvocationException {
        checkAnnualRegistrationsAccess(sponsorDelegateLogin, false);
    }

    @Test
    public void testEditConfiguration() throws GridInvocationException {
        HomePage homePage = checkAnnualRegistrationsAccess(sponsorRepresentativeLogin, true);
        AnnualRegistrationConfiguration originalConfiguration = dataSet.getAnnualRegistrationConfiguration();
        EditAnnualRegistrationConfigurationPage annualRegistrationConfigurationPage = homePage
                .getAnnualRegistrationsMenu().clickRequiredForms();
        checkAddAndRemoveForm(annualRegistrationConfigurationPage);
        checkAddAllForms(annualRegistrationConfigurationPage);
        checkEditFormOptionalities(annualRegistrationConfigurationPage);
        checkRemoveAllForms(annualRegistrationConfigurationPage);
        dataSet.reload();
        AnnualRegistrationConfiguration updatedConfiguration = dataSet.getAnnualRegistrationConfiguration();
        assertFalse(originalConfiguration.getId().equals(updatedConfiguration.getId()));
    }
    
    @Test
    public void testDownloadFormSamples() throws GridInvocationException, IOException {
        HomePage homePage = openHomePage(sponsorRepresentativeLogin, getCtepProvider());
        EditAnnualRegistrationConfigurationPage annualRegistrationConfigurationPage = homePage
                .getAnnualRegistrationsMenu().clickRequiredForms();
        checkAddAllForms(annualRegistrationConfigurationPage);
        for (RegistrationFormListing listing : annualRegistrationConfigurationPage.getListings()) {
            assertTrue(listing.clickDownloadSample().length() > 0);
        }

    }

    @Test
    public void testEditForm1572() throws GridInvocationException {
        HomePage homePage = checkAnnualRegistrationsAccess(sponsorRepresentativeLogin, true);
        AnnualRegistrationConfiguration originalConfiguration = dataSet.getAnnualRegistrationConfiguration();
        EditAnnualRegistrationConfigurationPage annualRegistrationConfigurationPage = homePage
                .getAnnualRegistrationsMenu().clickRequiredForms();

        FormType form1572 = getFormType(FormTypeEnum.CTEP_FORM_1572);
        String sampleProtocolText = ValueGenerator.getUniqueString(
                                AnnualRegistrationConfiguration.MAX_PROTOCOL_TEXT_FIELD_LENGTH);
        String subinvestigatorText =  ValueGenerator.getUniqueString(
                                        AnnualRegistrationConfiguration.MAX_SUB_INVESTIGATOR_TEXT_FIELD_LENGTH);
        annualRegistrationConfigurationPage.getHelper().addForm(form1572, OPTIONAL);
        annualRegistrationConfigurationPage.clickSaveButton();

        EditAnnual1572FormDialog editAnnual1572FormDialog = annualRegistrationConfigurationPage.
                getHelper().getListing(form1572).clickEditButton();
        editAnnual1572FormDialog.typeProtocolText(sampleProtocolText);
        editAnnual1572FormDialog.typeSubInvestigatorText(subinvestigatorText);
        editAnnual1572FormDialog.clickSaveButton();

        annualRegistrationConfigurationPage.clickSaveButton();

        dataSet.reload();
        AnnualRegistrationConfiguration updatedConfiguration = dataSet.getAnnualRegistrationConfiguration();
        assertFalse(originalConfiguration.getId().equals(updatedConfiguration.getId()));
        editAnnual1572FormDialog = annualRegistrationConfigurationPage.getHelper().getListing(form1572).clickEditButton();
        assertEquals(sampleProtocolText,editAnnual1572FormDialog.getProtocolTextInput());
        assertEquals(subinvestigatorText,editAnnual1572FormDialog.getSubInvestigatorTextInput());
    }

    private FormType getFormType(final FormTypeEnum formTypeEnum) {
        return Iterables.getOnlyElement(
            Collections2.filter(standardConfigureableForms, new Predicate<FormType>() {
                @Override
                public boolean apply(FormType formType) {
                    return formTypeEnum.equals(formType.getFormTypeEnum());
                }
            })
        );
    }

    @Test(expected = ValidationException.class)
    public void testEditForm1572_validationErrors() throws GridInvocationException {
        HomePage homePage = checkAnnualRegistrationsAccess(sponsorRepresentativeLogin, true);
        EditAnnualRegistrationConfigurationPage annualRegistrationConfigurationPage = homePage
                .getAnnualRegistrationsMenu().clickRequiredForms();

        FormType form1572 = getFormType(FormTypeEnum.CTEP_FORM_1572);

        annualRegistrationConfigurationPage.getHelper().addForm(form1572, OPTIONAL);
        annualRegistrationConfigurationPage.clickSaveButton();

        EditAnnual1572FormDialog editAnnual1572FormDialog = annualRegistrationConfigurationPage.
                getHelper().getListing(form1572).clickEditButton();
        editAnnual1572FormDialog.typeProtocolText(ValueGenerator.getUniqueString(
                        AnnualRegistrationConfiguration.MAX_PROTOCOL_TEXT_FIELD_LENGTH + 1));
        editAnnual1572FormDialog.typeSubInvestigatorText(ValueGenerator.getUniqueString(
                        AnnualRegistrationConfiguration.MAX_SUB_INVESTIGATOR_TEXT_FIELD_LENGTH + 1));
        editAnnual1572FormDialog.clickSaveButton();

    }

    private HomePage checkAnnualRegistrationsAccess(LoginAccount loginAccount, boolean shouldHaveAccess) throws GridInvocationException {
        HomePage homePage = openLoginPage().getHelper().goToHomePage(loginAccount, getCtepProvider());
        assertEquals(shouldHaveAccess, homePage.getAnnualRegistrationsMenu().isRequiredFormsMenuItemPresent());
        return homePage;
    }

    private void checkAddAndRemoveForm(EditAnnualRegistrationConfigurationPage annualRegistrationConfigurationPage) {
        assertTrue(annualRegistrationConfigurationPage.getListings().isEmpty());
        assertTrue(getSavedFormOptionalities().isEmpty());

        FormType formType = standardConfigureableForms.get(0);
        annualRegistrationConfigurationPage.getHelper().addForm(formType, OPTIONAL);
        annualRegistrationConfigurationPage.clickSaveButton();
        assertEquals(OPTIONAL, getSavedFormOptionalities().get(formType));

        annualRegistrationConfigurationPage.getHelper().getListing(formType).clickRemoveButton();
        assertTrue(annualRegistrationConfigurationPage.getListings().isEmpty());
        annualRegistrationConfigurationPage.clickSaveButton();
        assertTrue(getSavedFormOptionalities().isEmpty());
    }

    private Map<FormType, FormOptionality> getSavedFormOptionalities() {
        return dataSet.getLastCreatedObject(AnnualRegistrationConfiguration.class).getFormSetConfiguration()
                .getFormOptionalities();
    }

    private void checkAddAllForms(EditAnnualRegistrationConfigurationPage annualRegistrationConfigurationPage) {
        EditAnnualRegistrationConfigurationPageHelper helper = annualRegistrationConfigurationPage.getHelper();
        helper.addForm(standardConfigureableForms.get(0), OPTIONAL);
        helper.addForm(standardConfigureableForms.get(1), REQUIRED);
        helper.addForm(standardConfigureableForms.get(2), REQUIRED);
        annualRegistrationConfigurationPage.clickSaveButton();

        assertEquals(OPTIONAL, helper.getListing(standardConfigureableForms.get(0)).getOptionality());
        assertEquals(REQUIRED, helper.getListing(standardConfigureableForms.get(1)).getOptionality());
        assertEquals(REQUIRED, helper.getListing(standardConfigureableForms.get(2)).getOptionality());

        Map<FormType, FormOptionality> savedOptionalities = getSavedFormOptionalities();
        assertEquals(OPTIONAL, savedOptionalities.get(standardConfigureableForms.get(0)));
        assertEquals(REQUIRED, savedOptionalities.get(standardConfigureableForms.get(1)));
        assertEquals(REQUIRED, savedOptionalities.get(standardConfigureableForms.get(2)));
    }

    private void checkEditFormOptionalities(EditAnnualRegistrationConfigurationPage annualRegistrationConfigurationPage) {
        for (RegistrationFormListing listing : annualRegistrationConfigurationPage.getListings()) {
            listing.setOptionality(REQUIRED);
        }
        annualRegistrationConfigurationPage.clickSaveButton();
        for (FormOptionality optionality : getSavedFormOptionalities().values()) {
            assertEquals(REQUIRED, optionality);
        }
    }

    private void checkRemoveAllForms(EditAnnualRegistrationConfigurationPage annualRegistrationConfigurationPage) {
        for (RegistrationFormListing listing : annualRegistrationConfigurationPage.getListings()) {
            listing.clickRemoveButton();
        }
        annualRegistrationConfigurationPage.clickSaveButton();
        assertTrue(annualRegistrationConfigurationPage.getListings().isEmpty());
        assertTrue(getSavedFormOptionalities().isEmpty());
    }
}
