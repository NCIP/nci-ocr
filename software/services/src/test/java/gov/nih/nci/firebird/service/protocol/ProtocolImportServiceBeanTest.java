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

import static gov.nih.nci.firebird.service.protocol.ImportTestFile.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolPhase;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.test.OrganizationFactory;

import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fiveamsolutions.nci.commons.util.HibernateHelper;
import com.google.common.collect.Sets;

public class ProtocolImportServiceBeanTest {

    private ProtocolImportServiceBean bean = new ProtocolImportServiceBean();
    private ResourceBundle resources = ResourceBundle.getBundle("resources");
    @Mock private ProtocolImportDetailService mockProtocolImportService;
    @Mock private HibernateHelper mockHibernateHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bean.setProtocolImportService(mockProtocolImportService);
        bean.setHibernateHelper(mockHibernateHelper);
        bean.setResources(resources);
    }

    @Test
    public void testCreateImportJob() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        File importFile = getImportTestFile(TEST_IMPORT_SINGLE_PROTOCOL);
        ProtocolImportJob job = bean.createImportJob(importFile, sponsor);
        assertEquals(sponsor, job.getSponsor());
        assertNotNull(job);
        assertEquals(1, job.getDetails().size());
        ProtocolImportDetail detail = job.getDetails().get(0);
        assertEquals(0, detail.getIndex());
        assertEquals(ProtocolImportDetailStatus.NOT_STARTED, detail.getStatus());
        Protocol protocol = detail.getProtocol();
        assertEquals(sponsor, protocol.getSponsor());
        assertEquals("INC07-10-01", protocol.getProtocolNumber());
        assertEquals("Sample Protocol", protocol.getProtocolTitle());
        assertEquals(ProtocolPhase.PHASE_2_3, protocol.getPhase());
        assertEquals(2, protocol.getAgents().size());
        assertTrue(protocol.getAgentListForDisplay().contains("Aspirin"));
        assertTrue(protocol.getAgentListForDisplay().contains("Nyquil"));
        assertEquals(2, detail.getInvestigatorExternalIds().size());
        assertEquals("22248", detail.getInvestigatorExternalIds().get(0));
        assertEquals("23084", detail.getInvestigatorExternalIds().get(1));
        assertTrue(detail.isMarkedForImport());
        assertEquals(ProtocolImportJobStatus.NOT_STARTED, job.getStatus());
    }

    @Test
    public void testCreateImportJob_MultipleProtocols() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        File importFile = getImportTestFile(ImportTestFile.TEST_IMPORT_DCP_PROTOCOLS);
        ProtocolImportJob job = bean.createImportJob(importFile, sponsor);
        assertEquals(62, job.getDetails().size());
    }

    @Test(expected = ValidationException.class)
    public void testCreateImportJob_EmptyFile() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        File importFile = getImportTestFile(TEST_IMPORT_EMPTY_FILE);
        bean.createImportJob(importFile, sponsor);
    }

    @Test(expected = ValidationException.class)
    public void testCreateImportJob_InvalidNumberOfColumns() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        File importFile = getImportTestFile(TEST_IMPORT_INVALID_NUMBER_OF_COLUMNS);
        bean.createImportJob(importFile, sponsor);
    }

    @Test
    public void testCreateImportJob_MissingValues() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        File importFile = getImportTestFile(TEST_IMPORT_MISSING_VALUES);
        ProtocolImportJob job = bean.createImportJob(importFile, sponsor);
        ProtocolImportDetail detail = job.getDetails().get(0);
        String numberMissingMessage = getMessage("validation.failure.protocol.import.missing.value", "1", "1", "Sponsor Protocol ID");
        String titleMissingMessage = getMessage("validation.failure.protocol.import.missing.value", "1", "2", "Title");
        String phaseMissingMessage = getMessage("validation.failure.protocol.import.missing.value", "1", "3", "Phase");
        checkForExpectedFailures(detail, numberMissingMessage, titleMissingMessage, phaseMissingMessage);
        assertTrue(detail.getInvestigatorExternalIds().isEmpty());
        assertTrue(detail.getProtocol().getAgents().isEmpty());
    }

    @Test
    public void testCreateImportJob_InvalidLengths() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        File importFile = getImportTestFile(TEST_IMPORT_INVALID_LENGTHS);
        ProtocolImportJob job = bean.createImportJob(importFile, sponsor);
        ProtocolImportDetail detail = job.getDetails().get(0);
        String numberTooLongMessage = getMessage("validation.failure.protocol.import.invalid.length", "1", "1",
                "Sponsor Protocol ID", Protocol.MAX_PROTOCOL_NUM_SIZE, 201);
        String titleTooLongMessage = getMessage("validation.failure.protocol.import.invalid.length", "1", "2", "Title",
                Protocol.MAX_TITLE_LENGTH, 4001);
        String agentTooLongMessage = getMessage("validation.failure.protocol.import.invalid.length", "1", "5", "Agent",
                ProtocolAgent.MAX_AGENT_LENGTH, 201);
        checkForExpectedFailures(detail, numberTooLongMessage, titleTooLongMessage, agentTooLongMessage);
    }
    
    @Test
    public void testCreateImportJob_InvalidPhase() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        File importFile = getImportTestFile(TEST_IMPORT_INVALID_PHASE);
        ProtocolImportJob job = bean.createImportJob(importFile, sponsor);
        ProtocolImportDetail detail = job.getDetails().get(0);
        String invalidPhaseMessage = getMessage("validation.failure.protocol.import.invalid.phase", "1", "3", "invalid");
        checkForExpectedFailures(detail, invalidPhaseMessage);
    }

    @Test
    public void testCreateImportJob_DuplicateProtocolNumbersInFile() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        File importFile = getImportTestFile(TEST_IMPORT_MULTIPLE_PROTOCOL_DUPLICATE);
        ProtocolImportJob job = bean.createImportJob(importFile, sponsor);
        String duplicateNumberMessage1 = getMessage("validation.failure.protocol.import.duplicate.number.in.file", "2", "1", "MAY03-1-02");
        checkForExpectedFailures(job.getDetails().get(1), duplicateNumberMessage1);
        String duplicateNumberMessage2 = getMessage("validation.failure.protocol.import.duplicate.number.in.file", "4", "1", "MAY03-1-02");
        checkForExpectedFailures(job.getDetails().get(3), duplicateNumberMessage2);
    }

    private String getMessage(String messageKey, Object... parameterValues) {
        String message = resources.getString(messageKey);
        return MessageFormat.format(message, parameterValues);
    }

    static void checkForExpectedFailures(ProtocolImportDetail detail, String... expectedFailureMessageArray) {
        assertFalse(detail.isMarkedForImport());
        Set<String> expectedFailureMessages = Sets.newHashSet(expectedFailureMessageArray);
        Set<String> failureMessages = Sets.newHashSet();
        for (ValidationFailure failure : detail.getValidationResult().getFailures()) {
            failureMessages.add(failure.getMessage());
        }
        assertEquals("Actual messages: " + failureMessages, expectedFailureMessages.size(), failureMessages.size());
        assertTrue("Expected messages: " + expectedFailureMessages + "\nActual messages: " + failureMessages,
                failureMessages.containsAll(expectedFailureMessages));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateImportJob_FileNotFound() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        bean.createImportJob(new File("file_does_not_exist"), sponsor);
    }

    private File getImportTestFile(ImportTestFile testFileEnum) {
        ImportTestFileHelper fileHelper = new ImportTestFileHelper("demo");
        return new File(fileHelper.getResourcePath(testFileEnum));
    }

    @Test
    public void testValidate_ProtocolImportJob() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        File importFile = getImportTestFile(ImportTestFile.TEST_IMPORT_SINGLE_PROTOCOL);
        ProtocolImportJob job = bean.createImportJob(importFile, sponsor);
        bean.validate(job);
        verify(mockProtocolImportService).validate(job.getDetails().get(0));
        assertEquals(ProtocolImportJobStatus.VALIDATION_COMPLETE, job.getStatus());
        assertEquals(ProtocolImportDetailStatus.VALIDATING, job.getDetails().get(0).getStatus());
        verify(mockHibernateHelper).openAndBindSession();
        verify(mockHibernateHelper).unbindAndCleanupSession();
    }

    @Test
    public void testImportProtocols() throws ValidationException {
        Organization sponsor =  OrganizationFactory.getInstance().create();
        File importFile = getImportTestFile(ImportTestFile.TEST_IMPORT_SINGLE_PROTOCOL);
        ProtocolImportJob job = bean.createImportJob(importFile, sponsor);
        job.getDetails().get(0).setStatus(ProtocolImportDetailStatus.VALID);
        bean.importProtocols(job);
        verify(mockProtocolImportService).importProtocol(job.getDetails().get(0));
        assertEquals(ProtocolImportJobStatus.IMPORT_COMPLETE, job.getStatus());
        assertEquals(ProtocolImportDetailStatus.IMPORTING, job.getDetails().get(0).getStatus());
        verify(mockHibernateHelper).openAndBindSession();
        verify(mockHibernateHelper).unbindAndCleanupSession();
    }

}
