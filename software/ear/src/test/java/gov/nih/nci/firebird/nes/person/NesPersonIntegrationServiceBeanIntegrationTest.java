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
import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.coppa.po.faults.EntityValidationFault;
import gov.nih.nci.coppa.services.entities.person.client.PersonClient;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.ReplacedEntityException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.nes.common.ValidationErrorTranslator;
import gov.nih.nci.firebird.test.AbstractIntegrationTest;
import gov.nih.nci.firebird.test.PersonFactory;

import java.rmi.RemoteException;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;

public class NesPersonIntegrationServiceBeanIntegrationTest extends AbstractIntegrationTest {

    private static final String MISSING_INFORMATION_ERROR = " must be set";
    private static final String MALFORMED_EMAIL_ERROR = " is not a well-formed email address";

    @Inject
    private NesPersonIntegrationService service;
    @Inject
    ValidationErrorTranslator errorTranslator;
    @Inject
    ResourceBundle resources;
    private gov.nih.nci.firebird.data.Person knownNesPerson;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        knownNesPerson = getNesTestDataSource().getPerson();
    }

    @Test
    public void testSearchByName() {
        gov.nih.nci.firebird.data.Person[] results = service.searchByName(null, knownNesPerson.getLastName());
        assertTrue(results.length >= 1);
        results = service.searchByName(null, "ABCDEFGHIJK");
        assertEquals(0, results.length);
    }

    @Test
    public void testSearchByEmail() {
        gov.nih.nci.firebird.data.Person[] results = service.searchByEmail(knownNesPerson.getEmail());
        assertTrue(results.length >= 1);
        results = service.searchByEmail("nonexistent_firebird_search@example.com");
        assertEquals(0, results.length);
    }

    @Test
    public void testGetByNesId() throws UnavailableEntityException, ReplacedEntityException {
        String knownNesId = knownNesPerson.getNesId();
        gov.nih.nci.firebird.data.Person result = service.getById(knownNesId);
        assertEquals(knownNesId, result.getNesId());
        result = service.getById("0");
        assertNull(result);
    }

    @Test
    public void testCreatePerson() throws ValidationException {
        gov.nih.nci.firebird.data.Person p = PersonFactory.getInstance().create();
        p.setNesId(null);
        String result = service.createPerson(p);
        assertNotNull(result);
    }

    @Test
    public void testUpdatePerson() throws EntityValidationFault, RemoteException, ValidationException,
            UnavailableEntityException, ReplacedEntityException {
        PersonI personClient = spy(getInjector().getInstance(PersonClient.class));
        service = new NesPersonIntegrationServiceBean(personClient, mock(IdentifiedPersonI.class), errorTranslator);
        gov.nih.nci.firebird.data.Person p = PersonFactory.getInstance().create();
        p.setNesId(null);

        String result = service.createPerson(p);
        p.setNesId(result);

        p.getPostalAddress().setDeliveryAddress("Test Line 2 Change");
        service.updatePerson(p);

        p = service.getById(result);

        assertNotNull(p);
        verify(personClient).update(any(Person.class));
        /*
         * Intended to verify that the record in NES has been updated, however it appears that when obtaining the same
         * person record following an update, at least when the original record is in a pending status, the changes are
         * not immediately reflected when obtaining the record. As such, I'm not sure what I can check here beyond that
         * the calls were able to be made successfully, all functional tests are contained within the mock test.
         */
    }

    @Test
    public void testValidate_NoEmail() throws RemoteException {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.setEmail(null);
        try {
            service.validate(person);
            fail("A Validation Exception should have been thrown!");
        } catch (ValidationException e) {
            ValidationResult result = e.getResult();
            assertNotNull(result);
            assertEquals(1, result.getFailures().size());
            ValidationFailure failure = result.getFailures().iterator().next();
            assertEquals("email", failure.getFieldKey());
            assertEquals("Email Address" + MALFORMED_EMAIL_ERROR, failure.getMessage());
        }
    }

    @Test
    public void testValidate_NoFirstName() throws RemoteException {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.setFirstName(null);
        try {
            service.validate(person);
            fail("A Validation Exception should have been thrown!");
        } catch (ValidationException e) {
            ValidationResult result = e.getResult();
            assertNotNull(result);
            assertEquals(1, result.getFailures().size());
            ValidationFailure failure = result.getFailures().iterator().next();
            assertEquals("firstName", failure.getFieldKey());
            assertEquals("First Name" + MISSING_INFORMATION_ERROR, failure.getMessage());
        }
    }

    @Test
    public void testValidate_NoLastName() throws RemoteException {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.setLastName(null);
        try {
            service.validate(person);
            fail("A Validation Exception should have been thrown!");
        } catch (ValidationException e) {
            ValidationResult result = e.getResult();
            assertNotNull(result);
            assertEquals(1, result.getFailures().size());
            ValidationFailure failure = result.getFailures().iterator().next();
            assertEquals("lastName", failure.getFieldKey());
            assertEquals("Last Name" + MISSING_INFORMATION_ERROR, failure.getMessage());
        }
    }

    @Test
    public void testValidate_NoPostalAddress() throws RemoteException {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.setPostalAddress(null);
        try {
            service.validate(person);
            fail("A Validation Exception should have been thrown!");
        } catch (ValidationException e) {
            ValidationResult result = e.getResult();
            assertNotNull(result);
            assertEquals(1, result.getFailures().size());
            ValidationFailure failure = result.getFailures().iterator().next();
            assertNull(failure.getFieldKey());
            assertEquals("Postal Address" + MISSING_INFORMATION_ERROR, failure.getMessage());
        }
    }

    @Test
    public void testValidate_NoPostalAddressStreetAddress() throws RemoteException {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.getPostalAddress().setStreetAddress(null);
        try {
            service.validate(person);
            fail("A Validation Exception should have been thrown!");
        } catch (ValidationException e) {
            ValidationResult result = e.getResult();
            assertNotNull(result);
            assertEquals(1, result.getFailures().size());
            ValidationFailure failure = result.getFailures().iterator().next();
            assertEquals("postalAddress.streetAddress", failure.getFieldKey());
            assertEquals("Address Line 1" + MISSING_INFORMATION_ERROR, failure.getMessage());
        }
    }

    @Test
    public void testValidate_NoPostalAddressCity() throws RemoteException {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.getPostalAddress().setCity(null);
        try {
            service.validate(person);
            fail("A Validation Exception should have been thrown!");
        } catch (ValidationException e) {
            ValidationResult result = e.getResult();
            assertNotNull(result);
            assertEquals(1, result.getFailures().size());
            ValidationFailure failure = result.getFailures().iterator().next();
            assertEquals("postalAddress.cityOrMunicipality", failure.getFieldKey());
            assertEquals("City" + MISSING_INFORMATION_ERROR, failure.getMessage());
        }
    }

    @Test
    public void testValidate_NoPostalAddressState() throws RemoteException {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.getPostalAddress().setStateOrProvince(null);
        try {
            service.validate(person);
            fail("A Validation Exception should have been thrown!");
        } catch (ValidationException e) {
            ValidationResult result = e.getResult();
            assertNotNull(result);
            assertEquals(1, result.getFailures().size());
            ValidationFailure failure = result.getFailures().iterator().next();
            assertEquals("postalAddress.stateOrProvince", failure.getFieldKey());
            assertEquals("State" + MISSING_INFORMATION_ERROR, failure.getMessage());
        }
    }

    @Test
    public void testValidate_NoPostalAddressPostalCode() throws RemoteException {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.getPostalAddress().setPostalCode(null);
        try {
            service.validate(person);
            fail("A Validation Exception should have been thrown!");
        } catch (ValidationException e) {
            ValidationResult result = e.getResult();
            assertNotNull(result);
            assertEquals(1, result.getFailures().size());
            ValidationFailure failure = result.getFailures().iterator().next();
            assertEquals("postalAddress.postalCode", failure.getFieldKey());
            assertEquals("ZIP Code" + MISSING_INFORMATION_ERROR, failure.getMessage());
        }
    }

    @Test
    public void testValidate_MalformedPhoneNumber() throws RemoteException {
        gov.nih.nci.firebird.data.Person person = PersonFactory.getInstance().create();
        person.setPhoneNumber("123445");
        try {
            service.validate(person);
            fail("A Validation Exception should have been thrown!");
        } catch (ValidationException e) {
            ValidationResult result = e.getResult();
            assertNotNull(result);
            assertEquals(1, result.getFailures().size());
            ValidationFailure failure = result.getFailures().iterator().next();
            assertEquals("phoneNumber", failure.getFieldKey());
            assertEquals(resources.getString("nes.validation.error.phone.number.wrong.digits"), failure.getMessage());
        }
    }
}
