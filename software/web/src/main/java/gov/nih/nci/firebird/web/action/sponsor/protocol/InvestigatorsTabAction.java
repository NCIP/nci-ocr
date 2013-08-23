/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a organization or an entity, and all other entities that control,
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

package gov.nih.nci.firebird.web.action.sponsor.protocol;

import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.RevisedInvestigatorRegistration;
import gov.nih.nci.firebird.service.protocol.ProtocolService;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Sponsor's "Investigator" Tab for inviting investigators.
 */
@SuppressWarnings("PMD.TooManyMethods")
// Methods broken down for clarity
public class InvestigatorsTabAction extends AbstractProtocolDetailsTabAction {
    private static final long serialVersionUID = 1L;

    private String selectedPersonExternalId;
    private List<Long> invitedRegistrationIds;
    private Person investigator = new Person();

    /**
     * Constructor.
     *
     * @param protocolService the protocol service
     */
    @Inject
    public InvestigatorsTabAction(ProtocolService protocolService) {
        super(protocolService);
    }

    @Override
    public void prepare() {
        super.prepare();
        if (StringUtils.isNotBlank(selectedPersonExternalId)) {
            investigator = getPerson(getSelectedPersonExternalId());
        }
    }

    /**
     * JSON response for table data.
     *
     * @return table data to display as a JSON string.
     * @throws JSONException if serialization fails.
     */
    public String getRegistrationsAsJson() throws JSONException {
        List<RegistrationListing> listings = getRegistrationListings();
        return JSONUtil.serialize(listings);
    }

    private List<RegistrationListing> getRegistrationListings() {
        List<RegistrationListing> listings = Lists.newArrayList();
        for (AbstractProtocolRegistration registration : getProtocol().getAllInvestigatorRegistrations()) {
            listings.add(new RegistrationListing(registration));
        }
        Collections.sort(listings);
        return listings;
    }

    /**
     * Enter Action.
     *
     * @return SUCCESS
     */
    @Action(value = "investigatorsTab", results = @Result(location = "investigators_tab.jsp"))
    public String enterPage() {
        return SUCCESS;
    }

    /**
     * @return json response for the investigator selection.
     */
    public Person getInvestigator() {
        return investigator;
    }

    /**
     * @param investigator the investigator to set
     */
    public void setInvestigator(Person investigator) {
        this.investigator = investigator;
    }

    /**
     * @return investigator selected from the autocompleter.
     */
    public String getSelectedPersonExternalId() {
        return selectedPersonExternalId;
    }

    /**
     * @param selectedPersonExternalId investigator selected from the autocompleter.
     */
    public void setSelectedPersonExternalId(String selectedPersonExternalId) {
        this.selectedPersonExternalId = selectedPersonExternalId;
    }

    public List<Long> getInvitedRegistrationIds() {
        return invitedRegistrationIds;
    }

    public void setInvitedRegistrationIds(List<Long> invitedRegistrationIds) {
        this.invitedRegistrationIds = invitedRegistrationIds;
    }

    /**
     * Listing object for a registration.
     */
    @SuppressWarnings("ucd")
    // needs to be public for JSONUtil.serialize()
    public class RegistrationListing implements Serializable, Comparable<RegistrationListing> {
        private static final long serialVersionUID = 1L;
        private final SimpleDateFormat dateFormat;

        private final AbstractProtocolRegistration registration;

        /**
         * @param action action to help with formated strings
         * @param registration the registration for this row.
         */
        RegistrationListing(AbstractProtocolRegistration registration) {
            this.registration = registration;
            dateFormat = new SimpleDateFormat(getText("date.format.timestamp"), Locale.getDefault());
        }

        /**
         * @return registration id.
         */
        public Long getId() {
            return registration.getId();
        }

        /**
         * @return current registration's id (only if revised).
         */
        public Long getCurrentRegistrationId() {
            if (registration.getCurrentRegistration() != null) {
                return registration.getCurrentRegistration().getId();
            }
            return null;
        }

        /**
         * @return investigator in registration.
         */
        public Person getInvestigator() {
            return registration.getProfile().getPerson();
        }

        /**
         * @return registration status text.
         */
        public String getStatusText() {
            if (registration.getStatus() == RegistrationStatus.NOT_STARTED) {
                return registration.getStatus().getDisplay();
            }
            String[] args = {registration.getStatus().getDisplay(), getStatusDateDisplay()};
            return getText("registration.status.with.date", args);
        }

        private String getStatusDateDisplay() {
            return dateFormat.format(registration.getStatusDate());
        }

        /**
         * @return invitation status (or revised) text with date.
         */
        public String getInvitationStatusText() {
            String key;
            String date;

            if (registration.isInvestigatorRegistration() && !registration.isRevisedInvestigatorRegistration()) {
                InvestigatorRegistration investigatorRegistration = (InvestigatorRegistration) registration;
                if (!investigatorRegistration.getRevisedRegistrations().isEmpty()) {
                    RevisedInvestigatorRegistration revisedRegistration = Iterables.getFirst(
                            investigatorRegistration.getRevisedRegistrations(), null);
                    date = dateFormat.format(revisedRegistration.getRevisionDate());
                    key = "registration.invitation.status.revised.with.date";
                    return getText(key, new String[] {date});
                }
            }
            key = registration.getInvitation().getInvitationStatus().getKey() + ".with.date";
            date = dateFormat.format(registration.getInvitation().getInvitationChangeDate());
            return getText(key, new String[] {date});
        }

        /**
         * @return whether or not this registration is able to be activated
         */
        public boolean isAbleToBeReactivated() {
            if (!isRegistrationStatusModifiable()) {
                return false;
            }
            return registration.getStatus() == RegistrationStatus.INACTIVE;
        }

        private boolean isRegistrationStatusModifiable() {
            return isSponsorRepresentative() && !registration.isApprovedWithReactivatedRegistration()
                    && !(registration instanceof RevisedInvestigatorRegistration);
        }

        /**
         * @return whether or not this registration is able to be activated
         */
        public boolean isAbleToBeDeactivated() {
            if (!isRegistrationStatusModifiable()) {
                return false;
            }
            return registration.getStatus() != RegistrationStatus.INACTIVE;
        }

        /**
         * @return whether or not this registration is able to be removed
         */
        public boolean isRemovable() {
            return isSponsorRepresentative() && registration.getApprovalDate() == null && !isRegistrationRevision()
                    && !isApprovedRegistrationReactivation();
        }

        private boolean isRegistrationRevision() {
            Set<RevisedInvestigatorRegistration> revisedRegistrations = registration.getProtocol()
                    .getRevisedInvestigatorRegistrations();
            return Iterables.any(revisedRegistrations, new Predicate<RevisedInvestigatorRegistration>() {
                @Override
                public boolean apply(RevisedInvestigatorRegistration revisedRegistration) {
                    return revisedRegistration.getCurrentRegistration().equals(registration);
                }
            });
        }

        private boolean isApprovedRegistrationReactivation() {
            return registration.isApprovedRegistrationReactivation();
        }

        @Override
        public int compareTo(RegistrationListing right) {
            int result = this.getInvestigator().getSortableName().compareTo(
                    right.getInvestigator().getSortableName());
            if (result != 0) {
                return result;
            }

            if (this.getCurrentRegistrationId() == null) {
                return -1;
            }

            if (right.getCurrentRegistrationId() == null) {
                return 1;
            }

            result = this.getCurrentRegistrationId().compareTo(right.getCurrentRegistrationId());
            if (result != 0 && this.getCurrentRegistrationId().equals(right.getId())) {
                return 1;
            }
            return result;
        }

    }
}
