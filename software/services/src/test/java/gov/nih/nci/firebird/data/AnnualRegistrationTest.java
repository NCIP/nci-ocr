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
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus;
import gov.nih.nci.firebird.data.user.RegistrationCoordinatorRoleUtil;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.FormTypeFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.ValueGenerator;

import java.util.Date;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class AnnualRegistrationTest {

    private FormType form1572FormType = createFormType(FormTypeEnum.CTEP_FORM_1572);
    private FormType finanicalDisclosureFormType = createFormType(FormTypeEnum.CTEP_FINANCIAL_DISCLOSURE_FORM);
    private FormType sidfFormType = createFormType(FormTypeEnum.SUPPLEMENTAL_INVESTIGATOR_DATA_FORM);
    private FormType additionalAttachmentsFormType = createFormType(FormTypeEnum.ADDITIONAL_ATTACHMENTS);

    private FormType createFormType(FormTypeEnum formTypeEnum) {
        return FormTypeFactory.getInstance().create(formTypeEnum);
    }

    @Test
    public void testConfigureForm() {
        AnnualRegistration registration = new AnnualRegistration();

        checkAllFormsAreNull(registration);
        configureAllFormTypes(registration);

        assertNotNull(registration.getForm1572());
        assertNotNull(registration.getFinancialDisclosure());
        assertNotNull(registration.getSupplementalInvestigatorDataForm());
        assertNotNull(registration.getAdditionalAttachmentsForm());
    }

    private void checkAllFormsAreNull(AnnualRegistration registration) {
        assertNull(registration.getForm1572());
        assertNull(registration.getFinancialDisclosure());
        assertNull(registration.getSupplementalInvestigatorDataForm());
        assertNull(registration.getAdditionalAttachmentsForm());
    }

    private void configureAllFormTypes(AnnualRegistration registration) {
        registration.configureForm(form1572FormType);
        registration.configureForm(finanicalDisclosureFormType);
        registration.configureForm(sidfFormType);
        registration.configureForm(additionalAttachmentsFormType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureForm_IllegalArgument() {
        AnnualRegistration registration = new AnnualRegistration();
        registration.configureForm(createFormType(FormTypeEnum.FORM_1572));
    }

    @Test
    public void testRemoveForm() {
        AnnualRegistration registration = new AnnualRegistration();
        configureAllFormTypes(registration);
        registration.removeForm(form1572FormType);
        registration.removeForm(finanicalDisclosureFormType);
        registration.removeForm(sidfFormType);
        registration.removeForm(additionalAttachmentsFormType);
        checkAllFormsAreNull(registration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveForm_IllegalArgument() {
        AnnualRegistration registration = new AnnualRegistration();
        registration.removeForm(createFormType(FormTypeEnum.FORM_1572));
    }

    @Test
    public void testGetSponsor() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        assertEquals(registration.getConfiguration().getSponsor(), registration.getSponsor());
    }

    @Test
    public void testGetApprovedCoordinatorAndNotificationListEmailAddresses_CoordinatorsAndNotificationList() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        String coordinatorEmail = addRegistrationCoordinator(true, registration);
        String coordinatorEmail2 = addRegistrationCoordinator(true, registration);
        String notificationEmail1 = addNotificationEmailAddress(registration);
        String notificationEmail2 = addNotificationEmailAddress(registration);
        Set<String> emailAddresses = registration.getApprovedCoordinatorAndNotificationListEmailAddresses();
        Set<String> expectedEmailAddresses = Sets.newHashSet(coordinatorEmail, coordinatorEmail2, notificationEmail1,
                notificationEmail2);
        assertEquals(expectedEmailAddresses, emailAddresses);
    }

    private String addRegistrationCoordinator(boolean approved, AnnualRegistration registration) {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        ManagedInvestigator managedInvestigator = RegistrationCoordinatorRoleUtil.newManagedInvestigator(coordinator,
                registration.getProfile());
        if (approved) {
            managedInvestigator.setStatus(ManagedInvestigatorStatus.APPROVED);
        }
        registration.getProfile().getRegistrationCoordinatorMappings().add(managedInvestigator);
        return coordinator.getPerson().getEmail();
    }

    private String addNotificationEmailAddress(AnnualRegistration registration) {
        String notificationEmailAddress = ValueGenerator.getUniqueEmailAddress();
        registration.getNotificationEmailAddresses().add(notificationEmailAddress);
        return notificationEmailAddress;
    }

    @Test
    public void testGetApprovedCoordinatorAndNotificationListEmailAddresses_JustCoordinators() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        String coordinatorEmail = addRegistrationCoordinator(true, registration);
        String coordinatorEmail2 = addRegistrationCoordinator(true, registration);
        Set<String> emailAddresses = registration.getApprovedCoordinatorAndNotificationListEmailAddresses();
        Set<String> expectedEmailAddresses = Sets.newHashSet(coordinatorEmail, coordinatorEmail2);
        assertEquals(expectedEmailAddresses, emailAddresses);
    }

    @Test
    public void testGetApprovedCoordinatorAndNotificationListEmailAddresses_JustNotificationList() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        String notificationEmail1 = addNotificationEmailAddress(registration);
        String notificationEmail2 = addNotificationEmailAddress(registration);
        Set<String> emailAddresses = registration.getApprovedCoordinatorAndNotificationListEmailAddresses();
        Set<String> expectedEmailAddresses = Sets.newHashSet(notificationEmail1, notificationEmail2);
        assertEquals(expectedEmailAddresses, emailAddresses);
    }

    @Test
    public void testGetApprovedCoordinatorAndNotificationListEmailAddresses_None() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        Set<String> emailAddresses = registration.getApprovedCoordinatorAndNotificationListEmailAddresses();
        assertTrue(emailAddresses.isEmpty());
    }

    @Test
    public void testIsPendingRenewal_True() {
        AnnualRegistration registration = new AnnualRegistration();
        registration.setDueDate(new Date());
        assertTrue(registration.isPendingRenewal());
    }

    @Test
    public void testIsPendingRenewal_AlreadySubmitted() {
        AnnualRegistration registration = new AnnualRegistration();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        assertFalse(registration.isPendingRenewal());
    }

    @Test
    public void testIsPendingRenewal_NoDueDate() {
        AnnualRegistration registration = new AnnualRegistration();
        assertFalse(registration.isPendingRenewal());
    }

    @Test
    public void testApprovable() {
        AnnualRegistration registration = new AnnualRegistration();
        assertFalse(registration.isApprovable());
        registration.setStatus(RegistrationStatus.ACCEPTED);
        assertTrue(registration.isApprovable());
    }

    @Test
    public void testIsFinalized() throws Exception {
        AnnualRegistration registration = new AnnualRegistration();
        for (RegistrationStatus status : RegistrationStatus.values()) {
            registration.setStatus(status);
            boolean expected = status == RegistrationStatus.APPROVED;
            assertEquals(expected, registration.isFinalized());
        }
    }

    @Test
    public void testGetDocumentsForReview() throws Exception {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        addTwoSignedFormPdfs(registration);
        registration.getAdditionalAttachmentsForm().getAdditionalAttachments().add(new FirebirdFile());
        addLabCertificate(registration);
        registration.getFinancialDisclosure().getSupportingDocumentation().add(new FirebirdFile());
        assertEquals(5, registration.getDocumentsForReview().size());
    }

    private void addTwoSignedFormPdfs(AnnualRegistration registration) {
        Iterables.getFirst(registration.getFormConfiguration().keySet(), null).setSigningField("anything");
        Iterables.getLast(registration.getFormConfiguration().keySet()).setSigningField("anything");
        for (AbstractRegistrationForm form : registration.getForms()) {
            form.submitForm();
            form.setFlattenedPdf(new FirebirdFile());
        }
    }

    private void addLabCertificate(AnnualRegistration registration) {
        LaboratoryCertificate laboratoryCertificate = new LaboratoryCertificate(LaboratoryCertificateType.CAP);
        laboratoryCertificate.setCertificateFile(new FirebirdFile());
        ClinicalLaboratory lab = new ClinicalLaboratory();
        lab.addCertificate(laboratoryCertificate);
        Organization organization = OrganizationFactory.getInstance().create();
        organization.addRole(lab);
        registration.getForm1572().getLabs().add(organization);
    }

    @Test
    public void testSetRenewal() throws Exception {
        AnnualRegistration parent = new AnnualRegistration();
        AnnualRegistration renewal = new AnnualRegistration();
        parent.setRenewal(renewal);
        assertEquals(renewal, parent.getRenewal());
        assertEquals(parent, renewal.getParent());
    }

    @Test
    public void testSetRenewal_Null() throws Exception {
        AnnualRegistration parent = new AnnualRegistration();
        parent.setRenewal(null);
        assertNull(parent.getRenewal());
    }

    @Test
    public void testSetParent() throws Exception {
        AnnualRegistration renewal = new AnnualRegistration();
        renewal.setParent(null);
        assertNull(renewal.getParent());
    }

    @Test
    public void testPrepareForDeletion() throws Exception {
        AnnualRegistration parent = AnnualRegistrationFactory.getInstance().create();
        AnnualRegistration renewal = AnnualRegistrationFactory.getInstance().create();
        parent.setRenewal(renewal);
        assertEquals(renewal, parent.getRenewal());
        assertEquals(parent, renewal.getParent());
        renewal.prepareForDeletion();
        assertNull(parent.getRenewal());
        assertNull(renewal.getParent());
    }

    @Test
    public void testPrepareForDeletion_NoParent() throws Exception {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        registration.prepareForDeletion();
        assertNull(registration.getParent());
    }

}
