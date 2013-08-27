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
package gov.nih.nci.firebird.web.action.investigator.profile;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import com.google.common.base.Preconditions;
import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.State;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.inject.Inject;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action class to handle the Modification of Organization Association data including Adding, Removing, and editing.
 */
@Namespace("/investigator/profile/associations/org/ajax")
@Results(value = { @Result(name = "manageAssociatedOrg", location = "manage_org_association.jsp"),
                   @Result(name = "manageAssociatedOrgFields", location = "manage_org_association_fields.jsp") })
@InterceptorRef(value = "profileManagementStack")
@SuppressWarnings("PMD.TooManyMethods")
// Methods broken down for easier comprehension.
public class ManageOrganizationAssociationsAction extends AbstractProfileAction {

    static final String OHRP_FIELD_NAME = "associationOhrp";
    static final String PRACTICE_SITE_TYPE_FIELD_NAME = "practiceSiteType";
    static final String PHONE_NUMBER_FIELD_NAME = "associatedOrganization.phoneNumber";

    private static final long serialVersionUID = 1L;

    static final String RETURN_MANAGE_ASSOCIATION = "manageAssociatedOrg";
    static final String RETURN_MANAGE_ASSOCIATION_FIELDS = "manageAssociatedOrgFields";
    private static final String RESOURCE = "associatedOrganization";
    private final StateLookupService stateLookup;
    private final CountryLookupService countryLookup;
    private List<Country> countries;
    private List<State> states;
    private Organization associatedOrganization = new Organization();
    private OrganizationRoleType associationType;
    private String associationOhrp;
    private PracticeSiteType practiceSiteType;
    private String searchKey;
    private boolean ohrpRequired;

    /**
     * Constructor.
     *
     * @param profileService the profileService to set
     * @param stateLookup the stateLookupService to set
     * @param countryLookup country lookup service.
     */
    @Inject
    public ManageOrganizationAssociationsAction(InvestigatorProfileService profileService,
            StateLookupService stateLookup, CountryLookupService countryLookup) {
        super(profileService);
        this.stateLookup = stateLookup;
        this.countryLookup = countryLookup;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (StringUtils.isNotBlank(getSearchKey())) {
            try {
                setAssociatedOrganization(getOrganizationSearchService().getOrganization(getSearchKey()));
            } catch (UnavailableEntityException e) {
                addActionError(getText("organization.search.selected.organization.unavailable"));
            }
        }
        states = stateLookup.getAll();
        countries = countryLookup.getAll();
    }

    /**
     * Validates that the user has a profile and directs traffic to the Manage Associations ajax page.
     *
     * @return the struts forward.
     */
    @Actions({
            @Action(value = "manageAssociatedOrganizationAjaxEnter", results = @Result(
                    location = "manage_org_association.jsp")),
            @Action(value = "removeAssociatedOrganizationEnter", results = @Result(
                    location = "manage_org_association_delete.jsp")) })
    public String manageOrganizationAssociationEnter() {
        Preconditions.checkNotNull(getProfile(), "User Profile does not exist! You must create a "
                                                         + "user profile before editing an organization.");
        return SUCCESS;
    }

    /**
     * Directs traffic to the Manage Associations ajax page. Nes ID
     * corresponds to either an existing Association or the nesID of a queried Organization
     *
     * @return the struts forward.
     */
    @Action(value = "selectAssociatedOrganization", results = {
            @Result(name = RETURN_MANAGE_ASSOCIATION, location = "manage_org_association.jsp"),
            @Result(name = RETURN_MANAGE_ASSOCIATION_FIELDS, location = "manage_org_association_fields.jsp") })
    public String selectAssociatedOrganization() {
        checkForAssociation();

        if (hasActionErrors()) {
            return RETURN_MANAGE_ASSOCIATION;
        } else if (!getAssociatedOrganization().hasNesRecord()) {
            setAssociatedOrganization(new Organization());
        } else if (!isAdditionalDataRequired()) {
            return saveAction();
        }
        return RETURN_MANAGE_ASSOCIATION_FIELDS;
    }

    private void checkForAssociation() {
        for (OrganizationAssociation association : getProfile().getOrganizationAssociations(getAssociationType())) {
            if (association.getOrganizationRole().getOrganization().equals(associatedOrganization)) {
                addActionError(getText("organization.association.error.existing.association"));
                return;
            }
        }
    }

    private boolean isAdditionalDataRequired() {
        boolean isPracticeSite = getAssociationType() == OrganizationRoleType.PRACTICE_SITE;
        if (isPracticeSite) {
            PracticeSite existingPracticeSite = (PracticeSite) getAssociatedOrganization().getRole(associationType);
            if (existingPracticeSite != null) {
                setAssociationOhrp(existingPracticeSite.getDataField());
            }
            return true;
        }
        return false;
    }

    /**
     * Saves the Investigator Profile to the db and the Organization / association to NES.
     *
     * @return the struts forward
     */
    @Validations(customValidators = { @CustomValidator(type = "hibernate", fieldName = RESOURCE, parameters = {
            @ValidationParameter(name = "resourceKeyBase", value = "organization"),
            @ValidationParameter(name = "excludes", value = "nesId") }) },
            fieldExpressions = {
                @FieldExpressionValidator(
                        fieldName = "associatedOrganization.postalAddress.stateOrProvince",
                        expression = "associatedOrganization.postalAddress.stateOrProvinceValid",
                        key = "stateOrProvince.required") })
    @Action(value = "save", results = { @Result(name = INPUT, location = "manage_org_association_fields.jsp") })
    public String saveOrganizationAjax() {
        return validateFields() ? saveAction() : INPUT;
    }

    private boolean validateFields() {
        Preconditions.checkNotNull(associationType,
                                          "There is no Association Type set, a parameter must have been lost.");
        return isOhrpNumberValid() & isPracticeSiteTypeValid() & isPhoneNumberValid();
    }

    private boolean isOhrpNumberValid() {
        if ((!isOhrpRequired() && getAssociationOhrp() == null)
                || PracticeSite.isValidOhrpNumber(getAssociationOhrp())) {
            return true;
        } else {
            addFieldError(OHRP_FIELD_NAME, getText("organization.association.error.ohrp.invalid"));
            return false;
        }
    }

    private boolean isPracticeSiteTypeValid() {
        if (associationType == OrganizationRoleType.PRACTICE_SITE && associatedOrganization.getNesId() == null
                && getPracticeSiteType() == null) {
            addFieldError(PRACTICE_SITE_TYPE_FIELD_NAME,
                    getText("organization.association.error.practice.site.type.required"));
            return false;
        }
        return true;
    }

    private boolean isPhoneNumberValid() {
        boolean isCreatingNew = getAssociatedOrganization().getNesId() == null;
        if (isCreatingNew && getProfile().getUser().isCtepUser()
                && getAssociationType() == OrganizationRoleType.PRACTICE_SITE
                && isEmpty(getAssociatedOrganization().getPhoneNumber())) {
            addFieldError(PHONE_NUMBER_FIELD_NAME, getText("phoneNumber.required"));
            return false;
        }
        return true;
    }

    /**
     * actual work to be done when the save action is called.
     *
     * @return FirebirdUIConstants.RETURN_CLOSE_DIALOG
     */
    protected String saveAction() {
        try {
            saveAssociation();
        } catch (AssociationAlreadyExistsException e) {
            addActionError(getText("organization.association.error.existing.association"));
            return INPUT;
        } catch (ValidationException e) {
            return handleValidationException(e, RESOURCE);
        }
        return closeDialog();
    }

    private void saveAssociation() throws ValidationException, AssociationAlreadyExistsException {
        switch (associationType) {
            case PRACTICE_SITE:
                getProfileService().addAssociatedPracticeSite(getProfile(), associatedOrganization,
                                                              getAssociationOhrp(), practiceSiteType);
                break;
            case IRB:
                getProfileService().addAssociatedInstitutionalReviewBoard(getProfile(), associatedOrganization);
                break;
            case CLINICAL_LABORATORY:
                getProfileService().addAssociatedClinicalLab(getProfile(), associatedOrganization);
                break;
            default:
                throw new IllegalArgumentException("This block should not be reachable!");
        }
    }

    /**
     * Ajax action to save the updated OHRP number.
     *
     * @return NONE
     */
    @Action(value = "updateOhrp",
            results = { @Result(type = "json", name = INPUT, params = { "root", "actionErrors" }) })
    public String updateOhrp() {
        checkOrganizationValid();
        checkOhrpValid();

        OrganizationAssociation association = findAssociation();

        if (hasActionErrors()) {
            return INPUT;
        }

        getProfileService().updateAssociationOhrp(association, associationOhrp);
        return NONE;
    }

    private void checkOrganizationValid() {
        if (getAssociatedOrganization().getId() == null || !getAssociatedOrganization().hasNesRecord()) {
            addActionError(getText("organization.association.error.ohrp.update"));
        }
    }

    private void checkOhrpValid() {
        if (!PracticeSite.isValidOhrpNumber(getAssociationOhrp())) {
            addActionError(getText("organization.association.error.ohrp.invalid"));
        }
    }

    private OrganizationAssociation findAssociation() {
        if (getAssociationType() != null) {
            for (OrganizationAssociation association : getProfile().getOrganizationAssociations(getAssociationType())) {
                if (association.getOrganizationRole().getOrganization().equals(getAssociatedOrganization())) {
                    return association;
                }
            }
        }
        addActionError(getText("organization.association.error.invalid.selection"));
        return null;
    }

    /**
     * Remove an association from the investigator's profile matching the organization id
     * and type passed in from the page.
     *
     * @return the struts forward.
     */
    @Action("removeOrganizationAssociation")
    public String removeOrganizationAssociation() {
        OrganizationAssociation association = findAssociation();
        if (association != null) {
            getProfileService().deleteAssociatedOrganization(getProfile(), association);
        }
        return closeDialog();
    }

    /**
     * @param nesId the nesId of the association to set
     */
    public void setNesId(String nesId) {
        associatedOrganization.setNesId(nesId);
    }

    /**
     * @return the nesId of the association
     */
    public String getNesId() {
        return associatedOrganization.getNesId();
    }

    /**
     * @param associatedOrganization the associatedOrganization to set
     */
    public void setAssociatedOrganization(Organization associatedOrganization) {
        this.associatedOrganization = associatedOrganization;
    }

    /**
     * @return the associatedOrganization
     */
    public Organization getAssociatedOrganization() {
        return associatedOrganization;
    }

    /**
     * @return the associationType
     */
    public OrganizationRoleType getAssociationType() {
        return associationType;
    }

    /**
     * @param associationType the associationType to set
     */
    public void setAssociationType(OrganizationRoleType associationType) {
        this.associationType = associationType;
    }

    /**
     * @return The Practice Site sub type.
     */
    public PracticeSiteType getPracticeSiteType() {
        return practiceSiteType;
    }

    /**
     * @param practiceSiteType the subtype of the Practice Site
     */
    public void setPracticeSiteType(PracticeSiteType practiceSiteType) {
        this.practiceSiteType = practiceSiteType;
    }

    /**
     * @return the associationDataField
     */
    public String getAssociationOhrp() {
        return associationOhrp;
    }

    /**
     * @param associationOhrp the associationOhrp to set
     */
    public void setAssociationOhrp(String associationOhrp) {
        this.associationOhrp = associationOhrp;
    }

    /**
     * @return the searchKey
     */
    public String getSearchKey() {
        return searchKey;
    }

    /**
     * @param searchKey the searchKey to set
     */
    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    /**
     * @return the states
     */
    public List<State> getStates() {
        return states;
    }

    /**
     * @return the list of all countries.
     */
    public List<Country> getCountries() {
        return countries;
    }

    /**
     * @return boolean value indicating if the Organization requires a OHRP number.
     */
    public boolean isOhrpRequired() {
        return ohrpRequired;
    }

    /**
     * When you are adding Practice Sites through the Protocol Registration page the OHRP # is required, otherwise
     * it is an optional value. This parameter is set via url parameter when the user is clicking the Add New button.
     *
     * @param ohrpRequired if the Organization requires a OHRP number.
     */
    public void setOhrpRequired(boolean ohrpRequired) {
        this.ohrpRequired = ohrpRequired;
    }
}