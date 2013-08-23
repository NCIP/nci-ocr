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
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.BoardCertifiedSpecialty;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.Certification;
import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.MedicalLicense;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.Date;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

public class CredentialsTabActionTest extends AbstractWebTest {

    private static final Long TEST_PROFILE_ID = 1L;
    private InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
    private InvestigatorProfileService mockProfileService = mock(InvestigatorProfileService.class);
    private CredentialsTabAction action = new CredentialsTabAction(mockProfileService);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        profile.setId(TEST_PROFILE_ID);
        action.setProfile(profile);
    }

    @Test
    public void testEnterCredentialsTab() {
        assertEquals("enter", action.enterCredentialsTab());
    }

    @Test
    public void testEnterCredentialsTypeTab() {
        action.setCredentialType(DEGREE);
        assertEquals(DEGREE.name(), action.enterCredentialsTypeTab());
        action.setCredentialType(SPECIALTY);
        assertEquals(SPECIALTY.name(), action.enterCredentialsTypeTab());
        action.setCredentialType(MEDICAL_LICENSE);
        assertEquals(MEDICAL_LICENSE.name(), action.enterCredentialsTypeTab());
        action.setCredentialType(CERTIFICATION);
        assertEquals(CERTIFICATION.name(), action.enterCredentialsTypeTab());
    }

    @Test
    public void testGetCredentialJsonStringDegree() throws CredentialAlreadyExistsException, JSONException {
        Degree degree = CredentialFactory.getInstance().createDegree();
        Degree degree2 = CredentialFactory.getInstance().createDegree();
        performGetDegreesJsonTest(degree, degree2);
    }

    private void performGetDegreesJsonTest(Degree degree, Degree degree2) throws CredentialAlreadyExistsException,
            JSONException {
        profile.addCredential(degree);
        profile.addCredential(degree2);

        action.setCredentialType(DEGREE);
        String json = action.getCredentialJsonString();

        assertTrue(json.contains(degree.getDegreeType().getName()));
        assertTrue(json.contains(degree.getIssuer().getName()));
        assertTrue(json.contains(JSONUtil.serialize(degree.getEffectiveDate())));
        assertTrue(json.contains(degree2.getDegreeType().getName()));
        assertTrue(json.contains(degree2.getIssuer().getName()));
        assertTrue(json.contains(JSONUtil.serialize(degree2.getEffectiveDate())));
    }

    @Test
    public void testGetCredentialJsonStringDegree_DegreesWithSameDateAndType() throws CredentialAlreadyExistsException,
            JSONException {
        Degree degree = CredentialFactory.getInstance().createDegree();
        Degree degree2 = new Degree(profile, degree.getEffectiveDate(), OrganizationFactory.getInstance().create(),
                degree.getDegreeType());
        performGetDegreesJsonTest(degree, degree2);
    }

    @Test
    public void testGridTableActionMedicalLicense() throws CredentialAlreadyExistsException, JSONException {
        MedicalLicense license1 = CredentialFactory.getInstance().createLicense();
        MedicalLicense license2 = CredentialFactory.getInstance().createLicense();
        MedicalLicense license3 = CredentialFactory.getInstance().createLicense();
        license3.setLicenseType(license2.getLicenseType());
        license3.setState("IA");

        profile.addCredential(license1);
        profile.addCredential(license2);
        profile.addCredential(license3);

        action.setCredentialType(MEDICAL_LICENSE);
        String json = action.getCredentialJsonString();

        assertTrue(json.contains(license1.getLicenseType().getName()));
        assertTrue(json.contains(license1.getState()));
        assertTrue(json.contains(license1.getCountry()));
        assertTrue(json.contains(JSONUtil.serialize(license1.getEffectiveDate())));
        assertTrue(json.contains(JSONUtil.serialize(license1.getExpirationDate())));
        assertTrue(json.contains(license2.getLicenseType().getName()));
        assertTrue(json.contains(JSONUtil.serialize(license2.getEffectiveDate())));
        assertTrue(json.contains(JSONUtil.serialize(license2.getExpirationDate())));
        assertTrue(json.contains(license3.getLicenseType().getName()));
        assertTrue(json.contains(license3.getState()));
        assertTrue(json.contains(JSONUtil.serialize(license3.getEffectiveDate())));
        assertTrue(json.contains(JSONUtil.serialize(license3.getExpirationDate())));

    }

    @Test
    public void testGridTableActionSpecialty() throws CredentialAlreadyExistsException, JSONException {
        BoardCertifiedSpecialty specialty1 = CredentialFactory.getInstance().createSpecialty();
        BoardCertifiedSpecialty specialty2 = CredentialFactory.getInstance().createSpecialty();
        profile.addCredential(specialty1);
        profile.addCredential(specialty2);

        action.setCredentialType(SPECIALTY);
        String json = action.getCredentialJsonString();

        assertTrue(json.contains(specialty1.getSpecialtyType().getDisplay()));
        assertTrue(json.contains(specialty1.getSpecialtyType().getBoard().getName()));
        assertTrue(json.contains(specialty1.getStatus().getDisplay()));
        assertTrue(json.contains(JSONUtil.serialize(specialty1.getEffectiveDate())));
        assertTrue(json.contains(specialty2.getSpecialtyType().getDisplay()));
        assertTrue(json.contains(specialty2.getSpecialtyType().getBoard().getName()));
        assertTrue(json.contains(specialty2.getStatus().getDisplay()));
        assertTrue(json.contains(JSONUtil.serialize(specialty2.getEffectiveDate())));
    }

    @Test
    public void testGridTableActionCertification() throws CredentialAlreadyExistsException, JSONException {
        Certification certification1 = CredentialFactory.getInstance().createCertification();
        Certification certification2 = CredentialFactory.getInstance().createCertification();
        certification2.setExpirationDate(null);
        profile.addCredential(certification1);
        profile.addCredential(certification2);

        action.setCredentialType(CERTIFICATION);
        String json = action.getCredentialJsonString();

        assertTrue(json.contains(certification1.getCertificationType().getName()));
        assertTrue(json.contains(JSONUtil.serialize(certification1.getEffectiveDate())));
        assertTrue(json.contains(JSONUtil.serialize(certification1.getExpirationDate())));
        assertTrue(json.contains(certification2.getCertificationType().getName()));
        assertTrue(json.contains(JSONUtil.serialize(certification2.getEffectiveDate())));
        assertTrue(json.contains(JSONUtil.serialize(certification2.getExpirationDate())));
    }

    @Test
    public void testGridTableActionCertificate() throws CredentialAlreadyExistsException, JSONException {
        TrainingCertificate certificate = new TrainingCertificate(profile, new Date(), new Date(),
                CertificateType.HUMAN_RESEARCH_CERTIFICATE, null);
        profile.addCredential(certificate);

        action.setCredentialType(CERTIFICATE);
        String json = action.getCredentialJsonString();
        assertTrue(json.contains(action.getText(certificate.getCertificateType().getNameProperty())));
        assertTrue(json.contains(JSONUtil.serialize(certificate.getEffectiveDate())));
        assertTrue(json.contains(JSONUtil.serialize(certificate.getExpirationDate())));

    }

    @Test
    public void testGetCredentialJsonString() {

    }
}
