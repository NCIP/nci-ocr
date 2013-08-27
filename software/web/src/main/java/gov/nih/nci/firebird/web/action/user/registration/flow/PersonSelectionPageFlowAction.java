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
package gov.nih.nci.firebird.web.action.user.registration.flow;

import gov.nih.nci.firebird.cagrid.UserDetails;
import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.State;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.account.AccountManagementService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.person.PersonSearchResult;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 *  Page action for handling the Person Selection page of the flow.
 */
@Namespace("/user/registration/flow/personSelection")
@Results({ @Result(name = PersonSelectionPageFlowAction.PERSON_SEARCH, location = "../person_selection_search.jsp"),
        @Result(name = ActionSupport.INPUT, location = "../person_selection_fields.jsp") })
@SuppressWarnings("PMD.TooManyMethods") // requires multiple helper methods not easily factored out into another class
public class PersonSelectionPageFlowAction extends AbstractPageFlowAction {
    private static final long serialVersionUID = 1L;

    static final String CREATE_NEW = "NEW";
    static final String PERSON_SEARCH = "search";
    static final String FIELD_LOCATOR = "accountConfigurationData.person";

    private String selectedPersonKey;
    private String prepopulatedSearchString;
    private String navigationOption;
    private final StateLookupService stateLookup;
    private final PersonService personService;
    private final CountryLookupService countryLookup;
    private List<Country> countries;
    private List<State> states;
    private boolean personAutoselected;

    /**
     * @param personService the person service.
     * @param stateLookup state lookup service.
     * @param countryLookup country lookup service.
     * @param accountService the account management service
     */
    @Inject
    public PersonSelectionPageFlowAction(PersonService personService,
            StateLookupService stateLookup,
            CountryLookupService countryLookup,
            AccountManagementService accountService) {
        super(accountService);
        this.stateLookup = stateLookup;
        this.personService = personService;
        this.countryLookup = countryLookup;
    }

    @Override
    public void prepare() {
        super.prepare();
        states = stateLookup.getAll();
        countries = countryLookup.getAll();
        selectedPersonKey = getExistingPersonId();
    }

    /**
     * Overrides the default nextStep Action to add validation for the person being saved.
     *
     * @return the result of saving the data to the session object.
     */
    @Actions({
            @Action(value = "nextStep", results = { @Result(type = "chain", params = { "actionName", "enterNextStep",
                    "namespace", "/user/registration/nav" }) }),
            @Action(value = "gotoStep", results = { @Result(type = "chain", params = { "actionName", "enterFlowStep",
                    "namespace", "/user/registration/nav" }) }) })
    @Validations(customValidators = { @CustomValidator(type = "hibernate", fieldName = FIELD_LOCATOR, parameters = {
            @ValidationParameter(name = "resourceKeyBase", value = "person"),
            @ValidationParameter(name = "excludes", value = "nesId") }) },
            requiredStrings = { @RequiredStringValidator(fieldName = "accountConfigurationData.person.phoneNumber",
                    key = "phone.number.required") },
            fieldExpressions = {
                @FieldExpressionValidator(
                        fieldName = "accountConfigurationData.person.postalAddress.stateOrProvince",
                        expression = "accountConfigurationData.person == null || "
                            + "accountConfigurationData.person.postalAddress.stateOrProvinceValid",
                        key = "stateOrProvince.required")
            })
    @Override
    public String saveAndProceedNext() {
        try {
            personService.validatePerson(getAccountConfigurationData().getPerson());
            return super.saveAndProceedNext();
        } catch (ValidationException e) {
            return handleValidationException(e, FIELD_LOCATOR);
        }
    }

    @Action(value = "previousStep", results = { @Result(type = CHAIN, params = { ACTION_NAME, "enterPreviousStep",
            NAMESPACE, NAV_NAMESPACE }) })
    @Override
    public String saveAndProceedPrevious() {
        checkAccountConfigurationData();
        removeReturnLinkIfInvalid();
        return SUCCESS;
    }

    private void removeReturnLinkIfInvalid() {
        if (getAccountConfigurationData().getPerson() != null) {
            try {
                personService.validatePerson(getAccountConfigurationData().getPerson());
            } catch (ValidationException e) {
                getFlowController().removeVisitedStep(RegistrationFlowStep.VERIFICATION);
            }
        }
    }

    /**
     * Enter action navigating between showing the fields of data or the search capabilities.
     *
     * @return the struts forward
     */
    @Override
    @Action("enter")
    public String enterAction() {
        checkAccountConfigurationData();
        if (isInitialEntry()) {
            return doInitialEntry();
        } else if (PERSON_SEARCH.equals(navigationOption)) {
            return PERSON_SEARCH;
        } else {
            return INPUT;
        }
    }

    private boolean isInitialEntry() {
        return getAccountConfigurationData().getPerson() == null;
    }

    private String doInitialEntry() {
        List<PersonSearchResult> results = doEmailSearch();
        if (results.size() == 1) {
            getAccountConfigurationData().setPerson(results.get(0).getPerson());
            setPersonAutoselected(true);
            return INPUT;
        } else if (results.size() > 1) {
            setPrepopulatedSearchString(getCurrentGridSessionInformation().getAccount().getEmailAddress());
            return PERSON_SEARCH;
        } else {
            return handleNameSearch();
        }
    }

    private String handleNameSearch() {
        List<PersonSearchResult> results = doNameSearch();
        if (!results.isEmpty()) {
            String firstName = getCurrentGridSessionInformation().getAccount().getFirstName();
            String lastName = getCurrentGridSessionInformation().getAccount().getLastName();
            String nameSearchString = new StrBuilder(lastName).append(", ").append(firstName).toString();
            setPrepopulatedSearchString(nameSearchString);
        }
        return PERSON_SEARCH;
    }

    private List<PersonSearchResult> doNameSearch() {
        Person searchPerson = new Person();
        searchPerson.setFirstName(getCurrentGridSessionInformation().getAccount().getFirstName());
        searchPerson.setLastName(getCurrentGridSessionInformation().getAccount().getLastName());
        return getPersonSearchService().search(searchPerson);
    }

    private List<PersonSearchResult> doEmailSearch() {
        Person searchPerson = new Person();
        searchPerson.setEmail(getCurrentGridSessionInformation().getAccount().getEmailAddress());
        return getPersonSearchService().search(searchPerson);
    }

    @Override
    protected String performSave() {
        String strutsForward = SUCCESS;
        if (StringUtils.isNotEmpty(navigationOption)) {
            strutsForward = handleNavigation();
        } else if (StringUtils.isNotEmpty(selectedPersonKey)) {
            strutsForward = handleExistingPerson();
        } else {
            removeReturnLinkIfInvalid();
        }
        return strutsForward;
    }

    private String handleNavigation() {
        if (CREATE_NEW.equals(navigationOption)) {
            createNewPerson();
        } else if (PERSON_SEARCH.equals(navigationOption)) {
            return PERSON_SEARCH;
        }
        return SUCCESS;
    }

    private void createNewPerson() {
        Person person = new Person();
        UserDetails account = getCurrentGridSessionInformation().getAccount();
        person.setFirstName(account.getFirstName());
        person.setLastName(account.getLastName());
        person.setEmail(account.getEmailAddress());
        getAccountConfigurationData().setPerson(person);
    }

    private String handleExistingPerson() {
        Person selectedPerson = getPersonSearchService().getPerson(getSelectedPersonKey());
        if (selectedPerson == null) {
            selectedPerson = getAccountConfigurationData().getPerson();
        }
        if (getUserService().checkPersonAssociated(selectedPerson)) {
            return handlePersonAlreadyAssociated();
        } else {
            getPersonSearchService().clearResults();
            getAccountConfigurationData().setPerson(selectedPerson);
        }
        return SUCCESS;
    }

    private String handlePersonAlreadyAssociated() {
        addActionError(getText("investigator.profile.alreadyAssociated"));
        return getExistingPersonId() != null ? INPUT : PERSON_SEARCH;
    }

    private String getExistingPersonId() {
        if (getAccountConfigurationData().getPerson() != null) {
            return getAccountConfigurationData().getPerson().getNesId();
        } else {
            return null;
        }
    }

    /**
     * @return the selectedPersonKey
     */
    public String getSelectedPersonKey() {
        return selectedPersonKey;
    }

    /**
     * @param selectedPersonKey the selectedPersonKey to set
     */
    public void setSelectedPersonKey(String selectedPersonKey) {
        this.selectedPersonKey = selectedPersonKey;
    }

    /**
     * @return the prepopulatedSearchString
     */
    public String getPrepopulatedSearchString() {
        return prepopulatedSearchString;
    }

    /**
     * @param prepopulatedSearchString the prepopulatedSearchString to set
     */
    public void setPrepopulatedSearchString(String prepopulatedSearchString) {
        this.prepopulatedSearchString = prepopulatedSearchString;
    }

    /**
     * @param navigationOption the navigationOption to set
     */
    public void setNavigationOption(String navigationOption) {
        this.navigationOption = navigationOption;
    }

    /**
     * @return a List of States.
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
     * @return the personAutoselected
     */
    public boolean isPersonAutoselected() {
        return personAutoselected;
    }

    /**
     * @param personAutoselected the personAutoselected to set
     */
    public void setPersonAutoselected(boolean personAutoselected) {
        this.personAutoselected = personAutoselected;
    }

}
