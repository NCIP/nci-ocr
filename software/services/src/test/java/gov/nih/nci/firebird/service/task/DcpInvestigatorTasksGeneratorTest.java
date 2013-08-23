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

import static gov.nih.nci.firebird.data.InvitationStatus.*;
import static gov.nih.nci.firebird.data.RegistrationStatus.*;
import static gov.nih.nci.firebird.service.messages.FirebirdStringTemplate.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class DcpInvestigatorTasksGeneratorTest extends AbstractTaskGeneratorTest {

    private static final Set<String> GROUP_NAMES = Sets.newHashSet(UserRoleType.INVESTIGATOR.getGroupName(),
            UserRoleType.INVESTIGATOR.getVerifiedGroupName());

    @Inject
    private DcpInvestigatorTasksGenerator generator;

    private FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator();

    @Test
    public void testGenerateTasks_NotAnInvestigator() throws Exception {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();

        List<Task> tasks = generator.generateTasks(coordinator, null);

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGenerateTasks_InvestigatorRoleNeedsVerification() throws Exception {
        Set<String> groupNames = Sets.newHashSet(UserRoleType.INVESTIGATOR.getGroupName());

        List<Task> tasks = generator.generateTasks(investigator, groupNames);

        checkForOnlyTask(tasks, INVESTIGATOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE);
    }

    @Test
    // Not possible but needed for coverage
    public void testGenerateTasks_InvestigatorWithJustVerifiedGroupName() throws Exception {
        Set<String> groupNames = Sets.newHashSet(UserRoleType.INVESTIGATOR.getVerifiedGroupName());

        List<Task> tasks = generator.generateTasks(investigator, groupNames);

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGenerateTasks_InvestigatorRegistrationInvitation() throws Exception {
        InvestigatorRegistration invitedRegistration = createAndAddInvestigatorRegistration(NOT_STARTED, NO_RESPONSE);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        checkForOnlyTask(tasks, INVESTIGATOR_INVITATION_TASK_TITLE, invitedRegistration);
    }

    private InvestigatorRegistration createAndAddInvestigatorRegistration(RegistrationStatus registrationStatus,
            InvitationStatus invitationStatus) {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                registrationStatus);
        registration.getInvitation().setInvitationStatus(invitationStatus);
        investigator.getInvestigatorRole().getProfile().addRegistration(registration);
        return registration;
    }

    @Test
    public void testGenerateTasks_InactiveInvestigatorRegistrationInvitation() throws Exception {
        createAndAddInvestigatorRegistration(INACTIVE, NO_RESPONSE);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGenerateTasks_SubInvestigatorRegistrationInvitation() throws Exception {
        SubInvestigatorRegistration invitedRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        invitedRegistration.getInvitation().setInvitationStatus(InvitationStatus.NO_RESPONSE);
        investigator.getInvestigatorRole().getProfile().addRegistration(invitedRegistration);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        checkForOnlyTask(tasks, SUBINVESTIGATOR_INVITATION_TASK_TITLE, invitedRegistration);
    }

    @Test
    public void testGenerateTasks_InProgressRegistration() throws Exception {
        InvestigatorRegistration registration = createAndAddInvestigatorRegistration(IN_PROGRESS, RESPONDED);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        checkForOnlyTask(tasks, REGISTRATION_IN_PROGRESS_TASK_TITLE, registration);
    }

    @Test
    public void testGenerateTasks_ReactivatedInProgressRegistration() throws Exception {
        InvestigatorRegistration registration = createAndAddInvestigatorRegistration(IN_PROGRESS, REACTIVATED);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        checkForOnlyTask(tasks, REGISTRATION_REACTIVATED_TASK_TITLE, registration);
    }

    @Test
    public void testGenerateTasks_IncompleteRegistration() throws Exception {
        InvestigatorRegistration registration = createAndAddInvestigatorRegistration(INCOMPLETE, RESPONDED);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        checkForOnlyTask(tasks, REGISTRATION_IN_PROGRESS_TASK_TITLE, registration);
    }

    @Test
    public void testGenerateTasks_ReactivatedIncompleteRegistration() throws Exception {
        InvestigatorRegistration registration = createAndAddInvestigatorRegistration(INCOMPLETE, REACTIVATED);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        checkForOnlyTask(tasks, REGISTRATION_IN_PROGRESS_TASK_TITLE, registration);
    }

    @Test
    public void testGenerateTasks_RegistrationCompleted() throws Exception {
        InvestigatorRegistration registration = createAndAddInvestigatorRegistration(COMPLETED, RESPONDED);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        checkForOnlyTask(tasks, REGISTRATION_READY_FOR_SIGNING_TASK_TITLE, registration);
    }

    @Test
    public void testGenerateTasks_RegistrationReturned() throws Exception {
        InvestigatorRegistration registration = createAndAddInvestigatorRegistration(RETURNED, RESPONDED);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        checkForOnlyTask(tasks, REGISTRATION_RETURNED_TASK_TITLE, registration);
    }

    @Test
    public void testGenerateTasks_ProtocolUpdated() throws Exception {
        InvestigatorRegistration registration = createAndAddInvestigatorRegistration(PROTOCOL_UPDATED, RESPONDED);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        checkForOnlyTask(tasks, PROTOCOL_MODIFIED_TASK_TITLE, registration);
    }

    @Test
    public void testGenerateTasks_UnApprovedCoordinatorInvitation() throws Exception {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(
                investigator.getInvestigatorRole().getProfile());

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        checkForOnlyTask(tasks, REGISTRATION_COORDINATOR_REQUEST_TITLE, coordinator.getPerson());
    }

    @Test
    public void testGenerateTasks_ApprovedCoordinatorInvitation() throws Exception {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(
                investigator.getInvestigatorRole().getProfile());
        managedInvestigator.setStatus(ManagedInvestigatorStatus.APPROVED);

        List<Task> tasks = generator.generateTasks(investigator, GROUP_NAMES);

        assertTrue(tasks.isEmpty());
    }

}
