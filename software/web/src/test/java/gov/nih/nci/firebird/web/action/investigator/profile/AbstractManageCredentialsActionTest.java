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
package gov.nih.nci.firebird.web.action.investigator.profile;

import static gov.nih.nci.firebird.data.CredentialType.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.State;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.organization.InvalidatedOrganizationException;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class AbstractManageCredentialsActionTest extends AbstractWebTest {

    @Inject
    private InvestigatorProfileService mockProfileService;
    @Inject
    private OrganizationService mockOrganizationService;
    @Inject
    private StateLookupService mockStateLookupService;
    @Inject
    private CountryLookupService mockCountryLookupService;
    @Inject
    private TestAbstractManageCredentialsAction action;
    private InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
    private AbstractCredential<?> credential = CredentialFactory.getInstance().createDegree();
    private Organization issuer = OrganizationFactory.getInstance().create();
    private ValidationException validationException;
    private static final String ERROR_MESSAGE = "This is an error";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        FirebirdWebTestUtility.setUpGridSessionInformation(getMockSession(), "TESTING");
        action.setUserService(FirebirdWebTestUtility.setupUserServiceReturnProfile(profile));
        credential.setId(1L);
        profile.addCredential(credential);
        action.setProfile(profile);
        action.setServletRequest(getMockRequest());
        action.setPage(DEGREE.name());
        when(mockOrganizationService.getByExternalId(issuer.getExternalId())).thenReturn(issuer);
        ValidationResult result = new ValidationResult(new ValidationFailure(ERROR_MESSAGE));
        validationException = new ValidationException(result);
    }

    @Test
    public void testPrepare_NoCredentialNoId() {
        action.setCredential(null);
        action.prepare();
        assertNull(action.getCredential());
        assertNull(action.getEffectiveDate());
        assertNull(action.getExpirationDate());
        assertTrue(action.getExistingCredentials().isEmpty());
    }

    @Test
    public void testPrepare_setCredential() {
        action.setCredential(credential);
        credential.setIssuer(null);
        action.prepare();
        assertNull(credential.getIssuer());
        assertEquals(credential, action.getCredential());
        assertTrue(action.getExistingCredentials().contains(credential));
    }

    @Test
    public void testPrepare_NoIssuerLookup() {
        action.setCredential(credential);
        credential.setIssuer(new Organization());
        action.setIssuingOrganizationExternalId(null);
        action.prepare();
        assertNotSame(credential.getIssuer(), issuer);
        assertTrue(action.getExistingCredentials().contains(credential));
    }

    @Test
    public void testPrepare_UnavailableIssuer() throws InvalidatedOrganizationException {
        String key = "NES1";
        when(mockOrganizationService.getByExternalId(key)).thenThrow(new InvalidatedOrganizationException());
        action.setCredential(credential);
        credential.setIssuer(new Organization());
        action.setIssuingOrganizationExternalId(key);
        action.prepare();
        assertTrue(action.hasActionErrors());
        assertTrue(action.getExistingCredentials().contains(credential));
    }

    @Test
    public void testPrepare_GetSelectedIssuer() {
        action.setCredential(credential);
        action.setIssuingOrganizationExternalId(issuer.getExternalId());
        action.prepare();
        assertEquals(issuer, credential.getIssuer());
        assertTrue(action.getExistingCredentials().contains(credential));
    }

    @Test
    public void testPrepare_CredentialIdSet() {
        action.setCredential(null);
        action.setId(1L);
        action.prepare();
        assertEquals(credential, action.getCredential());
        assertTrue(action.getExistingCredentials().contains(credential));
    }

    @Test
    public void testPrepare_CredentialDatesNotSet() {
        action.setCredential(credential);
        credential.setEffectiveDate(null);
        credential.setExpirationDate(null);
        action.prepare();
        assertNull(action.getEffectiveDate());
        assertNull(action.getExpirationDate());
        assertEquals(credential, action.getCredential());
        assertTrue(action.getExistingCredentials().contains(credential));
    }

    @Test
    public void testPrepare_CredentialDatesSet() {
        action.setCredential(credential);
        credential.setEffectiveDate(new Date());
        credential.setExpirationDate(new Date());
        action.prepare();
        assertNotNull(action.getEffectiveDate());
        assertNotNull(action.getExpirationDate());
        assertTrue(action.getExistingCredentials().contains(credential));
    }

    @Test
    public void testSaveCredential() throws CredentialAlreadyExistsException, ValidationException {
        action.setCredential(credential);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveCredential());
        verify(mockProfileService).saveCredential(profile, credential, FormTypeEnum.CV);
    }

    @Test
    public void testSaveCredential_CredentialAlreadyExists() throws CredentialAlreadyExistsException,
            ValidationException {
        doThrow(new CredentialAlreadyExistsException()).when(mockProfileService).saveCredential(profile, credential,
                FormTypeEnum.CV);
        action.setCredential(credential);
        assertEquals(ActionSupport.INPUT, action.saveCredential());
        assertTrue(action.hasActionErrors());
        verifyZeroInteractions(mockOrganizationService);
    }

    @Test(expected = ValidationException.class)
    public void testSaveCredential_NesValidationError() throws IOException, CredentialAlreadyExistsException,
            ValidationException {
        doThrow(validationException).when(mockProfileService).saveCredential(profile, credential, FormTypeEnum.CV);
        action.setCredential(credential);
        action.saveCredential();
    }

    @Test
    public void testDeleteCredential() {
        assertEquals(profile.getCredential(credential.getId()), credential);
        action.setCredential(credential);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.deleteCredential(FormTypeEnum.CV));
        verify(mockProfileService).save(profile, FormTypeEnum.CV);
        assertTrue(profile.getCredentials().isEmpty());
    }

    @Test
    public void testValidateExpirationAfterEffectiveDates_Pass() throws ParseException, ValidationException {
        action.setEffectiveDate("11/2011");
        action.setExpirationDate("12/2011");
        action.validateExpirationAfterEffectiveDates();
    }

    @Test(expected = ValidationException.class)
    public void testValidateExpirationAfterEffectiveDates_ExpitationBeforeEffective() throws ParseException,
            ValidationException {
        action.setEffectiveDate("11/2011");
        action.setExpirationDate("10/2011");
        action.validateExpirationAfterEffectiveDates();
    }

    @Test(expected = ValidationException.class)
    public void testValidateExpirationAfterEffectiveDates_DatesEqual() throws ParseException, ValidationException {
        action.setEffectiveDate("11/2011");
        action.setExpirationDate("11/2011");
        action.validateExpirationAfterEffectiveDates();
    }

    public void testValidateExpirationAfterEffectiveDates_BothDatesNull() throws ParseException, ValidationException {
        try {
            action.setEffectiveDate(null);
            action.setExpirationDate(null);
            action.validateExpirationAfterEffectiveDates();
        } catch (ValidationException e) {
            fail("Shouldn't thow an exception");
        }
    }

    public void testValidateExpirationAfterEffectiveDates_EffectiveDateNull() throws ParseException,
            ValidationException {
        try {
            action.setEffectiveDate("11/2011");
            action.setExpirationDate(null);
            action.validateExpirationAfterEffectiveDates();
        } catch (ValidationException e) {
            fail("Shouldn't thow an exception");
        }
    }

    public void testValidateExpirationAfterEffectiveDates_ExpirationDateNull() throws ParseException,
            ValidationException {
        try {
            action.setEffectiveDate(null);
            action.setExpirationDate("11/2011");
            action.validateExpirationAfterEffectiveDates();
        } catch (ValidationException e) {
            fail("Shouldn't thow an exception");
        }
    }

    @Test(expected = ParseException.class)
    public void testValidateExpirationAfterEffectiveDates_MalFormedExpirationDate() throws ParseException,
            ValidationException {
        action.setEffectiveDate("11/2011");
        action.setExpirationDate("Not a date");
        action.validateExpirationAfterEffectiveDates();
    }

    @Test(expected = ParseException.class)
    public void testValidateExpirationAfterEffectiveDates_MalFormedEffectiveDate() throws ParseException,
            ValidationException {
        action.setEffectiveDate("Not a date");
        action.setExpirationDate("12/2011");
        action.validateExpirationAfterEffectiveDates();
    }

    @Test
    public void testGetStates() throws Exception {
        List<State> states = Lists.newArrayList();
        states.add(new State());
        when(mockStateLookupService.getAll()).thenReturn(states);
        action.prepare();
        assertSame(states, action.getStates());
    }

    @Test
    public void testGetCountries() throws Exception {
        List<Country> countries = Lists.newArrayList();
        countries.add(new Country("", "", "", ""));
        when(mockCountryLookupService.getAll()).thenReturn(countries);
        action.prepare();
        assertSame(countries, action.getCountries());
    }

    @Test
    public void testGetDialogId() throws Exception {
        assertEquals("profileDialog", action.getDialogId());
    }

    @SuppressWarnings("serial")
    private static class TestAbstractManageCredentialsAction extends AbstractManageCredentialsAction {

        @Inject
        public TestAbstractManageCredentialsAction(InvestigatorProfileService profileService,
                GenericDataRetrievalService dataService, StateLookupService stateLookupService,
                CountryLookupService countryLookupService) {
            super(profileService, dataService, stateLookupService, countryLookupService);
        }

    }

}
