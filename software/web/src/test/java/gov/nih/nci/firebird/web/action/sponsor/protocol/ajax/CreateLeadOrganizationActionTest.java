package gov.nih.nci.firebird.web.action.sponsor.protocol.ajax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import com.google.inject.Inject;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.State;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.organization.OrganizationSearchService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonSearchService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.test.AbstractWebTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class CreateLeadOrganizationActionTest extends AbstractWebTest {

    @Inject
    private PersonSearchService mockPersonSearchService;

    @Inject
    private PersonService mockPersonService;

    @Inject
    private OrganizationSearchService mockOrganizationSearchService;

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
    public void testPrepare_NoSearchKeys() {
        when(mockCountryLookupService.getAll()).thenReturn(new ArrayList<Country>());
        when(mockStateLookupService.getAll()).thenReturn(new ArrayList<State>());
        action.prepare();
        verifyZeroInteractions(mockPersonSearchService);
        verifyZeroInteractions(mockOrganizationSearchService);
        assertNotNull(action.getCountries());
        assertNotNull(action.getStates());
    }

    @Test
    public void testPrepare_PersonSearchKeys() {
        String searchKey = "12345";
        action.setSelectedPersonKey(searchKey);
        action.prepare();
        verify(mockPersonSearchService).getPerson(searchKey);
        verifyZeroInteractions(mockOrganizationSearchService);
    }

    @Test
    public void testPrepare_OrganizationSearchKeys() throws Exception {
        String searchKey = "12345";
        action.setSelectedOrganizationKey(searchKey);
        action.prepare();
        verify(mockOrganizationSearchService).getOrganization(searchKey);
        verifyZeroInteractions(mockPersonSearchService);
    }

    @Test
    public void testPrepare_InvalidOrganization() throws Exception {
        String searchKey = "12345";
        when(mockOrganizationSearchService.getOrganization(searchKey)).thenThrow(
                new UnavailableEntityException(null, null));
        action.setSelectedOrganizationKey(searchKey);
        action.prepare();
        verify(mockOrganizationSearchService).getOrganization(searchKey);
        assertTrue(action.hasActionErrors());
    }

    @Test
    public void testEnterCreateLeadOrganization() {
        assertEquals(Action.INPUT, action.enterCreateLeadOrganization());
    }

    @Test
    public void testSaveAndValidateLeadOrganization_NewPerson() throws ValidationException {
        String organizationSearchKey = "123454";
        Person newPerson = PersonFactory.getInstance().create();
        Person savedPerson = PersonFactory.getInstanceWithId().create();
        when(mockPersonService.createNesPerson(newPerson)).thenReturn(savedPerson);
        action.setCreateNewPerson(true);
        action.setSelectedOrganizationKey(organizationSearchKey);
        action.setPrincipalInvestigator(newPerson);
        assertEquals(ActionSupport.INPUT, action.saveAndValidateLeadOrganization());
        verifyZeroInteractions(mockOrganizationService);
        verify(mockPersonService).createNesPerson(newPerson);
        assertEquals(savedPerson.getId().toString(), action.getSelectedPersonKey());
    }

    @Test
    public void testSaveAndValidateLeadOrganization_NewOrganization() throws ValidationException {
        String personSearchKey = "123454";
        final Long newId = 12345L;
        final Organization newOrganization = OrganizationFactory.getInstance().create();
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                newOrganization.setId(newId);
                return null;
            }
        }).when(mockOrganizationService).create(newOrganization);
        action.setCreateNewOrganization(true);
        action.setSelectedPersonKey(personSearchKey);
        action.setOrganization(newOrganization);
        assertEquals(ActionSupport.INPUT, action.saveAndValidateLeadOrganization());
        verifyZeroInteractions(mockPersonService);
        verify(mockOrganizationService).create(newOrganization);
        assertEquals(newOrganization.getId().toString(), action.getSelectedOrganizationKey());
    }

    @Test
    public void testSaveAndValidateLeadOrganization_NoNew() throws ValidationException {
        String personSearchKey = "123454";
        String organizationSearchKey = "123454";
        action.setSelectedPersonKey(personSearchKey);
        action.setSelectedOrganizationKey(organizationSearchKey);
        assertEquals(ActionSupport.INPUT, action.saveAndValidateLeadOrganization());
        verifyZeroInteractions(mockPersonService);
        verifyZeroInteractions(mockOrganizationService);
    }

}
