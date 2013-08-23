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
package gov.nih.nci.firebird.web.action.investigator.annual.registration;

import static com.google.common.base.Preconditions.*;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.AnnualRegistrationType;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.InvestigatorStatus;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.web.action.investigator.profile.AbstractProfileAction;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Investigator's browse annual registrations action.
 */
@Namespace("/investigator/annual/registration")
public class BrowseAnnualRegistrationsAction extends AbstractProfileAction {

    private static final long serialVersionUID = 1L;
    private final AnnualRegistrationService annualRegistrationService;

    /**
     * @return whether or not the current user can create a registration
     */
    public boolean isCreateRegistrationAllowed() {
        return getProfile().canCreateAnnualRegistration();
    }

    /**
     * @param profileService profile service
     * @param annualRegistrationService annual registration service
     */
    @Inject
    public BrowseAnnualRegistrationsAction(InvestigatorProfileService profileService,
            AnnualRegistrationService annualRegistrationService) {
        super(profileService);
        this.annualRegistrationService = annualRegistrationService;
    }

    /**
     * Navigate to the Required Forms Tab.
     *
     * @return the struts forward.
     */
    @Action(value = "enterBrowseAnnualRegistrations", results = @Result(location = "browse_annual_registrations.jsp"))
    public String enter() {
        return SUCCESS;
    }

    /**
     * Navigate to the Required Forms Tab.
     *
     * @return the struts forward.
     */
    @Action(value = "createAnnualRegistration", results = @Result(location = "browse_annual_registrations.jsp"))
    public String createAnnualRegistration() {
        checkArgument(isCreateRegistrationAllowed(), "User is not able to create an annual registration");
        annualRegistrationService.createRegistration(getProfile());
        return SUCCESS;
    }

    /**
     * @return true if the Withdraw button should be displayed.
     */
    public boolean isWithdrawButtonVisible() {
        return getCurrentUser().isInvestigator()
                && getCurrentUser().getInvestigatorRole().getWithdrawalRequest() == null
                && getCurrentUser().getInvestigatorRole().getDisqualificationReason() == null;
    }

    /**
     * @return true if the Withdraw button should be displayed.
     */
    public boolean isReactivateButtonVisible() {
        return getCurrentUser().isInvestigator()
                && getCurrentUser().getInvestigatorRole().getStatus() == InvestigatorStatus.WITHDRAWN
                && !isReactivated() && getCurrentUser().getInvestigatorRole().getDisqualificationReason() == null;
    }

    private boolean isReactivated() {
        AnnualRegistration currentRegistration = getCurrentUser().getInvestigatorRole().getProfile()
                .getCurrentAnnualRegistration();
        return currentRegistration != null
                && currentRegistration.getAnnualRegistrationType() == AnnualRegistrationType.REACTIVATED
                && !currentRegistration.isFinalized();
    }

    /**
     * @return success, json grid data.
     * @throws JSONException if a serialization issue occurs
     */
    public String getGridTableData() throws JSONException {
        return JSONUtil.serialize(getAnnualRegistrationListings(), null, null, false, true, false);
    }

    /**
     * @return annual registration listings
     */
    public List<AnnualRegistrationListing> getAnnualRegistrationListings() {
        List<AnnualRegistrationListing> annualRegistrationListings = Lists.newArrayList();
        for (AnnualRegistration registration : getAnnualRegistrations()) {
            annualRegistrationListings.add(new AnnualRegistrationListing(registration));
        }
        Collections.sort(annualRegistrationListings);
        return annualRegistrationListings;
    }

    public boolean isReadOnly() {
        return !getCurrentUser().isCtepUser();
    }

    private Set<AnnualRegistration> getAnnualRegistrations() {
        return getProfile().getAnnualRegistrations();
    }

    /**
     * Listing object for an annual registration.
     */
    @SuppressWarnings("ucd")
    // needs to be public for JSONUtil.serialize()
    public class AnnualRegistrationListing implements Comparable<AnnualRegistrationListing> {
        private final Long id;
        private final String type;
        private final RegistrationStatus status;
        private final Date submissionDate;
        private final Date dueDate;
        private final boolean submittable;
        private final List<AnnualRegistrationFormListing> annualRegistrationFormListings;

        /**
         * @param registration annual registration
         */
        AnnualRegistrationListing(AnnualRegistration registration) {
            this.id = registration.getId();
            this.type = registration.getAnnualRegistrationType().getDisplay();
            this.status = registration.getStatus();
            this.submissionDate = registration.getLastSubmissionDate();
            this.dueDate = registration.getDueDate();
            this.annualRegistrationFormListings = getAnnualRegistrationFormListings(registration);
            this.submittable = registration.isSubmittable();
        }

        private List<AnnualRegistrationFormListing> getAnnualRegistrationFormListings(AnnualRegistration registration) {
            List<AnnualRegistrationFormListing> listings = Lists.newArrayList();
            for (AbstractRegistrationForm form : registration.getForms()) {
                listings.add(new AnnualRegistrationFormListing(form));
            }
            return listings;
        }

        /**
         * @return the id
         */
        public Long getId() {
            return id;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @return the status
         */
        public RegistrationStatus getStatus() {
            return status;
        }

        /**
         * @return the submissionDate
         */
        public Date getSubmissionDate() {
            return submissionDate;
        }

        /**
         * @return the dueDate
         */
        public Date getDueDate() {
            return dueDate;
        }

        /**
         * @return is submittable
         */
        public boolean isSubmittable() {
            return submittable;
        }

        /**
         * @return the annualRegistrationFormListings
         */
        public List<AnnualRegistrationFormListing> getAnnualRegistrationFormListings() {
            return annualRegistrationFormListings;
        }

        @Override
        public int compareTo(AnnualRegistrationListing otherListing) {
            Integer sortValueThis = getSortValue(this);
            Integer sortValueOther = getSortValue(otherListing);
            if (!sortValueThis.equals(sortValueOther)) {
                return sortValueThis.compareTo(sortValueOther);
            } else {
                return compareDueDates(otherListing);
            }
        }

        private Integer getSortValue(AnnualRegistrationListing listing) {
            switch (listing.getStatus()) {
            case RETURNED:
                return 0;
            case IN_PROGRESS:
                return 1;
            case COMPLETED:
                return 2;
            default:
                return -1;
            }
        }

        private int compareDueDates(AnnualRegistrationListing otherListing) {
            return new CompareToBuilder().append(getDueDate(), otherListing.getDueDate()).toComparison();
        }

    }

    /**
     * Listing object for an annual registration form.
     */
    @SuppressWarnings("ucd")
    // needs to be public for JSONUtil.serialize()
    public final class AnnualRegistrationFormListing {
        private final Long id;
        private final Long registrationId;
        private final String title;
        private final String name;
        private final FormOptionality optionality;
        private final Date lastUpdateDate;
        private final FormStatus status;

        /**
         * @param form annual registration form
         */
        AnnualRegistrationFormListing(AbstractRegistrationForm form) {
            this.id = form.getId();
            this.registrationId = form.getRegistration().getId();
            this.title = form.getFormType().getDescription();
            this.name = form.getFormType().getName();
            this.optionality = form.getFormOptionality();
            this.lastUpdateDate = form.getFormStatusDate();
            this.status = form.getFormStatus();
        }

        /**
         * @return the id
         */
        public Long getId() {
            return id;
        }

        /**
         * @return the registrationId
         */
        public Long getRegistrationId() {
            return registrationId;
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the optionality
         */
        public FormOptionality getOptionality() {
            return optionality;
        }

        /**
         * @return the lastUpdateDate
         */
        public Date getLastUpdateDate() {
            return lastUpdateDate;
        }

        /**
         * @return the status
         */
        public FormStatus getStatus() {
            return status;
        }

    }
}
