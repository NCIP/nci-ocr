package gov.nih.nci.firebird.web.action.sponsor.protocol;

import static gov.nih.nci.firebird.test.ValueGenerator.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.cagrid.UserSessionInformationFactory;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.SponsorRole;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.service.investigatorprofile.ProfileRefreshService;
import gov.nih.nci.firebird.service.organization.InvalidatedOrganizationException;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.person.external.InvalidatedPersonException;
import gov.nih.nci.firebird.service.protocol.ProtocolService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;

@SuppressWarnings("serial")
public class AbstractModifyProtocolActionTest extends AbstractWebTest {

    @Inject
    private ProtocolService mockProtocolService;
    @Inject
    private Provider<OrganizationService> mockOrganizationServiceProvider;
    @Inject
    private Provider<PersonService> mockPersonServiceProvider;
    @Inject
    private OrganizationService mockOrganizationService;
    @Inject
    private PersonService mockPersonService;
    @Inject
    private ProfileRefreshService mockProfileRefreshService;

    private Protocol protocol = ProtocolFactory.getInstanceWithId().createWithFormsDocuments();

    private AbstractModifyProtocolAction action;
    private FirebirdUser user;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        action = new AbstractModifyProtocolAction(mockProtocolService) {
            @Override
            protected void performSave() throws ValidationException {/* Do Nothing */
            }
        };
        action.setProfileRefreshService(mockProfileRefreshService);
        action.setProtocol(protocol);
        action.setPersonServiceProvider(mockPersonServiceProvider);
        action.setOrganizationServiceProvider(mockOrganizationServiceProvider);
        when(mockProtocolService.getById(protocol.getId())).thenReturn(protocol);
        when(mockProtocolService.create()).thenReturn(new Protocol());
        user = FirebirdUserFactory.getInstance().create();
        FirebirdWebTestUtility.setCurrentUser(action, user);
        if (protocol.getLeadOrganizations().isEmpty()) {
            addLeadOrganizationToProtocol();
        }
    }

    private ProtocolLeadOrganization addLeadOrganizationToProtocol() throws InvalidatedOrganizationException, InvalidatedPersonException {
        ProtocolLeadOrganization leadOrganization = createLeadOrganzation();
        return protocol.addLeadOrganization(leadOrganization.getOrganization(),
                leadOrganization.getPrincipalInvestigator());
    }

    private ProtocolLeadOrganization createLeadOrganzation() throws InvalidatedOrganizationException, InvalidatedPersonException {
        return createLeadOrganzation(OrganizationFactory.getInstanceWithId().create(), PersonFactory
                .getInstanceWithId().create());
    }

    private ProtocolLeadOrganization createLeadOrganzation(Organization organization, Person principalInvestigator)
            throws InvalidatedOrganizationException, InvalidatedPersonException {
        ProtocolLeadOrganization leadOrganization = new ProtocolLeadOrganization(null, organization,
                principalInvestigator);
        String organizationId = leadOrganization.getOrganization().getExternalId();
        when(mockOrganizationService.getByExternalId(organizationId)).thenReturn(leadOrganization.getOrganization());
        if (principalInvestigator != null) {
            String principalInvestigatorId = principalInvestigator.getExternalId();
            when(mockPersonService.getByExternalId(principalInvestigatorId)).thenReturn(
                    leadOrganization.getPrincipalInvestigator());
        }

        return leadOrganization;
    }

    @Test
    public void testPrepare_SponsorNoExternalId() {
        action.setSponsorExternalId(null);
        action.prepare();
        assertNull(action.getSponsor());
    }

    @Test
    public void testPrepare_SponsorWithExternalId() throws Exception {
        Organization sponsor = OrganizationFactory.getInstance().create();
        when(mockOrganizationService.getByExternalId(sponsor.getExternalId())).thenReturn(sponsor);

        action.setSponsorExternalId(sponsor.getExternalId());
        action.prepare();
        assertSame(sponsor, action.getSponsor());
    }

    @Test(expected = IllegalStateException.class)
    public void testPrepare_SponsorWithExternalId_InvalidatedOrganizationException() throws Exception {
        Organization sponsor = OrganizationFactory.getInstance().create();
        when(mockOrganizationService.getByExternalId(sponsor.getExternalId())).thenThrow(new InvalidatedOrganizationException());
        action.setSponsorExternalId(sponsor.getExternalId());
        action.prepare();
    }

    @Test
    public void testPrepare_NoProtocol() {
        action.setProtocol(null);
        action.prepare();
        verify(mockProtocolService).create();
    }

    @Test
    public void testPrepare_ProtocolNoId() {
        action.setProtocol(new Protocol());
        action.prepare();
        verify(mockProtocolService).create();
    }

    @Test
    public void testPrepare_ProtocolWithId() {
        action.prepare();
        verify(mockProtocolService).getById(protocol.getId());
    }

    @Test
    public void testPrepare_MultipleLeadOrganizationsWithSamePI() throws Exception {
        ProtocolLeadOrganization leadOrganization = addLeadOrganizationMapping();
        ProtocolLeadOrganization leadOrganization2 = createLeadOrganzation(OrganizationFactory.getInstanceWithId()
                .create(), leadOrganization.getPrincipalInvestigator());
        addLeadOrganizationMapping(leadOrganization2);
        action.prepare();

        verify(mockPersonService, times(2))
                .getByExternalId(leadOrganization.getPrincipalInvestigator().getExternalId());
        verify(mockOrganizationService).getByExternalId(leadOrganization.getOrganization().getExternalId());
        verify(mockOrganizationService).getByExternalId(leadOrganization2.getOrganization().getExternalId());
    }

    private ProtocolLeadOrganization addLeadOrganizationMapping() throws InvalidatedOrganizationException, InvalidatedPersonException {
        return addLeadOrganizationMapping(createLeadOrganzation());
    }

    private ProtocolLeadOrganization addLeadOrganizationMapping(ProtocolLeadOrganization leadOrganization)
            throws InvalidatedOrganizationException {
        if (leadOrganization.getPrincipalInvestigator() != null) {
            action.getLeadOrganizationIdMappings().put(leadOrganization.getOrganization().getExternalId(),
                    Lists.newArrayList(leadOrganization.getPrincipalInvestigator().getExternalId()));
        } else {
            List<String> emptyList = Collections.emptyList();
            action.getLeadOrganizationIdMappings().put(leadOrganization.getOrganization().getExternalId(), emptyList);
        }

        return leadOrganization;
    }

    @Test
    public void testPrepare_InvalidatedOrganization() throws Exception {
        ProtocolLeadOrganization leadOrganization = addLeadOrganizationMapping();
        when(mockOrganizationService.getByExternalId(leadOrganization.getOrganization().getExternalId())).thenThrow(
                new InvalidatedOrganizationException());
        action.prepare();

        assertTrue(action.getLeadOrganizationMappings().isEmpty());
        assertTrue(action.hasActionErrors());
        assertEquals(action.getText("sponsor.protocol.lead.organization.unavailable.organization.error"),
                Iterables.getFirst(action.getActionErrors(), null));
    }

    @Test
    public void testPrepare_MissingPrincipalInvestigator() throws Exception {
        addLeadOrganizationMapping(createLeadOrganzation(OrganizationFactory.getInstanceWithId().create(), null));
        action.prepare();

        assertTrue(action.hasActionErrors());
        assertEquals(action.getText("sponsor.protocol.lead.organization.principal.investigator.required"),
                Iterables.getFirst(action.getActionErrors(), null));
    }

    @Test
    public void testGetAgentNameSet() {
        List<String> agentList = Lists.newArrayList(getUniqueString(99), getUniqueString(1), getUniqueString(7),
                getUniqueString(200));
        String agents = StringUtils.join(agentList, ", ");

        action.setAgentList(agents);
        Set<String> splitAgents = action.getAgentNameSet();
        assertTrue(splitAgents.containsAll(agentList));
        assertTrue(agentList.containsAll(splitAgents));
    }

    @Test
    public void testGetAgentNameSet_AgentTooLong() {
        List<String> agentList = Lists.newArrayList(getUniqueString(100), getUniqueString(15), getUniqueString(5),
                getUniqueString(201));
        String agents = StringUtils.join(agentList, ',');

        action.setAgentList(agents);
        Set<String> splitAgents = action.getAgentNameSet();
        assertTrue(agentList.containsAll(splitAgents));
        assertEquals(agentList.size(), splitAgents.size() + 1);
        assertFalse(splitAgents.containsAll(agentList));
        assertTrue(action.hasFieldErrors());
    }

    @Test
    public void testGetAgentList_ProtocolAgents() {
        when(mockProtocolService.create()).thenReturn(protocol);
        action.prepare();

        assertEquals(getAgentString(action.getProtocol().getAgents()), action.getAgentList());
        action.setAgentList(null);
        assertEquals(null, action.getAgentList());
    }

    @Test
    public void testGetAgentList_AgentsAdded() {
        String agentList = getAgentString(action.getProtocol().getAgents()) + ",1234,5432";
        action.setAgentList(agentList);
        assertEquals(agentList, action.getAgentList());
    }

    @Test
    public void testGetAgentList_NoAgents() {
        action.getProtocol().getAgents().clear();
        action.setAgentList(null);
        assertEquals(null, action.getAgentList());
    }

    private String getAgentString(Set<ProtocolAgent> agents) {
        final StrBuilder agentString = new StrBuilder();
        for (ProtocolAgent agent : agents) {
            agentString.appendSeparator(", ");
            agentString.append(agent.getName());
        }
        return agentString.toString();
    }

    @Test
    public void testDoExistingLeadOrganizationMappingSetup_InitialEntry() {
        action.doExistingLeadOrganizationMappingSetup();
        verifyProtocolLeadOrganizationsInMappings(protocol);
    }

    private void verifyProtocolLeadOrganizationsInMappings(Protocol protocol) {
        assertFalse(protocol.getLeadOrganizations().isEmpty());
        assertEquals(protocol.getLeadOrganizations().size(), action.getLeadOrganizationMappings().size());
        for (ProtocolLeadOrganization leadOrganization : protocol.getLeadOrganizations()) {
            assertTrue(action.getLeadOrganizationMappings().containsKey(leadOrganization.getOrganization()));
            if (leadOrganization.getPrincipalInvestigator() != null) {
                assertTrue(action.getLeadOrganizationMappings().containsValue(
                        Lists.newArrayList(leadOrganization.getPrincipalInvestigator())));
            }
        }
    }

    @Test
    public void testGetSponsorListOrgsExist() {
        Organization organization = setUpSponsorUser();
        action.prepare();
        assertEquals(1, action.getSponsorList().size());
        assertEquals(organization, action.getSponsorList().get(0));
    }

    private Organization setUpSponsorUser() {
        Organization organization = OrganizationFactory.getInstance().create();
        FirebirdUser user = new FirebirdUser();
        user.setUsername("username");
        SponsorRole role = user.addSponsorRepresentativeRole(organization);
        FirebirdWebTestUtility.setCurrentUser(action, user);
        UserSessionInformation userSessionInformation = UserSessionInformationFactory.getInstance().create(
                user.getUsername(), role.getVerifiedSponsorGroupName());
        FirebirdWebTestUtility.setUpGridSessionInformation(action, userSessionInformation);
        return organization;
    }

    @Test
    public void testSaveProtocol_NoLeadOrganizations() {
        action.saveProtocol();
        assertTrue(protocol.getLeadOrganizations().isEmpty());
    }

    @Test
    public void testSaveProtocol_SingleLeadOrganizations() throws Exception {
        protocol.getLeadOrganizations().clear();
        ProtocolLeadOrganization leadOrganization = createPreparedLeadOrganizationMapping();
        action.saveProtocol();
        assertEquals(1, protocol.getLeadOrganizations().size());
        assertTrue(protocol.hasExistingLeadOrganization(leadOrganization.getOrganization(),
                leadOrganization.getPrincipalInvestigator()));
    }

    private ProtocolLeadOrganization createPreparedLeadOrganizationMapping() throws InvalidatedOrganizationException,
            InvalidatedPersonException {
        ProtocolLeadOrganization leadOrganization = addLeadOrganizationMapping();
        action.getLeadOrganizationMappings().put(leadOrganization.getOrganization(),
                Lists.newArrayList(leadOrganization.getPrincipalInvestigator()));

        return leadOrganization;
    }

    @Test
    public void testSaveProtocol_MultipleLeadOrganizations() throws Exception {
        protocol.getLeadOrganizations().clear();
        ProtocolLeadOrganization leadOrganization1 = createPreparedLeadOrganizationMapping();
        ProtocolLeadOrganization leadOrganization2 = createPreparedLeadOrganizationMapping();
        action.saveProtocol();
        assertEquals(2, protocol.getLeadOrganizations().size());
        assertTrue(protocol.hasExistingLeadOrganization(leadOrganization1.getOrganization(),
                leadOrganization1.getPrincipalInvestigator()));
        assertTrue(protocol.hasExistingLeadOrganization(leadOrganization2.getOrganization(),
                leadOrganization2.getPrincipalInvestigator()));
    }

    @Test
    public void testSaveProtocol_AddedLeadOrganizations() throws Exception {
        ProtocolLeadOrganization leadOrganization = createPreparedLeadOrganizationMapping();
        ProtocolLeadOrganization existingLeadOrganization = Iterables.getFirst(protocol.getLeadOrganizations(), null);

        action.getLeadOrganizationMappings().put(existingLeadOrganization.getOrganization(),
                Lists.newArrayList(existingLeadOrganization.getPrincipalInvestigator()));
        action.getLeadOrganizationIdMappings().put(existingLeadOrganization.getOrganization().getExternalId(),
                Lists.newArrayList(existingLeadOrganization.getPrincipalInvestigator().getExternalId()));

        action.saveProtocol();
        assertEquals(2, protocol.getLeadOrganizations().size());
        assertTrue(protocol.hasExistingLeadOrganization(leadOrganization.getOrganization(),
                leadOrganization.getPrincipalInvestigator()));
        assertTrue(protocol.getLeadOrganizations().contains(existingLeadOrganization));
    }

    @Test
    public void testSaveProtocol_RemovedLeadOrganizations() throws Exception {
        action.saveProtocol();
        assertEquals(0, protocol.getLeadOrganizations().size());
    }

    @Test
    public void testSaveProtocol_ReplaceLeadOrganization() throws Exception {
        ProtocolLeadOrganization existingLeadOrganization = Iterables.getFirst(protocol.getLeadOrganizations(), null);
        Person newPrincipalInvestigator = PersonFactory.getInstanceWithId().create();

        action.getLeadOrganizationMappings().put(existingLeadOrganization.getOrganization(),
                Lists.newArrayList(newPrincipalInvestigator));
        action.getLeadOrganizationIdMappings().put(existingLeadOrganization.getOrganization().getExternalId(),
                Lists.newArrayList(newPrincipalInvestigator.getExternalId()));

        action.saveProtocol();

        assertEquals(1, protocol.getLeadOrganizations().size());
        assertTrue(protocol.hasExistingLeadOrganization(existingLeadOrganization.getOrganization(),
                newPrincipalInvestigator));
        assertFalse(protocol.hasExistingLeadOrganization(existingLeadOrganization.getOrganization(),
                existingLeadOrganization.getPrincipalInvestigator()));
        assertFalse(protocol.getLeadOrganizations().contains(existingLeadOrganization));
    }

    @Test
    public void testSaveProtocol_RemoveAllLeadOrganizations() throws Exception {
        addLeadOrganizationToProtocol();
        addLeadOrganizationToProtocol();
        addLeadOrganizationToProtocol();
        action.getLeadOrganizationMappings().clear();

        assertFalse(action.getProtocol().getLeadOrganizations().isEmpty());
        action.saveProtocol();
        assertTrue(action.getProtocol().getLeadOrganizations().isEmpty());
    }
}
