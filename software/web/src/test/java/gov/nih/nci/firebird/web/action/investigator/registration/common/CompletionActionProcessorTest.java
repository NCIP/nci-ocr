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
package gov.nih.nci.firebird.web.action.investigator.registration.common;

import static gov.nih.nci.firebird.test.util.FirebirdPropertyUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.cagrid.UserSessionInformationFactory;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.security.Authenticator;
import gov.nih.nci.firebird.security.CredentialsHandlerFactory;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import gov.nih.nci.firebird.test.ValidationExceptionFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.List;
import java.util.Map;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.Iterables;
import com.opensymphony.xwork2.ActionSupport;

public class CompletionActionProcessorTest extends AbstractWebTest {

    private static final String TEST_BASE_USERNAME = "username";
    private static final String TEST_FULLY_QUALIFIED_USERNAME = "/OU=Dorian/CN=username";
    private static final String TEST_PROVIDER_URL = "https://provider";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_INVALID_USERNAME = "/OU=Dorian/CN=invalid_username";
    private static final String TEST_INVALID_PASSWORD = "bad password";

    @Mock
    private ProtocolRegistrationService mockRegistrationService;
    @Mock
    private CredentialsHandlerFactory mockCredentialsHandlerFactory;
    @Mock
    private AbstractRegistrationAction<AbstractProtocolRegistration> mockAction;
    @Mock
    private Authenticator mockAuthenticator;
    private CompletionActionProcessor<AbstractProtocolRegistration> processor;
    private AbstractProtocolRegistration registration = RegistrationFactory.getInstance()
            .createInvestigatorRegistration();
    private String comments = "comments";
    private FirebirdUser user = new FirebirdUser();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        processor = createProcessor(true);
        processor.setRegistration(registration);
        when(mockAction.getRequest()).thenReturn(getMockRequest());
        getMockRequest().setSecure(true);
        setUpForSigning();
    }

    private CompletionActionProcessor<AbstractProtocolRegistration> createProcessor(boolean httpsRequired) {
        CompletionActionProcessor<AbstractProtocolRegistration> processor = new CompletionActionProcessor<AbstractProtocolRegistration>(
                mockRegistrationService, mockCredentialsHandlerFactory, mockAction, httpsRequired);
        processor.setRegistration(registration);
        return processor;
    }

    private void setUpForSigning() throws LoginException {
        user.setUsername(TEST_FULLY_QUALIFIED_USERNAME);
        UserSessionInformation userSessionInformation = UserSessionInformationFactory.getInstance().create(
                TEST_FULLY_QUALIFIED_USERNAME);
        when(mockAction.getCurrentUser()).thenReturn(user);
        userSessionInformation.setIdentityProviderUrl(TEST_PROVIDER_URL);
        FirebirdWebTestUtility.setUpGridSessionInformation(getMockSession(), userSessionInformation);
        when(mockAction.getCurrentGridSessionInformation()).thenReturn(userSessionInformation);
        when(mockCredentialsHandlerFactory.getAuthenticator(TEST_PROVIDER_URL)).thenReturn(mockAuthenticator);
        when(mockAuthenticator.authenticateUser(eq(TEST_BASE_USERNAME), eq(TEST_PASSWORD), eq(TEST_PROVIDER_URL)))
                .thenReturn(userSessionInformation);
        when(
                mockAuthenticator.authenticateUser(eq(TEST_BASE_USERNAME.toUpperCase()), eq(TEST_PASSWORD),
                        eq(TEST_PROVIDER_URL))).thenReturn(userSessionInformation);
        when(
                mockAuthenticator.authenticateUser(eq(TEST_BASE_USERNAME), eq(TEST_INVALID_PASSWORD),
                        eq(TEST_PROVIDER_URL))).thenThrow(new FailedLoginException());
    }

    @Test
    public void testComplete() throws ValidationException {
        when(mockAction.isUserAnApprovedCoordinatorForRegistrationsProfile()).thenReturn(false);
        when(mockAction.isVerifiedForRegistrationSigning()).thenReturn(true);
        getMockRequest().addUserRole(UserRoleType.INVESTIGATOR.getVerifiedGroupName());
        String result = processor.complete(comments);
        assertEquals(CompletionActionProcessor.INVESTIGATOR_COMPLETION_SUCCESS, result);
    }

    @Test
    public void testComplete_InvestigatorNotVerified() throws ValidationException {
        when(mockAction.isUserAnApprovedCoordinatorForRegistrationsProfile()).thenReturn(false);
        String result = processor.complete(comments);
        assertEquals(CompletionActionProcessor.SIGNING_DISABLED, result);
    }

    @Test
    public void testComplete_Insecure_HttpsRequired() throws ValidationException {
        getMockRequest().addUserRole(UserRoleType.INVESTIGATOR.getVerifiedGroupName());
        getMockRequest().setSecure(false);
        when(mockAction.isUserAnApprovedCoordinatorForRegistrationsProfile()).thenReturn(false);
        when(mockAction.isVerifiedForRegistrationSigning()).thenReturn(true);
        String actionError = getPropertyAndSetUpMockForRetrieval("login.insecure");
        assertEquals(ActionSupport.INPUT, processor.complete(comments));
        verify(mockAction).addActionError(actionError);
    }

    private String getPropertyAndSetUpMockForRetrieval(String propertyKey) {
        String property = getPropertyText(propertyKey);
        when(mockAction.getText(propertyKey)).thenReturn(property);
        return property;
    }

    @Test
    public void testComplete_Insecure_HttpsNotRequired() throws ValidationException {
        getMockRequest().addUserRole(UserRoleType.INVESTIGATOR.getVerifiedGroupName());
        getMockRequest().setSecure(false);
        processor = createProcessor(false);
        when(mockAction.isUserAnApprovedCoordinatorForRegistrationsProfile()).thenReturn(false);
        when(mockAction.isVerifiedForRegistrationSigning()).thenReturn(true);
        assertEquals(CompletionActionProcessor.INVESTIGATOR_COMPLETION_SUCCESS, processor.complete(comments));
        verify(mockAction, never()).addActionError(anyString());
    }

    @Test
    public void testComplete_IsCoordinator() throws ValidationException {
        when(mockAction.isUserAnApprovedCoordinatorForRegistrationsProfile()).thenReturn(true);
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        when(mockAction.getCurrentUser()).thenReturn(coordinator);
        assertEquals(CompletionActionProcessor.COORDINATOR_COMPLETION_SUCCESS, processor.complete(comments));
        assertEquals(comments, registration.getCoordinatorComments());
        verify(mockRegistrationService).sendCoordinatorCompletedRegistrationEmail(coordinator, registration);
    }

    @Test
    public void testComplete_IsSponsor() throws ValidationException {
        when(mockAction.isCtepSponsor()).thenReturn(true);
        FirebirdUser sponsor = FirebirdUserFactory.getInstance().create();
        when(mockAction.getCurrentUser()).thenReturn(sponsor);
        assertEquals(CompletionActionProcessor.COORDINATOR_COMPLETION_SUCCESS, processor.complete(comments));
        assertEquals(comments, registration.getSponsorComments());
        verify(mockRegistrationService).sendCoordinatorCompletedRegistrationEmail(sponsor, registration);
    }

    @Test
    public void testComplete_IsSponsorDelegate() throws ValidationException {
        when(mockAction.isCtepSponsorDelegate()).thenReturn(true);
        FirebirdUser sponsorDelegate = FirebirdUserFactory.getInstance().create();
        when(mockAction.getCurrentUser()).thenReturn(sponsorDelegate);
        assertEquals(CompletionActionProcessor.COORDINATOR_COMPLETION_SUCCESS, processor.complete(comments));
        assertEquals(comments, registration.getSponsorComments());
        verify(mockRegistrationService).sendCoordinatorCompletedRegistrationEmail(sponsorDelegate, registration);
    }

    @Test
    public void testSign() throws LoginException {
        assertEquals(ActionSupport.SUCCESS, processor.sign(TEST_BASE_USERNAME, TEST_PASSWORD, null));
        verify(mockRegistrationService).signAndSubmit(registration, user, TEST_PASSWORD);
        verify(mockAuthenticator).authenticateUser(TEST_BASE_USERNAME, TEST_PASSWORD, TEST_PROVIDER_URL);
        assertNull(registration.getInvestigatorComments());
    }

    @Test
    public void testSign_WithComments() throws LoginException {
        String comments = "These are sample comments";
        assertEquals(ActionSupport.SUCCESS, processor.sign(TEST_BASE_USERNAME, TEST_PASSWORD, comments));
        verify(mockRegistrationService).signAndSubmit(registration, user, TEST_PASSWORD);
        verify(mockAuthenticator).authenticateUser(TEST_BASE_USERNAME, TEST_PASSWORD, TEST_PROVIDER_URL);
        assertEquals(comments, registration.getInvestigatorComments());
    }

    @Test
    public void testSign_InvalidPassword() throws FailedLoginException {
        String actionError = getPropertyAndSetUpMockForRetrieval("authentication.invalid.credentials");
        assertEquals(ActionSupport.INPUT, processor.sign(TEST_BASE_USERNAME, TEST_INVALID_PASSWORD, comments));
        verify(mockAction).addActionError(actionError);
    }

    @Test
    public void testSign_InvalidUsername() throws FailedLoginException {
        String actionError = getPropertyAndSetUpMockForRetrieval("authentication.invalid.credentials");
        assertEquals(ActionSupport.INPUT, processor.sign(TEST_INVALID_USERNAME, TEST_PASSWORD, comments));
        verify(mockAction).addActionError(actionError);
    }

    @Test
    public void testSign_UppercaseUsername() throws LoginException {
        assertEquals(ActionSupport.SUCCESS, processor.sign(TEST_BASE_USERNAME.toUpperCase(), TEST_PASSWORD, comments));
        verify(mockRegistrationService).signAndSubmit(registration, user, TEST_PASSWORD);
        verify(mockAuthenticator).authenticateUser(TEST_BASE_USERNAME.toUpperCase(), TEST_PASSWORD, TEST_PROVIDER_URL);
    }

    @Test
    public void testSign_LoginError() throws LoginException {
        LoginException mockLoginException = mock(LoginException.class);
        when(mockAuthenticator.authenticateUser(TEST_BASE_USERNAME, TEST_PASSWORD, TEST_PROVIDER_URL)).thenThrow(
                mockLoginException);
        String actionError = getPropertyAndSetUpMockForRetrieval("authentication.error");
        assertEquals(ActionSupport.INPUT, processor.sign(TEST_BASE_USERNAME, TEST_PASSWORD, comments));
        verify(mockAction).addActionError(actionError);
    }

    @Test
    public void testSign_Insecure_HttpsRequired() throws FailedLoginException {
        getMockRequest().setSecure(false);
        String actionError = getPropertyAndSetUpMockForRetrieval("login.insecure");
        assertEquals(ActionSupport.INPUT, processor.sign(TEST_BASE_USERNAME, TEST_PASSWORD, comments));
        verify(mockAction).addActionError(actionError);
    }

    @Test
    public void testSign_Insecure_HttpsNotRequired() throws FailedLoginException {
        processor = createProcessor(false);
        getMockRequest().setSecure(false);
        assertEquals(ActionSupport.SUCCESS, processor.sign(TEST_BASE_USERNAME, TEST_PASSWORD, comments));
        verify(mockAction, never()).addActionError(anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetDocumentsJson() throws JSONException {
        setUpRegistrationForFormJsonTest();
        String json = processor.getDocumentsJson();
        List<Map<String, Object>> jsonModel = (List<Map<String, Object>>) JSONUtil.deserialize(json);
        checkJsonObjects(jsonModel);
    }

    private void setUpRegistrationForFormJsonTest() {
        setUpExpectedSigningConfiguration();
        registration.getFinancialDisclosure().getSupportingDocumentation()
                .add(FirebirdFileFactory.getInstance().create());
        registration.getAdditionalAttachmentsForm().getAdditionalAttachments()
                .add(FirebirdFileFactory.getInstance().create());
        TrainingCertificate certificate = new TrainingCertificate(null, null, null,
                CertificateType.HUMAN_RESEARCH_CERTIFICATE, FirebirdFileFactory.getInstance().create());
        registration.getHumanResearchCertificateForm().selectCertificate(certificate);
    }

    private void setUpExpectedSigningConfiguration() {
        registration.getForm1572().getFormType().setSigningField("field");
        registration.getCurriculumVitaeForm().getFormType().setSigningField("field");
        registration.getFinancialDisclosure().getFormType().setSigningField("field");
        registration.getHumanResearchCertificateForm().getFormType().setSigningField(null);
    }

    private void checkJsonObjects(List<Map<String, Object>> jsonModel) {
        checkJsonForForms(jsonModel);
        checkJsonForDocuments(jsonModel);
    }

    private void checkJsonForForms(List<Map<String, Object>> jsonModel) {
        checkFormAtExpectedIndex(registration.getCurriculumVitaeForm(), jsonModel, 0);
        checkFormAtExpectedIndex(registration.getFinancialDisclosure(), jsonModel, 1);
        checkFormAtExpectedIndex(registration.getForm1572(), jsonModel, 2);
    }

    private void checkFormAtExpectedIndex(AbstractRegistrationForm form, List<Map<String, Object>> jsonFormValues,
            int expectedIndex) {
        Map<String, Object> jsonForm = jsonFormValues.get(expectedIndex);
        assertEquals("Failed expected ordering, form was " + jsonForm.get("name"), form.getId(), jsonForm.get("id"));
        assertEquals(form.getFormType().getDescription(), jsonForm.get("name"));
        assertEquals(form.isSignable(), jsonForm.get("signingRequired"));
        assertEquals(form.isSigned(), jsonForm.get("signed"));
        assertTrue((Boolean) jsonForm.get("registrationForm"));
    }

    private void checkJsonForDocuments(List<Map<String, Object>> jsonModel) {
        FirebirdFile supportingDocument = Iterables.getOnlyElement(registration.getFinancialDisclosure()
                .getSupportingDocumentation());
        FirebirdFile additionalAttachment = Iterables.getOnlyElement(registration.getAdditionalAttachmentsForm()
                .getAdditionalAttachments());
        FirebirdFile humanResearchCertificate = Iterables.getOnlyElement(
                registration.getHumanResearchCertificateForm().getCertificates()).getFile();
        checkDocumentAtExpectedIndex(supportingDocument, jsonModel, 3);
        checkDocumentAtExpectedIndex(additionalAttachment, jsonModel, 4);
        checkDocumentAtExpectedIndex(humanResearchCertificate, jsonModel, 5);
    }

    private void checkDocumentAtExpectedIndex(FirebirdFile file, List<Map<String, Object>> jsonFormValues,
            int expectedIndex) {
        Map<String, Object> jsonForm = jsonFormValues.get(expectedIndex);
        assertEquals("Failed expected ordering, document was " + jsonForm.get("name"), file.getId(), jsonForm.get("id"));
        assertEquals(file.getName(), jsonForm.get("name"));
        assertFalse((Boolean) jsonForm.get("signingRequired"));
        assertFalse((Boolean) jsonForm.get("signed"));
        assertFalse((Boolean) jsonForm.get("registrationForm"));
    }

    @Test
    public void testSubmitRegistration_ReturnedRegistration() throws ValidationException {
        registration.setStatus(RegistrationStatus.SUBMITTED);
        registration.setStatus(RegistrationStatus.RETURNED);
        assertEquals(CompletionActionProcessor.ENTER_COMMENTS, processor.submitRegistration());
        verify(mockRegistrationService).prepareForSubmission(registration);
    }

    @Test
    public void testSubmitRegistration_NonReturnedRegistration() throws ValidationException {
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        assertEquals(CompletionActionProcessor.COMPLETE_REGISTRATION, processor.submitRegistration());
        verify(mockRegistrationService).prepareForSubmission(registration);
    }

    @Test
    public void testSubmitRegistration_FailedValidation() throws ValidationException {
        getMockRequest().addUserRole(UserRoleType.INVESTIGATOR.getVerifiedGroupName());
        doThrow(ValidationExceptionFactory.getInstance().create()).when(mockRegistrationService).prepareForSubmission(
                registration);
        assertEquals(ActionSupport.INPUT, processor.submitRegistration());
        verify(mockRegistrationService).prepareForSubmission(registration);
    }

    @Test
    public void testValidateAndCleanComments_NotValid() {
        when(mockAction.validateRichTextSize(comments, CompletionActionProcessor.MAX_CHAR_COUNT)).thenReturn(false);
        assertNull(processor.validateAndCleanComments(comments));
    }

    @Test
    public void testValidateAndCleanComments() {
        when(mockAction.validateRichTextSize(comments, CompletionActionProcessor.MAX_CHAR_COUNT)).thenReturn(true);
        assertNotNull(processor.validateAndCleanComments(comments));
    }

    @Test
    public void testGetCleanInvestigatorComments() throws Exception {
        String dirtyComments = "<p>comments<script></script></p>";
        registration.setInvestigatorComments(dirtyComments);
        String expectedCleanComments = "<p>comments</p>";
        assertEquals(expectedCleanComments, processor.getCleanInvestigatorComments());
    }

}
