package gov.nih.nci.firebird.selenium2.pages.sponsor.representative.delegates;

import static gov.nih.nci.firebird.selenium2.pages.util.FirebirdTableUtils.getEquivalent;

import gov.nih.nci.firebird.commons.selenium2.util.WaitUtils;
import gov.nih.nci.firebird.data.user.SponsorRole;
import gov.nih.nci.firebird.selenium2.pages.base.ConfirmDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.delegates.BrowseSponsorDelegatesPage.SponsorDelegateListing;
import gov.nih.nci.firebird.selenium2.pages.util.FirebirdTableUtils.EquivalenceChecker;

import org.apache.commons.lang.builder.EqualsBuilder;

public class BrowseSponsorDelegatesPageHelper {

    private final BrowseSponsorDelegatesPage page;

    private static final EquivalenceChecker<SponsorDelegateListing, SponsorRole> SPONSOR_LISTING_EQUIVALENCE_CHECK =
            new EquivalenceChecker<BrowseSponsorDelegatesPage.SponsorDelegateListing, SponsorRole>() {
        @Override
        public boolean isEquivalent(SponsorDelegateListing listing, SponsorRole sponsorRole) {
            return new EqualsBuilder()
            .append(listing.getDisplayName(), sponsorRole.getUser().getPerson().getDisplayNameForList())
            .append(listing.getSponsor(), sponsorRole.getSponsor().getName())
            .append(listing.getId(), sponsorRole.getId()).isEquals();
        }
    };

    BrowseSponsorDelegatesPageHelper(BrowseSponsorDelegatesPage browseSponsorDelegatesPage) {
        page = browseSponsorDelegatesPage;
    }

    public void remove(SponsorRole role) {
        ConfirmDialog confirmDialog = getListing(role).clickRemove();
        confirmDialog.getHelper().checkTitleAndMessage(role.getSponsor().getName(), role.getUser().getPerson().getDisplayName());
        confirmDialog.clickConfirmButton();
        WaitUtils.pause(100); //Sometimes too quick to check page after dialog closes.
    }

    public SponsorDelegateListing getListing(SponsorRole role) {
        return getEquivalent(page.getDelegateListings(), role, SPONSOR_LISTING_EQUIVALENCE_CHECK);
    }

    public boolean isListed(SponsorRole role) {
        return getListing(role) != null;
    }
}