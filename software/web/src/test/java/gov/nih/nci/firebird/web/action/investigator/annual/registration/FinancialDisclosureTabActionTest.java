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
package gov.nih.nci.firebird.web.action.investigator.annual.registration;

import static org.apache.commons.lang3.StringUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationService;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.web.action.investigator.registration.common.FinancialDisclosureTabProcessor;
import gov.nih.nci.firebird.web.common.Struts2UploadedFileInfo;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.io.IOException;

import org.apache.struts2.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class FinancialDisclosureTabActionTest extends AbstractWebTest {

    private static final Long GOOD_REG_ID = 1L;
    private static final Long BAD_REG_ID = 2L;
    @Inject
    private AnnualRegistrationService mockRegistrationService;
    @Inject
    private FinancialDisclosureTabAction action;
    @Mock
    private FinancialDisclosureTabProcessor<AnnualRegistration> mockProcessor;
    private final AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(mockRegistrationService.getById(GOOD_REG_ID)).thenReturn(registration);
        when(mockRegistrationService.getById(BAD_REG_ID)).thenReturn(null);
        action.setRegistration(registration);
        action.setProcessor(mockProcessor);
    }

    @Test
    public void testPrepare() {
        AnnualRegistration reg = null;
        action.setRegistration(reg);
        action.prepare();
        verifyZeroInteractions(mockRegistrationService);

        reg = AnnualRegistrationFactory.getInstance().create();
        reg.setId(BAD_REG_ID);
        action.setRegistration(reg);
        action.prepare();

        reg.setId(GOOD_REG_ID);
        action.setRegistration(reg);
        action.prepare();
        verify(mockRegistrationService).getById(GOOD_REG_ID);
        assertEquals(registration, action.getRegistration());
    }

    @Test
    public void testViewFinancialDisclosure() {
        action.viewFinancialDisclosure();
        verify(mockProcessor).viewFinancialDisclosure();
    }

    @Test
    public void testSaveFinancialDisclosure() {
        action.saveFinancialDisclosure();
        verify(mockProcessor).saveFinancialDisclosure();
    }

    @Test
    public void testAddDocument() throws IOException {
        Struts2UploadedFileInfo document = new Struts2UploadedFileInfo();
        action.setDocument(document);
        action.addDocument();
        verify(mockProcessor).addDocument(document);
    }

    @Test
    public void testDeleteDocument() {
        action.setFileId(1L);
        action.deleteDocument();
        verify(mockProcessor).deleteDocument(1L);
    }

    @Test
    public void testGetFilesAsJson() throws JSONException {
        action.getFilesAsJson();
        verify(mockProcessor).getFilesAsJson();
    }

    @Test
    public void testGetForm() {
        assertEquals(registration.getFinancialDisclosure(), action.getForm());
    }

    @Test
    public void testGetPharmaceuticalCompaniesJson() throws JSONException {
        Organization pharmaceuticalCompany1 = OrganizationFactory.getInstanceWithId().create();
        pharmaceuticalCompany1.setCtepId("ctepId");
        Organization pharmaceuticalCompany2 = OrganizationFactory.getInstanceWithId().create();
        registration.getFinancialDisclosure().getPharmaceuticalCompanies()
                .addAll(Lists.newArrayList(pharmaceuticalCompany1, pharmaceuticalCompany2));
        String json = action.getPharmaceuticalCompaniesJson();
        checkJsonForPharmaceuticalCompany(json, pharmaceuticalCompany1);
        checkJsonForPharmaceuticalCompany(json, pharmaceuticalCompany2);
    }

    private void checkJsonForPharmaceuticalCompany(String json, Organization pharmaceuticalCompany) {
        assertTrue(json.contains(pharmaceuticalCompany.getId().toString()));
        assertTrue(json.contains(pharmaceuticalCompany.getNesId()));
        assertTrue(json.contains(pharmaceuticalCompany.getName()));
        assertTrue(json.contains(trimToEmpty(pharmaceuticalCompany.getCtepId())));
        assertTrue(json.contains(pharmaceuticalCompany.getEmail()));
        assertTrue(json.contains(pharmaceuticalCompany.getPhoneNumber()));
    }

}
