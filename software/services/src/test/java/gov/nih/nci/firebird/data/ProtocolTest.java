/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the NCI OCR Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the NCI OCR Software; (ii) distribute and
 * have distributed to and by third parties the NCI OCR Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM and the National Cancer Institute. If You do not include
 * such end-user documentation, You shall include this acknowledgment in the
 * Software itself, wherever such third-party acknowledgments normally appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", or "5AM"
 * to endorse or promote products derived from this Software. This License does
 * not authorize You to use any trademarks, service marks, trade names, logos or
 * product names of either NCI or 5AM, except as required to comply with the
 * terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC. OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.firebird.data;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Test;

public class ProtocolTest {

    @Test
    public void testUpdateAgents() {
        Protocol protocol = new Protocol();
        ProtocolAgent existingAgent1 = new ProtocolAgent("agent1");
        ProtocolAgent existingAgent2 = new ProtocolAgent("agent2");
        protocol.getAgents().add(existingAgent1);
        protocol.getAgents().add(existingAgent2);
        Set<String> updatedAgentNames = new HashSet<String>();
        updatedAgentNames.add("agent2");
        updatedAgentNames.add("agent3");
        protocol.updateAgents(updatedAgentNames);
        assertEquals(2, protocol.getAgents().size());
        Iterator<ProtocolAgent> iterator = protocol.getAgents().iterator();
        assertEquals(existingAgent2, iterator.next());
        assertEquals("agent3", iterator.next().getName());
    }

    @Test
    public void testAddInvestigatorRegistration() {
        Protocol protocol = new Protocol();
        assertTrue(protocol.getCurrentInvestigatorRegistrations().isEmpty());
        InvestigatorRegistration reg = RegistrationFactory.getInstance().createInvestigatorRegistration();
        protocol.addRegistration(reg);
        protocol.addRegistration(RegistrationFactory.getInstance().createSubinvestigatorRegistration(reg));
        assertSame(protocol, reg.getProtocol());
        assertEquals(1, protocol.getCurrentInvestigatorRegistrations().size());
        assertSame(reg, protocol.getCurrentInvestigatorRegistrations().iterator().next());
    }

    @Test
    public void testGetSubinvestigatorRegistrations() {
        Protocol protocol = new Protocol();
        assertTrue(protocol.getCurrentInvestigatorRegistrations().isEmpty());
        InvestigatorRegistration reg = RegistrationFactory.getInstance().createInvestigatorRegistration();
        SubInvestigatorRegistration subReg = RegistrationFactory.getInstance().createSubinvestigatorRegistration(reg);
        protocol.addRegistration(reg);
        protocol.addRegistration(subReg);
        assertEquals(1, protocol.getSubinvestigatorRegistrations().size());
        assertEquals(subReg, protocol.getSubinvestigatorRegistrations().iterator().next());
    }

    @Test
    public void testGetActiveSubinvestigatorRegistrations() {
        Protocol protocol = new Protocol();
        assertTrue(protocol.getCurrentInvestigatorRegistrations().isEmpty());
        InvestigatorRegistration reg = RegistrationFactory.getInstance().createInvestigatorRegistration();
        SubInvestigatorRegistration activeSubReg = RegistrationFactory.getInstance().createSubinvestigatorRegistration(
                reg);
        activeSubReg.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        SubInvestigatorRegistration noResponseSubReg = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(reg);
        noResponseSubReg.getInvitation().setInvitationStatus(InvitationStatus.NO_RESPONSE);
        SubInvestigatorRegistration unInvitedSubReg = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(reg);
        unInvitedSubReg.getInvitation().setInvitationStatus(InvitationStatus.NOT_INVITED);
        SubInvestigatorRegistration inactiveRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(reg);
        inactiveRegistration.setPrimaryRegistration(null);

        protocol.addRegistration(reg);
        protocol.addRegistration(activeSubReg);
        protocol.addRegistration(unInvitedSubReg);
        protocol.addRegistration(noResponseSubReg);
        protocol.addRegistration(inactiveRegistration);
        assertEquals(2, protocol.getActiveSubinvestigatorRegistrations().size());
        assertTrue(protocol.getActiveSubinvestigatorRegistrations().contains(activeSubReg));
        assertTrue(protocol.getActiveSubinvestigatorRegistrations().contains(noResponseSubReg));
    }

    @Test
    public void testAddLeadOrganization() {
        Protocol protocol = new Protocol();
        Organization organization = OrganizationFactory.getInstance().create();
        Person principalInvestigator = PersonFactory.getInstance().create();
        ProtocolLeadOrganization leadOrganization = protocol.addLeadOrganization(organization, principalInvestigator);
        assertEquals(protocol, leadOrganization.getProtocol());
        assertEquals(organization, leadOrganization.getOrganization());
        assertEquals(principalInvestigator, leadOrganization.getPrincipalInvestigator());
    }

    @Test
    public void testAddLeadOrganization_SameOrganizationDifferentInvestigator() {
        Protocol protocol = new Protocol();
        Organization organization = OrganizationFactory.getInstance().create();
        Person principalInvestigator1 = PersonFactory.getInstance().create();
        Person principalInvestigator2 = PersonFactory.getInstance().create();
        ProtocolLeadOrganization leadOrganization1 = protocol.addLeadOrganization(organization, principalInvestigator1);
        ProtocolLeadOrganization leadOrganization2 = protocol.addLeadOrganization(organization, principalInvestigator2);

        assertEquals(protocol, leadOrganization1.getProtocol());
        assertEquals(organization, leadOrganization1.getOrganization());
        assertEquals(principalInvestigator1, leadOrganization1.getPrincipalInvestigator());

        assertEquals(protocol, leadOrganization2.getProtocol());
        assertEquals(organization, leadOrganization2.getOrganization());
        assertEquals(principalInvestigator2, leadOrganization2.getPrincipalInvestigator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLeadOrganization_DuplicateLeadOrganization() {
        Protocol protocol = new Protocol();
        Organization organization = OrganizationFactory.getInstance().create();
        Person principalInvestigator = PersonFactory.getInstance().create();
        protocol.addLeadOrganization(organization, principalInvestigator);
        protocol.addLeadOrganization(organization, principalInvestigator);
    }

    @Test
    public void testHasEquivalentLeadOrganization() {
        Protocol protocol = new Protocol();
        Organization organization = OrganizationFactory.getInstance().create();
        Person principalInvestigator = PersonFactory.getInstance().create();
        protocol.addLeadOrganization(organization, principalInvestigator);

        assertTrue(protocol.hasExistingLeadOrganization(organization, principalInvestigator));
    }

    @Test
    public void testHasEquivalentLeadOrganization_Different() {
        Protocol protocol = new Protocol();
        Organization organization = OrganizationFactory.getInstance().create();
        Person principalInvestigator = PersonFactory.getInstance().create();
        protocol.addLeadOrganization(organization, principalInvestigator);

        assertFalse(protocol.hasExistingLeadOrganization(organization, PersonFactory.getInstance().create()));
        assertFalse(protocol.hasExistingLeadOrganization(OrganizationFactory.getInstance().create(),
                principalInvestigator));
    }

    @Test
    public void testCreateCopy() {
        String[] ignoreFields = new String[] { "revisionHistory", "registrations", "registrationConfiguration",
                "leadOrganizations" };
        Protocol protocol = ProtocolFactory.getInstanceWithId().create();
        Protocol copy = protocol.createCopy();
        assertTrue(EqualsBuilder.reflectionEquals(protocol, copy, ignoreFields));
        checkConfigurationClone(protocol.getRegistrationConfiguration(), copy.getRegistrationConfiguration());
        checkLeadOrganizations(protocol.getLeadOrganizations(), copy.getLeadOrganizations());
    }

    private void checkConfigurationClone(ProtocolRegistrationConfiguration original,
            ProtocolRegistrationConfiguration clone) {
        assertFalse(original == clone);
        assertFalse(original.getInvestigatorConfiguration() == clone.getInvestigatorConfiguration());
        assertFalse(original.getSubinvestigatorConfiguration() == clone.getSubinvestigatorConfiguration());
        assertEquals(original.getInvestigatorConfiguration().getFormOptionalities(), clone
                .getInvestigatorConfiguration().getFormOptionalities());
        assertEquals(original.getSubinvestigatorConfiguration().getFormOptionalities(), clone
                .getSubinvestigatorConfiguration().getFormOptionalities());
    }

    private void checkLeadOrganizations(Set<ProtocolLeadOrganization> leadOrganizations,
            Set<ProtocolLeadOrganization> cloneLeadOrganizations) {
        for (ProtocolLeadOrganization leadOrganization : leadOrganizations) {
            boolean foundMatch = false;

            for (ProtocolLeadOrganization cloneLeadOrganization : cloneLeadOrganizations) {
                if (cloneLeadOrganization.getOrganization().equals(leadOrganization.getOrganization())
                        && cloneLeadOrganization.getPrincipalInvestigator().equals(
                                leadOrganization.getPrincipalInvestigator())) {
                    foundMatch = true;
                }
            }

            assertTrue(foundMatch);
        }
    }

    @Test
    public void testGetAllInvestigatorRegistrations() throws Exception {
        Protocol protocol = new Protocol();
        RevisedInvestigatorRegistration revisedRegistration = createCurrentAndRevisedRegistration(protocol);

        Set<InvestigatorRegistration> allRegistrations = protocol.getAllInvestigatorRegistrations();
        assertTrue(allRegistrations.contains(revisedRegistration.getCurrentRegistration()));
        assertTrue(allRegistrations.contains(revisedRegistration));
    }

    private RevisedInvestigatorRegistration createCurrentAndRevisedRegistration(Protocol protocol) {
        InvestigatorRegistration currentRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        currentRegistration.setStatus(RegistrationStatus.APPROVED);
        RevisedInvestigatorRegistration revisedRegistration = currentRegistration.createRevisedRegistration();
        protocol.addRegistration(currentRegistration);
        protocol.addRegistration(revisedRegistration);
        return revisedRegistration;
    }

    @Test
    public void testGetCurrentInvestigatorRegistrations() throws Exception {
        Protocol protocol = new Protocol();
        RevisedInvestigatorRegistration revisedRegistration = createCurrentAndRevisedRegistration(protocol);

        Set<InvestigatorRegistration> allRegistrations = protocol.getCurrentInvestigatorRegistrations();
        assertTrue(allRegistrations.contains(revisedRegistration.getCurrentRegistration()));
        assertFalse(allRegistrations.contains(revisedRegistration));
    }

    @Test
    public void testGetRevisedInvestigatorRegistrations() throws Exception {
        Protocol protocol = new Protocol();
        RevisedInvestigatorRegistration revisedRegistration = createCurrentAndRevisedRegistration(protocol);

        Set<RevisedInvestigatorRegistration> revisedRegistrations = protocol.getRevisedInvestigatorRegistrations();
        assertFalse(revisedRegistrations.contains(revisedRegistration.getCurrentRegistration()));
        assertTrue(revisedRegistrations.contains(revisedRegistration));
    }

}
