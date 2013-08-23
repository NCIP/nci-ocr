package gov.nih.nci.firebird.web.action.sponsor.protocol;

import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.State;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.protocol.ProtocolService;

import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.google.inject.Inject;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action class for adding and inviting investigators to a registration.
 */
@Namespace("/sponsor/representative/protocol/ajax")
@InterceptorRef("sponsorRepresentativeProtocolManagementStack")
public class ManageRegistrationInvestigatorsAction extends InvestigatorsTabAction {
    private static final long serialVersionUID = 1L;
    private final StateLookupService stateLookup;
    private final CountryLookupService countryLookup;
    private List<Country> countries;
    private List<State> states;

    /**
     * Constructor.
     *
     * @param protocolService the protocol service
     * @param stateLookup the stateLookup service
     * @param countryLookup country lookup service.
     */
    @Inject
    public ManageRegistrationInvestigatorsAction(ProtocolService protocolService,
            StateLookupService stateLookup, CountryLookupService countryLookup) {
        super(protocolService);
        this.stateLookup = stateLookup;
        this.countryLookup = countryLookup;
    }

    @Override
    public void prepare() {
        super.prepare();
        states = stateLookup.getAll();
        countries = countryLookup.getAll();
    }

    /**
     * A single method used to handle all of the different enter actions.
     *
     * @return SUCCESS
     */
    @Actions(value = {
            @Action(value = "enterCreateInvestigator", results = @Result(location = "investigator_info_new.jsp")),
            @Action(value = "enterInvite", results = @Result(location = "invite.jsp")),
            @Action(value = "enterAddInvestigator", results = @Result(location = "add_investigator.jsp")) })
    public String enterPage() {
        return SUCCESS;
    }

    /**
     * add an investigator to a protocol.
     *
     * @return SUCCESS if all goes well.
     */
    @Action(value = "addInvestigator", results = { @Result(name = SUCCESS, location = "investigator_added.jsp"),
            @Result(name = INPUT, location = "investigator_info_new.jsp") })
    @Validations(customValidators = { @CustomValidator(type = "hibernate", fieldName = "investigator", parameters = {
            @ValidationParameter(name = "resourceKeyBase", value = "person"),
            @ValidationParameter(name = "excludes", value = "externalId") }) }, fieldExpressions = {
            @FieldExpressionValidator(fieldName = "investigator.postalAddress.stateOrProvince",
                    expression = "investigator.postalAddress.stateOrProvinceValid", key = "stateOrProvince.required") })
    public String addInvestigator() {
        checkForExistingRegistration();
        if (!hasActionErrors()) {
            try {
                addInvestigatorToProtocol();
            } catch (ValidationException e) {
                return handleValidationException(e);
            }
        }
        return SUCCESS;
    }

    private void checkForExistingRegistration() {
        for (InvestigatorRegistration reg : getProtocol().getCurrentInvestigatorRegistrations()) {
            Long registrationPersonId = reg.getProfile().getPerson().getId();
            checkIsSameInvestigator(registrationPersonId);
        }
    }

    private void checkIsSameInvestigator(Long registrationPersonId) {
        String[] personNameArg = {getInvestigator().getDisplayName() };

        if (registrationPersonId.equals(getInvestigator().getId())) {
            String msg = getText("sponsor.protocol.investigator.alreadyAdded", personNameArg);
            addActionError(msg);
        }
    }

    private void addInvestigatorToProtocol() throws ValidationException {
        String[] resultMessageReplacementArgs = {getInvestigator().getDisplayName() };
        getProtocolService().addInvestigator(getProtocol(), getInvestigator());
        String msg = getText("sponsor.protocol.investigator.added", resultMessageReplacementArgs);
        addActionMessage(msg);
    }

    /**
     * send an invitation to investigatorsTab involved in the protocol.
     *
     * @return close dialog if all went well.
     */
    @Validations(expressions = @ExpressionValidator(
            expression = "invitedRegistrationIds != null && invitedRegistrationIds.size > 0",
            key = "sponsor.protocol.investigator.invite.no.selection.warning.message"))
    @Action(value = "invite", results = { @Result(name = ERROR, location = "invite.jsp") })
    public String invite() {
        CollectionUtils.forAllDo(getProtocol().getCurrentInvestigatorRegistrations(), new Closure() {
            @Override
            public void execute(Object registration) {
                InvestigatorRegistration investigatorRegistration = (InvestigatorRegistration) registration;
                if (getInvitedRegistrationIds().contains(investigatorRegistration.getId())) {
                    getProtocolService().invite(investigatorRegistration);
                }
            }
        });
        return closeDialog();
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
}