package gov.nih.nci.firebird.service.protocol;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.inject.Inject;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.PersistentObjectUtil;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.service.AbstractGenericServiceBean;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.service.sponsor.SponsorNotificationService;
import org.hibernate.Query;

import javax.annotation.Resource;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 */

/**
 * Abstract Protocol Service.
 * @param <T> T extends PersistentObject
 */
public abstract class AbstractProtocolServiceBean<T extends PersistentObject>
        extends AbstractGenericServiceBean<T> {

    static final String HQL_FROM_BASE = "from ";

    private ResourceBundle resources;
    private ProtocolAgentService protocolAgentService;
    private ProtocolRegistrationService registrationService;
    private SponsorNotificationService sponsorNotificationService;

    Protocol getProtocol(String protocolNumber, Organization sponsor) {
        String hql = HQL_FROM_BASE + Protocol.class.getName()
                + " where protocolNumber = :protocolNumber and sponsor = :sponsor";
        Query query = getSession().createQuery(hql);
        query.setString("protocolNumber", protocolNumber);
        query.setEntity("sponsor", sponsor);
        return (Protocol) query.uniqueResult();
    }

    /**
     * Checks if another existing protocol already has a protocol number.
     *
     * @param protocol new or updated protocol to check
     * @return true if a duplicate number
     */
    public boolean hasDuplicateProtocolNumber(Protocol protocol) {
        if (!PersistentObjectUtil.isNew(protocol.getSponsor())) {
            Protocol existingProtocolWithNumber = getProtocol(protocol.getProtocolNumber(), protocol.getSponsor());
            return existingProtocolWithNumber != null && !PersistentObjectUtil.areSame(protocol,
                    existingProtocolWithNumber);
        } else {
            return false;
        }
    }

    void deleteForms(Set<AbstractRegistrationForm> removedForms) {
        for (AbstractRegistrationForm removedForm : removedForms) {
            getSession().delete(removedForm);
        }
    }

    public ResourceBundle getResources() {
        return resources;
    }

    public ProtocolAgentService getProtocolAgentService() {
        return protocolAgentService;
    }

    public ProtocolRegistrationService getRegistrationService() {
        return registrationService;
    }

    public SponsorNotificationService getSponsorNotificationService() {
        return sponsorNotificationService;
    }

    @Inject
    void setResources(ResourceBundle resources) {
        this.resources = resources;
    }

    @Resource(mappedName = "firebird/ProtocolAgentServiceBean/local")
    public void setProtocolAgentService(ProtocolAgentService protocolAgentService) {
        this.protocolAgentService = protocolAgentService;
    }

    @Resource(mappedName = "firebird/ProtocolRegistrationServiceBean/local")
    void setRegistrationService(ProtocolRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Resource(mappedName = "firebird/SponsorNotificationServiceBean/local")
    public void setSponsorNotificationService(SponsorNotificationService sponsorNotificationService) {
        this.sponsorNotificationService = sponsorNotificationService;
    }
}
