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
package gov.nih.nci.firebird.service.protocol;

import static gov.nih.nci.firebird.data.FormOptionality.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.data.ProtocolPhase;
import gov.nih.nci.firebird.data.ProtocolRevision;
import gov.nih.nci.firebird.test.FormTypeFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.junit.Test;

import com.google.common.collect.Iterables;

public class ProtocolModificationDetectorTest {

    private static final ResourceBundle resources = ResourceBundle.getBundle("resources");
    private static final String[] EXPECTED_INVESTIGATOR_MODIFICATION_DESCRIPTIONS = {
            getPropertyText("protocol.change.number.investigator.message", "initial", "some new number"),
            getPropertyText("protocol.change.title.investigator.message", "initial", "some new title"),
            getPropertyText("protocol.change.sponsor.investigator.message", "initial", "some other org"),
            getPropertyText("protocol.change.phase.investigator.message", "0", "IV"),
            getPropertyText("protocol.change.agent.removed.investigator.message", "initial"),
            getPropertyText("protocol.change.agent.added.investigator.message", "new agent 1"),
            getPropertyText("protocol.change.agent.added.investigator.message", "new agent 2"),
            getPropertyText("protocol.change.document.removed.investigator.message", "initial"),
            getPropertyText("protocol.change.document.added.investigator.message", "new file"),
            getPropertyText("protocol.change.form.optionality.investigator.investigator.message", "Form 1572",
                    "Required", "Optional"),
            getPropertyText("protocol.change.form.optionality.investigator.investigator.message",
                    "Financial Disclosure Form", "None", "Required"),
            getPropertyText("protocol.change.form.optionality.subinvestigator.investigator.message", "Form 1572",
                    "Optional", "None") };

    private static final String[] EXPECTED_SPONSOR_MODIFICATION_DESCRIPTIONS = {
            getPropertyText("protocol.change.number.sponsor.message", "initial", "some new number"),
            getPropertyText("protocol.change.title.sponsor.message", "initial", "some new title"),
            getPropertyText("protocol.change.sponsor.sponsor.message", "initial", "some other org"),
            getPropertyText("protocol.change.phase.sponsor.message", "0", "IV"),
            getPropertyText("protocol.change.agent.removed.sponsor.message", "initial"),
            getPropertyText("protocol.change.agent.added.sponsor.message", "new agent 1"),
            getPropertyText("protocol.change.agent.added.sponsor.message", "new agent 2"),
            getPropertyText("protocol.change.document.removed.sponsor.message", "initial"),
            getPropertyText("protocol.change.document.added.sponsor.message", "new file"),
            getPropertyText("protocol.change.form.optionality.investigator.sponsor.message", "Form 1572", "Required",
                    "Optional"),
            getPropertyText("protocol.change.form.optionality.investigator.sponsor.message",
                    "Financial Disclosure Form", "None", "Required"),
            getPropertyText("protocol.change.form.optionality.subinvestigator.sponsor.message", "Form 1572",
                    "Optional", "None") };

    private static final String LEAD_ORGANIZATION_REMOVED_INVESTIGATOR_KEY = "protocol.change.lead.organization.removed.investigator.message";
    private static final String LEAD_ORGANIZATION_REMOVED_SPONSOR_KEY = "protocol.change.lead.organization.removed.sponsor.message";

    private FormType form1572 = FormTypeFactory.getInstance().create(FormTypeEnum.FORM_1572);
    private FormType financialDisclosure = FormTypeFactory.getInstance().create(FormTypeEnum.FINANCIAL_DISCLOSURE_FORM);

    protected static String getPropertyText(String property, String... replacements) {
        return MessageFormat.format(resources.getString(property), (Object[]) replacements);
    }

    @Test
    public void testIsModificationDetected() {
        Protocol originalProtocol = createProtocol();
        ProtocolModificationDetector detector = new ProtocolModificationDetector(originalProtocol, originalProtocol,
                resources);
        assertFalse(detector.isModificationDetected());

        Protocol modifiedProtocol = getChangedCopy(originalProtocol);
        detector = new ProtocolModificationDetector(originalProtocol, modifiedProtocol, resources);
        assertTrue(detector.isModificationDetected());
    }

    private Protocol getChangedCopy(final Protocol original) {
        Protocol protocol = original.createCopy();
        protocol.getDocuments().clear();
        FirebirdFile file = new FirebirdFile(null, 0, "new file", null, null);
        protocol.getDocuments().add(file);
        protocol.getRegistrationConfiguration().setInvestigatorOptionality(form1572, OPTIONAL);
        protocol.getRegistrationConfiguration().setInvestigatorOptionality(financialDisclosure, REQUIRED);
        protocol.getRegistrationConfiguration().setSubinvestigatorOptionality(form1572, NONE);
        protocol.setProtocolTitle("some new title");
        protocol.setProtocolNumber("some new number");
        protocol.setPhase(ProtocolPhase.PHASE_4);
        Organization sponsor = OrganizationFactory.getInstance().create();
        sponsor.setName("some other org");
        protocol.setSponsor(sponsor);
        protocol.getAgents().clear();
        protocol.getAgents().add(new ProtocolAgent("new agent 1"));
        protocol.getAgents().add(new ProtocolAgent("new agent 2"));
        return protocol;
    }

    private Protocol createProtocol() {
        Protocol protocol = new Protocol();
        FirebirdFile file = new FirebirdFile(null, 0, "initial", null, null);
        protocol.getDocuments().add(file);
        protocol.getRegistrationConfiguration().setInvestigatorOptionality(form1572, REQUIRED);
        protocol.getRegistrationConfiguration().setSubinvestigatorOptionality(form1572, OPTIONAL);
        protocol.setProtocolTitle("initial");
        protocol.setProtocolNumber("initial");
        protocol.setPhase(ProtocolPhase.PHASE_0);
        Organization sponsor = OrganizationFactory.getInstance().create();
        sponsor.setName("initial");
        protocol.setSponsor(sponsor);
        protocol.getAgents().add(new ProtocolAgent("initial"));
        return protocol;
    }

    @Test
    public void testAddModifications() {
        Protocol originalProtocol = createProtocol();
        Protocol modifiedProtocol = getChangedCopy(originalProtocol);
        ProtocolModificationDetector detector = new ProtocolModificationDetector(originalProtocol, modifiedProtocol,
                resources);
        ProtocolRevision revision = new ProtocolRevision();
        detector.addModifications(revision);
        assertEquals(EXPECTED_INVESTIGATOR_MODIFICATION_DESCRIPTIONS.length, revision.getModifications().size());
        for (int i = 0; i < EXPECTED_INVESTIGATOR_MODIFICATION_DESCRIPTIONS.length; i++) {
            assertEquals(EXPECTED_INVESTIGATOR_MODIFICATION_DESCRIPTIONS[i], revision.getModifications().get(i)
                    .getInvestigatorDescription());
            assertEquals(EXPECTED_SPONSOR_MODIFICATION_DESCRIPTIONS[i], revision.getModifications().get(i)
                    .getSponsorDescription());
        }
    }

    @Test
    public void testAddModificationsNoChange() {
        Protocol unmodifiedProtocol = createProtocol();
        ProtocolModificationDetector detector = new ProtocolModificationDetector(unmodifiedProtocol,
                unmodifiedProtocol, resources);
        ProtocolRevision revision = new ProtocolRevision();
        detector.addModifications(revision);
        assertTrue(revision.getModifications().isEmpty());
    }

    @Test
    public void testAddModifications_RemovedLeadOrganization() {
        Protocol originalProtocol = ProtocolFactory.getInstanceWithId().create();
        Protocol changedProtocol = originalProtocol.createCopy();
        ProtocolLeadOrganization leadOrganization = Iterables.getFirst(originalProtocol.getLeadOrganizations(), null);
        changedProtocol.getLeadOrganizations().clear();
        ProtocolModificationDetector detector = new ProtocolModificationDetector(originalProtocol, changedProtocol,
                resources);
        ProtocolRevision revision = new ProtocolRevision();
        detector.addModifications(revision);
        assertEquals(1, revision.getModifications().size());
        String organizationName = leadOrganization.getOrganization().getName();
        String principalInvestigatorName = leadOrganization.getPrincipalInvestigator().getDisplayName();
        assertEquals(
                getPropertyText(LEAD_ORGANIZATION_REMOVED_INVESTIGATOR_KEY, organizationName, principalInvestigatorName),
                revision.getInvestigatorModificationDescriptions().get(0));
        assertEquals(
                getPropertyText(LEAD_ORGANIZATION_REMOVED_SPONSOR_KEY, organizationName, principalInvestigatorName),
                revision.getSponsorModificationDescriptions().get(0));
    }

    @Test
    public void testAddModifications_AddLeadOrganization() {
        Protocol originalProtocol = ProtocolFactory.getInstanceWithId().create();
        Protocol changedProtocol = originalProtocol.createCopy();
        ProtocolLeadOrganization leadOrganization = new ProtocolLeadOrganization(originalProtocol, OrganizationFactory
                .getInstance().create(), PersonFactory.getInstance().create());
        changedProtocol.getLeadOrganizations().add(leadOrganization);
        String expectedInvestigatorMessage = getAddedLeadOrganizationMessage(leadOrganization);
        String expectedSponsorMessage = getAddedLeadOrganizationMessage(leadOrganization);
        ProtocolModificationDetector detector = new ProtocolModificationDetector(originalProtocol, changedProtocol,
                resources);
        ProtocolRevision revision = new ProtocolRevision();
        detector.addModifications(revision);
        assertEquals(1, revision.getModifications().size());
        assertEquals(expectedInvestigatorMessage, revision.getInvestigatorModificationDescriptions().get(0));
        assertEquals(expectedSponsorMessage, revision.getSponsorModificationDescriptions().get(0));
    }

    // Same message for Investigator and Sponsor
    private String getAddedLeadOrganizationMessage(ProtocolLeadOrganization leadOrganization) {
        return getPropertyText("protocol.change.lead.organization.added.investigator.message", leadOrganization
                .getOrganization().getName(), leadOrganization.getPrincipalInvestigator().getDisplayName());
    }

    @Test
    public void testAddModifications_NoChanges() {
        Protocol originalProtocol = ProtocolFactory.getInstanceWithId().create();
        Protocol changedProtocol = originalProtocol.createCopy();
        ProtocolModificationDetector detector = new ProtocolModificationDetector(originalProtocol, changedProtocol,
                resources);
        ProtocolRevision revision = new ProtocolRevision();
        detector.addModifications(revision);
        assertEquals(0, revision.getModifications().size());
    }
}
