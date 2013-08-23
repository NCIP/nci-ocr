package gov.nih.nci.firebird.web.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import org.junit.Test;

public class ProtocolUiUtilsTest {

    @Test(expected = NullPointerException.class)
    public void testGetProtocolLeadOrganizationsJson_NullProtocol() throws Exception {
        ProtocolUiUtils.getProtocolLeadOrganizationsJson(null);
    }

    @Test
    public void testGetProtocolLeadOrganizationsJson_NoLeadOrganizations() throws Exception {
        Protocol protocol = new Protocol();
        String json = ProtocolUiUtils.getProtocolLeadOrganizationsJson(protocol);
        assertEquals("[]", json);
    }

    @Test
    public void testGetProtocolLeadOrganizationsJson() throws Exception {
        Protocol protocol = new Protocol();
        ProtocolLeadOrganization leadOrganization = protocol.addLeadOrganization(
                OrganizationFactory.getInstance().create(), PersonFactory.getInstance().create());
        ProtocolLeadOrganization leadOrganization2 = protocol.addLeadOrganization(
                OrganizationFactory.getInstance().create(), PersonFactory.getInstance().create());

        String json = ProtocolUiUtils.getProtocolLeadOrganizationsJson(protocol);
        verifyJsonContainsLeadOrganizationInfo(json, leadOrganization);
        verifyJsonContainsLeadOrganizationInfo(json, leadOrganization2);
    }

    private void verifyJsonContainsLeadOrganizationInfo(String json, ProtocolLeadOrganization leadOrganization) {
        assertTrue("JSON did not contain expected Value! " + json, json.contains(
                "\"name\":\"" + leadOrganization.getOrganization().getName() + "\""));
        assertTrue("JSON did not contain expected Value! " + json, json.contains(
                "\"displayName\":\"" + leadOrganization.getPrincipalInvestigator().getDisplayName() + "\""));
    }
}
