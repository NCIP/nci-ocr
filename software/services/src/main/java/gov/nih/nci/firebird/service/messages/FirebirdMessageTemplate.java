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

import static gov.nih.nci.firebird.service.messages.FirebirdContentTemplate.*;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.*;

import java.util.Arrays;
import java.util.List;

/**
 * An Enumeration of possible Email types.
 */
@SuppressWarnings("PMD.TooManyStaticImports")
// importing parameter enums only
public enum FirebirdMessageTemplate implements FirebirdTemplate {

    /**
     * (Temporary) Email Template for an Investigator Invitation to a Protocol Registration.
     */
    INVESTIGATOR_INVITATION_EMAIL(INVESTIGATOR_INVITATION_EMAIL_BODY, INVESTIGATOR_REGISTRATION, FIREBIRD_LINK,
                                  SPONSOR_EMAIL_ADDRESS),

    /**
     * (Temporary) Email Template for an Subinvestigator Invitation to a Protocol Registration.
     */
    SUBINVESTIGATOR_INVITATION_EMAIL(FirebirdContentTemplate.SUBINVESTIGATOR_INVITATION_EMAIL_BODY,
                                     SUBINVESTIGATOR_REGISTRATION, FIREBIRD_LINK, SPONSOR_EMAIL_ADDRESS),

    /**
     * Notify the sponsor that the (sub)investigator has submitted a registration for review.
     */
    SPONSOR_SUBMISSION_NOTIFICATION_EMAIL(SPONSOR_SUBMISSION_NOTIFICATION_EMAIL_BODY, REGISTRATION,
                                          FIREBIRD_LINK),

    /**
     * Notify the investigator that their submitted registration has been returned with comments.
     */
    REGISTRATION_RETURNED_EMAIL(REGISTRATION_RETURNED_NOTIFICATION_EMAIL_BODY, REGISTRATION, FIREBIRD_LINK,
            SPONSOR_EMAIL_ADDRESS),

    /**
     * Notifies the investigator and any subinvestigators that a registration packet has been approved and finalized.
     */
    REGISTRATION_PACKET_APPROVED_EMAIL(REGISTRATION_PACKET_APPROVED_EMAIL_BODY, INVESTIGATOR_REGISTRATION,
                                       SPONSOR_EMAIL_ADDRESS),

    /**
     * Notify the investigator that their submitted registration has been returned with comments.
     */
    REGISTRATION_PACKET_DEACTIVATED_EMAIL(REGISTRATION_PACKET_DEACTIVATED_EMAIL_BODY, REGISTRATION,
                                          SPONSOR_EMAIL_ADDRESS, COMMENTS),

    /**
     * Notify the investigator that their submitted registration has been returned with comments.
     */
    REGISTRATION_PACKET_REACTIVATED_EMAIL(REGISTRATION_PACKET_REACTIVATED_EMAIL_BODY, REGISTRATION, FIREBIRD_LINK,
                                          SPONSOR_EMAIL_ADDRESS, COMMENTS),

    /**
     * Notify the investigator / subinvestigator that there have been modifications to the protocol and they will be
     * required to re-submit their registration if they had previously submitted their registration.
     */
    PROTOCOL_MODIFIED_EMAIL(PROTOCOL_MODIFIED_EMAIL_BODY, REGISTRATION, FIREBIRD_LINK, SPONSOR_EMAIL_ADDRESS,
            PROTOCOL_REVISION),

    /**
     * Email notifying Investigator that a Registration Coordinator is requesting access to manage their profile and
     * registrations.
     */
    REGISTRATION_COORDINATOR_REQUEST_EMAIL(REGISTRATION_COORDINATOR_REQUEST_EMAIL_BODY, REGISTRATION_COORDINATOR),

    /**
     * Email notifying Investigator that a Registration Coordinator has completed one of their registrations and it is
     * now ready for signing and submission.
     */
    COORDINATOR_COMPLETED_REGISTRATION_EMAIL(COORDINATOR_COMPLETED_REGISTRATION_EMAIL_BODY, REGISTRATION,
                                             FIREBIRD_LINK, REGISTRATION_COORDINATOR),

    /**
     * Email notifying Investigator that a Registration Coordinator has completed their annual registrations and it is
     * now ready for signing and submission.
     */
    COORDINATOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL(COORDINATOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL_BODY,
                                                    ANNUAL_REGISTRATION, FIREBIRD_LINK, REGISTRATION_COORDINATOR),

    /**
     * Email template informing the coordinator that a previously managed investigator has suspended them from managing
     * their protocol registrations.
     */
    COORDINATOR_SUSPENDED_FROM_REGISTRATIONS_EMAIL(COORDINATOR_SUSPENDED_FROM_REGISTRATIONS_EMAIL_BODY,
            MANAGED_INVESTIGATOR, FIREBIRD_LINK, MANAGED_INVESTIGATOR, INVESTIGATOR),

    /**
     * Email template informing the coordinator that a previously managed investigator has unsuspended them from
     * managing their protocol registrations.
     */
    COORDINATOR_UNSUSPENDED_FROM_REGISTRATIONS_EMAIL(COORDINATOR_UNSUSPENDED_FROM_REGISTRATIONS_EMAIL_BODY,
                                                     MANAGED_INVESTIGATOR, FIREBIRD_LINK, INVESTIGATOR),

    /**
     * Email template informing the coordinator that a previously managed investigator has suspended them from managing
     * their profile.
     */
    COORDINATOR_SUSPENDED_FROM_PROFILE_EMAIL(COORDINATOR_SUSPENDED_FROM_PROFILE_EMAIL_BODY,
                                             MANAGED_INVESTIGATOR, FIREBIRD_LINK, INVESTIGATOR),

    /**
     * Email template informing the coordinator that a previously managed investigator has unsuspended them from
     * managing their profile.
     */
    COORDINATOR_UNSUSPENDED_FROM_PROFILE_EMAIL(COORDINATOR_UNSUSPENDED_FROM_PROFILE_EMAIL_BODY,
                                               MANAGED_INVESTIGATOR, FIREBIRD_LINK, INVESTIGATOR),

    /**
     * Email template informing the coordinator that a previously managed investigator has removed them as a
     * registration coordinator.
     */
    COORDINATOR_REMOVED_EMAIL(COORDINATOR_REMOVED_EMAIL_BODY, MANAGED_INVESTIGATOR, FIREBIRD_LINK, INVESTIGATOR),

    /**
     * Email template informing the a coordinator that an investigator has approved/rejected a request.
     */
    COORDINATOR_REQUEST_UPDATED_EMAIL(COORDINATOR_REQUEST_UPDATED_EMAIL_BODY, MANAGED_INVESTIGATOR),

    /**
     * Email template used to request a new login account and FIREBIRD access.
     */
    ACCOUNT_REQUEST_EMAIL(ACCOUNT_REQUEST_EMAIL_BODY, ACCOUNT_DATA),

    /**
     * Email template used to request FIREBIRD access.
     */
    ACCOUNT_REQUEST_EXISTING_LDAP_ACCOUNT_EMAIL(ACCOUNT_REQUEST_EXISTING_LDAP_ACCOUNT_EMAIL_BODY, ACCOUNT_DATA),

    /**
     * Email template used to notify potential new users that the request for addition of a new login account has been
     * made.
     */
    ACCOUNT_REQUEST_USER_NOTIFICATION_EMAIL(ACCOUNT_REQUEST_USER_NOTIFICATION_BODY, ACCOUNT_DATA,
                                            FIREBIRD_SUPPORT_EMAIL),

    /**
     * Email template used to notify a user they have been removed as a sponsor delegate for a sponsor.
     */
    DELEGATE_REMOVAL_NOTIFICATION_EMAIL(DELEGATE_REMOVAL_NOTIFICATION_BODY, SPONSOR_ROLE, SPONSOR_EMAIL_ADDRESS),

    /**
     * Email template used to notify Application Support that a user has added roles requiring verification.
     */
    ADDED_ROLES_SUPPORT_NOTIFICATION_EMAIL(ADDED_ROLES_SUPPORT_NOTIFICATION_BODY, FIREBIRD_USER,
                                           ACCOUNT_DATA),

    /**
     * Email template used to notify a subinvestigator that they have been removed from registrations.
     */
    REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL(REMOVE_SUBINVESTIGATOR_NOTIFICATION_BODY, REGISTRATIONS, INVESTIGATOR),

    /**
     * Email template used to notify a sponsor that a subinvestigator has been removed from a locked registrations.
     */
    REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL_TO_SPONSOR(REMOVE_SUBINVESTIGATOR_NOTIFICATION_TO_SPONSOR_BODY,
                                                         SUBINVESTIGATOR_REGISTRATION),

    /**
     * Email template used to notify an investigator their registration coordinator has removed their registration
     * coordinator role.
     */
    COORDINATOR_REMOVED_ROLE_EMAIL(COORDINATOR_REMOVED_ROLE_BODY, INVESTIGATOR, REGISTRATION_COORDINATOR),

    /**
     * Email template used to notify a sponsor that a packet is ready for approval.
     */
    PACKET_READY_FOR_APPROVAL_EMAIL(PACKET_READY_FOR_APPROVAL_EMAIL_BODY, FIREBIRD_USER, REGISTRATION,
                                    FIREBIRD_LINK),

    /**
     * Email subject used to notify support of an unhandled application exception.
     */
    UNHANDLED_EXCEPTION_EMAIL(UNHANDLED_EXCEPTION_EMAIL_BODY, FIREBIRD_USER, TIMESTAMP,
                              REQUEST_URL, REQUEST_PARAMETERS, STACKTRACE),

    /**
     * Email template used to notify an Investigator that they're registration packet has been removed from a protocol.
     */
    PACKET_REMOVED_EMAIL(PACKET_REMOVED_EMAIL_BODY, INVESTIGATOR_REGISTRATION, SPONSOR_EMAIL_ADDRESS),

    /**
     * Email template used to notify a coordinator or other person that the investigator has submitted their
     * registration.
     */
    COORDINATOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL(
            COORDINATOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_BODY, ANNUAL_REGISTRATION),

    /**
     * Email template used to notify the sponsor that the investigator has submitted their registration for review.
     */
    SPONSOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL(SPONSOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_BODY,
                                                              ANNUAL_REGISTRATION, FIREBIRD_LINK),

    /**
     * Notify the investigator that their submitted annual registration has been returned with comments.
     */
    ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_INVESTIGATOR(ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_INVESTIGATOR_BODY,
                                                       ANNUAL_REGISTRATION, FIREBIRD_LINK, SPONSOR_EMAIL_ADDRESS),

    /**
     * Notify the investigator's coordinator and other persons that their submitted annual registration has been
     * returned with comments.
     */
    ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_OTHERS(ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_OTHERS_BODY,
                                                 ANNUAL_REGISTRATION),

    /**
     * Notify the investigator that their submitted annual registration has been accepted.
     */
    ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_INVESTIGATOR(ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_INVESTIGATOR_BODY,
                                                       ANNUAL_REGISTRATION, FIREBIRD_LINK, SPONSOR_EMAIL_ADDRESS),

    /**
     * Notify the investigator's coordinator and other persons that their submitted annual registration has been
     * accepted.
     */
    ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_OTHERS(ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_OTHERS_BODY,
                                                 ANNUAL_REGISTRATION),

    /**
     * Notify the investigator's coordinator that an annual registration renewal should be renewed within
     * sixty days.
     */
    ANNUAL_REGISTRATION_RENEWAL_SIXTY_DAY_NOTICE_EMAIL(ANNUAL_REGISTRATION_RENEWAL_SIXTY_DAY_NOTICE_EMAIL_BODY,
                                                       ANNUAL_REGISTRATION, FIREBIRD_LINK),

    /**
     * Notify the investigator's coordinator that an annual registration renewal should be renewed within thirty days.
     */
    ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_COORDINATOR(
            ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_COORDINATOR_BODY, ANNUAL_REGISTRATION,
            FIREBIRD_LINK),

    /**
     * Notify the investigator that their annual registration renewal should be renewed within thirty days.
     */
    ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_INVESTIGATOR(
            ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_INVESTIGATOR_BODY, FIREBIRD_LINK),

    /**
     * Notify the investigator that their annual registration has been approved.
     */
    ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_INVESTIGATOR(ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_INVESTIGATOR_BODY,
                                                       ANNUAL_REGISTRATION),

    /**
     * Notify a coordinator that one of their investigator's annual registration has been approved.
     */
    ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_COORDINATOR(ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_COORDINATOR_BODY,
                                                      ANNUAL_REGISTRATION),

    /**
     * Email notifying a Registration Coordinator that a Sponsor has completed one of their managed investigator's
     * annual registrations and it is now ready for the investigator to sign and submit.
     */
    SPONSOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL(SPONSOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL_BODY, ANNUAL_REGISTRATION,
                                                FIREBIRD_LINK, SPONSOR, REGISTRATION_COORDINATOR),

    /**
     * Email requesting investigator withdrawal from further CTEP sponsored trials.
     */
    CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_EMAIL(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_EMAIL_BODY, INVESTIGATOR,
                                               FIREBIRD_LINK, COMMENTS),

    /**
     * Email notifying investigator that their request to withdrawal from NCI-sponsored clinical trials has been
     * accepted.
     */
    CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_INVESTIGATOR(
            CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_INVESTIGATOR_BODY),

    /**
     * Email notifying coordinator and others that their request to withdrawal from NCI-sponsored clinical trials has
     * been accepted.
     */
    CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_OTHERS(
            CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_OTHERS_BODY, INVESTIGATOR),

    /**
     * Email notifying investigator that their request to withdrawal from NCI-sponsored clinical trials has been denied.
     */
    CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_INVESTIGATOR(
            CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_INVESTIGATOR_BODY, COMMENTS),

    /**
     * Email notifying coordinator and others that their request to withdrawal from NCI-sponsored clinical trials has
     * been denied.
     */
    CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_OTHERS(
            CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_OTHERS_BODY, INVESTIGATOR, COMMENTS),

    /**
     * Email notifying an investigator that his/her status has been set to suspended.
     */
    CTEP_INVESTIGATOR_SUSPENEDED_EMAIL_TO_INVESTIGATOR(CTEP_INVESTIGATOR_SUSPENEDED_EMAIL_TO_INVESTIGATOR_BODY,
                                                       INVESTIGATOR),

    /**
     * Email notifying coordinator and others that a managed investigator's status has been set to suspended.
     */
    CTEP_INVESTIGATOR_SUSPENEDED_EMAIL_TO_COORDINATOR(CTEP_INVESTIGATOR_SUSPENEDED_EMAIL_TO_COORDINATOR_BODY,
                                                      REGISTRATION_COORDINATOR, INVESTIGATOR),

    /**
     * Email notifying investigator that his/her status has been set to disqualified.
     */
    CTEP_INVESTIGATOR_DISQUALIFIED_EMAIL_TO_INVESTIGATOR(CTEP_INVESTIGATOR_DISQUALIFIED_EMAIL_TO_INVESTIGATOR_BODY,
                                                         COMMENTS),

    /**
     * Email notifying coordinator that a managed investigator's status has been set to disqualified.
     */
    CTEP_INVESTIGATOR_DISQUALIFIED_EMAIL_TO_OTHERS(CTEP_INVESTIGATOR_DISQUALIFIED_EMAIL_TO_OTHERS_BODY, COMMENTS,
                                                   INVESTIGATOR),

    /**
     * Email notifying a sponsor that one of their investigators has initiated a revision to their registration.
     */
    REGISTRATION_REVISION_INITIATED_EMAIL_TO_SPONSOR(REGISTRATION_REVISION_INITIATED_EMAIL_TO_SPONSOR_BODY,
                                                     INVESTIGATOR_REGISTRATION),

    /**
     * Email notifying a registration coordinator that one of their managed investigators has initiated a revision to
     * their registration.
     */
    REGISTRATION_REVISION_INITIATED_EMAIL_TO_COORDINATOR(REGISTRATION_REVISION_INITIATED_EMAIL_TO_COORDINATOR_BODY,
                                                         INVESTIGATOR_REGISTRATION),

    /**
     * Email notifying an investigator that one of their registration coordinators has initiated a revision to their
     * registration.
     */
    REGISTRATION_REVISION_INITIATED_EMAIL_TO_INVESTIGATOR(REGISTRATION_REVISION_INITIATED_EMAIL_TO_INVESTIGATOR_BODY,
            REGISTRATION_COORDINATOR, INVESTIGATOR_REGISTRATION),

    /**
     * Email notifying a sponsor that one of their investigators has canceled a revision to their registration.
     */
    REGISTRATION_REVISION_CANCELED_EMAIL_TO_SPONSOR(REGISTRATION_REVISION_CANCELED_EMAIL_TO_SPONSOR_BODY,
            INVESTIGATOR_REGISTRATION),

    /**
     * Email notifying coordinator and others that one of their managed investigators has deleted their annual
     * registration packet.
     */
    INVESTIGATOR_DELETED_ANNUAL_REGISTRATION_EMAIL_TO_OTHERS(
            INVESTIGATOR_DELETED_ANNUAL_REGISTRATION_EMAIL_TO_OTHERS_BODY, INVESTIGATOR);

    private static final String SUBJECT_TEMPLATE_SUFFIX = "_SUBJECT";

    private final FirebirdTemplateParameter[] requiredParameters;
    private final FirebirdContentTemplate contentTemplate;

    FirebirdMessageTemplate(FirebirdContentTemplate contentTemplate, FirebirdTemplateParameter... requiredParameters) {
        this.contentTemplate = contentTemplate;
        this.requiredParameters = requiredParameters;
    }

    /**
     * @return the contentTemplate
     */
    public FirebirdContentTemplate getContentTemplate() {
        return contentTemplate;
    }

    /**
     * @return the subject template
     */
    public FirebirdStringTemplate getSubjectTemplate() {
        return FirebirdStringTemplate.valueOf(name() + SUBJECT_TEMPLATE_SUFFIX);
    }

    /**
     * @return the requiredParameters
     */
    public List<FirebirdTemplateParameter> getRequiredParameters() {
        return Arrays.asList(requiredParameters);
    }
}
