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
package gov.nih.nci.firebird.web.action.investigator.registration;

import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.RevisedInvestigatorRegistration;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.web.action.AbstractProtocolRegistrationAction;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * Action used to display and respond to protocol registration invitations.
 */
@Namespace("/investigator/registration")
@Results(value = { @Result(name = RegistrationAction.VIEW_RESULT, location = "view_registration.jsp"),
        @Result(name = RegistrationAction.BROWSE_RESULT, location = "browse_registrations.jsp") })
@InterceptorRef(value = "registrationManagementStack")
public class RegistrationAction extends AbstractProtocolRegistrationAction {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("ucd")
    // annotations access these
    static final String VIEW_RESULT = "view";
    @SuppressWarnings("ucd")
    // annotations access these
    static final String BROWSE_RESULT = "browse";

    /**
     * Creates an action instance.
     *
     * @param registrationService registration service
     * @param profileService profile service
     */
    @Inject
    public RegistrationAction(ProtocolRegistrationService registrationService,
            InvestigatorProfileService profileService) {
        super(registrationService, profileService);
    }

    /**
     * View the current invitation.
     *
     * @return the view page.
     */
    @Action("enterRegistrations")
    public String enterRegistrations() {
        if (!isRegistrationDeleted()) {
            updateRegistrationStatus();
            return VIEW_RESULT;
        }

        return BROWSE_RESULT;
    }

    /**
     * Ajax action to save the updated OHRP number.
     *
     * @return NONE
     */
    @Action(value = "lastProtocolUpdateAction",
            results = @Result(type = "json", params = { "root", "protocolUpdated" }))
    public String lastProtocolUpdateAction() {
        return SUCCESS;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        getRegistration().setId(id);
    }

    /**
     * @return the id
     */
    public Long getId() {
        return getRegistration().getId();
    }

    /**
     * @return registration listing objects
     */
    public List<RegistrationListing> getRegistrationListings() {
        List<RegistrationListing> listings = Lists.newArrayList();
        for (AbstractProtocolRegistration registration : getProfile().getCurrentProtocolRegistrations()) {
            if (registration.getInvitation().getInvitationStatus() != InvitationStatus.NOT_INVITED) {
                listings.add(new RegistrationListing(registration));
            }
        }
        Collections.sort(listings);
        return listings;
    }



    private void updateRegistrationStatus() {
        if ((getRegistration().getStatus() == RegistrationStatus.NOT_STARTED || getRegistration().getInvitation()
                .getInvitationStatus() == InvitationStatus.REACTIVATED)
                && !getRegistration().isApprovedRegistrationReactivation()) {
            getRegistration().getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
            getRegistration().setStatus(RegistrationStatus.IN_PROGRESS);
            getRegistrationService().save(getRegistration());
        }
    }

    /**
     * @return Returns a TreeSet (Sorted) of the Form Types to create the tabs for the registration view.
     */
    public Set<FormType> getForms() {
        return Sets.newTreeSet(getRegistration().getFormConfiguration().keySet());
    }

    /**
     * Table listing for a registration.
     */
    @SuppressWarnings("ucd")
    // needs to be public for JSONUtil.serialize()
    public class RegistrationListing implements Comparable<RegistrationListing> {
        private final AbstractProtocolRegistration registration;

        /**
         * @param registration The Registration this listing is based on.
         */
        RegistrationListing(AbstractProtocolRegistration registration) {
            this.registration = registration;
        }

        /**
         * @return the id
         */
        public Long getId() {
            return registration.getId();
        }

        /**
         * @return the protocolTitle
         */
        public String getTitle() {
            return registration.getProtocol().getProtocolTitle();
        }

        /**
         * @return the protocolNumber
         */
        public String getProtocolNumber() {
            return registration.getProtocol().getProtocolNumber();
        }

        /**
         * @return the sponsor
         */
        public String getSponsor() {
            return registration.getProtocol().getSponsor().getName();
        }

        /**
         * @return the status
         */
        public RegistrationStatus getStatus() {
            return registration.getStatus();
        }

        /**
         * @return type of registration
         */
        public String getType() {
            if (isInvestigatorRegistration()) {
                return getText("label.investigator");
            } else {
                return getText("label.subinvestigator");
            }
        }

        /**
         * @return if this listing represents an investigator registration or not.
         */
        public boolean isInvestigatorRegistration() {
            return registration.isInvestigatorRegistration();
        }

        /**
         * @return current registration's id.
         */
        public Long getCurrentRegistrationId() {
            if (registration.getCurrentRegistration() != null) {
                return registration.getCurrentRegistration().getId();
            }
            return null;
        }

        /**
         * @return all revised registrations
         */
        public List<RegistrationListing> getRevisedRegistrations() {
            if (!registration.isInvestigatorRegistration()) {
                return Collections.emptyList();
            }
            List<RegistrationListing> listings = Lists.newArrayList();
            for (RevisedInvestigatorRegistration revisedRegistration : ((InvestigatorRegistration) registration)
                    .getRevisedRegistrations()) {
                listings.add(new RegistrationListing(revisedRegistration));
            }
            Collections.sort(listings);
            return listings;
        }

        public boolean isSubmittable() {
            return registration.isSubmittable();
        }

        @Override
        public int compareTo(RegistrationListing right) {
            int result = this.getProtocolNumber().compareTo(right.getProtocolNumber());
            if (result != 0) {
                return result;
            }

            if (this.getCurrentRegistrationId() == null) {
                return -1;
            }

            if (right.getCurrentRegistrationId() == null) {
                return 1;
            }

            return this.getCurrentRegistrationId().compareTo(right.getCurrentRegistrationId());
        }

    }
}
