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
package gov.nih.nci.firebird.service.messages.email;

import gov.nih.nci.firebird.service.messages.FirebirdMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.mail.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * A Service Bean for sending emails.
 */
public class EmailServiceImpl implements EmailService {

    private static final Logger LOG = Logger.getLogger(EmailServiceImpl.class);
    static final String LINE_SEPARATOR = StringUtils.repeat("-", 50);
    static final String TO_OVERRIDE_HEADING = "Originally sent to:\n";
    static final String CC_OVERRIDE_HEADING = "Originally cc'ed to:\n";

    private Session mailSession;
    private String overrideEmailAddress;
    private String senderEmail;
    private String senderName;
    private Boolean sendMail;

    @Override
    public void sendMessage(String to, Collection<String> cc, String bounce, FirebirdMessage message) {
        sendMessage(Collections.singleton(to), cc, bounce, message);
    }

    @Override
    public void sendMessage(Collection<String> to, Collection<String> cc, String bounce, FirebirdMessage message) {
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("message has no TO address");
        }
        try {
            Email email = prepareSimpleMessage(to, cc, message);
            sendEmail(email, bounce);
            LOG.info("Sent email with subject \"" + message.getSubject() + "\" to recipients: " + to);
        } catch (EmailException e) {
            LOG.warn("An Error occurred while sending the email with subject \""
                    + message.getSubject() + "\" to recipients: " + to);
            throw new IllegalStateException("An Error occurred while sending the email with subject \""
                    + message.getSubject() + "\" to recipients: " + to, e);
        }
    }

    private synchronized void sendEmail(Email email, String bounce) throws EmailException {
        String originalMailSmtpFrom = getMailSmtpFrom();
        try {
            handleMailSmtpFromAddress(bounce);
            sendEmail(email);
        } finally {
            handleMailSmtpFromAddress(originalMailSmtpFrom);
        }
    }

    private String getMailSmtpFrom() {
        return mailSession.getProperty(Email.MAIL_SMTP_FROM);
    }

    private void handleMailSmtpFromAddress(String mailSmtpFrom) {
        if (StringUtils.isNotBlank(mailSmtpFrom)) {
            mailSession.getProperties().setProperty(Email.MAIL_SMTP_FROM, mailSmtpFrom);
        } else if (mailSession.getProperties().containsKey(Email.MAIL_SMTP_FROM)) {
            mailSession.getProperties().remove(Email.MAIL_SMTP_FROM);
        }
    }

    private void sendEmail(Email email) throws EmailException {
        initEmailInfo(email);
        if (sendMail) {
            email.send();
        } else {
            email.buildMimeMessage();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Email Sending is off. " + email.getMimeMessage());
            }
        }
    }

    private Email prepareSimpleMessage(Collection<String> to, Collection<String> cc, FirebirdMessage message)
            throws EmailException {
        Email email = new SimpleEmail();
        email.setSentDate(new Date());
        email.setSubject(message.getSubject());
        if (StringUtils.isBlank(overrideEmailAddress)) {
            addToAddresses(email, to);
            addCcAddresses(email, cc);
            email.setMsg(message.getBody());
        } else {
            email.addTo(overrideEmailAddress);
            email.setMsg(buildOverriddenEmailBody(to, cc, message));
        }
        return email;
    }

    private void addToAddresses(Email email, Collection<String> to) throws EmailException {
        for (String address : to) {
            if (StringUtils.isNotBlank(address)) {
                email.addTo(address);
            } else {
                throw new IllegalArgumentException("blank TO address");
            }
        }
    }

    private void addCcAddresses(Email email, Collection<String> cc) throws EmailException {
        if (cc != null && !cc.isEmpty()) {
            for (String address : cc) {
                if (StringUtils.isNotBlank(address)) {
                    email.addCc(address);
                }
            }
        }
    }

    private String buildOverriddenEmailBody(Collection<String> to, Collection<String> cc, FirebirdMessage message) {
        StringBuilder bodyText = new StringBuilder(TO_OVERRIDE_HEADING);
        for (String address : to) {
            bodyText.append(address);
            bodyText.append("\n");
        }
        if (cc != null && !cc.isEmpty()) {
            bodyText.append(CC_OVERRIDE_HEADING);
            for (String address : cc) {
                bodyText.append(address);
                bodyText.append("\n");
            }
        }
        bodyText.append(LINE_SEPARATOR);
        bodyText.append("\n");
        bodyText.append(message.getBody());
        return bodyText.toString();
    }

    /*
     * Need to look up the SMTP port from the set session due to Email class using its default incorrectly. This should
     * be fixed in next release.
     */
    private void initEmailInfo(Email email) throws EmailException {
        email.setMailSession(mailSession);

        String smtpPort = email.getMailSession().getProperty(Email.MAIL_PORT);

        email.setFrom(senderEmail, senderName);
        if (StringUtils.isNumeric(smtpPort) && Integer.valueOf(smtpPort) > 0) {
            email.setSmtpPort(Integer.valueOf(smtpPort));
        }
    }

    /**
     * Set the Java Mail session.
     *
     * @param mailSession the mail session obtained from the server
     */
    @Inject
    public void setMailSession(Session mailSession) {
        this.mailSession = mailSession;
    }

    /**
     * Set the property of whether or not to send email messages.
     *
     * @param sendMail the property value of if to send emails.
     */
    @Inject
    public void setSendMail(@Named("send.mail") Boolean sendMail) {
        this.sendMail = sendMail;
    }

    /**
     * @param senderEmail the sendEmail to set
     */
    @Inject
    public void setSenderEmail(@Named("firebird.email.send.address") String senderEmail) {
        this.senderEmail = senderEmail;
    }

    /**
     * @param senderName the sendUser to set
     */
    @Inject
    public void setSenderName(@Named("firebird.email.send.name") String senderName) {
        this.senderName = senderName;
    }

    /**
     * @param overrideEmailAddress if this is set, all emails will be hijacked and sent to this email address instead of
     *            their intended recipients.
     */
    @Inject
    public void setOverrideEmailAddress(@Named("mail.override.address") String overrideEmailAddress) {
        this.overrideEmailAddress = overrideEmailAddress;
    }
}
