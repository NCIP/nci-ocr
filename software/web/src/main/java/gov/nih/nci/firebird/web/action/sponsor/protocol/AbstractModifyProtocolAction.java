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
package gov.nih.nci.firebird.web.action.sponsor.protocol;

import static org.apache.commons.lang3.StringUtils.*;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.organization.InvalidatedOrganizationException;
import gov.nih.nci.firebird.service.protocol.ProtocolService;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Helper action for protocol management.
 */
@SuppressWarnings("PMD.TooManyMethods")
// broken down for easier readability.
abstract class AbstractModifyProtocolAction extends AbstractProtocolAction {
    private static final long serialVersionUID = 1L;

    private String agentList = "";
    private Organization sponsor;
    private String sponsorExternalId;
    private final Map<String, List<String>> leadOrganizationIdMappings = Maps.newHashMap();
    private final Map<Organization, List<Person>> leadOrganizationMappings = Maps.newHashMap();

    /**
     * @param protocolService the protocol service
     */
    protected AbstractModifyProtocolAction(ProtocolService protocolService) {
        super(protocolService);
    }

    @Override
    public void prepare() {
        super.prepare();
        if (getId(getProtocol()) == null) {
            setProtocol(getProtocolService().create());
        }
        setSponsor(lookupSponsor());
        configureLeadOrganizationMappings();
        agentList = StringUtils.trimToNull(getProtocol().getAgentListForDisplay());
    }

    private void configureLeadOrganizationMappings() {
        if (!leadOrganizationIdMappings.isEmpty()) {
            populateFromSubmittedValues();
        }
    }

    private Organization lookupSponsor() {
        if (!isEmpty(getSponsorExternalId())) {
            return getOrganization(getSponsorExternalId());
        } else {
            return null;
        }
    }

    private void populateFromSubmittedValues() {
        leadOrganizationMappings.clear();
        Iterator<Entry<String, List<String>>> mappingIterator = leadOrganizationIdMappings.entrySet().iterator();
        while (mappingIterator.hasNext()) {
            Entry<String, List<String>> mapping = mappingIterator.next();
            List<Person> investigators = lookupPrincipalInvestigators(mapping.getValue());
            try {
                this.leadOrganizationMappings.put(getOrganizationService().getByExternalId(mapping.getKey()),
                        investigators);
            } catch (InvalidatedOrganizationException e) {
                mappingIterator.remove();
                addActionError(getText("sponsor.protocol.lead.organization.unavailable.organization.error"));
            }
        }
    }

    private List<Person> lookupPrincipalInvestigators(List<String> externalIds) {
        List<Person> investigators = Lists.newArrayList();
        if (!externalIds.isEmpty()) {
            for (String externalId : externalIds) {
                Person investigator = getPerson(externalId);
                if (!investigators.contains(investigator)) {
                    investigators.add(investigator);
                }
            }
        } else {
            addActionError(getText("sponsor.protocol.lead.organization.principal.investigator.required"));
        }
        return investigators;
    }

    /**
     * @return parse agentList into individual agent Names.
     */
    Set<String> getAgentNameSet() {
        Set<String> agentNameSet = new HashSet<String>();
        agentList = StringUtils.trimToEmpty(agentList);
        for (String agentName : Splitter.on(',').omitEmptyStrings().trimResults().split(agentList)) {
            if (agentName.length() <= ProtocolAgent.MAX_AGENT_LENGTH) {
                agentNameSet.add(agentName);
            } else {
                addFieldError("agentList", getText("sponsor.protocol.agents.error", agentName));
            }
        }
        return agentNameSet;
    }

    /**
     * Prepares the Mapping Set when entering into the modification screen for the first time.
     */
    protected void doExistingLeadOrganizationMappingSetup() {
        for (ProtocolLeadOrganization leadOrganization : getProtocol().getLeadOrganizations()) {
            Organization organization = leadOrganization.getOrganization();
            Person principalInvestigator = leadOrganization.getPrincipalInvestigator();
            if (leadOrganizationMappings.containsKey(organization)) {
                leadOrganizationMappings.get(organization).add(principalInvestigator);
            } else {
                leadOrganizationMappings.put(organization, Lists.newArrayList(principalInvestigator));
            }
        }
    }

    /**
     * @return struts forward.
     */
    protected String saveProtocol() {
        getProtocol().setSponsor(getSponsor());
        getProtocol().updateAgents(getAgentNameSet());
        updateLeadOrganizations();
        if (getFieldErrors().size() > 0) {
            return INPUT;
        }
        try {
            performSave();
            return SUCCESS;
        } catch (ValidationException e) {
            return handleValidationException(e);
        }
    }

    private void updateLeadOrganizations() {
        if (leadOrganizationIdMappings.isEmpty()) {
            getProtocol().getLeadOrganizations().clear();
        } else {
            removeLeadOrganizationsFromProtocol();
            addNewLeadOrganizations();
        }
    }

    private void removeLeadOrganizationsFromProtocol() {
        Iterator<ProtocolLeadOrganization> leadOrganizationIterator = getProtocol().getLeadOrganizations().iterator();
        while (leadOrganizationIterator.hasNext()) {
            if (!checkLeadOrganizationSelected(leadOrganizationIterator.next())) {
                leadOrganizationIterator.remove();
            }
        }
    }

    private boolean checkLeadOrganizationSelected(ProtocolLeadOrganization leadOrganization) {
        String organizationExternalId = leadOrganization.getOrganization().getExternalId();
        String investigatorExternalId = leadOrganization.getPrincipalInvestigator().getExternalId();
        return leadOrganizationIdMappings.containsKey(organizationExternalId)
                && leadOrganizationIdMappings.get(organizationExternalId).contains(investigatorExternalId);
    }

    private void addNewLeadOrganizations() {
        for (Entry<String, List<String>> leadOrganizationIdMapping : leadOrganizationIdMappings.entrySet()) {
            Organization organization = getOrganizationWithId(leadOrganizationIdMapping.getKey());
            for (String personKey : leadOrganizationIdMapping.getValue()) {
                Person principalInvestigator = getInvestigatorWithId(organization, personKey);
                if (!getProtocol().hasExistingLeadOrganization(organization, principalInvestigator)) {
                    getProtocol().addLeadOrganization(organization, principalInvestigator);
                }
            }
        }
    }

    private Organization getOrganizationWithId(final String id) {
        return Iterables.find(leadOrganizationMappings.keySet(), new Predicate<Organization>() {
            public boolean apply(Organization organization) {
                return id.equals(organization.getExternalId());
            }
        });
    }

    private Person getInvestigatorWithId(Organization organization, final String id) {
        return Iterables.find(leadOrganizationMappings.get(organization), new Predicate<Person>() {
            public boolean apply(Person person) {
                return id.equals(person.getExternalId());
            }
        });
    }

    /**
     * @throws ValidationException if Validation errors exist.
     */
    protected abstract void performSave() throws ValidationException;

    public void setAgentList(String agentList) {
        this.agentList = agentList;
    }

    public String getAgentList() {
        return agentList;
    }

    public String getSponsorExternalId() {
        return sponsorExternalId;
    }

    public void setSponsorExternalId(String sponsorExternalId) {
        this.sponsorExternalId = sponsorExternalId;
    }

    public Organization getSponsor() {
        return sponsor;
    }

    void setSponsor(Organization sponsor) {
        this.sponsor = sponsor;
    }

    /**
     * @return The Map identifying the lead organization's external ID to the principal investigator's external ID.
     */
    public Map<String, List<String>> getLeadOrganizationIdMappings() {
        return leadOrganizationIdMappings;
    }

    /**
     * @return The mapping of lead organizations to primary investigators
     */
    public Map<Organization, List<Person>> getLeadOrganizationMappings() {
        return leadOrganizationMappings;
    }

}
