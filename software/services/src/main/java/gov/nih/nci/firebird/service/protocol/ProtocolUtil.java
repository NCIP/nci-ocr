package gov.nih.nci.firebird.service.protocol;

import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class.
 */
public class ProtocolUtil {

    /**
     * Will mark all registrations and sub-investigator registrations as Apporved.
     *
     * @param registration Registration
     */
    public static void markAllApproved(InvestigatorRegistration registration) {
        markApproved(registration);
        for (SubInvestigatorRegistration subInvestigatorRegistration : registration.getSubinvestigatorRegistrations()) {
            if (!RegistrationStatus.APPROVED.equals(subInvestigatorRegistration.getStatus())) {
                markApproved(subInvestigatorRegistration);
            }
        }
    }

    /**
     * Marks the Registration and all forms in that Registration as Approved.
     *
     * @param registration Registration
     */
    public static void markApproved(AbstractProtocolRegistration registration) {
        registration.setStatus(RegistrationStatus.APPROVED);
        for (AbstractRegistrationForm form : registration.getForms()) {
            form.setFormStatus(FormStatus.APPROVED);
        }
    }

    /**
     * Sets all forms in the Registration as In Progress.
     *
     * @param registration Registration
     */
    public static void setFormsToInProgress(AbstractProtocolRegistration registration) {
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            form.setFormStatus(FormStatus.IN_PROGRESS);
        }
    }


    /**
     * Returns  Email address of all the Investigator and Sub-Investigators for a given Registration.
     *
     * @param registration Registration
     * @return Set  Email addresses
     */
    public static Set<String> getAllEmailAddress(InvestigatorRegistration registration) {
        Set<String> addresses = new HashSet<String>();
        addresses.add(registration.getProfile().getPerson().getEmail());
        for (SubInvestigatorRegistration subInvestigatorRegistration : registration.getSubinvestigatorRegistrations()) {
            addresses.add(subInvestigatorRegistration.getProfile().getPerson().getEmail());
        }
        return addresses;
    }
}
