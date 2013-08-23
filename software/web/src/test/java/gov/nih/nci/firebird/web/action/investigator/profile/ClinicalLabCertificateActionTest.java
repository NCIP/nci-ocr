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

import static gov.nih.nci.firebird.data.LaboratoryCertificateType.*;
import static gov.nih.nci.firebird.data.OrganizationRoleType.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.common.FirebirdDateUtils;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.LaboratoryCertificateType;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.common.Struts2UploadedFileInfo;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.EnumSet;

import org.apache.struts2.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class ClinicalLabCertificateActionTest extends AbstractWebTest {

    private static final Long DEFAULT_ID = 12345L;
    private static final Long INVALID_ID = 54321L;

    ClinicalLabCertificateAction action;
    OrganizationAssociation orgAssociation;
    ClinicalLaboratory lab;
    LaboratoryCertificate certificate = new LaboratoryCertificate(CLIA);
    Date effectiveDate = new Date(System.currentTimeMillis() - 100000);
    Date expirationDate = new Date(System.currentTimeMillis() + 100000);

    @Inject
    OrganizationAssociationService mockAssociationService;
    @Inject
    InvestigatorProfileService mockProfileService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        InvestigatorProfile profile = new InvestigatorProfile();
        orgAssociation = profile.addOrganizationAssociation(OrganizationFactory.getInstance().create(),
                CLINICAL_LABORATORY);
        lab = (ClinicalLaboratory) orgAssociation.getOrganizationRole();
        action = new ClinicalLabCertificateAction(mockAssociationService, mockProfileService);
        action.setServletRequest(getMockRequest());

        when(mockAssociationService.getById(DEFAULT_ID)).thenReturn(orgAssociation);
        when(mockAssociationService.getById(INVALID_ID)).thenReturn(null);
    }

    /**
     * Test method for
     * {@link gov.nih.nci.firebird.web.action.investigator.profile.ClinicalLabCertificateAction#clinicalLabCertificateEnter()}
     * .
     */
    @Test
    public void testClinicalLabCertificateEnter() {
        lab.addCertificate(certificate);
        action.setAssociationId(DEFAULT_ID);
        action.prepare();
        action.getCertificate().setType(null);
        assertEquals(ClinicalLabCertificateAction.RETURN_VIEW_CERTIFICATES, action.clinicalLabCertificateEnter());
        assertNull(action.getCertificate());
        assertEquals(orgAssociation, action.getLaboratory());

        action.setCertificate(new LaboratoryCertificate(certificate.getType()));
        action.clinicalLabCertificateEnter();
        assertEquals(certificate, action.getCertificate());

        action.setCertificate(null);
        action.clinicalLabCertificateEnter();
        assertNull(action.getCertificate());

        action.setAssociationId(null);
        action.prepare();
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.clinicalLabCertificateEnter());
        assertNull(action.getLaboratory());
        assertNull(getMockRequest().getAttribute(FirebirdUIConstants.NAME_DIALOG_ID));

        action.setAssociationId(INVALID_ID);
        action.prepare();
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.clinicalLabCertificateEnter());
        assertNull(action.getLaboratory());
        assertNull(getMockRequest().getAttribute(FirebirdUIConstants.NAME_DIALOG_ID));
    }

    /**
     * Test method for
     * {@link gov.nih.nci.firebird.web.action.investigator.profile.ClinicalLabCertificateAction#getCertificateTypes()}.
     */
    @Test
    public void testGetCertificateTypes() {
        action.setAssociationId(DEFAULT_ID);
        action.prepare();

        assertTrue(lab.getCertificates().isEmpty());
        assertTrue(action.getCertificateTypes().containsAll(EnumSet.allOf(LaboratoryCertificateType.class)));

        lab.addCertificate(new LaboratoryCertificate(CLIA));
        assertEquals(1, action.getCertificateTypes().size());
        assertTrue(action.getCertificateTypes().contains(CAP));

        lab.removeCertificate(CLIA);
        assertEquals(3, action.getCertificateTypes().size());

        lab.addCertificate(new LaboratoryCertificate(CLP));
        assertEquals(1, action.getCertificateTypes().size());
        assertTrue(action.getCertificateTypes().contains(CAP));

        lab.removeCertificate(CLP);
        assertEquals(3, action.getCertificateTypes().size());

        lab.addCertificate(new LaboratoryCertificate(CAP));
        assertEquals(2, action.getCertificateTypes().size());
        assertTrue(action.getCertificateTypes().contains(CLP));
        assertTrue(action.getCertificateTypes().contains(CLIA));

        lab.addCertificate(new LaboratoryCertificate(CLP));
        assertEquals(0, action.getCertificateTypes().size());
    }

    @Test
    public void testSaveCertificateAction() throws Exception {
        LaboratoryCertificate cert = new LaboratoryCertificate(CAP);
        action.setServletContext(new MockServletContext());
        action.setAssociationId(DEFAULT_ID);
        action.prepare();
        action.setEffectiveDate("11/2011");
        action.setExpirationDate("12/2011");
        action.setCertificate(cert);
        action.setCertificateFile(new Struts2UploadedFileInfo());
        assertEquals(ActionSupport.INPUT, action.saveCertificateAction());
        verify(mockAssociationService).handleCertificate(eq(orgAssociation), eq(cert), any(File.class),
                any(FileMetadata.class));
    }

    @Test
    public void testSaveCertificateAction_IOException() throws Exception {
        LaboratoryCertificate cert = new LaboratoryCertificate(CLIA);
        doThrow(new IOException()).when(mockAssociationService).handleCertificate(eq(orgAssociation), eq(cert),
                any(File.class), any(FileMetadata.class));
        action.setAssociationId(DEFAULT_ID);
        action.prepare();
        action.setEffectiveDate("11/2011");
        action.setExpirationDate("12/2011");
        action.setCertificate(cert);
        action.setCertificateFile(new Struts2UploadedFileInfo());
        assertEquals(ActionSupport.INPUT, action.saveCertificateAction());
        assertEquals(1, action.getActionErrors().size());
    }

    @Test
    public void testSaveCertificateAction_ExpirationBeforeEffective() throws Exception {
        checkForEffectiveMustBeAfterExpirationDateError("11/2011", "10/2011");
    }

    private void checkForEffectiveMustBeAfterExpirationDateError(String effectiveDate, String expirationDate)
            throws ParseException, IOException {
        LaboratoryCertificate cert1 = new LaboratoryCertificate(CAP);
        action.setAssociationId(DEFAULT_ID);
        action.setCertificate(cert1);
        action.prepare();
        action.setEffectiveDate(effectiveDate);
        action.setExpirationDate(expirationDate);
        assertEquals(ActionSupport.INPUT, action.saveCertificateAction());
        assertEquals(action.getText("error.expiration.date.before.effective"),
                action.getFieldErrors().get("expirationDate").get(0));
        verify(mockAssociationService, never()).handleCertificate(any(OrganizationAssociation.class),
                any(LaboratoryCertificate.class), any(File.class), any(FileMetadata.class));
    }

    @Test
    public void testSaveCertificateAction_ExpirationEqualToEffective() throws Exception {
        checkForEffectiveMustBeAfterExpirationDateError("11/2011", "11/2011");
    }

    /**
     * Test method for
     * {@link gov.nih.nci.firebird.web.action.investigator.profile.ClinicalLabCertificateAction#saveCertificateAjax()}.
     */
    @Test
    public void testDeleteCertificateAjax() {
        action.setAssociationId(DEFAULT_ID);
        action.prepare();
        action.setCertificate(new LaboratoryCertificate(CLIA));
        lab.addCertificate(action.getCertificate());

        assertEquals(1, lab.getCertificates().size());
        action.deleteCertificateAction();
        assertEquals(0, lab.getCertificates().size());
        verify(mockAssociationService).save(orgAssociation);
    }

    @Test
    public void testGettersSetters() {
        certificate.setEffectiveDate(effectiveDate);
        certificate.setExpirationDate(expirationDate);
        action.setCertificate(certificate);

        action.setEffectiveDate(FirebirdDateUtils.getAsMonthAndYearOrEmptyString(effectiveDate));
        action.setExpirationDate(FirebirdDateUtils.getAsMonthAndYearOrEmptyString(expirationDate));
        assertEquals(FirebirdDateUtils.getAsMonthAndYearOrEmptyString(effectiveDate), action.getEffectiveDate());
        assertEquals(FirebirdDateUtils.getAsMonthAndYearOrEmptyString(expirationDate), action.getExpirationDate());

        action.setCertificateFile(null);
        assertNull(action.getCertificateFile());
    }

    @Test
    public void testGetCertificatesJson() throws JSONException {
        LaboratoryCertificate certificate2 = new LaboratoryCertificate(CAP);
        certificate.setEffectiveDate(effectiveDate);
        certificate.setExpirationDate(expirationDate);
        certificate.setCertificateFile(FirebirdFileFactory.getInstance().create());
        certificate2.setEffectiveDate(effectiveDate);
        certificate2.setExpirationDate(expirationDate);
        certificate2.setCertificateFile(FirebirdFileFactory.getInstance().create());

        lab.addCertificate(certificate);
        lab.addCertificate(certificate2);
        action.setAssociationId(DEFAULT_ID);
        action.prepare();
        String actualJson = action.getCertificatesJson();
        assertTrue(actualJson.contains(certificate.getType().name()));
        assertTrue(actualJson.contains(certificate2.getType().name()));
    }

}
