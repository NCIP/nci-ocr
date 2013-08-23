package gov.nih.nci.firebird.service.sponsor;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.ProtocolRevision;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.protocol.ProtocolUtil;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Service for sending notifications to Sponsors.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SponsorNotificationServiceBean  implements SponsorNotificationService {

    private EmailService emailService;
    private TemplateService templateService;
    private SponsorService sponsorService;

    @Override
    public void notifyOfDeactivation(AbstractProtocolRegistration registration, String comments) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.SPONSOR_EMAIL_ADDRESS, getSponsorEmailAddress(registration));
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.COMMENTS, RichTextUtil.convertToPlainText(comments));
        FirebirdMessage message = templateService.generateMessage(
                FirebirdMessageTemplate.REGISTRATION_PACKET_DEACTIVATED_EMAIL, parameterValues);
        emailService.sendMessage(registration.getProfile().getPerson().getEmail(), null, null, message);
    }

    @Override
    public void notifyOfReactivation(AbstractProtocolRegistration registration, String comments) {
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
    public void notifyInvestigatorsOfApproval(InvestigatorRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.SPONSOR_EMAIL_ADDRESS, getSponsorEmailAddress(registration));
        FirebirdMessage message = templateService.generateMessage(
                FirebirdMessageTemplate.REGISTRATION_PACKET_APPROVED_EMAIL, parameterValues);
        Set<String> addresses = ProtocolUtil.getAllEmailAddress(registration);
        emailService.sendMessage(addresses, null, null, message);
    }


    @Override
    public void sendPacketRemovedEmail(InvestigatorRegistration registration) {
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.INVESTIGATOR_REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.SPONSOR_EMAIL_ADDRESS, getSponsorEmailAddress(registration));
        FirebirdMessage message = templateService.generateMessage(FirebirdMessageTemplate.PACKET_REMOVED_EMAIL,
                parameterValues);
        emailService.sendMessage(registration.getProfile().getPerson().getEmail(), null, null, message);
    }

    @Override
    public void sendProtocolUpdateEmail(AbstractProtocolRegistration registration, ProtocolRevision revision) {
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

    private String getSponsorEmailAddress(AbstractProtocolRegistration registration) {
        return sponsorService.getSponsorEmailAddress(registration.getProtocol().getSponsor());
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


    @Resource(mappedName = "firebird/SponsorServiceBean/local")
    void setSponsorService(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

}
