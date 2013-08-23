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
package gov.nih.nci.firebird.nes.person;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.coppa.common.LimitOffset;
import gov.nih.nci.coppa.common.faults.TooManyResultsFault;
import gov.nih.nci.coppa.po.IdentifiedPerson;
import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.coppa.po.StringMap;
import gov.nih.nci.coppa.po.StringMapType.Entry;
import gov.nih.nci.coppa.po.faults.NullifiedEntityFault;
import gov.nih.nci.coppa.po.faults.SimpleIIMapType;
import gov.nih.nci.coppa.po.faults.SimpleIIMapTypeEntry;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.firebird.common.RemoteServiceException;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.CurationDataset;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.common.NesTranslatorHelperUtils;
import gov.nih.nci.firebird.nes.common.ValidationErrorTranslator;
import gov.nih.nci.firebird.nes.correlation.NesPersonRoleIntegrationService;
import gov.nih.nci.firebird.nes.correlation.PersonRoleType;
import gov.nih.nci.firebird.service.person.external.InvalidatedPersonException;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ValidationExceptionFactory;
import gov.nih.nci.iso21090.extensions.Id;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.time.DateUtils;
import org.iso._21090.CD;
import org.iso._21090.II;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class NesPersonServiceBeanTest {

    private static final String TEST_CTEP_ID = "CTEP";
    private static final String TEST_NES_ID = "NES";
    private static final String MISSING_INFORMATION_ERROR_SUFFIX = "must be set";
    private static final String MISSING_EMAIL_ERROR = "At least one  must be set";

    @Mock
    private static ValidationErrorTranslator mockTranslator;
    @Mock
    private PersonI mockPersonClient;
    @Mock
    private IdentifiedPersonI mockIdentifiedPersonClient;
    @Mock
    private NesPersonRoleIntegrationService mockNesPersonRoleService;

    private NesPersonServiceBean service;

    @Before
    public void setUp() {
        service = new NesPersonServiceBean(mockPersonClient, mockIdentifiedPersonClient, mockTranslator,
                mockNesPersonRoleService);
        service.setResources(ResourceBundle.getBundle("resources", Locale.getDefault()));
    }

    @Test
    public void testSearch_ByEmail() throws Exception {
        Person person = createSearchResultPerson();
        when(mockPersonClient.query(any(Person.class), any(LimitOffset.class))).thenReturn(new Person[] { person });
        String searchTerm = "search_email@example.com";
        List<gov.nih.nci.firebird.data.Person> persons = service.search(searchTerm);
        assertEquals(1, persons.size());
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        verify(mockPersonClient).query(personCaptor.capture(), any(LimitOffset.class));
        gov.nih.nci.firebird.data.Person translatedSearchPerson = getTranslatedSearchPersons(personCaptor);
        assertEquals(searchTerm, translatedSearchPerson.getEmail());
    }

    private Person createSearchResultPerson() {
        Person person = new Person();
        II identifier = new II();
        identifier.setRoot(NesIIRoot.PERSON.getRoot());
        identifier.setExtension("extension");
        person.setIdentifier(identifier);
        person.setStatusCode(NesTranslatorHelperUtils.buildStatus(CurationStatus.ACTIVE));
        return person;
    }

    private gov.nih.nci.firebird.data.Person getTranslatedSearchPersons(ArgumentCaptor<Person> personCaptor) {
        Person searchPerson = personCaptor.getValue();
        searchPerson.setStatusCode(new CD()); // adding the status code is only necessary so we can translate to
        searchPerson.getStatusCode().setCode("active"); // a FIREBIRD Person for easy inspection of the results
        gov.nih.nci.firebird.data.Person translatedSearchPerson = PersonTranslator.buildFirebirdPerson(searchPerson);
        return translatedSearchPerson;
    }

    @Test
    public void testSearch_ByName() throws Exception {
        Person person = createSearchResultPerson();
        when(mockPersonClient.query(any(Person.class), any(LimitOffset.class))).thenReturn(new Person[] { person });
        List<gov.nih.nci.firebird.data.Person> results = service.search("last, first");
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        verify(mockPersonClient).query(personCaptor.capture(), any(LimitOffset.class));
        gov.nih.nci.firebird.data.Person translatedSearchPerson = getTranslatedSearchPersons(personCaptor);
        assertEquals("first", translatedSearchPerson.getFirstName());
        assertEquals("last", translatedSearchPerson.getLastName());
        assertEquals(1, results.size());

        when(mockPersonClient.query(any(Person.class), any(LimitOffset.class))).thenReturn(null);

        results = service.search("term");
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testSearch_ByCtepId() throws Exception {
        String ctepId = "ctepId";
        String nesId = "nesId";
        IdentifiedPerson identifiedPerson = new IdentifiedPerson();
        II playerIi = new II();
        playerIi.setExtension(nesId);
        identifiedPerson.setPlayerIdentifier(playerIi);
        when(mockIdentifiedPersonClient.query(any(IdentifiedPerson.class), any(LimitOffset.class))).thenReturn(
                new IdentifiedPerson[] { identifiedPerson });
        when(mockPersonClient.getById(any(Id.class))).thenReturn(createSearchResultPerson());
        assertEquals(1, service.search(ctepId).size());
        ArgumentCaptor<Id> idCaptor = ArgumentCaptor.forClass(Id.class);
        verify(mockPersonClient).getById(idCaptor.capture());
        assertEquals(nesId, idCaptor.getValue().getExtension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearch_EmptyTerm() throws Exception {
        service.search(" ");
    }

    @Test(expected = RuntimeException.class)
    public void testSearch_TooManyResults() throws Exception {
        when(mockPersonClient.query(any(Person.class), any(LimitOffset.class))).thenThrow(new TooManyResultsFault());
        service.search("term");
    }

    @Test(expected = RuntimeException.class)
    public void testSearch_RemoteException() throws Exception {
        when(mockPersonClient.query(any(Person.class), any(LimitOffset.class))).thenThrow(new RemoteException());
        service.search("term");
    }

    @Test(expected = RuntimeException.class)
    public void testGetByExternalId_RemoteException() throws Exception {
        when(mockPersonClient.getById(any(Id.class))).thenThrow(new RemoteException());
        service.getByExternalId(TEST_NES_ID);
    }

    @Test
    public void testGetByExternalId_Success() throws Exception {
        setUpNesPersonQuery();
        assertNotNull(service.getByExternalId(TEST_NES_ID));
        verify(mockIdentifiedPersonClient).query(any(IdentifiedPerson.class), any(LimitOffset.class));
    }

    private void setUpNesPersonQuery() throws Exception {
        Person person = PersonTranslator.buildNesPerson(PersonFactory.getInstance().create());
        person.setStatusCode(new CD());
        person.getStatusCode().setCode("active");
        when(mockPersonClient.getById(any(Id.class))).thenReturn(person);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByExternalId_NoResult() throws Exception {
        when(mockPersonClient.getById(any(Id.class))).thenReturn(null);
        service.getByExternalId(TEST_NES_ID);
    }

    @Test(expected = InvalidatedPersonException.class)
    public void testGetByExternalId_NullifiedEntityFaultForDuplicate() throws Exception {
        String nullifiedId = "1";
        String replacementId = "2";
        NullifiedEntityFault fault = createNullifiedEntityFault(nullifiedId, replacementId);
        when(mockPersonClient.getById(any(Id.class))).thenThrow(fault);
        service.getByExternalId(nullifiedId);
    }

    private NullifiedEntityFault createNullifiedEntityFault(String nullifiedId, String replacementId) {
        NullifiedEntityFault fault = new NullifiedEntityFault();
        SimpleIIMapTypeEntry[] entries = new SimpleIIMapTypeEntry[1];
        entries[0] = new SimpleIIMapTypeEntry(nullifiedId, replacementId);
        SimpleIIMapType nullifiedEntries = new SimpleIIMapType();
        nullifiedEntries.setEntry(entries);
        fault.setNullifiedEntries(nullifiedEntries);
        return fault;
    }

    @Test(expected = InvalidatedPersonException.class)
    public void testGetByExternalId_NullifiedEntityFaultWithNoSubstitute() throws Exception {
        String nullifiedId = "1";
        NullifiedEntityFault fault = createNullifiedEntityFault(nullifiedId, null);
        when(mockPersonClient.getById(any(Id.class))).thenThrow(fault);
        service.getByExternalId(nullifiedId);
    }

    @Test(expected = InvalidatedPersonException.class)
    public void testGetByExternalId_NullifiedEntityWithSameReplacementId() throws Exception {
        String nullifiedId = "1";
        String replacementId = "1";
        NullifiedEntityFault fault = createNullifiedEntityFault(nullifiedId, replacementId);
        when(mockPersonClient.getById(any(Id.class))).thenThrow(fault);
        service.getByExternalId(nullifiedId);
    }

    @Test
    public void testSave_Create() throws Exception {
        String externalId = "externalId";
        when(mockPersonClient.create(any(Person.class))).thenReturn(
                NesTranslatorHelperUtils.buildId("root", externalId));
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().createWithoutExternalData();

        when(mockPersonClient.validate(any(Person.class))).thenReturn(new StringMap());
        service.save(person);
        assertNotNull(person.getExternalData());
        NesPersonData nesPersonData = getNesPersonData(person);
        assertEquals(externalId, person.getExternalId());
        assertNotNull(nesPersonData.getLastNesRefresh());
        assertEquals(CurationStatus.PENDING, person.getCurationStatus());
    }

    private NesPersonData getNesPersonData(gov.nih.nci.firebird.data.Person person) {
        return (NesPersonData) person.getExternalData();
    }

    @Test(expected = RuntimeException.class)
    public void testSave_Create_Fail() throws Exception {
        when(mockPersonClient.create(any(Person.class))).thenThrow(new RemoteException());
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().createWithoutExternalData();
        service.save(person);
    }

    @Test(expected = RuntimeException.class)
    public void testSave_Create_RemoteException() throws Exception {
        when(mockPersonClient.create(any(Person.class))).thenThrow(new RemoteException());

        service.save(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSave_Update_FailNonExist() throws Exception {
        PersonFactory pf = PersonFactory.getInstance();
        gov.nih.nci.firebird.data.Person p = pf.create();

        when(mockPersonClient.getById(any(Id.class))).thenReturn(null);
        when(mockPersonClient.validate(any(Person.class))).thenReturn(new StringMap());
        service.save(p);
    }

    @Test(expected = RuntimeException.class)
    public void testSave_Update_Fail() throws Exception {
        doThrow(new RemoteException()).when(mockPersonClient).update(any(Person.class));
        PersonFactory pf = PersonFactory.getInstance();
        gov.nih.nci.firebird.data.Person p = pf.create();

        service.save(p);
    }

    @Test
    public void testSave_Update() throws Exception {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        gov.nih.nci.firebird.data.Person existingPersonRecord = PersonFactory.getInstance().create();

        when(mockPersonClient.getById(any(Id.class))).thenReturn(PersonTranslator.buildNesPerson(existingPersonRecord));
        when(mockPersonClient.validate(any(Person.class))).thenReturn(new StringMap());
        service.save(person);
        verify(mockPersonClient).update(any(Person.class));
        assertTrue(person.isUpdatePending());
        assertTrue(person.isEquivalent(existingPersonRecord));
    }

    @Test
    public void testSave_UpdateWithInvalidatedPersonException() throws Exception {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        NesPersonData originalNesPersonData = getNesPersonData(person);

        NullifiedEntityFault fault = createNullifiedEntityFault(person.getExternalId(), null);
        when(mockPersonClient.getById(any(Id.class))).thenThrow(fault);
        when(mockPersonClient.validate(any(Person.class))).thenReturn(new StringMap());
        String externalId = "externalId";
        when(mockPersonClient.create(any(Person.class))).thenReturn(
                NesTranslatorHelperUtils.buildId("root", externalId));
        service.save(person);
        verify(mockPersonClient).create(any(Person.class));
        assertEquals(externalId, person.getExternalId());
        NesPersonData updatedNesPersonData = getNesPersonData(person);
        assertNotSame(originalNesPersonData, updatedNesPersonData);
    }

    @Test
    public void testSave_UpdateWithReplacedPersonRecord() throws Exception {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        NesPersonData originalNesPersonData = getNesPersonData(person);
        gov.nih.nci.firebird.data.Person replacmentPerson = PersonFactory.getInstance().create();

        NullifiedEntityFault fault = createNullifiedEntityFault(person.getExternalId(),
                replacmentPerson.getExternalId());
        Person replacementNesPerson = PersonTranslator.buildNesPerson(replacmentPerson);
        when(mockPersonClient.getById(any(Id.class))).thenThrow(fault).thenReturn(replacementNesPerson);
        when(mockPersonClient.validate(any(Person.class))).thenReturn(new StringMap());
        service.save(person);
        NesPersonData updatedNesPersonData = getNesPersonData(person);
        assertNotSame(originalNesPersonData, updatedNesPersonData);
        assertEquals(replacmentPerson.getExternalId(), person.getExternalId());
    }

    @Test
    public void testValidate() throws Exception {
        StringMap expectedResult = new StringMap();
        Entry expectedError = getStringMapEntry("email", MISSING_EMAIL_ERROR);
        expectedResult.getEntry().add(expectedError);
        when(mockPersonClient.validate(any(Person.class))).thenReturn(expectedResult);

        Map<String, String> errors = Maps.newHashMap();
        errors.put("email", "Email Address " + MISSING_INFORMATION_ERROR_SUFFIX);
        when(mockTranslator.translateStringMapValidation(expectedResult)).thenReturn(
                ValidationExceptionFactory.getInstance().create(errors).getResult());

        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.setEmail(null);
        try {
            service.validate(person);
            fail("A Validation Exception should have been thrown!");
        } catch (ValidationException e) {
            ValidationResult result = e.getResult();
            verify(mockPersonClient).validate(any(Person.class));
            assertNotNull(result);
            assertEquals(1, result.getFailures().size());
            ValidationFailure failure = result.getFailures().iterator().next();
            assertEquals("email", failure.getFieldKey());
            assertEquals("Email Address " + MISSING_INFORMATION_ERROR_SUFFIX, failure.getMessage());
        }
    }

    private Entry getStringMapEntry(String key, String value) {
        Entry expectedError = new Entry();
        expectedError.setKey(key);
        expectedError.getValue().add(value);
        return expectedError;
    }

    @Test
    public void testValidate_NoErrors() throws Exception {
        StringMap expectedResult = new StringMap();
        when(mockPersonClient.validate(any(Person.class))).thenReturn(expectedResult);

        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.setEmail(null);
        service.validate(person);
    }

    @Test
    public void testGetCtepId() throws Exception {
        setUpIdentifiedPersonSearch();
        assertEquals(TEST_CTEP_ID, service.getCtepId("1"));
    }

    private void setUpIdentifiedPersonSearch() throws Exception {
        IdentifiedPerson person = new IdentifiedPerson();
        II assignedIi = new II();
        assignedIi.setExtension(TEST_CTEP_ID);
        person.setAssignedId(assignedIi);
        II playerIi = new II();
        playerIi.setExtension(TEST_NES_ID);
        person.setPlayerIdentifier(playerIi);
        IdentifiedPerson[] persons = { person };
        when(mockIdentifiedPersonClient.query(any(IdentifiedPerson.class), any(LimitOffset.class))).thenReturn(persons);
    }

    @Test
    public void testGetCtepId_NoResults() throws Exception {
        when(mockIdentifiedPersonClient.query(any(IdentifiedPerson.class), any(LimitOffset.class))).thenReturn(null);
        assertNull(service.getCtepId("1"));
    }

    @Test(expected = RemoteServiceException.class)
    public void testGetCtepId_TooManyResultsFault() throws Exception {
        when(mockIdentifiedPersonClient.query(any(IdentifiedPerson.class), any(LimitOffset.class))).thenThrow(
                new TooManyResultsFault());
        service.getCtepId("1");
    }

    @Test(expected = RemoteServiceException.class)
    public void testGetCtepId_RemoteException() throws Exception {
        when(mockIdentifiedPersonClient.query(any(IdentifiedPerson.class), any(LimitOffset.class))).thenThrow(
                new RemoteException());
        service.getCtepId("1");
    }

    @Test
    public void testRefreshNow_WithChanges() throws Exception {
        gov.nih.nci.firebird.data.Person person = getPersonWithPendingUpdates();
        person.setProviderNumber(null);
        person.setCurationStatus(CurationStatus.PENDING);
        gov.nih.nci.firebird.data.Person updatedPerson = PersonFactory.getInstance().create();
        updatedPerson.setProviderNumber(null);
        updatedPerson.setCurationStatus(CurationStatus.ACTIVE);
        when(mockPersonClient.getById(any(Id.class))).thenReturn(PersonTranslator.buildNesPerson(updatedPerson));
        service.refreshNow(person);
        assertTrue(person.isEquivalent(updatedPerson));
        assertEquals(person.getCurationStatus(), updatedPerson.getCurationStatus());
        assertFalse(person.isUpdatePending());
        assertNotNull(getNesPersonData(person).getLastNesRefresh());
    }

    private gov.nih.nci.firebird.data.Person getPersonWithPendingUpdates() {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        ((NesPersonData) person.getExternalData()).requestUpdate();
        return person;
    }

    @Test
    public void testRefreshNow_NoChanges() throws Exception {
        gov.nih.nci.firebird.data.Person person = getPersonWithPendingUpdates();
        when(mockPersonClient.getById(any(Id.class))).thenReturn(PersonTranslator.buildNesPerson(person));
        service.refreshNow(person);
        assertTrue(person.isUpdatePending());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefreshNow_NoExternalId() {
        gov.nih.nci.firebird.data.Person person = new gov.nih.nci.firebird.data.Person();
        service.refreshNow(person);
    }

    @Test
    public void testRefreshNow_UnavailableEntityException() throws Exception {
        gov.nih.nci.firebird.data.Person person = getPersonWithPendingUpdates();
        NesPersonData orignalPersonData = getNesPersonData(person);
        when(mockPersonClient.getById(any(Id.class))).thenThrow(createNullifiedEntityFault("1", null));
        Id id = new Id();
        id.setRoot(NesIIRoot.PERSON.getRoot());
        id.setExtension("new_extension");
        when(mockPersonClient.create(any(Person.class))).thenReturn(id);
        service.refreshNow(person);
        verify(mockPersonClient).create(any(Person.class));
        assertEquals(CurationStatus.PENDING, person.getCurationStatus());
        NesPersonData updatedPersonData = getNesPersonData(person);
        assertNotSame(orignalPersonData, updatedPersonData);
        assertTrue(updatedPersonData.isUpdatePending());
        assertEquals(id.getExtension(), person.getExternalId());
    }

    @Test
    public void testRefreshIfStale_StaleRecord() throws Exception {
        NesPersonServiceBean serviceSpy = spy(service);
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        when(mockPersonClient.getById(any(Id.class))).thenReturn(PersonTranslator.buildNesPerson(person));
        Date staleDate = DateUtils.addDays(new Date(), -2);
        getNesPersonData(person).setLastNesRefresh(staleDate);
        serviceSpy.refreshIfStale(person);
        verify(serviceSpy).refreshNow(person);
    }

    @Test
    public void testRefreshIfStale_FreshRecord() throws Exception {
        NesPersonServiceBean serviceSpy = spy(service);
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        when(mockPersonClient.getById(any(Id.class))).thenReturn(PersonTranslator.buildNesPerson(person));
        serviceSpy.refreshIfStale(person);
        verify(serviceSpy, never()).refreshNow(person);
    }

    @Test
    public void testCheckCorrelation() throws Exception {
        NesPersonServiceBean serviceSpy = spy(service);
        when(serviceSpy.getAsyncNesPersonRoleServiceProxy()).thenReturn(mockNesPersonRoleService);
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        Organization organization = OrganizationFactory.getInstance().create();
        PersonRoleType type = PersonRoleType.HEALTH_CARE_PROVIDER;

        serviceSpy.checkCorrelation(person, organization, type);
        verify(mockNesPersonRoleService).ensureCorrelated(person, organization, type);
    }
    
    @Test
    public void testGetPersonsToBeCurated() {
        NesPersonServiceBean serviceSpy = spy(service);
        gov.nih.nci.firebird.data.Person activePerson = PersonFactory.getInstance().create();
        activePerson.setCurationStatus(CurationStatus.ACTIVE);
        gov.nih.nci.firebird.data.Person pendingPerson = PersonFactory.getInstance().create();
        pendingPerson.setCurationStatus(CurationStatus.PENDING);
        List<gov.nih.nci.firebird.data.Person> candidates = Lists.newArrayList(pendingPerson, activePerson);
        CurationDataset curationDataset = serviceSpy.getPersonsToBeCurated(candidates);
        verify(serviceSpy).refreshIfStale(pendingPerson);
        verify(serviceSpy).refreshIfStale(activePerson);
        assertEquals(2, curationDataset.getCsvHeaders().size());
        assertEquals(2, curationDataset.getDisplayHeaderKeys().size());
        assertEquals(1, curationDataset.getRows().size());
        List<String> row = curationDataset.getRows().get(0);
        assertEquals(pendingPerson.getExternalId(), row.get(0));
        assertEquals(pendingPerson.getDisplayNameForList(), row.get(1));
    }

}
