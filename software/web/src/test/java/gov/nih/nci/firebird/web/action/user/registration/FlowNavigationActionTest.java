package gov.nih.nci.firebird.web.action.user.registration;

import static gov.nih.nci.firebird.web.common.FirebirdUIConstants.REGISTRATION_FLOW_CONTROLLER;
import static gov.nih.nci.firebird.web.common.FirebirdUIConstants.ACCOUNT_CONFIGURATION_DATA;
import static gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep.PERSON_SELECTION;
import static gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep.VIEW_SELECTED_ROLES;
import static org.junit.Assert.*;

import java.util.EnumSet;

import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.service.account.AccountConfigurationData;
import gov.nih.nci.firebird.service.user.FirebirdUserService;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowController;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class FlowNavigationActionTest extends AbstractWebTest {

    private FlowNavigationAction action = new FlowNavigationAction();
    private RegistrationFlowController flow;
    private AccountConfigurationData data = new AccountConfigurationData();
    @Mock private FirebirdUserService mockUserService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(getMockRequest());
        action.setUserService(mockUserService);
    }

    private void setupExistingFlow() {
        flow = RegistrationFlowController.createRegistrationFlow();
        flow.setupFlowBodyWithSteps(EnumSet.copyOf(RegistrationFlowStep.getStepsForRole(UserRoleType.INVESTIGATOR)), VIEW_SELECTED_ROLES);
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
    public void testEnterNextStep() {
        setupExistingFlow();
        assertEquals(PERSON_SELECTION.name(), action.enterNextStep());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnterNextStepNoFlow() {
        action.enterNextStep();
    }

    @Test
    public void testEnterPreviousStep() {
        setupExistingFlow();
        flow.stepForward();
        assertEquals(VIEW_SELECTED_ROLES.name(), action.enterPreviousStep());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnterPreviousStepNoFlow() {
        action.enterPreviousStep();
    }

    @Test
    public void testEnterFlowStep() {
        RegistrationFlowStep flowStep = PERSON_SELECTION;
        setupExistingFlow();
        makeSureStepIsVisited(flowStep);
        action.setFlowStepToGoTo(flowStep);
        assertEquals(flowStep.name(), action.enterFlowStep());
    }

    private void makeSureStepIsVisited(RegistrationFlowStep stepToVisit) {
        RegistrationFlowStep currentStep = null;
        while (currentStep != stepToVisit) {
            currentStep = flow.stepForward();
        }
        flow.gotoStep(VIEW_SELECTED_ROLES);
    }

    @Test(expected = IllegalStateException.class)
    public void testEnterFlowStepNullStep() {
        setupExistingFlow();
        action.enterFlowStep();
    }

    @Test(expected = IllegalStateException.class)
    public void testEnterFlowStepNoFlow() {
        action.enterFlowStep();
    }

    private void setFlowStatusInSession(RegistrationFlowController flow) {
        getMockRequest().getSession().setAttribute(REGISTRATION_FLOW_CONTROLLER, flow);
    }

    private void setNewFlowDataInSession() {
        getMockRequest().getSession().setAttribute(ACCOUNT_CONFIGURATION_DATA, data);
    }
}
