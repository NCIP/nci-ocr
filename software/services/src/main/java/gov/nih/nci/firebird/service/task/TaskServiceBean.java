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
package gov.nih.nci.firebird.service.task;

import static gov.nih.nci.firebird.common.FirebirdDateUtils.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.RevisedInvestigatorRegistration;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.InvestigatorRole;
import gov.nih.nci.firebird.data.user.InvestigatorStatus;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationService;
import gov.nih.nci.firebird.service.annual.registration.RegistrationWithdrawalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.messages.FirebirdStringTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Session;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Implements user task retrieval functionality.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.ExcessiveClassLength" })
// requires many supporting methods to generate different tasks
public class TaskServiceBean implements TaskService {

    private TemplateService templateService;
    private ProtocolRegistrationService protocolRegistrationService;
    private AnnualRegistrationService annualRegistrationService;
    private InvestigatorProfileService investigatorProfileService;
    private RegistrationWithdrawalService registrationWithdrawalService;
    private SponsorService sponsorService;
    private Provider<Session> sessionProvider;

    @Override
    public List<Task> getTasks(FirebirdUser user, Set<String> groupNames) {
        List<Task> tasks = new ArrayList<Task>();
        tasks.addAll(handleInvestigatorTasks(user, groupNames));
        tasks.addAll(handleSponsorTasks(user, groupNames));
        tasks.addAll(handleCtepSponsorTasks(groupNames));
        tasks.addAll(handleCoordinatorTasks(user));
        tasks.addAll(handleMissingExpectedRoleTasks(user));
        Collections.sort(tasks);
        sessionProvider.get().clear();
        return tasks;
    }

    private List<Task> handleInvestigatorTasks(FirebirdUser user, Set<String> groupNames) {
        List<Task> tasks = Lists.newArrayList();
        if (user.isInvestigator()) {
            tasks.addAll(getInvestigatorRoleVerificationTask(groupNames));
            Set<AbstractProtocolRegistration> protocolRegistrations = user.getInvestigatorRole().getProfile()
                    .getCurrentProtocolRegistrations();
            tasks.addAll(getRegistrationInvitationTasks(protocolRegistrations));
            tasks.addAll(getRegistrationInProgressTasks(protocolRegistrations));
            tasks.addAll(getRegistrationIncompleteTasks(protocolRegistrations));
            tasks.addAll(getRegistrationCompletedTasks(protocolRegistrations));
            tasks.addAll(getRegistrationReturnedTasks(protocolRegistrations));
            tasks.addAll(getProtocolUpdatedRegistrationTasks(protocolRegistrations));
            tasks.addAll(handleRegistrationCoordinatorTasks(user));
            Set<AnnualRegistration> annualRegistrations = user.getInvestigatorRole().getProfile()
                    .getAnnualRegistrations();
            tasks.addAll(getRegistrationRenewalTasks(annualRegistrations));
            tasks.addAll(getAnnualRegistrationCompletedTasks(annualRegistrations));
            tasks.addAll(getAnnualRegistrationApprovedTasksForInvestigator(annualRegistrations));
            tasks.addAll(getAnnualRegistrationReturnedTasks(annualRegistrations));
            handleSuspendedInvestigator(tasks, user);
            handleDisqualifiedInvestigator(tasks, user);
        }
        return tasks;
    }

    private Set<Task> getInvestigatorRoleVerificationTask(Set<String> groupNames) {
        Set<Task> tasks = Sets.newHashSet();
        if (isUserAwaitingInvestigatorRoleVerification(groupNames)) {
            TaskType taskType = TaskType.INVESTIGATOR_ROLE_AWAITING_VERIFICATION;
            Date date = new Date();
            Map<FirebirdTemplateParameter, Object> parameters = Collections.emptyMap();
            String description = templateService.generateString(
                    FirebirdStringTemplate.INVESTIGATOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE, parameters);
            tasks.add(new Task(description, date, taskType, null));
        }
        return tasks;
    }

    private boolean isUserAwaitingInvestigatorRoleVerification(Set<String> groupNames) {
        return groupNames.contains(UserRoleType.INVESTIGATOR.getGroupName())
                && !groupNames.contains(UserRoleType.INVESTIGATOR.getVerifiedGroupName());
    }

    private Set<Task> getRegistrationInvitationTasks(Set<AbstractProtocolRegistration> registrations) {
        Set<Task> tasks = new HashSet<Task>();
        for (AbstractProtocolRegistration registration : getInvitedRegistrations(registrations)) {
            tasks.add(createRegistrationInvitationTask(registration));
        }
        return tasks;
    }

    private Set<AbstractProtocolRegistration> getInvitedRegistrations(Set<AbstractProtocolRegistration> registrations) {
        Set<AbstractProtocolRegistration> invitedRegistrations = Sets.newHashSet();
        for (AbstractProtocolRegistration registration : registrations) {
            if (InvitationStatus.NO_RESPONSE == registration.getInvitation().getInvitationStatus()
                    && registration.getStatus() != RegistrationStatus.INACTIVE) {
                invitedRegistrations.add(registration);
            }
        }
        return invitedRegistrations;
    }

    private Task createRegistrationInvitationTask(AbstractProtocolRegistration registration) {
        Date date = registration.getInvitation().getInvitationChangeDate();
        Map<FirebirdTemplateParameter, Object> values = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        String title = null;
        if (registration instanceof InvestigatorRegistration) {
            values.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
            title = templateService.generateString(FirebirdStringTemplate.INVESTIGATOR_INVITATION_TASK_TITLE, values);
        } else if (registration instanceof SubInvestigatorRegistration) {
            values.put(FirebirdTemplateParameter.SUBINVESTIGATOR_REGISTRATION, registration);
            title = templateService
                    .generateString(FirebirdStringTemplate.SUBINVESTIGATOR_INVITATION_TASK_TITLE, values);
        } else {
            throw new IllegalArgumentException("unknown registration type " + registration);
        }

        return new Task(title, date, TaskType.REGISTRATION_INVITATION, registration.getId());
    }

    private Set<Task> getRegistrationInProgressTasks(Set<AbstractProtocolRegistration> registrations) {
        Set<Task> tasks = new HashSet<Task>();
        for (AbstractProtocolRegistration registration : registrations) {
            if (RegistrationStatus.IN_PROGRESS == registration.getStatus()) {
                tasks.add(createRegistrationInProgressTask(registration));
            }
        }
        return tasks;
    }

    private Task createRegistrationInProgressTask(AbstractProtocolRegistration registration) {
        FirebirdStringTemplate title = null;
        if (RegistrationStatus.IN_PROGRESS == registration.getStatus()
                && InvitationStatus.REACTIVATED == registration.getInvitation().getInvitationStatus()) {
            title = FirebirdStringTemplate.REGISTRATION_REACTIVATED_TASK_TITLE;
        } else {
            title = FirebirdStringTemplate.REGISTRATION_IN_PROGRESS_TASK_TITLE;
        }
        return createRegistrationTask(TaskType.REGISTRATION_IN_PROGRESS, title, registration);
    }

    private Set<Task> getRegistrationCompletedTasks(Set<AbstractProtocolRegistration> registrations) {
        Set<Task> tasks = new HashSet<Task>();
        for (AbstractProtocolRegistration registration : registrations) {
            if (RegistrationStatus.COMPLETED == registration.getStatus()) {
                tasks.add(createRegistrationCompleteTask(registration));
            }
        }
        return tasks;
    }

    private Task createRegistrationCompleteTask(AbstractProtocolRegistration registration) {
        return createRegistrationTask(TaskType.REGISTRATION_READY_FOR_SIGNING,
                FirebirdStringTemplate.REGISTRATION_READY_FOR_SIGNING_TASK_TITLE, registration);
    }

    private Set<Task> getRegistrationIncompleteTasks(Set<AbstractProtocolRegistration> registrations) {
        Set<Task> tasks = new HashSet<Task>();
        for (AbstractProtocolRegistration registration : registrations) {
            if (RegistrationStatus.INCOMPLETE == registration.getStatus()) {
                // Incomplete tasks treated the same as in progress.
                tasks.add(createRegistrationInProgressTask(registration));
            }
        }
        return tasks;
    }

    private Set<Task> getRegistrationReturnedTasks(Set<AbstractProtocolRegistration> registrations) {
        Set<Task> tasks = new HashSet<Task>();
        for (AbstractProtocolRegistration registration : registrations) {
            if (RegistrationStatus.RETURNED == registration.getStatus()) {
                tasks.add(createRegistrationTask(TaskType.REGISTRATION_RETURNED,
                        FirebirdStringTemplate.REGISTRATION_RETURNED_TASK_TITLE, registration));
            }
        }
        return tasks;
    }

    private Set<Task> getProtocolUpdatedRegistrationTasks(Set<AbstractProtocolRegistration> registrations) {
        Set<Task> tasks = new HashSet<Task>();
        for (AbstractProtocolRegistration registration : registrations) {
            if (RegistrationStatus.PROTOCOL_UPDATED == registration.getStatus()) {
                tasks.add(createRegistrationTask(TaskType.REGISTRATION_IN_PROGRESS,
                        FirebirdStringTemplate.PROTOCOL_MODIFIED_TASK_TITLE, registration));
            }
        }
        return tasks;
    }

    private Task createRegistrationTask(TaskType taskType, FirebirdStringTemplate titleTemplate,
            AbstractProtocolRegistration registration) {
        Date date = registration.getStatusDate();
        Map<FirebirdTemplateParameter, Object> values = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        values.put(FirebirdTemplateParameter.REGISTRATION, registration);
        String title = templateService.generateString(titleTemplate, values);
        return new Task(title, date, taskType, registration.getId());
    }

    // Sponsor related methods

    private List<Task> handleSponsorTasks(FirebirdUser user, Set<String> groupNames) {
        List<Task> tasks = Lists.newArrayList();
        if (isUserAwaitingSponsorRoleVerification(user, groupNames)) {
            tasks.add(getSponsorRoleVerificationTask());
        }
        if (user.getSponsorRoles() != null && !user.getSponsorRoles().isEmpty()) {
            tasks.addAll(getSponsorRegistrationReviewTasks(user, groupNames));
        }
        return tasks;
    }

    private List<Task> getSponsorRegistrationReviewTasks(FirebirdUser user, Set<String> groupNames) {
        List<Task> tasks = Lists.newArrayList();
        Iterables.addAll(tasks, getRegistrationSubmittedTasks(user, groupNames));
        Iterables.addAll(tasks, getRegistrationInReviewTasks(user, groupNames));
        Iterables.addAll(tasks, getRegistrationPacketApprovalTasks(user, groupNames));
        return tasks;
    }

    private boolean isUserAwaitingSponsorRoleVerification(FirebirdUser user, Set<String> groupNames) {
        Organization sponsorWithAnnualRegistrations = sponsorService.getSponsorOrganizationWithAnnualRegistrations();
        for (Organization sponsor : user.getSponsorOrganizations()) {
            if (!sponsor.equals(sponsorWithAnnualRegistrations)
                    && !user.hasVerifiedSponsorRole(sponsor, groupNames)) {
                return true;
            }
        }
        return false;
    }

    private Task getSponsorRoleVerificationTask() {
        TaskType taskType = TaskType.SPONSOR_ROLE_AWAITING_VERIFICATION;
        Date date = new Date();
        Map<FirebirdTemplateParameter, Object> parameters = Collections.emptyMap();
        String description = templateService.generateString(
                FirebirdStringTemplate.SPONSOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE, parameters);
        return new Task(description, date, taskType, null);
    }

    private Iterable<Task> getRegistrationSubmittedTasks(FirebirdUser user, Set<String> groupNames) {
        List<AbstractProtocolRegistration> registrations =
                protocolRegistrationService.getByStatusForUser(RegistrationStatus.SUBMITTED, user, groupNames);
        return createRegistrationReviewTasks(registrations, FirebirdStringTemplate.REGISTRATION_SUBMITTED_TASK_TITLE);
    }

    private Iterable<Task> createRegistrationReviewTasks(Iterable<AbstractProtocolRegistration> registrations,
            final FirebirdStringTemplate titleTemplate) {
        return Iterables.transform(registrations, new Function<AbstractProtocolRegistration, Task>() {
            @Override
            public Task apply(AbstractProtocolRegistration registration) {
                return createRegistrationReviewTask(registration,
                        titleTemplate);
            }
        });
    }

    private Iterable<Task> getRegistrationInReviewTasks(FirebirdUser user, Set<String> groupNames) {
        List<AbstractProtocolRegistration> registrations =
                protocolRegistrationService.getByStatusForUser(RegistrationStatus.IN_REVIEW, user, groupNames);
        return createRegistrationReviewTasks(registrations, FirebirdStringTemplate.REGISTRATION_IN_REVIEW_TASK_TITLE);
    }

    private Iterable<Task> getRegistrationPacketApprovalTasks(FirebirdUser user, Set<String> groupNames) {
        List<AbstractProtocolRegistration> acceptedRegistrations =
                protocolRegistrationService.getByStatusForUser(RegistrationStatus.ACCEPTED, user, groupNames);
        Iterable<AbstractProtocolRegistration> approvableRegistrations =
                getApprovableRegistrations(acceptedRegistrations, user);
        return createRegistrationPacketApprovalTasks(approvableRegistrations);
    }

    private Iterable<AbstractProtocolRegistration> getApprovableRegistrations(
            List<AbstractProtocolRegistration> acceptedRegistrations, final FirebirdUser user) {
        return Iterables.filter(acceptedRegistrations, new Predicate<AbstractProtocolRegistration>() {
            @Override
            public boolean apply(AbstractProtocolRegistration registration) {
                if (registration instanceof RevisedInvestigatorRegistration
                        || !(registration instanceof InvestigatorRegistration)) {
                    return false;
                }
                InvestigatorRegistration investigatorRegistration = (InvestigatorRegistration) registration;
                return investigatorRegistration.isApprovable()
                        && user.isSponsorRepresentative(registration.getProtocol().getSponsor());
            }
        });
    }

    private Iterable<Task> createRegistrationPacketApprovalTasks(
            Iterable<AbstractProtocolRegistration> approvableRegistrations) {
        return Iterables.transform(approvableRegistrations, new Function<AbstractProtocolRegistration, Task>() {
            @Override
            public Task apply(AbstractProtocolRegistration registration) {
                return createRegistrationPacketApprovalTask((InvestigatorRegistration) registration);
            }
        });
    }

    private Task createRegistrationReviewTask(AbstractProtocolRegistration registration,
            FirebirdStringTemplate titleTemplate) {
        Date date = registration.getStatusDate();
        Map<FirebirdTemplateParameter, Object> values = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        values.put(FirebirdTemplateParameter.REGISTRATION, registration);
        String title = templateService.generateString(titleTemplate, values);
        return new Task(title, date, TaskType.REGISTRATION_FOR_REVIEW, registration.getId());
    }

    private Task createRegistrationPacketApprovalTask(InvestigatorRegistration registration) {
        Date date = registration.getStatusDate();
        Map<FirebirdTemplateParameter, Object> values = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        values.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
        String title = templateService.generateString(
                FirebirdStringTemplate.REGISTRATION_PACKET_AWAITING_APPROVAL_TITLE, values);
        return new Task(title, date, TaskType.REGISTRATION_PACKET_AWAITING_APPROVAL, registration.getId());
    }

    private List<Task> handleCtepSponsorTasks(Set<String> groupNames) {
        List<Task> tasks = Lists.newArrayList();
        tasks.addAll(getCtepSponsorAnnualRegistrationReviewTasks(groupNames));
        tasks.addAll(getRegistrationWithdrawalRequestTasks(groupNames));
        return tasks;
    }

    private List<Task> getCtepSponsorAnnualRegistrationReviewTasks(Set<String> groupNames) {
        List<Task> tasks = Lists.newArrayList();
        if (groupNames.contains(UserRoleType.CTEP_SPONSOR.getGroupName())
                || groupNames.contains(UserRoleType.CTEP_SPONSOR_DELEGATE.getGroupName())) {
            tasks.addAll(getCtepSponsorAnnualRegistrationReviewTasks(RegistrationStatus.SUBMITTED));
            tasks.addAll(getCtepSponsorAnnualRegistrationReviewTasks(RegistrationStatus.IN_REVIEW));
            tasks.addAll(getCtepSponsorAnnualRegistrationReviewTasks(RegistrationStatus.REVIEW_ON_HOLD));
        }
        return tasks;
    }

    private List<Task> getCtepSponsorAnnualRegistrationReviewTasks(RegistrationStatus status) {
        List<Task> tasks = Lists.newArrayList();
        for (AnnualRegistration registration : annualRegistrationService.getByStatus(status)) {
            FirebirdStringTemplate titleTemplate = getCtepSponsorAnnualRegistrationReviewTitleTemplate(status);
            tasks.add(createAnnualRegistrationReviewTask(registration, titleTemplate));
        }
        return tasks;
    }

    private FirebirdStringTemplate getCtepSponsorAnnualRegistrationReviewTitleTemplate(RegistrationStatus status) {
      switch (status) {
      case SUBMITTED:
          return FirebirdStringTemplate.ANNUAL_REGISTRATION_SUBMITTED_TASK_TITLE;
      case IN_REVIEW:
          return FirebirdStringTemplate.ANNUAL_REGISTRATION_IN_REVIEW_TASK_TITLE;
      case REVIEW_ON_HOLD:
          return FirebirdStringTemplate.ANNUAL_REGISTRATION_REVIIEW_ON_HOLD_TASK_TITLE;
      default:
          throw new IllegalArgumentException("Unsupported status for annual registration review: " + status);
      }
    }

    private List<Task> getRegistrationWithdrawalRequestTasks(Set<String> groupNames) {
        List<Task> tasks = Lists.newArrayList();
        if (groupNames.contains(UserRoleType.CTEP_SPONSOR.getGroupName())
                || groupNames.contains(UserRoleType.CTEP_SPONSOR_DELEGATE.getGroupName())) {
            for (FirebirdUser investigator : registrationWithdrawalService
                    .getAllInvestigatorsWithPendingWithdrawalRequests()) {
                tasks.add(createRegistrationWithdrawalRequestTask(investigator));
            }
        }
        return tasks;
    }

    private Task createRegistrationWithdrawalRequestTask(FirebirdUser investigator) {
        Date date = new Date();
        Map<FirebirdTemplateParameter, Object> values = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        values.put(FirebirdTemplateParameter.INVESTIGATOR, investigator.getPerson());
        String title = templateService.generateString(FirebirdStringTemplate.REQUEST_FOR_WITHDRAWAL_TASK_TITLE, values);
        return new Task(title, date, TaskType.REGISTRATION_WITHDRAW_REQUEST, investigator.getId());
    }

    private Task createAnnualRegistrationReviewTask(AnnualRegistration registration,
            FirebirdStringTemplate titleTemplate) {
        Date date = registration.getStatusDate();
        Map<FirebirdTemplateParameter, Object> values = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        values.put(FirebirdTemplateParameter.ANNUAL_REGISTRATION, registration);
        String title = templateService.generateString(titleTemplate, values);
        return new Task(title, date, TaskType.ANNUAL_REGISTRATION_FOR_REVIEW, registration.getId());
    }

    private List<Task> handleRegistrationCoordinatorTasks(FirebirdUser user) {
        List<Task> tasks = Lists.newArrayList();
        for (ManagedInvestigator managedInvestigator : user.getInvestigatorRole().getProfile()
                .getRegistrationCoordinatorMappings()) {
            if (!managedInvestigator.isApproved()) {
                tasks.add(createRegistrationCoordinatorTask(managedInvestigator));
            }
        }
        return tasks;
    }

    private Task createRegistrationCoordinatorTask(ManagedInvestigator managedInvestigator) {
        Date date = managedInvestigator.getCreateDate();
        Map<FirebirdTemplateParameter, Object> values = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        values.put(FirebirdTemplateParameter.REGISTRATION_COORDINATOR, managedInvestigator.getUser().getPerson());
        String title = templateService.generateString(FirebirdStringTemplate.REGISTRATION_COORDINATOR_REQUEST_TITLE,
                values);
        return new Task(title, date, TaskType.REGISTRATION_COORDINATOR_REQUEST, managedInvestigator.getId());
    }

    private List<Task> getRegistrationRenewalTasks(Set<AnnualRegistration> registrations) {
        List<Task> tasks = Lists.newArrayList();
        for (AnnualRegistration registration : registrations) {
            if (registration.isPendingRenewal()
                    && isCurrentDateBeforeAndWithinRangeOfDate(registration.getDueDate(),
                            annualRegistrationService.getDaysBeforeDueDateToSendSecondNotification())) {
                tasks.add(createRegistrationRenewalTask(registration));
            }
        }
        return tasks;
    }

    private Task createRegistrationRenewalTask(AnnualRegistration registration) {
        Date date = DateUtils.addDays(registration.getDueDate(),
                -annualRegistrationService.getDaysBeforeDueDateToSendSecondNotification());
        Map<FirebirdTemplateParameter, Object> parameters = Collections.emptyMap();
        String description = templateService.generateString(
                FirebirdStringTemplate.INVESTIGATOR_REGISTRATION_RENEWAL_TASK_TITLE, parameters);
        return new Task(description, date, TaskType.REGISTRATION_RENEWAL, registration.getId());
    }

    private List<Task> getAnnualRegistrationCompletedTasks(Set<AnnualRegistration> registrations) {
        List<Task> tasks = Lists.newArrayList();
        for (AnnualRegistration registration : registrations) {
            if (registration.getStatus() == RegistrationStatus.COMPLETED) {
                tasks.add(createAnnualRegistrationCompletedTask(registration));
            }
        }
        return tasks;
    }

    private Task createAnnualRegistrationCompletedTask(AnnualRegistration registration) {
        Date date = registration.getStatusDate();
        Map<FirebirdTemplateParameter, Object> parameters = Collections.emptyMap();
        String description = templateService.generateString(
                FirebirdStringTemplate.ANNUAL_REGISTRATION_READY_FOR_SIGNING_TASK_TITLE, parameters);
        return new Task(description, date, TaskType.ANNUAL_REGISTRATION_READY_FOR_SIGNING, registration.getId());
    }

    private List<Task> getAnnualRegistrationApprovedTasksForInvestigator(Set<AnnualRegistration> registrations) {
        List<Task> tasks = Lists.newArrayList();
        for (AnnualRegistration registration : registrations) {
            if (registration.getStatus() == RegistrationStatus.APPROVED
                    && !registration.isApprovalAcknowledgedByInvestigator()) {
                tasks.add(createAnnualRegistrationApprovedTask(registration,
                        FirebirdStringTemplate.ANNUAL_REGISTRATION_APPROVED_TASK_FOR_INVESTIGATOR_TITLE));
            }
        }
        return tasks;
    }

    private Task createAnnualRegistrationApprovedTask(AnnualRegistration registration,
            FirebirdStringTemplate taskTitle) {
        Date date = registration.getStatusDate();
        Map<FirebirdTemplateParameter, Object> parameters = Maps.newHashMap();
        parameters.put(FirebirdTemplateParameter.ANNUAL_REGISTRATION, registration);
        String description = templateService.generateString(taskTitle, parameters);
        return new Task(description, date, TaskType.ANNUAL_REGISTRATION_APPROVED, registration.getId());
    }

    private Set<Task> getAnnualRegistrationReturnedTasks(Set<AnnualRegistration> registrations) {
        Set<Task> tasks = new HashSet<Task>();
        for (AnnualRegistration registration : registrations) {
            if (RegistrationStatus.RETURNED == registration.getStatus()) {
                tasks.add(createAnnualRegistrationReturnedTask(registration));
            }
        }
        return tasks;
    }

    private Task createAnnualRegistrationReturnedTask(AnnualRegistration registration) {
        Date date = registration.getStatusDate();
        Map<FirebirdTemplateParameter, Object> parameters = Collections.emptyMap();
        String description = templateService.generateString(
                FirebirdStringTemplate.ANNUAL_REGISTRATION_RETURNED_TASK_TITLE, parameters);
        return new Task(description, date, TaskType.ANNUAL_REGISTRATION_RETURNED, registration.getId());
    }

    private void handleSuspendedInvestigator(List<Task> tasks, FirebirdUser investigator) {
        InvestigatorRole investigatorRole = investigator.getInvestigatorRole();
        if (isSuspended(investigator) && investigatorRole.isStatusChangeNotificationRequiredForInvestigator()) {
            Map<FirebirdTemplateParameter, Object> parameters = Collections.emptyMap();
            String description = templateService.generateString(
                    FirebirdStringTemplate.CTEP_INVESTIGATOR_SUSPENEDED_TASK_TITLE_FOR_INVESTIGATOR, parameters);
            AnnualRegistration registration = investigatorRole.getProfile().getCurrentAnnualRegistration();
            Task suspensionTask = new Task(description, new Date(), TaskType.INVESTIGATOR_SUSPENSION_NOTICE,
                    registration.getId());
            tasks.add(suspensionTask);
        }
    }

    private boolean isSuspended(FirebirdUser investigator) {
        return investigator.isCtepUser()
                && InvestigatorStatus.SUSPENDED.equals(investigator.getInvestigatorRole().getStatus());
    }

    private void handleDisqualifiedInvestigator(List<Task> tasks, FirebirdUser investigator) {
        InvestigatorRole investigatorRole = investigator.getInvestigatorRole();
        if (investigatorRole.getDisqualificationReason() != null
                && investigatorRole.getDisqualificationReason().getReason() != null
                && !investigatorRole.getDisqualificationReason().isAcknowledgedByInvestigator()) {
            Map<FirebirdTemplateParameter, Object> parameters = Collections.emptyMap();
            String description = templateService.generateString(
                    FirebirdStringTemplate.CTEP_INVESTIGATOR_DISQUALIFIED_TASK_TITLE_FOR_INVESTIGATOR, parameters);
            AnnualRegistration registration = investigatorRole.getProfile().getCurrentAnnualRegistration();
            Task suspensionTask = new Task(description, new Date(), TaskType.INVESTIGATOR_DISQUALIFICATION_NOTICE,
                    registration.getId());
            tasks.add(suspensionTask);
        }
    }

    private List<Task> handleCoordinatorTasks(FirebirdUser user) {
        List<Task> tasks = Lists.newArrayList();
        if (user.isRegistrationCoordinator()) {
            tasks.addAll(getCoordinatorRoleVerificationTask(user));
            tasks.addAll(getCoordinatorRegistrationRenewalTasks(user));
            tasks.addAll(getAnnualRegistrationApprovedTasksForCoordinator(user));
            if (user.isCtepUser()) {
                tasks.addAll(getInvestigatorSuspensionNoticeTasks(user));
                tasks.addAll(getInvestigatorDisqualificationNoticeTasks(user));
            }
        }
        return tasks;
    }

    private List<Task> getCoordinatorRoleVerificationTask(FirebirdUser user) {
        List<Task> tasks = Lists.newArrayList();
        if (containsCoordinatorRoleAwaitingApproval(user)) {
            tasks.add(createCoordinatorRoleVerificationTask());
        }
        return tasks;
    }

    private boolean containsCoordinatorRoleAwaitingApproval(FirebirdUser user) {
        Set<ManagedInvestigator> managedInvestigators = user.getRegistrationCoordinatorRole().getManagedInvestigators();
        return Iterables.any(managedInvestigators, new Predicate<ManagedInvestigator>() {
            @Override
            public boolean apply(ManagedInvestigator managedInvestigator) {
                return !managedInvestigator.isApproved();
            }
        });
    }

    private Task createCoordinatorRoleVerificationTask() {
        TaskType taskType = TaskType.COORDINATOR_ROLE_AWAITING_VERIFICATION;
        Date date = new Date();
        Map<FirebirdTemplateParameter, Object> parameters = Collections.emptyMap();
        String description = templateService.generateString(
                FirebirdStringTemplate.COORDINATOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE, parameters);
        return new Task(description, date, taskType, null);
    }

    private List<Task> getCoordinatorRegistrationRenewalTasks(FirebirdUser user) {
        List<Task> registrationRenewalTasks = Lists.newArrayList();
        for (InvestigatorProfile profile : user.getRegistrationCoordinatorRole().getApprovedManagedProfiles()) {
            for (AnnualRegistration registration : profile.getAnnualRegistrationsAwaitingRenewal()) {
                registrationRenewalTasks.add(createCoordinatorRegistrationRenewalTask(registration));
            }
        }
        return registrationRenewalTasks;
    }

    private Task createCoordinatorRegistrationRenewalTask(AnnualRegistration registration) {
        Date date = DateUtils.addDays(registration.getDueDate(),
                -annualRegistrationService.getDaysBeforeDueDateToSendFirstNotification());
        Map<FirebirdTemplateParameter, Object> parameters = Maps.newHashMap();
        parameters.put(FirebirdTemplateParameter.ANNUAL_REGISTRATION, registration);
        String description = templateService.generateString(
                FirebirdStringTemplate.COORDINATOR_REGISTRATION_RENEWAL_TASK_TITLE, parameters);
        return new Task(description, date, TaskType.REGISTRATION_RENEWAL, registration.getId());
    }

    private List<Task> getAnnualRegistrationApprovedTasksForCoordinator(FirebirdUser user) {
        List<Task> tasks = Lists.newArrayList();
        for (InvestigatorProfile profile : user.getRegistrationCoordinatorRole().getApprovedManagedProfiles()) {
            for (AnnualRegistration registration : profile.getAnnualRegistrations()) {
                if (registration.getStatus() == RegistrationStatus.APPROVED
                        && !registration.isApprovalAcknowledgedByCoordinator()) {
                    tasks.add(createAnnualRegistrationApprovedTask(registration,
                            FirebirdStringTemplate.ANNUAL_REGISTRATION_APPROVED_TASK_FOR_COORDINATOR_TITLE));
                }
            }
        }
        return tasks;
    }

    private List<Task> getInvestigatorSuspensionNoticeTasks(FirebirdUser coordinator) {
        List<Task> tasks = Lists.newArrayList();
        for (ManagedInvestigator role : coordinator.getRegistrationCoordinatorRole().getManagedInvestigators()) {
            FirebirdUser investigator = role.getInvestigatorProfile().getUser();
            if (isSuspended(investigator)
                && investigator.getInvestigatorRole().isStatusChangeNotificationRequiredForCoordinator()) {
                    tasks.add(createSuspendedInvestigatorCoordinatorNotificationTask(investigator));
            }
        }
        return tasks;
    }

    private Task createSuspendedInvestigatorCoordinatorNotificationTask(FirebirdUser investigator) {
        Date date = new Date();
        Map<FirebirdTemplateParameter, Object> parameters = Maps.newHashMap();
        parameters.put(FirebirdTemplateParameter.INVESTIGATOR, investigator.getPerson());
        AnnualRegistration registration =
                investigator.getInvestigatorRole().getProfile().getCurrentAnnualRegistration();
        String description = templateService.generateString(
                FirebirdStringTemplate.CTEP_INVESTIGATOR_SUSPENEDED_TASK_TITLE_FOR_COORDINATOR, parameters);
        return new Task(description, date, TaskType.INVESTIGATOR_SUSPENSION_NOTICE, registration.getId());
    }

    private List<Task> getInvestigatorDisqualificationNoticeTasks(FirebirdUser coordinator) {
        List<Task> tasks = Lists.newArrayList();
        for (ManagedInvestigator role : coordinator.getRegistrationCoordinatorRole().getManagedInvestigators()) {
            FirebirdUser investigator = role.getInvestigatorProfile().getUser();
            if (investigator.getInvestigatorRole().getDisqualificationReason() != null
                    && !investigator.getInvestigatorRole().getDisqualificationReason().isAcknowledgedByCoordinator()) {
                tasks.add(createDisqualifiedInvestigatorCoordinatorNotificationTask(investigator));
            }
        }
        return tasks;
    }

    private Task createDisqualifiedInvestigatorCoordinatorNotificationTask(FirebirdUser investigator) {
        Date date = new Date();
        Map<FirebirdTemplateParameter, Object> parameters = Maps.newHashMap();
        parameters.put(FirebirdTemplateParameter.INVESTIGATOR, investigator.getPerson());
        AnnualRegistration registration = investigator.getInvestigatorRole().getProfile()
                .getCurrentAnnualRegistration();
        String description = templateService.generateString(
                FirebirdStringTemplate.CTEP_INVESTIGATOR_DISQUALIFIED_TASK_TITLE_FOR_COORDINATOR, parameters);
        return new Task(description, date, TaskType.INVESTIGATOR_DISQUALIFICATION_NOTICE, registration.getId());
    }

    private List<Task> handleMissingExpectedRoleTasks(FirebirdUser user) {
        List<Task> tasks = Lists.newArrayList();
        if (user.getInvestigatorRole() == null) {
            tasks.addAll(getMissingExpectedInvestigatorRoleTasks(user));
        }
        return tasks;
    }

    private List<Task> getMissingExpectedInvestigatorRoleTasks(FirebirdUser user) {
        List<Task> tasks = Lists.newArrayList();
        InvestigatorProfile profile = investigatorProfileService.getByPerson(user.getPerson());
        if (profile != null) {
            Set<AbstractProtocolRegistration> registrations = profile.getCurrentProtocolRegistrations();
            Set<AbstractProtocolRegistration> invitedRegistrations = getInvitedRegistrations(registrations);
            if (containsInvestigatorRegistration(invitedRegistrations)) {
                tasks.add(createMissingExpectedInvestigatorRoleTask(true));
            } else if (containsSubInvestigatorRegistration(invitedRegistrations)) {
                tasks.add(createMissingExpectedInvestigatorRoleTask(false));
            }
        }
        return tasks;
    }

    private boolean containsInvestigatorRegistration(Set<AbstractProtocolRegistration> invitedRegistrations) {
        return CollectionUtils.exists(invitedRegistrations,
                PredicateUtils.instanceofPredicate(InvestigatorRegistration.class));
    }

    private boolean containsSubInvestigatorRegistration(Set<AbstractProtocolRegistration> invitedRegistrations) {
        return CollectionUtils.exists(invitedRegistrations,
                PredicateUtils.instanceofPredicate(SubInvestigatorRegistration.class));
    }

    private Task createMissingExpectedInvestigatorRoleTask(boolean investigatorRegistrationInvites) {
        TaskType taskType;
        if (investigatorRegistrationInvites) {
            taskType = TaskType.MISSING_EXPECTED_INVESTIGATOR_ROLE_WITH_INVESTIGATOR_REGISTRATION_INVITES;
        } else {
            taskType = TaskType.MISSING_EXPECTED_INVESTIGATOR_ROLE_WITH_SUBINVESTIGATOR_REGISTRATION_INVITES;
        }
        Date date = new Date();
        Map<FirebirdTemplateParameter, Object> parameters = Collections.emptyMap();
        String description = templateService.generateString(
                FirebirdStringTemplate.MISSING_EXPECTED_INVESTIGATOR_ROLE_TASK_TITLE, parameters);
        return new Task(description, date, taskType, null);
    }

    /**
     * @param templateService the templateService to set
     */
    @Inject
    void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }

    /**
     * @param protocolRegistrationService the protocolRegistrationService to set
     */
    @Inject
    void setProtocolRegistrationService(ProtocolRegistrationService protocolRegistrationService) {
        this.protocolRegistrationService = protocolRegistrationService;
    }

    /**
     * @param annualRegistrationService the annualRegistrationService to set
     */
    @Inject
    void setAnnualRegistrationService(AnnualRegistrationService annualRegistrationService) {
        this.annualRegistrationService = annualRegistrationService;
    }

    /**
     * @param investigatorProfileService the investigatorProfileService to set
     */
    @Inject
    public void setInvestigatorProfileService(InvestigatorProfileService investigatorProfileService) {
        this.investigatorProfileService = investigatorProfileService;
    }

    @Inject
    void setRegistrationWithdrawalService(RegistrationWithdrawalService registrationWithdrawalService) {
        this.registrationWithdrawalService = registrationWithdrawalService;
    }

    @Inject
    void setSponsorService(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    /**
     * @param sessionProvider the sessionProvider to set
     */
    @Inject
    public void setSessionProvider(Provider<Session> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

}
