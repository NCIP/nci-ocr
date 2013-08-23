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
package gov.nih.nci.firebird.service.investigatorprofile;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.AbstractPersonAssociation;
import gov.nih.nci.firebird.data.CertifiedSpecialtyBoard;
import gov.nih.nci.firebird.data.CertifiedSpecialtyType;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.OrderingDesignee;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.PersistentObjectUtil;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.ShippingDesignee;
import gov.nih.nci.firebird.data.SubInvestigator;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.AbstractGenericServiceBean;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonAssociationService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of the investigator profile service.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SuppressWarnings({ "PMD.TooManyMethods" })
// facade service expected to expose many methods
public class InvestigatorProfileServiceBean extends AbstractGenericServiceBean<InvestigatorProfile> implements
        InvestigatorProfileService {

    private static final String PROFILE_BY_PERSON_QUERY = "from " + InvestigatorProfile.class.getName()
            + " where person = :person";
    private static final String SPECIALTIES_BY_BOARD = "from " + CertifiedSpecialtyType.class.getName()
            + " where board = :board";

    private OrganizationService organizationService;
    private OrganizationAssociationService organizationAssociationService;
    private PersonAssociationService personAssociationService;
    private FileService fileService;
    private ProtocolRegistrationService registrationService;
    private InvestigatorProfileHelper investigatorProfileHelper;
    private InvestigatorRegistrationHelper investigatorRegistrationHelper;

    @Override
    public void setPrimaryOrganization(InvestigatorProfile profile, PrimaryOrganization primaryOrganization)
            throws ValidationException {
        Preconditions.checkNotNull(profile.getPerson());
        Preconditions.checkNotNull(primaryOrganization);
        investigatorProfileHelper.associatePrimaryOrganization(profile, primaryOrganization);
        save(profile, FormTypeEnum.CV, FormTypeEnum.FINANCIAL_DISCLOSURE_FORM);
    }

    @Override
    public void createPrimaryOrganization(InvestigatorProfile profile) throws ValidationException {
        Preconditions.checkNotNull(profile.getPerson());
        Preconditions.checkNotNull(profile.getPrimaryOrganization());
        Preconditions.checkNotNull(profile.getPrimaryOrganization().getType(),
                "Primary organization type can't be null");
        Preconditions.checkArgument(PersistentObjectUtil.isNew(profile.getPrimaryOrganization().getOrganization()),
                "Profile primary organization is not new");
        organizationAssociationService.createNewPrimaryOrganization(profile.getPrimaryOrganization());
        investigatorProfileHelper.checkCorrelation(profile);
        save(profile, FormTypeEnum.CV, FormTypeEnum.FINANCIAL_DISCLOSURE_FORM);
    }

    @Override
    public OrganizationAssociation addAssociatedPracticeSite(InvestigatorProfile profile, Organization organization,
                                                             String dataField, PracticeSiteType practiceSiteType)
            throws AssociationAlreadyExistsException,
            ValidationException {
        OrganizationAssociation association = profile.addOrganizationAssociation(organization,
                OrganizationRoleType.PRACTICE_SITE);
        association.getOrganizationRole().setDataField(dataField);
        ((PracticeSite) association.getOrganizationRole()).setType(practiceSiteType);
        saveNewAssociation(profile, association);
        return association;
    }

    private void saveNewAssociation(InvestigatorProfile profile, OrganizationAssociation association)
            throws ValidationException {
        organizationAssociationService.handleNew(association);
        save(profile);
    }

    @Override
    public OrganizationAssociation addAssociatedInstitutionalReviewBoard(InvestigatorProfile profile,
                                                                         Organization organization)
            throws AssociationAlreadyExistsException, ValidationException {
        OrganizationAssociation association = profile
                .addOrganizationAssociation(organization, OrganizationRoleType.IRB);
        saveNewAssociation(profile, association);
        return association;
    }

    @Override
    public OrganizationAssociation addAssociatedClinicalLab(InvestigatorProfile profile, Organization organization)
            throws AssociationAlreadyExistsException, ValidationException {
        OrganizationAssociation association = profile.addOrganizationAssociation(organization,
                OrganizationRoleType.CLINICAL_LABORATORY);
        saveNewAssociation(profile, association);
        return association;
    }

    @Override
    public void deleteAssociatedOrganization(InvestigatorProfile profile, OrganizationAssociation association) {
        profile.getOrganizationAssociations().remove(association);
        investigatorRegistrationHelper.deleteAssociatedOrganizationFromUnlockedRegistrations(profile, association);
        save(profile);
    }

    @Override
    public void updateAssociationOhrp(OrganizationAssociation association, String ohrp) {
        investigatorProfileHelper.validateAssociationOhrp(association, ohrp);

        association.getOrganizationRole().setDataField(ohrp);
        save(association.getProfile());
    }


    @Override
    public SubInvestigator addSubInvestigator(InvestigatorProfile profile, Person person)
            throws AssociationAlreadyExistsException, ValidationException {
        SubInvestigator subInvestigator = profile.addSubInvestigator(person);
        personAssociationService.handleNew(subInvestigator);
        save(profile);
        return subInvestigator;
    }

    @Override
    public void deleteAssociatedSubInvestigator(InvestigatorProfile profile, SubInvestigator subInvestigator) {
        profile.removeSubInvestigator(subInvestigator);
        personAssociationService.delete(subInvestigator);
        investigatorRegistrationHelper.deleteSubInvestigatorRegistrations(profile, subInvestigator);
        save(profile);
    }

    @Override
    public OrderingDesignee addOrderingDesignee(InvestigatorProfile profile, Person person)
            throws AssociationAlreadyExistsException, ValidationException {
        OrderingDesignee orderingDesignee = profile.addOrderingDesignee(person);
        personAssociationService.handleNew(orderingDesignee);
        save(profile);
        return orderingDesignee;
    }

    @Override
    public void setShippingDesignee(InvestigatorProfile profile, ShippingDesignee shippingDesignee)
            throws ValidationException {
        shippingDesignee.setProfile(profile);
        if (shippingDesignee.getOrganization() != null
                && StringUtils.isEmpty(shippingDesignee.getOrganization().getExternalId())) {
            organizationService.create(shippingDesignee.getOrganization(), OrganizationRoleType.GENERIC_ORGANIZATION);
        }
        profile.setShippingDesignee(shippingDesignee);
        personAssociationService.handleNew(shippingDesignee);
        save(profile);
    }

    @Override
    public void deleteAssociatedOrderingDesignee(InvestigatorProfile profile, OrderingDesignee orderingDesignee) {
        profile.removeOrderingDesignee(orderingDesignee);
        removePersonAssociation(profile, orderingDesignee);
    }

    @Override
    public void deleteAssociatedShippingDesignee(InvestigatorProfile profile, ShippingDesignee shippingDesignee) {
        profile.setShippingDesignee(null);
        removePersonAssociation(profile, shippingDesignee);
    }

    private void removePersonAssociation(InvestigatorProfile profile, AbstractPersonAssociation personAssociation) {
        personAssociationService.delete(personAssociation);
        save(profile);
    }

    @Override
    public void saveCredential(InvestigatorProfile profile, AbstractCredential<?> credential,
                               FormTypeEnum... formTypesToSetToRevised) throws CredentialAlreadyExistsException,
            ValidationException {
        if (credential.getIssuer() != null && StringUtils.isEmpty(credential.getIssuer().getExternalId())) {
            organizationService.create(credential.getIssuer(), OrganizationRoleType.GENERIC_ORGANIZATION);
        }
        if (PersistentObjectUtil.isNew(credential)) {
            profile.addCredential(credential);
        }
        save(profile, formTypesToSetToRevised);
    }

    @Override
    public void saveCertificate(InvestigatorProfile profile, TrainingCertificate cert, File certificateFile,
                                FileMetadata fileMetadata) throws CredentialAlreadyExistsException, IOException,
            ValidationException {
        if (fileMetadata != null) {
            FirebirdFile file = fileService.createFile(certificateFile, fileMetadata);
            cert.setFile(file);
        } else if (cert.getFile() == null) {
            throw new IllegalArgumentException("File information is required to add this certificate!");
        }
        if (!PersistentObjectUtil.isNew(cert)) {
            registrationService.setRegistrationFormStatusesToRevisedIfReviewed(
                    investigatorRegistrationHelper.getAffectedRegistrationsByCertificateModification(profile, cert),
                    FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE);
        }
        saveCredential(profile, cert, FormTypeEnum.CV);
    }


    @Override
    public InvestigatorProfile getByPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person was null");
        }
        if (person.getId() == null) {
            return null;
        }
        Query q = getSession().createQuery(PROFILE_BY_PERSON_QUERY);
        q.setEntity("person", person);
        return (InvestigatorProfile) q.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    // hibernate does not provide typed arguments
    @Override
    public List<CertifiedSpecialtyType> getSpecialtiesByBoard(CertifiedSpecialtyBoard board) {
        if (board == null) {
            throw new IllegalArgumentException("Certification Board was null");
        }
        List<CertifiedSpecialtyType> types = null;
        Query q = getSession().createQuery(SPECIALTIES_BY_BOARD);
        q.setEntity("board", board);
        types = q.list();
        Collections.sort(types, new Comparator<CertifiedSpecialtyType>() {

            @Override
            public int compare(CertifiedSpecialtyType o1, CertifiedSpecialtyType o2) {
                int ret = o1.getDesignation().name().compareTo(o2.getDesignation().name());

                if (ret == 0) {
                    ret = o1.getName().compareTo(o2.getName());
                }

                return ret;
            }
        });
        return types;
    }

    @Override
    public List<InvestigatorProfile> search(String searchTerm) {
       return investigatorProfileHelper.search(searchTerm);
    }

    @Override
    public List<InvestigatorProfile> searchForCtepProfiles(String searchTerm) {
        return investigatorProfileHelper.getOnlyCtepInvestigators(search(searchTerm));
    }

    @Override
    public Long save(InvestigatorProfile profile, FormTypeEnum... formTypesToSetToRevised) {
        registrationService.setReturnedOrRevisedRegistrationsFormStatusesToRevised(profile, formTypesToSetToRevised);
        return save(profile);
    }

    @Override
    public void deleteCertificate(InvestigatorProfile profile, TrainingCertificate certificate) {
        investigatorRegistrationHelper.breakCertificateLinks(profile, certificate);
        profile.getCredentials().remove(certificate);
        if (profile.isOrphan(certificate.getFile())) {
            getSession().delete(certificate.getFile());
        }
        save(profile, FormTypeEnum.CV);
    }


    @Override
    public FirebirdFile addFile(InvestigatorProfile profile, File contents, FileMetadata fileMetadata)
            throws IOException {
        FirebirdFile file = fileService.createFile(contents, fileMetadata);
        profile.getUploadedFiles().add(file);
        save(profile);
        return file;
    }

    @Override
    public void removeFile(InvestigatorProfile aProfile, FirebirdFile aFile) {
        FirebirdFile file = fileService.getById(aFile.getId());
        InvestigatorProfile profile = getById(aProfile.getId());
        profile.getUploadedFiles().remove(file);
        investigatorProfileHelper.removeFileFromUnLockedRegistrations(profile, file);
        save(profile);
        if (profile.isOrphan(file)) {
            fileService.delete(file);
        }
    }

    @Resource(mappedName = "firebird/OrganizationServiceBean/local")
    void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Resource(mappedName = "firebird/OrganizationAssociationServiceBean/local")
    void setOrganizationAssociationService(OrganizationAssociationService organizationAssociationService) {
        this.organizationAssociationService = organizationAssociationService;
    }

    @Resource(mappedName = "firebird/PersonAssociationServiceBean/local")
    void setPersonAssociationService(PersonAssociationService personAssociationService) {
        this.personAssociationService = personAssociationService;
    }

    @Resource(mappedName = "firebird/FileServiceBean/local")
    void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Resource(mappedName = "firebird/ProtocolRegistrationServiceBean/local")
    void setRegistrationService(ProtocolRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Inject
    void setInvestigatorProfileHelper(InvestigatorProfileHelper investigatorProfileHelper) {
        this.investigatorProfileHelper = investigatorProfileHelper;
    }

    @Inject
    public void setInvestigatorRegistrationHelper(InvestigatorRegistrationHelper investigatorRegistrationHelper) {
        this.investigatorRegistrationHelper = investigatorRegistrationHelper;
    }
}