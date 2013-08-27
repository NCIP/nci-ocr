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
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.service.investigatorprofile.ProfileNesRefreshService;
import gov.nih.nci.firebird.service.organization.OrganizationSearchService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonSearchService;
import gov.nih.nci.firebird.service.protocol.ProtocolService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

@SuppressWarnings("serial")
public class AbstractModifyProtocolActionTest extends AbstractWebTest {

    @Inject
    private ProtocolService mockProtocolService;
    @Inject
    private OrganizationService mockOrgService;
    @Inject
    private OrganizationSearchService mockOrgSearchService;
    @Inject
    private PersonSearchService mockPersonSearchService;
    @Inject
    private ProfileNesRefreshService mockProfileRefreshService;

    private Protocol protocol = ProtocolFactory.getInstanceWithId().createWithFormsDocuments();

    private AbstractModifyProtocolAction action;
    private FirebirdUser user;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        action = new AbstractModifyProtocolAction(mockProtocolService, mockOrgService) {
            @Override
            protected void performSave() throws ValidationException {/* Do Nothing */
            }
        };
        action.setProfileRefreshService(mockProfileRefreshService);
        action.setProtocol(protocol);
        action.setPersonSearchService(mockPersonSearchService);
        action.setOrganizationSearchService(mockOrgSearchService);
        when(mockProtocolService.getById(protocol.getId())).thenReturn(protocol);
        when(mockProtocolService.create()).thenReturn(new Protocol());
        user = FirebirdUserFactory.getInstance().create();
        FirebirdWebTestUtility.setCurrentUser(action, user);
        if (protocol.getLeadOrganizations().isEmpty()) {
            addLeadOrganizationToProtocol();
        }
    }

    private ProtocolLeadOrganization addLeadOrganizationToProtocol() throws UnavailableEntityException {
        ProtocolLeadOrganization leadOrganization = createLeadOrganzation();
        return protocol.addLeadOrganization(leadOrganization.getOrganization(),
                leadOrganization.getPrincipalInvestigator());
    }

    private ProtocolLeadOrganization createLeadOrganzation() throws UnavailableEntityException {
        return createLeadOrganzation(OrganizationFactory.getInstanceWithId().create(), PersonFactory
                .getInstanceWithId().create());
    }

    private ProtocolLeadOrganization createLeadOrganzation(Organization organization, Person principalInvestigator)
            throws UnavailableEntityException {
        ProtocolLeadOrganization leadOrganization = new ProtocolLeadOrganization(null, organization,
                principalInvestigator);
        String organizationId = action.getIdAsString(leadOrganization.getOrganization());
        String principalInvestigatorId = action.getIdAsString(leadOrganization.getPrincipalInvestigator());
        when(mockOrgSearchService.getOrganization(organizationId)).thenReturn(leadOrganization.getOrganization());
        when(mockPersonSearchService.getPerson(principalInvestigatorId)).thenReturn(
                leadOrganization.getPrincipalInvestigator());

        return leadOrganization;
    }

    @Test
    public void testPrepare_NoSponsor() {
        action.setSponsor(null);
        action.prepare();
        assertNull(action.getSponsor());
    }

    @Test
    public void testPrepare_SponsorNoId() {
        Organization sponsor = new Organization();
        action.setSponsor(sponsor);
        action.prepare();
        assertSame(sponsor, action.getSponsor());
    }

    @Test
    public void testPrepare_SponsorWithId() {
        long sponsorId = 1L;
        Organization sponsor = new Organization();
        sponsor.setId(sponsorId);
        Organization dbSponsor = OrganizationFactory.getInstance().create();
        dbSponsor.setId(sponsorId);
        when(mockOrgService.getById(sponsorId)).thenReturn(dbSponsor);

        action.setSponsor(sponsor);
        action.prepare();
        assertSame(dbSponsor, action.getSponsor());
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

    private void verifyProtocolLeadOrganizationsInMappings(Protocol protocol) {
        assertFalse(protocol.getLeadOrganizations().isEmpty());
        assertEquals(protocol.getLeadOrganizations().size(), action.getLeadOrganizationMappings().size());
        for (ProtocolLeadOrganization leadOrganization : protocol.getLeadOrganizations()) {
            String organizationId = action.getIdAsString(leadOrganization.getOrganization());
            String principalInvestigatorId = action.getIdAsString(leadOrganization.getPrincipalInvestigator());
            assertTrue(action.getLeadOrganizationMappings().containsKey(organizationId));
            assertEquals(action.getLeadOrganizations().get(organizationId), leadOrganization.getOrganization());
            if (leadOrganization.getPrincipalInvestigator() != null) {
                assertTrue(action.getLeadOrganizationMappings().containsValue(Lists.newArrayList(principalInvestigatorId)));
                assertEquals(action.getPrincipalInvestigators().get(principalInvestigatorId),
                        leadOrganization.getPrincipalInvestigator());
            }
        }
    }

    @Test
    public void testPrepare_MultipleLeadOrganizationsWithSamePI() throws Exception {
        ProtocolLeadOrganization leadOrganization = addLeadOrganizationMapping();
        ProtocolLeadOrganization leadOrganization2 = createLeadOrganzation(OrganizationFactory.getInstanceWithId()
                .create(), leadOrganization.getPrincipalInvestigator());
        addLeadOrganizationMapping(leadOrganization2);
        action.prepare();

        verify(mockPersonSearchService).getPerson(action.getIdAsString(leadOrganization.getPrincipalInvestigator()));
        verify(mockOrgSearchService).getOrganization(action.getIdAsString(leadOrganization.getOrganization()));
        verify(mockOrgSearchService).getOrganization(action.getIdAsString(leadOrganization2.getOrganization()));
    }

    private ProtocolLeadOrganization addLeadOrganizationMapping() throws UnavailableEntityException {
        return addLeadOrganizationMapping(createLeadOrganzation());
    }

    private ProtocolLeadOrganization addLeadOrganizationMapping(ProtocolLeadOrganization leadOrganization)
            throws UnavailableEntityException {
        String organizationId = action.getIdAsString(leadOrganization.getOrganization());
        String principalInvestigatorId = action.getIdAsString(leadOrganization.getPrincipalInvestigator());
        action.getLeadOrganizationMappings().put(organizationId, Lists.newArrayList(principalInvestigatorId));

        return leadOrganization;
    }

    @Test
    public void testPrepare_UnavailableOrganization() throws Exception {
        ProtocolLeadOrganization leadOrganization = addLeadOrganizationMapping();
        when(mockOrgSearchService.getOrganization(action.getIdAsString(leadOrganization.getOrganization()))).thenThrow(
                new UnavailableEntityException(null, null));
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

    private ProtocolLeadOrganization createPreparedLeadOrganizationMapping() throws UnavailableEntityException {
        ProtocolLeadOrganization leadOrganization = addLeadOrganizationMapping();
        String organizationId = action.getIdAsString(leadOrganization.getOrganization());
        String principalInvestigatorId = action.getIdAsString(leadOrganization.getPrincipalInvestigator());
        action.getLeadOrganizations().put(organizationId, leadOrganization.getOrganization());
        action.getPrincipalInvestigators().put(principalInvestigatorId, leadOrganization.getPrincipalInvestigator());

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
        String leadOrganizationKey = action.getIdAsString(existingLeadOrganization.getOrganization());
        String principalInvestigatorKey = action.getIdAsString(existingLeadOrganization.getPrincipalInvestigator());
        action.getLeadOrganizationMappings().put(leadOrganizationKey, Lists.newArrayList(principalInvestigatorKey));
        action.getLeadOrganizations().put(leadOrganizationKey, existingLeadOrganization.getOrganization());
        action.getPrincipalInvestigators().put(principalInvestigatorKey,
                existingLeadOrganization.getPrincipalInvestigator());
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
        String leadOrganizationKey = ObjectUtils.toString(existingLeadOrganization.getOrganization().getId());
        String principalInvestigatorKey = ObjectUtils.toString(newPrincipalInvestigator.getId());
        action.getLeadOrganizationMappings().put(leadOrganizationKey, Lists.newArrayList(principalInvestigatorKey));
        action.getLeadOrganizations().put(leadOrganizationKey, existingLeadOrganization.getOrganization());
        action.getPrincipalInvestigators().put(principalInvestigatorKey, newPrincipalInvestigator);
        action.saveProtocol();

        assertEquals(1, protocol.getLeadOrganizations().size());
        assertTrue(protocol.hasExistingLeadOrganization(existingLeadOrganization.getOrganization(),
                newPrincipalInvestigator));
        assertFalse(protocol.hasExistingLeadOrganization(existingLeadOrganization.getOrganization(),
                existingLeadOrganization.getPrincipalInvestigator()));
        assertFalse(protocol.getLeadOrganizations().contains(existingLeadOrganization));
    }

    @Test
    public void testSaveProtocol_RemoveAllLeadOrganizations() throws UnavailableEntityException {
        addLeadOrganizationToProtocol();
        addLeadOrganizationToProtocol();
        addLeadOrganizationToProtocol();
        action.getLeadOrganizationMappings().clear();

        assertFalse(action.getProtocol().getLeadOrganizations().isEmpty());
        action.saveProtocol();
        assertTrue(action.getProtocol().getLeadOrganizations().isEmpty());
    }
}
