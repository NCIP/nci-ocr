package gov.nih.nci.firebird.web.action.user.registration.flow;

import static gov.nih.nci.firebird.web.common.FirebirdUIConstants.REGISTRATION_FLOW_CONTROLLER;
import static gov.nih.nci.firebird.data.user.UserRoleType.*;
import static gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import gov.nih.nci.firebird.service.account.AccountManagementService;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowController;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep;

public class RoleSelectionPageFlowActionTest extends AbstractFlowActionTestBase {

    @Inject
    private AccountManagementService mockAccountService;
    @Inject
    private RoleSelectionPageFlowAction action;
    private RegistrationFlowController flow = RegistrationFlowController.createAddRolesFlow();
    private FirebirdUser user = FirebirdUserFactory.getInstance().create();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setRequest();
    }

    private void setRequest() {
        action.setServletRequest(getMockRequest());
    }

    private void prepareAction() {
        setAccountConfigurationDataInSession(getAccountConfigurationData());
        FirebirdWebTestUtility.setCurrentUser(action, user);
        getMockRequest().getSession().setAttribute(REGISTRATION_FLOW_CONTROLLER, flow);
        action.prepare();
    }

    @Test
    public void testPrepare() {
        prepareAction();
        assertNotNull(action.getAccountConfigurationData());
    }

    @Test
    public void testEnterAction() {
        prepareAction();
        List<UserRoleType> roles = null;
        getAccountConfigurationData().getRoles().add(INVESTIGATOR);
        getAccountConfigurationData().getRoles().add(SPONSOR);
        assertEquals(ActionSupport.INPUT, action.enterAction());
        roles = action.getSelectedRoles();
        assertTrue(roles.contains(INVESTIGATOR));
        assertTrue(roles.contains(SPONSOR));
    }

    @Test(expected = IllegalStateException.class)
    public void testEnterAction_NoFlow() {
        action.enterAction();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformSaveAndProceedNext_NoRoles() {
        prepareAction();
        assertEquals(ActionSupport.SUCCESS, action.saveAndProceedNext());
    }

    private void checkFlowContainsAllSteps(RegistrationFlowStep... steps) {
        assertTrue(flow.getFlowSteps().containsAll(Arrays.asList(steps)));
    }

    @Test
    public void testPerformSave_AddInvestigator() throws Exception {
        final int expectedNumberOfSteps = 4;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(INVESTIGATOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, PRIMARY_ORGANIZATION_SELECTION, VERIFICATION);
        assertTrue(getAccountConfigurationData().getRoles().contains(INVESTIGATOR));
    }

    @Test
    public void testPerformSave_AddInvestigatorExistingUser() throws Exception {
        final int expectedNumberOfSteps = 3;
        user.setId(1L);
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(INVESTIGATOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PRIMARY_ORGANIZATION_SELECTION, VERIFICATION);
        assertTrue(getAccountConfigurationData().getRoles().contains(INVESTIGATOR));
    }

    @Test
    public void testPerformSave_AddInvestigatorTwice() {
        final int expectedNumberOfSteps = 4;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(INVESTIGATOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, PRIMARY_ORGANIZATION_SELECTION, VERIFICATION);

        reinitializeAction();
        action.setSelectedRoles(Lists.newArrayList(INVESTIGATOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, PRIMARY_ORGANIZATION_SELECTION, VERIFICATION);
        assertTrue(getAccountConfigurationData().getRoles().contains(INVESTIGATOR));
    }

    private void reinitializeAction() {
        action = new RoleSelectionPageFlowAction(mockAccountService);
        setRequest();
        prepareAction();
    }

    @Test
    public void testPerformSave_AddSponsor() {
        final int expectedNumberOfSteps = 4;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, SPONSOR_ORGANIZATIONS, VERIFICATION);
        assertTrue(getAccountConfigurationData().getRoles().contains(SPONSOR));
    }

    @Test
    public void testPerformSave_RemoveSponsor() {
        final int expectedNumberOfSteps = 4;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR));
        action.performSave();
        getAccountConfigurationData().getSponsorOrganizations().add(OrganizationFactory.getInstance().create());

        reinitializeAction();
        action.setSelectedRoles(Lists.newArrayList(INVESTIGATOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(flow.getFlowSteps().toString(), expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, PRIMARY_ORGANIZATION_SELECTION, VERIFICATION);
        assertTrue(getAccountConfigurationData().getRoles().contains(INVESTIGATOR));
        assertFalse(getAccountConfigurationData().getRoles().contains(SPONSOR));
        assertTrue(getAccountConfigurationData().getSponsorOrganizations().isEmpty());
    }

    @Test
    public void testPerformSave_SponsorAndDelegateRemoveSponsor() throws Exception {
        final int expectedNumberOfSteps = 4;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR, SPONSOR_DELEGATE));
        action.performSave();
        getAccountConfigurationData().getSponsorOrganizations().add(OrganizationFactory.getInstance().create());
        getAccountConfigurationData().getDelegateOrganizations().add(OrganizationFactory.getInstance().create());
        assertTrue(getAccountConfigurationData().getRoles().contains(SPONSOR));
        assertTrue(getAccountConfigurationData().getRoles().contains(SPONSOR_DELEGATE));

        reinitializeAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR_DELEGATE));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, SPONSOR_DELEGATES, VERIFICATION);
        assertFalse(getAccountConfigurationData().getRoles().contains(SPONSOR));
        assertTrue(getAccountConfigurationData().getRoles().contains(SPONSOR_DELEGATE));
        assertTrue(getAccountConfigurationData().getSponsorOrganizations().isEmpty());
        assertFalse(getAccountConfigurationData().getDelegateOrganizations().isEmpty());
    }

    @Test
    public void testPerformSave_AddSponsorAndInvestigator() {
        final int expectedNumberOfSteps = 5;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR, INVESTIGATOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, PRIMARY_ORGANIZATION_SELECTION,
                SPONSOR_ORGANIZATIONS, VERIFICATION);
        assertTrue(getAccountConfigurationData().getRoles().contains(INVESTIGATOR));
        assertTrue(getAccountConfigurationData().getRoles().contains(SPONSOR));
    }

    @Test
    public void testPerformSave_SponsorInvestigatorChangeToJustSponsor() throws Exception {
        final int expectedNumberOfSteps = 4;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR, INVESTIGATOR));
        action.performSave();

        reinitializeAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, SPONSOR_ORGANIZATIONS, VERIFICATION);
        assertFalse(getAccountConfigurationData().getRoles().contains(INVESTIGATOR));
        assertTrue(getAccountConfigurationData().getRoles().contains(SPONSOR));
    }

    @Test
    public void testPerformSave_SponsorInvestigatorChangeToRegistrationCoordinator() throws Exception {
        final int expectedNumberOfSteps = 5;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR, INVESTIGATOR));
        action.performSave();

        reinitializeAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR, REGISTRATION_COORDINATOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION,
                INVESTIGATOR_SELECTION, SPONSOR_ORGANIZATIONS, VERIFICATION);
        assertFalse(getAccountConfigurationData().getRoles().contains(INVESTIGATOR));
        assertTrue(getAccountConfigurationData().getRoles().contains(REGISTRATION_COORDINATOR));
    }

    @Test
    public void testPerformSave_RemoveCoordinator() throws Exception {
        final int expectedNumberOfSteps = 5;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR, REGISTRATION_COORDINATOR));
        action.performSave();
        getAccountConfigurationData().getSelectedInvestigators().add(InvestigatorProfileFactory.getInstance().create());

        reinitializeAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR, INVESTIGATOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, PRIMARY_ORGANIZATION_SELECTION,
                SPONSOR_ORGANIZATIONS, VERIFICATION);
        assertTrue(getAccountConfigurationData().getRoles().contains(SPONSOR));
        assertTrue(getAccountConfigurationData().getRoles().contains(INVESTIGATOR));
        assertFalse(getAccountConfigurationData().getRoles().contains(REGISTRATION_COORDINATOR));
        assertTrue(getAccountConfigurationData().getSelectedInvestigators().isEmpty());
    }

    @Test
    public void testPerformSave_RemoveDelegate() throws Exception {
        final int expectedNumberOfSteps = 4;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR_DELEGATE));
        action.performSave();
        getAccountConfigurationData().getDelegateOrganizations().add(OrganizationFactory.getInstance().create());

        reinitializeAction();
        action.setSelectedRoles(Lists.newArrayList(INVESTIGATOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, PRIMARY_ORGANIZATION_SELECTION, VERIFICATION);
        assertTrue(getAccountConfigurationData().getRoles().contains(INVESTIGATOR));
        assertFalse(getAccountConfigurationData().getRoles().contains(SPONSOR_DELEGATE));
        assertTrue(getAccountConfigurationData().getDelegateOrganizations().isEmpty());
    }

    @Test
    public void testPerformSave_SponsorAndDelegateRemoveDelegate() throws Exception {
        final int expectedNumberOfSteps = 4;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR, SPONSOR_DELEGATE));
        action.performSave();
        getAccountConfigurationData().getSponsorOrganizations().add(OrganizationFactory.getInstance().create());
        getAccountConfigurationData().getDelegateOrganizations().add(OrganizationFactory.getInstance().create());

        reinitializeAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, SPONSOR_ORGANIZATIONS, VERIFICATION);
        assertTrue(getAccountConfigurationData().getRoles().contains(SPONSOR));
        assertFalse(getAccountConfigurationData().getRoles().contains(SPONSOR_DELEGATE));
        assertFalse(getAccountConfigurationData().getSponsorOrganizations().isEmpty());
        assertTrue(getAccountConfigurationData().getDelegateOrganizations().isEmpty());
    }

    @Test
    public void testPerformSave_DelegateNowSponserAndDelegate() throws Exception {
        final int expectedNumberOfSteps = 5;
        prepareAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR_DELEGATE));
        action.performSave();
        getAccountConfigurationData().getDelegateOrganizations().add(OrganizationFactory.getInstance().create());

        reinitializeAction();
        action.setSelectedRoles(Lists.newArrayList(SPONSOR, SPONSOR_DELEGATE));
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(expectedNumberOfSteps, flow.getFlowSteps().size());
        checkFlowContainsAllSteps(ROLE_SELECTION, PERSON_SELECTION, SPONSOR_ORGANIZATIONS, SPONSOR_DELEGATES,
                VERIFICATION);
        assertTrue(getAccountConfigurationData().getRoles().contains(SPONSOR));
        assertTrue(getAccountConfigurationData().getRoles().contains(SPONSOR_DELEGATE));
        assertTrue(getAccountConfigurationData().getSponsorOrganizations().isEmpty());
        assertTrue(getAccountConfigurationData().getDelegateOrganizations().isEmpty());
    }

    @Test
    public void testGetAvailableRoles_NewUser() {
        prepareAction();
        assertEquals(UserRoleType.STANDARD_ROLES, action.getAvailableRoles());
    }

    @Test
    public void testGetAvailableRoles_Investigator() {
        user.setId(1L);
        user.createInvestigatorRole(InvestigatorProfileFactory.getInstance().create());
        prepareAction();
        assertEquals(getOtherSelectableRoles(UserRoleType.INVESTIGATOR), action.getAvailableRoles());
    }

    private EnumSet<UserRoleType> getOtherSelectableRoles(UserRoleType type) {
        return EnumSet.complementOf(EnumSet.of(type, 
                UserRoleType.CTEP_INVESTIGATOR, 
                UserRoleType.CTEP_VERIFIED_INVESTIGATOR, 
                UserRoleType.CTEP_REGISTRATION_COORDINATOR,
                UserRoleType.CTEP_SPONSOR,
                UserRoleType.CTEP_SPONSOR_DELEGATE));
    }

    @Test
    public void testGetAvailableRoles_Coordinator() {
        user.setId(1L);
        user.createRegistrationCoordinatorRole();
        prepareAction();
        assertEquals(getOtherSelectableRoles(UserRoleType.REGISTRATION_COORDINATOR), action.getAvailableRoles());
    }

    @Test
    public void testGetAvailableRoles_Sponsor() {
        user.setId(1L);
        user.addSponsorRepresentativeRole(OrganizationFactory.getInstance().create());
        prepareAction();
        assertEquals(getOtherSelectableRoles(UserRoleType.SPONSOR), action.getAvailableRoles());
    }

    @Test
    public void testGetAvailableRoles_Delegate() {
        user.setId(1L);
        user.addSponsorDelegateRole(OrganizationFactory.getInstance().create());
        prepareAction();
        assertEquals(getOtherSelectableRoles(UserRoleType.SPONSOR_DELEGATE), action.getAvailableRoles());
    }

    @Test
    public void testGetAvailableRoles_SuperUser() {
        user.setId(1L);
        user.createInvestigatorRole(InvestigatorProfileFactory.getInstance().create());
        user.createRegistrationCoordinatorRole();
        user.addSponsorRepresentativeRole(OrganizationFactory.getInstance().create());
        user.addSponsorDelegateRole(OrganizationFactory.getInstance().create());
        prepareAction();
        assertEquals(EnumSet.noneOf(UserRoleType.class), action.getAvailableRoles());
    }
}
