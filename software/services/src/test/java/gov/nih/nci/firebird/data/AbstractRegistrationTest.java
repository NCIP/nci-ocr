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
package gov.nih.nci.firebird.data;

import static gov.nih.nci.firebird.data.AbstractRegistration.*;
import static gov.nih.nci.firebird.data.FormTypeEnum.*;
import static gov.nih.nci.firebird.data.RegistrationStatus.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.FirebirdModule;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.util.EnumSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.inject.Guice;
import com.google.inject.Inject;

public class AbstractRegistrationTest {

    @Inject
    private ResourceBundle resources;
    private InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();

    @Before
    public void setUp() {
        Guice.createInjector(new FirebirdModule()).injectMembers(this);
        System.setProperty("registration.validation.require.nes.status.active", "true");
    }

    @Test
    public void testIsLockedForInvestigator() {
        for (RegistrationStatus status : RegistrationStatus.values()) {
            registration.setStatus(status);
            boolean expected = LOCKED_STATUSES.contains(status);
            assertEquals(expected, registration.isLockedForInvestigator());
        }
    }

    @Test
    public void testGetForm() {
        for (FormType formType : registration.getFormConfiguration().keySet()) {
            assertNotNull(registration.getForm(formType));
        }
        assertNull(registration.getForm(new FormType("not present")));
    }

    @Test
    public void testGetForm_FormTypeEnum() {
        for (FormTypeEnum formType : FormTypeEnum.PROTOCOL_REGISTATION_FORM_TYPES) {
            assertNotNull(registration.getForm(formType));
        }
    }

    @Test
    public void testIsCompleteable() {
        registration.setStatus(RegistrationStatus.IN_REVIEW);
        registration.getForm(CV).setFormStatus(FormStatus.SUBMITTED);
        registration.getForm(FORM_1572).setFormStatus(FormStatus.IN_PROGRESS);
        registration.getForm(FINANCIAL_DISCLOSURE_FORM).setFormStatus(FormStatus.IN_PROGRESS);
        registration.getForm(HUMAN_RESEARCH_CERTIFICATE).setFormStatus(FormStatus.IN_PROGRESS);
        assertFalse(registration.isCompleteable());

        registration.getForm(FORM_1572).setFormStatus(FormStatus.ACCEPTED);
        registration.getForm(FINANCIAL_DISCLOSURE_FORM).setFormStatus(FormStatus.ACCEPTED);
        registration.getForm(HUMAN_RESEARCH_CERTIFICATE).setFormStatus(FormStatus.ACCEPTED);
        assertFalse(registration.isCompleteable());

        registration.getForm(CV).setFormStatus(FormStatus.REJECTED);
        assertFalse(registration.isCompleteable());

        registration.getForm(CV).setComments("These are comments about this terrible CV");
        assertTrue(registration.isCompleteable());

        registration.setStatus(APPROVED);
        assertFalse(registration.isCompleteable());
    }

    @Test
    public void testIsCompleted_True() {
        EnumSet<RegistrationStatus> completedStatuses = EnumSet.of(APPROVED, ACCEPTED, RETURNED);
        for (RegistrationStatus status : completedStatuses) {
            registration.setStatus(status);
            assertTrue(status + " should be completed!", registration.isCompleted());
        }
    }

    @Test
    public void testIsCompleted_False() {
        EnumSet<RegistrationStatus> completedStatuses = EnumSet.complementOf(EnumSet.of(APPROVED, ACCEPTED, RETURNED));
        for (RegistrationStatus status : completedStatuses) {
            registration.setStatus(status);
            assertFalse(status + " should not be completed!", registration.isCompleted());
        }
    }

    @Test
    public void testValidate() throws ValidationException {
        registration = new InvestigatorRegistration();
        registration.setStatus(IN_PROGRESS);
        registration.validate(resources);
        assertEquals(IN_PROGRESS, registration.getStatus());
    }

    @Test(expected = ValidationException.class)
    public void testValidate_failValidation() throws ValidationException {
        registration.setStatus(IN_PROGRESS);
        registration.validate(resources);
    }

    @Test
    public void testValidateUnReviewedAndUnRevisedForms() throws Exception {
        registration = new InvestigatorRegistration();
        registration.setStatus(IN_PROGRESS);
        registration.validateUnReviewedAndUnRevisedForms(resources);
        assertEquals(IN_PROGRESS, registration.getStatus());
    }

    @Test(expected = ValidationException.class)
    public void testValidateUnReviewedAndUnRevisedForms_failValidation() throws Exception {
        registration.setStatus(IN_PROGRESS);
        registration.validateUnReviewedAndUnRevisedForms(resources);
    }

    @Test
    public void testGetUnReviewedAndUnRevisedForms() throws Exception {
        registration.getCurriculumVitaeForm().setFormStatus(FormStatus.ACCEPTED);
        registration.getFinancialDisclosure().setFormStatus(FormStatus.REVISED);
        registration.getHumanResearchCertificateForm().setFormStatus(FormStatus.IN_PROGRESS);
        registration.getForm1572().setFormStatus(FormStatus.REJECTED);
        Iterable<AbstractRegistrationForm> unReviewedAndUnRevisedForms = registration.getUnReviewedAndUnRevisedForms();
        assertTrue(Iterables.contains(unReviewedAndUnRevisedForms, registration.getHumanResearchCertificateForm()));
        assertFalse(Iterables.contains(unReviewedAndUnRevisedForms, registration.getCurriculumVitaeForm()));
        assertFalse(Iterables.contains(unReviewedAndUnRevisedForms, registration.getFinancialDisclosure()));
        assertFalse(Iterables.contains(unReviewedAndUnRevisedForms, registration.getForm1572()));
    }

    @Test
    public void testFindFormById() {
        AbstractRegistrationForm expectedForm = registration.getForm(FORM_1572);
        expectedForm.setId(1L);
        AbstractRegistrationForm actualForm = registration.findFormById(expectedForm.getId());
        assertEquals(expectedForm, actualForm);
    }

    @Test
    public void testFindFormById_DoesNotExist() {
        assertNull(registration.findFormById(1L));
    }

    @Test
    public void testIsSubmittable_True() {
        for (RegistrationStatus status : AbstractProtocolRegistration.SUBMITTABLE_STATUSES) {
            registration.setStatus(status);
            assertTrue(status.name(), registration.isSubmittable());
        }
    }

    @Test
    public void testIsSubmittable_False() {
        EnumSet<RegistrationStatus> submittableNotExpected = EnumSet
                .complementOf(AbstractProtocolRegistration.SUBMITTABLE_STATUSES);
        for (RegistrationStatus status : submittableNotExpected) {
            registration.setStatus(status);
            assertFalse(status.name(), registration.isSubmittable());
        }
    }

    @Test
    public void testGetOrganizations() {
        Set<Organization> organizations = registration.getOrganizations();
        for (FormTypeEnum formType : FormTypeEnum.PROTOCOL_REGISTATION_FORM_TYPES) {
            assertTrue(organizations.containsAll(registration.getForm(formType).getOrganizations()));
        }
    }

    @Test
    public void testGetPersons() {
        Set<Person> persons = registration.getPersons();
        for (FormTypeEnum formType : FormTypeEnum.PROTOCOL_REGISTATION_FORM_TYPES) {
            assertTrue(persons.containsAll(registration.getForm(formType).getPersons()));
        }
    }

    @Test
    public void testIsNotificationRequiredForUpdate_True() {
        for (RegistrationStatus status : AbstractProtocolRegistration.NOTIFICATION_REQUIRED_ON_UPDATE_STATUSES) {
            registration.setStatus(status);
            assertTrue(status.name(), registration.isNotificationRequiredForUpdate());
        }
    }

    @Test
    public void testIsNotificationNotRequiredForUpdate_False() {
        EnumSet<RegistrationStatus> requiredNotExpected = EnumSet
                .complementOf(AbstractProtocolRegistration.NOTIFICATION_REQUIRED_ON_UPDATE_STATUSES);
        for (RegistrationStatus status : requiredNotExpected) {
            registration.setStatus(status);
            assertFalse(status.name(), registration.isNotificationRequiredForUpdate());
        }
    }

    @Test
    public void testGetFormsForSponsorReview_WithAttachments() {
        FirebirdFile attachment = new FirebirdFile();
        registration.getAdditionalAttachmentsForm().getAdditionalAttachments().add(attachment);
        Set<AbstractRegistrationForm> forms = registration.getFormsForSponsorReview();
        assertTrue(forms.contains(registration.getForm(FORM_1572)));
        assertTrue(forms.contains(registration.getForm(CV)));
        assertTrue(forms.contains(registration.getFinancialDisclosure()));
        assertTrue(forms.contains(registration.getForm(HUMAN_RESEARCH_CERTIFICATE)));
        assertTrue(forms.contains(registration.getAdditionalAttachmentsForm()));
    }

    @Test
    public void testGetFormsForSponsorReview_WithoutAttachments() {
        Set<AbstractRegistrationForm> forms = registration.getFormsForSponsorReview();
        assertTrue(forms.contains(registration.getForm(FORM_1572)));
        assertTrue(forms.contains(registration.getForm(CV)));
        assertTrue(forms.contains(registration.getFinancialDisclosure()));
        assertTrue(forms.contains(registration.getForm(HUMAN_RESEARCH_CERTIFICATE)));
        assertFalse(forms.contains(registration.getAdditionalAttachmentsForm()));
    }

    @Test
    public void testGetForms() {
        FirebirdFile attachment = new FirebirdFile();
        registration.getAdditionalAttachmentsForm().getAdditionalAttachments().add(attachment);
        Set<AbstractRegistrationForm> forms = registration.getForms();
        assertTrue(forms.contains(registration.getForm(FORM_1572)));
        assertTrue(forms.contains(registration.getForm(CV)));
        assertTrue(forms.contains(registration.getFinancialDisclosure()));
        assertTrue(forms.contains(registration.getForm(HUMAN_RESEARCH_CERTIFICATE)));
        assertTrue(forms.contains(registration.getAdditionalAttachmentsForm()));
    }

    @Test
    public void testIsResubmitRequiredForUpdate_True() {
        for (RegistrationStatus status : AbstractProtocolRegistration.RESUBMIT_REQUIRED_ON_UPDATE_STATUSES) {
            registration.setStatus(status);
            assertTrue(status.name(), registration.isResubmitRequiredForUpdate());
        }
    }

    @Test
    public void testIsResubmitRequiredForUpdate_False() {
        EnumSet<RegistrationStatus> resubmitNotExpected = EnumSet
                .complementOf(AbstractProtocolRegistration.RESUBMIT_REQUIRED_ON_UPDATE_STATUSES);
        for (RegistrationStatus status : resubmitNotExpected) {
            registration.setStatus(status);
            assertFalse(status.name(), registration.isResubmitRequiredForUpdate());
        }
    }

    @Test
    public void testGetPlainTextSponsorComments() {
        String expectedPlainText = "These are comments.";
        String comments = "<html><body><pre>" + expectedPlainText + "</html></body></pre>";
        registration.setSponsorComments(comments);
        assertEquals(expectedPlainText, registration.getPlainTextSponsorComments());
    }

    @Test
    public void testGetPlainTextCoordinatorComments() {
        String expectedPlainText = "These are comments.";
        String comments = "<html><body><pre>" + expectedPlainText + "</html></body></pre>";
        registration.setCoordinatorComments(comments);
        assertEquals(expectedPlainText, registration.getPlainTextCoordinatorComments());
    }

    @Test
    public void testSetStatus() {
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        assertNotNull(registration.getStatusDate());
        assertNull(registration.getLastSubmissionDate());
    }

    @Test
    public void testSetStatus_Submitted() {
        registration.setStatus(RegistrationStatus.SUBMITTED);
        assertNotNull(registration.getStatusDate());
        assertNotNull(registration.getLastSubmissionDate());
    }

    @Test
    public void testSetStatus_Approved() {
        registration.setStatus(RegistrationStatus.APPROVED);
        assertNotNull(registration.getStatusDate());
        assertNotNull(registration.getApprovalDate());
    }

    @Test
    public void testGetFormComments() {
        String cvComments = "cvComments";
        String hrcComments = "hrcComments";
        registration.getForm(CV).setFormStatus(FormStatus.REJECTED);
        registration.getForm(CV).setComments(cvComments);
        registration.getForm(HUMAN_RESEARCH_CERTIFICATE).setComments(hrcComments);
        String formComments = registration.getFormComments();
        assertTrue(formComments.contains(registration.getForm(CV).getFormType().getDescription()));
        assertTrue(formComments.contains(cvComments));
        assertFalse(formComments.contains(registration.getForm(HUMAN_RESEARCH_CERTIFICATE).getFormType()
                .getDescription()));
        assertFalse(formComments.contains(hrcComments));
    }

    @Test
    public void testIsReviewable_True() {
        for (RegistrationStatus status : AbstractRegistration.REVIEWABLE_STATUSES) {
            registration.setStatus(status);
            assertTrue(status + " should be reviewable!", registration.isReviewable());
        }
    }

    @Test
    public void testIsReviewable_False() {
        EnumSet<RegistrationStatus> nonReviewableStatuses = EnumSet
                .complementOf(AbstractRegistration.REVIEWABLE_STATUSES);
        for (RegistrationStatus status : nonReviewableStatuses) {
            registration.setStatus(status);
            assertFalse(status + " should not be reviewable!", registration.isReviewable());
        }
    }

    @Test
    public void testIsReviewOnHold() {
        for (RegistrationStatus status : RegistrationStatus.values()) {
            boolean expected = status == RegistrationStatus.REVIEW_ON_HOLD;
            registration.setStatus(status);
            assertEquals(expected, registration.isReviewOnHold());
        }
    }

    @Test
    public void testSetFormStatuses() {
        registration.setFormStatuses(FormStatus.APPROVED);
        for (AbstractRegistrationForm form : registration.getForms()) {
            FormStatus status = form.getFormStatus();
            assertTrue(FormStatus.APPROVED == status || FormStatus.NOT_APPLICABLE == status);
        }
    }

    @Test
    public void testPrepareForDeletion() {
        InvestigatorProfile profile = registration.getProfile();
        registration.prepareForDeletion();
        assertNull(registration.getProfile());
        assertFalse(profile.getRegistrations().contains(registration));
    }

    @Test
    public void testIsAnnualRegistration() throws Exception {
        AnnualRegistration registration = new AnnualRegistration();
        assertTrue(registration.isAnnualRegistration());
    }

    @Test
    public void testIsAnnualRegistration_False() throws Exception {
        InvestigatorRegistration registration = new InvestigatorRegistration();
        assertFalse(registration.isAnnualRegistration());
    }

    @Test
    public void testClearNonRejectedFormsComments() throws Exception {
        registration.getForm(CV).setFormStatus(FormStatus.REJECTED);
        registration.getForm(CV).setComments("comments");
        registration.getForm(HUMAN_RESEARCH_CERTIFICATE).setComments("comments");
        registration.clearNonRejectedFormsComments();
        assertFalse(isEmpty(registration.getForm(CV).getComments()));
        assertTrue(isEmpty(registration.getForm(HUMAN_RESEARCH_CERTIFICATE).getComments()));
    }

    @Test
    public void testIsCommentsEntered_SponsorComments() throws Exception {
        registration.setSponsorComments("comments");
        assertTrue(registration.isCommentsEntered());
    }

    @Test
    public void testIsCommentsEntered_FormComments() throws Exception {
        registration.getForm1572().setFormStatus(FormStatus.REJECTED);
        registration.getForm1572().setComments("comments");
        assertTrue(registration.isCommentsEntered());
    }

    @Test
    public void testIsCommentsEntered_InvestigatorComments() throws Exception {
        registration.setInvestigatorComments("comments");
        assertTrue(registration.isCommentsEntered());
    }

    @Test
    public void testIsCommentsEntered_CoordinatorComments() throws Exception {
        registration.setCoordinatorComments("comments");
        assertTrue(registration.isCommentsEntered());
    }

    @Test
    public void testIsCommentsEntered_NoComments() throws Exception {
        assertFalse(registration.isCommentsEntered());
    }

}
