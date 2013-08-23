package gov.nih.nci.firebird.selenium2.pages.sponsor.representative.protocol;

import static org.junit.Assert.assertNotNull;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;

public class ProtocolImportDetailDialogHelper {

    private final ProtocolImportDetailDialog dialog;

    ProtocolImportDetailDialogHelper(ProtocolImportDetailDialog protocolImportDetailDialog) {
        dialog = protocolImportDetailDialog;
    }

    public void checkLeadOrganizationInTable(ProtocolLeadOrganization expectedLeadOrganization) {
        assertNotNull(dialog.getProtocolInformationTag().getHelper().getLeadOrganizationListing(
                expectedLeadOrganization));
    }
}
