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

import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.person.external.InvalidatedPersonException;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Session;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Bean that implements validation and import of individual protocols.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@SuppressWarnings("PMD.TooManyMethods")
// methods broken down for clarity
public class ProtocolImportDetailServiceBean implements ProtocolImportDetailService {
    private PersonService personService;
    private ProtocolService protocolService;
    private OrganizationService organizationService;
    private ResourceBundle resources;
    private Provider<Session> sessionProvider;
    private SessionContext sessionContext;
    private final Map<String, Person> lookedUpPersons = Maps.newHashMap();

    @Resource(mappedName = "firebird/PersonServiceBean/local")
    void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Resource(mappedName = "firebird/ProtocolServiceBean/local")
    void setProtocolService(ProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    @Resource(mappedName = "firebird/OrganizationServiceBean/local")
    void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Inject
    void setResources(ResourceBundle resources) {
        this.resources = resources;
    }

    @Resource
    void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    @Inject
    void setSessionProvider(Provider<Session> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Override
    public void validate(ProtocolImportDetail detail) {
        lookedUpPersons.clear();
        detail.setStatus(ProtocolImportDetailStatus.VALIDATING);
        checkForDuplicateProtocolNumber(detail);
        handleLeadOrganizations(detail);
        checkInvestigators(detail);
        setFinalValidationStatus(detail);
    }

    private void checkForDuplicateProtocolNumber(ProtocolImportDetail detail) {
        if (protocolService.hasDuplicateProtocolNumber(detail.getProtocol())) {
            detail.addFailure(resources, "validation.failure.protocol.import.duplicate.number",
                    ProtocolImportJob.PROTOCOL_NUMBER_COLUMN_INDEX, detail.getProtocol().getProtocolNumber());
        }
    }

    private void handleLeadOrganizations(ProtocolImportDetail detail) {
        if (!detail.getLeadOrganizationMappings().isEmpty()) {

            Map<String, Organization> organizations = lookupMappedOrganizations(detail);
            Map<String, Person> principalInvestigators = lookupMappedInvestigators(detail);

            for (Map.Entry<String, String> mapping : detail.getLeadOrganizationMappings().entrySet()) {
                if (validateMapping(mapping, detail)) {
                    Organization leadOrganization = organizations.get(mapping.getKey());
                    Person principalInvestigator = principalInvestigators.get(mapping.getValue());
                    addLeadOrganization(detail, leadOrganization, principalInvestigator);
                }
            }
        }
    }

    private void addLeadOrganization(ProtocolImportDetail detail, Organization leadOrganization,
            Person principalInvestigator) {
        if (leadOrganization != null && principalInvestigator != null) {
            detail.getProtocol().addLeadOrganization(leadOrganization, principalInvestigator);
        }
    }

    private boolean validateMapping(Map.Entry<String, String> mapping, ProtocolImportDetail detail) {
        if (StringUtils.isBlank(mapping.getKey()) || StringUtils.isBlank(mapping.getValue())) {
            detail.addFailure(resources, "validation.failure.protocol.import.invalid.lead.organization.key",
                    ProtocolImportJob.PROTOCOL_LEAD_ORGANIZATION_COLUMN_INDEX, mapping);
            return false;
        }
        return true;
    }

    private Map<String, Person> lookupMappedInvestigators(ProtocolImportDetail detail) {
        Set<String> principalInvestigatorIds = Sets.newHashSet(detail.getLeadOrganizationMappings().values());
        Map<String, Person> lookedUpInvestigators = Maps.newHashMap();

        for (String externalId : principalInvestigatorIds) {
            if (StringUtils.isNotBlank(externalId)) {
                Person person = retrievePrincipalInvestigator(detail, externalId);
                lookedUpInvestigators.put(externalId, person);
            }
        }

        return lookedUpInvestigators;
    }

    private Person retrievePrincipalInvestigator(ProtocolImportDetail detail, String investigatorExternalId) {
        return retrieveInvestigator(detail, investigatorExternalId,
                ProtocolImportJob.PROTOCOL_LEAD_ORGANIZATION_COLUMN_INDEX);
    }

    private Person retrieveInvestigator(ProtocolImportDetail detail, String investigatorExternalId, int column) {
        if (!NumberUtils.isDigits(investigatorExternalId)) {
            handleInvalidInvestigatorIdFormat(detail, investigatorExternalId, column);
            return null;
        }
        Person investigator = lookedUpPersons.get(investigatorExternalId);
        if (investigator == null) {
            investigator = lookupInvestigator(detail, investigatorExternalId, column);
            lookedUpPersons.put(investigatorExternalId, investigator);
        }
        return investigator;
    }

    private Person lookupInvestigator(ProtocolImportDetail detail, String investigatorExternalId, int column) {
        try {
            Person investigator = personService.getByExternalId(investigatorExternalId);
            if (investigator == null) {
                handleInvalidInvestigatorId(detail, investigatorExternalId, column);
            }
            return investigator;
        } catch (InvalidatedPersonException e) {
            handleInvalidInvestigatorId(detail, investigatorExternalId, column);
        }
        return null;
    }

    private void handleInvalidInvestigatorIdFormat(ProtocolImportDetail detail, String investigatorExternalId,
            int column) {
        detail.addFailure(resources, "validation.failure.protocol.import.invalid.investigator.id.format", column,
                investigatorExternalId);
    }

    private void handleInvalidInvestigatorId(ProtocolImportDetail detail, String investigatorExternalId, int column) {
        detail.addFailure(resources, "validation.failure.protocol.import.invalid.investigator.id", column,
                investigatorExternalId);
    }

    private Map<String, Organization> lookupMappedOrganizations(ProtocolImportDetail detail) {
        Set<String> leadOrganizationIds = detail.getLeadOrganizationMappings().keySet();
        Map<String, Organization> lookedUpOrganizations = Maps.newHashMap();

        for (String ctepId : leadOrganizationIds) {
            if (StringUtils.isNotBlank(ctepId)) {
                lookedUpOrganizations.put(ctepId, retrieveLeadOrganization(detail, ctepId));
            }
        }

        return lookedUpOrganizations;
    }

    private Organization retrieveLeadOrganization(ProtocolImportDetail detail, String ctepId) {
        List<Organization> leadOrganizations = organizationService.getByAlternateIdentifier(ctepId,
                OrganizationRoleType.GENERIC_ORGANIZATION);
        if (leadOrganizations.isEmpty()) {
            handleInvalidLeadOrganizationCtepId(detail, ctepId);
        } else if (leadOrganizations.size() > 1) {
            handleTooManyLeadOrganizationsReturned(detail, ctepId);
        } else {
            return leadOrganizations.get(0);
        }
        return null;
    }

    private void handleInvalidLeadOrganizationCtepId(ProtocolImportDetail detail, String ctepId) {
        detail.addFailure(resources, "validation.failure.protocol.import.invalid.lead.organization.ctep.id",
                ProtocolImportJob.PROTOCOL_LEAD_ORGANIZATION_COLUMN_INDEX, ctepId);
    }

    private void handleTooManyLeadOrganizationsReturned(ProtocolImportDetail detail, String ctepId) {
        detail.addFailure(resources, "validation.failure.protocol.import.too.many.lead.organizations.returned",
                ProtocolImportJob.PROTOCOL_LEAD_ORGANIZATION_COLUMN_INDEX, ctepId);
    }

    private void checkInvestigators(ProtocolImportDetail detail) {
        for (String investigatorExternalId : detail.getInvestigatorExternalIds()) {
            Person investigator = retrieveInvestigator(detail, investigatorExternalId);
            if (investigator != null) {
                detail.getInvestigators().add(investigator);
            }
        }
    }

    private Person retrieveInvestigator(ProtocolImportDetail detail, String investigatorExternalId) {
        return retrieveInvestigator(detail, investigatorExternalId,
                ProtocolImportJob.PROTOCOL_INVESTIGATOR_IDS_COLUMN_INDEX);
    }

    private void setFinalValidationStatus(ProtocolImportDetail detail) {
        if (detail.getValidationResult().isValid()) {
            detail.setStatus(ProtocolImportDetailStatus.VALID);
        } else {
            detail.setStatus(ProtocolImportDetailStatus.INVALID);
        }
    }

    @Override
    public void importProtocol(ProtocolImportDetail detail) {
        lookedUpPersons.clear();
        detail.setStatus(ProtocolImportDetailStatus.IMPORTING);
        List<Person> investigators = retrieveInvestigators(detail);
        if (detail.getValidationResult().isValid()) {
            Protocol protocolToSave = detail.getProtocol().createCopy();
            protocolService.addFormTypes(protocolToSave.getRegistrationConfiguration());
            protocolService.save(protocolToSave);
            addInvestigators(detail, investigators, protocolToSave);
            sessionProvider.get().flush();
            detail.setStatus(ProtocolImportDetailStatus.IMPORT_COMPLETE);
        } else {
            detail.setStatus(ProtocolImportDetailStatus.IMPORT_ERROR);
        }
    }

    private void addInvestigators(ProtocolImportDetail detail, List<Person> investigators, Protocol protocol) {
        for (Person investigator : investigators) {
            addInvestigator(detail, investigator, protocol);
        }
    }

    private void addInvestigator(ProtocolImportDetail detail, Person investigator, Protocol protocol) {
        try {
            protocolService.addInvestigator(protocol, investigator);
        } catch (ValidationException e) {
            detail.getValidationResult().addFailures(e.getResult());
            detail.setStatus(ProtocolImportDetailStatus.IMPORT_ERROR);
            sessionContext.setRollbackOnly();
        }
    }

    private List<Person> retrieveInvestigators(ProtocolImportDetail detail) {
        List<Person> investigators = Lists.newArrayList();
        for (String investigatorExternalId : detail.getInvestigatorExternalIds()) {
            investigators.add(retrieveInvestigator(detail, investigatorExternalId));
        }
        return investigators;
    }

}
