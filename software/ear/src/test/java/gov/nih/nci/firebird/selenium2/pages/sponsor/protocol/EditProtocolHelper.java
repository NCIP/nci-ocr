package gov.nih.nci.firebird.selenium2.pages.sponsor.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.data.ProtocolPhase;
import gov.nih.nci.firebird.test.ValueGenerator;

import java.util.List;

import com.google.common.collect.Sets;

public final class EditProtocolHelper extends AbstractProtocolModificationPageHelper{

    private final EditProtocolPage page;

    EditProtocolHelper(EditProtocolPage page) {
        super(page);
        this.page = page;
    }

    public void checkProtocolDisplayed(Protocol protocol) {
        assertEquals(protocol.getProtocolNumber(), page.getProtocolNumber());
        assertEquals(protocol.getProtocolTitle(), page.getProtocolTitle());
        assertEquals(protocol.getPhase().getDisplay(), page.getPhase());
        assertEquals(protocol.getSponsor().getName().toString(), page.getHelper().getSponsor());
        List<String> agentNames = page.getAgentNames();
        assertEquals(protocol.getAgents().size(), agentNames.size());
        for (ProtocolAgent agent : protocol.getAgents()) {
            assertTrue("Could not find agent '" + agent.getName() + "' in the list + " + agentNames,
                    agentNames.contains(agent.getName()));
        }
    }

    public void checkSaveButtonToggling(Protocol protocol, ProtocolLeadOrganization alternateLeadOrganization) {
        assertFalse(page.isSaveButtonPresent());
        checkProtocolNumberTogglesSaveButton(protocol);
        checkProtocolTitleTogglesSaveButton(protocol);
        checkLeadOrganizationTogglesSaveButton(alternateLeadOrganization);
        checkPhaseTogglesSaveButton(protocol);
        checkAgentTogglesSaveButton();
    }

    private void checkProtocolNumberTogglesSaveButton(Protocol protocol) {
        page.setProtocolNumber(protocol.getProtocolNumber() + "1");
        assertTrue(page.isSaveButtonPresent());
        page.setProtocolNumber(protocol.getProtocolNumber());
        assertFalse(page.isSaveButtonPresent());
    }

    private void checkProtocolTitleTogglesSaveButton(Protocol protocol) {
        page.setProtocolTitle(protocol.getProtocolTitle() + "1");
        assertTrue(page.isSaveButtonPresent());
        page.setProtocolTitle(protocol.getProtocolTitle());
        assertFalse(page.isSaveButtonPresent());
    }

    private void checkLeadOrganizationTogglesSaveButton(ProtocolLeadOrganization alternateLeadOrganization) {
        CreateLeadOrganizationDialog createLeadOrganizationDialog = page.clickSelectLeadOrganizationBtn();
        createLeadOrganizationDialog.getHelper().enterDataAndSave(alternateLeadOrganization);
        assertTrue(page.isSaveButtonPresent());
        getLeadOrganizationListing(alternateLeadOrganization).clickDeleteLeadOrganization();
        assertFalse(page.isSaveButtonPresent());
    }

    public Protocol changeProtocol() {
        Protocol editedProtocol = new Protocol();
        editedProtocol.setProtocolNumber(ValueGenerator.getUniqueString());
        editedProtocol.setProtocolTitle(ValueGenerator.getUniqueString());
        editedProtocol.setPhase(ProtocolPhase.PHASE_4);
        editedProtocol.updateAgents(Sets.newHashSet(ValueGenerator.getUniqueString()));
        editedProtocol.setSponsor(new Organization());
        changeProtocol(editedProtocol, "some comment");
        return editedProtocol;
    }

     public void changeProtocol(Protocol protocol, String comment) {
         page.setProtocolNumber(protocol.getProtocolNumber());
         page.setProtocolTitle(protocol.getProtocolTitle());
         page.selectPhase(protocol.getPhase());
         for (String agent : page.getAgentNames()) {
             page.deleteAgent(agent);
         }
         for (ProtocolAgent agent : protocol.getAgents()) {
             page.getHelper().addAgent(agent.getName());
         }
         page.typeComment(comment);
     }

    private void checkPhaseTogglesSaveButton(Protocol protocol) {
        ProtocolPhase newPhase = (protocol.getPhase().ordinal() == 0) ? ProtocolPhase.values()[1]
                                                                      : ProtocolPhase.values()[0];
        page.selectPhase(newPhase);
        assertTrue(page.isSaveButtonPresent());
        page.selectPhase(protocol.getPhase());
        assertFalse(page.isSaveButtonPresent());
    }

    private void checkAgentTogglesSaveButton() {
        page.getHelper().addAgent("agent");
        assertTrue(page.isSaveButtonPresent());
        page.deleteAgent("agent");
        assertFalse(page.isSaveButtonPresent());
    }

}