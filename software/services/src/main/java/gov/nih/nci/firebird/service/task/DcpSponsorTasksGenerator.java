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
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.RevisedInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.service.messages.FirebirdStringTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;

import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Class for generating tasks for a sponsor user.
 */
class DcpSponsorTasksGenerator {

    private final TemplateService templateService;
    private final ProtocolRegistrationService protocolRegistrationService;
    private final SponsorService sponsorService;

    @Inject
    DcpSponsorTasksGenerator(TemplateService templateService, ProtocolRegistrationService protocolRegistrationService,
            SponsorService sponsorService) {
        super();
        this.templateService = templateService;
        this.protocolRegistrationService = protocolRegistrationService;
        this.sponsorService = sponsorService;
    }

    List<Task> generateTasks(FirebirdUser user, Set<String> groupNames) {
        List<Task> tasks = Lists.newArrayList();
        if (isUserAwaitingSponsorRoleVerification(user, groupNames)) {
            tasks.add(getSponsorRoleVerificationTask());
        }
        if (!user.getSponsorRoles().isEmpty()) {
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
            if (!sponsor.equals(sponsorWithAnnualRegistrations) && !user.hasVerifiedSponsorRole(sponsor, groupNames)) {
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
        List<AbstractProtocolRegistration> registrations = protocolRegistrationService.getByStatusForUser(
                RegistrationStatus.SUBMITTED, user, groupNames);
        return createRegistrationReviewTasks(registrations, FirebirdStringTemplate.REGISTRATION_SUBMITTED_TASK_TITLE);
    }

    private Iterable<Task> createRegistrationReviewTasks(Iterable<AbstractProtocolRegistration> registrations,
            final FirebirdStringTemplate titleTemplate) {
        return Iterables.transform(registrations, new Function<AbstractProtocolRegistration, Task>() {
            @Override
            public Task apply(AbstractProtocolRegistration registration) {
                return createRegistrationReviewTask(registration, titleTemplate);
            }
        });
    }

    private Iterable<Task> getRegistrationInReviewTasks(FirebirdUser user, Set<String> groupNames) {
        List<AbstractProtocolRegistration> registrations = protocolRegistrationService.getByStatusForUser(
                RegistrationStatus.IN_REVIEW, user, groupNames);
        return createRegistrationReviewTasks(registrations, FirebirdStringTemplate.REGISTRATION_IN_REVIEW_TASK_TITLE);
    }

    private Iterable<Task> getRegistrationPacketApprovalTasks(FirebirdUser user, Set<String> groupNames) {
        List<AbstractProtocolRegistration> acceptedRegistrations = protocolRegistrationService.getByStatusForUser(
                RegistrationStatus.ACCEPTED, user, groupNames);
        Iterable<AbstractProtocolRegistration> approvableRegistrations = getApprovableRegistrations(
                acceptedRegistrations, user);
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

}
