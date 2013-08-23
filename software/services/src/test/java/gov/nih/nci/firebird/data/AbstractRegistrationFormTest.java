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

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.FirebirdModule;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.person.NesPersonData;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.text.MessageFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Inject;

@SuppressWarnings("serial")
public class AbstractRegistrationFormTest {
    @Inject
    private ResourceBundle resources;
    private AbstractRegistrationForm form;

    @Before
    public void setUp() {
        Guice.createInjector(new FirebirdModule()).injectMembers(this);
        System.setProperty("registration.validation.require.nes.status.active", "true");
        form = new TestForm();
    }

    @Test
    public void testGetFormFileBaseName() {
        InvestigatorRegistration registration = setUpRegistrationFor1572NameTest();
        assertEquals("prot1234_last_first_form_1572", registration.getForm1572().getFormFileBaseName());
    }

    private InvestigatorRegistration setUpRegistrationFor1572NameTest() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getProfile().getPerson().setLastName("Last");
        registration.getProfile().getPerson().setFirstName("First");
        registration.getProtocol().setProtocolNumber("PROT1234");
        return registration;
    }

    @Test
    public void testGetInProgressFileName() {
        InvestigatorRegistration registration = setUpRegistrationFor1572NameTest();
        assertEquals("prot1234_last_first_form_1572.pdf", registration.getForm1572().getInProgressFileName());
    }

    @Test
    public void testCheckSubmissionReadiness_FormerlyCompleteIncompleteRegistration() {
        AbstractRegistrationForm form = createInvalidForm(RegistrationStatus.INCOMPLETE);
        form.setFormStatus(FormStatus.COMPLETED);
        try {
            form.checkSubmissionReadiness(resources);
            fail("Validation should have failed!");
        } catch (ValidationException e) {
            assertEquals(FormStatus.INCOMPLETE, form.getFormStatus());
        }
    }

    private AbstractRegistrationForm createInvalidForm(final RegistrationStatus registrationStatus) {
        AbstractRegistrationForm form = new TestForm() {
            @Override
            protected void validate(ValidationResult result, ResourceBundle resources) {
                result.addFailure(new ValidationFailure("error"));
            }
        };
        form.getRegistration().setStatus(registrationStatus);

        return form;
    }

    @Test
    public void testCheckSubmissionReadiness_FormerlyCompleteInProgressRegistration() {
        AbstractRegistrationForm form = createInvalidForm(RegistrationStatus.IN_PROGRESS);
        form.setFormStatus(FormStatus.COMPLETED);
        try {
            form.checkSubmissionReadiness(resources);
            fail("Validation should have failed!");
        } catch (ValidationException e) {
            assertEquals(FormStatus.IN_PROGRESS, form.getFormStatus());
        }
    }

    @Test
    public void testCheckSubmissionReadiness_FormerlyInCompleteInCompleteRegistration() {
        AbstractRegistrationForm form = createInvalidForm(RegistrationStatus.INCOMPLETE);
        form.setFormStatus(FormStatus.INCOMPLETE);
        try {
            form.checkSubmissionReadiness(resources);
            fail("Validation should have failed!");
        } catch (ValidationException e) {
            assertEquals(FormStatus.INCOMPLETE, form.getFormStatus());
        }
    }

    @Test
    public void testCheckSubmissionReadiness_SubmittedFormSubmittedRegistration() throws ValidationException {
        form.setFormStatus(FormStatus.SUBMITTED);
        form.getRegistration().setStatus(RegistrationStatus.SUBMITTED);
        form.checkSubmissionReadiness(resources);
        assertEquals(FormStatus.SUBMITTED, form.getFormStatus());
    }

    @Test
    public void testCheckSubmissionReadiness_Returned() throws ValidationException {
        form.setFormStatus(FormStatus.REVISED);
        form.getRegistration().setStatus(RegistrationStatus.SUBMITTED); // populate lastSubmissionDate
        form.getRegistration().setStatus(RegistrationStatus.RETURNED);
        form.checkSubmissionReadiness(resources);
        assertEquals(FormStatus.REVISED, form.getFormStatus());
    }

    @Test
    public void testCheckSubmissionReadiness_RevisedRegistration() throws ValidationException {
        form.setFormStatus(FormStatus.ACCEPTED);
        form.getRegistration().setStatus(RegistrationStatus.SUBMITTED); // populate lastSubmissionDate
        form.getRegistration().setStatus(RegistrationStatus.IN_PROGRESS);
        form.checkSubmissionReadiness(resources);
        assertEquals(FormStatus.ACCEPTED, form.getFormStatus());
    }

    @Test
    public void testCheckSubmissionReadiness_InProgress() throws ValidationException {
        form.setFormStatus(FormStatus.IN_PROGRESS);
        form.getRegistration().setStatus(RegistrationStatus.IN_PROGRESS);
        form.checkSubmissionReadiness(resources);
        assertEquals(FormStatus.COMPLETED, form.getFormStatus());
    }

    @Test
    public void testValidate() throws Exception {
        ValidationResult result = new ValidationResult();
        final Person uncuratedPerson = PersonFactory.getInstanceWithId().create();
        uncuratedPerson.setCurationStatus(CurationStatus.PENDING);
        final Person curatedPerson = PersonFactory.getInstanceWithId().create();

        final Organization uncuratedOrg = OrganizationFactory.getInstanceWithId().create();
        uncuratedOrg.setCurationStatus(CurationStatus.UNSAVED);
        final Organization curatedOrg = OrganizationFactory.getInstanceWithId().create();
        curatedOrg.setCurationStatus(CurationStatus.ACTIVE);

        AbstractRegistrationForm form = new TestForm() {
            @Override
            public Set<Person> getPersons() {
                return Sets.newHashSet(uncuratedPerson, curatedPerson);
            }

            @Override
            public Set<Organization> getOrganizations() {
                return Sets.newHashSet(uncuratedOrg, curatedOrg);
            }
        };

        System.setProperty("registration.validation.require.nes.status.active", "false");
        form.validate(result, resources);
        assertTrue(result.isValid());
        System.setProperty("registration.validation.require.nes.status.active", "true");

        result = new ValidationResult();
        form.validate(result, resources);
        assertEquals(2, result.getFailures().size());
        hasError(
                result,
                getErrorMessage("validation.failure.uncurated", uncuratedPerson.getDisplayName(), form.getFormType()
                        .getName(), uncuratedPerson.getCurationStatus().name()));
        hasError(
                result,
                getErrorMessage("validation.failure.uncurated", uncuratedOrg.getName(), form.getFormType().getName(),
                        uncuratedOrg.getCurationStatus().name()));
        assertEquals(2, form.getInvalidEntityIds().size());
        assertTrue(form.getInvalidEntityIds().contains(uncuratedPerson.getId()));
        assertTrue(form.getInvalidEntityIds().contains(uncuratedOrg.getId()));
    }

    @Test
    public void testValidate_NullifiedOrganization() {
        final Organization organization = OrganizationFactory.getInstanceWithId().create();
        organization.setCurationStatus(CurationStatus.NULLIFIED);
        AbstractRegistrationForm form = new TestForm() {
            @Override
            public Set<Organization> getOrganizations() {
                return Sets.newHashSet(organization);
            }
        };
        ValidationResult result = new ValidationResult();
        form.validate(result, resources);
        assertTrue(hasError(result,
                getErrorMessage("validation.failures.organization.nullified", organization.getName(), "Test Form")));
        assertEquals(1, form.getInvalidEntityIds().size());
        assertTrue(form.getInvalidEntityIds().contains(organization.getId()));
    }

    @Test
    public void testValidate_NullifiedPerson() {
        final Person person = PersonFactory.getInstanceWithId().create();
        person.setCurationStatus(CurationStatus.NULLIFIED);
        AbstractRegistrationForm form = new TestForm() {
            @Override
            public Set<Person> getPersons() {
                return Sets.newHashSet(person);
            }
        };
        ValidationResult result = new ValidationResult();
        form.validate(result, resources);
        assertTrue(hasError(result,
                getErrorMessage("validation.failures.person.nullified", person.getDisplayName(), "Test Form")));
        assertEquals(1, form.getInvalidEntityIds().size());
        assertTrue(form.getInvalidEntityIds().contains(person.getId()));
    }

    @Test
    public void testValidate_PersonPendingUpdates() {
        final Person person = PersonFactory.getInstanceWithId().create();
        person.setCurationStatus(CurationStatus.ACTIVE);
        ((NesPersonData) person.getExternalData()).requestUpdate();
        AbstractRegistrationForm form = new TestForm() {
            @Override
            public Set<Person> getPersons() {
                return Sets.newHashSet(person);
            }
        };
        ValidationResult result = new ValidationResult();
        form.validate(result, resources);
        assertTrue(hasError(result,
                getErrorMessage("validation.failure.pending.nes.updates", person.getDisplayName(), "Test Form")));
        assertEquals(1, form.getInvalidEntityIds().size());
        assertTrue(form.getInvalidEntityIds().contains(person.getId()));
    }

    @Test
    public void testRequiresValidation() {
        form.getRegistration().getFormConfiguration().put(form.getFormType(), FormOptionality.REQUIRED);
        assertTrue(form.requiresValidation());
        form.getRegistration().getFormConfiguration().put(form.getFormType(), FormOptionality.OPTIONAL);
        assertTrue(form.requiresValidation());
        form.getRegistration().getFormConfiguration().put(form.getFormType(), FormOptionality.NONE);
        assertFalse(form.requiresValidation());
    }

    protected String getErrorMessage(String key, Object... args) {
        return MessageFormat.format(resources.getString(key), args);
    }

    protected boolean hasError(ValidationResult result, String errorMessage) {
        for (ValidationFailure failure : result.getFailures()) {
            if (failure.getMessage().equals(errorMessage)) {
                return true;
            }
        }
        return false;
    }

    protected ResourceBundle getResources() {
        return resources;
    }

    @Test
    public void testIsSubmittable_True() {
        for (FormStatus formStatus : AbstractRegistrationForm.SUBMITTABLE_STATUSES) {
            form.setFormStatus(formStatus);
            assertTrue(form.isSubmittable());
        }
    }

    @Test
    public void testIsSubmittable_False() {
        EnumSet<FormStatus> nonSubmittibleStatuses = EnumSet
                .complementOf(AbstractRegistrationForm.SUBMITTABLE_STATUSES);
        for (FormStatus formStatus : nonSubmittibleStatuses) {
            form.setFormStatus(formStatus);
            assertFalse(form.isSubmittable());
        }
    }

    @Test
    public void testIsReviewRequired() {
        assertTrue(form.isReviewRequired());
    }

    @Test
    public void testIsSigned_True() {
        form.setSignedPdf(new FirebirdFile());
        assertTrue(form.isSigned());
    }

    @Test
    public void testIsSigned_False() {
        assertFalse(form.isSigned());
    }

    private class TestForm extends AbstractRegistrationForm {

        private static final long serialVersionUID = 1L;
        private AbstractRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();

        public TestForm() {
            super(new FormType("Test Form"));
        }

        @Override
        public Set<Person> getPersons() {
            return Sets.newHashSet();
        }

        @Override
        public AbstractRegistration getRegistration() {
            return registration;
        }

    }

    @Test
    public void testGetPlainTextComments() {
        String expectedPlainText = "These are comments.";
        String comments = "<html><body><pre>" + expectedPlainText + "</html></body></pre>";
        form.setComments(comments);
        assertEquals(expectedPlainText, form.getPlainTextComments());
    }

    @Test
    public void testReturnForm() {
        FirebirdFile flattenedPdf = new FirebirdFile();
        FirebirdFile signedPdf = new FirebirdFile();
        form.setFlattenedPdf(flattenedPdf);
        form.setSignedPdf(signedPdf);
        Set<PersistentObject> objectsToDelete = form.returnForm();
        assertTrue(objectsToDelete.contains(flattenedPdf));
        assertTrue(objectsToDelete.contains(signedPdf));
        assertNull(form.getFlattenedPdf());
        assertNull(form.getSignedPdf());
    }

    @Test
    public void testReturnForm_PdfsNotOrphaned() {
        FirebirdFile flattenedPdf = new FirebirdFile();
        FirebirdFile signedPdf = new FirebirdFile();
        form.setFlattenedPdf(flattenedPdf);
        form.setSignedPdf(signedPdf);
        form.getRegistration().getProfile().getUploadedFiles().add(flattenedPdf);
        form.getRegistration().getProfile().getUploadedFiles().add(signedPdf);
        Set<PersistentObject> objectsToDelete = form.returnForm();
        assertFalse(objectsToDelete.contains(flattenedPdf));
        assertFalse(objectsToDelete.contains(signedPdf));
    }

    @Test
    public void testSubmitForm() throws AssociationAlreadyExistsException {
        form.getRegistration().setStatus(RegistrationStatus.COMPLETED);
        form.setFormStatus(FormStatus.COMPLETED);
        form.submitForm();
        assertEquals(FormStatus.SUBMITTED, form.getFormStatus());
    }

    @Test
    public void testSubmitForm_Returned() {
        form.getRegistration().setStatus(RegistrationStatus.SUBMITTED); // populate lastSubmissionDate
        form.getRegistration().setStatus(RegistrationStatus.RETURNED);
        form.setFormStatus(FormStatus.ACCEPTED);
        form.submitForm();
        assertEquals(FormStatus.ACCEPTED, form.getFormStatus());
    }

    @Test
    public void testSubmitForm_Revised() {
        form.getRegistration().setStatus(RegistrationStatus.SUBMITTED); // populate lastSubmissionDate
        form.getRegistration().setStatus(RegistrationStatus.IN_PROGRESS);
        form.setFormStatus(FormStatus.ACCEPTED);
        form.submitForm();
        assertEquals(FormStatus.ACCEPTED, form.getFormStatus());
    }

    @Test
    public void testCreateValidationFailure() {
        String messageKey = "validation.failure.missing.practice.site";
        ValidationFailure validationFailure = form.createValidationFailure(getResources(), messageKey);
        assertEquals(getResources().getString(messageKey), validationFailure.getMessage());
    }

    @Test
    public void testGetFormOptionality() {
        form.getRegistration().getFormConfiguration().put(form.getFormType(), FormOptionality.REQUIRED);
        assertEquals(FormOptionality.REQUIRED, form.getFormOptionality());
    }

    @Test
    public void testGetFormOptionality_NullOptionality() {
        assertEquals(FormOptionality.NONE, form.getFormOptionality());
    }

    @Test
    public void testSetFormStatus_NoChange() {
        form = spy(form);
        form.setFormStatus(FormStatus.COMPLETED);
        form.setFormStatus(FormStatus.COMPLETED);
        verify(form).setFormStatusDate(any(Date.class));
        assertEquals(FormStatus.COMPLETED, form.getFormStatus());
    }

    @Test
    public void testSetFormStatus_Change() {
        form = spy(form);
        form.setFormStatus(FormStatus.COMPLETED);
        form.setFormStatus(FormStatus.INCOMPLETE);
        verify(form, times(2)).setFormStatusDate(any(Date.class));
        assertEquals(FormStatus.INCOMPLETE, form.getFormStatus());
    }

    @Test
    public void testGetOrganizations() {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        form.getRegistration().setProfile(profile);
        Set<Organization> organizations = form.getOrganizations();
        assertTrue(Iterables.getOnlyElement(organizations).equals(profile.getPrimaryOrganization().getOrganization()));
    }

    @Test
    public void testGetOrganizations_NullPrimaryOrganization() {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        profile.setPrimaryOrganization(null);
        form.getRegistration().setProfile(profile);
        assertTrue(form.getOrganizations().isEmpty());
    }

    @Test
    public void testGetNumberOfAdditionalDocuments() {
        assertEquals(0, form.getNumberOfAdditionalDocuments());
    }

    @Test
    public void testIsAdditionalDocumentsUploaded() {
        assertFalse(form.isAdditionalDocumentsUploaded());
    }

    @Test
    public void testIsCurrentlyReviewable_InReview() {
        form.getRegistration().setStatus(RegistrationStatus.IN_REVIEW);
        for (FormStatus status : FormStatus.values()) {
            form.setFormStatus(status);
            boolean expected = AbstractRegistrationForm.CURRENTLY_REVIEWABLE_STATUSES.contains(status);
            assertEquals(expected, form.isCurrentlyReviewable());
        }
    }

    @Test
    public void testIsCurrentlyReviewable_NotInReview() {
        form.getRegistration().setStatus(RegistrationStatus.IN_PROGRESS);
        for (FormStatus status : FormStatus.values()) {
            form.setFormStatus(status);
            assertEquals(false, form.isCurrentlyReviewable());
        }
    }

    @Test
    public void testIsReviewed() {
        for (FormStatus status : FormStatus.values()) {
            form.setFormStatus(status);
            boolean expected = AbstractRegistrationForm.REVIEWED_STATUSES.contains(status);
            assertEquals(expected, form.isReviewed());
        }
    }

    @Test
    public void testIsCommentsLinkToInvestigatorToBeDisplayed_NoComments() throws Exception {
        assertFalse(form.isCommentsLinkToInvestigatorToBeDisplayed());
    }

    @Test
    public void testIsCommentsLinkToInvestigatorToBeDisplayed_Accepted() throws Exception {
        form.setComments("comments");
        form.setFormStatus(FormStatus.ACCEPTED);
        assertFalse(form.isCommentsLinkToInvestigatorToBeDisplayed());
    }

    @Test
    public void testIsCommentsLinkToInvestigatorToBeDisplayed_InReview() throws Exception {
        form.setComments("comments");
        form.setFormStatus(FormStatus.IN_REVIEW);
        assertFalse(form.isCommentsLinkToInvestigatorToBeDisplayed());
    }

    @Test
    public void testIsCommentsLinkToInvestigatorToBeDisplayed_True() throws Exception {
        form.setComments("comments");
        assertTrue(form.isCommentsLinkToInvestigatorToBeDisplayed());
    }

    @Test
    public void testIsCollectionOfDocuments() throws Exception {
        for (FormTypeEnum formType : FormTypeEnum.values()) {
            form.getFormType().setFormTypeEnum(formType);
            boolean expected = AbstractRegistrationForm.FORMS_WITH_COLLECTION_OF_DOCUMENTS.contains(formType);
            assertEquals(expected, form.isCollectionOfDocuments());
        }
    }

}
