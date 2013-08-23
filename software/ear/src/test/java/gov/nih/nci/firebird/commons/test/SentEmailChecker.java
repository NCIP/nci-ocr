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
package gov.nih.nci.firebird.commons.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

public class SentEmailChecker {

    public static final String TO_HEADER_NAME = "To";
    public static final String MIME_VERSION_HEADER_NAME = "MIME-Version";
    public static final String MESSAGE_ID_HEADER_NAME = "Message-ID";
    public static final String SUBJECT_HEADER_NAME = "Subject";
    public static final String DATE_HEADER_NAME = "Date";
    public static final String CONTENT_TRANSFER_ENCODING_HEADER_NAME = "Content-Transfer-Encoding";
    public static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    public static final String FROM_HEADER_NAME = "From";

    @Inject
    @Named("mail.smtp.port")
    private int port;

    private GreenMail greenMail;

    /**
     * Starts the test SMTP server.
     */
    public void start() {
        checkPort();
        ServerSetup config = new ServerSetup(port, null, ServerSetup.PROTOCOL_SMTP);
        greenMail = new GreenMail(config);
        greenMail.start();
    }

    /**
     * Stops the test SMTP server.
     */
    public void stop() {
        greenMail.stop();
    }

    private void checkPort() throws RuntimeException {
        try {
            ServerSocket testSocket = new ServerSocket(port);
            testSocket.close();
        } catch (IOException e) {
            throw new IllegalStateException("Could not open port " + port, e);
        }
    }

    public MimeMessage[] getSentEmails() {
        return greenMail.getReceivedMessages();
    }

    public int getSentEmailSize() {
        return greenMail.getReceivedMessages().length;
    }

    public void checkTo(MimeMessage message, String... expectedAddresses) {
        Set<String> actualAddresses = getToAddresses(message);
        assertTrue(
                "Address mismatch, expected " + Arrays.toString(expectedAddresses) + " but was "
                        + actualAddresses + "\n" + message.toString(),
                isMessageTo(actualAddresses, expectedAddresses));
    }

    private Set<String> getToAddresses(MimeMessage message) {
        try {
            Address[] addresses = message.getRecipients(RecipientType.TO);
            Set<String> addressStrings = Sets.newHashSet();
            for (Address address : addresses) {
                addressStrings.add(((InternetAddress) address).getAddress());
            }
            return addressStrings;
        } catch (MessagingException e) {
            throw new IllegalStateException("Unexpected MessagingException", e);
        }
    }

    private boolean isMessageTo(Set<String> actualAddresses, String... expectedAddresses) {
        return expectedAddresses.length == actualAddresses.size()
                && actualAddresses.containsAll(Arrays.asList(expectedAddresses));
    }

    public MimeMessage getSentEmail(String to, String subject) {
        waitForIncomingEmail();
        for (MimeMessage message : getSentEmails()) {
            if (isMessageTo(message, to) && getMessageSubject(message).equals(subject)) {
                return message;
            }
        }
        fail("Didn't receive email:\nTo: " + to + "\nSubject: " + subject);
        return null;
    }

    private void waitForIncomingEmail() {
        try {
            greenMail.waitForIncomingEmail(1);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isMessageTo(MimeMessage message, String to) {
        return isMessageTo(getToAddresses(message), to);
    }

    public String getMessageSubject(MimeMessage message) {
        try {
            return message.getSubject();
        } catch (MessagingException e) {
            throw new IllegalStateException("Unexpected MessagingException", e);
        }
    }

    public MimeMessage getSentEmail(String subject) {
        waitForIncomingEmail();
        for (MimeMessage message : getSentEmails()) {
            if (getMessageSubject(message).equals(subject)) {
                return message;
            }
        }
        fail("Didn't receive email:\nSubject: " + subject);
        return null;
    }

    public void checkSubject(MimeMessage message, String subject) {
        assertEquals(message.toString(), subject, getMessageSubject(message));
    }

    public String getContent(MimeMessage message) {
        try {
            return (String) message.getContent();
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected IOException", e);
        } catch (MessagingException e) {
            throw new IllegalStateException("Unexpected MessagingException", e);
        }

    }

    /**
     * Will assert that the correct number of emails are received, providing a wait of
     * up to a default timeout (10 seconds) for those messages to be processed and received.
     *
     * @param expectedCount count of emails expected
     */
    public void assertEmailCount(final int expectedCount) {
        Predicate<Integer> expectedNumberOfEmailsReceived = getExpectedEmailCountReceivedPredicate();
        try {
            new FluentWait<Integer>(expectedCount).withTimeout(61, TimeUnit.SECONDS).until(expectedNumberOfEmailsReceived);
        } catch (TimeoutException e) {
            fail(getEmailCountFailureMessage(expectedCount));
        }
    }

    private String getEmailCountFailureMessage(int expectedCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("Expected ");
        sb.append(expectedCount);
        sb.append(" emails but received ");
        sb.append(getSentEmailSize());
        sb.append(". Received emails were:");
        for (MimeMessage message : getSentEmails()) {
            sb.append("\nTo: ");
            sb.append(Joiner.on(", ").join(getToAddresses(message)));
            sb.append("; Subject: ");
            sb.append(getMessageSubject(message));
        }
        return sb.toString();
    }

    private Predicate<Integer> getExpectedEmailCountReceivedPredicate() {
        Predicate<Integer> predicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer expectedEmailCount) {
                Integer actualEmailCount = getSentEmailSize();
                return expectedEmailCount.equals(actualEmailCount);
            }
        };
        return predicate;
    }

}
