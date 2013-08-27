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
package gov.nih.nci.firebird.web.action;

import static gov.nih.nci.firebird.service.task.TaskType.*;
import gov.nih.nci.firebird.service.task.Task;
import gov.nih.nci.firebird.service.task.TaskService;
import gov.nih.nci.firebird.service.task.TaskType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.google.inject.Inject;

/**
 * This action retrieves Tasks for the current user and formats a JSON result for task display.
 */
@Namespace("/")
public class TaskListAction extends FirebirdActionSupport {

    private static final long serialVersionUID = 1L;
    private final TaskService taskService;
    private List<TaskRow> taskRows;

    /**
     * Creates an instance.
     *
     * @param taskService TaskService instance
     */
    @Inject
    public TaskListAction(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Provides a reference to the JSON result.
     *
     * @return success
     */
    @Action(value = "index", results = @Result(location = "index.jsp"))
    @Override
    public String execute() {
        loadTaskRows(taskService.getTasks(getCurrentUser(), getCurrentUserGroupNames()));
        return SUCCESS;
    }

    /**
     * @return prepare JSON result.
     * @throws JSONException due to serialization problem.
     */
    public String getTaskRowsJSON() throws JSONException {
        return JSONUtil.serialize(getTaskRows());
    }

    /**
     * @return prepare list of the task rows.
     */
    public List<TaskRow> getTaskRows() {
        return taskRows;
    }

    private void loadTaskRows(List<Task> tasks) {
        taskRows = new ArrayList<TaskRow>(tasks.size());
        for (Task task : tasks) {
            taskRows.add(new TaskRow(task));
        }
    }

    /**
     * Task model for use in JSON result.
     */
    @SuppressWarnings("PMD.CyclomaticComplexity")
    // Switch statement expected to have multiple paths
    public final class TaskRow implements Serializable {

        private static final long serialVersionUID = 1L;
        private final Date dateAdded;
        private final String description;
        private final String taskActionUrl;
        private final Long taskActionTargetId;
        private final boolean modal;
        private final EnumSet<TaskType> modalTasks = EnumSet.of(REGISTRATION_INVITATION,
                REGISTRATION_COORDINATOR_REQUEST, SPONSOR_ROLE_AWAITING_VERIFICATION,
                INVESTIGATOR_ROLE_AWAITING_VERIFICATION,
                MISSING_EXPECTED_INVESTIGATOR_ROLE_WITH_INVESTIGATOR_REGISTRATION_INVITES,
                MISSING_EXPECTED_INVESTIGATOR_ROLE_WITH_SUBINVESTIGATOR_REGISTRATION_INVITES,
                ANNUAL_REGISTRATION_APPROVED, REGISTRATION_WITHDRAW_REQUEST);
        private final EnumSet<TaskType> registrationTasks = EnumSet.of(REGISTRATION_INVITATION,
                REGISTRATION_IN_PROGRESS, REGISTRATION_RETURNED, REGISTRATION_READY_FOR_SIGNING);
        private final EnumSet<TaskType> annualRegistrationTasks = EnumSet.of(REGISTRATION_RENEWAL,
                ANNUAL_REGISTRATION_READY_FOR_SIGNING, ANNUAL_REGISTRATION_APPROVED, INVESTIGATOR_SUSPENSION_NOTICE,
                INVESTIGATOR_DISQUALIFICATION_NOTICE, ANNUAL_REGISTRATION_RETURNED);
        private final EnumSet<TaskType> protocolTasks = EnumSet.of(REGISTRATION_FOR_REVIEW,
                REGISTRATION_PACKET_AWAITING_APPROVAL);
        private final EnumSet<TaskType> sponsorAnnualRegistrationTasks = EnumSet.of(ANNUAL_REGISTRATION_FOR_REVIEW);
        private final EnumSet<TaskType> settingsTasks = EnumSet.of(REGISTRATION_COORDINATOR_REQUEST);
        private final EnumSet<TaskType> userAccountTasks = EnumSet.of(SPONSOR_ROLE_AWAITING_VERIFICATION,
                INVESTIGATOR_ROLE_AWAITING_VERIFICATION,
                MISSING_EXPECTED_INVESTIGATOR_ROLE_WITH_INVESTIGATOR_REGISTRATION_INVITES,
                MISSING_EXPECTED_INVESTIGATOR_ROLE_WITH_SUBINVESTIGATOR_REGISTRATION_INVITES);
        private final EnumSet<TaskType> coordinatorTasks = EnumSet.of(COORDINATOR_ROLE_AWAITING_VERIFICATION);
        private final EnumSet<TaskType> registrationWithdrawalTasks = EnumSet.of(REGISTRATION_WITHDRAW_REQUEST);

        private static final String REGISTRATION_ACTION_NAMESPACE = "/investigator/registration";
        private static final String INVESTIGATOR_ANNUAL_REGISTRATION_ACTION_NAMESPACE =
                                                        "/investigator/annual/registration";
        private static final String PROTOCOL_ACTION_NAMESPACE = "/sponsor/protocol";
        private static final String SPONSOR_ANNUAL_REGISTRATION_ACTION_NAMESPACE = "/sponsor/annual/registration";
        private static final String REGISTRATION_WITHDRAWAL_ACTION_NAMESPACE = "/sponsor/annual/investigator";
        private static final String SETTINGS_ACTION_NAMESPACE = "/investigator/settings";
        private static final String USER_ACCOUNT_ACTION_NAMESPACE = "/user";
        private static final String AJAX_ACTION_NAMESPACE = "/ajax";
        private static final String COORDINATOR_ACTION_NAMESPACE = "/coordinator";

        TaskRow(Task task) {
            dateAdded = task.getDateAdded();
            description = task.getDescription();
            taskActionTargetId = task.getTargetId();
            taskActionUrl = buildUrl(task.getType());
            modal = modalTasks.contains(task.getType());
        }

        private String buildUrl(TaskType type) {
            StringBuilder url = new StringBuilder();
            getUrlRoot(url, type);

            if (modalTasks.contains(type)) {
                url.append(AJAX_ACTION_NAMESPACE);
            }
            getUrlAction(url, type);
            return url.toString();
        }

        private void getUrlRoot(StringBuilder builder, TaskType type) {
            if (registrationTasks.contains(type)) {
                builder.append(REGISTRATION_ACTION_NAMESPACE);
            } else if (annualRegistrationTasks.contains(type)) {
                builder.append(INVESTIGATOR_ANNUAL_REGISTRATION_ACTION_NAMESPACE);
            } else if (protocolTasks.contains(type)) {
                builder.append(PROTOCOL_ACTION_NAMESPACE);
            } else if (settingsTasks.contains(type)) {
                builder.append(SETTINGS_ACTION_NAMESPACE);
            } else if (userAccountTasks.contains(type)) {
                builder.append(USER_ACCOUNT_ACTION_NAMESPACE);
            } else if (coordinatorTasks.contains(type)) {
                builder.append(COORDINATOR_ACTION_NAMESPACE);
            } else if (sponsorAnnualRegistrationTasks.contains(type)) {
                builder.append(SPONSOR_ANNUAL_REGISTRATION_ACTION_NAMESPACE);
            } else if (registrationWithdrawalTasks.contains(type)) {
                builder.append(REGISTRATION_WITHDRAWAL_ACTION_NAMESPACE);
            }
        }

        @SuppressWarnings("PMD.ExcessiveMethodLength")
        // switch needs to account for all types
        private void getUrlAction(StringBuilder builder, TaskType type) {
            switch (type) {
            case REGISTRATION_INVITATION:
                builder.append("/viewInvitation.action?id=").append(taskActionTargetId);
                break;
            case REGISTRATION_IN_PROGRESS:
            case REGISTRATION_READY_FOR_SIGNING:
            case REGISTRATION_RETURNED:
                builder.append("/enterRegistrations.action?id=").append(taskActionTargetId);
                break;
            case REGISTRATION_RENEWAL:
            case INVESTIGATOR_SUSPENSION_NOTICE:
            case INVESTIGATOR_DISQUALIFICATION_NOTICE:
            case ANNUAL_REGISTRATION_READY_FOR_SIGNING:
            case ANNUAL_REGISTRATION_RETURNED:
                builder.append("/enterViewRegistration.action?registration.id=").append(taskActionTargetId);
                break;
            case ANNUAL_REGISTRATION_APPROVED:
                builder.append("/enterViewRegistrationApprovedNotification.action?registration.id=").append(
                        taskActionTargetId);
                break;
            case REGISTRATION_FOR_REVIEW:
                builder.append("/review/enter.action?registration.id=").append(taskActionTargetId);
                break;
            case REGISTRATION_PACKET_AWAITING_APPROVAL:
                builder.append("/review/enter.action?registration.id=").append(taskActionTargetId + "&selectedTab=0");
                break;
            case REGISTRATION_COORDINATOR_REQUEST:
                builder.append("/viewCoordinatorRequest.action?managedInvestigatorId=").append(taskActionTargetId);
                break;
            case SPONSOR_ROLE_AWAITING_VERIFICATION:
                builder.append("/sponsorVerificationPending.action");
                break;
            case INVESTIGATOR_ROLE_AWAITING_VERIFICATION:
                builder.append("/investigatorVerificationPending.action");
                break;
            case COORDINATOR_ROLE_AWAITING_VERIFICATION:
                builder.append("/investigators/browse.action");
                break;
            case MISSING_EXPECTED_INVESTIGATOR_ROLE_WITH_INVESTIGATOR_REGISTRATION_INVITES:
                builder.append("/unselectedExpectedRole.action?investigatorType=")
                        .append(getText("label.investigator"));
                break;
            case MISSING_EXPECTED_INVESTIGATOR_ROLE_WITH_SUBINVESTIGATOR_REGISTRATION_INVITES:
                builder.append("/unselectedExpectedRole.action?investigatorType=").append(
                        getText("label.subinvestigator"));
                break;
            case ANNUAL_REGISTRATION_FOR_REVIEW:
                builder.append("/review/enterReviewRegistration.action?registration.id=").append(taskActionTargetId);
                break;
            case REGISTRATION_WITHDRAW_REQUEST:
                builder.append("/viewRegistrationWithdrawalRequest.action?user.id=").append(taskActionTargetId);
                break;
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
            }

        }

        /**
         * @return the dateAdded
         */
        public Date getDateAdded() {
            return dateAdded;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return the taskActionUrl
         */
        public String getTaskActionUrl() {
            return taskActionUrl;
        }

        /**
         * @return the taskActionTargetId
         */
        public Long getTaskActionTargetId() {
            return taskActionTargetId;
        }

        /**
         * @return the modal
         */
        public boolean isModal() {
            return modal;
        }

    }

}
