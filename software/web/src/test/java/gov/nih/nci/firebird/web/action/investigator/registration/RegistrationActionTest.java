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
package gov.nih.nci.firebird.web.action.investigator.registration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;

import org.apache.struts2.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.opensymphony.xwork2.ActionSupport;

import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AbstractSupplementalForm;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.RevisedInvestigatorRegistration;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.web.action.investigator.registration.RegistrationAction.RegistrationListing;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

public class RegistrationActionTest extends AbstractWebTest {

    @Mock
    private ProtocolRegistrationService mockRegistrationService;
    @Mock
    private InvestigatorProfileService mockProfileService;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private ServletOutputStream mockServletOutputStream;
    private RegistrationAction action;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(mockResponse.getOutputStream()).thenReturn(mockServletOutputStream);
        action = new RegistrationAction(mockRegistrationService, mockProfileService);
    }

    @Test
    public void testRegistrationListingCompareTo_DifferentProtocols() {
        InvestigatorRegistration registration1 = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        InvestigatorRegistration registration2 = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        RegistrationListing listing1 = action.new RegistrationListing(registration1);
        RegistrationListing listing2 = action.new RegistrationListing(registration2);
        assertTrue(listing1.compareTo(listing2) != 0);
    }

    @Test
    public void testRegistrationListingCompareTo_OnlyOneRevised() {
        InvestigatorRegistration registration1 = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        registration1.setStatus(RegistrationStatus.APPROVED);
        InvestigatorRegistration registration2 = registration1.createRevisedRegistration();
        RegistrationListing listing1 = action.new RegistrationListing(registration1);
        RegistrationListing listing2 = action.new RegistrationListing(registration2);
        assertEquals(-1, listing1.compareTo(listing2));
        assertEquals(1, listing2.compareTo(listing1));
    }

    @Test
    public void testRegistrationListingCompareTo_BothRevised() {
        InvestigatorRegistration registration1 = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        registration1.setStatus(RegistrationStatus.APPROVED);
        InvestigatorRegistration registration2 = registration1.createRevisedRegistration();
        InvestigatorRegistration registration3 = registration1.createRevisedRegistration();
        RegistrationListing listing1 = action.new RegistrationListing(registration2);
        RegistrationListing listing2 = action.new RegistrationListing(registration3);
        assertEquals(listing1.getCurrentRegistrationId().compareTo(listing2.getCurrentRegistrationId()),
                listing1.compareTo(listing2));
    }

    @Test
    public void testGetRegistrations() throws JSONException {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        RevisedInvestigatorRegistration revisedRegistration = createRevisedRegistration(profile);
        action.setProfile(profile);

        profile.getAllProtocolRegistrations();

        List<RegistrationListing> listings = action.getRegistrationListings();
        assertEquals(1, listings.size());
        RegistrationListing listing = listings.get(0);
        assertEquals(1, listing.getRevisedRegistrations().size());
        checkEquivalent(listing, revisedRegistration.getCurrentRegistration());
        RegistrationListing revisedListing = listing.getRevisedRegistrations().get(0);
        checkEquivalent(revisedListing, revisedRegistration);
    }

    private void checkEquivalent(RegistrationListing listing, AbstractProtocolRegistration registration) {
        assertEquals(registration.getProtocol().getProtocolTitle(), listing.getTitle());
        assertEquals(registration.getProtocol().getProtocolNumber(), listing.getProtocolNumber());
        assertEquals(registration.getProtocol().getSponsor().getName(), listing.getSponsor());
        assertEquals(registration.getStatus(), listing.getStatus());
    }

    private RevisedInvestigatorRegistration createRevisedRegistration(InvestigatorProfile profile) {
        InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId().createInvestigatorRegistration(
                profile);
        registration.setStatus(RegistrationStatus.APPROVED);
        RevisedInvestigatorRegistration revisedRegistration = registration.createRevisedRegistration();
        revisedRegistration.setId(ValueGenerator.getUniqueLong());
        return revisedRegistration;
    }

    @Test
    public void testGetRegistrations_NotInvited() throws JSONException {
        AbstractProtocolRegistration registration = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        registration.getInvitation().setInvitationStatus(InvitationStatus.NOT_INVITED);
        action.setProfile(registration.getProfile());

        List<RegistrationListing> listings = action.getRegistrationListings();
        assertTrue(listings.isEmpty());
    }

    @Test
    public void testEnterRegistration_NoId() {
        action.setId(null);
        assertEquals("browse", action.enterRegistrations());
    }

    @Test
    public void testEnterRegistration_NoRegistration() {
        action.setRegistration(new InvestigatorRegistration());
        assertEquals("browse", action.enterRegistrations());
    }

    @Test
    public void testEnterRegistration_NotStartedRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.NOT_STARTED);
        when(mockRegistrationService.getById(registration.getId())).thenReturn(registration);
        action.setId(registration.getId());
        action.prepare();
        assertEquals("view", action.enterRegistrations());
        assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (form instanceof AbstractSupplementalForm) {
                assertEquals(FormStatus.NOT_APPLICABLE, form.getFormStatus());
            } else {
                assertEquals(FormStatus.NOT_STARTED, form.getFormStatus());
            }
        }
        verify(mockRegistrationService).save(registration);
    }

    @Test
    public void testEnterRegistration_InProgressRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        action.setId(registration.getId());
        when(mockRegistrationService.getById(registration.getId())).thenReturn(registration);
        action.prepare();
        assertEquals("view", action.enterRegistrations());
        verify(mockRegistrationService, never()).save(registration);
    }

    @Test
    public void testEnterRegistration_ReactivatedRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        registration.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
        action.setId(registration.getId());
        when(mockRegistrationService.getById(registration.getId())).thenReturn(registration);
        action.prepare();
        assertEquals("view", action.enterRegistrations());
        assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
        assertEquals(InvitationStatus.RESPONDED, registration.getInvitation().getInvitationStatus());
        verify(mockRegistrationService).save(registration);
    }

    @Test
    public void testLastProtocolUpdateAction() {
        assertEquals(ActionSupport.SUCCESS, action.lastProtocolUpdateAction());
    }
}
