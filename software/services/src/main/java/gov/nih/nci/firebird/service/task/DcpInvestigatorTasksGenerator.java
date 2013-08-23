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

import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.service.messages.FirebirdStringTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.messages.TemplateService;

import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * Class for generating tasks for an investigator user.
 */
@SuppressWarnings("PMD.TooManyMethods")
// requires many supporting methods to generate different tasks
class DcpInvestigatorTasksGenerator {

    private final TemplateService templateService;

    @Inject
    DcpInvestigatorTasksGenerator(TemplateService templateService) {
        super();
        this.templateService = templateService;
    }

    List<Task> generateTasks(FirebirdUser user, Set<String> groupNames) {
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
            tasks.addAll(getCoordinatorInvitationTasks(user));
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

    private List<Task> getCoordinatorInvitationTasks(FirebirdUser user) {
        List<Task> tasks = Lists.newArrayList();
        for (ManagedInvestigator managedInvestigator : user.getInvestigatorRole().getProfile()
                .getRegistrationCoordinatorMappings()) {
            if (!managedInvestigator.isApproved()) {
                tasks.add(createCoordinatorInvitationTask(managedInvestigator));
            }
        }
        return tasks;
    }

    private Task createCoordinatorInvitationTask(ManagedInvestigator managedInvestigator) {
        Date date = managedInvestigator.getCreateDate();
        Map<FirebirdTemplateParameter, Object> values = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        values.put(FirebirdTemplateParameter.REGISTRATION_COORDINATOR, managedInvestigator.getUser().getPerson());
        String title = templateService.generateString(FirebirdStringTemplate.REGISTRATION_COORDINATOR_REQUEST_TITLE,
                values);
        return new Task(title, date, TaskType.REGISTRATION_COORDINATOR_REQUEST, managedInvestigator.getId());
    }

}
