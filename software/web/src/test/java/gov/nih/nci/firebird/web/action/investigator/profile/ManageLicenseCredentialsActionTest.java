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
import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.MedicalLicense;
import gov.nih.nci.firebird.data.MedicalLicenseType;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.ValidationExceptionFactory;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class ManageLicenseCredentialsActionTest extends AbstractWebTest {

    @Inject
    private InvestigatorProfileService mockProfileService;
    @Inject
    private GenericDataRetrievalService mockDataService;
    @Inject
    private ManageLicenseCredentialsAction action;
    private InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
    private MedicalLicense license = CredentialFactory.getInstance().createLicense();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setProfile(profile);
        action.setServletRequest(getMockRequest());
        action.setPage(MEDICAL_LICENSE.name());
    }

    @Test
    public void testManageCredentialsAjaxEnter() {
        action.setPage(null);
        assertEquals(FirebirdUIConstants.RETURN_FIELDS_PAGE, action.manageCredentialsAjaxEnter());
        action.setPage("CLOSE");
        assertEquals("CLOSE", action.manageCredentialsAjaxEnter());
    }

    @Test
    public void testSaveLicense() throws Exception {
        when(mockDataService.getPersistentObject(eq(MedicalLicenseType.class), anyLong())).thenReturn(
                CredentialFactory.getInstance().createLicense().getLicenseType());
        action.setLicense(license);
        action.setExpirationDate("12/2010");
        action.setLicenseTypeId(1234L);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveLicense());
        verify(mockProfileService).saveCredential(profile, license, FormTypeEnum.CV);
    }

    @Test
    public void testSaveLicense_SaveError() throws Exception {
        when(mockDataService.getPersistentObject(eq(MedicalLicenseType.class), anyLong())).thenReturn(
                CredentialFactory.getInstance().createLicense().getLicenseType());
        doThrow(new CredentialAlreadyExistsException()).when(mockProfileService).saveCredential(profile, license,
                FormTypeEnum.CV);
        action.setLicense(license);
        action.setExpirationDate("12/2010");
        action.setLicenseTypeId(1234L);
        assertEquals(ActionSupport.INPUT, action.saveLicense());
    }

    @Test
    public void testSaveLicense_ValidationError() throws Exception {
        when(mockDataService.getPersistentObject(eq(MedicalLicenseType.class), anyLong())).thenReturn(
                CredentialFactory.getInstance().createLicense().getLicenseType());
        doThrow(ValidationExceptionFactory.getInstance().create()).when(mockProfileService).saveCredential(profile,
                license, FormTypeEnum.CV);
        action.setLicense(license);
        action.setExpirationDate("12/2010");
        action.setLicenseTypeId(1234L);
        assertEquals(ActionSupport.INPUT, action.saveLicense());
    }

    @Test
    public void testDeleteLicense() throws CredentialAlreadyExistsException {
        profile.addCredential(license);
        action.setProfile(profile);
        action.setLicense(license);
        assertEquals(1, profile.getCredentials().size());

        action.deleteLicense();
        assertEquals(0, profile.getCredentials().size());
        verify(mockProfileService).save(profile, FormTypeEnum.CV);
    }

    @Test
    public void testGetRankedsLicenses() {
        List<MedicalLicenseType> licenses = Lists.newArrayList();
        MedicalLicense license1 = CredentialFactory.getInstance().createLicense();
        MedicalLicense license2 = CredentialFactory.getInstance().createLicense();
        licenses.add(license1.getLicenseType());
        licenses.add(license2.getLicenseType());
        when(mockDataService.getAllRanked(MedicalLicenseType.class)).thenReturn(licenses);
        assertEquals(licenses, action.getRankedLicenses());
    }

    @Test
    public void testGetLicensesWithRankedOnTop() {
        List<MedicalLicenseType> licenses = Lists.newArrayList();
        MedicalLicense license = CredentialFactory.getInstance().createLicense();
        MedicalLicense rankedLicense = CredentialFactory.getInstance().createLicense();
        licenses.add(license.getLicenseType());
        licenses.add(rankedLicense.getLicenseType());
        List<MedicalLicenseType> rankedLicenses = Lists.newArrayList();
        rankedLicenses.add(rankedLicense.getLicenseType());

        when(mockDataService.getAllSorted(MedicalLicenseType.class)).thenReturn(licenses);
        when(mockDataService.getAllRanked(MedicalLicenseType.class)).thenReturn(rankedLicenses);

        List<MedicalLicenseType> expectedDegrees = Lists.newArrayList(rankedLicenses);
        expectedDegrees.addAll(licenses);
        assertEquals(expectedDegrees, action.getLicensesWithRankedOnTop());
    }

    @Test
    public void testLicenseGetters() {
        MedicalLicense credential = CredentialFactory.getInstance().createLicense();
        credential.getLicenseType().setId(1L);

        action.setCredential(new Degree());
        assertNull(action.getLicense());

        action.setLicense(new MedicalLicense());

        when(mockDataService.getPersistentObject(MedicalLicenseType.class, null)).thenReturn(null);
        when(mockDataService.getPersistentObject(MedicalLicenseType.class, 1L)).thenReturn(credential.getLicenseType());

        action.setLicenseTypeId(null);
        assertNull(action.getLicenseTypeId());
        action.setLicenseTypeId(1L);
        assertEquals(Long.valueOf(1L), action.getLicenseTypeId());

    }

}
