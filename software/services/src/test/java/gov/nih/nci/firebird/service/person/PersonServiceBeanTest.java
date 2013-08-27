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
package gov.nih.nci.firebird.service.person;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.coppa.po.faults.EntityValidationFault;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.ReplacedEntityException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.nes.common.ValidationErrorTranslator;
import gov.nih.nci.firebird.nes.person.NesPersonIntegrationService;
import gov.nih.nci.firebird.nes.person.NesPersonIntegrationServiceBean;
import gov.nih.nci.firebird.test.AbstractHibernateTestCase;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ValueGenerator;

import java.rmi.RemoteException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.inject.Inject;

public class PersonServiceBeanTest extends AbstractHibernateTestCase {

    private static final String VALID_NES_ID = "1";
    private static final String INVALID_NES_ID = "5";

    @Inject
    private PersonServiceBean bean;
    private NesPersonIntegrationService mockNesPersonService = mock(NesPersonIntegrationService.class);
    private Person person = PersonFactory.getInstance().create();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        person.setNesId(VALID_NES_ID);
        bean.setNesIntegrationService(mockNesPersonService);
        when(mockNesPersonService.getById(VALID_NES_ID)).thenReturn(person);
        when(mockNesPersonService.createPerson(any(Person.class))).thenReturn(VALID_NES_ID);
    }

    /**
     * Test the person display name.
     */
    @Test
    public void testDisplayName() {
        Person person = new Person();
        person.setFirstName("fname");
        assertEquals("fname", person.getDisplayName());

        person.setLastName("lname");
        assertEquals("fname lname", person.getDisplayName());

        person.setPrefix("Mr");
        assertEquals("Mr fname lname", person.getDisplayName());

        person.setMiddleName("     ");
        assertEquals("Mr fname lname", person.getDisplayName());
    }

    /**
     * test the crud methods.
     */
    @Test
    public void testCreateRetrieveUpdateDelete() {
        Person person1 = PersonFactory.getInstance().create();

        Long id = bean.save(person1);
        flushAndClearSession();

        Person person2 = (Person) getCurrentSession().get(Person.class, id);
        assertTrue(person1.isEquivalent(person2));
        getCurrentSession().clear();

        List<Person> results = bean.getAll();
        assertEquals(1, results.size());

        Person person3 = PersonFactory.getInstance().create();
        bean.save(person3);
        flushAndClearSession();

        results = bean.getAll();
        assertEquals(2, results.size());

        person2 = bean.getById(id);
        bean.delete(person2);

        results = bean.getAll();
        assertEquals(1, results.size());
        flushAndClearSession();

        assertNull(getCurrentSession().get(Person.class, id));
    }

    @Test
    public void testImportNewOrExistingNesPerson() throws UnavailableEntityException {
        // import person with invalid NES_ID. This should return null, and not insert FB person in DB
        Person person1 = bean.importNesPerson(INVALID_NES_ID);
        assertNull(person1);
        List<Person> people = bean.getAll();
        assertEquals(0, people.size());
        flushAndClearSession();

        // import person with known NES_ID. This person should end up in DB.
        person1 = bean.importNesPerson(VALID_NES_ID);
        assertNotNull(person1);
        people = bean.getAll();
        assertEquals(1, people.size());
        assertEquals(person1, people.get(0));

        // import person with same NES_ID. Should update, not add person
        person1 = bean.importNesPerson(VALID_NES_ID);
        assertNotNull(person1);
        people = bean.getAll();
        assertEquals(1, people.size());
        flushAndClearSession();

        // Does retrieved instance equal imported instance
        Person person2 = bean.getById(person1.getId());
        assertTrue(person1.isEquivalent(person2));

        // Modify value in person instance, save, then import again to insure value changes
        String lastName = person2.getLastName();
        person2.setLastName("testvaluechanged");
        assertFalse(person1.isEquivalent(person2));
        bean.save(person2);
        flushAndClearSession();

        person1 = bean.importNesPerson(VALID_NES_ID);
        assertNotNull(person1);
        assertEquals(lastName, person1.getLastName());
    }

    @Test
    public void testCreateNesPerson() throws ValidationException {
        Person person = PersonFactory.getInstance().create();
        person.setNesId(null);
        person = bean.createNesPerson(person);

        assertNotNull(person.getNesId());
    }

    @Test
    public void testUpdateNesPerson() throws ValidationException {
        Person personCopy = new Person();
        bean.copyPersonFields(personCopy, person);
        String updatedLastName = ValueGenerator.getUniqueString();
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);

        bean.save(person);
        flushAndClearSession();

        personCopy.setId(person.getId());
        personCopy.setLastName(updatedLastName);

        bean.updateNesPerson(personCopy);

        person = reloadObject(person);
        verify(mockNesPersonService).updatePerson(personCaptor.capture());
        assertFalse(person.isEquivalent(personCaptor.getValue()));
        assertFalse(person.getLastName().equals(updatedLastName));
        assertNotNull(person.getUpdateRequested());
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateNesPersonFail() throws EntityValidationFault, RemoteException, ValidationException {
        PersonI mockPersonClient = mock(PersonI.class);
        ValidationErrorTranslator translator = mock(ValidationErrorTranslator.class);
        doThrow(new RemoteException()).when(mockPersonClient).update(any(gov.nih.nci.coppa.po.Person.class));

        NesPersonIntegrationService nesService = new NesPersonIntegrationServiceBean(mockPersonClient,
                mock(IdentifiedPersonI.class), translator);
        bean = new PersonServiceBean();
        bean.setNesIntegrationService(nesService);

        bean.updateNesPerson(PersonFactory.getInstance().create());
    }

    @Test
    public void testRefreshFromNes_WithChanges() throws UnavailableEntityException, ReplacedEntityException {
        Person person = getPersonWithPendingUpdates();
        Person updatedPerson = PersonFactory.getInstance().create();
        when(mockNesPersonService.getById(person.getNesId())).thenReturn(updatedPerson);
        bean.refreshFromNes(person);
        assertTrue(person.isEquivalent(updatedPerson));
        assertTrue(updatedPerson.isEquivalent(person));
        assertFalse(person.isUpdatePending());
        assertNotNull(person.getLastNesRefresh());
    }

    private Person getPersonWithPendingUpdates() {
        Person person = PersonFactory.getInstance().create();
        person.requestUpdate();
        return person;
    }

    @Test
    public void testRefreshFromNes_NoChanges() throws UnavailableEntityException, ReplacedEntityException {
        Person person = getPersonWithPendingUpdates();
        when(mockNesPersonService.getById(person.getNesId())).thenReturn(person);
        bean.refreshFromNes(person);
        assertTrue(person.isUpdatePending());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefreshFromNes_NoNesId() {
        Person person = new Person();
        bean.refreshFromNes(person);
    }

    @Test
    public void testRefreshFromNes_UnavailableEntityException() throws UnavailableEntityException,
            ReplacedEntityException {
        Person person = getPersonWithPendingUpdates();
        when(mockNesPersonService.getById(person.getNesId())).thenThrow(
                new UnavailableEntityException(null, person.getNesId()));
        bean.refreshFromNes(person);
        assertEquals(CurationStatus.NULLIFIED, person.getNesStatus());
        assertTrue(person.isUpdatePending());
    }

    @Test
    public void testRefreshFromNes_ReplacedEntityException() throws UnavailableEntityException, ReplacedEntityException {
        Person person = getPersonWithPendingUpdates();
        String nesId = person.getNesId();
        when(mockNesPersonService.getById(nesId)).thenThrow(new ReplacedEntityException(null, nesId, null));
        bean.refreshFromNes(person);
        assertEquals(CurationStatus.NULLIFIED, person.getNesStatus());
        assertTrue(person.isUpdatePending());
    }

    @Test
    public void testValidatePerson() throws ValidationException {
        bean.validatePerson(person);
        verify(mockNesPersonService).validate(person);
    }

    @Test
    public void testGetPersonsToBeCurated() {
        Person curatedPerson = PersonFactory.getInstance().create();
        curatedPerson.setNesStatus(CurationStatus.ACTIVE);
        Person uncuratedPerson = PersonFactory.getInstance().create();
        uncuratedPerson.setNesStatus(CurationStatus.PENDING);
        Person inactivePerson = PersonFactory.getInstance().create();
        inactivePerson.setNesStatus(CurationStatus.INACTIVE);
        Person pendingUpdatesPerson = getPersonWithPendingUpdates();
        save(curatedPerson, uncuratedPerson, inactivePerson, pendingUpdatesPerson);

        List<Person> personsWaitingForCuration = bean.getPersonsToBeCurated();
        assertEquals(2, personsWaitingForCuration.size());
        assertTrue(personsWaitingForCuration.contains(uncuratedPerson));
        assertTrue(personsWaitingForCuration.contains(pendingUpdatesPerson));
    }

    @Test
    public void testSaveProviderNumberPass() {
        Person curatedPerson = PersonFactory.getInstance().create();
        curatedPerson.setProviderNumber("1234");

        Long id = bean.save(curatedPerson);
        flushAndClearSession();

        Person loadedPerson = (Person) getCurrentSession().get(Person.class, id);
        assertSame(curatedPerson.getProviderNumber(), loadedPerson.getProviderNumber());
    }

    @Test(expected = RuntimeException.class)
    public void testSaveProviderNumberFailValidation() {
        Person curatedPerson = PersonFactory.getInstance().create();
        curatedPerson.setProviderNumber(ValueGenerator.getUniqueString(51));

        bean.save(curatedPerson);
    }

    @Test
    public void testGetByCtepId_LocalResult() {
        Person person = PersonFactory.getInstance().create();
        String ctepId = ValueGenerator.getUniqueString(5);
        person.setCtepId(ctepId);
        saveAndFlush(person);
        
        assertSamePersistentObjects(person, bean.getByCtepId(ctepId));
    }

    @Test
    public void testGetByCtepId_NesResult() throws UnavailableEntityException {
        Person person = PersonFactory.getInstance().create();
        String ctepId = ValueGenerator.getUniqueString(5);
        person.setCtepId(ctepId);
        when(mockNesPersonService.getByCtepId(ctepId)).thenReturn(person);
        assertSame(person, bean.getByCtepId(ctepId));
    }

    @Test
    public void testGetByCtepId_NoResults() throws UnavailableEntityException {
        String ctepId = ValueGenerator.getUniqueString(5);
        when(mockNesPersonService.getByCtepId(ctepId)).thenReturn(null);
        assertNull(bean.getByCtepId(ctepId));
    }

    @Test
    public void testGetByCtepId_UnavailableEntityException() throws UnavailableEntityException {
        String ctepId = ValueGenerator.getUniqueString(5);
        when(mockNesPersonService.getByCtepId(ctepId)).thenThrow(new UnavailableEntityException(null, null));
        assertNull(bean.getByCtepId(ctepId));
    }

}
