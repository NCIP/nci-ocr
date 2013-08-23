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
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.test.AbstractHibernateTestCase;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonAssociationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.google.common.collect.Sets;

public class InvestigatorProfileHibernateTest extends AbstractHibernateTestCase {

    private final OrganizationFactory organizationFactory = OrganizationFactory.getInstance();
    private final PersonFactory personFactory = PersonFactory.getInstance();
    private InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();

    @Test
    public void testAddOrganizationAssociation() throws AssociationAlreadyExistsException {
        Organization organization = organizationFactory.create();
        OrganizationAssociation association = profile
                .addOrganizationAssociation(organization, OrganizationRoleType.IRB);
        assertEquals(profile, association.getProfile());
        assertEquals(organization, association.getOrganizationRole().getOrganization());
        getCurrentSession().save(association.getOrganizationRole().getOrganization());
        getCurrentSession().save(profile);
        flushAndClearSession();
        association = (OrganizationAssociation) getCurrentSession().get(OrganizationAssociation.class,
                association.getId());
        assertEquals(organization.getId(), association.getOrganizationRole().getOrganization().getId());
        organization = association.getOrganizationRole().getOrganization();
        assertEquals(profile.getId(), association.getProfile().getId());
        profile = association.getProfile();

        assertTrue(profile.getOrganizationAssociations().contains(association));
        assertEquals(OrganizationRoleType.IRB, association.getType());
        assertTrue(profile.getOrganizationAssociations().contains(association));
        assertTrue(profile.getOrganizationAssociations(OrganizationRoleType.IRB).contains(association));
        assertFalse(profile.getOrganizationAssociations(OrganizationRoleType.CLINICAL_LABORATORY).contains(association));
    }

    @Test(expected = AssociationAlreadyExistsException.class)
    public void testAddOrganizationAssociationDuplicate() throws AssociationAlreadyExistsException {
        Organization organization = organizationFactory.create();
        profile.addOrganizationAssociation(organization, OrganizationRoleType.IRB);
        profile.addOrganizationAssociation(organization, OrganizationRoleType.IRB);
    }

    @Test
    public void testAddPersonAssociation() throws AssociationAlreadyExistsException {
        Person person = personFactory.create();
        OrderingDesignee orderingDesignee = profile.addOrderingDesignee(person);
        assertEquals(profile, orderingDesignee.getProfile());
        assertEquals(person, orderingDesignee.getPerson());
        getCurrentSession().save(orderingDesignee.getPerson());
        getCurrentSession().save(profile);
        flushAndClearSession();
        orderingDesignee = (OrderingDesignee) getCurrentSession().get(OrderingDesignee.class, orderingDesignee.getId());
        assertEquals(person.getId(), orderingDesignee.getPerson().getId());
        person = orderingDesignee.getPerson();
        assertEquals(profile.getId(), orderingDesignee.getProfile().getId());
        profile = orderingDesignee.getProfile();

        assertTrue(profile.getOrderingDesignees().contains(orderingDesignee));
        assertTrue(profile.getOrderingDesignees().contains(orderingDesignee));
        assertTrue(profile.getOrderingDesignees().contains(orderingDesignee));
        assertFalse(profile.getSubInvestigators().contains(orderingDesignee));
    }

    @Test(expected = AssociationAlreadyExistsException.class)
    public void testAddPersonAssociationDuplicate() throws AssociationAlreadyExistsException {
        Person person = personFactory.create();
        profile.addOrderingDesignee(person);
        profile.addOrderingDesignee(person);
    }

    @Test
    public void testGetCredentialsByType() {
        Degree c = new Degree();
        profile.getCredentials().add(c);
        profile.getCredentials().add(new MedicalLicense(profile, new Date(), null, null, null, null));

        Set<AbstractCredential<?>> l = profile.getCredentials(CredentialType.DEGREE);
        assertEquals(1, l.size());
        assertSame(c, l.iterator().next());
    }

    @Test
    public void testGetCredentialsByClass() {
        Degree c = new Degree();
        profile.getCredentials().add(c);
        profile.getCredentials().add(new Certification());

        Set<Degree> l = profile.getCredentials(Degree.class);
        assertEquals(1, l.size());
        assertSame(c, l.iterator().next());
    }

    @Test
    public void testGetCredentialsBySuperClass() {
        @SuppressWarnings("serial")
        class HibernateProxy extends Degree {
        }
        HibernateProxy c = new HibernateProxy();
        profile.getCredentials().add(c);

        Set<Degree> l = profile.getCredentials(Degree.class);
        assertEquals(1, l.size());
        assertSame(c, l.iterator().next());
    }

    @Test
    public void testGetCredentialById() throws CredentialAlreadyExistsException {
        Degree degree1 = CredentialFactory.getInstance().createDegree();
        Degree degree2 = CredentialFactory.getInstance().createDegree();

        profile.addCredential(degree1);
        profile.addCredential(degree2);

        saveAndFlush(degree1.getDegreeType(), degree2.getDegreeType(), profile);

        AbstractCredential<?> cred = profile.getCredential(degree1.getId());
        assertEquals(degree1, cred);
        assertFalse(degree2.equals(cred));

        cred = profile.getCredential(degree2.getId());
        assertEquals(degree2, cred);
        assertFalse(degree1.equals(cred));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCredentialByIdInvalidId() {
        saveAndFlush(profile);
        assertNull(profile.getCredential(1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCredentialByIdNullId() {
        saveAndFlush(profile);
        assertNull(profile.getCredential(null));
    }

    @Test
    public void testAddCredential() throws CredentialAlreadyExistsException {
        Degree c = new Degree();
        profile.addCredential(c);

        Set<Degree> l = profile.getCredentials(Degree.class);
        assertSame(c, l.iterator().next());
        assertSame(profile, c.getProfile());
    }

    @Test(expected = CredentialAlreadyExistsException.class)
    public void testAddCredentialAlreadyExist() throws CredentialAlreadyExistsException {
        Degree degree = new Degree();
        degree.setId(1L);
        Degree degree2 = new Degree();
        profile.addCredential(degree);

        profile.addCredential(degree); // No failure, same as existing
        profile.addCredential(degree2);
    }

    @Test
    public void testAddRegistration() {
        assertTrue(profile.getRegistrations().isEmpty());
        InvestigatorRegistration reg = RegistrationFactory.getInstance().createInvestigatorRegistration(null,
                ProtocolFactory.getInstance().createWithFormsDocuments());
        reg.setProtocol(ProtocolFactory.getInstance().create()); // protocol cannot be null due to sorting by
                                                                 // protocolNumber
        profile.addRegistration(reg);
        assertSame(profile, reg.getProfile());
        assertEquals(1, profile.getRegistrations().size());
        assertSame(reg, profile.getRegistrations().iterator().next());
    }

    @Test
    public void testClinicalResearchExperience() {
        ClinicalResearchExperience cre = new ClinicalResearchExperience("some text");
        profile.setClinicalResearchExperience(cre);
        getCurrentSession().save(profile);
        flushAndClearSession();
        profile = reloadObject(profile);
        assertEquals(cre.getText(), profile.getClinicalResearchExperience().getText());
    }

    @Test
    public void testRemovePersonAssociation() throws AssociationAlreadyExistsException {
        Person person = personFactory.create();
        OrderingDesignee orderingDesignee = profile.addOrderingDesignee(person);
        assertTrue(profile.getOrderingDesignees().contains(orderingDesignee));
        profile.removeOrderingDesignee(orderingDesignee);
        assertFalse(profile.getOrderingDesignees().contains(orderingDesignee));
    }

    @Test
    public void testGetAllPersons() throws AssociationAlreadyExistsException {
        profile.addOrderingDesignee(personFactory.create());
        profile.addSubInvestigator(personFactory.create());
        profile.setShippingDesignee(PersonAssociationFactory.getInstance().createShippingDesignee());
        List<Person> persons = profile.getAllPersons();
        assertEquals(4, persons.size());
    }

    @Test
    public void testGetAllOrganizations() throws AssociationAlreadyExistsException, CredentialAlreadyExistsException {
        Organization associatedOrganization = organizationFactory.create();
        profile.addOrganizationAssociation(associatedOrganization, OrganizationRoleType.CLINICAL_LABORATORY);
        profile.addCredential(CredentialFactory.getInstance().createDegree());
        List<Organization> organizations = profile.getAllOrganizations();
        assertEquals(3, organizations.size());
    }

    @Test
    public void testGetCurrentProtocolRegistrations() {
        InvestigatorRegistration investigatorRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        SubInvestigatorRegistration subinvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        AnnualRegistration annualRegistration = new AnnualRegistration();
        profile.addRegistration(investigatorRegistration);
        profile.addRegistration(subinvestigatorRegistration);
        profile.addRegistration(annualRegistration);
        assertEquals(2, profile.getCurrentProtocolRegistrations().size());
        assertTrue(profile.getCurrentProtocolRegistrations().contains(investigatorRegistration));
        assertTrue(profile.getCurrentProtocolRegistrations().contains(subinvestigatorRegistration));
    }

    @Test
    public void testGetCurrentProtocolRegistrations_RevisionsAreExcluded() {
        InvestigatorRegistration investigatorRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        investigatorRegistration.setStatus(RegistrationStatus.APPROVED);
        profile.addRegistration(investigatorRegistration);
        investigatorRegistration.createRevisedRegistration();
        assertEquals(1, profile.getCurrentProtocolRegistrations().size());
        assertTrue(profile.getCurrentProtocolRegistrations().contains(investigatorRegistration));
    }

    @Test
    public void testGetCurrentProtocolRegistrations_SubinvestigatorRegistrationsRemovedInRevisionAreExcluded() {
        InvestigatorRegistration investigatorRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        SubInvestigatorRegistration subinvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        profile.addRegistration(subinvestigatorRegistration);
        profile.addRegistration(investigatorRegistration);
        subinvestigatorRegistration.setPrimaryRegistration(investigatorRegistration);
        investigatorRegistration.setStatus(RegistrationStatus.APPROVED);
        RevisedInvestigatorRegistration revisedRegistration = investigatorRegistration.createRevisedRegistration();
        profile.addRegistration(revisedRegistration);
        subinvestigatorRegistration.setPrimaryRegistration(null);
        investigatorRegistration.getSubinvestigatorRegistrations().remove(subinvestigatorRegistration);
        assertEquals(1, profile.getCurrentProtocolRegistrations().size());
        assertTrue(profile.getCurrentProtocolRegistrations().contains(investigatorRegistration));
    }

    @Test
    public void testGetAllProtocolRegistrations() {
        InvestigatorRegistration investigatorRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        investigatorRegistration.setStatus(RegistrationStatus.APPROVED);
        profile.addRegistration(investigatorRegistration);
        RevisedInvestigatorRegistration revisedRegistration = investigatorRegistration.createRevisedRegistration();
        profile.addRegistration(revisedRegistration);
        assertEquals(2, profile.getAllProtocolRegistrations().size());
        assertTrue(profile.getAllProtocolRegistrations().contains(investigatorRegistration));
        assertTrue(profile.getAllProtocolRegistrations().contains(revisedRegistration));
    }

    @Test
    public void testGetAnnualRegistrations() {
        InvestigatorRegistration investigatorRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        SubInvestigatorRegistration subinvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        AnnualRegistration annualRegistration = new AnnualRegistration();
        profile.addRegistration(investigatorRegistration);
        profile.addRegistration(subinvestigatorRegistration);
        profile.addRegistration(annualRegistration);
        assertEquals(1, profile.getAnnualRegistrations().size());
        assertTrue(profile.getAnnualRegistrations().contains(annualRegistration));
    }

    @Test
    public void testGetAnnualRegistrationsAwaitingRenewal() {
        AnnualRegistration registrationWithDueDate = addAnnualRegistration(profile,
                DateUtils.addDays(new Date(), 60 - 1));
        addAnnualRegistration(profile, null); // without due date
        AnnualRegistration registrationAlreadySubmitted = addAnnualRegistration(profile,
                DateUtils.addDays(new Date(), 60 - 1));
        registrationAlreadySubmitted.setStatus(RegistrationStatus.SUBMITTED);

        assertEquals(Sets.newHashSet(registrationWithDueDate), profile.getAnnualRegistrationsAwaitingRenewal());
    }

    private AnnualRegistration addAnnualRegistration(InvestigatorProfile profile, Date dueDate) {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        registration.setDueDate(dueDate);
        profile.addRegistration(registration);
        return registration;
    }

    @Test
    public void testGetCurrentAnnualRegistration() {
        AnnualRegistration registration1 = AnnualRegistrationFactory.getInstanceWithId().create();
        AnnualRegistration registration2 = AnnualRegistrationFactory.getInstanceWithId().create();
        profile.addRegistration(registration1);
        profile.addRegistration(registration2);
        assertSame(registration2, profile.getCurrentAnnualRegistration());
    }

    @Test
    public void testGetCurrentAnnualRegistration_NoRegistrations() {
        assertNull(profile.getCurrentAnnualRegistration());
    }

    @Test
    public void testHasCtepRegistrationCoordinator_True() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        ManagedInvestigator coordinatorMapping = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(
                profile);
        coordinatorMapping.setCtepAssociate(true);
        assertTrue(profile.hasCtepRegistrationCoordinator());
    }

    @Test
    public void testHasCtepRegistrationCoordinator_CoordinatorNotCtep() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(profile);
        assertFalse(profile.hasCtepRegistrationCoordinator());
    }

    @Test
    public void testHasCtepRegistrationCoordinator_NoCoordinators() {
        assertFalse(profile.hasCtepRegistrationCoordinator());
    }

    @Test
    public void testGetHumanResearchCertificates() throws CredentialAlreadyExistsException {
        TrainingCertificate certificate1 = CredentialFactory.getInstance().createCertificate(
                CertificateType.HUMAN_RESEARCH_CERTIFICATE);
        TrainingCertificate certificate2 = CredentialFactory.getInstance().createCertificate(
                CertificateType.HUMAN_RESEARCH_CERTIFICATE);
        profile.addCredential(certificate1);
        profile.addCredential(certificate2);
        List<TrainingCertificate> humanResearchCertificates = profile.getHumanResearchCertificates();
        assertEquals(2, humanResearchCertificates.size());
        assertTrue(humanResearchCertificates.contains(certificate1));
        assertTrue(humanResearchCertificates.contains(certificate2));
    }

    @Test
    public void testGetHumanResearchCertificates_Empty() throws CredentialAlreadyExistsException {
        List<TrainingCertificate> humanResearchCertificates = profile.getHumanResearchCertificates();
        assertTrue(humanResearchCertificates.isEmpty());
    }

    @Test
    public void testSetShippingDesignee() throws Exception {
        ShippingDesignee shippingDesignee = PersonAssociationFactory.getInstance().createShippingDesignee();
        profile.setShippingDesignee(shippingDesignee);
        assertEquals(shippingDesignee, profile.getShippingDesignee());
        profile.setShippingDesignee(null);
        assertNull(profile.getShippingDesignee());
    }

    @Test
    public void testAddSubInvestigator() throws Exception {
        Person person = personFactory.create();
        SubInvestigator subInvestigator = profile.addSubInvestigator(person);
        assertEquals(1, profile.getSubInvestigators().size());
        assertEquals(subInvestigator, profile.getSubInvestigators().iterator().next());
        assertEquals(person, subInvestigator.getPerson());
    }

    @Test(expected = AssociationAlreadyExistsException.class)
    public void testAddSubInvestigator_Duplicate() throws Exception {
        Person person = personFactory.create();
        profile.addSubInvestigator(person);
        profile.addSubInvestigator(person);
    }

    @Test
    public void testRemoveSubInvestigator() throws Exception {
        Person person = personFactory.create();
        SubInvestigator subInvestigator = profile.addSubInvestigator(person);
        assertEquals(1, profile.getSubInvestigators().size());
        profile.removeSubInvestigator(subInvestigator);
        assertTrue(profile.getSubInvestigators().isEmpty());
    }

    @Test
    public void testGetCurrentCredentials() throws Exception {
        Degree degree = new Degree();
        profile.getCredentials().add(degree);
        MedicalLicense medicalLicense = new MedicalLicense(profile, new Date(), null, null, null, null);
        profile.getCredentials().add(medicalLicense);
        SortedSet<AbstractCredential<?>> degrees = profile.getCurrentCredentials(CredentialType.DEGREE);
        assertEquals(Sets.newHashSet(degree), degrees);
        SortedSet<AbstractCredential<?>> licenses = profile.getCurrentCredentials(CredentialType.MEDICAL_LICENSE);
        assertEquals(Sets.newHashSet(medicalLicense), licenses);
    }

    @Test
    public void testRemoveRegistration() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        profile.addRegistration(registration);
        assertEquals(1, profile.getRegistrations().size());
        profile.removeRegistration(registration);
        assertTrue(profile.getRegistrations().isEmpty());

    }

}
