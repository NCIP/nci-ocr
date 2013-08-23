package gov.nih.nci.firebird.web.action.sponsor.protocol.ajax;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.State;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.organization.InvalidatedOrganizationException;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.inject.Inject;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class CreateLeadOrganizationActionTest extends AbstractWebTest {

    private static final String EXTERNAL_ID = "12345";

    @Inject
    private PersonService mockPersonService;

    @Inject
    private OrganizationService mockOrganizationService;

    @Inject
    private CountryLookupService mockCountryLookupService;

    @Inject
    private StateLookupService mockStateLookupService;

    @Inject
    private CreateLeadOrganizationAction action;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        FirebirdWebTestUtility.setupMockRequest(action);
    }

    @Test
    public void testPrepare_NoExternalIds() {
        when(mockCountryLookupService.getAll()).thenReturn(new ArrayList<Country>());
        when(mockStateLookupService.getAll()).thenReturn(new ArrayList<State>());
        action.prepare();
        verifyZeroInteractions(mockPersonService);
        verifyZeroInteractions(mockOrganizationService);
        assertNotNull(action.getCountries());
        assertNotNull(action.getStates());
    }

    @Test
    public void testPrepare_WithPersonExternalId() throws Exception {
        action.setSelectedPersonExternalId(EXTERNAL_ID);
        action.prepare();
        verify(mockPersonService).getByExternalId(EXTERNAL_ID);
        verifyZeroInteractions(mockOrganizationService);
    }

    @Test
    public void testPrepare_WithOrganizationExternalId() throws Exception {
        action.setSelectedOrganizationExternalId(EXTERNAL_ID);
        action.prepare();
        verify(mockOrganizationService).getByExternalId(EXTERNAL_ID);
        verifyZeroInteractions(mockPersonService);
    }

    @Test
    public void testPrepare_InvalidatedOrganization() throws Exception {
        when(mockOrganizationService.getByExternalId(EXTERNAL_ID)).thenThrow(new InvalidatedOrganizationException());
        action.setSelectedOrganizationExternalId(EXTERNAL_ID);
        action.prepare();
        verify(mockOrganizationService).getByExternalId(EXTERNAL_ID);
        assertTrue(action.hasActionErrors());
    }

    @Test
    public void testEnterCreateLeadOrganization() {
        assertEquals(Action.INPUT, action.enterCreateLeadOrganization());
    }

    @Test
    public void testSaveAndValidateLeadOrganization_NewPerson() throws ValidationException {
        Person newPerson = PersonFactory.getInstance().create();
        action.setCreateNewPerson(true);
        action.setSelectedOrganizationExternalId(EXTERNAL_ID);
        action.setPrincipalInvestigator(newPerson);
        assertEquals(ActionSupport.INPUT, action.saveAndValidateLeadOrganization());
        verifyZeroInteractions(mockOrganizationService);
        verify(mockPersonService).save(newPerson);
        assertEquals(newPerson.getExternalId(), action.getSelectedPersonExternalId());
    }

    @Test
    public void testSaveAndValidateLeadOrganization_NewOrganization() throws ValidationException {
        final Long newId = 12345L;
        final Organization newOrganization = OrganizationFactory.getInstance().create();
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                newOrganization.setId(newId);
                return null;
            }
        }).when(mockOrganizationService).create(newOrganization, OrganizationRoleType.GENERIC_ORGANIZATION);
        action.setCreateNewOrganization(true);
        action.setSelectedPersonExternalId(EXTERNAL_ID);
        action.setOrganization(newOrganization);
        assertEquals(ActionSupport.INPUT, action.saveAndValidateLeadOrganization());
        verifyZeroInteractions(mockPersonService);
        verify(mockOrganizationService).create(newOrganization, OrganizationRoleType.GENERIC_ORGANIZATION);
        assertEquals(newOrganization.getExternalId(), action.getSelectedOrganizationExternalId());
    }

    @Test
    public void testSaveAndValidateLeadOrganization_NoNew() throws ValidationException {
        action.setSelectedPersonExternalId(EXTERNAL_ID);
        action.setSelectedOrganizationExternalId(EXTERNAL_ID);
        assertEquals(ActionSupport.INPUT, action.saveAndValidateLeadOrganization());
        verifyZeroInteractions(mockPersonService);
        verifyZeroInteractions(mockOrganizationService);
    }

}
