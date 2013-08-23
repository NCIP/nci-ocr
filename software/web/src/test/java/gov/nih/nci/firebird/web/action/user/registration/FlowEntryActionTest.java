package gov.nih.nci.firebird.web.action.user.registration;

import static gov.nih.nci.firebird.web.common.FirebirdUIConstants.REGISTRATION_FLOW_CONTROLLER;
import static gov.nih.nci.firebird.web.common.FirebirdUIConstants.ACCOUNT_CONFIGURATION_DATA;
import static gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep.PERSON_SELECTION;
import static gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep.PRIMARY_ORGANIZATION_SELECTION;
import static gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep.ROLE_SELECTION;
import static gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep.VERIFICATION;
import static gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep.VIEW_SELECTED_ROLES;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.EnumSet;
import java.util.Set;

import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.service.account.AccountConfigurationData;
import gov.nih.nci.firebird.service.account.AccountManagementService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowController;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class FlowEntryActionTest extends AbstractWebTest {

    @Inject private FlowEntryAction action;
    @Inject private AccountManagementService mockAccountService;
    private RegistrationFlowController flow;
    private AccountConfigurationData data = new AccountConfigurationData();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(getMockRequest());
    }

    private void setupExistingFlow() {
        flow = RegistrationFlowController.createRegistrationFlow();
        flow.setupFlowBodyWithSteps(EnumSet.copyOf(RegistrationFlowStep.getStepsForRole(UserRoleType.INVESTIGATOR)), ROLE_SELECTION);
        setupExistingFlow(flow);
    }

    private void setupExistingFlow(RegistrationFlowController flow) {
        setFlowStatusInSession(flow);
        setNewFlowDataInSession();
        action.prepare();
    }

    @Test
    public void testPrepareNoFlow() {
        action.prepare();
        assertNull(action.getFlow());
    }

    @Test
    public void testPrepareFlowExists() {
        setupExistingFlow();
        assertEquals(flow, action.getFlow());
    }

    @Test
    public void testEnterRegistrationFlow() {
        AccountConfigurationData createdData = new AccountConfigurationData();
        createdData.getRoles().addAll(UserRoleType.STANDARD_ROLES);
        when(mockAccountService.createConfigurationForNewUser(any(UserSessionInformation.class))).thenReturn(createdData);
        assertEquals(ActionSupport.SUCCESS, action.enterRegistrationFlow());
        assertNotNull(getFlowControllerFromSession());
        assertEquals(RegistrationFlowStep.VIEW_SELECTED_ROLES, getFlowControllerFromSession().getCurrentStep());
        assertEquals(createdData, action.getAccountConfigurationData());
        Set<RegistrationFlowStep> expectedFlowSteps = EnumSet.of(VIEW_SELECTED_ROLES, PERSON_SELECTION, PRIMARY_ORGANIZATION_SELECTION, VERIFICATION);
        assertTrue(action.getFlow().getFlowSteps().containsAll(expectedFlowSteps));
    }

    @Test
    public void testEnterRegistrationFlow_NoRoles() {
        AccountConfigurationData createdData = new AccountConfigurationData();
        when(mockAccountService.createConfigurationForNewUser(any(UserSessionInformation.class))).thenReturn(createdData);
        assertEquals(ActionSupport.SUCCESS, action.enterRegistrationFlow());
        assertEquals(createdData, action.getAccountConfigurationData());
        assertTrue(action.getFlow().getFlowSteps().containsAll(EnumSet.of(VIEW_SELECTED_ROLES, VERIFICATION)));
    }

    @Test
    public void testEnterAddRolesFlow() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        FirebirdWebTestUtility.setCurrentUser(action, user);
        assertEquals(ActionSupport.SUCCESS, action.enterAddRolesFlow());
        checkSessionAndAccountData(user);
        assertNull(action.getAccountConfigurationData().getPreselectedRole());
    }

    private void checkSessionAndAccountData(FirebirdUser user) {
        assertNotNull(getFlowControllerFromSession());
        assertEquals(RegistrationFlowStep.ROLE_SELECTION, getFlowControllerFromSession().getCurrentStep());
        assertNotNull(action.getAccountConfigurationData());
        assertNotSame(data, action.getAccountConfigurationData());
        assertEquals(user.getPerson(), action.getAccountConfigurationData().getPerson());
    }

    @Test
    public void testEnterAddRolesFlow_InvestigatorPreselected() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        FirebirdWebTestUtility.setCurrentUser(action, user);
        action.setPreselectedRole(UserRoleType.INVESTIGATOR);
        assertEquals(ActionSupport.SUCCESS, action.enterAddRolesFlow());
        checkSessionAndAccountData(user);
        assertEquals(UserRoleType.INVESTIGATOR, action.getAccountConfigurationData().getPreselectedRole());
    }

    @Test
    public void testEnterAddRolesFlow_InvestigatorPreselectedButAlreadySelected() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        user.createInvestigatorRole(InvestigatorProfileFactory.getInstance().create());
        FirebirdWebTestUtility.setCurrentUser(action, user);
        action.setPreselectedRole(UserRoleType.INVESTIGATOR);
        assertEquals(ActionSupport.SUCCESS, action.enterAddRolesFlow());
        checkSessionAndAccountData(user);
        assertNull(action.getAccountConfigurationData().getPreselectedRole());
    }

    @Test
    public void testEnterRegistrationFlowAlreadyExists() {
        setupExistingFlow();
        action.enterRegistrationFlow();
        assertEquals(ActionSupport.SUCCESS, action.enterRegistrationFlow());
        assertNotNull(getFlowControllerFromSession());
        assertNotNull(action.getAccountConfigurationData());
        assertSame(data, action.getAccountConfigurationData());
    }

    @Test
    public void testEnterRegistrationFlowCurrentStepRefreshPage() {
        setupExistingFlow();
        flow.stepForward();
        action.enterRegistrationFlow();
        assertEquals(ActionSupport.SUCCESS, action.enterRegistrationFlow());
        assertNotNull(getFlowControllerFromSession());
        assertNotNull(action.getAccountConfigurationData());
        assertSame(data, action.getAccountConfigurationData());
    }

    @Test
    public void testEnterExistingFlow() {
        setupExistingFlow();
        assertEquals(ActionSupport.SUCCESS, action.enterExistingFlow());
    }

    @Test
    public void testEnterExistingFlowNoFlow() {
        assertEquals(FlowNavigationAction.LOGIN, action.enterExistingFlow());
    }

    @Test
    public void testCancelFlow() {
        setupExistingFlow();
        assertEquals(FlowNavigationAction.LOGIN, action.cancelFlow());
        assertNull(getMockRequest().getSession(false));
    }

    private RegistrationFlowController getFlowControllerFromSession() {
        return (RegistrationFlowController) getMockSession().getAttribute(REGISTRATION_FLOW_CONTROLLER);
    }

    private void setFlowStatusInSession(RegistrationFlowController flow) {
        getMockSession().setAttribute(REGISTRATION_FLOW_CONTROLLER, flow);
    }

    private void setNewFlowDataInSession() {
        getMockSession().setAttribute(ACCOUNT_CONFIGURATION_DATA, data);
    }
}
