package gov.nih.nci.firebird.service.sponsor;

import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.ProtocolRevision;

import javax.ejb.Local;

/**
 * Service for sending notifications to Sponsors.
 */
@Local
public interface SponsorNotificationService  {

    /**
     * Notify sponsor of the registration that the registration packet has been de-activated.
     *
     * @param registration Protocol Registration
     * @param comments     Additional comments
     */
    void notifyOfDeactivation(AbstractProtocolRegistration registration, String comments);

    /**
     * Notify sponsor of the registration that the registration packet has been re-activated.
     *
     * @param registration Protocol Registration
     * @param comments     Additional comments
     */
    void notifyOfReactivation(AbstractProtocolRegistration registration, String comments);

    /**
     * Notify Investigators and sub-investigators that the Registration has been approved.
     *
     * @param registration Registration
     */
    void notifyInvestigatorsOfApproval(InvestigatorRegistration registration);

    /**
     * Sends a Protocol Update notification.
     * @param registration Original Registration
     * @param revision Updated Registration
     */
    void sendProtocolUpdateEmail(AbstractProtocolRegistration registration, ProtocolRevision revision);

    /**
     * Sends an email notifying the Sponsor that a Registration Packet has been removed.
     *
     * @param registration Registration
     */
    void sendPacketRemovedEmail(InvestigatorRegistration registration);
}
