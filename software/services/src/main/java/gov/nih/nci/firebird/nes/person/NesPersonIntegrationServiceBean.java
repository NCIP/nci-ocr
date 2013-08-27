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

import gov.nih.nci.coppa.common.LimitOffset;
import gov.nih.nci.coppa.common.faults.TooManyResultsFault;
import gov.nih.nci.coppa.po.IdentifiedPerson;
import gov.nih.nci.coppa.po.StringMap;
import gov.nih.nci.coppa.po.faults.NullifiedEntityFault;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.AbstractBaseNesService;
import gov.nih.nci.firebird.nes.common.NesTranslatorHelperUtils;
import gov.nih.nci.firebird.nes.common.ReplacedEntityException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.nes.common.ValidationErrorTranslator;
import gov.nih.nci.iso21090.extensions.Id;

import java.rmi.RemoteException;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.iso._21090.II;

import com.google.inject.Inject;

/**
 * The implementation of the NesPersonIntegrationService that connects to NES.
 */
@SuppressWarnings("PMD.TooManyMethods") // private helper methods broken out for readability
public class NesPersonIntegrationServiceBean extends AbstractBaseNesService implements NesPersonIntegrationService {

    private static final String CTEP_ROOT = "2.16.840.1.113883.3.26.6.1";
    static final char[] INVALID_EMAIL_ADDRESS_CHARACTERS = {'%', '#', '^', '`', '{', '}', '|' };

    private final PersonI personClient;
    private final IdentifiedPersonI identifiedPersonClient;
    private final ValidationErrorTranslator errorTranslator;

    /**
     * Build the integration service bean.
     *
     * @param personClient the person client.
     * @param identifiedPersonClient the NES IdentifiedPerson client.
     * @param errorTranslator the resource bundle for the services.
     */
    @Inject
    public NesPersonIntegrationServiceBean(PersonI personClient, IdentifiedPersonI identifiedPersonClient,
            ValidationErrorTranslator errorTranslator) {
        this.personClient = personClient;
        this.identifiedPersonClient = identifiedPersonClient;
        this.errorTranslator = errorTranslator;
    }

    @Override
    public Person[] searchByName(String firstName, String lastName) {
        gov.nih.nci.coppa.po.Person example = PersonTranslator.buildNesPerson(firstName, lastName);
        return search(example);
    }

    private Person[] search(gov.nih.nci.coppa.po.Person example) {
        LimitOffset limitOffset = createLimitOffset();
        gov.nih.nci.coppa.po.Person[] results = new gov.nih.nci.coppa.po.Person[0];
        try {
            results = personClient.query(example, limitOffset);
        } catch (TooManyResultsFault e) {
            handleUnexpectedError(e);
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
        return translateResults(results);
    }

    private Person[] translateResults(gov.nih.nci.coppa.po.Person[] results) {
        if (results == null) {
            return new Person[0];
        }
        return PersonTranslator.buildFirebirdPeople(results);
    }

    @Override
    public Person[] searchByEmail(String email) {
        if (StringUtils.containsAny(email, INVALID_EMAIL_ADDRESS_CHARACTERS)) {
            return new Person[0];
        }
        Person searchPerson = new Person();
        searchPerson.setEmail(email);
        searchPerson.setPostalAddress(null);
        gov.nih.nci.coppa.po.Person example = PersonTranslator.buildNesPerson(searchPerson);
        return search(example);
    }

    @Override
    public Person getById(String nesId) throws UnavailableEntityException, ReplacedEntityException {
        Person person = null;
        try {
            gov.nih.nci.coppa.po.Person result = personClient.getById(PersonTranslator.buildId(nesId));
            person = PersonTranslator.buildFirebirdPerson(result);
            if (person != null) {
                person.setCtepId(getCtepId(nesId));
            }
        } catch (NullifiedEntityFault e) {
            handleNullifiedPerson(e, nesId);
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
        return person;
    }

    private void handleNullifiedPerson(NullifiedEntityFault e, String nullifiedId)
            throws UnavailableEntityException, ReplacedEntityException {
        String correctedNesId = getCorrectedNesIdExtension(e, nullifiedId);
        if (correctedNesId == null) {
            getLog().warn("Person with NES ID " + nullifiedId + " has been nullified with no replacement indicated");
            throw new UnavailableEntityException(e, nullifiedId);
        } else if (correctedNesId.equals(nullifiedId)) {
            getLog().warn("Person with NES ID " + nullifiedId
                    + " has been nullified but the replacement indicates the same id");
            throw new UnavailableEntityException(e, nullifiedId);
        } else {
            getLog().warn("Person with NES ID " + nullifiedId
                    + " has been nullified and replaced by person with id " + correctedNesId);
            throw new ReplacedEntityException(e, nullifiedId, correctedNesId);
        }
    }

    @Override
    public String createPerson(Person person) {
        gov.nih.nci.coppa.po.Person nesPerson = PersonTranslator.buildNesPerson(person);

        Id result = null;
        String nesId = null;

        try {
            result = personClient.create(nesPerson);
            nesId = result.getExtension();
            person.setLastNesRefresh(new Date());
            person.setNesStatus(CurationStatus.PENDING);
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
        return nesId;
    }

    @Override
    public CurationStatus updatePerson(Person p) {
        gov.nih.nci.coppa.po.Person nesPerson = null;
        CurationStatus status = p.getNesStatus();
        try {
            /*
             * The NES Update service fails if the Person that is sent has a status different than the Person that they
             * have. For this reason, we are querying them for the current record that they have so that we can get the
             * current status to send back to them.
             */
            gov.nih.nci.coppa.po.Person existingPerson = personClient.getById(PersonTranslator.buildId(p.getNesId()));
            //TODO need to catch an exception for if NES has flagged this as duplicate and remap to the active record.
            if (existingPerson != null) {
                nesPerson = PersonTranslator.buildNesPerson(p);
                nesPerson.setStatusCode(existingPerson.getStatusCode());
                personClient.update(nesPerson);
                status = NesTranslatorHelperUtils.handleCurationStatus(existingPerson.getStatusCode());
            } else {
                throw new IllegalArgumentException("No Pre-existing NES record exists to be updated for this person");
            }
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
        return status;
    }

    @Override
    public void validate(Person person) throws ValidationException {
        gov.nih.nci.coppa.po.Person nesPerson = PersonTranslator.buildNesPerson(person);

        try {
            StringMap validateResults = personClient.validate(nesPerson);
            if (!validateResults.getEntry().isEmpty()) {
                ValidationResult validationResult = errorTranslator.translateStringMapValidation(
                        validateResults);
                throw new ValidationException(validationResult);
            }
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
    }

    @Override
    public String getCtepId(String nesId) {
        IdentifiedPerson searchIdentifiedPerson = createCtepIdentifiedPersonForNesId(nesId);
        IdentifiedPerson identifiedPerson = searchForIdentifiedPerson(searchIdentifiedPerson);
        if (identifiedPerson != null) {
            return identifiedPerson.getAssignedId().getExtension();
        } else {
            return null;
        }
    }

    private IdentifiedPerson searchForIdentifiedPerson(IdentifiedPerson searchIdentifiedPerson) {
        IdentifiedPerson identifiedPerson = null;
        try {
            IdentifiedPerson[] identifiedPersons = identifiedPersonClient.query(searchIdentifiedPerson,
                    createLimitOffset());
            if (!ArrayUtils.isEmpty(identifiedPersons)) {
                identifiedPerson = identifiedPersons[0];
            }
        } catch (TooManyResultsFault e) {
            handleUnexpectedError(e);
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
        return identifiedPerson;
    }

    private IdentifiedPerson createCtepIdentifiedPersonForNesId(String nesId) {
        II assignedId = new II();
        assignedId.setRoot(CTEP_ROOT);
        IdentifiedPerson searchIdentifiedPerson = new IdentifiedPerson();
        searchIdentifiedPerson.setAssignedId(assignedId);
        searchIdentifiedPerson.setPlayerIdentifier(PersonTranslator.buildIi(nesId));
        return searchIdentifiedPerson;
    }
    
    @Override
    public Person getByCtepId(String ctepId) throws UnavailableEntityException {
        IdentifiedPerson searchIdentifiedPerson = createCtepIdentifiedPersonForCtepId(ctepId);
        IdentifiedPerson result = searchForIdentifiedPerson(searchIdentifiedPerson);
        if (result != null) {
            return getPersonOrReplacement(result.getPlayerIdentifier().getExtension());
        } else {
            getLog().warn("Couldn't find Person with CTEP ID: " + ctepId);
            return null;
        }
    }

    private Person getPersonOrReplacement(final String nesId) throws UnavailableEntityException {
        try {
            return getById(nesId);
        } catch (ReplacedEntityException e) {
            return getPersonOrReplacement(e.getReplacmentNesId());
        }
    }

    private IdentifiedPerson createCtepIdentifiedPersonForCtepId(String ctepId) {
        II assignedId = new II();
        assignedId.setRoot(CTEP_ROOT);
        assignedId.setExtension(ctepId);
        IdentifiedPerson searchIdentifiedPerson = new IdentifiedPerson();
        searchIdentifiedPerson.setAssignedId(assignedId);
        return searchIdentifiedPerson;
    }

}