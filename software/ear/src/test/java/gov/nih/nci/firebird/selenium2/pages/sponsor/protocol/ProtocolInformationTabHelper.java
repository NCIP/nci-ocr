package gov.nih.nci.firebird.selenium2.pages.sponsor.protocol;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.Protocol;

import gov.nih.nci.firebird.data.ProtocolLeadOrganization;

public final class ProtocolInformationTabHelper {

    private final ProtocolInformationTab tab;

    public ProtocolInformationTabHelper(ProtocolInformationTab tab) {
        this.tab = tab;
    }

    public void checkRevisions(Protocol protocol, boolean isSponsor) {
        tab.getProtocolHistoryTag().getHelper().checkRevisions(protocol, isSponsor);
    }

    public void checkProtocolDisplayed(Protocol protocol) {
        assertEquals(protocol.getProtocolTitle(), tab.getProtocolTitle());
        assertEquals(protocol.getProtocolNumber(), tab.getSponsorProtocolId());
        assertEquals(protocol.getSponsor().getName(), tab.getSponsor());
        assertEquals(protocol.getPhase().getDisplay(), tab.getPhase());
        assertEquals(protocol.getAgentListForDisplay(), tab.getAgents());
        tab.getProtocolInformationTag().getHelper().checkLeadOrganizationsDisplayed(protocol);
    }

    public void checkLeadOrganizationListed(ProtocolLeadOrganization leadOrganization) {
        assertNotNull(tab.getProtocolInformationTag().getHelper().getLeadOrganizationListing(leadOrganization));
    }

    public void checkLeadOrganizationNotListed(ProtocolLeadOrganization leadOrganization) {
        assertNull(tab.getProtocolInformationTag().getHelper().getLeadOrganizationListing(leadOrganization));
    }

}