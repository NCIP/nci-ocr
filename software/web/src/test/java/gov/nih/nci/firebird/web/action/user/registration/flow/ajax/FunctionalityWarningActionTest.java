package gov.nih.nci.firebird.web.action.user.registration.flow.ajax;

import static gov.nih.nci.firebird.data.user.UserRoleType.*;
import static gov.nih.nci.firebird.web.common.FirebirdUIConstants.*;
import static gov.nih.nci.firebird.nes.NesIdTestUtil.getNesIdExtension;
import static org.junit.Assert.*;

import java.util.EnumSet;
import java.util.Set;

import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.cagrid.UserSessionInformationFactory;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.service.account.AccountConfigurationData;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowController;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class FunctionalityWarningActionTest extends AbstractWebTest {

    @Inject
    private FunctionalityWarningAction action;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(getMockRequest());
    }

    @Test
    public void testAcknowledgeFunctionalityWarning() {
        getMockRequest().getSession().setAttribute(ACCOUNT_CONFIGURATION_DATA, new AccountConfigurationData());
        getMockRequest().getSession().setAttribute(REGISTRATION_FLOW_CONTROLLER, RegistrationFlowController.createRegistrationFlow());

        assertNotNull(getMockSession().getAttribute(FirebirdUIConstants.REGISTRATION_FLOW_CONTROLLER));
        assertNotNull(getMockSession().getAttribute(FirebirdUIConstants.ACCOUNT_CONFIGURATION_DATA));

        assertEquals(ActionSupport.INPUT, action.acknowledgeFunctionalityWarning());

        assertNull(getMockSession().getAttribute(FirebirdUIConstants.REGISTRATION_FLOW_CONTROLLER));
        assertNull(getMockSession().getAttribute(FirebirdUIConstants.ACCOUNT_CONFIGURATION_DATA));
    }

    @Test
    public void testAcknowledgeFunctionalityWarning_NewUser() {
        action.setNewUser(true);
        assertEquals(ActionSupport.SUCCESS, action.acknowledgeFunctionalityWarning());
    }

    @Test
    public void testEnterFunctionalityWarning_NewUser() {
        action.setNewUser(true);
        assertEquals(FunctionalityWarningAction.NEW_USER_RESULT, action.enterFunctionalityWarning());
    }

    @Test
    public void testEnterFunctionalityWarning_AddRoles() {
        action.setNewUser(false);
        assertEquals(FunctionalityWarningAction.ADD_ROLES_RESULT, action.enterFunctionalityWarning());
    }

    @Test
    public void testGetUnverifiedRoles_NoRolesVerified() {
        UserSessionInformation sessionInformation = setUpWithAllRoles(false);
        FirebirdWebTestUtility.setUpGridSessionInformation(action, sessionInformation);
        assertTrue(action.getUnverifiedRoles().containsAll(UserRoleType.STANDARD_ROLES));
    }

    @Test
    public void testGetUnverifiedRoles_AllRolesVerified() {
        UserSessionInformation sessionInformation = setUpWithAllRoles(true);
        FirebirdWebTestUtility.setUpGridSessionInformation(action, sessionInformation);
        action.getAccountConfigurationData().getRoles().remove(REGISTRATION_COORDINATOR);
        assertTrue(action.getUnverifiedRoles().isEmpty());
    }

    public UserSessionInformation setUpWithAllRoles(boolean verified) {
        AccountConfigurationData accountConfigurationData = new AccountConfigurationData();
        getMockRequest().getSession().setAttribute(ACCOUNT_CONFIGURATION_DATA, accountConfigurationData);
        Organization sponsor1 = OrganizationFactory.getInstance().create();
        Organization sponsor2 = OrganizationFactory.getInstance().create();
        Set<String> groupNames = Sets.newHashSet(
                INVESTIGATOR.getGroupName(), 
                REGISTRATION_COORDINATOR.getGroupName(),
                SPONSOR.getGroupName() + "_" + getNesIdExtension(sponsor1),
                SPONSOR_DELEGATE.getGroupName() + "_" + getNesIdExtension(sponsor2));
        if (verified) {
            groupNames.add(INVESTIGATOR.getVerifiedGroupName());
            groupNames.add(REGISTRATION_COORDINATOR.getVerifiedGroupName());
            groupNames.add(SPONSOR.getVerifiedGroupName() + "_" + getNesIdExtension(sponsor1));
            groupNames.add(SPONSOR_DELEGATE.getVerifiedGroupName() + "_" + getNesIdExtension(sponsor2));
        }
        UserSessionInformation sessionInformation = UserSessionInformationFactory.getInstance().create("username", groupNames);
        accountConfigurationData.getRoles().addAll(EnumSet.allOf(UserRoleType.class));
        accountConfigurationData.getSponsorOrganizations().add(sponsor1);
        accountConfigurationData.getDelegateOrganizations().add(sponsor2);
        return sessionInformation;
    }

}