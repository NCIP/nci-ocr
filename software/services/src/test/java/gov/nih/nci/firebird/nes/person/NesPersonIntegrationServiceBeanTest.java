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
import gov.nih.nci.coppa.po.faults.EntityValidationFault;
import gov.nih.nci.coppa.po.faults.NullifiedEntityFault;
import gov.nih.nci.coppa.po.faults.SimpleIIMapType;
import gov.nih.nci.coppa.po.faults.SimpleIIMapTypeEntry;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.NesTranslatorHelperUtils;
import gov.nih.nci.firebird.nes.common.ReplacedEntityException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.nes.common.ValidationErrorTranslator;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ValidationExceptionFactory;
import gov.nih.nci.iso21090.extensions.Id;

import java.rmi.RemoteException;
import java.util.Map;

import org.iso._21090.CD;
import org.iso._21090.II;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class NesPersonIntegrationServiceBeanTest {

    private static final String TEST_CTEP_ID = "CTEP";
    private static final String TEST_NES_ID = "NES";

    @Mock
    private static ValidationErrorTranslator mockTranslator;
    @Mock
    private PersonI mockPersonClient;
    @Mock
    private IdentifiedPersonI mockIdentifiedPersonClient;
    private NesPersonIntegrationServiceBean service;

    private static final String MISSING_INFORMATION_ERROR_SUFFIX = "must be set";
    private static final String MISSING_EMAIL_ERROR = "At least one  must be set";

    @Before
    public void setUp() {
        service = new NesPersonIntegrationServiceBean(mockPersonClient, mockIdentifiedPersonClient, mockTranslator);
    }

    @Test
    public void testSearchByName() throws TooManyResultsFault, RemoteException {
        Person person = createSearchResultPerson();
        when(mockPersonClient.query(any(Person.class), any(LimitOffset.class))).thenReturn(new Person[] { person });
        gov.nih.nci.firebird.data.Person[] results = service.searchByName("first", "last");
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        verify(mockPersonClient).query(personCaptor.capture(), any(LimitOffset.class));
        gov.nih.nci.firebird.data.Person translatedSearchPerson = getTranslatedSearchPersons(personCaptor);
        assertEquals("first", translatedSearchPerson.getFirstName());
        assertEquals("last", translatedSearchPerson.getLastName());
        assertEquals(1, results.length);
        assertNotNull(results[0].getLastNesRefresh());

        when(mockPersonClient.query(any(Person.class), any(LimitOffset.class))).thenReturn(null);

        results = service.searchByName(null, null);
        assertNotNull(results);
        assertEquals(0, results.length);
    }

    private Person createSearchResultPerson() {
        Person person = new Person();
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

    @Test(expected = RuntimeException.class)
    public void testSearchByName_TooManyResults() throws Exception {
        when(mockPersonClient.query(any(Person.class), any(LimitOffset.class))).thenThrow(new TooManyResultsFault());
        service.searchByName(null, null);
    }

    @Test(expected = RuntimeException.class)
    public void testSearchByName_RemoteException() throws Exception {
        when(mockPersonClient.query(any(Person.class), any(LimitOffset.class))).thenThrow(new RemoteException());
        service.searchByName(null, null);
    }

    @Test
    public void testSearchByEmail() throws RemoteException {
        Person person = createSearchResultPerson();
        when(mockPersonClient.query(any(Person.class), any(LimitOffset.class))).thenReturn(new Person[] { person });
        String email = "search_email@example.com";
        gov.nih.nci.firebird.data.Person[] persons = service.searchByEmail(email);
        assertEquals(1, persons.length);
        assertNotNull(persons[0].getLastNesRefresh());
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        verify(mockPersonClient).query(personCaptor.capture(), any(LimitOffset.class));
        gov.nih.nci.firebird.data.Person translatedSearchPerson = getTranslatedSearchPersons(personCaptor);
        assertEquals(email, translatedSearchPerson.getEmail());
    }

    @Test
    public void testSearchByEmail_InvalidCharacters() throws RemoteException {
        for (char invalidCharacter : NesPersonIntegrationServiceBean.INVALID_EMAIL_ADDRESS_CHARACTERS) {
            String email = invalidCharacter + "@test.com";
            assertTrue(service.searchByEmail(email).length == 0);
        }
        verifyZeroInteractions(mockPersonClient);
    }

    @Test(expected = RuntimeException.class)
    public void testGetById_RemoteException() throws Exception {
        when(mockPersonClient.getById(any(Id.class))).thenThrow(new RemoteException());
        service.getById(TEST_NES_ID);
    }

    @Test
    public void testGetById_Success() throws Exception {
        setUpNesPersonQuery();
        assertNotNull(service.getById(TEST_NES_ID));
        verify(mockIdentifiedPersonClient).query(any(IdentifiedPerson.class), any(LimitOffset.class));
    }

    private void setUpNesPersonQuery() throws RemoteException, NullifiedEntityFault {
        Person person = PersonTranslator.buildNesPerson(PersonFactory.getInstance().create());
        person.setStatusCode(new CD());
        person.getStatusCode().setCode("active");
        when(mockPersonClient.getById(any(Id.class))).thenReturn(person);
    }

    @Test
    public void testGetById_NoResult() throws Exception {
        when(mockPersonClient.getById(any(Id.class))).thenReturn(null);
        assertNull(service.getById(TEST_NES_ID));
        verifyZeroInteractions(mockIdentifiedPersonClient);
    }

    @Test
    public void testCreatePerson() throws EntityValidationFault, RemoteException, ValidationException {
        String nesId = "nesId";
        when(mockPersonClient.create(any(Person.class))).thenReturn(NesTranslatorHelperUtils.buildId("root", nesId));
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.setNesId(null);

        assertEquals(nesId, service.createPerson(person));
        assertNotNull(person.getLastNesRefresh());
        assertEquals(CurationStatus.PENDING, person.getNesStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testCreatePerson_Fail() throws EntityValidationFault, RemoteException, ValidationException {
        when(mockPersonClient.create(any(Person.class))).thenThrow(new RemoteException());
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.setNesId(null);

        service.createPerson(person);
    }

    @Test(expected = RuntimeException.class)
    public void testCreatePerson_RemoteException() throws Exception {
        when(mockPersonClient.create(any(Person.class))).thenThrow(new RemoteException());

        service.createPerson(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePerson_FailNonExist() throws EntityValidationFault, RemoteException, ValidationException {
        PersonFactory pf = PersonFactory.getInstance();
        gov.nih.nci.firebird.data.Person p = pf.create();

        when(mockPersonClient.getById(any(Id.class))).thenReturn(null);
        service.updatePerson(p);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdatePerson_Fail() throws EntityValidationFault, RemoteException, ValidationException {
        doThrow(new RemoteException()).when(mockPersonClient).update(any(Person.class));
        PersonFactory pf = PersonFactory.getInstance();
        gov.nih.nci.firebird.data.Person p = pf.create();

        service.createPerson(p);
    }

    @Test
    public void testValidate() throws RemoteException {
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
    public void testValidate_NoErrors() throws RemoteException {
        StringMap expectedResult = new StringMap();
        when(mockPersonClient.validate(any(Person.class))).thenReturn(expectedResult);

        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.setEmail(null);
        try {
            service.validate(person);
        } catch (ValidationException e) {
            fail("A Validation Exception should not have been thrown!");
        }
    }

    @Test(expected = ReplacedEntityException.class)
    public void testGetById_NullifiedEntityFaultForDuplicate() throws NullifiedEntityFault, RemoteException,
            UnavailableEntityException, ReplacedEntityException {
        String nullifiedId = "1";
        String replacementId = "2";
        NullifiedEntityFault fault = createNullifiedEntityFault(nullifiedId, replacementId);
        when(mockPersonClient.getById(any(Id.class))).thenThrow(fault);
        service.getById(nullifiedId);
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

    @Test(expected = UnavailableEntityException.class)
    public void testGetById_NullifiedEntityFaultWithNoSubstitute() throws NullifiedEntityFault, RemoteException,
            UnavailableEntityException, ReplacedEntityException {
        String nullifiedId = "1";
        NullifiedEntityFault fault = createNullifiedEntityFault(nullifiedId, null);
        when(mockPersonClient.getById(any(Id.class))).thenThrow(fault);
        service.getById(nullifiedId);
    }

    @Test(expected = UnavailableEntityException.class)
    public void testGetById_NullifiedEntityWithSameReplacementId() throws NullifiedEntityFault, RemoteException,
            UnavailableEntityException, ReplacedEntityException {
        String nullifiedId = "1";
        String replacementId = "1";
        NullifiedEntityFault fault = createNullifiedEntityFault(nullifiedId, replacementId);
        when(mockPersonClient.getById(any(Id.class))).thenThrow(fault);
        service.getById(nullifiedId);
    }

    @Test
    public void testGetCtepId() throws TooManyResultsFault, RemoteException {
        setUpIdentifiedPersonSearch();
        assertEquals(TEST_CTEP_ID, service.getCtepId("1"));
    }

    private void setUpIdentifiedPersonSearch() throws RemoteException, TooManyResultsFault {
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
    public void testGetCtepId_NoResults() throws TooManyResultsFault, RemoteException {
        when(mockIdentifiedPersonClient.query(any(IdentifiedPerson.class), any(LimitOffset.class))).thenReturn(null);
        assertNull(service.getCtepId("1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCtepId_TooManyResultsFault() throws Exception {
        when(mockIdentifiedPersonClient.query(any(IdentifiedPerson.class), any(LimitOffset.class))).thenThrow(
                new TooManyResultsFault());
        service.getCtepId("1");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCtepId_RemoteException() throws Exception {
        when(mockIdentifiedPersonClient.query(any(IdentifiedPerson.class), any(LimitOffset.class))).thenThrow(
                new RemoteException());
        service.getCtepId("1");
    }

    @Test
    public void testGetByCtepId() throws TooManyResultsFault, RemoteException, UnavailableEntityException {
        setUpIdentifiedPersonSearch();
        setUpNesPersonQuery();
        assertNotNull(service.getByCtepId(TEST_CTEP_ID));
    }

    @Test
    public void testGetByCtepId_NotFound() throws TooManyResultsFault, RemoteException, UnavailableEntityException {
        assertNull(service.getByCtepId(TEST_CTEP_ID));
    }

    @Test
    public void testGetByCtepId_ReplacedEntityException() throws TooManyResultsFault, RemoteException, UnavailableEntityException {
        setUpIdentifiedPersonSearch();
        Person replacementPerson = PersonTranslator.buildNesPerson(PersonFactory.getInstance().create());
        NullifiedEntityFault fault = createNullifiedEntityFault(TEST_NES_ID, "replacement_nes_id");
        when(mockPersonClient.getById(any(Id.class))).thenThrow(fault).thenReturn(replacementPerson);
        assertNotNull(service.getByCtepId(TEST_CTEP_ID));
    }

}
