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
package gov.nih.nci.firebird.service.messages;

import static gov.nih.nci.firebird.service.messages.FirebirdContentTemplate.*;
import static gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate.*;
import static gov.nih.nci.firebird.service.messages.FirebirdStringTemplate.*;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.*;
import static gov.nih.nci.firebird.test.ValueGenerator.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.ProtocolModification;
import gov.nih.nci.firebird.data.ProtocolRevision;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.data.user.SponsorRole;
import gov.nih.nci.firebird.nes.NesIdTestUtil;
import gov.nih.nci.firebird.service.account.AccountConfigurationData;
import gov.nih.nci.firebird.service.account.AccountConfigurationDataFactory;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class TemplateServiceImplTest {

    private static final String CTEP_HELP_DESK_EMAIL = "pmbregpend@ctep.nci.nih.gov";
    private static final String TEST_LINK = "/reg";
    private static final String TEST_BASE_URL = "http://localhost:8080/ocr";

    private static ResourceBundle bundle = ResourceBundle.getBundle("resources", Locale.getDefault());
    @Mock
    private VelocityEngine mockVelocityEngine;
    private TemplateServiceImpl bean;

    enum RequiredTemplateParameter {
        REGISTRATION, INVESTIGATOR_REGISTRATION, SUBINVESTIGATOR_REGISTRATION, FIREBIRD_LINK,
        REGISTRATION_COMMENTS, FORM_COMMENTS, PROTOCOL_REVISION,
        FIREBIRD_USER, REGISTRATION_COORDINATOR, REGISTRATION_COORDINATOR_ROLE, INVESTIGATOR,
        FIREBIRD_SUPPORT_EMAIL, SPONSOR_EMAIL_ADDRESS, SPONSOR_ROLE,
        ACCOUNT_DATA, REGISTRATIONS, TIMESTAMP, REQUEST_URL, REQUEST_PARAMETERS, STACKTRACE, COMMENTS,
        ANNUAL_REGISTRATION, SPONSOR;
    }

    @Before
    public void setUp() {
        bean = new TemplateServiceImpl(mockVelocityEngine, bundle, TEST_BASE_URL,
                                       NesIdTestUtil.TEST_NES_ID_STRING);
    }

    @Test
    public void testGenerateString() {
        TemplateServiceImpl myBean = getTemplateServiceImplNoMockWithAnnualRegistrationSponsor();
        Map<FirebirdTemplateParameter, Object> valueMap = getRequiredValues();
        String subject = myBean.generateString(INVESTIGATOR_INVITATION_EMAIL_SUBJECT, valueMap);
        InvestigatorRegistration registration = (InvestigatorRegistration) valueMap.get(INVESTIGATOR_REGISTRATION);
        String expectedSubject = "You have been invited to register for Protocol "
                                 + registration.getProtocol().getProtocolNumber() + ".";
        assertEquals(expectedSubject, subject);
    }

    /**
     * @param content
     */
    private void checkForUnresolvedParameters(String content) {
        assertFalse("Content had unresolved parameters:\n" + content, content.contains("$") || content.contains("#"));
    }

    @Test
    public void testGenerateContent() {
        bean = getTemplateServiceImplNoMockWithAnnualRegistrationSponsor();
        Map<FirebirdTemplateParameter, Object> valueMap = getRequiredValues();
        String content = bean.generateContent(INVESTIGATOR_INVITATION_EMAIL_BODY, valueMap);
        checkForUnresolvedParameters(content);
    }

    @Test
    public void testGenerateAllMessages() {
        bean = getTemplateServiceImplNoMockWithAnnualRegistrationSponsor();
        Map<FirebirdMessageTemplate, Map<FirebirdTemplateParameter, Object>> parameters
                = new EnumMap<FirebirdMessageTemplate, Map<FirebirdTemplateParameter, Object>>(
                FirebirdMessageTemplate.class);
        parameters.put(INVESTIGATOR_INVITATION_EMAIL, getRequiredValues());
        parameters.put(
                SUBINVESTIGATOR_INVITATION_EMAIL,
                getRequiredValues(RequiredTemplateParameter.SUBINVESTIGATOR_REGISTRATION,
                                  RequiredTemplateParameter.FIREBIRD_LINK,
                                  RequiredTemplateParameter.SPONSOR_EMAIL_ADDRESS));
        parameters.put(
                SPONSOR_SUBMISSION_NOTIFICATION_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION,
                                  RequiredTemplateParameter.FIREBIRD_LINK));
        parameters.put(REGISTRATION_PACKET_APPROVED_EMAIL, getRequiredValues());
        parameters.put(
                REGISTRATION_RETURNED_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION, RequiredTemplateParameter.FIREBIRD_LINK,
                                  RequiredTemplateParameter.REGISTRATION_COMMENTS,
                                  RequiredTemplateParameter.FORM_COMMENTS,
                                  RequiredTemplateParameter.SPONSOR_EMAIL_ADDRESS));
        parameters.put(
                REGISTRATION_PACKET_DEACTIVATED_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION,
                                  RequiredTemplateParameter.REGISTRATION_COMMENTS,
                                  RequiredTemplateParameter.SPONSOR_EMAIL_ADDRESS, RequiredTemplateParameter.COMMENTS));
        parameters.put(
                REGISTRATION_PACKET_REACTIVATED_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION, RequiredTemplateParameter.FIREBIRD_LINK,
                                  RequiredTemplateParameter.REGISTRATION_COMMENTS,
                                  RequiredTemplateParameter.SPONSOR_EMAIL_ADDRESS, RequiredTemplateParameter.COMMENTS));
        parameters.put(
                PROTOCOL_MODIFIED_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION, RequiredTemplateParameter.FIREBIRD_LINK,
                                  RequiredTemplateParameter.PROTOCOL_REVISION,
                                  RequiredTemplateParameter.SPONSOR_EMAIL_ADDRESS));

        parameters.put(FirebirdMessageTemplate.REGISTRATION_COORDINATOR_REQUEST_EMAIL,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR));
        parameters.put(FirebirdMessageTemplate.COORDINATOR_REQUEST_UPDATED_EMAIL,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR_ROLE));
        parameters.put(
                FirebirdMessageTemplate.COORDINATOR_COMPLETED_REGISTRATION_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION, RequiredTemplateParameter.FIREBIRD_LINK,
                                  RequiredTemplateParameter.REGISTRATION_COORDINATOR));
        parameters
                .put(FirebirdMessageTemplate.COORDINATOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL,
                     getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION,
                                       RequiredTemplateParameter.FIREBIRD_LINK,
                                       RequiredTemplateParameter.REGISTRATION_COORDINATOR));
        parameters.put(
                FirebirdMessageTemplate.COORDINATOR_SUSPENDED_FROM_REGISTRATIONS_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR_ROLE,
                                  RequiredTemplateParameter.FIREBIRD_LINK, RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(
                FirebirdMessageTemplate.COORDINATOR_UNSUSPENDED_FROM_REGISTRATIONS_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR_ROLE,
                                  RequiredTemplateParameter.FIREBIRD_LINK, RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(
                FirebirdMessageTemplate.COORDINATOR_SUSPENDED_FROM_PROFILE_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR_ROLE,
                                  RequiredTemplateParameter.FIREBIRD_LINK, RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(
                FirebirdMessageTemplate.COORDINATOR_UNSUSPENDED_FROM_PROFILE_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR_ROLE,
                                  RequiredTemplateParameter.FIREBIRD_LINK, RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(
                FirebirdMessageTemplate.COORDINATOR_REMOVED_EMAIL,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR_ROLE,
                                  RequiredTemplateParameter.FIREBIRD_LINK, RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(FirebirdMessageTemplate.ACCOUNT_REQUEST_EMAIL,
                       getRequiredValues(RequiredTemplateParameter.ACCOUNT_DATA));
        parameters.put(FirebirdMessageTemplate.ACCOUNT_REQUEST_EXISTING_LDAP_ACCOUNT_EMAIL,
                       getRequiredValues(RequiredTemplateParameter.ACCOUNT_DATA));
        parameters.put(
                FirebirdMessageTemplate.ACCOUNT_REQUEST_USER_NOTIFICATION_EMAIL,
                getRequiredValues(RequiredTemplateParameter.ACCOUNT_DATA,
                                  RequiredTemplateParameter.FIREBIRD_SUPPORT_EMAIL));
        parameters.put(
                FirebirdMessageTemplate.DELEGATE_REMOVAL_NOTIFICATION_EMAIL,
                getRequiredValues(RequiredTemplateParameter.SPONSOR_ROLE,
                                  RequiredTemplateParameter.SPONSOR_EMAIL_ADDRESS));
        parameters.put(FirebirdMessageTemplate.ADDED_ROLES_SUPPORT_NOTIFICATION_EMAIL,
                       getRequiredValues(RequiredTemplateParameter.FIREBIRD_USER,
                                         RequiredTemplateParameter.ACCOUNT_DATA));
        parameters.put(FirebirdMessageTemplate.REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATIONS,
                                         RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(
                FirebirdMessageTemplate.COORDINATOR_REMOVED_ROLE_EMAIL,
                getRequiredValues(RequiredTemplateParameter.INVESTIGATOR,
                                  RequiredTemplateParameter.REGISTRATION_COORDINATOR));
        parameters.put(
                FirebirdMessageTemplate.PACKET_READY_FOR_APPROVAL_EMAIL,
                getRequiredValues(RequiredTemplateParameter.FIREBIRD_USER, RequiredTemplateParameter.REGISTRATION,
                                  RequiredTemplateParameter.FIREBIRD_LINK));
        parameters.put(
                FirebirdMessageTemplate.UNHANDLED_EXCEPTION_EMAIL,
                getRequiredValues(RequiredTemplateParameter.FIREBIRD_USER, RequiredTemplateParameter.TIMESTAMP,
                                  RequiredTemplateParameter.REQUEST_URL, RequiredTemplateParameter.REQUEST_PARAMETERS,
                                  RequiredTemplateParameter.STACKTRACE));
        parameters.put(
                FirebirdMessageTemplate.PACKET_REMOVED_EMAIL,
                getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION,
                                  RequiredTemplateParameter.SPONSOR_EMAIL_ADDRESS));
        parameters.put(FirebirdMessageTemplate.REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL_TO_SPONSOR,
                       getRequiredValues(RequiredTemplateParameter.SUBINVESTIGATOR_REGISTRATION));
        parameters.put(
                FirebirdMessageTemplate.SPONSOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL,
                getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION,
                                  RequiredTemplateParameter.FIREBIRD_LINK));
        parameters.put(FirebirdMessageTemplate.COORDINATOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(
                FirebirdMessageTemplate.ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_INVESTIGATOR,
                getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION,
                                  RequiredTemplateParameter.FIREBIRD_LINK,
                                  RequiredTemplateParameter.SPONSOR_EMAIL_ADDRESS));
        parameters.put(FirebirdMessageTemplate.ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_OTHERS,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(
                FirebirdMessageTemplate.ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_INVESTIGATOR,
                getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION,
                                  RequiredTemplateParameter.FIREBIRD_LINK,
                                  RequiredTemplateParameter.SPONSOR_EMAIL_ADDRESS));
        parameters.put(FirebirdMessageTemplate.ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_OTHERS,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(
                FirebirdMessageTemplate.ANNUAL_REGISTRATION_RENEWAL_SIXTY_DAY_NOTICE_EMAIL,
                getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION,
                                  RequiredTemplateParameter.FIREBIRD_LINK));
        parameters.put(
                FirebirdMessageTemplate.ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_COORDINATOR,
                getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION,
                                  RequiredTemplateParameter.FIREBIRD_LINK));
        parameters.put(FirebirdMessageTemplate.ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_INVESTIGATOR,
                       getRequiredValues(RequiredTemplateParameter.FIREBIRD_LINK));
        parameters.put(FirebirdMessageTemplate.ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_INVESTIGATOR,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(FirebirdMessageTemplate.ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_COORDINATOR,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(
                FirebirdMessageTemplate.SPONSOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL,
                getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION,
                                  RequiredTemplateParameter.FIREBIRD_LINK, RequiredTemplateParameter.SPONSOR,
                                  RequiredTemplateParameter.REGISTRATION_COORDINATOR));
        parameters.put(FirebirdMessageTemplate.CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_EMAIL,
                       getRequiredValues(RequiredTemplateParameter.FIREBIRD_LINK,
                                         RequiredTemplateParameter.INVESTIGATOR,
                                         RequiredTemplateParameter.COMMENTS));
        parameters.put(FirebirdMessageTemplate.CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_INVESTIGATOR,
                       getRequiredValues());
        parameters.put(FirebirdMessageTemplate.CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_OTHERS,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(FirebirdMessageTemplate.CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_INVESTIGATOR,
                       getRequiredValues(RequiredTemplateParameter.COMMENTS));
        parameters.put(FirebirdMessageTemplate.CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_OTHERS,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR, RequiredTemplateParameter.COMMENTS));
        parameters.put(FirebirdMessageTemplate.CTEP_INVESTIGATOR_SUSPENEDED_EMAIL_TO_COORDINATOR,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR,
                                         RequiredTemplateParameter.REGISTRATION_COORDINATOR));
        parameters.put(FirebirdMessageTemplate.CTEP_INVESTIGATOR_SUSPENEDED_EMAIL_TO_INVESTIGATOR,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(FirebirdMessageTemplate.CTEP_INVESTIGATOR_DISQUALIFIED_EMAIL_TO_INVESTIGATOR,
                       getRequiredValues(RequiredTemplateParameter.COMMENTS));
        parameters.put(FirebirdMessageTemplate.CTEP_INVESTIGATOR_DISQUALIFIED_EMAIL_TO_OTHERS,
                       getRequiredValues(RequiredTemplateParameter.COMMENTS, RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(FirebirdMessageTemplate.REGISTRATION_REVISION_INITIATED_EMAIL_TO_COORDINATOR,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(FirebirdMessageTemplate.REGISTRATION_REVISION_INITIATED_EMAIL_TO_SPONSOR,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(FirebirdMessageTemplate.REGISTRATION_REVISION_INITIATED_EMAIL_TO_INVESTIGATOR,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR,
                                         RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(FirebirdMessageTemplate.REGISTRATION_REVISION_CANCELED_EMAIL_TO_SPONSOR,
                getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));

        for (FirebirdMessageTemplate template : FirebirdMessageTemplate.values()) {
            FirebirdMessage firebirdMessage = bean.generateMessage(template, parameters.get(template));
            checkMessage(firebirdMessage);
        }
    }

    @Test
    public void testGenerateAllStrings() {
        bean = getTemplateServiceImplNoMockWithAnnualRegistrationSponsor();
        Map<FirebirdStringTemplate, Map<FirebirdTemplateParameter, Object>> parameters
                = new EnumMap<FirebirdStringTemplate, Map<FirebirdTemplateParameter, Object>>(
                FirebirdStringTemplate.class);

        parameters.put(INVESTIGATOR_INVITATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(SUBINVESTIGATOR_INVITATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.SUBINVESTIGATOR_REGISTRATION));
        parameters.put(SPONSOR_SUBMISSION_NOTIFICATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(REGISTRATION_PACKET_APPROVED_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(REGISTRATION_RETURNED_EMAIL_SUBJECT, getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(REGISTRATION_PACKET_DEACTIVATED_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(REGISTRATION_PACKET_REACTIVATED_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(PROTOCOL_MODIFIED_EMAIL_SUBJECT, getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(INVESTIGATOR_INVITATION_TASK_TITLE,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(SUBINVESTIGATOR_INVITATION_TASK_TITLE,
                       getRequiredValues(RequiredTemplateParameter.SUBINVESTIGATOR_REGISTRATION));
        parameters.put(REGISTRATION_IN_PROGRESS_TASK_TITLE, getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(REGISTRATION_REACTIVATED_TASK_TITLE, getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(REGISTRATION_SUBMITTED_TASK_TITLE, getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(REGISTRATION_IN_REVIEW_TASK_TITLE, getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(REGISTRATION_RETURNED_TASK_TITLE, getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(REGISTRATION_PACKET_AWAITING_APPROVAL_TITLE,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(PROTOCOL_MODIFIED_TASK_TITLE, getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(REGISTRATION_COORDINATOR_REQUEST_TITLE,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR));
        parameters.put(REGISTRATION_COORDINATOR_REQUEST_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR));
        parameters.put(COORDINATOR_REQUEST_UPDATED_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR_ROLE));
        parameters.put(REGISTRATION_READY_FOR_SIGNING_TASK_TITLE,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(
                COORDINATOR_COMPLETED_REGISTRATION_EMAIL_SUBJECT,
                getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR,
                                  RequiredTemplateParameter.REGISTRATION));
        parameters.put(COORDINATOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR));
        parameters.put(COORDINATOR_SUSPENDED_FROM_PROFILE_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(COORDINATOR_UNSUSPENDED_FROM_PROFILE_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(COORDINATOR_SUSPENDED_FROM_REGISTRATIONS_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(COORDINATOR_UNSUSPENDED_FROM_REGISTRATIONS_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(COORDINATOR_REMOVED_EMAIL_SUBJECT, getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(ACCOUNT_REQUEST_EMAIL_SUBJECT, getRequiredValues(RequiredTemplateParameter.ACCOUNT_DATA));
        parameters.put(ACCOUNT_REQUEST_EXISTING_LDAP_ACCOUNT_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.ACCOUNT_DATA));
        parameters.put(ACCOUNT_REQUEST_USER_NOTIFICATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.ACCOUNT_DATA));
        parameters.put(SPONSOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE, getRequiredValues());
        parameters.put(INVESTIGATOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE, getRequiredValues());
        parameters.put(DELEGATE_REMOVAL_NOTIFICATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.SPONSOR_ROLE));
        parameters.put(ADDED_ROLES_SUPPORT_NOTIFICATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.FIREBIRD_USER));
        parameters.put(REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL_TO_SPONSOR_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.SUBINVESTIGATOR_REGISTRATION));
        parameters.put(COORDINATOR_REMOVED_ROLE_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR));
        parameters.put(PACKET_READY_FOR_APPROVAL_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION));
        parameters.put(MISSING_EXPECTED_INVESTIGATOR_ROLE_TASK_TITLE, getRequiredValues());
        parameters.put(COORDINATOR_ROLE_AWAITING_VERIFICATION_TASK_TITLE, getRequiredValues());
        parameters.put(COORDINATOR_REGISTRATION_RENEWAL_TASK_TITLE,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(INVESTIGATOR_REGISTRATION_RENEWAL_TASK_TITLE, getRequiredValues());
        parameters.put(UNHANDLED_EXCEPTION_EMAIL_SUBJECT, getRequiredValues());
        parameters.put(PACKET_REMOVED_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(COORDINATOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(SPONSOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(ANNUAL_REGISTRATION_IN_REVIEW_TASK_TITLE,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(ANNUAL_REGISTRATION_REVIIEW_ON_HOLD_TASK_TITLE,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(ANNUAL_REGISTRATION_SUBMITTED_TASK_TITLE,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_OTHERS_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_OTHERS_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_INVESTIGATOR_SUBJECT, getRequiredValues());
        parameters.put(ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_INVESTIGATOR_SUBJECT, getRequiredValues());
        parameters.put(ANNUAL_REGISTRATION_RENEWAL_SIXTY_DAY_NOTICE_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_COORDINATOR_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters
                .put(ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_INVESTIGATOR_SUBJECT, getRequiredValues());
        parameters.put(ANNUAL_REGISTRATION_READY_FOR_SIGNING_TASK_TITLE, getRequiredValues());
        parameters.put(ANNUAL_REGISTRATION_APPROVED_TASK_FOR_INVESTIGATOR_TITLE, getRequiredValues());
        parameters.put(ANNUAL_REGISTRATION_APPROVED_TASK_FOR_COORDINATOR_TITLE,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_INVESTIGATOR_SUBJECT,
                       getRequiredValues());
        parameters.put(ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_COORDINATOR_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(SPONSOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.SPONSOR,
                                         RequiredTemplateParameter.ANNUAL_REGISTRATION));
        parameters.put(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_EMAIL_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_INVESTIGATOR_SUBJECT,
                       getRequiredValues());
        parameters.put(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_OTHERS_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_INVESTIGATOR_SUBJECT,
                       getRequiredValues());
        parameters.put(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_OTHERS_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(REQUEST_FOR_WITHDRAWAL_TASK_TITLE,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(CTEP_INVESTIGATOR_SUSPENEDED_EMAIL_TO_COORDINATOR_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(CTEP_INVESTIGATOR_SUSPENEDED_EMAIL_TO_INVESTIGATOR_SUBJECT,
                       getRequiredValues());
        parameters.put(CTEP_INVESTIGATOR_SUSPENEDED_TASK_TITLE_FOR_COORDINATOR,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(CTEP_INVESTIGATOR_SUSPENEDED_TASK_TITLE_FOR_INVESTIGATOR,
                       getRequiredValues());
        parameters.put(CTEP_INVESTIGATOR_DISQUALIFIED_EMAIL_TO_INVESTIGATOR_SUBJECT,
                       getRequiredValues());
        parameters.put(CTEP_INVESTIGATOR_DISQUALIFIED_EMAIL_TO_OTHERS_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(CTEP_INVESTIGATOR_DISQUALIFIED_TASK_TITLE_FOR_INVESTIGATOR,
                       getRequiredValues());
        parameters.put(CTEP_INVESTIGATOR_DISQUALIFIED_TASK_TITLE_FOR_COORDINATOR,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR));
        parameters.put(REGISTRATION_REVISION_INITIATED_EMAIL_TO_SPONSOR_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(REGISTRATION_REVISION_INITIATED_EMAIL_TO_COORDINATOR_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(REGISTRATION_REVISION_INITIATED_EMAIL_TO_INVESTIGATOR_SUBJECT,
                       getRequiredValues(RequiredTemplateParameter.REGISTRATION_COORDINATOR,
                                         RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(REGISTRATION_REVISION_CANCELED_EMAIL_TO_SPONSOR_SUBJECT,
                getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION));
        parameters.put(ANNUAL_REGISTRATION_RETURNED_TASK_TITLE, getRequiredValues());

        for (FirebirdStringTemplate template : FirebirdStringTemplate.values()) {
            String firebirdMessage = bean.generateString(template, parameters.get(template));
            assertFalse(StringUtils.isEmpty(firebirdMessage));
            checkForUnresolvedParameters(firebirdMessage);
        }
    }

    private TemplateServiceImpl getTemplateServiceImplNoMockWithAnnualRegistrationSponsor() {
        return new TemplateServiceImpl(getVelocity(), bundle, TEST_BASE_URL, NesIdTestUtil.TEST_NES_ID_STRING);
    }

    private void checkMessage(FirebirdMessage firebirdMessage) {
        assertFalse(StringUtils.isEmpty(firebirdMessage.getSubject()));
        assertFalse(StringUtils.isEmpty(firebirdMessage.getBody()));
        checkForUnresolvedParameters(firebirdMessage.getSubject());
        checkForUnresolvedParameters(firebirdMessage.getBody());
    }

    private static VelocityEngine getVelocity() {
        VelocityEngine velocity = new VelocityEngine();
        Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("class.resource.loader.class", ClasspathResourceLoader.class.getName());
        props.put("class.resource.loader.cache", "true");
        props.put("class.resource.loader.modificationCheckInterval", "2");
        props.put("runtime.log", "target/test-log/velocity.log");
        velocity.init(props);
        return velocity;
    }

    private Map<FirebirdTemplateParameter, Object> getRequiredValues() {
        return getRequiredValues(RequiredTemplateParameter.INVESTIGATOR_REGISTRATION,
                                 RequiredTemplateParameter.FIREBIRD_LINK,
                                 RequiredTemplateParameter.SPONSOR_EMAIL_ADDRESS);
    }

    private Map<FirebirdTemplateParameter, Object> getRequiredValues(RequiredTemplateParameter... requirements) {
        Map<FirebirdTemplateParameter, Object> values = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        AbstractProtocolRegistration registration = null;
        Arrays.sort(requirements);

        for (RequiredTemplateParameter req : requirements) {
            switch (req) {
                case REGISTRATION:
                    registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
                    values.put(REGISTRATION, registration);
                    break;
                case INVESTIGATOR_REGISTRATION:
                    registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
                    values.put(INVESTIGATOR_REGISTRATION, registration);
                    break;
                case SUBINVESTIGATOR_REGISTRATION:
                    registration = RegistrationFactory.getInstance().createSubinvestigatorRegistration();
                    values.put(SUBINVESTIGATOR_REGISTRATION, registration);
                    break;
                case FIREBIRD_LINK:
                    values.put(FIREBIRD_LINK, TEST_LINK);
                    break;
                case REGISTRATION_COMMENTS:
                    registration.setSponsorComments("These are Registration Comments");
                    break;
                case FORM_COMMENTS:
                    for (AbstractRegistrationForm form : registration.getForms()) {
                        form.setComments("These are comments for rejected form " + form.getFormType().getName());
                    }
                    break;
                case PROTOCOL_REVISION:
                    ProtocolRevision revision = new ProtocolRevision();
                    revision.setDate(new Date());
                    revision.setComment("This is a sponsor comment.");
                    ProtocolModification modification = new ProtocolModification("Something changed.",
                                                                                 "Something changed.");
                    revision.addModification(modification);
                    values.put(PROTOCOL_REVISION, revision);
                    break;
                case FIREBIRD_USER:
                    values.put(FIREBIRD_USER, FirebirdUserFactory.getInstance().createInvestigator("username"));
                    break;
                case REGISTRATION_COORDINATOR:
                    values.put(REGISTRATION_COORDINATOR, PersonFactory.getInstance().create());
                    break;
                case SPONSOR:
                    values.put(SPONSOR, PersonFactory.getInstance().create());
                    break;
                case REGISTRATION_COORDINATOR_ROLE:
                    FirebirdUser coordinstor = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
                    ManagedInvestigator managedInvestigator = coordinstor.getRegistrationCoordinatorRole()
                            .addManagedInvestigator(InvestigatorProfileFactory.getInstance().create());
                    values.put(MANAGED_INVESTIGATOR, managedInvestigator);
                    break;
                case INVESTIGATOR:
                    values.put(INVESTIGATOR, PersonFactory.getInstance().create());
                    break;
                case ACCOUNT_DATA:
                    AccountConfigurationData configurationData = AccountConfigurationDataFactory.getInstance().create();
                    configurationData.setUsername("username");
                    values.put(ACCOUNT_DATA, configurationData);
                    break;
                case FIREBIRD_SUPPORT_EMAIL:
                    values.put(FIREBIRD_SUPPORT_EMAIL, "firebird-support@5amsolutions.com");
                    break;
                case SPONSOR_EMAIL_ADDRESS:
                    values.put(SPONSOR_EMAIL_ADDRESS, getUniqueEmailAddress());
                    break;
                case SPONSOR_ROLE:
                    values.put(SPONSOR_ROLE, getSponsorRole());
                    break;
                case REGISTRATIONS:
                    values.put(REGISTRATIONS, Lists.newArrayList(RegistrationFactory.getInstance()
                                                                         .createSubinvestigatorRegistration(),
                                                                 RegistrationFactory.getInstance()
                                                                         .createSubinvestigatorRegistration()));
                    break;
                case TIMESTAMP:
                    values.put(TIMESTAMP, new Date().toString());
                    break;
                case REQUEST_URL:
                    values.put(REQUEST_URL, TEST_LINK);
                    break;
                case REQUEST_PARAMETERS:
                    values.put(REQUEST_PARAMETERS, getUniqueString());
                    break;
                case STACKTRACE:
                    values.put(STACKTRACE, getUniqueString());
                    break;
                case COMMENTS:
                    values.put(COMMENTS, getUniqueString());
                    break;
                case ANNUAL_REGISTRATION:
                    AnnualRegistration annualRegistration = AnnualRegistrationFactory.getInstance().create();
                    annualRegistration.setStatus(RegistrationStatus.SUBMITTED);
                    annualRegistration.getProfile().getPerson().setCtepId("1234");
                    annualRegistration.setRenewalDate(DateUtils.addYears(new Date(), 1));
                    values.put(ANNUAL_REGISTRATION, annualRegistration);
                    break;
            }

        }

        return values;
    }

    private SponsorRole getSponsorRole() {
        return FirebirdUserFactory.getInstance().create()
                .addSponsorDelegateRole(OrganizationFactory.getInstance().create());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMessage_MissingTemplateParameters() {
        Template template = mock(Template.class);
        when(mockVelocityEngine.getTemplate(anyString())).thenReturn(template);
        bean.generateMessage(INVESTIGATOR_INVITATION_EMAIL, new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class));
    }

    @Test
    public void testGenerateMessage_NullTemplateParameters() {
        Template template = mock(Template.class);
        when(mockVelocityEngine.getTemplate(anyString())).thenReturn(template);
        Map<FirebirdTemplateParameter, Object> valueMap = getRequiredValues(RequiredTemplateParameter.TIMESTAMP,
                                                                            RequiredTemplateParameter.REQUEST_URL,
                                                                            RequiredTemplateParameter.REQUEST_PARAMETERS,
                                                                            RequiredTemplateParameter.STACKTRACE);
        valueMap.put(FirebirdTemplateParameter.FIREBIRD_USER, null);
        FirebirdMessage message = bean.generateMessage(UNHANDLED_EXCEPTION_EMAIL, valueMap);
        checkForUnresolvedParameters(message.getBody());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMessage_InvalidParameterType() {
        Template template = mock(Template.class);
        when(mockVelocityEngine.getTemplate(anyString())).thenReturn(template);
        Map<FirebirdTemplateParameter, Object> valueMap = getRequiredValues();
        valueMap.put(INVESTIGATOR_REGISTRATION, new Object());
        bean.generateMessage(INVESTIGATOR_INVITATION_EMAIL, valueMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMessageFileNotFound() {
        when(mockVelocityEngine.getTemplate(anyString()))
                .thenThrow(new ResourceNotFoundException(any(Throwable.class)));
        bean.generateMessage(INVESTIGATOR_INVITATION_EMAIL, getRequiredValues());
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateMessageUnknownError() {
        when(mockVelocityEngine.getTemplate(anyString())).thenThrow(new ParseErrorException(anyString()));
        bean.generateMessage(INVESTIGATOR_INVITATION_EMAIL, getRequiredValues());
    }

    @Test
    public void testGenerateMessage_WithSponsorWithAnnualRegistrations() {
        bean = getTemplateServiceImplNoMockWithAnnualRegistrationSponsor();
        FirebirdMessage message = bean.generateMessage(INVESTIGATOR_INVITATION_EMAIL, getRequiredValues());
        assertTrue(message.getBody().contains(CTEP_HELP_DESK_EMAIL));
    }

    @Test
    public void testGenerateMessage_WithoutSponsorWithAnnualRegistrations() {
        bean = getTemplateServiceImplNoMockWithoutAnnualRegistrationSponsor();
        FirebirdMessage message = bean.generateMessage(INVESTIGATOR_INVITATION_EMAIL, getRequiredValues());
        assertFalse(message.getBody().contains(CTEP_HELP_DESK_EMAIL));
    }

    private TemplateServiceImpl getTemplateServiceImplNoMockWithoutAnnualRegistrationSponsor() {
        return new TemplateServiceImpl(getVelocity(), bundle, TEST_BASE_URL, null);
    }

    @Test
    public void testInvestigatorInvitationTaskTitleTemplate() {
        bean = getTemplateServiceImplNoMockWithAnnualRegistrationSponsor();
        String title = bean.generateString(INVESTIGATOR_INVITATION_TASK_TITLE, getRequiredValues());
        checkForUnresolvedParameters(title);
    }
}
