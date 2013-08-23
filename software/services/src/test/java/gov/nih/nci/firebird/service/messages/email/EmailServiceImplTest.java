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

import com.google.common.collect.Lists;
import static org.junit.Assert.*;

import gov.nih.nci.firebird.service.messages.FirebirdMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.Email;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

public class EmailServiceImplTest {

    private static final String TEST_FROM_NAME = "FIREBIRD Support";
    private static final String TEST_FROM_ADDRESS = "send-email@example.com";
    private static final String TEST_TO_ADDRESS = "email@example.com";
    private static final String TEST_SUBJECT = "Subject";
    private static final String TEST_CONTENT = "Hello";
    private static final List<String> TEST_CC_ADDRESSES = Lists.newArrayList("email1@example.com", "email2@example.com");
    private static final String TEST_BOUNCE_ADDRESS = "bounce@example.com";

    private EmailServiceImpl bean;
    private Session session;
    private Properties props;
    private Mailbox mailbox;
    private FirebirdMessage testMessage = new FirebirdMessage(TEST_SUBJECT, TEST_CONTENT);

    @Before
    public void setUp() throws AddressException {
        props = new Properties();
        props.setProperty(Email.MAIL_PORT, "25");
        session = Session.getInstance(props);
        mailbox = Mailbox.get(TEST_TO_ADDRESS);
        bean = new EmailServiceImpl();
        bean.setSenderName(TEST_FROM_NAME);
        bean.setSenderEmail(TEST_FROM_ADDRESS);
        bean.setMailSession(session);
        bean.setSendMail(true);
    }

    @After
    public void tearDown() {
        Mailbox.clearAll();
    }

    @Test
    public void testSendMessage() throws MessagingException, IOException {
        bean.sendMessage(TEST_TO_ADDRESS, TEST_CC_ADDRESSES, TEST_BOUNCE_ADDRESS, testMessage);
        Message message = mailbox.get(0);
        InternetAddress fromAddress = (InternetAddress) message.getFrom()[0];
        assertEquals(TEST_FROM_ADDRESS, fromAddress.getAddress());
        assertEquals(TEST_FROM_NAME, fromAddress.getPersonal());
        InternetAddress toAddress = (InternetAddress) message.getRecipients(RecipientType.TO)[0];
        assertEquals(TEST_TO_ADDRESS, toAddress.getAddress());
        assertEquals(2, message.getRecipients(RecipientType.CC).length);
        InternetAddress ccAddress1 = (InternetAddress) message.getRecipients(RecipientType.CC)[0];
        InternetAddress ccAddress2 = (InternetAddress) message.getRecipients(RecipientType.CC)[1];
        assertEquals(TEST_CC_ADDRESSES.get(0), ccAddress1.getAddress());
        assertEquals(TEST_CC_ADDRESSES.get(1), ccAddress2.getAddress());
        assertEquals(TEST_SUBJECT, message.getSubject());
        assertEquals(TEST_CONTENT, message.getContent());
    }

    @Test
    public void testSendMessage_OverrideEmail() throws MessagingException, IOException {
        String messageContent = doSendMessageTest(TEST_CC_ADDRESSES);
        assertTrue(messageContent.contains(EmailServiceImpl.CC_OVERRIDE_HEADING));
        assertTrue(messageContent.contains(TEST_CC_ADDRESSES.get(0)));
        assertTrue(messageContent.contains(TEST_CC_ADDRESSES.get(1)));
    }

    private String doSendMessageTest(List<String> cc) throws MessagingException, IOException {
        String overrideEmail = "overrideEmail@example.com";
        mailbox = Mailbox.get(overrideEmail);
        bean.setOverrideEmailAddress(overrideEmail);
        bean.sendMessage(TEST_TO_ADDRESS, cc, null, testMessage);
        Message message = mailbox.get(0);
        InternetAddress fromAddress = (InternetAddress) message.getFrom()[0];
        assertEquals(TEST_FROM_ADDRESS, fromAddress.getAddress());
        assertEquals(TEST_FROM_NAME, fromAddress.getPersonal());
        InternetAddress toAddress = (InternetAddress) message.getRecipients(RecipientType.TO)[0];
        assertEquals(overrideEmail, toAddress.getAddress());
        assertNull(message.getRecipients(RecipientType.CC));
        assertEquals(TEST_SUBJECT, message.getSubject());
        assertTrue(message.getContent().toString().contains(TEST_CONTENT));
        assertTrue(message.getContent().toString().contains(EmailServiceImpl.TO_OVERRIDE_HEADING));
        assertTrue(message.getContent().toString().contains(TEST_TO_ADDRESS));
        assertTrue(message.getContent().toString().contains(EmailServiceImpl.LINE_SEPARATOR));

        return message.getContent().toString();
    }

    @Test
    public void testSendMessage_OverrideEmailNoCC() throws MessagingException, IOException {
        String messageContent = doSendMessageTest(null);
        assertFalse(messageContent.contains(EmailServiceImpl.CC_OVERRIDE_HEADING));
        assertFalse(messageContent.contains(TEST_CC_ADDRESSES.get(0)));
        assertFalse(messageContent.contains(TEST_CC_ADDRESSES.get(1)));
    }

    @Test
    public void testSendMessageSendMailFalse() throws MessagingException, IOException {
        bean.setSendMail(false);
        bean.sendMessage(TEST_TO_ADDRESS, null, null, testMessage);
        assertTrue(mailbox.isEmpty());
    }

    @Test
    public void testSendMessageEmptyCC() throws MessagingException {
        Collection<String> cc = new HashSet<String>();
        bean.sendMessage(TEST_TO_ADDRESS, cc, null, testMessage);
        Message message = mailbox.get(0);
        assertNull(message.getRecipients(RecipientType.CC));
    }

    @Test
    public void testSendMessageInvalidSessionPort() {
        props.setProperty(Email.MAIL_PORT, "-12345");
        bean.setMailSession(Session.getInstance(props));
        bean.sendMessage(TEST_TO_ADDRESS, null, null, testMessage);
        assertFalse(mailbox.isEmpty());
    }

    @Test
    public void testSendMessageInvalidSessionPortNonNumeric() {
        props.setProperty(Email.MAIL_PORT, "not a number");
        bean.setMailSession(Session.getInstance(props));
        bean.sendMessage(TEST_TO_ADDRESS, null, null, testMessage);
        assertFalse(mailbox.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageNoToAddress() {
        bean.sendMessage((String)null, null, null, testMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageEmptyToAddress() {
        Collection<String> to = new HashSet<String>();
        bean.sendMessage(to, null, null, testMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageBlankToAddress() {
        bean.sendMessage(Collections.singleton("   "), null, null, testMessage);
    }

    @Test(expected = IllegalStateException.class)
    public void testSendMessageIllegalStateException() {
        bean.sendMessage(TEST_TO_ADDRESS,  null, null, new FirebirdMessage("subject", null));
    }

}
