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
package gov.nih.nci.firebird.security;

import gov.nih.nci.coppa.common.LimitOffset;
import gov.nih.nci.coppa.po.HealthCareFacility;
import gov.nih.nci.coppa.po.IdentifiedPerson;
import gov.nih.nci.coppa.services.structuralroles.healthcarefacility.common.HealthCareFacilityI;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.ctep.ces.ocr.api.CesInvestigatorService;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.PrimaryOrganizationType;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.InvestigatorStatus;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.NesId;
import gov.nih.nci.firebird.service.ctep.iam.IamIntegrationService;
import gov.nih.nci.firebird.service.investigator.InvestigatorService;
import gov.nih.nci.firebird.service.organization.InvalidatedOrganizationException;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.person.external.InvalidatedPersonException;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.user.FirebirdUserService;
import gov.nih.nci.firebird.service.user.UserCnUtils;

import java.rmi.RemoteException;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.iso._21090.CD;
import org.iso._21090.II;

import com.google.inject.Inject;

/**
 * Retrieves authorized group information from CTEP's IAM system.
 */
@SuppressWarnings("PMD.TooManyMethods")
// will refactor when no longer using stubbed CTEP functionality
class IamRoleHandler extends AbstractCredentialHandler implements RoleHandler {

    private static final Logger LOG = Logger.getLogger(IamRoleHandler.class);
    private static final String CTEP_ROOT = "2.16.840.1.113883.3.26.6.1";
    private static final int RANDOM_ORGANIZATION_OFFSET_RANGE = 500;

    private final IamIntegrationService iamIntegrationService;
    private final PersonService personService;
    private final InvestigatorService investigatorService;
    private final CesInvestigatorService cesInvestigatorService;
    private final SponsorService sponsorService;
    private final HealthCareFacilityI healthCareFacilityService;
    private final OrganizationService organizationService;
    private final IdentifiedPersonI identifiedPersonService;

    @Inject
    @SuppressWarnings("PMD.ExcessiveParameterList")
    // CHECKSTYLE:OFF: Maximum Parameters - requires all services
    // requires all services
    IamRoleHandler(FirebirdUserService userService, IamIntegrationService iamIntegrationService,
            PersonService personService, CesInvestigatorService cesInvestigatorService,
            InvestigatorService investigatorService, SponsorService sponsorService,
            HealthCareFacilityI healthCareFacilityService, OrganizationService organizationService,
            IdentifiedPersonI identifiedPersonService) {
        // CHECKSTYLE:ON
        super(userService);
        this.iamIntegrationService = iamIntegrationService;
        this.personService = personService;
        this.cesInvestigatorService = cesInvestigatorService;
        this.investigatorService = investigatorService;
        this.sponsorService = sponsorService;
        this.healthCareFacilityService = healthCareFacilityService;
        this.organizationService = organizationService;
        this.identifiedPersonService = identifiedPersonService;
    }

    @Override
    public void handleRoles(UserSessionInformation sessionInformation) {
        sessionInformation.addGroupName(FirebirdConstants.AUTHENTICATED_USER_ROLE);
        sessionInformation.addGroupNames(getRoles(sessionInformation.getFullyQualifiedUsername()));
        sessionInformation.addGroupName(FirebirdConstants.REGISTERED_USER_ROLE);
        FirebirdUser user = getUserService().getUserInfo(sessionInformation);
        if (user == null) {
            user = createUser(sessionInformation);
        }
        updateRoles(user, sessionInformation.getGroupNames());
        getUserService().save(user);
        handleUserUpdates(user);
    }

    private void updateRoles(FirebirdUser user, Set<String> groupNames) {
        checkInvestigatorRole(user, groupNames);
        checkCoordinatorRole(user, groupNames);
        checkSponsorRole(user, groupNames);
        checkSponsorDelegateRole(user, groupNames);
    }

    private void checkInvestigatorRole(FirebirdUser user, Set<String> groupNames) {
        if (groupNames.contains(UserRoleType.CTEP_INVESTIGATOR.getGroupName()) && user.getInvestigatorRole() == null) {
            InvestigatorProfile profile = new InvestigatorProfile();
            profile.setPerson(user.getPerson());
            profile.setPrimaryOrganization(getPrimaryOrganization());
            user.createInvestigatorRole(profile);
        }
    }

    private PrimaryOrganization getPrimaryOrganization() {
        II ii = getIi(getHealthCareFacility());
        Organization organization = null;
        try {
            organization = getOrganization(ii);
        } catch (InvalidatedOrganizationException e) {
            Logger.getLogger(getClass()).error("Couldn't load Primary Organization", e);
        }
        return new PrimaryOrganization(organization, PrimaryOrganizationType.HEALTH_CARE_FACILITY);
    }

    private HealthCareFacility getHealthCareFacility() {
        HealthCareFacility searchFacility = new HealthCareFacility();
        searchFacility.setStatus(getActiveStatusCode());
        try {
            return healthCareFacilityService.query(searchFacility, createLimitOffset())[0];
        } catch (RemoteException e) {
            Logger.getLogger(getClass()).error("Couldn't load Primary Organization", e);
            throw new IllegalStateException(e);
        }
    }

    private CD getActiveStatusCode() {
        CD activeCode = new CD();
        activeCode.setCode("active");
        return activeCode;
    }

    private LimitOffset createLimitOffset() {
        LimitOffset limitOffset = new LimitOffset();
        limitOffset.setOffset(RandomUtils.nextInt(RANDOM_ORGANIZATION_OFFSET_RANGE));
        limitOffset.setLimit(1);
        return limitOffset;
    }

    private II getIi(HealthCareFacility facility) {
        for (II ii : facility.getIdentifier().getItem()) {
            if (NesIIRoot.HEALTH_CARE_FACILITY.getRoot().equals(ii.getRoot())) {
                return ii;
            }
        }
        throw new IllegalArgumentException("No II in Correlation for root " + NesIIRoot.HEALTH_CARE_FACILITY.getRoot());
    }

    private Organization getOrganization(II identifier) throws InvalidatedOrganizationException {
        String externalId = new NesId(identifier).toString();
        return getOrganization(externalId);
    }

    private Organization getOrganization(String externalId) throws InvalidatedOrganizationException {
        return organizationService.getByExternalId(externalId);
    }

    private void checkCoordinatorRole(FirebirdUser user, Set<String> groupNames) {
        if (groupNames.contains(UserRoleType.CTEP_REGISTRATION_COORDINATOR.getGroupName())
                && user.getRegistrationCoordinatorRole() == null) {
            user.createRegistrationCoordinatorRole();
        }
    }

    private void checkSponsorRole(FirebirdUser user, Set<String> groupNames) {
        Organization sponsorOrganizationWithAnnualRegistrations = sponsorService
                .getSponsorOrganizationWithAnnualRegistrations();
        if (groupNames.contains(UserRoleType.CTEP_SPONSOR.getGroupName())
                && !user.getSponsorRepresentativeOrganizations().contains(sponsorOrganizationWithAnnualRegistrations)) {
            user.addSponsorRepresentativeRole(sponsorOrganizationWithAnnualRegistrations);
        }
    }

    private void checkSponsorDelegateRole(FirebirdUser user, Set<String> groupNames) {
        Organization sponsorOrganizationWithAnnualRegistrations = sponsorService
                .getSponsorOrganizationWithAnnualRegistrations();
        if (groupNames.contains(UserRoleType.CTEP_SPONSOR_DELEGATE.getGroupName())
                && !user.getSponsorDelegateOrganizations().contains(sponsorOrganizationWithAnnualRegistrations)) {
            user.addSponsorDelegateRole(sponsorOrganizationWithAnnualRegistrations);
        }
    }

    private void handleUserUpdates(FirebirdUser user) {
        if (user.isInvestigator()) {
            String ctepId = user.getPerson().getCtepId();
            InvestigatorStatus status = InvestigatorStatus.translate(cesInvestigatorService
                    .getInvestigatorStatus(ctepId));
            investigatorService.handleStatus(user, status);
        }
    }

    private Set<String> getRoles(String fullyQualifiedUserName) {
        Entry<String, String>[] parsedCn = UserCnUtils.parseCn(fullyQualifiedUserName);
        String basename = UserCnUtils.getFirstValue("CN", parsedCn);
        return iamIntegrationService.getGroups(basename);
    }

    private FirebirdUser createUser(UserSessionInformation sessionInformation) {
        FirebirdUser user = new FirebirdUser();
        user.setUsername(sessionInformation.getFullyQualifiedUsername());
        Person person = getByCtepId(sessionInformation.getAccount().getCtepId());
        user.setPerson(person);
        return user;
    }

    private Person getByCtepId(String ctepId) {
        String nesId = getNesId(ctepId);
        try {
            return personService.getByExternalId(nesId);
        } catch (InvalidatedPersonException e) {
            LOG.error("Couldn't retrieve person with CTEP Id: " + ctepId, e);
            throw new IllegalStateException(e);
        }
    }

    private String getNesId(String ctepId) {
        IdentifiedPerson identifiedPerson = createIdentifiedPersonForCtepId(ctepId);
        LimitOffset limitOffset = new LimitOffset();
        limitOffset.setLimit(1);
        try {
            IdentifiedPerson[] results = identifiedPersonService.query(identifiedPerson, limitOffset);
            if (!ArrayUtils.isEmpty(results)) {
                return results[0].getPlayerIdentifier().getExtension();
            } else {
                throw new IllegalArgumentException("No IdentifiedPerson found for CTEP Id: " + ctepId);
            }
        } catch (RemoteException e) {
            LOG.error("Couldn't retrieve NES ID for CTEP ID: " + ctepId, e);
            throw new IllegalStateException(e);
        }
    }

    private IdentifiedPerson createIdentifiedPersonForCtepId(String ctepId) {
        II assignedId = new II();
        assignedId.setRoot(CTEP_ROOT);
        assignedId.setExtension(ctepId);
        IdentifiedPerson searchIdentifiedPerson = new IdentifiedPerson();
        searchIdentifiedPerson.setAssignedId(assignedId);
        return searchIdentifiedPerson;
    }

}
