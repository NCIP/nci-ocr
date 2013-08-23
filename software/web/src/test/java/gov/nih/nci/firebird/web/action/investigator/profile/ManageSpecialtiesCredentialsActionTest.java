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
package gov.nih.nci.firebird.web.action.investigator.profile;

import static gov.nih.nci.firebird.data.CredentialType.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.BoardCertifiedSpecialty;
import gov.nih.nci.firebird.data.CertifiedSpecialtyBoard;
import gov.nih.nci.firebird.data.CertifiedSpecialtyType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.ValidationExceptionFactory;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class ManageSpecialtiesCredentialsActionTest extends AbstractWebTest {

    @Inject
    private InvestigatorProfileService mockProfileService;
    @Inject
    private GenericDataRetrievalService mockDataService;
    @Inject
    private ManageSpecialtiesCredentialsAction action;
    private InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
    private BoardCertifiedSpecialty specialty = CredentialFactory.getInstance().createSpecialty();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setProfile(profile);
        action.setServletRequest(getMockRequest());
        action.setPage(SPECIALTY.name());
    }

    @Test
    public void testManageCredentialsAjaxEnter() {
        action.setPage(null);
        assertEquals(FirebirdUIConstants.RETURN_FIELDS_PAGE, action.manageCredentialsAjaxEnter());
        action.setPage("CLOSE");
        assertEquals("CLOSE", action.manageCredentialsAjaxEnter());

        action.setSpecialty(specialty);
        action.setPage(FirebirdUIConstants.RETURN_FIELDS_PAGE);
        assertEquals(FirebirdUIConstants.RETURN_FIELDS_PAGE, action.manageCredentialsAjaxEnter());
        assertNotNull(action.getSpecialtyBoardId());
    }

    @Test
    public void testSaveSpecialty() throws Exception {
        when(mockDataService.getPersistentObject(eq(CertifiedSpecialtyType.class), anyLong())).thenReturn(
                CredentialFactory.getInstance().createSpecialty().getSpecialtyType());
        action.setSpecialty(specialty);
        action.setEffectiveDate("12/2010");
        action.setSpecialtyTypeId(1234L);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveSpecialty());
        verify(mockProfileService).saveCredential(profile, specialty, FormTypeEnum.CV);
    }

    @Test
    public void testSaveSpecialty_SaveError() throws Exception {
        when(mockDataService.getPersistentObject(eq(CertifiedSpecialtyType.class), anyLong())).thenReturn(
                CredentialFactory.getInstance().createSpecialty().getSpecialtyType());
        doThrow(new CredentialAlreadyExistsException()).when(mockProfileService).saveCredential(profile, specialty,
                FormTypeEnum.CV);
        action.setSpecialty(specialty);
        action.setEffectiveDate("12/2010");
        action.setSpecialtyTypeId(1234L);
        assertEquals(ActionSupport.INPUT, action.saveSpecialty());
    }

    @Test
    public void testSaveCertification_InvalidDates() throws Exception {
        action.setSpecialty(specialty);
        action.setEffectiveDate("12/2010");
        action.setExpirationDate("11/2010");
        assertEquals(ActionSupport.INPUT, action.saveSpecialty());
        verifyZeroInteractions(mockProfileService);
        assertEquals(action.getText("error.expiration.date.before.effective"),
                action.getFieldErrors().get("expirationDate").get(0));
    }

    @Test
    public void testSave_SpecialtySaveValidationError() throws Exception {
        when(mockDataService.getPersistentObject(eq(CertifiedSpecialtyType.class), anyLong())).thenReturn(
                CredentialFactory.getInstance().createSpecialty().getSpecialtyType());
        doThrow(ValidationExceptionFactory.getInstance().create()).when(mockProfileService).saveCredential(profile,
                specialty, FormTypeEnum.CV);
        action.setSpecialty(specialty);
        action.setEffectiveDate("12/2010");
        action.setSpecialtyTypeId(1234L);
        assertEquals(ActionSupport.INPUT, action.saveSpecialty());
    }

    @Test
    public void testDeleteSpecialty() throws CredentialAlreadyExistsException {
        profile.addCredential(specialty);
        action.setProfile(profile);
        action.setSpecialty(specialty);
        assertEquals(1, profile.getCredentials().size());

        action.deleteSpecialty();
        assertEquals(0, profile.getCredentials().size());
        verify(mockProfileService).save(profile, FormTypeEnum.CV);
    }

    @Test
    public void testGetCredentialTypes() {
        assertNotNull(action.getSpecialtyBoards());
    }

    @Test
    public void testGetBoards() {
        BoardCertifiedSpecialty specialty = CredentialFactory.getInstance().createSpecialty();
        List<CertifiedSpecialtyType> types = new ArrayList<CertifiedSpecialtyType>();
        types.add(specialty.getSpecialtyType());

        when(mockDataService.getPersistentObject(CertifiedSpecialtyBoard.class, null)).thenReturn(null);
        when(mockDataService.getPersistentObject(CertifiedSpecialtyBoard.class, 1L)).thenReturn(
                specialty.getSpecialtyType().getBoard());
        when(mockProfileService.getSpecialtiesByBoard(specialty.getSpecialtyType().getBoard())).thenReturn(types);

        action.setSpecialtyBoardId(null);
        assertEquals(ActionSupport.SUCCESS, action.getSpecialties());
        assertTrue(action.getJsonResult().get("specialties").isEmpty());

        action.setSpecialtyBoardId(1L);
        assertEquals(ActionSupport.SUCCESS, action.getSpecialties());
        assertNotNull(action.getJsonResult());
        assertNotNull(action.getJsonResult().get("specialties"));
    }

    @Test
    public void testSpecialtyGetters() {
        BoardCertifiedSpecialty specialty = CredentialFactory.getInstance().createSpecialty();
        specialty.getSpecialtyType().setId(1L);
        specialty.getSpecialtyType().getBoard().setId(1L);
        action.setSpecialty(new BoardCertifiedSpecialty());

        when(mockDataService.getPersistentObject(CertifiedSpecialtyBoard.class, null)).thenReturn(null);
        when(mockDataService.getPersistentObject(CertifiedSpecialtyBoard.class, 1L)).thenReturn(
                specialty.getSpecialtyType().getBoard());
        when(mockDataService.getPersistentObject(CertifiedSpecialtyType.class, null)).thenReturn(null);
        when(mockDataService.getPersistentObject(CertifiedSpecialtyType.class, 1L)).thenReturn(
                specialty.getSpecialtyType());

        action.setSpecialtyBoardId(null);
        assertNull(action.getSpecialtyBoardId());
        action.setSpecialtyBoardId(1L);
        assertEquals(Long.valueOf(1L), action.getSpecialtyBoardId());

        action.setSpecialtyTypeId(null);
        assertNull(action.getSpecialtyTypeId());
        action.setSpecialtyTypeId(1L);
        assertEquals(Long.valueOf(1L), action.getSpecialtyTypeId());
    }

}
