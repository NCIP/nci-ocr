package gov.nih.nci.firebird.web.action.user.registration.flow;

import static gov.nih.nci.firebird.data.user.UserRoleType.*;
import static gov.nih.nci.firebird.nes.NesIdTestUtil.getNesIdExtension;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.cagrid.GridInvocationException;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.cagrid.UserSessionInformationFactory;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.account.AccountConfigurationData;
import gov.nih.nci.firebird.service.account.AccountManagementService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class VerificationPageFlowActionTest extends AbstractFlowActionTestBase {

    @Inject
    private VerificationPageFlowAction action;
    @Inject
    private AccountManagementService mockAccountService;
    private FirebirdUser user = FirebirdUserFactory.getInstance().create();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(getMockRequest());
        setAccountConfigurationDataInSession(getAccountConfigurationData());
        action.prepare();
        user.setId(1L);
        setUpGridSessionInformation();
    }

    @Test
    public void testPerformSave() {
        assertEquals(ActionSupport.SUCCESS, action.performSave());
    }

    @Test
    public void testSaveAndProceedNext_NewUser() throws ValidationException, GridInvocationException {
        assertEquals(VerificationPageFlowAction.USER_HOME_RETURN, action.saveAndProceedNext());
        verify(mockAccountService).create(action.getAccountConfigurationData());
    }

    @Test
    public void testSaveAndProceedNext_ExistingUser() throws ValidationException, GridInvocationException {
        FirebirdWebTestUtility.setCurrentUser(action, user);
        assertEquals(VerificationPageFlowAction.MY_ACCOUNT_RETURN, action.saveAndProceedNext());
        verify(mockAccountService).addRoles(user, action.getAccountConfigurationData());
        verify(getMockSession()).setAttribute(FirebirdUIConstants.ROLES_UPDATED, true);
    }

    @Test
    public void testSaveAndProceedNext_VerifiedRoles() {
        getAccountConfigurationData().getRoles().add(INVESTIGATOR);
        setUpGridSessionInformation(INVESTIGATOR.getGroupName(), INVESTIGATOR.getVerifiedGroupName());
        assertEquals(VerificationPageFlowAction.USER_HOME_RETURN, action.saveAndProceedNext());
    }

    @Test
    public void testSaveAndProceedNext_UnverifiedNewUser() {
        getAccountConfigurationData().getRoles().add(INVESTIGATOR);
        setUpGridSessionInformation(INVESTIGATOR.getGroupName());
        assertEquals(VerificationPageFlowAction.FUNCTIONALITY_WARNING_RETURN, action.saveAndProceedNext());
    }

    @Test
    public void testSaveAndProceedNext_UnverifiedAddRoles() {
        getAccountConfigurationData().getRoles().add(INVESTIGATOR);
        setUpGridSessionInformation(INVESTIGATOR.getGroupName());
        FirebirdWebTestUtility.setCurrentUser(action, user);
        assertEquals(VerificationPageFlowAction.FUNCTIONALITY_WARNING_RETURN, action.saveAndProceedNext());
    }

    public void setUpGridSessionInformation(String... groupNames) {
        UserSessionInformation sessionInformation = UserSessionInformationFactory.getInstance().create(user.getUsername(), groupNames);
        FirebirdWebTestUtility.setUpGridSessionInformation(action, sessionInformation);
    }

    @Test
    public void testSaveAndProceedNext_GridInvocationException() throws ValidationException, GridInvocationException {
        FirebirdWebTestUtility.setCurrentUser(action, user);
        GridInvocationException mockGridInvocationException = mock(GridInvocationException.class);
        doThrow(mockGridInvocationException).when(mockAccountService).addRoles(eq(user), any(AccountConfigurationData.class));
        assertEquals(ActionSupport.INPUT, action.saveAndProceedNext());
        assertTrue(action.hasActionErrors());
    }

    @Test
    public void testSaveAndProceedNext_ValidationError() throws ValidationException, GridInvocationException {
        ValidationFailure validationFailure = new ValidationFailure("failed");
        ValidationResult result = new ValidationResult(validationFailure);
        ValidationException validationException = new ValidationException(result);
        doThrow(validationException).when(mockAccountService).create(any(AccountConfigurationData.class));
        assertEquals(ActionSupport.INPUT, action.saveAndProceedNext());
    }

    @Test
    public void testIsVerificationRequiredForRoles_VerifiedInvestigator() {
        getAccountConfigurationData().getRoles().add(INVESTIGATOR);
        setUpGridSessionInformation(INVESTIGATOR.getGroupName(), INVESTIGATOR.getVerifiedGroupName());
        assertEquals(false, action.isVerificationRequiredForRoles());
    }

    @Test
    public void testIsVerificationRequiredForRoles_UnverifiedInvestigator() {
        getAccountConfigurationData().getRoles().add(INVESTIGATOR);
        setUpGridSessionInformation(INVESTIGATOR.getGroupName());
        assertEquals(true, action.isVerificationRequiredForRoles());
    }

    @Test
    public void testIsVerificationRequiredForRoles_RegistrationCoordinator() {
        getAccountConfigurationData().getRoles().add(REGISTRATION_COORDINATOR);
        assertEquals(true, action.isVerificationRequiredForRoles());
    }

    @Test
    public void testIsVerificationRequiredForRoles_VerifiedSponsor() {
        getAccountConfigurationData().getRoles().add(SPONSOR);
        Organization sponsor = OrganizationFactory.getInstance().create();
        getAccountConfigurationData().getSponsorOrganizations().add(sponsor);
        setUpGridSessionInformation(SPONSOR.getGroupName(),
                SPONSOR.getVerifiedGroupName(),
                SPONSOR.getGroupName() + "_" + getNesIdExtension(sponsor),
                SPONSOR.getVerifiedGroupName() + "_" + getNesIdExtension(sponsor));
        assertEquals(false, action.isVerificationRequiredForRoles());
    }

    @Test
    public void testIsVerificationRequiredForRoles_UnverifiedSponsor() {
        getAccountConfigurationData().getRoles().add(SPONSOR);
        Organization sponsor = OrganizationFactory.getInstance().create();
        getAccountConfigurationData().getSponsorOrganizations().add(sponsor);
        setUpGridSessionInformation(SPONSOR.getGroupName(),
                SPONSOR.getGroupName() + "_" + getNesIdExtension(sponsor));
        assertEquals(true, action.isVerificationRequiredForRoles());
    }

    @Test
    public void testIsVerificationRequiredForRoles_VerifiedSponsorDelegate() {
        getAccountConfigurationData().getRoles().add(SPONSOR_DELEGATE);
        Organization sponsor = OrganizationFactory.getInstance().create();
        getAccountConfigurationData().getDelegateOrganizations().add(sponsor);
        setUpGridSessionInformation(SPONSOR_DELEGATE.getGroupName(),
                SPONSOR_DELEGATE.getVerifiedGroupName(),
                SPONSOR_DELEGATE.getGroupName() + "_" + getNesIdExtension(sponsor),
                SPONSOR_DELEGATE.getVerifiedGroupName() + "_" + getNesIdExtension(sponsor));
        assertEquals(false, action.isVerificationRequiredForRoles());
    }

    @Test
    public void testIsVerificationRequiredForRoles_UnverifiedSponsorDelegate() {
        getAccountConfigurationData().getRoles().add(SPONSOR_DELEGATE);
        Organization sponsor = OrganizationFactory.getInstance().create();
        getAccountConfigurationData().getDelegateOrganizations().add(sponsor);
        setUpGridSessionInformation(SPONSOR_DELEGATE.getGroupName(),
                SPONSOR_DELEGATE.getGroupName() + "_" + getNesIdExtension(sponsor));
        assertTrue(action.isVerificationRequiredForRoles());
    }

}
