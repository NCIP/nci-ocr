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
package gov.nih.nci.firebird.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import org.apache.commons.lang.text.StrBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * Investigation Protocol.
 */
@Entity
@Table(name = Protocol.PROTOCOL_STRING, uniqueConstraints = { @UniqueConstraint(columnNames = { "protocol_number",
                                                                                                "sponsor_id" }) })
@SuppressWarnings({ "PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods" }) 
// literals appear in warning suppressions, class exposes mostly getters and setters
public final class Protocol implements PersistentObject, Cloneable {

    private static final long serialVersionUID = 1L;
    static final String PROTOCOL_STRING = "protocol"; // for PMD

    /**
     * Maximum allowable length for protocol number field.
     */
    public static final int MAX_PROTOCOL_NUM_SIZE = 200;

    /**
     * Maximum allowable length for protocol title field.
     */
    public static final int MAX_TITLE_LENGTH = 4000;

    /**
     * sort protocols by protocol number.
     */
    public static final Comparator<Protocol> PROTOCOL_NUMBER_COMPARATOR = new ProtocolNumberComparator();

    private Long id;
    private String protocolNumber;
    private String protocolTitle;
    private Organization sponsor;
    private Set<ProtocolLeadOrganization> leadOrganizations = Sets.newHashSet();
    private ProtocolPhase phase;
    private SortedSet<ProtocolAgent> agents = new TreeSet<ProtocolAgent>();
    private Set<FirebirdFile> documents = new HashSet<FirebirdFile>();
    private ProtocolRegistrationConfiguration registrationConfiguration = new ProtocolRegistrationConfiguration();
    private Set<AbstractProtocolRegistration> registrations = new HashSet<AbstractProtocolRegistration>();
    private List<ProtocolRevision> revisionHistory = new ArrayList<ProtocolRevision>();
    private Date lastUpdate;

    /**
     * Instantiates a new Protocol. This constructor should only be used by Struts and
     * Hibernate frameworks -- client code should use the <code>create</code> method
     * below.
     */
    public Protocol() {
        // empty constructor body
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return investigational agents used in this protocol.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "protocol_protocolagent", joinColumns = @JoinColumn(name = "protocol_id"),
               inverseJoinColumns = @JoinColumn(name = "protocolagent_id"))
    @ForeignKey(name = "protocol_fkey", inverseName = "protocolagent_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL })
    @Sort(type = SortType.NATURAL)
    public SortedSet<ProtocolAgent> getAgents() {
        return agents;
    }

    @SuppressWarnings("unused")     // required by Hibernate
    private void setAgents(SortedSet<ProtocolAgent> agents) {
        this.agents = agents;
    }

    /**
     * @return a comma-separated list of the protocol's agents
     */
    @Transient
    public String getAgentListForDisplay() {
        StrBuilder builder = new StrBuilder();
        for (ProtocolAgent agent : getAgents()) {
            builder.appendSeparator(", ");
            builder.append(agent.getName());
        }
        return builder.toString();
    }

    /**
     * @return files documenting this protocol.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "protocol_firebirdfile", joinColumns = @JoinColumn(name = PROTOCOL_STRING),
               inverseJoinColumns = @JoinColumn(name = "firebirdfile"))
    @ForeignKey(name = "protocol_fkey", inverseName = "firebirdfile_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public Set<FirebirdFile> getDocuments() {
        return documents;
    }

    @SuppressWarnings("unused")     // required by Hibernate
    private void setDocuments(Set<FirebirdFile> documents) {
        this.documents = documents;
    }

    /**
     * @return the trial phase.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = ProtocolPhase.MAX_LENGTH)
    @NotNull
    public ProtocolPhase getPhase() {
        return phase;
    }

    /**
     * @param phase trial phase.
     */
    public void setPhase(ProtocolPhase phase) {
        this.phase = phase;
    }

    /**
     * @return protocol number.
     */
    @NotEmpty
    @Column(name = "protocol_number")
    @Length(min = 1, max = MAX_PROTOCOL_NUM_SIZE)
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * @param protocolNumber protocol number.
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    /**
     * @return protocol title.
     */
    @NotNull
    @Column(name = "protocol_title")
    @Length(min = 1, max = MAX_TITLE_LENGTH)
    public String getProtocolTitle() {
        return protocolTitle;
    }

    /**
     * @param protocolTitle protocol title.
     */
    public void setProtocolTitle(String protocolTitle) {
        this.protocolTitle = protocolTitle;
    }

    /**
     * @return sponsor organization.
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "sponsor_id")
    @ForeignKey(name = "protocol_sponsor_fkey")
    @NotNull
    public Organization getSponsor() {
        return sponsor;
    }

    /**
     * @param sponsor sponsor org.
     */
    public void setSponsor(Organization sponsor) {
        this.sponsor = sponsor;
    }

    /**
     * @return the Set of Lead Organizations.
     */
    @OneToMany(mappedBy = "protocol", fetch = FetchType.LAZY)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public Set<ProtocolLeadOrganization> getLeadOrganizations() {
        return leadOrganizations;
    }

    /**
     * @param leadOrganizations the Lead Organizations to set.
     */
    public void setLeadOrganizations(Set<ProtocolLeadOrganization> leadOrganizations) {
        this.leadOrganizations = leadOrganizations;
    }

    /**
     * Add a new Lead Organization to the Protocol.
     *
     * @param organization The Organization.
     * @param principalInvestigator The Principal Investigator person.
     * @return The new LeadOrganization object.
     */
    public ProtocolLeadOrganization addLeadOrganization(Organization organization, Person principalInvestigator) {
        Preconditions.checkArgument(!hasExistingLeadOrganization(organization, principalInvestigator),
                "Lead Organization organizations must be unique!");

        ProtocolLeadOrganization leadOrganization = new ProtocolLeadOrganization(this, organization,
                principalInvestigator);
        getLeadOrganizations().add(leadOrganization);
        return leadOrganization;
    }

    /**
     * Checks to see if this protocol already contains an equivalent Lead organization.
     *
     *
     * @param organization The Organization
     * @param principalInvestigator The Principal Investigator
     * @return if there is already a Lead organization in this protocol that matches.
     */
    public boolean hasExistingLeadOrganization(Organization organization, Person principalInvestigator) {
        return getLeadOrganizations().contains(new ProtocolLeadOrganization(this, organization, principalInvestigator));
    }

    /**
     * @return the registrationConfiguration
     */
    @Embedded
    public ProtocolRegistrationConfiguration getRegistrationConfiguration() {
        return registrationConfiguration;
    }

    void setRegistrationConfiguration(ProtocolRegistrationConfiguration registrationConfiguration) {
        this.registrationConfiguration = registrationConfiguration;
    }

    /**
     * Updates the set of <code>ProtocolAgents</code> associated with this protocol. Any new agents will be added and
     * any existing agents not in the list of names will be removed.
     *
     * @param agentNames contains the complete set of agent names for agents to be associated to this protocol.
     */
    public void updateAgents(Set<String> agentNames) {
        Set<String> newAgentNames = Sets.newHashSet(agentNames);
        Set<ProtocolAgent> removedAgents = Sets.newHashSet();
        for (ProtocolAgent agent : getAgents()) {
            if (agentNames.contains(agent.getName())) {
                newAgentNames.remove(agent.getName());
            } else {
                removedAgents.add(agent);
            }
        }
        addAgents(newAgentNames);
        getAgents().removeAll(removedAgents);
    }

    private void addAgents(Set<String> newAgentNames) {
        for (String agentName : newAgentNames) {
            getAgents().add(new ProtocolAgent(agentName));
        }
    }

    @OneToMany(mappedBy = PROTOCOL_STRING, fetch = FetchType.LAZY)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    private Set<AbstractProtocolRegistration> getRegistrationsInternal() {
        return registrations;
    }

    @SuppressWarnings("unused")     // required by Hibernate
    private void setRegistrationsInternal(Set<AbstractProtocolRegistration> investigatorRegistrationsInternal) {
        this.registrations = investigatorRegistrationsInternal;
    }

    /**
     * @param registration the registration to add to this protocol.
     */
    public void addRegistration(AbstractProtocolRegistration registration) {
        registration.setProtocol(this);
        registration.configureForms();
        getRegistrationsInternal().add(registration);
    }

    /**
     * @return current investigator registrations (aren't revised).
     */
    @Transient
    public Set<InvestigatorRegistration> getCurrentInvestigatorRegistrations() {
        return ImmutableSet.copyOf(Iterables.filter(getAllInvestigatorRegistrations(),
                new Predicate<InvestigatorRegistration>() {
                    @Override
                    public boolean apply(InvestigatorRegistration registration) {
                        return !(registration instanceof RevisedInvestigatorRegistration);
                    }
                }));
    }

    /**
     * @return all investigator registrations (current and revised).
     */
    @Transient
    public Set<InvestigatorRegistration> getAllInvestigatorRegistrations() {
        return ImmutableSet.copyOf(Iterables.filter(getRegistrationsInternal(), InvestigatorRegistration.class));
    }

    /**
     * @return all revised investigator registrations.
     */
    @Transient
    public Set<RevisedInvestigatorRegistration> getRevisedInvestigatorRegistrations() {
        return ImmutableSet.copyOf(Iterables.filter(getRegistrationsInternal(), RevisedInvestigatorRegistration.class));
    }

    /**
     * @return subinvestigator registrations.
     */
    @Transient
    public Set<SubInvestigatorRegistration> getSubinvestigatorRegistrations() {
        return FluentIterable.from(getRegistrationsInternal()).filter(SubInvestigatorRegistration.class)
                .toImmutableSet();
    }

    /**
     * @return active subinvestigator registrations.
     */
    @Transient
    public Set<SubInvestigatorRegistration> getActiveSubinvestigatorRegistrations() {
        return FluentIterable
                .from(getSubinvestigatorRegistrations())
                .filter(Predicates.and(AbstractProtocolRegistration.IS_INVITED_PREDICATE,
                        SubInvestigatorRegistration.IS_ACTIVE_PREDICATE)).toImmutableSet();
    }

    /**
     * @return an unmodifiable set of all registrations (both primary and subinvestigators).
     */
    @Transient
    public Set<AbstractProtocolRegistration> getAllRegistrations() {
        return Collections.unmodifiableSet(getRegistrationsInternal());
    }

    /**
     * @return changes made to this protocol.
     */
    @OneToMany(mappedBy = PROTOCOL_STRING, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @OrderBy("date DESC")
    private List<ProtocolRevision> getRevisionHistoryInternal() {
        return revisionHistory;
    }

    @SuppressWarnings("unused")     // required by Hibernate
    private void setRevisionHistoryInternal(List<ProtocolRevision> revisionHistoryInternal) {
        this.revisionHistory = revisionHistoryInternal;
    }

    /**
     * @return changes made to this protocol in descending order by date.
     */
    @Transient
    public List<ProtocolRevision> getRevisionHistory() {
        return Collections.unmodifiableList(getRevisionHistoryInternal());
    }

    /**
     * @return the lastUpdated
     */
    @Column(name = "last_update")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastUpdate() {
        if (lastUpdate != null) {
            return (Date) lastUpdate.clone();
        }
        return null;
    }

    /**
     * @param lastUpdate the lastUpdated to set
     */
    private void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return a clone to be used for state change comparison.
     */
    @SuppressWarnings("PMD.ProperCloneImplementation")
    // PMD, this is how I want to clone a protocol.
    @Override
    public Protocol clone() {
        Protocol copy = new Protocol();
        copy.getAgents().addAll(getAgents());
        copy.getDocuments().addAll(getDocuments());
        copy.setId(getId());
        copy.setPhase(getPhase());
        copy.setProtocolNumber(getProtocolNumber());
        copy.setProtocolTitle(getProtocolTitle());
        ProtocolRegistrationConfiguration configuration = copy.getRegistrationConfiguration();
        configuration.getInvestigatorConfiguration().setFormOptionalities(new HashMap<FormType, FormOptionality>(
                getRegistrationConfiguration().getInvestigatorConfiguration().getFormOptionalities()));
        configuration.getSubinvestigatorConfiguration().setFormOptionalities(new HashMap<FormType, FormOptionality>(
                getRegistrationConfiguration().getSubinvestigatorConfiguration().getFormOptionalities()));
        copy.setSponsor(getSponsor());
        copyLeadOrganizations(copy);
        return copy;
    }

    private void copyLeadOrganizations(Protocol copy) {
        for (ProtocolLeadOrganization leadOrganization : leadOrganizations) {
            copy.addLeadOrganization(leadOrganization.getOrganization(), leadOrganization.getPrincipalInvestigator());
        }
    }

    /**
     * @param revision add this revision to the history.
     */
    public void addRevision(ProtocolRevision revision) {
        revision.setProtocol(this);
        getRevisionHistoryInternal().add(revision);
        setLastUpdate(revision.getDate());
    }

    /**
     * Removes a registration association from a protocol.
     * 
     * @param registration the registration to remove from this protocol
     */
    public void removeRegistration(AbstractProtocolRegistration registration) {
        registration.setProtocol(null);
        getRegistrationsInternal().remove(registration);
    }

    /**
     * comparator for ordering registrations by protocol number.
     */
    private static final class ProtocolNumberComparator implements Comparator<Protocol> {
        @Override
        public int compare(Protocol p1, Protocol p2) {
            return p1.getProtocolNumber().compareTo(p2.getProtocolNumber());
        }
    }

}
