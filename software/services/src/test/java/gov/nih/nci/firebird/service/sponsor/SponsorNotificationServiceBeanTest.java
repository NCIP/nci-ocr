package gov.nih.nci.firebird.service.sponsor;

import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class SponsorNotificationServiceBeanTest {

    @Mock
    private TemplateService mockTemplateService;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private SponsorService mockSponsorService;

    private SponsorNotificationServiceBean bean = new SponsorNotificationServiceBean();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bean.setTemplateService(mockTemplateService);
        bean.setEmailService(mockEmailService);
        bean.setSponsorService(mockSponsorService);
    }

    @Test
    public void testNotifyInvestigatorsOfApproval() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();

        bean.notifyInvestigatorsOfApproval(registration);

        verify(mockSponsorService).getSponsorEmailAddress(registration.getProtocol().getSponsor());
        verify(mockTemplateService).generateMessage(eq(FirebirdMessageTemplate.REGISTRATION_PACKET_APPROVED_EMAIL),
                any(Map.class));
        verify(mockEmailService).sendMessage(any(Collection.class), any(Collection.class), anyString(),
                any(FirebirdMessage.class));
    }

    @Test
    public void testNotifyOfDeactivation() {
        String comments = "You are a poopyhead and we don't like you.";
        Protocol protocol = ProtocolFactory.getInstance().createWithForms();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                InvestigatorProfileFactory.getInstance().create(), protocol);

        bean.notifyOfDeactivation(registration, comments);

        verify(mockSponsorService).getSponsorEmailAddress(protocol.getSponsor());
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.REGISTRATION_PACKET_DEACTIVATED_EMAIL), any(Map.class));
        verify(mockEmailService).sendMessage(anyString(), anyCollection(), anyString(),
                any(FirebirdMessage.class));
    }

    @Test
    public void testNotifyOfReactivation() {
        Protocol protocol = ProtocolFactory.getInstance().createWithForms();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                InvestigatorProfileFactory.getInstance().create(), protocol);
        bean.notifyOfReactivation(registration, "These are the comments of failure");

        verify(mockSponsorService).getSponsorEmailAddress(protocol.getSponsor());
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.REGISTRATION_PACKET_REACTIVATED_EMAIL), any(Map.class));
        verify(mockEmailService).sendMessage(anyString(), anyCollection(), anyString(),
                any(FirebirdMessage.class));
    }
}
