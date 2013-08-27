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
package gov.nih.nci.firebird.test.data;

import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AdditionalAttachmentsForm;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.AnnualRegistrationType;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.LoginAccount.SponsorDelegateLogin;
import gov.nih.nci.firebird.test.LoginAccount.CoordinatorLogin;
import gov.nih.nci.firebird.test.LoginAccount.SponsorLogin;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.nes.TargetGridResources;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class AnnualRegistrationTestDataSet extends AnnualRegistrationConfigurationTestDataSet {

    private AnnualRegistrationDataComponent initialRegistrationData;
    private AnnualRegistrationDataComponent renewalRegistrationData;
    private CoordinatorUserDataComponent coordinatorData;

    private AnnualRegistrationTestDataSet(TestDataLoader dataLoader, TargetGridResources gridResources,
            LoginAccount sponsorLogin, LoginAccount coordinatorLogin, LoginAccount sponsorDelegateLogin) {
        super(dataLoader, gridResources, sponsorLogin, sponsorDelegateLogin);
        initialRegistrationData = new AnnualRegistrationDataComponent(this, getAnnualRegistrationConfigurationData(),
                getProfileData());
        renewalRegistrationData = new AnnualRegistrationDataComponent(this, getAnnualRegistrationConfigurationData(),
                getProfileData());
        coordinatorData = new CoordinatorUserDataComponent(this, gridResources, coordinatorLogin,
                getInvestigatorUserData());
    }

    @Override
    protected void configureData() throws Exception {
        super.configureData();
        Date now = new Date();
        Date initialRegistrationDate = DateUtils.addMonths(now, -11);
        Date renewalDueDate = DateUtils.addYears(initialRegistrationDate, 1);
        initialRegistrationData.setUp(RegistrationStatus.APPROVED, AnnualRegistrationType.INITIAL,
                initialRegistrationDate, null);
        renewalRegistrationData.setUp(RegistrationStatus.IN_PROGRESS, AnnualRegistrationType.RENEWAL, now,
                renewalDueDate);
        getCoordinatorData().configureAsCtepUser();
        getCoordinatorData().approveAllRegistrationCoordinatorRoles();
    }

    AnnualRegistrationDataComponent getInitialRegistrationData() {
        return initialRegistrationData;
    }

    public AnnualRegistration getInitialRegistration() {
        return getInitialRegistrationData().getRegistration();
    }

    AnnualRegistrationDataComponent getRenewalRegistrationData() {
        return renewalRegistrationData;
    }

    public AnnualRegistration getRenewalRegistration() {
        return getRenewalRegistrationData().getRegistration();
    }

    CoordinatorUserDataComponent getCoordinatorData() {
        return coordinatorData;
    }

    public LoginAccount getRegistrationCoordinatorLogin() {
        return getCoordinatorData().getLogin();
    }

    public FirebirdUser getRegistrationCoordinatorUser() {
        return getCoordinatorData().getUser();
    }

    public ManagedInvestigator getManagedInvestigator() {
        return getRegistrationCoordinatorUser().getRegistrationCoordinatorRole().getManagedInvestigators().iterator()
                .next();
    }

    public static AnnualRegistrationTestDataSet create(TestDataLoader dataLoader, TargetGridResources gridResources) {
        AnnualRegistrationTestDataSet dataSet = new AnnualRegistrationTestDataSet(dataLoader, gridResources,
                SponsorLogin.fbcisponsor1, CoordinatorLogin.fbcicoord1, SponsorDelegateLogin.fbcidel1);
        dataSet.load();
        return dataSet;
    }

    public static AnnualRegistrationTestDataSet createWithDefaultFormOptionalitiesAndCompleteProfile(
            TestDataLoader dataLoader, TargetGridResources gridResources) {
        AnnualRegistrationTestDataSet dataSet = new AnnualRegistrationTestDataSet(dataLoader, gridResources,
                SponsorLogin.fbcisponsor1, CoordinatorLogin.fbcicoord1, SponsorDelegateLogin.fbcidel1) {
            @Override
            protected void configureData() throws Exception {
                getProfileData().setUpCompleteProfile();
                getAnnualRegistrationConfigurationData().setUpWithDefaultFormOptionalities();
                super.configureData();
            }
        };
        dataSet.load();
        return dataSet;
    }

    public static AnnualRegistrationTestDataSet createReadyForSubmission(TestDataLoader dataLoader,
            TargetGridResources gridResources) {
        AnnualRegistrationTestDataSet dataSet = new AnnualRegistrationTestDataSet(dataLoader, gridResources,
                SponsorLogin.fbcisponsor1, CoordinatorLogin.fbcicoord1, SponsorDelegateLogin.fbcidel1) {
            @Override
            protected void configureData() throws Exception {
                getAnnualRegistrationConfigurationData().setUpWithDefaultFormOptionalities();
                super.configureData();
                getProfileData().setUpCompleteProfile();
                getAnnualRegistrationConfigurationData().setUpWithDefaultFormOptionalities();
                getRenewalRegistrationData().setUpCompleteRegistration();
            }
        };
        dataSet.load();
        return dataSet;
    }

    public static AnnualRegistrationTestDataSet createWithStatus(TestDataLoader dataLoader,
            TargetGridResources gridResources, final RegistrationStatus status) {
        AnnualRegistrationTestDataSet dataSet = new AnnualRegistrationTestDataSet(dataLoader, gridResources,
                SponsorLogin.fbcisponsor1, CoordinatorLogin.fbcicoord1, SponsorDelegateLogin.fbcidel1) {
            protected void configureData() throws Exception {
                getAnnualRegistrationConfigurationData().setUpWithDefaultFormOptionalities();
                super.configureData();
                getProfileData().setUpCompleteProfile();
                getRenewalRegistrationData().setUpCompleteRegistration();
                getRenewalRegistration().setStatus(status);
                if (status == RegistrationStatus.SUBMITTED) {
                    addPdfFilesToForms();
                    addAdditionalDocumentsToForms();
                    addEmailsToNotificationList();
                }
            }

            private void addPdfFilesToForms() {
                for (AbstractRegistrationForm form : getRenewalRegistration().getForms()) {
                    form.submitForm();
                    if (form.isReviewRequired()) {
                        form.setFlattenedPdf(FirebirdFileFactory.getInstance().create());
                        form.setSignedPdf(FirebirdFileFactory.getInstance().create());
                    }
                }
            }

            private void addAdditionalDocumentsToForms() {
                AdditionalAttachmentsForm additionalAttachmentsForm = getRenewalRegistration().getAdditionalAttachmentsForm();
                additionalAttachmentsForm.getAdditionalAttachments().add(FirebirdFileFactory.getInstance().create());
                additionalAttachmentsForm.getAdditionalAttachments().add(FirebirdFileFactory.getInstance().create());
                getRenewalRegistration().getFinancialDisclosure().getSupportingDocumentation().add(FirebirdFileFactory.getInstance().create());
            }

            private void addEmailsToNotificationList() {
                getRenewalRegistration().getNotificationEmailAddresses().add(ValueGenerator.getUniqueEmailAddress());
                getRenewalRegistration().getNotificationEmailAddresses().add(ValueGenerator.getUniqueEmailAddress());
            }
        };
        dataSet.load();
        return dataSet;
    }

}
