package gov.nih.nci.firebird.service.messages.email;

import gov.nih.nci.firebird.service.messages.FirebirdEmail;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Abstract class for a message driven bean that listens for emails over a JMS queue and sends them. Implementations of
 * this class must define the queue for the message driven bean to subscribe to.
 */
abstract class AbstractEmailSender implements MessageListener {
    private static final Logger LOG = Logger.getLogger(AbstractEmailSender.class);
    private EmailService emailService;

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            FirebirdEmail firebirdEmail = (FirebirdEmail) objectMessage.getObject();
            emailService.sendMessage(firebirdEmail.getTo(), firebirdEmail.getCc(), firebirdEmail.getBounce(),
                    firebirdEmail.getMessage());
        } catch (JMSException e) {
            LOG.error("Could not extract FirebirdEmail out of incoming JMS message", e);
        }
    }

    @Inject
    void setEmailService(@Named("actualEmailService")
    EmailService emailService) {
        this.emailService = emailService;
    }
}