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
package gov.nih.nci.firebird.web.action.investigator.profile;

import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.State;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
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
 * Action class that handles the addition of new Subinvestigators to an Investigator's Profile.
 */
@Namespace(AddSubinvestigatorAction.NAMESPACE)
@Results(value = {
        @Result(name = AddSubinvestigatorAction.RETURN_SEARCH,
                location = AddSubinvestigatorAction.JSP_SEARCH),
        @Result(name = AddSubinvestigatorAction.RETURN_FIELDS,
                location = AddSubinvestigatorAction.JSP_FIELDS) })
@InterceptorRef(value = "profileManagementStack")
public class AddSubinvestigatorAction extends AbstractProfileAction {
    private static final long serialVersionUID = 1L;

    private static final String JSP_PATH = "/WEB-INF/content";
    @SuppressWarnings("ucd")
    // annotations access these
    static final String NAMESPACE = "/investigator/profile/associations/subinvestigators/ajax";
    @SuppressWarnings("ucd")
    // annotations access these
    static final String JSP_FIELDS = JSP_PATH + NAMESPACE +  "/manage_subinvestigator_association_fields.jsp";
    @SuppressWarnings("ucd")
    // annotations access these
    static final String JSP_SEARCH = JSP_PATH + NAMESPACE + "/manage_subinvestigator_association.jsp";

    static final String RETURN_SEARCH = "search";
    static final String RETURN_FIELDS = "fields";
    private static final String RESOURCE = "subinvestigator";
    private Person subinvestigator;
    private String selectedPersonExternalId;
    private final StateLookupService stateLookup;
    private final CountryLookupService countryLookup;
    private List<Country> countries;
    private List<State> states;

    /**
     * Constructor.
     *
     * @param profileService the profileService to set
     * @param stateLookup the stateLookupService to set
     * @param countryLookup country lookup service.
     */
    @Inject
    public AddSubinvestigatorAction(InvestigatorProfileService profileService,
            StateLookupService stateLookup, CountryLookupService countryLookup) {
        super(profileService);
        this.stateLookup = stateLookup;
        this.countryLookup = countryLookup;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (!StringUtils.isEmpty(getSelectedPersonExternalId())) {
            setSubinvestigator(getPerson(getSelectedPersonExternalId()));
        }
        setStates(stateLookup.getAll());
        countries = countryLookup.getAll();
    }

    /**
     * Validates that the user has a profile and directs traffic to the Manage Associations ajax page.
     *
     * @return the struts forward.
     */
    @Action("enterSearch")
    public String manageSubinvestigatorAssociationAjaxEnter() {
        if (getProfile() == null) {
            throw new IllegalArgumentException("User Profile does not exist! "
                    + "You must create a user profile before editing an person.");
        }
        return RETURN_SEARCH;
    }

    /**
     * Validates that the user has an associated person object (which may be new)
     * and directs traffic to the Manage Associations ajax page.
     *
     * @return the struts forward.
     */
    @Action("enterFields")
    public String manageSubinvestigatorAssociationFields() {
        if (getSubinvestigator() == null) {
            subinvestigator = new Person();
        }
        return RETURN_FIELDS;
    }

    /**
     * Saves the Investigator Profile to the db and the Person / association to NES.
     *
     * @return the struts forward
     */
    @Validations(customValidators = { @CustomValidator(type = "hibernate", fieldName = RESOURCE, parameters = {
            @ValidationParameter(name = "resourceKeyBase", value = "person"),
            @ValidationParameter(name = "excludes", value = "externalId") }) },
            fieldExpressions = {
                @FieldExpressionValidator(fieldName = "subinvestigator.postalAddress.stateOrProvince",
                        expression = "subinvestigator.postalAddress.stateOrProvinceValid",
                        key = "stateOrProvince.required") })
    @Actions({
        @Action(value = "save", results = { @Result(name = INPUT, location = JSP_FIELDS) }),
        @Action(value = "select", results = { @Result(name = INPUT, location = JSP_SEARCH) })
    })
    public String saveSubinvestigatorAjax() {
        return saveAction();
    }

    /**
     * actual work to be done when the save action is called.
     *
     * @return FirebirdUIConstants.RETURN_CLOSE_DIALOG
     */
    protected String saveAction() {
        try {
            getProfileService().addSubInvestigator(getProfile(), subinvestigator);
        } catch (AssociationAlreadyExistsException e) {
            if (errorOnAssociationAlreadyExistsException()) {
                handleAssociationAlreadyExists();
                return INPUT;
            }
        } catch (ValidationException e) {
            return handleValidationException(e);
        }

        return closeDialog();
    }

    /**
     * @return whether or not it is an error if an AssociationAlreadyExistsException is caught
     */
    protected boolean errorOnAssociationAlreadyExistsException() {
        return true;
    }

    private void handleAssociationAlreadyExists() {
        addActionError(getText("duplicate.subinvestigator.error"));
    }

    /**
     * @param states the states to set
     */
    public void setStates(List<State> states) {
        this.states = states;
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
     * @param subinvestigator the subinvestigator to set
     */
    public void setSubinvestigator(Person subinvestigator) {
        this.subinvestigator = subinvestigator;
    }

    /**
     * @return the subinvestigator
     */
    public Person getSubinvestigator() {
        return subinvestigator;
    }

    /**
     * @param selectedPersonExternalId the selectedPersonExternalId to set
     */
    public void setSelectedPersonExternalId(String selectedPersonExternalId) {
        this.selectedPersonExternalId = selectedPersonExternalId;
    }

    /**
     * @return the selectedPersonExternalId
     */
    public String getSelectedPersonExternalId() {
        return selectedPersonExternalId;
    }
}