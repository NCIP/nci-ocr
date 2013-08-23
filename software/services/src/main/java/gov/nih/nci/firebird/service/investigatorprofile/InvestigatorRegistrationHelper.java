package gov.nih.nci.firebird.service.investigatorprofile;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.HumanResearchCertificateForm;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.ProtocolForm1572;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigator;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.EqualPredicate;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class for managing  Investigator Registrations.
 */
public class InvestigatorRegistrationHelper {

    static final EnumSet<RegistrationStatus> INVALID_SUBINVESTIGATOR_REMOVAL_STATES = EnumSet
            .of(RegistrationStatus.RETURNED);

    private ProtocolRegistrationService registrationService;

    /**
     * Removes the links of a Training Certificate from a Investigator.
     *
     * @param profile     Investigator Profile
     * @param certificate Training Certificate
     */
    void breakCertificateLinks(InvestigatorProfile profile, TrainingCertificate certificate) {
        registrationService.setRegistrationFormStatusesToRevisedIfReviewed(
                getAffectedRegistrationsByCertificateModification(profile, certificate),
                FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE);

        certificate.unLinkSubmittedCertificates();
        for (AbstractProtocolRegistration registration : profile.getCurrentProtocolRegistrations()) {
            if (registration.getHumanResearchCertificateForm() != null) {
                registration.getHumanResearchCertificateForm().deselectCertificate(certificate);
            }
        }
    }

    /**
     * Deletes Associated Organization from an Unlocked Registration.
     *
     * @param profile     Investigator Profile
     * @param association Organization Association
     */
    void deleteAssociatedOrganizationFromUnlockedRegistrations(InvestigatorProfile profile,
                                                               OrganizationAssociation association) {
        registrationService.setRegistrationFormStatusesToRevisedIfReviewed(
                getAffectedRegistrationsByAssociatedOrganizationDelete(profile, association.getOrganizationRole()
                        .getOrganization()), FormTypeEnum.FORM_1572);

        for (AbstractRegistration registration : getUnlockedRegistrations(profile)) {
            handleForm1572Association(registration, association);
        }
    }

    /**
     * Deletes all Removable SubInvestigators registrations.
     *
     * @param profile         Investigator Profile
     * @param subInvestigator SubInvestigator
     */
    void deleteSubInvestigatorRegistrations(InvestigatorProfile profile, SubInvestigator subInvestigator) {
        List<SubInvestigatorRegistration> subInvestigatorRegistrations = registrationService
                .getSubinvestigatorRegistrations(profile, subInvestigator.getPerson());

        subInvestigatorRegistrations = Lists.newArrayList(Collections2.filter(subInvestigatorRegistrations,
                new Predicate<SubInvestigatorRegistration>() {
                    @Override
                    public boolean apply(SubInvestigatorRegistration input) {
                        return isSubinvestigatorRegistrationRemoveable(input.getPrimaryRegistration());
                    }
                }));

        if (!subInvestigatorRegistrations.isEmpty()) {
            registrationService.removeSubInvestigatorRegistrationAndNotify(subInvestigatorRegistrations);
        }
    }

    /**
     * Returns a List of Registrations.
     *
     * @param profile     Investigator Profile
     * @param certificate Training Certificate
     * @return List of Registrations
     */
    Set<AbstractProtocolRegistration> getAffectedRegistrationsByCertificateModification(
            InvestigatorProfile profile, TrainingCertificate certificate) {
        Set<AbstractProtocolRegistration> registrations = Sets.newHashSet();

        for (AbstractProtocolRegistration registration
                : registrationService.getReturnedOrRevisedRegistrations(profile)) {
            HumanResearchCertificateForm hrcForm = registration.getHumanResearchCertificateForm();
            if (hrcForm != null && CollectionUtils.exists(hrcForm.getCertificates(), new EqualPredicate(certificate))) {
                registrations.add(registration);
            }
        }
        return registrations;
    }

    private Set<AbstractProtocolRegistration> getAffectedRegistrationsByAssociatedOrganizationDelete(
            InvestigatorProfile profile, Organization organization) {
        Set<AbstractProtocolRegistration> registrations = Sets.newHashSet();
        for (AbstractProtocolRegistration registration
                : registrationService.getReturnedOrRevisedRegistrations(profile)) {
            ProtocolForm1572 protocolForm1572 = registration.getForm1572();
            if (protocolForm1572 != null
                    && CollectionUtils.exists(protocolForm1572.getOrganizations(), new EqualPredicate(organization))) {
                registrations.add(registration);
                break;
            }
        }
        return registrations;
    }

    private void handleForm1572Association(AbstractRegistration registration, OrganizationAssociation association) {
        if (registration instanceof AbstractProtocolRegistration) {
            Organization associatedOrganization = association.getOrganizationRole().getOrganization();
            AbstractProtocolRegistration protocolRegistration = (AbstractProtocolRegistration) registration;
            if (protocolRegistration.getForm1572() != null) {
                protocolRegistration.getForm1572().getAssociatedOrganizations(association.getType())
                        .remove(associatedOrganization);
                registrationService.save(protocolRegistration);
            }
        }
    }

    private Set<AbstractRegistration> getUnlockedRegistrations(InvestigatorProfile profile) {
        Set<AbstractRegistration> unlockedRegistrations = Sets.newHashSet();
        for (AbstractRegistration registration : profile.getRegistrations()) {
            if (!registration.isLockedForInvestigator()) {
                unlockedRegistrations.add(registration);
            }
        }
        return unlockedRegistrations;
    }

    private boolean isSubinvestigatorRegistrationRemoveable(InvestigatorRegistration primary) {
        return !primary.isLockedForInvestigator()
                && !INVALID_SUBINVESTIGATOR_REMOVAL_STATES.contains(primary.getStatus());
    }

    @Inject
    void setRegistrationService(ProtocolRegistrationService registrationService) {
        this.registrationService = registrationService;
    }
}

