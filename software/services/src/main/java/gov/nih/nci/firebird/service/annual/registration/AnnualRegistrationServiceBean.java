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
package gov.nih.nci.firebird.service.annual.registration;

import static com.google.common.base.Preconditions.*;
import static gov.nih.nci.firebird.common.FirebirdDateUtils.*;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.*;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.AbstractAnnualRegistrationForm;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AdditionalAttachmentsForm;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.AnnualRegistrationType;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.InvestigatorStatus;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.registration.AbstractBaseRegistrationServiceBean;

import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Query;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Provides persistence functionality and other services for investigator annual registrations.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SuppressWarnings("PMD.TooManyMethods")
// requires many supporting methods for readability
public class AnnualRegistrationServiceBean extends AbstractBaseRegistrationServiceBean<AnnualRegistration> implements
        AnnualRegistrationService {

    private static final String REGISTRATIONS_REQUIRING_RENEWAL_HQL = "from "
            + AnnualRegistration.class.getName()
            + " where renewed = false and renewalDate <= :renewalDate "
            + "and profile.user.investigatorRole.status not in ('"
            + InvestigatorStatus.WITHDRAWN + "','" + InvestigatorStatus.DISQUALIFIED + "')";
    private static final String UNSUBMITTED_WITHOUT_REMINDER_SENT_REGISTRATIONS_WITH_DUE_DATES_BEFORE_DATE_HQL = "from "
            + AnnualRegistration.class.getName()
            + " where lastSubmissionDate is null and secondRenewalNotificationSent = false and dueDate <= :date "
            + "and profile.user.investigatorRole.status  not in ('"
            + InvestigatorStatus.WITHDRAWN + "','" + InvestigatorStatus.DISQUALIFIED + "')";

    private AnnualRegistrationConfigurationService configurationService;
    private Integer daysBeforeDueDateToSendFirstNotification;
    private Integer daysBeforeDueDateToSendSecondNotification;
    private Integer daysAfterDueDateToUseAsBasisForRenewalDate;

    @Resource(mappedName = "firebird/AnnualRegistrationConfigurationServiceBean/local")
    void setConfigurationService(AnnualRegistrationConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Inject
    void setDaysBeforeDueDateToSendFirstNotification(
            @Named("annual.registration.due.first.notification.days") int daysBeforeDueDateToSendFirstNotification) {
        this.daysBeforeDueDateToSendFirstNotification = daysBeforeDueDateToSendFirstNotification;
    }

    @Override
    public Integer getDaysBeforeDueDateToSendFirstNotification() {
        return daysBeforeDueDateToSendFirstNotification;
    }

    @Inject
    void setDaysBeforeDueDateToSendSecondNotification(
            @Named("annual.registration.due.second.notification.days") int daysBeforeDueDateToSendSecondNotification) {
        this.daysBeforeDueDateToSendSecondNotification = daysBeforeDueDateToSendSecondNotification;
    }

    @Override
    public Integer getDaysBeforeDueDateToSendSecondNotification() {
        return daysBeforeDueDateToSendSecondNotification;
    }

    @Inject
    void setDaysAfterDueDateToUseAsBasisForRenewalDate(
            @Named("annual.registration.renewal.based.on.due.date.days")
            int daysAfterDueDateToUseAsBasisForRenewalDate) {
        this.daysAfterDueDateToUseAsBasisForRenewalDate = daysAfterDueDateToUseAsBasisForRenewalDate;
    }

    @Override
    public AnnualRegistration createRegistration(InvestigatorProfile profile) {
        checkArgument(profile.canCreateAnnualRegistration(), "Cannot create annual registration for profile");
        if (profile.getAnnualRegistrations().isEmpty()) {
            return createInitial(profile);
        } else {
            return createRenewal(profile.getCurrentAnnualRegistration());
        }
    }

    /**
     * Creates the initial annual registration for an investigator.
     *
     * @param profile the investigator's profile.
     * @return the new registration.
     */
    AnnualRegistration createInitial(InvestigatorProfile profile) {
        AnnualRegistration registration = createRegistrationWithCurrentConfiguration(profile,
                AnnualRegistrationType.INITIAL);
        save(registration);
        return registration;
    }

    private AnnualRegistration createRegistrationWithCurrentConfiguration(InvestigatorProfile profile,
            AnnualRegistrationType type) {
        AnnualRegistration registration = new AnnualRegistration();
        registration.setAnnualRegistrationType(type);
        registration.setStatus(RegistrationStatus.NOT_STARTED);
        registration.setConfiguration(configurationService.getCurrentConfiguration());
        registration.setProfile(profile);
        profile.addRegistration(registration);
        registration.configureForms();
        return registration;
    }

    AnnualRegistration createRenewal(AnnualRegistration registration) {
        AnnualRegistration renewalRegistration = new AnnualRegistration();
        renewalRegistration.setAnnualRegistrationType(AnnualRegistrationType.RENEWAL);
        renewalRegistration.setStatus(RegistrationStatus.NOT_STARTED);
        renewalRegistration.setConfiguration(configurationService.getCurrentConfiguration());
        renewalRegistration.setDueDate(registration.getRenewalDate());
        InvestigatorProfile profile = registration.getProfile();
        profile.addRegistration(renewalRegistration);
        renewalRegistration.configureForms();
        copyOldFormsDataIntoRenewalRegistration(registration, renewalRegistration);
        save(renewalRegistration);
        return renewalRegistration;
    }

    private void copyOldFormsDataIntoRenewalRegistration(AnnualRegistration registration,
            AnnualRegistration renewalRegistration) {
        for (AbstractRegistrationForm form : renewalRegistration.getForms()) {
            AbstractRegistrationForm oldForm = registration.getForm(form.getFormType());
            if (oldForm != null) {
                if (form instanceof AbstractAnnualRegistrationForm) {
                    ((AbstractAnnualRegistrationForm) form).copyForm(oldForm);
                } else if (form instanceof AdditionalAttachmentsForm) {
                    ((AdditionalAttachmentsForm) form).copyForm((AdditionalAttachmentsForm) oldForm);
                }
            }
        }
    }

    @Override
    public Set<AnnualRegistration> getRegistrations(InvestigatorProfile profile) {
        return profile.getAnnualRegistrations();
    }

    @Override
    public FirebirdMessage getCoordinatorCompletedRegistrationEmailMessage(FirebirdUser coordinator,
            AnnualRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.ANNUAL_REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK, generateRegistrationLink(registration));
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION_COORDINATOR, coordinator.getPerson());
        return getTemplateService().generateMessage(
                FirebirdMessageTemplate.COORDINATOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL, parameterValues);
    }

    private String generateRegistrationLink(AnnualRegistration registration) {
        return FirebirdConstants.ANNUAL_REGISTRATION_URL_PATH_WITH_ID_PARAM + registration.getId();
    }

    @Override
    protected void sendSubmissionNotifications(AnnualRegistration registration) {
        getSponsorService().notifySubmittedRegistration(registration);
        Set<String> emailAddresses = registration.getApprovedCoordinatorAndNotificationListEmailAddresses();
        sendCoordinatorAndNotificationListEmailMessage(registration, emailAddresses);
    }

    private void sendCoordinatorAndNotificationListEmailMessage(AnnualRegistration registration,
            Set<String> toEmailAddresses) {
        FirebirdMessage submissionNotificationEmail = getCoordinatorAndNotificationListEmailMessage(registration);
        String investigatorEmailAddress = registration.getProfile().getPerson().getEmail();
        for (String emailAddress : toEmailAddresses) {
            getEmailService().sendMessage(emailAddress, null, investigatorEmailAddress, submissionNotificationEmail);
        }
    }

    private FirebirdMessage getCoordinatorAndNotificationListEmailMessage(AnnualRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(ANNUAL_REGISTRATION, registration);
        return getTemplateService().generateMessage(
                FirebirdMessageTemplate.COORDINATOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL, parameterValues);
    }

    @Override
    public void createPendingRenewals() {
        Iterable<AnnualRegistration> registrationsRequiringRenewals = getRegistrationsRequiringRenewal();
        createRenewals(registrationsRequiringRenewals);
        sendInitialRenewalNotices(registrationsRequiringRenewals);
    }

    @SuppressWarnings("unchecked")
    // Hibernate list() method is untyped
    private List<AnnualRegistration> getRegistrationsRequiringRenewal() {
        Date renewalDate = DateUtils.addDays(new Date(), daysBeforeDueDateToSendFirstNotification);
        Query query = getSession().createQuery(REGISTRATIONS_REQUIRING_RENEWAL_HQL);
        query.setDate("renewalDate", renewalDate);
        return query.list();
    }

    private void createRenewals(Iterable<AnnualRegistration> registrationsRequiringRenewal) {
        for (AnnualRegistration registrationRequiringRenewal : registrationsRequiringRenewal) {
            registrationRequiringRenewal.setRenewal(createRenewal(registrationRequiringRenewal));
        }
    }

    private void sendInitialRenewalNotices(Iterable<AnnualRegistration> registrationsRequiringRenewals) {
        for (AnnualRegistration registration : registrationsRequiringRenewals) {
            if (investigatorHasRegistrationCoordinators(registration)) {
                FirebirdMessage notificationEmail = generateInitialNotice(registration);
                sendEmailToRegistrationCoordinators(registration, notificationEmail);
            }
        }
    }

    private boolean investigatorHasRegistrationCoordinators(AnnualRegistration registration) {
        return !registration.getProfile().getRegistrationCoordinatorMappings().isEmpty();
    }

    private FirebirdMessage generateInitialNotice(AnnualRegistration registration) {
        Map<FirebirdTemplateParameter, Object> values = Maps.newHashMap();
        values.put(FirebirdTemplateParameter.ANNUAL_REGISTRATION, registration);
        String renewalLink = generateRegistrationLink(registration.getRenewal());
        values.put(FirebirdTemplateParameter.FIREBIRD_LINK, renewalLink);
        FirebirdMessage notificationEmail = getTemplateService().generateMessage(
                FirebirdMessageTemplate.ANNUAL_REGISTRATION_RENEWAL_SIXTY_DAY_NOTICE_EMAIL, values);
        return notificationEmail;
    }

    private void sendEmailToRegistrationCoordinators(AnnualRegistration registration, FirebirdMessage email) {
        for (ManagedInvestigator coordinatorMapping : registration.getProfile().getRegistrationCoordinatorMappings()) {
            String coordinatorEmail = coordinatorMapping.getUser().getPerson().getEmail();
            getEmailService().sendMessage(coordinatorEmail, null, null, email);
        }
    }

    @Override
    public void sendRenewalReminders() {
        sendRenewalReminders(getUnSubmittedWithoutReminderSentRegistrationsWithinSecondNotificationWindow());
    }

    @SuppressWarnings("unchecked")
    // Hibernate list() method is untyped
    private List<AnnualRegistration> getUnSubmittedWithoutReminderSentRegistrationsWithinSecondNotificationWindow() {
        Query query = getSession().createQuery(
                UNSUBMITTED_WITHOUT_REMINDER_SENT_REGISTRATIONS_WITH_DUE_DATES_BEFORE_DATE_HQL);
        Date date = DateUtils.addDays(new Date(), daysBeforeDueDateToSendSecondNotification);
        query.setDate("date", date);
        return query.list();
    }

    private void sendRenewalReminders(Collection<AnnualRegistration> registrationsWithinSecondNotificationWindow) {
        for (AnnualRegistration registration : registrationsWithinSecondNotificationWindow) {
            sendRenewalReminders(registration);
        }
    }

    private void sendRenewalReminders(AnnualRegistration registration) {
        sendRenewalReminderToInvestigator(registration);
        sendRenewalReminderToCoordinators(registration);
        registration.setSecondRenewalNotificationSent(true);
    }

    private void sendRenewalReminderToInvestigator(AnnualRegistration registration) {
        FirebirdMessage message = createRenewalReminderEmailToInvestigator(registration);
        String investigatorEmail = registration.getProfile().getPerson().getEmail();
        getEmailService().sendMessage(investigatorEmail, null, null, message);
    }

    private FirebirdMessage createRenewalReminderEmailToInvestigator(AnnualRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK, generateRegistrationLink(registration));
        return getTemplateService().generateMessage(
                FirebirdMessageTemplate.ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_INVESTIGATOR,
                parameterValues);
    }

    private void sendRenewalReminderToCoordinators(AnnualRegistration registration) {
        if (investigatorHasRegistrationCoordinators(registration)) {
            FirebirdMessage message = createRenewalReminderEmailToCoordinator(registration);
            sendEmailToRegistrationCoordinators(registration, message);
        }
    }

    private FirebirdMessage createRenewalReminderEmailToCoordinator(AnnualRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK, generateRegistrationLink(registration));
        parameterValues.put(FirebirdTemplateParameter.ANNUAL_REGISTRATION, registration);
        return getTemplateService().generateMessage(
                FirebirdMessageTemplate.ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_COORDINATOR,
                parameterValues);
    }

    @Override
    public void submitRegistration(AnnualRegistration registration) {
        registration.setStatus(RegistrationStatus.SUBMITTED);
        setRenewalDateIfNecessary(registration);
    }

    void setRenewalDateIfNecessary(AnnualRegistration registration) {
        if (registration.getRenewalDate() == null) {
            if (useDueDateForRenewalDate(registration)) {
                registration.setRenewalDate(DateUtils.addYears(registration.getDueDate(), 1));
            } else {
                registration.setRenewalDate(DateUtils.addYears(new Date(), 1));
            }
        }
    }

    private boolean useDueDateForRenewalDate(AnnualRegistration registration) {
        Date dueDate = registration.getDueDate();
        if (dueDate == null) {
            return false;
        } else {
            return isCurrentDateBeforeAndWithinRangeOfDate(dueDate, daysBeforeDueDateToSendFirstNotification)
                || isCurrentDateAfterDueDateAndWithinRenewalRange(dueDate);
        }
    }

    private boolean isCurrentDateAfterDueDateAndWithinRenewalRange(Date dueDate) {
        Date now = new Date();
        Date boundaryDate = DateUtils.addDays(dueDate, daysAfterDueDateToUseAsBasisForRenewalDate);
        return now.before(boundaryDate) && now.after(dueDate);
    }

    @Override
    public void sendCoordinatorCompletedRegistrationEmail(FirebirdUser userCompletedBy,
            AnnualRegistration registration) {
        ManagedInvestigator coordinatorMapping = registration.getProfile().getCtepRegistrationCoordinatorMapping();
        if (coordinatorMapping != null && !coordinatorMapping.getUser().equals(userCompletedBy)) {
            sendSponsorCompletedRegistrationEmail(userCompletedBy, coordinatorMapping.getUser(), registration);
        }
        super.sendCoordinatorCompletedRegistrationEmail(userCompletedBy, registration);
    }

    private void sendSponsorCompletedRegistrationEmail(FirebirdUser sponsor, FirebirdUser coordinator,
            AnnualRegistration registration) {
        String coordinatorEmail = coordinator.getPerson().getEmail();
        FirebirdMessage message = getSponsorCompletedRegistrationEmailMessage(sponsor, coordinator, registration);
        getEmailService().sendMessage(coordinatorEmail, null, null, message);
    }

    private FirebirdMessage getSponsorCompletedRegistrationEmailMessage(FirebirdUser sponsor, FirebirdUser coordinator,
            AnnualRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.ANNUAL_REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK, generateRegistrationLink(registration));
        parameterValues.put(FirebirdTemplateParameter.SPONSOR, sponsor.getPerson());
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION_COORDINATOR, coordinator.getPerson());
        return getTemplateService().generateMessage(
                FirebirdMessageTemplate.SPONSOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL, parameterValues);
    }

    @Override
    public AnnualRegistration createReactivatedRegistration(InvestigatorProfile profile) {
        AnnualRegistration registration = createRegistrationWithCurrentConfiguration(profile,
                AnnualRegistrationType.REACTIVATED);
        save(registration);
        return registration;
    }

    @Override
    public void removeUnFinalizedRegistrations(FirebirdUser investigator) {
        checkArgument(investigator.isInvestigator(), "User is not an investigator");
        Set<AnnualRegistration> registrations = investigator.getInvestigatorRole().getProfile()
                .getAnnualRegistrations();
        for (AnnualRegistration registration : registrations) {
            if (!registration.isFinalized()) {
                registration.prepareForDeletion();
                delete(registration);
            }
        }
    }

    @Override
    public void delete(AnnualRegistration registration, FirebirdUser currentUser) {
        checkArgument(isCurrentUsersRegistration(registration, currentUser),
                "Only investigator can delete their registration");
        removeRenewalReferences(registration);
        super.delete(registration);
        sendRegistrationDeletionEmailToCoordinator(registration);
    }

    private boolean isCurrentUsersRegistration(AnnualRegistration registration, FirebirdUser currentUser) {
        return currentUser.isInvestigator()
                && currentUser.getInvestigatorRole().getProfile().equals(registration.getProfile());
    }

    private void removeRenewalReferences(AnnualRegistration registration) {
        if (registration.getParent() != null) {
            registration.getParent().setRenewal(null);
            registration.setParent(null);
        }
    }

    private void sendRegistrationDeletionEmailToCoordinator(AnnualRegistration registration) {
        if (registration.getProfile().hasCtepRegistrationCoordinator()) {
            String coordinatorEmail = registration.getProfile().getCtepRegistrationCoordinatorMapping().getUser()
                    .getPerson().getEmail();
            Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                    FirebirdTemplateParameter.class);
            parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR, registration.getProfile().getPerson());
            FirebirdMessage message = getTemplateService().generateMessage(
                    FirebirdMessageTemplate.INVESTIGATOR_DELETED_ANNUAL_REGISTRATION_EMAIL_TO_OTHERS, parameterValues);
            getEmailService().sendMessage(coordinatorEmail, null, null, message);
        }
    }

    @Override
    protected Class<AnnualRegistration> getRegistrationClass() {
        return AnnualRegistration.class;
    }

}
