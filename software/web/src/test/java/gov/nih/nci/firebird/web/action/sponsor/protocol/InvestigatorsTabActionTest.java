/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a organization or an entity, and all other entities that control,
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

package gov.nih.nci.firebird.web.action.sponsor.protocol;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.RevisedInvestigatorRegistration;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.service.person.PersonSearchService;
import gov.nih.nci.firebird.service.protocol.ProtocolService;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.action.sponsor.protocol.InvestigatorsTabAction.RegistrationListing;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class InvestigatorsTabActionTest extends AbstractWebTest {

    @Inject
    private ProtocolService protocolService;
    @Inject
    private PersonSearchService searchService;
    @Inject
    private InvestigatorsTabAction action;
    private Protocol protocol;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(ServletActionContext.getRequest()); // closeDialog() needs a request
        action.setPersonSearchService(searchService);
        protocol = ProtocolFactory.getInstanceWithId().createWithFormsDocuments();
        action.setProtocol(protocol);
        FirebirdUser sponsor = new FirebirdUser();
        sponsor.addSponsorRepresentativeRole(protocol.getSponsor());
        FirebirdWebTestUtility.setCurrentUser(action, sponsor);
    }

    @Test
    public void testPrepare() {
        when(protocolService.getById(protocol.getId())).thenReturn(protocol);
        action.prepare();
        verify(protocolService).getById(protocol.getId());
    }

    @Test
    public void testEnterPage() {
        assertEquals(ActionSupport.SUCCESS, action.enterPage());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetRegistrations() throws JSONException {
        InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        action.getProtocol().addRegistration(registration);
        String json = action.getRegistrationsAsJson();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) JSONUtil.deserialize(json);
        assertEquals(1, rows.size());
        Map<String, Object> registrationRow = rows.get(0);
        assertEquals(registration.getId(), registrationRow.get("id"));
        assertEquals(registration.getProfile().getPerson().getDisplayName(),
                ((Map<String, Object>) registrationRow.get("investigator")).get("displayName").toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat(action.getText("date.format.timestamp"));
        ArrayList<String> replacementArgs = Lists.newArrayList(dateFormat.format(registration.getInvitation()
                .getInvitationChangeDate()));
        String expectedInvitationStatusText = action.getText("registration.invitation.status.NO_RESPONSE.with.date",
                replacementArgs);
        assertEquals(expectedInvitationStatusText, registrationRow.get("invitationStatusText").toString());
        replacementArgs = Lists.newArrayList(registration.getStatus().getDisplay(),
                dateFormat.format(registration.getStatusDate()));
        String expectedRegistrationStatusText = action.getText("registration.status.with.date", replacementArgs);
        assertEquals(expectedRegistrationStatusText, registrationRow.get("statusText").toString());
    }

    @Test
    public void testGetRegistrationsAsJson() throws Exception {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();

        RevisedInvestigatorRegistration revisedRegistration1 = createRevisedRegistrationWithParent(profile, protocol);
        RevisedInvestigatorRegistration revisedRegistration2 = createRevisedRegistrationWithParent(profile, protocol);
        SubInvestigatorRegistration subinvestigatorRegistration = RegistrationFactory.getInstanceWithId()
                .createSubinvestigatorRegistration();
        protocol.addRegistration(subinvestigatorRegistration);

        String json = action.getRegistrationsAsJson();

        checkJsonForRegistration(json, revisedRegistration1.getCurrentRegistration());
        checkJsonForRegistration(json, revisedRegistration1);
        checkJsonForRegistration(json, revisedRegistration2.getCurrentRegistration());
        checkJsonForRegistration(json, revisedRegistration2);

        assertFalse(json.contains(subinvestigatorRegistration.getProfile().getPerson().getDisplayName()));
    }

    private RevisedInvestigatorRegistration createRevisedRegistrationWithParent(InvestigatorProfile profile,
            Protocol protocol) {
        InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId().createInvestigatorRegistration(
                profile, protocol);
        registration.setStatus(RegistrationStatus.APPROVED);

        RevisedInvestigatorRegistration revisedRegistration = registration.createRevisedRegistration();
        revisedRegistration.setId(ValueGenerator.getUniqueLong());
        protocol.addRegistration(revisedRegistration);
        return revisedRegistration;
    }

    private void checkJsonForRegistration(String json, AbstractProtocolRegistration registration) {
        assertTrue(json.contains(registration.getId().toString()));
        assertTrue(json.contains(registration.getProfile().getPerson().getDisplayName()));
        if (registration instanceof RevisedInvestigatorRegistration) {
            Long currentRegistrationId = ((RevisedInvestigatorRegistration) registration).getCurrentRegistration()
                    .getId();
            assertTrue(json.contains(currentRegistrationId.toString()));
        } else {
            assertTrue(json.contains(registration.getStatus().getDisplay()));
            assertTrue(json.contains("Revised on"));
        }
        assertTrue(json.contains(registration.getStatus().getDisplay()));
    }

    @Test
    public void testRegistrationListing_InvestigatorRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                protocol);
        RegistrationListing listing = action.new RegistrationListing(registration);

        assertTrue(listing.isAbleToBeDeactivated());
        assertFalse(listing.isAbleToBeReactivated());
        assertTrue(listing.isRemovable());

        registration.setStatus(RegistrationStatus.INACTIVE);
        assertFalse(listing.isAbleToBeDeactivated());
        assertTrue(listing.isAbleToBeReactivated());
        assertTrue(listing.isRemovable());

        registration.setStatus(RegistrationStatus.APPROVED);
        assertTrue(listing.isAbleToBeDeactivated());
        assertFalse(listing.isAbleToBeReactivated());
        assertFalse(listing.isRemovable());

        registration.getProtocol().addRegistration(registration.createRevisedRegistration());
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        assertTrue(listing.isAbleToBeDeactivated());
        assertFalse(listing.isAbleToBeReactivated());
        assertFalse(listing.isRemovable());
    }

    @Test
    public void testRegistrationListing_RevisedRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                protocol);
        registration.setStatus(RegistrationStatus.APPROVED);
        RevisedInvestigatorRegistration revisedRegistration = registration.createRevisedRegistration();
        RegistrationListing listing = action.new RegistrationListing(revisedRegistration);

        assertFalse(listing.isAbleToBeDeactivated());
        assertFalse(listing.isAbleToBeReactivated());
        assertFalse(listing.isRemovable());
    }

    @Test
    public void testRegistrationListing_SponsorDelegate() {
        FirebirdUser sponsor = new FirebirdUser();
        sponsor.addSponsorDelegateRole(protocol.getSponsor());
        FirebirdWebTestUtility.setCurrentUser(action, sponsor);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                protocol);
        RegistrationListing listing = action.new RegistrationListing(registration);

        assertFalse(listing.isAbleToBeDeactivated());
        assertFalse(listing.isAbleToBeReactivated());
        assertFalse(listing.isRemovable());
    }

    @Test
    public void testRegistrationListing_ReactivatedApprovedRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                RegistrationStatus.APPROVED);
        registration.setStatus(RegistrationStatus.INACTIVE);
        InvestigatorRegistration reactivatedRegistration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                RegistrationStatus.IN_PROGRESS);
        reactivatedRegistration.getProtocol().addRegistration(registration);
        reactivatedRegistration.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        registration.setCurrentRegistration(reactivatedRegistration);
        reactivatedRegistration.getParentRegistrations().add(registration);
        RegistrationListing listing = action.new RegistrationListing(reactivatedRegistration);

        assertTrue(listing.isAbleToBeDeactivated());
        assertFalse(listing.isAbleToBeReactivated());
        assertFalse(listing.isRemovable());
    }

    @Test
    public void testRegistrationListing_getStatusText_NotStarted() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(RegistrationStatus.NOT_STARTED);
        RegistrationListing listing = action.new RegistrationListing(registration);
        assertEquals(RegistrationStatus.NOT_STARTED.getDisplay(), listing.getStatusText());
    }

    @Test
    public void testRegistrationListing_isAbleToBeReactivated_ApprovedWithReactivatedRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(RegistrationStatus.APPROVED);
        registration.setStatus(RegistrationStatus.INACTIVE);
        InvestigatorRegistration reactivatedRegistration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        reactivatedRegistration.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        registration.setCurrentRegistration(reactivatedRegistration);
        RegistrationListing listing = action.new RegistrationListing(registration);
        assertFalse(listing.isAbleToBeReactivated());
    }

    @Test
    public void testRegistrationListing_isRemovable_ApprovedWithReactivatedRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(RegistrationStatus.APPROVED);
        registration.setStatus(RegistrationStatus.INACTIVE);
        InvestigatorRegistration reactivatedRegistration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        reactivatedRegistration.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        reactivatedRegistration.getParentRegistrations().add(registration);
        RegistrationListing listing = action.new RegistrationListing(reactivatedRegistration);
        assertFalse(listing.isRemovable());
    }

    @Test
    public void testRegistrationListing_isRemovable_RevisedRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(RegistrationStatus.APPROVED);
        RevisedInvestigatorRegistration revisedRegistration = registration.createRevisedRegistration();
        revisedRegistration.setStatus(RegistrationStatus.IN_PROGRESS);
        registration.setApprovalDate(null);
        registration.getProtocol().addRegistration(revisedRegistration);
        RegistrationListing listing = action.new RegistrationListing(registration);
        assertFalse(listing.isRemovable());
    }

}