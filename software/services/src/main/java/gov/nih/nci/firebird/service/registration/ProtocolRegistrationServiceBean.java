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
package gov.nih.nci.firebird.service.registration;

import static com.google.common.base.Preconditions.*;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.RevisedInvestigatorRegistration;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.person.external.InvalidatedPersonException;

import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Query;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

/**
 * Provides persistence functionality and other services for registrations.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SuppressWarnings({ "PMD.TooManyMethods" })
// facade service expected to expose many methods
// methods broken down for readability
public class ProtocolRegistrationServiceBean extends AbstractBaseRegistrationServiceBean<AbstractProtocolRegistration>
        implements ProtocolRegistrationService {

    private static final String INVESTIGATOR_PROFILE_PARAM = "investigatorProfile";
    private static final String SUBINVESTIGATOR_PERSON_PARAM = "subInvestigator";
    private static final String FIND_SUBINVESTIGATORS_HQL = "from " + SubInvestigatorRegistration.class.getName()
            + " where profile.person = :" + SUBINVESTIGATOR_PERSON_PARAM + " and primaryRegistration in ( from "
            + InvestigatorRegistration.class.getName() + " where profile = :" + INVESTIGATOR_PROFILE_PARAM + " )";

    @Override
    public InvestigatorRegistration createInvestigatorRegistration(Protocol protocol, Person investigator)
            throws ValidationException {
        addPersonToNesIfNecessary(investigator);
        InvestigatorRegistration registration = new InvestigatorRegistration();
        registration.setStatus(RegistrationStatus.NOT_STARTED);
        registration.getInvitation().setInvitationStatus(InvitationStatus.NOT_INVITED);
        getOrCreateProfile(investigator).addRegistration(registration);
        protocol.addRegistration(registration);
        save(registration);
        return registration;
    }

    @Override
    protected void sendSubmissionNotifications(AbstractProtocolRegistration registration) {
        getSponsorService().notifySubmittedRegistration(registration);
    }

    private void addPersonToNesIfNecessary(Person investigator) throws ValidationException {
        if (!investigator.hasExternalRecord()) {
            getPersonService().save(investigator);
        }
    }

    private InvestigatorProfile getOrCreateProfile(Person investigator) {
        InvestigatorProfile profile = getProfileService().getByPerson(investigator);
        if (profile == null) {
            profile = new InvestigatorProfile();
            try {
                getPersonService().save(investigator);
            } catch (ValidationException e) {
                throw new IllegalArgumentException("Investigator person is invalid", e);
            }
            profile.setPerson(investigator);
            getProfileService().save(profile);
        }
        return profile;
    }

    @Override
    public SubInvestigatorRegistration createSubinvestigatorRegistration(InvestigatorRegistration primaryRegistration,
            Person investigator) {
        SubInvestigatorRegistration registration = null;
        for (SubInvestigatorRegistration subReg : primaryRegistration.getSubinvestigatorRegistrations()) {
            // no need to re add the ones that are already added.
            if (investigator.getId().equals(subReg.getProfile().getPerson().getId())) {
                registration = subReg;
                break;
            }
        }
        if (registration == null) {
            registration = new SubInvestigatorRegistration();
            registration.setStatus(RegistrationStatus.NOT_STARTED);
            registration.getInvitation().setInvitationStatus(InvitationStatus.NOT_INVITED);
            getOrCreateProfile(investigator).addRegistration(registration);
            primaryRegistration.addSubinvestigatorRegistration(registration);
            primaryRegistration.getProtocol().addRegistration(registration);
            primaryRegistration.getSubinvestigatorRegistrations().add(registration);
            save(primaryRegistration, FormTypeEnum.FORM_1572);
            save(registration);
        }

        return registration;
    }

    @Override
    public void createSubinvestigatorRegistrations(InvestigatorRegistration registration,
            List<String> personExternalIds) {
        try {
            for (String externalId : personExternalIds) {
                createSubinvestigatorRegistration(registration, getPersonService().getByExternalId(externalId));
            }
        } catch (InvalidatedPersonException e) {
            throw new IllegalStateException(e);
        }
        save(registration);
    }

    @Override
    public void inviteToRegistration(AbstractProtocolRegistration registration) {
        if (InvitationStatus.NOT_INVITED.equals(registration.getInvitation().getInvitationStatus())) {
            registration.getInvitation().setInvitationStatus(InvitationStatus.NO_RESPONSE);
            registration.getInvitation().setInvitationChangeDate(new Date());
        }
        save(registration);
        sendInvitationEmail(registration);
    }

    @Override
    public void removeSubInvestigatorRegistrationAndNotify(SubInvestigatorRegistration registration) {
        removeSubInvestigatorRegistrationAndNotify(Lists.newArrayList(registration));
    }

    @Override
    public void removeSubInvestigatorRegistrationAndNotify(List<SubInvestigatorRegistration> registrations) {
        sendRemovedSubinvestigatorRegistrationEmails(registrations);
        removeSubinvestigatorRegistrations(registrations);
    }

    private void removeSubinvestigatorRegistrations(List<SubInvestigatorRegistration> registrations) {
        removeCurrentRegistrationReferences(registrations);
        for (SubInvestigatorRegistration registration : registrations) {
            removeActiveSubInvestigatorRegistration(registration);
        }
        removePrimaryRegistrationReferences(registrations);
    }

    private void sendRemovedSubinvestigatorRegistrationEmails(List<SubInvestigatorRegistration> registrations) {
        List<SubInvestigatorRegistration> removedRegistrationsForNotify = Lists.newArrayList();
        for (SubInvestigatorRegistration registration : registrations) {
            if (registration.getInvitation().isInvited()) {
                removedRegistrationsForNotify.add(registration);
            }
        }
        notifySubinvestigatorOfRemoval(removedRegistrationsForNotify);
        notifySponsorOfRemovalIfNecessary(removedRegistrationsForNotify);
    }

    private void removeActiveSubInvestigatorRegistration(SubInvestigatorRegistration registration) {
        if (!registration.getPrimaryRegistration().isLockedForInvestigator()) {
            removeSubInvestigatorRegistration(registration);
        } else {
            throw new IllegalArgumentException(
                    "The provided Subinvestigator Registration is not in a valid state for removal."
                            + " It must be part of an unlocked registration!");
        }
    }

    private void removeSubInvestigatorRegistration(SubInvestigatorRegistration registration) {
        registration.getPrimaryRegistration().getSubinvestigatorRegistrations().remove(registration);
        save(registration.getPrimaryRegistration(), FormTypeEnum.FORM_1572);
        if (!registration.isReferenced()) {
            registration.prepareForDeletion();
            delete(registration);
        }
    }

    private void notifySubinvestigatorOfRemoval(List<SubInvestigatorRegistration> deletedRegistrations) {
        if (!deletedRegistrations.isEmpty()) {
            String subInvestigatorEmail = deletedRegistrations.get(0).getProfile().getPerson().getEmail();
            FirebirdMessage message = getSubInvestigatorRemovedMessage(deletedRegistrations);
            getEmailService().sendMessage(subInvestigatorEmail, null, null, message);
        }
    }

    private FirebirdMessage getSubInvestigatorRemovedMessage(List<SubInvestigatorRegistration> deletedRegistrations) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.REGISTRATIONS, deletedRegistrations);
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR, deletedRegistrations.iterator().next()
                .getPrimaryRegistration().getProfile().getPerson());
        return getTemplateService().generateMessage(FirebirdMessageTemplate.REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL,
                parameterValues);
    }

    private void notifySponsorOfRemovalIfNecessary(List<SubInvestigatorRegistration> deletedRegistrations) {
        for (SubInvestigatorRegistration registration : deletedRegistrations) {
            if (registration.isLockedForInvestigator()) {
                notifySponsorOfRemoval(registration);
            }
        }
    }

    private void notifySponsorOfRemoval(SubInvestigatorRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.SUBINVESTIGATOR_REGISTRATION, registration);
        FirebirdMessage message = getTemplateService().generateMessage(
                FirebirdMessageTemplate.REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL_TO_SPONSOR, parameterValues);
        String sponsorEmail = getSponsorEmailAddress(registration);
        getEmailService().sendMessage(sponsorEmail, null, null, message);
    }

    private void removePrimaryRegistrationReferences(List<SubInvestigatorRegistration> registrations) {
        for (SubInvestigatorRegistration registration : registrations) {
            registration.setPrimaryRegistration(null);
        }
    }

    private void removeCurrentRegistrationReferences(List<SubInvestigatorRegistration> removedRegistrations) {
        for (SubInvestigatorRegistration removedRegistration : removedRegistrations) {
            for (SubInvestigatorRegistration subInvestigatorRegistration : removedRegistration.getProtocol()
                    .getSubinvestigatorRegistrations()) {
                if (removedRegistration.equals(subInvestigatorRegistration.getCurrentRegistration())) {
                    subInvestigatorRegistration.setCurrentRegistration(null);
                }
            }
        }
    }

    @Override
    public FirebirdMessage getCoordinatorCompletedRegistrationEmailMessage(FirebirdUser coordinator,
            AbstractProtocolRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK, generateRegistrationLink(registration));
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION_COORDINATOR, coordinator.getPerson());
        return getTemplateService().generateMessage(FirebirdMessageTemplate.COORDINATOR_COMPLETED_REGISTRATION_EMAIL,
                parameterValues);
    }

    private String generateRegistrationLink(AbstractProtocolRegistration registration) {
        return FirebirdConstants.REGISTRATION_URL_PATH_WITH_ID_PARAM + registration.getId();
    }

    private void sendInvitationEmail(AbstractProtocolRegistration registration) {
        String to = registration.getProfile().getPerson().getEmail();
        FirebirdMessage invitationMessage;
        if (registration instanceof InvestigatorRegistration) {
            invitationMessage = getInvitationEmailMessage((InvestigatorRegistration) registration);
        } else {
            invitationMessage = getInvitationEmailMessage((SubInvestigatorRegistration) registration);
        }
        getEmailService().sendMessage(to, null, null, invitationMessage);
    }

    private FirebirdMessage getInvitationEmailMessage(InvestigatorRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK, FirebirdConstants.FIREBIRD_HOME_URL_PATH);
        parameterValues.put(FirebirdTemplateParameter.SPONSOR_EMAIL_ADDRESS, getSponsorEmailAddress(registration));
        return getTemplateService().generateMessage(FirebirdMessageTemplate.INVESTIGATOR_INVITATION_EMAIL,
                parameterValues);
    }

    private String getSponsorEmailAddress(AbstractProtocolRegistration registration) {
        return getSponsorService().getSponsorEmailAddress(registration.getProtocol().getSponsor());
    }

    private FirebirdMessage getInvitationEmailMessage(SubInvestigatorRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.SUBINVESTIGATOR_REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK, FirebirdConstants.FIREBIRD_HOME_URL_PATH);
        parameterValues.put(FirebirdTemplateParameter.SPONSOR_EMAIL_ADDRESS, getSponsorEmailAddress(registration));
        return getTemplateService().generateMessage(FirebirdMessageTemplate.SUBINVESTIGATOR_INVITATION_EMAIL,
                parameterValues);
    }

    @Override
    public Set<AbstractProtocolRegistration> getRegistrations(InvestigatorProfile profile) {
        return profile.getCurrentProtocolRegistrations();
    }

    @SuppressWarnings("unchecked")
    // Hibernate does not provide typed returns
    @Override
    public List<SubInvestigatorRegistration> getSubinvestigatorRegistrations(InvestigatorProfile profile,
            Person subInvestigator) {
        return (List<SubInvestigatorRegistration>) getSession().createQuery(FIND_SUBINVESTIGATORS_HQL)
                .setEntity(SUBINVESTIGATOR_PERSON_PARAM, subInvestigator)
                .setEntity(INVESTIGATOR_PROFILE_PARAM, profile).list();
    }

    @Override
    protected void submitRegistration(AbstractProtocolRegistration registration) {
        registration.setStatus(RegistrationStatus.SUBMITTED);
    }

    @Override
    public void initiateRevision(InvestigatorRegistration registration, FirebirdUser user) {
        checkArgument(registration.isRevisable(), "Registration with status of "
                + registration.getStatus().getDisplay() + " is not able to be initiated for revision");
        if (RegistrationStatus.APPROVED.equals(registration.getStatus())) {
            registration.createRevisedRegistration();
            registration.setApprovalDate(null);
            resetApprovedFormstoAccepted(registration);
        }
        deleteAll(registration.prepareFormsForReturn());
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        registration.clearNonRejectedFormsComments();
        save(registration);
        sendRegistrationRevisionNotifications(registration, user);
    }

    private void resetApprovedFormstoAccepted(InvestigatorRegistration registration) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (FormStatus.APPROVED == form.getFormStatus()) {
                form.setFormStatus(FormStatus.ACCEPTED);
            }
        }
    }

    private void sendRegistrationRevisionNotifications(InvestigatorRegistration registration, FirebirdUser user) {
        if (registration.getProfile().getPerson().equals(user.getPerson())) {
            sendRegistrationRevisionNotificationToCoordinators(registration);
        } else if (user.isRegistrationCoordinator()
                && user.getRegistrationCoordinatorRole().isApprovedToManageInvestigatorRegistrations(
                        registration.getProfile())) {
            sendRegistrationRevisionNotificationToInvestigator(registration, user);
            sendRegistrationRevisionNotificationToCoordinators(registration, user);
        } else {
            throw new IllegalArgumentException(user.getPerson().getDisplayName()
                    + " is not able to initiate a registration revision for "
                    + registration.getProfile().getPerson().getDisplayName());
        }
        sendRegistrationRevisionNotificationToSponsor(registration);
    }

    private void sendRegistrationRevisionNotificationToSponsor(InvestigatorRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
        FirebirdMessage message = getTemplateService().generateMessage(
                FirebirdMessageTemplate.REGISTRATION_REVISION_INITIATED_EMAIL_TO_SPONSOR, parameterValues);
        getEmailService().sendMessage(getSponsorEmailAddress(registration), null, null, message);
    }

    private void sendRegistrationRevisionNotificationToInvestigator(InvestigatorRegistration registration,
            FirebirdUser coordinator) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION_COORDINATOR, coordinator.getPerson());
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
        FirebirdMessage message = getTemplateService().generateMessage(
                FirebirdMessageTemplate.REGISTRATION_REVISION_INITIATED_EMAIL_TO_INVESTIGATOR, parameterValues);
        getEmailService().sendMessage(registration.getProfile().getPerson().getEmail(), null, null, message);
    }

    private void sendRegistrationRevisionNotificationToCoordinators(InvestigatorRegistration registration,
            FirebirdUser... excludedCoordinators) {
        for (ManagedInvestigator managedInvestigator : registration.getProfile().getRegistrationCoordinatorMappings()) {
            if (!managedInvestigator.isSuspendedRegistrationAccess()
                    && !ArrayUtils.contains(excludedCoordinators, managedInvestigator.getUser())) {
                sendRegistrationRevisionNotificationToCoordinator(registration, managedInvestigator.getUser());
            }
        }
    }

    private void sendRegistrationRevisionNotificationToCoordinator(InvestigatorRegistration registration,
            FirebirdUser coordinator) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
        FirebirdMessage message = getTemplateService().generateMessage(
                FirebirdMessageTemplate.REGISTRATION_REVISION_INITIATED_EMAIL_TO_COORDINATOR, parameterValues);
        getEmailService().sendMessage(coordinator.getPerson().getEmail(), null, null, message);
    }

    @Override
    public InvestigatorRegistration cancelRevision(InvestigatorRegistration registration, FirebirdUser user) {
        checkArgument(user.canManageRegistration(registration),
                "Passed in user cannot cancel the registration revision");
        checkArgument(registration.isCancelable(), "Passed in InvestigatorRegistration is not cancelable");
        RevisedInvestigatorRegistration revisedRegistration = Iterables.getFirst(
                registration.getRevisedRegistrations(), null);
        removeNewlyAddedSubinvestigatorRegistrations(registration, revisedRegistration);
        InvestigatorRegistration nowActiveRegistration = revisedRegistration.cancelRevision();
        save(nowActiveRegistration);
        delete(registration);
        delete(revisedRegistration);
        sendRegistrationRevisionCanceledNotifications(registration);
        prepareForDeletion(registration, revisedRegistration);
        return nowActiveRegistration;
    }

    private void prepareForDeletion(InvestigatorRegistration registration,
            RevisedInvestigatorRegistration revisedRegistration) {
        registration.prepareForDeletion();
        revisedRegistration.prepareForDeletion();
    }

    private void removeNewlyAddedSubinvestigatorRegistrations(InvestigatorRegistration registration,
            RevisedInvestigatorRegistration revisedRegistration) {
        SetView<SubInvestigatorRegistration> newlyAddedSubinvestigatorRegistrations = Sets.difference(
                registration.getSubinvestigatorRegistrations(), revisedRegistration.getSubinvestigatorRegistrations());
        removeSubInvestigatorRegistrationAndNotify(Lists.newArrayList(newlyAddedSubinvestigatorRegistrations));
    }

    private void sendRegistrationRevisionCanceledNotifications(InvestigatorRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = Maps.newHashMap();
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
        FirebirdMessage message = getTemplateService().generateMessage(
                FirebirdMessageTemplate.REGISTRATION_REVISION_CANCELED_EMAIL_TO_SPONSOR, parameterValues);
        getEmailService().sendMessage(getSponsorEmailAddress(registration), null, null, message);
    }

    @Override
    protected Class<AbstractProtocolRegistration> getRegistrationClass() {
        return AbstractProtocolRegistration.class;
    }

    @SuppressWarnings("unchecked")
    // Hibernate's list() method is untyped
    @Override
    public List<AbstractProtocolRegistration> getByStatusForUser(RegistrationStatus status, FirebirdUser user,
            Set<String> groupNames) {
        checkNotNull(status);
        checkNotNull(user);
        checkNotNull(groupNames);
        List<Organization> verifiedSponsorOrganizations = user.getVerifiedSponsorOrganizations(groupNames);
        if (verifiedSponsorOrganizations.isEmpty()) {
            return Collections.emptyList();
        } else {
            String registrationQueryHql = "from " + AbstractProtocolRegistration.class.getName()
                    + " where status = :status and protocol.sponsor in (:sponsors)";
            Query query = getSession().createQuery(registrationQueryHql);
            query.setParameter("status", status);
            query.setParameterList("sponsors", verifiedSponsorOrganizations);
            return query.list();
        }
    }

}
