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
import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.iso21090.extensions.Id;

import java.util.List;

import org.iso._21090.ADXP;
import org.iso._21090.AddressPartType;
import org.iso._21090.CD;
import org.iso._21090.II;
import org.iso._21090.TEL;
import org.iso._21090.TELEmail;
import org.iso._21090.TELPhone;
import org.junit.Test;

public class PersonTranslatorTest {
    private PersonFactory personFactory = PersonFactory.getInstance();
    
    /**
     * Test building the id.
     */
    @Test
    public void testBuildId() {
        Id id = PersonTranslator.buildId(null);
        assertNull(id);

        id = PersonTranslator.buildId("1");
        assertEquals("1", id.getExtension());

        assertNull(PersonTranslator.buildId(null));
    }

    @Test
    public void testHandleTelCommAddresses() throws ValidationException {
        gov.nih.nci.firebird.data.Person p = personFactory.create();
        Person nesPerson = PersonTranslator.buildNesPerson(p);

        TEL newTel = new TEL();
        newTel.setValue("tst:13245654321");
        nesPerson.getTelecomAddress().getItem().add(newTel);
        boolean[] tests = new boolean[3];
        for (TEL t : nesPerson.getTelecomAddress().getItem()) {
            if (t instanceof TELEmail) {
                t.setValue("123546577");
                PersonTranslator.buildFirebirdPerson(nesPerson);
                tests[0] = true;
            } else if (t instanceof TELPhone) {
                t.setValue(null);
                PersonTranslator.buildFirebirdPerson(nesPerson);
                tests[1] = true;
            } else {
                PersonTranslator.buildFirebirdPerson(nesPerson);
                if (!tests[2]) {
                    tests[2] = true;
                }
            }
        }

        if (!(tests[0] && tests[1] && tests[2])) {
            fail("Did not hit all tests: " + tests[0] + " | " + tests[1] + " | " + tests[2]);
        }

        nesPerson.setTelecomAddress(null);
        PersonTranslator.buildFirebirdPerson(nesPerson);
    }

    @Test
    public void testHandlePostalAddresses() throws ValidationException {
        gov.nih.nci.firebird.data.Person person = personFactory.create();
        person.setProviderNumber(null);
        person.getPostalAddress().setDeliveryAddress("Rm 100");

        Person nesPerson = PersonTranslator.buildNesPerson(person);

        for (ADXP ap : nesPerson.getPostalAddress().getPart()) {
            if (AddressPartType.ADL.equals(ap.getType())) {
                assertTrue(person.isEquivalent(PersonTranslator.buildFirebirdPerson(nesPerson)));

                ap.setType(AddressPartType.DAL);
                assertFalse(person.isEquivalent(PersonTranslator.buildFirebirdPerson(nesPerson)));
            }
        }
        nesPerson.setPostalAddress(null);
        assertNull(PersonTranslator.buildFirebirdPerson(nesPerson).getPostalAddress());
    }

    @Test
    public void testBuildFirebirdPersons_Empty() throws Exception {
        List<gov.nih.nci.firebird.data.Person> persons = PersonTranslator.buildFirebirdPersons(null);
        assertTrue(persons.isEmpty());
    }

    @Test
    public void testBuildFirebirdPersons() throws Exception {
        Person[] people = new Person[2];

        gov.nih.nci.firebird.data.Person newPerson = new gov.nih.nci.firebird.data.Person();
        newPerson.setCurationStatus(CurationStatus.ACTIVE);
        newPerson.setPostalAddress(null);
        gov.nih.nci.firebird.data.Person fbPerson1 = personFactory.create();
        fbPerson1.setProviderNumber(null);
        gov.nih.nci.firebird.data.Person fbPerson2 = personFactory.createWithoutExternalData();
        fbPerson2.setProviderNumber(null);
        people[0] = PersonTranslator.buildNesPerson(fbPerson1);
        people[1] = PersonTranslator.buildNesPerson(fbPerson2);

        List<gov.nih.nci.firebird.data.Person> results = PersonTranslator.buildFirebirdPersons(people);
        assertEquals(2, results.size());
        assertTrue(fbPerson1.isEquivalent(results.get(0)));
        assertTrue(fbPerson2.isEquivalent(results.get(1)));
    }
    
    @Test
    public void testBuildFirebirdPerson() {
        Person nesPerson = new Person();
        II ii = new II();
        ii.setRoot(NesIIRoot.PERSON.getRoot());
        ii.setExtension("id");
        nesPerson.setIdentifier(ii);
        CD cd = new CD();
        cd.setCode(CurationStatus.ACTIVE.getCodeValue());
        nesPerson.setStatusCode(cd);
        gov.nih.nci.firebird.data.Person firebirdPerson = PersonTranslator.buildFirebirdPerson(nesPerson);
        NesPersonData nesPersonData = (NesPersonData) firebirdPerson.getExternalData();
        assertEquals(ii.getExtension(), nesPersonData.getExternalId());
        assertNotNull(nesPersonData.getLastNesRefresh());
        assertNull(nesPersonData.getUpdateRequested());
        assertEquals(CurationStatus.ACTIVE, firebirdPerson.getCurationStatus());
    }

}
