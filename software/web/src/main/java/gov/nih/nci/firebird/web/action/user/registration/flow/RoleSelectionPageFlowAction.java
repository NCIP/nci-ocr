package gov.nih.nci.firebird.web.action.user.registration.flow;

import static gov.nih.nci.firebird.web.common.FirebirdUIConstants.REGISTRATION_FLOW_CONTROLLER;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.service.account.AccountManagementService;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowController;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep;

import java.util.EnumSet;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;

/**
 * User Registration Action that handles necessary actions when Selecting Roles.
 */
@Namespace("/user/registration/flow/roleSelection")
@Results({ @Result(name = ActionSupport.INPUT, location = "../role_selection.jsp") })
@SuppressWarnings({ "PMD.TooManyMethods" })
//Excessive methods due to factoring out functionality for readability.
public class RoleSelectionPageFlowAction extends AbstractPageFlowAction {
    private static final long serialVersionUID = 1L;
    private static final EnumSet<RegistrationFlowStep> UNNECESSARY_STEPS_FOR_ADD_ROLE = EnumSet
            .of(RegistrationFlowStep.PERSON_SELECTION);

    private List<UserRoleType> selectedRoles = Lists.newArrayList();
    private RegistrationFlowController flow;
    private final EnumSet<RegistrationFlowStep> updatedFlowSteps = EnumSet.noneOf(RegistrationFlowStep.class);

    /**
     * @param accountService the account service
     */
    @Inject
    public RoleSelectionPageFlowAction(AccountManagementService accountService) {
        super(accountService);
    }

    @Override
    public void prepare() {
        super.prepare();
        flow = (RegistrationFlowController) getSession().getAttribute(REGISTRATION_FLOW_CONTROLLER);
    }

    @Override
    @Action("enter")
    public String enterAction() {
        checkAccountConfigurationData();
        selectedRoles.addAll(getAccountConfigurationData().getRoles());
        return INPUT;
    }

    @Actions({
            @Action(value = "nextStep", results = { @Result(type = "chain", params = { "actionName", "enterNextStep",
                    "namespace", "/user/registration/nav" }) }),
            @Action(value = "gotoStep", results = { @Result(type = "chain", params = { "actionName", "enterFlowStep",
                    "namespace", "/user/registration/nav" }) }) })
    @FieldExpressionValidator(fieldName = "selectedRoles", key = "user.registration.role.selection.error",
            expression = "selectedRoles.size() > 0")
    @Override
    public String saveAndProceedNext() {
        return super.saveAndProceedNext();
    }

    @Override
    protected String performSave() {
        EnumSet<UserRoleType> selectedRoleSet = getSetOfRoles();
        if (checkRolesChanged()) {
            handleSelectedRoles(selectedRoleSet);
            cleanupOldRoles(EnumSet.complementOf(selectedRoleSet));
            cleanupOldSteps();
            getAccountConfigurationData().setRoles(selectedRoleSet);
            setupFlowForExistingUser();
            flow.setupFlowBodyWithSteps(updatedFlowSteps, RegistrationFlowStep.ROLE_SELECTION);
        }

        return SUCCESS;
    }

    private EnumSet<UserRoleType> getSetOfRoles() {
        if (selectedRoles.isEmpty()) {
            throw new IllegalArgumentException("The User must have selected at least 1 role to continue!");
        } else {
            return EnumSet.copyOf(selectedRoles);
        }
    }

    private boolean checkRolesChanged() {
        return getAccountConfigurationData().getRoles().size() != selectedRoles.size()
                || !getAccountConfigurationData().getRoles().containsAll(selectedRoles);
    }

    private void handleSelectedRoles(EnumSet<UserRoleType> selectedRoleSet) {
        for (UserRoleType role : selectedRoleSet) {
            if (!isRolePreviouslySelected(role)) {
                setupNewRoles(role);
            }
            updatedFlowSteps.addAll(RegistrationFlowStep.getStepsForRole(role));
        }
    }

    private boolean isRolePreviouslySelected(UserRoleType role) {
        return getAccountConfigurationData().getRoles().contains(role);
    }

    private void setupNewRoles(UserRoleType role) {
        if (UserRoleType.SPONSOR == role) {
            getAccountConfigurationData().getSponsorOrganizations().clear();
            getAccountConfigurationData().getDelegateOrganizations().clear();
        }
    }

    private void cleanupOldRoles(EnumSet<UserRoleType> removedRoles) {
        for (UserRoleType role : removedRoles) {
            if (UserRoleType.REGISTRATION_COORDINATOR == role) {
                getAccountConfigurationData().getSelectedInvestigators().clear();
            } else if (UserRoleType.SPONSOR == role) {
                getAccountConfigurationData().getSponsorOrganizations().clear();
            } else if (UserRoleType.SPONSOR_DELEGATE == role) {
                getAccountConfigurationData().getDelegateOrganizations().clear();
            }
        }
    }

    private void cleanupOldSteps() {
        EnumSet<RegistrationFlowStep> stepsNotInFlow = EnumSet.complementOf(updatedFlowSteps);
        for (RegistrationFlowStep step : stepsNotInFlow) {
            handleOldStep(step);
        }
    }

    private void handleOldStep(RegistrationFlowStep step) {
        switch (step) {
        case PERSON_SELECTION:
            getAccountConfigurationData().setPerson(null);
            break;
        case PRIMARY_ORGANIZATION_SELECTION:
            getAccountConfigurationData().setPrimaryOrganization(null);
            break;
        default:
            //do nothing
        }
    }

    private void setupFlowForExistingUser() {
        if (!isNewUser()) {
            updatedFlowSteps.removeAll(UNNECESSARY_STEPS_FOR_ADD_ROLE);
        }
    }

    /**
     * @return the roles
     */
    public List<UserRoleType> getSelectedRoles() {
        return selectedRoles;
    }

    /**
     * @param roles the roles to set
     */
    public void setSelectedRoles(List<UserRoleType> roles) {
        this.selectedRoles = roles;
    }

    /**
     * @return the Set of roles that are available for the current user to select.
     */
    public EnumSet<UserRoleType> getAvailableRoles() {
        EnumSet<UserRoleType> roles = EnumSet.copyOf(UserRoleType.STANDARD_ROLES);
        configureForExistingUser(roles);
        return roles;
    }

    private void configureForExistingUser(EnumSet<UserRoleType> roles) {
        if (!isNewUser()) {
            roles.removeAll(getCurrentUser().getRoles());
        }
    }

}
