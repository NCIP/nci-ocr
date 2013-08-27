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

import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.NesEntityException;
import gov.nih.nci.firebird.nes.common.ReplacedEntityException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.nes.person.NesPersonIntegrationService;
import gov.nih.nci.firebird.service.AbstractGenericServiceBean;
import gov.nih.nci.firebird.service.search.FirebirdAnnotatedBeanSearchCriteria;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Implementation of the person service.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersonServiceBean extends AbstractGenericServiceBean<Person> implements PersonService {
    
    private static final Logger LOG = Logger.getLogger(PersonServiceBean.class);

    private NesPersonIntegrationService nesIntegrationService;
    private Provider<FirebirdAnnotatedBeanSearchCriteria<Person>> criteriaProvider;

    @Inject
    void setNesIntegrationService(final NesPersonIntegrationService nesIntegrationService) {
        this.nesIntegrationService = nesIntegrationService;
    }

    @Inject
    void setCriteriaProvider(Provider<FirebirdAnnotatedBeanSearchCriteria<Person>> criteriaProvider) {
        this.criteriaProvider = criteriaProvider;
    }

    @Override
    public Person importNesPerson(final String nesId) throws UnavailableEntityException {
        Person nesPerson = getPersonFromNes(nesId);

        if (nesPerson != null) {
            Person savedPerson = getByNesId(nesId);
            if (savedPerson != null) {
                copyPersonFields(savedPerson, nesPerson);
                save(savedPerson);
                nesPerson = savedPerson;
            }
            save(nesPerson);
        }

        return nesPerson;
    }

    private Person getPersonFromNes(final String nesId) throws UnavailableEntityException {
        try {
            return nesIntegrationService.getById(nesId);
        } catch (ReplacedEntityException e) {
            return getPersonFromNes(e.getReplacmentNesId());
        }
    }

    private Person getByNesId(final String nesId) {
        FirebirdAnnotatedBeanSearchCriteria<Person> criteria = criteriaProvider.get();
        criteria.getCriteria().setNesId(nesId);
        return Iterables.getOnlyElement(search(criteria), null);
    }

    /*
     * Used for copying the fields from the imported Nes Person onto the person that is already
     * saved in the database.
     *
     * @param exists the person from the db
     * @param retrieved person from NES
     * @return if there were any updates besides NES Status
     */
    boolean copyPersonFields(final Person target, final Person source) {
        boolean isUpdateNecessary = !source.isEquivalent(target);
        if (isUpdateNecessary) {
            target.setFirstName(source.getFirstName());
            target.setLastName(source.getLastName());
            target.setMiddleName(source.getMiddleName());
            target.setPrefix(source.getPrefix());
            target.setSuffix(source.getSuffix());
            target.setNesId(source.getNesId());
            target.setNesStatus(source.getNesStatus());
            target.setEmail(source.getEmail());
            target.setPhoneNumber(source.getPhoneNumber());
            target.getPostalAddress().copyFrom(source.getPostalAddress());
        }
        return isUpdateNecessary;
    }

    @Override
    public void refreshFromNes(Person person) {
        if (person.getNesId() == null) {
            throw new IllegalArgumentException("No NES ID");
        }
        Person nesPerson;
        try {
            nesPerson = nesIntegrationService.getById(person.getNesId());
            if (!person.isEquivalent(nesPerson)) {
                person.clearPendingUpdate();
            }
            copyPersonFields(person, nesPerson);
        } catch (NesEntityException e) {
            person.setNesStatus(CurationStatus.NULLIFIED);
        }
        person.setLastNesRefresh(new Date());
        save(person);
    }

    @Override
    public Person createNesPerson(Person person) throws ValidationException {
        validatePerson(person);
        String id = nesIntegrationService.createPerson(person);

        person.setNesId(id);
        save(person);

        return person;
    }

    @Override
    public void updateNesPerson(Person person) throws ValidationException {
        validatePerson(person);

        CurationStatus newStatus = nesIntegrationService.updatePerson(person);

        Person savedPerson = getById(person.getId());
        savedPerson.setNesStatus(newStatus);
        savedPerson.setProviderNumber(person.getProviderNumber());
        updateLocalPerson(savedPerson);
    }

    private void updateLocalPerson(Person person) {
        person.requestUpdate();
        save(person);
    }

    @Override
    public void validatePerson(Person person) throws ValidationException {
        nesIntegrationService.validate(person);
    }

    @SuppressWarnings("unchecked")
    //Hibernate does not provide typed results
    @Override
    public List<Person> getPersonsToBeCurated() {
        return getSessionProvider()
                .get()
                .createCriteria(Person.class)
                .add(Restrictions.or(Restrictions.eq("nesStatus", CurationStatus.PENDING),
                        Restrictions.isNotNull("updateRequested"))).list();
    }
    
    @Override
    public Person getByCtepId(String ctepId) {
        Person person = getByCtepIdLocal(ctepId);
        if (person == null) {
            try {
                return nesIntegrationService.getByCtepId(ctepId);
            } catch (UnavailableEntityException e) {
                LOG.warn("Couldn't retrieve Person for CTEP ID: " + ctepId, e);
                return null;
            }
        }
        return person;
    }

    private Person getByCtepIdLocal(String ctepId) {
        FirebirdAnnotatedBeanSearchCriteria<Person> criteria = criteriaProvider.get();
        criteria.getCriteria().setCtepId(ctepId);
        return Iterables.getOnlyElement(search(criteria), null);
    }
}
