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

import static gov.nih.nci.firebird.service.messages.FirebirdStringTemplate.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.InvestigatorStatus;
import gov.nih.nci.firebird.data.user.InvestigatorWithdrawalRequest;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus;
import gov.nih.nci.firebird.data.user.RegistrationCoordinatorRoleUtil;
import gov.nih.nci.firebird.data.user.SponsorRole;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationService;
import gov.nih.nci.firebird.service.annual.registration.RegistrationWithdrawalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.messages.FirebirdStringTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Provider;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class TaskServiceBeanTest {

    private Set<String> groupNames;

    private TaskServiceBean bean = new TaskServiceBean();
    @Mock
    private TemplateService mockTemplateService;
    @Mock
    private ProtocolRegistrationService mockProtocolRegistrationService;
    @Mock
    private AnnualRegistrationService mockAnnualRegistrationService;
    @Mock
    private InvestigatorProfileService mockInvestigatorProfileService;
    @Mock
    private RegistrationWithdrawalService mockRegistrationWithdrawalService;
    @Mock
    private SponsorService mockSponsorService;
    @Mock
    private Provider<Session> mockSessionProvider;
    @Mock
    private Session mockSession;
    private String testDescription = "test description for ";
    private Organization sponsorOrg = OrganizationFactory.getInstance().create();

    @Before
    public void setUp() {
        when(mockSessionProvider.get()).thenReturn(mockSession);
        bean.setSessionProvider(mockSessionProvider);
        bean.setTemplateService(mockTemplateService);
        bean.setProtocolRegistrationService(mockProtocolRegistrationService);
        bean.setInvestigatorProfileService(mockInvestigatorProfileService);
        bean.setAnnualRegistrationService(mockAnnualRegistrationService);
        bean.setRegistrationWithdrawalService(mockRegistrationWithdrawalService);
        bean.setSponsorService(mockSponsorService);
        when(mockAnnualRegistrationService.getDaysBeforeDueDateToSendFirstNotification()).thenReturn(60);
        when(mockAnnualRegistrationService.getDaysBeforeDueDateToSendSecondNotification()).thenReturn(30);
        for (FirebirdStringTemplate template : FirebirdStringTemplate.values()) {
            when(mockTemplateService.generateString(eq(template), any(Map.class))).thenReturn(
                    testDescription + template);
        }
        when(mockTemplateService.generateString(eq(REGISTRATION_COORDINATOR_REQUEST_TITLE), any(Map.class)))
                .thenAnswer(new Answer<String>() {
                    @Override
                    public String answer(InvocationOnMock invocation) throws Throwable {
                        Map<FirebirdTemplateParameter, Object> parameterValues = (Map<FirebirdTemplateParameter, Object>) invocation
                                .getArguments()[1];
                        Person param = (Person) parameterValues.get(FirebirdTemplateParameter.REGISTRATION_COORDINATOR);
                        return testDescription + REGISTRATION_COORDINATOR_REQUEST_TITLE + param.toString();
                    }
                });
        groupNames = Sets.newHashSet(UserRoleType.INVESTIGATOR.getGroupName(),
                UserRoleType.INVESTIGATOR.getVerifiedGroupName());
    }

    @Test
    public void testGetTasks() throws InterruptedException {
        FirebirdUser user = new FirebirdUser();
        List<Task> tasks = bean.getTasks(user, groupNames);
        assertTrue(tasks.isEmpty());

        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);
        tasks = bean.getTasks(user, groupNames);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGetTasks_InvestigatorVerificationPending() {
        groupNames.remove(UserRoleType.INVESTIGATOR.getVerifiedGroupName());
        FirebirdUser investigator = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        investigator.createInvestigatorRole(profile);
        List<Task> tasks = bean.getTasks(investigator, groupNames);
        assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        assertEquals(TaskType.INVESTIGATOR_ROLE_AWAITING_VERIFICATION, task.getType());
        assertEquals(testDescription + INVESTIGATOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE, task.getDescription());
    }

    @Test
    public void testGetTasks_RegistrationCoordinatorVerificationPending() {
        FirebirdUser coordinator = new FirebirdUser();
        coordinator.createRegistrationCoordinatorRole();
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(
                InvestigatorProfileFactory.getInstance().create());
        List<Task> tasks = bean.getTasks(coordinator, groupNames);
        Task task = Iterables.getOnlyElement(tasks);
        assertEquals(TaskType.COORDINATOR_ROLE_AWAITING_VERIFICATION, task.getType());
        assertEquals(testDescription + COORDINATOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE, task.getDescription());
    }

    @Test
    public void testGetTasks_InvestigatorTasksValidNumTasks() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        InvestigatorRegistration registrationNoResponse = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationNoResponse.setId(1L);
        registrationNoResponse.getInvitation().setInvitationStatus(InvitationStatus.NO_RESPONSE);
        profile.addRegistration(registrationNoResponse);

        InvestigatorRegistration registrationNoInvite = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationNoInvite.setId(3L);
        registrationNoInvite.getInvitation().setInvitationStatus(InvitationStatus.NOT_INVITED);
        profile.addRegistration(registrationNoInvite);

        InvestigatorRegistration registrationInProgress = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationInProgress.setId(4L);
        registrationInProgress.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        registrationInProgress.setStatus(RegistrationStatus.IN_PROGRESS);
        profile.addRegistration(registrationInProgress);

        InvestigatorRegistration registrationIncomplete = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationIncomplete.setId(5L);
        registrationIncomplete.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        registrationIncomplete.setStatus(RegistrationStatus.INCOMPLETE);
        profile.addRegistration(registrationIncomplete);

        InvestigatorRegistration registrationReturned = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationReturned.setId(6L);
        registrationReturned.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        registrationReturned.setStatus(RegistrationStatus.RETURNED);
        profile.addRegistration(registrationReturned);

        InvestigatorRegistration registrationReactivated = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationReactivated.setId(7L);
        registrationReactivated.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        registrationReactivated.setStatus(RegistrationStatus.IN_PROGRESS);
        profile.addRegistration(registrationReactivated);

        InvestigatorRegistration registrationIncompleteReactivated = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationIncompleteReactivated.setId(8L);
        registrationIncompleteReactivated.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        registrationIncompleteReactivated.setStatus(RegistrationStatus.INCOMPLETE);
        profile.addRegistration(registrationIncompleteReactivated);

        InvestigatorRegistration protocolUpdatedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        protocolUpdatedRegistration.setId(9L);
        protocolUpdatedRegistration.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        protocolUpdatedRegistration.setStatus(RegistrationStatus.PROTOCOL_UPDATED);
        profile.addRegistration(protocolUpdatedRegistration);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(7, tasks.size());
    }

    @Test
    public void testGetTasks_InvestigatorTasksRespondToInvite() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        InvestigatorRegistration registrationNoResponse = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationNoResponse.setId(1L);
        registrationNoResponse.getInvitation().setInvitationStatus(InvitationStatus.NO_RESPONSE);
        profile.addRegistration(registrationNoResponse);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_INVITATION, INVESTIGATOR_INVITATION_TASK_TITLE, registrationNoResponse,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_InvestigatorTasksNotInvited() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        InvestigatorRegistration registrationNoInvite = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationNoInvite.setId(3L);
        registrationNoInvite.getInvitation().setInvitationStatus(InvitationStatus.NOT_INVITED);
        profile.addRegistration(registrationNoInvite);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(0, tasks.size());
    }

    @Test
    public void testGetTasks_InvestigatorTasksInProgress() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        InvestigatorRegistration registrationInProgress = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationInProgress.setId(4L);
        registrationInProgress.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        registrationInProgress.setStatus(RegistrationStatus.IN_PROGRESS);
        profile.addRegistration(registrationInProgress);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_IN_PROGRESS, REGISTRATION_IN_PROGRESS_TASK_TITLE, registrationInProgress,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_InvestigatorTasksIncomplete() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        InvestigatorRegistration registrationIncomplete = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationIncomplete.setId(5L);
        registrationIncomplete.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        registrationIncomplete.setStatus(RegistrationStatus.INCOMPLETE);
        profile.addRegistration(registrationIncomplete);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_IN_PROGRESS, REGISTRATION_IN_PROGRESS_TASK_TITLE, registrationIncomplete,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_InvestigatorTasksReturned() throws InterruptedException {
        InvestigatorRegistration registrationReturned = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationReturned.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        registrationReturned.setStatus(RegistrationStatus.RETURNED);

        List<Task> tasks = bean.getTasks(registrationReturned.getProfile().getUser(), groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_RETURNED, REGISTRATION_RETURNED_TASK_TITLE, registrationReturned, tasks.get(0));
    }

    @Test
    public void testGetTasks_AnnualRegistrationReturned() throws InterruptedException {
        AnnualRegistration registrationReturned = AnnualRegistrationFactory.getInstance().create();
        registrationReturned.setStatus(RegistrationStatus.RETURNED);

        List<Task> tasks = bean.getTasks(registrationReturned.getProfile().getUser(), groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.ANNUAL_REGISTRATION_RETURNED, ANNUAL_REGISTRATION_RETURNED_TASK_TITLE, registrationReturned, tasks.get(0));
    }

    @Test
    public void testGetTasks_InvestigatorTasksReactivatedInProgress() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        InvestigatorRegistration registrationReactivated = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationReactivated.setId(7L);
        registrationReactivated.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        registrationReactivated.setStatus(RegistrationStatus.IN_PROGRESS);
        profile.addRegistration(registrationReactivated);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_IN_PROGRESS, REGISTRATION_REACTIVATED_TASK_TITLE, registrationReactivated,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_InvestigatorTaskInvitedAndDeactivated() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        InvestigatorRegistration deactivatedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        deactivatedRegistration.setId(7L);
        deactivatedRegistration.getInvitation().setInvitationStatus(InvitationStatus.NO_RESPONSE);
        deactivatedRegistration.setStatus(RegistrationStatus.INACTIVE);
        profile.addRegistration(deactivatedRegistration);

        tasks = bean.getTasks(user, groupNames);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGetTasks_InvestigatorTasksReactivatedIncomplete() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        InvestigatorRegistration registrationIncompleteReactivated = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationIncompleteReactivated.setId(8L);
        registrationIncompleteReactivated.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        registrationIncompleteReactivated.setStatus(RegistrationStatus.INCOMPLETE);
        profile.addRegistration(registrationIncompleteReactivated);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_IN_PROGRESS, REGISTRATION_IN_PROGRESS_TASK_TITLE,
                registrationIncompleteReactivated, tasks.get(0));
    }

    @Test
    public void testGetTasks_InvestigatorTasksProtocolUpdated() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        InvestigatorRegistration protocolUpdatedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        protocolUpdatedRegistration.setId(9L);
        protocolUpdatedRegistration.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        protocolUpdatedRegistration.setStatus(RegistrationStatus.PROTOCOL_UPDATED);
        profile.addRegistration(protocolUpdatedRegistration);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_IN_PROGRESS, PROTOCOL_MODIFIED_TASK_TITLE, protocolUpdatedRegistration,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_InvestigatorTasksRegistrationReadyForSigning() throws InterruptedException {
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        registration.setStatus(RegistrationStatus.COMPLETED);
        registration.setId(1L);
        profile.addRegistration(registration);

        List<Task> tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        verifyTask(TaskType.REGISTRATION_READY_FOR_SIGNING,
                FirebirdStringTemplate.REGISTRATION_READY_FOR_SIGNING_TASK_TITLE, registration, task);
    }

    @Test
    public void testGetTasks_SubInvestigatorTasksValidNumTasks() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        SubInvestigatorRegistration subRegistrationNoResponse = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationNoResponse.setId(1L);
        subRegistrationNoResponse.getInvitation().setInvitationStatus(InvitationStatus.NO_RESPONSE);
        profile.addRegistration(subRegistrationNoResponse);

        SubInvestigatorRegistration subRegistrationNoInvite = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationNoInvite.setId(3L);
        subRegistrationNoInvite.getInvitation().setInvitationStatus(InvitationStatus.NOT_INVITED);
        profile.addRegistration(subRegistrationNoInvite);

        SubInvestigatorRegistration subRegistrationInProgress = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationInProgress.setId(4L);
        subRegistrationInProgress.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        subRegistrationInProgress.setStatus(RegistrationStatus.IN_PROGRESS);
        profile.addRegistration(subRegistrationInProgress);

        SubInvestigatorRegistration subRegistrationIncomplete = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationIncomplete.setId(5L);
        subRegistrationIncomplete.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        subRegistrationIncomplete.setStatus(RegistrationStatus.INCOMPLETE);
        profile.addRegistration(subRegistrationIncomplete);

        SubInvestigatorRegistration subRegistrationReturned = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationReturned.setId(6L);
        subRegistrationReturned.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        subRegistrationReturned.setStatus(RegistrationStatus.RETURNED);
        profile.addRegistration(subRegistrationReturned);

        SubInvestigatorRegistration subRegistrationReactivated = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationReactivated.setId(7L);
        subRegistrationReactivated.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        subRegistrationReactivated.setStatus(RegistrationStatus.IN_PROGRESS);
        profile.addRegistration(subRegistrationReactivated);

        SubInvestigatorRegistration subRegIncompleteReactivated = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegIncompleteReactivated.setId(8L);
        subRegIncompleteReactivated.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        subRegIncompleteReactivated.setStatus(RegistrationStatus.INCOMPLETE);
        profile.addRegistration(subRegIncompleteReactivated);

        SubInvestigatorRegistration protocolUpdatedRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        protocolUpdatedRegistration.setId(9L);
        protocolUpdatedRegistration.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        protocolUpdatedRegistration.setStatus(RegistrationStatus.PROTOCOL_UPDATED);
        profile.addRegistration(protocolUpdatedRegistration);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(7, tasks.size());
    }

    @Test
    public void testGetTasks_SubInvestigatorTasksRespondToInvite() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        SubInvestigatorRegistration subRegistrationNoResponse = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationNoResponse.setId(1L);
        subRegistrationNoResponse.getInvitation().setInvitationStatus(InvitationStatus.NO_RESPONSE);
        profile.addRegistration(subRegistrationNoResponse);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_INVITATION, SUBINVESTIGATOR_INVITATION_TASK_TITLE, subRegistrationNoResponse,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_SubInvestigatorTasksNotInvited() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        SubInvestigatorRegistration subRegistrationNoInvite = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationNoInvite.setId(3L);
        subRegistrationNoInvite.getInvitation().setInvitationStatus(InvitationStatus.NOT_INVITED);
        profile.addRegistration(subRegistrationNoInvite);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(0, tasks.size());
    }

    @Test
    public void testGetTasks_SubInvestigatorTasksInProgress() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        SubInvestigatorRegistration subRegistrationInProgress = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationInProgress.setId(4L);
        subRegistrationInProgress.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        subRegistrationInProgress.setStatus(RegistrationStatus.IN_PROGRESS);
        profile.addRegistration(subRegistrationInProgress);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_IN_PROGRESS, REGISTRATION_IN_PROGRESS_TASK_TITLE, subRegistrationInProgress,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_SubInvestigatorTasksIncomplete() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        SubInvestigatorRegistration subRegistrationIncomplete = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationIncomplete.setId(5L);
        subRegistrationIncomplete.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        subRegistrationIncomplete.setStatus(RegistrationStatus.INCOMPLETE);
        profile.addRegistration(subRegistrationIncomplete);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_IN_PROGRESS, REGISTRATION_IN_PROGRESS_TASK_TITLE, subRegistrationIncomplete,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_SubInvestigatorTasksReturned() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        SubInvestigatorRegistration subRegistrationReturned = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationReturned.setId(6L);
        subRegistrationReturned.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        subRegistrationReturned.setStatus(RegistrationStatus.RETURNED);
        profile.addRegistration(subRegistrationReturned);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_RETURNED, REGISTRATION_RETURNED_TASK_TITLE, subRegistrationReturned,
                tasks.get(0));
    }

    @Test
    public void testGetTasksSubInvestigatorTasks_ReactivatedInProgress() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        SubInvestigatorRegistration subRegistrationReactivated = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegistrationReactivated.setId(7L);
        subRegistrationReactivated.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        subRegistrationReactivated.setStatus(RegistrationStatus.IN_PROGRESS);
        profile.addRegistration(subRegistrationReactivated);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_IN_PROGRESS, REGISTRATION_REACTIVATED_TASK_TITLE, subRegistrationReactivated,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_SubInvestigatorTasksReactivatedIncomplete() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        SubInvestigatorRegistration subRegIncompleteReactivated = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subRegIncompleteReactivated.setId(8L);
        subRegIncompleteReactivated.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        subRegIncompleteReactivated.setStatus(RegistrationStatus.INCOMPLETE);
        profile.addRegistration(subRegIncompleteReactivated);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_IN_PROGRESS, REGISTRATION_IN_PROGRESS_TASK_TITLE, subRegIncompleteReactivated,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_SubInvestigatorTasksProtocolUpdated() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        SubInvestigatorRegistration protocolUpdatedRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        protocolUpdatedRegistration.setId(9L);
        protocolUpdatedRegistration.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        protocolUpdatedRegistration.setStatus(RegistrationStatus.PROTOCOL_UPDATED);
        profile.addRegistration(protocolUpdatedRegistration);

        tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());

        verifyTask(TaskType.REGISTRATION_IN_PROGRESS, PROTOCOL_MODIFIED_TASK_TITLE, protocolUpdatedRegistration,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_SponsorTasksSubmittedRegistration() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser sponsor = new FirebirdUser();
        SponsorRole role = sponsor.addSponsorRepresentativeRole(sponsorOrg);
        Set<String> groupNames = Sets.newHashSet(role.getVerifiedSponsorGroupName());

        AbstractProtocolRegistration registrationSubmitted = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationSubmitted.setId(6L);
        registrationSubmitted.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        registrationSubmitted.setStatus(RegistrationStatus.SUBMITTED);

        List<AbstractProtocolRegistration> registrationList = Lists.newArrayList(registrationSubmitted);
        when(mockProtocolRegistrationService.getByStatusForUser(RegistrationStatus.SUBMITTED, sponsor, groupNames)).thenReturn(registrationList);

        tasks = bean.getTasks(sponsor, groupNames);
        assertEquals(1, tasks.size());
        verifyTask(TaskType.REGISTRATION_FOR_REVIEW, REGISTRATION_SUBMITTED_TASK_TITLE, registrationSubmitted,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_SponsorTasksSubmittedAnnualRegistration() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        List<Task> tasks = getSponsorAnnualRegistrationTasks(registration);
        assertEquals(1, tasks.size());
        verifyTask(TaskType.ANNUAL_REGISTRATION_FOR_REVIEW, ANNUAL_REGISTRATION_SUBMITTED_TASK_TITLE, registration,
                tasks.get(0));
    }

    private List<Task> getSponsorAnnualRegistrationTasks(AnnualRegistration registration) {
        FirebirdUser sponsor = new FirebirdUser();
        Set<String> groupNames = Sets.newHashSet(UserRoleType.CTEP_SPONSOR.getGroupName());
        when(mockAnnualRegistrationService.getByStatus(registration.getStatus())).thenReturn(Lists.newArrayList(registration));
        return bean.getTasks(sponsor, groupNames);
    }

    private void verifyTask(TaskType type, FirebirdStringTemplate desc, AnnualRegistration registration, Task task) {
        Date date = registration.getStatusDate();
        verifyTask(type, testDescription + desc, date, registration.getId(), task);
    }

    @Test
    public void testGetTasks_SponsorTasksInReviewAnnualRegistration() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();
        registration.setStatus(RegistrationStatus.IN_REVIEW);
        List<Task> tasks = getSponsorAnnualRegistrationTasks(registration);
        assertEquals(1, tasks.size());
        verifyTask(TaskType.ANNUAL_REGISTRATION_FOR_REVIEW, ANNUAL_REGISTRATION_IN_REVIEW_TASK_TITLE, registration,
                tasks.get(0));
    }

    @Test
    public void testGetTasks_SponsorTasksInReviewRegistration() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser sponsor = new FirebirdUser();
        SponsorRole role = sponsor.addSponsorRepresentativeRole(sponsorOrg);
        Set<String> groupNames = Sets.newHashSet(role.getVerifiedSponsorGroupName());

        AbstractProtocolRegistration registrationInReview = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationInReview.setId(8L);
        registrationInReview.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        registrationInReview.setStatus(RegistrationStatus.IN_REVIEW);

        List<AbstractProtocolRegistration> registrationList = Lists.newArrayList(registrationInReview);
        when(mockProtocolRegistrationService.getByStatusForUser(RegistrationStatus.IN_REVIEW, sponsor, groupNames)).thenReturn(registrationList);

        tasks = bean.getTasks(sponsor, groupNames);
        assertEquals(1, tasks.size());
        verifyTask(TaskType.REGISTRATION_FOR_REVIEW, FirebirdStringTemplate.REGISTRATION_IN_REVIEW_TASK_TITLE,
                registrationInReview, tasks.get(0));
    }

    @Test
    public void testGetTasks_SponsorTasksAcceptedRegistration() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser sponsor = new FirebirdUser();
        SponsorRole role = sponsor.addSponsorRepresentativeRole(sponsorOrg);
        Set<String> groupNames = Sets.newHashSet(role.getVerifiedSponsorGroupName());
        AbstractProtocolRegistration registration = createAcceptedRegistration(sponsor, groupNames);
        tasks = bean.getTasks(sponsor, groupNames);
        assertEquals(1, tasks.size());
        verifyTask(TaskType.REGISTRATION_PACKET_AWAITING_APPROVAL,
                FirebirdStringTemplate.REGISTRATION_PACKET_AWAITING_APPROVAL_TITLE,
                registration, tasks.get(0));

    }

    private AbstractProtocolRegistration createAcceptedRegistration(FirebirdUser sponsor, Set<String> groupNames) {
        AbstractProtocolRegistration registrationAccepted = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        registrationAccepted.setId(7L);
        registrationAccepted.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        registrationAccepted.setStatus(RegistrationStatus.ACCEPTED);
        registrationAccepted.getProtocol().setSponsor(sponsorOrg);
        List<AbstractProtocolRegistration> registrationList = Lists.newArrayList(registrationAccepted);
        when(mockProtocolRegistrationService.getByStatusForUser(RegistrationStatus.ACCEPTED, sponsor, groupNames)).thenReturn(registrationList);
        return registrationAccepted;
    }

    @Test
    public void testGetTasks_SponsorDelegateTasksAcceptedRegistration() throws InterruptedException {
        List<Task> tasks = null;
        FirebirdUser sponsor = new FirebirdUser();
        SponsorRole role = sponsor.addSponsorDelegateRole(sponsorOrg);
        Set<String> groupNames = Sets.newHashSet(role.getVerifiedSponsorGroupName());
        createAcceptedRegistration(sponsor, groupNames);
        tasks = bean.getTasks(sponsor, groupNames);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGetTasks_SponsorVerificationPending() {
        FirebirdUser sponsor = new FirebirdUser();
        sponsor.addSponsorRepresentativeRole(sponsorOrg);
        List<Task> tasks = bean.getTasks(sponsor, groupNames);
        assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        assertEquals(TaskType.SPONSOR_ROLE_AWAITING_VERIFICATION, task.getType());
        assertEquals(testDescription + SPONSOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE, task.getDescription());
    }

    @Test
    public void testGetTasks_SponsorVerificationPending_AnnualRegistrationSponsor() {
        FirebirdUser sponsor = new FirebirdUser();
        sponsor.addSponsorRepresentativeRole(sponsorOrg);
        when(mockSponsorService.getSponsorOrganizationWithAnnualRegistrations()).thenReturn(sponsorOrg);
        List<Task> tasks = bean.getTasks(sponsor, groupNames);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGetTasks_RegistrationCoordinatorTasksValidNumberOfTasks() throws InterruptedException {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator("john", profile);
        FirebirdUser coordinatorTrusted = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        FirebirdUser coordinatorNew = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        ManagedInvestigator roleTrusted = coordinatorTrusted.getRegistrationCoordinatorRole().addManagedInvestigator(
                profile);
        roleTrusted.setStatus(ManagedInvestigatorStatus.APPROVED);
        RegistrationCoordinatorRoleUtil.setId(roleTrusted, 1L);
        ManagedInvestigator roleNew = coordinatorNew.getRegistrationCoordinatorRole().addManagedInvestigator(profile);
        RegistrationCoordinatorRoleUtil.setId(roleNew, 2L);

        List<Task> tasks = bean.getTasks(investigator, groupNames);
        assertEquals(1, tasks.size());
    }

    @Test
    public void testGetTasks_RegistrationCoordinatorTasksRegistrationCoordinatorRequest() throws InterruptedException {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator("john", profile);
        FirebirdUser coordinatorTrusted = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        FirebirdUser coordinatorNew = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        ManagedInvestigator managedInvestigatorTrusted = coordinatorTrusted.getRegistrationCoordinatorRole()
                .addManagedInvestigator(profile);
        managedInvestigatorTrusted.setStatus(ManagedInvestigatorStatus.APPROVED);
        RegistrationCoordinatorRoleUtil.setId(managedInvestigatorTrusted, 1L);
        ManagedInvestigator managedInvestigatorNew = coordinatorNew.getRegistrationCoordinatorRole()
                .addManagedInvestigator(profile);
        RegistrationCoordinatorRoleUtil.setId(managedInvestigatorNew, 2L);

        List<Task> tasks = bean.getTasks(investigator, groupNames);
        assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        String expectedDescription = testDescription + REGISTRATION_COORDINATOR_REQUEST_TITLE
                + coordinatorNew.getPerson().toString();
        verifyTask(TaskType.REGISTRATION_COORDINATOR_REQUEST, expectedDescription,
                managedInvestigatorNew.getCreateDate(), 2L, task);
    }

    @Test
    public void testGetTasks_ExpectedInvestigatorRoleWithInvestigatorRegistrationInvites() throws InterruptedException {
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        when(mockInvestigatorProfileService.getByPerson(user.getPerson())).thenReturn(profile);
        InvestigatorRegistration registrationNoResponse = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        registrationNoResponse.getInvitation().setInvitationStatus(InvitationStatus.NO_RESPONSE);
        profile.addRegistration(registrationNoResponse);

        List<Task> tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());
        assertEquals(TaskType.MISSING_EXPECTED_INVESTIGATOR_ROLE_WITH_INVESTIGATOR_REGISTRATION_INVITES, tasks.get(0)
                .getType());
        assertEquals(testDescription + MISSING_EXPECTED_INVESTIGATOR_ROLE_TASK_TITLE, tasks.get(0).getDescription());
    }

    @Test
    public void testGetTasks_ExpectedInvestigatorRoleWithSubinvestigatorRegistrationInvites()
            throws InterruptedException {
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        when(mockInvestigatorProfileService.getByPerson(user.getPerson())).thenReturn(profile);
        SubInvestigatorRegistration registrationNoResponse = RegistrationFactory.getInstanceWithId()
                .createSubinvestigatorRegistration();
        registrationNoResponse.getInvitation().setInvitationStatus(InvitationStatus.NO_RESPONSE);
        profile.addRegistration(registrationNoResponse);

        List<Task> tasks = bean.getTasks(user, groupNames);
        assertEquals(1, tasks.size());
        assertEquals(TaskType.MISSING_EXPECTED_INVESTIGATOR_ROLE_WITH_SUBINVESTIGATOR_REGISTRATION_INVITES, tasks
                .get(0).getType());
        assertEquals(testDescription + MISSING_EXPECTED_INVESTIGATOR_ROLE_TASK_TITLE, tasks.get(0).getDescription());
    }

    private void verifyTask(TaskType type, FirebirdStringTemplate desc, AbstractProtocolRegistration reg, Task task) {
        Date date = reg.getStatusDate();
        if (TaskType.REGISTRATION_INVITATION.equals(type)) {
            date = reg.getInvitation().getInvitationChangeDate();
        }
        verifyTask(type, testDescription + desc, date, reg.getId(), task);
    }

    private void verifyTask(TaskType type, String desc, Date added, Long targetId, Task task) {
        assertEquals(targetId, task.getTargetId());
        assertEquals(desc, task.getDescription());
        assertEquals(added, task.getDateAdded());
        assertEquals(type, task.getType());
    }

    @Test
    public void testGetTasks_CoordinatorRegistrationRenewal() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(profile)
                .setStatus(ManagedInvestigatorStatus.APPROVED);
        AnnualRegistration registrationWithDueDate = addAnnualRegistration(profile,
                DateUtils.addDays(new Date(), 60 - 1));
        addAnnualRegistration(profile, null); // without due date
        AnnualRegistration registrationAlreadySubmitted = addAnnualRegistration(profile,
                DateUtils.addDays(new Date(), 60 - 1));
        registrationAlreadySubmitted.setStatus(RegistrationStatus.SUBMITTED);

        Set<String> groupNames = Collections.emptySet();
        List<Task> tasks = bean.getTasks(coordinator, groupNames);
        Map<FirebirdTemplateParameter, Object> parameters = Maps.newHashMap();
        parameters.put(FirebirdTemplateParameter.ANNUAL_REGISTRATION, registrationWithDueDate);
        verify(mockTemplateService).generateString(FirebirdStringTemplate.COORDINATOR_REGISTRATION_RENEWAL_TASK_TITLE,
                parameters);
        Task task = Iterables.getOnlyElement(tasks);

        verifyTask(TaskType.REGISTRATION_RENEWAL, testDescription + COORDINATOR_REGISTRATION_RENEWAL_TASK_TITLE,
                DateUtils.addDays(registrationWithDueDate.getDueDate(), -60), registrationWithDueDate.getId(), task);
    }

    private AnnualRegistration addAnnualRegistration(InvestigatorProfile profile, Date dueDate) {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        registration.setDueDate(dueDate);
        profile.addRegistration(registration);
        return registration;
    }

    @Test
    public void testGetTasks_InvestigatorRegistrationRenewal() {
        FirebirdUser user = new FirebirdUser();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);

        AnnualRegistration registrationWithinWindow = addAnnualRegistration(profile,
                DateUtils.addDays(new Date(), 30 - 1));
        addAnnualRegistration(profile, DateUtils.addDays(new Date(), 30 + 1));
        AnnualRegistration submittedRegistration = addAnnualRegistration(profile, DateUtils.addDays(new Date(), 30 - 1));
        submittedRegistration.setStatus(RegistrationStatus.SUBMITTED);

        List<Task> tasks = bean.getTasks(user, groupNames);
        Task task = Iterables.getOnlyElement(tasks);

        verifyTask(TaskType.REGISTRATION_RENEWAL, testDescription + INVESTIGATOR_REGISTRATION_RENEWAL_TASK_TITLE,
                DateUtils.addDays(registrationWithinWindow.getDueDate(), -30), registrationWithinWindow.getId(), task);
    }

    @Test
    public void testGetTasks_AnnualRegistrationReadyForSigning() {
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator();
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        registration.setStatus(RegistrationStatus.COMPLETED);
        investigator.getInvestigatorRole().getProfile().addRegistration(registration);
        List<Task> tasks = bean.getTasks(investigator, groupNames);
        Task task = Iterables.getOnlyElement(tasks);
        verifyTask(TaskType.ANNUAL_REGISTRATION_READY_FOR_SIGNING, ANNUAL_REGISTRATION_READY_FOR_SIGNING_TASK_TITLE,
                registration, task);
    }

    @Test
    public void testGetTasks_AnnualRegistrationApprovedForInvestigator_UnAcknowledged() {
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator();
        AnnualRegistration registration = addRegistration(investigator, false);
        List<Task> tasks = bean.getTasks(investigator, groupNames);
        Task task = Iterables.getOnlyElement(tasks);
        verifyTask(TaskType.ANNUAL_REGISTRATION_APPROVED, ANNUAL_REGISTRATION_APPROVED_TASK_FOR_INVESTIGATOR_TITLE,
                registration, task);
    }

    private AnnualRegistration addRegistration(FirebirdUser investigator, boolean approvalAcknowledged) {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        registration.setStatus(RegistrationStatus.APPROVED);
        registration.setApprovalAcknowledgedByInvestigator(approvalAcknowledged);
        registration.setApprovalAcknowledgedByCoordinator(approvalAcknowledged);
        investigator.getInvestigatorRole().getProfile().addRegistration(registration);
        return registration;
    }

    @Test
    public void testGetTasks_AnnualRegistrationApprovedForInvestigator_Acknowledged() {
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator();
        addRegistration(investigator, true);
        List<Task> tasks = bean.getTasks(investigator, groupNames);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGetTasks_AnnualRegistrationApprovedForCoordinator_UnAcknowledged() {
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator();
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.getRegistrationCoordinatorRole()
                .addManagedInvestigator(investigator.getInvestigatorRole().getProfile())
                .setStatus(ManagedInvestigatorStatus.APPROVED);
        AnnualRegistration registration = addRegistration(investigator, false);
        List<Task> tasks = bean.getTasks(coordinator, groupNames);
        Task task = Iterables.getOnlyElement(tasks);
        verifyTask(TaskType.ANNUAL_REGISTRATION_APPROVED, ANNUAL_REGISTRATION_APPROVED_TASK_FOR_COORDINATOR_TITLE,
                registration, task);
    }

    @Test
    public void testGetTasks_AnnualRegistrationApprovedForCoordinator_Acknowledged() {
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator();
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.getRegistrationCoordinatorRole()
                .addManagedInvestigator(investigator.getInvestigatorRole().getProfile())
                .setStatus(ManagedInvestigatorStatus.APPROVED);
        addRegistration(investigator, true);
        List<Task> tasks = bean.getTasks(coordinator, groupNames);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGetTasks_WithdrawalRequest() {
        FirebirdUser sponsor = new FirebirdUser();
        Set<String> groupNames = Sets.newHashSet(UserRoleType.CTEP_SPONSOR.getGroupName());
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator();
        investigator.setId(1L);
        investigator.getInvestigatorRole().setWithdrawalRequest(new InvestigatorWithdrawalRequest("I'm done"));
        when(mockRegistrationWithdrawalService.getAllInvestigatorsWithPendingWithdrawalRequests()).thenReturn(
                Lists.newArrayList(investigator));
        List<Task> tasks = bean.getTasks(sponsor, groupNames);
        Task task = Iterables.getOnlyElement(tasks);
        assertEquals(Long.valueOf(1L), task.getTargetId());
        assertEquals(testDescription + REQUEST_FOR_WITHDRAWAL_TASK_TITLE, task.getDescription());
        assertEquals(TaskType.REGISTRATION_WITHDRAW_REQUEST, task.getType());
    }

    @Test
    public void testGetTasks_InvestigatorSuspendedNotification_Unacknowledged() {
        FirebirdUser investigator = createSuspendedInvestigator(false);
        Set<String> groupNames = Sets.newHashSet(UserRoleType.CTEP_INVESTIGATOR.getGroupName());
        List<Task> tasks = bean.getTasks(investigator, groupNames);
        Task task = Iterables.getOnlyElement(tasks);
        assertEquals(testDescription + CTEP_INVESTIGATOR_SUSPENEDED_TASK_TITLE_FOR_INVESTIGATOR, task.getDescription());
        assertEquals(TaskType.INVESTIGATOR_SUSPENSION_NOTICE, task.getType());
        assertEquals(investigator.getInvestigatorRole().getProfile().getCurrentAnnualRegistration().getId(),
                task.getTargetId());
    }

    private FirebirdUser createSuspendedInvestigator(boolean acknowledged) {
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator();
        investigator.setCtepUser(true);
        investigator.getInvestigatorRole().setStatus(InvestigatorStatus.SUSPENDED);
        investigator.getInvestigatorRole().setStatusChangeNotificationRequiredForCoordinator(!acknowledged);
        investigator.getInvestigatorRole().setStatusChangeNotificationRequiredForInvestigator(!acknowledged);
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();
        investigator.getInvestigatorRole().getProfile().addRegistration(registration);
        return investigator;
    }

    @Test
    public void testGetTasks_InvestigatorSuspendedNotification_Acknowledged() {
        FirebirdUser investigator = createSuspendedInvestigator(true);
        investigator.getInvestigatorRole().setStatusChangeNotificationRequiredForInvestigator(false);
        Set<String> groupNames = Sets.newHashSet(UserRoleType.CTEP_INVESTIGATOR.getGroupName());
        List<Task> tasks = bean.getTasks(investigator, groupNames);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGetTasks_InvestigatorSuspendedNotificationForCoordinator_Unacknowledged() {
        FirebirdUser coordinator = createCoordinatorForSuspendedInvestigator(false);
        Set<String> groupNames = Sets.newHashSet(UserRoleType.CTEP_REGISTRATION_COORDINATOR.getGroupName());
        List<Task> tasks = bean.getTasks(coordinator, groupNames);
        Task task = Iterables.getOnlyElement(tasks);
        assertEquals(testDescription + CTEP_INVESTIGATOR_SUSPENEDED_TASK_TITLE_FOR_COORDINATOR, task.getDescription());
        assertEquals(TaskType.INVESTIGATOR_SUSPENSION_NOTICE, task.getType());
        InvestigatorProfile profile = coordinator.getRegistrationCoordinatorRole().getManagedProfiles().get(0);
        assertEquals(profile.getCurrentAnnualRegistration().getId(), task.getTargetId());
    }

    private FirebirdUser createCoordinatorForSuspendedInvestigator(boolean acknowledged) {
        FirebirdUser investigator = createSuspendedInvestigator(acknowledged);
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.setCtepUser(true);
        ManagedInvestigator coordinatorRole = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(
                investigator.getInvestigatorRole().getProfile());
        coordinatorRole.setStatus(ManagedInvestigatorStatus.APPROVED);
        return coordinator;
    }

    @Test
    public void testGetTasks_InvestigatorSuspendedNotificationForCoordinator_Acknowledged() {
        FirebirdUser coordinator = createCoordinatorForSuspendedInvestigator(true);
        Set<String> groupNames = Sets.newHashSet(UserRoleType.CTEP_REGISTRATION_COORDINATOR.getGroupName());
        List<Task> tasks = bean.getTasks(coordinator, groupNames);
        assertTrue(tasks.isEmpty());
    }

    // ----

    @Test
    public void testGetTasks_InvestigatorDisqualifiedNotification_Unacknowledged() {
        FirebirdUser investigator = createDisqualifiedInvestigator(false);
        Set<String> groupNames = Sets.newHashSet(UserRoleType.CTEP_INVESTIGATOR.getGroupName());
        List<Task> tasks = bean.getTasks(investigator, groupNames);
        Task task = Iterables.getOnlyElement(tasks);
        assertEquals(testDescription + CTEP_INVESTIGATOR_DISQUALIFIED_TASK_TITLE_FOR_INVESTIGATOR,
                task.getDescription());
        assertEquals(TaskType.INVESTIGATOR_DISQUALIFICATION_NOTICE, task.getType());
        assertEquals(investigator.getInvestigatorRole().getProfile().getCurrentAnnualRegistration().getId(),
                task.getTargetId());
    }

    private FirebirdUser createDisqualifiedInvestigator(boolean acknowledged) {
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator();
        investigator.setCtepUser(true);
        investigator.getInvestigatorRole().disqualifyInvestigator("reason");
        investigator.getInvestigatorRole().getDisqualificationReason().setAcknowledgedByInvestigator(acknowledged);
        investigator.getInvestigatorRole().getDisqualificationReason().setAcknowledgedByCoordinator(acknowledged);
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();
        investigator.getInvestigatorRole().getProfile().addRegistration(registration);
        return investigator;
    }

    @Test
    public void testGetTasks_InvestigatorDisqualifiedNotification_Acknowledged() {
        FirebirdUser investigator = createDisqualifiedInvestigator(true);
        Set<String> groupNames = Sets.newHashSet(UserRoleType.CTEP_INVESTIGATOR.getGroupName());
        List<Task> tasks = bean.getTasks(investigator, groupNames);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGetTasks_InvestigatorDisqualifiedNotificationForCoordinator_Unacknowledged() {
        FirebirdUser coordinator = createCoordinatorForDisqualifiedInvestigator(false);
        Set<String> groupNames = Sets.newHashSet(UserRoleType.CTEP_REGISTRATION_COORDINATOR.getGroupName());
        List<Task> tasks = bean.getTasks(coordinator, groupNames);
        Task task = Iterables.getOnlyElement(tasks);
        assertEquals(testDescription + CTEP_INVESTIGATOR_DISQUALIFIED_TASK_TITLE_FOR_COORDINATOR, task.getDescription());
        assertEquals(TaskType.INVESTIGATOR_DISQUALIFICATION_NOTICE, task.getType());
        InvestigatorProfile profile = coordinator.getRegistrationCoordinatorRole().getManagedProfiles().get(0);
        assertEquals(profile.getCurrentAnnualRegistration().getId(), task.getTargetId());
    }

    private FirebirdUser createCoordinatorForDisqualifiedInvestigator(boolean acknowledged) {
        FirebirdUser investigator = createDisqualifiedInvestigator(acknowledged);
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.setCtepUser(true);
        ManagedInvestigator coordinatorRole = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(
                investigator.getInvestigatorRole().getProfile());
        coordinatorRole.setStatus(ManagedInvestigatorStatus.APPROVED);
        return coordinator;
    }

    @Test
    public void testGetTasks_InvestigatorDisqualifiedNotificationForCoordinator_Acknowledged() {
        FirebirdUser coordinator = createCoordinatorForDisqualifiedInvestigator(true);
        Set<String> groupNames = Sets.newHashSet(UserRoleType.CTEP_REGISTRATION_COORDINATOR.getGroupName());
        List<Task> tasks = bean.getTasks(coordinator, groupNames);
        assertTrue(tasks.isEmpty());
    }

}
