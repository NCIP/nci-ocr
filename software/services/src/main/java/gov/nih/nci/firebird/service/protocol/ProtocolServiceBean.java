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

package gov.nih.nci.firebird.service.protocol;

import static com.google.common.base.Preconditions.*;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolRegistrationConfiguration;
import gov.nih.nci.firebird.data.ProtocolRevision;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.RegistrationType;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.AbstractGenericServiceBean;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Service for Investigational Protocols.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.ExcessiveClassLength" })
// facade service expected to expose many methods
public class ProtocolServiceBean extends AbstractGenericServiceBean<Protocol> implements ProtocolService {

    private static final Set<FormOptionality> SUBINVESTIGATOR_REQUIRED_OPTIONALITIES = Sets.newHashSet(
            FormOptionality.REQUIRED, FormOptionality.OPTIONAL);
    private static final String HQL_FROM_BASE = "from ";

    private FormTypeService formTypeService;
    private ProtocolRegistrationService registrationService;
    private EmailService emailService;
    private TemplateService templateService;
    private SponsorService sponsorService;
    private ResourceBundle resources;

    @Inject
    void setRegistrationService(ProtocolRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Inject
    void setFormTypeService(FormTypeService formTypeService) {
        this.formTypeService = formTypeService;
    }

    @Inject
    void setEmailService(@Named("jmsEmailService")
    EmailService emailService) {
        this.emailService = emailService;
    }

    @Inject
    void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Inject
    void setResources(ResourceBundle resources) {
        this.resources = resources;
    }

    @Inject
    void setSponsorService(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @Override
    public Protocol create() {
        Protocol protocol = new Protocol();
        addFormTypes(protocol.getRegistrationConfiguration());
        return protocol;
    }

    @Override
    public void addFormTypes(ProtocolRegistrationConfiguration configuration) {
        List<FormType> configurableFormTypes = formTypeService.getStandardConfigureableForms(RegistrationType.PROTOCOL);
        configurableFormTypes.addAll(formTypeService.getSupplementalForms(RegistrationType.PROTOCOL));
        for (FormType formType : configurableFormTypes) {
            configuration.setInvestigatorOptionality(formType, formType.getInvestigatorDefault());
            configuration.setSubinvestigatorOptionality(formType, formType.getSubinvestigatorDefault());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    // Hibernate list() method is untyped
    public List<ProtocolAgent> getAgents(String startOfName) {
        String agentQueryHql = HQL_FROM_BASE + ProtocolAgent.class.getName()
                + " where lower(name) like :startOfName order by lower(name)";
        Query query = getSessionProvider().get().createQuery(agentQueryHql);
        query.setString("startOfName", startOfName.toLowerCase(Locale.getDefault()) + "%");
        return query.list();
    }

    @Override
    protected void validate(Protocol protocol) throws ValidationException {
        validateNumber(protocol);
    }

    private void validateNumber(Protocol protocol) throws ValidationException {
        if (hasDuplicateProtocolNumber(protocol)) {
            String message = resources.getString("sponsor.protocol.duplicate.number.error");
            ValidationResult result = new ValidationResult(new ValidationFailure("protocol.protocolNumber", message));
            throw new ValidationException(result);
        }
    }

    @Override
    public boolean hasDuplicateProtocolNumber(Protocol protocol) {
        if (!isNew(protocol.getSponsor())) {
            Protocol existingProtocolWithNumber = getProtocol(protocol.getProtocolNumber(), protocol.getSponsor());
            return existingProtocolWithNumber != null && !areSame(protocol, existingProtocolWithNumber);
        } else {
            return false;
        }
    }

    private boolean isNew(PersistentObject persistentObject) {
        return persistentObject.getId() == null;
    }

    private boolean areSame(Protocol protocol, Protocol compareToExistingProtocol) {
        return compareToExistingProtocol.getId().equals(protocol.getId());
    }

    @Override
    public Long save(Protocol protocol) {
        handleAgents(protocol);
        return super.save(protocol);
    }

    private Protocol getProtocol(String protocolNumber, Organization sponsor) {
        String hql = HQL_FROM_BASE + Protocol.class.getName()
                + " where protocolNumber = :protocolNumber and sponsor = :sponsor";
        Query query = getSessionProvider().get().createQuery(hql);
        query.setString("protocolNumber", protocolNumber);
        query.setEntity("sponsor", sponsor);
        return (Protocol) query.uniqueResult();
    }

    private void handleAgents(Protocol protocol) {
        // Set below used to avoid conflict when changes are automatically flushed for getAgentbyName() query
        Set<ProtocolAgent> incomingAgentSet = new HashSet<ProtocolAgent>();
        incomingAgentSet.addAll(protocol.getAgents());
        protocol.getAgents().clear();
        for (ProtocolAgent agent : incomingAgentSet) {
            handleAgent(protocol, agent);
        }
    }

    private void handleAgent(Protocol protocol, ProtocolAgent agent) {
        if (agent.getId() != null) {
            protocol.getAgents().add(agent);
        } else {
            ProtocolAgent existingAgent = getAgentByName(agent.getName());
            if (existingAgent == null) {
                getSessionProvider().get().save(agent);
                protocol.getAgents().add(agent);
            } else {
                protocol.getAgents().add(existingAgent);
            }
        }
    }

    private ProtocolAgent getAgentByName(String agentName) {
        String agentQueryHql = HQL_FROM_BASE + ProtocolAgent.class.getName() + " where lower(name) = :agentName";
        Query query = getSessionProvider().get().createQuery(agentQueryHql);
        query.setString("agentName", agentName.toLowerCase(Locale.getDefault()));
        return (ProtocolAgent) query.uniqueResult();
    }

    @Override
    public InvestigatorRegistration addInvestigator(Protocol protocol, Person investigator) throws ValidationException {
        return registrationService.createInvestigatorRegistration(protocol, investigator);
    }

    @Override
    public void invite(InvestigatorRegistration registration) {
        registrationService.inviteToRegistration(registration);
    }

    @Override
    @SuppressWarnings("unchecked")
    // Hibernate only returns untyped collections
    public List<Protocol> getProtocols(FirebirdUser user, Set<String> groupNames) {
        checkNotNull(user);
        checkNotNull(groupNames);
        List<Organization> verifiedSponsorOrganizations = user.getVerifiedSponsorOrganizations(groupNames);
        if (verifiedSponsorOrganizations.isEmpty()) {
            return Collections.emptyList();
        } else {
            String protocolQueryHql = HQL_FROM_BASE + Protocol.class.getName() + " where sponsor in (:sponsors)";
            Query query = getSessionProvider().get().createQuery(protocolQueryHql);
            query.setParameterList("sponsors", verifiedSponsorOrganizations);
            return query.list();
        }
    }

    @Override
    public void approvePacket(InvestigatorRegistration registration) {
        if (!registration.isApprovable()) {
            throw new IllegalArgumentException("Registration is not in an approvable state");
        }
        markAllApproved(registration);
        notifyInvestigatorsOfApproval(registration);
        registrationService.save(registration);
    }

    private void markAllApproved(InvestigatorRegistration registration) {
        markApproved(registration);
        for (SubInvestigatorRegistration subInvestigatorRegistration : registration.getSubinvestigatorRegistrations()) {
            if (!RegistrationStatus.APPROVED.equals(subInvestigatorRegistration.getStatus())) {
                markApproved(subInvestigatorRegistration);
            }
        }
    }

    private void markApproved(AbstractProtocolRegistration registration) {
        registration.setStatus(RegistrationStatus.APPROVED);
        for (AbstractRegistrationForm form : registration.getForms()) {
            form.setFormStatus(FormStatus.APPROVED);
        }
    }

    @Override
    public void deactivatePacket(InvestigatorRegistration registration, String comments) {
        if (StringUtils.isNotEmpty(comments)) {
            deactivateRegistration(registration, comments);
            for (SubInvestigatorRegistration subRegistration : registration.getSubinvestigatorRegistrations()) {
                deactivateRegistration(subRegistration, comments);
            }
        } else {
            throw new IllegalArgumentException("Comments must be entered when deactivating a registration packet!");
        }
    }

    private void deactivateRegistration(AbstractProtocolRegistration registration, String comments) {
        registration.setStatus(RegistrationStatus.INACTIVE);
        for (AbstractRegistrationForm form : registration.getForms()) {
            form.setFormStatus(FormStatus.INACTIVE);
            form.setComments(null);
        }
        registrationService.save(registration);
        notifyOfDeactivation(registration, comments);
    }

    @Override
    public void reactivatePacket(InvestigatorRegistration registration, String comments) {
        checkArgument(registration.getStatus() == RegistrationStatus.INACTIVE,
                "Failed to re-activate registration packet due to it being " + registration.getStatus()
                        + " instead of INACTIVE.");
        if (registration.getApprovalDate() != null) {
            reactivateApprovedPacket(registration, comments);
        } else {
            reactivateUnApprovedPacket(registration, comments);
        }
    }

    private void reactivateApprovedPacket(InvestigatorRegistration registration, String comments) {
        InvestigatorRegistration reactivatedRegistration = (InvestigatorRegistration) reactivateApprovedRegistration(
                registration, comments);
        reactivatedRegistration.getSubinvestigatorRegistrations().clear();
        for (SubInvestigatorRegistration subRegistration : registration.getSubinvestigatorRegistrations()) {
            SubInvestigatorRegistration subInvestigatorRegistration =
                    (SubInvestigatorRegistration) reactivateApprovedRegistration(subRegistration, comments);
            reactivatedRegistration.addSubinvestigatorRegistration(subInvestigatorRegistration);
        }
    }

    private AbstractProtocolRegistration reactivateApprovedRegistration(AbstractProtocolRegistration registration,
            String comments) {
        AbstractProtocolRegistration reactivatedRegistration = registration.isInvestigatorRegistration()
                ? new InvestigatorRegistration() : new SubInvestigatorRegistration();
        reactivatedRegistration.copyFrom(registration);
        registration.setCurrentRegistration(reactivatedRegistration);
        reactivatedRegistration.setStatus(RegistrationStatus.IN_PROGRESS);
        reactivatedRegistration.setFormStatuses(FormStatus.IN_PROGRESS);
        reactivatedRegistration.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);

        registrationService.save(reactivatedRegistration);
        registrationService.save(registration);
        notifyOfReactivation(registration, comments);
        return reactivatedRegistration;
    }

    private void reactivateUnApprovedPacket(InvestigatorRegistration registration, String comments) {
        reactivateUnApprovedRegistration(registration, comments);
        for (SubInvestigatorRegistration subRegistration : registration.getSubinvestigatorRegistrations()) {
            reactivateUnApprovedRegistration(subRegistration, comments);
        }
    }

    private void reactivateUnApprovedRegistration(AbstractProtocolRegistration registration, String comments) {
        if (InvitationStatus.NO_RESPONSE == registration.getInvitation().getInvitationStatus()
                || InvitationStatus.NOT_INVITED == registration.getInvitation().getInvitationStatus()) {
            registration.setStatus(RegistrationStatus.NOT_STARTED);
            registration.setFormStatuses(FormStatus.NOT_STARTED);
        } else {
            registration.setStatus(RegistrationStatus.IN_PROGRESS);
            registration.getInvitation().setInvitationStatus(InvitationStatus.REACTIVATED);
            registration.getInvitation().setInvitationChangeDate(new Date());
            registration.setFormStatuses(FormStatus.IN_PROGRESS);
        }
        Set<PersistentObject> objectsToDelete = registration.prepareFormsForReturn();
        deleteAll(objectsToDelete);
        registrationService.save(registration);
        notifyOfReactivation(registration, comments);
    }

    @Override
    public void removePacket(InvestigatorRegistration registration) {
        if (registration.getStatus() != RegistrationStatus.APPROVED) {
            registrationService.delete(registration);
            sendPacketRemovedEmail(registration);
        } else {
            throw new IllegalStateException("Cannot remove an investigator if the registration status is approved.");
        }
    }

  private void sendPacketRemovedEmail(InvestigatorRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.SPONSOR_EMAIL_ADDRESS, getSponsorEmailAddress(registration));
        FirebirdMessage message = templateService.generateMessage(FirebirdMessageTemplate.PACKET_REMOVED_EMAIL,
                parameterValues);
        emailService.sendMessage(registration.getProfile().getPerson().getEmail(), null, null, message);
    }

    private void notifyInvestigatorsOfApproval(InvestigatorRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.SPONSOR_EMAIL_ADDRESS, getSponsorEmailAddress(registration));
        FirebirdMessage message = templateService.generateMessage(
                FirebirdMessageTemplate.REGISTRATION_PACKET_APPROVED_EMAIL, parameterValues);
        Set<String> addresses = getAllEmailAddress(registration);
        emailService.sendMessage(addresses, null, null, message);
    }

    private String getSponsorEmailAddress(AbstractProtocolRegistration registration) {
        return sponsorService.getSponsorEmailAddress(registration.getProtocol().getSponsor());
    }

    private Set<String> getAllEmailAddress(InvestigatorRegistration registration) {
        Set<String> addresses = new HashSet<String>();
        addresses.add(registration.getProfile().getPerson().getEmail());
        for (SubInvestigatorRegistration subInvestigatorRegistration : registration.getSubinvestigatorRegistrations()) {
            addresses.add(subInvestigatorRegistration.getProfile().getPerson().getEmail());
        }
        return addresses;
    }

    private void notifyOfDeactivation(AbstractProtocolRegistration registration, String comments) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.SPONSOR_EMAIL_ADDRESS, getSponsorEmailAddress(registration));
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.COMMENTS, RichTextUtil.convertToPlainText(comments));
        FirebirdMessage message = templateService.generateMessage(
                FirebirdMessageTemplate.REGISTRATION_PACKET_DEACTIVATED_EMAIL, parameterValues);
        emailService.sendMessage(registration.getProfile().getPerson().getEmail(), null, null, message);
    }

    private void notifyOfReactivation(AbstractProtocolRegistration registration, String comments) {
        String url = FirebirdConstants.REGISTRATION_URL_PATH_WITH_ID_PARAM + registration.getId();
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK, url);
        parameterValues.put(FirebirdTemplateParameter.SPONSOR_EMAIL_ADDRESS, getSponsorEmailAddress(registration));
        parameterValues.put(FirebirdTemplateParameter.COMMENTS, RichTextUtil.convertToPlainText(comments));
        FirebirdMessage message = templateService.generateMessage(
                FirebirdMessageTemplate.REGISTRATION_PACKET_REACTIVATED_EMAIL, parameterValues);
        emailService.sendMessage(registration.getProfile().getPerson().getEmail(), null, null, message);
    }

    @Override
    public void updateProtocol(Protocol original, Protocol revised, String comment) throws ValidationException {
        ProtocolModificationDetector detector = new ProtocolModificationDetector(original, revised, resources);
        if (!detector.isModificationDetected()) {
            return;
        }
        validateChanges(revised, comment);
        ProtocolRevision revision = new ProtocolRevision();
        revision.setDate(new Date());
        revision.setComment(comment);
        revised.addRevision(revision);
        detector.addModifications(revision);
        validateAndSave(revised);
        getSessionProvider().get().flush();
        handleAffectedRegistrations(revised, revision);
    }

    private void validateChanges(Protocol revised, String comment) throws ValidationException {
        ValidationResult validationResults = new ValidationResult();
        checkComments(comment, validationResults);
        checkInvestigatorOptionalities(revised, validationResults);
        checkSubInvestigatorOptionalities(revised, validationResults);
        if (!validationResults.isValid()) {
            getSessionProvider().get().evict(revised);
            throw new ValidationException(validationResults);
        }
    }

    private void checkComments(String comment, ValidationResult validationResults) {
        if (StringUtils.isBlank(comment)) {
            validationResults.addFailure(new ValidationFailure("comment", resources
                    .getString("protocol.change.comment.required")));
        }
    }

    private void checkInvestigatorOptionalities(Protocol revised, ValidationResult validationResults) {
        Collection<FormOptionality> optionalities = revised.getRegistrationConfiguration()
                .getInvestigatorConfiguration().getFormOptionalities().values();
        if (!optionalities.contains(FormOptionality.REQUIRED)) {
            validationResults.addFailure(new ValidationFailure(resources
                    .getString("protocol.investigator.optionalities.require.one.required.form")));
        }
    }

    private void checkSubInvestigatorOptionalities(Protocol revised, ValidationResult validationResults) {
        Collection<FormOptionality> optionalities = revised.getRegistrationConfiguration()
                .getSubinvestigatorConfiguration().getFormOptionalities().values();
        if (!CollectionUtils.containsAny(optionalities, SUBINVESTIGATOR_REQUIRED_OPTIONALITIES)) {
            String message = resources
                    .getString("protocol.subinvestigator.optionalities.require.one.required.or.optional.form");
            validationResults.addFailure(new ValidationFailure(message));
        }
    }

    private void handleAffectedRegistrations(Protocol protocol, ProtocolRevision revision) {
        for (AbstractProtocolRegistration registration : protocol.getAllRegistrations()) {
            Set<AbstractRegistrationForm> removedForms = registration.configureForms();
            deleteForms(removedForms);
            if (registration.isNotificationRequiredForUpdate()) {
                sendProtocolUpdateEmail(registration, revision);
            }
            if (registration.isResubmitRequiredForUpdate()) {
                registration.setStatus(RegistrationStatus.PROTOCOL_UPDATED);
                setFormsToInProgress(registration);
                Set<PersistentObject> objectsToDelete = registration.prepareFormsForReturn();
                deleteAll(objectsToDelete);
                registrationService.save(registration);
            }
        }
    }

    private void deleteForms(Set<AbstractRegistrationForm> removedForms) {
        for (AbstractRegistrationForm removedForm : removedForms) {
            getSessionProvider().get().delete(removedForm);
        }
    }

    private void setFormsToInProgress(AbstractProtocolRegistration registration) {
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            form.setFormStatus(FormStatus.IN_PROGRESS);
        }
    }

    private void sendProtocolUpdateEmail(AbstractProtocolRegistration registration, ProtocolRevision revision) {
        String url = FirebirdConstants.REGISTRATION_URL_PATH_WITH_ID_PARAM + registration.getId();
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK, url);
        parameterValues.put(FirebirdTemplateParameter.PROTOCOL_REVISION, revision);
        parameterValues.put(FirebirdTemplateParameter.SPONSOR_EMAIL_ADDRESS, getSponsorEmailAddress(registration));
        FirebirdMessage message = templateService.generateMessage(FirebirdMessageTemplate.PROTOCOL_MODIFIED_EMAIL,
                parameterValues);
        emailService.sendMessage(registration.getProfile().getPerson().getEmail(), null, null, message);
    }

}
