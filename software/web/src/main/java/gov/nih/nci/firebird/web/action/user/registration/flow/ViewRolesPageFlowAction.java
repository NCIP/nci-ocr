package gov.nih.nci.firebird.web.action.user.registration.flow;

import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.service.account.AccountManagementService;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * User Registration Action that handles necessary actions when Selecting Roles.
 */
@Namespace("/user/registration/flow/viewRoles")
@Results({ @Result(location = "../view_roles.jsp") })
public class ViewRolesPageFlowAction extends AbstractPageFlowAction {
    
    private static final long serialVersionUID = 1L;

    /**
     * @param accountService the account service
     */
    @Inject
    public ViewRolesPageFlowAction(AccountManagementService accountService) {
        super(accountService);
    }

    @Override
    @Action("enter")
    public String enterAction() {
        checkAccountConfigurationData();
        return SUCCESS;
    }

    @Actions({
            @Action(value = "nextStep", results = { @Result(type = "chain", params = { "actionName", "enterNextStep",
                    "namespace", "/user/registration/nav" }) }),
            @Action(value = "gotoStep", results = { @Result(type = "chain", params = { "actionName", "enterFlowStep",
                    "namespace", "/user/registration/nav" }) }) })
    @Override
    public String saveAndProceedNext() {
        return super.saveAndProceedNext();
    }

    @Override
    protected String performSave() {
        return SUCCESS;
    }

    /**
     * @return the list of roles associated with this user for display.
     */
    public List<String> getRoleNames() {
        List<String> roleNames = Lists.newArrayList();
        if (getAccountConfigurationData().getRoles().contains(UserRoleType.INVESTIGATOR)) {
            roleNames.add(UserRoleType.INVESTIGATOR.getDisplay());
        }
        if (getAccountConfigurationData().getRoles().contains(UserRoleType.REGISTRATION_COORDINATOR)) {
            roleNames.add(UserRoleType.REGISTRATION_COORDINATOR.getDisplay());
        }
        for (Organization sponsor : getAccountConfigurationData().getSponsorOrganizations()) {
            roleNames.add(getText("user.registration.sponsor.role.display", 
                    new String[] {UserRoleType.SPONSOR.getDisplay(), sponsor.getName()}));
        }
        for (Organization sponsor : getAccountConfigurationData().getDelegateOrganizations()) {
            roleNames.add(getText("user.registration.sponsor.role.display", 
                    new String[] {UserRoleType.SPONSOR_DELEGATE.getDisplay(), sponsor.getName()}));
        }
        return roleNames;
    }


}
