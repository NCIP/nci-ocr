package gov.nih.nci.firebird.selenium2.pages.components.tags;

import static gov.nih.nci.firebird.selenium2.pages.util.FirebirdTableUtils.getEquivalent;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.selenium2.pages.util.FirebirdTableUtils;

public class ProtocolInformationTagHelper {

    private final ProtocolInformationTag tag;

    private static final
    FirebirdTableUtils.EquivalenceChecker<ProtocolInformationTag.LeadOrganizationListing, ProtocolLeadOrganization>
            LEAD_ORGANIZATION_EQUIVALENCE_CHECKER = new FirebirdTableUtils.EquivalenceChecker
            <ProtocolInformationTag.LeadOrganizationListing, ProtocolLeadOrganization>() {
        @Override
        public boolean isEquivalent(ProtocolInformationTag.LeadOrganizationListing listing,
                                    ProtocolLeadOrganization leadOrganization) {
            boolean isOrganizationNameEqual = leadOrganization.getOrganization().getName().equals(
                    listing.getOrganizationName());
            boolean isPrincipalInvestigatorEqual =
                    ((leadOrganization.getPrincipalInvestigator() == null
                      && listing.getPrincipalInvestigatorName().equals("None"))
                      || leadOrganization.getPrincipalInvestigator().getDisplayName().equals(
                            listing.getPrincipalInvestigatorName()));

            return isOrganizationNameEqual && isPrincipalInvestigatorEqual;
        }
    };

    ProtocolInformationTagHelper(ProtocolInformationTag protocolInformationTag) {
        this.tag = protocolInformationTag;
    }

    public ProtocolInformationTag.LeadOrganizationListing getLeadOrganizationListing(
            ProtocolLeadOrganization leadOrganization) {
        return getEquivalent(tag.getLeadOrganizationListings(), leadOrganization,
                             LEAD_ORGANIZATION_EQUIVALENCE_CHECKER);
    }

    public void checkLeadOrganizationsDisplayed(Protocol protocol) {
        for (ProtocolLeadOrganization leadOrganization : protocol.getLeadOrganizations()) {
            assertNotNull(getLeadOrganizationListing(leadOrganization));
        }
    }
}
