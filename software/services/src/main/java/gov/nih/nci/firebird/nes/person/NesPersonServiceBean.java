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

import static com.google.common.base.Preconditions.*;
import gov.nih.nci.coppa.common.LimitOffset;
import gov.nih.nci.coppa.common.faults.TooManyResultsFault;
import gov.nih.nci.coppa.po.IdentifiedPerson;
import gov.nih.nci.coppa.po.StringMap;
import gov.nih.nci.coppa.po.faults.NullifiedEntityFault;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.CurationDataset;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.AbstractBaseNesService;
import gov.nih.nci.firebird.nes.common.ValidationErrorTranslator;
import gov.nih.nci.firebird.nes.correlation.NesPersonRoleIntegrationService;
import gov.nih.nci.firebird.nes.correlation.PersonRoleType;
import gov.nih.nci.firebird.service.person.external.ExternalPersonService;
import gov.nih.nci.firebird.service.person.external.InvalidatedPersonException;
import gov.nih.nci.iso21090.extensions.Id;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.iso._21090.II;
import org.jboss.ejb3.common.proxy.plugins.async.AsyncUtils;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Provides integration with NES's caGrid Person service.
 */
@SuppressWarnings("PMD.TooManyMethods")
// private helper methods broken out for readability
public class NesPersonServiceBean extends AbstractBaseNesService implements ExternalPersonService {

    private static final String CTEP_ROOT = "2.16.840.1.113883.3.26.6.1";
    private static final char[] INVALID_EMAIL_ADDRESS_CHARACTERS = {'%', '#', '^', '`', '{', '}', '|'};
    private static final Logger LOG = Logger.getLogger(NesPersonServiceBean.class);

    private final PersonI personClient;
    private final IdentifiedPersonI identifiedPersonClient;
    private final ValidationErrorTranslator errorTranslator;
    private final NesPersonRoleIntegrationService nesPersonRoleService;

    /**
     * @param personClient the person client.
     * @param identifiedPersonClient the NES IdentifiedPerson client.
     * @param errorTranslator the resource bundle for the services.
     * @param nesPersonRoleService the NES person role service.
     */
    @Inject
    public NesPersonServiceBean(PersonI personClient, IdentifiedPersonI identifiedPersonClient,
            ValidationErrorTranslator errorTranslator, NesPersonRoleIntegrationService nesPersonRoleService) {
        this.personClient = personClient;
        this.identifiedPersonClient = identifiedPersonClient;
        this.errorTranslator = errorTranslator;
        this.nesPersonRoleService = nesPersonRoleService;
    }

    @Override
    public List<Person> search(String term) {
        Preconditions.checkArgument(StringUtils.isNotBlank(term), "Search term may not be blank");
        List<Person> results;
        if (isEmailSearch(term)) {
            results = searchByEmail(term);
        } else {
            results = searchByCtepId(term);
            if (results.isEmpty()) {
                results = searchByName(term);
            }
        }
        return results;
    }

    private boolean isEmailSearch(String term) {
        return EmailValidator.getInstance().isValid(term);
    }

    private List<Person> searchByEmail(String email) {
        if (StringUtils.containsAny(email, INVALID_EMAIL_ADDRESS_CHARACTERS)) {
            return Collections.emptyList();
        }
        Person searchPerson = new Person();
        searchPerson.setEmail(email);
        searchPerson.setPostalAddress(null);
        gov.nih.nci.coppa.po.Person example = PersonTranslator.buildNesPerson(searchPerson);
        return search(example);
    }

    private List<Person> search(gov.nih.nci.coppa.po.Person example) {
        LimitOffset limitOffset = createLimitOffset();
        gov.nih.nci.coppa.po.Person[] results = new gov.nih.nci.coppa.po.Person[0];
        try {
            results = personClient.query(example, limitOffset);
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
        return PersonTranslator.buildFirebirdPersons(results);
    }

    private List<Person> searchByCtepId(String ctepId) {
        List<Person> results = Collections.emptyList();
        IdentifiedPerson searchIdentifiedPerson = new IdentifiedPerson();
        II ii = new II();
        ii.setRoot(CTEP_ROOT);
        ii.setExtension(ctepId);
        searchIdentifiedPerson.setAssignedId(ii);
        try {
            IdentifiedPerson[] identifiedPersons =
                    identifiedPersonClient.query(searchIdentifiedPerson, createLimitOffset());
            if (identifiedPersons != null && identifiedPersons.length > 0) {
                results = getPersons(identifiedPersons);
            }
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
        return results;
    }

    private List<Person> getPersons(IdentifiedPerson[] identifiedPersons) throws RemoteException {
        List<Person> persons = Lists.newArrayList();
        for (IdentifiedPerson identifiedPerson : identifiedPersons) {
            String nesId = identifiedPerson.getPlayerIdentifier().getExtension();
            try {
                persons.add(getByNesId(nesId));
            } catch (InvalidatedPersonException e) {
                String ctepId = identifiedPerson.getAssignedId().getExtension();
                LOG.warn("Person record with CTEP ID " + ctepId + " was nullified with no replacement");
            }
        }
        return persons;
    }

    private List<Person> searchByName(String term) {
        Iterable<String> nameParts = Splitter.on(',').limit(2).trimResults().split(term);
        String lastName = Iterables.get(nameParts, 0, "");
        String firstName = Iterables.get(nameParts, 1, "");
        gov.nih.nci.coppa.po.Person example = PersonTranslator.buildNesPerson(firstName, lastName);
        return search(example);
    }

    @Override
    public Person getByExternalId(String nesId) throws InvalidatedPersonException {
        Person person = null;
        try {
            person = getByNesId(nesId);
            if (person != null) {
                person.setCtepId(getCtepId(nesId));
            }
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
        return person;
    }

    private Person getByNesId(String nesId) throws RemoteException, InvalidatedPersonException {
        Person person;
        try {
            gov.nih.nci.coppa.po.Person result = personClient.getById(PersonTranslator.buildId(nesId));
            checkState(result != null, "No record exists for NES ID " + nesId);
            person = PersonTranslator.buildFirebirdPerson(result);
        } catch (NullifiedEntityFault e) {
            person = handleNullifiedPerson(e, nesId);
        }
        return person;
    }

    String getCtepId(String nesId) {
        IdentifiedPerson searchIdentifiedPerson = createCtepIdentifiedPersonForNesId(nesId);
        IdentifiedPerson identifiedPerson = searchForIdentifiedPerson(searchIdentifiedPerson);
        if (identifiedPerson != null) {
            return identifiedPerson.getAssignedId().getExtension();
        } else {
            return null;
        }
    }

    private IdentifiedPerson createCtepIdentifiedPersonForNesId(String nesId) {
        II assignedId = new II();
        assignedId.setRoot(CTEP_ROOT);
        IdentifiedPerson searchIdentifiedPerson = new IdentifiedPerson();
        searchIdentifiedPerson.setAssignedId(assignedId);
        searchIdentifiedPerson.setPlayerIdentifier(PersonTranslator.buildIi(nesId));
        return searchIdentifiedPerson;
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

    private Person handleNullifiedPerson(NullifiedEntityFault nullifiedFault, String nullifiedId)
            throws InvalidatedPersonException {
        String correctedNesId = getCorrectedNesIdExtension(nullifiedFault, nullifiedId);
        if (correctedNesId == null) {
            getLog().warn("Person with NES ID " + nullifiedId + " has been nullified with no replacement indicated");
            throw new InvalidatedPersonException();
        } else if (correctedNesId.equals(nullifiedId)) {
            getLog().warn(
                    "Person with NES ID " + nullifiedId
                            + " has been nullified but the replacement indicates the same id");
            throw new InvalidatedPersonException();
        } else {
            getLog().warn(
                    "Person with NES ID " + nullifiedId + " has been nullified and replaced by person with id "
                            + correctedNesId);
            return getByExternalId(correctedNesId);
        }
    }

    @Override
    public void validate(Person person) throws ValidationException {
        gov.nih.nci.coppa.po.Person nesPerson = PersonTranslator.buildNesPerson(person);
        try {
            StringMap validateResults = personClient.validate(nesPerson);
            if (!validateResults.getEntry().isEmpty()) {
                ValidationResult validationResult = errorTranslator.translateStringMapValidation(validateResults);
                throw new ValidationException(validationResult);
            }
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
    }

    @Override
    public void save(Person person) throws ValidationException {
        validate(person);
        if (!person.hasExternalRecord()) {
            create(person);
        } else {
            handleUpdate(person);
        }
    }

    private void create(Person person) {
        try {
            gov.nih.nci.coppa.po.Person nesPerson = PersonTranslator.buildNesPerson(person);
            Id result = personClient.create(nesPerson);
            String nesId = result.getExtension();
            NesPersonData nesPersonData = new NesPersonData();
            nesPersonData.setExternalId(nesId);
            nesPersonData.setLastNesRefresh(new Date());
            person.setExternalData(nesPersonData);
            person.setCurationStatus(CurationStatus.PENDING);
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
    }

    private void handleUpdate(Person person) {
        try {
            Person existingPerson = getByNesId(person.getExternalId());
            existingPerson.setProviderNumber(person.getProviderNumber());
            if (!existingPerson.isEquivalent(person)) {
                update(person, existingPerson);
            }
        } catch (InvalidatedPersonException e) {
            getNesPersonData(person).clearPendingUpdate();
            create(person);
        } catch (RemoteException e) {
            handleUnexpectedError(e);

        }
    }

    private void update(Person person, Person existingPerson) {
        try {
            person.setCurationStatus(existingPerson.getCurationStatus());
            gov.nih.nci.coppa.po.Person nesPerson = PersonTranslator.buildNesPerson(person);
            personClient.update(nesPerson);
            copyPersonFields(existingPerson, person);
            ((NesPersonData) person.getExternalData()).requestUpdate();
        } catch (RemoteException e) {
            handleUnexpectedError(e);
        }
    }

    @Override
    public void refreshIfStale(Person person) {
        if (isStale(person)) {
            refreshNow(person);
        }
    }

    private boolean isStale(Person person) {
        Date lastNesRefresh = getNesPersonData(person).getLastNesRefresh();
        return isStale(lastNesRefresh);
    }

    @Override
    public void refreshNow(Person person) {
        checkArgument(person.getExternalId() != null, "No External ID");
        try {
            NesPersonData nesPersonData = getNesPersonData(person);
            Person externalPerson = getByExternalId(person.getExternalId());
            if (!person.isEquivalent(externalPerson)) {
                nesPersonData.clearPendingUpdate();
            }
            copyPersonFields(externalPerson, person);
            nesPersonData.setLastNesRefresh(new Date());
        } catch (InvalidatedPersonException e) {
            getLog().warn(
                    "Attempted to synchronize an invalidated person (NES ID - " + person.getExternalId()
                            + ").  Creating a new person instead");
            create(person);
            getNesPersonData(person).setUpdateRequested(new Date());
        }
    }

    private NesPersonData getNesPersonData(Person person) {
        return (NesPersonData) person.getExternalData();
    }

    private void copyPersonFields(Person source, Person target) {
        target.copyContactInformation(source);
        target.setCurationStatus(source.getCurationStatus());
        if (!source.getExternalId().equals(target.getExternalId())) {
            target.setExternalData(source.getExternalData());
        }
    }

    @Override
    public void checkCorrelation(Person person, Organization organization, PersonRoleType type)
            throws ValidationException {
        NesPersonRoleIntegrationService asyncProxy = getAsyncNesPersonRoleServiceProxy();
        asyncProxy.ensureCorrelated(person, organization, type);
    }

    NesPersonRoleIntegrationService getAsyncNesPersonRoleServiceProxy() {
        return AsyncUtils.mixinAsync(nesPersonRoleService);
    }

    @Override
    public CurationDataset getPersonsToBeCurated(List<Person> candidates) {
        refreshAll(candidates);
        CurationDataset curationDataset = new CurationDataset(getCurationCsvHeaders(), getCurationDisplayHeaderKeys());
        addPersonsToBeCurated(curationDataset, candidates);
        return curationDataset;
    }

    private void refreshAll(List<Person> persons) {
        for (Person person : persons) {
            refreshIfStale(person);
        }
    }

    private List<String> getCurationCsvHeaders() {
        return Lists.newArrayList(
                getResources().getString("curation.external_id.header"),
                getResources().getString("person.curation.name.header")
        );
    }

    private List<String> getCurationDisplayHeaderKeys() {
        return Lists.newArrayList(
                "label.nes.id",
                "label.name"
        );
    }

    private void addPersonsToBeCurated(CurationDataset curationDataset, List<Person> candidates) {
        for (Person person : candidates) {
            if (requiresCuration(person)) {
                List<String> row = Lists.newArrayList(
                        person.getExternalId(),
                        person.getDisplayNameForList()
                );
                curationDataset.addRow(row);
            }
        }
    }

    private boolean requiresCuration(Person person) {
        return person.getCurationStatus() == CurationStatus.PENDING || person.isUpdatePending();
    }

}
