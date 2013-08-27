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

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.nes.NesId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.NotNull;

import com.fiveamsolutions.nci.commons.audit.Auditable;

/**
 * Class for sponsor roles (both sponsor representatives and delegates).
 */
@Entity(name = "sponsor_role")
public class SponsorRole implements Auditable {

    /**
     * verified_sponsor.
     */
    public static final String VERIFIED_SPONSOR_GROUP_NAME = "verified_sponsor";

    /**
     * verified_sponsor_delegate.
     */
    public static final String VERIFIED_SPONSOR_DELEGATE_GROUP_NAME = "verified_sponsor_delegate";

    /**
     * sponsor.
     */
    public static final String SPONSOR_GROUP_NAME = "sponsor";

    /**
     * sponsor_delegate.
     */
    public static final String SPONSOR_DELEGATE_GROUP_NAME = "sponsor_delegate";

    private static final long serialVersionUID = 1L;
    private Long id;
    private FirebirdUser user;
    private Organization sponsor;
    private boolean delegate;
    private boolean verified;

    SponsorRole() {
        // no-arg constructor required by Hibernate
    }

    /**
     * Creates a new role.
     *
     * @param user the user the role belongs to
     * @param sponsor the sponsor organization
     */
    SponsorRole(FirebirdUser user, Organization sponsor) {
        this(user, sponsor, false);
    }

    /**
     * Creates a new role as either a delegate or representative, indicated by flag.
     *
     * @param user the user the role belongs to
     * @param sponsor the sponsor organization.
     * @param delegate if the user is a delegate for sponsor.
     */
    SponsorRole(FirebirdUser user, Organization sponsor, boolean delegate) {
        setUser(user);
        setSponsor(sponsor);
        setDelegate(delegate);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the sponsor organization
     */
    @ManyToOne(optional = false)
    @Cascade(SAVE_UPDATE)
    @JoinColumn(name = "sponsor_id")
    @ForeignKey(name = "sponsor_role_organization_fkey")
    public Organization getSponsor() {
        return sponsor;
    }

    private void setSponsor(Organization sponsor) {
        this.sponsor = sponsor;
    }

    /**
     * @return the user
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "firebird_user_id")
    @ForeignKey(name = "sponsor_role_user_fkey")
    public FirebirdUser getUser() {
        return user;
    }

    private void setUser(FirebirdUser user) {
        this.user = user;
    }

    /**
     * @return the delegate
     */
    @Column(name = "delegate")
    @NotNull
    public boolean isDelegate() {
        return delegate;
    }

    /**
     * @param delegate the delegate to set
     */
    private void setDelegate(boolean delegate) {
        this.delegate = delegate;
    }

    /**
     * Returns the base group name for the parent group used in Grid Grouper for this role.
     *
     * @return the group name.
     */
    @Transient
    public String getRoleGroupName() {
        if (isDelegate()) {
            return SPONSOR_DELEGATE_GROUP_NAME;
        } else {
            return SPONSOR_GROUP_NAME;
        }
    }

    /**
     * Returns the base group name for the parent group used in Grid Grouper for this role.
     *
     * @return the group name.
     */
    @Transient
    public String getVerifiedRoleGroupName() {
        if (isDelegate()) {
            return VERIFIED_SPONSOR_DELEGATE_GROUP_NAME;
        } else {
            return VERIFIED_SPONSOR_GROUP_NAME;
        }
    }

    /**
     * Returns the base group name used in Grid Grouper for this sponsor role.
     *
     * @return the group name.
     */
    @Transient
    public String getSponsorOrganizationGroupName() {
        return buildSponsorGroupName(getSponsor(), isDelegate(), false);
    }

    /**
     * Returns the base group name used in Grid Grouper for verified users with this sponsor role.
     *
     * @return the group name.
     */
    @Transient
    public String getVerifiedSponsorGroupName() {
        return buildSponsorGroupName(getSponsor(), isDelegate(), true);
    }

    /**
     * @param sponsor get group name for sponsor representatives for this organization
     * @return the group name
     */
    public static String getSponsorRepresentativeOrganizationGroupName(Organization sponsor) {
        return buildSponsorGroupName(sponsor, false, false);
    }

    /**
     * @param sponsor get group name for sponsor delegates for this organization
     * @return the group name
     */
    public static String getSponsorDelegateOrganizationGroupName(Organization sponsor) {
        return buildSponsorGroupName(sponsor, true, false);
    }

    /**
     * @param sponsor get group name for verified sponsor representatives for this organization
     * @return the group name
     */
    public static String getVerifiedSponsorRepresentativeOrganizationGroupName(Organization sponsor) {
        return buildSponsorGroupName(sponsor, false, true);
    }

    /**
     * @param nesId get group name for verified sponsor representatives for this NES ID
     * @return the group name
     */
    public static String getVerifiedSponsorRepresentativeOrganizationGroupName(String nesId) {
        return buildSponsorGroupName(nesId, false, true);
    }

    /**
     * @param sponsor get group name for verified sponsor delegates for this organization
     * @return the group name
     */
    public static String getVerifiedSponsorDelegateOrganizationGroupName(Organization sponsor) {
        return buildSponsorGroupName(sponsor, true, true);
    }

    /**
     * @param nesId get group name for verified sponsor delegates for this NES ID
     * @return the group name
     */
    public static String getVerifiedSponsorDelegateOrganizationGroupName(String nesId) {
        return buildSponsorGroupName(nesId, true, true);
    }

    /**
     * @param sponsor get group display name for sponsor representatives for this organization
     * @return the display name
     */
    public static String getSponsorRepresentativeOrganizationGroupDisplayName(Organization sponsor) {
        return buildGroupDisplayName(sponsor, false, false);
    }

    /**
     * @param sponsor get group display name for sponsor delegates for this organization
     * @return the display name
     */
    public static String getSponsorDelegateOrganizationGroupDisplayName(Organization sponsor) {
        return buildGroupDisplayName(sponsor, true, false);
    }

    /**
     * @param sponsor get group display name for verified sponsor representatives for this organization
     * @return the display name
     */
    public static String getVerifiedSponsorRepresentativeOrganizationGroupDisplayName(Organization sponsor) {
        return buildGroupDisplayName(sponsor, false, true);
    }

    /**
     * @param sponsor get group display name for verified sponsor delegates for this organization
     * @return the display name
     */
    public static String getVerifiedSponsorDelegateOrganizationGroupDisplayName(Organization sponsor) {
        return buildGroupDisplayName(sponsor, true, true);
    }

    private static String buildSponsorGroupName(Organization sponsor, boolean delegate, boolean verifiedGroup) {
        return buildSponsorGroupName(sponsor.getNesId(), delegate, verifiedGroup);
    }

    private static String buildSponsorGroupName(String nesId, boolean delegate, boolean verifiedGroup) {
        String nesIdExtesion = new NesId(nesId).getExtension();
        StringBuilder builder = new StringBuilder();
        if (verifiedGroup) {
            builder.append("verified_");
        }
        builder.append("sponsor_");
        if (delegate) {
            builder.append("delegate_");
        }
        builder.append(nesIdExtesion);
        return builder.toString();
    }

    /**
     * Returns the group display name used in Grid Grouper for this sponsor role.
     *
     * @return the group display name.
     */
    @Transient
    public String getSponsorOrganizationGroupDisplayName() {
        return buildGroupDisplayName(sponsor, isDelegate(), false);
    }

    /**
     * Returns the group display name used in Grid Grouper for verified users with this sponsor role.
     *
     * @return the group display name.
     */
    @Transient
    public String getVerifiedSponsorOrganizationGroupDisplayName() {
        return buildGroupDisplayName(sponsor, isDelegate(), true);
    }

    private static String buildGroupDisplayName(Organization sponsor, boolean delegate, boolean verifiedGroup) {
        StringBuilder builder = new StringBuilder();
        builder.append("FIREBIRD ");
        if (verifiedGroup) {
            builder.append("Verified ");
        }
        builder.append(sponsor.getName());
        if (delegate) {
            builder.append(" Sponsor Delegates");
        } else {
            builder.append(" Sponsors");
        }
        return builder.toString();
    }

    /**
     * @return true if the user has been verified in Grid Grouper for this role
     */
    @Transient
    public boolean isVerfied() {
        return verified;
    }

    /**
     * @param verified the verified to set
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}
