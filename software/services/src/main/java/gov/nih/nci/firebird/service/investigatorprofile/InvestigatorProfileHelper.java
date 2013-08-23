package gov.nih.nci.firebird.service.investigatorprofile;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.correlation.PersonRoleType;
import gov.nih.nci.firebird.service.person.PersonService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;


/**
 * Helper class for managing  Investigator Profiles.
 */
public class InvestigatorProfileHelper {

    private static final String PERSON_LIST_PARAMETER_NAME = "persons";
    private static final String PROFILES_BY_PERSON_HQL = "from " + InvestigatorProfile.class.getName()
            + " where person in (:" + PERSON_LIST_PARAMETER_NAME + ") order by person.lastName, person.firstName";

    private Provider<Session> sessionProvider;
    private PersonService personService;


    /**
     * Will associate primary organization to a Investigator Profile.
     *
     * @param profile             Investigator Profile
     * @param primaryOrganization Primary Organization
     * @throws ValidationException
     */
    void associatePrimaryOrganization(InvestigatorProfile profile, PrimaryOrganization primaryOrganization)
            throws ValidationException {
        profile.setPrimaryOrganization(primaryOrganization);
        checkCorrelation(profile);
    }

    /**
     * Validates the OrganizationAssociation and OHRP and will throw an IllegalArgumentException if invalid.
     *
     * @param association Organization Association
     * @param ohrp        OHRP Number
     */
    void validateAssociationOhrp(OrganizationAssociation association, String ohrp) {
        checkOhrpValid(ohrp);
        checkAssociationNotNull(association);
        checkAssociationValid(association);
    }

    /**
     * Delegates to the person service.
     * Checks for an existing structural role corresponding to type, creating one if it doesn't already exist.
     *
     * @param profile Investigator Profile
     * @throws ValidationException Exception
     */
    void checkCorrelation(InvestigatorProfile profile) throws ValidationException {
        Person person = profile.getPerson();
        Organization organization = profile.getPrimaryOrganization().getOrganization();
        personService.checkCorrelation(person, organization, PersonRoleType.HEALTH_CARE_PROVIDER);
    }


    /**
     * Filter a List of Investigator Profiles and returns only CTEP Investigators.
     *
     * @param profiles Investigator Profile
     * @return List of CTEP Investigator Profiles
     */
    List<InvestigatorProfile> getOnlyCtepInvestigators(List<InvestigatorProfile> profiles) {
        return Lists.newArrayList(Iterables.filter(profiles, new Predicate<InvestigatorProfile>() {
            @Override
            public boolean apply(InvestigatorProfile profile) {
                return BooleanUtils.toBoolean(profile.getUser().isCtepUser());
            }
        }));
    }


    /**
     * Search method for Investigators.
     *
     * @param searchTerm String search term
     * @return List of Investigator Profiles.
     */
    List<InvestigatorProfile> search(String searchTerm) {
        List<Person> persons = personService.search(searchTerm);
        return getProfiles(persons);
    }


    /**
     * Removes a File from all Unlocked Registrations.
     *
     * @param profile Investigator Profile
     * @param file    File to be removed
     */
    void removeFileFromUnLockedRegistrations(InvestigatorProfile profile, FirebirdFile file) {
        for (AbstractRegistration registration : profile.getRegistrations()) {
            if (!registration.isLockedForInvestigator() && registration.getAdditionalAttachmentsForm() != null) {
                registration.getAdditionalAttachmentsForm().getAdditionalAttachments().remove(file);
                getSession().saveOrUpdate(registration);
            }
        }
    }

    private void checkOhrpValid(String ohrp) {
        if (StringUtils.isBlank(ohrp)) {
            throw new IllegalArgumentException("OHRP Number must be set!");
        }
    }

    private void checkAssociationNotNull(OrganizationAssociation association) {
        if (association == null) {
            throw new IllegalArgumentException("Null Association Provided!");
        }
    }

    private void checkAssociationValid(OrganizationAssociation association) {
        EnumSet<OrganizationRoleType> ohrpRoles = EnumSet.of(OrganizationRoleType.PRACTICE_SITE);

        OrganizationRoleType associationRole = association.getOrganizationRole().getRoleType();
        if (!ohrpRoles.contains(associationRole)) {
            throw new IllegalArgumentException("The Provided Association does not have an OHRP field!");
        }
    }

    @SuppressWarnings("unchecked")
    // hibernate does not provide typed arguments
    private List<InvestigatorProfile> getProfiles(List<Person> persons) {
        Collection<Person> personsWithRecords = getPersonsWithRecords(persons);
        if (personsWithRecords.isEmpty()) {
            return Collections.emptyList();
        } else {
            Query query = getSession().createQuery(PROFILES_BY_PERSON_HQL);
            query.setParameterList(PERSON_LIST_PARAMETER_NAME, personsWithRecords);
            return query.list();
        }
    }

    private Collection<Person> getPersonsWithRecords(List<Person> persons) {
        Collection<Person> personsInDatabase = Collections2.filter(persons, new Predicate<Person>() {
            @Override
            public boolean apply(Person person) {
                return person.getId() != null;
            }

        });
        return personsInDatabase;
    }

    private Session getSession() {
        return sessionProvider.get();
    }

    @Inject
    public void setSessionProvider(Provider<Session> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Inject
    void setPersonService(PersonService personService) {
        this.personService = personService;
    }

}
