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
package gov.nih.nci.firebird.web.action.sponsor.protocol;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.protocol.registration.review.ProtocolRegistrationReviewService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class ReviewRegistrationAjaxActionTest extends AbstractWebTest {
    private SimpleDateFormat dateFormat;
    @Inject
    private ProtocolRegistrationReviewService mockRegistrationReviewService;
    @Inject
    private ProtocolRegistrationService mockRegistrationService;
    @Inject
    private ReviewRegistrationAjaxAction action;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(getMockRequest());
        dateFormat = new SimpleDateFormat(action.getText("date.format.timestamp"), Locale.getDefault());
    }

    @Test
    public void testAcceptRegistrationNotApprovable() throws ValidationException {
        InvestigatorRegistration registration = spy(RegistrationFactory.getInstance().createInvestigatorRegistration());
        when(registration.isApprovable()).thenReturn(false);
        when(registration.isCompleteable()).thenReturn(true);
        action.setRegistration(registration);
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        FirebirdWebTestUtility.setCurrentUser(action, user);

        assertNull(action.acceptRegistration());
        verify(mockRegistrationReviewService).acceptRegistration(user, registration);
    }

    @Test
    public void testAcceptRegistrationApprovable() throws ValidationException {
        InvestigatorRegistration registration = spy(RegistrationFactory.getInstance().createInvestigatorRegistration());
        when(registration.isApprovable()).thenReturn(true);
        when(registration.isCompleteable()).thenReturn(true);
        action.setRegistration(registration);
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        FirebirdWebTestUtility.setCurrentUser(action, user);

        assertEquals(ActionSupport.SUCCESS, action.acceptRegistration());
        verify(mockRegistrationReviewService).acceptRegistration(user, registration);
    }

    @Test
    public void testAcceptRegistrationApprovable_SponsorDelegate() throws ValidationException {
        InvestigatorRegistration registration = spy(RegistrationFactory.getInstance().createInvestigatorRegistration());
        when(registration.isApprovable()).thenReturn(true);
        when(registration.isCompleteable()).thenReturn(true);
        action.setRegistration(registration);
        FirebirdUser sponsorDelegate = FirebirdUserFactory.getInstance().create();
        Organization registrationSponsor = registration.getProtocol().getSponsor();
        sponsorDelegate.addSponsorDelegateRole(registrationSponsor);
        FirebirdWebTestUtility.setCurrentUser(action, sponsorDelegate);

        assertEquals(null, action.acceptRegistration());
        verify(mockRegistrationReviewService).acceptRegistration(sponsorDelegate, registration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAcceptRegistrationFail() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        action.setRegistration(registration);

        action.acceptRegistration();
    }

    @Test
    public void testSubmittedRegistrationEnter() {
        assertEquals(ActionSupport.SUCCESS, action.submittedRegistrationEnter());
    }

    @Test
    public void testSubmittedRegistrationEnter_NullRegistration() {
        action.setRegistration(null);
        assertEquals(ActionSupport.INPUT, action.submittedRegistrationEnter());
    }

    @Test
    public void testGetRegistrationFormsJson() throws JSONException {
        InvestigatorRegistration reg = RegistrationFactory.getInstanceWithId().createInvestigatorRegistration();
        action.setRegistration(reg);
        String json = action.getRegistrationFormsJson();
        for (AbstractRegistrationForm form : reg.getFormsForSponsorReview()) {
            assertTrue(json.contains(form.getFormType().getFormTypeEnum().getDisplay()));
            assertTrue(json.contains(form.getFormType().getDescription()));
            assertTrue(json.contains(form.getFormStatus().getDisplay()));
            assertTrue(json.contains(Boolean.toString(form.isReviewRequired())));
            assertTrue(json.contains(Boolean.toString(form.isAdditionalDocumentsUploaded())));
            assertTrue(json.contains(JSONUtil.serialize(dateFormat.format(form.getFormStatusDate()))));
        }
    }

    @Test
    public void testPrepare_SubmittedAndReviewed() {
        InvestigatorRegistration registration = setUpRegistration(RegistrationStatus.SUBMITTED, FormStatus.REJECTED);
        action.prepare();
        assertEquals(RegistrationStatus.IN_REVIEW, registration.getStatus());
    }

    private InvestigatorRegistration setUpRegistration(RegistrationStatus regsitrationStatus, FormStatus formStatus) {
        InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        registration.setStatus(regsitrationStatus);
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            form.setFormStatus(formStatus);
        }
        when(mockRegistrationService.getById(registration.getId())).thenReturn(registration);
        action.setRegistration(registration);
        return registration;
    }

    @Test
    public void testPrepare_NotSubmittedAndReviewed() {
        InvestigatorRegistration registration = setUpRegistration(RegistrationStatus.RETURNED, FormStatus.REJECTED);
        action.prepare();
        assertEquals(RegistrationStatus.RETURNED, registration.getStatus());
    }

    @Test
    public void testPrepare_SubmittedAndNotReviewed() {
        InvestigatorRegistration registration = setUpRegistration(RegistrationStatus.SUBMITTED, FormStatus.REVISED);
        action.prepare();
        assertEquals(RegistrationStatus.SUBMITTED, registration.getStatus());
    }
}
