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
package gov.nih.nci.firebird.service.messages;

import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.FIREBIRD_USER;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.REGISTRATION;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.REGISTRATION_COORDINATOR;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.INVESTIGATOR;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.SUBINVESTIGATOR_REGISTRATION;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.SPONSOR_ROLE;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.ACCOUNT_DATA;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.ANNUAL_REGISTRATION;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.SPONSOR;

import java.util.Arrays;
import java.util.List;

/**
 * An Enumeration of possible Email types.
 */
@SuppressWarnings("PMD.TooManyStaticImports")
// importing parameter enums only
public enum FirebirdStringTemplate implements FirebirdTemplate {

    /**
     * Email Template for an Investigator Invitation to a Protocol Registration.
     */
    INVESTIGATOR_INVITATION_EMAIL_SUBJECT(INVESTIGATOR_REGISTRATION),

    /**
     * Email Template for an Sub-Investigator Invitation to a Protocol Registration.
     */
    SUBINVESTIGATOR_INVITATION_EMAIL_SUBJECT(SUBINVESTIGATOR_REGISTRATION),

    /**
     * Email subject when a sponsor is notified about the submission of a registration.
     */
    SPONSOR_SUBMISSION_NOTIFICATION_EMAIL_SUBJECT(REGISTRATION),

    /**
     * Email subject for notification email sent on registration packet approval.
     */
    REGISTRATION_PACKET_APPROVED_EMAIL_SUBJECT(INVESTIGATOR_REGISTRATION),

    /**
     * Email subject for notification email sent on registration packet approval.
     */
    REGISTRATION_RETURNED_EMAIL_SUBJECT(REGISTRATION),

    /**
     * Email subject for notification email sent on registration packet deactivation.
     */
    REGISTRATION_PACKET_DEACTIVATED_EMAIL_SUBJECT(REGISTRATION),

    /**
     * Email subject for notification email sent on registration packet reactivation.
     */
    REGISTRATION_PACKET_REACTIVATED_EMAIL_SUBJECT(REGISTRATION),

    /**
     * Email subject for notification email sent on protocol modification.
     */
    PROTOCOL_MODIFIED_EMAIL_SUBJECT(REGISTRATION),

    /**
     * Email subject for notification email sent on protocol completion by coordinator.
     */
    COORDINATOR_COMPLETED_REGISTRATION_EMAIL_SUBJECT(REGISTRATION_COORDINATOR, REGISTRATION),

    /**
     * Email subject for notification email sent on annual registration completion by coordinator.
     */
    COORDINATOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL_SUBJECT(REGISTRATION_COORDINATOR),

    /**
     * Email subject for informing the coordinator that a previously managed investigator has suspended them from
     * managing their protocol registrations.
     */
    COORDINATOR_SUSPENDED_FROM_REGISTRATIONS_EMAIL_SUBJECT(INVESTIGATOR),

    /**
     * Email subject for informing the coordinator that a previously managed investigator has unsuspended them from
     * managing their protocol registrations.
     */
    COORDINATOR_UNSUSPENDED_FROM_REGISTRATIONS_EMAIL_SUBJECT(INVESTIGATOR),

    /**
     * Email subject for informing the coordinator that a previously managed investigator has suspended them from
     * managing their profile.
     */
    COORDINATOR_SUSPENDED_FROM_PROFILE_EMAIL_SUBJECT(INVESTIGATOR),

    /**
     * Email subject for informing the coordinator that a previously managed investigator has unsuspended them from
     * managing their profile.
     */
    COORDINATOR_UNSUSPENDED_FROM_PROFILE_EMAIL_SUBJECT(INVESTIGATOR),

    /**
     * Email subject for informing the coordinator that a previously managed investigator has removed them as a
     * registration coordinator.
     */
    COORDINATOR_REMOVED_EMAIL_SUBJECT(INVESTIGATOR),

    /**
     * Task header for an Investigator Invitation to a Protocol Registration.
     */
    INVESTIGATOR_INVITATION_TASK_TITLE(INVESTIGATOR_REGISTRATION),

    /**
     * Task header for an Sub-Investigator Invitation to a Protocol Registration.
     */
    SUBINVESTIGATOR_INVITATION_TASK_TITLE(SUBINVESTIGATOR_REGISTRATION),

    /**
     * Task header for an Investigator Invitation to a Protocol Registration.
     */
    REGISTRATION_IN_PROGRESS_TASK_TITLE(REGISTRATION),

    /**
     * Task header for an Investigator Invitation to a Protocol Registration.
     */
    REGISTRATION_REACTIVATED_TASK_TITLE(REGISTRATION),

    /**
     * Task header for a Sponsor's notification of a completed registration.
     */
    REGISTRATION_SUBMITTED_TASK_TITLE(REGISTRATION),

    /**
     * Task header for a Sponsor that a registration is still under review.
     */
    REGISTRATION_IN_REVIEW_TASK_TITLE(REGISTRATION),

    /**
     * Task header for a Sponsor's notification of a completed annual registration.
     */
    ANNUAL_REGISTRATION_SUBMITTED_TASK_TITLE(ANNUAL_REGISTRATION),

    /**
     * Task header for a Sponsor that an annual registration is still under review.
     */
    ANNUAL_REGISTRATION_IN_REVIEW_TASK_TITLE(ANNUAL_REGISTRATION),

    /**
     * Task header for a Sponsor that the review of an annual registration is on hold.
     */
    ANNUAL_REGISTRATION_REVIIEW_ON_HOLD_TASK_TITLE(ANNUAL_REGISTRATION),

    /**
     * Task header for an Registration that has been rejected and returned.
     */
    REGISTRATION_RETURNED_TASK_TITLE(REGISTRATION),

    /**
     * Task header for an annual Registration that has been rejected and returned.
     */
    ANNUAL_REGISTRATION_RETURNED_TASK_TITLE,

    /**
     * Task header for a registration packet awaiting approval.
     */
    REGISTRATION_PACKET_AWAITING_APPROVAL_TITLE(INVESTIGATOR_REGISTRATION),

    /**
     * Task header for when a protocol has been updated.
     */
    PROTOCOL_MODIFIED_TASK_TITLE(REGISTRATION),

    /**
     * Registration Coordinator requests access to an investigator profile (task).
     */
    REGISTRATION_COORDINATOR_REQUEST_TITLE(REGISTRATION_COORDINATOR),

    /**
     * Registration Coordinator has requested access to an invenstigator profile (email).
     */
    REGISTRATION_COORDINATOR_REQUEST_EMAIL_SUBJECT(REGISTRATION_COORDINATOR),

    /**
     * registration coordinator's request was updated.
     */
    COORDINATOR_REQUEST_UPDATED_EMAIL_SUBJECT,

    /**
     * A registration is complete and ready to be signed by the investigator.
     */
    REGISTRATION_READY_FOR_SIGNING_TASK_TITLE(REGISTRATION),

    /**
     * Email subject template used to request a new login account and FIREBIRD access.
     */
    ACCOUNT_REQUEST_EMAIL_SUBJECT(ACCOUNT_DATA),

    /**
     * Email subject template used to request FIREBIRD access.
     */
    ACCOUNT_REQUEST_EXISTING_LDAP_ACCOUNT_EMAIL_SUBJECT(ACCOUNT_DATA),

    /**
     * Email subject template used to request addition of a new login account.
     */
    ACCOUNT_REQUEST_USER_NOTIFICATION_EMAIL_SUBJECT,

    /**
     * A sponsor or sponsor delegate user role is awaiting verification.
     */
    SPONSOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE,

    /**
     * An investigator user role is awaiting verification.
     */
    INVESTIGATOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE,

    /**
     * A registration coordinator user role is awaiting verification from the requested investigator(s).
     */
    COORDINATOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE,

    /**
     * A Registration Coordinator can renew one of their managed investigator's annual registration.
     */
    COORDINATOR_REGISTRATION_RENEWAL_TASK_TITLE(ANNUAL_REGISTRATION),

    /**
     * An investigator can renew their annual registration.
     */
    INVESTIGATOR_REGISTRATION_RENEWAL_TASK_TITLE,

    /**
     * Email subject template used to request verification of added roles.
     */
    ADDED_ROLES_SUPPORT_NOTIFICATION_EMAIL_SUBJECT(FIREBIRD_USER),

    /**
     * Email subject template used to notify a user they have been removed as a sponsor delegate for a sponsor.
     */
    DELEGATE_REMOVAL_NOTIFICATION_EMAIL_SUBJECT(SPONSOR_ROLE),

    /**
     * Email subject used to notify a subinvestigator that they have been removed from registrations.
     */
    REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL_SUBJECT(INVESTIGATOR),

    /**
     * Email template used to notify a sponsor that a subinvestigator has been removed from a locked registrations.
     */
    REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL_TO_SPONSOR_SUBJECT(SUBINVESTIGATOR_REGISTRATION),

    /**
     * Email subject used to notify an investigator their registration coordinator has removed their registration
     * coordinator role.
     */
    COORDINATOR_REMOVED_ROLE_EMAIL_SUBJECT(REGISTRATION_COORDINATOR),

    /**
     * Email subject used to notify a sponsor that a packet is ready for approval.
     */
    PACKET_READY_FOR_APPROVAL_EMAIL_SUBJECT(REGISTRATION),

    /**
     * A user has investigator registration(s) but has not selected the investigator role.
     */
    MISSING_EXPECTED_INVESTIGATOR_ROLE_TASK_TITLE,

    /**
     * Email subject used to notify support of an unhandled application exception.
     */
    UNHANDLED_EXCEPTION_EMAIL_SUBJECT,

    /**
     * Email template used to notify an Investigator that they're registration packet has been removed from a protocol.
     */
    PACKET_REMOVED_EMAIL_SUBJECT(INVESTIGATOR_REGISTRATION),

    /**
     * Email template used to notify a coordinator or other person that the investigator has submitted their
     * registration.
     */
    COORDINATOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL_SUBJECT(ANNUAL_REGISTRATION),

    /**
     * Email template used to notify the sponsor that the investigator has submitted their registration for review.
     */
    SPONSOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL_SUBJECT(ANNUAL_REGISTRATION),

    /**
     * Notify the investigator that their submitted annual registration has been returned with comments.
     */
    ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_INVESTIGATOR_SUBJECT(),

    /**
     * Notify the investigator's coordinator and other persons that their submitted annual registration has been
     * returned with comments.
     */
    ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_OTHERS_SUBJECT(ANNUAL_REGISTRATION),

    /**
     * Notify the investigator that their submitted annual registration has been accepted.
     */
    ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_INVESTIGATOR_SUBJECT(),

    /**
     * Notify the investigator's coordinator and other persons that their submitted annual registration has been
     * accepted.
     */
    ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_OTHERS_SUBJECT(ANNUAL_REGISTRATION),

    /**
     * Notify the investigator's coordinator that an annual registration renewal should be renewed within
     * sixty days.
     */
    ANNUAL_REGISTRATION_RENEWAL_SIXTY_DAY_NOTICE_EMAIL_SUBJECT(ANNUAL_REGISTRATION),

    /**
     * Notify the investigator's coordinator that an annual registration renewal should be renewed within thirty days.
     */
    ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_COORDINATOR_SUBJECT(ANNUAL_REGISTRATION),

    /**
     * Notify the investigator that there annual registration renewal should be renewed within thirty days.
     */
    ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_INVESTIGATOR_SUBJECT(),

    /**
     * A registration is complete and ready to be signed by the investigator.
     */
    ANNUAL_REGISTRATION_READY_FOR_SIGNING_TASK_TITLE(),

    /**
     * An annual registration has been approved by the sponsor or delegate.
     */
    ANNUAL_REGISTRATION_APPROVED_TASK_FOR_INVESTIGATOR_TITLE(),

    /**
     * An annual registration has been approved by the sponsor or delegate.
     */
    ANNUAL_REGISTRATION_APPROVED_TASK_FOR_COORDINATOR_TITLE(ANNUAL_REGISTRATION),

    /**
     * An annual registration has been approved by the sponsor or delegate.
     */
    ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_INVESTIGATOR_SUBJECT(),

    /**
     * An annual registration has been approved by the sponsor or delegate.
     */
    ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_COORDINATOR_SUBJECT(ANNUAL_REGISTRATION),

    /**
     * Email notifying a Registration Coordinator that a Sponsor has completed one of their managed investigator's
     * annual registrations and it is now ready for the investigator to sign and submit.
     */
    SPONSOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL_SUBJECT(SPONSOR, ANNUAL_REGISTRATION),

    /**
     * Email requesting investigator withdrawal from further CTEP sponsored trials.
     */
    CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_EMAIL_SUBJECT(INVESTIGATOR),

    /**
     * Request to a Sponsor (Delegate) to withdraw from participating in NCI-sponsored clinical trials.
     */
    REQUEST_FOR_WITHDRAWAL_TASK_TITLE(INVESTIGATOR),

    /**
     * Email notifying investigator that their request to withdrawal from NCI-sponsored clinical trials has been
     * accepted.
     */
    CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_INVESTIGATOR_SUBJECT(),

    /**
     * Email notifying coordinator and others that their request to withdrawal from NCI-sponsored clinical trials has
     * been accepted.
     */
    CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_OTHERS_SUBJECT(INVESTIGATOR),

    /**
     * Email notifying investigator that their request to withdrawal from NCI-sponsored clinical trials has been denied.
     */
    CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_INVESTIGATOR_SUBJECT(),

    /**
     * Email notifying coordinator and others that their request to withdrawal from NCI-sponsored clinical trials has
     * been denied.
     */
    CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_OTHERS_SUBJECT(INVESTIGATOR),

    /**
     * Email notifying an investigator that his/her status has been set to suspended.
     */
    CTEP_INVESTIGATOR_SUSPENEDED_EMAIL_TO_INVESTIGATOR_SUBJECT(),

    /**
     * Email notifying coordinator and others that a managed investigator's status has been set to suspended.
     */
    CTEP_INVESTIGATOR_SUSPENEDED_EMAIL_TO_COORDINATOR_SUBJECT(INVESTIGATOR),

    /**
     * Notification that an investigator has been suspended.
     */
    CTEP_INVESTIGATOR_SUSPENEDED_TASK_TITLE_FOR_INVESTIGATOR(),

    /**
     * Notification to a registration coordinator that an investigator has been suspended.
     */
    CTEP_INVESTIGATOR_SUSPENEDED_TASK_TITLE_FOR_COORDINATOR(INVESTIGATOR),

    /**
     * Email notifying investigator that his/her status has been set to disqualified.
     */
    CTEP_INVESTIGATOR_DISQUALIFIED_EMAIL_TO_INVESTIGATOR_SUBJECT(),

    /**
     * Email notifying coordinator that a managed investigator's status has been set to disqualified.
     */
    CTEP_INVESTIGATOR_DISQUALIFIED_EMAIL_TO_OTHERS_SUBJECT(INVESTIGATOR),

    /**
     * Notification informing an investigator that his/her status has been set to disqualified.
     */
    CTEP_INVESTIGATOR_DISQUALIFIED_TASK_TITLE_FOR_INVESTIGATOR(),

    /**
     * Notification informing a coordinator that a managed investigator's status has been set to disqualified.
     */
    CTEP_INVESTIGATOR_DISQUALIFIED_TASK_TITLE_FOR_COORDINATOR(INVESTIGATOR),

    /**
     * Email notifying a sponsor that one of their investigators has initiated a revision to their registration.
     */
    REGISTRATION_REVISION_INITIATED_EMAIL_TO_SPONSOR_SUBJECT(INVESTIGATOR_REGISTRATION),

    /**
     * Email notifying a registration coordinator that one of their managed investigators has initiated a revision to
     * their registration.
     */
    REGISTRATION_REVISION_INITIATED_EMAIL_TO_COORDINATOR_SUBJECT(INVESTIGATOR_REGISTRATION),

    /**
     * Email notifying an investigator that one of their managed investigators has initiated a revision to
     * their registration.
     */
    REGISTRATION_REVISION_INITIATED_EMAIL_TO_INVESTIGATOR_SUBJECT(REGISTRATION_COORDINATOR, INVESTIGATOR_REGISTRATION),

    /**
     * Email notifying a sponsor that one of their investigators has canceled a revision to their registration.
     */
    REGISTRATION_REVISION_CANCELED_EMAIL_TO_SPONSOR_SUBJECT(INVESTIGATOR_REGISTRATION),

    /**
     * Email notifying coordinator and others that one of their managed investigators has deleted their annual
     * registration packet.
     */
    INVESTIGATOR_DELETED_ANNUAL_REGISTRATION_EMAIL_TO_OTHERS_SUBJECT(INVESTIGATOR);


    private static final String TEMPLATE_PROPERTY_NAME_PREFIX = "template.string.";
    private final FirebirdTemplateParameter[] requiredParameters;

    FirebirdStringTemplate(FirebirdTemplateParameter... requiredParameters) {
        this.requiredParameters = requiredParameters;
    }

    /**
     * @return the subjectProperty
     */
    public String getTemplatePropertyName() {
        return TEMPLATE_PROPERTY_NAME_PREFIX + name();
    }

    /**
     * @return the requiredParameters
     */
    @Override
    public List<FirebirdTemplateParameter> getRequiredParameters() {
        return Arrays.asList(requiredParameters);
    }
}
