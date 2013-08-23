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

import static gov.nih.nci.firebird.data.OrganizationRoleType.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.data.ProtocolRegistrationConfiguration;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.person.external.InvalidatedPersonException;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Provider;

public class ProtocolImportDetailServiceBeanTest {

    private static final String INVALID_INVESTIGATOR_ID_VALIDATION_FAILURE_MESSAGE_KEY = "validation.failure.protocol.import.invalid.investigator.id";
    private static final String INVALID_INVESTIGATOR_ID_FORMAT_VALIDATION_FAILURE_MESSAGE_KEY = "validation.failure.protocol.import.invalid.investigator.id.format";
    private static final String INVALID_LEAD_ORGANIZATION_ID_VALIDATION_FAILURE_MESSAGE_KEY = "validation.failure.protocol.import.invalid.lead.organization.ctep.id";
    private static final String INVALID_LEAD_ORGANIZATION_KEY_VALIDATION_FAILURE_MESSAGE_KEY = "validation.failure.protocol.import.invalid.lead.organization.key";
    private static final String INVALID_LEAD_ORGANIZATION_OLD_FORMAT_VALIDATION_FAILURE_MESSAGE_KEY = "validation.failure.protocol.import.invalid.lead.organization.mapping";
    private static final String TOO_MANY_LEAD_ORGANIZATIONS_RETURNED_VALIDATION_FAILURE_MESSAGE_KEY = "validation.failure.protocol.import.too.many.lead.organizations.returned";

    private ProtocolImportDetailServiceBean bean = new ProtocolImportDetailServiceBean();
    @Mock
    private PersonService mockPersonService;
    @Mock
    private OrganizationService mockOrganizationService;
    @Mock
    private ProtocolService mockProtocolService;
    @Mock
    private Provider<Session> mockSessionProvider;
    @Mock
    private Session mockSession;
    private ResourceBundle resources = ResourceBundle.getBundle("resources");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bean.setPersonService(mockPersonService);
        bean.setProtocolService(mockProtocolService);
        bean.setOrganizationService(mockOrganizationService);
        bean.setResources(resources);
        bean.setSessionProvider(mockSessionProvider);
        when(mockSessionProvider.get()).thenReturn(mockSession);
    }

    @Test
    public void testValidate_Valid() throws Exception {
        Person person1 = PersonFactory.getInstance().create();
        Person person2 = PersonFactory.getInstance().create();
        Organization organization = OrganizationFactory.getInstance().create();
        when(mockPersonService.getByExternalId("22248")).thenReturn(person1);
        when(mockPersonService.getByExternalId("23084")).thenReturn(person2);
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(
                Lists.newArrayList(organization));
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        bean.validate(detail);
        verify(mockPersonService, times(1)).getByExternalId("22248");
        verify(mockPersonService).getByExternalId("23084");
        assertEquals(ProtocolImportDetailStatus.VALID, detail.getStatus());
        assertTrue(detail.isMarkedForImport());
        assertEquals(person1, detail.getInvestigators().get(0));
        assertEquals(person2, detail.getInvestigators().get(1));

        ProtocolLeadOrganization leadOrganization = Iterables.getFirst(detail.getProtocol().getLeadOrganizations(),
                null);
        assertEquals(organization, leadOrganization.getOrganization());
        assertEquals(person1, leadOrganization.getPrincipalInvestigator());
    }

    private ProtocolImportDetail getValidProtocolImportDetail() throws Exception {
        return getValidProtocolImportDetail(ImportTestFile.TEST_IMPORT_SINGLE_PROTOCOL);
    }

    private ProtocolImportDetail getValidProtocolImportDetail(ImportTestFile testFile) throws Exception {
        Organization sponsor = OrganizationFactory.getInstanceWithId().create();
        File importFile = getImportTestFile(testFile);
        ProtocolImportJob job = new ProtocolImportJob(sponsor);
        job.loadFromFile(importFile, resources);
        ProtocolImportDetail detail = job.getDetails().get(0);
        return detail;
    }

    private File getImportTestFile(ImportTestFile testFileEnum) {
        ImportTestFileHelper fileHelper = new ImportTestFileHelper("demo");
        return new File(fileHelper.getResourcePath(testFileEnum));
    }

    @Test
    public void testValidate_ValidMultipleLeadOrganizations() throws Exception {
        Person person1 = PersonFactory.getInstance().create();
        Person person2 = PersonFactory.getInstance().create();
        Organization leadOrg1 = OrganizationFactory.getInstanceWithId().create();
        Organization leadOrg2 = OrganizationFactory.getInstanceWithId().create();
        when(mockPersonService.getByExternalId(anyString())).thenReturn(person1).thenReturn(person2);
        when(mockPersonService.getByExternalId("269650")).thenReturn(person1);
        when(mockPersonService.getByExternalId("269720")).thenReturn(person2);
        when(mockOrganizationService.getByAlternateIdentifier("VA166", GENERIC_ORGANIZATION)).thenReturn(
                Lists.newArrayList(leadOrg1));
        when(mockOrganizationService.getByAlternateIdentifier("02019", GENERIC_ORGANIZATION)).thenReturn(
                Lists.newArrayList(leadOrg2));
        ProtocolImportDetail detail = getValidProtocolImportDetail(ImportTestFile.TEST_IMPORT_SINGLE_PROTOCOL_MULTIPLE_LEAD_ORGANIZATIONS);
        bean.validate(detail);
        verify(mockPersonService).getByExternalId("22248");
        verify(mockPersonService).getByExternalId("23084");
        verify(mockPersonService).getByExternalId("269650");
        verify(mockPersonService).getByExternalId("269720");
        verify(mockOrganizationService).getByAlternateIdentifier("VA166", GENERIC_ORGANIZATION);
        verify(mockOrganizationService).getByAlternateIdentifier("02019", GENERIC_ORGANIZATION);
        assertEquals(ProtocolImportDetailStatus.VALID, detail.getStatus());
        assertTrue(detail.isMarkedForImport());
        for (ProtocolLeadOrganization leadOrganization : detail.getProtocol().getLeadOrganizations()) {
            if (leadOrganization.getOrganization().equals(leadOrg1)) {
                assertEquals(leadOrg1, leadOrganization.getOrganization());
                assertEquals(person1, leadOrganization.getPrincipalInvestigator());
            } else {
                assertEquals(leadOrg2, leadOrganization.getOrganization());
                assertEquals(person2, leadOrganization.getPrincipalInvestigator());
            }
        }
    }

    @Test
    public void testValidate_ValidNoLeadOrganizations() throws Exception {
        Person person1 = PersonFactory.getInstance().create();
        Person person2 = PersonFactory.getInstance().create();
        when(mockPersonService.getByExternalId(anyString())).thenReturn(person1).thenReturn(person2);
        ProtocolImportDetail detail = getValidProtocolImportDetail(ImportTestFile.TEST_IMPORT_SINGLE_PROTOCOL_NO_LEAD_ORGANIZATIONS);
        bean.validate(detail);
        verify(mockPersonService).getByExternalId("22248");
        verify(mockPersonService).getByExternalId("23084");
        verify(mockOrganizationService, never()).search(anyString(), eq(GENERIC_ORGANIZATION));
        assertEquals(ProtocolImportDetailStatus.VALID, detail.getStatus());
        assertTrue(detail.isMarkedForImport());
        assertTrue(detail.getProtocol().getLeadOrganizations().isEmpty());
    }

    @Test
    public void testValidate_DuplicateProtocolNumber() throws Exception {
        when(mockPersonService.getByExternalId(anyString())).thenReturn(new Person());
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(
                Lists.newArrayList(new Organization()));
        String duplicateProtocolNumber = "duplicate";
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        detail.getProtocol().setProtocolNumber(duplicateProtocolNumber);
        when(mockProtocolService.hasDuplicateProtocolNumber(detail.getProtocol())).thenReturn(true);
        bean.validate(detail);
        assertEquals(ProtocolImportDetailStatus.INVALID, detail.getStatus());
        String failureMessage = getMessage("validation.failure.protocol.import.duplicate.number", "1", "1",
                duplicateProtocolNumber);
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    private String getMessage(String messageKey, Object... parameterValues) {
        String message = resources.getString(messageKey);
        return MessageFormat.format(message, parameterValues);
    }

    @Test
    public void testValidate_AlreadyInvalid() throws Exception {
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        detail.addFailure(resources, "validation.failure.protocol.import.missing.value", 1);
        bean.validate(detail);
        assertEquals(ProtocolImportDetailStatus.INVALID, detail.getStatus());
        assertFalse(detail.isMarkedForImport());
    }

    @Test
    public void testValidate_InvestigatorIdNotFound() throws Exception {
        String invalidExternalId = "23084";
        when(mockPersonService.getByExternalId(anyString())).thenReturn(new Person());
        when(mockPersonService.getByExternalId(invalidExternalId)).thenReturn(null);
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(
                Lists.newArrayList(new Organization()));
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        bean.validate(detail);
        assertEquals(ProtocolImportDetailStatus.INVALID, detail.getStatus());
        String failureMessage = getMessage(INVALID_INVESTIGATOR_ID_VALIDATION_FAILURE_MESSAGE_KEY, "1", "6",
                invalidExternalId);
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    @Test
    public void testValidate_InvestigatorIdUnavailableEntityException() throws Exception {
        String invalidExternalId = "23084";
        when(mockPersonService.getByExternalId(anyString())).thenReturn(new Person());
        when(mockPersonService.getByExternalId(invalidExternalId)).thenThrow(new InvalidatedPersonException());
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(
                Lists.newArrayList(new Organization()));
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        bean.validate(detail);
        assertEquals(ProtocolImportDetailStatus.INVALID, detail.getStatus());
        String failureMessage = getMessage(INVALID_INVESTIGATOR_ID_VALIDATION_FAILURE_MESSAGE_KEY, "1", "6",
                invalidExternalId);
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    @Test
    public void testValidate_InvalidLeadOrganizationCtepId() throws Exception {
        String invalidCtepId = "VA166";
        when(mockPersonService.getByExternalId(anyString())).thenReturn(new Person());
        List<Organization> emptyList = Collections.emptyList();
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(
                emptyList);
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        bean.validate(detail);
        assertEquals(ProtocolImportDetailStatus.INVALID, detail.getStatus());
        String failureMessage = getMessage(INVALID_LEAD_ORGANIZATION_ID_VALIDATION_FAILURE_MESSAGE_KEY, "1", "4",
                invalidCtepId);
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    @Test
    public void testValidate_NullCtepId() throws Exception {
        Person person1 = PersonFactory.getInstance().create();
        Person person2 = PersonFactory.getInstance().create();
        when(mockPersonService.getByExternalId(anyString())).thenReturn(person1).thenReturn(person2);
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        Map.Entry<String, String> mapping = Iterables.getFirst(detail.getLeadOrganizationMappings().entrySet(), null);
        detail.getLeadOrganizationMappings().put(null, mapping.getValue());
        detail.getLeadOrganizationMappings().remove(mapping.getKey());
        bean.validate(detail);
        String failureMessage = getMessage(INVALID_LEAD_ORGANIZATION_KEY_VALIDATION_FAILURE_MESSAGE_KEY, "1", "4",
                Iterables.getFirst(detail.getLeadOrganizationMappings().entrySet(), null));
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    @Test
    public void testValidate_OldFormatLeadOrganization() throws Exception {
        Person person1 = PersonFactory.getInstance().create();
        Person person2 = PersonFactory.getInstance().create();
        when(mockPersonService.getByExternalId(anyString())).thenReturn(person1).thenReturn(person2);
        ProtocolImportDetail detail = getValidProtocolImportDetail(ImportTestFile.TEST_IMPORT_SINGLE_PROTOCOL_OLD_FORMAT);
        bean.validate(detail);
        String failureMessage = getMessage(INVALID_LEAD_ORGANIZATION_OLD_FORMAT_VALIDATION_FAILURE_MESSAGE_KEY, "1",
                "4", "VA166");
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    @Test
    public void testValidate_InvalidPrincipalInvestigatorExternalId() throws Exception {
        String invalidExternalId = "ABC1234";
        Person person1 = PersonFactory.getInstance().create();
        Person person2 = PersonFactory.getInstance().create();
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(
                Lists.newArrayList(new Organization()));
        when(mockPersonService.getByExternalId(anyString())).thenReturn(person1).thenReturn(person2);
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        Map.Entry<String, String> mapping = Iterables.getFirst(detail.getLeadOrganizationMappings().entrySet(), null);
        detail.getLeadOrganizationMappings().put(mapping.getKey(), invalidExternalId);
        bean.validate(detail);
        String failureMessage = getMessage(INVALID_INVESTIGATOR_ID_FORMAT_VALIDATION_FAILURE_MESSAGE_KEY, "1", "4",
                invalidExternalId);
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    @Test
    public void testValidate_NullPrincipalInvestigatorExternalId() throws Exception {
        Person person1 = PersonFactory.getInstance().create();
        Person person2 = PersonFactory.getInstance().create();
        when(mockPersonService.getByExternalId(anyString())).thenReturn(person1).thenReturn(person2);
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(
                Lists.newArrayList(new Organization()));
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        Map.Entry<String, String> mapping = Iterables.getFirst(detail.getLeadOrganizationMappings().entrySet(), null);
        detail.getLeadOrganizationMappings().put(mapping.getKey(), null);
        bean.validate(detail);
        String failureMessage = getMessage(INVALID_LEAD_ORGANIZATION_KEY_VALIDATION_FAILURE_MESSAGE_KEY, "1", "4",
                Iterables.getFirst(detail.getLeadOrganizationMappings().entrySet(), null));
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    @Test
    public void testValidate_TooManyLeadOrganizationsReturned() throws Exception {
        String ctepId = "VA166";
        List<Organization> leadOrganizations = Lists.newArrayList(new Organization(), new Organization());
        when(mockPersonService.getByExternalId(anyString())).thenReturn(new Person());
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(leadOrganizations);
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        bean.validate(detail);
        assertEquals(ProtocolImportDetailStatus.INVALID, detail.getStatus());
        String failureMessage = getMessage(TOO_MANY_LEAD_ORGANIZATIONS_RETURNED_VALIDATION_FAILURE_MESSAGE_KEY, "1",
                "4", ctepId);
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    @Test
    public void testImportProtocol() throws Exception {
        String[] ignoreFields = new String[] { "revisionHistory", "registrations", "registrationConfiguration" };
        Person investigator = new Person();
        ArgumentCaptor<Protocol> protocolCaptor = ArgumentCaptor.forClass(Protocol.class);
        when(mockPersonService.getByExternalId(anyString())).thenReturn(investigator);
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(
                Lists.newArrayList(new Organization()));
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        bean.importProtocol(detail);
        assertEquals(ProtocolImportDetailStatus.IMPORT_COMPLETE, detail.getStatus());
        verify(mockProtocolService).save(protocolCaptor.capture());
        Protocol protocol = detail.getProtocol();
        assertTrue(EqualsBuilder.reflectionEquals(protocol, protocolCaptor.getValue(), ignoreFields));
        verify(mockProtocolService, times(2)).addInvestigator(protocolCaptor.capture(), eq(investigator));
        assertTrue(EqualsBuilder.reflectionEquals(protocol, protocolCaptor.getValue(), ignoreFields));
        verify(mockProtocolService).addFormTypes(any(ProtocolRegistrationConfiguration.class));
    }

    @Test
    public void testImportProtocol_InvalidInvestigatorId() throws Exception {
        String invalidExternalId = "23084";
        when(mockPersonService.getByExternalId(anyString())).thenReturn(new Person());
        when(mockPersonService.getByExternalId(invalidExternalId)).thenReturn(null);
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(
                Lists.newArrayList(new Organization()));
        ProtocolImportDetail detail = getValidProtocolImportDetail();
        bean.importProtocol(detail);
        assertEquals(ProtocolImportDetailStatus.IMPORT_ERROR, detail.getStatus());
        String failureMessage = getMessage(INVALID_INVESTIGATOR_ID_VALIDATION_FAILURE_MESSAGE_KEY, "1", "6",
                invalidExternalId);
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    @Test
    public void testImportProtocol_InvestigatorIdNotANumber() throws Exception {
        String invalidExternalId = "23XZ4";
        when(mockPersonService.getByExternalId(anyString())).thenReturn(new Person());
        when(mockPersonService.getByExternalId(invalidExternalId)).thenReturn(null);
        when(mockOrganizationService.getByAlternateIdentifier(anyString(), eq(GENERIC_ORGANIZATION))).thenReturn(
                Lists.newArrayList(new Organization()));
        ProtocolImportDetail detail = getInvalidProtocolImportDetail();
        bean.importProtocol(detail);
        assertEquals(ProtocolImportDetailStatus.IMPORT_ERROR, detail.getStatus());
        String failureMessage = getMessage(INVALID_INVESTIGATOR_ID_FORMAT_VALIDATION_FAILURE_MESSAGE_KEY, "1", "6",
                invalidExternalId);
        ProtocolImportServiceBeanTest.checkForExpectedFailures(detail, failureMessage);
    }

    private ProtocolImportDetail getInvalidProtocolImportDetail() throws Exception {
        Organization sponsor = OrganizationFactory.getInstanceWithId().create();
        File importFile = getImportTestFile(ImportTestFile.TEST_IMPORT_INVALID_INVESTIGATOR_ID_FORMAT);
        ProtocolImportJob job = new ProtocolImportJob(sponsor);
        job.loadFromFile(importFile, resources);
        return job.getDetails().get(0);
    }

}
