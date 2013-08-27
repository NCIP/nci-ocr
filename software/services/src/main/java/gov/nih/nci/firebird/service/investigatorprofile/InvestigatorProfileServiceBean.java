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

import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.AbstractPersonAssociation;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.CertifiedSpecialtyBoard;
import gov.nih.nci.firebird.data.CertifiedSpecialtyType;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.HumanResearchCertificateForm;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.OrderingDesignee;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.ProtocolForm1572;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.ShippingDesignee;
import gov.nih.nci.firebird.data.SubInvestigator;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.correlation.NesPersonRoleIntegrationService;
import gov.nih.nci.firebird.nes.correlation.PersonRoleType;
import gov.nih.nci.firebird.service.AbstractGenericServiceBean;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonAssociationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.service.search.FirebirdAnnotatedBeanSearchCriteria;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.validator.EmailValidator;
import org.hibernate.Query;
import org.jboss.ejb3.common.proxy.plugins.async.AsyncUtils;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.fiveamsolutions.nci.commons.search.SearchCriteria;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * Implementation of the investigator profile service.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.ExcessiveClassLength" })
// facade service expected to expose many methods
public class InvestigatorProfileServiceBean extends AbstractGenericServiceBean<InvestigatorProfile> implements
        InvestigatorProfileService {

    private static final String PERSON_LIST_PARAMETER_NAME = "persons";
    private static final String PROFILES_BY_PERSON_HQL = "from " + InvestigatorProfile.class.getName()
            + " where person in (:" + PERSON_LIST_PARAMETER_NAME + ") order by person.lastName, person.firstName";
    private static final String PROFILE_BY_PERSON_QUERY = "from " + InvestigatorProfile.class.getName()
            + " where person = :person";
    private static final String SPECIALTIES_BY_BOARD = "from " + CertifiedSpecialtyType.class.getName()
            + " where board = :board";

    static final EnumSet<RegistrationStatus> INVALID_SUBINVESTIGATOR_REMOVAL_STATES = EnumSet
            .of(RegistrationStatus.RETURNED);

    private PersonService personService;
    private OrganizationService organizationService;
    private OrganizationAssociationService organizationAssociationService;
    private PersonAssociationService personAssociationService;
    private NesPersonRoleIntegrationService nesPersonRoleService;
    private FileService fileService;
    private ProtocolRegistrationService registrationService;

    @Override
    public void setPrimaryPerson(final InvestigatorProfile profile, final Person person) throws ValidationException {
        associatePerson(profile, person);
    }

    private void associatePerson(final InvestigatorProfile profile, Person person) throws ValidationException {
        if (isNew(person)) {
            personService.save(person);
        }
        profile.setPerson(person);
        checkCorrelation(profile);
        save(profile);
    }

    @Override
    public void setPrimaryOrganization(InvestigatorProfile profile, PrimaryOrganization primaryOrganization)
            throws ValidationException {
        if (primaryOrganization != null) {
            associatePrimaryOrganization(profile, primaryOrganization);
        } else {
            throw new IllegalArgumentException("The provided organization cannot be null!");
        }
    }

    @Override
    public void createPrimaryOrganization(InvestigatorProfile profile) throws ValidationException {
        Preconditions.checkNotNull(profile.getPrimaryOrganization());
        Preconditions.checkNotNull(profile.getPrimaryOrganization().getType(),
                "Primary organization type can't be null");
        Preconditions.checkArgument(isNew(profile.getPrimaryOrganization().getOrganization()),
                "Profile primary organization is not new");
        organizationAssociationService.createNewPrimaryOrganization(profile.getPrimaryOrganization());
        checkCorrelation(profile);
        save(profile, FormTypeEnum.CV, FormTypeEnum.FINANCIAL_DISCLOSURE_FORM);
    }

    private boolean isNew(PersistentObject persistentObject) {
        return persistentObject.getId() == null;
    }

    private void associatePrimaryOrganization(InvestigatorProfile profile, PrimaryOrganization primaryOrganization)
            throws ValidationException {
        profile.setPrimaryOrganization(primaryOrganization);
        checkCorrelation(profile);
        save(profile, FormTypeEnum.CV, FormTypeEnum.FINANCIAL_DISCLOSURE_FORM);
    }

    private void checkCorrelation(InvestigatorProfile profile) throws ValidationException {
        Person person = profile.getPerson();
        Organization organization = profile.getPrimaryOrganization().getOrganization();
        if (person != null && organization != null) {
            ensureHealthCareProviderRoleExists(person, organization);
        }
    }

    private void ensureHealthCareProviderRoleExists(Person person, Organization organization)
            throws ValidationException {
        NesPersonRoleIntegrationService asyncProxy = AsyncUtils.mixinAsync(nesPersonRoleService);
        asyncProxy.ensureCorrelated(person, organization, PersonRoleType.HEALTH_CARE_PROVIDER);
    }

    @Override
    public OrganizationAssociation addAssociatedPracticeSite(InvestigatorProfile profile, Organization organization,
            String dataField, PracticeSiteType practiceSiteType) throws AssociationAlreadyExistsException,
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
            Organization organization) throws AssociationAlreadyExistsException, ValidationException {
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
        registrationService.setRegistrationFormStatusesToRevisedIfReviewed(
                getAffectedRegistrationsByAssociatedOrganizationDelete(profile, association.getOrganizationRole()
                        .getOrganization()), FormTypeEnum.FORM_1572);
        deleteAssociatedOrganizationFromUnlockedRegistrations(profile, association);
        save(profile);
    }

    private Set<AbstractProtocolRegistration> getAffectedRegistrationsByAssociatedOrganizationDelete(
            InvestigatorProfile profile, Organization organization) {
        Set<AbstractProtocolRegistration> registrations = Sets.newHashSet();
        for (AbstractProtocolRegistration registration
                : registrationService.getReturnedOrRevisedRegistrations(profile)) {
            ProtocolForm1572 protocolForm1572 = registration.getForm1572();
            if (protocolForm1572 != null
                    && CollectionUtils.exists(protocolForm1572.getOrganizations(), new EqualPredicate(organization))) {
                registrations.add(registration);
                break;
            }
        }
        return registrations;
    }

    private void deleteAssociatedOrganizationFromUnlockedRegistrations(InvestigatorProfile profile,
            OrganizationAssociation association) {
        for (AbstractRegistration registration : getUnlockedRegistrations(profile)) {
            handleForm1572Association(registration, association);
        }
    }

    private void handleForm1572Association(AbstractRegistration registration, OrganizationAssociation association) {
        if (registration instanceof AbstractProtocolRegistration) {
            Organization associatedOrganization = association.getOrganizationRole().getOrganization();
            AbstractProtocolRegistration protocolRegistration = (AbstractProtocolRegistration) registration;
            if (protocolRegistration.getForm1572() != null) {
                protocolRegistration.getForm1572().getAssociatedOrganizations(association.getType())
                        .remove(associatedOrganization);
                registrationService.save(protocolRegistration);
            }
        }
    }

    private Set<AbstractRegistration> getUnlockedRegistrations(InvestigatorProfile profile) {
        Set<AbstractRegistration> unlockedRegistrations = Sets.newHashSet();
        for (AbstractRegistration registration : profile.getRegistrations()) {
            if (!registration.isLockedForInvestigator()) {
                unlockedRegistrations.add(registration);
            }
        }
        return unlockedRegistrations;
    }

    @Override
    public void updateAssociationOhrp(OrganizationAssociation association, String ohrp) {
        checkOhrpValid(ohrp);
        checkAssociationNotNull(association);
        checkAssociationValid(association);

        association.getOrganizationRole().setDataField(ohrp);
        save(association.getProfile());
    }

    private void checkOhrpValid(String ohrp) {
        if (StringUtils.isBlank(ohrp)) {
            throw new IllegalArgumentException("OHRP Number must be set!");
        }
    }

    private void checkAssociationNotNull(OrganizationAssociation association) {
        if (association == null) {
            throw new IllegalArgumentException("Null Association Provided!");
        }
    }

    private void checkAssociationValid(OrganizationAssociation association) {
        EnumSet<OrganizationRoleType> ohrpRoles = EnumSet.of(OrganizationRoleType.PRACTICE_SITE);

        OrganizationRoleType associationRole = association.getOrganizationRole().getRoleType();
        if (!ohrpRoles.contains(associationRole)) {
            throw new IllegalArgumentException("The Provided Association does not have an OHRP field!");
        }
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
        deleteSubInvestigatorRegistrations(profile, subInvestigator);
        save(profile);
    }

    private void deleteSubInvestigatorRegistrations(InvestigatorProfile profile, SubInvestigator subInvestigator) {
        List<SubInvestigatorRegistration> subInvestigatorRegistrations = registrationService
                .getSubinvestigatorRegistrations(profile, subInvestigator.getPerson());

        subInvestigatorRegistrations = Lists.newArrayList(Collections2.filter(subInvestigatorRegistrations,
                new Predicate<SubInvestigatorRegistration>() {
                    @Override
                    public boolean apply(SubInvestigatorRegistration input) {
                        return isSubinvestigatorRegistrationRemoveable(input.getPrimaryRegistration());
                    }
                }));

        if (!subInvestigatorRegistrations.isEmpty()) {
            registrationService.removeSubInvestigatorRegistrationAndNotify(subInvestigatorRegistrations);
        }
    }

    private boolean isSubinvestigatorRegistrationRemoveable(InvestigatorRegistration primary) {
        return !primary.isLockedForInvestigator()
                && !INVALID_SUBINVESTIGATOR_REMOVAL_STATES.contains(primary.getStatus());
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
                && StringUtils.isEmpty(shippingDesignee.getOrganization().getNesId())) {
            organizationService.create(shippingDesignee.getOrganization());
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
            FormTypeEnum... formTypesToSetToRevised) throws CredentialAlreadyExistsException, ValidationException {
        if (credential.getIssuer() != null && StringUtils.isEmpty(credential.getIssuer().getNesId())) {
            organizationService.create(credential.getIssuer());
        }
        if (isNew(credential)) {
            profile.addCredential(credential);
        }
        save(profile, formTypesToSetToRevised);
    }

    @Override
    public void saveCertificate(InvestigatorProfile profile, TrainingCertificate cert, File certificateFile,
            FileMetadata fileMetadata) throws CredentialAlreadyExistsException, IOException, ValidationException {
        if (fileMetadata != null) {
            FirebirdFile file = fileService.createFile(certificateFile, fileMetadata);
            cert.setFile(file);
        } else if (cert.getFile() == null) {
            throw new IllegalArgumentException("File information is required to add this certificate!");
        }
        if (!isNew(cert)) {
            registrationService.setRegistrationFormStatusesToRevisedIfReviewed(
                    getAffectedRegistrationsByCertificateModification(profile, cert),
                    FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE);
        }
        saveCredential(profile, cert, FormTypeEnum.CV);
    }

    private Set<AbstractProtocolRegistration> getAffectedRegistrationsByCertificateModification(
            InvestigatorProfile profile, TrainingCertificate certificate) {
        Set<AbstractProtocolRegistration> registrations = Sets.newHashSet();

        for (AbstractProtocolRegistration registration
                : registrationService.getReturnedOrRevisedRegistrations(profile)) {
            HumanResearchCertificateForm hrcForm = registration.getHumanResearchCertificateForm();
            if (hrcForm != null && CollectionUtils.exists(hrcForm.getCertificates(), new EqualPredicate(certificate))) {
                registrations.add(registration);
            }
        }
        return registrations;
    }

    @Override
    public InvestigatorProfile getByPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person was null");
        }
        if (person.getId() == null) {
            return null;
        }
        Query q = getSessionProvider().get().createQuery(PROFILE_BY_PERSON_QUERY);
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
        Query q = getSessionProvider().get().createQuery(SPECIALTIES_BY_BOARD);
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
    public List<InvestigatorProfile> searchForCtepProfiles(String searchTerm) {
        List<InvestigatorProfile> nameOrEmailMatches = getNameOrEmailMatches(searchTerm);
        List<InvestigatorProfile> ctepIdMatches = getCtepIdMatches(searchTerm);
        ArrayList<InvestigatorProfile> profiles = Lists.newArrayList(Iterables
                .concat(nameOrEmailMatches, ctepIdMatches));
        return getOnlyCtepInvestigators(profiles);
    }

    private List<InvestigatorProfile> getNameOrEmailMatches(String searchTerm) {
        if (StringUtils.isNotBlank(searchTerm)) {
            if (isEmailSearch(searchTerm)) {
                return getEmailMatches(searchTerm);
            } else {
                return getNameMatches(searchTerm);
            }
        }
        return Collections.emptyList();
    }

    private boolean isEmailSearch(String searchTerm) {
        return EmailValidator.getInstance().isValid(searchTerm);
    }

    private List<InvestigatorProfile> getEmailMatches(String searchTerm) {
        return search(configureEmailSearch(searchTerm));
    }

    private Person configureEmailSearch(String searchTerm) {
        Person searchPerson = new Person();
        searchPerson.setEmail(searchTerm);
        return searchPerson;
    }

    private List<InvestigatorProfile> getNameMatches(String searchTerm) {
        return search(configureNameSearch(searchTerm));
    }

    private List<InvestigatorProfile> search(Person searchPerson) {
        if (searchPerson == null) {
            return Collections.emptyList();
        }
        SearchCriteria<Person> criteria = createPersonSearchCriteria(searchPerson);
        List<Person> persons = personService.search(criteria);
        return getProfiles(persons);
    }

    private SearchCriteria<Person> createPersonSearchCriteria(Person searchPerson) {
        return new FirebirdAnnotatedBeanSearchCriteria<Person>(searchPerson, getSessionProvider());
    }

    @SuppressWarnings("unchecked")
    // hibernate does not provide typed arguments
    private List<InvestigatorProfile> getProfiles(List<Person> persons) {
        if (persons.isEmpty()) {
            return Collections.emptyList();
        } else {
            Query query = getSessionProvider().get().createQuery(PROFILES_BY_PERSON_HQL);
            query.setParameterList(PERSON_LIST_PARAMETER_NAME, persons);
            return query.list();
        }
    }

    private Person configureNameSearch(String searchTerm) {
        Person searchPerson = new Person();
        String[] names = searchTerm.split(",");
        String firstName = "";
        String lastName = "";
        if (names.length > 0) {
            lastName = names[0].trim();
        }
        if (names.length > 1) {
            firstName = names[1].trim();
        }
        searchPerson.setLastName(lastName);
        searchPerson.setFirstName(firstName);
        return searchPerson;
    }

    private List<InvestigatorProfile> getCtepIdMatches(String searchTerm) {
        return search(configureCtepIdSearch(searchTerm));
    }

    private Person configureCtepIdSearch(String searchTerm) {
        Person searchPerson = new Person();
        searchPerson.setCtepId(searchTerm);
        return searchPerson;
    }

    private List<InvestigatorProfile> getOnlyCtepInvestigators(List<InvestigatorProfile> profiles) {
        return Lists.newArrayList(Iterables.filter(profiles, new Predicate<InvestigatorProfile>() {
            @Override
            public boolean apply(InvestigatorProfile profile) {
                return BooleanUtils.toBoolean(profile.getUser().isCtepUser());
            }
        }));
    }

    @Override
    public List<InvestigatorProfile> search(String searchTerm) {
        return getNameOrEmailMatches(searchTerm);
    }

    @Override
    public Long save(InvestigatorProfile profile, FormTypeEnum... formTypesToSetToRevised) {
        registrationService.setReturnedOrRevisedRegistrationsFormStatusesToRevised(profile, formTypesToSetToRevised);
        return save(profile);
    }

    @Override
    public void deleteCertificate(InvestigatorProfile profile, TrainingCertificate certificate) {
        registrationService.setRegistrationFormStatusesToRevisedIfReviewed(
                getAffectedRegistrationsByCertificateModification(profile, certificate),
                FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE);
        breakCertificateLinks(profile, certificate);
        profile.getCredentials().remove(certificate);
        if (profile.isOrphan(certificate.getFile())) {
            getSessionProvider().get().delete(certificate.getFile());
        }
        save(profile, FormTypeEnum.CV);
    }

    private void breakCertificateLinks(InvestigatorProfile profile, TrainingCertificate certificate) {
        certificate.unLinkSubmittedCertificates();
        for (AbstractProtocolRegistration registration : profile.getCurrentProtocolRegistrations()) {
            if (registration.getHumanResearchCertificateForm() != null) {
                registration.getHumanResearchCertificateForm().deselectCertificate(certificate);
            }
        }
    }

    @Inject
    void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Inject
    void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Inject
    void setOrganizationAssociationService(OrganizationAssociationService organizationAssociationService) {
        this.organizationAssociationService = organizationAssociationService;
    }

    @Inject
    void setPersonAssociationService(PersonAssociationService personAssociationService) {
        this.personAssociationService = personAssociationService;
    }

    @Inject
    void setNesPersonRoleService(NesPersonRoleIntegrationService nesPersonRoleService) {
        this.nesPersonRoleService = nesPersonRoleService;
    }

    @Inject
    void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Inject
    void setRegistrationService(ProtocolRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

}