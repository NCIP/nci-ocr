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
package gov.nih.nci.firebird.data.user;

import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Keystore;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.service.user.UserCnUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.fiveamsolutions.nci.commons.search.Searchable;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Provides application information about a user of the system.
 */
@Entity(name = "firebird_user")
@SuppressWarnings("PMD.TooManyMethods")
// Mainly getters and setters
public class FirebirdUser implements PersistentObject {

    private static final long serialVersionUID = 1L;

    private static final int USERNAME_LENGTH = 100;

    private Long id;
    private String username;
    private Person person;
    private InvestigatorRole investigatorRole;
    private Set<SponsorRole> sponsorRoles = new HashSet<SponsorRole>();
    private Boolean eulaAccepted;
    private Keystore keystore;
    private RegistrationCoordinatorRole registrationCoordinatorRole;
    private boolean ctepUser;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Searchable
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id the user's id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the fully distinguished username
     */
    @Column(updatable = false, unique = true)
    @Length(max = USERNAME_LENGTH)
    @NotEmpty
    @Searchable(matchMode = Searchable.MATCH_MODE_CONTAINS)
    public String getUsername() {
        return username;
    }

    /**
     * @param username the fully qualified username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the person
     */
    @OneToOne
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @JoinColumn(name = "person_id")
    @ForeignKey(name = "firebird_user_person_fkey")
    @Valid
    @NotNull
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
     * @return the user's investigator role or null if not an investigator.
     */
    @Embedded
    public InvestigatorRole getInvestigatorRole() {
        return investigatorRole;
    }

    private void setInvestigatorRole(InvestigatorRole investigatorRole) {
        this.investigatorRole = investigatorRole;
    }

    /**
     * @return the registrationCoordinatorRole
     */
    @OneToOne(mappedBy = "user")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @Valid
    public RegistrationCoordinatorRole getRegistrationCoordinatorRole() {
        return registrationCoordinatorRole;
    }

    void setRegistrationCoordinatorRole(RegistrationCoordinatorRole registrationCoordinatorRole) {
        this.registrationCoordinatorRole = registrationCoordinatorRole;
    }

    /**
     * @return the sponsorRoles
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Set<SponsorRole> getSponsorRoles() {
        return sponsorRoles;
    }

    @SuppressWarnings("ucd")
    // setter required by hibernate
    void setSponsorRoles(Set<SponsorRole> sponsorRoles) {
        this.sponsorRoles = sponsorRoles;
    }

    /**
     * Sets up the user as an investigator.
     *
     * @param profile the investigator's profile.
     */
    public void createInvestigatorRole(InvestigatorProfile profile) {
        setInvestigatorRole(new InvestigatorRole(this, profile));
    }

    /**
     * Sets up the user as an registration coordinator.
     */
    public void createRegistrationCoordinatorRole() {
        setRegistrationCoordinatorRole(new RegistrationCoordinatorRole(this));
    }

    /**
     * Remove the Registration Coordinator Role from the user, useful if adjusting FIREBIRD roles.
     */
    public void removeRegistrationCoordinatorRole() {
        setRegistrationCoordinatorRole(null);
    }

    /**
     * Adds a new sponsor representative role.
     *
     * @param sponsor the sponsor organization
     * @return added role.
     */
    public SponsorRole addSponsorRepresentativeRole(Organization sponsor) {
        SponsorRole role = new SponsorRole(this, sponsor);
        getSponsorRoles().add(role);
        return role;
    }

    /**
     * @return all sponsor organizations the user has a role as a representative or delegate.
     */
    @Transient
    public List<Organization> getSponsorOrganizations() {
        return getSponsorOrganizations(null);
    }

    /**
     * @param groupNames the current group names for all Grid Grouper groups for this user
     * @return all sponsor organizations the user is verfied for either as a representative or delegate.
     */
    public List<Organization> getVerifiedSponsorOrganizations(Set<String> groupNames) {
        return getVerifiedSponsorOrganizations(groupNames, getSponsorOrganizations());
    }

    private List<Organization> getVerifiedSponsorOrganizations(final Set<String> groupNames,
            List<Organization> sponsorOrganizations) {
        return Lists.newArrayList(Iterables.filter(sponsorOrganizations, new Predicate<Organization>() {
            @Override
            public boolean apply(Organization sponsorOrganization) {
                return hasVerifiedSponsorRole(sponsorOrganization, groupNames);
            }
        }));
    }

    /**
     * @param groupNames the current group names for all Grid Grouper groups for this user
     * @return all sponsor organizations the user is verified for as a representative.
     */
    public List<Organization> getVerifiedSponsorRepresentativeOrganizations(Set<String> groupNames) {
        return getVerifiedSponsorOrganizations(groupNames, getSponsorRepresentativeOrganizations());
    }

    private List<Organization> getSponsorOrganizations(Boolean isDelegate) {
        List<Organization> sponsors = new ArrayList<Organization>();
        for (SponsorRole role : getSponsorRoles()) {
            if (isDelegate == null || isDelegate == role.isDelegate()) {
                sponsors.add(role.getSponsor());
            }
        }

        Collections.sort(sponsors);
        return sponsors;
    }

    /**
     * @return all sponsor organizations the user is authorized for as a representative.
     */
    @Transient
    public List<Organization> getSponsorRepresentativeOrganizations() {
        return getSponsorOrganizations(false);
    }

    /**
     * @return all sponsor organizations the user is authorized for either as a delegate.
     */
    @Transient
    public List<Organization> getSponsorDelegateOrganizations() {
        return getSponsorOrganizations(true);
    }

    /**
     * Adds a new sponsor delegate role.
     *
     * @param sponsor the sponsor organization
     * @return the new sponsor delegate role
     */
    public SponsorRole addSponsorDelegateRole(Organization sponsor) {
        SponsorRole role = new SponsorRole(this, sponsor, true);
        getSponsorRoles().add(role);
        return role;
    }

    /**
     * @return the acceptedEULA
     */
    @Column(name = "accepted_eula")
    @SuppressWarnings("ucd")
    // required by hibernate to create column
    public Boolean isEulaAccepted() {
        return eulaAccepted;
    }

    /**
     * @param eulaAccepted the acceptedEULA to set
     */
    public void setEulaAccepted(Boolean eulaAccepted) {
        this.eulaAccepted = eulaAccepted;
    }

    /**
     * @return signing key-store
     */
    @Embedded
    public Keystore getKeystore() {
        return keystore;
    }

    /**
     * @param keystore signing key-store.
     */
    public void setKeystore(Keystore keystore) {
        this.keystore = keystore;
    }

    /**
     * @return the user basename, i.e. the final CN element of the fully qualified username.
     */
    @Transient
    public String getBaseUsername() {
        Entry<String, String>[] parsedCn = UserCnUtils.parseCn(getUsername());
        return UserCnUtils.getFirstValue("CN", parsedCn);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FirebirdUser)) {
            return false;
        }
        FirebirdUser user = (FirebirdUser) obj;

        return new EqualsBuilder().append(getUsername(), user.getUsername()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getUsername()).toHashCode();
    }

    /**
     * @return whether or not this user is an investigator
     */
    @Transient
    public boolean isInvestigator() {
        return getInvestigatorRole() != null;
    }

    /**
     * @return whether or not this user is a sponsor representative
     */
    @Transient
    public boolean isSponsorRepresentative() {
        return !getSponsorRepresentativeOrganizations().isEmpty();
    }

    /**
     * @param sponsorOrganization organization to check
     * @return whether or not this user is a sponsor representative for the given organization
     */
    @Transient
    public boolean isSponsorRepresentative(Organization sponsorOrganization) {
        return getSponsorRepresentativeOrganizations().contains(sponsorOrganization);
    }

    /**
     * @param sponsorOrganization organization to check
     * @return whether or not this user is a sponsor delegate for the given organization
     */
    @Transient
    public boolean isSponsorDelegate(Organization sponsorOrganization) {
        return getSponsorDelegateOrganizations().contains(sponsorOrganization);
    }

    /**
     * @return whether or not this user is a sponsor delegate
     */
    @Transient
    public boolean isSponsorDelegate() {
        return !getSponsorDelegateOrganizations().isEmpty();
    }

    /**
     * @return whether or not this user is a registration coordinator
     */
    @Transient
    public boolean isRegistrationCoordinator() {
        return getRegistrationCoordinatorRole() != null;
    }

    /**
     * Determines whether this user has a verified role for the given sponsor.
     *
     * @param sponsor check for verification of role for this sponsor
     * @param groupNames the current Grid Grouper group names that the user belongs to
     * @return true if a verified sponsor representative or sponsor delegate for the sponsor organization.
     */
    @Transient
    public boolean hasVerifiedSponsorRole(Organization sponsor, Set<String> groupNames) {
        for (SponsorRole role : getSponsorRoles()) {
            if (sponsor.equals(role.getSponsor()) && groupNames.contains(role.getVerifiedSponsorGroupName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return An EnumSet of the UserRoleTypes that this user represents (unverified).
     */
    @Transient
    public EnumSet<UserRoleType> getRoles() {
        EnumSet<UserRoleType> roles = EnumSet.noneOf(UserRoleType.class);
        if (isInvestigator()) {
            roles.add(UserRoleType.INVESTIGATOR);
        }
        if (isRegistrationCoordinator()) {
            roles.add(UserRoleType.REGISTRATION_COORDINATOR);
        }
        if (isSponsorRepresentative()) {
            roles.add(UserRoleType.SPONSOR);
        }
        if (isSponsorDelegate()) {
            roles.add(UserRoleType.SPONSOR_DELEGATE);
        }
        return roles;
    }

    /**
     * @return all profiles for which this user is an investigator or active registration coordinator.
     */
    @Transient
    public Set<InvestigatorProfile> getActiveProfiles() {
        Set<InvestigatorProfile> profiles = Sets.newHashSet();
        if (isInvestigator()) {
            profiles.add(getInvestigatorRole().getProfile());
        }
        if (isRegistrationCoordinator()) {
            profiles.addAll(getRegistrationCoordinatorRole().getApprovedManagedProfiles());
        }
        return profiles;
    }

    /**
     * @return Boolean flag indicating the user is a CTEP User
     */
    @Column(name = "ctep_user")
    public boolean isCtepUser() {
        return ctepUser;
    }

    /**
     * @param ctepUser Boolean flag indicating the user is a CTEP User
     */
    public void setCtepUser(boolean ctepUser) {
        this.ctepUser = ctepUser;
    }

    /**
     * @param registration registration
     * @return whether or not this user can manage the passed in registration
     */
    public boolean canManageRegistration(InvestigatorRegistration registration) {
        return isInvestigatorForRegistration(registration) || isApprovedCoordinatorForRegistration(registration);
    }

    private boolean isInvestigatorForRegistration(InvestigatorRegistration registration) {
        return isInvestigator() && getInvestigatorRole().getProfile().equals(registration.getProfile());
    }

    private boolean isApprovedCoordinatorForRegistration(InvestigatorRegistration registration) {
        return isRegistrationCoordinator()
                && getRegistrationCoordinatorRole().isApprovedToManageInvestigatorRegistrations(
                        registration.getProfile());
    }

}
