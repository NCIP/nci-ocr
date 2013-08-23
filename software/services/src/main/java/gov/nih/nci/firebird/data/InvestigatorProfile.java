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

import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.InvestigatorStatus;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.fiveamsolutions.nci.commons.audit.Auditable;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

/**
 * Class that represents an investigator's profile.
 */
@SuppressWarnings({ "unused", "PMD.TooManyMethods", "PMD.ExcessiveClassLength" })
// private get/setters required by Hibernate & many private support methods required
@Entity(name = "investigator_profile")
public class InvestigatorProfile implements Auditable {

    private static final long serialVersionUID = 1L;

    private static final String PROFILE_COLUMN = "profile";
    /**
     * compare by profile's person name.
     *
     * @see Person#NAME_COMPARATOR
     */
    public static final Comparator<InvestigatorProfile> INVESTIGATOR_NAME_COMPARATOR = new InvestigatorNameComparator();

    private Long id;
    private Person person;
    private PrimaryOrganization primaryOrganization;
    private Set<OrganizationAssociation> organizationAssociations = Sets.newHashSet();
    private Set<FirebirdFile> uploadedFiles = Sets.newHashSet();
    private Set<AbstractCredential<?>> credentials = Sets.newHashSet();
    private Set<AbstractRegistration> registrations = Sets.newHashSet();
    private ClinicalResearchExperience clinicalResearchExperience = new ClinicalResearchExperience();
    private Set<ManagedInvestigator> registrationCoordinatorMappings = new HashSet<ManagedInvestigator>();
    private FirebirdUser user;
    private Set<SubInvestigator> subInvestigators = Sets.newHashSet();
    private Set<OrderingDesignee> orderingDesignees = Sets.newHashSet();
    private ShippingDesignee shippingDesignee;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id db id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the person
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "person_id")
    @ForeignKey(name = "investigatorprofile_person_fkey")
    @Valid
    public Person getPerson() {
        return person;
    }

    /**
     * @param person the person to set
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * @return the organization
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "primary_organization_id")
    @ForeignKey(name = "investigatorprofile_primary_organization_fkey")
    @Valid
    public PrimaryOrganization getPrimaryOrganization() {
        return primaryOrganization;
    }

    /**
     * @param primaryOrganization the primary organization to set
     */
    public void setPrimaryOrganization(PrimaryOrganization primaryOrganization) {
        this.primaryOrganization = primaryOrganization;
    }

    /**
     * @return the uploaded files.
     */
    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "investigatorprofile_file",
        joinColumns = @JoinColumn(name = "investigatorprofile"), inverseJoinColumns = @JoinColumn(name = "file"))
    @ForeignKey(name = "investigatorprofile_fkey", inverseName = "file_fkey")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    public Set<FirebirdFile> getUploadedFiles() {
        return uploadedFiles;
    }

    /**
     * @param uploadedFiles uploaded files.
     */
    public void setUploadedFiles(Set<FirebirdFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    /**
     * @return the set of organization associations.
     */
    @OneToMany(mappedBy = PROFILE_COLUMN, fetch = FetchType.LAZY)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public Set<OrganizationAssociation> getOrganizationAssociations() {
        return organizationAssociations;
    }

    /**
     * @param organizationAssociations the organizationAssociations to set
     */
    private void setOrganizationAssociations(Set<OrganizationAssociation> organizationAssociations) {
        this.organizationAssociations = organizationAssociations;
    }

    /**
     * Associates the profile with an organization.
     *
     * @param associatedOrganization the organization to associate
     * @param type the association type
     * @return the new association object.
     * @throws AssociationAlreadyExistsException if this profile already has such an association.
     */
    public OrganizationAssociation addOrganizationAssociation(Organization associatedOrganization,
            OrganizationRoleType type) throws AssociationAlreadyExistsException {
        AbstractOrganizationRole role = associatedOrganization.getRole(type);
        if (role == null) {
            role = associatedOrganization.createRole(type);
        }
        OrganizationAssociation organizationAssociation = new OrganizationAssociation(this, role);
        boolean added = getOrganizationAssociations().add(organizationAssociation);
        if (!added) {
            throw new AssociationAlreadyExistsException();
        }
        return organizationAssociation;
    }

    /**
     * Returns the <code>OrganizationAssociations</code> that correspond to the requested type.
     *
     * @param type retrieve associations of this type
     * @return the matching associations.
     */
    public Set<OrganizationAssociation> getOrganizationAssociations(final OrganizationRoleType type) {
        return Sets.filter(getOrganizationAssociations(), new Predicate<OrganizationAssociation>() {
            @Override
            public boolean apply(OrganizationAssociation organizationAssociation) {
                return type.equals(organizationAssociation.getType());
            }
        });

    }

    /**
     * @return this profiles sub investigators
     */
    @OneToMany(mappedBy = PROFILE_COLUMN, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SubInvestigator> getSubInvestigatorsInternal() {
        return subInvestigators;
    }

    private void setSubInvestigatorsInternal(Set<SubInvestigator> subInvestigatorsInternal) {
        this.subInvestigators = subInvestigatorsInternal;
    }

    /**
     * @return this profiles sub investigators
     */
    @Transient
    public Set<SubInvestigator> getSubInvestigators() {
        return Collections.unmodifiableSet(subInvestigators);
    }

    /**
     * @return this profiles ordering designees
     */
    @OneToMany(mappedBy = PROFILE_COLUMN, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderingDesignee> getOrderingDesigneesInternal() {
        return orderingDesignees;
    }

    private void setOrderingDesigneesInternal(Set<OrderingDesignee> orderingDesigneesInternal) {
        this.orderingDesignees = orderingDesigneesInternal;
    }

    /**
     * @return an unmodifiable set of this profile's ordering designees
     */
    @Transient
    public Set<OrderingDesignee> getOrderingDesignees() {
        return Collections.unmodifiableSet(orderingDesignees);
    }

    /**
     * @return the shippingDesignee
     */
    @OneToOne(mappedBy = PROFILE_COLUMN)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public ShippingDesignee getShippingDesignee() {
        return shippingDesignee;
    }

    /**
     * @param shippingDesignee the shippingDesignee to set
     */
    public void setShippingDesignee(ShippingDesignee shippingDesignee) {
        this.shippingDesignee = shippingDesignee;
    }

    /**
     * @return the registrationCoordinators
     */
    @OneToMany(mappedBy = "investigatorProfile", cascade = { CascadeType.REFRESH, CascadeType.REMOVE })
    public Set<ManagedInvestigator> getRegistrationCoordinatorMappings() {
        return registrationCoordinatorMappings;
    }

    private void setRegistrationCoordinatorMappings(Set<ManagedInvestigator> registrationCoordinatorMappings) {
        this.registrationCoordinatorMappings = registrationCoordinatorMappings;
    }

    /**
     * AAssociates the person to this profile as a sub investigator.
     *
     * @param aPerson the Person to add as a sub investigator
     * @return the new SubInvestigator object.
     * @throws AssociationAlreadyExistsException if this profile already has this sub investigator.
     */
    public SubInvestigator addSubInvestigator(Person aPerson) throws AssociationAlreadyExistsException {
        SubInvestigator subInvestigator = new SubInvestigator(this, aPerson);
        boolean added = subInvestigators.add(subInvestigator);
        if (!added) {
            throw new AssociationAlreadyExistsException();
        }
        return subInvestigator;
    }

    /**
     * Removes the sub investigator from the profile.
     *
     * @param subInvestigator Sub investigator to remove
     * @return Whether or not removal was successful
     */
    public boolean removeSubInvestigator(SubInvestigator subInvestigator) {
        return subInvestigators.remove(subInvestigator);
    }

    /**
     * Associates the person to this profile as an ordering designee.
     *
     * @param aPerson the Person to add as a ordering designee
     * @return the new OrderingDesignee object.
     * @throws AssociationAlreadyExistsException if this profile already has this ordering designee.
     */
    public OrderingDesignee addOrderingDesignee(Person aPerson) throws AssociationAlreadyExistsException {
        OrderingDesignee orderingDesignee = new OrderingDesignee(this, aPerson);
        boolean added = orderingDesignees.add(orderingDesignee);
        if (!added) {
            throw new AssociationAlreadyExistsException();
        }
        return orderingDesignee;
    }

    /**
     * Removes the ordering designee from the profile.
     *
     * @param orderingDesignee drdering designee to remove
     * @return Whether or not removal was successful
     */
    public boolean removeOrderingDesignee(OrderingDesignee orderingDesignee) {
        return orderingDesignees.remove(orderingDesignee);
    }

    /**
     * @param credential credential to add.
     * @throws CredentialAlreadyExistsException if this profile already has such an credential..
     */
    public void addCredential(AbstractCredential<?> credential) throws CredentialAlreadyExistsException {
        credential.setProfile(this);
        checkAlreadyExists(credential);
        getCredentials().add(credential);
    }

    void checkAlreadyExists(AbstractCredential<?> credentialToCheck) throws CredentialAlreadyExistsException {
        for (AbstractCredential<?> credential : getCredentials(credentialToCheck.getType())) {
            if (credentialToCheck != credential && credential.isDuplicateCredential(credentialToCheck)) {
                throw new CredentialAlreadyExistsException();
            }
        }
    }

    /**
     * @return all credentials associated to this profile.
     */
    @OneToMany(mappedBy = PROFILE_COLUMN, fetch = FetchType.LAZY)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public Set<AbstractCredential<?>> getCredentials() {
        return credentials;
    }

    private void setCredentials(Set<AbstractCredential<?>> credentials) {
        this.credentials = credentials;
    }

    /**
     * get credentials of the specified type.
     *
     * @param type type to match.
     * @return credentials matching the specified type.
     */
    public SortedSet<AbstractCredential<?>> getCredentials(final CredentialType type) {
        return getSortedSet(Sets.filter(getCredentials(), new Predicate<AbstractCredential<?>>() {
            @Override
            public boolean apply(AbstractCredential<?> credential) {
                return type.equals(credential.getType());
            }
        }));
    }

    private static SortedSet<AbstractCredential<?>> getSortedSet(Set<AbstractCredential<?>> credentials) {
        SortedSet<AbstractCredential<?>> sortedCredentials = new TreeSet<AbstractCredential<?>>();
        sortedCredentials.addAll(credentials);
        return sortedCredentials;
    }

    /**
     * Get all credentials of the specified type that are not expired.
     *
     * @param type type to match.
     * @return credentials matching the specified type.
     */
    public SortedSet<AbstractCredential<?>> getCurrentCredentials(final CredentialType type) {
        return getSortedSet(Sets.filter(getCredentials(type), new Predicate<AbstractCredential<?>>() {
            @Override
            public boolean apply(AbstractCredential<?> credential) {
                return !credential.isExpired();
            }
        }));
    }

    /**
     * get credentials of the specified type.
     *
     * @param <C> Credential
     * @param type type to match.
     * @return credentials matching the specified type.
     */
    @SuppressWarnings("unchecked")
    // this is guaranteed to be a safe cast
    public <C extends AbstractCredential<?>> SortedSet<C> getCredentials(final Class<C> type) {
        return (SortedSet<C>) getSortedSet(Sets.filter(getCredentials(), new Predicate<AbstractCredential<?>>() {
            @Override
            public boolean apply(AbstractCredential<?> credential) {
                return type.isAssignableFrom(credential.getClass());
            }
        }));
    }

    /**
     * Lookup a Credential from this profile given the ID.
     *
     * @param credentialId the ID of the credential
     * @return the matching credential or null if it is not found.
     */
    public AbstractCredential<?> getCredential(final Long credentialId) {
        if (credentialId == null) {
            throw new IllegalArgumentException("Credential ID cannot be Null!");
        }
        for (AbstractCredential<?> credential : getCredentials()) {
            if (credential.getId().equals(credentialId)) {
                return credential;
            }
        }
        throw new IllegalArgumentException("Credential ID " + credentialId + " was not found!");
    }

    /**
     * @return all of this user's human research certificates
     */
    @Transient
    public List<TrainingCertificate> getHumanResearchCertificates() {
        List<TrainingCertificate> certificates = Lists.newArrayList();
        for (AbstractCredential<?> credential : getCredentials(CredentialType.CERTIFICATE)) {
            TrainingCertificate certificate = (TrainingCertificate) credential;
            if (certificate.getCertificateType() == CertificateType.HUMAN_RESEARCH_CERTIFICATE) {
                certificates.add(certificate);
            }
        }
        return certificates;
    }

    @OneToMany(mappedBy = PROFILE_COLUMN, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    private Set<AbstractRegistration> getRegistrationsInternal() {
        return registrations;
    }

    private void setRegistrationsInternal(Set<AbstractRegistration> investigatorRegistrationsInternal) {
        this.registrations = investigatorRegistrationsInternal;
    }

    /**
     * @param registration the registration to add to this profile.
     */
    public void addRegistration(AbstractRegistration registration) {
        registration.setProfile(this);
        getRegistrationsInternal().add(registration);
    }

    /**
     * @param registration the registration to add to this profile.
     * @return true if the registration was found and removed.
     */
    boolean removeRegistration(AbstractRegistration registration) {
        registration.setProfile(null);
        return getRegistrationsInternal().remove(registration);
    }

    /**
     * @return all registrations.
     */
    @Transient
    public Set<AbstractRegistration> getRegistrations() {
        return Collections.unmodifiableSet(getRegistrationsInternal());
    }

    /**
     * @return current (not revised) protocol registrations only (investigator and subinvestigator).
     */
    @Transient
    public Set<AbstractProtocolRegistration> getCurrentProtocolRegistrations() {
        Set<AbstractProtocolRegistration> protocolRegistrations = Sets.newHashSet();
        for (AbstractProtocolRegistration registration : getRegistrations(AbstractProtocolRegistration.class)) {
            if (shouldIncludeProtocolRegistration(registration)) {
                protocolRegistrations.add(registration);
            }
        }
        return protocolRegistrations;
    }

    private boolean shouldIncludeProtocolRegistration(AbstractProtocolRegistration registration) {
        return !isRevised(registration) && !isRemovedSubinvestigator(registration);
    }

    private boolean isRevised(AbstractProtocolRegistration registration) {
        return registration instanceof RevisedInvestigatorRegistration;
    }

    private boolean isRemovedSubinvestigator(AbstractProtocolRegistration registration) {
        if (registration instanceof SubInvestigatorRegistration) {
            return ((SubInvestigatorRegistration) registration).getPrimaryRegistration() == null;
        } else {
            return false;
        }
    }

    /**
     * @return all (including revised) protocol registrations only (investigator and subinvestigator).
     */
    @Transient
    public Set<AbstractProtocolRegistration> getAllProtocolRegistrations() {
        Set<AbstractProtocolRegistration> protocolRegistrations = Sets.newHashSet();
        for (AbstractProtocolRegistration registration : getRegistrations(AbstractProtocolRegistration.class)) {
            if (!isRemovedSubinvestigator(registration)) {
                protocolRegistrations.add(registration);
            }
        }
        return protocolRegistrations;
    }

    @Transient
    private <T extends AbstractRegistration> Set<T> getRegistrations(Class<T> registrationType) {
        return Sets.newHashSet(Iterables.filter(getRegistrations(), registrationType));
    }

    /**
     * @return the current annual registration if one exists, otherwise null
     */
    @Transient
    public AnnualRegistration getCurrentAnnualRegistration() {
        return Iterables.getLast(getSortedRegistrationsAscendingByCreationOrder(), null);
    }

    @Transient
    private List<AnnualRegistration> getSortedRegistrationsAscendingByCreationOrder() {
        return Ordering.natural().onResultOf(new Function<AnnualRegistration, Long>() {
            @Override
            public Long apply(AnnualRegistration registration) {
                return registration.getId();
            }
        }).sortedCopy(getAnnualRegistrations());
    }

    /**
     * @return annual registrations only.
     */
    @Transient
    public Set<AnnualRegistration> getAnnualRegistrations() {
        return getRegistrations(AnnualRegistration.class);
    }

    /**
     * @return annual registrations awaiting renewal.
     */
    @Transient
    public Set<AnnualRegistration> getAnnualRegistrationsAwaitingRenewal() {
        return Sets.filter(getAnnualRegistrations(), new Predicate<AnnualRegistration>() {
            @Override
            public boolean apply(AnnualRegistration registration) {
                return registration.isPendingRenewal();
            }
        });
    }

    /**
     * @return Clinical Research Experience holder.
     */
    @Embedded
    public ClinicalResearchExperience getClinicalResearchExperience() {
        return clinicalResearchExperience;
    }

    /**
     * @param clinicalResearchExperience Clinical Research Experience holder.
     */
    public void setClinicalResearchExperience(ClinicalResearchExperience clinicalResearchExperience) {
        this.clinicalResearchExperience = clinicalResearchExperience;
    }

    /**
     * comparator for a profile.investigator by last name, then first name.
     */
    private static final class InvestigatorNameComparator implements Comparator<InvestigatorProfile> {
        @Override
        public int compare(InvestigatorProfile p1, InvestigatorProfile p2) {
            return Person.NAME_COMPARATOR.compare(p1.getPerson(), p2.getPerson());
        }
    }

    /**
     * @return all persons referenced in this profile.
     */
    @Transient
    public List<Person> getAllPersons() {
        List<Person> persons = Lists.newArrayList();
        persons.add(getPerson());
        persons.addAll(getAssociationPersons(subInvestigators));
        persons.addAll(getAssociationPersons(orderingDesignees));
        if (getShippingDesignee() != null) {
            persons.add(getShippingDesignee().getPerson());
        }
        return persons;
    }

    @Transient
    private <T extends AbstractPersonAssociation> Collection<Person> getAssociationPersons(Collection<T> associations) {
        return Collections2.transform(associations, new Function<T, Person>() {
            public Person apply(T association) {
                return association.getPerson();
            }
        });
    }

    /**
     * @return all organizations associated to this profile.
     */
    @Transient
    public List<Organization> getAllOrganizations() {
        List<Organization> organizations = Lists.newArrayList();
        organizations.add(getPrimaryOrganization().getOrganization());
        organizations.addAll(getAssociatedOrganizations());
        organizations.addAll(getCredentialIssuers());
        return organizations;
    }

    @Transient
    private Collection<Organization> getAssociatedOrganizations() {
        return Collections2.transform(getOrganizationAssociations(),
                new Function<OrganizationAssociation, Organization>() {
                    @Override
                    public Organization apply(OrganizationAssociation association) {
                        return association.getOrganizationRole().getOrganization();
                    }
                });
    }

    @Transient
    private Collection<Organization> getCredentialIssuers() {
        Set<Organization> issuers = Sets.newHashSet();
        for (AbstractCredential<?> credential : getCredentials()) {
            if (credential.getIssuer() != null) {
                issuers.add(credential.getIssuer());
            }
        }
        return issuers;
    }

    /**
     * @return user
     */
    @OneToOne(mappedBy = "investigatorRole.profile")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public FirebirdUser getUser() {
        return user;
    }

    /**
     * @param user user to set
     */
    public void setUser(FirebirdUser user) {
        this.user = user;
    }

    /**
     * @return whether or not this profile has a CTEP registration coordinator
     */
    public boolean hasCtepRegistrationCoordinator() {
        return getCtepRegistrationCoordinatorMapping() != null;
    }

    /**
     * @return this profiles CTEP registration coordinator if one exists, otherwise null
     */
    @Transient
    public ManagedInvestigator getCtepRegistrationCoordinatorMapping() {
        return Iterables.find(getRegistrationCoordinatorMappings(), new Predicate<ManagedInvestigator>() {
            @Override
            public boolean apply(ManagedInvestigator mapping) {
                return mapping.isCtepAssociate();
            }
        }, null);
    }

    /**
     * Indicates if a file has no references in this profile.
     *
     * @param file the FirebirdFile to check
     * @return true if no references exist, false otherwise
     */
    public boolean isOrphan(FirebirdFile file) {
        if (isFileInProfile(file)) {
            return false;
        }
        for (AbstractRegistration registration : this.getRegistrations()) {
            if (registration.isFileReferenced(file)) {
                return false;
            }
        }
        return true;
    }

    private boolean isFileInProfile(FirebirdFile file) {
        return getUploadedFiles().contains(file);
    }

    /**
     * Checks if this profile contains an organization association of a specific type.
     *
     * @param organization check for this organization
     * @param type the type of association role to check for
     * @return true if referenced
     */
    boolean containsAssociation(final Organization organization, OrganizationRoleType type) {
        Set<OrganizationAssociation> associations = getOrganizationAssociations(type);
        return Iterables.any(associations, new Predicate<OrganizationAssociation>() {
            @Override
            public boolean apply(OrganizationAssociation association) {
                return association.getOrganizationRole().getOrganization().equals(organization);
            }
        });
    }

    /**
     * @return whether or not this profile can create an annual registration
     */
    public boolean canCreateAnnualRegistration() {
        return getUser().isCtepUser() && !isInvestigatorsStatusWithdrawn()
                && (getAnnualRegistrations().isEmpty() || hasRenewalRegistrationBeenDeleted());
    }

    @Transient
    private boolean isInvestigatorsStatusWithdrawn() {
        return getUser().getInvestigatorRole().getStatus() == InvestigatorStatus.WITHDRAWN;
    }

    private boolean hasRenewalRegistrationBeenDeleted() {
        return getCurrentAnnualRegistration().isRenewed() && getCurrentAnnualRegistration().getRenewal() == null;
    }

}