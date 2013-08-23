package gov.nih.nci.firebird.data;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Class that represents the Lead Organization and Principal Investigator of a Protocol.
 */
@Entity
@Table(name = "protocol_lead_organization")
public class ProtocolLeadOrganization implements PersistentObject {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Organization organization;
    private Person principalInvestigator;
    private Protocol protocol;

    @SuppressWarnings("unused")
    private ProtocolLeadOrganization() {
        //No-op for Hibernate
    }

    /**
     * @param protocol The Protocol which this is a part of.
     * @param organization The Lead organization
     * @param principalInvestigator The Principal Investigator
     */
    public ProtocolLeadOrganization(Protocol protocol, Organization organization, Person principalInvestigator) {
        this.organization = organization;
        this.protocol = protocol;
        this.principalInvestigator = principalInvestigator;
    }

    /**
     * @return the id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id .
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the Lead Organization.
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "organization_id")
    @ForeignKey(name = "lead_organization_to_organization_fkey")
    @Valid
    @NotNull
    public Organization getOrganization() {
        return organization;
    }

    /**
     * @param organization the lead organization.
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /**
     * @return the Principal investigator
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "person_id")
    @ForeignKey(name = "lead_organization_to_person_fkey")
    @NotNull
    @Valid
    public Person getPrincipalInvestigator() {
        return principalInvestigator;
    }

    /**
     * @param principalInvestigator .
     */
    public void setPrincipalInvestigator(Person principalInvestigator) {
        this.principalInvestigator = principalInvestigator;
    }

    /**
     * @return the protocol.
     */
    @ManyToOne(optional = false)
    @ForeignKey(name = "lead_organization_to_protocol_fkey")
    @NotNull
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * @param protocol .
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof ProtocolLeadOrganization)) {
            return false;
        }
        ProtocolLeadOrganization other = (ProtocolLeadOrganization) otherObject;
        return new EqualsBuilder().append(getOrganization(), other.getOrganization())
                .append(getPrincipalInvestigator(), other.getPrincipalInvestigator()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getOrganization()).append(getPrincipalInvestigator()).toHashCode();
    }
}
