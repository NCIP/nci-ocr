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
package gov.nih.nci.firebird.service.registration.review;

import static gov.nih.nci.firebird.data.FormStatus.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.ProtocolForm1572;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Iterables;
import com.google.inject.Provider;

@RunWith(MockitoJUnitRunner.class)
public class AbstractRegistrationReviewServiceTest {

    @Mock
    private SponsorService mockSponsorService;
    @Mock
    private Provider<Session> mockSessionProvider;
    @Mock
    private Session mockSession;

    private AbstractRegistrationReviewService<AbstractProtocolRegistration> bean = new AbstractRegistrationReviewService<AbstractProtocolRegistration>() {

        @Override
        protected void sendRegistrationAcceptedNotifications(FirebirdUser user,
                AbstractProtocolRegistration registration) {
            // Do nothing
        }

        @Override
        protected void sendRegistrationRejectedNotifications(AbstractProtocolRegistration registration) {
            // Do nothing
        }
    };

    @Before
    public void setUp() {
        when(mockSessionProvider.get()).thenReturn(mockSession);
        bean.setSessionProvider(mockSessionProvider);
        bean.setSponsorService(mockSponsorService);
    }

    @Test
    public void testAcceptForm() {
        ProtocolForm1572 protocolForm = new ProtocolForm1572();
        protocolForm.setFormStatus(REJECTED);
        protocolForm.setComments("Testing");

        bean.acceptForm(protocolForm);
        assertEquals(ACCEPTED, protocolForm.getFormStatus());
        assertEquals("Testing", protocolForm.getComments());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAcceptForm_NotInReview() {
        ProtocolForm1572 protocolForm = new ProtocolForm1572();
        protocolForm.setFormStatus(IN_PROGRESS);
        bean.acceptForm(protocolForm);
    }

    @Test
    public void testRejectForm() {
        String comments = "These are comments";
        ProtocolForm1572 protocolForm = new ProtocolForm1572();
        protocolForm.setFormStatus(IN_REVIEW);

        bean.rejectForm(protocolForm, comments);
        assertEquals(REJECTED, protocolForm.getFormStatus());
        assertEquals(comments, protocolForm.getComments());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectForm_NotInReview() {
        ProtocolForm1572 protocolForm = new ProtocolForm1572();
        protocolForm.setFormStatus(IN_PROGRESS);
        bean.rejectForm(protocolForm, "Blah blah blah");
    }

    @Test
    public void testClearFormReviewStatus() {
        ProtocolForm1572 protocolForm = new ProtocolForm1572();
        protocolForm.setFormStatus(REJECTED);
        protocolForm.setComments("Testing");

        bean.clearFormReviewStatus(protocolForm);
        assertEquals(IN_REVIEW, protocolForm.getFormStatus());
        assertEquals("Testing", protocolForm.getComments());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClearFormReviewStatus_NotInReview() {
        ProtocolForm1572 protocolForm = new ProtocolForm1572();
        protocolForm.setFormStatus(IN_PROGRESS);
        bean.clearFormReviewStatus(protocolForm);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReviewFormFail() {
        ProtocolForm1572 protocolForm = new ProtocolForm1572();
        protocolForm.setFormStatus(SUBMITTED);

        bean.acceptForm(protocolForm);
    }

    @Test
    public void testRejectRegistration() throws CredentialAlreadyExistsException {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        profile.addCredential(CredentialFactory.getInstance().createCertificate());
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                profile, ProtocolFactory.getInstance().createWithFormsDocuments());
        RegistrationFactory.getInstance().setupInReview(registration);
        String comments = "This registration is just bad";

        registration.getForm1572().setComments("comments");
        registration.getForm1572().setFormStatus(REJECTED);
        registration.getCurriculumVitaeForm().setComments("comments");

        bean = spy(bean);
        bean.rejectRegistration(registration, comments);

        assertNotNull(registration.getForm1572().getComments());
        assertNull(registration.getCurriculumVitaeForm().getComments());

        assertEquals(RegistrationStatus.RETURNED, registration.getStatus());
        assertEquals(registration.getSponsorComments(), comments);
        verify(bean).sendRegistrationRejectedNotifications(registration);
    }

    @Test(expected = IllegalStateException.class)
    public void testRejectRegistrationInvalidStateFail() {
        InvestigatorRegistration reg = createTestRegistrationForAcceptance();
        reg.setStatus(RegistrationStatus.IN_PROGRESS);

        bean.rejectRegistration(reg, "This is a test");
    }

    private InvestigatorRegistration createTestRegistrationForAcceptance() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.IN_REVIEW);
        return registration;
    }

    @Test
    public void testRejectRegistrationNoComments() {
        InvestigatorRegistration reg = createTestRegistrationForAcceptance();
        reg.setStatus(RegistrationStatus.IN_REVIEW);
        bean = spy(bean);
        bean.rejectRegistration(reg, "");
        assertEquals(RegistrationStatus.RETURNED, reg.getStatus());
        assertEquals(reg.getSponsorComments(), "");
        verify(bean).sendRegistrationRejectedNotifications(reg);
    }

    @Test
    public void testAcceptRegistration() throws CredentialAlreadyExistsException {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                profile, ProtocolFactory.getInstance().createWithFormsDocuments());
        profile.addCredential(CredentialFactory.getInstance().createCertificate());
        RegistrationFactory.getInstance().setupInReview(registration);

        registration.setSponsorComments("comments");
        registration.getForm1572().setComments("comments");
        registration.getForm1572().setFormStatus(REJECTED);
        registration.getCurriculumVitaeForm().setComments("comments");

        bean = spy(bean);
        bean.acceptRegistration(profile.getUser(), registration);

        assertEquals(RegistrationStatus.ACCEPTED, registration.getStatus());
        assertEquals(StringUtils.EMPTY, registration.getSponsorComments());
        assertNotNull(registration.getForm1572().getComments());
        assertNull(registration.getCurriculumVitaeForm().getComments());
        verify(bean).sendRegistrationAcceptedNotifications(profile.getUser(), registration);
        verify(bean).save(registration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAcceptRegistration_NotInReview() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        bean.acceptRegistration(registration.getProfile().getUser(), registration);
    }

    @Test
    public void testCleanUpForRevision() throws CredentialAlreadyExistsException {
        FirebirdFile attachment = FirebirdFileFactory.getInstance().create();
        FirebirdFile deletedAttachment = FirebirdFileFactory.getInstance().create();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        profile.getUploadedFiles().add(attachment);
        TrainingCertificate cert = CredentialFactory.getInstance().createCertificate();
        profile.addCredential(cert);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                profile, ProtocolFactory.getInstance().createWithFormsDocuments());
        registration.getAdditionalAttachmentsForm().getAdditionalAttachments().add(attachment);
        registration.getAdditionalAttachmentsForm().getAdditionalAttachments().add(deletedAttachment);
        RegistrationFactory.getInstance().setupInReview(registration);
        String comments = "This registration is just bad";
        bean.rejectRegistration(registration, comments);
        verify(mockSession, never()).delete(cert.getFile());
        verify(mockSession).delete(deletedAttachment);
        verify(mockSession, never()).delete(attachment);
    }

    @Test
    public void testCleanUpForRevision_RemovedOrganizationAssociations() throws CredentialAlreadyExistsException,
            AssociationAlreadyExistsException {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                profile, ProtocolFactory.getInstance().createWithFormsDocuments());
        registration.setStatus(RegistrationStatus.IN_REVIEW);

        Organization irb = addOrganizationAssociation(registration, OrganizationRoleType.IRB, true);
        Organization practiceSite = addOrganizationAssociation(registration, OrganizationRoleType.PRACTICE_SITE, true);
        Organization lab = addOrganizationAssociation(registration, OrganizationRoleType.CLINICAL_LABORATORY, true);
        addOrganizationAssociation(registration, OrganizationRoleType.IRB, false);
        addOrganizationAssociation(registration, OrganizationRoleType.PRACTICE_SITE, false);
        addOrganizationAssociation(registration, OrganizationRoleType.CLINICAL_LABORATORY, false);

        String comments = "This registration is just bad";
        bean.rejectRegistration(registration, comments);
        assertEquals(irb, Iterables.getOnlyElement(registration.getForm1572().getIrbs()));
        assertEquals(practiceSite, Iterables.getOnlyElement(registration.getForm1572().getPracticeSites()));
        assertEquals(lab, Iterables.getOnlyElement(registration.getForm1572().getLabs()));
    }

    private Organization addOrganizationAssociation(InvestigatorRegistration registration, OrganizationRoleType type,
            boolean addToProfile) throws AssociationAlreadyExistsException {
        Organization organization = OrganizationFactory.getInstance().create();
        registration.getForm1572().getAssociatedOrganizations(type).add(organization);
        if (addToProfile) {
            registration.getProfile().addOrganizationAssociation(organization, type);
        }
        return organization;
    }

}
