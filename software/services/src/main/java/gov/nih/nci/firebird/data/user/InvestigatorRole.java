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

import static com.google.common.base.Preconditions.checkArgument;
import gov.nih.nci.firebird.data.InvestigatorProfile;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Parent;

/**
 * Links an investigator user to their profile.
 */
@Embeddable
public class InvestigatorRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private FirebirdUser user;
    private InvestigatorProfile profile;
    private InvestigatorWithdrawalRequest withdrawalRequest;
    private InvestigatorStatus status;
    private boolean statusChangeNotificationRequiredForInvestigator;
    private boolean statusChangeNotificationRequiredForCoordinator;
    private DisqualificationReason disqualificationReason;

    InvestigatorRole(FirebirdUser user, InvestigatorProfile profile) {
        this.user = user;
        this.profile = profile;
    }

    InvestigatorRole() {
        // no-arg constructor required by Hibernate
    }

    /**
     * @return the user
     */
    @Parent
    public FirebirdUser getUser() {
        return user;
    }

    @SuppressWarnings("unused") // required by Hibernate
    private void setUser(FirebirdUser user) {
        this.user = user;
    }

    /**
     * @return the profile
     */
    @OneToOne
    @JoinColumn(name = "investigator_role_profile_id")
    @ForeignKey(name = "investigator_role_profile_fkey")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    public InvestigatorProfile getProfile() {
        return profile;
    }

    @SuppressWarnings("ucd")
    // setter required by hibernate, used in tests
    void setProfile(InvestigatorProfile profile) {
        this.profile = profile;
    }

    /**
     * @return the withdrawalRequest
     */
    @Embedded
    public InvestigatorWithdrawalRequest getWithdrawalRequest() {
        return withdrawalRequest;
    }

    /**
     * @param withdrawalRequest the withdrawalRequest to set
     */
    public void setWithdrawalRequest(InvestigatorWithdrawalRequest withdrawalRequest) {
        this.withdrawalRequest = withdrawalRequest;
    }

    /**
     * @return the current investigator status
     */
    @Enumerated(EnumType.STRING)
    public InvestigatorStatus getStatus() {
        return status;
    }

    /**
     * @param status investigator status
     */
    public void setStatus(InvestigatorStatus status) {
        this.status = status;
    }

    /**
     * @return the statusChangeNotificationRequiredForInvestigator
     */
    @Column(name = "status_change_notification_required_for_investigator")
    public boolean isStatusChangeNotificationRequiredForInvestigator() {
        return statusChangeNotificationRequiredForInvestigator;
    }

    /**
     * @param statusChangeNotificationRequiredForInvestigator the statusChangeNotificationRequiredForInvestigator to set
     */
    public void setStatusChangeNotificationRequiredForInvestigator(
            boolean statusChangeNotificationRequiredForInvestigator) {
        this.statusChangeNotificationRequiredForInvestigator = statusChangeNotificationRequiredForInvestigator;
    }

    /**
     * @return the statusChangeNotificationRequiredForCoordinator
     */
    @Column(name = "status_change_notification_required_for_coordinator")
    public boolean isStatusChangeNotificationRequiredForCoordinator() {
        return statusChangeNotificationRequiredForCoordinator;
    }

    /**
     * @param statusChangeNotificationRequiredForCoordinator the statusChangeNotificationRequiredForCoordinator to set
     */
    public void setStatusChangeNotificationRequiredForCoordinator(
            boolean statusChangeNotificationRequiredForCoordinator) {
        this.statusChangeNotificationRequiredForCoordinator = statusChangeNotificationRequiredForCoordinator;
    }

    /**
     * Sets the investigator to disqualified and stores the reasoning.
     *
     * @param reason reason for disqualification
     */
    public void disqualifyInvestigator(String reason) {
        checkArgument(StringUtils.isNotBlank(reason), "Disqualification reason can't be empty");
        setDisqualificationReason(new DisqualificationReason(reason));
        if (!getProfile().hasCtepRegistrationCoordinator()) {
            disqualificationReason.setAcknowledgedByCoordinator(true);
        }
        setStatus(InvestigatorStatus.DISQUALIFIED);
    }

    private void setDisqualificationReason(DisqualificationReason disqualificationReason) {
        this.disqualificationReason = disqualificationReason;
    }

    /**
     * @return the disqualificationReason
     */
    @Embedded
    public DisqualificationReason getDisqualificationReason() {
        return disqualificationReason;
    }

}
