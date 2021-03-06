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
package gov.nih.nci.firebird.test.data;

import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.InstitutionalReviewBoard;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.LaboratoryCertificateType;
import gov.nih.nci.firebird.data.OrderingDesignee;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.PrimaryOrganizationType;
import gov.nih.nci.firebird.data.ShippingDesignee;
import gov.nih.nci.firebird.data.SubInvestigator;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.nes.ExternalEntityTestDataSource;
import gov.nih.nci.firebird.test.nes.TargetGridResources;

import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Hibernate;

import com.google.common.collect.Iterators;

public class ProfileBuilder extends AbstractDataComponentBuilder<InvestigatorProfile> {

    private final CredentialTypesData credentialTypesData;
    private InvestigatorProfile profile = new InvestigatorProfile();
    private Iterator<PracticeSiteType> PRACTICE_SITE_TYPES = Iterators.cycle(PracticeSiteType.values());

    protected ProfileBuilder(TargetGridResources gridResources, TestDataLoader dataLoader,
            CredentialTypesData credentialTypesData) {
        super(gridResources, dataLoader);
        this.credentialTypesData = credentialTypesData;
        profile.setPerson(getTestDataSource().getPerson());
        Organization primaryOrganization = getGridResources().getTestDataSource().getPracticeSite()
                .getOrganization();
        profile.setPrimaryOrganization(new PrimaryOrganization(primaryOrganization,
                PrimaryOrganizationType.HEALTH_CARE_FACILITY));
    }

    public ProfileBuilder complete() {
        try {
            setUpAssociatedOrganizations();
            setupDesigneeAssociations();
            setUpCredentialsNoMd();
        } catch (AssociationAlreadyExistsException e) {
            throw new IllegalStateException("Couldn't create complete profile for investigator", e);
        } catch (CredentialAlreadyExistsException e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    public ProfileBuilder withPerson(Person person) {
        profile.setPerson(person);
        return this;
    }

    private void setUpAssociatedOrganizations() throws AssociationAlreadyExistsException {
        addPracticeSite();
        addPracticeSite();
        addClinicalLab();
        addClinicalLab();
        profile.addOrganizationAssociation(getTestDataSource().getIrb().getOrganization(), OrganizationRoleType.IRB);
        profile.addOrganizationAssociation(createNewIrb().getOrganization(), OrganizationRoleType.IRB);
    }

    private void addPracticeSite() throws AssociationAlreadyExistsException {
        OrganizationAssociation association = profile.addOrganizationAssociation(setUpPracticeSiteWithOhrp(),
                OrganizationRoleType.PRACTICE_SITE);
        ((PracticeSite) association.getOrganizationRole()).setType(PRACTICE_SITE_TYPES.next());
    }
    private void addClinicalLab() throws AssociationAlreadyExistsException {
        OrganizationAssociation association = profile.addOrganizationAssociation(
                getTestDataSource().getClinicalLab().getOrganization(),
                OrganizationRoleType.CLINICAL_LABORATORY);
        ClinicalLaboratory lab = (ClinicalLaboratory) association.getOrganizationRole();
        LaboratoryCertificate certificate = new LaboratoryCertificate(LaboratoryCertificateType.CAP);
        certificate.setCertificateFile(FirebirdFileFactory.getInstance().create());
        certificate.setEffectiveDate(DateUtils.addYears(new Date(), -1));
        certificate.setExpirationDate(DateUtils.addYears(new Date(), 1));
        lab.addCertificate(certificate);
    }


    private void setupDesigneeAssociations() throws AssociationAlreadyExistsException {
        profile.addOrderingDesignee(getTestDataSource().getPerson());
        profile.addOrderingDesignee(getTestDataSource().getPerson());
        ShippingDesignee shippingDesignee = new ShippingDesignee(profile, getTestDataSource().getPerson());
        shippingDesignee.setOrganization(getNesOrganization());
        shippingDesignee.setShippingAddress(ValueGenerator.getUniqueAddress());
        profile.setShippingDesignee(shippingDesignee);
    }

    private ExternalEntityTestDataSource getTestDataSource() {
        return getGridResources().getTestDataSource();
    }

    private Organization setUpPracticeSiteWithOhrp() {
        PracticeSite practiceSite = getTestDataSource().getPracticeSite();
        practiceSite.setOhrpAssuranceNumber(ValueGenerator.getUniqueOhrpNumber());
        return practiceSite.getOrganization();
    }

    private InstitutionalReviewBoard createNewIrb() {
        InstitutionalReviewBoard irb = new InstitutionalReviewBoard();
        Organization organization = getNesOrganization();
        if (organization.getRole(OrganizationRoleType.IRB) == null) {
            organization.addRole(irb);
        } else {
            irb.setOrganization(organization);
        }
        return irb;
    }

    private Organization getNesOrganization() {
        return getTestDataSource().getOrganization();
    }

    private void setUpCredentialsNoMd() throws CredentialAlreadyExistsException {
        Degree degree = CredentialFactory.getInstance().createDegree(
                credentialTypesData.getNonMdEquivalentDegreeType(), getNesOrganization());
        degree.setIssuer(getNesOrganization());
        profile.addCredential(degree);
        setUpCredentials();
    }

    private void setUpCredentials() throws CredentialAlreadyExistsException {
        profile.addCredential(CredentialFactory.getInstance().createLicense(credentialTypesData.getLicenseType()));
        profile.addCredential(CredentialFactory.getInstance().createSpecialty(credentialTypesData.getSpecialtyType()));
        profile.addCredential(CredentialFactory.getInstance().createCertification(
                credentialTypesData.getCertificationType()));
        profile.addCredential(CredentialFactory.getInstance().createFellowship(getNesOrganization(),
                credentialTypesData.getMedicalSubSpecialty()));
        profile.addCredential(CredentialFactory.getInstance().createInternship(getNesOrganization(),
                credentialTypesData.getMedicalSpecialty()));
        profile.addCredential(CredentialFactory.getInstance().createInternship(getNesOrganization(),
                credentialTypesData.getMedicalSpecialty()));
        profile.addCredential(CredentialFactory.getInstance().createResidency(getNesOrganization(),
                credentialTypesData.getMedicalSpecialty()));
        profile.addCredential(CredentialFactory.getInstance().createWorkHistory(getNesOrganization()));
        profile.addCredential(CredentialFactory.getInstance().createWorkHistory(getNesOrganization()));
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate(
                CertificateType.HUMAN_RESEARCH_CERTIFICATE, getNesOrganization());
        profile.addCredential(certificate);
    }

    @Override
    void reload() {
        super.reload();
        Hibernate.initialize(profile.getPrimaryOrganization());
        for (AbstractProtocolRegistration protocolRegistration : profile.getCurrentProtocolRegistrations()) {
            AbstractProtocolRegistrationBuilder.initialize(protocolRegistration);
        }
        for (AnnualRegistration annualRegistration : profile.getAnnualRegistrations()) {
            AnnualRegistrationBuilder.initialize(annualRegistration);
        }
        for (AbstractCredential<?> credential : profile.getCredentials()) {
            Hibernate.initialize(credential);
        }
        for (SubInvestigator subInvestigator : profile.getSubInvestigators()) {
            Hibernate.initialize(subInvestigator);
        }
        for (OrganizationAssociation association : profile.getOrganizationAssociations()) {
            Hibernate.initialize(association);
        }
        for (OrderingDesignee designee : profile.getOrderingDesignees()) {
            Hibernate.initialize(designee);
        }
    }

    @Override
    InvestigatorProfile getObject() {
        return profile;
    }

    @Override
    void setObject(InvestigatorProfile profile) {
        this.profile = profile;
    }

}
