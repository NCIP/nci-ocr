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
package gov.nih.nci.firebird.web.action.investigator.registration;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.Certification;
import gov.nih.nci.firebird.data.CredentialType;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.ProtocolRevision;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import gov.nih.nci.firebird.test.ValidationExceptionFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.action.investigator.registration.common.RegistrationTabActionProcessor;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.struts2.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class RegistrationTabActionTest extends AbstractWebTest {

    @Inject
    private ProtocolRegistrationService mockRegistrationService;
    @Inject
    private GenericDataRetrievalService mockDataService;
    @Inject
    private RegistrationTabAction action;
    @Mock
    private RegistrationTabActionProcessor<AbstractProtocolRegistration> mockProcessor;
    private final InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId()
            .createInvestigatorRegistration();
    private final FormType form1572Type = registration.getForm1572().getFormType();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(mockRegistrationService.getById(anyLong())).thenReturn(registration);
        doThrow(ValidationExceptionFactory.getInstance().create()).when(mockRegistrationService).prepareForSubmission(
                registration);
        when(mockDataService.getPersistentObject(eq(FormType.class), anyLong())).thenReturn(form1572Type);
        action.setProcessor(mockProcessor);
        action.setRegistration(registration);
    }

    @Test
    public void testPrepare_NoValues() {
        action.setRegistration(new InvestigatorRegistration());
        action.prepare();
        assertNull(action.getRegistration().getId());
        assertNull(action.getFormType().getId());
        assertNull(action.getForm());
    }

    @Test
    public void testPrepare_Values() {
        action.setRegistration(new InvestigatorRegistration());
        action.getFormType().setId(1L);
        action.getRegistration().setId(1L);
        action.prepare();

        assertNotNull(action.getRegistration().getId());
        assertNotNull(action.getFormType().getId());
        assertNotNull(action.getForm());
    }

    @Test
    public void testViewForm() {
        action.setFormType(form1572Type);
        action.viewForm();
        verify(mockProcessor).viewForm(registration.getForm(form1572Type));
    }

    @Test
    public void testViewOverview() throws ValidationException {
        assertEquals(RegistrationTabAction.VIEW_OVERVIEW, action.viewOverview());
        verify(mockRegistrationService).checkUnReviewedAndUnRevisedFormCompletionStatus(registration);
    }

    @Test
    public void testViewOverview_Locked() throws ValidationException {
        registration.setStatus(RegistrationStatus.INACTIVE);
        assertEquals(RegistrationTabAction.VIEW_OVERVIEW, action.viewOverview());
        verifyZeroInteractions(mockRegistrationService);
    }

    @Test
    public void testViewOverview_Returned() throws ValidationException {
        registration.setStatus(RegistrationStatus.RETURNED);
        assertEquals(RegistrationTabAction.VIEW_OVERVIEW, action.viewOverview());
        verifyZeroInteractions(mockRegistrationService);
    }

    @Test
    public void testViewOverview_NoRegistration() {
        action.setRegistration(new InvestigatorRegistration());
        assertEquals(FirebirdUIConstants.RETURN_ACCESS_DENIED_ENTER, action.viewOverview());
    }

    @Test
    public void testViewProtocolInformation() {
        assertEquals(RegistrationTabAction.VIEW_PROTOCOL_INFORMATION, action.viewProtocolInformation());
    }

    @Test
    public void testViewProtocolInformation_NoRegistration() {
        action.setRegistration(new InvestigatorRegistration());
        assertEquals(FirebirdUIConstants.RETURN_ACCESS_DENIED_ENTER, action.viewProtocolInformation());
    }

    @Test
    public void testGetRegistrationFormsJson() throws JSONException {
        String actualJson = action.getRegistrationFormsJson();
        for (AbstractRegistrationForm form : registration.getForms()) {
            checkFormIncludedInJson(actualJson, form);
        }
    }

    private void checkFormIncludedInJson(String actualJson, AbstractRegistrationForm form) throws JSONException {
        assertTrue(actualJson.contains(form.getId().toString()));
        assertTrue(actualJson.contains(form.getFormType().getId().toString()));
        assertTrue(actualJson.contains(form.getFormType().getDescription()));
        assertTrue(actualJson.contains(form.getFormOptionality().getDisplay()));
        assertTrue(actualJson.contains(form.getFormType().getName().replaceAll(" ", "_")));
        if (form.getComments() != null) {
            assertTrue(actualJson.contains(form.getComments()));
        }
        assertTrue(actualJson.contains(form.getFormStatus().getDisplay()));
    }

    @Test
    public void testGetCvCredentials_NoCredentials() throws CredentialAlreadyExistsException {
        assertTrue(action.getCvCredentials(CredentialType.CERTIFICATION).isEmpty());
    }

    @Test
    public void testGetCvCredentials_ExpiredCredentials() throws CredentialAlreadyExistsException {
        Certification certificationExpired = CredentialFactory.getInstance().createCertification();
        certificationExpired.setExpirationDate(DateUtils.addYears(new Date(), -1));
        registration.getProfile().addCredential(certificationExpired);
        Set<AbstractCredential<?>> certifications = (Set<AbstractCredential<?>>) action
                .getCvCredentials(CredentialType.CERTIFICATION);
        assertEquals(1, certifications.size());
        assertTrue(certifications.iterator().next().isExpired());
    }

    @Test
    public void testGetCvCredentials_ValidCredentials() throws CredentialAlreadyExistsException {
        Certification certificationCurrent = CredentialFactory.getInstance().createCertification();
        certificationCurrent.setExpirationDate(DateUtils.addYears(new Date(), 2));
        registration.getProfile().addCredential(certificationCurrent);
        assertEquals(1, action.getCvCredentials(CredentialType.CERTIFICATION).size());
        assertFalse(action.getCvCredentials(CredentialType.CERTIFICATION).iterator().next().isExpired());
    }

    @Test
    public void testGetRevisionHistoryJsonNoChanges() throws JSONException {
        String json = action.getRevisionHistoryJson();
        assertEquals("Actual response: " + json, "[]", json);
    }

    @Test
    public void testGetRevisionHistoryJson() throws JSONException {
        ProtocolRevision revision1 = ProtocolFactory.getInstanceWithId()
                .addProtocolRevision(registration.getProtocol());
        ProtocolRevision revision2 = ProtocolFactory.getInstanceWithId()
                .addProtocolRevision(registration.getProtocol());
        String json = action.getRevisionHistoryJson();

        checkJsonForRevision(revision1, json);
        checkJsonForRevision(revision2, json);
    }

    private void checkJsonForRevision(ProtocolRevision revision, String json) {
        assertTrue(json.contains(revision.getId().toString()));
        assertTrue(json.contains(revision.getComment()));
        for (String message : revision.getInvestigatorModificationDescriptions()) {
            assertTrue(json.contains(message));
        }
    }

    @Test
    public void testProfileContainsExpiredCredentials_True() throws CredentialAlreadyExistsException {
        Certification certificationExpired = CredentialFactory.getInstance().createCertification();
        certificationExpired.setExpirationDate(DateUtils.addYears(new Date(), -1));
        registration.getProfile().addCredential(certificationExpired);
        Certification certificationCurrent = CredentialFactory.getInstance().createCertification();
        certificationCurrent.setExpirationDate(DateUtils.addYears(new Date(), 2));
        registration.getProfile().addCredential(certificationCurrent);

        assertTrue(action.profileContainsExpiredCredentials());
    }

    @Test
    public void testProfileContainsExpiredCredentials_False() throws CredentialAlreadyExistsException {
        Certification certificationCurrent = CredentialFactory.getInstance().createCertification();
        certificationCurrent.setExpirationDate(DateUtils.addYears(new Date(), 2));
        registration.getProfile().addCredential(certificationCurrent);

        assertFalse(action.profileContainsExpiredCredentials());
    }

    @Test
    public void testGetForm() {
        action.setFormType(form1572Type);
        assertEquals(registration.getForm(form1572Type), action.getForm());
    }

    @Test
    public void testGetForm_FormTypeNull() {
        action.setFormType(null);
        assertNull(action.getForm());
    }

    @Test
    public void testGetForm_RegistrationNull() {
        action.setRegistration(null);
        assertNull(action.getForm());
    }

    @Test
    public void testGetCleanFormComments() throws Exception {
        action.setFormType(form1572Type);
        registration.getForm1572().setComments("<script>alert('hello');</script>Testing Comments");
        assertEquals("Testing Comments", action.getCleanFormComments());
    }

    @Test
    public void testGetCleanFormComments_NullForm() throws Exception {
        action.setFormType(null);
        assertNull(action.getCleanFormComments());
    }

    @Test
    public void testEnterComments() {
        assertEquals(ActionSupport.SUCCESS, action.enterComments());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitiateRegistrationRevision_SubInvestigatorRegistration() throws Exception {
        action.setRegistration(RegistrationFactory.getInstance().createSubinvestigatorRegistration());
        action.initiateRegistrationRevision();
    }

    @Test
    public void testInitiateRegistrationRevision_InvestigatorRegistration() throws Exception {
        FirebirdUser currentUser = new FirebirdUser();
        FirebirdWebTestUtility.setCurrentUser(action, currentUser);
        assertEquals(ActionSupport.NONE, action.initiateRegistrationRevision());
        verify(mockRegistrationService).initiateRevision(registration, currentUser);
    }

    @Test
    public void testCancelRegistrationRevision() throws Exception {
        FirebirdUser currentUser = new FirebirdUser();
        FirebirdWebTestUtility.setCurrentUser(action, currentUser);
        assertEquals(ActionSupport.NONE, action.cancelRegistrationRevision());
        verify(mockRegistrationService).cancelRevision(registration, currentUser);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCancelRegistrationRevision_SubinvestigatorRegistration() throws Exception {
        SubInvestigatorRegistration registration = RegistrationFactory.getInstance().createSubinvestigatorRegistration();
        action.setRegistration(registration);
        action.cancelRegistrationRevision();
    }

}
