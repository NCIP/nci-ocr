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
package gov.nih.nci.firebird.selenium2.tests.investigator.annual.registration;

import static gov.nih.nci.firebird.selenium2.pages.util.FirebirdEmailUtils.*;
import static gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate.*;
import static org.apache.commons.lang.time.DateUtils.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.util.WaitUtils;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.AnnualRegistrationType;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.OverviewTab;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.service.periodic.DailyJobService;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.util.Date;

import org.junit.Test;

import com.google.inject.Inject;

public class RegistrationRenewalReminderTest extends AbstractFirebirdWebDriverTest {

    @Inject
    private DailyJobService dailyJobService;
    @Inject
    private DataSetBuilder builder;

    private DataSet dataSet;
    private AnnualRegistration dueDateWithinSecondWindow;
    private AnnualRegistration dueDateOutsideOfSecondWindow;
    private AnnualRegistration renewDateWithinFirstWindow;
    private AnnualRegistration renewDateOutsideOfFirstWindow;
    private FirebirdUser investigator;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        investigator = builder.createInvestigatorWithCompleteProfile().asCtepUser().get();
        builder.createCoordinator().asCtepUser().withApprovedMangedInvestigator(investigator);
        createTestRegistrations();
        dataSet = builder.build();
    }

    private void createTestRegistrations() {
        dueDateWithinSecondWindow = createRegistration(addDays(new Date(), 30), null);
        dueDateOutsideOfSecondWindow = createRegistration(addDays(new Date(), 30 + 1), null);
        renewDateWithinFirstWindow = createRegistration(null, addDays(new Date(), 60));
        renewDateOutsideOfFirstWindow = createRegistration(null, addDays(new Date(), 60 + 1));
    }

    private AnnualRegistration createRegistration(Date dueDate, Date renewalDate) {
        AnnualRegistration registration = builder.createAnnualRegistration(investigator).get();
        registration.setDueDate(dueDate);
        registration.setRenewalDate(renewalDate);
        registration.setStatus(RegistrationStatus.NOT_STARTED);
        registration.setProfile(investigator.getInvestigatorRole().getProfile());
        registration.setAnnualRegistrationType(AnnualRegistrationType.RENEWAL);
        return registration;
    }

    @Test
    public void testRegistrationRenewalReminder() {
        startTimerAndWait();
        reloadRegistrations();
        checkRegistrationsForSecondRenewalSent();
        checkRegistrationsForCreatedRenewalRegistration();
        checkForEmails();
        checkForCoordinatorTaskListItems();
        checkForInvestigatorTaskListItem();
    }

    private void startTimerAndWait() {
        dailyJobService.createTestTimer(new Date());
        WaitUtils.pause(1000);
    }

    private void reloadRegistrations() {
        dueDateWithinSecondWindow = dataSet.reloadObject(dueDateWithinSecondWindow);
        dueDateOutsideOfSecondWindow = dataSet.reloadObject(dueDateOutsideOfSecondWindow);
        renewDateWithinFirstWindow = dataSet.reloadObject(renewDateWithinFirstWindow);
        renewDateOutsideOfFirstWindow = dataSet.reloadObject(renewDateOutsideOfFirstWindow);
    }

    private void checkRegistrationsForSecondRenewalSent() {
        assertTrue(dueDateWithinSecondWindow.isSecondRenewalNotificationSent());
        assertFalse(dueDateOutsideOfSecondWindow.isSecondRenewalNotificationSent());
        assertFalse(renewDateWithinFirstWindow.isSecondRenewalNotificationSent());
        assertFalse(renewDateOutsideOfFirstWindow.isSecondRenewalNotificationSent());
    }

    private void checkRegistrationsForCreatedRenewalRegistration() {
        assertNull(dueDateWithinSecondWindow.getRenewal());
        assertNull(dueDateOutsideOfSecondWindow.getRenewal());
        assertNotNull(renewDateWithinFirstWindow.getRenewal());
        assertNull(renewDateOutsideOfFirstWindow.getRenewal());
    }

    private void checkForEmails() {
        // dueDateWithinSecondWindow - email to Coordinator and Investigator
        // renewDateWithinFirstWindow - email to Coordinator only
        getEmailChecker().assertEmailCount(3);
        String coordinatorEmail = dataSet.getCoordinator().getPerson().getEmail();
        getEmailChecker().getSentEmail(
                coordinatorEmail,
                getExpectedSubject(ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_COORDINATOR,
                        dueDateWithinSecondWindow));
        getEmailChecker().getSentEmail(investigator.getPerson().getEmail(),
                getExpectedSubject(ANNUAL_REGISTRATION_RENEWAL_THIRTY_DAY_NOTICE_EMAIL_TO_INVESTIGATOR));
        getEmailChecker().getSentEmail(coordinatorEmail,
                getExpectedSubject(ANNUAL_REGISTRATION_RENEWAL_SIXTY_DAY_NOTICE_EMAIL, renewDateWithinFirstWindow));
    }

    private void checkForCoordinatorTaskListItems() {
        HomePage homePage = openHomePage(dataSet.getCoordinatorLogin(), getCtepProvider());
        assertEquals(3, homePage.getTasks().size());
        homePage = checkForTaskListItem(homePage, dueDateWithinSecondWindow);
        homePage = checkForTaskListItem(homePage, dueDateOutsideOfSecondWindow);
        homePage = checkForTaskListItem(homePage, getCreatedRenewalRegistration());
    }

    private HomePage checkForTaskListItem(HomePage homePage, AnnualRegistration registration) {
        OverviewTab overviewTab = homePage.getHelper().openRegistrationRenewalTask(registration);
        return overviewTab.getPage().clickHome();
    }

    private AnnualRegistration getCreatedRenewalRegistration() {
        return dataSet.getLastCreatedObject(AnnualRegistration.class);
    }

    private void checkForInvestigatorTaskListItem() {
        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin(), getCtepProvider());
        assertEquals(1, homePage.getTasks().size());
        homePage = checkForTaskListItem(homePage, dueDateWithinSecondWindow);
    }

}
