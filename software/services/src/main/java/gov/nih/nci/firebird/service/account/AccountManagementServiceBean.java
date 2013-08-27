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
package gov.nih.nci.firebird.service.account;

import static gov.nih.nci.firebird.data.user.ManagedInvestigator.PARTIAL_SUSPENSION_STATES;
import static gov.nih.nci.firebird.data.user.ManagedInvestigator.SUSPENSION_STATES;
import static gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus.APPROVED;
import static gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus.SUSPENDED;
import static gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus.SUSPENDED_FROM_PROFILE;
import static gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus.SUSPENDED_FROM_REGISTRATIONS;
import gov.nih.nci.firebird.cagrid.GridGrouperService;
import gov.nih.nci.firebird.cagrid.GridInvocationException;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus;
import gov.nih.nci.firebird.data.user.SponsorRole;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.user.FirebirdUserService;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.hibernate.Session;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * Account management implementation.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.TooManyStaticImports" })
// Static imports for status enum values.
// contains many private helper methods and dependency setters
public class AccountManagementServiceBean implements AccountManagementService {


    private Provider<Session> sessionProvider;
    private EmailService emailService;
    private TemplateService templateService;
    private FirebirdUserService userService;
    private GridGrouperService gridGrouperService;
    private String supportEmailAddress;
    private SponsorService sponsorService;
    private Provider<AccountConfigurationHelper> configurationHelperProvider;

    @Inject
    void setSessionProvider(Provider<Session> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Inject
    void setEmailService(@Named("jmsEmailService")
    EmailService emailService) {
        this.emailService = emailService;
    }

    @Inject
    void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Inject
    void setUserService(FirebirdUserService userService) {
        this.userService = userService;
    }

    @Inject
    void setGridGrouperService(GridGrouperService gridGrouperService) {
        this.gridGrouperService = gridGrouperService;
    }

    @Inject
    void setSponsorService(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @Inject
    void setSupportEmailAddress(@Named("firebird.email.support.address") String supportEmailAddress) {
        this.supportEmailAddress = supportEmailAddress;
    }

    @Inject
    void setConfigurationHelperProvider(Provider<AccountConfigurationHelper> configurationHelperProvider) {
        this.configurationHelperProvider = configurationHelperProvider;
    }

    @Override
    public AccountConfigurationData createConfigurationForNewUser(UserSessionInformation userSessionInformation) {
        AccountConfigurationData data = new AccountConfigurationData();
        data.setUsername(userSessionInformation.getFullyQualifiedUsername());
        Set<String> groupNames = userSessionInformation.getGroupNames();
        handleInvestigatorRole(data, groupNames);
        handleRegistrationCoordinatorRole(data, groupNames);
        handleSponsorRoles(data, groupNames);
        return data;
    }

    private void handleInvestigatorRole(AccountConfigurationData data, Set<String> groupNames) {
        if (groupNames.contains(UserRoleType.INVESTIGATOR.getGroupName())) {
            data.getRoles().add(UserRoleType.INVESTIGATOR);
        }
    }

    private void handleRegistrationCoordinatorRole(AccountConfigurationData data, Set<String> groupNames) {
        if (groupNames.contains(UserRoleType.REGISTRATION_COORDINATOR.getGroupName())) {
            data.getRoles().add(UserRoleType.REGISTRATION_COORDINATOR);
        }
    }

    private void handleSponsorRoles(AccountConfigurationData data, Set<String> groupNames) {
        for (Organization sponsor : sponsorService.getSponsorOrganizations()) {
            if (groupNames.contains(SponsorRole.getSponsorRepresentativeOrganizationGroupName(sponsor))) {
                data.getSponsorOrganizations().add(sponsor);
                data.getRoles().add(UserRoleType.SPONSOR);
            } else if (groupNames.contains(SponsorRole.getSponsorDelegateOrganizationGroupName(sponsor))) {
                data.getDelegateOrganizations().add(sponsor);
                data.getRoles().add(UserRoleType.SPONSOR_DELEGATE);
            }
        }
    }

    @Override
    public void create(AccountConfigurationData configurationData) throws ValidationException {
        configurationHelperProvider.get().createUser(configurationData);
    }

    @Override
    public void addRoles(FirebirdUser user, AccountConfigurationData configurationData) throws GridInvocationException,
            ValidationException {
        configurationHelperProvider.get().addRoles(user, configurationData);
    }

    @Override
    public void rejectCoordinatorRequest(ManagedInvestigator managedInvestigator) {
        notifyCoodinatorRequest(managedInvestigator);
        deleteManagedInvestigator(managedInvestigator);
    }

    private void deleteManagedInvestigator(ManagedInvestigator managedInvestigator) {
        Session session = sessionProvider.get();
        managedInvestigator.getUser().getRegistrationCoordinatorRole()
                .removeManagedInvestigator(managedInvestigator.getInvestigatorProfile());
        session.delete(managedInvestigator);
        session.update(managedInvestigator.getUser());
        session.update(managedInvestigator.getInvestigatorProfile());
    }

    @Override
    public void acceptCoordinatorRequest(ManagedInvestigator managedInvestigator) {
        managedInvestigator.setStatus(APPROVED);
        notifyCoodinatorRequest(managedInvestigator);
        sessionProvider.get().update(managedInvestigator);
    }

    private void notifyCoodinatorRequest(ManagedInvestigator managedInvestigator) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.MANAGED_INVESTIGATOR, managedInvestigator);
        FirebirdMessage message = templateService.generateMessage(
                FirebirdMessageTemplate.COORDINATOR_REQUEST_UPDATED_EMAIL, parameterValues);
        emailService.sendMessage(managedInvestigator.getUser().getPerson().getEmail(), null, null, message);
    }

    @Override
    public void addManagedInvestigators(FirebirdUser coordinator, List<InvestigatorProfile> investigatorProfiles) {
        if (!coordinator.isRegistrationCoordinator()) {
            throw new IllegalArgumentException("User does not have the Registration Coordinator Role");
        }
        if (!investigatorProfiles.isEmpty()) {
            AccountConfigurationHelper configurationHelper = configurationHelperProvider.get();
            for (InvestigatorProfile profile : investigatorProfiles) {
                ManagedInvestigator registrationCoordinatorRole = coordinator.getRegistrationCoordinatorRole()
                        .addManagedInvestigator(profile);
                configurationHelper.notifyOfRegistrationCoordinatorRequest(registrationCoordinatorRole);
            }
            userService.save(coordinator);
        }
    }

    @Override
    public void removeManagedInvestigator(ManagedInvestigator managedInvestigator) {
        deleteManagedInvestigator(managedInvestigator);
        sendManageCoordinatorAccessEmail(managedInvestigator, FirebirdMessageTemplate.COORDINATOR_REMOVED_EMAIL);
    }

    private void sendManageCoordinatorAccessEmail(ManagedInvestigator managedInvestigator,
            FirebirdMessageTemplate template) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.MANAGED_INVESTIGATOR, managedInvestigator);
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR, managedInvestigator.getInvestigatorProfile()
                .getPerson());
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK,
                FirebirdConstants.BROWSE_INVESTIGATORS_URL_PATH);
        FirebirdMessage message = templateService.generateMessage(template, parameterValues);
        emailService.sendMessage(managedInvestigator.getUser().getPerson().getEmail(), null, null, message);
    }

    @Override
    public void suspendCoordinatorRegistrationAccess(ManagedInvestigator managedInvestigator) {
        ManagedInvestigatorStatus nextState = findNextSuspendState(managedInvestigator.getStatus(),
                SUSPENDED_FROM_REGISTRATIONS);
        managedInvestigator.setStatus(nextState);
        sessionProvider.get().update(managedInvestigator);
        sendManageCoordinatorAccessEmail(managedInvestigator,
                FirebirdMessageTemplate.COORDINATOR_SUSPENDED_FROM_REGISTRATIONS_EMAIL);
    }

    private ManagedInvestigatorStatus findNextSuspendState(ManagedInvestigatorStatus currentStatus,
            ManagedInvestigatorStatus nextStatus) {
        if (!SUSPENSION_STATES.contains(nextStatus)) {
            throw new IllegalStateException();
        } else if (currentStatus == SUSPENDED
                || PARTIAL_SUSPENSION_STATES.equals(EnumSet.of(currentStatus, nextStatus))) {
            return SUSPENDED;
        } else {
            return nextStatus;
        }
    }

    @Override
    public void activateCoordinatorRegistrationAccess(ManagedInvestigator managedInvestigator) {
        if (managedInvestigator.getStatus() == SUSPENDED) {
            managedInvestigator.setStatus(SUSPENDED_FROM_PROFILE);
        } else {
            managedInvestigator.setStatus(APPROVED);
        }
        sessionProvider.get().update(managedInvestigator);
        sendManageCoordinatorAccessEmail(managedInvestigator,
                FirebirdMessageTemplate.COORDINATOR_UNSUSPENDED_FROM_REGISTRATIONS_EMAIL);
    }

    @Override
    public void suspendCoordinatorProfileAccess(ManagedInvestigator managedInvestigator) {
        ManagedInvestigatorStatus nextState = findNextSuspendState(managedInvestigator.getStatus(),
                SUSPENDED_FROM_PROFILE);
        managedInvestigator.setStatus(nextState);
        sessionProvider.get().update(managedInvestigator);
        sendManageCoordinatorAccessEmail(managedInvestigator,
                FirebirdMessageTemplate.COORDINATOR_SUSPENDED_FROM_PROFILE_EMAIL);
    }

    @Override
    public void activateCoordinatorProfileAccess(ManagedInvestigator managedInvestigator) {
        if (managedInvestigator.getStatus() == SUSPENDED) {
            managedInvestigator.setStatus(SUSPENDED_FROM_REGISTRATIONS);
        } else {
            managedInvestigator.setStatus(APPROVED);
        }
        sessionProvider.get().update(managedInvestigator);
        sendManageCoordinatorAccessEmail(managedInvestigator,
                FirebirdMessageTemplate.COORDINATOR_UNSUSPENDED_FROM_PROFILE_EMAIL);
    }

    @Override
    public void requestAccount(AccountConfigurationData accountInformation) {
        notifySupportOfAccountRequest(accountInformation);
        notifyUserOfAccountRequest(accountInformation);
    }

    private void notifySupportOfAccountRequest(AccountConfigurationData accountInformation) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.ACCOUNT_DATA, accountInformation);
        FirebirdMessageTemplate template;
        template = getAccountRequestToSupportTemplate(accountInformation);
        FirebirdMessage message = templateService.generateMessage(template, parameterValues);
        emailService.sendMessage(supportEmailAddress, null, null, message);
    }

    private FirebirdMessageTemplate getAccountRequestToSupportTemplate(AccountConfigurationData accountInformation) {
        if (accountInformation.isExistingLdapAccount()) {
            return FirebirdMessageTemplate.ACCOUNT_REQUEST_EXISTING_LDAP_ACCOUNT_EMAIL;
        } else {
            return FirebirdMessageTemplate.ACCOUNT_REQUEST_EMAIL;
        }
    }

    private void notifyUserOfAccountRequest(AccountConfigurationData accountInformation) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.ACCOUNT_DATA, accountInformation);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_SUPPORT_EMAIL, supportEmailAddress);
        FirebirdMessageTemplate template = FirebirdMessageTemplate.ACCOUNT_REQUEST_USER_NOTIFICATION_EMAIL;
        FirebirdMessage message = templateService.generateMessage(template, parameterValues);
        emailService.sendMessage(accountInformation.getPerson().getEmail(), null, null, message);
    }

    @Override
    public void removeRole(FirebirdUser user, UserRoleType role) throws GridInvocationException {
        if (user == null || role == null) {
            throw new IllegalArgumentException("Arguments must be non-null");
        }
        checkHasRole(user, role);
        switch (role) {
            case SPONSOR:
                removeSponsorRoles(user, false);
                break;
            case SPONSOR_DELEGATE:
                removeSponsorRoles(user, true);
                break;
            case REGISTRATION_COORDINATOR:
                removeRegistrationCoordinatorRole(user);
                break;
            default:
                throw new IllegalArgumentException("Illegal request to remove role of type " + role);
        }
        userService.save(user);
    }

    private void checkHasRole(FirebirdUser user, UserRoleType role) {
        if (!user.getRoles().contains(role)) {
            throw new IllegalArgumentException("User " + user.getUsername() + " does not have role: " + role);
        }
    }

    private void removeSponsorRoles(FirebirdUser user, boolean removeDelegateRoles) throws GridInvocationException {
        Set<SponsorRole> sponsorRoles = Sets.newHashSet(user.getSponsorRoles());
        for (SponsorRole role : sponsorRoles) {
            if (shouldRemove(role, removeDelegateRoles)) {
                user.getSponsorRoles().remove(role);
                gridGrouperService.removeGridUserFromGroup(user.getUsername(), role.getSponsorOrganizationGroupName());
                gridGrouperService.removeGridUserFromGroup(user.getUsername(), role.getVerifiedSponsorGroupName());
            }
        }
    }

    private boolean shouldRemove(SponsorRole role, boolean removeDelegateRoles) {
        return !role.isDelegate() ^ removeDelegateRoles;
    }

    private void removeRegistrationCoordinatorRole(FirebirdUser user) throws GridInvocationException {
        if (!user.isRegistrationCoordinator()) {
            throw new IllegalArgumentException("User does not have the Registration Coordinator Role");
        }
        gridGrouperService.removeGridUserFromGroup(user.getUsername(),
                UserRoleType.REGISTRATION_COORDINATOR.getGroupName());
        notifyInvestigatorsOfCoordinatorRoleRemoval(user);
        user.getRegistrationCoordinatorRole().getManagedInvestigators().clear();
        sessionProvider.get().delete(user.getRegistrationCoordinatorRole());
        user.removeRegistrationCoordinatorRole();
    }

    private void notifyInvestigatorsOfCoordinatorRoleRemoval(FirebirdUser user) {
        for (InvestigatorProfile investigatorProfile : user.getRegistrationCoordinatorRole().getManagedProfiles()) {
            notifyInvestigatorOfCoordinatorRoleRemoval(investigatorProfile, user);
        }
    }

    private void notifyInvestigatorOfCoordinatorRoleRemoval(InvestigatorProfile profile, FirebirdUser user) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR, profile.getPerson());
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION_COORDINATOR, user.getPerson());
        FirebirdMessage message = templateService.generateMessage(
                FirebirdMessageTemplate.COORDINATOR_REMOVED_ROLE_EMAIL, parameterValues);
        emailService.sendMessage(profile.getPerson().getEmail(), null, null, message);
    }
}
