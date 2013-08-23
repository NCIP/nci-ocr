package gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;

public final class ProtocolInfoTabHelper {

    private final ProtocolInfoTab tab;

    ProtocolInfoTabHelper(ProtocolInfoTab tab) {
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
        for (ProtocolLeadOrganization leadOrganization : protocol.getLeadOrganizations()) {
            assertNotNull(tab.getProtocolInformationTag().getHelper().getLeadOrganizationListing(leadOrganization));
        }
    }

}